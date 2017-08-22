package com.techexpo.springboot.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceConnection {

	private String source;
	private String target;
	private AggregateMetrics metrics;
	private List<AggregateServiceNotice> notices;
	
	@JsonProperty("class")
	private String className;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	

	public AggregateMetrics getMetrics() {
		return metrics;
	}

	public void setMetrics(AggregateMetrics metrics) {
		this.metrics = metrics;
	}

	public List<AggregateServiceNotice> getNotices() {
		return notices;
	}

	public void setNotices(List<AggregateServiceNotice> notices) {
		this.notices = notices;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "ServiceConnection [source=" + source + ", target=" + target
				+ ", metrics=" + metrics + ", notices=" + notices
				+ ", className=" + className + "]";
	}

	
	
	
}
