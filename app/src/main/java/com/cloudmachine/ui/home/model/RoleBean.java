package com.cloudmachine.ui.home.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojw on 2017/6/8.
 */

public class RoleBean implements Parcelable {

    /**
     * type : 管理者
     * permission : 20,21,22
     * description : 拥有为机器添加和删除成员的权限(适用于调度者、租赁者、工程业主等)
     * creator : 1
     * updater : 1
     * id : 2
     * createTime : 2016-06-29
     * updateTime : 2016-06-29
     */

    private String type;
    private String permission;
    private String description;
    private int creator;
    private int updater;
    private long id;
    private String createTime;
    private String updateTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public int getUpdater() {
        return updater;
    }

    public void setUpdater(int updater) {
        this.updater = updater;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.permission);
        dest.writeString(this.description);
        dest.writeInt(this.creator);
        dest.writeInt(this.updater);
        dest.writeLong(this.id);
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
    }

    public RoleBean() {
    }

    protected RoleBean(Parcel in) {
        this.type = in.readString();
        this.permission = in.readString();
        this.description = in.readString();
        this.creator = in.readInt();
        this.updater = in.readInt();
        this.id = in.readLong();
        this.createTime = in.readString();
        this.updateTime = in.readString();
    }

    public static final Parcelable.Creator<RoleBean> CREATOR = new Parcelable.Creator<RoleBean>() {
        @Override
        public RoleBean createFromParcel(Parcel source) {
            return new RoleBean(source);
        }

        @Override
        public RoleBean[] newArray(int size) {
            return new RoleBean[size];
        }
    };
}
