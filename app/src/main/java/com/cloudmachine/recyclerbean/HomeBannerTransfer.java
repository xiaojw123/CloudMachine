package com.cloudmachine.recyclerbean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/26 下午1:47
 * 修改人：shixionglu
 * 修改时间：2017/3/26 下午1:47
 * 修改备注：
 */

public class HomeBannerTransfer implements HomePageType, Serializable {

    public ArrayList<HomeBannerBean> bannerStories;
    public ArrayList<String> images;
    public ArrayList<String> titles;
    public ArrayList<Long> ids;
    public ArrayList<String> jumpLinks;
    public ArrayList<String> wxLogo;
    public ArrayList<String> wxUrl;
    public ArrayList<String> description;


    public HomeBannerTransfer(ArrayList<HomeBannerBean> bannerStories) {
        this.bannerStories = bannerStories;
        initData();
    }

    private void initData() {

        images = new ArrayList<>();
        titles = new ArrayList<>();
        jumpLinks = new ArrayList<>();
        ids = new ArrayList<>();
        wxUrl = new ArrayList<>();
        wxLogo = new ArrayList<>();
        description = new ArrayList<>();

        for (HomeBannerBean bannerBean : bannerStories) {
            images.add(bannerBean.picAddress);
            titles.add(bannerBean.adsTitle);
//            jumpLinks.add(bannerBean.picUrl);
            if (TextUtils.isEmpty(bannerBean.adsLink)) {
                jumpLinks.add(bannerBean.picUrl);
                wxUrl.add(bannerBean.picUrl);
            } else {
                jumpLinks.add(bannerBean.adsLink);
                wxUrl.add(bannerBean.adsLink);
            }
            ids.add(bannerBean.id);
            wxLogo.add(bannerBean.shareAddress);
            description.add(bannerBean.adsDescription);
        }
    }

    @Override
    public String toString() {
        return "HomeBannerTransfer{" +
                "bannerStories=" + bannerStories +
                ", images=" + images +
                ", titles=" + titles +
                ", ids=" + ids +
                ", jumpLinks=" + jumpLinks +
                ", wxLogo=" + wxLogo +
                ", wxUrl=" + wxUrl +
                '}';
    }
}
