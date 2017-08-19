package com.techexpo.springboot.model;

import java.util.List;

public class ServiceDetails {
	private String name;
	private List<Instance> instance;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Instance> getInstance() {
		return instance;
	}
	public void setInstance(List<Instance> instance) {
		this.instance = instance;
	}
	@Override
	public String toString() {
		return "ServiceDetails [name=" + name + ", instance=" + instance + "]";
	}
	
	
}
