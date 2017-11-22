package com.javeriana.executescript.server.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.javeriana.executescript.server.dto.Message;
import com.javeriana.executescript.server.service.VlcService;

@Service
public class DefaultVlcService implements VlcService {

  public static final Logger LOG = LoggerFactory.getLogger(DefaultMulticastService.class);

  /*
   * (non-Javadoc)
   *
   * @see com.javeriana.vlcmulticast.server.service.impl.VlcService#runVlcCommand(com.javeriana.
   * vlcmulticast.server.dto.MulticastMessage)
   */
  @Override
  public void runVlcCommand(Message multicastMessage) throws IOException {
    LOG.debug("Executing command{}", multicastMessage.getVlcCommand());
    Runtime.getRuntime().exec(multicastMessage.getVlcCommand());
  }
}
