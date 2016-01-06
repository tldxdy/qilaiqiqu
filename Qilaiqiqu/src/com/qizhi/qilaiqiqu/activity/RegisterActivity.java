package com.qizhi.qilaiqiqu.activity;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */
public class RegisterActivity extends Activity implements OnClickListener,
		CallBackPost {

	private LinearLayout backLayout;// 返回按钮

	private TextView sendTxt;

	private EditText phoneEdt;
	private EditText passEdt;
	private EditText codeEdt;

	private Timer timer;
	private TimerTask task;

	private int i = 60;
	private int timeFlag = 1;
	private int phoneFlag = 1; // 电话号码输入标识 1为未满11位，2为满11位
	private int passFlag = 1; // 密码输入标识 1为不满足6至13位输入条件，2为满足
	private int finishFlag = 1; // 退出标识 1为不允许推出，2为允许推出

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
		setContentView(R.layout.activity_register);
		initView();
		initEvnet();
	}

	private void initView() {
		backLayout = (LinearLayout) findViewById(R.id.layout_registerActivity_back);
		sendTxt = (TextView) findViewById(R.id.btn_registerActivity_send);
		phoneEdt = (EditText) findViewById(R.id.edt_registerActivity_phone);
		passEdt = (EditText) findViewById(R.id.edt_registerActivity_pass);
		codeEdt = (EditText) findViewById(R.id.edt_registerActivity_code);
	}

	private void initEvnet() {
		sendTxt.setOnClickListener(this);
		backLayout.setOnClickListener(this);
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
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable e) {
				if (e.length() == 11) {
					phoneFlag = 2;
				} else {

					phoneFlag = 1;
				}
			}
		});
		passEdt.addTextChangedListener(new TextWatcher() {

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
				if (e.length() > 5 && e.length() <= 13) {
					passFlag = 2;
				} else {
					passFlag = 1;
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_registerActivity_back:
			finish();
			break;

		case R.id.btn_registerActivity_send:
			if (phoneFlag == 2) {
				if (passFlag == 2) {
					if (timeFlag == 1) {
						startTime();
						httpSendCode();
						timeFlag = 2;
					} else {
						new SystemUtil().makeToast(RegisterActivity.this,
								"验证码每60秒发送发送一次");
					}
				} else {
					new SystemUtil().makeToast(RegisterActivity.this,
							"密码为6至13位");
				}
			} else {
				new SystemUtil().makeToast(RegisterActivity.this, "手机号码为11位");
			}
			break;

		default:
			break;
		}
	}

	private void httpSendCode() {
		String url = "common/sendSmsAuthCode.html";
		String mobilePhone = phoneEdt.getText().toString().trim();

		RequestParams params = new RequestParams();
		params.addBodyParameter("mobilePhone", mobilePhone);
		params.addBodyParameter("type", "YHZC");

		new XUtilsUtil().httpPost(url, params, RegisterActivity.this);
	}

	private void httpRegister() {
		finishFlag = 2;
		String url = "common/userRegister.html";
		String mobilePhone = phoneEdt.getText().toString().trim();
		String loginPwd = passEdt.getText().toString().trim();
		String codeNo = codeEdt.getText().toString().trim();

		RequestParams params = new RequestParams();
		params.addBodyParameter("mobilePhone", mobilePhone);
		params.addBodyParameter("loginPwd", loginPwd);
		params.addBodyParameter("codeNo", codeNo);

		new XUtilsUtil().httpPost(url, params, RegisterActivity.this);
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
		// new SystemUtil().makeToast(RegisterActivity.this, "请求成功"
		// + responseInfo.result);
		try {
			JSONObject jsonObject = new JSONObject(responseInfo.result);
			if (jsonObject.getBoolean("result")) {
				if (finishFlag == 1) {
					new SystemUtil().makeToast(RegisterActivity.this, "验证码已发送");
				}else {
					new SystemUtil().makeToast(RegisterActivity.this, "注册成功");
					finish();
				}
			} else {
				String string = jsonObject.getString("message");
				new SystemUtil().makeToast(RegisterActivity.this, string);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		new SystemUtil().makeToast(RegisterActivity.this, "请求失败" + error + ":"
				+ msg);
		if (finishFlag == 2) {
			codeEdt.setText("");
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
	
}
