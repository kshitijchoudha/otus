package com.techexpo.springboot.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceNode {

	private String renderer;
	private String name;
	private String maxVolume;
	
	@JsonProperty("class")
	private String className;
	
	private List<ServiceNode> nodes;
	
	@JsonProperty("service-details")
	private AggregateServiceDetails serviceDetails;
	
	private MetaData metadata;
	
	private List<AggregateCluster> clusters;
	
	private List<ServiceConnection> connections;
	
	public String getRenderer() {
		return renderer;
	}
	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMaxVolume() {
		return maxVolume;
	}
	public void setMaxVolume(String maxVolume) {
		this.maxVolume = maxVolume;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<ServiceNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<ServiceNode> nodes) {
		this.nodes = nodes;
	}
	public AggregateServiceDetails getServiceDetails() {
		return serviceDetails;
	}
	public void setServiceDetails(AggregateServiceDetails serviceDetails) {
		this.serviceDetails = serviceDetails;
	}
	public MetaData getMetadata() {
		return metadata;
	}
	public void setMetadata(MetaData metadata) {
		this.metadata = metadata;
	}
	public List<AggregateCluster> getClusters() {
		return clusters;
	}
	public void setClusters(List<AggregateCluster> clusters) {
		this.clusters = clusters;
	}
	
	public List<ServiceConnection> getConnections() {
		return connections;
	}
	public void setConnections(List<ServiceConnection> connections) {
		this.connections = connections;
	}
	@Override
	public String toString() {
		return "ServiceNode [renderer=" + renderer + ", name=" + name
				+ ", maxVolume=" + maxVolume + ", className=" + className
				+ ", nodes=" + nodes + ", serviceDetails=" + serviceDetails
				+ ", metadata=" + metadata + ", clusters=" + clusters
				+ ", connections=" + connections + "]";
	}
	
}
