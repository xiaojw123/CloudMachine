package com.cloudmachine.ui.repair.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.activities.WebviewActivity;
import com.cloudmachine.api.ApiConstants;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.struc.BOInfo;
import com.cloudmachine.struc.CWInfo;
import com.cloudmachine.struc.WorkDetailBean;
import com.cloudmachine.struc.WorkSettleBean;
import com.cloudmachine.struc.WorkcollarListBean;
import com.cloudmachine.ui.repair.contract.RepairFinishContract;
import com.cloudmachine.ui.repair.model.RepairFinishModel;
import com.cloudmachine.ui.repair.presenter.RepairFinishPresenter;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepairFinishDetailActivity extends BaseAutoLayoutActivity<RepairFinishPresenter, RepairFinishModel> implements RepairFinishContract.View, View.OnClickListener {
    String orderNum;
    @BindView(R.id.order_detail_item_container1)
    LinearLayout orderDetailItemContainer1;
    @BindView(R.id.order_detail_item_container2)
    LinearLayout orderDetailItemContainer2;
    @BindView(R.id.order_detail_item_container3)
    LinearLayout orderDetailItemContainer3;
    @BindView(R.id.order_detail_item_container2_title)
    TextView itemContainer2titleTv;
    int common_text_size=16;
    int common_padding=25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reparir_finsih_detail);
        ButterKnife.bind(this);
        String orderNum = getIntent().getStringExtra("orderNum");
        String flag = getIntent().getStringExtra("flag");
        mPresenter.updateRepairFinishDetail(orderNum, flag);
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void returnDetailView(BOInfo boInfo) {
    }

    @Override
    public void returnDetailView(CWInfo boInfo) {
        WorkDetailBean workDetail = boInfo.getWorkDetail();
        WorkSettleBean workSettle = boInfo.getWorkSettle();
        orderDetailItemContainer1.addView(getItemContainer1("机械品牌", workDetail.getVbrandname()));
        orderDetailItemContainer1.addView(getItemContainer1("机械型号", workDetail.getVmaterialname()));
        ArrayList<WorkcollarListBean> workcollars = boInfo.getWorkcollarList();
        if (workcollars != null && workcollars.size() > 0) {
            itemContainer2titleTv.setVisibility(View.VISIBLE);
            orderDetailItemContainer2.setVisibility(View.VISIBLE);
            for (WorkcollarListBean collarDetail : workcollars) {
                double qty = getDoubleFromString(collarDetail.getUse_QTY());
                String price = CommonUtils.formartPrice(String.valueOf(getDoubleFromString(collarDetail.getUnit_PRICE()) * qty));
                orderDetailItemContainer2.addView(getItemContainer2(collarDetail.getPart_NAME() + "x" + qty, "¥ " + price));
            }
        }
        orderDetailItemContainer3.addView(getItemContainer1("耗件总费用", "¥ " + CommonUtils.formartPrice(workSettle.getNpartstotalamount())));
        orderDetailItemContainer3.addView(getItemContainer1("工时费用", "¥ " + CommonUtils.formartPrice(workSettle.getNrepairworkhourcost())));
        orderDetailItemContainer3.addView(getItemContainer1("耗件折扣", "¥ " + CommonUtils.formartPrice(workSettle.getNdiscounttotalamount())));
        orderDetailItemContainer3.addView(getItemContainer3("优惠券", "¥ " + CommonUtils.formartPrice(workSettle.getNmaxamount()),tintDrawable(getResources().getDrawable(R.drawable.coupon_description_selector),ColorStateList.valueOf(getResources().getColor(R.color.public_blue_bg)))));
        orderDetailItemContainer3.addView(getItemContainer1("合计", "¥ " + CommonUtils.formartPrice(workSettle.getNreceiveamount())));
    }

    @NonNull
    private FrameLayout getItemContainer2(String key, String value) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout cotainer = new FrameLayout(this);
        cotainer.setLayoutParams(params);
        FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER_VERTICAL;
        TextView title_tv = new TextView(this);
        title_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, common_text_size);
        title_tv.setTextColor(getResources().getColor(R.color.c_5));
        title_tv.setLayoutParams(titleParams);
        title_tv.setText(key);
        FrameLayout.LayoutParams valueParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        valueParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        TextView value_tv = new TextView(this);
        value_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, common_text_size);
        value_tv.setTextColor(getResources().getColor(R.color.c_5));
        value_tv.setLayoutParams(valueParams);
        value_tv.setText(value);
        cotainer.setPadding(0,common_padding,0,common_padding);
        cotainer.addView(title_tv);
        cotainer.addView(value_tv);
        return cotainer;
    }

    private double getDoubleFromString(String value) {
        double intVaue = 0;
        if (!TextUtils.isEmpty(value)) {
            intVaue = Double.parseDouble(value);
        }
        return intVaue;
    }


    @NonNull
    private FrameLayout getItemContainer1(String key, String value) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout cotainer = new FrameLayout(this);
        cotainer.setLayoutParams(params);
        FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER_VERTICAL;
        TextView title_tv = new TextView(this);
        title_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, common_text_size);
        title_tv.setTextColor(getResources().getColor(R.color.black));
        title_tv.setLayoutParams(titleParams);
        title_tv.setText(key);
        FrameLayout.LayoutParams valueParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        valueParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        TextView value_tv = new TextView(this);
        value_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, common_text_size);
        value_tv.setTextColor(getResources().getColor(R.color.c_5));
        value_tv.setLayoutParams(valueParams);
        value_tv.setText(value);
        cotainer.setPadding(0,common_padding,0,common_padding);
        cotainer.addView(title_tv);
        cotainer.addView(value_tv);
        return cotainer;
    }

    @NonNull
    private FrameLayout getItemContainer3(String key, String value,Drawable drawable) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout cotainer = new FrameLayout(this);
        cotainer.setLayoutParams(params);
        FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER_VERTICAL;
        TextView title_tv = new TextView(this);
        title_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, common_text_size);
        title_tv.setTextColor(getResources().getColor(R.color.black));
        title_tv.setLayoutParams(titleParams);
        title_tv.setText(key);
        FrameLayout.LayoutParams valueParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        valueParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        TextView value_tv = new TextView(this);
        value_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, common_text_size);
        value_tv.setTextColor(getResources().getColor(R.color.c_5));
        value_tv.setLayoutParams(valueParams);
        value_tv.setText(value);
        cotainer.setPadding(0,common_padding,0,common_padding);
        cotainer.addView(title_tv);
        ImageView img=new ImageView(this);
        FrameLayout.LayoutParams imgParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgParams.gravity=Gravity.CENTER_VERTICAL;
        imgParams.leftMargin=180;
        img.setLayoutParams(imgParams);
        img.setImageDrawable(drawable);
        img.setOnClickListener(this);
        cotainer.addView(img);
        cotainer.addView(value_tv);
        return cotainer;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, WebviewActivity.class);
        intent.putExtra(Constants.P_WebView_Title,"说明");
        intent.putExtra(Constants.P_WebView_Url, ApiConstants.H5_HOST+"n/coupon_description");
        startActivity(intent);
    }

    //改变drwable颜色
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

}
