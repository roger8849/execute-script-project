package com.javeriana.executescript.client.service.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javeriana.executescript.client.dto.Address;
import com.javeriana.executescript.client.dto.Message;
import com.javeriana.executescript.client.util.ObjectConverter;
import com.javeriana.executescript.client.util.ServerProperties;

@Service
public class DefaultClientService {
  public static final Logger LOG = LoggerFactory.getLogger(DefaultClientService.class);

  @Autowired
  private ServerProperties serverProperties;

  public Address getMemoryServerInformation() {
    List<Address> address =
        ObjectConverter.fromPropertiesAddressToAddressList(serverProperties.getInetAddress());
    return null;
  }

  private Message sendMessage(Address address) throws UnknownHostException, IOException {
    Socket clientSocket = new Socket(address.getInetAddress(), address.getPort());
    return null;
  }
}
