package com.cloudmachine.recycleadapter.delegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudmachine.R;
import com.cloudmachine.activities.WebviewActivity;
import com.cloudmachine.api.ApiConstants;
import com.cloudmachine.recyclerbean.HomeIssueDetailBean;
import com.cloudmachine.recyclerbean.HomePageType;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/27 上午9:56
 * 修改人：shixionglu
 * 修改时间：2017/3/27 上午9:56
 * 修改备注：
 */

public class HomeIssueDetailDelegate implements ItemViewDelegate<HomePageType>{

    private Context mContext;
    private CircleImageView mIssueIcon;
    private HomeIssueDetailBean mHomeIssueDetailBean;
    private TextView mNickName;
    private TextView mTvBrand;
    private TextView mTvModel;
    private TextView mTvTime;
    private TextView mTvIssueDesc;
    private TextView mTvAskCount;
    private TextView mTvState;
    private LinearLayout mLlHotIssue;
    private int mId;
    private Long mMyId;

    @Override
    public int getItemViewLayoutId() {
        return R.layout.delegate_home_issuedetail;
    }

    @Override
    public boolean isForViewType(HomePageType item, int position) {
        return item instanceof HomeIssueDetailBean;
    }

    @Override
    public void convert(ViewHolder holder, HomePageType homePageType, int position) {
        mContext = holder.getConvertView().getContext();
        mHomeIssueDetailBean = (HomeIssueDetailBean) homePageType;
        Constants.MyLog(mHomeIssueDetailBean.toString());
        mId = mHomeIssueDetailBean.id;
        if (MemeberKeeper.getOauth(mContext) != null) {
            mMyId = MemeberKeeper.getOauth(mContext).getWjdsId();
        }
        //头像
        mIssueIcon = (CircleImageView) holder.getView(R.id.issue_icon);
        if (mHomeIssueDetailBean.askerLogo != null) {
            Glide.with(mContext).load(mHomeIssueDetailBean.askerLogo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .error(R.drawable.default_img)
                    .into(mIssueIcon);
        } else {
            Glide.with(mContext).load(R.drawable.master_news)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .into(mIssueIcon);
        }
        //用户昵称
        mNickName = (TextView) holder.getView(R.id.tv_nick_name);
        if (mHomeIssueDetailBean.askerName != null) {
            mNickName.setText(mHomeIssueDetailBean.askerName);
        }
        //机器品牌
        mTvBrand = (TextView) holder.getView(R.id.tv_brand);
        if (mHomeIssueDetailBean.askerBrand != null) {
            mTvBrand.setText(mHomeIssueDetailBean.askerBrand);
        }
        //机器型号
        mTvModel = (TextView) holder.getView(R.id.tv_model);
        if (mHomeIssueDetailBean.askerModel != null) {
            mTvBrand.setText(mHomeIssueDetailBean.askerModel);
        }
        //提问时间
        mTvTime = (TextView) holder.getView(R.id.tv_time);
        if (mHomeIssueDetailBean.askTime != null) {
            mTvTime.setText(mHomeIssueDetailBean.askTime);
        }
        //问题描述
        mTvIssueDesc = (TextView) holder.getView(R.id.tv_issue_desc);
        if (mHomeIssueDetailBean.askDesc != null) {
            mTvIssueDesc.setText(mHomeIssueDetailBean.askDesc);
        }
        //回答问题数量
        mTvAskCount = (TextView) holder.getView(R.id.tv_ask_count);
        mTvAskCount.setText(mHomeIssueDetailBean.answerCount+"条回答");
        //问题状态
        mTvState = (TextView) holder.getView(R.id.state);
        if (mHomeIssueDetailBean.state == 0) {
            mTvState.setText("待解决");
        } else if (mHomeIssueDetailBean.state == 1) {
            mTvState.setText("已解决");
        }

        mLlHotIssue = (LinearLayout) holder.getView(R.id.ll_hot_issue);
        mLlHotIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mHomeIssueDetailBean.url != null) {
                    Intent intent = new Intent(mContext,WebviewActivity.class);
                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.P_WebView_Url,
//                            "http://h5.test.cloudm.com/n/ask_qdetail?qid="+mId+"&myid="+mMyId);
                bundle.putString(Constants.P_WebView_Url,
                        ApiConstants.H5_HOST+"n/ask_qdetail?qid="+mId+"&myid="+mMyId);
                    bundle.putBoolean(Constants.P_WebView_With_Title,false);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
//                }
            }
        });
    }
}
