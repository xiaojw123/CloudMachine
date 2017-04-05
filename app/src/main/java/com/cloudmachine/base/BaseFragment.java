package com.cloudmachine.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudmachine.base.baserx.RxManager;
import com.cloudmachine.utils.LoadingDialog;
import com.cloudmachine.utils.TUtil;
import com.cloudmachine.utils.UMListUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 项目名称：CloudMachine
 * 类描述：Fragment基类的封装
 * 创建人：shixionglu
 * 创建时间：2017/3/16 下午5:06
 * 修改人：shixionglu
 * 修改时间：2017/3/16 下午5:06
 * 修改备注：
 */

public abstract class BaseFragment<T extends BasePresenter, E extends BaseModel> extends Fragment {


    public  T         mPresenter;
    public  E         mModel;
    public  RxManager mRxManager;
    public View      viewParent;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        UMListUtil.getUMListUtil().sendStruEvent(this.getClass().getSimpleName(), this.getActivity());
        if (null != viewParent) {
            ViewGroup parent = (ViewGroup) viewParent.getParent();
            if (null != parent) {
                parent.removeView(viewParent);
            }
        } else {
            viewParent = inflater.inflate(getLayoutResource(), null);
            mUnbinder = ButterKnife.bind(this, viewParent);
            mRxManager = new RxManager();
            mPresenter = TUtil.getT(this, 0);
            mModel = TUtil.getT(this, 1);
            if (mPresenter != null) {
                mPresenter.mContext = this.getActivity();
            }
            UMListUtil.getUMListUtil().sendStruEvent(this.getClass().getSimpleName(), getActivity());
            initPresenter();
            initView();
        }

        return viewParent;
    }

    protected abstract void initView();

    protected abstract void initPresenter();

    protected abstract int getLayoutResource();

    @Override
    public void onResume() {
        super.onResume();
        UMListUtil.getUMListUtil().startCustomEvent(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        UMListUtil.getUMListUtil().endCustomEvent(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        UMListUtil.getUMListUtil().removeList(this.getClass().getSimpleName());
        if (mPresenter != null)
            mPresenter.onDestroy();
        mRxManager.clear();
    }

    /**
     * 开启加载进度条
     */
    public void startProgressDialog() {
        LoadingDialog.showDialogForLoading(getActivity());
    }

    /**
     * 开启加载进度条
     *
     * @param msg
     */
    public void startProgressDialog(String msg) {
        LoadingDialog.showDialogForLoading(getActivity(), msg, true);
    }

    /**
     * 停止加载进度条
     */
    public void stopProgressDialog() {
        LoadingDialog.cancelDialogForLoading();
    }




}
