package com.cloudmachine.ui.home.model;

import com.google.gson.JsonObject;

/**
 * Created by xiaojw on 2017/7/24.
 */

public class JsBody {


    /**
     * left_event : backPrevPage()
     * center_title : 问答社区
     * right_title : 我要提问
     * right_event : goNewPage()
     */
    private String title;
    private String desc;
    private String link;
    private String left_event;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private String center_title;
    private String right_title;
    private String right_event;
    private String share_title;
    private String selectAddress_event;
    private JsonObject share_content;

    public JsonObject getShare_content() {
        return share_content;
    }

    public void setShare_content(JsonObject share_content) {
        this.share_content = share_content;
    }

    public String getClick_event() {
        return click_event;
    }

    public void setClick_event(String click_event) {
        this.click_event = click_event;
    }

    private String login_event;


    private String click_event;

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getLeft_event() {
        return left_event;
    }

    public String getLogin_event() {
        return login_event;
    }

    public void setLogin_event(String login_event) {
        this.login_event = login_event;
    }

    public void setLeft_event(String left_event) {
        this.left_event = left_event;
    }

    public String getCenter_title() {
        return center_title;
    }

    public void setCenter_title(String center_title) {
        this.center_title = center_title;
    }

    public String getRight_title() {
        return right_title;
    }

    public void setRight_title(String right_title) {
        this.right_title = right_title;
    }

    public String getRight_event() {
        return right_event;
    }

    public void setRight_event(String right_event) {
        this.right_event = right_event;
    }
}
