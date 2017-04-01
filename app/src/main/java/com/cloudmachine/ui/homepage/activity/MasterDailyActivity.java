package com.cloudmachine.ui.homepage.activity;

import android.os.Bundle;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.recyclerbean.MasterDailyBean;
import com.cloudmachine.ui.homepage.contract.MasterDailyContract;
import com.cloudmachine.ui.homepage.model.MasterDailyModel;
import com.cloudmachine.ui.homepage.presenter.MasterDailyPresenter;
import com.cloudmachine.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：CloudMachine
 * 类描述：大师日报列表
 * 创建人：shixionglu
 * 创建时间：2017/3/30 下午5:08
 * 修改人：shixionglu
 * 修改时间：2017/3/30 下午5:08
 * 修改备注：
 */

public class MasterDailyActivity extends BaseAutoLayoutActivity<MasterDailyPresenter, MasterDailyModel> implements MasterDailyContract.View {

    @BindView(R.id.title_layout)
    TitleView mTitleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_daily);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        mTitleLayout.setTitle("大师日报");
        mPresenter.getMasterDailyInfo(1,20);
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }


    @Override
    public void returnMasterDailyInfo(BaseRespose<List<MasterDailyBean>> masterDailyBeanBaseResposeList) {
        Constants.MyLog("走到这里");
    }
}
