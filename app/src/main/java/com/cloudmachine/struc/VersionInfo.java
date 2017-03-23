package com.cloudmachine.struc;

import java.io.Serializable;


public class VersionInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7315948534531077379L;
	private int mustUpdate;
	private String system;
	private String version;
	private String message;
	private String link;
	

	public int getMustUpdate() {
		return mustUpdate;
	}
	public void setMustUpdate(int mustUpdate) {
		this.mustUpdate = mustUpdate;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
}
