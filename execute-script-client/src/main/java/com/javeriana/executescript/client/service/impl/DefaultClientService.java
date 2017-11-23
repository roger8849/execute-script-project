package com.javeriana.executescript.client.service.impl;

import java.io.IOException;
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

  public Address getMemoryServersInformation() throws UnknownHostException, IOException {
    List<Address> addresses =
        ObjectConverter.fromPropertiesAddressToAddressList(serverProperties.getInetAddress());
    this.sendMessageToServices(addresses);
    return null;
  }

  private Message sendMessageToServices(List<Address> addresses)
      throws UnknownHostException, IOException {
    Message outputMessage = new Message();
    outputMessage.setMessageType(MessageType.ASKING);
    Socket clientSocket = null;
    ObjectOutputStream oos = null;
    for (Address address : addresses) {
      try {
        clientSocket = new Socket(address.getInetAddress(), address.getPort());
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.writeObject(outputMessage);
      } finally {
        if (null != clientSocket) {
          clientSocket.close();
        }
      }
    }

    return null;
  }
}
