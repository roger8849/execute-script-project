package com.javeriana.executescript.client.dto;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import com.javeriana.executescript.client.enumeration.MessageType;
import com.javeriana.executescript.client.util.ObjectConverter;

public class Message implements Serializable {
  /** */
  private static final long serialVersionUID = 2348465134494597549L;

  private UUID messageId;
  private MessageType messageType;
  private Long freeMemory;
  private byte[] scriptToExecute;

  public Message() {
    super();
  }

  public Message(UUID messageId, MessageType messageType, Long freeMemory, byte[] scriptToExecute) {
    super();
    this.messageId = messageId;
    this.messageType = messageType;
    this.freeMemory = freeMemory;
    this.scriptToExecute = scriptToExecute;
  }

  public UUID getMessageId() {
    if (this.messageId == null) {
      this.messageId = UUID.randomUUID();
    }
    return messageId;
  }

  public void setMessageId(UUID messageId) {
    this.messageId = messageId;
  }

  public MessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(MessageType messageType) {
    this.messageType = messageType;
  }

  public Long getFreeMemory() {
    return freeMemory;
  }

  public void setFreeMemory(Long freeMemory) {
    this.freeMemory = freeMemory;
  }

  public byte[] getScriptToExecute() {
    return scriptToExecute;
  }

  public void setScriptToExecute(byte[] scriptToExecute) {
    this.scriptToExecute = scriptToExecute;
  }

  @Override
  public String toString() {
    try {
      return ObjectConverter.fromObjectToJsonString(this);
    } catch (IOException e) {
      return super.toString();
    }
  }

}
