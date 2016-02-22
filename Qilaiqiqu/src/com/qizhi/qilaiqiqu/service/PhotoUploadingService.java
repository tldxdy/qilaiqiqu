package com.qizhi.qilaiqiqu.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.model.TravelsinformationModel;
import com.qizhi.qilaiqiqu.progress.FileUploadAsyncTask;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class PhotoUploadingService extends Service {

	public static boolean isStart = false;
	
	private List<TravelsinformationModel> list;

	private List<String> imgListUrl;

	private XUtilsUtil xUtilsUtil;
	
	private String title;
	
	private int num;
	
	private SharedPreferences preferences;
	
	private int updateListSum;
	
	private boolean falg = false;
	
	private int articleId;
	
	private int screenWidth;
	
	private SystemUtil sUtil;
	
	

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String s = (String) msg.obj;
				imgListUrl.add(s);
				if (list.size() - 1 != num) {
					num = num + 1;
					try {
						String f = sUtil.saveMyBitmap(SystemUtil.compressImageFromFile(list.get(num).getArticleImage(), screenWidth));
						File file = new File(f);
						new FileUploadAsyncTask(getApplicationContext(), preferences, "QYJ", handler)
						.execute(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					publishTravels();
				}
				break;
			case 2:
				list.remove(num);
				if (list.size() - 1 != num) {
					num = num + 1;
					try {
						String f = sUtil.saveMyBitmap(SystemUtil.compressImageFromFile(list.get(num).getArticleImage(), screenWidth));
						File file = new File(f);
						new FileUploadAsyncTask(getApplicationContext(), preferences, "QYJ", handler)
						.execute(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					publishTravels();
				}
				break;
			 

			default:
				break;
			}
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
	return null;
	}

	@Override
	public void onCreate() {
		isStart = true;
		xUtilsUtil = new XUtilsUtil();
		imgListUrl = new ArrayList<String>();
		list = new ArrayList<TravelsinformationModel>();
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		sUtil = new SystemUtil();
		super.onCreate();
	}

	
	
	
	
	
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void onStart(Intent intent, int startId) {
		 if (intent != null) {  
			 list = (List<TravelsinformationModel>) intent.getSerializableExtra("list");
			 
			 System.out.println("------------------");
			 System.out.println(list.size());
			 System.out.println("------------------");
			 
			 
			 
			 title = intent.getStringExtra("title");
			 screenWidth = intent.getIntExtra("screenWidth",1200);
			 falg = intent.getBooleanExtra("falg", false);
			 imgListUrl = new ArrayList<String>();
			 Toasts.show(getApplicationContext(), "正在发布，请稍候", 0);
			 /*new SystemUtil().makeToast(getApplicationContext(),
						"正在发布，请稍候");*/
			 if(!falg){
				 num = 0;
				 String f;
				try {
					f = sUtil.saveMyBitmap(SystemUtil.compressImageFromFile(list.get(num).getArticleImage(), screenWidth));
					File file = new File(f);
					new FileUploadAsyncTask(getApplicationContext(), preferences, "QYJ", handler)
					.execute(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				 //new SystemUtil().httpClient(list.get(num).getArticleImage(),preferences, handler, "QYJ");
			 }else{
				 articleId = intent.getIntExtra("articleId", -1);
				 updateListSum = intent.getIntExtra("updateListSum", -1);
				 if (list.size() > updateListSum) {
					num = updateListSum;
					String f;
					try {
						f = sUtil.saveMyBitmap(SystemUtil.compressImageFromFile(list.get(num).getArticleImage(), screenWidth));
						File file = new File(f);
						
						new FileUploadAsyncTask(getApplicationContext(), preferences, "QYJ", handler)
						.execute(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}else{
					publishTravels();
				}
			 }
			 
		 }
		
		super.onStart(intent, startId);
	}

	
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	private void publishTravels() {
		// 使用NameValuePair来保存要传递的Post参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uniqueKey", preferences.getString(
				"uniqueKey", null)));
		params.add(new BasicNameValuePair("userId", preferences.getInt(
				"userId", -1) + ""));
		params.add(new BasicNameValuePair("title", title));

		if (falg) {
			params.add(new BasicNameValuePair("articleId", articleId + ""));
		}
		for (int i = 0; i < list.size(); i++) {

			// 需要注意的是 如果某个图片的说明 为空 时 请传递空 不能 少一个属性 每一个数组必须保证相同的长度
			if (!falg) {
				params.add(new BasicNameValuePair("articleImage", imgListUrl
						.get(i)));
			} else {
				if(i < updateListSum){
					params.add(new BasicNameValuePair("articleImage", list.get(i)
							.getArticleImage()));
				}else{
					params.add(new BasicNameValuePair("articleImage", imgListUrl
							.get(i - updateListSum)));
				}
			}

			if (list.get(i).getImageMemo() == null) {
				params.add(new BasicNameValuePair("imageMemo", ""));
			} else {
				params.add(new BasicNameValuePair("imageMemo", list.get(i)
						.getImageMemo()));
			}

			if (list.get(i).getAddress() == null) {
				params.add(new BasicNameValuePair("address", ""));
			} else {
				params.add(new BasicNameValuePair("address", list.get(i)
						.getAddress()));
			}

			if (list.get(i).getMemo() == null) {
				params.add(new BasicNameValuePair("memo", ""));
			} else {
				params.add(new BasicNameValuePair("memo", list.get(i).getMemo()));
			}

		}
		RequestParams params2 = new RequestParams();
		try {
			params2.setBodyEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			String url;
			if (!falg) {
				url = "mobile/articleMemo/insertArticle.html";
			} else {
				url = "mobile/articleMemo/updateArticle.html";
			}
			xUtilsUtil.httpPost(url, params2, new CallBackPost() {

				@Override
				public void onMySuccess(ResponseInfo<String> responseInfo) {
					JSONObject jsonObject = null;
					try {
						jsonObject = new JSONObject(responseInfo.result);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (jsonObject.optBoolean("result")) {
						if (!falg) {
							Toasts.show(getApplicationContext(), "发表成功", 0);
							/*new SystemUtil().makeToast(getApplicationContext(),
									"发表成功");*/
						} else {
							Toasts.show(getApplicationContext(), "修改成功", 0);
							/*new SystemUtil().makeToast(getApplicationContext(),
									"修改成功");*/
						}
					} else {
						//System.out.println(jsonObject.optString("message"));
						Toasts.show(getApplicationContext(), jsonObject.optString("message"), 0);
						/*new SystemUtil().makeToast(getApplicationContext(),
								jsonObject.optString("message"));*/
					}
					isStart = false;
					onDestroy();
					
				}

				@Override
				public void onMyFailure(HttpException error, String msg) {
					isStart = false;
					onDestroy();
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
