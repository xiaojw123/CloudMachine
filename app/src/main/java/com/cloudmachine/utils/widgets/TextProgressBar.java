package com.cloudmachine.utils.widgets;

/**
 * Created by shixionglu on 2016/10/16.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.cloudm.autolayout.utils.AutoUtils;

/*****
 * 
 * @author lixiaodaoaaa http://weibo.com/lixiaodaoaaa
 *         http://t.qq.com/lixiaodaoaaa
 * 
 */
public class TextProgressBar extends ProgressBar {
	private static final int TEXTSIZE = 30;
	// private String str;
	private Paint mPaint;
	private String str = "0时";

	public TextProgressBar(Context context) {
		super(context);
		initText();
	}

	public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initText();
	}

	public TextProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initText();
	}

	@Override
	public void setProgress(int progress) {
		// setText(progress);
		super.setProgress(progress);

	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect rect = new Rect();
		if (!TextUtils.isEmpty(str)) {
			this.mPaint.getTextBounds(this.str, 0, this.str.length(), rect);
			int x = (getWidth() / 2) - rect.centerX();// 让现实的字体处于中心位置;;
			int y = (getHeight() / 2) - rect.centerY();// 让显示的字体处于中心位置;;
			canvas.drawText(this.str, x, y, this.mPaint);
		}
		// canvas.drawText(mActivity.str,x,y,this.mPaint);
	}

	// 初始化，画笔
	private void initText() {
		this.setMax(24);
		this.mPaint = new Paint();
		this.mPaint.setAntiAlias(true);// 设置抗锯齿;;;;
		this.mPaint.setColor(Color.parseColor("#0b807e"));
		this.mPaint.setTextSize(AutoUtils.getPercentHeightSizeBigger(TEXTSIZE));
	}

	// 设置文字内容
	public void setText(String progress) {

		// int i = (int) ((progress * 1.0f / this.getMax()) * 100);
		// this.str = String.valueOf(i) + "%";
		this.str = progress;

	}

}