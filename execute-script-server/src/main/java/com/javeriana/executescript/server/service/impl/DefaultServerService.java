package com.javeriana.executescript.server.service.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
    do {
      try {
        socket = ss.accept();
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
        Message message = (Message) ois.readObject();
        LOG.debug("Message received: {}", message);
        Message outputMessage = processInMessage(message);
        LOG.debug("Message to send: {}", outputMessage);
        oos.writeObject(outputMessage);
      } catch (ClassNotFoundException e) {
        LOG.error("Message is not a well known object type. ", e);
      } catch (Exception e) {
        keepAlive = false;
        LOG.error("Critical error in the application.", e);
      }
    } while (keepAlive);

  }

  private Message processInMessage(Message message) throws IOException {
    Message responseMessage = new Message();
    if (MessageType.ASKING.equals(message.getMessageType())) {
      responseMessage.setMessageType(MessageType.RESPONSE);
      responseMessage.setFreeMemory(Runtime.getRuntime().freeMemory());
    } else if (MessageType.EXECUTE_SCRIPT.equals(message.getMessageType())) {
      // TODO execute script
      responseMessage.setMessageType(MessageType.FINAL_RESPONSE);

    } else {
      throw new IOException("MessageType not recognized.");
    }
    return responseMessage;
  }

}
