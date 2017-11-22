package com.javeriana.vlcmulticast.server.service.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javeriana.vlcmulticast.server.dto.MulticastMessage;
import com.javeriana.vlcmulticast.server.enumeration.MessageType;
import com.javeriana.vlcmulticast.server.service.MulticastService;
import com.javeriana.vlcmulticast.server.util.MulticastProperties;
import com.javeriana.vlcmulticast.server.util.ObjectConverter;

@Service
public class DefaultMulticastService implements MulticastService {
  public static final Logger LOG = LoggerFactory.getLogger(DefaultMulticastService.class);

  @Autowired
  private MulticastProperties multicastProperties;

  /*
   * (non-Javadoc)
   *
   * @see
   * com.javeriana.vlcmulticast.server.service.impl.MulticastService#askAndReceiveVlcConfiguration()
   */
  @Override
  public MulticastMessage askAndReceiveVlcConfiguration() throws Exception {
    InetAddress multicastAddress = InetAddress.getByName(multicastProperties.getInetAddress());
    Integer port = multicastProperties.getInetPort();
    Integer timeout = multicastProperties.getTimeout();

    return receiveVlcConfigurationResponse(multicastAddress, port, timeout);
  }

  private MulticastMessage receiveVlcConfigurationResponse(InetAddress multicastAddress,
      Integer port, Integer timeout) throws Exception {
    MulticastSocket clientSocket = new MulticastSocket(port);
    MulticastMessage configurationReceived = null;
    clientSocket.setSoTimeout(timeout);
    try {
      clientSocket.joinGroup(multicastAddress);
      DatagramPacket receivedPacket = null;
      boolean keepAsking = true;
      do {
        try {
          this.askForConfiguration(multicastAddress, port);
          LOG.debug("Waiting for response");
          synchronized (this) {
            this.wait(500);
          }
          byte[] buffer = new byte[1024];
          receivedPacket = new DatagramPacket(buffer, buffer.length);
          clientSocket.receive(receivedPacket);
          configurationReceived = ObjectConverter.fromByteDataToMessage(receivedPacket.getData());
          if (configurationReceived != null && MessageType.PARAMETERS_OF_START.toString()
              .equalsIgnoreCase(configurationReceived.getMessageType().toString())) {
            LOG.debug("Received configuration stop asking: {}", configurationReceived);
            keepAsking = false;
          } else {
            LOG.debug("No response got it, retrying in 5 seconds.");
            synchronized (this) {
              this.wait(5000);
            }
          }
        } catch (SocketTimeoutException e) {
          LOG.debug("No response got it, retrying in 5 seconds.");
          synchronized (this) {
            this.wait(5000);
          }
        } catch (Exception e) {
          LOG.error("Exception occurred asking for the configuration.", e);
        }
      } while (keepAsking);
    } finally {
      clientSocket.close();
    }
    return configurationReceived;
  }

  private void askForConfiguration(InetAddress multicastAddress, Integer port) throws Exception {
    MulticastMessage message = new MulticastMessage(UUID.randomUUID(), MessageType.ASKING, "");
    LOG.debug("Init message send succesfuly sent: {}", message);
    DatagramSocket serverSocket = new DatagramSocket();
    try {

      byte[] dataMessage = ObjectConverter.fromMessageToByteData(message);
      DatagramPacket msgPacket =
          new DatagramPacket(dataMessage, dataMessage.length, multicastAddress, port);
      serverSocket.send(msgPacket);
      LOG.debug("Message succesfuly sent: {}", message);
    } catch (Exception e) {
      LOG.debug(e.getMessage());
      throw e;

    } finally {
      serverSocket.close();
    }

  }

}
