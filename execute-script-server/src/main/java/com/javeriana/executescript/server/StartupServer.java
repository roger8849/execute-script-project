package com.javeriana.executescript.server;

import static java.lang.System.exit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.javeriana.executescript.server.service.impl.DefaultServerService;

@SpringBootApplication
public class StartupServer implements CommandLineRunner {

  public static final Logger LOG = LoggerFactory.getLogger(StartupServer.class);

  @Autowired
  private DefaultServerService defaultServerService;

  public static void main(String[] args) throws Exception {

    SpringApplication app = new SpringApplication(StartupServer.class);
    app.run(args);

  }

  @Override
  public void run(String... args) throws Exception {
    this.defaultServerService.runServer();
    exit(0);
  }
}
