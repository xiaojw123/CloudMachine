package com.cloudmachine.chart.interfaces;

import com.cloudmachine.chart.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
