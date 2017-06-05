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
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.wheelview.OnWheelScrollListener;
import com.cloudmachine.utils.widgets.wheelview.WheelView;
import com.cloudmachine.utils.widgets.wheelview.adapter.ArrayWheelAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RemarkInfoActivity extends BaseAutoLayoutActivity implements OnWheelScrollListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        remarkinfoTitleview.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        remarkinfoTitleview.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(v.getContext(), "保存...");
                finish();
            }
        });
        items = new String[]{"白领", "金陵", "蓝田", "朱雀"};
        ArrayWheelAdapter memberAdapter = new ArrayWheelAdapter(this, items);
        remarkInfoWlv.setViewAdapter(memberAdapter);
        remarkInfoWlv.setCurrentItem(selectIndex);
        remarkInfoWlv.addScrollingListener(this);
    }

    @Override
    public void initPresenter() {

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

}
