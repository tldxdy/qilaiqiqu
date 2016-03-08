package com.qizhi.qilaiqiqu.activity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.RidingListAdapter;
import com.qizhi.qilaiqiqu.model.RidingDraftModel;
import com.qizhi.qilaiqiqu.model.RidingModel;
import com.qizhi.qilaiqiqu.model.RidingModelList;
import com.qizhi.qilaiqiqu.model.TravelsinformationModel;
import com.qizhi.qilaiqiqu.sqlite.DBManager;
import com.qizhi.qilaiqiqu.ui.FooterListView;
import com.qizhi.qilaiqiqu.ui.FooterListView.OnfreshListener;
import com.qizhi.qilaiqiqu.ui.Refresh;
import com.qizhi.qilaiqiqu.utils.PopupWindowUploading;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */

public class RidingActivity extends Activity implements OnClickListener,
		OnItemClickListener, CallBackPost,OnRefreshListener,OnfreshListener{

	public static RidingActivity ridingActivity;

	private RidingListAdapter adapter;

	private LinearLayout layoutBtn;

	private FooterListView ridingList;

	private List<RidingModelList> list;
	private List<RidingDraftModel> rDraftModels;
	private XUtilsUtil xUtilsUtil;
	private RidingModel ridingModel;
	private SharedPreferences preferences;
	
	private PopupWindowUploading pUploading;
	
	private Refresh swipeLayout;
	private View header;
	
	private int pageIndex = 1;
	private boolean isFirst = true;
	
	private DBManager dbManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_riding);

		initView();
		initEvent();

	}

	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	private void initView() {
		dbManager = new DBManager(this);
		ridingModel = new RidingModel();
		list = new ArrayList<RidingModelList>();
		xUtilsUtil = new XUtilsUtil();
		preferences = getSharedPreferences("userLogin",Context.MODE_PRIVATE);
		
		layoutBtn = (LinearLayout) findViewById(R.id.layout_ridingActivity_back);
		ridingList = (FooterListView) findViewById(R.id.list_ridingActivity_riding);
		header = getLayoutInflater().inflate(R.layout.header, null);
		swipeLayout = (Refresh) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		ridingList.addHeaderView(header);
		pUploading = new PopupWindowUploading(this);
		
		
	}
	private boolean f = true;
	private Handler handler = new Handler();
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(f){
			f = false; 
			pUploading.show(this.findViewById(R.id.layout_riding_top));
		}
	}

	private void initEvent() {
		layoutBtn.setOnClickListener(this);
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		if(position <= rDraftModels.size()){
			System.out.println();
			Intent intent = new Intent(this, ReleaseActivity.class);
			Gson gson = new Gson();
			Type type = new TypeToken<List<TravelsinformationModel>>(){}.getType();
			List<TravelsinformationModel> trList = gson.fromJson(rDraftModels.get(position - 1).getJsonString(), type);
			System.out.println(rDraftModels.size()  + "-----------" + position);
			
			intent.putExtra("_id", rDraftModels.get(position - 1).get_id());
			intent.putExtra("list", (Serializable) trList);
			startActivity(intent);
			return;
		}else if(position <= rDraftModels.size() + list.size()){
			Intent intent = new Intent(RidingActivity.this,
					RidingDetailsActivity.class);
			intent.putExtra("isMe", true);
			intent.putExtra("articleId",list.get(position - rDraftModels.size() - 1).getArticleId() );
			startActivity(intent);
		}
		
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
		rDraftModels = dbManager.queryAll();
		isFirst = true;
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				httpRiding();
			}
		}, 500);
		super.onStart();
	}

	public void httpRiding() {
		String url = "mobile/articleMemo/queryArticleMemoByUserIdPaginationList.html";
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		if(isFirst){
			pageIndex = 1;
		}
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize", "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost(url, params, this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		pUploading.dismiss();
					
					
					
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
			if(isFirst){
				list = ridingModel.getDataList();
				adapter = new RidingListAdapter(this, list , rDraftModels);
				ridingList.setAdapter(adapter);
				ridingList.setDividerHeight(0);
				//ridingList.setOnRefreshListener(this);
			}else{
				list.addAll(ridingModel.getDataList());
			}
			adapter.notifyDataSetChanged();
			swipeLayout.setRefreshing(false);
			ridingList.setOnItemClickListener(this);
			swipeLayout.setOnRefreshListener(this);
			ridingList.setOnfreshListener(this);
			ridingList.completeRefresh();
			//ridingList.finishRefreshing();
			
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		swipeLayout.setRefreshing(false);
		ridingList.completeRefresh();
		Toasts.show(this, msg, 0);
		pUploading.dismiss();
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

	@Override
	public void onRefresh() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				pageIndex = 1;
				isFirst = true;
				httpRiding();
			}
		}, 1500);

	}


	@Override
	public void onLoadingMore() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				pageIndex = pageIndex + 1;
				isFirst = false;
				httpRiding();
			}
		}, 1500);
	}

/*	@Override
	public void onRefresh() {
		pageIndex = 1;
		isFirst = true;
		httpRiding();
	}

	@Override
	public void onLoadingMore() {
		pageIndex = pageIndex + 1;
		isFirst = false;
		httpRiding();
	}
*/
}
