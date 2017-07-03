package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.struc.MemberInfoSlide;
import com.cloudmachine.ui.home.contract.RemarkInfoContract;
import com.cloudmachine.ui.home.model.RemarkInfoModel;
import com.cloudmachine.ui.home.model.RoleBean;
import com.cloudmachine.ui.home.presenter.RemarkInfoPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.wheelview.OnWheelScrollListener;
import com.cloudmachine.utils.widgets.wheelview.WheelView;
import com.cloudmachine.utils.widgets.wheelview.adapter.ArrayWheelAdapter;
import com.github.mikephil.charting.utils.AppLog;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RemarkInfoActivity extends BaseAutoLayoutActivity<RemarkInfoPresenter, RemarkInfoModel> implements RemarkInfoContract.View, OnWheelScrollListener {
    public static final String REMARK_INFO = "remark_info";
    @BindView(R.id.nickname_edt)
    EditText nicknameEdt;
    @BindView(R.id.remarkname_edt)
    EditText remarknameEdt;
    @BindView(R.id.role_container)
    FrameLayout roleContainer;
    @BindView(R.id.remarkinfo_titleview)
    TitleView remarkinfoTitleview;
    @BindView(R.id.remarkinfo_wlv)
    WheelView remarkInfoWlv;
    @BindView(R.id.remarkinfo_wheelview_cancel)
    TextView remarkinfoWheelviewCancel;
    @BindView(R.id.remarkinfo_wheelview_title)
    TextView remarkinfoWheelviewTitle;
    @BindView(R.id.remarkinfo_wheelview_determine)
    TextView remarkinfoWheelviewDetermine;
    @BindView(R.id.remarkinfo_roleselect_layout)
    LinearLayout roleselectLayout;
    @BindView(R.id.remarkinfo_role_name)
    TextView roleNameTv;
    int selectIndex;
    String[] items;
    List<RoleBean> mRoleList;
    MemberInfoSlide memberInfo;
    HashMap<String, Long> mRoleMap = new HashMap<>();
    long deviceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark_info);
        ButterKnife.bind(this);
        initView();
        mPresenter.updateRoleListView();
    }

    private void initView() {
        memberInfo = (MemberInfoSlide) getIntent().getSerializableExtra(REMARK_INFO);
        deviceId = getIntent().getLongExtra(Constants.P_DEVICEID, 0);
        remarkinfoTitleview.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        remarkinfoTitleview.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remark = remarknameEdt.getText().toString();
                String roleName = roleNameTv.getText().toString();
                long roleId;
                if (mRoleMap.containsKey(roleName)) {
                    roleId = mRoleMap.get(roleName);
                }else{
                    roleId=memberInfo.getRoleIdS();
                }
                long id=memberInfo.getId();
                AppLog.print("updateRemarkInfo id___"+id+", memberid_"+memberInfo.getMemberId()+", deviceId_"+deviceId+", remark"+remark+", roleId"+roleId);
                mPresenter.updateRemarkInfo(id, memberInfo.getMemberId(), deviceId, remark, roleId);
            }
        });
        nicknameEdt.setText(memberInfo.getName());
        remarknameEdt.setText(memberInfo.getRoleRemark());
        roleNameTv.setText(memberInfo.getRole());
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @OnClick({R.id.nickname_edt, R.id.remarkname_edt, R.id.role_container, R.id.remarkinfo_wheelview_cancel, R.id.remarkinfo_wheelview_determine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nickname_edt:
                break;
            case R.id.remarkname_edt:
                break;
            case R.id.role_container:
                roleselectLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.remarkinfo_wheelview_cancel:
                roleselectLayout.setVisibility(View.GONE);
                break;
            case R.id.remarkinfo_wheelview_determine:
                if (items.length > selectIndex) {
                    roleNameTv.setText(items[selectIndex]);
                }
                roleselectLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {
        ;
    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        selectIndex = wheel.getCurrentItem();
    }

    @Override
    public void returnRoleListView(List<RoleBean> roleList) {
        mRoleList = roleList;
        int len = roleList.size();
        items = new String[len];
        for (int i = 0; i < len; i++) {
            RoleBean bean = roleList.get(i);
            String roleType = bean.getType();
            items[i] = roleType;
            mRoleMap.put(roleType, bean.getId());
        }
        ArrayWheelAdapter memberAdapter = new ArrayWheelAdapter(this, items);
        remarkInfoWlv.setViewAdapter(memberAdapter);
        remarkInfoWlv.setCurrentItem(selectIndex);
        remarkInfoWlv.addScrollingListener(this);
    }

    @Override
    public void updateRemarkSuccess() {
        finish();
    }

    @Override
    public void updateRemarkFailed() {
    }
}
