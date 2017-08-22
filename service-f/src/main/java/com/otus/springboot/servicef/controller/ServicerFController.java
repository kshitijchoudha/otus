package com.otus.springboot.servicef.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicerFController {
	
	@Autowired
	ServiceGClient serviceGClient;

	@Autowired
	ServiceIClient serviceIClient;
	
	@RequestMapping(value="/hello", method=RequestMethod.GET)
    public String greeting() {
        return "[F][" + serviceGClient.hello() + "][" + serviceIClient.hello() + "]";
    }

	@FeignClient("service-g")
	 interface ServiceGClient {
	  @RequestMapping(value = "/hello", method = RequestMethod.GET)
	  String hello();
	}

	@FeignClient("service-i")
	 interface ServiceIClient {
	  @RequestMapping(value = "/hello", method = RequestMethod.GET)
	  String hello();
	}
}
