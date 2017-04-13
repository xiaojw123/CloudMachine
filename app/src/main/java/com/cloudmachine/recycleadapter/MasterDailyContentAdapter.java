package com.cloudmachine.recycleadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudmachine.R;
import com.cloudmachine.activities.WebviewActivity;
import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.recyclerbean.MasterDailyBean;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;

import java.util.ArrayList;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/13 下午7:42
 * 修改人：shixionglu
 * 修改时间：2017/4/13 下午7:42
 * 修改备注：
 */

public class MasterDailyContentAdapter extends RecyclerView.Adapter<MasterDailyContentAdapter.MasterDailyHolder> {


    private Context                    mContext;
    private ArrayList<MasterDailyBean> dataList;
    private LayoutInflater             inflater;
    private int                        mId;


    public MasterDailyContentAdapter(Context context,ArrayList<MasterDailyBean> dataList) {

        mContext = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public MasterDailyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflater.inflate(R.layout.recycler_item_masterdaily, parent, false);
        return new MasterDailyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MasterDailyHolder holder, final int position) {

        mId = dataList.get(position).id;
        Glide.with(mContext).load(dataList.get(position).picAddress)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .error(R.drawable.master_default_icon)
                .into(holder.mMasterIcon);
        holder.mTvTitle.setText(dataList.get(position).artTitle);
        holder.mTvModifyTime.setText(dataList.get(position).gmtModifiedFormat);
        holder.mTvReadCount.setText(dataList.get(position).readCount+"  阅读");
        holder.mMasterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataList.get(position).picUrl != null) {
                    addReadCount();
                    Intent intent = new Intent(mContext, WebviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.P_WebView_Url,dataList.get(position).picUrl);
                    Constants.MyLog("打印的跳转链接"+dataList.get(position).picUrl);
                    bundle.putString(Constants.P_WebView_Title, "大师日报");
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MasterDailyHolder extends RecyclerView.ViewHolder {

        private ImageView      mMasterIcon;
        private TextView       mTvTitle;
        private TextView       mTvModifyTime;
        private TextView       mTvReadCount;
        private RelativeLayout mMasterLayout;


        public MasterDailyHolder(View itemView) {
            super(itemView);
            mMasterIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mTvModifyTime = (TextView) itemView.findViewById(R.id.modify_time);
            mTvReadCount = (TextView) itemView.findViewById(R.id.tv_read_count);
            mMasterLayout = (RelativeLayout) itemView.findViewById(R.id.rl_master_layout);

        }
    }


    private void addReadCount() {

        Api.getDefault(HostType.GUOSHUAI_HOST).readCount(mId)
                .compose(RxSchedulers.<BaseRespose>io_main())
                .subscribe(new RxSubscriber<BaseRespose>(mContext,false) {
                    @Override
                    protected void _onNext(BaseRespose baseRespose) {
                        if (baseRespose.code == 800) {
                            ToastUtils.success(((String) baseRespose.result), true);
                        } else {
                            ToastUtils.error(baseRespose.message,true);
                        }
                    }
                    @Override
                    protected void _onError(String message) {

                    }
                });
    }
}
