package com.qizhi.qilaiqiqu.activity;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.GroupGridAdapter;
import com.qizhi.qilaiqiqu.adapter.GroupMemberAdapter;
import com.qizhi.qilaiqiqu.model.GroupMemberModel;
import com.qizhi.qilaiqiqu.model.UserLoginModel;
import com.qizhi.qilaiqiqu.model.GroupMemberModel.Data.UserList;
import com.qizhi.qilaiqiqu.utils.ActivityCollectorUtil;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.squareup.picasso.Picasso;

public class GroupActivity extends HuanxinLogOutActivity implements
		OnItemClickListener, OnClickListener {

	private GridView gridview;
	private LinearLayout backLayout;
	private RelativeLayout ownerLayout;

	private ImageView ownerImg;
	private TextView ownerName;

	private TextView txtQuit;

	private GroupMemberModel model;
	private GroupMemberAdapter adapter;

	private SharedPreferences sharedPreferences;

	private List<UserList> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_group);
		initView();
		getGroupMember();
	}

	private void initView() {
		ownerImg = (ImageView) findViewById(R.id.groupActivity_ownerImg);
		ownerName = (TextView) findViewById(R.id.groupActivity_ownerName);

		txtQuit = (TextView) findViewById(R.id.groupActivity_quitActivity);

		gridview = (GridView) findViewById(R.id.gridview);
		ownerLayout = (RelativeLayout) findViewById(R.id.groupActivity_owner);
		backLayout = (LinearLayout) findViewById(R.id.layout_groupActivity_back);

		sharedPreferences = getSharedPreferences("userLogin",
				Context.MODE_PRIVATE);
	}

	private void initEvent() {
		// gridview.setAdapter(adapter);
		backLayout.setOnClickListener(this);
		ownerLayout.setOnClickListener(this);

		txtQuit.setOnClickListener(this);

		gridview.setOnItemClickListener(this);

		adapter = new GroupMemberAdapter(list, GroupActivity.this);
		gridview.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_groupActivity_back:
			finish();
			break;

		case R.id.groupActivity_owner:
			startActivity(new Intent(GroupActivity.this, PersonActivity.class)
					.putExtra("userId", model.getData().getOwner().getUserId()));
			break;

		case R.id.groupActivity_quitActivity:
			quitActivity();
			break;
		default:
			break;
		}
	}

	private void quitActivity() {
		RequestParams params = new RequestParams("UTF-8");
		params.addQueryStringParameter("activityId",
				getIntent().getIntExtra("activityId", -1) + "");
		params.addQueryStringParameter("userId",
				sharedPreferences.getInt("userId", -1) + "");
		params.addQueryStringParameter("uniqueKey",
				sharedPreferences.getString("uniqueKey", null));

		new XUtilsUtil().httpPost(
				"mobile/participant/cancelActivityParticipant.html", params,
				new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						JSONObject object;

						try {
							object = new JSONObject(result);
							if (object.getBoolean("result")) {
								new SystemUtil()
										.makeToast(
												GroupActivity.this,
												"已对取消《"
														+ getIntent()
																.getStringExtra(
																		"groupName")
														+ "》活动的报名");
								new ActivityCollectorUtil().finishAll();
								startActivity(new Intent(GroupActivity.this,
										MainActivity.class).putExtra(
										"fragmentNum", 1));
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						new SystemUtil()
								.makeToast(GroupActivity.this, "取消报名失败");
					}
				});
	}

	private void getGroupMember() {
		RequestParams params = new RequestParams("UTF-8");
		params.addQueryStringParameter("imGroupId",
				getIntent().getStringExtra("username") + "");
		params.addQueryStringParameter("uniqueKey",
				sharedPreferences.getString("uniqueKey", null));

		new XUtilsUtil().httpPost(
				"mobile/activity/queryGroupForUserPaginationList.html", params,
				new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						JSONObject object;

						try {
							object = new JSONObject(result);
							if (object.getBoolean("result")) {
								Gson gson = new Gson();
								Type type = new TypeToken<GroupMemberModel>() {
								}.getType();
								model = gson.fromJson(result, type);
								list = model.getData().getUserList();
							}

							initEvent();

							ownerName.setText(model.getData().getOwner()
									.getUserName()
									+ "");
							Picasso.with(GroupActivity.this)
									.load(SystemUtil.IMGPHTH
											+ model.getData().getOwner()
													.getUserImage())
									.into(ownerImg);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {

					}
				});

	}

	@Override
	public void onItemClick(AdapterView<?> viewGroup, View v, int position,
			long arg3) {
		startActivity(new Intent(GroupActivity.this, PersonActivity.class)
				.putExtra("userId", model.getData().getUserList().get(position)
						.getUserId()));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
