
package com.cloudmachine.utils.mpchart;

import android.content.Context;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.chart.components.MarkerView;
import com.cloudmachine.chart.data.CandleEntry;
import com.cloudmachine.chart.data.Entry;
import com.cloudmachine.chart.highlight.Highlight;
import com.cloudmachine.chart.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private ArrayList<String> xData;
    public MyMarkerView(Context context, int layoutResource,List<String> xData) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.tvContent);
        this.xData = (ArrayList<String>)xData;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

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
    	
        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
//        	String xD = "";
//        	if(null != xData && xData.size()> e.getXIndex() && null != xData.get(e.getXIndex())){
//        		xD = xData.get(e.getXIndex());
//        	}
            tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true) );
        }
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
