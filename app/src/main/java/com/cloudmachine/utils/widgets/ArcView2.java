package com.cloudmachine.utils.widgets;


import com.cloudmachine.R;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MathHelper;
import com.cloudmachine.utils.widgets.dashboard.DashboardView.StripeMode;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Interpolator;

public class ArcView2 extends View{

	private static final int mStartAngle = 180;//起始弧度
	private static final int mSweepAngle = 180;//总弧度多少
    
	private int mMinProgress = 0;
    private int mMaxProgress = 100;
    private float mProgress = 0;
    private int mBigRadius = 260;
    private int mSmallRadius = 100;
    
    private int mViewWidth; // 控件宽度
    private int mViewHeight; // 控件高度
    private float mCenterX;
    private float mCenterY;
    // 画圆所在的距形区域
    private RectF mBigRectF,mSmallRectF,mCurrentRectF;
    private Paint mBigPaint,mSmallPaint,mCurrentPaint;
    private Paint mPaintPointer;
    
    private float initAngle;
    private int mCircleRadius; // 中心圆半径
    private int mPointerRadius; // 指针半径
    private Path path;

    public ArcView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init(){
    	mBigRectF = new RectF();
    	mSmallRectF = new RectF();
    	mCurrentRectF = new RectF();
    	
    	mBigPaint = new Paint();
    	mBigPaint.setAntiAlias(true);//抗锯齿功能
    	mBigPaint.setStyle(Style.FILL);
    	mBigPaint.setStrokeWidth(0);
    	mBigPaint.setColor(getResources().getColor(R.color.chart_not));//设置画笔颜色    
    	
    	mCurrentPaint = new Paint();
    	mCurrentPaint.setAntiAlias(true);//抗锯齿功能
    	mCurrentPaint.setStyle(Style.FILL);
    	mBigPaint.setStrokeWidth(0);
    	mCurrentPaint.setColor(getResources().getColor(R.color.chart_yes));//设置画笔颜色    
    	
    	mSmallPaint = new Paint();
    	mSmallPaint.setAntiAlias(true);//抗锯齿功能
    	mSmallPaint.setStyle(Style.FILL);
    	mSmallPaint.setColor(getResources().getColor(R.color.white));//设置画笔颜色   
    	
    	mPaintPointer = new Paint();
        mPaintPointer.setAntiAlias(true);
        mPaintPointer.setStyle(Style.FILL);
        mPaintPointer.setColor(Color.parseColor("#35373e"));
        
        path = new Path();
        
        mBigRadius = (int)getResources().getDimension(R.dimen.oilamount_oil_big_radius);
        mSmallRadius = (int)getResources().getDimension(R.dimen.oilamount_oil_small_radius);
    	
        initAngle = getAngleFromResult(mProgress);
        mCircleRadius = mBigRadius/17;
        mPointerRadius = mBigRadius/5*4;
        
    	mViewWidth = mBigRadius * 2 + getPaddingLeft() + getPaddingRight() ;
    	mViewHeight = mBigRadius * 2  + getPaddingLeft() + getPaddingRight() ;
    	mCenterX = mViewWidth / 2.0f;
        mCenterY = mViewHeight / 2.0f;
        mBigRectF = new RectF(mCenterX-mBigRadius,mCenterX-mBigRadius,mCenterX+mBigRadius,mCenterX+mBigRadius);
        mSmallRectF = new RectF(mCenterX-mSmallRadius,mCenterX-mSmallRadius,mCenterX+mSmallRadius,mCenterX+mSmallRadius);
        mCurrentRectF = new RectF();
        mCurrentRectF.left = mBigRectF.left -1;
        mCurrentRectF.top = mBigRectF.top -1;
        mCurrentRectF.right = mBigRectF.right +1;
        mCurrentRectF.bottom = mBigRectF.bottom +1;
        
        
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
                totalRadius = mBigRadius;
                mViewHeight = totalRadius + mCircleRadius + /* dpToPx(2) +*/
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
        // 绘制圆圈，进度条背景
        canvas.drawArc(mBigRectF, mStartAngle, mSweepAngle, true, mBigPaint);
        canvas.drawArc(mCurrentRectF, mStartAngle, getArcAngleFromResult(mProgress), true, mCurrentPaint);
        canvas.drawArc(mSmallRectF, mStartAngle, mSweepAngle, true, mSmallPaint);
//        mPaint.setColor(Color.rgb(0xf8, 0x60, 0x30));
//        canvas.drawArc(mRectF, mStartAngle, ((float) mProgress / mMaxProgress) * mSweepAngle, false, mPaint);

      //绘制三角形指针
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
                mCircleRadius / 2, mPaintPointer);
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setProgress(int progress) {
//        this.mProgress = progress;
//      this.invalidate();
       ValueAnimator valueAnimator = ValueAnimator.ofFloat(mMinProgress, progress);
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
            	mProgress = Float.valueOf(valueAnimator.getAnimatedValue().toString());
            	init();
                invalidate();
            }
        });
        valueAnimator.start();

    }

    public void setProgressNotInUiThread(int progress) {
        this.mProgress = progress;
        this.postInvalidate();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    /**
     * 通过数值得到角度位置
     */
    private float getAngleFromResult(float result) {
    	if (result > mMaxProgress)
    		return mMaxProgress;
    	return mSweepAngle * (result - mMinProgress) / (mMaxProgress - mMinProgress) + mStartAngle;
    	
    }
    
    /**
     * 通过数值得到角度位置
     */
    private float getArcAngleFromResult(float result) {
    	if(result<=0)
    		return 0;
    	if(result>mMaxProgress)
    		return mSweepAngle;
    	return result/mMaxProgress*mSweepAngle;
    }
    /**
     * 依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
     */
    public float[] getCoordinatePoint(int radius, float cirAngle) {
        float[] point = new float[2];

        double arcAngle = Math.toRadians(cirAngle); //将角度转换为弧度
        if (cirAngle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (cirAngle == 90) {
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        } else if (cirAngle > 90 && cirAngle < 180) {
            arcAngle = Math.PI * (180 - cirAngle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (cirAngle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        } else if (cirAngle > 180 && cirAngle < 270) {
            arcAngle = Math.PI * (cirAngle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        } else if (cirAngle == 270) {
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        } else {
            arcAngle = Math.PI * (360 - cirAngle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }

        return point;
    }
}
