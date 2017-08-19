package com.techexpo.springboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Port {
	@JsonProperty("$")
	private String dollar;
	
	@JsonProperty("@enabled")
	private String enabled;

	public String getDollar() {
		return dollar;
	}

	public void setDollar(String dollar) {
		this.dollar = dollar;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "Port [dollar=" + dollar + ", enabled=" + enabled + "]";
	}
	
	
	
}
