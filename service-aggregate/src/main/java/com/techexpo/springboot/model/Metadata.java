package com.techexpo.springboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Metadata {
	
	@JsonProperty("@class")
	private String className;
}
