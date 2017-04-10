package com.cloudmachine.ui.personal.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudmachine.R;
import com.cloudmachine.activities.AboutCloudActivity;
import com.cloudmachine.activities.ViewCouponActivity;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.main.MainActivity;
import com.cloudmachine.struc.Member;
import com.cloudmachine.struc.ScoreInfo;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.ui.personal.activity.MyQRCodeActivity;
import com.cloudmachine.ui.personal.activity.PersonalDataActivity;
import com.cloudmachine.ui.personal.contract.PersonalContract;
import com.cloudmachine.ui.personal.model.PersonalModel;
import com.cloudmachine.ui.personal.presenter.PersonalPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ShareDialog;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.WeChatShareUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cloudmachine.R.id.exitlayout;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/18 下午3:22
 * 修改人：shixionglu
 * 修改时间：2017/3/18 下午3:22
 * 修改备注：
 */

public class PersonalFragment extends BaseFragment<PersonalPresenter, PersonalModel> implements PersonalContract.View, Handler.Callback, View.OnClickListener {


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
    @BindView(R.id.mobile)
    TextView       mMobile;
    @BindView(R.id.rl_score)
    RelativeLayout mRlScore;
    @BindView(R.id.exitLogin)
    Button         mExitLogin;
    @BindView(R.id.scrollView)
    ScrollView     mScrollView;
    @BindView(R.id.rl_myquestion)
    RelativeLayout mRlMyquestion;
    @BindView(R.id.rl_insurance_consulting)
    RelativeLayout mRlInsuranceConsulting;


    private Context         mContext;
    private Handler         mHandler;
    private WeChatShareUtil weChatShareUtil;//微信分享工具类
    private int             infoType;
    private int             infoMemberId;
    private long memberId = -1;
    private boolean     isMyInfo;
    private Member      mMember;
    private PopupWindow mpopupWindow;

    //分享信息
    private String sessionTitle       = "云机械";
    private String sessionDescription = "我的工程机械设备都在云机械APP，你的设备在哪里，赶紧加入吧！";
    private String sessionUrl         = "http://www.cloudm.com/yjx";

    @Override
    protected void initView() {

        initRootView();// 控件初始化
        initListener();
        initData();
        weChatShareUtil = WeChatShareUtil.getInstance(getContext());
    }

    private void initData() {

        memberId = MemeberKeeper.getOauth(getActivity()).getId();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getMemberInfoById(memberId);
    }

    private void initListener() {

        mLlPersonalInformation.setOnClickListener(this);
        mRlQrCode.setOnClickListener(this);
        mAboutAndHelp.setOnClickListener(this);
        mShareApp.setOnClickListener(this);
        mRlCoupon.setOnClickListener(this);
        mExitlayout.setOnClickListener(this);
        mRlMyquestion.setOnClickListener(this);
        mRlInsuranceConsulting.setOnClickListener(this);

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
            case R.id.ll_personal_information://跳转到个人资料页面
                if (mMember != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("memberInfo", mMember);
                    Constants.toActivity(this.getActivity(), PersonalDataActivity.class, bundle, false);
                }
                break;
            case R.id.rl_qr_code:
                Constants.toActivity(getActivity(), MyQRCodeActivity.class, null, false);
                break;
            case R.id.rl_coupon:
                //优惠券列表（待修改）
                Constants.toActivity(getActivity(), ViewCouponActivity.class, null, false);
                break;
            case R.id.rl_score:
                mPresenter.getUserScoreInfo(memberId);
                break;
            case R.id.about_and_help:
                //关于与帮助页面
                Constants.toActivity(getActivity(), AboutCloudActivity.class, null);
                break;
            case R.id.share_app:
                ShareDialog shareDialog = new ShareDialog(this.getContext(), sessionUrl, sessionTitle, sessionDescription, -1);
                shareDialog.show();
                MobclickAgent.onEvent(mContext, UMengKey.count_share_app);
                break;
            case exitlayout:
                showPopMenu();
                break;
            case R.id.bt_cancel:
                mpopupWindow.dismiss();
                break;
            case R.id.bt_exitLogin:
                mpopupWindow.dismiss();
                Constants.isMcLogin = true;
                JPushInterface.setAliasAndTags(getActivity().getApplicationContext(), "", null, null);
                MemeberKeeper.clearOauth(getActivity());
                ((MainActivity) getActivity()).loginOut();
                break;
            case R.id.rl_myquestion:
                Constants.MyLog("进来了，，么@@@@@@@@");
                Constants.MyLog("拿到的挖机大师id"+MemeberKeeper.getOauth(getActivity()).getWjdsId());
                if (MemeberKeeper.getOauth(getActivity()).getWjdsId() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url"," http://h5.test.cloudm.com/n/ask_myq");
                    Constants.toActivity(getActivity(), QuestionCommunityActivity.class,bundle,false);
                }
                break;
            case R.id.rl_insurance_consulting:
                break;
        }
    }

    @Override
    public void returnMemberInfo(Member member) {
        this.mMember = member;
        if (mMember != null) {
            //序列化信息到本地
            MemeberKeeper.saveOAuth(member, mContext);
            String logo = mMember.getLogo();
            Glide.with(mContext).load(logo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .error(R.drawable.default_img)
                    .into(mHeadIamge);
            String name = mMember.getName();
            mNickname.setText(name);
            String mobile = mMember.getMobile();
            mMobile.setText(mobile);
        }
    }

    //拿到用户积分信息
    @Override
    public void returnUserScoreInfo(ScoreInfo scoreInfo) {
        if (scoreInfo != null) {
            mScoreText.setText(scoreInfo.getPoint());
        }
    }

    private void showPopMenu() {
        View view = View.inflate(getActivity().getApplicationContext(), R.layout.popup_menu, null);
        Button bt_cancle = (Button) view.findViewById(R.id.bt_cancel);
        Button bt_exitLogin = (Button) view.findViewById(R.id.bt_exitLogin);
        bt_cancle.setOnClickListener(this);
        bt_exitLogin.setOnClickListener(this);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mpopupWindow.dismiss();
            }
        });

        view.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in));
        LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.push_bottom_in));

        if (mpopupWindow == null) {
            mpopupWindow = new PopupWindow(getActivity());
            mpopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mpopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            mpopupWindow.setBackgroundDrawable(new BitmapDrawable());

            mpopupWindow.setFocusable(true);
            mpopupWindow.setOutsideTouchable(true);
            mpopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        mpopupWindow.setContentView(view);
        mpopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mpopupWindow.update();
    }

}
