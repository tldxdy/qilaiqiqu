package com.qizhi.qilaiqiqu.activity;

import android.app.Activity;
import android.os.Bundle;

import com.qizhi.qilaiqiqu.utils.ActivityCollectorUtil;

public class HuanxinLogOutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityCollectorUtil.addActivity(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ActivityCollectorUtil.removeActivity(this);
	}
}
