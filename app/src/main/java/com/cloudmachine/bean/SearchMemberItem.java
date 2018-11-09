package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2018/9/25.
 */

public class SearchMemberItem {


    /**
     * memberId : 880
     * mobile : 15268168675
     * middlelogo : http://medias.test.cloudm.com/member/6e70d6e1-c3ab-400e-86f2-e9d406a6b90b.jpeg
     * nickName : 水冷
     * sex : null
     */

    private int memberId;
    private String mobile;
    private String middlelogo;
    private String nickName;
    private Object sex;

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

    public String getMiddlelogo() {
        return middlelogo;
    }

    public void setMiddlelogo(String middlelogo) {
        this.middlelogo = middlelogo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Object getSex() {
        return sex;
    }

    public void setSex(Object sex) {
        this.sex = sex;
    }
}
