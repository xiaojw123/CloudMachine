package com.cloudmachine.bean;

import java.io.Serializable;
/**
 * 会员基本信息
 * @author WeiSky
 *
 */
public class MemberBasic implements Serializable{
	
	private static final long serialVersionUID = -5911162704927686881L;
	
	private Long id;//
	private Long memberId;//会员表ID
	private String birthday;//生日
	private Long province;//所在地省ID
	private Long district;//所在地地区ID
	private Long city;//所在地县 市 区ID
	private String companyName;//单位名称
	private String address;//邮寄地址
	private String code;//邮编
	private String job;//职位
	private Long score;//积分
	private String hobby;//爱好
	private String userType;//用过的机型
	private String rank;//头衔
	private String community;//社区贡献
	private int sex = 3;//性别 1：男  2：女  3:保密 未知
	
	private int year;
	private int month;
	private int day;
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getCommunity() {
		return community;
	}
	public void setCommunity(String community) {
		this.community = community;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getScore() {
		return score;
	}
	public void setScore(Long score) {
		this.score = score;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public Long getProvince() {
		return province;
	}
	public void setProvince(Long province) {
		this.province = province;
	}
	public Long getDistrict() {
		return district;
	}
	public void setDistrict(Long district) {
		this.district = district;
	}
	public Long getCity() {
		return city;
	}
	public void setCity(Long city) {
		this.city = city;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	
}
