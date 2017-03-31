package com.cloudmachine.utils;

import android.app.Dialog;
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

import com.cloudmachine.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

import static com.cloudmachine.utils.WeChatShareUtil.weChatShareUtil;


public class ShareDialog extends Dialog {

    private final View view;
    private ImageView ivSession;
    private ImageView ivTimeline;
    private Button btnCancel;

    private String webpageUrl;
    private String msgTitle;
    private String msgDesc;
    private Bitmap msgBitmap;
    private boolean result = true;
    private Context mContext;
    private String sessionTitle;
    private String sessionDescription;
    private String sessionUrl;
    private Bitmap sessionThumb;
    private String title;
    private String description;
    private String url;
    private int imageSource = -1;
    private Bitmap mThumb;

    public ShareDialog(Context context,String webpageUrl,String msgTitle,String msgDesc,int resource) {
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
        initData();
    }

    /**
     * 初始化window窗口
     */
    private void initWindow(){
        Window window = getWindow();//当前弹窗所在的窗口对象
        WindowManager.LayoutParams attributes = window.getAttributes();

        attributes.gravity = Gravity.BOTTOM ;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;

        window.setAttributes(attributes);
    }

    private void initView(){
        ivSession = (ImageView) view.findViewById(R.id.iv_session);
        ivTimeline = (ImageView) view.findViewById(R.id.iv_timeline);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
    }

    private void initData(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 分享给微信朋友
        ivSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null != webpageUrl) {
                    sessionUrl = webpageUrl;
                }
                if (null != msgTitle) {
                    sessionTitle = msgTitle;
                }
                if (null != msgDesc) {
                    sessionDescription = msgDesc;
                }
                if (imageSource != -1) {
                    sessionThumb = BitmapFactory.decodeResource(mContext.getResources(), imageSource);
                } else {
                    sessionThumb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.corner);
                }

                result = weChatShareUtil.shareUrl(sessionUrl, sessionTitle, null, sessionDescription, SendMessageToWX.Req.WXSceneSession);
                if (!result) {
                    Toast.makeText(mContext, "没有检测到微信", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        // 分享到朋友圈
        ivTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (weChatShareUtil.isSupportWX()) {
                    if (null != webpageUrl) {
                        url = webpageUrl;
                    }
                    if (null != msgTitle) {
                        title = msgTitle;
                    }
                    if (null != msgDesc) {
                        description = msgDesc;
                    }
                    if (imageSource != -1) {
                        mThumb = BitmapFactory.decodeResource(mContext.getResources(), imageSource);
                    } else {
                        mThumb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.corner);
                    }
                    result = weChatShareUtil.shareUrl(url, title, mThumb, description, SendMessageToWX.Req.WXSceneTimeline);
                } else {
                    Toast.makeText(mContext, "手机上微信版本不支持分享到朋友圈", Toast.LENGTH_SHORT).show();
                }
                if (!result) {
                    Toast.makeText(mContext, "没有检测到微信", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }


}









/**
 * 测试阶段发起微信分享
 * @param type
 */
   /* private void shareTest(int type){
        String webpageUrl = "http://baidu.com";
        String msgTitle = "狗粮";
        String msgDesc = "吃狗粮";
        Bitmap msgBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.close);
        share(webpageUrl,msgTitle,msgDesc,type,msgBitmap);
    }*/


/**
 * 发起微信分享
 * @param webpageUrl 分享url
 * @param msgTitle  分享title
 * @param msgDesc   分享desc
 * @param type  分享类型,Session微信好友/Timeline朋友圈
 */
    /*private void share(String webpageUrl,String msgTitle,String msgDesc,int type,Bitmap msgBitmap){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = msgTitle;
        msg.description = msgDesc;
        Bitmap thumb = msgBitmap;
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = type;
        //MyApplication.instance.wxapi.sendReq(req);
    }*/