
package com.cloudmachine.utils.mpchart;

import android.content.Context;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.bean.ScanningOilLevelInfo;
import com.cloudmachine.chart.components.MarkerView;
import com.cloudmachine.chart.data.Entry;
import com.cloudmachine.chart.highlight.Highlight;
import com.cloudmachine.chart.utils.AppLog;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private ScanningOilLevelInfo[] mOilLeveArray;

    public MyMarkerView(Context context, int layoutResource, ScanningOilLevelInfo[] oilLeveArray, boolean isFlag) {
        super(context, layoutResource, isFlag);
        mOilLeveArray = oilLeveArray;

        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        AppLog.print("index__" + highlight.getDataSetIndex());

    	/*if (e instanceof BarEntry) {

            BarEntry be = (BarEntry) e;

            if(be.getVals() != null) {

                // draw the stack value
                tvContent.setText("" + Utils.formatNumber(be.getVals()[highlight.getStackIndex()], 0, true));
            } else {
                tvContent.setText("" + Utils.formatNumber(be.getVal(), 0, true));
            }
        } else {

            tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));
        }*/
        if (mOilLeveArray != null && mOilLeveArray.length > e.getXIndex()) {
            ScanningOilLevelInfo oilLevel = mOilLeveArray[e.getXIndex()];
            if (oilLevel != null) {
                tvContent.setText(oilLevel.getLevel() + "% " + oilLevel.getTime());
            }
        }
//
//        if (e instanceof CandleEntry) {
//
//            CandleEntry ce = (CandleEntry) e;
//
//            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
//        } else {
////        	String xD = "";
////        	if(null != xData && xData.size()> e.getXIndex() && null != xData.get(e.getXIndex())){
////        		xD = xData.get(e.getXIndex());
////        	}
//            tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true) );
//        }
    }

    @Override
    public int getXOffset() {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset() {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}
