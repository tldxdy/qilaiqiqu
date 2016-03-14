package com.qizhi.qilaiqiqu.fragment;

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
import com.qizhi.qilaiqiqu.activity.ActivityDetailsActivity;
import com.qizhi.qilaiqiqu.adapter.HistoryAdapter;
import com.qizhi.qilaiqiqu.model.StartAndParticipantActivityModel;
import com.qizhi.qilaiqiqu.utils.RefreshLayout.OnLoadListener;
import com.qizhi.qilaiqiqu.utils.RefreshLayout;
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
import android.widget.ListView;

public class HistoryFragment extends Fragment implements OnItemClickListener,OnRefreshListener,OnLoadListener{
	private ListView historyList;
	private View view;
	private List<StartAndParticipantActivityModel> dataList;
	private HistoryAdapter adapter;
	private Context context;
	private SharedPreferences preferences;
	private XUtilsUtil xUtilsUtil;
	private int pageIndex = 1;
	private RefreshLayout swipeLayout;
	private View header;
	
	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_history,null);
		historyList = (ListView) view.findViewById(R.id.list_fragment_manage);
		context = getActivity();
		preferences = context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		dataList = new ArrayList<StartAndParticipantActivityModel>();
		xUtilsUtil = new XUtilsUtil();
		header = View.inflate(getActivity(),R.layout.header, null);
		swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		historyList.addHeaderView(header);
		
		return view;
	}


	@Override
	public void onResume() {
		pageIndex = 1;
		data();
		super.onResume();
	}
	private void data() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize",  "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/activity/queryUserRelevantActivityStateActendList.html", params, new CallBackPost() {
			
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
					Gson gson = new Gson();
					dataList = gson.fromJson(jsonObject.optJSONArray("dataList").toString(), new TypeToken<ArrayList<StartAndParticipantActivityModel>>(){}.getType());
				}
				adapter = new HistoryAdapter(context, dataList, preferences.getInt("userId", -1));
				historyList.setAdapter(adapter);
				historyList.setOnItemClickListener(HistoryFragment.this);
				swipeLayout.setOnRefreshListener(HistoryFragment.this);
				swipeLayout.setOnLoadListener(HistoryFragment.this);
				
				
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				
			}
		});
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if(position > dataList.size()){
			return;
		}
		Intent intent = new Intent(getActivity(), ActivityDetailsActivity.class);
		intent.putExtra("activityId", dataList.get(position - 1).getActivityId());
		getActivity().startActivity(intent);
	}



	private void dataJ() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize",  "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/activity/queryUserRelevantActivityStateActendList.html", params, new CallBackPost() {
			
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
					Gson gson = new Gson();
					ArrayList<StartAndParticipantActivityModel> lists = gson.fromJson(jsonObject.optJSONArray("dataList").toString(), new TypeToken<ArrayList<StartAndParticipantActivityModel>>(){}.getType());
					if(pageIndex == 1){
						dataList.clear();
						dataList.addAll(lists);
						Toasts.show(getActivity(), "刷新成功", 0);
					}else if(1 < pageIndex && pageIndex <= pageCount){
						dataList.addAll(lists);
						Toasts.show(getActivity(), "加载成功",0);
						
					}
					// 更新UI
					adapter.notifyDataSetChanged();
				}else{
					Toasts.show(getActivity(),	jsonObject.optString("message"), 0 );
				}
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
			}
		});
	}

	@Override
	public void onRefresh() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				swipeLayout.setRefreshing(false);
				pageIndex = 1;
					dataJ();
				
			}
		}, 1500);

	}

	@Override
	public void onLoad() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				swipeLayout.setLoading(false);
				pageIndex = pageIndex + 1;
				dataJ();
			}
		}, 1500);
	}

	
}
