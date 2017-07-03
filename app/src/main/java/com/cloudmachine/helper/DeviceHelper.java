package com.cloudmachine.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.cloudmachine.struc.NewRepairInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by xiaojw on 2017/6/9.
 */

public class DeviceHelper {
    private static final String SP_NAME = "deviceinfo";
    private static final String KEY_REPAIRINFO = "repairinfo";
    private static SharedPreferences deviceSp;

    public static void saveRepairInfo(Context context, NewRepairInfo info) {
        initSP(context);
        SharedPreferences.Editor editor = deviceSp.edit();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(info);
            String info_Base64 = new String(Base64.encode(bos.toByteArray(), Base64.DEFAULT));
            editor.putString(KEY_REPAIRINFO, info_Base64);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearRepairInfo(Context context) {
        initSP(context);
        SharedPreferences.Editor editor = deviceSp.edit();
        editor.clear();
        editor.commit();
    }

    public static NewRepairInfo getRepairInfo(Context context) {
        initSP(context);
        NewRepairInfo info = null;
        try {
            String info_Base64 = deviceSp.getString(KEY_REPAIRINFO, null);
            if (!TextUtils.isEmpty(info_Base64)) {
                byte[] info_byte = Base64.decode(info_Base64.getBytes(), Base64.DEFAULT);
                ByteArrayInputStream bis = new ByteArrayInputStream(info_byte);
                ObjectInputStream ois = new ObjectInputStream(bis);
                info = (NewRepairInfo) ois.readObject();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    private static void initSP(Context context) {
        if (deviceSp == null) {
            deviceSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }


    }


}
