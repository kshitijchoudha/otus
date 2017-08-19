package com.techexpo.springboot.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techexpo.springboot.model.ServiceInformation;

@RestController
public class ServiceAggregateRestController {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAggregateRestController.class);

	
	@Autowired
    private DiscoveryClient discoveryClient;
	
	private static final String URL = "http://localhost:8761/eureka/apps/";
	
	@RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }
	
	@RequestMapping(value="/service-deactivate/{applicationName}", method = RequestMethod.GET)
    public boolean unregister(@PathVariable String applicationName) {
		/*
		ServiceInformation serviceInformation = null;
		LOGGER.info("Deactivate Service from Eureka starts..." + applicationName);
		String GETSERVICER_URL = URL + applicationName;
		String jsonString = new RestTemplate().getForObject(GETSERVICER_URL, String.class);
		LOGGER.info("Instance information......" + jsonString);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			serviceInformation = mapper.readValue(jsonString, ServiceInformation.class);
			LOGGER.info("=====================OBEJCT:==================" + serviceInformation.toString());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

		ServiceInformation serviceInformation  = getServiceInformation(applicationName);
		
		String appName = serviceInformation.getApplication().getName();
		String instanceId = serviceInformation.getApplication().getInstance().get(0).getInstanceId();

		LOGGER.info("====INSTATNCE ID ====" + instanceId);
		
		HttpHeaders headers = new HttpHeaders();
		HttpEntity entity = new HttpEntity(headers);
//		String UNREGISTER_URL = URL + applicationName + "/Kavya.attlocal.net:fm-be:8082/status?value=OUT_OF_SERVICE";
		
		String UNREGISTER_URL = URL + applicationName + "/" +  instanceId + "/status?value=OUT_OF_SERVICE";

		ResponseEntity<String> deregisterResp
		  = new RestTemplate().exchange(UNREGISTER_URL,HttpMethod.PUT, entity, String.class);
		LOGGER.info("deregisterResp:");


		LOGGER.info("Deactivate Service from Eureka ends..." + applicationName);
		return true;
	}
	

	@RequestMapping(value="/service-activate/{applicationName}", method = RequestMethod.GET)
    public boolean activateServicer(@PathVariable String applicationName) {
		LOGGER.info("Activate Service from Eureka starts..." + applicationName);
/*
		String GETSERVICER_URL = URL + applicationName;
		String jsonString = new RestTemplate().getForObject(GETSERVICER_URL, String.class);
		LOGGER.info("Instance information......" + jsonString);
*/		
		ServiceInformation serviceInformation  = getServiceInformation(applicationName);
		
		String appName = serviceInformation.getApplication().getName();
		String instanceId = serviceInformation.getApplication().getInstance().get(0).getInstanceId();

		LOGGER.info("====INSTATNCE ID ====" + instanceId);

		
		HttpHeaders headers = new HttpHeaders();
		HttpEntity entity = new HttpEntity(headers);
//		String UNREGISTER_URL = URL + applicationName + "/Kavya.attlocal.net:fm-be:8082/status?value=UP";
		String REGISTER_URL = URL + appName + "/" + instanceId + "/status?value=UP";

		
		ResponseEntity<String> deregisterResp
		  = new RestTemplate().exchange(REGISTER_URL,HttpMethod.DELETE, entity, String.class);
		LOGGER.info("deregisterResp:" + deregisterResp);
		LOGGER.info("Activate Service from Eureka ends..." + applicationName);
		
		return true;
		
	}

	
	@RequestMapping(value="/instances/", method = RequestMethod.GET)
    public List<String> getAll() {
		LOGGER.info("GetAll method.......");
		List<String> instances =  this.discoveryClient.getServices();
		LOGGER.info("instances method......." + instances);
        return instances;
    }
	
	@LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
	private ServiceInformation getServiceInformation(String applicationName) {
		ServiceInformation serviceInformation = null;
		LOGGER.info("Deactivate Service from Eureka starts..." + applicationName);
		String GETSERVICER_URL = URL + applicationName;
		String jsonString = new RestTemplate().getForObject(GETSERVICER_URL, String.class);
		LOGGER.info("Instance information......" + jsonString);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			serviceInformation = mapper.readValue(jsonString, ServiceInformation.class);
			LOGGER.info("=====================OBEJCT:==================" + serviceInformation.toString());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serviceInformation;
	}
	
}
