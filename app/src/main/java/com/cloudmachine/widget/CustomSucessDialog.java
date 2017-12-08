package com.cloudmachine.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;

public class CustomSucessDialog extends Dialog {

    public CustomSucessDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomSucessDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    public static class Builder {
        private Context context;
        private CharSequence message, title1, title2;
        private String positiveButtonText;
        private String netrualButtonText;
        private String negativeButtonText;
        private int resNegativeColor;
        private View contentView;
        private OnClickListener netrualButtonClickListener;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private TextView mNetrualBtn;
        private TextView mPositvieBtn;
        private TextView mNegativeBtn;
        private TextView mMessageTv;
        private LinearLayout mChooseBtnLayout;
        private ImageView mAlertImg;
        private int resId;
        private boolean isLeft;
        private TextView mTitle1Tv, mTitle2Tv;
        private LinearLayout mTitle2Layout;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle1(String title1) {
            this.title1 = title1;
            return this;
        }

        public Builder setTitle2(String title2) {
            this.title2 = title2;
            return this;
        }


        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public void setGravityLeft(boolean isLeft) {
            this.isLeft = isLeft;
        }

        public CustomSucessDialog create() {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomSucessDialog dialog = new CustomSucessDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.custom_success_dialog, null);
            initContentView(dialog, layout);
            dialog.setContentView(layout);
            return dialog;
        }


        private void initContentView(final CustomSucessDialog dialog, View layout) {
            mTitle1Tv = (TextView) layout.findViewById(R.id.custom_sucess_title1);
            mTitle2Tv = (TextView) layout.findViewById(R.id.custom_sucess_title2);
            mTitle2Layout = (LinearLayout) layout.findViewById(R.id.custom_sucess_title2_container);
            mAlertImg = (ImageView) layout.findViewById(R.id.alert_img);
            mNetrualBtn = (TextView) layout.findViewById(R.id.netrualButton);
            mPositvieBtn = (TextView) layout.findViewById(R.id.positiveButton);
            mNegativeBtn = (TextView) layout.findViewById(R.id.negativeButton);
            mMessageTv = (TextView) layout.findViewById(R.id.dialog_message);
            mChooseBtnLayout = (LinearLayout) layout.findViewById(R.id.chooseBtnCotainer);
            // set the content message
            if (resId != 0) {
                mAlertImg.setBackgroundResource(resId);
            }
            mTitle1Tv.setText(title1);
            mTitle2Tv.setText(title2);
            if (message != null) {
                if (isLeft) {
                    mMessageTv.setGravity(Gravity.LEFT);
                }
                mMessageTv.setText(message);
            }

            mNetrualBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     dialog.dismiss();
                    ((Activity)v.getContext()).finish();
                }
            });

//            if (netrualButtonText != null) {
//                mNetrualBtn.setVisibility(View.VISIBLE);
//                mChooseBtnLayout.setVisibility(View.GONE);
//                mNetrualBtn.setText(netrualButtonText);
//                if (netrualButtonClickListener != null) {
//                    mNetrualBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            netrualButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEUTRAL);
//                        }
//                    });
//                }
//            } else {
//                mNetrualBtn.setVisibility(View.GONE);
//            }
            // set the confirm button
            if (positiveButtonText != null) {
                mNetrualBtn.setVisibility(View.GONE);
                mChooseBtnLayout.setVisibility(View.VISIBLE);
                mPositvieBtn.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    mPositvieBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                mPositvieBtn.setVisibility(
                        View.GONE);
            }


            // set the cancel button
            if (negativeButtonText != null) {
                mNetrualBtn.setVisibility(View.GONE);
                mChooseBtnLayout.setVisibility(View.VISIBLE);
                if (resNegativeColor != 0) {
                    mNegativeBtn.setTextColor(resNegativeColor);
                }
                mNegativeBtn.setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    mNegativeBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                mNegativeBtn.setVisibility(
                        View.GONE);
            }
        }


        /**
         * Set the Dialog message from resource
         *
         * @param
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }


        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setNeutralButton(int nectrualButtonText,
                                        OnClickListener listener) {
            this.netrualButtonText = (String) context
                    .getText(nectrualButtonText);
            this.netrualButtonClickListener = listener;
            return this;
        }

        public Builder setAlertIcon(int resId) {
            this.resId = resId;
            return this;
        }

        public Builder setNeutralButton(String nectrualButtonText,
                                        OnClickListener listener) {
            this.netrualButtonText = nectrualButtonText;
            this.netrualButtonClickListener = listener;
            return this;
        }


        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int resNegativeColor, String negativeButtonText,
                                         OnClickListener listener) {
            this.resNegativeColor = resNegativeColor;
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

    }

}
