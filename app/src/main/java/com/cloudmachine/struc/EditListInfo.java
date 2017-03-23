package com.cloudmachine.struc;

import java.io.Serializable;


public class EditListInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2480655924189887879L;
	private String name;
	private boolean isClick;
	private long id;
	private String str1;
	private String str2;
	private String PK_PROD_DEF;
	private String PK_BRAND;
	private String PK_VHCL_MATERIAL;
	
	

	public String getPK_BRAND() {
		return PK_BRAND;
	}
	public void setPK_BRAND(String pK_BRAND) {
		PK_BRAND = pK_BRAND;
	}
	public String getPK_VHCL_MATERIAL() {
		return PK_VHCL_MATERIAL;
	}
	public void setPK_VHCL_MATERIAL(String pK_VHCL_MATERIAL) {
		PK_VHCL_MATERIAL = pK_VHCL_MATERIAL;
	}
	public String getPK_PROD_DEF() {
		return PK_PROD_DEF;
	}
	public void setPK_PROD_DEF(String pK_PROD_DEF) {
		PK_PROD_DEF = pK_PROD_DEF;
	}
	public long getId() {
		return id;
	}
	public void setId(long l) {
		this.id = l;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStr1() {
		return str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	public String getStr2() {
		return str2;
	}
	public void setStr2(String str2) {
		this.str2 = str2;
	}
	public boolean getIsClick(){
		return isClick;
	}
	public void setIsClick(boolean isClick){
		this.isClick = isClick;
	}
	
	
}
