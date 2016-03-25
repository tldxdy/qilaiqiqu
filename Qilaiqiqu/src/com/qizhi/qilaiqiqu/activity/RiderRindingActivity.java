package com.qizhi.qilaiqiqu.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.RiderRecommendAdapter;
import com.umeng.analytics.MobclickAgent;


/**
 * 
 * @author Administrator
 *			陪骑
 *
 */
public class RiderRindingActivity extends HuanxinLogOutActivity implements
		OnClickListener, OnItemClickListener {
	private LinearLayout backLayout;
	private ListView riderList;
	private RiderRecommendAdapter adapter;
	private List<?> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rider_recommend);
		initView();
		initEvent();
	}

	private void initView() {

		backLayout = (LinearLayout) findViewById(R.id.layout_actioncenteractivity_back);
		riderList = (ListView) findViewById(R.id.list_activityriderrecommend_list);
		/*adapter = new RiderRecommendAdapter(this, list);
		riderList.setAdapter(adapter);
		riderList.setDividerHeight(0);*/

	}

	private void initEvent() {
		backLayout.setOnClickListener(this);
		riderList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_actioncenteractivity_back:
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
