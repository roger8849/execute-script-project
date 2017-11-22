package com.javeriana.executescript.server.service;

import com.javeriana.executescript.server.dto.Message;

public interface MulticastService {

  Message askAndReceiveVlcConfiguration() throws Exception;

}
