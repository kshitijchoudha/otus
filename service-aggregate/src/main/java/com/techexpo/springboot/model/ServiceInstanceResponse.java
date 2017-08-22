package com.techexpo.springboot.model;

import java.util.List;

public class ServiceInstanceResponse {
	private String versions__delta;
	private String apps__hashcode;
	
	private List<ServiceDetails> application;

	public String getVersions__delta() {
		return versions__delta;
	}

	public void setVersions__delta(String versions__delta) {
		this.versions__delta = versions__delta;
	}

	public String getApps__hashcode() {
		return apps__hashcode;
	}

	public void setApps__hashcode(String apps__hashcode) {
		this.apps__hashcode = apps__hashcode;
	}

	public List<ServiceDetails> getApplication() {
		return application;
	}

	public void setApplication(List<ServiceDetails> application) {
		this.application = application;
	}

	@Override
	public String toString() {
		return "ServiceInstanceResponse [versions__delta=" + versions__delta
				+ ", apps__hashcode=" + apps__hashcode + ", application="
				+ application + "]";
	}
	
	
	
}
