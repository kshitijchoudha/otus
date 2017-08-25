package com.techexpo.springboot.response;

public class AggregateServiceNotice {
	//left blank intentionally
	/*
	 * "title": "CPU usage average at 80%",
              "link": "http://link/to/relevant/thing",
              "severity": 1
	 */
	String title;
	String link;
	int severity;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getSeverity() {
		return severity;
	}
	public void setSeverity(int severity) {
		this.severity = severity;
	}
	
	@Override
	public String toString() {
		return "AggregateServiceNotice [title=" + title + ", link=" + link + ", severity=" + severity + "]";
	}
	
}
