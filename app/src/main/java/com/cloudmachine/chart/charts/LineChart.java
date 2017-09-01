
package com.cloudmachine.chart.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.cloudmachine.chart.data.Entry;
import com.cloudmachine.chart.data.LineData;
import com.cloudmachine.chart.highlight.Highlight;
import com.cloudmachine.chart.interfaces.LineDataProvider;
import com.cloudmachine.chart.renderer.LineChartRenderer;

/**
 * Chart that draws lines, surfaces, circles, ...
 * 
 * @author Philipp Jahoda
 */
public class LineChart extends BarLineChartBase<LineData> implements LineDataProvider {

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new LineChartRenderer(this, mAnimator, mViewPortHandler);
    }

    @Override
    protected void calcMinMax() {
        super.calcMinMax();

        if (mDeltaX == 0 && mData.getYValCount() > 0)
            mDeltaX = 1;
    }
    
    @Override
    public LineData getLineData() {
        return mData;
    }
    
    public float[] getMarkerPosition(Entry e, Highlight highlight) {
    	return super.getMarkerPosition(e, highlight);
    	
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if(mRenderer != null && mRenderer instanceof LineChartRenderer) {
            ((LineChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }
}
