package com.cloudmachine.bean;

import com.cloudmachine.bean.DisadvantageBean;

import java.util.List;

/**
 * Created by xiaojw on 2017/8/11.
 */

public class EvaluateInfoBean {


    /**
     * advantage : [{"code_NAME":"技术好","code":"90011001"}]
     * disadvantage : [{"code_NAME":"客户之声","code":"90021010"}]
     * satisfaction : 5
     * evaluate_PATH : 10111005
     * next_VISITDATE :
     * suggestion :
     * visit_REASON :
     * pk_CSS_WORK_EVALUATE : 100000612
     */

    private int satisfaction;
    private String evaluate_PATH;
    private String next_VISITDATE;
    private String suggestion;
    private String visit_REASON;
    private int pk_CSS_WORK_EVALUATE;
    private List<DisadvantageBean> advantage;
    private List<DisadvantageBean> disadvantage;

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }

    public String getEvaluate_PATH() {
        return evaluate_PATH;
    }

    public void setEvaluate_PATH(String evaluate_PATH) {
        this.evaluate_PATH = evaluate_PATH;
    }

    public String getNext_VISITDATE() {
        return next_VISITDATE;
    }

    public void setNext_VISITDATE(String next_VISITDATE) {
        this.next_VISITDATE = next_VISITDATE;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getVisit_REASON() {
        return visit_REASON;
    }

    public void setVisit_REASON(String visit_REASON) {
        this.visit_REASON = visit_REASON;
    }

    public int getPk_CSS_WORK_EVALUATE() {
        return pk_CSS_WORK_EVALUATE;
    }

    public void setPk_CSS_WORK_EVALUATE(int pk_CSS_WORK_EVALUATE) {
        this.pk_CSS_WORK_EVALUATE = pk_CSS_WORK_EVALUATE;
    }

    public List<DisadvantageBean> getAdvantage() {
        for (DisadvantageBean bean : advantage) {
            bean.setChecked(true);
        }
        return advantage;
    }

    public void setAdvantage(List<DisadvantageBean> advantage) {
        this.advantage = advantage;
    }

    public List<DisadvantageBean> getDisadvantage() {
        for (DisadvantageBean bean : disadvantage) {
            bean.setChecked(true);
        }
        return disadvantage;
    }

    public void setDisadvantage(List<DisadvantageBean> disadvantage) {
        this.disadvantage = disadvantage;
    }

}
