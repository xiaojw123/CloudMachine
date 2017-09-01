package com.cloudmachine.chart.interfaces;

import com.cloudmachine.chart.components.YAxis;
import com.cloudmachine.chart.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
