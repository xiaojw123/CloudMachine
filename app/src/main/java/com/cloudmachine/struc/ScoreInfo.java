package com.cloudmachine.struc;

public class ScoreInfo {
	
		private String update_DATE;  
			private String server_TIME;
		private int point_AVAILABLE; 
		private int point;
	    private int point_TOTAL;

	public int getPoint_TOTAL() {
		return point_TOTAL;
	}

	public void setPoint_TOTAL(int point_TOTAL) {
		this.point_TOTAL = point_TOTAL;
	}

	public int getPoint() {
			return point;
		}
		public void setPoint(int point) {
			this.point = point;
		}
		
		public int getPointAvailable() {
			return point_AVAILABLE;
		}
		public void setPointAvailable(int POINT_AVAILABLE) {
			this.point_AVAILABLE = POINT_AVAILABLE;
		}
		
		public String getUpdateDate() {
			return update_DATE = null!=update_DATE?update_DATE:"";
		}
		public void setUpdateDate(String UPDATE_DATE) {
			this.update_DATE = UPDATE_DATE;
		}
		
		public String getServerTime() {
			return server_TIME = (null!=server_TIME?server_TIME:"");
		}
		public void setServerTime(String server_TIME) {
			this.server_TIME = server_TIME;
		}


	@Override
	public String toString() {
		return "ScoreInfo{" +
				"update_DATE='" + update_DATE + '\'' +
				", server_TIME='" + server_TIME + '\'' +
				", point_AVAILABLE=" + point_AVAILABLE +
				", point=" + point +
				'}';
	}
}
