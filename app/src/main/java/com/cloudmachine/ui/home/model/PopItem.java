package com.cloudmachine.ui.home.model;

/**
 * Created by xiaojw on 2017/8/16.
 */

public class PopItem {


    /**
     * jumpLink : null
     * actPictureLink : http://download.cloudm.com/image/login50.png
     * popupType : 3
     */

    private String jumpLink;
    private String actPictureLink;
    private int popupType;

    public String getJumpLink() {
        return jumpLink;
    }

    public void setJumpLink(String jumpLink) {
        this.jumpLink = jumpLink;
    }

    public String getActPictureLink() {
        return actPictureLink;
    }

    public void setActPictureLink(String actPictureLink) {
        this.actPictureLink = actPictureLink;
    }

    public int getPopupType() {
        return popupType;
    }

    public void setPopupType(int popupType) {
        this.popupType = popupType;
    }
}
