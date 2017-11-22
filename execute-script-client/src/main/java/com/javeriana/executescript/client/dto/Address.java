package com.javeriana.executescript.client.dto;

public class Address {
  private String inetAddress;

  private Integer port;

  public Address() {
    super();
  }

  public Address(String inetAddress, Integer port) {
    super();
    this.inetAddress = inetAddress;
    this.port = port;
  }

  public String getInetAddress() {
    return inetAddress;
  }

  public void setInetAddress(String inetAddress) {
    this.inetAddress = inetAddress;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  @Override
  public String toString() {
    return new StringBuilder("Inet address: ").append(this.inetAddress).append(" Port: ")
        .append(this.port).toString();
  }

}
