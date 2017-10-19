package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.CWInfo;
import com.cloudmachine.bean.WorkSettleBean;
import com.cloudmachine.bean.WorkcollarListBean;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.utils.Constants;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class ConsumptionActivity extends BaseAutoLayoutActivity {
    String totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption);
        MobclickAgent.onEvent(this, MobEvent.REPAIR_MATERIAL);
        CWInfo info = (CWInfo) getIntent().getSerializableExtra(Constants.CWINFO);
        LinearLayout consumptionLLt = (LinearLayout) findViewById(R.id.consumption_container_llt);
        if (info == null) {
            consumptionLLt.addView(getEmptyView());
        } else {
            WorkSettleBean bean = info.getWorkSettle();
            if (bean != null) {
                totalPrice = bean.getNpartstotalamount();
            }
            List<WorkcollarListBean> collarList = info.getWorkcollarList();
            for (WorkcollarListBean item : collarList) {
                consumptionLLt.addView(getItemView(item));
                consumptionLLt.addView(getLineView());
            }
            consumptionLLt.addView(getTotalItemView());
        }

    }

    @Override
    public void initPresenter() {

    }

    private FrameLayout getTotalItemView() {
        FrameLayout itemView = getContainer();
        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.gravity = Gravity.CENTER_VERTICAL;
        TextView titleTv = new TextView(this);
        titleTv.setLayoutParams(params1);
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        titleTv.setTextColor(getResources().getColor(R.color.cor9));
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        titleTv.setText("合计");
        TextView priceTv = new TextView(this);
        priceTv.setLayoutParams(params2);
        priceTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        priceTv.setTextColor(getResources().getColor(R.color.cor9));
        priceTv.setText("¥" + totalPrice);
        itemView.addView(titleTv);
        itemView.addView(priceTv);
        return itemView;

    }


    private FrameLayout getItemView(WorkcollarListBean item) {
        FrameLayout itemView = getContainer();
        TextView titleTv = new TextView(this);
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        titleTv.setTextColor(getResources().getColor(R.color.cor9));
        FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER_VERTICAL;
        titleTv.setLayoutParams(titleParams);
        double qiy = Double.parseDouble(item.getUse_QTY());
        double unitPrice = Double.parseDouble(item.getUnit_PRICE());
        int qiyInt = (int) qiy;
        double price = unitPrice * qiyInt;
        titleTv.setText(item.getPart_NAME() + "      x" + qiyInt);
        TextView priceTv = new TextView(this);
        FrameLayout.LayoutParams priceParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        priceParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        priceTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        priceTv.setTextColor(getResources().getColor(R.color.cor9));

        priceTv.setLayoutParams(priceParams);
        priceTv.setText("¥" + price);
        itemView.addView(titleTv);
        itemView.addView(priceTv);
        return itemView;
    }

    @NonNull
    private View getLineView() {
        FrameLayout.LayoutParams lineParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dimen_size_1));
        lineParams.gravity = Gravity.BOTTOM;
        View view = new View(this);
        view.setLayoutParams(lineParams);
        view.setBackgroundColor(getResources().getColor(R.color.cor12));
        return view;
    }

    @NonNull
    private FrameLayout getContainer() {
        int padL = (int) getResources().getDimension(R.dimen.pad2);
        FrameLayout itemView = new FrameLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dimen_size_60));
        itemView.setLayoutParams(params);
        itemView.setBackgroundColor(getResources().getColor(R.color.white));
        itemView.setPadding(padL, 0, padL, 0);
        return itemView;
    }

    private View getEmptyView() {
        TextView emptv = new TextView(this);
        emptv.setText("无相关数据");
        emptv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        emptv.setLayoutParams(params);
        return emptv;
    }

}
