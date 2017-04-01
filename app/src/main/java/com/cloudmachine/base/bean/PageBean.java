package com.cloudmachine.base.bean;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/30 下午7:46
 * 修改人：shixionglu
 * 修改时间：2017/3/30 下午7:46
 * 修改备注：
 */

public class PageBean {

    //消息总条数
    public int totalElements = 5;

    //总页数
    public int totalPages;

    //是否最后一页
    public boolean last;

    //是否第一页
    public boolean first;

    //当前页数量
    public int numberOfElements;

    @Override
    public String toString() {
        return "PageBean{" +
                "totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", last=" + last +
                ", first=" + first +
                ", numberOfElements=" + numberOfElements +
                '}';
    }
}
