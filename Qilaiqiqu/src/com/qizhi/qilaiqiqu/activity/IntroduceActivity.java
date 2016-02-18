package com.qizhi.qilaiqiqu.activity;

import com.qizhi.qilaiqiqu.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

public class IntroduceActivity extends Activity implements OnClickListener {

	private LinearLayout backLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_introduce);
		initView();
		initEvent();
	}

	private void initView() {
		backLayout = (LinearLayout) findViewById(R.id.layout_introduceActivity_back);

	}

	private void initEvent() {
		backLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_introduceActivity_back:
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

}
