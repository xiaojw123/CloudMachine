package com.cloudmachine.utils.mpchart;

import java.text.DecimalFormat;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

public class MyYAxisValueFormatter implements YAxisValueFormatter {

	 private DecimalFormat mFormat;
	 private String u;
	    public MyYAxisValueFormatter(String u) {
	        mFormat = new DecimalFormat("###,###,###,##0");
	        this.u = u;
	    }

	    @Override
	    public String getFormattedValue(float value, YAxis yAxis) {
	        return mFormat.format(value) + " "+u;
	    }
}
