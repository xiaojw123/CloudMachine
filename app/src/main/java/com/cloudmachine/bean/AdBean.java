package com.cloudmachine.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by xiaojw on 2017/12/19.
 */

public class AdBean extends DataSupport{
    /**
     * adId : null
     * adTitle : 云盒子免费拿
     * adDescription : 解锁你挖机的新姿势，让挖机6到飞起
     * adPicUrl : http://medias.cloudm.com/img/20171209/8eb7debb-44cf-472c-b9eb-9e796f8bb3b5.png
     * adJumpLink : https://h5.cloudm.com/n/hd/spread?sO=2
     * skipType : 1
     * adType : 4
     * adStopTime : 5
     * displayType : 3
     * adSort : 1
     */

    private String adId;
    private String adTitle;
    private String adDescription;
    private String adPicUrl;
    private String adJumpLink;
    private int skipType;
    private int adType;
    private int adStopTime;
    private int displayType;
    private int adSort;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdDescription() {
        return adDescription;
    }

    public void setAdDescription(String adDescription) {
        this.adDescription = adDescription;
    }

    public String getAdPicUrl() {
        return adPicUrl;
    }

    public void setAdPicUrl(String adPicUrl) {
        this.adPicUrl = adPicUrl;
    }

    public String getAdJumpLink() {
        return adJumpLink;
    }

    public void setAdJumpLink(String adJumpLink) {
        this.adJumpLink = adJumpLink;
    }

    public int getSkipType() {
        return skipType;
    }

    public void setSkipType(int skipType) {
        this.skipType = skipType;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public int getAdStopTime() {
        return adStopTime;
    }

    public void setAdStopTime(int adStopTime) {
        this.adStopTime = adStopTime;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public int getAdSort() {
        return adSort;
    }

    public void setAdSort(int adSort) {
        this.adSort = adSort;
    }
    private String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }


//
//    /**
//     * id : 1
//     * creator : null
//     * gmtCreate : null
//     * modifier : null
//     * gmtModified : 1513587308000
//     * yn : 0
//     * adLink : http://pic4.nipic.com/20091217/3885730_124701000519_2.jpg
//     * openLink : http://image.baidu.com/search/detail?http://medias.cloudm.com/static/h5/img/hd/tq/congratulations_you_get_vouchers.png
//     * adType : 0
//     * adSort : 1
//     * adTime : 5
//     * displayType : 3
//     */
//
//    private int id;
//    private String creator; //创建人
//    private String gmtCreate;//创建时间
//    private String modifier;//修改人
//    private String gmtModified;//修改时间
//    private int yn;//0 是否删除0：未删除 1:删除
//    private String adLink;//广告地址
//    private String openLink;//点击跳转地址
//    private int adType;//广告类型 0 图片 1 视频 2 其他
//    private int adSort;//广告顺序
//    private int adTime;//广告停留时间（秒）
//    private int displayType;//0:不展示 1:只展示一次 2：每天只展示一次 3.每次启动展示 4:前后台切换展示


//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getCreator() {
//        return creator;
//    }
//
//    public void setCreator(String creator) {
//        this.creator = creator;
//    }
//
//    public String getGmtCreate() {
//        return gmtCreate;
//    }
//
//    public void setGmtCreate(String gmtCreate) {
//        this.gmtCreate = gmtCreate;
//    }
//
//    public String getModifier() {
//        return modifier;
//    }
//
//    public void setModifier(String modifier) {
//        this.modifier = modifier;
//    }
//
//    public String getGmtModified() {
//        return gmtModified;
//    }
//
//    public void setGmtModified(String gmtModified) {
//        this.gmtModified = gmtModified;
//    }
//
//    public int getYn() {
//        return yn;
//    }
//
//    public void setYn(int yn) {
//        this.yn = yn;
//    }
//
//    public String getAdLink() {
//        return adLink;
//    }
//
//    public void setAdLink(String adLink) {
//        this.adLink = adLink;
//    }
//
//    public String getOpenLink() {
//        return openLink;
//    }
//
//    public void setOpenLink(String openLink) {
//        this.openLink = openLink;
//    }
//
//    public int getAdType() {
//        return adType;
//    }
//
//    public void setAdType(int adType) {
//        this.adType = adType;
//    }
//
//    public int getAdSort() {
//        return adSort;
//    }
//
//    public void setAdSort(int adSort) {
//        this.adSort = adSort;
//    }
//
//    public int getAdTime() {
//        return adTime;
//    }
//
//    public void setAdTime(int adTime) {
//        this.adTime = adTime;
//    }
//
//    public int getDisplayType() {
//        return displayType;
//    }
//
//    public void setDisplayType(int displayType) {
//        this.displayType = displayType;
//    }


}
