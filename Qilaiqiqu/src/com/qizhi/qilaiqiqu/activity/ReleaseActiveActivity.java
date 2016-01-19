package com.qizhi.qilaiqiqu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.ReleaseActiveGridAdapter;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

public class ReleaseActiveActivity extends Activity implements OnClickListener,
		CallBackPost {

	private EditText timeEdt;
	private EditText dateEdt;
	private EditText themeEdt;
	private EditText signatureEdt;
	private EditText moneyTxt;

	private TextView signatureTxt;
	private TextView releaseTxt;
	private TextView memoTxt;

	private LinearLayout dateLayout;
	private LinearLayout timeLayout;

	private GridView pictureGrid;

	XUtilsUtil xUtilsUtil;

	SharedPreferences preferences;

	private ReleaseActiveGridAdapter adapter;

	private ImageView addImg;

	private ArrayList<String> photoList;
	private boolean isFirst = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_activity_release);
		initView();
		initEvent();
	}

	private void initView() {
		dateEdt = (EditText) findViewById(R.id.edt_releaseActiveActivity_date);
		timeEdt = (EditText) findViewById(R.id.edt_releaseActiveActivity_time);
		themeEdt = (EditText) findViewById(R.id.edt_releaseActiveActivity_theme);
		moneyTxt = (EditText) findViewById(R.id.edt_releaseActiveActivity_money);
		signatureEdt = (EditText) findViewById(R.id.edt_releaseActiveActivity_signature);

		signatureTxt = (TextView) findViewById(R.id.txt_releaseActiveActivity_textLengh);
		releaseTxt = (TextView) findViewById(R.id.txt_releaseActiveActivity_release);
		memoTxt = (TextView) findViewById(R.id.txt_releaseActiveActivity_memoLengh);

		dateLayout = (LinearLayout) findViewById(R.id.layout_releaseActiveActivity_date);
		timeLayout = (LinearLayout) findViewById(R.id.layout_releaseActiveActivity_time);

		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		addImg = (ImageView) findViewById(R.id.img_releaseActiveActivity_add);

		photoList = new ArrayList<String>();
		isFirst = getIntent().getBooleanExtra("isFirst", false);
		if (isFirst) {
			photoList = getIntent().getStringArrayListExtra("photoList");
		}

		pictureGrid = (GridView) findViewById(R.id.grid_releaseActiveActivity_picture);
		adapter = new ReleaseActiveGridAdapter(photoList, this);
		pictureGrid.setAdapter(adapter);
		Toast.makeText(this, "list.size()" + photoList.size(), 0).show();
	}

	private void initEvent() {

		addImg.setOnClickListener(this);

		dateLayout.setOnClickListener(this);
		timeLayout.setOnClickListener(this);

		releaseTxt.setOnClickListener(this);
		themeEdt.addTextChangedListener(new TextWatcher() {

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
				signatureTxt.setText(Html.fromHtml(e.length()
						+ "<font color='#9d9d9e'>/10</font>"));
			}
		});

		signatureEdt.addTextChangedListener(new TextWatcher() {

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
				memoTxt.setText(Html.fromHtml(e.length()
						+ "<font color='#9d9d9e'>/150</font>"));
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_releaseActiveActivity_release:
			release();
			break;

		case R.id.layout_releaseActiveActivity_date:

			break;

		case R.id.layout_releaseActiveActivity_time:

			break;

		case R.id.img_releaseActiveActivity_add:
			startActivity(new Intent(ReleaseActiveActivity.this,
					SelectImagesActivity.class));
			break;

		default:
			break;
		}
	}

	private void release() {
		RequestParams params2 = new RequestParams();
		params2.addBodyParameter("userId", preferences.getInt("userId", -1)
				+ "");
		params2.addBodyParameter("activityTitle", themeEdt.getText().toString());
		params2.addBodyParameter("activityMemo", signatureEdt.getText()
				.toString());
		params2.addBodyParameter("startDate", "");
		params2.addBodyParameter("duration", "");
		params2.addBodyParameter("lanInfo", "");
		params2.addBodyParameter("lanName", "");
		params2.addBodyParameter("mileage", "");
		params2.addBodyParameter("activityImage", "");
		params2.addBodyParameter("location", "");
		params2.addBodyParameter("outlay", moneyTxt.getText().toString());
		params2.addBodyParameter("uniqueKey", "");

		xUtilsUtil.httpPost("mobile/activity/publishActivity.html", params2,
				this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {

	}

	@Override
	public void onMyFailure(HttpException error, String msg) {

	}

}
