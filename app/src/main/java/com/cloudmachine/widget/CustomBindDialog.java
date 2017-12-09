package com.cloudmachine.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cloudmachine.R;

public class CustomBindDialog extends Dialog {

    private static final String BIND_WARN_MESSAGE = "为了您的账户安全，请先验证\n已发送验证码到%s手机";

    public CustomBindDialog(Context context) {
        super(context);
    }

    public CustomBindDialog(Context context, int theme) {
        super(context, theme);
    }


    public  static  class Builder {
        private Context context;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private View.OnClickListener codeGetBtnClickListener;
        private TextView mPositvieBtn;
        private TextView mNegativeBtn;
        private TextView mMessageTv;
        private String phoneNum;
        private TextView mGetcodeTv;
        private  TextView mAccountEdt;

        public Builder(Context context) {
            this.context = context;
        }


        public CustomBindDialog create() {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomBindDialog dialog = new CustomBindDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.custom_bind_dialog, null);
            initContentView(dialog, layout);
            dialog.setContentView(layout);
            dialog.setCancelable(false);
            return dialog;
        }

        private void initContentView(final CustomBindDialog dialog, View layout) {
            mGetcodeTv= (TextView) layout.findViewById(R.id.bind_getcode_tv);
            mAccountEdt= (TextView) layout.findViewById(R.id.bind_pay_acount_edt);
            mPositvieBtn = (TextView) layout.findViewById(R.id.positiveButton);
            mNegativeBtn = (TextView) layout.findViewById(R.id.negativeButton);
            mMessageTv = (TextView) layout.findViewById(R.id.bind_msg_tv);
            if (!TextUtils.isEmpty(phoneNum)) {
                mMessageTv.setText(String.format(BIND_WARN_MESSAGE, phoneNum));
            }
            if (codeGetBtnClickListener!=null){
                mGetcodeTv.setOnClickListener(codeGetBtnClickListener);
            }
            if (positiveButtonClickListener != null) {
                mPositvieBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        positiveButtonClickListener.onClick(dialog,
                                DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }


            if (negativeButtonClickListener != null) {
                mNegativeBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        negativeButtonClickListener.onClick(dialog,
                                DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            }
        }

        public TextView getCodeTv(){
            return mGetcodeTv;
        }


        public Builder setCodeGetListener(View.OnClickListener codeGetBtnClickListener){
            this.codeGetBtnClickListener=codeGetBtnClickListener;
            return this;
        }
        public String getInputCode(){
            return mAccountEdt.getText().toString();
        }
        public Builder setPhoneNum(String phoneNum) {
            int len = phoneNum.length();
            if (len > 5) {
                this.phoneNum = phoneNum.substring(0, 3) + "******" + phoneNum.substring(len - 2, len);
            } else {
                this.phoneNum = phoneNum;
            }
            return this;
        }


        public Builder setPositiveButtonClickListener(
                OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }


        public Builder setNegativeButtonClickListener(
                OnClickListener listener) {
            this.negativeButtonClickListener = listener;
            return this;
        }


    }

    @Override
    public void dismiss() {
        getContext();
        super.dismiss();
    }

}
