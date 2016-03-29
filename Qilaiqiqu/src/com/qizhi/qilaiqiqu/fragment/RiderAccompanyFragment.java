package com.qizhi.qilaiqiqu.fragment;

import java.lang.reflect.Type;
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
import com.qizhi.qilaiqiqu.activity.PersonActivity;
import com.qizhi.qilaiqiqu.adapter.RiderAccompanyAdapter;
import com.qizhi.qilaiqiqu.model.RiderApplyModle;
import com.qizhi.qilaiqiqu.utils.RefreshLayout;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.RefreshLayout.OnLoadListener;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


/**
 * 
 * @author Administrator
 *			陪骑
 *
 */
public class RiderAccompanyFragment extends Fragment implements OnItemClickListener, CallBackPost,OnRefreshListener,OnLoadListener{
	private View view;
	private ListView agreementList;
	private List<RiderApplyModle> list;
	private RiderAccompanyAdapter adapter;
	private int pageIndex = 1;
	private SharedPreferences preferences;
	private XUtilsUtil xUtilsUtil;
	private RefreshLayout swipeLayout;
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.rider_frament_agreementandserve, null);
		agreementList = (ListView) view.findViewById(R.id.rider_fragment_agreementandserve);
		list = new ArrayList<RiderApplyModle>();
		swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		adapter = new RiderAccompanyAdapter(getActivity(), list);
		agreementList.setAdapter(adapter);
		agreementList.setOnItemClickListener(this);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);
		
		xUtilsUtil = new XUtilsUtil();
		preferences = getActivity().getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		data();
		return view;
	}
	
	
	private void data() {
		pageIndex = 1;
		RequestParams params = new RequestParams();
		int riderId = preferences.getInt("riderId", -1);
		if(riderId == 0){
			return;
		}
		params.addBodyParameter("riderId",preferences.getInt("riderId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize",  "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/attendRider/queryAppointRiderApplyByRiderId.html", params, this);
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
			Type type = new TypeToken<List<RiderApplyModle>>(){}.getType();
			List<RiderApplyModle> lists = gson.fromJson
					(jsonObject.optJSONArray("dataList").toString(), type);
			list.addAll(lists);
			adapter.notifyDataSetChanged();
		}else{
			Toasts.show(getActivity(),jsonObject.optString("message"), 0);
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		Toasts.show(getActivity(),"网络异常", 0);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent intent = new Intent(getActivity(), PersonActivity.class);
		
		if(list.get(position).getIsAgree().equals("normal")){
			intent.putExtra("isRider", 1);
		}else if(list.get(position).getIsAgree().equals("true")){
			intent.putExtra("isRider", 2);
		}else if(list.get(position).getIsAgree().equals("false")){
			intent.putExtra("isRider", 3);
		}
		intent.putExtra("userId", list.get(position).getUserId());
		intent.putExtra("applyId", list.get(position).getApplyId());
		startActivity(intent);
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

	private void dataJ() {
		RequestParams params = new RequestParams();
		int riderId = preferences.getInt("riderId", -1);
		if(riderId == 0){
			return;
		}
		params.addBodyParameter("riderId",preferences.getInt("riderId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize",  "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/attendRider/queryAppointRiderApplyByRiderId.html", params,new CallBackPost() {
			
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
					Type type = new TypeToken<List<RiderApplyModle>>(){}.getType();
					List<RiderApplyModle> lists = gson.fromJson
							(jsonObject.optJSONArray("dataList").toString(), type);
					if(pageIndex == 1){
						list.clear();
					}
					list.addAll(lists);
					
					adapter.notifyDataSetChanged();
					
				}else{
					Toasts.show(getActivity(),jsonObject.optString("message"), 0);
				}
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				Toasts.show(getActivity(),"网络异常", 0);
			}
		});
	}
}
