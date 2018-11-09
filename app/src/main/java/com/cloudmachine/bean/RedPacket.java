package com.cloudmachine.bean;

import java.util.List;

/**
 * Created by xiaojw on 2018/11/7.
 */

public class RedPacket {


    /**
     * redPacketRecordVOS : []
     * imageUrl :
     * jumpUrl :
     * isOpenActivity : 1
     */

    private String imageUrl;
    private String jumpUrl;
    private int isOpenActivity;//0：未开启 1：开启
    private List<RecordVos> redPacketRecordVOS;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public int getIsOpenActivity() {
        return isOpenActivity;
    }

    public void setIsOpenActivity(int isOpenActivity) {
        this.isOpenActivity = isOpenActivity;
    }

    public List<RecordVos> getRedPacketRecordVOS() {
        return redPacketRecordVOS;
    }

    public void setRedPacketRecordVOS(List<RecordVos> redPacketRecordVOS) {
        this.redPacketRecordVOS = redPacketRecordVOS;
    }

    public class RecordVos {

        private double redPacketMoney;

        private int redPacketStatus;

        private int id;

        public double getRedPacketMoney() {
            return redPacketMoney;
        }

        public void setRedPacketMoney(double redPacketMoney) {
            this.redPacketMoney = redPacketMoney;
        }

        public int getRedPacketStatus() {
            return redPacketStatus;
        }

        public void setRedPacketStatus(int redPacketStatus) {
            this.redPacketStatus = redPacketStatus;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }


}
