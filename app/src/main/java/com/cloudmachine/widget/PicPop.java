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
import com.cloudmachine.ui.home.contract.ExtrContract;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.PhotosGallery;

/**
 * Created by xiaojw on 2018/6/26.
 */

public class PicPop extends PopupWindow implements View.OnClickListener{
    public static final int TYPE_CAMERA=0x11;
    public static final int TYPE_PICK=0x12;
    private OnPopUpdateListener mListener;
    Button pickBtn;
    View lineView;
    public PicPop(Context context){
        super(context);
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
        pickBtn= (Button) contentView.findViewById(R.id.pop_pick_btn);
        lineView=contentView.findViewById(R.id.pop_pick_line);
        Button cancelBtn= (Button) contentView.findViewById(R.id.pop_cancel_btn);
        contentView.setOnClickListener(this);
        shootBtn.setOnClickListener(this);
        pickBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }
    public void setOnPopUpdateListener(OnPopUpdateListener listener){
        mListener=listener;
    }

    public void setPickEnable(boolean enable){
        if (enable){
            lineView.setVisibility(View.VISIBLE);
            pickBtn.setVisibility(View.VISIBLE);
        }else{
            lineView.setVisibility(View.GONE);
            pickBtn.setVisibility(View.GONE);
        }
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
                if (mListener!=null){
                    mListener.updateGuideView(TYPE_CAMERA);
                }

                break;
            case R.id.pop_pick_btn:
                dismiss();
                if (mListener!=null){
                    mListener.updateGuideView(TYPE_PICK);
                }
                break;
        }



    }

    public interface OnPopUpdateListener{

        void updateGuideView(int actionType);


    }

}
