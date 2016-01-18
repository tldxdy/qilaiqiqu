package com.qizhi.qilaiqiqu.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.qizhi.qilaiqiqu.R;

public class ReleaseActiveActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_activity_release);
	}
	
}
