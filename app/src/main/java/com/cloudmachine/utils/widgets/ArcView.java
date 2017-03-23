package com.cloudmachine.utils.widgets;


import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MathHelper;
import com.cloudmachine.utils.widgets.dashboard.DashboardView.StripeMode;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Interpolator;

public class ArcView extends View{

	private static float currentValue = 0f;
	private static final int mSweepAngle = 180;//总弧度多少
	private static final int mStartAngle = 180;//起始弧度
	private int mRadius;
	private float mCenterX;
    private float mCenterY;
    
    private int mArcColor; // 弧度颜色
    
    private RectF mRectStripe, mRectStripe2;
    private Paint mPaintPointer;
    
    private int mViewWidth; // 控件宽度
    private int mViewHeight; // 控件高度
    
    private int mMaxProgress = 100;

    private float mProgress = 0;

    private int mStripeWidth = 40;


    // 画圆所在的距形区域
    private final RectF mRectF,mRectF2;

    private final Paint mPaint;



    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRectF = new RectF();
        mRectF2 = new RectF();
        mPaint = new Paint();
        mPaintPointer = new Paint();
        mPaintPointer.setAntiAlias(true);
        init();
    }
    
    private void init(){
    	mRadius = dpToPx(80);
//    	mStripeWidth = dpToPx(150);
    	int totalRadius = mRadius;
    	mViewWidth = totalRadius * 2 + getPaddingLeft() + getPaddingRight() + dpToPx(2) * 2;
    	mViewHeight = totalRadius * 2 + getPaddingLeft() + getPaddingRight() + dpToPx(2) * 2;
    	mCenterX = mViewWidth / 2.0f;
        mCenterY = mViewHeight / 2.0f;
       int r = mRadius + dpToPx(1) - mStripeWidth / 2;
        mRectStripe = new RectF(mCenterX - r, mCenterY - r, mCenterX + r, mCenterY + r);
    }
    
    

    @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
    	int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mViewWidth = dpToPx(widthSize);
        } else {
            if (widthMode == MeasureSpec.AT_MOST)
                mViewWidth = Math.min(mViewWidth, widthSize);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mViewHeight = dpToPx(heightSize);
        } else {
            int totalRadius;
                totalRadius = mRadius;
                mViewHeight = totalRadius +  dpToPx(2) +
                        getPaddingTop() + getPaddingBottom() ;
            if (widthMode == MeasureSpec.AT_MOST)
                mViewHeight = Math.min(mViewHeight, widthSize);
        }

        setMeasuredDimension(mViewWidth, mViewHeight);
	}

	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        // 设置画笔相关属性
        mPaint.setAntiAlias(true);//抗锯齿功能
        mPaint.setColor(Color.rgb(0xe9, 0xe9, 0xe9));//设置画笔颜色    
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(mStripeWidth);
        mPaint.setStyle(Style.FILL);
        // 位置
        mRectF.left = mStripeWidth / 2; // 左上角x
        mRectF.top = mStripeWidth/2; // 左上角y
        mRectF.right = width - mStripeWidth / 2; // 左下角x
        mRectF.bottom = height - mStripeWidth / 2; // 右下角y
       
        mRectF2.left = mRectF.left;
        mRectF2.top = mRectF.top;
        mRectF2.right = mRectF.right*2;
        mRectF2.bottom = mRectF.bottom*2;
       /* mRectF.left = 100; // 左上角x
        mRectF.top = 0; // 左上角y
        mRectF.right = 200; // 左下角x
        mRectF.bottom = 200; // 右下角y
*/        
        // 绘制圆圈，进度条背景
        canvas.drawArc(mRectF2, mStartAngle, mSweepAngle, true, mPaint);
        mPaint.setColor(Color.rgb(0xf8, 0x60, 0x30));
        canvas.drawArc(mRectF, mStartAngle, ((float) mProgress / mMaxProgress) * mSweepAngle, true, mPaint);
/*
      //绘制三角形指针
        mPaintPointer.setStyle(Paint.Style.FILL);
        mPaintPointer.setColor(mArcColor);
        float[] point1 = getCoordinatePoint(mCircleRadius / 2, initAngle + 90);
        path.moveTo(point1[0], point1[1]);
        float[] point2 = getCoordinatePoint(mCircleRadius / 2, initAngle - 90);
        path.lineTo(point2[0], point2[1]);
        float[] point3 = getCoordinatePoint(mPointerRadius, initAngle);
        path.lineTo(point3[0], point3[1]);
        path.close();
        canvas.drawPath(path, mPaintPointer);
        // 绘制三角形指针底部的圆弧效果
        canvas.drawCircle((point1[0] + point2[0]) / 2, (point1[1] + point2[1]) / 2,
                mCircleRadius / 2, mPaintPointer);*/
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setProgress(int progress) {
//        this.mProgress = progress;
//        currentValue
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, progress);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float v) {
                return 1 - (1 - v) * (1 - v) * (1 - v);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//            	valueAnimator.getAnimatedValue()
            	Constants.MyLog(""+valueAnimator.getAnimatedValue());
            	mProgress = Float.valueOf(valueAnimator.getAnimatedValue().toString());
//                currentValue = (float) (valueAnimator.getAnimatedValue().toString());
                invalidate();
            }
        });
        valueAnimator.start();
//        this.invalidate();
    }

    public void setProgressNotInUiThread(int progress) {
        this.mProgress = progress;
        this.postInvalidate();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    /**
     * 依比例绘制扇区
     *
     * @param paintArc    画笔
     * @param cirX        x坐标
     * @param cirY        y坐标
     * @param radius      半径
     * @param offsetAngle 偏移
     * @param curretAngle 当前值
     * @throws Exception 例外
     */
    protected void drawPercent(Canvas canvas, Paint paintArc,
                               final float cirX,
                               final float cirY,
                               final float radius,
                               final float offsetAngle,
                               final float curretAngle) throws Exception {
        try {
            float arcLeft = sub(cirX , radius);
            float arcTop = sub(cirY , radius);
            float arcRight = add(cirX , radius);
            float arcBottom = add(cirY , radius);
            RectF arcRF0 = new RectF(arcLeft, arcTop, arcRight, arcBottom);
            //在饼图中显示所占比例
            canvas.drawArc(arcRF0, offsetAngle, curretAngle, true, paintArc);
        } catch (Exception e) {
            throw e;
        }
    }
    /**
	 * 加法运算
	 * @param v1 参数1
	 * @param v2 参数2
	 * @return 结果
	 */
	 protected float add(float v1, float v2) 
	 {
		 return MathHelper.getInstance().add(v1, v2);
	 }
	 /**
	  * 减法运算
	  * @param v1 参数1
	  * @param v2 参数2
	  * @return 运算结果
	  */
    protected float sub(float v1, float v2) 
	 {
		 return MathHelper.getInstance().sub(v1, v2);
	 }
	
}
