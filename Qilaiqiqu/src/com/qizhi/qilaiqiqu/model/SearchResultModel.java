package com.qizhi.qilaiqiqu.model;

import java.util.List;

public class SearchResultModel {

	private int pageIndex;
	private int pageCount;
	private int dataCount;
	private boolean result;
	private List<SearchDataList> dataList;

	public class SearchDataList {
		private int id;
		private String state;
		private String type;
		private String duration;
		private String userName;
		private int userId;
		private String userImage;
		private String createDate;
		private String startDate;
		private String defaultImage;
		private int scanNum;
		private int praiseNum;
		private String outlay;
		private String title;
		private boolean involved;
		private boolean praised;

		public String getOutlay() {
			return outlay;
		}

		public void setOutlay(String outlay) {
			this.outlay = outlay;
		}

		public boolean isPraised() {
			return praised;
		}

		public void setPraised(boolean praised) {
			this.praised = praised;
		}

		public String getDuration() {
			return duration;
		}

		public void setDuration(String duration) {
			this.duration = duration;
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

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

		public String getUserImage() {
			return userImage;
		}

		public void setUserImage(String userImage) {
			this.userImage = userImage;
		}

		public String getDefaultImage() {
			return defaultImage;
		}

		public void setDefaultImage(String defaultImage) {
			this.defaultImage = defaultImage;
		}

		public boolean isInvolved() {
			return involved;
		}

		public void setInvolved(boolean involved) {
			this.involved = involved;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
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

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getDataCount() {
		return dataCount;
	}

	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public List<SearchDataList> getDataList() {
		return dataList;
	}

	public void setDataList(List<SearchDataList> dataList) {
		this.dataList = dataList;
	}

}
