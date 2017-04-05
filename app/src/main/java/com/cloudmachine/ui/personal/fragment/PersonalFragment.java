package com.cloudmachine.ui.personal.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.ui.personal.contract.PersonalContract;
import com.cloudmachine.ui.personal.model.PersonalModel;
import com.cloudmachine.ui.personal.presenter.PersonalPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.WeChatShareUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/18 下午3:22
 * 修改人：shixionglu
 * 修改时间：2017/3/18 下午3:22
 * 修改备注：
 */

public class PersonalFragment extends BaseFragment<PersonalPresenter, PersonalModel> implements PersonalContract.View,Handler.Callback, View.OnClickListener {


    @BindView(R.id.title_layout)
    TitleView       mTitleLayout;  //标题头
    @BindView(R.id.head_iamge)
    CircleImageView mHeadIamge;    //圆形头像
    @BindView(R.id.nickname)
    TextView        mNickname;     //昵称
    @BindView(R.id.ll_personal_information)
    LinearLayout    mLlPersonalInformation; //个人信息页面
    @BindView(R.id.rl_qr_code)
    RelativeLayout  mRlQrCode;     //二维码
    @BindView(R.id.rl_coupon)
    RelativeLayout  mRlCoupon;     //优惠券
    @BindView(R.id.score_text)
    TextView        mScoreText;     //积分
    @BindView(R.id.about_and_help)
    RelativeLayout  mAboutAndHelp;  //关于与帮助
    @BindView(R.id.share_app)
    RelativeLayout  mShareApp;      //分享app
    @BindView(R.id.exitlayout)
    LinearLayout    mExitlayout;    //退出登录
    Unbinder unbinder;


    private Context         mContext;
    private Handler         mHandler;
    private WeChatShareUtil weChatShareUtil;//微信分享工具类
    private int infoType;
    private int infoMemberId;
    private boolean isMyInfo;

    //图片存储根目录
    private static final String IMAGE_PATH = Environment
            .getExternalStorageDirectory().toString()
            + File.separator
            + "cloudmachine" + File.separator + "images" + File.separator;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    //需要申请的权限数组
    static final         String[] PERMISSIONS         = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int      REQUEST_PICK_IMAGE  = 1; //相册选取
    private static final int      REQUEST_CAPTURE     = 2;  //拍照
    private static final int      REQUEST_PICTURE_CUT = 3;  //剪裁图片
    private static final int      REQUEST_PERMISSION  = 4;  //权限请求

    //分享信息
    private String sessionTitle       = "云机械";
    private String sessionDescription = "我的工程机械设备都在云机械APP，你的设备在哪里，赶紧加入吧！";
    private String sessionUrl         = "http://www.cloudm.com/yjx";

    @Override
    protected void initView() {

        init();
        initRootView();// 控件初始化
        initListener();
        weChatShareUtil = WeChatShareUtil.getInstance(getContext());
    }

    private void initListener() {

        mLlPersonalInformation.setOnClickListener(this);
    }

    private void initRootView() {

        mContext = getActivity();
        mHandler = new Handler(this);
        getIntentData();
        initTitleLayout();
        try {
            if (MemeberKeeper.getOauth(getActivity()).getId() != infoMemberId && infoType != 0) {
                isMyInfo = false;
            }
        } catch (Exception e) {
            isMyInfo = false;
        }


    }

    private void initTitleLayout() {

        mTitleLayout.setTitle("我");
    }

    private void getIntentData() {

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {

                infoType = bundle.getInt(Constants.P_MERMBERTYPE);
                infoMemberId = bundle.getInt(Constants.P_MERMBERID);
            } catch (Exception e) {
                Constants.MyLog(e.getMessage());
            }

        }
    }

    private void init() {

        mPermissionsChecker = new PermissionsChecker(this.getContext());
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_personal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_personal_information:

                break;
        }
    }
}
