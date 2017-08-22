package com.techexpo.springboot.response;

public class AggregateMetrics {
	
	private String normal;
	private String danger;
	public String getNormal() {
		return normal;
	}
	public void setNormal(String normal) {
		this.normal = normal;
	}
	public String getDanger() {
		return danger;
	}
	public void setDanger(String danger) {
		this.danger = danger;
	}
	@Override
	public String toString() {
		return "Metrics [normal=" + normal + ", danger=" + danger + "]";
	}
	
	
}
