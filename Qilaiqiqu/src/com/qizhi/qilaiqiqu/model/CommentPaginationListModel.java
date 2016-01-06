package com.qizhi.qilaiqiqu.model;

import java.util.List;

public class CommentPaginationListModel {
	private Integer pageIndex;
	private Integer	pageCount;
	private Integer dataCount;
	private String result;
	private List<RidingCommentModel> dataList;
	private String message;

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getDataCount() {
		return dataCount;
	}

	public void setDataCount(Integer dataCount) {
		this.dataCount = dataCount;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<RidingCommentModel> getDataList() {
		return dataList;
	}

	public void setDataList(List<RidingCommentModel> dataList) {
		this.dataList = dataList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	
}
