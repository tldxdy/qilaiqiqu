package com.qizhi.qilaiqiqu.model;

public class ChatJsonModel {
	private Integer _id;
	private String json_name;
	private String json_string;
	
	
	public ChatJsonModel(Integer _id, String json_name, String json_string) {
		super();
		this._id = _id;
		this.json_name = json_name;
		this.json_string = json_string;
	}
	
	
	public ChatJsonModel() {
		super();
	}


	public Integer get_id() {
		return _id;
	}
	public void set_id(Integer _id) {
		this._id = _id;
	}
	public String getJson_name() {
		return json_name;
	}
	public void setJson_name(String json_name) {
		this.json_name = json_name;
	}
	public String getJson_string() {
		return json_string;
	}
	public void setJson_string(String json_string) {
		this.json_string = json_string;
	}


	@Override
	public String toString() {
		return "ChatJsonModel [_id=" + _id + ", json_name=" + json_name
				+ ", json_string=" + json_string + "]";
	}
	
}
