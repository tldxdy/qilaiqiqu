package com.qizhi.qilaiqiqu.model;

/**
 * 
 * @author 游记信息
 *
 */
public class TravelsinformationModel {
	private String memo;		//内容
	private String articleImage;	//图片
	private String imageMemo;		//图片描述
	private String address;		//所在位置
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
	@Override
	public String toString() {
		return "ReleaseTravels [memo=" + memo + ", articleImage="
				+ articleImage + ", imageMemo=" + imageMemo + ", address="
				+ address + "]";
	}
}
