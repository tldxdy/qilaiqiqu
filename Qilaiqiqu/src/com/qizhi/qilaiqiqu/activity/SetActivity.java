package com.qizhi.qilaiqiqu.activity;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.ConstantsUtil;
import com.qizhi.qilaiqiqu.utils.DataCleanManager;
import com.qizhi.qilaiqiqu.utils.PushSlideSwitchView;
import com.qizhi.qilaiqiqu.utils.PushSlideSwitchView.OnSwitchChangedListener;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */

public class SetActivity extends HuanxinLogOutActivity implements OnClickListener {

	private LinearLayout backLayout;
	private LinearLayout opintionLayout;
	private LinearLayout introduceLayout;
	private LinearLayout clearCacheLayout;

	private PushSlideSwitchView view1;
	private PushSlideSwitchView view2;
	private PushSlideSwitchView view3;

	private int logoutFlag = 1;// 为1未登录,为2已登录

	private TextView logoutTxt;
	private TextView cacheTxt;

	DataCleanManager cleanManager;

	private SharedPreferences sp;
	Editor editor;

	private Tencent mTencent;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				httpQuery();
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set);

		sp = getSharedPreferences("userLogin", 0);
		if (sp.getInt("userId", -1) == -1) {
			logoutFlag = 1;
		} else {
			logoutFlag = 2;
		}

		initView();
		initEvent();

	}

	private void initView() {
		editor = sp.edit();// 获取编辑器

		view1 = (PushSlideSwitchView) findViewById(R.id.push_set_warm_switchview1);
		// view2 = (PushSlideSwitchView)
		// findViewById(R.id.push_set_warm_switchview2);
		// view3 = (PushSlideSwitchView)
		// findViewById(R.id.push_set_warm_switchview3);

		cacheTxt = (TextView) findViewById(R.id.txt_setActivity_cache);
		logoutTxt = (TextView) findViewById(R.id.txt_setActivity_logout);
		backLayout = (LinearLayout) findViewById(R.id.layout_setActivity_back);
		opintionLayout = (LinearLayout) findViewById(R.id.layout_setActivity_opintion);
		introduceLayout = (LinearLayout) findViewById(R.id.layout_setActivity_introduce);
		clearCacheLayout = (LinearLayout) findViewById(R.id.layout_setActivity_clearCache);

		mTencent = Tencent.createInstance(ConstantsUtil.APP_ID_TX,
				this.getApplicationContext());
	}

	private void initEvent() {
		if (logoutFlag == 2) {
			logoutTxt.setVisibility(View.VISIBLE);
			opintionLayout.setVisibility(View.VISIBLE);
		} else {
			logoutTxt.setVisibility(View.GONE);
			opintionLayout.setVisibility(View.GONE);
		}
		try {
			cacheTxt.setText(cleanManager.getTotalCacheSize(SetActivity.this));
		} catch (Exception e) {
			e.printStackTrace();
		}

		logoutTxt.setOnClickListener(this);
		backLayout.setOnClickListener(this);
		opintionLayout.setOnClickListener(this);
		introduceLayout.setOnClickListener(this);
		clearCacheLayout.setOnClickListener(this);
		view1.setChecked(sp.getBoolean("view1", true));
		System.out.println("sp.getBoolean(view1, true):"
				+ sp.getBoolean("view1", true));
		view1.setOnChangeListener(new OnSwitchChangedListener() {

			@Override
			public void onSwitchChange(PushSlideSwitchView switchView,
					boolean isChecked) {
				EMChatOptions options = EMChatManager.getInstance()
						.getChatOptions();
				options = EMChatManager.getInstance().getChatOptions();
				if (isChecked) {
					System.out.println("isChecked:" + isChecked);
					editor.putBoolean("view1", isChecked);
					editor.commit();
					options.setNotifyBySoundAndVibrate(isChecked);
					System.out.println("sp.getBoolean(view1, true):"
							+ sp.getBoolean("view1", false));
				} else {
					System.out.println("isChecked:" + isChecked);
					editor.putBoolean("view1", isChecked);
					editor.commit();
					options.setNotifyBySoundAndVibrate(isChecked);
					System.out.println("sp.getBoolean(view1, true):"
							+ sp.getBoolean("view1", false));
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_setActivity_back:
			finish();
			break;

		case R.id.layout_setActivity_opintion:
			startActivity(new Intent(SetActivity.this, OpinionActivity.class));
			break;

		case R.id.layout_setActivity_introduce:
			startActivity(new Intent(SetActivity.this, IntroduceActivity.class));
			break;

		case R.id.txt_setActivity_logout:
			logOut();
			logoutQQ();
			break;

		case R.id.layout_setActivity_clearCache:
			clearCache();
			break;

		default:
			break;
		}
	}

	private void clearCache() {
		cleanManager.clearAllCache(SetActivity.this);
		try {
			cacheTxt.setText(cleanManager.getTotalCacheSize(SetActivity.this));
			new SystemUtil().makeToast(this, "缓存已清除");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void logOut() {
		EMChatManager.getInstance().logout(new EMCallBack() {

			@Override
			public void onSuccess() {
				Log.d("LOGOUT", "环信登出成功!");
				System.err.println("环信登出成功!");

				SharedPreferences sp = getSharedPreferences("userInfo",
						Context.MODE_PRIVATE);
				Editor userInfo_Editor = sp.edit();
				userInfo_Editor.putBoolean("isLogin", false);
				userInfo_Editor.commit();
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, String message) {
				Log.d("LOGOUT", "环信登出失败!" + message);
				System.err.println("环信登出失败!" + message);
			}
		});
	}

	public void logoutQQ() {
		mTencent.logout(this);
	}

	private void httpQuery() {

		String url = "mobile/push/releaseToken.html";
		RequestParams params = new RequestParams();

		params.addBodyParameter("userId", sp.getInt("userId", -1) + "");
		params.addBodyParameter("uniqueKey", sp.getString("uniqueKey", null));
		// String checkCode = sharedPreferences.getString("checkCode", null);
		// String defaultCode = sharedPreferences.getString("defaultCode",
		// null);
		// params.addBodyParameter("authCode",Encryption.encryptionMethod(checkCode,
		// defaultCode));
		new XUtilsUtil().httpPost(url, params, new CallBackPost() {

			@Override
			public void onMySuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("result")) {
						/**
						 * SharedPreferences清空用户Id和uniqueKey
						 */

						editor.putInt("userId", -1);
						editor.putString("riderId", null);
						editor.putString("userImage", null);
						editor.putString("uniqueKey", null);
						editor.putString("imPassword", null);
						editor.putString("imUserName", null);
						editor.putString("mobilePhone", null);
						/*
						 * editor.putString("checkCode",
						 * jsonObject.optString("checkCode"));
						 * editor.putString("defaultCode",
						 * jsonObject.optString("defaultCode"));
						 */
						editor.commit();
						SetActivity.this.finish();
						/*
						 * startActivity(new Intent(SetActivity.this,
						 * LoginActivity.class));
						 */
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onMyFailure(HttpException error, String msg) {

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
