package com.cloudmachine.recyclerbean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/29 上午10:52
 * 修改人：shixionglu
 * 修改时间：2017/3/29 上午10:52
 * 修改备注：
 */

public class HomeNewsTransfer implements HomePageType, Serializable {

    public ArrayList<HomeNewsBean> homeNewsBeans;
    public ArrayList<String> adsTitle;
    public ArrayList<String> adsDescription;
    public ArrayList<String> picUrl;
    public ArrayList<String> picAddress;

    public HomeNewsTransfer(ArrayList<HomeNewsBean> homeNewsBean) {
        this.homeNewsBeans = homeNewsBean;
        initData();
    }

    private void initData() {

        adsTitle = new ArrayList<>();
        adsDescription = new ArrayList<>();
        picUrl = new ArrayList<>();
        picAddress = new ArrayList<>();

        /*for (int i=1;i<=homeNewsBean.size();i++) {
            adsTitle.add(homeNewsBean.get(i).adsTitle);
            adsDescription.add(homeNewsBean.get(i).adsDescription);
            picUrl.add(homeNewsBean.get(i).picUrl);
            picAddress.add(homeNewsBean.get(i).picAddress);
        }*/
        for (HomeNewsBean homeNewsBean : homeNewsBeans) {
            adsTitle.add(homeNewsBean.adsTitle);
            adsDescription.add(homeNewsBean.adsDescription);
            picUrl.add(homeNewsBean.picUrl);
            picAddress.add(homeNewsBean.picAddress);
        }
    }

    @Override
    public String toString() {
        return "HomeNewsTransfer{" +
                "homeNewsBeans=" + homeNewsBeans +
                ", adsTitle=" + adsTitle +
                ", adsDescription=" + adsDescription +
                ", picUrl=" + picUrl +
                ", picAddress=" + picAddress +
                '}';
    }
}
