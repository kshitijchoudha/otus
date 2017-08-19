package com.techexpo.springboot.mapper.dto;

public class FMServiceInstance {
	private String instanceId;
	private String hostName;
	private String app;
	private String ipAddr;
	private String port ;
	private String status;
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
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
		return "FMServiceInstance [instanceId=" + instanceId + ", hostName="
				+ hostName + ", app=" + app + ", ipAddr=" + ipAddr + ", port="
				+ port + ", status=" + status + "]";
	}
	
	

}
