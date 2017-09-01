package com.cloudmachine.chart.interfaces;

import com.cloudmachine.chart.components.YAxis.AxisDependency;
import com.cloudmachine.chart.data.BarLineScatterCandleBubbleData;
import com.cloudmachine.chart.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    int getMaxVisibleCount();
    boolean isInverted(AxisDependency axis);
    
    int getLowestVisibleXIndex();
    int getHighestVisibleXIndex();

    BarLineScatterCandleBubbleData getData();
}
