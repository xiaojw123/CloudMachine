package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.Constants;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2016/12/5 下午3:23
 * 修改人：shixionglu
 * 修改时间：2016/12/5 下午3:23
 * 修改备注：
 */

public class UseHelpActivity extends BaseAutoLayoutActivity implements View.OnClickListener {

    private TitleView mTitleLayout;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usehelp);
        mContext = this;
        initTitleLayout();
        initView();
    }

    @Override
    public void initPresenter() {

    }

    private void initView() {

        RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.rl1);
        rl1.setOnClickListener(this);
        RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.rl2);
        rl2.setOnClickListener(this);
        RelativeLayout rl3 = (RelativeLayout) findViewById(R.id.rl3);
        rl3.setOnClickListener(this);
        RelativeLayout rl4 = (RelativeLayout) findViewById(R.id.rl4);
        rl4.setOnClickListener(this);
        RelativeLayout rl5 = (RelativeLayout) findViewById(R.id.rl5);
        rl5.setOnClickListener(this);
        RelativeLayout rl6 = (RelativeLayout) findViewById(R.id.rl6);
        rl6.setOnClickListener(this);
        RelativeLayout rl7 = (RelativeLayout) findViewById(R.id.rl7);
        rl7.setOnClickListener(this);
        RelativeLayout rl8 = (RelativeLayout) findViewById(R.id.rl8);
        rl8.setOnClickListener(this);
        RelativeLayout rl9 = (RelativeLayout) findViewById(R.id.rl9);
        rl9.setOnClickListener(this);
        RelativeLayout rl10 = (RelativeLayout) findViewById(R.id.rl10);
        rl10.setOnClickListener(this);
    }

    private void initTitleLayout() {
        mTitleLayout = (TitleView) findViewById(R.id.title_layout);
        mTitleLayout.setTitle("使用帮助");
        /*mTitleLayout.setLeftText(-1, "返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        mTitleLayout.setLeftImage(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl1:
                Intent rl1 = new Intent(mContext, WebviewActivity.class);
                rl1.putExtra(Constants.P_WebView_Url, "http://mp.weixin.qq.com/s/vTg43AZmU-0ddM2BdqvKQg?scene=21#wechat_redirect");
                startActivity(rl1);
                break;
            case R.id.rl2:
                Intent rl2 = new Intent(mContext, WebviewActivity.class);
                rl2.putExtra(Constants.P_WebView_Url, "http://mp.weixin.qq.com/s/S3FaCKmjDTb249ECvzh3FA?scene=21#wechat_redirect");
                startActivity(rl2);
                break;
            case R.id.rl3:
                break;
            case R.id.rl4:
                Intent rl4 = new Intent(mContext, WebviewActivity.class);
                rl4.putExtra(Constants.P_WebView_Url, "http://mp.weixin.qq.com/s/f6yVTzYkOxYoGsQY4pjLAg?scene=21#wechat_redirect");
                startActivity(rl4);
                break;
            case R.id.rl5:
                Intent rl5 = new Intent(mContext, WebviewActivity.class);
                rl5.putExtra(Constants.P_WebView_Url, "http://mp.weixin.qq.com/s/pi_tfMzqXI0MO4j5Cd-5lg?scene=21#wechat_redirect");
                startActivity(rl5);
                break;
            case R.id.rl6:
                Intent rl6 = new Intent(mContext, WebviewActivity.class);
                rl6.putExtra(Constants.P_WebView_Url, "http://mp.weixin.qq.com/s/BUigCGBU8BE_pen8PHIEeQ?scene=21#wechat_redirect");
                startActivity(rl6);
                break;
            case R.id.rl7:
                Intent rl7 = new Intent(mContext, WebviewActivity.class);
                rl7.putExtra(Constants.P_WebView_Url, "http://mp.weixin.qq.com/s/Q5sOaBJ25Vcq3BSr3nzkaQ?scene=21#wechat_redirect");
                startActivity(rl7);
                break;
            case R.id.rl8:
                Intent rl8 = new Intent(mContext, WebviewActivity.class);
                rl8.putExtra(Constants.P_WebView_Url, "http://mp.weixin.qq.com/s/8AnjQVuDRij0hyH1dQELXw?scene=21#wechat_redirect");
                startActivity(rl8);
                break;
            case R.id.rl9:
                Intent rl9 = new Intent(mContext, WebviewActivity.class);
                rl9.putExtra(Constants.P_WebView_Url, "http://mp.weixin.qq.com/s/TdyOlxAS8wzNW9RI-dm6Hw?scene=21#wechat_redirect");
                startActivity(rl9);
                break;
            case R.id.rl10:
                Intent rl10 = new Intent(mContext, WebviewActivity.class);
                rl10.putExtra(Constants.P_WebView_Url, "http://mp.weixin.qq.com/s/G12eAyxW33-StO2ijKFNLg");
                startActivity(rl10);
                break;
            default:
                break;
        }
    }
}
