
package com.cloudmachine.chart.renderer;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Paint.Align;

import com.cloudmachine.chart.animation.ChartAnimator;
import com.cloudmachine.chart.buffer.BarBuffer;
import com.cloudmachine.chart.buffer.HorizontalBarBuffer;
import com.cloudmachine.chart.data.BarData;
import com.cloudmachine.chart.data.BarDataSet;
import com.cloudmachine.chart.data.BarEntry;
import com.cloudmachine.chart.interfaces.BarDataProvider;
import com.cloudmachine.chart.utils.Transformer;
import com.cloudmachine.chart.utils.Utils;
import com.cloudmachine.chart.formatter.ValueFormatter;
import com.cloudmachine.chart.utils.ViewPortHandler;

import java.util.List;

/**
 * Renderer for the HorizontalBarChart.
 * 
 * @author Philipp Jahoda
 */
public class HorizontalBarChartRenderer extends BarChartRenderer {

    public HorizontalBarChartRenderer(BarDataProvider chart, ChartAnimator animator,
            ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);

        mValuePaint.setTextAlign(Align.LEFT);
    }

    @Override
    public void initBuffers() {

        BarData barData = mChart.getBarData();
        mBarBuffers = new HorizontalBarBuffer[barData.getDataSetCount()];

        for (int i = 0; i < mBarBuffers.length; i++) {
            BarDataSet set = barData.getDataSetByIndex(i);
            mBarBuffers[i] = new HorizontalBarBuffer(set.getValueCount() * 4 * set.getStackSize(),
                    barData.getGroupSpace(),
                    barData.getDataSetCount(), set.isStacked());
        }
    }

    protected void drawDataSet(Canvas c, BarDataSet dataSet, int index) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mShadowPaint.setColor(dataSet.getBarShadowColor());

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        List<BarEntry> entries = dataSet.getYVals();

        // initialize the buffer
        BarBuffer buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setBarSpace(dataSet.getBarSpace());
        buffer.setDataSet(index);
        buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));

        buffer.feed(entries);

        trans.pointValuesToPixel(buffer.buffer);

        for (int j = 0; j < buffer.size(); j += 4) {

            if (!mViewPortHandler.isInBoundsTop(buffer.buffer[j + 3]))
                break;

            if (!mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 1]))
                continue;

            if (mChart.isDrawBarShadowEnabled()) {
                c.drawRect(mViewPortHandler.contentLeft(), buffer.buffer[j + 1],
                        mViewPortHandler.contentRight(),
                        buffer.buffer[j + 3], mShadowPaint);
            }

            // Set the color for the currently drawn value. If the index
            // is
            // out of bounds, reuse colors.
            mRenderPaint.setColor(dataSet.getColor(j / 4));
            if(dataSet.getRound()[0]!=0 || dataSet.getRound()[1]!=0){
            	RectF rf = new RectF();
                rf.left = buffer.buffer[j];
                rf.top = buffer.buffer[j + 1];
                rf.right = buffer.buffer[j + 2];
                rf.bottom = buffer.buffer[j + 3];
                c.drawRoundRect(rf, dataSet.getRound()[0], dataSet.getRound()[1], mRenderPaint);
            }else{
            	c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
            			buffer.buffer[j + 3], mRenderPaint);
            }
            
            
        }
    }

    @Override
    public void drawValues(Canvas c) {
        // if values are drawn
        if (passesCheck()) {

            List<BarDataSet> dataSets = mChart.getBarData().getDataSets();

            final float valueOffsetPlus = Utils.convertDpToPixel(5f);
            float posOffset = 0f;
            float negOffset = 0f;
            final boolean drawValueAboveBar = mChart.isDrawValueAboveBarEnabled();

            for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {

                BarDataSet dataSet = dataSets.get(i);

                if (!dataSet.isDrawValuesEnabled() || dataSet.getEntryCount() == 0)
                    continue;

                boolean isInverted = mChart.isInverted(dataSet.getAxisDependency());

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);
                
                final float halfTextHeight = Utils.calcTextHeight(mValuePaint, "10") / 2f;

                ValueFormatter formatter = dataSet.getValueFormatter();

                Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

                List<BarEntry> entries = dataSet.getYVals();

                float[] valuePoints = getTransformedValues(trans, entries, i);
                
                // if only single values are drawn (sum)
                if (!dataSet.isStacked()) {

                    for (int j = 0; j < valuePoints.length * mAnimator.getPhaseX(); j += 2) {

                        if (!mViewPortHandler.isInBoundsTop(valuePoints[j + 1]))
                            break;

                        if (!mViewPortHandler.isInBoundsX(valuePoints[j]))
                            continue;

                        if (!mViewPortHandler.isInBoundsBottom(valuePoints[j + 1]))
                            continue;

                        BarEntry e = entries.get(j / 2);
                        float val = e.getVal();
                        String formattedValue = formatter.getFormattedValue(val, e, i, mViewPortHandler);

                        // calculate the correct offset depending on the draw position of the value
                        float valueTextWidth = Utils.calcTextWidth(mValuePaint, formattedValue);
                        posOffset = (drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth + valueOffsetPlus));
                        negOffset = (drawValueAboveBar ? -(valueTextWidth + valueOffsetPlus) : valueOffsetPlus);

                        if (isInverted) {
                            posOffset = -posOffset - valueTextWidth;
                            negOffset = -negOffset - valueTextWidth;
                        }

                        drawValue(c, formattedValue, valuePoints[j] + (val >= 0 ? posOffset : negOffset),
                                valuePoints[j + 1] + halfTextHeight);
                    }

                    // if each value of a potential stack should be drawn
                } else {

                    for (int j = 0; j < (valuePoints.length - 1) * mAnimator.getPhaseX(); j += 2) {

                        BarEntry e = entries.get(j / 2);

                        float[] vals = e.getVals();

                        // we still draw stacked bars, but there is one
                        // non-stacked
                        // in between
                        if (vals == null) {

                            if (!mViewPortHandler.isInBoundsTop(valuePoints[j + 1]))
                                break;

                            if (!mViewPortHandler.isInBoundsX(valuePoints[j]))
                                continue;

                            if (!mViewPortHandler.isInBoundsBottom(valuePoints[j + 1]))
                                continue;

                            float val = e.getVal();
                            String formattedValue = formatter.getFormattedValue(val, e, i, mViewPortHandler);

                            // calculate the correct offset depending on the draw position of the value
                            float valueTextWidth = Utils.calcTextWidth(mValuePaint, formattedValue);
                            posOffset = (drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth + valueOffsetPlus));
                            negOffset = (drawValueAboveBar ? -(valueTextWidth + valueOffsetPlus) : valueOffsetPlus);

                            if (isInverted) {
                                posOffset = -posOffset - valueTextWidth;
                                negOffset = -negOffset - valueTextWidth;
                            }

                            drawValue(c, formattedValue, valuePoints[j]
                                    + (e.getVal() >= 0 ? posOffset : negOffset),
                                    valuePoints[j + 1] + halfTextHeight);

                        } else {

                            float[] transformed = new float[vals.length * 2];

                            float posY = 0f;
                            float negY = -e.getNegativeSum();

                            for (int k = 0, idx = 0; k < transformed.length; k += 2, idx++) {

                                float value = vals[idx];
                                float y;

                                if (value >= 0f) {
                                    posY += value;
                                    y = posY;
                                } else {
                                    y = negY;
                                    negY -= value;
                                }

                                transformed[k] = y * mAnimator.getPhaseY();
                            }

                            trans.pointValuesToPixel(transformed);

                            for (int k = 0; k < transformed.length; k += 2) {

                                float val = vals[k / 2];
                                if(dataSet.getValuesColorsSameRect()){
                                	mValuePaint.setColor(dataSet.getColor(k / 2));
                                }
                                String formattedValue = formatter.getFormattedValue(val, e, i, mViewPortHandler);

                                // calculate the correct offset depending on the draw position of the value
                                float valueTextWidth = Utils.calcTextWidth(mValuePaint, formattedValue);
                                posOffset = (drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth + valueOffsetPlus));
                                negOffset = (drawValueAboveBar ? -(valueTextWidth + valueOffsetPlus) : valueOffsetPlus);

                                if (isInverted) {
                                    posOffset = -posOffset - valueTextWidth;
                                    negOffset = -negOffset - valueTextWidth;
                                }

                                float x = transformed[k]
                                        + (val >= 0 ? posOffset : negOffset);
                                float y = valuePoints[j + 1];

                                if (!mViewPortHandler.isInBoundsTop(y))
                                    break;

                                if (!mViewPortHandler.isInBoundsX(x))
                                    continue;

                                if (!mViewPortHandler.isInBoundsBottom(y))
                                    continue;

                                drawValue(c, formattedValue, x, y + halfTextHeight);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void drawValue(Canvas c, String valueText, float x, float y) {
        c.drawText(valueText, x, y, mValuePaint);
    }

    @Override
    protected void prepareBarHighlight(float x, float y1, float y2, float barspaceHalf,
            Transformer trans) {

        float top = x - 0.5f + barspaceHalf;
        float bottom = x + 0.5f - barspaceHalf;
        float left = y1;
        float right = y2;

        mBarRect.set(left, top, right, bottom);

        trans.rectValueToPixelHorizontal(mBarRect, mAnimator.getPhaseY());
    }

    @Override
    public float[] getTransformedValues(Transformer trans, List<BarEntry> entries,
            int dataSetIndex) {
        return trans.generateTransformedValuesHorizontalBarChart(entries, dataSetIndex,
                mChart.getBarData(), mAnimator.getPhaseY());
    }

    @Override
    protected boolean passesCheck() {
        return mChart.getBarData().getYValCount() < mChart.getMaxVisibleCount()
                * mViewPortHandler.getScaleY();
    }
}
