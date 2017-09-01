package com.cloudmachine.bean;

import java.util.List;


public class MessageBO {
	private long id;
	private String inviterNickname;
	private long inviterId;
	private String deviceName;
	private Device deviceList;
	private List<Permission> permissionList;
	//状态0 标明未处理1表示已经同意 2,表示已经拒绝
	private int status; // 1:未处理消息 2:拒绝邀请 3:接受邀请 4:未读报警消息 5:已读报警
	private String role;
	private String permission;
	private String imgpath;
	private String isRecommend;
	private String inviteTime;
	private int messageType; //1:邀请消息 2:移交机主 3:机器报警消息
	private String title;
	private String content;
	private String message;
	private String url;
	private int count;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public String getIsRecommend() {
		return isRecommend;
	}
	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}
	public String getInviteTime() {
		return inviteTime;
	}
	public void setInviteTime(String inviteTime) {
		this.inviteTime = inviteTime;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<Permission> getPermissionList() {
		return permissionList;
	}
	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}
	public String getInviterNickname() {
		return inviterNickname;
	}
	public void setInviterNickname(String inviterNickname) {
		this.inviterNickname = inviterNickname;
	}
	public long getInviterId() {
		return inviterId;
	}
	public void setInviterId(long inviterId) {
		this.inviterId = inviterId;
	}
	public Device getDeviceList() {
		return deviceList;
	}
	public void setDeviceList(Device deviceList) {
		this.deviceList = deviceList;
	}
	
	public void setMessageType(int messageType){
		this.messageType = messageType;
	}
	public int getMessageType(){
		return this.messageType;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return this.title;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	public String getContent(){
		return this.content;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	public String getMessage(){
		return this.message;
	}
	@Override
	public String toString() {
		return "MessageBO [id=" + id + ", inviterNickname=" + inviterNickname
				+ ", inviterId=" + inviterId + ", deviceName=" + deviceName
				+ ", deviceList=" + deviceList + ", permissionList="
				+ permissionList + ", status=" + status + ", role=" + role
				+ ", permission=" + permission + ", imgpath=" + imgpath
				+ ", isRecommend=" + isRecommend + ", inviteTime=" + inviteTime
				+ ", messageType=" + messageType + ", title=" + title
				+ ", content=" + content + ", message=" + message + "]";
	}
	
}
