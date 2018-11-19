package com.cloudmachine.utils;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.Toast;

import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.activities.GalleryActivity;
import com.cloudmachine.activities.ImagePagerActivity;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.ui.personal.activity.PersonalDataActivity;
import com.cloudmachine.utils.photo.util.ImageItem;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import rx.functions.Action1;

public class Constants {
    public static final int HANDLER_SEARCHMEMBER_SUCCESS = 0x02;
    public static final int HANDLER_SEARCHMEMBER_FAIL = 0x03;
    public static final int HANDLER_ADDMEMBER_SUCCESS = 0x04;
    public static final int HANDLER_ADDMEMBER_FAIL = 0x05;
    public static final int HANDLER_SWITCH_GUIDACTIVITY = 0x10;
    public static final int HANDLER_SWITCH_MAINACTIVITY = 0x11;
    public static final int HANDLER_FINISH_IMAGEPAGERACTIVITY = 0x12;
    public static final int HANDLER_GETMACHINEBRAND_SUCCESS = 0x13;
    public static final int HANDLER_GETMACHINEBRAND_FAIL = 0x14;
    public static final int HANDLER_GETMACHINEMODEL_SUCCESS = 0x15;
    public static final int HANDLER_GETMACHINEMODEL_FAIL = 0x16;
    public static final int HANDLER_TIMER = 0x17;
    public static final int HANDLER_BLOGSUPPORTLIST_SUCCESS = 0x18;
    public static final int HANDLER_BLOGSUPPORTLIST_FAIL = 0x19;
    public static final int HANDLER_GETCODE_SUCCESS = 0x20;
    public static final int HANDLER_GETCODE_FAIL = 0x21;
    public static final int HANDLER_UPDATEMEMBERINFO_SUCCESS = 0x24;
    public static final int HANDLER_UPDATEMEMBERINFO_FAIL = 0x25;
    public static final int HANDLER_FORGETPWD_SUCCESS = 0x26;
    public static final int HANDLER_FORGETPWD_FAIL = 0x27;
    public static final int HANDLER_UPDATEPWD_SUCCESS = 0x28;
    public static final int HANDLER_UPDATEPWD_FAIL = 0x29;
    public static final int HANDLER_REGISTER_SUCCESS = 0x30;
    public static final int HANDLER_REGISTER_FAIL = 0x31;
    public static final int HANDLER_SEARCHBLOG_SUCCESS = 0x32;
    public static final int HANDLER_SEARCHBLOG_FAIL = 0x33;
    public static final int HANDLER_DAILYWORK_SUCCESS = 0x34;
    public static final int HANDLER_DAILYWORK_FAIL = 0x35;
    public static final int HANDLER_LOCUS_SUCCESS = 0x36;
    public static final int HANDLER_LOCUS_FAIL = 0x37;
    public static final int HANDLER_CHART_MARKER_TIME = 0x38;
    public static final int HANDLE_GETWORKTIMELIST_SUCCESS = 0x39;
    public static final int HANDLE_GETWORKTIMELIST_FAILD = 0x40;
    public static final int HANDLER_GETTAGINFO_SUCCESS = 0x41;
    public static final int HANDLER_ADDMEMBER = 0x42;
    public static final int REQUEST_SELECTCITY = 0x43;
    public static final int REQUEST_MAPCHOOSE = 0x44;
    public static final int HANDLER_NEWREPAIR_SUCCESS = 0x45;
    public static final int HANDLER_NEWREPAIR_FAILD = 0x46;
    public static final int HANDLER_GET_WORKDETAIL_FAILD = 0x49;
    public static final int HANDLER_GETDATASTATISTICS_SUCCESS = 0x50;
    public static final int HANDLER_GETDATASTATISTICS_FAILD = 0x51;
    public static final int HANDLER_GETCOUPONS_FAILD = 0x52;
    public static final int HANDLER_GETCWPAY_SUCCESS = 0x54;
    public static final int HANDLER_GETCWPAY_FAILD = 0x55;
    public static final int HANDLER_UPLOAD_SUCCESS = 0x56;
    public static final int HANDLER_UPLOAD_FAILD = 0x57;
    public static final int HANDLER_UPDATE_INFO_SUCCESS = 0x58;
    public static final int HANDLER_UPDATE_INFO_FAILD = 0x59;
    public static final int HANDLER_JS_BODY = 0x60;
    public static final int HANDLER_JS_JUMP = 0x61;
    public static final int HANDLER_JS_ALERT = 0x62;
    public static final int HANDLER_GET_EVALUATE_INFO_SUCCESS = 0x63;
    public static final int HANDLER_REPAIR = 0x64;
    public static final int HANDLER_SHOW_CLOSE_BTN = 0x65;
    public static final int HANDLER_HIDEN_CLOSE_BTN = 0x66;
    public static final int HANDLER_ALIPAY_RESULT = 0x67;
    public static final int HANDLER_JUMP_MY_ORDER = 0x68;
    public static final int HANDLER_H5_JUMP = 0x69;
    public static final int HANDLER_CHANGE_BOX_ACT = 0x70;
    public static final int HANDLER_RESULT_APLIPAY = 0x71;
    public static final int HANDLER_UPDATE_PROGRESS = 0x72;
    public static final int HANDLER_BACk_LOCATION = 0x73;
    public static final int HANDLER_FACE_RECOGNITION = 0x74;
    public static final String P_DEVICEID = "DeviceId";
    public static final String P_DEVICENAME = "DeviceName";
    public static final String P_OILLAVE = "oillave";
    public static final String P_SEARCHLISTTYPE = "searchListType";
    public static final String P_TITLETEXT = "titleText";
    public static final String P_TITLENAME = "titleName";
    public static final String P_EDITTYPE = "editType";
    public static final String P_ITEMTYPE = "itemType";
    public static final String P_TYPEID = "typeid";
    public static final String P_BRANDID = "brandid";
    public static final String P_MODELID = "modelid";
    public static final String P_PAYTYPE = "payType";
    public static final String P_PAYAMOUNT = "payAmount";
    public static final String P_RECEIVERLIST = "receiverList";
    public static final String P_EDITRESULTSTRING = "editResultString";
    public static final String P_EDITRESULTITEM = "editResultItem";
    public static final String P_IMAGEBROWERDELETE = "imageBrowerDelete";
    public static final String P_EDIT_LIST_VALUE1 = "editListValue1";
    public static final String P_EDIT_LIST_VALUE2 = "editListValue2";
    public static final String P_EDIT_LIST_ITEM_NAME = "editlistItemName";
    public static final String P_CITYNAME = "city_name";
    public static final String P_SEARCHINFO = "searchInfo";
    public static final String S_TIME_FG = "-";
    public static final int E_DEVICE_TEXT = 10;
    public static final int E_DEVICE_DATA = E_DEVICE_TEXT + 1;
    public static final int E_DEVICE_LIST = E_DEVICE_DATA + 1;

    public static final int E_ITEMS_deviceName = 50;// 设备名称
    public static final int E_ITEMS_category = E_ITEMS_deviceName + 1;// 品类
    public static final int E_ITEMS_brand = E_ITEMS_category + 1;// 品牌
    public static final int E_ITEMS_model = E_ITEMS_brand + 1;// 型号
    public static final int E_ITEMS_buyPrice = E_ITEMS_model + 1;// 购机价格
    public static final int REQUEST_ToSearchActivity = E_ITEMS_buyPrice + 1;
    public static final int REQUEST_ToSearchDeviceActivity = REQUEST_ToSearchActivity + 1;
    public static final String KEY_isHomeGuide = "isHomeGuide"; // 是否首页引导过
    public static final String KEY_DownloadId = "DownloadID"; // 下载的ID
    public static final String KEY_NewVersion = "NewVersion"; // 最新版本
    public static final int REQUEST_ImageActivity = 100;
    public static final int CLICK_POSITION = 101;
    public static final java.lang.String APP_ID = "wxfb6afbcc23f867df";
    public static boolean isChangeDevice;
    public static Class<?> photoParent;
    public static Bitmap photoBimap;
    public static final String DateFormat1 = "yyyy-MM-dd";
    public static final int PAY_TYPE_WX = 0;//微信客户端支付
    public static final int PAY_TYPE_ALIPAY = 1;//支付宝客户端支付
    public static final int PAY_TYPE_PURSE = 99;//钱包
    public static final String FLUSH_TOKEN="flush_token";
    private static List<Activity> activityList = new ArrayList<Activity>();


    public static void removeActivity(Activity ac) {
        activityList.remove(ac);
    }


    public static void exit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // System.exit(0);
        }
    }

    public static DisplayImageOptions displayDeviceImageOptions = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.mc_default_icon)
            .showImageForEmptyUri(R.drawable.mc_default_icon)
            .showImageOnFail(R.drawable.mc_default_icon).cacheInMemory(true)
            .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(10))
            .build();

    public static DisplayImageOptions displayListImageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.mc_default_icon) // 设置图片在下载期间显示的图片
            .showImageForEmptyUri(R.drawable.mc_default_icon)// 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.mc_default_icon) // 设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
            .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
            .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
            .build();// 构建完成

    public static ImageLoaderConfiguration getConfiguration(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "imageloader/Cache");
        return new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
                // Can slow ImageLoader, use it carefully (Better don't use
                // it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                // You can pass your own memory cache
                // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100)
                // 缓存的文件数量
                .discCache(new UnlimitedDiscCache(cacheDir))
                // 自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(
                        new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
                // (5
                // s),
                // readTimeout
                // (30
                // s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();// 开始构建
    }

    public static final String IMAGE_PATH = Environment
            .getExternalStorageDirectory().toString()
            + File.separator
            + "cloudmachine" + File.separator + "images" + File.separator;

    public static final String SYS_IMG_PATH = Environment
            .getExternalStorageDirectory().toString() + File.separator + "DCIM" + File.separator;

    public static void ToastAction(String msg) {
        if (!TextUtils.isEmpty(msg))
            Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_LONG)
                    .show();
    }

    public static void MyToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_LONG)
                    .show();
        }
    }


    public static String getNowData() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String getNowTimeAll() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");// yyyy-MM-dd
        return simpleDateFormat.format(date);
    }

    public static String getDateBefore(int day) {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return formatMonth(now.get(Calendar.MONTH))
                + now.get(Calendar.DAY_OF_MONTH) + "日" + " "
                + formatWeek(now.get(Calendar.DAY_OF_WEEK));
        // Date outDate = now.getTime();
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//
        // 可以方便地修改日期格式
        // String hehe = dateFormat.format(outDate);
        // return hehe;
    }

    public static String getDateBefore2(int day) {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        Date outDate = now.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式
        String hehe = dateFormat.format(outDate);
        return hehe;
    }

    private static String formatWeek(int week) {
        switch (week) {
            case Calendar.SUNDAY:
                return "周日";
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            default:
                return "";
        }
    }

    private static String formatMonth(int month) {
        switch (month) {
            case Calendar.JANUARY:
                return "1月";
            case Calendar.FEBRUARY:
                return "2月";
            case Calendar.MARCH:
                return "3月";
            case Calendar.APRIL:
                return "4月";
            case Calendar.MAY:
                return "5月";
            case Calendar.JUNE:
                return "6月";
            case Calendar.JULY:
                return "7月";
            case Calendar.AUGUST:
                return "8月";
            case Calendar.SEPTEMBER:
                return "9月";
            case Calendar.OCTOBER:
                return "10月";
            case Calendar.NOVEMBER:
                return "11月";
            case Calendar.DECEMBER:
                return "12月";
            default:
                return "";
        }
    }

    public static int getDateDays(String date1, String date2,
                                  String format) {
        long betweenTime = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(date1);// 通过日期格式的parse()方法将字符串转换成日期
            Date dateBegin = sdf.parse(date2);
            betweenTime = date.getTime() - dateBegin.getTime();
            betweenTime = betweenTime / 1000 / 60 / 60 / 24;
        } catch (Exception e) {
            return -1;

        }
        return (int) betweenTime;
    }


    private static int getDateDays(Date date, Date dateBegin) {
        long betweenTime = 0;
        // SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            // Date date = sdf.parse(date1);// 通过日期格式的parse()方法将字符串转换成日期
            // Date dateBegin = sdf.parse(date2);
            betweenTime = date.getTime() - dateBegin.getTime();
            betweenTime = betweenTime / 1000 / 60 / 60 / 24;
        } catch (Exception e) {
        }
        return (int) betweenTime;
    }


    public static String changeDateFormat2(String time, String oldStr,
                                           String newStr, int[] b, String[] str) {
        if (null != b && null != str && b.length == str.length) {
            try {
                int len = b.length;
                int btween = Constants.getDateDays(Calendar.getInstance()
                        .getTime(), new SimpleDateFormat(oldStr).parse(time));
                for (int i = 0; i < len; i++) {
                    if (b[i] == btween) {
                        return str[i];
                    }
                }
            } catch (Exception e) {
            }
        }
        SimpleDateFormat oldsdf = new SimpleDateFormat(oldStr);
        SimpleDateFormat newsdf = new SimpleDateFormat(newStr);
        Date d = null;
        try {
            d = oldsdf.parse(time); // 将给定的字符串中的日期提取出来
        } catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace(); // 打印异常信息
            return time;
        }
        return newsdf.format(d);
    }

    public static long getDatetolong(String date, String format) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new SimpleDateFormat(format).parse(date));
            return c.getTimeInMillis();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }


    public static void toActivity(Activity activity, Class cl,
                                  Bundle bundle) {
        Intent intent = new Intent(activity, cl);
        if (null != bundle)
            intent.putExtras(bundle);
        activity.startActivity(intent);
//        activity.overridePendingTransition(R.anim.slide_right_in,
//                R.anim.slide_left_out);
    }

    public static void toActivity(Activity activity, Class cl,
                                  Bundle bundle, boolean finish) {
        Intent intent = new Intent(activity, cl);
        if (null != bundle)
            intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
        if (finish) {
            activity.finish();
        }
    }

    public static void toActivityForR(Activity activity, Class cl,
                                      Bundle bundle) {
        Intent intent = new Intent(activity, cl);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, 0);
        activity.overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public static void toActivityForR(Activity activity, Class cl,
                                      Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, cl);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }


    private static CustomDialog alertDialog;

    public static void updateVersion(final Context context, int mustUpdate, String message, final String link) {
        if (TextUtils.isEmpty(link)) {
            return;
        }
        if (mustUpdate == 0 || mustUpdate == 1) {
            message = message.replace("|", "\n");
            if (null == alertDialog) {
                showDialog(context, mustUpdate, message, link);
            } else {
                if (!alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    alertDialog.cancel();
                    alertDialog = null;
                    showDialog(context, mustUpdate, message, link);
                }
            }
        }
    }

    private static void showDialog(final Context context, int mustUpdate, String message, final String link) {

        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setAlertIcon(R.drawable.icon_update);
        builder.setGravityLeft(true);
        builder.setMessage(message);
        if (mustUpdate == 1 && context instanceof HomeActivity) {
            builder.setNeutralButton("好的", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    versionDownload(context, link);
                }
            });
        } else {
            builder.setNegativeButton(context.getResources().getColor(R.color.cor10), "稍后再说", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (context instanceof BaseAutoLayoutActivity) {
                        ((BaseAutoLayoutActivity) context).mRxManager.post(HomeActivity.RXEVENT_UPDATE_REMIND, true);
                    }

                }
            });
            builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    versionDownload(context, link);
                    if (context instanceof BaseAutoLayoutActivity) {
                        ((BaseAutoLayoutActivity) context).mRxManager.post(HomeActivity.RXEVENT_UPDATE_REMIND, false);
                    }
                }
            });
        }
        alertDialog = builder.create();
        if (mustUpdate == 1) {
            alertDialog.setCancelable(false);
        }
        alertDialog.show();
    }


    private static void versionDownload(final Context context, final String link) {
        RxPermissions.getInstance(context).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean grant) {
                        if (grant) {
                            try {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    Uri content_url = Uri.parse(link);
                                    intent.setData(content_url);
                                    intent.setClassName("com.android.browser",
                                            "com.android.browser.BrowserActivity");
                                    context.startActivity(intent);
                                } else {
                                    clearApk();
                                    DownloadManager downloadManager = (DownloadManager) context
                                            .getSystemService(Context.DOWNLOAD_SERVICE);
                                    Uri uri = Uri.parse(link);
                                    Request request = new Request(uri);
                                    request.setAllowedOverRoaming(false);
                                    //通知栏显示
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    // 设置允许使用的网络类型，这里是移动网络和wifi都可以
                                    request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
                                            | Request.NETWORK_WIFI);
                                    // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    // 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
                                    // request.setShowRunningNotification(false);
                                    // 不显示下载界面
                                    request.setTitle("云机械");
                                    request.setDescription("正在下载中...");
                                    request.setVisibleInDownloadsUi(true);
                                    //设置下载的路径
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Constants.APK_NAME);
                                    //设置下载的路径
                                    // 设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件
                                    // 在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个
                                    // 目录下面
                                    // request.setDestinationInExternalFilesDir(this, null,
                                    // "tar.apk");
                                    long id = downloadManager.enqueue(request);
                                    // TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
                                    MySharedPreferences
                                            .setSharedPLong(Constants.KEY_DownloadId, id);
                                }
                            } catch (Exception e) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(link);
                                intent.setData(content_url);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                context.startActivity(intent);
                            }
                        } else {
                            ToastUtils.showToast(context, "更新失败，需开启存储读写权限，请到设置->权限管理中开启");
                        }

                    }
                });
    }

    private static void clearApk() {
        File downloadDic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (downloadDic.exists()) {
            for (File f : downloadDic.listFiles()) {
                if (f.getName().contains("cloudm")) {
                    f.delete();
                }
            }
        }
    }


    public static boolean isOwner(int roleType) {
        return roleType == 1;
    }

    public static void gotoImageBrower(Activity activity,
                                       String position, int id) {
        // position 1:选中的预览 2:所有预览

        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("ID", id);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.zoomin, R.anim.zoomno);
    }

    public static void gotoImageBrower(Activity activity, int position,
                                       ArrayList<ImageItem> dataList, boolean isDelete) {
        try {
            Intent intent = new Intent(activity, ImagePagerActivity.class);
            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_ITEM, dataList);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
            intent.putExtra(Constants.P_IMAGEBROWERDELETE, isDelete);
            activity.startActivityForResult(intent,
                    REQUEST_ImageActivity);
            // activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.zoomin, R.anim.zoomno);
        } catch (Exception e) {
        }

    }


    public static boolean isMobileNO(String mobiles) {

		/*
         * Pattern p = Pattern
		 * .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"); Matcher m =
		 * p.matcher(mobiles); return m.matches();
		 */
        String telRegex = "[1]\\d{10}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);

    }


    public static String toViewString(String str) {
        return null != str ? str : "";
    }

    public static final String FILE_PROVIDER="com.cloudmachine.fileprovider";
    public static final String CUSTOMER_PHONE_BOX = "400-008-0581";
    public static final String CUSTOMER_PHONE_REPAIR = "400-816-9911";
    public static final String H5_URL = "H5_url";
    public static final String MC_DEVICEID = "McDeviceId";
    public static final String MC_MEMBER = "Member";
    public static final String MEMBER_ID = "memberId";
    public static final int RESULT_QUERY_BY_TIME = 0x34;
    public static final int INVALID_DEVICE_ID = -1;
    public static final String UPDATE_DEVICE_LIST = "updateDeviceList";
    public static final String UPDATE_DEVICE_NAME = "updateDeviceName";
    public static final String PAGET_TYPE = "page_type";
    public static final String SN_ID = "snid";
    public static final String VIDEO_ID = "video_id";
    public static final String IS_ONLINE = "is_online";
    public static final String IS_VIDEO = "is_video";
    public static final String IMEI = "imei";
    public static final String OPERATOR_LIST = "operator_list";
    public static final String NAME = "name";
    public static final String MOBILE = "mobie";
    public static final String ENUM_ITEM = "enum_item";
    public static final String RELATION_POSITION = "relation_position";
    public static final String DEVICE_NAME = "device_name";
    public static final String UNIQUEID = "uniqueId";
    public static final String REAL_NAME = "real_name";
    public static final String RESULT = "result";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_ID = "id";
    public static final String IS_OWNER = "is_owner";
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_DETAIL = "device_detail";
    public static final String ANDROID = "Android";
    public static final String REPAIR_DETAIL = "repair_detail";
    public static final String ORDER_NO = "order_no";
    public static final String MACHINE_TYPE = "machineType";
    public static final String RELATION_CODE = "relation_code";
    public static final String APP_NAME = "appName";

    public static void callJsMethod(WebView webView, String jsParams) {
        AppLog.print("call jsMethod__" + jsParams);
        webView.loadUrl("javascript:" + jsParams);
    }

    public static final String CURRENT_LOC = "当前位置";
    public static final String APK_NAME = "cloudm.apk";

    public interface IPageType {
        String PAGE_DEVICE_INFO = "p_device_info";//设备基本信息
    }

    public interface PermissionType {
        int CAMERA = 1;
        int STORAGE = 2;
        int LOCATION = 3;
        int STORAGE_CAMERA = 4;
        int ADDRESS_BOOK = 5;
        int LOCATION_STORAGE=6;
    }

    public static final String[] PERMISSIONS_CAMER_SD = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


    public static boolean checkToken(BaseRespose respose) {
        if (respose.getCode() == -10086) {
            exitAccount();
            MyApplication.getInstance().startActivity(new Intent(MyApplication.getInstance(), LoginActivity.class));
            return true;
        }
        return false;
    }

    public static void exitAccount(){
        MobclickAgent.onEvent(MyApplication.getInstance(), UMengKey.count_logout);
        JPushInterface.setAliasAndTags(MyApplication.getInstance(), "", null, null);
        MemeberKeeper.clearOauth(MyApplication.getInstance());
        WebStorage.getInstance().deleteAllData();
    }

}
