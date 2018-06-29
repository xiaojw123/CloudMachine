package com.cloudmachine.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.ReplacementTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.cloudmachine.R;
import com.cloudmachine.camera.CameraActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.PhotosGallery;

/**
 * Created by xiaojw on 2018/6/26.
 */

public class PicPop extends PopupWindow implements View.OnClickListener{
    public static final int REQUEST_PICK_IMAGE=0x16;
    private Context mContext;
    private String mType;
    public PicPop(Context context){
        super(context);
        mContext=context;
        View contentView= LayoutInflater.from(context).inflate(R.layout.pop_pic_layout,null);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setAnimationStyle(R.style.PopAnimationStyle);
        setFocusable(true);
        setOutsideTouchable(false);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        Button shootBtn= (Button) contentView.findViewById(R.id.pop_shoot_btn);
        Button pickBtn= (Button) contentView.findViewById(R.id.pop_pick_btn);
        Button cancelBtn= (Button) contentView.findViewById(R.id.pop_cancel_btn);
        contentView.setOnClickListener(this);
        shootBtn.setOnClickListener(this);
        pickBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }
    public void setPicType(String type){
        mType=type;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pop_pic_container:
            case R.id.pop_cancel_btn:
                dismiss();
                break;
            case  R.id.pop_shoot_btn:
                dismiss();
                Bundle bundle = new Bundle();
                bundle.putString(CameraActivity.PIC_TYPE, mType);
                Constants.toActivityForR((Activity) mContext, CameraActivity.class, bundle, CameraActivity.REQUEST_CODE);
                break;
            case R.id.pop_pick_btn:
                ((Activity)mContext).startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                        REQUEST_PICK_IMAGE);
                dismiss();
                break;
        }



    }

}
