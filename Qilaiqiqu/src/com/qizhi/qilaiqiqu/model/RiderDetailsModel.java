package com.qizhi.qilaiqiqu.model;

import java.util.List;

public class RiderDetailsModel {

	private String userApplyPeriod;
	private String attendPeriod;
	private String canApplyPeriod;
	private String userAttendPeriod;
	private AttendRider attendRider;
	private String imPassword;
	private String userApplyState;
	private String imUserName;

	public class AttendRider {
		private String state;
		private String userName;
		private Integer userId;
		private Integer integral;
		private Integer applyNum;
		private Integer riderId;
		private String attendArea;
		private String attendPeriod;
		private String attendPrice;
		private String bicycleType;
		private String rideCourse;
		private String riderImage;
		private String riderPhone;
		private String riderMemo;
		private Integer integralNum;
		private Integer attendTimes;

		private List<?> riderCommentList;

		private Integer commentCount;
		private Integer weekTimes;
		private Integer appNum;
		private String isrecomemd;
		private String allowSystemRecomend;
		private String isSystemRecomend;

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public Integer getUserId() {
			return userId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		public Integer getIntegral() {
			return integral;
		}

		public void setIntegral(Integer integral) {
			this.integral = integral;
		}

		public Integer getApplyNum() {
			return applyNum;
		}

		public void setApplyNum(Integer applyNum) {
			this.applyNum = applyNum;
		}

		public Integer getRiderId() {
			return riderId;
		}

		public void setRiderId(Integer riderId) {
			this.riderId = riderId;
		}

		public String getAttendArea() {
			return attendArea;
		}

		public void setAttendArea(String attendArea) {
			this.attendArea = attendArea;
		}

		public String getAttendPeriod() {
			return attendPeriod;
		}

		public void setAttendPeriod(String attendPeriod) {
			this.attendPeriod = attendPeriod;
		}

		public String getAttendPrice() {
			return attendPrice;
		}

		public void setAttendPrice(String attendPrice) {
			this.attendPrice = attendPrice;
		}

		public String getBicycleType() {
			return bicycleType;
		}

		public void setBicycleType(String bicycleType) {
			this.bicycleType = bicycleType;
		}

		public String getRideCourse() {
			return rideCourse;
		}

		public void setRideCourse(String rideCourse) {
			this.rideCourse = rideCourse;
		}

		public String getRiderImage() {
			return riderImage;
		}

		public void setRiderImage(String riderImage) {
			this.riderImage = riderImage;
		}

		public String getRiderPhone() {
			return riderPhone;
		}

		public void setRiderPhone(String riderPhone) {
			this.riderPhone = riderPhone;
		}

		public String getRiderMemo() {
			return riderMemo;
		}

		public void setRiderMemo(String riderMemo) {
			this.riderMemo = riderMemo;
		}

		public Integer getIntegralNum() {
			return integralNum;
		}

		public void setIntegralNum(Integer integralNum) {
			this.integralNum = integralNum;
		}

		public Integer getAttendTimes() {
			return attendTimes;
		}

		public void setAttendTimes(Integer attendTimes) {
			this.attendTimes = attendTimes;
		}

		public List<?> getRiderCommentList() {
			return riderCommentList;
		}

		public void setRiderCommentList(List<?> riderCommentList) {
			this.riderCommentList = riderCommentList;
		}

		public Integer getCommentCount() {
			return commentCount;
		}

		public void setCommentCount(Integer commentCount) {
			this.commentCount = commentCount;
		}

		public Integer getWeekTimes() {
			return weekTimes;
		}

		public void setWeekTimes(Integer weekTimes) {
			this.weekTimes = weekTimes;
		}

		public Integer getAppNum() {
			return appNum;
		}

		public void setAppNum(Integer appNum) {
			this.appNum = appNum;
		}

		public String getIsrecomemd() {
			return isrecomemd;
		}

		public void setIsrecomemd(String isrecomemd) {
			this.isrecomemd = isrecomemd;
		}

		public String getAllowSystemRecomend() {
			return allowSystemRecomend;
		}

		public void setAllowSystemRecomend(String allowSystemRecomend) {
			this.allowSystemRecomend = allowSystemRecomend;
		}

		public String getIsSystemRecomend() {
			return isSystemRecomend;
		}

		public void setIsSystemRecomend(String isSystemRecomend) {
			this.isSystemRecomend = isSystemRecomend;
		}

	}

	public String getUserApplyPeriod() {
		return userApplyPeriod;
	}

	public void setUserApplyPeriod(String userApplyPeriod) {
		this.userApplyPeriod = userApplyPeriod;
	}

	public String getAttendPeriod() {
		return attendPeriod;
	}

	public void setAttendPeriod(String attendPeriod) {
		this.attendPeriod = attendPeriod;
	}

	public String getCanApplyPeriod() {
		return canApplyPeriod;
	}

	public void setCanApplyPeriod(String canApplyPeriod) {
		this.canApplyPeriod = canApplyPeriod;
	}

	public String getUserAttendPeriod() {
		return userAttendPeriod;
	}

	public void setUserAttendPeriod(String userAttendPeriod) {
		this.userAttendPeriod = userAttendPeriod;
	}

	public AttendRider getAttendRider() {
		return attendRider;
	}

	public void setAttendRider(AttendRider attendRider) {
		this.attendRider = attendRider;
	}

	public String getImPassword() {
		return imPassword;
	}

	public void setImPassword(String imPassword) {
		this.imPassword = imPassword;
	}

	public String getUserApplyState() {
		return userApplyState;
	}

	public void setUserApplyState(String userApplyState) {
		this.userApplyState = userApplyState;
	}

	public String getImUserName() {
		return imUserName;
	}

	public void setImUserName(String imUserName) {
		this.imUserName = imUserName;
	}
}
