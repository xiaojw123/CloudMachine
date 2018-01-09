package com.cloudmachine.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.cloudmachine.activities.AboutCloudActivity;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.helper.CustomActivityManager;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.ui.home.activity.DeviceDetailActivity;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.home.activity.MessageDetailActivity;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.ui.homepage.activity.ViewMessageActivity;
import com.cloudmachine.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知, content:" + context + "___Activity__" + CustomActivityManager.getInstance().getTopActivity());
            updateView(bundle);
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))

        {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            notifactionToActivity(context, bundle);


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))

        {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))

        {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else

        {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }

    }

    // TODO: 2017/11/30 通知类型 type
    private void updateView(Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (!TextUtils.isEmpty(extras)) {
            try {
                JSONObject extraJson = new JSONObject(extras);
                if (extraJson.has("type")) {
                    Activity topAct = CustomActivityManager.getInstance().getTopActivity();
                    String type = extraJson.getString("type");
                    switch (type) {
                        case "11":
                            if (topAct != null && topAct instanceof HomeActivity) {
                                ((HomeActivity) topAct).updateDeviceMessage();
                            }
                            break;
                        case "12":
                            Class topClass = topAct.getClass();
                            if (BaseAutoLayoutActivity.class.isAssignableFrom(topClass)) {
                                String url = extraJson.optString("url");
                                ((BaseAutoLayoutActivity) topAct).showDepsitPayPop(url);
                            }
                            break;

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to HomeActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        if (HomeActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(HomeActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(HomeActivity.KEY_MESSAGE, message);
            if (!TextUtils.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (extraJson.length() > 0) {
                        msgIntent.putExtra(HomeActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            context.sendBroadcast(msgIntent);
        }
    }

    private void notifactionToActivity(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (!TextUtils.isEmpty(extras)) {
            try {
                JSONObject extraJson = new JSONObject(extras);
                if (extraJson.has("type")) {
                    String type = extraJson.getString("type");
                    int iType = Integer.valueOf(type);
                    switch (iType) {//1,邀请消息  2,报警消息 3,版本消息  4,电子围栏 5,问卷调查
                        case 1:
                        case 5:
                        case 11:
                            //打开自定义的Activity    //MessageActivity
                            Intent i1 = new Intent(context, ViewMessageActivity.class);
                            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i1);
                            return;
                        case 2:
                            if (extraJson.has("deviceId") && extraJson.has("nickName")) {
                                String deviceIdStr = extraJson.getString("deviceId");
                                String nickNameStr = extraJson.getString("nickName");
                                Intent i2 = new Intent(context, DeviceDetailActivity.class);
                                i2.putExtra(Constants.P_DEVICEID, Long.valueOf(deviceIdStr));
                                i2.putExtra(Constants.P_DEVICENAME, nickNameStr);
                                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(i2);
                            }
                            return;
                        case 3:
                            Intent i3 = new Intent(context, AboutCloudActivity.class);
                            i3.putExtras(bundle);
                            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i3);
                            return;
                        case 8:
                            Intent i4 = new Intent(context, QuestionCommunityActivity.class);
                            bundle.putString(QuestionCommunityActivity.H5_URL, ApiConstants.AppBoxDetail);
                            i4.putExtras(bundle);
                            i4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i4);
                            return;
                        case 9:
                        case 10:
//                            notifaction__Result__{"url":"http:\/\/h5.test.cloudm.com\/n\/ask_chatlist?qid=949&auid=4823&myid=4852","type":"9"}
                            Intent notifyIntent = new Intent(context, QuestionCommunityActivity.class);
                            String notifyUrl = extraJson.optString("url");
                            notifyIntent.putExtra(QuestionCommunityActivity.H5_URL, notifyUrl);
                            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(notifyIntent);
                            break;
                        case 12:
                        case 14:
                            Intent tbIntent=new Intent(context,QuestionCommunityActivity.class);
                            tbIntent.putExtra(QuestionCommunityActivity.H5_URL, extraJson.optString("url"));
                            context.startActivity(tbIntent);
                            break;
                        case 15://消息内容跳转
                            String idStr=extraJson.getString("id");
                            Intent detailIntent=new Intent(context, MessageDetailActivity.class);
                            detailIntent.putExtra(MessageDetailActivity.MESSAGE_ID,idStr);
                            context.startActivity(detailIntent);
                            break;

                        default:
                            Intent i2 = new Intent(context, HomeActivity.class);
                            i2.putExtras(bundle);
                            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i2);
                            break;
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
