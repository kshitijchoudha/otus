package com.techexpo.springboot.util;

import java.util.ArrayList;
import java.util.List;

import com.techexpo.springboot.model.ServiceDetails;
import com.techexpo.springboot.model.ServiceInformation;
import com.techexpo.springboot.response.AggregateCluster;
import com.techexpo.springboot.response.AggregateMetrics;
import com.techexpo.springboot.response.AggregateResponse;
import com.techexpo.springboot.response.AggregateServiceDetails;
import com.techexpo.springboot.response.AggregateServiceNotice;
import com.techexpo.springboot.response.MetaData;
import com.techexpo.springboot.response.ServiceConnection;
import com.techexpo.springboot.response.ServiceNode;
import com.techexpo.springboot.model.Instance;

public class AggregateDataUtil {

	public static AggregateResponse createDummyDate(List<ServiceDetails>  serviceInfos) {
		AggregateResponse response = new AggregateResponse();
		//setting Connections
		response.setConnections(getConnections());
		//setting Nodes
		response.setNodes(getNodes(serviceInfos));
		return response;
	}
	
	
	//Nodes
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
		metaData.setStreaming("1");
		node.setMetadata(metaData);
		
		node.setClusters(new ArrayList<AggregateCluster>());
		
		node.setRenderer("focusedChild");		
		node.setClassName("normal");
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
		serviceConnection.setTarget("us-west-2");
		
		AggregateMetrics metrics = new AggregateMetrics();
		metrics.setNormal("26037.626");
		metrics.setDanger("92.37");
		serviceConnection.setMetrics(metrics);
		
		serviceConnection.setNotices(new ArrayList<AggregateServiceNotice>());
		serviceConnections.add(serviceConnection);
		
		return serviceConnections;
		
	}
}
