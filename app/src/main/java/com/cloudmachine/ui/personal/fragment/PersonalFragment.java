package com.cloudmachine.ui.personal.fragment;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.ui.personal.contract.PersonalContract;
import com.cloudmachine.ui.personal.model.PersonalModel;
import com.cloudmachine.ui.personal.presenter.PersonalPresenter;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.WeChatShareUtil;
import com.cloudmachine.utils.widgets.Dialog.LoadingDialog;
import com.cloudmachine.utils.widgets.RadiusButtonView;

import java.io.File;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/18 下午3:22
 * 修改人：shixionglu
 * 修改时间：2017/3/18 下午3:22
 * 修改备注：
 */

public class PersonalFragment extends BaseFragment<PersonalPresenter, PersonalModel> implements PersonalContract.View {


    private Context mContext;
    private Handler mHandler;

    private TextView textView4, textView5, score_text;
    private RelativeLayout nickLayout, set_qrcode, phoneLayout, set_suggest, about_cloud;
    private View         my_pwd;
    private LinearLayout exitlayout;
    private ImageView    imageView;
    private View         imageLayout;
    private String       imageFileName;
    private String uploadResult = "";
    private LoadingDialog progressDialog;
    private TitleView     title_layout;
    private final        String   LOG_TAG             = "UpdateInfoActivity";
    // 请求码
    private static final int      IMAGE_REQUEST_CODE  = 0;
    private static final int      CAMERA_REQUEST_CODE = 1;
    private static final int      RESULT_REQUEST_CODE = 2;
    private              String[] items               = new String[]{"选择本地图片", "拍照"};
    private              String[] itemSex             = new String[]{"男", "女"};

    private int infoSign = -1;

    private String backPic, frontPic, headPic;

    private int imgsign = -1;
    private int sex     = 1;
    private RadioButton radioButton1, radioButton2, radioButton3;
    private static final String IMAGE_PATH = Environment
            .getExternalStorageDirectory().toString()
            + File.separator
            + "cloudmachine" + File.separator + "images" + File.separator;
    private String imString;
    private int    infoType, signBetweenTime;
    private int infoMemberId;
    private boolean isMyInfo = true;
    private View             privacyInfo;
    private boolean          isUpdateImage;
    private PopupWindow      mpopupWindow;
    private RadiusButtonView score_button;
    private TextView         score_num;
    private View             score_layout;
    private ScrollView       scrollView;
    private boolean isIntegralAgain = true;

    private RelativeLayout     mAboutAndHelp;
    private RelativeLayout     mShareAPP;
    private WeChatShareUtil    weChatShareUtil;
    private RelativeLayout     rlCoupon;
    private Uri                imageUri;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    //需要申请的权限数组
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int REQUEST_PICK_IMAGE = 1; //相册选取
    private static final int REQUEST_CAPTURE = 2;  //拍照
    private static final int REQUEST_PICTURE_CUT = 3;  //剪裁图片
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    private boolean isClickCamera;
    private String imagePath;

    @Override
    protected void initView() {

        init();
        //initRootView();// 控件初始化
        weChatShareUtil = WeChatShareUtil.getInstance(getContext());
    }

    private void init() {

        mPermissionsChecker =  new PermissionsChecker(this.getContext());
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this,mModel);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_personal;
    }
}
