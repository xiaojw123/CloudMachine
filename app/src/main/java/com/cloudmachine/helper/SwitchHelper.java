package com.cloudmachine.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xiaojw on 2017/6/5.
 */

public class SwitchHelper {
    private static final String SWITCH_INFO = "switch_info";
    private static SharedPreferences switchSp;
    private static final String KEY_SWITCH_AD = "key_switch_ad";

    public static void setSwitchPromotionAd(Context context, boolean flag) {
        initSP(context);
        SharedPreferences.Editor editor = switchSp.edit();
        editor.putBoolean(KEY_SWITCH_AD, flag);
        editor.commit();
    }

    public static boolean isAdShowed(Context context) {
        initSP(context);
        return switchSp.getBoolean(KEY_SWITCH_AD, false);
    }

    private static void initSP(Context context) {
        if (switchSp == null) {
            switchSp = context.getSharedPreferences(SWITCH_INFO, Context.MODE_PRIVATE);
        }

    }

}
