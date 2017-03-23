package com.cloudmachine.utils.widgets;



import com.cloudmachine.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.TypedValue;
import android.view.View;

public class MapMarkerView extends View{

	private Context m_context;  
	private int mViewWidth = 22; // 控件宽度
    private int mViewHeight = 22; // 控件高度
    public MapMarkerView(Context context) {  
        super(context);  
        // TODO Auto-generated constructor stub  
          
        m_context=context;  
        mViewHeight = mViewWidth = (int)getResources().getDimension(R.dimen.mc_map_one_point_radius);
//        invalidate();
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
        } 

        setMeasuredDimension(mViewWidth, mViewHeight);
	}
    //重写OnDraw（）函数，在每次重绘时自主实现绘图  
    @Override  
    protected void onDraw(Canvas canvas) {  
        // TODO Auto-generated method stub  
        super.onDraw(canvas);  
          
          
        //设置画笔基本属性  
        Paint paint=new Paint();  
        paint.setAntiAlias(true);//抗锯齿功能  
        paint.setColor(Color.parseColor("#0c82fb"));  //设置画笔颜色      
        paint.setStyle(Style.FILL);//设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE  
        paint.setStrokeWidth(5);//设置画笔宽度  
          
        //设置画布背景颜色       
          
        //画圆  
        canvas.drawCircle(mViewWidth/2, mViewWidth/2, mViewWidth/2, paint);      
    }  
    
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
