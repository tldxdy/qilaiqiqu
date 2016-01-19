package com.qizhi.qilaiqiqu.activity;

import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Toast;
import cn.jpush.android.api.TagAliasCallback;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.fragment.FansFragment;
import com.qizhi.qilaiqiqu.utils.DataCleanManager;
import com.qizhi.qilaiqiqu.utils.PushSlideSwitchView;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.PushSlideSwitchView.OnSwitchChangedListener;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */

public class SetActivity extends Activity implements OnClickListener,
		CallBackPost {

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
		// view2.setOnChangeListener(new OnSwitchChangedListener() {
		//
		// @Override
		// public void onSwitchChange(PushSlideSwitchView switchView,
		// boolean isChecked) {
		// new SystemUtil().makeToast(SetActivity.this, "滑动开关2:"
		// + isChecked);
		// }
		// });
		// view3.setOnChangeListener(new OnSwitchChangedListener() {
		//
		// @Override
		// public void onSwitchChange(PushSlideSwitchView switchView,
		// boolean isChecked) {
		// new SystemUtil().makeToast(SetActivity.this, "滑动开关3:"
		// + isChecked);
		// }
		// });
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

				SharedPreferences sp = getSharedPreferences("userInfo",
						Context.MODE_PRIVATE);
				Editor userInfo_Editor = sp.edit();
				userInfo_Editor.putBoolean("isLogin", false);
				userInfo_Editor.commit();

				httpQuery();
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

	private void httpQuery() {
		SharedPreferences sharedPreferences = getSharedPreferences("userLogin",
				Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();// 获取编辑器

		String url = "mobile/push/releaseToken.html";
		RequestParams params = new RequestParams();

		params.addBodyParameter("userId",
				sharedPreferences.getInt("userId", -1) + "");
		params.addBodyParameter("uniqueKey",
				sharedPreferences.getString("uniqueKey", null));
		new XUtilsUtil().httpPost(url, params, this);

		/**
		 * SharedPreferences清空用户Id和uniqueKey
		 */
		editor.putInt("userId", -1);
		editor.putString("uniqueKey", null);
		editor.putString("imUserName", null);
		editor.putString("imPassword", null);
		editor.putString("mobilePhone", null);
		editor.putString("riderId", null);
		editor.commit();

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

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		Toast.makeText(SetActivity.this, responseInfo.result + "", 0).show();
		String result = responseInfo.result;
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getBoolean("result")) {
				SetActivity.this.finish();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		// TODO Auto-generated method stub

	}

}
