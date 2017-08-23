package com.techexpo.springboot.response;

public class MetaData {
	private int streaming;

	

	public int getStreaming() {
		return streaming;
	}



	public void setStreaming(int streaming) {
		this.streaming = streaming;
	}



	@Override
	public String toString() {
		return "MetaData [streaming=" + streaming + "]";
	}
	
	
}
