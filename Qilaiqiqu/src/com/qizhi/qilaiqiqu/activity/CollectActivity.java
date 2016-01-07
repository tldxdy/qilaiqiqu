package com.qizhi.qilaiqiqu.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.CollectListAdapter;
import com.qizhi.qilaiqiqu.model.CollectModel;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */
public class CollectActivity extends Activity implements OnClickListener,OnItemClickListener, CallBackPost{

	private LinearLayout layoutBtn;// 返回按钮

	private ListView collectList;// 收藏
	
	private CollectListAdapter adapter;
	
	private List<CollectModel> list;
	private XUtilsUtil xUtilsUtil;
	private SharedPreferences preferences;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_collect);

		initView();
		initEvent();
	}

	private void initView() {
		list = new ArrayList<CollectModel>();
		xUtilsUtil = new XUtilsUtil();
		preferences = getSharedPreferences("userLogin",Context.MODE_PRIVATE);
		layoutBtn = (LinearLayout) findViewById(R.id.layout_collectActivity_back);
		collectList = (ListView) findViewById(R.id.list_collectActivity_collect);
	}

	private void initEvent() {
		layoutBtn.setOnClickListener(this);
		collectList.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_collectActivity_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		Intent intent = new Intent(this, RidingDetailsActivity.class);
		intent.putExtra("isMe", false);
		intent.putExtra("articleId",list.get(position).getQuoteId() );
		startActivity(intent);
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	
	
	
	@Override
	protected void onStart() {
		httpCollect();
		super.onStart();
	}

	private void httpCollect() {
		String url = "mobile/collect/queryCollectForArticleMemoList.html";
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageStart", "0");
		params.addBodyParameter("pageSize", "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost(url, params, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
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
			if(jsonObject.optBoolean("result")){
				JSONArray jArray = jsonObject.optJSONArray("dataList");
				Gson gson = new Gson();
				Type type = new TypeToken<List<CollectModel>>(){}.getType();
				list = gson.fromJson(jArray.toString(), type);
				adapter = new CollectListAdapter(this,list);
				collectList.setAdapter(adapter);
			}
		
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		
	}
}
