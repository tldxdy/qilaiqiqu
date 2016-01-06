package com.qizhi.qilaiqiqu.model;

import java.io.Serializable;

public class PersonageUserInformationModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private String userName;
	private String sex;
	private String mobilePhone;
	private String userImage;
	private String address;
	private String userMemo;
	private String uniqueKey;
	private Integer concernNum;
	private Integer fansNum;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserMemo() {
		return userMemo;
	}

	public void setUserMemo(String userMemo) {
		this.userMemo = userMemo;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public Integer getConcernNum() {
		return concernNum;
	}

	public void setConcernNum(Integer concernNum) {
		this.concernNum = concernNum;
	}

	public Integer getFansNum() {
		return fansNum;
	}

	public void setFansNum(Integer fansNum) {
		this.fansNum = fansNum;
	}

	@Override
	public String toString() {
		return "PersonageUserInformationModel [userId=" + userId
				+ ", userName=" + userName + ", sex=" + sex + ", mobilePhone="
				+ mobilePhone + ", userImage=" + userImage + ", address="
				+ address + ", userMemo=" + userMemo + ", uniqueKey="
				+ uniqueKey + "]";
	}

}
