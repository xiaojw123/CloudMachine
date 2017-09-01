package com.cloudmachine.utils.mpchart;

import com.cloudmachine.chart.data.Entry;
import com.cloudmachine.chart.formatter.ValueFormatter;
import com.cloudmachine.chart.utils.ViewPortHandler;

import java.text.DecimalFormat;

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
