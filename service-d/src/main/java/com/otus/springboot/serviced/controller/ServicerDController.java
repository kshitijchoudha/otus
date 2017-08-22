package com.otus.springboot.serviced.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicerDController {
	
	@Autowired
	ServiceHClient serviceHClient;
	
	@RequestMapping(value="/hello", method = RequestMethod.GET)
    public String greeting() {
        return "[D][" + serviceHClient.hello() + "]";
    }

	@FeignClient(name = "service-h", fallback = ServiceHClientFallback.class)
	 interface ServiceHClient {
	  @RequestMapping(value = "/hello", method = RequestMethod.GET)
	  String hello();
	}
	
	@Component
	public class ServiceHClientFallback implements ServiceHClient {
		public String hello() {
			return "[h]";
		}
	}

}
