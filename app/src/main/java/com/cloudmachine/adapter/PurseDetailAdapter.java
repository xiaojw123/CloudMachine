package com.cloudmachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.bean.PurseDetailItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2017/11/30.
 */

public class PurseDetailAdapter extends BaseRecyclerAdapter<PurseDetailItem> {

    public PurseDetailAdapter(Context context) {
        super(context);
    }

    public PurseDetailAdapter(Context context, List<PurseDetailItem> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PurseDetailHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_purse_detail, parent, false));
    }

      class PurseDetailHolder extends BaseHolder<PurseDetailItem> {
        @BindView(R.id.item_purse_operation_tv)
        TextView itemPurseOperationTv;
        @BindView(R.id.item_balance_tv)
        TextView itemBalanceTv;
        @BindView(R.id.item_date_tv)
        TextView itemDateTv;
        @BindView(R.id.item_acount_change_tv)
        TextView itemAcountChangeTv;
        private PurseDetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void initViewHolder(PurseDetailItem item) {

        }
    }

}
