package com.qizhi.qilaiqiqu.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.RidingDetailsActivity;
import com.qizhi.qilaiqiqu.adapter.RidingCollectAdapter;
import com.qizhi.qilaiqiqu.model.CollectModel;
import com.qizhi.qilaiqiqu.ui.FooterListView;
import com.qizhi.qilaiqiqu.ui.Refresh;
import com.qizhi.qilaiqiqu.ui.FooterListView.OnfreshListener;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class RidingCollectFragment extends Fragment implements OnItemClickListener,CallBackPost,OnRefreshListener,OnfreshListener{

	private FooterListView manageList;
	private View view;
	private List<CollectModel> Articlelist;
	private RidingCollectAdapter adapter;
	private Context context;
	private SharedPreferences preferences;
	private XUtilsUtil xUtilsUtil;
	private int pageIndex = 1;
	private Refresh swipeLayout;
	private View header;
	
	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_riding,null);
		manageList = (FooterListView) view.findViewById(R.id.list_mainActivity_slideShow);
		context = getActivity();
		preferences = context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		xUtilsUtil = new XUtilsUtil();
		Articlelist = new ArrayList<CollectModel>();
		header = View.inflate(getActivity(),R.layout.header, null);
		swipeLayout = (Refresh) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		manageList.addHeaderView(header);
		swipeLayout.setOnRefreshListener(this);
		manageList.setOnfreshListener(this);
		data();
		return view;
}

	private void data() {
		RequestParams params = new RequestParams("UTF-8");
		pageIndex = 1;
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize", "10");
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/collect/queryCollectForArticleMemoList.html", params,this);
	}
	
	@Override
	public void onRefresh() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				pageIndex = 1;
					dataJ();
			}
		}, 1500);

	}

	@Override
	public void onLoadingMore() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				pageIndex = pageIndex + 1;
				dataJ();
			}
		}, 1500);
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
			pageIndex = jsonObject.optInt("pageIndex");

			JSONArray jsonArray = jsonObject
					.optJSONArray("dataList");
			// 数据获取
			Gson gson = new Gson();
			Type type = new TypeToken<List<CollectModel>>() {
			}.getType();
			Articlelist = gson.fromJson(jsonArray.toString(),
					type);

			adapter = new RidingCollectAdapter(getActivity(), Articlelist);
			manageList.setAdapter(adapter);
			manageList
					.setOnItemClickListener(this);
			swipeLayout.setOnRefreshListener(this);
			manageList.setOnfreshListener(this);
			// 更新UI
			adapter.notifyDataSetChanged();
			swipeLayout.setRefreshing(false);
			manageList.completeRefresh();
		}

	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		swipeLayout.setRefreshing(false);
		manageList.completeRefresh();
	}
	
	private void dataJ() {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize", "10");
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/collect/queryCollectForArticleMemoList.html", params,new CallBackPost() {
			
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
					pageIndex = jsonObject.optInt("pageIndex");
					int pageCount = jsonObject.optInt("pageCount");

					JSONArray jsonArray = jsonObject
							.optJSONArray("dataList");
					// 数据获取
					Gson gson = new Gson();
					Type type = new TypeToken<List<CollectModel>>() {
					}.getType();
					List<CollectModel> lists = gson.fromJson(
							jsonArray.toString(), type);
					pageIndex = jsonObject.optInt("pageIndex");
					if(pageIndex == 1){
						
						Articlelist = lists;
						//adapter = new RidingListAdapter(this, list , new );
						//manageList.setAdapter(adapter);
						//manageList.setOnItemClickListener(RidingFragment.this);
						Toasts.show(context, "刷新成功", 0);
						//new SystemUtil().makeToast(getActivity(), "刷新成功");
					}else if(1 < pageIndex && pageIndex <= pageCount){
						Articlelist.addAll(lists);
						Toasts.show(context, "加载成功", 0);
						//new SystemUtil().makeToast(getActivity(), "加载成功");
					}else{
						pageIndex = jsonObject.optInt("pageIndex");
						Toasts.show(context, "以显示全部内容", 0);
					}

					// 更新UI

					//manageList.finishRefreshing();
				}
				adapter.notifyDataSetChanged();
				swipeLayout.setRefreshing(false);
				manageList.completeRefresh();
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
					swipeLayout.setRefreshing(false);
					manageList.completeRefresh();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent intent = new Intent(context, RidingDetailsActivity.class);
		intent.putExtra("isMe", false);
		intent.putExtra("articleId",Articlelist.get(position-1).getQuoteId() );
		startActivity(intent);
	}
}
