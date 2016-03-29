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
import com.qizhi.qilaiqiqu.activity.RiderAuthenticationActivity;
import com.qizhi.qilaiqiqu.activity.RiderListActivity;
import com.qizhi.qilaiqiqu.activity.RiderRecommendActiviity;
import com.qizhi.qilaiqiqu.adapter.ManageAdapter;
import com.qizhi.qilaiqiqu.model.RiderRecommendModel;
import com.qizhi.qilaiqiqu.model.StartAndParticipantActivityModel;
import com.qizhi.qilaiqiqu.utils.RefreshLayout;
import com.qizhi.qilaiqiqu.utils.RefreshLayout.OnLoadListener;
import com.qizhi.qilaiqiqu.utils.CircleImageViewUtil;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author Administrator
 *			活动推荐主页
 */

public class ActivityFragment extends Fragment implements OnItemClickListener,CallBackPost,OnRefreshListener,OnLoadListener,OnClickListener{
	
	private ListView manageList;
	private View view;
	private List<StartAndParticipantActivityModel> dataList;
	private ManageAdapter adapter;
	private Context context;
	private SharedPreferences preferences;
	private XUtilsUtil xUtilsUtil;
	private int pageIndex = 1;
	private RefreshLayout swipeLayout;
	
	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_manage,null);
		manageList = (ListView) view.findViewById(R.id.list_fragment_manage);
		xUtilsUtil = new XUtilsUtil();
		context = getActivity();
		preferences = context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		initViewHeader();
		dataList = new ArrayList<StartAndParticipantActivityModel>();
		swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		adapter = new ManageAdapter(context, dataList , preferences.getInt("userId", -1) );
		manageList.setAdapter(adapter);
		manageList.setDividerHeight(0);
		manageList.setOnItemClickListener(this);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);
		data();
		return view;
	}
	private LinearLayout riderLayout;
	private LinearLayout numLayout;
	private RelativeLayout becomeLayout;
	private TextView activityTxt;
	private RelativeLayout peiqiLayout;
	private void initViewHeader() {
		riderList = new ArrayList<RiderRecommendModel>();
		View view = View.inflate(getActivity(),R.layout.rideractivity_activity_header, null);
		riderLayout = (LinearLayout) view.findViewById(R.id.layout_rideractivity_activity);
		numLayout = (LinearLayout) view.findViewById(R.id.layout_rideractivity_activity_num);
		becomeLayout = (RelativeLayout) view.findViewById(R.id.layout_rideractivity_activity_becomerider);
		peiqiLayout = (RelativeLayout) view.findViewById(R.id.layout_rideractivity_peiqi);
		
		//initNumRider();
		activityTxt = (TextView) view.findViewById(R.id.txt_rideractivity_activity);
		manageList.addHeaderView(view);
		becomeLayout.setOnClickListener(this);
		riderLayout.setOnClickListener(this);
		peiqiLayout.setOnClickListener(this);
	}
	
	private List<RiderRecommendModel> riderList;
	private void initNumRider() {
		xUtilsUtil.httpPost("common/queryRecommendAttendRiderList.html?authCode=admin"
				, new RequestParams(), new CallBackPost() {
			
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
					numLayout.removeAllViews();
					Gson gson = new Gson();
					riderList = gson.fromJson(jsonObject.optJSONArray("dataList").toString(),
							new TypeToken<ArrayList<RiderRecommendModel>>(){}.getType());
					for(int i= 0; i < riderList.size(); i++){
						if(i == 6){
			 				return;
						}
						String[] img = riderList.get(i).getRiderImage().split(",");
						System.out.println(img[0]);
						
						CircleImageViewUtil imageView = new CircleImageViewUtil(getActivity());
						numLayout.addView(imageView);
						Picasso.with(getActivity()).load(SystemUtil.IMGPHTH + img[0])
						.resize(dp2px(getActivity(), 35f),
								dp2px(getActivity(), 35f))
								.centerInside()
								.into(imageView);
						imageView.setPadding(5, 0, 5, 0);
					}
					
				}
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				Toasts.show(getActivity(), "msg", 0);
			}
		});
		
		
		
	}
	/**
	 * dp转px
	 * 
	 * @param context
	 * @param val
	 * @return
	 */
	public static int dp2px(Context context, float dpVal) {
		return (int) TypedValue.applyDimension(2,
				dpVal, context.getResources().getDisplayMetrics());
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	private void data() {
		initNumRider();
		pageIndex = 1;
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize",  "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("common/queryHotActivity.html", params, this);
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
			if(dataList.size() != 0){
				activityTxt.setVisibility(View.VISIBLE);
			}
			adapter = new ManageAdapter(context, dataList , preferences.getInt("userId", -1) );
			manageList.setAdapter(adapter);
		}
	}
	@Override
	public void onMyFailure(HttpException error, String msg) {
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		//Toasts.show(context, "点击" + position, 0);
		if(position == 0){
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
		xUtilsUtil.httpPost("common/queryHotActivity.html", params, new CallBackPost() {
			
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
						Toasts.show(getActivity(), "加载成功", 0);
					}
					
				}else{
					Toasts.show(getActivity(), jsonObject.optString("message"), 0);
				}
				// 更新UI
				adapter.notifyDataSetChanged();
				
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
				initNumRider();
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

	public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_rideractivity_activity_becomerider:
				System.out.println("跳转成为陪骑士页面");
				if (preferences.getInt("userId", -1) != -1) {
					startActivity(new Intent(getActivity(),
							RiderAuthenticationActivity.class));
				}
				break;
			case R.id.layout_rideractivity_activity:
				//推荐陪骑
				startActivity(new Intent(getActivity(),
						RiderRecommendActiviity.class));
				break;
			case R.id.layout_rideractivity_peiqi:
				startActivity(new Intent(getActivity(),
						RiderListActivity.class));
				break;
		default:
			break;
		}
	}

}
