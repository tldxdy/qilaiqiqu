package com.qizhi.qilaiqiqu.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.RiderRecommendAdapter;
import com.qizhi.qilaiqiqu.model.RiderRecommendModel;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 
 * @author Administrator
 *			陪骑推荐
 *
 */
public class RiderRecommendActiviity extends HuanxinLogOutActivity implements
		OnClickListener, OnItemClickListener, CallBackPost {
	
	private LinearLayout backLayout;
	private ListView riderList;
	private RiderRecommendAdapter adapter;
	private List<RiderRecommendModel> list;
	private int pageIndex = 1;
	private SharedPreferences preferences;
	private XUtilsUtil xUtilsUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rider_recommend);
		initView();
		initEvent();
	}

	private void initView() {
		list = new ArrayList<RiderRecommendModel>();
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		xUtilsUtil = new XUtilsUtil();
		
		backLayout = (LinearLayout) findViewById(R.id.layout_actioncenteractivity_back);
		riderList = (ListView) findViewById(R.id.list_activityriderrecommend_list);
		adapter = new RiderRecommendAdapter(this, list);
		riderList.setAdapter(adapter);
		riderList.setDividerHeight(0);
		data();
		
	}

	private void initEvent() {
		backLayout.setOnClickListener(this);
		riderList.setOnItemClickListener(this);
	}

	private void data() {
		pageIndex = 1;
		RequestParams params = new RequestParams();
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize",  "10");
		xUtilsUtil.httpPost("common/queryAttendRiderPaginationList.html", params, this);
	}
	
	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		String s = responseInfo.result;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(s);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObject.optBoolean("result")) {
			Gson gson = new Gson();
			List<RiderRecommendModel> lists = gson.fromJson(jsonObject.optJSONArray("dataList").toString(),
					new TypeToken<ArrayList<RiderRecommendModel>>(){}.getType());
			list.clear();
			list.addAll(lists);
			adapter.notifyDataSetChanged();
		}else{
			Toasts.show(this, jsonObject.optInt("message"), 0);
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		Toasts.show(this, msg , 0);
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Toasts.show(this, position + "", 0);
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
