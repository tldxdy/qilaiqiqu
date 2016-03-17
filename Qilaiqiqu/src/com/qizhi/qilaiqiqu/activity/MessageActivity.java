package com.qizhi.qilaiqiqu.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import cn.jpush.android.api.JPushInterface;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.PersonMessageListAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */
public class MessageActivity extends HuanxinLogOutActivity implements OnClickListener,
		OnItemClickListener {

	private Button backBtn;

	private ListView messageList;
	
	private PersonMessageListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_message);

		initView();
		initEvent();
	}

	private void initView() {
		backBtn = (Button) findViewById(R.id.btn_messageActivity_back);
		messageList = (ListView) findViewById(R.id.list_messageActivity_message);
		adapter = new PersonMessageListAdapter(this, 1);
		messageList.setAdapter(adapter);
	}

	private void initEvent() {
		backBtn.setOnClickListener(this);
		messageList.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_messageActivity_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}
	
}
