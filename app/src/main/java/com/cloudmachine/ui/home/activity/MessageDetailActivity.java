package com.cloudmachine.ui.home.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.MessageBO;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.widget.CommonTitleView;

public class MessageDetailActivity extends BaseAutoLayoutActivity {
    public static final String MESSAGE_ID = "message_id";
    public static final String MESSAGE_CONTENT = "message_content";
    public static final String MESSAGE_TIME = "message_time";
    public static final String MESSAGE_TITLE = "message_title";

    TextView mContentTv;
    TextView mTimeTv;
    CommonTitleView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        initView();
        Intent intent = getIntent();
        String id = intent.getStringExtra(MESSAGE_ID);
        String title = intent.getStringExtra(MESSAGE_TITLE);
        if (!TextUtils.isEmpty(title)) {
            mTitleView.setTitleName(title);
        }
        if (!TextUtils.isEmpty(id)) {
            obtainDetailById(id);
        } else {
            String content = intent.getStringExtra(MESSAGE_CONTENT);
            String time = intent.getStringExtra(MESSAGE_TIME);
            updateMessageDetailView(content, time);
        }
    }

    private void initView() {
        mTitleView = (CommonTitleView) findViewById(R.id.message_detail_title);
        mContentTv = (TextView) findViewById(R.id.message_detail_content);
        mTimeTv = (TextView) findViewById(R.id.message_detail_time);

    }

    private class MyURLSpan extends ClickableSpan {

        private String url;

        public MyURLSpan(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(MessageDetailActivity.this, QuestionCommunityActivity.class);
            intent.putExtra(QuestionCommunityActivity.H5_URL, url);
            startActivity(intent);
        }

    }

    private void updateMessageDetailView(String content, String time) {
        mTimeTv.setText(time);
        mContentTv.setText(content);
        if (TextUtils.isEmpty(content)) {
            return;
        }
        CharSequence text = mContentTv.getText();
        if (text instanceof Spannable) {
            int length = text.length();
            Spannable sp = (Spannable) text;
            URLSpan urls[] = sp.getSpans(0, length, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();
            for (URLSpan urlSpan : urls) {
                int start = sp.getSpanStart(urlSpan);
                int end = sp.getSpanEnd(urlSpan);
                MyURLSpan myURLSpan = new MyURLSpan(urlSpan.getURL());
                StyleSpan span = new StyleSpan(Typeface.BOLD);
                //这个一定要记得设置，不然点击不生效
                mContentTv.setMovementMethod(LinkMovementMethod.getInstance());
                AppLog.print("Span start__" + start + ", end__" + end);
                style.setSpan(myURLSpan, start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                style.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setTitleBlack(style, 0);
            mContentTv.setText(style);
        } else {
            if (content.contains("\"")) {
                SpannableStringBuilder spannableStr = new SpannableStringBuilder(text);
                setTitleBlack(spannableStr, 0);
                mContentTv.setText(spannableStr);
            }
        }
    }

    public void setTitleBlack(SpannableStringBuilder style, int index) {
        String content = style.toString();
        String[] formartText = content.split("\"");
        for (int i = 0; i < formartText.length - 1; i += 2) {
            int start = formartText[i].length() + content.indexOf(formartText[i]);
            int end = formartText[i + 1].length() + content.indexOf(formartText[i + 1]) + 1;
            style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.cor8)), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }


    private void obtainDetailById(String messageId) {
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).getMessageByid(messageId).compose(RxHelper.<MessageBO>handleResult()).subscribe(new RxSubscriber<MessageBO>(mContext) {
            @Override
            protected void _onNext(MessageBO messageDetail) {
                if (messageDetail != null) {
                    updateMessageDetailView(messageDetail.getContent(), messageDetail.getInviteTime());
                }

            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }

    @Override
    public void initPresenter() {

    }
}
