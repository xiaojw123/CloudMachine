package com.cloudmachine.bean;

import java.util.List;

/**
 * Created by xiaojw on 2018/6/27.
 */

public class AddressBookItem {
    private String name;
    private String firstLetter;

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMobile() {
        return mobile;
    }

    public void setMobile(List<String> mobile) {
        this.mobile = mobile;
    }

    private List<String> mobile;

}
