package com.otus.springboot.servicea.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
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
	
	@FeignClient(name = "service-d", fallback = ServiceDClientFallback.class)
	public interface ServiceDClient {
	  @RequestMapping(value = "/hello", method = RequestMethod.GET)
	  public String hello();
	}
	
	@Component
	public class ServiceDClientFallback implements ServiceDClient {
		public String hello() {
			return "[d]";
		}
	}
}
