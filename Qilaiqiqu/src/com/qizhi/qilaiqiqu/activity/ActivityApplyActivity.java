package com.qizhi.qilaiqiqu.activity;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.ActivityApplyAdapter;
import com.qizhi.qilaiqiqu.utils.SystemUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityApplyActivity extends Activity implements OnClickListener{

	private LinearLayout layoutBtn;// 返回按钮
	private ImageView photoImg;
	private TextView cancelTxt;
	private TextView nameTxt;
	private GridView gridView;
	
	private ActivityApplyAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_activity_apply);
		initView();
		initEvent();
	}



	private void initView() {
		layoutBtn = (LinearLayout) findViewById(R.id.layout_activityactivityapply_back);
		photoImg = (ImageView) findViewById(R.id.img_activityactivityapply_photo);
		cancelTxt = (TextView) findViewById(R.id.txt_activityactivityapply_cancel);
		nameTxt = (TextView) findViewById(R.id.txt_activityactivityapply_name);
		gridView = (GridView) findViewById(R.id.gridView__activityactivityapply);
		
	}
	private void initEvent() {
		layoutBtn.setOnClickListener(this);
		cancelTxt.setOnClickListener(this);
		
		adapter = new ActivityApplyAdapter(this);
		gridView.setAdapter(adapter);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_activityactivityapply_back:
			finish();
			break;
		case R.id.txt_activityactivityapply_cancel:
			new SystemUtil().makeToast(this, "取消报名");
			break;

		default:
			break;
		}
	}
}
