package com.cloudmachine.bean;

import java.util.List;

/**
 * Created by xiaojw on 2018/10/23.
 */

public class AuthInfoDetail {


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * uniqueId : NDEyNzI4MTk4OTAxMDk2ODM5
     * resideAddress : null
     * annualIncome : null
     * picture : [{"id":44,"picUrl":"http://192.168.1.109:3306//member/logo/2018/10/73e7739f-351f-4c00-8429-e9101968f9fc.JPEG","auditStatus":1},{"id":45,"picUrl":"http://192.168.1.109:3306//member/logo/2018/10/fb60137a-7e1a-42f1-81c9-e7c771e5b355.JPEG","auditStatus":1}]
     * device : null
     */

    private int code=-1;
    private String emergencyContactName;
    private String emergencyContactMobile;
    private String emergencyRelation;

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactMobile() {
        return emergencyContactMobile;
    }

    public void setEmergencyContactMobile(String emergencyContactMobile) {
        this.emergencyContactMobile = emergencyContactMobile;
    }

    public String getEmergencyRelation() {
        return emergencyRelation;
    }

    public void setEmergencyRelation(String emergencyRelation) {
        this.emergencyRelation = emergencyRelation;
    }

    private String uniqueId;
    private String resideAddress;
    private String annualIncome;
    private Object device;
    private List<PictureBean> picture;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getResideAddress() {
        return resideAddress;
    }

    public void setResideAddress(String resideAddress) {
        this.resideAddress = resideAddress;
    }

    public String getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(String annualIncome) {
        this.annualIncome = annualIncome;
    }

    public Object getDevice() {
        return device;
    }

    public void setDevice(Object device) {
        this.device = device;
    }

    public List<PictureBean> getPicture() {
        return picture;
    }

    public void setPicture(List<PictureBean> picture) {
        this.picture = picture;
    }

    public static class PictureBean {
        /**
         * id : 44
         * picUrl : http://192.168.1.109:3306//member/logo/2018/10/73e7739f-351f-4c00-8429-e9101968f9fc.JPEG
         * auditStatus : 1
         */

        private int id;
        private String picUrl;
        private int auditStatus;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public int getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(int auditStatus) {
            this.auditStatus = auditStatus;
        }
    }
}
