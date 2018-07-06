package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2018/7/4.
 */

public class LoanAuthInfo {

    private long memberId;
    private int idCardAuthStatus;
    private int figureAuthStatus;
    private int cardFourElementAuthStatus;
    private int relationAuthStatus;
    private int operatorAuthorizedStatus;

    public boolean isIdCardAuth(){
        return idCardAuthStatus==1;
    }
    public boolean isBankCardAuth(){
        return  cardFourElementAuthStatus==1;
    }
    public boolean isFigureAuth(){

      return  figureAuthStatus==1;
    }
    public boolean isRelationAuth(){
        return relationAuthStatus==1;
    }
    public boolean isOpeatoryAuth(){
        return operatorAuthorizedStatus==1;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public int getIdCardAuthStatus() {
        return idCardAuthStatus;
    }

    public void setIdCardAuthStatus(int idCardAuthStatus) {
        this.idCardAuthStatus = idCardAuthStatus;
    }

    public int getFigureAuthStatus() {
        return figureAuthStatus;
    }

    public void setFigureAuthStatus(int figureAuthStatus) {
        this.figureAuthStatus = figureAuthStatus;
    }

    public int getCardFourElementAuthStatus() {
        return cardFourElementAuthStatus;
    }

    public void setCardFourElementAuthStatus(int cardFourElementAuthStatus) {
        this.cardFourElementAuthStatus = cardFourElementAuthStatus;
    }

    public int getRelationAuthStatus() {
        return relationAuthStatus;
    }

    public void setRelationAuthStatus(int relationAuthStatus) {
        this.relationAuthStatus = relationAuthStatus;
    }

    public int getOperatorAuthorize() {
        return operatorAuthorizedStatus;
    }

    public void setOperatorAuthorize(int operatorAuthorizedStatus) {
        this.operatorAuthorizedStatus = operatorAuthorizedStatus;
    }
}
