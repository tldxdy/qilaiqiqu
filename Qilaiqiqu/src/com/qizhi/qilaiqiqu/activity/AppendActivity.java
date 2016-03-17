package com.qizhi.qilaiqiqu.activity;

import java.util.List;

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
import com.qizhi.qilaiqiqu.adapter.AppendGridAdapter;
import com.qizhi.qilaiqiqu.model.ActivityModel;
import com.qizhi.qilaiqiqu.model.ActivityModel.ParticipantList;

public class AppendActivity extends HuanxinLogOutActivity implements
		OnItemClickListener, OnClickListener {

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	ActivityModel model;

	private AppendGridAdapter adapter;

	private List<ParticipantList> participantList;

	private GridView gridview;
	private LinearLayout backLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_append);
		initView();
		initEvent();
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		gridview = (GridView) findViewById(R.id.gridview);
		backLayout = (LinearLayout) findViewById(R.id.layout_appendActivity_back);

		Intent intent = this.getIntent();
		participantList = (List<ParticipantList>) intent
				.getSerializableExtra("append");
		adapter = new AppendGridAdapter(participantList, this);

	}

	private void initEvent() {
		gridview.setAdapter(adapter);
		backLayout.setOnClickListener(this);
		gridview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		startActivity(new Intent(AppendActivity.this, PersonActivity.class)
				.putExtra("userId", participantList.get(position).getUserId()));
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

}
