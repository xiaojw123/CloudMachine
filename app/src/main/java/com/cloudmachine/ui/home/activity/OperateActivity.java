package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.UserInfo;
import com.cloudmachine.utils.MemeberKeeper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 运营商授权
 */

public class OperateActivity extends BaseAutoLayoutActivity implements View.OnClickListener {
    @BindView(R.id.opeator_foraget_tv)
    TextView foragetTv;
    @BindView(R.id.operator_submit_btn)
    RadiusButtonView submitBtn;
    @BindView(R.id.operator_mobile_tv)
    TextView mobieTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);
        ButterKnife.bind(this);
        Member member = MemeberKeeper.getOauth(mContext);
        if (member!=null){
            mobieTv.setText(member.getMobile());
        }
        submitBtn.setOnClickListener(this);

    }

    @Override
    public void initPresenter() {

    }

    @OnClick(R.id.opeator_foraget_tv)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.opeator_foraget_tv:
                break;
            case R.id.radius_button_text:
                break;

        }


    }
}
