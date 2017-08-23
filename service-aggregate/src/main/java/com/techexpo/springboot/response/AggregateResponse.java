package com.techexpo.springboot.response;

import java.util.List;

public class AggregateResponse {
	private String renderer;
	private String name;
	
	private List<ServiceNode> nodes;
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
	public List<ServiceNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<ServiceNode> nodes) {
		this.nodes = nodes;
	}
	public List<ServiceConnection> getConnections() {
		return connections;
	}
	public void setConnections(List<ServiceConnection> connections) {
		this.connections = connections;
	}
	@Override
	public String toString() {
		return "AggregateResponse [renderer=" + renderer + ", name=" + name
				+ ", nodes=" + nodes + ", connections=" + connections + "]";
	}
	
	

}
