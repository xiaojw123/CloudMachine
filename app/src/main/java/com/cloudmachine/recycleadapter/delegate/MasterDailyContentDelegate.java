package com.cloudmachine.recycleadapter.delegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudmachine.R;
import com.cloudmachine.activities.WebviewActivity;
import com.cloudmachine.recyclerbean.MasterDailyBean;
import com.cloudmachine.recyclerbean.MasterDailyType;
import com.cloudmachine.utils.Constants;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 项目名称：CloudMachine
 * 类描述：大师日报正文
 * 创建人：shixionglu
 * 创建时间：2017/4/7 上午9:19
 * 修改人：shixionglu
 * 修改时间：2017/4/7 上午9:19
 * 修改备注：
 */

public class MasterDailyContentDelegate implements ItemViewDelegate<MasterDailyType>{

    private Context mContext;
    private MasterDailyBean mMasterDailyBean;
    private ImageView mMasterIcon;
    private TextView mTvTitle;
    private TextView mTvModifyTime;
    private TextView mTvReadCount;
    private RelativeLayout mMasterLayout;

    @Override
    public int getItemViewLayoutId() {
        return R.layout.recycler_item_masterdaily;
    }

    @Override
    public boolean isForViewType(MasterDailyType item, int position) {
        return item instanceof MasterDailyBean;
    }

    @Override
    public void convert(ViewHolder holder, MasterDailyType masterDailyType, int position) {

        mContext = holder.getConvertView().getContext();
        mMasterDailyBean = (MasterDailyBean) masterDailyType;
        mMasterIcon = (ImageView) holder.getView(R.id.iv_icon);
        Glide.with(mContext).load(mMasterDailyBean.picAddress)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .error(R.drawable.master_default_icon)
                .into(mMasterIcon);
        //标题
        mTvTitle = (TextView) holder.getView(R.id.tv_title);
        mTvTitle.setText(mMasterDailyBean.artTitle);

        //时间
        mTvModifyTime = (TextView) holder.getView(R.id.modify_time);
        mTvModifyTime.setText(mMasterDailyBean.gmtModifiedFormat);

        //阅读量
        mTvReadCount = (TextView) holder.getView(R.id.tv_read_count);
        mTvReadCount.setText(mMasterDailyBean.readCount+"  阅读");

        mMasterLayout = (RelativeLayout) holder.getView(R.id.rl_master_layout);
        mMasterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMasterDailyBean.picUrl != null) {
                    Intent intent = new Intent(mContext, WebviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.P_WebView_Url,mMasterDailyBean.picUrl);
                    bundle.putString(Constants.P_WebView_Title, "大师日报");
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });
    }
}
