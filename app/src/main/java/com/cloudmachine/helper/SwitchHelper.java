package com.cloudmachine.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xiaojw on 2017/6/5.
 */

public class SwitchHelper {
    private static final String SWITCH_INFO = "switch_info";
    private static SharedPreferences switchSp;
    private static final String KEY_SWITCH_AD_TIME = "key_switch_ad_time";

    public static void setSwitchPromotionAdTime(Context context, long time) {
        initSP(context);
        SharedPreferences.Editor editor = switchSp.edit();
        editor.putLong(KEY_SWITCH_AD_TIME, time);
        editor.commit();
    }


    public static boolean isAdShowed(Context context,long time) {
        initSP(context);
        long lastTiime=switchSp.getLong(KEY_SWITCH_AD_TIME,0);
        return time==lastTiime;
    }

    private static void initSP(Context context) {
        if (switchSp == null) {
            switchSp = context.getSharedPreferences(SWITCH_INFO, Context.MODE_PRIVATE);
        }

    }

}
