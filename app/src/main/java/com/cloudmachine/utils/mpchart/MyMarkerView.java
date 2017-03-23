
package com.cloudmachine.utils.mpchart;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.cloudmachine.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;

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
