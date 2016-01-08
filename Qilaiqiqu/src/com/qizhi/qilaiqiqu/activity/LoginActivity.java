package com.qizhi.qilaiqiqu.activity;

import java.lang.reflect.Type;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.NetUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.UserLoginModel;
import com.qizhi.qilaiqiqu.utils.ConstantsUtil;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author hujianbo
 * 
 */

public class LoginActivity extends Activity implements OnClickListener,
		CallBackPost {

	private Button loginBtn;// 登录按钮
	private Button visitorBtn;// 游客模式
	private Button registerBtn;// 注册按钮

	private EditText usernameEdt;// 用户账户
	private EditText passwordEdt;// 用户密码

	private ImageView qqImg;
	private ImageView wechatImg;
	private ImageView weiboImg;

	private TextView fogetTxt;// 忘记密码

	private XUtilsUtil httpUtils;

	private IWXAPI api;

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		api = WXAPIFactory.createWXAPI(this, ConstantsUtil.APP_ID);
		initView();
		initEvent();
	}

	private void initView() {
		httpUtils = new XUtilsUtil();
		fogetTxt = (TextView) findViewById(R.id.txt_loginActivity_forget);

		loginBtn = (Button) findViewById(R.id.btn_loginActivity_login);
		visitorBtn = (Button) findViewById(R.id.btn_loginActivity_visitor);
		registerBtn = (Button) findViewById(R.id.btn_loginActivity_register);

		usernameEdt = (EditText) findViewById(R.id.edt_loginActivity_username);
		passwordEdt = (EditText) findViewById(R.id.edt_loginActivity_password);

		qqImg = (ImageView) findViewById(R.id.img_loginactivity_qq);
		wechatImg = (ImageView) findViewById(R.id.img_loginactivity_wechat);
		weiboImg = (ImageView) findViewById(R.id.img_loginactivity_weibo);

		sp = getSharedPreferences("userInfo", 0);
		String name = sp.getString("USER_NAME", "");
		String pass = sp.getString("USER_PASSWORD", "");

		if (sp.getBoolean("isLogin", false)) {// true为已登录过,false为未曾登录
			usernameEdt.setText(name);
			passwordEdt.setText(pass);
			login();
		}

	}

	private void initEvent() {
		fogetTxt.setOnClickListener(this);

		loginBtn.setOnClickListener(this);
		visitorBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);

		qqImg.setOnClickListener(this);
		wechatImg.setOnClickListener(this);
		weiboImg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_loginActivity_login:

			login();

			break;

		case R.id.btn_loginActivity_visitor:
			new SystemUtil().makeToast(this, "游客模式");
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			break;

		case R.id.btn_loginActivity_register:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;

		case R.id.txt_loginActivity_forget:
			startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
			break;

		case R.id.img_loginactivity_qq:
			new SystemUtil().makeToast(this, "qq登录");
			break;

		case R.id.img_loginactivity_wechat:
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "qilaiqiqu";
			api.sendReq(req);
			finish();
			break;

		case R.id.img_loginactivity_weibo:
			new SystemUtil().makeToast(this, "微博登录");
			break;
		}
	}

	private void login() {
		RequestParams params = new RequestParams("UTF-8");
		params.addQueryStringParameter("mobilePhone", usernameEdt.getText()
				.toString());
		params.addQueryStringParameter("loginPwd", passwordEdt.getText()
				.toString());
		httpUtils.httpPost("common/queryUserLogin.html", params,
				new CallBackPost() {

					@Override
					public void onMyFailure(HttpException error, String msg) {

					}

					@SuppressLint("CommitPrefEdits")
					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						String s = responseInfo.result;
						JSONObject jsonObject = null;
						String message = null;
						boolean falg = false;
						try {
							jsonObject = new JSONObject(s);
							falg = jsonObject.getBoolean("result");
						} catch (JSONException e) {
							e.printStackTrace();
						}

						if (falg) {
							try {
								JSONObject data = jsonObject
										.getJSONObject("data");
								Gson gson = new Gson();
								Type type = new TypeToken<UserLoginModel>() {
								}.getType();
								final UserLoginModel userLogin = gson.fromJson(
										data.toString(), type);

								String imUserName = userLogin.getImUserName();

								// 设置极光推送 用户别名
								JPushInterface.setAliasAndTags(
										getApplicationContext(), imUserName,
										null, mAliasCallback);

								/**
								 * SharedPreferences存储用户Id和uniqueKey
								 */
								SharedPreferences sharedPreferences = getSharedPreferences(
										"userLogin", Context.MODE_PRIVATE);
								Editor editor = sharedPreferences.edit();// 获取编辑器
								editor.putInt("userId", userLogin.getUserId());
								editor.putString("uniqueKey",
										userLogin.getUniqueKey());
								editor.putString("userImage",
										userLogin.getUserImage());
								editor.commit();

								final Editor userInfo_Editor = sp.edit();
								// 登录环信
								EMChatManager.getInstance().login(
										userLogin.getImUserName(),
										userLogin.getImPassword(),
										new EMCallBack() {

											@Override
											public void onSuccess() {

												// 发送CID
												cIdPost(userLogin.getUserId());

												// 保存用户名密码
												userInfo_Editor.putString(
														"USER_NAME",
														usernameEdt.getText()
																.toString());
												userInfo_Editor.putString(
														"USER_PASSWORD",
														passwordEdt.getText()
																.toString());
												userInfo_Editor.putBoolean(
														"isLogin", true);
												userInfo_Editor.commit();

												// 更新环信用户昵称
												EMChatManager
														.getInstance()
														.updateCurrentUserNick(
																usernameEdt
																		.getText()
																		.toString());

												EMGroupManager.getInstance()
														.loadAllGroups();
												Intent intent = new Intent(
														LoginActivity.this,
														MainActivity.class);
												intent.putExtra("userLogin",
														userLogin);
												startActivity(intent);
												LoginActivity.this.finish();

												Log.d("main", "环信登录成功！");
												System.out.println("环信登录成功！");
											}

											@Override
											public void onProgress(int code,
													String status) {

											}

											@Override
											public void onError(int code,
													String message) {
												Log.d("main", "环信登录失败！"
														+ message);
												System.out.println("环信登录失败！"
														+ message);
											}
										});
							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else {
							try {
								message = jsonObject.getString("message");
								new SystemUtil().makeToast(LoginActivity.this,
										message);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	// 实现ConnectionListener接口
	private class MyConnectionListener implements EMConnectionListener {
		@Override
		public void onConnected() {
			new SystemUtil().makeToast(LoginActivity.this, "已连接到服务器");
		}

		@Override
		public void onDisconnected(final int error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (error == EMError.USER_REMOVED) {
						new SystemUtil().makeToast(LoginActivity.this,
								"显示帐号已经被移除");
					} else if (error == EMError.CONNECTION_CONFLICT) {
						new SystemUtil().makeToast(LoginActivity.this,
								"显示帐号在其他设备登陆");
					} else {
						if (NetUtils.hasNetwork(LoginActivity.this)) {
							new SystemUtil().makeToast(LoginActivity.this,
									"连接不到聊天服务器");
						} else {
							new SystemUtil().makeToast(LoginActivity.this,
									"当前网络不可用，请检查网络设置");
						}
					}
				}
			});
		}
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

	/**
	 * 向服务器提交设备ID
	 */
	public void cIdPost(int USER_ID) {
		// 注册设备码
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String DEVICE_ID = tm.getDeviceId();

		String url = "common/saveToken.html";

		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userId", USER_ID + "");
		params.addBodyParameter("pushToken", DEVICE_ID);
		params.addBodyParameter("adviceType", "ANDROID");

		new XUtilsUtil().httpPost(url, params, LoginActivity.this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		System.out.println("登录界面向服务器提交CID成功!" + responseInfo.result);
		Log.i("qilaiqiqu", "登录界面向服务器提交CID成功!" + responseInfo.result);
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		System.out.println("登录界面向服务器提交CID出错:" + msg + "!");
		Log.i("qilaiqiqu", "登录界面向服务器提交CID出错:" + msg + "!");
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

}
