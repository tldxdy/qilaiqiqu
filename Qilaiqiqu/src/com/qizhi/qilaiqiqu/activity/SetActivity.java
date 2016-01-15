package com.qizhi.qilaiqiqu.activity;

import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.DataCleanManager;
import com.qizhi.qilaiqiqu.utils.PushSlideSwitchView;
import com.qizhi.qilaiqiqu.utils.PushSlideSwitchView.OnSwitchChangedListener;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */

public class SetActivity extends Activity implements OnClickListener {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set);

		SharedPreferences sp = getSharedPreferences("userLogin", 0);
		if (sp.getInt("userId", -1) == -1) {
			logoutFlag = 1;
		} else {
			logoutFlag = 2;
		}

		initView();
		initEvent();

	}

	private void initView() {
		view1 = (PushSlideSwitchView) findViewById(R.id.push_set_warm_switchview1);
//		view2 = (PushSlideSwitchView) findViewById(R.id.push_set_warm_switchview2);
//		view3 = (PushSlideSwitchView) findViewById(R.id.push_set_warm_switchview3);

		cacheTxt = (TextView) findViewById(R.id.txt_setActivity_cache);
		logoutTxt = (TextView) findViewById(R.id.txt_setActivity_logout);
		backLayout = (LinearLayout) findViewById(R.id.layout_setActivity_back);
		opintionLayout = (LinearLayout) findViewById(R.id.layout_setActivity_opintion);
		introduceLayout = (LinearLayout) findViewById(R.id.layout_setActivity_introduce);
		clearCacheLayout = (LinearLayout) findViewById(R.id.layout_setActivity_clearCache);

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

		view1.setOnChangeListener(new OnSwitchChangedListener() {

			@Override
			public void onSwitchChange(PushSlideSwitchView switchView,
					boolean isChecked) {
				new SystemUtil().makeToast(SetActivity.this, "滑动开关1:"
						+ isChecked);
			}
		});
//		view2.setOnChangeListener(new OnSwitchChangedListener() {
//
//			@Override
//			public void onSwitchChange(PushSlideSwitchView switchView,
//					boolean isChecked) {
//				new SystemUtil().makeToast(SetActivity.this, "滑动开关2:"
//						+ isChecked);
//			}
//		});
//		view3.setOnChangeListener(new OnSwitchChangedListener() {
//
//			@Override
//			public void onSwitchChange(PushSlideSwitchView switchView,
//					boolean isChecked) {
//				new SystemUtil().makeToast(SetActivity.this, "滑动开关3:"
//						+ isChecked);
//			}
//		});
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

				// 设置极光推送 用户别名
				JPushInterface.setAliasAndTags(getApplicationContext(), "",
						null, mAliasCallback);

				/**
				 * SharedPreferences清空用户Id和uniqueKey
				 */
				SharedPreferences sharedPreferences = getSharedPreferences(
						"userLogin", Context.MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();// 获取编辑器
				editor.putInt("userId", -1);
				editor.putString("uniqueKey", null);
				editor.putString("imUserName", null);
				editor.putString("imPassword", null);
				editor.commit();

				SharedPreferences sp = getSharedPreferences("userInfo",
						Context.MODE_PRIVATE);
				Editor userInfo_Editor = sp.edit();
				userInfo_Editor.putBoolean("isLogin", false);
				userInfo_Editor.commit();

				SetActivity.this.finish();
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

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Log.i("JPush", logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Log.i("JPush", logs);
				break;

			default:
				logs = "Failed with errorCode = " + code;
				Log.e("JPush", logs);
			}

		}

	};

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
