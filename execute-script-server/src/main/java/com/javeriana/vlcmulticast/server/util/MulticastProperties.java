package com.javeriana.vlcmulticast.server.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties("multicast")
public class MulticastProperties {
  private String inetAddress;
  private Integer inetPort;
  private Integer timeout;

  public String getInetAddress() {
    return inetAddress;
  }

  public void setInetAddress(String inetAddress) {
    this.inetAddress = inetAddress;
  }

  public Integer getInetPort() {
    return inetPort;
  }

  public void setInetPort(Integer inetPort) {
    this.inetPort = inetPort;
  }

  public Integer getTimeout() {
    return timeout;
  }

  public void setTimeout(Integer timeout) {
    this.timeout = timeout;
  }

}
