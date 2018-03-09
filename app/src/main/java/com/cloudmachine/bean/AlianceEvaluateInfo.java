package com.cloudmachine.bean;

import java.util.List;

/**
 * Created by xiaojw on 2018/3/6.
 */

public class AlianceEvaluateInfo {


    /**
     * evaluateDTO : {"id":4,"creator":1106,"gmtCreate":1520314996000,"modifier":1106,"gmtModified":1520314827000,"yn":0,"orderId":321,"orderNo":"CMA_20180301154239751001","remark":"are you ok","level":1,"pathway":0}
     * tagCodeKeys : null
     * evaluateTagDTOS : null
     * advantage : [{"categoryCode":106,"codeKey":106001,"codeValue":"技术好"},{"categoryCode":106,"codeKey":106002,"codeValue":"效率高"}]
     * disadvantage : [{"categoryCode":107,"codeKey":107001,"codeValue":"故障未修复"}]
     */

    private EvaluateDTOBean evaluateDTO;
    private String tagCodeKeys;
    private String evaluateTagDTOS;
    private List<AdvantageBean> advantage;
    private List<DisadvantageBean> disadvantage;

    public EvaluateDTOBean getEvaluateDTO() {
        return evaluateDTO;
    }

    public void setEvaluateDTO(EvaluateDTOBean evaluateDTO) {
        this.evaluateDTO = evaluateDTO;
    }

    public String getTagCodeKeys() {
        return tagCodeKeys;
    }

    public void setTagCodeKeys(String tagCodeKeys) {
        this.tagCodeKeys = tagCodeKeys;
    }

    public Object getEvaluateTagDTOS() {
        return evaluateTagDTOS;
    }

    public void setEvaluateTagDTOS(String evaluateTagDTOS) {
        this.evaluateTagDTOS = evaluateTagDTOS;
    }

    public List<AdvantageBean> getAdvantage() {
        return advantage;
    }

    public void setAdvantage(List<AdvantageBean> advantage) {
        this.advantage = advantage;
    }

    public List<DisadvantageBean> getDisadvantage() {
        return disadvantage;
    }

    public void setDisadvantage(List<DisadvantageBean> disadvantage) {
        this.disadvantage = disadvantage;
    }

    public static class EvaluateDTOBean {
        /**
         * id : 4
         * creator : 1106
         * gmtCreate : 1520314996000
         * modifier : 1106
         * gmtModified : 1520314827000
         * yn : 0
         * orderId : 321
         * orderNo : CMA_20180301154239751001
         * remark : are you ok
         * level : 1
         * pathway : 0
         */

        private int id;
        private int creator;
        private long gmtCreate;
        private int modifier;
        private long gmtModified;
        private int yn;
        private int orderId;
        private String orderNo;
        private String remark;
        private int level;
        private int pathway;

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

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getPathway() {
            return pathway;
        }

        public void setPathway(int pathway) {
            this.pathway = pathway;
        }
    }

    public static class AdvantageBean {
        /**
         * categoryCode : 106
         * codeKey : 106001
         * codeValue : 技术好
         */

        private int categoryCode;
        private int codeKey;
        private String codeValue;

        public int getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(int categoryCode) {
            this.categoryCode = categoryCode;
        }

        public int getCodeKey() {
            return codeKey;
        }

        public void setCodeKey(int codeKey) {
            this.codeKey = codeKey;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }
    }

    public static class DisadvantageBean {
        /**
         * categoryCode : 107
         * codeKey : 107001
         * codeValue : 故障未修复
         */

        private int categoryCode;
        private int codeKey;
        private String codeValue;

        public int getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(int categoryCode) {
            this.categoryCode = categoryCode;
        }

        public int getCodeKey() {
            return codeKey;
        }

        public void setCodeKey(int codeKey) {
            this.codeKey = codeKey;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }
    }
}
