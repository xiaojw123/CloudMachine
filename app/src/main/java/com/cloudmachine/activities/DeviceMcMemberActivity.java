package com.cloudmachine.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.cloudmachine.R;
import com.cloudmachine.adapter.BaseRecyclerAdapter;
import com.cloudmachine.adapter.MemberSlideAdapter;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.autolayout.widgets.SlideView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.MemberInfo;
import com.cloudmachine.bean.MemberInfoSlide;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.listener.OnItemClickListener;
import com.cloudmachine.listener.RecyclerItemClickListener;
import com.cloudmachine.net.task.DevicesDeleteMemberAsync;
import com.cloudmachine.net.task.DevicesMemberListAsync;
import com.cloudmachine.ui.home.activity.RemarkInfoActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.widget.CommonTitleView;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DeviceMcMemberActivity extends BaseAutoLayoutActivity implements Callback, RecyclerItemClickListener.OnItemClickListener, OnItemClickListener, BaseRecyclerAdapter.OnItemClickListener {

    private Context mContext;
    private Handler mHandler;
    private long deviceId;
    private int deviceType;
    private SwipeMenuRecyclerView member_srlv;
    private MemberSlideAdapter pAdapter;
    private List<MemberInfoSlide> dataResult = new ArrayList<MemberInfoSlide>();
    private int quartersId;
    private RadiusButtonView add_member;
    private View empty_layout;
    private SlideView mLastSlideViewWithStatusOn;
    boolean isOwner;
    int curMemberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_mc_member);
        mContext = this;
        mHandler = new Handler(this);
        getIntentData();
        initView();
    }


    @Override
    public void initPresenter() {

    }

    private void getIntentData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {
                deviceId = bundle.getLong(Constants.P_DEVICEID);
                deviceType = bundle.getInt(Constants.P_DEVICETYPE);
            } catch (Exception e) {
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.TIME_MACHINE_MEMBER);
        getMemberList();
    }


    private void initView() {
        initTitleLayout();
        empty_layout = findViewById(R.id.empty_layout);
        pAdapter = new MemberSlideAdapter(dataResult, mContext);
        member_srlv = (SwipeMenuRecyclerView) findViewById(R.id.member_swipeRlv);
        member_srlv.setLayoutManager(new LinearLayoutManager(this));
        add_member = (RadiusButtonView) findViewById(R.id.add_member);
        if (!Constants.isNoEditInMcMember(deviceId, deviceType)) {
            isOwner = true;
            pAdapter.setOnItemClickListener(this);
            member_srlv.setSwipeMenuCreator(CommonUtils.getMenuCreator(this));
            member_srlv.setSwipeMenuItemClickListener(menuItemClickListener);
            add_member.setVisibility(View.VISIBLE);
            add_member.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(mContext, UMengKey.count_memeber_add);
                    gotoSearch();
                }
            });
        } else {
            add_member.setVisibility(View.GONE);
        }
        member_srlv.setAdapter(pAdapter);

    }

    SwipeMenuItemClickListener menuItemClickListener = new SwipeMenuItemClickListener() {


        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            if (menuBridge.getDirection() == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                curMemberId = dataResult.get(menuBridge.getAdapterPosition()).getMemberId();
                //删除item
                MobclickAgent.onEvent(DeviceMcMemberActivity.this, UMengKey.count_memeber_delete);
                showAlertPop();
            }
    }
};

public void showAlertPop(){
        CustomDialog.Builder builder=new CustomDialog.Builder(this);
        builder.setMessage("确认删除该成员吗？删除后成员将无法查看设备信息");
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
@Override
public void onClick(DialogInterface dialog,int which){
        dialog.dismiss();
        }
        });
        builder.setPositiveButton("删除",new DialogInterface.OnClickListener(){

@Override
public void onClick(DialogInterface dialog,int which){
        MobclickAgent.onEvent(DeviceMcMemberActivity.this,MobEvent.COUNT_MEMEBER_DELETE);
        dialog.dismiss();
        new DevicesDeleteMemberAsync(DeviceMcMemberActivity.this,mHandler).execute(String.valueOf(curMemberId),
        String.valueOf(deviceId));

        }
        });
        builder.create().show();
        }


private void initTitleLayout(){
        CommonTitleView title_layout=(CommonTitleView)findViewById(R.id.title_layout);
        if(!Constants.isNoEditInMcMember(deviceId,deviceType)){
        title_layout.setRightText(getResources().getString(R.string.mc_member_add),new OnClickListener(){
@Override
public void onClick(View v){
        gotoSearch();
        }
        });
        }

        }

private void gotoSearch(){
        Bundle bundle=new Bundle();
        bundle.putInt(Constants.P_SEARCHLISTTYPE,3);
        bundle.putLong(Constants.P_DEVICEID,deviceId);
        Constants.toActivity(this,SearchActivity.class,bundle);
        }


@Override
public boolean handleMessage(Message msg){
        // TODO Auto-generated method stub
        switch(msg.what){
        case Constants.HANDLER_GETDEVICEMEMBER_SUCCESS:
        List<MemberInfo> tM=(List<MemberInfo>)msg.obj;
        if(null!=tM){
        dataResult.clear();
        for(int i=0;i<tM.size();i++){
        MemberInfoSlide minfo=new MemberInfoSlide();
        MemberInfo tmInfo=tM.get(i);
        minfo.setId(tmInfo.getId());
        minfo.setRoleIdS(tmInfo.getRoleIdS());
        minfo.setMemberId(tmInfo.getMemberId());
        minfo.setName(tmInfo.getName());
        minfo.setRoleRemark(tmInfo.getRoleRemark());
        minfo.setRole(tmInfo.getRole());
        minfo.setMiddlelogo(tmInfo.getMiddlelogo());
        dataResult.add(minfo);
        }
//				dataResult.addAll(tM);
        if(null!=dataResult&&dataResult.size()>0){
        empty_layout.setVisibility(View.GONE);
        }
        pAdapter.notifyDataSetChanged();
        tM=null;
        }

        break;
        case Constants.HANDLER_GETDEVICEMEMBER_FAIL:

        break;
        case Constants.HANDLER_DELETEMEMBER_SUCCESS:
        Constants.MyToast((String)msg.obj);
        getMemberList();
        break;
        case Constants.HANDLER_DELETEMBER_FAIL:
        Constants.MyToast((String)msg.obj);
        break;

        }
        return false;
        }


private void getMemberList(){
//		if(deviceId != Constants.MC_Simulation_DeviceId){
//			new DevicesMemberListAsync(deviceId,mContext,mHandler).execute();
//		}
        new DevicesMemberListAsync(deviceId,mContext,mHandler).execute();
        }

@Override
protected void onActivityResult(int requestCode,int resultCode,Intent data){
        // TODO Auto-generated method stub
//		switch (resultCode)
//		{
//		case RESULT_OK:
//				Bundle bundle=data.getExtras();
//				if(bundle.getBoolean(Constants.P_ISDELETEMERMBER))
//					getMemberList();
//			break;
//		}
        }


@Override
public void onItemClick(View view,int position){
        if(isOwner){
        MemberInfoSlide memberInfo=(MemberInfoSlide)view.getTag(R.id.id_RemarkInfo);
        if(UserHelper.isLogin(this)){
        long memberId=UserHelper.getMemberId(this);
        if(memberInfo!=null&&memberId!=memberInfo.getMemberId()){
        Intent intent=new Intent(this,RemarkInfoActivity.class);
        intent.putExtra(Constants.P_DEVICEID,deviceId);
        intent.putExtra(RemarkInfoActivity.ID,(long)memberInfo.getId());
        intent.putExtra(RemarkInfoActivity.NAME,memberInfo.getName());
        intent.putExtra(RemarkInfoActivity.MEMBER_ID,memberId);
        intent.putExtra(RemarkInfoActivity.ROLE,memberInfo.getRole());
        intent.putExtra(RemarkInfoActivity.ROLEIDS,memberInfo.getRoleIdS());
        intent.putExtra(RemarkInfoActivity.ROLEREMARK,memberInfo.getRoleRemark());
        startActivity(intent);
        }
        }
        }
        }
        }
