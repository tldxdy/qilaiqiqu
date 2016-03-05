package com.qizhi.qilaiqiqu.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import cn.jpush.android.api.JPushInterface;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.ConstantsUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */
public class StartActivity extends Activity {

	private Timer timer;
	private TimerTask task;

	private IWXAPI api;// 第三方app于微信通信的API开放接口

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				stopTime();
				startActivity(new Intent(StartActivity.this, MainActivity.class)
						.putExtra("loginFlag", 1));

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
	 * 向微信终端注册APP_ID_WX
	 */
	private void regToWx() {
		// 通过WXAPIFactory工厂,获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(StartActivity.this,
				ConstantsUtil.APP_ID_WX, true);
		// 将应用的APP_ID_WX注册到微信
		api.registerApp(ConstantsUtil.APP_ID_WX);
	}

}
