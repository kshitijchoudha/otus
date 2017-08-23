package com.techexpo.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.techexpo.springboot.application.Application;
import com.techexpo.springboot.model.Instance;
import com.techexpo.springboot.model.Port;
import com.techexpo.springboot.model.Response;
import com.techexpo.springboot.model.ServiceDetails;
import com.techexpo.springboot.model.ServiceInformation;
import com.techexpo.springboot.model.VPCFlowLogResponse;
import com.techexpo.springboot.response.AggregateResponse;
import com.techexpo.springboot.util.AggregateDataUtil;
import com.techexpo.springboot.util.AmazonS3ClientUtil;

@RestController
public class ServiceAggregateRestController {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAggregateRestController.class);

	
	@Autowired
    private DiscoveryClient discoveryClient;
	
	private static final String URL = "http://34.193.236.46:8761/eureka/apps/";
	
	@RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }
	
	@RequestMapping(value="/service-deactivate/{applicationName}", method = RequestMethod.GET)
    public boolean unregister(@PathVariable String applicationName) {
		ServiceInformation serviceInformation  = getServiceInformation(applicationName);
		String appName = serviceInformation.getApplication().getName();
		String instanceId = serviceInformation.getApplication().getInstance().get(0).getInstanceId();
		LOGGER.info("====INSTATNCE ID ====" + instanceId);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity entity = new HttpEntity(headers);
//		String UNREGISTER_URL = URL + applicationName + "/Kavya.attlocal.net:fm-be:8082/status?value=OUT_OF_SERVICE";
//		String UNREGISTER_URL = URL + applicationName + "/" +  instanceId + "/status?value=OUT_OF_SERVICE";
		String UNREGISTER_URL = URL + applicationName + "/" +  instanceId + "/status?value=DOWN";
		ResponseEntity<String> deregisterResp
		  = new RestTemplate().exchange(UNREGISTER_URL,HttpMethod.PUT, entity, String.class);
		LOGGER.info("Deactivate Service from Eureka ends..." + applicationName);
		return true;
	}
	

	@RequestMapping(value="/service-activate/{applicationName}", method = RequestMethod.GET)
    public boolean activateServicer(@PathVariable String applicationName) {
		LOGGER.info("Activate Service from Eureka starts..." + applicationName);
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
		LOGGER.info("Activate Service from Eureka ends..." + applicationName);
		return true;
	}

	@RequestMapping(value="/data1/", method = RequestMethod.GET)
    public List<String> getAllInstances() {
		LOGGER.info("GetAll method.......");
		List<String> instances =  this.discoveryClient.getServices();
		LOGGER.info("instances method......." + instances);
		List<ServiceInformation> serviceInfos = new ArrayList<ServiceInformation>();
		for (String jsonString : instances) {
			serviceInfos.add(getServiceInformation(jsonString));
		}
		
		ServiceInformation eurekeInfo = new ServiceInformation();
		ServiceDetails eurekaDetails = new ServiceDetails();
		List<Instance> eurekainstances = new ArrayList<Instance>();
		Instance eurekainstance = new Instance();
		eurekainstance.setApp("Eureka");
		eurekainstance.setIpAddr("34.193.236.46");
		Port port = new Port();
		port.setDollar("8761");
		eurekainstance.setPort(port);
		eurekainstance.setInstanceId("-1");
		eurekainstances.add(eurekainstance);
		eurekaDetails.setInstance(eurekainstances);
		eurekeInfo.setApplication(eurekaDetails);		
		serviceInfos.add(eurekeInfo);
		
		ServiceInformation internetInfo = new ServiceInformation();
		ServiceDetails internetDetails = new ServiceDetails();		
		List<Instance> internetInstances = new ArrayList<Instance>();
		Instance interInstance = new Instance();
		
		interInstance.setApp("INTERNET");
		interInstance.setIpAddr("");
		Port internetport = new Port();
		internetport.setDollar("1111");
		interInstance.setPort(internetport);
		interInstance.setInstanceId("-1");
		internetInstances.add(interInstance);
		internetDetails.setInstance(internetInstances);
		internetInfo.setApplication(internetDetails);		
		serviceInfos.add(internetInfo);		
		
		for(ServiceInformation serviceInfo: serviceInfos) {
			System.out.println("serviceInfo..." + serviceInfo.toString());
		}

		//get S3 data
		AmazonS3ClientUtil s3Client = new AmazonS3ClientUtil();
        return instances;
    }
	
	@RequestMapping(value="/data/", method = RequestMethod.GET)
    public AggregateResponse getAll() throws IOException {
		LOGGER.info("GetAll method.......");
		HttpHeaders headers = new HttpHeaders();
		HttpEntity entity = new HttpEntity(headers);
		String REGISTER_URL = URL;
		String jsonString = new RestTemplate().getForObject(URL, String.class);
		
		Response resp = getServiceInfo(jsonString);

		List<ServiceDetails> serviceDetails = resp.getApplications().getApplication();
		
		//ServiceInformation eurekeInfo = new ServiceInformation();
		ServiceDetails eurekaDetails = new ServiceDetails();
		List<Instance> eurekainstances = new ArrayList<Instance>();
		Instance eurekainstance = new Instance();
		eurekainstance.setApp("Eureka");
		eurekainstance.setIpAddr("34.193.236.46");
		Port port = new Port();
		port.setDollar("8761");
		eurekainstance.setPort(port);
		eurekainstance.setInstanceId("-1");
		
		eurekainstances.add(eurekainstance);
		eurekaDetails.setInstance(eurekainstances);
		serviceDetails.add(eurekaDetails);
		
//		eurekeInfo.setApplication(eurekaDetails);		
//		serviceDetails.add(eurekeInfo);
//		
		//ServiceInformation internetInfo = new ServiceInformation();
		ServiceDetails internetDetails = new ServiceDetails();		
		List<Instance> internetInstances = new ArrayList<Instance>();
		Instance interInstance = new Instance();
		
		interInstance.setApp("INTERNET");
		interInstance.setIpAddr("");
		Port internetport = new Port();
		internetport.setDollar("1111");
		interInstance.setPort(internetport);
		interInstance.setInstanceId("-1");
		internetInstances.add(interInstance);
		internetDetails.setInstance(internetInstances);
		serviceDetails.add(internetDetails);
//		internetInfo.setApplication(internetDetails);		
//		serviceInfos.add(internetInfo);		
//		
		for(ServiceDetails serviceInfo: serviceDetails) {
			System.out.println("serviceInfo..." + serviceInfo.toString());
		}


		
		
		//AggregateResponse response = AggregateDataUtil.createDummyDate(serviceDetails);

		//get S3 data
		AmazonS3ClientUtil s3Client = new AmazonS3ClientUtil();
		String accessKey = Application.ACCESS_KEY;
		String secretAccessKey = Application.SECRET_ACCESS_KEY;
		List<VPCFlowLogResponse> vpcLogResponse = s3Client.readObjectFromS3(accessKey, secretAccessKey);

		AggregateResponse response = AggregateDataUtil.createData(serviceDetails, vpcLogResponse);

		return response;
    }
	
	@RequestMapping(value="/data-dummy/", method = RequestMethod.GET)
    public AggregateResponse getDummyData() {
		LOGGER.info("getDummyData method.......");
//		List<String> instances =  this.discoveryClient.getServices();
//		LOGGER.info("instances method......." + instances);
		List<ServiceInformation> serviceInfos = new ArrayList<ServiceInformation>();		HttpHeaders headers = new HttpHeaders();
		HttpEntity entity = new HttpEntity(headers);
		String REGISTER_URL = URL;
		String jsonString = new RestTemplate().getForObject(URL, String.class);
		Response resp = getServiceInfo(jsonString);
		List<ServiceDetails> serviceDetails = resp.getApplications().getApplication();
		AggregateResponse response = AggregateDataUtil.createDummyDate(serviceDetails);
        return response;
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
	
	private Response getServiceInfo(String jsonString) {
		System.out.println("[getServiceInfo] JSON String:" + jsonString);
		Response serviceInformation = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			serviceInformation = mapper.readValue(jsonString, Response.class);
			System.out.println(serviceInformation.toString());
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
