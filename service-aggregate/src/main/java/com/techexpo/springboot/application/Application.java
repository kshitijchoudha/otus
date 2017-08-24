/**
 * 
 */
package com.techexpo.springboot.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Madhu
 *
 */
@SpringBootApplication
@ComponentScan ({"com.techexpo"})
@EnableDiscoveryClient
@EnableAutoConfiguration
public class Application {


    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    public static String ACCESS_KEY;
    public static String SECRET_ACCESS_KEY;
    public static String SUCCESS_RANDOM_COUNT;
    public static String FAILED_RANDOM_COUNT;
    public static String EUREKA_FLAG;

    
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		if(null != args && args.length >= 5)  {
			ACCESS_KEY = args[0];
			SECRET_ACCESS_KEY = args[1];
			SUCCESS_RANDOM_COUNT = args[2];
			FAILED_RANDOM_COUNT = args[3];
			EUREKA_FLAG = args[4];
		} else {
			System.err.print("Pass Access Key and Secret Access Key");
			System.exit(0);
		}
		LOGGER.info("Starting OTUS Service-Aggregate Spring Boot Application ..................................");
		SpringApplication.run(Application.class, args);
	}

}
