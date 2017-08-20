package com.otus.springboot.serviceb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ServicerBController {
	
	   private static final Logger LOGGER = LoggerFactory.getLogger(ServicerBController.class);
	    
	    private static final String SERVICE_C_URL="http://localhost:9083/greeting";
	    private static final String SERVICE_D_URL="http://localhost:9084/greeting";
	    
	    
	    private static final String[] SERVICE_URLS={SERVICE_C_URL, SERVICE_D_URL};
	    
	    @Autowired
	    RestTemplate restTemplate;
	 
	
	@RequestMapping(value="/getDetails/", method = RequestMethod.GET)
    public List<String> greeting() {
		List<String> names = new ArrayList<String>();
		LOGGER.info("getting employee details");
		names.add("OTUS-B --> ");
		
		for(String url: SERVICE_URLS) {		
			String cname = getFromService(url);
			if(!StringUtils.isEmpty(cname)) {
				names.add(cname);
			}
		}
		return names;
    }
	
	private String getFromService(String url) {
		String str = null;
		LOGGER.info("Getting response from URL: " + url);
		try {
			return restTemplate.getForObject(url, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
