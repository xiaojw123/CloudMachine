package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2017/11/2.
 */

public class QiToken {


    /**
     * uptoken : PbC3RYPbbnhDtn8B6XTpv2yhrm9wuuPFJQ0zGoAy:hSxJFf3v0OrYCWP_XpyOTxi3BM0=:eyJzY29wZSI6ImNsb3VkbS13ZWItdGVzdCIsImRlYWRsaW5lIjoxNTA5NjE5NTg4fQ==
     * origin : http://medias.test.cloudm.com/
     */

    private String uptoken;
    private String origin;

    public String getUptoken() {
        return uptoken;
    }

    public void setUptoken(String uptoken) {
        this.uptoken = uptoken;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
