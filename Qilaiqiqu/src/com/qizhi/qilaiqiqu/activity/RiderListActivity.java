package com.qizhi.qilaiqiqu.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.RiderListAdapter;
import com.qizhi.qilaiqiqu.adapter.RiderListAdapter.OnRecyclerViewItemClickListener;
import com.qizhi.qilaiqiqu.model.RiderRecommendModel;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.qizhi.qilaiqiqu.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

public class RiderListActivity extends HuanxinLogOutActivity implements
		OnClickListener, OnRecyclerViewItemClickListener, OnRefreshListener {

	private LinearLayout backLayout;
	private XRecyclerView recyclerView;
	private RiderListAdapter adapter;
	private List<RiderRecommendModel> list;
	private int pageIndex = 1;
	private SharedPreferences preferences;
	private XUtilsUtil xUtilsUtil;
	private SwipeRefreshLayout refreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_riderlist);
		initView();
		initEvent();
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		backLayout = (LinearLayout) findViewById(R.id.layout_appendActivity_back);
		recyclerView = (XRecyclerView) findViewById(R.id.recycle_view);
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager( 2,
                StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setRefreshProgressStyle(22);
        recyclerView.setLaodingMoreProgressStyle(7);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        recyclerView.refreshComplete();
                    }

                }, 1000);
            }

            @Override
            public void onLoadMore() {
            	
            	refreshLayout.postDelayed(new Runnable() {

        			@Override
        			public void run() {
        				recyclerView.loadMoreComplete();
                    	pageIndex = pageIndex + 1;
                    	initDatas();
        			}
        		}, 1500);
            }
        });

		refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		refreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		list = new ArrayList<RiderRecommendModel>();
		adapter = new RiderListAdapter(list, this);
		recyclerView.setAdapter(adapter);

		// 设置item之间的间隔
		SpacesItemDecoration decoration = new SpacesItemDecoration(10);
		recyclerView.addItemDecoration(decoration);

		xUtilsUtil = new XUtilsUtil();
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		refreshLayout.setOnRefreshListener(this);
		adapter.setOnItemClickListener(this);
		
		pageIndex = 1;
		initData();
	}

	private void initDatas() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("pageIndex", pageIndex + "");
		//params.addBodyParameter("pageIndex", "1");
		
		params.addBodyParameter("pageSize", 10 + "");
		xUtilsUtil.httpPost("common/queryAttendRiderPaginationList.html",
				params, new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						String s = responseInfo.result;
						JSONObject jsonObject = null;
						System.out.println(s);
						try {
							jsonObject = new JSONObject(s);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (jsonObject.optBoolean("result")) {
							Gson gson = new Gson();
							List<RiderRecommendModel> lists = gson
									.fromJson(
											jsonObject.optJSONArray("dataList")
													.toString(),
											new TypeToken<ArrayList<RiderRecommendModel>>() {
											}.getType());
							list.addAll(lists);
							
						} else {
							/*Toasts.show(RiderListActivity.this,
									jsonObject.optInt("message"), 0);*/
						}
						adapter.notifyDataSetChanged();
						recyclerView.refreshComplete();
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						//Toasts.show(RiderListActivity.this, msg, 0);
						recyclerView.refreshComplete();
					}
				});
	}

	private void initEvent() {
		backLayout.setOnClickListener(this);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_appendActivity_back:
			finish();
			break;

		default:
			break;
		}
	}

	public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

		private int space;

		public SpacesItemDecoration(int space) {
			this.space = space;
		}

		@Override
		public void getItemOffsets(Rect outRect, View view,
				RecyclerView parent, RecyclerView.State state) {
			outRect.left = space;
			outRect.right = space;
			outRect.bottom = space;
			outRect.top = space/2;
		}
	}

	@Override
	public void onItemClick(View view, Integer position) {

		// Toasts.show(this, position + "", 0);
		startActivity(new Intent(this, RiderDetailsActivity.class).putExtra(
				"riderId", list.get(position).getRiderId()));
		// System.out.println("-------------"+ list.get(position).getRiderId());
		// .putExtra("userId", preferences.getInt("userId", -1) + ""));
	}

	@Override
	public void onRefresh() {
		refreshLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				refreshLayout.setRefreshing(false);
				pageIndex = 1;
				initData();
			}
		}, 1500);

	}
	private void initData() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("pageIndex", pageIndex + "");
		//params.addBodyParameter("pageIndex", "1");
		params.addBodyParameter("pageSize", 10 + "");
		xUtilsUtil.httpPost("common/queryAttendRiderPaginationList.html",
				params, new CallBackPost() {

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
							List<RiderRecommendModel> lists = gson
									.fromJson(
											jsonObject.optJSONArray("dataList")
													.toString(),
											new TypeToken<ArrayList<RiderRecommendModel>>() {
											}.getType());
							list.clear();
							list.addAll(lists);
							adapter.notifyDataSetChanged();
							recyclerView.refreshComplete();
						} else {
							Toasts.show(RiderListActivity.this,
									jsonObject.optInt("message"), 0);
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						Toasts.show(RiderListActivity.this, msg, 0);
					}
				});
	}
}
