package com.techexpo.springboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataCenterInfo {
	
	@JsonProperty("@class")
	private String className;
	
	@JsonProperty("name")
	private String name;
}
