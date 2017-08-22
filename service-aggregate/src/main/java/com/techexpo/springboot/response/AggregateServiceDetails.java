package com.techexpo.springboot.response;

public class AggregateServiceDetails {

	private String instanceId;
	private String ipAddress;
	private String port;
	private String status;
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ServiceDetails [instanceId=" + instanceId + ", ipAddress="
				+ ipAddress + ", port=" + port + ", status=" + status + "]";
	}
	
	
}
