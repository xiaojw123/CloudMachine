package com.cloudmachine.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.chart.utils.AppLog;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;

import java.math.BigDecimal;

/**
 * Created by xiaojw on 2017/5/20.
 */

public class CommonUtils {

    public static double subtractDouble(double a,double b){
        BigDecimal aDecimal=new BigDecimal(Double.toString(a));
        BigDecimal bigDecimal=new BigDecimal(Double.toString(b));
        return Math.max(aDecimal.subtract(bigDecimal).doubleValue(),0);
    }


    public static  void showPermissionDialog(Context context){
        CustomDialog.Builder builder=new CustomDialog.Builder(context);
        builder.setMessage("需要开启定位服务，请到设置->找到位置权限，打开定位服务");
        builder.setNeutralButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static Animation getTraslateAnim(){
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(500);
        return animation;
    }


    public static SwipeMenuCreator getMenuCreator(final Context context) {
        return new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                AppLog.print("onCreateMenu____"+viewType);
                SwipeMenuItem item = new SwipeMenuItem(context);
                item.setBackgroundColor(context.getResources().getColor(R.color.cor17));
                item.setText("删除");
                item.setTextSize(22);
                item.setTextColor(context.getResources().getColor(R.color.cor15));
                item.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                item.setWidth(DensityUtil.dip2px(context, 68));
                swipeRightMenu.addMenuItem(item);
            }
        };
    }

    public static void callPhone(Context context, String phone) {
        if (TextUtils.isEmpty(phone)){
            return;
        }
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));//跳转到拨号界面，同时传递电话号码
        context.startActivity(dialIntent);
    }

    public static Spanned formatOilValue(int oilValue) {
        return Html.fromHtml("剩余油位  <font color=#0096e0>" + oilValue + "%<font>");
    }

    public static Spanned formatTimeLen(float value) {
        String timelen = "0时";
        if (value > 0) {
            int h = (int) value;
            int m = Math.round((value - h) * 60);
            if (h > 0) {
                if (m > 0)
                    timelen = h + "时" + m + "分";
                else
                    timelen = h + "时";
            } else {
                timelen = m + "分";
            }
        }
        return Html.fromHtml("今日工时 <font color=#3cbca3>" + timelen + "<font>");
    }


    public static PopupWindow getAnimPop(View contentView) {
        PopupWindow pop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 实例化一个ColorDrawable颜色为半透明
        pop.setAnimationStyle(R.style.PopAnimationStyle);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(contentView);
        return pop;
    }

//    public static String formartPrice(String price) {
//        if (!TextUtils.isEmpty(price)) {
//            if (price.contains(".")) {
//                int index = price.indexOf(".");
//                int len = price.length();
//                if (index == len - 2 && (price.charAt(len - 1) == '0')) {
//                    return price.substring(0, index);
//                }
//            }
//            return price;
//        }
//        return "0";
//    }


    public static String formartPrice(String price) {
        if (!TextUtils.isEmpty(price)) {
            if (price.contains(".")) {
                int index = price.indexOf(".");
                int len = price.length();
                int lastIndex = len - 1;
                if (lastIndex > index) {
                    // .0
                    int index1 = index + 1;
                    if (lastIndex == index1 && price.charAt(lastIndex) == '0') {
                        return price.substring(0, index);
                    }
                    int index2 = index + 2;
                    if (lastIndex >= index2) {
                        if (price.charAt(index1) == '0'
                                && price.charAt(index2) == '0') {
                            return price.substring(0, index);
                        } else {
                            if (lastIndex > index2) {
                                int index3=index + 3;
                                return price.substring(0, index3);
                            } else {
                                return price;
                            }
                        }

                    }

                } else {
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
