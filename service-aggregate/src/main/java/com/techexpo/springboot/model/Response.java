package com.techexpo.springboot.model;

public class Response {
	private ServiceInstanceResponse applications;

	public ServiceInstanceResponse getApplications() {
		return applications;
	}

	public void setApplications(ServiceInstanceResponse applications) {
		this.applications = applications;
	}

	@Override
	public String toString() {
		return "Response [applications=" + applications + "]";
	}
	
}
