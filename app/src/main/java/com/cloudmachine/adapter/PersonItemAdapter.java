package com.cloudmachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.autonavi.rtbt.IFrameForRTBT;
import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.base.Operator;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.ui.home.activity.PersonChooseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2018/6/11.
 */

public class PersonItemAdapter extends BaseRecyclerAdapter<Operator> {
    private List<Operator> selOperators = new ArrayList<>();
    private List<String> selOperatorIds=new ArrayList<>();

    private List<Operator> mLastSelItems;

    public PersonItemAdapter(Context context, List<Operator> items) {
        super(context, items);
    }

    //TODO:成员被移除选项更新数据,需要同步数据过滤
    public void setSelOperators(List<Operator> lastSelItems) {
        if (lastSelItems != null && lastSelItems.size() > 0) {
            mLastSelItems = lastSelItems;
            selOperators.addAll(mLastSelItems);
            for (Operator item:mLastSelItems){
                String id=String.valueOf(item.getId());
                if (!selOperatorIds.contains(id)){
                    selOperatorIds.add(id);
                }
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_person_choose, parent, false);
        return new PersonItemHolder(itemView);
    }

    public List<Operator> getSelOperators() {
        return selOperators;
    }


    public class PersonItemHolder extends BaseHolder<Operator> implements CompoundButton.OnCheckedChangeListener {
        @BindView(R.id.item_person_name)
        TextView nameTv;
        @BindView(R.id.item_person_cb)
        CheckBox cb;
        @BindView(R.id.item_person_unauth)
        TextView unAuthTv;

        private PersonItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void initViewHolder(Operator item) {
            if (item.getIsAuth() == 1) {
                nameTv.setTextColor(mContext.getResources().getColor(R.color.cor8));
                nameTv.setText(item.getName());
                unAuthTv.setVisibility(View.GONE);
                cb.setEnabled(true);
                cb.setAlpha(1f);
            } else {
                nameTv.setTextColor(mContext.getResources().getColor(R.color.cor10));
                nameTv.setText(item.getNickName());
                unAuthTv.setVisibility(View.VISIBLE);
                cb.setEnabled(false);
                cb.setAlpha(0.3f);
            }
            cb.setTag(item);
            cb.setOnCheckedChangeListener(this);
            if (mLastSelItems != null && mLastSelItems.size() > 0) {
                for (Operator lasttem : mLastSelItems) {
                    if (lasttem.getId() == item.getId()) {
                        cb.setChecked(true);
                        cb.setEnabled(false);
                    }
                }
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Operator item = (Operator) buttonView.getTag();
            AppLog.print("onCheckedChanged____"+isChecked);
            String idStr=String.valueOf(item.getId());
            if (isChecked) {
                if (!selOperatorIds.contains(idStr)) {
                    selOperatorIds.add(idStr);
                    selOperators.add(item);
                }
            } else {
                if (selOperatorIds.contains(idStr)) {
                    selOperatorIds.remove(idStr);
                    selOperators.remove(item);
                }
            }
            AppLog.print("updateRightTitleText___"+selOperatorIds.size());
            ((PersonChooseActivity)mContext).updateRightTitleText(selOperators.size());

        }
    }


}
