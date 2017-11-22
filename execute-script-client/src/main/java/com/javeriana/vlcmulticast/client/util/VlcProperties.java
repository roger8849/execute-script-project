package com.javeriana.vlcmulticast.client.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties("vlc")
public class VlcProperties {

  private String inetAddress;
  private Integer inetPort;
  private String command;

  public Integer getInetPort() {
    return inetPort;
  }

  public void setInetPort(Integer inetPort) {
    this.inetPort = inetPort;
  }

  public String getInetAddress() {
    return inetAddress;
  }

  public void setInetAddress(String inetAddress) {
    this.inetAddress = inetAddress;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

}
