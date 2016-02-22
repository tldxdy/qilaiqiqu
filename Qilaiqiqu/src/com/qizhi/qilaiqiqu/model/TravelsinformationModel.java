package com.qizhi.qilaiqiqu.model;

import java.io.Serializable;

/**
 * 
 * @author 游记信息
 *
 */
public class TravelsinformationModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String time;
	private String title;
	private String memo;		//内容
	private String articleImage;	//图片
	private String imageMemo;		//图片描述
	private String address;		//所在位置
	
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "TravelsinformationModel [time=" + time + ", title=" + title
				+ ", memo=" + memo + ", articleImage=" + articleImage
				+ ", imageMemo=" + imageMemo + ", address=" + address + "]";
	}
	
}
