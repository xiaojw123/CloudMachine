package com.cloudmachine.ui.home.model;

/**
 * Created by xiaojw on 2018/1/4.
 */

public class MessageDetail {


    /**
     * id : 1103
     * fromMemberId : 134
     * toMemberId : 1106
     * createTime : 1467798332000
     * updateTime : 1467798340000
     * message : null
     * status : 3
     * permissionIds : 20,21,22
     * deviceIds : 614
     * roleIds : 1
     * messageType : 2
     * roleRemark : null
     * title : null
     * readStatus : null
     * isDelete : 1
     * url : null
     * reply_name : null
     */

    private int id;
    private int fromMemberId;
    private int toMemberId;
    private long createTime;
    private long updateTime;
    private Object message;
    private int status;
    private String permissionIds;
    private String deviceIds;
    private String roleIds;
    private int messageType;
    private Object roleRemark;
    private Object title;
    private Object readStatus;
    private int isDelete;
    private Object url;
    private Object reply_name;
    private String content;


    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromMemberId() {
        return fromMemberId;
    }

    public void setFromMemberId(int fromMemberId) {
        this.fromMemberId = fromMemberId;
    }

    public int getToMemberId() {
        return toMemberId;
    }

    public void setToMemberId(int toMemberId) {
        this.toMemberId = toMemberId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(String permissionIds) {
        this.permissionIds = permissionIds;
    }

    public String getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(String deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Object getRoleRemark() {
        return roleRemark;
    }

    public void setRoleRemark(Object roleRemark) {
        this.roleRemark = roleRemark;
    }

    public Object getTitle() {
        return title;
    }

    public void setTitle(Object title) {
        this.title = title;
    }

    public Object getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Object readStatus) {
        this.readStatus = readStatus;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public Object getReply_name() {
        return reply_name;
    }

    public void setReply_name(Object reply_name) {
        this.reply_name = reply_name;
    }
}
