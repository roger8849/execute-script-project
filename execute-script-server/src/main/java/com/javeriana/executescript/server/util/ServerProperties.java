package com.javeriana.executescript.server.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties("server")
public class ServerProperties {
  private Integer inetPort;

  public Integer getInetPort() {
    return inetPort;
  }

  public void setInetPort(Integer inetPort) {
    this.inetPort = inetPort;
  }

}
