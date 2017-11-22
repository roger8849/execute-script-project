package com.javeriana.executescript.client.util;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties("server")
public class ServerProperties {

  private List<String> inetAddress;

  public List<String> getInetAddress() {
    return inetAddress;
  }

  public void setInetAddress(List<String> inetAddress) {
    this.inetAddress = inetAddress;
  }

}
