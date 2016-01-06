package com.qizhi.qilaiqiqu.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CareModel {

	private int pageStart;
	private int pageCount;
	private boolean result;
	private ArrayList<CareDataList> dataList;

	public class CareDataList implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int userId;
		private String userImage;
		private int attentionId;
		private int quoteUserId;
		private String userMemo;
		private String userName;

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public String getUserImage() {
			return userImage;
		}

		public void setUserImage(String userImage) {
			this.userImage = userImage;
		}

		public int getAttentionId() {
			return attentionId;
		}

		public void setAttentionId(int attentionId) {
			this.attentionId = attentionId;
		}

		public int getQuoteUserId() {
			return quoteUserId;
		}

		public void setQuoteUserId(int quoteUserId) {
			this.quoteUserId = quoteUserId;
		}

		public String getUserMemo() {
			return userMemo;
		}

		public void setUserMemo(String userMemo) {
			this.userMemo = userMemo;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

	}

	private int pageEnd;
	private String pageHtml;
	private String message;

	public int getPageStart() {
		return pageStart;
	}

	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public ArrayList<CareDataList> getDataList() {
		return dataList;
	}

	public void setDataList(ArrayList<CareDataList> dataList) {
		this.dataList = dataList;
	}

	public int getPageEnd() {
		return pageEnd;
	}

	public void setPageEnd(int pageEnd) {
		this.pageEnd = pageEnd;
	}

	public String getPageHtml() {
		return pageHtml;
	}

	public void setPageHtml(String pageHtml) {
		this.pageHtml = pageHtml;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
