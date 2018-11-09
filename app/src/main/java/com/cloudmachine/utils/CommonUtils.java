package com.cloudmachine.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.bean.LarkMemberInfo;
import com.cloudmachine.bean.Member;
import com.cloudmachine.camera.CameraActivity;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.listener.IMapListener;
import com.cloudmachine.ui.home.activity.DeviceDetailActivity;
import com.cloudmachine.ui.home.activity.fragment.BaseMapFragment;
import com.cloudmachine.widget.CustomSucessDialog;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaojw on 2017/5/20.
 */

public class CommonUtils {

    //对webview内存进行释放
    public static void clearWebView(WebView webView) {
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
        }
        assert webView!=null;
        webView.stopLoading();
        webView.getSettings().setJavaScriptEnabled(false);
        webView.clearHistory();
        webView.clearView();
        webView.removeAllViews();
        webView.destroy();
    }


    public static Member convertMember(LarkMemberInfo info) {
        Member member = new Member();
        member.setId(info.getId());
        member.setMobile(info.getMobile());
        member.setNickName(info.getNickName());
        member.setLogo(info.getLogo());
        member.setUniqueId(info.getUniqueId());
        member.setIsAuth(info.getIsAuth());
        member.setAlipayLogo(info.getAlipayLogo());
        member.setAlipayNickname(info.getAlipayNickname());
        member.setAlipayUserId(info.getAlipayUserId());
        member.setWecharNickname(info.getWecharNickname());
        member.setWecharLogo(info.getWecharLogo());
        member.setOpenId(info.getOpenId());
        member.setUnionId(info.getUnionid());
        return member;
    }


    public static boolean isActivityDestoryed(Context context) {
        if (context == null || ((Activity) (context)).isFinishing()) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (((Activity) (context)).isDestroyed()) {
                return true;
            }
        }
        return false;
    }


    public static void showDialog(Context context, String message, String negativeText, String positiveText, final DialogInterface.OnClickListener positiveListener) {

        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setMessage(message);
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (positiveListener != null) {
                    positiveListener.onClick(dialog, which);
                }

            }
        });
        builder.create().show();
    }

    public static void showBackDialog(final Context context) {
        showDialog(context, "确定放弃提交吗？返回后页面数据将不会保存", "取消", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) context).finish();
            }
        });
    }

    public static void showDeletePicDialog(Context context, final DialogInterface.OnClickListener listener) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setMessage("确定要删除这张照片吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(dialog, which);
                }
            }
        });
        builder.create().show();
    }

    public static ImageView getMarkerView(Context context, float width, float height, int resId) {
        ImageView img = new ImageView(context);
        img.setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(context, width), DensityUtil.dip2px(context, height)));
        img.setImageResource(resId);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        return img;

    }

    public static void updateReomteMarkerOpt(final Context context, String picUrl, final LatLng latLng, final IMapListener listener) {
        Glide.with(context).load(picUrl)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        ImageView img = new ImageView(context);
                        img.setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(context, 50), DensityUtil.dip2px(context, 37)));
                        img.setImageDrawable(resource);
                        img.setScaleType(ImageView.ScaleType.FIT_XY);
                        MarkerOptions options = new MarkerOptions();
                        options.icon(BitmapDescriptorFactory.fromView(img));
                        options.position(latLng);
                        listener.updateMarkerOptions(options);
                    }
                });
    }


    public static String getMarkerIconUrl(String typePicUrl, int status) {
        return typePicUrl + "_" + status + "@3x.png";
    }

    public static String formartTime(long timeL) {
//        if (timeL > 0) {
//            timeL /= 1000;
//            long m = timeL / 3600;
//            long s = timeL - m * 3600;
//            String mStr = m < 10 ? "0" + m : String.valueOf(m);
//            String sStr = s < 10 ? "0" + s : String.valueOf(s);
//            return mStr + ":" + sStr;
//        }
        double timeLD = timeL / 1000.0;
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(timeLD);
    }

    public static void showSuccessDialog(Context context, String title, String cashAmount, String message) {
        CustomSucessDialog.Builder builder = new CustomSucessDialog.Builder(context);
        builder.setTitle1(title);
        builder.setTitle2(cashAmount);
        builder.setMessage(message);
        builder.create().show();
    }

    public static String getD5Str(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (!TextUtils.isEmpty(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlEncode) {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    if (keyToLower) {
                        buf.append(key.toLowerCase() + "=" + val);
                    } else {
                        buf.append(key + "=" + val);
                    }
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }


    public static boolean isHConfig(String sn) {
        if (!TextUtils.isEmpty(sn)) {
            if (sn.length() >= 9) {
                if (!sn.substring(2, 6).equals("2016") && sn.charAt(8) == 'A') {
                    return true;
                }
            }

        }
        return false;

    }

    public static String getPastDate(int offset) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - offset);
        Date today = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(today);
    }

    public static String getPastYear(int offset) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) - offset);
        Date today = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(today);
    }

    public static String getPastMonth(int offset) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - offset);
        Date today = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(today);
    }


    public static String getDateStamp() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR));
        Date today = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(today);
    }

    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    public static String getDateStamp(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        return sdf.format(new Date(timeStamp));
    }

    public static String getDateStampF(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//这个是你要转成后的时间的格式
        return sdf.format(new Date(timeStamp));
    }

    //I-yyy-MM-dd hh:mm:ss O-yyyy年MM月
    public static String getSalaryMonth(String timeStamp) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");//这个是你要转成后的时间的格式
//        String fm = sdf.format(new Date(timeStamp)).substring(2);
//        return fm;
        if (!TextUtils.isEmpty(timeStamp)) {
            String[] timeArray = timeStamp.split("-");
            return timeArray[0] + "年" + timeArray[1] + "月";
        }
        return "未知时间";
    }

    //I-yyy-MM-dd hh:mm:ss O-yyyyMM
    public static long getMonth(String timeStamp) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");//这个是你要转成后的时间的格式
//        String fm = sdf.format(new Date(timeStamp));
//        return Long.parseLong(fm);
        if (!TextUtils.isEmpty(timeStamp)) {
            String[] timeArray = timeStamp.split("-");
            if (timeArray.length >= 2) {
                return Long.parseLong(timeArray[0] + timeArray[1]);
            }
        }
        return 0;
    }

    public static double subtractDouble(double a, double b) {
        BigDecimal aDecimal = new BigDecimal(Double.toString(a));
        BigDecimal bigDecimal = new BigDecimal(Double.toString(b));
        return Math.max(aDecimal.subtract(bigDecimal).doubleValue(), 0);
    }


    public static void showPermissionDialog(Context context, int type) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        String msg = "需要开启形影权限，请到设置->权限管理中开启";
        switch (type) {
            case Constants.PermissionType.CAMERA:
                msg = "需要开启相机服务，请到设置->隐私->相机服务，打开相机服务";
                break;
            case Constants.PermissionType.STORAGE:
                msg = "需要开启读写存储权限，请到设置->权限管理，打开读写存储权限";
                break;
            case Constants.PermissionType.LOCATION:
                msg = "需要开启定位服务，请到设置->找到位置权限，打开定位服务";
                break;
            case Constants.PermissionType.STORAGE_CAMERA:
                msg = "需要开启读写存储权限和相机服务，请到设置->权限管理打开";
                break;
            case Constants.PermissionType.ADDRESS_BOOK:
                msg = "需要开启通讯录，请到设置->找到云机械->通讯录，打开通讯录";
                break;
        }
        builder.setMessage(msg);
        builder.setNeutralButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (BaseMapFragment.isShowDialog) {
                    BaseMapFragment.isShowDialog = false;
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static void showFinishPermissionDialog(final Context context) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setMessage("需要开启定位服务，请到设置->找到位置权限，打开定位服务");
        builder.setNeutralButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ((Activity) context).finish();
            }
        });
        builder.create().show();
    }

    public static void showCameraSDPermissionDialog(final Context context) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setMessage("需要开启相机服务和存储权限，请到设置->权限管理，打开相机服务和存储权限");
        builder.setNeutralButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ((Activity) context).finish();
            }
        });
        builder.create().show();
    }

    public static Animation getTraslateAnim() {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(500);
        return animation;
    }


    public static SwipeMenuCreator getMenuCreator(final Context context) {
        return new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                AppLog.print("onCreateMenu____" + viewType);
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
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));//跳转到拨号界面，同时传递电话号码
        context.startActivity(dialIntent);
    }

    public static Spanned formatOilValue(int oilValue) {
        return Html.fromHtml("剩余油位  <font color=#0096e0>" + oilValue + "%<font>");
    }

    public static Spanned formaLocTime(String locTime) {
        return Html.fromHtml("定位时间  <font color=#0096e0>" + locTime + "<font>");
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
                                int index3 = index + 3;
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
        if (len1 < len2 && curVerision.indexOf(onlineVersion) == 0) {
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

    public static String fillParams(String url, String key, String value) {
        if (!TextUtils.isEmpty(value)) {
            if (url != null && !url.contains(key)) {
                if (url.contains("?")) {
                    url += "&";
                } else {
                    url += "?";
                }
                url += key + "=" + value;
            }
        }
        return url;
    }

}
