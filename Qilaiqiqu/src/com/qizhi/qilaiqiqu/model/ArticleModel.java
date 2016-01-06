package com.qizhi.qilaiqiqu.model;

import java.util.List;


/**
 * 
 * @author 系统热门游记展示
 *
 */
public class ArticleModel {
	private Integer articleId; // 游记ID
	private String title; // 游记标题
	private String createDate; // 发表时间
	private Integer scanNum; // 浏览量
	private String defaultShowImage; // 展示图片
	private String userImage; // 用户头像
	private Integer userId; // 用户Id
	private Integer praiseNum;
	private String updateDate;
    private Integer updateTimes;
    private String memo;
    private String articleImage;
    private Integer virtualPraise;
	private Integer virtualScan;
	private String imageMemo;
	private List<?> commentList;
	private List<?> articleShareList;
	private List<?> bannerList;
    private Integer commentNum;
	private String integralNum;
	private String address;
	private String location;
	private String state;
	private String userName;
	private boolean isPraised;
	private String rewardIntegral;
	
	
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getScanNum() {
		return scanNum;
	}

	public void setScanNum(Integer scanNum) {
		this.scanNum = scanNum;
	}

	public String getDefaultShowImage() {
		return defaultShowImage;
	}

	public void setDefaultShowImage(String defaultShowImage) {
		this.defaultShowImage = defaultShowImage;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getUpdateTimes() {
		return updateTimes;
	}

	public void setUpdateTimes(Integer updateTimes) {
		this.updateTimes = updateTimes;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getArticleImage() {
		return articleImage;
	}

	public void setArticleImage(String articleImage) {
		this.articleImage = articleImage;
	}

	public Integer getVirtualPraise() {
		return virtualPraise;
	}

	public void setVirtualPraise(Integer virtualPraise) {
		this.virtualPraise = virtualPraise;
	}

	public Integer getVirtualScan() {
		return virtualScan;
	}

	public void setVirtualScan(Integer virtualScan) {
		this.virtualScan = virtualScan;
	}

	public String getImageMemo() {
		return imageMemo;
	}

	public void setImageMemo(String imageMemo) {
		this.imageMemo = imageMemo;
	}

	public List<?> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<?> commentList) {
		this.commentList = commentList;
	}

	public List<?> getArticleShareList() {
		return articleShareList;
	}

	public void setArticleShareList(List<?> articleShareList) {
		this.articleShareList = articleShareList;
	}

	public List<?> getBannerList() {
		return bannerList;
	}

	public void setBannerList(List<?> bannerList) {
		this.bannerList = bannerList;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public String getIntegralNum() {
		return integralNum;
	}

	public void setIntegralNum(String integralNum) {
		this.integralNum = integralNum;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	
	
	
}
