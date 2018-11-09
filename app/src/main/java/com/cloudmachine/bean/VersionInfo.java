package com.cloudmachine.bean;



public class VersionInfo{


	/**
	 * id : 1
	 * system : Android
	 * version : 5.0.1
	 * link : http://updatepackage.cloudm.com/android/apk/CloudMachine.apk
	 * message : 版本5.0.1 优化界面和操作流程，提高用户体验
	 * createTime : null
	 * updateTime : null
	 * productId : 2
	 * mustUpdate : 0
	 */

	private int id;
	private String system;
	private String version;
	private String link;
	private String message;
	private String createTime;
	private String updateTime;
	private int productId;
	private int mustUpdate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getMustUpdate() {
		return mustUpdate;
	}

	public void setMustUpdate(int mustUpdate) {
		this.mustUpdate = mustUpdate;
	}
}
