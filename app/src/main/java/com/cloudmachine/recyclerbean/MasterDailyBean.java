package com.cloudmachine.recyclerbean;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/31 上午9:26
 * 修改人：shixionglu
 * 修改时间：2017/3/31 上午9:26
 * 修改备注：
 */

public class MasterDailyBean implements Serializable{

    public int id;
    //标题
    public String artTitle;
    //描述
    public String artDescription;
    //跳转链接
    public String picUrl;
    //图片地址
    public String picAddress;
    //分享图片位置
    public String shareAddress;
    //文章类型id
    public int artType;
    //阅读量
    public int readCount;
    //最后一次修改时间
    public String gmtModifiedFormat;
    //分享链接
    public String artLink;


    @Override
    public String toString() {
        return "MasterDailyBean{" +
                "id=" + id +
                ", artTitle='" + artTitle + '\'' +
                ", artDescription='" + artDescription + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", picAddress='" + picAddress + '\'' +
                ", shareAddress='" + shareAddress + '\'' +
                ", artType=" + artType +
                ", readCount=" + readCount +
                ", gmtModifiedFormat='" + gmtModifiedFormat + '\'' +
                ", artLink='" + artLink + '\'' +
                '}';
    }
}
