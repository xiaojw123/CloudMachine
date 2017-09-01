package com.cloudmachine.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.Member;
import com.cloudmachine.utils.MemeberKeeper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2017/5/6.
 */

public class UserHelper {

    private static List<McDeviceInfo> sDeviceList = new ArrayList<>();

    public static void setMyDevices(List<McDeviceInfo> deviceList) {
        sDeviceList = deviceList;
    }

    public static List<McDeviceInfo> getMyDevices() {
        return sDeviceList;
    }

    public static Long getWjdxID(Context context) {
        Member member = MemeberKeeper.getOauth(context);
        if (member != null) {
            Long id = member.getWjdsId();
            if (id != null) {
                return id;

            }
        }
        return null;
    }

    public static boolean isLogin(Context context) {
        return MemeberKeeper.getOauth(context) != null;
    }

    public static long getMemberId(Context context) {
        return MemeberKeeper.getOauth(context).getId();
    }

    public static void saveInfo(Context context, String phone, String logoUrl) {
        if (loginLogSp == null) {
            loginLogSp = context.getSharedPreferences(loginLOGO, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = loginLogSp.edit();
        editor.putString(phone, logoUrl);
        editor.commit();
    }

    public static String getLogo(Context context, String phone) {
        if (loginLogSp == null) {
            loginLogSp = context.getSharedPreferences(loginLOGO, Context.MODE_PRIVATE);
        }
        return loginLogSp.getString(phone, null);
    }


    private static SharedPreferences loginLogSp;
    private static String loginLOGO = "logo_sp";

}
