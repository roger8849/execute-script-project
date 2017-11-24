package com.javeriana.executescript.client.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javeriana.executescript.client.dto.Address;
import com.javeriana.executescript.dto.Message;
import com.javeriana.executescript.enumeration.MessageType;

public class ObjectConverter {

  public static final Logger LOG = LoggerFactory.getLogger(ObjectConverter.class);

  public static byte[] fromMessageToByteData(Message message) throws IOException {
    String jsonObject = fromObjectToJsonString(message);
    byte[] data = jsonObject.getBytes();
    return data;
  }

  public static Message fromByteDataToMessage(byte[] data)
      throws IOException, ClassNotFoundException {
    String jsonString = new String(data);
    jsonString = jsonString.trim();
    return fromJsonStringToMulticastMessage(jsonString);
  }

  public static String fromObjectToJsonString(Object object)
      throws JsonGenerationException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(object);
  }

  public static Message fromJsonStringToMulticastMessage(String jsonObject) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(jsonObject, Message.class);
  }

  public static List<Address> fromPropertiesAddressToAddressList(List<String> urls) {
    List<Address> addresses = new ArrayList<>();
    for (String url : urls) {
      String[] urlPair = url.split(":");
      addresses.add(new Address(urlPair[0], Integer.parseInt(urlPair[1])));
    }
    return addresses;
  }

  public static Message getMessageScriptToSend(String filePath) throws IOException {
    Message message = new Message(UUID.randomUUID(), MessageType.EXECUTE_SCRIPT, null, null, null);
    File file = new File(filePath);
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    byte[] fileByteArray = new byte[(int) file.length()];
    try {
      fis = new FileInputStream(file);
      bis = new BufferedInputStream(fis);
      bis.read(fileByteArray, 0, fileByteArray.length);
    } catch (FileNotFoundException e) {
      LOG.error("Error transforming the file {}", filePath, e);
      throw e;
    } finally {
      if (null != fis) {
        fis.close();
      }
      if (null != bis) {
        bis.close();
      }
    }
    message.setScriptToExecute(fileByteArray);
    return message;
  }

  private ObjectConverter() {}

}
