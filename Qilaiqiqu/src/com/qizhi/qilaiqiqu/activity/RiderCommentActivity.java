package com.qizhi.qilaiqiqu.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

public class RiderCommentActivity extends HuanxinLogOutActivity implements
		OnClickListener {

	private TextView numTxt;
	private TextView nameTxt;

	private Button comfirmBtn;

	private EditText commentEdt;

	private LinearLayout backLayout;

	private String riderId;
	private XUtilsUtil xUtilsUtil;
	private SharedPreferences preferences;
	
	private InputMethodManager imm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rider_comment);
		initView();
		initEvent();
	}

	private void initView() {
		xUtilsUtil = new XUtilsUtil();
		riderId = getIntent().getStringExtra("riderId");
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		comfirmBtn = (Button) findViewById(R.id.btn_riderCommentActivity_comfirm);
		numTxt = (TextView) findViewById(R.id.txt_riderCommentActivity_return_num);
		nameTxt = (TextView) findViewById(R.id.txt_riderCommentActivity_return_name);
		commentEdt = (EditText) findViewById(R.id.edt_riderCommentActivity_my_return);
		backLayout = (LinearLayout) findViewById(R.id.layout_riderCommentActivity_back);

	}

	private void initEvent() {
		comfirmBtn.setOnClickListener(this);
		backLayout.setOnClickListener(this);
		commentEdt.addTextChangedListener(new TextWatcher() {

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
				numTxt.setText(Html.fromHtml(s.length()
						+ "<font color='#9d9d9e'>/50</font>"));
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_riderCommentActivity_comfirm:
			commentRiding();
			break;

		case R.id.layout_riderCommentActivity_back:
			finish();
			break;

		default:
			break;
		}
	}

	private void commentRiding() {
		RequestParams params = new RequestParams("UTF-8");
		params.addQueryStringParameter("riderId", riderId);
		params.addQueryStringParameter("userId",
				preferences.getInt("userId", -1) + "");
		params.addBodyParameter("superId", "0");
		params.addBodyParameter("parentId", "0");
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		params.addBodyParameter("commentMemo", commentEdt.getText().toString());

		xUtilsUtil.httpPost("mobile/riderComment/insertComment.html", params,
				new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						String s = responseInfo.result;
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(s);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (jsonObject.optBoolean("result")) {
							commentEdt.setText("");
							commentEdt.setHint("说几句吧");
							Toasts.show(RiderCommentActivity.this, "评论成功", 0);

							imm.hideSoftInputFromWindow(
									commentEdt.getWindowToken(), 0);
							finish();
						} else {
							Toasts.show(RiderCommentActivity.this,
									"评论失败:"+jsonObject.optString("message"), 0);
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						Toasts.show(RiderCommentActivity.this, "未知错误，请重试", 0);
					}
				});
	}
}
