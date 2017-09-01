package com.cloudmachine.utils;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

import static com.cloudmachine.utils.WeChatShareUtil.weChatShareUtil;


public class ShareDialog extends Dialog implements View.OnClickListener {
    private final View view;
    private String webpageUrl;
    private String msgTitle;
    private String msgDesc;
    private Context mContext;
    private int imageSource = -1;
    private String iconUrl;

    public ShareDialog(Context context, String webpageUrl, String msgTitle, String msgDesc, int resource) {
        super(context, R.style.ShareDialog);

        this.webpageUrl = webpageUrl;
        this.msgTitle = msgTitle;
        this.msgDesc = msgDesc;
        this.imageSource = resource;
        this.mContext = context;

        view = getLayoutInflater().inflate(R.layout.widget_dialog_share, null);
        setContentView(view);
        initWindow();
        initView();
    }

    public ShareDialog(Context context, String webpageUrl, String msgTitle, String msgDesc, String icon) {
        super(context, R.style.ShareDialog);

        this.webpageUrl = webpageUrl;
        this.msgTitle = msgTitle;
        this.msgDesc = msgDesc;
        this.iconUrl = icon;
        this.mContext = context;

        view = getLayoutInflater().inflate(R.layout.widget_dialog_share, null);
        setContentView(view);
        initWindow();
        initView();
    }

    /**
     * 初始化window窗口
     */
    private void initWindow() {
        Window window = getWindow();//当前弹窗所在的窗口对象
        WindowManager.LayoutParams attributes = window.getAttributes();

        attributes.gravity = Gravity.BOTTOM;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;

        window.setAttributes(attributes);
    }

    private void initView() {
        if (weChatShareUtil == null) {
            weChatShareUtil = WeChatShareUtil.getInstance(mContext);
        }
        ImageView ivSession = (ImageView) view.findViewById(R.id.iv_session);
        ImageView ivTimeline = (ImageView) view.findViewById(R.id.iv_timeline);
        ImageView ivCopyLink= (ImageView) view.findViewById(R.id.iv_copylink);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        ivSession.setOnClickListener(this);
        ivTimeline.setOnClickListener(this);
        ivCopyLink.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.iv_copylink:
//                webpageUrl
                ClipboardManager cm= (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(webpageUrl);
                ToastUtils.showCenterToast(v.getContext(),"复制成功");
                dismiss();
                break;
            case R.id.iv_session:  // 分享给微信朋友
                boolean result1 = true;
                try {
                    result1 = weChatShareUtil.shareUrl(webpageUrl, msgTitle, null, msgDesc, SendMessageToWX.Req.WXSceneSession);
                } catch (Exception e) {
                    result1 = false;
                }
                if (!result1) {
                    Toast.makeText(mContext, "分享失败!", Toast.LENGTH_SHORT).show();
                }
                dismiss();
                break;
            case R.id.iv_timeline: // 分享到朋友圈
                boolean result2 = true;
                if (weChatShareUtil.isSupportWX()) {
                    Bitmap mThumb;
                    if (imageSource != -1) {
                        mThumb = BitmapFactory.decodeResource(mContext.getResources(), imageSource);
                    } else {
                        mThumb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.corner);
                    }

                    if (iconUrl != null) {
                        try {
                            mThumb = Glide.with(mContext)
                                    .load(iconUrl)
                                    .asBitmap()
                                    .centerCrop()
                                    .into(20, 20)
                                    .get();
                        } catch (Exception e) {
                            e.printStackTrace();
                            result2 = false;
                        }
                    }
                    try {
                        result2 = weChatShareUtil.shareUrl(webpageUrl, msgTitle, mThumb, msgDesc, SendMessageToWX.Req.WXSceneTimeline);
                    } catch (Exception e) {
                        result2 = false;
                    }
                } else {
                    Toast.makeText(mContext, "手机上微信版本不支持分享到朋友圈", Toast.LENGTH_SHORT).show();
                }
                if (!result2) {
                    Toast.makeText(mContext, "没有检测到微信", Toast.LENGTH_SHORT).show();
                }
                dismiss();
                break;
        }


    }
}
