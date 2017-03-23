package com.cloudmachine.struc;

public class AgentInfo {
		private long id;
		private String name;
		private String userName;
		private String telephone; 
		private String landline; 
		private String mail;  
		private String fax; 
	
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		
		public String getMail() {
			return mail = null!=mail?mail:"";
		}
		public void setMail(String mail) {
			this.mail = mail;
		}
		
		public String getName() {
			return name = null!=name?name:"";
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUserName() {
			return userName = null!=userName?userName:"";
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getTelephone() {
			return telephone = null!=telephone?telephone:"";
		}
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
		public String getLandline() {
			return landline = null!=landline?landline:"";
		}
		public void setLandline(String landline) {
			this.landline = landline;
		}
		public String getFax() {
			return fax = null!=fax?fax:"";
		}
		public void setFax(String fax) {
			this.fax = fax;
		}
}
