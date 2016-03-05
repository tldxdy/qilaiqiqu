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
import com.qizhi.qilaiqiqu.activity.ActivityDetailsActivity;
import com.qizhi.qilaiqiqu.activity.ActivityDiscussActivity;
import com.qizhi.qilaiqiqu.activity.CommentMessageActivity;
import com.qizhi.qilaiqiqu.adapter.MyMessageAdapter;
import com.qizhi.qilaiqiqu.model.CommentModel;
import com.qizhi.qilaiqiqu.model.SystemMessageModel;
import com.qizhi.qilaiqiqu.ui.FooterListView.OnfreshListener;
import com.qizhi.qilaiqiqu.ui.FooterListView;
import com.qizhi.qilaiqiqu.ui.Refresh;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
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

public class MessageFragment extends Fragment implements OnItemClickListener,CallBackPost,OnRefreshListener, OnfreshListener{
	private View view;
	private Context context;
	
	private FooterListView myMessageList;		//系统消息的集合
	private MyMessageAdapter adapter;
	private XUtilsUtil xUtilsUtil;
	private Integer pageIndex = 1;
	private SharedPreferences preferences;
	private List<SystemMessageModel> list;
	private Refresh swipeLayout;
	private View header;
	
	
	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_message,null);
		myMessageList = (FooterListView) view.findViewById(R.id.list_fragment_message);
		context = getActivity();
		preferences = context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		list = new ArrayList<SystemMessageModel>();
		xUtilsUtil = new XUtilsUtil();
		header = View.inflate(getActivity(),R.layout.header, null);
		swipeLayout = (Refresh) view.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		myMessageList.addHeaderView(header);
		swipeLayout.setOnRefreshListener(this);
		//swipeLayout.setOnLoadListener(this);
		
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
		params.addBodyParameter("pageSize", "20");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/systemMessage/querySystemMessageList.html", params, this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if(position > list.size()){
			return;
		}
		
		int systemMessageId = list.get(position - 1).getSystemMessageId();
		
		RequestParams params = new RequestParams();
		params.addBodyParameter("systemMessageId", systemMessageId + "");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/systemMessage/querySystemMessageDetails.html", params, new CallBackPost() {
			
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
					try {
						SystemMessageModel smm = new Gson().fromJson(jsonObject.getJSONObject("data").optString("tempSystemMessage").toString(), SystemMessageModel.class);
						JSONObject jo = new JSONObject(smm.getContentJson());
						if("QYJPL".equals(smm.getMessageType())){
							CommentModel commentModel = new Gson().fromJson(jo.toString(), CommentModel.class);
							//骑游记评论
							Intent intent = new Intent(getActivity(), CommentMessageActivity.class);
							intent.putExtra("commentModel", commentModel);
							intent.putExtra("isComment", 1);
							startActivity(intent);
						}else if("QYJHF".equals(smm.getMessageType())){
							//骑游记回复
							CommentModel commentModel = new Gson().fromJson(jo.toString(), CommentModel.class);
							Intent intent = new Intent(getActivity(), CommentMessageActivity.class);
							intent.putExtra("commentModel", commentModel);
							intent.putExtra("isComment", 2);
							startActivity(intent);
						}else if("HDBM".equals(smm.getMessageType())){
							int activityId = jo.optInt("activityId");
							Intent intent = new Intent(getActivity(), ActivityDetailsActivity.class);
							//intent.putExtra("superId",jo.optInt("commentId"));
							intent.putExtra("activityId", activityId);
							startActivity(intent);
						}else if("HDYQDS".equals(smm.getMessageType())){
							int activityId = jo.optInt("activityId");
							Intent intent = new Intent(getActivity(), ActivityDetailsActivity.class);
							//intent.putExtra("superId",jo.optInt("commentId"));
							intent.putExtra("activityId", activityId);
							startActivity(intent);
						}else if("HDHF".equals(smm.getMessageType())){
							int activityId = jo.optInt("activityId");
							Intent intent = new Intent(getActivity(), ActivityDiscussActivity.class);
							//intent.putExtra("superId",jo.optInt("commentId"));
							intent.putExtra("activityId", activityId);
							startActivity(intent);
						}else if("HDDSJG".equals(smm.getMessageType())){
							//活动报名{"activityId":169}
							int activityId = jo.optInt("activityId");
							
							Intent intent;
							if("".equals(jo.optString("memo"))){
								intent = new Intent(getActivity(), ActivityDetailsActivity.class);
								intent.putExtra("integral", jo.optInt("integral"));
								intent.putExtra("sumIntegral", jo.optInt("sumIntegral"));
								intent.putExtra("userName", jo.optString("userName"));
								intent.putExtra("activityId", activityId);
								startActivity(intent);
							}else{
								intent = new Intent(getActivity(), ActivityDiscussActivity.class);
								//intent.putExtra("superId",jo.optInt("commentId"));
								intent.putExtra("activityId", activityId);
								startActivity(intent);
							}
							
							
							
						}else if("HDYQDS".equals(smm.getMessageType())){
							//活动详情打赏
							int activityId = jo.optInt("activityId");
							Intent intent = new Intent(getActivity(), ActivityDetailsActivity.class);
							intent.putExtra("activityId", activityId);
							getActivity().startActivity(intent);
							
						}else if("PQS".equals(smm.getMessageType())){
							//陪骑士
							
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				
			}
		});
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
			Type type = new TypeToken<List<SystemMessageModel>>(){}.getType();
			list = gson.fromJson(jsonObject.optJSONArray("dataList").toString(), type);
			adapter = new MyMessageAdapter(context, list);
			myMessageList.setAdapter(adapter);
			myMessageList.setOnItemClickListener(this);
			myMessageList.setOnfreshListener(this);
			//myMessageList.setOnRefreshListener(this);
			
			//myMessageList.finishRefreshing();
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		
	}
	
/*	@Override
	public void onRefresh() {
		pageIndex = 1;
		System.out.println("aaaaaa");
		data();
		
	}


	@Override
	public void onLoadingMore() {
		System.out.println(pageIndex);
		pageIndex = pageIndex + 1;
		dataJ();
	}*/


	private void dataJ() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize", "20");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/systemMessage/querySystemMessageList.html", params, new CallBackPost() {
			
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
					Type type = new TypeToken<List<SystemMessageModel>>(){}.getType();
					List<SystemMessageModel> dataList = gson.fromJson(jsonObject.optJSONArray("dataList").toString(), type);
					if(pageIndex == 1){
						list = dataList;
						new SystemUtil().makeToast(getActivity(), "刷新成功");
					}else if(1 < pageIndex && pageIndex <= pageCount){
						list.addAll(dataList);
						new SystemUtil().makeToast(getActivity(), "加载成功");
					}else if(pageIndex > pageCount){
						list.addAll(dataList);
						//new SystemUtil().makeToast(getActivity(), "已显示全部内容");
					}
					// 更新UI
					adapter.notifyDataSetChanged();
				//	myMessageList.finishRefreshing();
					
				}
				swipeLayout.setRefreshing(false);
				myMessageList.completeRefresh();
				
				/*if(pageIndex == 1){
					swipeLayout.setRefreshing(false);
				}else{
					swipeLayout.setLoading(false);
				}*/
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				swipeLayout.setRefreshing(false);
				/*if(pageIndex == 1){
					swipeLayout.setRefreshing(false);
				}else{
					swipeLayout.setLoading(false);
				}*/
			}
		});
	}


	@Override
	public void onRefresh() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				pageIndex = 1;
				dataJ();
				// 更新数据
				// 更新完后调用该方法结束刷新
				
			}
		}, 2000);

	}


	@Override
	public void onLoadingMore() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 更新数据
				// 更新完后调用该方法结束刷新
				pageIndex = pageIndex + 1;
				dataJ();
			}
		}, 2000);
	}

/*	@Override
	public void onLoad() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 更新数据
				// 更新完后调用该方法结束刷新
				pageIndex = pageIndex + 1;
				dataJ();
			}
		}, 1000);
	}*/
}
	
