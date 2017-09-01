package com.cloudmachine.bean;


public class DeviceMember {
	private String middlelogo;
	private int  sex;
	private String name;
	private String companyName;
	private String role;
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMiddlelogo() {
		return middlelogo;
	}
	public void setMiddlelogo(String middlelogo) {
		this.middlelogo = middlelogo;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
