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
import com.qizhi.qilaiqiqu.adapter.ManageAdapter;
import com.qizhi.qilaiqiqu.model.StartAndParticipantActivityModel;
import com.qizhi.qilaiqiqu.ui.PullFreshListView;
import com.qizhi.qilaiqiqu.ui.PullFreshListView.OnRefreshListener;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ManageFragment extends Fragment  implements OnItemClickListener,OnRefreshListener,CallBackPost{

	private PullFreshListView manageList;
	private View view;
	private List<StartAndParticipantActivityModel> dataList;
	private ManageAdapter adapter;
	private Context context;
	private SharedPreferences preferences;
	private XUtilsUtil xUtilsUtil;
	private int pageIndex = 1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_manage,null);
		manageList = (PullFreshListView) view.findViewById(R.id.list_fragment_manage);
		context = getActivity();
		preferences = context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		xUtilsUtil = new XUtilsUtil();
		dataList = new ArrayList<StartAndParticipantActivityModel>();
		
		return view;
	}
	@Override
	public void onResume() {
		data();
		super.onResume();
	}
	private void data() {
		pageIndex = 1;
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize",  "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/activity/queryUserRelevantActivityStateUnderwayList.html", params, this);
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
			Gson gson = new Gson();
			dataList = gson.fromJson(jsonObject.optJSONArray("dataList").toString(), new TypeToken<ArrayList<StartAndParticipantActivityModel>>(){}.getType());
			
			adapter = new ManageAdapter(context, dataList , preferences.getInt("userId", -1) );
			manageList.setAdapter(adapter);
			manageList.setOnItemClickListener(this);
			manageList.setOnRefreshListener(this);
		}
	}
	@Override
	public void onMyFailure(HttpException error, String msg) {
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		new SystemUtil().makeToast(getActivity(), position +"");
	}


	@Override
	public void onRefresh() {
		pageIndex = 1;
		dataJ();
		
	}


	@Override
	public void onLoadingMore() {
		pageIndex = pageIndex + 1;
		dataJ();
	}
	
	
	private void dataJ() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize",  "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/activity/queryUserRelevantActivityStateUnderwayList.html", params, new CallBackPost() {
			
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
						dataList = lists;
						new SystemUtil().makeToast(getActivity(), "刷新成功");
					}else if(1 < pageIndex && pageIndex <= pageCount){
						dataList.addAll(lists);
						new SystemUtil().makeToast(getActivity(), "加载成功");
					}
					
				}else{
					new SystemUtil().makeToast(getActivity(),	jsonObject.optString("message") );
				}
				// 更新UI
				adapter.notifyDataSetChanged();
				manageList.finishRefreshing();
				
				
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				
			}
		});
	}

	
}
