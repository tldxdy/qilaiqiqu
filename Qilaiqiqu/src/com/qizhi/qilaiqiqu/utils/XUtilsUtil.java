package com.qizhi.qilaiqiqu.utils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class XUtilsUtil {

	public final static String URL = "http://mobile.weride.com.cn/";

	public void httpPost(String url, RequestParams params,
			final CallBackPost back) {

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, URL + url + "?authCode=admin", params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						System.out.println("开始请求");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							System.out.println("加载中");
						} else {
							System.out.println("未加载");
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println("请求成功");
						back.onMySuccess(responseInfo);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("请求失败");
						back.onMyFailure(error, msg);
					}
				});
	}

	public void httpGet(String url, final CallBackGet back) {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET, url + "?authCode=admin",
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						System.out.println("开始请求");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							System.out.println("加载中");
						} else {
							System.out.println("未加载");
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println("请求成功");
						back.onMySuccess(responseInfo);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("请求失败");
						back.onMyFailure(error, msg);
					}
				});
	}

	public interface CallBackPost {

		public void onMySuccess(ResponseInfo<String> responseInfo);

		public void onMyFailure(HttpException error, String msg);
	}

	public interface CallBackGet {

		public void onMySuccess(ResponseInfo<String> responseInfo);

		public void onMyFailure(HttpException error, String msg);
	}

}
