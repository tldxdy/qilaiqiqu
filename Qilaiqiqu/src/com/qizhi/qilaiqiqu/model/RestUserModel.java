package com.qizhi.qilaiqiqu.model;

/**
 * 
 * @author 其他用户信息
 * 
 */
public class RestUserModel {
	private Integer userId;
	private String userImage; // 用户头像
	private String userName; // 用户名
	private String userMemo; // 用户签名
	private String sex; // 用户性别
	private Integer integral; // 积分数
	private Integer concernNum; // 用户关注数
	private Integer fansNum; // 关注用户数
	private String address;	//地址
/*	private Integer articleMemoNum;
	private Integer activityNum;
	private String state;
	private boolean attentionFlag;
	private Integer participantNum;
	private String imId;
	private boolean attendRiderFlag;
	*/
	
	
	public String getUserImage() {
		return userImage;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public String getUserMemo() {
		return userMemo;
	}

	public void setUserMemo(String userMemo) {
		this.userMemo = userMemo;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
