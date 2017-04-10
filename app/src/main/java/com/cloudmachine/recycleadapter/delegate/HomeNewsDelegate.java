package com.cloudmachine.recycleadapter.delegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudmachine.R;
import com.cloudmachine.activities.WanaCloudBox;
import com.cloudmachine.activities.WebviewActivity;
import com.cloudmachine.recyclerbean.HomeNewsTransfer;
import com.cloudmachine.recyclerbean.HomePageType;
import com.cloudmachine.ui.homepage.activity.MasterDailyActivity;
import com.cloudmachine.utils.Constants;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/26 下午4:46
 * 修改人：shixionglu
 * 修改时间：2017/3/26 下午4:46
 * 修改备注：
 */

public class HomeNewsDelegate implements ItemViewDelegate<HomePageType>, View.OnClickListener {


    private Context mContext;
    private HomeNewsTransfer mHomeNewsTransfer;
    private ImageView mIvNewsLeft;
    private LinearLayout mLlNewsLeft;
    private RelativeLayout mRlMasterNews;
    private TextView mTvMasterNews;
    private TextView mTvMasterNewsDesc;
    private CircleImageView mIvMasterPic;
    private RelativeLayout mRlWanaBox;
    private TextView mTvWanaBox;
    private TextView mTvWanaBoxDesc;
    private CircleImageView mIvWanaBox;

    @Override
    public int getItemViewLayoutId() {
        Constants.MyLog("进来了");
        return R.layout.delegate_item_news;
    }

    @Override
    public boolean isForViewType(HomePageType item, int position) {
        return item instanceof HomeNewsTransfer;
    }

    @Override
    public void convert(ViewHolder holder, HomePageType homePageType, int position) {
        mContext = holder.getConvertView().getContext();
        mHomeNewsTransfer = (HomeNewsTransfer) homePageType;
        initLeft(holder);
        initTop(holder);
        initBottom(holder);
    }

    private void initBottom(ViewHolder holder) {

        mRlWanaBox = (RelativeLayout) holder.getView(R.id.rl_wana_box);
        mRlWanaBox.setOnClickListener(this);
        mTvWanaBox = (TextView) holder.getView(R.id.tv_wana_box);
        mTvWanaBoxDesc = (TextView) holder.getView(R.id.tv_wana_box_desc);
        mIvWanaBox = (CircleImageView) holder.getView(R.id.iv_wanabox);
        for (int i = 0;i<mHomeNewsTransfer.picAddress.size();i++) {

            if (mHomeNewsTransfer.adsMidSort.get(i) != null && mHomeNewsTransfer.adsMidSort.get(i) == 3) {

                mTvMasterNews.setText(mHomeNewsTransfer.adsTitle.get(i));
                mTvMasterNewsDesc.setText(mHomeNewsTransfer.adsDescription.get(i));
                Glide.with(mContext).load(mHomeNewsTransfer.picAddress.get(i))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .crossFade()
                        .error(R.drawable.wana_box)
                        .into(mIvWanaBox);
            }
        }
    }

    private void initTop(ViewHolder holder) {

        mRlMasterNews = (RelativeLayout) holder.getView(R.id.rl_master_news);
        mRlMasterNews.setOnClickListener(this);
        //大师日报标题
        mTvMasterNews = (TextView) holder.getView(R.id.tv_master_news);
        //大师日报描述
        mTvMasterNewsDesc = (TextView) holder.getView(R.id.tv_master_news_desc);
        //大师日报图片
        mIvMasterPic = (CircleImageView) holder.getView(R.id.iv_master_pic);
        for (int i = 0;i<mHomeNewsTransfer.picAddress.size();i++) {

            if (mHomeNewsTransfer.adsMidSort.get(i) != null && mHomeNewsTransfer.adsMidSort.get(i) == 2) {

                mTvMasterNews.setText(mHomeNewsTransfer.adsTitle.get(i));
                mTvMasterNewsDesc.setText(mHomeNewsTransfer.adsDescription.get(i));
                Glide.with(mContext).load(mHomeNewsTransfer.picAddress.get(i))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .crossFade()
                        .error(R.drawable.master_news)
                        .into(mIvMasterPic);
            }
        }

    }

    private void initLeft(ViewHolder holder) {

        mIvNewsLeft = (ImageView) holder.getView(R.id.iv_news_left);
        mLlNewsLeft = (LinearLayout) holder.getView(R.id.ll_news_left);
        mLlNewsLeft.setOnClickListener(this);
        for (int i = 0;i<mHomeNewsTransfer.picAddress.size();i++) {
            if (mHomeNewsTransfer.adsMidSort.get(i) != null && mHomeNewsTransfer.adsMidSort.get(i)== 1) {
                Glide.with(mContext).load(mHomeNewsTransfer.picAddress.get(i))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .crossFade()
                        .into(mIvNewsLeft);
            }
            if (mHomeNewsTransfer.adsMidSort.get(i) != null && mHomeNewsTransfer.adsMidSort.get(i) == 2) {

            }

            if (mHomeNewsTransfer.adsMidSort.get(i) != null && mHomeNewsTransfer.adsMidSort.get(i) == 3) {

            }
        }
        /*Glide.with(mContext).load(mHomeNewsTransfer.picAddress.get(0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .into(mIvNewsLeft);*/

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //左侧图片的点击事件
            case R.id.ll_news_left:
                Intent intent = new Intent(mContext,WebviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.P_WebView_Url, mHomeNewsTransfer.picUrl.get(0));
                bundle.putString(Constants.P_WebView_Title, "商城");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                break;
            case R.id.rl_master_news:
                //大师日报点击跳转
                Intent masterIntent = new Intent(mContext, MasterDailyActivity.class);
                mContext.startActivity(masterIntent);
                break;
            case R.id.rl_wana_box:
                Intent wanaBoxIntent = new Intent(mContext, WanaCloudBox.class);
                mContext.startActivity(wanaBoxIntent);
                break;
        }
    }
}
