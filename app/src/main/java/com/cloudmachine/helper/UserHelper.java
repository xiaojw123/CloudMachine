package com.cloudmachine.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.text.TextUtils;
import android.util.ArraySet;

import com.autonavi.rtbt.IFrameForRTBT;
import com.cloudmachine.bean.DeviceItem;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.Member;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiaojw on 2017/5/6.
 */

public class UserHelper {
    public static String TOKEN;
    public static String ID;

    private static List<McDeviceInfo> sDeviceList = new ArrayList<>();

    public static void setMyDevices(List<McDeviceInfo> deviceList) {
        sDeviceList = deviceList;
    }

    public static List<McDeviceInfo> getMyDevices() {
        return sDeviceList;
    }


//    public static boolean isLogin(Context context) {
//        return MemeberKeeper.getOauth(context) != null;
//    }

    // TODO: 2018/9/13 登录判断
    public static boolean isLogin(Context context) {
        String token = getValue(context, "token");
        return !TextUtils.isEmpty(token);
    }


    //    public static long getMemberId(Context context) {
//        return MemeberKeeper.getOauth(context).getId();
//    }
    public static long getMemberId(Context context) {
        if (TextUtils.isEmpty(ID)) {
            return -1;
        } else {
            return Integer.parseInt(ID);
        }
    }


    public static String getValue(Context context, String key) {
        if (sUserSp == null) {
            sUserSp = context.getSharedPreferences(USER_SP, Context.MODE_PRIVATE);
        }
        return sUserSp.getString(key, null);
    }

    public static void saveKeyValue(Context context, String key, String value) {
        if (sUserSp == null) {
            sUserSp = context.getSharedPreferences(USER_SP, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sUserSp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveKeyValue(Context context, Map<String, String> data) {
        if (sUserSp == null) {
            sUserSp = context.getSharedPreferences(USER_SP, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sUserSp.edit();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue());
        }
        editor.apply();
    }


    public static void saveInfo(Context context, String key, String value) {
        if (loginLogSp == null) {
            loginLogSp = context.getSharedPreferences(loginLOGO, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = loginLogSp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getLogo(Context context, String phone) {
        if (loginLogSp == null) {
            loginLogSp = context.getSharedPreferences(loginLOGO, Context.MODE_PRIVATE);
        }
        return loginLogSp.getString(phone, null);
    }

    private static SharedPreferences sUserSp;
    private static SharedPreferences loginLogSp;
    private static SharedPreferences GuideTagSp;
    private static final String USER_SP = "user_sp";
    private static final String loginLOGO = "logo_sp";
    private static final String GuideTag = "guide_tag";
    private static final String HConfigGuideTagPic = "guide_tag_hc_pic";
    private static final String HConfigGuideTagVideo = "guide_tag_hc_video";
    private static final String WorkTimeGuideTag = "work_time_guide";
    private static final String OilCustomTextTag = "oil_custom_text_tag";

    public static void insertGuideTag(Context context, boolean flag) {
        if (GuideTagSp == null) {
            GuideTagSp = context.getSharedPreferences(GuideTag, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = GuideTagSp.edit();
        editor.putBoolean(GuideTag, flag);
        editor.apply();
    }

    public static void insertHConfigGuideTag(Context context, boolean flag, boolean isVideoTag) {
        if (GuideTagSp == null) {
            GuideTagSp = context.getSharedPreferences(GuideTag, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = GuideTagSp.edit();
        if (isVideoTag) {
            editor.putBoolean(HConfigGuideTagVideo, flag);
        } else {
            editor.putBoolean(HConfigGuideTagPic, flag);
        }
        editor.apply();
    }

    public static void insertWorkTimeGuideTag(Context context, boolean flag) {
        if (GuideTagSp == null) {
            GuideTagSp = context.getSharedPreferences(GuideTag, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = GuideTagSp.edit();
        editor.putBoolean(WorkTimeGuideTag, flag);
        editor.apply();
    }

    public static boolean getWorkTimeGuideTag(Context context) {
        if (GuideTagSp == null) {
            GuideTagSp = context.getSharedPreferences(GuideTag, Context.MODE_PRIVATE);
        }
        return GuideTagSp.getBoolean(WorkTimeGuideTag, false);
    }


    public static boolean getGuideTag(Context context) {
        if (GuideTagSp == null) {
            GuideTagSp = context.getSharedPreferences(GuideTag, Context.MODE_PRIVATE);
        }
        return GuideTagSp.getBoolean(GuideTag, false);
    }

    public static boolean getHConfigGuideTag(Context context, boolean isVideoTag) {
        if (GuideTagSp == null) {
            GuideTagSp = context.getSharedPreferences(GuideTag, Context.MODE_PRIVATE);
        }
        if (isVideoTag) {
            return GuideTagSp.getBoolean(HConfigGuideTagVideo, false);
        } else {
            return GuideTagSp.getBoolean(HConfigGuideTagPic, false);
        }
    }


    public static void insertCustomTextTag(Context context, boolean isShowed) {
        if (GuideTagSp == null) {
            GuideTagSp = context.getSharedPreferences(GuideTag, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = GuideTagSp.edit();
        editor.putBoolean(OilCustomTextTag, isShowed);
        editor.apply();
    }

    public static boolean getCustomTextTag(Context context) {
        if (GuideTagSp == null) {
            GuideTagSp = context.getSharedPreferences(GuideTag, Context.MODE_PRIVATE);
        }
        return GuideTagSp.getBoolean(OilCustomTextTag, false);
    }

    public static void setOwner(Context context, long memberId, boolean isOwner) {
        if (memberId== Constants.INVALID_DEVICE_ID){
            return;
        }
        if (GuideTagSp == null) {
            GuideTagSp = context.getSharedPreferences(GuideTag, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = GuideTagSp.edit();
        editor.putBoolean(String.valueOf(memberId), isOwner);
        editor.apply();
    }

    public static boolean isOwner(Context context, long memberId) {
        if (memberId==Constants.INVALID_DEVICE_ID)
        if (GuideTagSp == null) {
            GuideTagSp = context.getSharedPreferences(GuideTag, Context.MODE_PRIVATE);
        }
        return GuideTagSp.getBoolean(String.valueOf(memberId), false);
    }


}
