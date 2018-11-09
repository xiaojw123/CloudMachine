package com.cloudmachine.bean;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/26 上午4:17
 * 修改人：shixionglu
 * 修改时间：2017/2/26 上午4:17
 * 修改备注：
 */

public class AliPayBean implements Serializable {
    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    private String outTradeNo;
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
