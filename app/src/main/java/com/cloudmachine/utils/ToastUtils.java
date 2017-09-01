package com.cloudmachine.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.Toast;

import com.cloudmachine.MyApplication;

import es.dmoral.toasty.Toasty;

/**
 * 项目名称：CloudMachine
 * 类描述：吐丝工具类
 * 创建人：shixionglu
 * 创建时间：2017/3/17 下午2:27
 * 修改人：shixionglu
 * 修改时间：2017/3/17 下午2:27
 * 修改备注：
 */

public class ToastUtils {

    public static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static void showCenterToast(Context context,String message){
        Toast t=Toast.makeText(context,message,Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();
    }

    /**
     *
     * @param msg
     * @param withIcon 是否需要图片
     */
    public static void error(String msg,boolean withIcon) {
        Toasty.error(MyApplication.mContext, msg, Toast.LENGTH_SHORT, withIcon).show();
    }

    public static void success(String msg,boolean withIcon) {
        Toasty.success(MyApplication.mContext, msg, Toast.LENGTH_SHORT, withIcon).show();
    }

    public static void info(String msg,boolean withIcon) {
        Toasty.info(MyApplication.mContext, msg, Toast.LENGTH_SHORT, withIcon).show();
    }

    public static void warning(String msg,boolean withIcon) {
        Toasty.warning(MyApplication.mContext, msg, Toast.LENGTH_SHORT, withIcon).show();
    }

    public static void normal(String msg,boolean withIcon) {
        Toasty.normal(MyApplication.mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public static void normal(String msg, boolean withIcon, Drawable icon ) {
        Toasty.normal(MyApplication.mContext, msg, Toast.LENGTH_SHORT,icon,withIcon).show();
    }
}
