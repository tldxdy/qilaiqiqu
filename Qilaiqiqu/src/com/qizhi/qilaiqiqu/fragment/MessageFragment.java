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
import com.qizhi.qilaiqiqu.activity.RiderDetailsActivity;
import com.qizhi.qilaiqiqu.adapter.MyMessageAdapter;
import com.qizhi.qilaiqiqu.model.CommentModel;
import com.qizhi.qilaiqiqu.model.SystemMessageModel;
import com.qizhi.qilaiqiqu.utils.RefreshLayout;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.RefreshLayout.OnLoadListener;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

public class MessageFragment extends Fragment implements OnItemClickListener,CallBackPost,OnRefreshListener, OnLoadListener,OnItemLongClickListener{
	private View view;
	private Context context;
	
	private ListView myMessageList;		//系统消息的集合
	private MyMessageAdapter adapter;
	private XUtilsUtil xUtilsUtil;
	private Integer pageIndex = 1;
	private SharedPreferences preferences;
	private List<SystemMessageModel> list;
	private RefreshLayout swipeLayout;
	private View header;
	
	
	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_message,null);
		myMessageList = (ListView) view.findViewById(R.id.list_fragment_message);
		context = getActivity();
		preferences = context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		list = new ArrayList<SystemMessageModel>();
		xUtilsUtil = new XUtilsUtil();
		header = View.inflate(getActivity(),R.layout.header, null);
		swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		myMessageList.addHeaderView(header);
		adapter = new MyMessageAdapter(context, list);
		myMessageList.setAdapter(adapter);
		myMessageList.setOnItemClickListener(this);
		myMessageList.setOnItemLongClickListener(this);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);
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
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		String s = responseInfo.result;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(s);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObject.optBoolean("result")) {
			list.clear();
			Gson gson = new Gson();
			Type type = new TypeToken<List<SystemMessageModel>>(){}.getType();
			List<SystemMessageModel> lists = gson.fromJson(jsonObject.optJSONArray("dataList").toString(), type);
			list.addAll(lists);
			adapter.notifyDataSetChanged();
		}
		}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		
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
				System.out.println("==========================");
				System.out.println(s);
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
							int applyId = jo.optInt("applyId");
							Intent intent = new Intent(getActivity(), ActivityDetailsActivity.class);
							intent.putExtra("activityId", activityId);
							intent.putExtra("applyId", applyId);
							getActivity().startActivity(intent);
							
						}else if("PQSYQDS".equals(smm.getMessageType())){
							//陪骑士邀请打赏
							int riderId = jo.optInt("riderId");
							Intent intent = new Intent(getActivity(), RiderDetailsActivity.class);
							intent.putExtra("pushType", "PQSYQDS");
							intent.putExtra("riderId", riderId);
							getActivity().startActivity(intent);
							
						}else if("PQSDSJG".equals(smm.getMessageType())){
							//陪骑士打赏查看
							int riderId = jo.optInt("riderId");
							int integral = jo.optInt("integral");
							int sumIntegral = jo.optInt("sumIntegral");
							String userName = jo.optString("userName");
							Intent intent = new Intent(getActivity(), RiderDetailsActivity.class);
							intent.putExtra("pushType", "PQSYQDS");
							intent.putExtra("riderId", riderId);
							if(integral != 0){
								intent.putExtra("integral", integral);
								intent.putExtra("sumIntegral", sumIntegral);
								intent.putExtra("userName", userName);
							}
							
							getActivity().startActivity(intent);
							
							
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
						list.clear();
						list.addAll(dataList);
						Toasts.show(getActivity(), "刷新成功", 0);
					}else if(1 < pageIndex && pageIndex <= pageCount){
						list.addAll(dataList);
						Toasts.show(getActivity(), "加载成功", 0);
					}else if(pageIndex > pageCount){
						list.addAll(dataList);
					}
					// 更新UI
					adapter.notifyDataSetChanged();
					
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


	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
			showPopupWindow(view,list.get(position - 1).getSystemMessageId(), position - 1);
		return true;
	}
	
	  private void showPopupWindow(View view,final Integer systemMessageId, final int position) {
	  // 一个自定义的布局，作为显示的内容 
		  View mview = LayoutInflater.from(getActivity()).
				  inflate(R.layout.popup_messagefragment, null);
	  
		  Button qubj = (Button) mview.findViewById(R.id.btn_personaldataactivity_phone); 
		  Button scxt =(Button) mview .findViewById(R.id.btn_personaldataactivity_photograph);
		  LinearLayout quxiao = (LinearLayout) mview.findViewById(R.id.quxiao);
	  
	  final PopupWindow popupWindow = new PopupWindow(mview,
	  LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
	  
	  popupWindow.setTouchable(true);
	  
	  popupWindow.setAnimationStyle(R.style.Popupfade_in_out);
	  
	  popupWindow.setTouchInterceptor(new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return false;
		}
	});
	  qubj.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			popupWindow.dismiss();
			biaoji(systemMessageId, position);
			
		}
	});
	  scxt.setOnClickListener(new OnClickListener() {
			
		@Override
		public void onClick(View v) {
			popupWindow.dismiss();
			delete(systemMessageId,position);
		}
	});
	  quxiao.setOnClickListener(new OnClickListener() {
			
		@Override
		public void onClick(View v) {
			popupWindow.dismiss();
		}
	});
	  
	  
	  
	  // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
	  popupWindow.setBackgroundDrawable(getResources().getDrawable(
	  R.drawable.corners_layout)); // 设置好参数之后再show
	  popupWindow.showAtLocation(view, Gravity.CENTER, 0, Gravity.CENTER);
	  
	  }
	
	private void delete(Integer systemMessageId, final int position) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("systemMessageId", systemMessageId + "");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/systemMessage/deleteSystemMessage.html", params, new CallBackPost() {
			
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
					list.remove(position);
					adapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				
			}
		});
	}
	
	private void biaoji(Integer systemMessageId, final int position){
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
					SystemMessageModel messageModel = list.get(position);
					messageModel.setState("YESVIEW");
					list.set(position, messageModel);
					adapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				
			}
		});
	}
}
	
