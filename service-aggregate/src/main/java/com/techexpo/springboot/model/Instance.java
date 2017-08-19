package com.techexpo.springboot.model;

public class Instance {
	private String instanceId;
	private String hostName;
	private String app;
	private String ipAddr;
	private String status;
	private Port port; 
	private Port securePort; 
	private String countryId;
	private DataCenterInfo dataCenterInfo;
	private LeaseInfo leaseInfo;
	private Metadata metadata ;
	
	private String homePageUrl;
	private String statusPageUrl;
	private String healthCheckUrl;
	private String vipAddress;
	private String secureVipAddress;
	private String isCoordinatingDiscoveryServer;
	private String lastUpdatedTimestamp;
	private String lastDirtyTimestamp;
	private String actionType;	
	private String overriddenstatus;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Port getPort() {
		return port;
	}
	public void setPort(Port port) {
		this.port = port;
	}
	public Port getSecurePort() {
		return securePort;
	}
	public void setSecurePort(Port securePort) {
		this.securePort = securePort;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public DataCenterInfo getDataCenterInfo() {
		return dataCenterInfo;
	}
	public void setDataCenterInfo(DataCenterInfo dataCenterInfo) {
		this.dataCenterInfo = dataCenterInfo;
	}
	public LeaseInfo getLeaseInfo() {
		return leaseInfo;
	}
	public void setLeaseInfo(LeaseInfo leaseInfo) {
		this.leaseInfo = leaseInfo;
	}
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	public String getHomePageUrl() {
		return homePageUrl;
	}
	public void setHomePageUrl(String homePageUrl) {
		this.homePageUrl = homePageUrl;
	}
	public String getStatusPageUrl() {
		return statusPageUrl;
	}
	public void setStatusPageUrl(String statusPageUrl) {
		this.statusPageUrl = statusPageUrl;
	}
	public String getHealthCheckUrl() {
		return healthCheckUrl;
	}
	public void setHealthCheckUrl(String healthCheckUrl) {
		this.healthCheckUrl = healthCheckUrl;
	}
	public String getVipAddress() {
		return vipAddress;
	}
	public void setVipAddress(String vipAddress) {
		this.vipAddress = vipAddress;
	}
	public String getSecureVipAddress() {
		return secureVipAddress;
	}
	public void setSecureVipAddress(String secureVipAddress) {
		this.secureVipAddress = secureVipAddress;
	}
	public String getIsCoordinatingDiscoveryServer() {
		return isCoordinatingDiscoveryServer;
	}
	public void setIsCoordinatingDiscoveryServer(
			String isCoordinatingDiscoveryServer) {
		this.isCoordinatingDiscoveryServer = isCoordinatingDiscoveryServer;
	}
	public String getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}
	public void setLastUpdatedTimestamp(String lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}
	public String getLastDirtyTimestamp() {
		return lastDirtyTimestamp;
	}
	public void setLastDirtyTimestamp(String lastDirtyTimestamp) {
		this.lastDirtyTimestamp = lastDirtyTimestamp;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getOverriddenstatus() {
		return overriddenstatus;
	}
	public void setOverriddenstatus(String overriddenstatus) {
		this.overriddenstatus = overriddenstatus;
	}
	@Override
	public String toString() {
		return "Instance [instanceId=" + instanceId + ", hostName=" + hostName
				+ ", app=" + app + ", ipAddr=" + ipAddr + ", status=" + status
				+ ", port=" + port + ", securePort=" + securePort
				+ ", countryId=" + countryId + ", dataCenterInfo="
				+ dataCenterInfo + ", leaseInfo=" + leaseInfo + ", metadata="
				+ metadata + ", homePageUrl=" + homePageUrl
				+ ", statusPageUrl=" + statusPageUrl + ", healthCheckUrl="
				+ healthCheckUrl + ", vipAddress=" + vipAddress
				+ ", secureVipAddress=" + secureVipAddress
				+ ", isCoordinatingDiscoveryServer="
				+ isCoordinatingDiscoveryServer + ", lastUpdatedTimestamp="
				+ lastUpdatedTimestamp + ", lastDirtyTimestamp="
				+ lastDirtyTimestamp + ", actionType=" + actionType
				+ ", overriddenstatus=" + overriddenstatus + "]";
	}
	
}
