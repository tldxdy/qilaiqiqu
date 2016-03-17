package com.qizhi.qilaiqiqu.activity;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.UserLoginModel;
import com.qizhi.qilaiqiqu.utils.ConstantsUtil;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author hujianbo
 * 
 */

public class LoginActivity extends HuanxinLogOutActivity implements
		OnClickListener {

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

	private IUiListener loginListener; // 腾讯授权登录监听器

	private Tencent mTencent;
	private String registrationID;

	private ProgressDialog progDialog = null;// 搜索时进度条

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		api = WXAPIFactory.createWXAPI(this, ConstantsUtil.APP_ID_WX);
		mTencent = Tencent.createInstance(ConstantsUtil.APP_ID_TX,
				this.getApplicationContext());
		initView();
		initEvent();
	}

	private void initView() {
		httpUtils = new XUtilsUtil();
		registrationID = JPushInterface.getRegistrationID(LoginActivity.this);
		System.out.println("registrationID:" + registrationID);

		fogetTxt = (TextView) findViewById(R.id.txt_loginActivity_forget);

		loginBtn = (Button) findViewById(R.id.btn_loginActivity_login);
		visitorBtn = (Button) findViewById(R.id.btn_loginActivity_visitor);
		registerBtn = (Button) findViewById(R.id.btn_loginActivity_register);

		usernameEdt = (EditText) findViewById(R.id.edt_loginActivity_username);
		passwordEdt = (EditText) findViewById(R.id.edt_loginActivity_password);

		qqImg = (ImageView) findViewById(R.id.img_loginactivity_qq);
		wechatImg = (ImageView) findViewById(R.id.img_loginactivity_wechat);
		weiboImg = (ImageView) findViewById(R.id.img_loginactivity_weibo);

		loginListener = new IUiListener() {

			@Override
			public void onError(UiError arg0) {

			}

			@Override
			public void onComplete(Object value) {

				System.out.println("有数据返回..");
				if (value == null) {
					return;
				}

				try {
					final JSONObject jo = (JSONObject) value;

					int ret = jo.getInt("ret");

					System.out.println("json=" + String.valueOf(jo));

					if (ret == 0) {
						String openID = jo.getString("openid");
						String accessToken = jo.getString("access_token");
						String expires = jo.getString("expires_in");
						mTencent.setOpenId(openID);
						mTencent.setAccessToken(accessToken, expires);

						QQToken qqToken = mTencent.getQQToken();
						UserInfo info = new UserInfo(getApplicationContext(),
								qqToken);
						info.getUserInfo(new IUiListener() {

							@Override
							public void onError(UiError msg) {
								Toasts.show(LoginActivity.this, "未知错误：" + msg,
										0);
							}

							@Override
							public void onComplete(Object response) {
								JSONObject re = (JSONObject) response;
								System.out.println("response="
										+ String.valueOf(response));

								sendTencentCode(re, jo);
							}

							@Override
							public void onCancel() {
								System.out
										.println("onCancel()onCancel()onCancel()onCancel()onCancel()onCancel()onCancel()onCancel()onCancel()onCancel()onCancel()onCancel()onCancel()");
							}
						});
					}

				} catch (Exception e) {
				}

			}

			@Override
			public void onCancel() {

			}
		};

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
			finish();
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			break;

		case R.id.btn_loginActivity_register:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;

		case R.id.txt_loginActivity_forget:
			startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
			break;

		case R.id.img_loginactivity_qq:
			showProgressDialog();
			// 如果session无效，就开始登录
			if (!mTencent.isSessionValid()) {
				// 开始qq授权登录
				mTencent.login(LoginActivity.this, "all", loginListener);
			} else {
				dissmissProgressDialog();
				Toasts.show(LoginActivity.this, "您已登陆,请先退出登陆", 0);
			}

			break;

		case R.id.img_loginactivity_wechat:
			showProgressDialog();
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "qilaiqiqu";
			api.sendReq(req);
			break;

		case R.id.img_loginactivity_weibo:
			new SystemUtil().makeToast(this, "微博登录");
			break;
		}
	}

	private void sendTencentCode(JSONObject re, JSONObject jo) {
		RequestParams params = new RequestParams("UTF-8");

		System.out.println("pushToken:" + registrationID);
		System.out.println("openId:" + jo.optString("openid"));
		System.out.println("accessToken:" + jo.optString("access_token"));
		System.out.println("nickname:" + re.optString("nickname"));
		System.out.println("gender:" + re.optString("gender"));
		System.out.println("avatarURL50:" + re.optString("figureurl_1"));
		System.out.println("avatarURL100:" + re.optString("figureurl_2"));

		params.addQueryStringParameter("pushToken", registrationID);
		params.addQueryStringParameter("adviceType", "ANDROID");
		params.addQueryStringParameter("openId", jo.optString("openid"));
		params.addQueryStringParameter("accessToken",
				jo.optString("access_token"));
		params.addQueryStringParameter("nickname", re.optString("nickname"));
		params.addQueryStringParameter("gender", re.optString("gender"));
		params.addQueryStringParameter("avatarURL50",
				re.optString("figureurl_1"));
		params.addQueryStringParameter("avatarURL100",
				re.optString("figureurl_2"));
		httpUtils.httpPost("common/qqLogin.html", params, new CallBackPost() {

			@Override
			public void onMySuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				System.out.println("QQ用户信息:" + result);

				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("result")) {
						JSONObject data = jsonObject.getJSONObject("data");

						/**
						 * SharedPreferences存储用户Id和uniqueKey
						 */
						SharedPreferences sharedPreferences = getSharedPreferences(
								"userLogin", Context.MODE_PRIVATE);
						Editor editor = sharedPreferences.edit();// 获取编辑器
						editor.putString("imId", data.optString("imId"));
						editor.putString("mobilePhone",
								data.optString("mobilePhone"));
						editor.putString("imPassword",
								data.optString("imPassword"));
						editor.putInt("userId", data.optInt("userId"));
						editor.putString("userImage",
								data.optString("userImage"));
						editor.putString("userName", data.optString("userName"));
						editor.putString("imUserName",
								data.optString("imUserName"));
						editor.putString("uniqueKey",
								data.optString("uniqueKey"));
						editor.putInt("loginFlag", 1);
						editor.commit();
						Toast.makeText(LoginActivity.this, "登录成功",
								Toast.LENGTH_LONG).show();
						LoginActivity.this.finish();
						startActivity(new Intent(LoginActivity.this, MainActivity.class));
					} else {
						Toasts.show(LoginActivity.this,
								"登录失败:" + jsonObject.getString("message"), 0);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onMyFailure(HttpException error, String msg) {
				System.out.println("QQLogin失败:" + msg + ":" + error);
			}

		});

	}

	private void login() {
		RequestParams params = new RequestParams("UTF-8");

		params.addQueryStringParameter("mobilePhone", usernameEdt.getText()
				.toString());
		params.addQueryStringParameter("loginPwd", passwordEdt.getText()
				.toString());
		params.addQueryStringParameter("pushToken", registrationID);
		params.addQueryStringParameter("adviceType", "ANDROID");
		httpUtils.httpPost("common/queryUserLogin.html", params,
				new CallBackPost() {

					@Override
					public void onMyFailure(HttpException error, String msg) {

					}

					@SuppressLint("CommitPrefEdits")
					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						String s = responseInfo.result;
						System.out.println(s);
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

								/**
								 * SharedPreferences存储用户Id和uniqueKey
								 */
								SharedPreferences sharedPreferences = getSharedPreferences(
										"userLogin", Context.MODE_PRIVATE);
								Editor editor = sharedPreferences.edit();// 获取编辑器
								editor.putInt("userId", userLogin.getUserId());
								editor.putString("imId", userLogin.getImId());
								editor.putString("uniqueKey",
										userLogin.getUniqueKey());
								editor.putString("userName",
										userLogin.getUserName());
								editor.putString("userImage",
										userLogin.getUserImage());
								editor.putString("imUserName",
										userLogin.getImUserName());
								editor.putString("imPassword",
										userLogin.getImPassword());
								editor.putString("mobilePhone",
										userLogin.getMobilePhone());
								editor.putString("riderId",
										userLogin.getRiderId());

								editor.putInt("loginFlag", 1);
								editor.commit();

								LoginActivity.this.finish();
								startActivity(new Intent(LoginActivity.this, MainActivity.class));
								/*
								 * startActivity(new Intent(LoginActivity.this,
								 * MainActivity.class));
								 */

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

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		dissmissProgressDialog();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("TAG", "-->onActivityResult " + requestCode + " resultCode="
				+ resultCode);

		mTencent.onActivityResultData(requestCode, resultCode, data,
				loginListener);

		System.out.println("requestCode:" + requestCode
				+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("resultCode:" + resultCode
				+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		if (requestCode == Constants.REQUEST_API) {
			// if(resultCode == Constants.RESULT_LOGIN) {
			System.out
					.println("data:" + data + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			Tencent.handleResultData(data, loginListener);
			Log.d("TAG", "-->onActivityResult handle logindata");
			// }
		} else if (requestCode == Constants.REQUEST_APPBAR) {
			// if (resultCode == Constants.RESULT_LOGIN) {
			Toasts.show(LoginActivity.this,
					data.getStringExtra(Constants.LOGIN_INFO) + "........", 0);
			// }
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/*
	*//**
	 * 菜单、返回键响应
	 */
	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK) { exitBy2Click(); // 调用双击退出函数 } return
	 * false; }
	 */

	/**
	 * 双击退出函数
	 */

	/*
	 * private void exitBy2Click() { Timer tExit = null; if (isExit == false) {
	 * isExit = true; // 准备退出 Toast.makeText(this, "再按一次退出程序",
	 * Toast.LENGTH_SHORT).show(); tExit = new Timer(); tExit.schedule(new
	 * TimerTask() {
	 * 
	 * @Override public void run() { isExit = false; // 取消退出 } }, 2000); //
	 * 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务 } else { finish(); System.exit(0); } }
	 */

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("拉取第三方授权页面...");
		progDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

}
