package com.cloudmachine.bean;

import java.io.Serializable;


public class MemberInfo implements Serializable{
	private int id;
	private String name;
	private String role;
	private int memberId;
	private int roleIdS;
	private String middlelogo;//头像
	private String companyName;
	private String permissName;//拥有的权限，如有多项则以“,”拼接，比如“20,21”
	private int sex;
	private String dataTree;
	private String mobi;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String roleRemark;

	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getRoleIdS() {
		return roleIdS;
	}
	public void setRoleIdS(int roleIdS) {
		this.roleIdS = roleIdS;
	}
	public String getMiddlelogo() {
		return middlelogo;
	}
	public void setMiddlelogo(String middlelogo) {
		this.middlelogo = middlelogo;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPermissName() {
		return permissName;
	}
	public void setPermissName(String permissName) {
		this.permissName = permissName;
	}
	public String getDataTree() {
		return dataTree;
	}
	public void setDataTree(String dataTree) {
		this.dataTree = dataTree;
	}
	public String getMobi() {
		return mobi;
	}
	public void setMobi(String mobi) {
		this.mobi = mobi;
	}
	public String getRoleRemark() {
		return roleRemark;
	}
	public void setRoleRemark(String roleRemark) {
		this.roleRemark = roleRemark;
	}
	
}
