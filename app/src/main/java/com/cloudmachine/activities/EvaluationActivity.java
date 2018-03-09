package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.CommentsInfo;
import com.cloudmachine.bean.DisadvantageBean;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.net.task.GetEvaluateInfoTask;
import com.cloudmachine.net.task.GetTagInfoAsync;
import com.cloudmachine.net.task.SubmitCommentAsync;
import com.cloudmachine.ui.home.model.EvaluateInfoBean;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.tagview.TagBaseAdapter;
import com.cloudmachine.utils.widgets.tagview.TagCloudLayout;
import com.cloudmachine.utils.widgets.tagview.TagCloudLayout.TagItemClickListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 评价页面
 *
 * @author shixionglu
 */

public class EvaluationActivity extends BaseAutoLayoutActivity implements
        Callback {
    public static final String ACTION_TYPE = "action_type";
    private Context mContext;
    private Handler mHandler;
    private List<DisadvantageBean> advantageBeans = new ArrayList<>();// 优点集合
    private List<DisadvantageBean> disadvantageBeans = new ArrayList<>();// 建议集合
    private TagBaseAdapter advantageAdapter;// 优点适配器
    private TagBaseAdapter disAdvantageAdapter;// 建议适配器
    private TagCloudLayout advantage;
    private TagCloudLayout disadvantage;
    private RadiusButtonView submit;
    private String css_work_no;
    private boolean isAlliance;
    private String cust_telString;
    private int actionType;
    private LinearLayout editLayout;
    private LinearLayout adItemLayout, disAdItemLayout;
    private EditText sbjEdt;
    private TextView tvDesc;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        mContext = this;
        mHandler = new Handler(this);
        getIntentData();
        tvDesc=(TextView) findViewById(R.id.tv_desc);
        sbjEdt = (EditText) findViewById(R.id.comment_my_sbj);
        adItemLayout = (LinearLayout) findViewById(R.id.advantage_item_layout);

        disAdItemLayout = (LinearLayout) findViewById(R.id.disadvantage_item_layout);
        editLayout = (LinearLayout) findViewById(R.id.et_other_ad);
        advantage = (TagCloudLayout) findViewById(R.id.advantage);
        disadvantage = (TagCloudLayout) findViewById(R.id.disadvantage);
        ratingBar = (RatingBar) findViewById(R.id.rb_rating_bar);// 星星
        submit = (RadiusButtonView) findViewById(R.id.login_btn);
        advantageAdapter = new TagBaseAdapter(mContext, advantageBeans);
        disAdvantageAdapter = new TagBaseAdapter(mContext, disadvantageBeans);
        advantage.setAdapter(advantageAdapter);
        disadvantage.setAdapter(disAdvantageAdapter);
        if (actionType == 1) {
            tvDesc.setText(getResources().getString(R.string.her_rate));
            //获取工单评价信息
            ratingBar.setIsIndicator(true);
            submit.setVisibility(View.GONE);
            sbjEdt.setEnabled(false);
            new GetEvaluateInfoTask(mHandler, css_work_no,isAlliance).execute();
        } else {
            sbjEdt.setEnabled(true);
            tvDesc.setText(getResources().getString(R.string.rating_bar));
            submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    StringBuffer sbAdvantage = new StringBuffer();
                    StringBuffer sb1 = new StringBuffer();
                    StringBuffer sb2 = new StringBuffer();

                    for (int i = 0; i < advantageBeans.size(); i++) {
                        DisadvantageBean bean = advantageBeans.get(i);
                        if (bean.isChecked()) {
                            sb1.append(bean.getCode())
                                    .append("_");
                        }
                    }
                    for (int j = 0; j < disadvantageBeans.size(); j++) {
                        DisadvantageBean bean = disadvantageBeans.get(j);
                        if (bean.isChecked()) {
                            sb2.append(bean.getCode()).append("_");
                        }

                    }
                    if (sb1.length() > 0) { //T_A_   D_
                        if (sb2.length() > 0) {
                            sb2.deleteCharAt(sb2.length() - 1);
                            sbAdvantage.append(sb1).append(sb2);
                        } else {
                            sb1.deleteCharAt(sb1.length() - 1);
                            sbAdvantage.append(sb1);
                        }
                    } else {
                        if (sb2.length() > 0) {
                            sb2.deleteCharAt(sb2.length() - 1);
                            sbAdvantage.append(sb2);
                        }
                    }
                    AppLog.print("evalutTag___" + sbAdvantage.toString());
                    if (TextUtils.isEmpty(css_work_no)) {
                        Constants.ToastAction("工单id不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(cust_telString)) {
                        Constants.ToastAction("评价人电话不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(sbAdvantage.toString())) {
                        Constants.ToastAction("评价标签不能为空");
                        return;
                    }
                    int rating = (int) ratingBar.getRating();// 满意度评分
                    if (rating<1||rating>5){
                        ToastUtils.showToast(EvaluationActivity.this,"评分区间【1-5】");
                        return;
                    }
                    new SubmitCommentAsync(mContext, mHandler, css_work_no, String
                            .valueOf(rating), cust_telString, sbAdvantage
                            .toString(), sbjEdt.getText().toString(),isAlliance).execute();
                }
            });

            new GetTagInfoAsync(mHandler,isAlliance).execute();

        }


    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        //MobclickAgent.onPageStart(UMengKey.time_repair_comment);
        super.onResume();
        MobclickAgent.onEvent(EvaluationActivity.this,MobEvent.TIME_REPAIR_COMMENT);
    }

    @Override
    protected void onPause() {
        //MobclickAgent.onPageEnd(UMengKey.time_repair_comment);
        super.onPause();
    }

    private void getIntentData() {
        Intent intent = this.getIntent();
        isAlliance=intent.getBooleanExtra("isAlliance",false);
        css_work_no = intent.getStringExtra("orderNum");// 工单id
        cust_telString = intent.getStringExtra("tel");// 评价人电话
        actionType = intent.getIntExtra(ACTION_TYPE, 0);

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.HANDLER_GET_EVALUATE_INFO_SUCCESS:
                if (null != msg.obj) {
                    advantage.setItemEnable(false);
                    disadvantage.setItemEnable(false);
                    EvaluateInfoBean bean = (EvaluateInfoBean) msg.obj;
                    ratingBar.setRating(bean.getSatisfaction());
                    updateData(bean.getAdvantage(), bean.getDisadvantage());
                    String suggestion=bean.getSuggestion();
                    if (TextUtils.isEmpty(suggestion)){
                        editLayout.setVisibility(View.GONE);
                    }else{
                        sbjEdt.setText(suggestion);
                    }
                }
                break;
            case Constants.HANDLER_GET_EVALUATE_INFO_FAILED:

                break;

            case Constants.HANDLER_GETTAGINFO_SUCCESS:
                if (null != msg.obj) {

                    CommentsInfo commentsInfo = (CommentsInfo) msg.obj;
                    ArrayList<DisadvantageBean> advaList = commentsInfo
                            .getAdvantage();
                    ArrayList<DisadvantageBean> disAdvaList = commentsInfo
                            .getDisadvantage();
                    updateData(advaList, disAdvaList);
                }

                break;

            case Constants.HANDLER_SUBMITCOMMENT_SUCCESS:
                Constants.ToastAction(String.valueOf(msg.obj));
                finish();
            case Constants.HANDLER_SUBMITCOMMENT_FAILD:
                Constants.ToastAction(String.valueOf(msg.obj));
            default:
                break;
        }
        return false;
    }

    private void updateData(List<DisadvantageBean> advaList, List<DisadvantageBean> disAdvaList) {
        advantageBeans.clear();
        disadvantageBeans.clear();
        advantageBeans.addAll(advaList);
        disadvantageBeans.addAll(disAdvaList);
        if (advantageBeans.size() > 0) {
            adItemLayout.setVisibility(View.VISIBLE);
            advantage.setVisibility(View.VISIBLE);
            advantage.setItemClickListener(new TagItemClickListener() {

                @Override
                public void itemClick(int position) {
                    advantageBeans.get(position).setChecked(
                            !advantageBeans.get(position).isChecked());

                    advantageAdapter.notifyDataSetChanged();
                }
            });
            advantageAdapter.notifyDataSetChanged();
        }
        if (disadvantageBeans.size() > 0) {
            disAdItemLayout.setVisibility(View.VISIBLE);
            disadvantage.setVisibility(View.VISIBLE);
            disadvantage.setItemClickListener(new TagItemClickListener() {

                @Override
                public void itemClick(int position) {
                    disadvantageBeans.get(position).setChecked(
                            !disadvantageBeans.get(position).isChecked());
                    disAdvantageAdapter.notifyDataSetChanged();
                }
            });

            disAdvantageAdapter.notifyDataSetChanged();
        }
    }

}
