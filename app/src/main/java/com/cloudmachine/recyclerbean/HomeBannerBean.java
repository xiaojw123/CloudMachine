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
    //图片
    public String picAddress;
    //跳转链接
    public String picUrl;
    //广告id
    public long id;

    @Override
    public String toString() {
        return "HomeBannerBean{" +
                "adsTitle='" + adsTitle + '\'' +
                ", picAddress='" + picAddress + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", id=" + id +
                '}';
    }
}
