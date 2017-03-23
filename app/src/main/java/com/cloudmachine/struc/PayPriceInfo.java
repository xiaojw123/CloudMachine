package com.cloudmachine.struc;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/26 上午4:08
 * 修改人：shixionglu
 * 修改时间：2017/2/26 上午4:08
 * 修改备注：
 */

public class PayPriceInfo implements Serializable {

    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PayPriceInfo{" +
                "amount=" + amount +
                '}';
    }
}
