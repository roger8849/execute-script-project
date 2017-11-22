package com.javeriana.executescript.server.service;

import com.javeriana.executescript.server.dto.MulticastMessage;

public interface MulticastService {

  MulticastMessage askAndReceiveVlcConfiguration() throws Exception;

}
