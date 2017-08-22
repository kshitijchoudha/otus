package com.otus.springboot.servicea.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceARestController {
	
    @Autowired
    ServiceDClient serviceDClient;
    
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
		return "[A][" + serviceDClient.hello() + "]";
	}
	
	@FeignClient("service-d")
	 interface ServiceDClient {
	  @RequestMapping(value = "/hello", method = RequestMethod.GET)
	  String hello();
	}
}
