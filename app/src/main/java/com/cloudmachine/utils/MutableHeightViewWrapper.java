package com.cloudmachine.utils;

import android.util.Log;
import android.view.View;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/28 上午12:50
 * 修改人：shixionglu
 * 修改时间：2017/2/28 上午12:50
 * 修改备注：
 */

public class MutableHeightViewWrapper {

    private View view;
    public MutableHeightViewWrapper(View view){
        this.view = view;
    }

    public void setHeight(int height) {
        Log.i("height","MutableHeightViewWrapper set = "+view.getLayoutParams().height);
        view.getLayoutParams().height = height;
        view.requestLayout();
    }

    public int getHeight() {
        Log.i("height","MutableHeightViewWrapper get = "+view.getLayoutParams().height);
        return view.getLayoutParams().height;

    }
}