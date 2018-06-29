package com.cloudmachine.bean;

import java.io.Serializable;



/**
 * 会员基本信息表
 * 
 * @author ChenJunhui
 * 
 */
public class Member implements Serializable{

	private static final long serialVersionUID = 263723165474245016L;

	private Long num = 1L;
	private int isAuth;//1:已认证 0：未认证

	public int getIsAuth() {
		return isAuth;
	}


	public void setIsAuth(int isAuth) {
		this.isAuth = isAuth;
	}
	public boolean isAuth(){
		return isAuth==1;
	}

	private Long wjdsId;//对应挖机大师id
	private Long wjdsStatus;//对应挖机大师角色
	private Long wjdsRole_id;//挖机大师角色
	private Long id;//主键
	private String name;//真实姓名
	private String nickName;// 昵称 
	private String email;//邮箱
	private String idCard;//身份证号
	private String mobile;//手机 
	private String password;//密码
	private String role;//角色  与机器的关系（机主、调度、司机、维保、销售、配件供应商、修理厂、主机代理商、租赁商、租机者、工程业主）
	private int isValidate;// 是否邮箱验证通过
	
	private String logo;//大头像 
//	private String middlelogo;//小头像
//	private String smalllogo;//小小头像
	private int isDeleted;//逻辑删除标记 1有效 0删除
	private int reKgStatus;//0开启  1关闭
	private String icphotoFont;
	private String icphotoBack;
	
	private int sex;
	private String sexStr;

	private String unionId;
	private String openId;
	private String invitecode;
	private String inviteUserid;
	private String wecharNickname;

	public String getAlipayNickname() {
		return alipayNickname;
	}

	public void setAlipayNickname(String alipayNickname) {
		this.alipayNickname = alipayNickname;
	}

	public String getAlipayUserId() {
		return alipayUserId;
	}

	public void setAlipayUserId(String alipayUserId) {
		this.alipayUserId = alipayUserId;
	}

	public String getAlipayLogo() {
		return alipayLogo;
	}

	public void setAlipayLogo(String alipayLogo) {
		this.alipayLogo = alipayLogo;
	}

	private String wecharLogo;
	private String alipayNickname;
	private String alipayUserId;
	private String alipayLogo;

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public Long getWjdsStatus() {
		return wjdsStatus;
	}

	public void setWjdsStatus(Long wjdsStatus) {
		this.wjdsStatus = wjdsStatus;
	}

	public Long getWjdsRole_id() {
		return wjdsRole_id;
	}

	public void setWjdsRole_id(Long wjdsRole_id) {
		this.wjdsRole_id = wjdsRole_id;
	}

	public Long getWjdsId() {
		return wjdsId;
	}

	public void setWjdsId(Long wjdsId) {
		this.wjdsId = wjdsId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getInvitecode() {
		return invitecode;
	}

	public void setInvitecode(String invitecode) {
		this.invitecode = invitecode;
	}

	public String getInviteUserid() {
		return inviteUserid;
	}

	public void setInviteUserid(String inviteUserid) {
		this.inviteUserid = inviteUserid;
	}

	public String getWecharNickname() {
		return wecharNickname;
	}

	public void setWecharNickname(String wecharNickname) {
		this.wecharNickname = wecharNickname;
	}

	public String getWecharLogo() {
		return wecharLogo;
	}

	public void setWecharLogo(String wecharLogo) {
		this.wecharLogo = wecharLogo;
	}

	public String getIcphotoFont() {
		return icphotoFont;
	}
	public void setIcphotoFont(String icphotoFont) {
		this.icphotoFont = icphotoFont;
	}
	public String getIcphotoBack() {
		return icphotoBack;
	}
	public void setIcphotoBack(String icphotoBack) {
		this.icphotoBack = icphotoBack;
	}
	private String createTime;//
	private String updateTime;//
	private Long updater;//
	private String MailCode;
	public String getMailCode() {
		return MailCode;
	}
	public void setMailCode(String mailCode) {
		MailCode = mailCode;
	}
	private String roleName;//角色名称
	private MemberBasic memberBasic;//会员基本信息
	public MemberBasic getMemberBasic() {
		return null!=memberBasic?memberBasic:new MemberBasic() ;
	}
	public void setMemberBasic(MemberBasic memberBasic) {
		this.memberBasic = memberBasic;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getIsDeleted() {
		return isDeleted;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public Long getUpdater() {
		return updater;
	}
	public void setUpdater(Long updater) {
		this.updater = updater;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobi) {
		this.mobile = mobi;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickName;
	}
	public void setNickname(String nickname) {
		this.nickName = nickname;
	}
	public int getIsValidate() {
		return isValidate;
	}
	public void setIsValidate(int isValidate) {
		this.isValidate = isValidate;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String biglogo) {
		this.logo = biglogo;
	}
	/*public String getMiddlelogo() {
		return middlelogo;
	}
	public void setMiddlelogo(String middlelogo) {
		this.middlelogo = middlelogo;
	}
	public String getSmalllogo() {
		return smalllogo;
	}
	public void setSmalllogo(String smalllogo) {
		this.smalllogo = smalllogo;
	}*/
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setSex(int sex){
		this.sex = sex;
	}
	public int getSex(){
		return this.sex;
	}
	
	public void setSexStr(String sexStr){
		this.sexStr = sexStr;
	}
	public String getSexStr(){
		if(sex == 1){
			sexStr = "男";
		}else if(sex == 2){
			sexStr = "女";
		}else{
			sexStr = "保密";
		}
		return this.sexStr;
	}
	
	public void setReKgStatus(int reKgStatus){
		this.reKgStatus = reKgStatus;
	}
	public int getReKgStatus(){
		return this.reKgStatus;
	}


	@Override
	public String toString() {
		return "Member{" +
				"id=" + id +
				", name='" + name + '\'' +
				", nickName='" + nickName + '\'' +
				", email='" + email + '\'' +
				", idCard='" + idCard + '\'' +
				", mobile='" + mobile + '\'' +
				", password='" + password + '\'' +
				", role='" + role + '\'' +
				", isValidate=" + isValidate +
				", logo='" + logo + '\'' +
				", isDeleted=" + isDeleted +
				", reKgStatus=" + reKgStatus +
				", icphotoFont='" + icphotoFont + '\'' +
				", icphotoBack='" + icphotoBack + '\'' +
				", sex=" + sex +
				", sexStr='" + sexStr + '\'' +
				", unionId='" + unionId + '\'' +
				", openId='" + openId + '\'' +
				", invitecode='" + invitecode + '\'' +
				", inviteUserid='" + inviteUserid + '\'' +
				", wecharNickname='" + wecharNickname + '\'' +
				", wecharLogo='" + wecharLogo + '\'' +
				", createTime='" + createTime + '\'' +
				", updateTime='" + updateTime + '\'' +
				", updater=" + updater +
				", MailCode='" + MailCode + '\'' +
				", roleName='" + roleName + '\'' +
				", memberBasic=" + memberBasic +
				'}';
	}
}
