package com.qizhi.qilaiqiqu.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import com.qizhi.qilaiqiqu.adapter.CollectListAdapter;
import com.qizhi.qilaiqiqu.model.CollectModel;
import com.qizhi.qilaiqiqu.utils.RefreshLayout;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.RefreshLayout.OnLoadListener;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */
public class CollectActivity extends Activity implements OnClickListener,OnItemClickListener, CallBackPost,OnRefreshListener,
OnLoadListener{

	private LinearLayout layoutBtn;// 返回按钮

	private ListView collectList;// 收藏
	
	private CollectListAdapter adapter;
	
	private List<CollectModel> list;
	private XUtilsUtil xUtilsUtil;
	private SharedPreferences preferences;
	private int pageIndex = 1;
	private boolean isFirst = true;
	private RefreshLayout swipeLayout;
	private View header;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_collect);

		initView();
		initEvent();
		data();
	}


	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	private void initView() {
		list = new ArrayList<CollectModel>();
		xUtilsUtil = new XUtilsUtil();
		preferences = getSharedPreferences("userLogin",Context.MODE_PRIVATE);
		layoutBtn = (LinearLayout) findViewById(R.id.layout_collectActivity_back);
		collectList = (ListView) findViewById(R.id.list_collectActivity_collect);
		header = View.inflate(this,R.layout.header, null);
		swipeLayout = (RefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		collectList.addHeaderView(header);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);
	}

	private void initEvent() {
		layoutBtn.setOnClickListener(this);
		collectList.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_collectActivity_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		Intent intent = new Intent(this, RidingDetailsActivity.class);
		intent.putExtra("isMe", false);
		intent.putExtra("articleId",list.get(position-1).getQuoteId() );
		startActivity(intent);
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	
	
	
	@Override
	protected void onStart() {
		//httpCollect();
		super.onStart();
	}
	
	private void data() {
		String url = "mobile/collect/queryCollectForArticleMemoList.html";
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize", "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost(url, params, new CallBackPost() {
			
			@Override
			public void onMySuccess(ResponseInfo<String> responseInfo) {
				String s = responseInfo.result;
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(s);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(jsonObject.optBoolean("result")){
					JSONArray jArray = jsonObject.optJSONArray("dataList");
					Gson gson = new Gson();
					Type type = new TypeToken<List<CollectModel>>(){}.getType();
					list = gson.fromJson(jArray.toString(), type);
					adapter = new CollectListAdapter(CollectActivity.this,list);
					collectList.setAdapter(adapter);
					collectList.setDividerHeight(0);
					collectList.setOnItemClickListener(CollectActivity.this);
				}
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				
			}
		});
	}

	private void httpCollect() {
		String url = "mobile/collect/queryCollectForArticleMemoList.html";
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize", "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost(url, params, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
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
			if(jsonObject.optBoolean("result")){
				JSONArray jArray = jsonObject.optJSONArray("dataList");
				Gson gson = new Gson();
				Type type = new TypeToken<List<CollectModel>>(){}.getType();
				List<CollectModel> lists = gson.fromJson(jArray.toString(), type);
				if(pageIndex == 1){
					list = lists;
					Toasts.show(this, "刷新成功", 0);
				}else{
					list.addAll(lists);
					if(lists.size() == 0){
						Toasts.show(this, "已显示全部内容", 0);
					}else{
						Toasts.show(this, "加载成功", 0);
					}
				}
				adapter.notifyDataSetChanged();
			}
				
			if(pageIndex == 1){
				swipeLayout.setRefreshing(false);
			}else{
				swipeLayout.setLoading(false);
			}
		
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		if(pageIndex == 1){
			swipeLayout.setRefreshing(false);
		}else{
			swipeLayout.setLoading(false);
		}
	}

	@Override
	public void onRefresh() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				pageIndex = 1;
				httpCollect();
				// 更新数据
				// 更新完后调用该方法结束刷新
				
			}
		}, 1000);

	}

	@Override
	public void onLoad() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 更新数据
				// 更新完后调用该方法结束刷新
				pageIndex = pageIndex + 1;
				httpCollect();
				
			}
		}, 1000);
	}


/*	@Override
	public void onRefresh() {
		isFirst =true;
		pageIndex = 1;
		httpCollect();
	}

	@Override
	public void onLoadingMore() {
		isFirst =false;
		pageIndex = pageIndex + 1;
		httpCollect();
	}*/
}
