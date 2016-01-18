package com.qizhi.qilaiqiqu.model;

public class ArticleDetailListModel {
	private String memo;
	private Integer articleId;
	private Integer articleDetailId;
	private String articleImage;
	private String imageMemo;
	private String address;

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public Integer getArticleDetailId() {
		return articleDetailId;
	}

	public void setArticleDetailId(Integer articleDetailId) {
		this.articleDetailId = articleDetailId;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
