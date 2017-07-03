package com.cloudmachine.utils;

import android.text.TextUtils;

/**
 * Created by xiaojw on 2017/5/20.
 */

public class CommonUtils {


    public static String formartPrice(String price) {
        if (!TextUtils.isEmpty(price)) {
            if (price.contains(".")) {
                int index = price.indexOf(".");
                int len = price.length();
                if (index == len - 2 && (price.charAt(len - 1) == '0')) {
                    return price.substring(0, index);
                }
            }
            return price;
        }
        return "0";
    }

    public static boolean checVersion(String curVerision, String onlineVersion) {
        if (TextUtils.isEmpty(onlineVersion) || TextUtils.isEmpty(curVerision)) {
            return false;
        }
        // 2.2.1
        int len1 = onlineVersion.length();
        int len2 = curVerision.length();
        if (len1 < len2 && curVerision.contains(onlineVersion)) {
            return false;
        }
        if (len1 > len2
                && onlineVersion.contains(curVerision)) {
            return true;
        }
        curVerision = curVerision.replace('.', ':');
        onlineVersion = onlineVersion.replace('.', ':');
        String[] curV = curVerision.split(":");
        String[] onlineV = onlineVersion.split(":");
        int len = Math.min(curV.length, onlineV.length);
        for (int i = 0; i < len; i++) {
            try {
                int curIndex = Integer.parseInt(curV[i]);
                int onLineIndex = Integer.parseInt(onlineV[i]);
                System.out.println("cur index___" + curIndex + ",  online__"
                        + onLineIndex);
                if (curIndex < onLineIndex) {
                    return true;
                } else if (curIndex > onLineIndex) {
                    return false;
                }
            } catch (Exception e) {
                System.out.println("非法参数异常");
                return false;
            }
        }
        return false;
    }
}
