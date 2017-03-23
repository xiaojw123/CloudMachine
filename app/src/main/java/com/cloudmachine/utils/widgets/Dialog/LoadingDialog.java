package com.cloudmachine.utils.widgets.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;


public class LoadingDialog extends Dialog {


	private static LoadingDialog customProgressDialog = null;
	private Context context;

	public LoadingDialog(Context context) {
		super(context);
		this.context = context;
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	public static LoadingDialog createDialog(Context context) {
			synchronized (LoadingDialog.class) {
				customProgressDialog = new LoadingDialog(context,
						R.style.CustomProgressDialog);
				customProgressDialog.setContentView(R.layout.inner_loadingdialog);
				customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
			}
		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}

		ImageView imageView = (ImageView) customProgressDialog
				.findViewById(R.id.logingdialog_imageview);
		RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setDuration(1 * 800);
		rotateAnimation.setRepeatCount(-1);
		rotateAnimation.setInterpolator(new LinearInterpolator());
		imageView.startAnimation(rotateAnimation);
	}

	/**
	 * 
	 * [Summary] setTitile ������������
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public LoadingDialog setTitile(String strTitle) {
		return customProgressDialog;
	}

	/**
	 * 
	 * [Summary] setMessage ������������������������
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public LoadingDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.loaingdialog_textview);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return customProgressDialog;
	}

}
