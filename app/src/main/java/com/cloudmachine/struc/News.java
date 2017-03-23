package com.cloudmachine.struc;

import java.io.Serializable;

public class News implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3877513290631445711L;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	private Long id;
	private String image;
	private String title;
	private String summary;
	private String logo;
	private String images;
	private String content;
	private String memberName;
	private String replyMemberName;
	private int memberId;
	private boolean isFavour;
	private boolean isCommentFavour;
	private boolean isParised;
	private String location;
	private int count;
	
	
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public boolean isParised() {
		return isParised;
	}
	public void setParised(boolean isParised) {
		this.isParised = isParised;
	}
	public boolean isCommentFavour() {
		return isCommentFavour;
	}
	public void setCommentFavour(boolean isCommentFavour) {
		this.isCommentFavour = isCommentFavour;
	}
	public boolean isFavour() {
		return isFavour;
	}
	public void setFavour(boolean isFavour) {
		this.isFavour = isFavour;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getReplyMemberName() {
		return replyMemberName;
	}
	public void setReplyMemberName(String replyMemberName) {
		this.replyMemberName = replyMemberName;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	private int goodCount;
	
	private int goodsCount;
	
	

	


	public int getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}
	public int getGoodCount() {
		
		return goodCount;
	}
	public void setGoodCount(int goodCount) {
		this.goodCount = goodCount;
	}

	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public boolean isHot() {
		return hot;
	}
	public void setHot(boolean hot) {
		this.hot = hot;
	}
	public boolean isTop() {
		return top;
	}
	public void setTop(boolean top) {
		this.top = top;
	}

	private String url;
	private String time;
	private String author;
	private boolean hot;
	private boolean top;
	private int commentNum;

}

