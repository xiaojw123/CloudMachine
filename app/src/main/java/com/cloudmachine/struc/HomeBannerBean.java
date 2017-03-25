package com.cloudmachine.struc;

import com.cloudmachine.itemtype.HomeTypeItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/20 下午6:51
 * 修改人：shixionglu
 * 修改时间：2017/3/20 下午6:51
 * 修改备注：
 */

public class HomeBannerBean implements HomeTypeItem, Serializable {

    private List<TopStoriesEntity> topStories;
    private List<String>           images;
    private List<String>           titles;
    private List<Integer>          ids;

    public HomeBannerBean(List<TopStoriesEntity> topStories) {
        this.topStories = topStories;
        initData();
    }

    private void initData() {
        images = new ArrayList<>();
        titles = new ArrayList<>();
        ids = new ArrayList<>();

        for (TopStoriesEntity topStory : topStories) {
            images.add(topStory.getImage());
            titles.add(topStory.getTitle());
            ids.add(topStory.getId());
        }
    }

    public List<TopStoriesEntity> getTopStories() {
        return topStories;
    }

    public List<String> getImages() {
        return images;
    }

    public List<String> getTitles() {
        return titles;
    }

    public List<Integer> getIds() {
        return ids;
    }

}
