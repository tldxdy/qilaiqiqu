package com.qizhi.qilaiqiqu.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import cn.jpush.android.api.JPushInterface;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.ConstantsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */
public class StartActivity extends Activity implements CallBackPost {

	private Timer timer;
	private TimerTask task;

	private IWXAPI api;// 第三方app于微信通信的API开放接口

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				stopTime();
				startActivity(new Intent(StartActivity.this,
						MainActivity.class).putExtra("loginFlag", 1));
				finish();
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);

		startTime();
		regToWx();
		cIdPost();
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		JPushInterface.setLatestNotificationNumber(this, 3);
	}
		

	
	/**
	 * @param 5秒启动handle
	 */
	private void startTime() {
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = handler.obtainMessage();
				message.arg1 = 1;
				handler.sendMessage(message);
			}
		};
		timer.schedule(task, 3000);
	}

	private void stopTime() {
		timer.cancel();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}

	/**
	 * 向微信终端注册APP_Id
	 */
	private void regToWx() {
		// 通过WXAPIFactory工厂,获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(StartActivity.this,
				ConstantsUtil.APP_ID, true);
		// 将应用的APP_Id注册到微信
		api.registerApp(ConstantsUtil.APP_ID);
	}

	/**
	 * 向服务器提交设备ID
	 */
	public void cIdPost() {
		// 注册设备码
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String DEVICE_ID = tm.getDeviceId();

		String url = "common/saveToken.html";

		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("pushToken", DEVICE_ID);
		params.addBodyParameter("adviceType", "ANDROID");

		new XUtilsUtil().httpPost(url, params, StartActivity.this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		System.out.println("启动界面向服务器提交CID成功!" + responseInfo.result);
		Log.i("qilaiqiqu", "启动界面向服务器提交CID成功!" + responseInfo.result);
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		System.out.println("启动界面向服务器提交CID出错:" + msg + "!");
		Log.i("qilaiqiqu", "启动界面向服务器提交CID出错:" + msg + "!");
	}

}
