package com.javeriana.vlcmulticast.server.service;

import com.javeriana.vlcmulticast.server.dto.MulticastMessage;

public interface MulticastService {

  MulticastMessage askAndReceiveVlcConfiguration() throws Exception;

}
