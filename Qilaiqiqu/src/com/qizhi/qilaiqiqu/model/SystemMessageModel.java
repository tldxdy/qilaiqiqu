package com.qizhi.qilaiqiqu.model;

public class SystemMessageModel {
	 private Integer userId; 
	 private String createDate; 
     private Integer systemMessageId;
     private String contentJson;
     private String messageType;
     private String state;
     private String content;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Integer getSystemMessageId() {
		return systemMessageId;
	}
	public void setSystemMessageId(Integer systemMessageId) {
		this.systemMessageId = systemMessageId;
	}
	public String getContentJson() {
		return contentJson;
	}
	public void setContentJson(String contentJson) {
		this.contentJson = contentJson;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
}
