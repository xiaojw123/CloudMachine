package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2018/8/6.
 */

public class AuthBean {


    /**
     * userUniqueNo : MzEyMzQxMTExMTExMTExMTEx
     * realName : 肖继伟
     * idCardNo : 3****************1
     * idCardAuthStatus : 1
     * cardFourElementAuthStatus : 0
     * operatorAuthorizedStatus : 0
     * relationAuthStatus : 0
     * cardFourElementAuthStatusTxt : 未验证证
     * operatorAuthorizedStatusTxt : 未授权
     * relationAuthStatusTxt : 未完善
     * auditStatus : 0
     * identityCheckStatus : 0
     * licenceCheckStatus : 0
     * incomeCheckStatus : 0
     * machineCheckStatus : 0
     * auditStatusTxt : 待完善
     * identityCheckStatusTxt : 待审核
     * licenceCheckStatusTxt : 待审核
     * incomeCheckStatusTxt : 待审核
     * machineCheckStatusTxt : 待审核
     * bankStatusTxt : null
     */

    private String userUniqueNo;
    private String realName;
    private String idCardNo;
    private int idCardAuthStatus;
    private int cardFourElementAuthStatus;
    private int operatorAuthorizedStatus;

    public String getResideAddressCheckStatusTxt() {
        return resideAddressCheckStatusTxt;
    }

    public void setResideAddressCheckStatusTxt(String resideAddressCheckStatusTxt) {
        this.resideAddressCheckStatusTxt = resideAddressCheckStatusTxt;
    }

    private int relationAuthStatus;
    private int resideAddressCheckStatus;
    private String resideAddressCheckStatusTxt;
    private String cardFourElementAuthStatusTxt;
    private String operatorAuthorizedStatusTxt;

    public int getResideAddressCheckStatus() {
        return resideAddressCheckStatus;
    }

    public void setResideAddressCheckStatus(int resideAddressCheckStatus) {
        this.resideAddressCheckStatus = resideAddressCheckStatus;
    }

    private String relationAuthStatusTxt;
    private int auditStatus;
    private int identityCheckStatus;// 0:待审核 1：审核中 2:审核通过 3:审核不通过
    private int licenceCheckStatus;
    private int incomeCheckStatus;
    private int machineCheckStatus;
    private int platformStatus;
    private String auditStatusTxt;
    private String identityCheckStatusTxt;

    public int getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(int platformStatus) {
        this.platformStatus = platformStatus;
    }

    private String licenceCheckStatusTxt;
    private String incomeCheckStatusTxt;
    private String machineCheckStatusTxt;
    private String bankStatusTxt;

    public String getUserUniqueNo() {
        return userUniqueNo;
    }

    public void setUserUniqueNo(String userUniqueNo) {
        this.userUniqueNo = userUniqueNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public int getIdCardAuthStatus() {
        return idCardAuthStatus;
    }

    public void setIdCardAuthStatus(int idCardAuthStatus) {
        this.idCardAuthStatus = idCardAuthStatus;
    }

    public int getCardFourElementAuthStatus() {
        return cardFourElementAuthStatus;
    }

    public void setCardFourElementAuthStatus(int cardFourElementAuthStatus) {
        this.cardFourElementAuthStatus = cardFourElementAuthStatus;
    }

    public int getOperatorAuthorizedStatus() {
        return operatorAuthorizedStatus;
    }

    public void setOperatorAuthorizedStatus(int operatorAuthorizedStatus) {
        this.operatorAuthorizedStatus = operatorAuthorizedStatus;
    }

    public int getRelationAuthStatus() {
        return relationAuthStatus;
    }

    public void setRelationAuthStatus(int relationAuthStatus) {
        this.relationAuthStatus = relationAuthStatus;
    }

    public String getCardFourElementAuthStatusTxt() {
        return cardFourElementAuthStatusTxt;
    }

    public void setCardFourElementAuthStatusTxt(String cardFourElementAuthStatusTxt) {
        this.cardFourElementAuthStatusTxt = cardFourElementAuthStatusTxt;
    }

    public String getOperatorAuthorizedStatusTxt() {
        return operatorAuthorizedStatusTxt;
    }

    public void setOperatorAuthorizedStatusTxt(String operatorAuthorizedStatusTxt) {
        this.operatorAuthorizedStatusTxt = operatorAuthorizedStatusTxt;
    }

    public String getRelationAuthStatusTxt() {
        return relationAuthStatusTxt;
    }

    public void setRelationAuthStatusTxt(String relationAuthStatusTxt) {
        this.relationAuthStatusTxt = relationAuthStatusTxt;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public int getIdentityCheckStatus() {
        return identityCheckStatus;
    }

    public void setIdentityCheckStatus(int identityCheckStatus) {
        this.identityCheckStatus = identityCheckStatus;
    }

    public int getLicenceCheckStatus() {
        return licenceCheckStatus;
    }

    public void setLicenceCheckStatus(int licenceCheckStatus) {
        this.licenceCheckStatus = licenceCheckStatus;
    }

    public int getIncomeCheckStatus() {
        return incomeCheckStatus;
    }

    public void setIncomeCheckStatus(int incomeCheckStatus) {
        this.incomeCheckStatus = incomeCheckStatus;
    }

    public int getMachineCheckStatus() {
        return machineCheckStatus;
    }

    public void setMachineCheckStatus(int machineCheckStatus) {
        this.machineCheckStatus = machineCheckStatus;
    }

    public String getAuditStatusTxt() {
        return auditStatusTxt;
    }

    public void setAuditStatusTxt(String auditStatusTxt) {
        this.auditStatusTxt = auditStatusTxt;
    }

    public String getIdentityCheckStatusTxt() {
        return identityCheckStatusTxt;
    }

    public void setIdentityCheckStatusTxt(String identityCheckStatusTxt) {
        this.identityCheckStatusTxt = identityCheckStatusTxt;
    }

    public String getLicenceCheckStatusTxt() {
        return licenceCheckStatusTxt;
    }

    public void setLicenceCheckStatusTxt(String licenceCheckStatusTxt) {
        this.licenceCheckStatusTxt = licenceCheckStatusTxt;
    }

    public String getIncomeCheckStatusTxt() {
        return incomeCheckStatusTxt;
    }

    public void setIncomeCheckStatusTxt(String incomeCheckStatusTxt) {
        this.incomeCheckStatusTxt = incomeCheckStatusTxt;
    }

    public String getMachineCheckStatusTxt() {
        return machineCheckStatusTxt;
    }

    public void setMachineCheckStatusTxt(String machineCheckStatusTxt) {
        this.machineCheckStatusTxt = machineCheckStatusTxt;
    }

    public String getBankStatusTxt() {
        return bankStatusTxt;
    }

    public void setBankStatusTxt(String bankStatusTxt) {
        this.bankStatusTxt = bankStatusTxt;
    }
}
