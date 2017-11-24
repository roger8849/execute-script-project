package com.javeriana.executescript.client.service.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javeriana.executescript.client.dto.Address;
import com.javeriana.executescript.client.util.ObjectConverter;
import com.javeriana.executescript.client.util.ServerProperties;
import com.javeriana.executescript.dto.Message;
import com.javeriana.executescript.enumeration.MessageType;

@Service
public class DefaultClientService {
  public static final Logger LOG = LoggerFactory.getLogger(DefaultClientService.class);

  @Autowired
  private ServerProperties serverProperties;

  public void executeClient() throws IOException {
    List<Address> addresses =
        ObjectConverter.fromPropertiesAddressToAddressList(serverProperties.getInetAddress());
    Address lowestBusyServer = this.askForLowestBusyServer(addresses);
    if (null == lowestBusyServer) {
      throw new UnknownHostException("Server information is empty");
    }
    Message scriptMessage =
        ObjectConverter.getMessageScriptToSend(serverProperties.getResourceScriptPath());

    Socket clientSocket = null;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;

    try {
      clientSocket = new Socket(lowestBusyServer.getInetAddress(), lowestBusyServer.getPort());
      oos = new ObjectOutputStream(clientSocket.getOutputStream());
      ois = new ObjectInputStream(clientSocket.getInputStream());
      LOG.debug("Message script to send {}", scriptMessage);
      oos.writeObject(scriptMessage);
      LOG.debug("Receiving script messaging");
      Message scriptMessageReceived = (Message) ois.readObject();
      while (scriptMessageReceived != null
          && !scriptMessageReceived.getResponse().contains("Timer")) {
        LOG.debug("Server execution response: {}", scriptMessageReceived.getResponse());
        scriptMessageReceived = (Message) ois.readObject();
        // ois.close();
        // ois = new ObjectInputStream(clientSocket.getInputStream());
      }

    } catch (Exception e) {
    } finally {

    }

  }

  private Address askForLowestBusyServer(List<Address> addresses)
      throws UnknownHostException, IOException {
    Address lowestBusyServer = null;
    long memoryUsage = Long.MAX_VALUE;
    Message outputMessage = new Message();
    outputMessage.setMessageType(MessageType.ASKING);
    Socket clientSocket = null;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    for (Address address : addresses) {
      Message inputMessage = null;
      try {
        clientSocket = new Socket(address.getInetAddress(), address.getPort());
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ois = new ObjectInputStream(clientSocket.getInputStream());
        LOG.debug("asking for server information: {}  {}", address, outputMessage);
        oos.writeObject(outputMessage);
        LOG.debug("Getting response from server.");
        inputMessage = (Message) ois.readObject();
        LOG.debug("Message got it {}", inputMessage);
        if (inputMessage != null && inputMessage.getFreeMemory() < memoryUsage) {
          lowestBusyServer = address;
        }
      } catch (ClassNotFoundException e) {
        LOG.error("Response from server is not clear {}", inputMessage);
      } finally {
        if (null != clientSocket) {
          clientSocket.close();
        }
      }
    }

    return lowestBusyServer;
  }
}
