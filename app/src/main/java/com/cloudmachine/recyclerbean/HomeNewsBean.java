package com.cloudmachine.recyclerbean;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/27 上午12:40
 * 修改人：shixionglu
 * 修改时间：2017/3/27 上午12:40
 * 修改备注：
 */

public class HomeNewsBean implements HomePageType,Serializable{

    //广告位置排序左上下
    public Integer adsMidSort;
    //广告标题
    public String adsTitle;
    //广告描述
    public String adsDescription;
    //点击图片跳转链接
    public String picUrl;
    //展示图片链接
    public String picAddress;
    //分享图片位置
    public String shareAddress;
    public String adsLink;

    @Override
    public String toString() {
        return "HomeNewsBean{" +
                "adsMidSort=" + adsMidSort +
                ", adsTitle='" + adsTitle + '\'' +
                ", adsDescription='" + adsDescription + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", picAddress='" + picAddress + '\'' +
                ", shareAddress='" + shareAddress + '\'' +
                ", adsLink='" + adsLink + '\'' +
                '}';
    }
}
