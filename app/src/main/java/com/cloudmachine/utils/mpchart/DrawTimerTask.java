package com.cloudmachine.utils.mpchart;

import android.os.Handler;
import android.os.Message;

import com.cloudmachine.chart.charts.LineChart;
import com.cloudmachine.chart.data.Entry;
import com.cloudmachine.chart.highlight.Highlight;

import java.util.TimerTask;

public class DrawTimerTask extends TimerTask{
	LineChart chart;
	Entry e;
	Highlight highlight;
	Handler handler;
	int what;
	public DrawTimerTask(LineChart chart,Entry e, Highlight highlight,Handler handler,int what){
		this.e = e;
		this.highlight = highlight;
		this.handler = handler;
		this.chart = chart;
		this.what = what;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		 Message message = new Message();      
	      message.what = what;   
	      float f[] = new float[3];
	      float p[] = chart.getMarkerPosition(e, highlight);
	      f[0] = p[0];
	      f[1] = p[1];
	      f[2] = e.getVal();
	      message.obj = f;
	      handler.sendMessage(message); 
	}

}
