package com.javeriana.vlcmulticast;

import static java.lang.System.exit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Startup implements CommandLineRunner {
  public static void main(String[] args) throws Exception {

    // disabled banner, don't want to see the spring logo
    SpringApplication app = new SpringApplication(Startup.class);
    app.run(args);

  }

  // Put your logic here.
  @Override
  public void run(String... args) throws Exception {
    exit(0);
  }
}
