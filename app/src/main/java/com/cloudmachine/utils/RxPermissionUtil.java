package com.cloudmachine.utils;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by xiaojw on 2017/9/8.
 */

public class RxPermissionUtil {

    Activity mActivity;
    Fragment mFragment;
    private RxPermissionUtil(Activity activity) {
        mActivity = activity;
    }
    private RxPermissionUtil(Fragment fragment) {
        mFragment = fragment;
    }





}
