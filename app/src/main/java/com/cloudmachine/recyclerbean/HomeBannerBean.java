package com.cloudmachine.recyclerbean;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：轮播图实体类型
 * 创建人：shixionglu
 * 创建时间：2017/3/26 下午1:41
 * 修改人：shixionglu
 * 修改时间：2017/3/26 下午1:41
 * 修改备注：
 */

public class HomeBannerBean implements Serializable,HomePageType {

    //标题
    public String adsTitle;
    //描述
    public String adsDescription;
    //图片地址
    public String picAddress;
    //点击图片跳转链接
    public String picUrl;
    //广告id
    public long id;
    //微信分享需要展示的图标
    public String shareAddress;
    //微信分享的地址
    public String adsLink;

    public int skipType;
    public int adsMidSort;
    public long gmtModified;


    @Override
    public String toString() {
        return "HomeBannerBean{" +
                "adsTitle='" + adsTitle + '\'' +
                ", adsDescription='" + adsDescription + '\'' +
                ", picAddress='" + picAddress + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", id=" + id +
                ", shareAddress='" + shareAddress + '\'' +
                ", adsLink='" + adsLink + '\'' +
                ", skipType='" + skipType + '\'' +
                ", adsMidSort='" + adsMidSort + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                '}';
    }
}
