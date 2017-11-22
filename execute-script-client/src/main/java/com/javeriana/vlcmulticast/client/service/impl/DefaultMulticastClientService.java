package com.javeriana.vlcmulticast.client.service.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javeriana.vlcmulticast.client.dto.MulticastMessage;
import com.javeriana.vlcmulticast.client.enumeration.MessageType;
import com.javeriana.vlcmulticast.client.service.MulticastClientService;
import com.javeriana.vlcmulticast.client.util.MulticastProperties;
import com.javeriana.vlcmulticast.client.util.ObjectConverter;
import com.javeriana.vlcmulticast.client.util.VlcProperties;

@Service
public class DefaultMulticastClientService implements MulticastClientService {
  public static final Logger LOG = LoggerFactory.getLogger(DefaultMulticastClientService.class);

  @Autowired
  private MulticastProperties multicastProperties;
  @Autowired
  private VlcProperties vlcProperties;

  /*
   * (non-Javadoc)
   *
   * @see com.javeriana.vlcmulticast.client.service.impl.MulticastClientService#listenForRequests()
   */
  @Override
  public void listenForRequests() throws IOException {
    MulticastMessage multicastMessage = null;
    InetAddress multicastAddress = InetAddress.getByName(multicastProperties.getInetAddress());
    Integer port = Integer.parseInt(multicastProperties.getInetPort());
    byte[] buffer = new byte[1024];
    MulticastSocket multicastSocket = null;
    try {
      do {
        try {
          multicastSocket = new MulticastSocket(port);
          multicastSocket.joinGroup(multicastAddress);

          LOG.debug("Listening for params.");
          DatagramPacket msgPacket = new DatagramPacket(buffer, buffer.length);
          multicastSocket.receive(msgPacket);
          multicastMessage = ObjectConverter.fromByteDataToMessage(msgPacket.getData());
          LOG.debug("Message listened: {}",
              ObjectConverter.fromObjectToJsonString(multicastMessage));
          if (multicastMessage != null && MessageType.ASKING.toString()
              .equalsIgnoreCase(multicastMessage.getMessageType().toString())) {
            this.sendVlcConfiguration(multicastAddress, port);
          }
        } catch (Exception e) {
          LOG.error("Error getting listening for a message.", e);
        }
      } while (true);
    } finally {
      if (multicastSocket != null)
        multicastSocket.close();
    }

  }

  private void sendVlcConfiguration(InetAddress multicastAddress, Integer port)
      throws SocketException, IOException {
    MulticastMessage message = new MulticastMessage(UUID.randomUUID(),
        MessageType.PARAMETERS_OF_START, vlcProperties.getCommand());
    LOG.debug("Init message send succesfuly sent: {}", message);
    DatagramSocket serverSocket = new DatagramSocket();
    try {

      byte[] dataMessage = ObjectConverter.fromMessageToByteData(message);
      DatagramPacket msgPacket =
          new DatagramPacket(dataMessage, dataMessage.length, multicastAddress, port);
      serverSocket.send(msgPacket);
      LOG.debug("Message succesfuly sent: {}", message);
    } catch (IOException e) {
      LOG.debug(e.getMessage());
      throw e;

    } finally {
      serverSocket.close();
    }
  }

}
