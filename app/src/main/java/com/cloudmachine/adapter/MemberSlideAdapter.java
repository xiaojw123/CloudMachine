package com.cloudmachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.bean.LarkMemberItem;
import com.cloudmachine.bean.Member;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.widgets.RadiusButtonView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuLayout;

import java.util.List;

import butterknife.BindView;


public class MemberSlideAdapter extends BaseRecyclerAdapter<LarkMemberItem> {
    private LayoutInflater layoutInflater;
    private DisplayImageOptions displayImageOptions;
    OnItemClickListener mOnItemClickListener;
    Member member;

    public MemberSlideAdapter(List<LarkMemberItem> dataResult, Context context) {
        super(context, dataResult);
        this.layoutInflater = LayoutInflater.from(context);
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_head)
                .showImageOnFail(R.drawable.ic_default_head)
                .showImageForEmptyUri(R.drawable.ic_default_head)
                .showImageOnFail(R.drawable.ic_default_head).cacheInMemory(true)
                .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5))
                .build();
        member = MemeberKeeper.getOauth(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public int getPosition() {

        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MemberSlideHolder(layoutInflater.inflate(R.layout.mc_search_listview, null));
    }


    class MemberSlideHolder extends BaseHolder<LarkMemberItem> {
        @BindView(R.id.info_img)
        ImageView info_img;
        @BindView(R.id.arrow)
        ImageView arrow;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.summary)
        TextView summary;
        @BindView(R.id.arrow_add)
        RadiusButtonView arrow_add;

        public MemberSlideHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initViewHolder(LarkMemberItem item) {
            if (item != null) {
                String imgString = item.getMiddleLogo();
                String titleName;
                String remarkName = item.getRemark();
                if (!TextUtils.isEmpty(remarkName)) {
                    titleName = remarkName;
                } else {
                    titleName = item.getNickName();
                }
                View rightView = itemView.findViewById(R.id.swipe_right);
                int color = mContext.getResources().getColor(R.color.cor8);
                if (item.getRoleId() == 1) {
                    color = mContext.getResources().getColor(R.color.cor2);
                    if (rightView != null) {
                        setSwipeParent(rightView.getParent(), false);
                    }
                } else {
                    if (rightView != null) {
                        setSwipeParent(rightView.getParent(), true);
                    }
                }
                if (member != null) {
                    if (item.getMemberId() == member.getId()) {
                        titleName = "我";
                    }
                }
                title.setTextColor(color);
                title.setText(titleName);
                summary.setText(item.getRoleValue());
                ImageLoader.getInstance().displayImage(imgString, info_img,
                        displayImageOptions, null);
                itemView.setTag(R.id.id_RemarkInfo, item);
                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener == null) {
                            return;
                        }
                        mOnItemClickListener.onItemClick(v, position);
                    }
                });
            }

        }

    }

    public void setSwipeParent(ViewParent parent, boolean isEnable) {
        if (parent != null) {
            if (parent instanceof SwipeMenuLayout) {
                ((SwipeMenuLayout) parent).setSwipeEnable(isEnable);
            }
            setSwipeParent(parent.getParent(), isEnable);
        }


    }

}
