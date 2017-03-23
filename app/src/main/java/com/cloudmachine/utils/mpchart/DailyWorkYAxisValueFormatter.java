package com.cloudmachine.utils.mpchart;

import java.text.DecimalFormat;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class DailyWorkYAxisValueFormatter implements ValueFormatter {

	 private DecimalFormat mFormat;
	 private String u;
	    public DailyWorkYAxisValueFormatter(String u) {
	        mFormat = new DecimalFormat("###,###,###,##0.00");
	        this.u = u;
	    }

		@Override
		public String getFormattedValue(float value, Entry entry,
				int dataSetIndex, ViewPortHandler viewPortHandler) {
			// TODO Auto-generated method stub
			
			return ValueFormatUtil.getWorkTime(value,"");
		}
		
		
}
