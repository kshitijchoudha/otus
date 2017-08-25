package com.techexpo.springboot.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.techexpo.springboot.application.Application;
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
	private static Map<String, String> dependencyMapWithEureka = null;
	
	static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("INTERNET_OTUS-UI", "");
        aMap.put("OTUS-UI_SERVICE-A", "");
        aMap.put("OTUS-UI_SERVICE-B", "");
        aMap.put("OTUS-UI_SERVICE-C", "");
        aMap.put("OTUS-UI_SERVICE-AGGREGATE", "");
        aMap.put("SERVICE-A_SERVICE-D", "");
        aMap.put("SERVICE-D_SERVICE-H", "");
        aMap.put("SERVICE-B_SERVICE-E", "");
        aMap.put("SERVICE-B_SERVICE-F", "");
        aMap.put("SERVICE-F_SERVICE-G", "");
        aMap.put("SERVICE-F_SERVICE-I", "");
        dependencyMap = Collections.unmodifiableMap(aMap);

        aMap.put("OTUS-UI_EUREKA", "");
        aMap.put("SERVICE-A_EUREKA", "");
        aMap.put("SERVICE-B_EUREKA", "");
        aMap.put("SERVICE-C_EUREKA", "");
        aMap.put("SERVICE-AGGREGATE_EUREKA", "");
        aMap.put("SERVICE-D_EUREKA", "");
        aMap.put("SERVICE-E_EUREKA", "");
        aMap.put("SERVICE-F_EUREKA", "");
        aMap.put("SERVICE-G_EUREKA", "");
        aMap.put("SERVICE-H_EUREKA", "");
        aMap.put("SERVICE-I_EUREKA", "");
        dependencyMapWithEureka = Collections.unmodifiableMap(aMap);
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
	
	// create predefined connections
	private static List<ServiceConnection> getPredefinedConnections() {
		List<ServiceConnection> serviceConnections = new ArrayList<ServiceConnection>();
		Map<String, String> map =  getMap();
		
		for (String key : map.keySet()) {
			String[] srcdst = key.split("_");
			String src = srcdst[0];
			String dst = srcdst[1];
			
			ServiceConnection serviceConnection = new ServiceConnection();
			serviceConnection.setClassName("normal");
			serviceConnection.setSource(src);
			serviceConnection.setTarget(dst);
			AggregateMetrics metrics = new AggregateMetrics();
			metrics.setDanger(0);
			metrics.setNormal(0);
			serviceConnection.setMetrics(metrics);
			serviceConnection.setNotices(new ArrayList<AggregateServiceNotice>());
			serviceConnections.add(serviceConnection);
		}
		
		return serviceConnections;
	}
	
	private static Map<String, ServiceConnection> getFlowMap(List<ServiceConnection> connections) {
		Map<String, ServiceConnection> flowmap = new HashMap<String, ServiceConnection>();
		
		for (ServiceConnection con : connections) {
			String key = con.getSource() + "_" + con.getTarget();
			flowmap.put(key, con);
		}
		
		return flowmap;
	}
	
	private static List<ServiceConnection> getConnections(List<ServiceDetails> serviceInfos, List<VPCFlowLogResponse> vpcLogResponse) {
		List<ServiceConnection> serviceConnections = getPredefinedConnections();
		Map<String, ServiceConnection> flowLogMap = getFlowMap(serviceConnections);
		
		if(null != vpcLogResponse && vpcLogResponse.size() > 0) {
			System.out.println("====size===" + vpcLogResponse.size());
		}
		
		Map<String, String> map =  getMap();
		
		for (VPCFlowLogResponse vpcLog :vpcLogResponse) {
			String sourceIp = vpcLog.getSourceAddress();
			String destIp = vpcLog.getDestinationAddress();
			String status = vpcLog.getAction();
			
			String sourceAppName = getServiceName(sourceIp, serviceInfos);
			String destAppName= getServiceName(destIp, serviceInfos);
			String sourceStatus = getServiceStatus(sourceIp,serviceInfos);
			String destStatus = getServiceStatus(destIp,serviceInfos);

			
			System.out.println("sourceAppName:" + sourceAppName + ":: Source Status:" + sourceStatus);
			System.out.println("destAppName:" + destAppName + ":: destination Status:" + destStatus);

			String key = sourceAppName + "_" + destAppName;

			if (map.containsKey(key)) {
				System.out.println("Key Found:" + key);
				ServiceConnection serviceConnection = flowLogMap.get(key);
				boolean srcDown = sourceStatus.equalsIgnoreCase("DOWN");
				boolean dstDown = destStatus.equalsIgnoreCase("DOWN");
				if (srcDown || dstDown) {
					int dangerCount = serviceConnection.getMetrics().getDanger();
					serviceConnection.getMetrics().setDanger(dangerCount + vpcLog.getPackets());
					serviceConnection.getMetrics().setNormal(0);
					List<AggregateServiceNotice> notices = serviceConnection.getNotices();
					if (notices.isEmpty()) {
						AggregateServiceNotice aNotice = new AggregateServiceNotice();
						aNotice.setTitle(srcDown ? sourceAppName + " is DOWN" : destAppName + " is DOWN");
						aNotice.setSeverity(getSeverity(serviceConnection.getMetrics().getDanger()));
						notices.add(aNotice);
					}
				} else {
					if (status.equalsIgnoreCase("REJECT")) {
						int rejectCount = serviceConnection.getMetrics().getDanger() + vpcLog.getPackets();
						serviceConnection.getMetrics().setDanger(rejectCount);
					} else {
						int successCount = serviceConnection.getMetrics().getNormal() + vpcLog.getPackets();
						serviceConnection.getMetrics().setNormal(successCount);
					}
				}
			} else {
				System.out.println("KEY Not found:" + key);
			}

		}
		
		// not using factor for now.
//		Iterator<String> keySetIterator = flowLogMap.keySet().iterator(); 
//		while(keySetIterator.hasNext()) { 
//			String key = keySetIterator.next(); 
//			System.out.println("key: " + key + " value: " + flowLogMap.get(key)); 
//			//RANDOM_COUNT - Bumping Normal and Danger count
//			int failedCnt = flowLogMap.get(key).getMetrics().getDanger();
//			failedCnt = failedCnt * Integer.parseInt(Application.FAILED_RANDOM_COUNT);
//			flowLogMap.get(key).getMetrics().setDanger(failedCnt);
//			
//			int successCnt = flowLogMap.get(key).getMetrics().getNormal();
//			successCnt = successCnt * Integer.parseInt(Application.SUCCESS_RANDOM_COUNT);
//			flowLogMap.get(key).getMetrics().setNormal(successCnt);
//			
//			
//			serviceConnections.add(flowLogMap.get(key));
//		}
		return serviceConnections;
	}
	
	private static int getSeverity(int danger) {
		if (danger < 100) {
			return 1;
		} else {
			return 2;
		}
	}
	
	private static Map<String, String> getMap() {
		if (Application.EUREKA_FLAG.equalsIgnoreCase("true")) {
			System.out.println("Map Name:" + dependencyMapWithEureka);
			return dependencyMapWithEureka;
		}
		System.out.println("Map Name:" + dependencyMap);
		return dependencyMap;
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
	
	private static String getServiceStatus(String ipAddress, List<ServiceDetails> serviceInfos ) {
		String serviceStatus = new String("UP");
		for(ServiceDetails serviceDetail : serviceInfos) {			
			if (serviceDetail.getInstance().get(0).getIpAddr().equalsIgnoreCase(ipAddress)) {
				if(serviceDetail.getInstance().get(0).getApp().equalsIgnoreCase("EUREKA")) {
					return "UP";
				}
				return serviceDetail.getInstance().get(0).getStatus();
			}
		}
		
		return serviceStatus;
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
