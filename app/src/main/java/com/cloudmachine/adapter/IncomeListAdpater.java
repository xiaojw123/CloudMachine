package com.cloudmachine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.bean.SalaryHistoryItem;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.ui.home.activity.IncomeSpendActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.DensityUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiaojw on 2018/6/12.
 */

public class IncomeListAdpater extends BaseRecyclerAdapter<SalaryHistoryItem> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private int mType;//1支付 其他 收入
    private int tmpPos;

    public IncomeListAdpater(Context context, List<SalaryHistoryItem> items) {
        super(context, items);
        setHasStableIds(true);
    }
    public void setType(int type) {
        mType = type;

    }


    @Override
    public int getItemViewType(int position) {
        tmpPos = position;
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_income, parent, false);
        return new IncomeHolder(itemView);
    }


    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.list_item_income_header, parent, false);
        return new HeaderHolder(headerView);
    }


    @Override
    public long getHeaderId(int position) {
        if (mItems != null && mItems.size() > position) {
            SalaryHistoryItem item = mItems.get(position);
            if (item != null) {
                return CommonUtils.getMonth(item.getPayoffTime());
            }
        }
        return -1;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        AppLog.print("onBindHeaderViewHolder___"+position);
        if (position<0){
            return;
        }
        if (holder != null) {
            TextView toalTv = (TextView) holder.itemView.findViewById(R.id.month_toal_tv);
            if (toalTv != null) {
                if (mItems != null && mItems.size() > position) {
                    SalaryHistoryItem item = mItems.get(position);
                    String typeStr;
                    String color;
                    if (mType == IncomeSpendActivity.TYPE_SPEND) {
                        typeStr = "支出";
                        color = "#333333";
                    } else {
                        typeStr = "收入";
                        color = "#ff8901";
                    }
                    Spanned toalTextSp = Html.fromHtml(CommonUtils.getSalaryMonth(item.getPayoffTime()) + "  " + typeStr + "<font color=\"" + color + "\"> ¥ " + item.getTotalAmountOfInterval() + "</font>");
                    toalTv.setText(toalTextSp);
                }
            }
        }
    }


    public class HeaderHolder extends RecyclerView.ViewHolder {


        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    public class IncomeHolder extends BaseHolder<SalaryHistoryItem> {

        @BindView(R.id.item_income_payee)
        TextView payeeTv;
        @BindView(R.id.item_income_time)
        TextView timeTv;
        @BindView(R.id.itme_income_amount)
        TextView amountTv;
        @BindView(R.id.itme_income_line)
        View lineView;


        public IncomeHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initViewHolder(SalaryHistoryItem item) {

        }

        public void initViewHolder(SalaryHistoryItem item, int position) {
            int len = mItems.size();
            if (position == len - 1) {
                lineView.setVisibility(View.GONE);
            } else {
                lineView.setVisibility(View.VISIBLE);
                int nextPos = position + 1;
                if (len > nextPos) {
                    long curMoth = CommonUtils.getMonth(item.getPayoffTime());
                    SalaryHistoryItem nextItem = mItems.get(nextPos);
                    long nextMoth = CommonUtils.getMonth(nextItem.getPayoffTime());
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                    if (curMoth != nextMoth) {
                        params.bottomMargin = DensityUtil.dip2px(mContext, 10);
                    } else {
                        params.bottomMargin = 0;
                    }
                }
            }
            if (mType == IncomeSpendActivity.TYPE_SPEND) {
                payeeTv.setText("收款人:" + item.getReceiveMemberName());
                amountTv.setTextColor(mContext.getResources().getColor(R.color.cor8));
            } else {
                payeeTv.setText("付款人:" + item.getPayMemberName());
                amountTv.setTextColor(mContext.getResources().getColor(R.color.c_ff8901));
            }
            amountTv.setText(String.valueOf(item.getSalaryAmount()));
            timeTv.setText(item.getFoarmtDate());

        }
    }

}
