package com.javeriana.vlcmulticast.client.service;

import java.io.IOException;

public interface MulticastClientService {

  void listenForRequests() throws IOException;

}
