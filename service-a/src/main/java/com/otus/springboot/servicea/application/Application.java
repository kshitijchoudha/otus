package com.otus.springboot.servicea.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan ({"com.otus.springboot.servicea"})
@EnableDiscoveryClient
@EnableAutoConfiguration
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		LOGGER.info("Starting OTUS Servicer-A Spring Boot Application .XXX................................");
		SpringApplication.run(Application.class, args);
	}
}
