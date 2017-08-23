package com.otus.springboot.servicea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
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
