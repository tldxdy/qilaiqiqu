package com.qizhi.qilaiqiqu.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.qizhi.qilaiqiqu.R;

public class GroupActivity extends Activity implements OnItemClickListener, OnClickListener  {
	
	private List<?> members;

	private GridView gridview;
	private LinearLayout backLayout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_group);
		initView();
		initEvent();
		
	}
	
	private void initView() {
		gridview = (GridView) findViewById(R.id.gridview);
		backLayout = (LinearLayout) findViewById(R.id.layout_appendActivity_back);
		
		Intent intent = this.getIntent();

	}

	private void initEvent() {
//		gridview.setAdapter(adapter);
		backLayout.setOnClickListener(this);
		gridview.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_appendActivity_back:
			finish();
			break;

		default:
			break;
		}		
	}

	@Override
	public void onItemClick(AdapterView<?> viewGroup, View v, int position, long arg3) {
		
	}

}
