package com.qizhi.qilaiqiqu.model;

import java.io.Serializable;
import java.util.List;

public class ActivityModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4243357488988840130L;
	private List<ParticipantList> participantList;
	private List<ArticleMemoList> articleMemoList;
	private int participantCount;
	private Activitys activity;

	public class ParticipantList implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String state;
		private int activityId;
		private int userId;
		private String activityTitle;
		private String isSend;
		private String mobilePhone;
		private int participantId;
		private String userImage;
		private int integral;
		private String applyDate;
		private String isReward;
		private String userName;

		public int getActivityId() {
			return activityId;
		}

		public void setActivityId(int activityId) {
			this.activityId = activityId;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public String getActivityTitle() {
			return activityTitle;
		}

		public void setActivityTitle(String activityTitle) {
			this.activityTitle = activityTitle;
		}

		public String getIsSend() {
			return isSend;
		}

		public void setIsSend(String isSend) {
			this.isSend = isSend;
		}

		public String getMobilePhone() {
			return mobilePhone;
		}

		public void setMobilePhone(String mobilePhone) {
			this.mobilePhone = mobilePhone;
		}

		public int getParticipantId() {
			return participantId;
		}

		public void setParticipantId(int participantId) {
			this.participantId = participantId;
		}

		public String getUserImage() {
			return userImage;
		}

		public void setUserImage(String userImage) {
			this.userImage = userImage;
		}

		public int getIntegral() {
			return integral;
		}

		public void setIntegral(int integral) {
			this.integral = integral;
		}

		public String getApplyDate() {
			return applyDate;
		}

		public void setApplyDate(String applyDate) {
			this.applyDate = applyDate;
		}

		public String getIsReward() {
			return isReward;
		}

		public void setIsReward(String isReward) {
			this.isReward = isReward;
		}

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

	}

	public class ArticleMemoList implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int userId;
		private String createDate;
		private String memo;
		private int scanNum;
		private int praiseNum;
		private int articleId;
		private String updateDate;
		private int updateTimes;
		private String articleImage;
		private String userImage;
		private int virtualPraise;
		private int virtualScan;
		private String imageMemo;
		private String defaultShowImage;
		private String commentList;
		private String articleShareList;
		private String bannerList;
		private int commentNum;
		private String integralNum;
		private boolean isPraised;
		private String rewardIntegral;
		private String address;
		private String location;
		private String state;
		private String title;
		private String userName;

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public String getCreateDate() {
			return createDate;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public int getScanNum() {
			return scanNum;
		}

		public void setScanNum(int scanNum) {
			this.scanNum = scanNum;
		}

		public int getPraiseNum() {
			return praiseNum;
		}

		public void setPraiseNum(int praiseNum) {
			this.praiseNum = praiseNum;
		}

		public int getArticleId() {
			return articleId;
		}

		public void setArticleId(int articleId) {
			this.articleId = articleId;
		}

		public String getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(String updateDate) {
			this.updateDate = updateDate;
		}

		public int getUpdateTimes() {
			return updateTimes;
		}

		public void setUpdateTimes(int updateTimes) {
			this.updateTimes = updateTimes;
		}

		public String getArticleImage() {
			return articleImage;
		}

		public void setArticleImage(String articleImage) {
			this.articleImage = articleImage;
		}

		public String getUserImage() {
			return userImage;
		}

		public void setUserImage(String userImage) {
			this.userImage = userImage;
		}

		public int getVirtualPraise() {
			return virtualPraise;
		}

		public void setVirtualPraise(int virtualPraise) {
			this.virtualPraise = virtualPraise;
		}

		public int getVirtualScan() {
			return virtualScan;
		}

		public void setVirtualScan(int virtualScan) {
			this.virtualScan = virtualScan;
		}

		public String getImageMemo() {
			return imageMemo;
		}

		public void setImageMemo(String imageMemo) {
			this.imageMemo = imageMemo;
		}

		public String getDefaultShowImage() {
			return defaultShowImage;
		}

		public void setDefaultShowImage(String defaultShowImage) {
			this.defaultShowImage = defaultShowImage;
		}

		public String getCommentList() {
			return commentList;
		}

		public void setCommentList(String commentList) {
			this.commentList = commentList;
		}

		public String getArticleShareList() {
			return articleShareList;
		}

		public void setArticleShareList(String articleShareList) {
			this.articleShareList = articleShareList;
		}

		public String getBannerList() {
			return bannerList;
		}

		public void setBannerList(String bannerList) {
			this.bannerList = bannerList;
		}

		public int getCommentNum() {
			return commentNum;
		}

		public void setCommentNum(int commentNum) {
			this.commentNum = commentNum;
		}

		public String getIntegralNum() {
			return integralNum;
		}

		public void setIntegralNum(String integralNum) {
			this.integralNum = integralNum;
		}

		public boolean isPraised() {
			return isPraised;
		}

		public void setPraised(boolean isPraised) {
			this.isPraised = isPraised;
		}

		public String getRewardIntegral() {
			return rewardIntegral;
		}

		public void setRewardIntegral(String rewardIntegral) {
			this.rewardIntegral = rewardIntegral;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

	}

	public class Activitys implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int duration;
		private String startDate;
		private int activityId;
		private int userId;
		private String imUserName;
		private String activityTitle;
		private String isSend;
		private String createDate;
		private String activityMemo;
		private int scanNum;
		private int praiseNum;
		private String outlay;
		private String imGroupId;
		private String userImage;
		private String activityNotice;
		private String outPersons;
		private String integral;
		private boolean reward;
		private String mileage;
		private String activityImage;
		private String lanInfo;
		private String lanName;
		private String participantList;
		private String activityCommentList;
		private String activityShareList;
		private String defaultImage;
		private int virtualPraise;
		private int virtualScan;
		private String applyNum;
		private String cancelNum;
		private String deleteNum;
		private String imId;
		private String isSponsor;
		private boolean involved;
		private boolean userPraised;
		private boolean userCollected;
		private String location;
		private String state;
		private String userName;

		public int getDuration() {
			return duration;
		}

		public void setDuration(int duration) {
			this.duration = duration;
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public int getActivityId() {
			return activityId;
		}

		public void setActivityId(int activityId) {
			this.activityId = activityId;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public String getImUserName() {
			return imUserName;
		}

		public void setImUserName(String imUserName) {
			this.imUserName = imUserName;
		}

		public String getActivityTitle() {
			return activityTitle;
		}

		public void setActivityTitle(String activityTitle) {
			this.activityTitle = activityTitle;
		}

		public String getIsSend() {
			return isSend;
		}

		public void setIsSend(String isSend) {
			this.isSend = isSend;
		}

		public String getCreateDate() {
			return createDate;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public String getActivityMemo() {
			return activityMemo;
		}

		public void setActivityMemo(String activityMemo) {
			this.activityMemo = activityMemo;
		}

		public int getScanNum() {
			return scanNum;
		}

		public void setScanNum(int scanNum) {
			this.scanNum = scanNum;
		}

		public int getPraiseNum() {
			return praiseNum;
		}

		public void setPraiseNum(int praiseNum) {
			this.praiseNum = praiseNum;
		}

		public String getOutlay() {
			return outlay;
		}

		public void setOutlay(String outlay) {
			this.outlay = outlay;
		}

		public String getImGroupId() {
			return imGroupId;
		}

		public void setImGroupId(String imGroupId) {
			this.imGroupId = imGroupId;
		}

		public String getUserImage() {
			return userImage;
		}

		public void setUserImage(String userImage) {
			this.userImage = userImage;
		}

		public String getActivityNotice() {
			return activityNotice;
		}

		public void setActivityNotice(String activityNotice) {
			this.activityNotice = activityNotice;
		}

		public String getOutPersons() {
			return outPersons;
		}

		public void setOutPersons(String outPersons) {
			this.outPersons = outPersons;
		}

		public String getIntegral() {
			return integral;
		}

		public void setIntegral(String integral) {
			this.integral = integral;
		}

		public boolean isReward() {
			return reward;
		}

		public void setReward(boolean reward) {
			this.reward = reward;
		}

		public String getMileage() {
			return mileage;
		}

		public void setMileage(String mileage) {
			this.mileage = mileage;
		}

		public String getActivityImage() {
			return activityImage;
		}

		public void setActivityImage(String activityImage) {
			this.activityImage = activityImage;
		}

		public String getLanInfo() {
			return lanInfo;
		}

		public void setLanInfo(String lanInfo) {
			this.lanInfo = lanInfo;
		}

		public String getLanName() {
			return lanName;
		}

		public void setLanName(String lanName) {
			this.lanName = lanName;
		}

		public String getParticipantList() {
			return participantList;
		}

		public void setParticipantList(String participantList) {
			this.participantList = participantList;
		}

		public String getActivityCommentList() {
			return activityCommentList;
		}

		public void setActivityCommentList(String activityCommentList) {
			this.activityCommentList = activityCommentList;
		}

		public String getActivityShareList() {
			return activityShareList;
		}

		public void setActivityShareList(String activityShareList) {
			this.activityShareList = activityShareList;
		}

		public String getDefaultImage() {
			return defaultImage;
		}

		public void setDefaultImage(String defaultImage) {
			this.defaultImage = defaultImage;
		}

		public int getVirtualPraise() {
			return virtualPraise;
		}

		public void setVirtualPraise(int virtualPraise) {
			this.virtualPraise = virtualPraise;
		}

		public int getVirtualScan() {
			return virtualScan;
		}

		public void setVirtualScan(int virtualScan) {
			this.virtualScan = virtualScan;
		}

		public String getApplyNum() {
			return applyNum;
		}

		public void setApplyNum(String applyNum) {
			this.applyNum = applyNum;
		}

		public String getCancelNum() {
			return cancelNum;
		}

		public void setCancelNum(String cancelNum) {
			this.cancelNum = cancelNum;
		}

		public String getDeleteNum() {
			return deleteNum;
		}

		public void setDeleteNum(String deleteNum) {
			this.deleteNum = deleteNum;
		}

		public String getImId() {
			return imId;
		}

		public void setImId(String imId) {
			this.imId = imId;
		}

		public String getIsSponsor() {
			return isSponsor;
		}

		public void setIsSponsor(String isSponsor) {
			this.isSponsor = isSponsor;
		}

		public boolean isInvolved() {
			return involved;
		}

		public void setInvolved(boolean involved) {
			this.involved = involved;
		}

		public boolean isUserPraised() {
			return userPraised;
		}

		public void setUserPraised(boolean userPraised) {
			this.userPraised = userPraised;
		}

		public boolean isUserCollected() {
			return userCollected;
		}

		public void setUserCollected(boolean userCollected) {
			this.userCollected = userCollected;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

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

	}

	public List<ParticipantList> getParticipantList() {
		return participantList;
	}

	public void setParticipantList(List<ParticipantList> participantList) {
		this.participantList = participantList;
	}

	public List<ArticleMemoList> getArticleMemoList() {
		return articleMemoList;
	}

	public void setArticleMemoList(List<ArticleMemoList> articleMemoList) {
		this.articleMemoList = articleMemoList;
	}

	public int getParticipantCount() {
		return participantCount;
	}

	public void setParticipantCount(int participantCount) {
		this.participantCount = participantCount;
	}

	public Activitys getActivitys() {
		return activity;
	}

	public void setActivitys(Activitys activitys) {
		this.activity = activitys;
	}

}
