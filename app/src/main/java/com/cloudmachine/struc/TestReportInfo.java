package com.cloudmachine.struc;

import java.util.ArrayList;

/**
 * 
 * @author shixionglu
 * 检测报告实体类
 */
/**
 * Created by shixionglu on 2016/10/19.
 */

public class TestReportInfo {


    /**
     * reList : [{"referRange":"0-350","testResult":"0bar","bexcess":true,"item":"主泵压力","id":11},{"referRange":"0-20","testResult":"0bar","bexcess":true,"item":"回油压力","id":7},{"referRange":"15-50","testResult":"39bar","bexcess":true,"item":"先导压力","id":6},{"referRange":"0-90","testResult":"69.00℃","bexcess":true,"item":"回油温度","id":3},{"referRange":"0-90","testResult":"68℃","bexcess":true,"item":"主泵泄油口温度","id":2},{"referRange":"-20-80","testResult":"55℃","bexcess":true,"item":"环境温度","id":1}]
     * checkDate : 1474275153000
     * pastTime : 16-9-19
     */

    private long checkDate;
    private String pastTime;
    /**
     * referRange : 0-350
     * testResult : 0bar
     * bexcess : true
     * item : 主泵压力
     * id : 11
     */
    
    private ArrayList<ReListBean> reList;

    @Override
	public String toString() {
		return "TestReportInfo [checkDate=" + checkDate + ", pastTime="
				+ pastTime + ", reList=" + reList + "]";
	}

	public long getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(long checkDate) {
        this.checkDate = checkDate;
    }

    public String getPastTime() {
        return pastTime;
    }

    public void setPastTime(String pastTime) {
        this.pastTime = pastTime;
    }

    public ArrayList<ReListBean> getReList() {
        return reList;
    }

    public void setReList(ArrayList<ReListBean> reList) {
        this.reList = reList;
    }

    public static class ReListBean {
        private String referRange;
        private String testResult;
        private boolean bexcess;
        private String item;
        private int id;

        public String getReferRange() {
            return referRange;
        }

        public void setReferRange(String referRange) {
            this.referRange = referRange;
        }

        public String getTestResult() {
            return testResult;
        }

        public void setTestResult(String testResult) {
            this.testResult = testResult;
        }

        public boolean isBexcess() {
            return bexcess;
        }

        public void setBexcess(boolean bexcess) {
            this.bexcess = bexcess;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

		@Override
		public String toString() {
			return "ReListBean [referRange=" + referRange + ", testResult="
					+ testResult + ", bexcess=" + bexcess + ", item=" + item
					+ ", id=" + id + "]";
		}
        
    }
}