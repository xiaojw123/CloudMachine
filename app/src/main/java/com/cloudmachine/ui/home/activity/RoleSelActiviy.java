package com.cloudmachine.ui.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.EmunBean;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.listener.RecyclerItemClickListener;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class RoleSelActiviy extends BaseAutoLayoutActivity {
    public static final String ROLE_LIST = "role_list";
    public static final String ROLE_BEAN = "role_bean";
    public static final String ROLE_NAME = "role_name";
    RoleSelAdapter adapter;
    String roleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_sel);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.role_sel_rlv);
        List<EmunBean> roleList = getIntent().getParcelableArrayListExtra(ROLE_LIST);
        roleName = getIntent().getStringExtra(ROLE_NAME);
        adapter = new RoleSelAdapter(roleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration lineDecor = new DividerItemDecoration(this, LinearLayout.VERTICAL);
        lineDecor.setDrawable(getResources().getDrawable(R.drawable.divider_line_horztial));
        recyclerView.addItemDecoration(lineDecor);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                adapter.setSelectPosition(position);
                Intent data = new Intent();
                EmunBean bean = (EmunBean) view.getTag();
                data.putExtra(ROLE_BEAN, bean);
                setResult(RESULT_OK, data);
                finish();
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.TIME_MACHINE_MEMBER_ROLE);
    }

    @Override
    public void initPresenter() {

    }
    private class RoleSelAdapter extends RecyclerView.Adapter<RoleSelHoler> {

        List<EmunBean> mRoleList;
        int mSelPosition;

        public RoleSelAdapter(List<EmunBean> roleList) {
            mRoleList = roleList;
        }

        public void setSelectPosition(int selPosition) {
            mSelPosition = selPosition;
            notifyDataSetChanged();
        }


        @Override
        public RoleSelHoler onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params1.gravity = Gravity.CENTER_VERTICAL;
            FrameLayout itemView = new FrameLayout(context);
            TextView titleTv = new TextView(context);
            titleTv.setTextColor(getResources().getColor(R.color.cor9));
            titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            titleTv.setLayoutParams(params1);
            ImageView selImg = new ImageView(context);
            selImg.setImageResource(R.drawable.ic_selected);
            FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params2.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
            selImg.setLayoutParams(params2);
            selImg.setVisibility(View.INVISIBLE);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.spa3)));
            itemView.addView(titleTv);
            itemView.addView(selImg);
            itemView.setBackgroundColor(getResources().getColor(R.color.white));
            int left = (int) getResources().getDimension(R.dimen.pad2);
            itemView.setPadding(left, 0, left, 0);
            return new RoleSelHoler(itemView);
        }

        @Override
        public void onBindViewHolder(RoleSelHoler holder, int position) {
            if (mRoleList == null || mRoleList.size() < 1) {
                return;
            }
            EmunBean bean = mRoleList.get(position);
            String type = bean.getValueName();
            holder.titleTv.setText(type);
            if (roleName.equals(type)) {
                holder.selImg.setVisibility(View.VISIBLE);
            } else {
                holder.selImg.setVisibility(View.INVISIBLE);
            }
            holder.itemView.setTag(bean);


        }

        @Override
        public int getItemCount() {
            return mRoleList != null ? mRoleList.size() : 0;
        }


    }

    class RoleSelHoler extends RecyclerView.ViewHolder {
        TextView titleTv;
        ImageView selImg;

        public RoleSelHoler(View itemView) {
            super(itemView);
            FrameLayout container = (FrameLayout) itemView;
            titleTv = (TextView) container.getChildAt(0);
            selImg = (ImageView) container.getChildAt(1);
        }
    }

}
