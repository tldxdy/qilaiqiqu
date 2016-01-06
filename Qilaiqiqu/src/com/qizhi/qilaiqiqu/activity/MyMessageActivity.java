package com.qizhi.qilaiqiqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.MyMessageAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author hujianbo
 *
 */
public class MyMessageActivity extends Activity implements OnClickListener,OnItemClickListener{

	private ListView MyMessageList;		//系统消息的集合
	private LinearLayout backLayout;		//返回图片
	private MyMessageAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_message);
		
		initView();
	}

	private void initView() {
		backLayout = (LinearLayout) findViewById(R.id.layout_mymessageactivity_back);
		backLayout.setOnClickListener(this);
		MyMessageList = (ListView) findViewById(R.id.list_mymessageactivity_my_message);
		adapter = new MyMessageAdapter(this);
		MyMessageList.setAdapter(adapter);
		MyMessageList.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_mymessageactivity_back:
			MyMessageActivity.this.finish();
			//Toast.makeText(this, "点击返回", 0).show();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(this, "点击了item"+position, 0).show();
		Intent intent = new Intent(this, CommentMessageActivity.class);
		startActivity(intent);
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
