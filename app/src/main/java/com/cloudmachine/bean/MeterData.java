package com.cloudmachine.bean;




public class MeterData{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378328757313056734L;
	
	private float pointerNum;
//	private float pointerNum2;
	private float pointerSum;
//	private String barNum;
	private String title;
	private int state; //1:正常 0:偏低 2:偏高 
	private int meterBg;
	
	public float getPointerNum() {
		return pointerNum;
	}
	public void setPointerNum(float pointerNum) {
		this.pointerNum = pointerNum;
	}
	
//	public float getPointerNum2() {
//		return pointerNum2;
//	}
//	public void setPointerNum2(float pointerNum2) {
//		this.pointerNum2 = pointerNum2;
//	}
	
	public float getPointerSum() {
		return pointerSum;
	}
	public void setPointerSum(float pointerSum) {
		this.pointerSum = pointerSum;
	}
	
//	public String getBarNum() {
//		return barNum;
//	}
//	public void setBarNum(String barNum) {
//		this.barNum = barNum;
//	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public int getMeterBg() {
		return meterBg;
	}
	public void setMeterBg(int meterBg) {
		this.meterBg = meterBg;
	}
	
	

}
