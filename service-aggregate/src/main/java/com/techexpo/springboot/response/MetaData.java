package com.techexpo.springboot.response;

public class MetaData {
	private String streaming;

	public String getStreaming() {
		return streaming;
	}

	public void setStreaming(String streaming) {
		this.streaming = streaming;
	}

	@Override
	public String toString() {
		return "MetaData [streaming=" + streaming + "]";
	}
	
	
}
