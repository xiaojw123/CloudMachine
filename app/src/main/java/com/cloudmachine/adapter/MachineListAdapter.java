package com.cloudmachine.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.bean.DeviceAuthItem;

import java.util.List;

import static com.cloudmachine.R.id.owership_status_tv;

/**
 * Created by xiaojw on 2018/8/3.
 */

public class MachineListAdapter extends BaseRecyclerAdapter<DeviceAuthItem> {
    public MachineListAdapter(Context context, List<DeviceAuthItem> items) {
        super(context, items);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_machine_owership, parent, false);
        return new MachineHolder(itemView);
    }

    private class MachineHolder extends BaseHolder<DeviceAuthItem> {

        TextView nameTv;
        TextView statusTv;

        private MachineHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.owership_name_tv);
            statusTv = (TextView) itemView.findViewById(owership_status_tv);
        }


        @Override
        public void initViewHolder(DeviceAuthItem item) {
            Spanned name = Html.fromHtml(item.getBrand() + "<font color='#888888'>(" + item.getModel() + ")</font>");
            nameTv.setText(name);
            int authStatus = item.getAuditStatus();
            if (authStatus == 0) {
                statusTv.setTextColor(mContext.getResources().getColor(R.color.c_ff8901));
                Drawable rightDrawable = mContext.getResources().getDrawable(R.drawable.arrow_right);
                rightDrawable.setBounds(0, 0, rightDrawable.getIntrinsicWidth(), rightDrawable.getIntrinsicHeight());
                statusTv.setCompoundDrawables(null, null, rightDrawable, null);
            } else {
                if (authStatus == 1) {
                    statusTv.setTextColor(mContext.getResources().getColor(R.color.c_ff8901));
                } else {
                    statusTv.setTextColor(mContext.getResources().getColor(R.color.cor20));
                }
                statusTv.setCompoundDrawables(null, null, null, null);
            }
            statusTv.setText(item.getAuditStatusTxt());

        }
    }
}
