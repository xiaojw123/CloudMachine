package com.cloudmachine.chart.interfaces;

import com.cloudmachine.chart.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
