package com.qizhi.qilaiqiqu.activity;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

public class BindPhoneActivity extends Activity implements OnClickListener,
		CallBackPost {

	private LinearLayout backLayout;// 返回按钮

	private TextView sendTxt;

	private int i = 60;
	private EditText phoneEdt;
	private EditText codeEdt;

	private Timer timer;
	private TimerTask task;
	private int timeFlag = 1;
	private int phoneFlag = 1; // 电话号码输入标识 1为未满11位，2为满11位
	private int finishFlag = 1; // 退出标识 1为不允许推出，2为允许推出
	private int userId;
	private String uniqueKey;
	private String mobilePhone;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			sendTxt.setFocusableInTouchMode(false);
			sendTxt.setText("重新发送(" + i + ")");
			if (msg.arg1 == 0) {
				stopTime();
			} else {
				startTime();
			}
			super.handleMessage(msg);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bind_phone);

		initView();
		initEvnet();
	}

	private void initView() {
		backLayout = (LinearLayout) findViewById(R.id.layout_bindphoneactivity_back);

		sendTxt = (TextView) findViewById(R.id.btn_bindphoneactivity_send);

		phoneEdt = (EditText) findViewById(R.id.edt_bindphoneactivity_phone);
		codeEdt = (EditText) findViewById(R.id.edt_bindphoneactivity_code);

		Intent intent = getIntent();
		userId = intent.getIntExtra("userId", -1);
		uniqueKey = intent.getStringExtra("uniqueKey");
	}

	private void initEvnet() {
		backLayout.setOnClickListener(this);
		sendTxt.setOnClickListener(this);
		codeEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable e) {
				if (e.length() == 6) {
					httpRegister();
					codeEdt.setText("");
				}
			}
		});
		phoneEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() != 11) {
					phoneFlag = 1;
				} else {
					phoneFlag = 2;
				}
			}
		});
	}

	/**
	 * 手机获取验证码
	 */
	private void httpSendCode() {
		String url = "common/sendSmsAuthCode.html";
		mobilePhone = phoneEdt.getText().toString().trim();

		RequestParams params = new RequestParams();
		params.addBodyParameter("mobilePhone", mobilePhone);
		params.addBodyParameter("type", "XGSJHM");

		new XUtilsUtil().httpPost(url, params, BindPhoneActivity.this);
	}

	private void httpRegister() {
		finishFlag = 2;
		String url = "mobile/user/changeUserMobilePhone.html";
		final String mobilePhone = phoneEdt.getText().toString().trim();
		String codeNo = codeEdt.getText().toString().trim();

		RequestParams params = new RequestParams();
		params.addBodyParameter("mobilePhone", mobilePhone);
		params.addBodyParameter("uniqueKey", uniqueKey);
		params.addBodyParameter("userId", userId + "");
		params.addBodyParameter("codeNo", codeNo);

		new XUtilsUtil().httpPost(url, params, new CallBackPost() {

			@Override
			public void onMySuccess(ResponseInfo<String> responseInfo) {
				try {
					JSONObject jsonObject = new JSONObject(responseInfo.result);
					if (jsonObject.getBoolean("result")) {
						if (finishFlag == 2) {
							new SystemUtil().makeToast(BindPhoneActivity.this,
									"绑定成功");
							Intent intent = new Intent();
							intent.putExtra("mobilePhone", mobilePhone);
							setResult(4, intent);
							finishFlag = 1;
							finish();
						}

					} else {
						String string = jsonObject.getString("message");
						new SystemUtil().makeToast(BindPhoneActivity.this,
								string);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onMyFailure(HttpException error, String msg) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_bindphoneactivity_back:
			finish();
			break;
		case R.id.btn_bindphoneactivity_send:
			if (phoneFlag == 2) {
				if (timeFlag == 1) {
					startTime();
					httpSendCode();
					timeFlag = 2;
				} else {
					new SystemUtil().makeToast(BindPhoneActivity.this,
							"验证码每60秒发送发送一次");
				}
			} else {
				new SystemUtil().makeToast(BindPhoneActivity.this, "手机号码为11位");
			}
			break;

		default:
			break;
		}
	}

	/**
	 * @param 5秒启动handle
	 */
	private void startTime() {
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				i--;
				Message message = handler.obtainMessage();
				message.arg1 = i;
				handler.sendMessage(message);
			}

		};

		timer.schedule(task, 1000);
	}

	private void stopTime() {
		timeFlag = 1;
		i = 60;
		sendTxt.setText("重新发送");
		timer.cancel();
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		try {
			JSONObject jsonObject = new JSONObject(responseInfo.result);
			if (jsonObject.getBoolean("result")) {
				if (finishFlag == 1) {
					new SystemUtil()
							.makeToast(BindPhoneActivity.this, "验证码已发送");
				} else {
					new SystemUtil().makeToast(BindPhoneActivity.this, "绑定成功");
					Intent intent = new Intent();
					intent.putExtra("mobilePhone", mobilePhone);
					setResult(4, intent);
					finish();
				}
			} else {
				String string = jsonObject.getString("message");
				new SystemUtil().makeToast(BindPhoneActivity.this, string);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		new SystemUtil().makeToast(BindPhoneActivity.this, "请求失败" + error + ":"
				+ msg);
		if (finishFlag == 2) {
			codeEdt.setText("");
		}
	}
}
