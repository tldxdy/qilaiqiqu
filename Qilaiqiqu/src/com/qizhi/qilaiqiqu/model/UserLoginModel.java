package com.qizhi.qilaiqiqu.model;

import java.io.Serializable;

public class UserLoginModel implements Serializable{
	private static final long serialVersionUID = 1L;
	private String imId;
	private String mobilePhone;
	private String imPassword;
	private Integer userId;
	private String userImage;
	private String userName;
	private String imUserName;
	private String uniqueKey;

	public String getImId() {
		return imId;
	}

	public void setImId(String imId) {
		this.imId = imId;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getImPassword() {
		return imPassword;
	}

	public void setImPassword(String imPassword) {
		this.imPassword = imPassword;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getImUserName() {
		return imUserName;
	}

	public void setImUserName(String imUserName) {
		this.imUserName = imUserName;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	@Override
	public String toString() {
		return "UserClass [imId=" + imId + ", mobilePhone=" + mobilePhone
				+ ", imPassword=" + imPassword + ", userId=" + userId
				+ ", userImage=" + userImage + ", userName=" + userName
				+ ", imUserName=" + imUserName + ", uniqueKey=" + uniqueKey
				+ "]";
	}

}
