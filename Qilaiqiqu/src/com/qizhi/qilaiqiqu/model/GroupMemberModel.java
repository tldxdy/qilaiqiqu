package com.qizhi.qilaiqiqu.model;

import java.util.List;

public class GroupMemberModel {

	private String authCode;
	private boolean result;
	private String defaultCode;
	private String checkCode;
	private Data data;

	public class Data {
		private Owner owner;
		private List<UserList> userList;
		private String activityNotice;

		public class Owner {
			private String userName;
			private int userId;
			private String imUserName;
			private String mobilePhone;
			private String userImage;
			private String userMemo;
			private String imId;
			private String sex;

			public String getUserName() {
				return userName;
			}

			public void setUserName(String userName) {
				this.userName = userName;
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

			public String getMobilePhone() {
				return mobilePhone;
			}

			public void setMobilePhone(String mobilePhone) {
				this.mobilePhone = mobilePhone;
			}

			public String getUserImage() {
				return userImage;
			}

			public void setUserImage(String userImage) {
				this.userImage = userImage;
			}

			public String getUserMemo() {
				return userMemo;
			}

			public void setUserMemo(String userMemo) {
				this.userMemo = userMemo;
			}

			public String getImId() {
				return imId;
			}

			public void setImId(String imId) {
				this.imId = imId;
			}

			public String getSex() {
				return sex;
			}

			public void setSex(String sex) {
				this.sex = sex;
			}

		}

		public class UserList {
			private String userName;
			private int userId;
			private String imUserName;
			private String mobilePhone;
			private String userImage;
			private String userMemo;
			private String imId;
			private String sex;

			public String getUserName() {
				return userName;
			}

			public void setUserName(String userName) {
				this.userName = userName;
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

			public String getMobilePhone() {
				return mobilePhone;
			}

			public void setMobilePhone(String mobilePhone) {
				this.mobilePhone = mobilePhone;
			}

			public String getUserImage() {
				return userImage;
			}

			public void setUserImage(String userImage) {
				this.userImage = userImage;
			}

			public String getUserMemo() {
				return userMemo;
			}

			public void setUserMemo(String userMemo) {
				this.userMemo = userMemo;
			}

			public String getImId() {
				return imId;
			}

			public void setImId(String imId) {
				this.imId = imId;
			}

			public String getSex() {
				return sex;
			}

			public void setSex(String sex) {
				this.sex = sex;
			}

		}

		public Owner getOwner() {
			return owner;
		}

		public void setOwner(Owner owner) {
			this.owner = owner;
		}

		public List<UserList> getUserList() {
			return userList;
		}

		public void setUserList(List<UserList> userList) {
			this.userList = userList;
		}

		public String getActivityNotice() {
			return activityNotice;
		}

		public void setActivityNotice(String activityNotice) {
			this.activityNotice = activityNotice;
		}

	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getDefaultCode() {
		return defaultCode;
	}

	public void setDefaultCode(String defaultCode) {
		this.defaultCode = defaultCode;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

}
