package com.cloudmachine.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cloudmachine.R;
import com.cloudmachine.bean.ScreenInfo;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.utils.DensityUtil;


/**
 * User:lizhangqu(513163535@qq.com)
 * Date:2015-09-04
 * Time: 18:03
 */
public class PreviewBorderView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private int mScreenH;
    private int mScreenW;
    private int mSWidth;
    private Canvas mCanvas;
    private Paint mPaint;
    private Paint mPaintLine;
    private SurfaceHolder mHolder;
    private Thread mThread;
    private static final String DEFAULT_TIPS_TEXT = "请将身份证和表框四角对齐，调整好光线";
    private static final int DEFAULT_TIPS_TEXT_SIZE = 16;
    private static final int DEFAULT_TIPS_TEXT_COLOR = Color.GREEN;
    /**
     * 自定义属性
     */
    private float tipTextSize;
    private int tipTextColor;
    private String tipText;

    public PreviewBorderView(Context context) {
        this(context, null);
    }

    public PreviewBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewBorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    /**
     * 初始化自定义属性
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PreviewBorderView);
        try {
            tipTextSize = a.getDimension(R.styleable.PreviewBorderView_tipTextSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TIPS_TEXT_SIZE, getResources().getDisplayMetrics()));
            tipTextColor = a.getColor(R.styleable.PreviewBorderView_tipTextColor, DEFAULT_TIPS_TEXT_COLOR);
            tipText = a.getString(R.styleable.PreviewBorderView_tipText);
            if (tipText == null) {
                tipText = DEFAULT_TIPS_TEXT;
            }
        } finally {
            a.recycle();
        }


    }

    /**
     * 初始化绘图变量
     */
    private void init() {
        this.mHolder = getHolder();
        this.mHolder.addCallback(this);
        this.mHolder.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.WHITE);
        this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mPaintLine = new Paint();
        this.mPaintLine.setColor(tipTextColor);
        this.mPaintLine.setStrokeWidth(3.0F);
        setKeepScreenOn(true);
    }

    /**
     * 绘制取景框
     */
    private void draw() {
        try {
            this.mCanvas = this.mHolder.lockCanvas();
            this.mCanvas.drawARGB(100, 0, 0, 0);
            this.mScreenW = mScreenH * 4 / 3;
            int left = mScreenW / 2 - mScreenH * 2 / 3 + mScreenH / 6;
            int top = mScreenH / 6;
            int right = mScreenW / 2 + mScreenH * 2 / 3 - mScreenH / 6;
            int bottom = mScreenH * 5 / 6;
            int width = right - left;
            int height = bottom - top;
            int mLeft = (mSWidth - width) / 2;
            int mRight = mLeft + width;
            mPaintLine.setColor(getResources().getColor(R.color.c_ff8901));
            mCanvas.drawRect(new RectF(mLeft, top, mRight, bottom), mPaint);
            mCanvas.drawLine(mLeft, top, mLeft, top + 50, mPaintLine);
            mCanvas.drawLine(mLeft, top, mLeft + 50, top, mPaintLine);
            mCanvas.drawLine(mRight, top, mRight, top + 50, mPaintLine);
            mCanvas.drawLine(mRight, top, mRight - 50, top, mPaintLine);
            mCanvas.drawLine(mLeft, bottom, mLeft, bottom - 50, mPaintLine);
            mCanvas.drawLine(mLeft, bottom, mLeft + 50, bottom, mPaintLine);
            mCanvas.drawLine(mRight, bottom, mRight, bottom - 50, mPaintLine);
            mCanvas.drawLine(mRight, bottom, mRight - 50, bottom, this.mPaintLine);
            mPaintLine.setColor(Color.WHITE);
            mPaintLine.setTextSize(tipTextSize);
            mPaintLine.setAntiAlias(true);
            mPaintLine.setDither(true);
            float length = mPaintLine.measureText(tipText);
            float bound = (ScreenInfo.screen_width - bottom- tipTextSize)/ 2;
            mCanvas.drawText(tipText, mLeft + width / 2 - length / 2, bottom + bound, mPaintLine);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //获得宽高，开启子线程绘图
        mScreenW = getWidth();
        mScreenH = getHeight();
        mSWidth = mScreenW;
        mThread = new Thread(this);
        mThread.start();
        AppLog.print("surfaceCreated___w__" + mScreenW + "___h_" + mScreenH);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //停止线程
        try {
            mThread.interrupt();
            mThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //子线程绘图
        draw();
    }
}
