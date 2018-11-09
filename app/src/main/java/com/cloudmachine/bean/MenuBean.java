package com.cloudmachine.bean;

import android.text.TextUtils;

/**
 * Created by xiaojw on 2017/12/19.
 */

public class MenuBean{


    /**
     * id : 1
     * creator : 1
     * gmtCreate : 1513587161000
     * modifier : 1
     * gmtModified : 1513596788000
     * yn : 0
     * menuTitle : 设备
     * menuLink : 设备链接地址
     * menuType : 0
     * menuMake : 1
     * menuSort : 1
     * menuHot : 0
     */

    private int id;

    public String getMenuLink() {
        if (!TextUtils.isEmpty(menuLink)) {
            return menuLink.trim();
        }
        return menuLink;
    }

    public void setMenuLink(String menuLink) {
        this.menuLink = menuLink;
    }

    private int creator;//创建人
    private String gmtCreate;//创建时间
    private int modifier;//修改人
    private String gmtModified;//修改时间
    private int yn;//是否删除
    private String menuTitle;//首页菜单名称
    private String menuLink;//首页菜单链接地址
    private int menuType;//菜单类型 0 内部服务 1 外部链接
    private int menuMake;//菜单标识 1：设备 2：维修 3: H5:
    private int menuSort;//菜单顺序
    private int menuHot;//是否高亮 0 不是 1 是

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public int getYn() {
        return yn;
    }

    public void setYn(int yn) {
        this.yn = yn;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }


    public int getMenuType() {
        return menuType;
    }

    public void setMenuType(int menuType) {
        this.menuType = menuType;
    }

    public int getMenuMake() {
        return menuMake;
    }

    public void setMenuMake(int menuMake) {
        this.menuMake = menuMake;
    }

    public int getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(int menuSort) {
        this.menuSort = menuSort;
    }

    public int getMenuHot() {
        return menuHot;
    }

    public void setMenuHot(int menuHot) {
        this.menuHot = menuHot;
    }

}
