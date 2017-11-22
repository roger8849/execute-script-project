package com.javeriana.executescript.server.service;

import java.io.IOException;

import com.javeriana.executescript.server.dto.Message;

public interface VlcService {

  void runVlcCommand(Message multicastMessage) throws IOException;

}
