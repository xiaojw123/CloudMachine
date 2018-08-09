package com.cloudmachine.helper;

/**
 * Created by xiaojw on 2017/8/8.
 */

public interface OrderStatus {
    String COMPLETED = "已完工";
    String EVALUATION = "待评价";
    String ING="进行中";
    String PAY="待支付";
    String WAIT="等待接单";
    String CANCEL="已取消";
    String DROP="已丢单";
    String REPAIRING="维修中";
    String CLOSED="已关闭";



}
