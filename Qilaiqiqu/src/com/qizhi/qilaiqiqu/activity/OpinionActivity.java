package com.qizhi.qilaiqiqu.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author leiqian
 * 
 */

public class OpinionActivity extends Activity implements OnClickListener,
		CallBackPost {

	private TextView sendTxt;

	private EditText emailEdt;
	private EditText opintionEdt;

	private LinearLayout backLayout;

	private XUtilsUtil xUtilsUtil;

	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_opintion);
		super.onCreate(savedInstanceState);
		initView();
		initEvent();
	}

	private void initView() {
		xUtilsUtil = new XUtilsUtil();
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		sendTxt = (TextView) findViewById(R.id.txt_opintionActivity_send);

		emailEdt = (EditText) findViewById(R.id.edt_opintionActivity_email);
		opintionEdt = (EditText) findViewById(R.id.edt_opintionActivity_opintion);

		backLayout = (LinearLayout) findViewById(R.id.layout_opinionActivity_back);

	}

	private void initEvent() {
		sendTxt.setOnClickListener(this);
		backLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_opintionActivity_send:
			if (preferences.getInt("userId", -1) != -1) {
				if (isEmail(emailEdt.getText().toString().trim())) {
					RequestParams params = new RequestParams("UTF-8");
					params.addBodyParameter("userId",
							preferences.getInt("userId", -1) + "");
					params.addBodyParameter("email", emailEdt.getText()
							.toString().trim());
					params.addBodyParameter("content", opintionEdt.getText()
							.toString().trim());
					params.addBodyParameter("uniqueKey",
							preferences.getString("uniqueKey", null));
					xUtilsUtil.httpPost("mobile/feedback/insertFeedback.html",
							params, this);
				}else{
					new SystemUtil().makeToast(this, "请输入正确的邮箱格式");
				}
			}

			break;

		case R.id.layout_opinionActivity_back:
			finish();
			break;

		default:
			break;
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

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		String s = responseInfo.result;
		try {
			JSONObject jsonObject = new JSONObject(s);
			if (jsonObject.optBoolean("result")) {
				new SystemUtil().makeToast(this, "我们已经收到你反馈的内容");
			} else {
				new SystemUtil().makeToast(this,
						jsonObject.optString("message"));
			}
			emailEdt.setText("");
			opintionEdt.setText("");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {

	}

	// 判断email格式是否正确
	public boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|"
				+ "(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}
}
