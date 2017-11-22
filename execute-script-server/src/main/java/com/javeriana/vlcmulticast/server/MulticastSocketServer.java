package com.javeriana.vlcmulticast.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastSocketServer {

  final static String INET_ADDR = "239.255.255.250";
  final static int PORT = 34567;

  public static void main(String[] args) throws UnknownHostException, InterruptedException {
    // Get the address that we are going to connect to.
    InetAddress addr = InetAddress.getByName(INET_ADDR);

    // Open a new DatagramSocket, which will be used to send the data.

    try (DatagramSocket serverSocket = new DatagramSocket()) {
      for (int i = 0; i < 5; i++) {
        String msg = "Sent message no " + i;

        // Create a packet that will contain the data
        // (in the form of bytes) and send it.
        DatagramPacket msgPacket =
            new DatagramPacket(msg.getBytes(), msg.getBytes().length, addr, PORT);
        serverSocket.send(msgPacket);

        System.out.println("Roger local Server sent packet with msg: " + msg);
        Thread.sleep(500);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
