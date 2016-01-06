package com.qizhi.qilaiqiqu.model;

import java.io.Serializable;

/**
 * 
 * @author 发布游记
 * 
 */
public class PublishTravelsModel implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private String title;
	private String memo;
	private String address;
	private String articleImage;
	private String imageMemo;
	private String location;
	private String uniqueKey;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getArticleImage() {
		return articleImage;
	}

	public void setArticleImage(String articleImage) {
		this.articleImage = articleImage;
	}

	public String getImageMemo() {
		return imageMemo;
	}

	public void setImageMemo(String imageMemo) {
		this.imageMemo = imageMemo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	@Override
	public String toString() {
		return "PublishTravelsModel [userId=" + userId + ", title=" + title
				+ ", memo=" + memo + ", address=" + address + ", articleImage="
				+ articleImage + ", imageMemo=" + imageMemo + ", location="
				+ location + ", uniqueKey=" + uniqueKey + "]";
	}

}
