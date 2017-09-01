package com.cloudmachine.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shixionglu on 2016/10/27.
 * 评论页面
 */

public class CommentsInfo implements Serializable{


    /**
	 * 
	 */
	private static final long serialVersionUID = -4381516092739412835L;
	/**
     * code_NAME : 故障未修复
     * code : 90021001
     */

    private ArrayList<DisadvantageBean> disadvantage;
    /**
     * code_NAME : 技术好
     * code : 90011001
     */

    private ArrayList<DisadvantageBean> advantage;


    

    public ArrayList<DisadvantageBean> getDisadvantage() {
        return disadvantage;
    }

    public void setDisadvantage(ArrayList<DisadvantageBean> disadvantage) {
        this.disadvantage = disadvantage;
    }

    public ArrayList<DisadvantageBean> getAdvantage() {
        return advantage;
    }

    public void setAdvantage(ArrayList<DisadvantageBean> advantage) {
        this.advantage = advantage;
    }

}
