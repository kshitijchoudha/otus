package com.otus.springboot.serviceb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicerBController {
	
	@Autowired
	ServiceEClient serviceEClient;
	 
	@Autowired
	ServiceFClient serviceFClient;
	
	@RequestMapping(value="/hello", method = RequestMethod.GET)
    public String greeting() {
		return "[B][" + serviceEClient.hello() + "][" + serviceFClient.hello() + "]";
    }
	
	@FeignClient(name = "service-e", fallback = ServiceEClientFallback.class)
	 interface ServiceEClient {
	  @RequestMapping(value = "/hello", method = RequestMethod.GET)
	  String hello();
	}

	@FeignClient(name = "service-f", fallback = ServiceFClientFallback.class)
	 interface ServiceFClient {
	  @RequestMapping(value = "/hello", method = RequestMethod.GET)
	  String hello();
	}
	
	@Component
	public class ServiceEClientFallback implements ServiceEClient {
		public String hello() {
			return "[e]";
		}
	}
	
	@Component
	public class ServiceFClientFallback implements ServiceFClient {
		public String hello() {
			return "[f]";
		}
	}
}
