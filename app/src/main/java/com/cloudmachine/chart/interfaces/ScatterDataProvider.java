package com.cloudmachine.chart.interfaces;

import com.cloudmachine.chart.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
