package com.cloudmachine.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by xiaojw on 2017/12/19.
 */

public class MenuBean implements Parcelable {


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
    private long gmtCreate;//创建时间
    private int modifier;//修改人
    private long gmtModified;//修改时间
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

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.creator);
        dest.writeLong(this.gmtCreate);
        dest.writeInt(this.modifier);
        dest.writeLong(this.gmtModified);
        dest.writeInt(this.yn);
        dest.writeString(this.menuTitle);
        dest.writeString(this.menuLink);
        dest.writeInt(this.menuType);
        dest.writeInt(this.menuMake);
        dest.writeInt(this.menuSort);
        dest.writeInt(this.menuHot);
    }

    public MenuBean() {
    }

    protected MenuBean(Parcel in) {
        this.id = in.readInt();
        this.creator = in.readInt();
        this.gmtCreate = in.readLong();
        this.modifier = in.readInt();
        this.gmtModified = in.readLong();
        this.yn = in.readInt();
        this.menuTitle = in.readString();
        this.menuLink = in.readString();
        this.menuType = in.readInt();
        this.menuMake = in.readInt();
        this.menuSort = in.readInt();
        this.menuHot = in.readInt();
    }

    public static final Parcelable.Creator<MenuBean> CREATOR = new Parcelable.Creator<MenuBean>() {
        @Override
        public MenuBean createFromParcel(Parcel source) {
            return new MenuBean(source);
        }

        @Override
        public MenuBean[] newArray(int size) {
            return new MenuBean[size];
        }
    };
}
