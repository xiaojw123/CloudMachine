package com.cloudmachine.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.cloudmachine.R;
import com.cloudmachine.adapter.BaseRecyclerAdapter;
import com.cloudmachine.adapter.MemberSlideAdapter;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.LarkMemberItem;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.listener.OnItemClickListener;
import com.cloudmachine.listener.RecyclerItemClickListener;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.activity.RemarkInfoActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.widget.CommonTitleView;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

public class DeviceMcMemberActivity extends BaseAutoLayoutActivity implements RecyclerItemClickListener.OnItemClickListener, OnItemClickListener, BaseRecyclerAdapter.OnItemClickListener {

    private long deviceId;
    int curMemberId;
    boolean isOwner;
    private SwipeMenuRecyclerView member_srlv;
    private View empty_layout;
    MemberSlideAdapter mAdapter;
    List<LarkMemberItem> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_mc_member);
        initParams();
        initView();
    }
    @Override
    public void initPresenter() {

    }

    private void initParams() {
        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            deviceId = bundle.getLong(Constants.DEVICE_ID);
            isOwner = bundle.getBoolean(Constants.IS_OWNER);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.TIME_MACHINE_MEMBER);
        updateMemberList();
    }


    private void initView() {
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.title_layout);
        empty_layout = findViewById(R.id.empty_layout);
        member_srlv = (SwipeMenuRecyclerView) findViewById(R.id.member_swipeRlv);
        member_srlv.setLayoutManager(new LinearLayoutManager(this));
        RadiusButtonView add_member = (RadiusButtonView) findViewById(R.id.add_member);
        if (isOwner) {
            titleView.setRightText(getResources().getString(R.string.mc_member_add),rightClickLi);
            member_srlv.setSwipeMenuCreator(CommonUtils.getMenuCreator(this));
            member_srlv.setSwipeMenuItemClickListener(menuItemClickLi);
            add_member.setVisibility(View.VISIBLE);
            add_member.setOnClickListener(rightClickLi);
        } else {
            add_member.setVisibility(View.GONE);
        }
    }
    OnClickListener rightClickLi=new OnClickListener() {
        @Override
        public void onClick(View v) {
            MobclickAgent.onEvent(mContext, UMengKey.count_memeber_add);
            gotoSearch();
        }
    };
    SwipeMenuItemClickListener menuItemClickLi = new SwipeMenuItemClickListener() {


        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            if (menuBridge.getDirection() == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                if (mItems != null && mItems.size() > 0) {
                    curMemberId = mItems.get(menuBridge.getAdapterPosition()).getMemberId();
                    //删除item
                    MobclickAgent.onEvent(DeviceMcMemberActivity.this, UMengKey.count_memeber_delete);
                    showAlertPop();
                }
            }
        }
    };

    public void showAlertPop() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("确认删除该成员吗？删除后成员将无法查看设备信息");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                member_srlv.smoothCloseMenu();
            }
        });
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                MobclickAgent.onEvent(DeviceMcMemberActivity.this, MobEvent.COUNT_MEMEBER_DELETE);
                dialog.dismiss();
                member_srlv.smoothCloseMenu();
                deleDeviceMember();
            }
        });
        builder.create().show();
    }



    private void gotoSearch() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.P_SEARCHLISTTYPE, 1);
        bundle.putLong(Constants.DEVICE_ID, deviceId);
        Constants.toActivity(this, SearchActivity.class, bundle);
    }




    @Override
    public void onItemClick(View view, int position) {
        if (isOwner) {
            LarkMemberItem memberInfo = (LarkMemberItem) view.getTag(R.id.id_RemarkInfo);
            if (UserHelper.isLogin(this)) {
                long memberId = UserHelper.getMemberId(this);
                if (memberInfo != null && memberId != memberInfo.getMemberId()) {
                    Intent intent = new Intent(this, RemarkInfoActivity.class);
                    intent.putExtra(Constants.DEVICE_ID, deviceId);
                    intent.putExtra(RemarkInfoActivity.ID, memberInfo.getGroupId());
                    intent.putExtra(RemarkInfoActivity.NAME, memberInfo.getNickName());
                    intent.putExtra(RemarkInfoActivity.MEMBER_ID, memberId);
                    intent.putExtra(RemarkInfoActivity.ROLE, memberInfo.getRoleValue());
                    intent.putExtra(RemarkInfoActivity.ROLEIDS, memberInfo.getRoleId());
                    intent.putExtra(RemarkInfoActivity.ROLEREMARK, memberInfo.getRemark());
                    startActivity(intent);
                }
            }
        }
    }


    private void updateMemberList() {
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getMemberGroup(deviceId).compose(RxHelper.<List<LarkMemberItem>>handleResult()).subscribe(new RxSubscriber<List<LarkMemberItem>>(mContext) {
            @Override
            protected void _onNext(List<LarkMemberItem> items) {
                mItems = items;
                if (null != items && items.size() > 0) {
                    empty_layout.setVisibility(View.GONE);
                    member_srlv.setVisibility(View.VISIBLE);
                    if (mAdapter == null) {
                        mAdapter = new MemberSlideAdapter(items, mContext);
                        mAdapter.setOnItemClickListener(DeviceMcMemberActivity.this);
                        member_srlv.setAdapter(mAdapter);
                    } else {
                        mAdapter.updateItems(items);
                    }
                } else {
                    empty_layout.setVisibility(View.VISIBLE);
                    member_srlv.setVisibility(View.GONE);
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }
    public void deleDeviceMember(){
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).deleteDeviceMember(deviceId,curMemberId).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                updateMemberList();
            }
            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);
            }
        }));
    }

}
