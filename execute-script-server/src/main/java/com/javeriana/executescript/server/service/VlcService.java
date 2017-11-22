package com.javeriana.executescript.server.service;

import java.io.IOException;

import com.javeriana.executescript.server.dto.MulticastMessage;

public interface VlcService {

  void runVlcCommand(MulticastMessage multicastMessage) throws IOException;

}
