package com.qizhi.qilaiqiqu.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.qizhi.qilaiqiqu.utils.ActivityCollectorUtil;

public class HuanxinLogOutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ActivityCollectorUtil.addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollectorUtil.removeActivity(this);
	}
}
