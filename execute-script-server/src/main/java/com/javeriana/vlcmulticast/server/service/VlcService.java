package com.javeriana.vlcmulticast.server.service;

import java.io.IOException;

import com.javeriana.vlcmulticast.server.dto.MulticastMessage;

public interface VlcService {

  void runVlcCommand(MulticastMessage multicastMessage) throws IOException;

}
