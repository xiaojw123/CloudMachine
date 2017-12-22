package com.cloudmachine.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by xiaojw on 2017/12/19.
 */

public class AdBean extends DataSupport{


    /**
     * id : 1
     * creator : null
     * gmtCreate : null
     * modifier : null
     * gmtModified : 1513587308000
     * yn : 0
     * adLink : http://pic4.nipic.com/20091217/3885730_124701000519_2.jpg
     * openLink : http://image.baidu.com/search/detail?http://medias.cloudm.com/static/h5/img/hd/tq/congratulations_you_get_vouchers.png
     * adType : 0
     * adSort : 1
     * adTime : 5
     * displayType : 3
     */

    private int id;
    private Object creator; //创建人
    private Object gmtCreate;//创建时间
    private Object modifier;//修改人
    private long gmtModified;//修改时间
    private int yn;//0 是否删除0：未删除 1:删除
    private String adLink;//广告地址
    private String openLink;//点击跳转地址
    private int adType;//广告类型 0 图片 1 视频 2 其他
    private int adSort;//广告顺序
    private int adTime;//广告停留时间（秒）
    private int displayType;//0:不展示 1:只展示一次 2：每天只展示一次 3.每次启动展示 4:前后台切换展示
    private String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getCreator() {
        return creator;
    }

    public void setCreator(Object creator) {
        this.creator = creator;
    }

    public Object getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Object gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Object getModifier() {
        return modifier;
    }

    public void setModifier(Object modifier) {
        this.modifier = modifier;
    }

    public long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public int getYn() {
        return yn;
    }

    public void setYn(int yn) {
        this.yn = yn;
    }

    public String getAdLink() {
        return adLink;
    }

    public void setAdLink(String adLink) {
        this.adLink = adLink;
    }

    public String getOpenLink() {
        return openLink;
    }

    public void setOpenLink(String openLink) {
        this.openLink = openLink;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public int getAdSort() {
        return adSort;
    }

    public void setAdSort(int adSort) {
        this.adSort = adSort;
    }

    public int getAdTime() {
        return adTime;
    }

    public void setAdTime(int adTime) {
        this.adTime = adTime;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }


}
