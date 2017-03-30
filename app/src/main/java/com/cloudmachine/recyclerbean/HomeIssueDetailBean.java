package com.cloudmachine.recyclerbean;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/27 上午11:34
 * 修改人：shixionglu
 * 修改时间：2017/3/27 上午11:34
 * 修改备注：
 */

public class HomeIssueDetailBean implements HomePageType,Serializable{

    //问题id
    public int id;
    //提问人头像
    public String askerLogo;
    //提问人昵称
    public String askerName;
    //提问人机器品牌
    public String  askerBrand;
    //提问人机器型号
    public String askerModel;
    //提问时间
    public String askTime;
    //问题描述
    public String askDesc;
    //问题回答数量
    public int answerCount;
    //问题状态
    public int state;
    //点击跳转链接
    public String url;

    @Override
    public String toString() {
        return "HomeIssueDetailBean{" +
                "id=" + id +
                ", askerLogo='" + askerLogo + '\'' +
                ", askerName='" + askerName + '\'' +
                ", askerBrand='" + askerBrand + '\'' +
                ", askerModel='" + askerModel + '\'' +
                ", askTime='" + askTime + '\'' +
                ", askDesc='" + askDesc + '\'' +
                ", answerCount=" + answerCount +
                ", state=" + state +
                ", url='" + url + '\'' +
                '}';
    }
}
