package com.cloudmachine.struc;

public class DeviceInfo {
		private Long id;
		private String name;
		private String brand;
		private String model;
		private String image;
		private int checkStatus;
	
		public int getCheckStatus() {
			return checkStatus;
		}
		public void setCheckStatus(int checkStatus) {
			this.checkStatus = checkStatus;
		}
		public Long getId() {
			return id;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getBrand() {
			return brand;
		}
		public void setBrand(String brand) {
			this.brand = brand;
		}
		public String getModel() {
			return model;
		}
		public void setModel(String model) {
			this.model = model;
		}
}
