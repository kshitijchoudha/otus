package com.techexpo.springboot.model;

public class VPCFlowLogResponse {

	private String version;
	private String accointId;
	private String interfaceId;
	private String sourceAddress;
	private String destinationAddress;
	private String sourcePort;
	private String destinationPort;
	private String protocol;
	private String packets;
	private String bytes;
	private String startTime;
	private String endTime;
	private String action;
	private String logStatus;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAccointId() {
		return accointId;
	}
	public void setAccointId(String accointId) {
		this.accointId = accointId;
	}
	public String getInterfaceId() {
		return interfaceId;
	}
	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}
	public String getSourceAddress() {
		return sourceAddress;
	}
	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}
	public String getDestinationAddress() {
		return destinationAddress;
	}
	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
	public String getSourcePort() {
		return sourcePort;
	}
	public void setSourcePort(String sourcePort) {
		this.sourcePort = sourcePort;
	}
	public String getDestinationPort() {
		return destinationPort;
	}
	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getPackets() {
		return packets;
	}
	public void setPackets(String packets) {
		this.packets = packets;
	}
	public String getBytes() {
		return bytes;
	}
	public void setBytes(String bytes) {
		this.bytes = bytes;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getLogStatus() {
		return logStatus;
	}
	public void setLogStatus(String logStatus) {
		this.logStatus = logStatus;
	}
	@Override
	public String toString() {
		return "VPCFlowLogResponse [version=" + version + ", accointId="
				+ accointId + ", interfaceId=" + interfaceId
				+ ", sourceAddress=" + sourceAddress + ", destinationAddress="
				+ destinationAddress + ", sourcePort=" + sourcePort
				+ ", destinationPort=" + destinationPort + ", protocol="
				+ protocol + ", packets=" + packets + ", bytes=" + bytes
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", action=" + action + ", logStatus=" + logStatus + "]";
	}
	
	

}
