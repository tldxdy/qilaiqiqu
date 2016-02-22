package com.qizhi.qilaiqiqu.model;

import java.io.Serializable;

public class RidingDraftModel implements Serializable{
	private static final long serialVersionUID = 1L;
	private int _id;
	private String jsonString;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public RidingDraftModel(int _id, String jsonString) {
		super();
		this._id = _id;
		this.jsonString = jsonString;
	}

	public RidingDraftModel() {
		super();
	}

}
