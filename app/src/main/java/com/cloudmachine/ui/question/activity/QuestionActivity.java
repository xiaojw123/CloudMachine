package com.cloudmachine.ui.question.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.ui.question.contract.QuestionContract;
import com.cloudmachine.ui.question.model.QuestionModel;
import com.cloudmachine.ui.question.presenter.QuestionPresenter;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/28 下午4:54
 * 修改人：shixionglu
 * 修改时间：2017/3/28 下午4:54
 * 修改备注：
 */

public class QuestionActivity extends BaseAutoLayoutActivity<QuestionPresenter, QuestionModel> implements QuestionContract.View {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_question);
    }

    @Override
    public void initPresenter() {

    }
}
