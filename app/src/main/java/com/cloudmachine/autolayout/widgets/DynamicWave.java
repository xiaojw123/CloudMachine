package com.cloudmachine.autolayout.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.cloudmachine.autolayout.utils.AutoUtils;
import com.cloudmachine.R;

public class DynamicWave extends View {

	private static final int TEXTSIZE = 30;
    // 波纹颜色
    private static final int WAVE_PAINT_COLOR = 0x884cb7e8;//0000aa   8bd1f0
    // y = Asin(wx+b)+h
    private static final float STRETCH_FACTOR_A = 5;
    private static final int OFFSET_Y = 0;
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 3;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 1;
    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;
    private float[] mYPositions;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOneOffset;
    private int mXTwoOffset;

    private Paint mWavePaint;
    private DrawFilter mDrawFilter;
    
    private Paint mPicPaint;
    private int waveH = 0;
    private PorterDuffXfermode mPorterDuffXfermode;
    private Bitmap mMaskBitmap;
    private Rect mMaskSrcRect, mMaskDestRect;
    
    private Paint mBigCirclePaint;
    private Paint mTextPaint;
    private int lave;
    private int padding = AutoUtils.getPercentHeightSizeBigger(5);
    
    public DynamicWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = AutoUtils.getPercentHeightSizeBigger(TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = AutoUtils.getPercentHeightSizeBigger(TRANSLATE_X_SPEED_TWO);

        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Style.FILL);
        // 设置画笔颜色
        mWavePaint.setColor(WAVE_PAINT_COLOR);
        // 防抖动
        mWavePaint.setDither(true);
        // 开启图像过滤
        mWavePaint.setFilterBitmap(true);
        
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        
        mPicPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPicPaint.setDither(true);
        mPicPaint.setColor(Color.RED);
        mMaskBitmap = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.circle_500))
                .getBitmap();
        
        mBigCirclePaint = new Paint(); 
        mBigCirclePaint.setStyle(Style.STROKE);//设置空心
        
        
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);// 设置抗锯齿;;;;
        mTextPaint.setColor(Color.parseColor("#0096e0"));
        mTextPaint.setTextSize(AutoUtils.getPercentHeightSizeBigger(TEXTSIZE));
        
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        canvas.drawColor(Color.TRANSPARENT);
        
        int sc = canvas.saveLayer(0, 0, mTotalWidth, mTotalHeight, null, Canvas.ALL_SAVE_FLAG);
        
        resetPositonY();
        for (int i = 0; i < mTotalWidth; i++) {

            // 减400只是为了控制波纹绘制的y的在屏幕的位置，大家可以改成一个变量，然后动态改变这个变量，从而形成波纹上升下降效果
            // 绘制第一条水波纹
            canvas.drawLine(i, mTotalHeight - mResetOneYPositions[i] - waveH, i,
                    mTotalHeight,
                    mWavePaint);

            // 绘制第二条水波纹
            canvas.drawLine(i, mTotalHeight - mResetTwoYPositions[i] - waveH, i,
                    mTotalHeight,
                    mWavePaint);
        }

        // 改变两条波纹的移动点
        mXOneOffset += mXOffsetSpeedOne;
        mXTwoOffset += mXOffsetSpeedTwo;

        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }
        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0;
        }
     // 设置图像的混合模式
        mPicPaint.setXfermode(mPorterDuffXfermode);
        // 绘制遮罩圆
        canvas.drawBitmap(mMaskBitmap, mMaskSrcRect, mMaskDestRect,
        		mPicPaint);
        mPicPaint.setXfermode(null);
     
        mBigCirclePaint.setColor(Color.parseColor("#ffffff"));
        mBigCirclePaint.setStrokeWidth(padding);  
        canvas.drawCircle(mTotalWidth/2, mTotalHeight/2, mTotalWidth/2, mBigCirclePaint);// 大圆  
        
        mBigCirclePaint.setColor(Color.parseColor("#55bbe7"));
        mBigCirclePaint.setStrokeWidth(padding);  
        canvas.drawCircle(mTotalWidth/2, mTotalHeight/2, mTotalWidth/2-padding, mBigCirclePaint);// 中白圆
        
        mBigCirclePaint.setColor(Color.parseColor("#ffffff"));
        mBigCirclePaint.setStrokeWidth(padding);  
        canvas.drawCircle(mTotalWidth/2, mTotalHeight/2, mTotalWidth/2-padding*2, mBigCirclePaint);// 中白圆
        
        Rect rect = new Rect();
        String str = lave+"%";
        if (!TextUtils.isEmpty(str)) {
        	mTextPaint.getTextBounds(str, 0, str.length(), rect);
			int x = (getWidth() / 2) - rect.centerX();// 让现实的字体处于中心位置;;
			int y = (getHeight() / 2) - rect.centerY();// 让显示的字体处于中心位置;;
			canvas.drawText(str, x, y, mTextPaint);
		}
        
        canvas.restoreToCount(sc);
        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        postInvalidate();
    }

    private void resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

        int yTwoInterval = mYPositions.length - mXTwoOffset;
        System.arraycopy(mYPositions, mXTwoOffset, mResetTwoYPositions, 0,
                yTwoInterval);
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w;
        mTotalHeight = h;
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[mTotalWidth];
        // 用于保存波纹二的y值
        mResetTwoYPositions = new float[mTotalWidth];

        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);

        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[i] = (float) (((float)AutoUtils.getPercentHeightSizeBigger((int)STRETCH_FACTOR_A)) * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
        
        int maskWidth = mMaskBitmap.getWidth();
        int maskHeight = mMaskBitmap.getHeight();
        mMaskSrcRect = new Rect(0, 0, maskWidth, maskHeight);
        mMaskDestRect = new Rect(0, 0, mTotalWidth, mTotalHeight);
        
        setLave(lave);
    }

    public void setLave(int lave){
    	this.lave = lave;
    	if(lave>0){
    		float f = ((float)lave)/100;
    		waveH = (int)(f * mTotalHeight);
    	}
    	
    }
}
