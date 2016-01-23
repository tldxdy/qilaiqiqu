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
import com.qizhi.qilaiqiqu.activity.CommentMessageActivity;
import com.qizhi.qilaiqiqu.activity.DiscussActivity;
import com.qizhi.qilaiqiqu.activity.MyMessageActivity;
import com.qizhi.qilaiqiqu.adapter.MyMessageAdapter;
import com.qizhi.qilaiqiqu.model.CommentModel;
import com.qizhi.qilaiqiqu.model.SystemMessageModel;
import com.qizhi.qilaiqiqu.ui.PullFreshListView;
import com.qizhi.qilaiqiqu.ui.PullFreshListView.OnRefreshListener;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MessageFragment extends Fragment implements OnItemClickListener,OnRefreshListener,CallBackPost{
	private View view;
	private Context context;
	
	private PullFreshListView myMessageList;		//系统消息的集合
	private MyMessageAdapter adapter;
	private XUtilsUtil xUtilsUtil;
	private Integer pageIndex = 1;
	private SharedPreferences preferences;
	private List<SystemMessageModel> list;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_message,null);
		myMessageList = (PullFreshListView) view.findViewById(R.id.list_fragment_message);
		context = getActivity();
		preferences = context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		list = new ArrayList<SystemMessageModel>();
		xUtilsUtil = new XUtilsUtil();
		
		
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
		params.addBodyParameter("pageSize", "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/systemMessage/querySystemMessageList.html", params, this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
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
						SystemMessageModel smm = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), SystemMessageModel.class);
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
							//活动报名
							
						}else if("HDYQDS".equals(smm.getMessageType())){
							//活动详情打赏
						}else if("HDBM".equals(smm.getMessageType())){
							
						}else if("HDBM".equals(smm.getMessageType())){
							
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
			myMessageList.setOnRefreshListener(this);
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		
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
		params.addBodyParameter("pageSize", "10");
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
						new SystemUtil().makeToast(getActivity(), "以显示全部内容");
					}
					// 更新UI
					adapter.notifyDataSetChanged();
					myMessageList.finishRefreshing();
					
				}
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				
			}
		});
	}

}
	
