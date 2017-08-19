package com.otus.springboot.servicea.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceARestController {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceARestController.class);
	
    static {
    	System.out.println("Static block");
    }
    
	@RequestMapping(value="/getEmployee/", method = RequestMethod.GET)
    public List<String> getName() {
		List<String> names = new ArrayList<String>();
		LOGGER.info("getting employee details");
		names.add("Madhu");
		return names;
	}
	
//	@LoadBalanced
//    @Bean
//    RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
}
