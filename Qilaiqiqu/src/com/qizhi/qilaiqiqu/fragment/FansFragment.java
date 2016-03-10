package com.qizhi.qilaiqiqu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.PersonActivity;
import com.qizhi.qilaiqiqu.adapter.FansFragmentListAdapter;
import com.qizhi.qilaiqiqu.model.FansModel;
import com.qizhi.qilaiqiqu.model.FansModel.FansDataList;
import com.qizhi.qilaiqiqu.utils.RefreshLayout;
import com.qizhi.qilaiqiqu.utils.RefreshLayout.OnLoadListener;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

public class FansFragment extends Fragment implements OnItemClickListener,OnRefreshListener, CallBackPost,OnLoadListener {

	private View view;

	private ListView fansList;

	private List<FansDataList> dataList;

	private FansFragmentListAdapter adapter;
	
	private int pageIndex = 1;
	private boolean isFirst = true;
	private static SharedPreferences ferences;
	private RefreshLayout swipeLayout;
	private View header;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_fans,null);
		fansList = (ListView) view.findViewById(R.id.list_fansFragment);
		ferences = getActivity().getSharedPreferences("userLogin", 0);
		dataList = new ArrayList<FansDataList>();
		header = View.inflate(getActivity(),R.layout.header, null);
		swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		fansList.addHeaderView(header);
		data();
		return view;
	}
	
	
	private void data(){
		RequestParams params = new RequestParams();
		params.addBodyParameter("quoteUserId", ferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize", "10");
		params.addBodyParameter("uniqueKey", ferences.getString("uniqueKey", null));
		new XUtilsUtil().httpPost("mobile/attention/queryCoverAttentionPaginationList.html", params,new CallBackPost() {
			
			@Override
			public void onMySuccess(ResponseInfo<String> responseInfo) {
				try {
					JSONObject object = new JSONObject(responseInfo.result);
					if(object.optBoolean("result")){
						Gson gson = new Gson();
						FansModel fansModel = gson.fromJson(object.toString(),
								new TypeToken<FansModel>() {
								}.getType());
						ArrayList<FansDataList> fansDataList = fansModel.getDataList();
						System.out.println(fansDataList.size());
						dataList.clear();
						dataList.addAll(fansDataList);
						adapter = new FansFragmentListAdapter(getActivity(), dataList);
					}
					fansList.setAdapter(adapter);
					fansList.setOnItemClickListener(FansFragment.this);
					swipeLayout.setOnRefreshListener(FansFragment.this);
					swipeLayout.setOnLoadListener(FansFragment.this);
						
						
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
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		int attentionId = dataList.get(position-1).getUserId();
		startActivity(new Intent(getActivity(), PersonActivity.class).putExtra("userId", attentionId));
	}
	
	private void dataJ() {

		String url = "mobile/attention/queryCoverAttentionPaginationList.html";
		RequestParams params = new RequestParams();
		int quoteUserId = ferences.getInt("userId", -1);
		String pageSize = "10";
		String uniqueKey = ferences.getString("uniqueKey", null);

		params.addBodyParameter("quoteUserId", quoteUserId + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize", pageSize);
		params.addBodyParameter("uniqueKey", uniqueKey);
		new XUtilsUtil().httpPost(url, params,FansFragment.this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		try {
			JSONObject object = new JSONObject(responseInfo.result);
			Gson gson = new Gson();
			FansModel fansModel = gson.fromJson(object.toString(),
					new TypeToken<FansModel>() {
					}.getType());
			ArrayList<FansDataList> fansDataList = fansModel.getDataList();
			if(isFirst){
				dataList.clear();
				dataList.addAll(fansDataList);
			}else{
				dataList.addAll(fansDataList);
			}
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		
	}
	@Override
	public void onRefresh() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				swipeLayout.setRefreshing(false);
				pageIndex = 1;
				isFirst =true;
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
				isFirst =false;
				dataJ();
			}
		}, 1500);
	}
}
