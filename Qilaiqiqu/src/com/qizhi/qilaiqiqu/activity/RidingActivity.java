package com.qizhi.qilaiqiqu.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.RidingListAdapter;
import com.qizhi.qilaiqiqu.model.CollectModel;
import com.qizhi.qilaiqiqu.model.RidingModel;
import com.qizhi.qilaiqiqu.model.RidingModelList;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */

public class RidingActivity extends Activity implements OnClickListener,
		OnItemClickListener, CallBackPost {

	public static RidingActivity ridingActivity;

	private RidingListAdapter adapter;

	private LinearLayout layoutBtn;

	private ListView ridingList;

	private List<RidingModelList> list;
	private XUtilsUtil xUtilsUtil;
	private RidingModel ridingModel;
	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_riding);

		initView();
		initEvent();

	}

	private void initView() {
		ridingModel = new RidingModel();
		list = new ArrayList<RidingModelList>();
		xUtilsUtil = new XUtilsUtil();
		preferences = getSharedPreferences("userLogin",Context.MODE_PRIVATE);
		
		layoutBtn = (LinearLayout) findViewById(R.id.layout_ridingActivity_back);
		ridingList = (ListView) findViewById(R.id.list_ridingActivity_riding);
		
	}

	private void initEvent() {
		layoutBtn.setOnClickListener(this);
		ridingList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		Intent intent = new Intent(RidingActivity.this,
				RidingDetailsActivity.class);
		intent.putExtra("isMe", true);
		intent.putExtra("articleId",list.get(position).getArticleId() );
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_ridingActivity_back:
			finish();
			break;

		default:
			break;
		}
	}

	
	
	
	
	@Override
	protected void onStart() {
		httpRiding();
		
		super.onStart();
	}

	public void httpRiding() {
		String url = "mobile/articleMemo/queryArticleMemoByUserIdPaginationList.html";
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageStart", "0");
		params.addBodyParameter("pageSize", "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost(url, params, RidingActivity.this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(responseInfo.result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(jsonObject.optBoolean("result")){
			Gson gson = new Gson();
			Type type = new TypeToken<RidingModel>(){}.getType();
			ridingModel = gson.fromJson(jsonObject.toString(), type);
			list = ridingModel.getDataList();
			adapter = new RidingListAdapter(this, list);
			ridingList.setAdapter(adapter);
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {

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