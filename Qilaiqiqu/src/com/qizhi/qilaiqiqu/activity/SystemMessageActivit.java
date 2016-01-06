package com.qizhi.qilaiqiqu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qizhi.qilaiqiqu.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 *
 */

public class SystemMessageActivit extends Activity implements OnClickListener{
	
	private LinearLayout backLayout;
	
	private TextView contentTxt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_system_message);
		super.onCreate(savedInstanceState);
		initView();
		initEvent();
	}

	private void initView() {
		backLayout = (LinearLayout) findViewById(R.id.layout_systemMessageActivity_back);
		contentTxt = (TextView) findViewById(R.id.txt_systemMessageActivity_content);
	}

	private void initEvent() {
		backLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_systemMessageActivity_back:
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
