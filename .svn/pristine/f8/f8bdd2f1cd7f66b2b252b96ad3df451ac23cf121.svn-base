package com.cloudmachine.listener;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


public abstract class OnClickEffectiveListener implements OnClickListener
{
    private long oldOnClickTime = 0;
    private long newOnClickTime = 0;
public void onClick(View v)
{
    newOnClickTime = System.currentTimeMillis();
    if(newOnClickTime - oldOnClickTime >1000){
        onClickEffective(v);
    }else{
        Log.e("Test", "无效点击");
    }
    oldOnClickTime = newOnClickTime;
}
public abstract void onClickEffective(View v);
}