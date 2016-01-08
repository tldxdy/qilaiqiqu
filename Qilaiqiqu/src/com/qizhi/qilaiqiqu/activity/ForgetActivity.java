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
public class ForgetActivity extends Activity implements OnClickListener,
		CallBackPost {

	private EditText codeEdt;// 验证码
	private EditText phoneEdt;// 手机号码
	private EditText confirmEdt;// 确认密码
	private EditText newPassEdt;// 密码

	private LinearLayout backLauout;
	private TextView sendTxt;

	private TextView confirmTxt;
	private TextView newPassTxt;

	private Timer timer;
	private TimerTask task;

	private int i = 60;
	private int timeFlag = 1;
	private int passFlag = 1; // 密码输入标识 1为不满足6至13位输入条件，2为满足
	private int phoneFlag = 1;
	private int finishFlag = 1; // 退出标识 1为不允许推出，2为允许推出

	private int length; //新密码长度

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
		setContentView(R.layout.activity_forget);
		initView();
		initEvent();
	}

	private void initView() {
		codeEdt = (EditText) findViewById(R.id.edt_forgetActivity_code);
		phoneEdt = (EditText) findViewById(R.id.edt_forgetActivity_phone);
		confirmEdt = (EditText) findViewById(R.id.edt_forgetActivity_confirm);
		newPassEdt = (EditText) findViewById(R.id.edt_forgetActivity_newPass);

		confirmTxt = (TextView) findViewById(R.id.txt_forgetActivity_confirm);
		newPassTxt = (TextView) findViewById(R.id.txt_forgetActivity_newPass);

		backLauout = (LinearLayout) findViewById(R.id.layout_forgetActivity_back);
		sendTxt = (TextView) findViewById(R.id.btn_forgetActivity_send);
	}

	private void initEvent() {
		backLauout.setOnClickListener(this);
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

					confirmTxt.setTextColor(getResources().getColor(
							R.color.dark));
					newPassTxt.setTextColor(getResources().getColor(
							R.color.dark));

					confirmEdt.setFocusable(true);
					confirmEdt.setFocusableInTouchMode(true);
					confirmEdt.requestFocus();
					confirmEdt.findFocus();

					newPassEdt.setFocusable(true);
					newPassEdt.setFocusableInTouchMode(true);
					newPassEdt.requestFocus();
					newPassEdt.findFocus();

				}
			}
		});

		newPassEdt.addTextChangedListener(new TextWatcher() {
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
					length = e.length();
				} else {
					passFlag = 1;
				}
			}
		});

		confirmEdt.addTextChangedListener(new TextWatcher() {

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
				if (e.length() == length && e.length() != 0) {
					if (!confirmEdt.getText().toString().trim()
							.equals(newPassEdt.getText().toString().trim())) {
						new SystemUtil().makeToast(ForgetActivity.this,
								"两次输入不一致"
										+ confirmEdt.getText().toString()
												.trim()
										+ ":"
										+ newPassEdt.getText().toString()
												.trim());
						newPassEdt.setText("");
						confirmEdt.setText("");
					} else {
						httpModifi();
					}
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_forgetActivity_send:
			if (phoneFlag == 2) {
				if (timeFlag == 1) {
					startTime();
					httpSendCode();
					timeFlag = 2;
				} else {
					new SystemUtil().makeToast(ForgetActivity.this,
							"验证码每60秒发送发送一次");
				}
			} else {
				new SystemUtil().makeToast(ForgetActivity.this, "手机号码为11位");
			}
			break;

		case R.id.layout_forgetActivity_back:
			finish();
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
		params.addBodyParameter("type", "ZHMM");

		new XUtilsUtil().httpPost(url, params, ForgetActivity.this);
	}

	private void httpModifi() {
		finishFlag = 2;
		String url = "common/changeUserPassword.html";
		String mobilePhone = phoneEdt.getText().toString().trim();
		String password = confirmEdt.getText().toString().trim();
		String codeNo = codeEdt.getText().toString().trim();

		RequestParams params = new RequestParams();
		params.addBodyParameter("mobilePhone", mobilePhone);
		params.addBodyParameter("password", password);
		params.addBodyParameter("codeNo", codeNo);

		new XUtilsUtil().httpPost(url, params, ForgetActivity.this);
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
		try {
			JSONObject jsonObject = new JSONObject(responseInfo.result);
			if (jsonObject.getBoolean("result")) {
				if (finishFlag == 1) {
					new SystemUtil().makeToast(ForgetActivity.this, "验证码已发送");
				} else {
					new SystemUtil().makeToast(ForgetActivity.this, "修改成功");
					finish();
				}
			} else {
				String string = jsonObject.getString("message");
				new SystemUtil().makeToast(ForgetActivity.this, string);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onMyFailure(HttpException error, String msg) {

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
