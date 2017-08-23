package com.techexpo.springboot.response;

public class AggregateMetrics {
	
	private int normal;
	private int danger;
	
	
	
	public int getNormal() {
		return normal;
	}



	public void setNormal(int normal) {
		this.normal = normal;
	}



	public int getDanger() {
		return danger;
	}



	public void setDanger(int danger) {
		this.danger = danger;
	}



	@Override
	public String toString() {
		return "Metrics [normal=" + normal + ", danger=" + danger + "]";
	}
	
	
}
