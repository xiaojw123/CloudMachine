package com.cloudmachine.bean;

public class MessageBO {


	/**
	 * id : 21
	 * fromMemberNickname : null
	 * fromMemberId : 0
	 * imgpath :
	 * fromMemberMobile : null
	 * inviteDate : 18-06-20
	 * status : 1
	 * deviceName : 体验机
	 * messageType : 11
	 * title : 工资
	 * content : 耿铭辰向您发了一笔工资1元，点击查看详情
	 * url : null
	 */

	private int id;
	private String fromMemberNickname;
	private int fromMemberId;
	private String imgpath;
	private String fromMemberMobile;
	private String inviteDate;
	private int status;//1.未处理 2.拒绝邀请 3.同意邀请 4.已读
	private String deviceName;
	private int messageType;
	private String title;
	private String content;
	private String url;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFromMemberNickname() {
		return fromMemberNickname;
	}

	public void setFromMemberNickname(String fromMemberNickname) {
		this.fromMemberNickname = fromMemberNickname;
	}

	public int getFromMemberId() {
		return fromMemberId;
	}

	public void setFromMemberId(int fromMemberId) {
		this.fromMemberId = fromMemberId;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	public String getFromMemberMobile() {
		return fromMemberMobile;
	}

	public void setFromMemberMobile(String fromMemberMobile) {
		this.fromMemberMobile = fromMemberMobile;
	}

	public String getInviteDate() {
		return inviteDate;
	}

	public void setInviteDate(String inviteDate) {
		this.inviteDate = inviteDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
