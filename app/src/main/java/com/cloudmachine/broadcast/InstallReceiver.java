package com.cloudmachine.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cloudmachine.chart.utils.AppLog;

/**
 * Created by xiaojw on 2017/10/31.
 */

public class InstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String act = intent.getAction();
        AppLog.print("InstallReceiver____act:" + act);


    }
}
