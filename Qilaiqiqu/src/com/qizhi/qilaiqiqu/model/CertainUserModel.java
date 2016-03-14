package com.qizhi.qilaiqiqu.model;

import java.io.Serializable;

/**
 * 
 * @author 其他用户信息
 * 
 */
public class CertainUserModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer userId;
	private String userImage; // 用户头像
	private String userName; // 用户名
	private String userMemo; // 用户签名
	private String sex; // 用户性别
	private Integer integral; // 积分数
	private Integer concernNum; // 用户关注数
	private Integer fansNum; // 关注用户数
	private String address; // 地址
	private Integer articleMemoNum;
	private Integer activityNum;
	private String state;
	private Integer riderId;
	private boolean attentionFlag;
	private Integer participantNum;
	private String imId;
	private boolean attendRiderFlag;
	private Integer concern;
	private Long createDate;
	private String loginName;
	private String idCard;
	private String imUserName;
	private Integer userFans;
	private String mobilePhone;
	private String email;
	private String realName;

	public Integer getRiderId() {
		return riderId;
	}

	public void setRiderId(Integer riderId) {
		this.riderId = riderId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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

	public Integer getArticleMemoNum() {
		return articleMemoNum;
	}

	public void setArticleMemoNum(Integer articleMemoNum) {
		this.articleMemoNum = articleMemoNum;
	}

	public Integer getActivityNum() {
		return activityNum;
	}

	public void setActivityNum(Integer activityNum) {
		this.activityNum = activityNum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isAttentionFlag() {
		return attentionFlag;
	}

	public void setAttentionFlag(boolean attentionFlag) {
		this.attentionFlag = attentionFlag;
	}

	public Integer getParticipantNum() {
		return participantNum;
	}

	public void setParticipantNum(Integer participantNum) {
		this.participantNum = participantNum;
	}

	public String getImId() {
		return imId;
	}

	public void setImId(String imId) {
		this.imId = imId;
	}

	public boolean isAttendRiderFlag() {
		return attendRiderFlag;
	}

	public void setAttendRiderFlag(boolean attendRiderFlag) {
		this.attendRiderFlag = attendRiderFlag;
	}

	public Integer getConcern() {
		return concern;
	}

	public void setConcern(Integer concern) {
		this.concern = concern;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getImUserName() {
		return imUserName;
	}

	public void setImUserName(String imUserName) {
		this.imUserName = imUserName;
	}

	public Integer getUserFans() {
		return userFans;
	}

	public void setUserFans(Integer userFans) {
		this.userFans = userFans;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Override
	public String toString() {
		return "CertainUserModel [userId=" + userId + ", userImage="
				+ userImage + ", userName=" + userName + ", userMemo="
				+ userMemo + ", sex=" + sex + ", integral=" + integral
				+ ", concernNum=" + concernNum + ", fansNum=" + fansNum
				+ ", address=" + address + ", articleMemoNum=" + articleMemoNum
				+ ", activityNum=" + activityNum + ", state=" + state
				+ ", riderId=" + riderId + ", attentionFlag=" + attentionFlag
				+ ", participantNum=" + participantNum + ", imId=" + imId
				+ ", attendRiderFlag=" + attendRiderFlag + ", concern="
				+ concern + ", createDate=" + createDate + ", loginName="
				+ loginName + ", idCard=" + idCard + ", imUserName="
				+ imUserName + ", userFans=" + userFans + ", mobilePhone="
				+ mobilePhone + ", email=" + email + ", realName=" + realName
				+ "]";
	}
	
	

}
