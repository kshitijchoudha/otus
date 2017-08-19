package com.techexpo.springboot.model;

public class ServiceInformation {
	ServiceDetails application;

	public ServiceDetails getApplication() {
		return application;
	}

	public void setApplication(ServiceDetails application) {
		this.application = application;
	}

	@Override
	public String toString() {
		return "ServiceInformation [application=" + application + "]";
	}
	
}
