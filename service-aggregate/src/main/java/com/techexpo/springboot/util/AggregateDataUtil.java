package com.techexpo.springboot.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.techexpo.springboot.model.Instance;
import com.techexpo.springboot.model.ServiceDetails;
import com.techexpo.springboot.model.VPCFlowLogResponse;
import com.techexpo.springboot.response.AggregateCluster;
import com.techexpo.springboot.response.AggregateMetrics;
import com.techexpo.springboot.response.AggregateResponse;
import com.techexpo.springboot.response.AggregateServiceDetails;
import com.techexpo.springboot.response.AggregateServiceNotice;
import com.techexpo.springboot.response.MetaData;
import com.techexpo.springboot.response.ServiceConnection;
import com.techexpo.springboot.response.ServiceNode;

public class AggregateDataUtil {
	
	private static Map<String, String> dependencyMap = null;
	
	static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("INTERNET-OTUS-UI", "");
        aMap.put("OTUS-UI-SERVICE-A", "");
        aMap.put("OTUS-UI-SERVICE-B", "");
        aMap.put("OTUS-UI-SERVICE-C", "");
        aMap.put("OTUS-UI-SERVICE-AGGREGATE", "");
        aMap.put("SERVICE-A-SERVICE-D", "");
        aMap.put("SERVICE-D-SERVICE-H", "");
        aMap.put("SERVICE-B-SERVICE-E", "");
        aMap.put("SERVICE-B-SERVICE-F", "");
        aMap.put("SERVICE-F-SERVICE-G", "");
        aMap.put("SERVICE-F-SERVICE-I", "");
        dependencyMap = Collections.unmodifiableMap(aMap);
    }

	public static AggregateResponse createDummyDate(List<ServiceDetails>  serviceInfos) {
		AggregateResponse response = new AggregateResponse();
		//setting Connections
		response.setConnections(getConnections());
		//setting Nodes
		response.setNodes(getNodes(serviceInfos));
		return response;
	}
	
	
	public static AggregateResponse createData(List<ServiceDetails> serviceInfos, List<VPCFlowLogResponse> vpcLogResponse) {
		AggregateResponse response = new AggregateResponse();
		response.setName("edge");
		response.setRenderer("global");
		//setting Connections
		response.setConnections(getConnections());
		//setting Nodes
		response.setNodes(getNodes(serviceInfos, vpcLogResponse));
		return response;
	}
	
	//Nodes
	private static List<ServiceNode> getNodes(List<ServiceDetails> serviceInfos, List<VPCFlowLogResponse> vpcLogResponse) {
		List<ServiceNode> nodes = new ArrayList<ServiceNode>();
		nodes.add(getInternetNode());
		nodes.add(populateRegionNode(serviceInfos, vpcLogResponse));	
		return nodes;
	}
	
	private static ServiceNode populateRegionNode(List<ServiceDetails> serviceInfos, List<VPCFlowLogResponse> vpcLogResponse) {
		List<ServiceNode> nodes = new ArrayList<ServiceNode>();
		ServiceNode node = new ServiceNode();
		node.setRenderer("region");
		node.setMaxVolume(50000);
		node.setClusters(new ArrayList<AggregateCluster>());
		node.setName("us-east-2");
		node.setClassName("normal");
		//updated - missing per json
		for(ServiceDetails serviceInformation: serviceInfos) {
			nodes.add(getAppplicationNode(serviceInformation));
		}
		//setting all nodes
		node.setNodes(nodes);
		
		//setting Connections
		node.setConnections(getConnections(serviceInfos, vpcLogResponse));
		return node;
	}
	
	private static List<ServiceConnection> getConnections(List<ServiceDetails> serviceInfos, List<VPCFlowLogResponse> vpcLogResponse) {
		List<ServiceConnection> serviceConnections = new ArrayList<ServiceConnection>();
		Map<String, ServiceConnection> flowLogMap = new HashMap<String, ServiceConnection>();
		
		System.out.println("====size===" + vpcLogResponse.size());
		
		for (VPCFlowLogResponse vpcLog :vpcLogResponse) {
			String sourceIp = vpcLog.getSourceAddress();
			String destIp = vpcLog.getDestinationAddress();
			String status = vpcLog.getAction();
			
			String sourceAppName = getServiceName(sourceIp, serviceInfos);
			String destAppName= getServiceName(destIp, serviceInfos);
			System.out.println("sourceAppName:" + sourceAppName);
			System.out.println("destAppName:" + destAppName);
//			
			String key = sourceAppName + "-" + destAppName;
//			String key = sourceIp + "-" + destIp;
			
//			if(sourceAppName.equalsIgnoreCase("INTERNET") && destAppName.equalsIgnoreCase("INTERNET")) {
//			} else {
			if(dependencyMap.containsKey(key)) {
				System.out.println("Key Found:" + key);
				if (flowLogMap.keySet().contains(key)) {
					ServiceConnection serviceConnection = flowLogMap.get(key);
					if(status.equalsIgnoreCase("REJECT")) {
						int rejectCount = serviceConnection.getMetrics().getDanger() + 1;
						serviceConnection.getMetrics().setDanger(rejectCount);
					} else {
						int successCount  = serviceConnection.getMetrics().getNormal() + 1;
						serviceConnection.getMetrics().setNormal(successCount);
					}
					flowLogMap.put(key, serviceConnection);
					
				} else {
					ServiceConnection serviceConnection = new ServiceConnection();
					serviceConnection.setClassName("normal");
					serviceConnection.setSource(sourceAppName);
					serviceConnection.setTarget(destAppName);
					AggregateMetrics metrics = new AggregateMetrics();
					if(status.equalsIgnoreCase("REJECT")) {
						metrics.setDanger(1);
						metrics.setNormal(0);
					} else {
						metrics.setNormal(1);
						metrics.setDanger(0);
	
					}
					serviceConnection.setMetrics(metrics);
					serviceConnection.setNotices(new ArrayList<AggregateServiceNotice>());
					flowLogMap.put(key, serviceConnection);
				}
			} else {
				System.out.println("KEY Not found:" + key);
			}
			
		}
		
		Iterator<String> keySetIterator = flowLogMap.keySet().iterator(); 
		while(keySetIterator.hasNext()) { 
			String key = keySetIterator.next(); 
			System.out.println("key: " + key + " value: " + flowLogMap.get(key)); 
			
			serviceConnections.add(flowLogMap.get(key));
		}
		return serviceConnections;
	}
	
	private static String getServiceName(String ipAddress, List<ServiceDetails> serviceInfos ) {
		String serviceName = new String("INTERNET");
		for(ServiceDetails serviceDetail : serviceInfos) {
			if (serviceDetail.getInstance().get(0).getIpAddr().equalsIgnoreCase(ipAddress)) {
				return serviceDetail.getInstance().get(0).getApp();
			}
		}
		return serviceName;
	}
	
	//Dummy Data Nodes
	private static List<ServiceNode> getNodes(List<ServiceDetails> serviceInfos) {
		List<ServiceNode> nodes = new ArrayList<ServiceNode>();
		nodes.add(getInternetNode());
		for(ServiceDetails serviceInformation: serviceInfos) {
			nodes.add(getAppplicationNode(serviceInformation));
		}
		return nodes;
	}
	
	private static ServiceNode getAppplicationNode(ServiceDetails serviceInformation) {
		ServiceNode node = new ServiceNode();
		Instance instance = serviceInformation.getInstance().get(0);
		node.setName(instance.getApp());
		AggregateServiceDetails details = new AggregateServiceDetails();
		details.setInstanceId(instance.getInstanceId());
		details.setIpAddress(instance.getIpAddr());
		details.setPort(""+instance.getPort().getDollar());
		details.setStatus(instance.getStatus());
		node.setServiceDetails(details);
		
		MetaData metaData = new MetaData();
		metaData.setStreaming(1);
		node.setMetadata(metaData);
		
		node.setClusters(new ArrayList<AggregateCluster>());
		
		node.setRenderer("focusedChild");		
		node.setClassName("normal");
		node.setMaxVolume(100);
		node.setConnections(new ArrayList<ServiceConnection>());
		return node;
	}
	
	private static ServiceNode getInternetNode() {
		ServiceNode node = new ServiceNode();
		node.setRenderer("region");
		node.setName("INTERNET");
		node.setClassName("normal");
		return node;
	}
	
	//Connections
	private static List<ServiceConnection> getConnections() {
		List<ServiceConnection> serviceConnections = new ArrayList<ServiceConnection>();
		ServiceConnection serviceConnection = new ServiceConnection();
		serviceConnection.setClassName("normal");
		serviceConnection.setSource("INTERNET");
		serviceConnection.setTarget("us-east-2");
		
		AggregateMetrics metrics = new AggregateMetrics();
		metrics.setNormal(26037);
		metrics.setDanger(92);
		serviceConnection.setMetrics(metrics);
		
		serviceConnection.setNotices(new ArrayList<AggregateServiceNotice>());
		serviceConnections.add(serviceConnection);
		
		return serviceConnections;
		
	}
}
