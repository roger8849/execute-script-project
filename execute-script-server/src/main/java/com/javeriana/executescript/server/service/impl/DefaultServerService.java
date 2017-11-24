package com.javeriana.executescript.server.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javeriana.executescript.dto.Message;
import com.javeriana.executescript.enumeration.MessageType;
import com.javeriana.executescript.server.util.ServerProperties;

@Service
public class DefaultServerService {
  public static final Logger LOG = LoggerFactory.getLogger(DefaultServerService.class);

  @Autowired
  private ServerProperties serverProperties;

  public void runServer() throws IOException {
    ServerSocket ss = new ServerSocket(serverProperties.getInetPort());
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    Socket socket = null;
    boolean keepAlive = true;
    try {
      do {
        try {
          socket = ss.accept();
          ois = new ObjectInputStream(socket.getInputStream());
          oos = new ObjectOutputStream(socket.getOutputStream());
          Message message = (Message) ois.readObject();
          LOG.debug("Message received: {}", message);
          Message outputMessage = processInMessage(message, oos);
          LOG.debug("Message to send: {}", outputMessage);
          oos.writeObject(outputMessage);
        } catch (ClassNotFoundException e) {
          LOG.error("Message is not a well known object type. ", e);
        } catch (Exception e) {
          keepAlive = false;
          LOG.error("Critical error in the application.", e);
        }
      } while (keepAlive);

    } finally {
      ss.close();
      if (null != socket) {
        socket.close();
      }
    }

  }

  private Message processInMessage(Message message, ObjectOutputStream oos) throws IOException {
    Message responseMessage = new Message();
    if (MessageType.ASKING.equals(message.getMessageType())) {
      responseMessage.setMessageType(MessageType.RESPONSE);
      responseMessage.setFreeMemory(Runtime.getRuntime().freeMemory());
    } else if (MessageType.EXECUTE_SCRIPT.equals(message.getMessageType())) {
      this.createFileOnDisk(message);
      this.executeScript(oos);
    } else {
      throw new IOException("MessageType not recognized.");
    }
    return responseMessage;
  }

  private void createFileOnDisk(Message inputMessage) throws IOException {
    try {
      File file = new File("script.jar");
      FileUtils.writeByteArrayToFile(file, inputMessage.getScriptToExecute());
      file.createNewFile();
    } catch (IOException e) {
      LOG.error("Error processing the file byte array script received {}", e);
      throw e;
    }
  }

  private void executeScript(ObjectOutputStream oos) throws IOException {
    Message message = new Message();
    message.setMessageType(MessageType.SCRIPT_RESPONSE);

    String command = "java -jar script.jar 99999999";
    Process p = Runtime.getRuntime().exec(command);

    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String s;
    while ((s = reader.readLine()) != null) {
      LOG.debug("Executing command line {}", s);
      message.setResponse(s);
      oos.writeObject(message);
      oos.flush();
      oos.reset();
    }
  }

}
