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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.MyMessageAdapter;
import com.qizhi.qilaiqiqu.adapter.SearchResultAdapter;
import com.qizhi.qilaiqiqu.adapter.SlideShowListAdapter;
import com.qizhi.qilaiqiqu.model.SystemMessageModel;
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
public class MyMessageActivity extends Activity implements OnClickListener,OnItemClickListener, CallBackPost, RemoveListener{

	private SlideCutListView myMessageList;		//系统消息的集合
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
		myMessageList = (SlideCutListView) findViewById(R.id.list_mymessageactivity_my_message);
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
		try {
			JSONObject jo = new JSONObject(list.get(position).getContentJson());
			if("QYJPL".equals(list.get(position).getMessageType())){
				//骑游记评论
				
			}else if("HDBM".equals(list.get(position).getMessageType())){
				//活动报名
				
			}else if("HDYQDS".equals(list.get(position).getMessageType())){
				//活动详情打赏
			}else if("HDBM".equals(list.get(position).getMessageType())){
				
			}else if("HDBM".equals(list.get(position).getMessageType())){
				
			}
			
			
			
		} catch (JSONException e) {
			e.printStackTrace();
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
			adapter = new MyMessageAdapter(this, list);
			myMessageList.setAdapter(adapter);
			myMessageList.setOnItemClickListener(this);
			myMessageList.setRemoveListener(this);
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		
	}

	@Override
	public void removeItem(RemoveDirection direction, final int position) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("systemMessageId",list.get(position).getSystemMessageId() + "");
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
					new SystemUtil().makeToast(MyMessageActivity.this, "删除成功");
				}
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				
			}
		});
		
	}
	
}
