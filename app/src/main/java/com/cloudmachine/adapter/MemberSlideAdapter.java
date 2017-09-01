package com.cloudmachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.listener.OnItemClickListener;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.MemberInfoSlide;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.widgets.RadiusButtonView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

import butterknife.BindView;


public class MemberSlideAdapter extends SwipeMenuAdapter {
    private List<MemberInfoSlide> dataResult;
    private LayoutInflater layoutInflater;
    private DisplayImageOptions displayImageOptions;
    int position;
    OnItemClickListener mOnItemClickListener;
    Context context;
    Member member;

    public MemberSlideAdapter(List<MemberInfoSlide> dataResult, Context context) {
        super();
        this.context = context;
        this.dataResult = dataResult;
        this.layoutInflater = LayoutInflater.from(context);
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_head)
                .showImageOnFail(R.drawable.ic_default_head)
                .showImageForEmptyUri(R.drawable.ic_default_head)
                .showImageOnFail(R.drawable.ic_default_head).cacheInMemory(true)
                .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5))
                .build();
        member=MemeberKeeper.getOauth(context);
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
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder, final int position) {
        this.position = position;
        if (dataResult != null && dataResult.size() > 0) {
            MemberInfoSlide dInfo = dataResult.get(position);
            if (dInfo != null) {
                MemberSlideHolder holder = (MemberSlideHolder) itemHolder;
                String imgString = dInfo.getMiddlelogo();
                String titleName;
                String remarkName = dInfo.getRoleRemark();
                if (!TextUtils.isEmpty(remarkName)) {
                    titleName = remarkName;
                } else {
                    titleName = dInfo.getName();
                }
                View rightView = holder.itemView.findViewById(R.id.swipe_right);
                int color = context.getResources().getColor(R.color.cor8);
                if (dInfo.getRoleIdS() == 1) {
                    color = context.getResources().getColor(R.color.cor2);
                    if (rightView != null) {
                        rightView.setVisibility(View.GONE);
                    }
                } else {
                    if (rightView != null) {
                        rightView.setVisibility(View.VISIBLE);
                    }
                }
                if (member!=null){
                    if (dInfo.getMemberId()==member.getId()){
                        titleName="我";
                    }
                }
                holder.title.setTextColor(color);
                holder.title.setText(titleName);
                holder.summary.setText(dInfo.getRole());
                ImageLoader.getInstance().displayImage(imgString, holder.info_img,
                        displayImageOptions, null);
                holder.itemView.setTag(R.id.id_RemarkInfo, dInfo);
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener==null){
                            return;
                        }
                        mOnItemClickListener.onItemClick(v, position);
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return dataResult != null && dataResult.size() > 0 ? dataResult.size() : 0;
    }


    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return layoutInflater.inflate(R.layout.mc_search_listview, null);

    }

    @Override
    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new MemberSlideHolder(realContentView);
    }


    class MemberSlideHolder extends BaseHolder<MemberInfoSlide> {
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
        public void initViewHolder(MemberInfoSlide dInfo) {


        }
    }

}
