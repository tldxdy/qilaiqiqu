package com.qizhi.qilaiqiqu.model;

import java.util.List;

public class ArticleMemoDetailModel {
	private boolean userCollected;
	private boolean userPraised;
	private ArticleModel articleMemo;
	private List<?> activityList;
	public boolean isUserCollected() {
		return userCollected;
	}
	public void setUserCollected(boolean userCollected) {
		this.userCollected = userCollected;
	}
	public boolean isUserPraised() {
		return userPraised;
	}
	public void setUserPraised(boolean userPraised) {
		this.userPraised = userPraised;
	}
	public ArticleModel getArticleMemo() {
		return articleMemo;
	}
	public void setArticleMemo(ArticleModel articleMemo) {
		this.articleMemo = articleMemo;
	}
	public List<?> getActivityList() {
		return activityList;
	}
	public void setActivityList(List<?> activityList) {
		this.activityList = activityList;
	}
	

	
}
