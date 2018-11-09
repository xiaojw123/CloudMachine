package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2018/9/19.
 */

public class LarkMemberItem {


    /**
     * groupId : 0
     * memberId : 880
     * mobile : 15268168675
     * middleLogo : http://medias.test.cloudm.com/member/6e70d6e1-c3ab-400e-86f2-e9d406a6b90b.jpeg
     * realName : 肖纪伟
     * nickName : 水冷
     * roleId : 1
     * roleValue : 机主
     * remark : null
     */

    private int groupId;
    private int memberId;
    private String mobile;
    private String middleLogo;
    private String realName;
    private String nickName;
    private int roleId;
    private String roleValue;
    private String remark;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMiddleLogo() {
        return middleLogo;
    }

    public void setMiddleLogo(String middleLogo) {
        this.middleLogo = middleLogo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleValue() {
        return roleValue;
    }

    public void setRoleValue(String roleValue) {
        this.roleValue = roleValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
