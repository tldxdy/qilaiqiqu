package com.qizhi.qilaiqiqu.model;

import java.io.Serializable;
import java.util.List;

public class RidingModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer pageIndex;
	private Integer pageCount;
	private Integer dataCount;
	private boolean result;
	private List<RidingModelList> dataList;
	private String message;

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getDataCount() {
		return dataCount;
	}

	public void setDataCount(Integer dataCount) {
		this.dataCount = dataCount;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public List<RidingModelList> getDataList() {
		return dataList;
	}

	public void setDataList(List<RidingModelList> dataList) {
		this.dataList = dataList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
