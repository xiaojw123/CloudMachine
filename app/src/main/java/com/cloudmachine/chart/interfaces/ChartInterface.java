package com.cloudmachine.chart.interfaces;

import android.graphics.PointF;
import android.graphics.RectF;

import com.cloudmachine.chart.data.ChartData;
import com.cloudmachine.chart.formatter.ValueFormatter;

/**
 * Interface that provides everything there is to know about the dimensions,
 * bounds, and range of the chart.
 * 
 * @author Philipp Jahoda
 */
public interface ChartInterface {

    float getXChartMin();

    float getXChartMax();

    float getYChartMin();

    float getYChartMax();
    
    int getXValCount();

    int getWidth();

    int getHeight();

    PointF getCenterOfView();

    PointF getCenterOffsets();

    RectF getContentRect();
    
    ValueFormatter getDefaultValueFormatter();

    ChartData getData();
}
