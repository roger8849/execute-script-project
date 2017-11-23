package com.javeriana.executescript.client;

import static java.lang.System.exit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.javeriana.executescript.client.service.impl.DefaultClientService;

@SpringBootApplication
public class StartupClient implements CommandLineRunner {

  public static final Logger LOG = LoggerFactory.getLogger(StartupClient.class);

  @Autowired
  private DefaultClientService clientService;

  public static void main(String[] args) throws Exception {

    SpringApplication app = new SpringApplication(StartupClient.class);
    app.run(args);

  }

  @Override
  public void run(String... args) throws Exception {
    this.clientService.executeClient();
    exit(0);
  }
}
