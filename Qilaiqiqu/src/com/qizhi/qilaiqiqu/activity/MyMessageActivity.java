package com.qizhi.qilaiqiqu.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.MyMessageAdapter;
import com.qizhi.qilaiqiqu.model.SystemMessageModel;
import com.qizhi.qilaiqiqu.utils.SideslipDeleteListView;
import com.qizhi.qilaiqiqu.utils.SlideCutListView;
import com.qizhi.qilaiqiqu.utils.SlideCutListView.RemoveDirection;
import com.qizhi.qilaiqiqu.utils.SlideCutListView.RemoveListener;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author hujianbo
 *
 */
public class MyMessageActivity extends Activity implements OnClickListener,OnItemClickListener, CallBackPost{

	private SideslipDeleteListView myMessageList;		//系统消息的集合
	private LinearLayout backLayout;		//返回图片
	private MyMessageAdapter adapter;
	private XUtilsUtil xUtilsUtil;
	private Integer pageIndex = 1;
	private SharedPreferences preferences;
	List<SystemMessageModel> list;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_message);
		initView();
	}

	private void initView() {
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		xUtilsUtil = new XUtilsUtil();
		list = new ArrayList<SystemMessageModel>();
		
		backLayout = (LinearLayout) findViewById(R.id.layout_mymessageactivity_back);
		backLayout.setOnClickListener(this);
		myMessageList = (SideslipDeleteListView) findViewById(R.id.list_mymessageactivity_my_message);
		/*adapter = new MyMessageAdapter(this);
		MyMessageList.setAdapter(adapter);
		MyMessageList.setOnItemClickListener(this);*/
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_mymessageactivity_back:
			MyMessageActivity.this.finish();
			//Toast.makeText(this, "点击返回", 0).show();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(myMessageList.canClick()){
			int systemMessageId = list.get(position).getSystemMessageId();
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
								//骑游记评论
								Intent intent = new Intent(MyMessageActivity.this, DiscussActivity.class);
								intent.putExtra("articleId", jo.optInt("articleId"));//"articleId\":97,\"commentId\":2
								intent.putExtra("commentId", jo.optInt("commentId"));
								startActivity(intent);
							}else if("QYJHF".equals(smm.getMessageType())){
								//骑游记回复
								Intent intent = new Intent(MyMessageActivity.this, DiscussActivity.class);
								intent.putExtra("articleId", jo.optInt("articleId"));//"articleId\":97,\"commentId\":2
								intent.putExtra("commentId", jo.optInt("commentId"));
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
	}
	
	@Override
	protected void onResume() {
		data();
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void data() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize", "10");
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
			Gson gson = new Gson();
			Type type = new TypeToken<List<SystemMessageModel>>(){}.getType();
			list = gson.fromJson(jsonObject.optJSONArray("dataList").toString(), type);
			adapter = new MyMessageAdapter(this, list, myMessageList);
			myMessageList.setAdapter(adapter);
			myMessageList.setOnItemClickListener(this);
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		
	}

}
