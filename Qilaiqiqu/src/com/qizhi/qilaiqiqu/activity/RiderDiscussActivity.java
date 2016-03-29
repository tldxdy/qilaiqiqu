package com.qizhi.qilaiqiqu.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.ActivityDiscussListAdapter;
import com.qizhi.qilaiqiqu.model.ActivityCommentModel;
import com.qizhi.qilaiqiqu.utils.RefreshLayout;
import com.qizhi.qilaiqiqu.utils.RefreshLayout.OnLoadListener;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author hujianbo 陪骑评论
 * 
 */
public class RiderDiscussActivity extends HuanxinLogOutActivity implements
		OnClickListener, OnItemClickListener, OnTouchListener,
		OnRefreshListener, OnLoadListener {

	private LinearLayout backLayout;

	private ListView discussList;

	private EditText contentEdit;

	private Button discussBtn;

	private TextView titleTxt;

	private ActivityDiscussListAdapter adapter;

	private int activityId;
	private XUtilsUtil xUtilsUtil;
	private SharedPreferences preferences;

	private List<ActivityCommentModel> list;

	private boolean flag = true;

	private int superId = -1;
	private InputMethodManager imm;

	private int pageIndex = 1;

	private RefreshLayout swipeLayout;
	private View header;

	private String riderId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_discuss);

		initView();
		initEvent();
	}

	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	private void initView() {
		riderId = getIntent().getStringExtra("riderId");
		
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		activityId = getIntent().getIntExtra("activityId", -1);
		xUtilsUtil = new XUtilsUtil();
		list = new ArrayList<ActivityCommentModel>();
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		backLayout = (LinearLayout) findViewById(R.id.layout_discussactivity_back);

		discussBtn = (Button) findViewById(R.id.btn_discussactivity_discuss);

		contentEdit = (EditText) findViewById(R.id.edt__discussactivity_content);

		discussList = (ListView) findViewById(R.id.list_discussactivity_discuss);
		titleTxt = (TextView) findViewById(R.id.txt_discussactivity_title);
		titleTxt.setText("陪骑评论");

		superId = getIntent().getIntExtra("commentId", -1);
		if (superId != -1) {
			// contentEdit.setHint("回复" + list.get(position).getUserName() +
			// ":");
			contentEdit.setFocusable(true);
			imm.showSoftInputFromInputMethod(contentEdit.getWindowToken(), 0);
			imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		}
		header = View.inflate(this, R.layout.header, null);
		swipeLayout = (RefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		discussList.addHeaderView(header);
	}

	private void initEvent() {

		backLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_discussactivity_discuss:
			commentRiding();
			break;
		case R.id.layout_discussactivity_back:
			finish();
			break;
		}
	}

	private void commentRiding() {
		RequestParams params = new RequestParams("UTF-8");
		params.addQueryStringParameter("riderId", riderId);
		params.addQueryStringParameter("userId",
				preferences.getInt("userId", -1) + "");
		if (superId != -1) {
			params.addBodyParameter("superId", superId + "");
			params.addBodyParameter("parentId", superId + "");
		} else {
			params.addBodyParameter("superId", "0");
			params.addBodyParameter("parentId", "0");
		}
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		params.addBodyParameter("commentMemo", contentEdit.getText().toString());
		
		xUtilsUtil.httpPost("mobile/riderComment/insertComment.html", params,
				new CallBackPost() {

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
							contentEdit.setText("");
							contentEdit.setHint("说几句吧");
							if (flag) {
								Toasts.show(RiderDiscussActivity.this, "评论成功",
										0);
							} else {
								Toasts.show(RiderDiscussActivity.this, "回复成功",
										0);
							}
							discussBtn.setText("评论");

							imm.hideSoftInputFromWindow(
									contentEdit.getWindowToken(), 0);
						} else {
							Toasts.show(RiderDiscussActivity.this,
									jsonObject.optString("message"), 0);
						}
						flag = true;
						pageIndex = 1;
						queryCommentPaginationList();
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						flag = true;
					}
				});
	}

	private void queryCommentPaginationList() {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("riderId", riderId);
		params.addBodyParameter("pageIndex", "2");
		xUtilsUtil.httpPost("common/queryRiderCommentPaginationList.html",
				params, new CallBackPost() {

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
							JSONArray dataList = jsonObject
									.optJSONArray("dataList");
							Gson gson = new Gson();
							Type type = new TypeToken<List<ActivityCommentModel>>() {
							}.getType();
							list = gson.fromJson(dataList.toString(), type);
							adapter = new ActivityDiscussListAdapter(
									RiderDiscussActivity.this, list);
							discussList.setAdapter(adapter);
							discussList
									.setOnTouchListener(RiderDiscussActivity.this);

							discussList
									.setOnItemClickListener(RiderDiscussActivity.this);
							swipeLayout
									.setOnRefreshListener(RiderDiscussActivity.this);
							swipeLayout
									.setOnLoadListener(RiderDiscussActivity.this);

						} else {
							Toasts.show(RiderDiscussActivity.this,
									jsonObject.optString("message"), 0);
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						Toasts.show(RiderDiscussActivity.this, msg, 0);
					}
				});
	}

	@Override
	protected void onResume() {
		discussBtn.setOnClickListener(this);
		queryCommentPaginationList();
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		flag = false;
		System.out.println(position);
		superId = list.get(position - 1).getCommentId();
		contentEdit.setHint("回复" + list.get(position - 1).getUserName() + ":");
		discussBtn.setText("回复");
		contentEdit.setFocusable(true);
		contentEdit.setFocusableInTouchMode(true);
		contentEdit.requestFocus();
		imm.showSoftInputFromInputMethod(contentEdit.getWindowToken(), 0);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * ACTION_DOWN: 表示用户开始触摸 ACTION_MOVE: 表示用户在移动(手指或者其他) ACTION_UP:表示用户抬起了手指
	 * ACTION_CANCEL:表示手势被取消了
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			System.out.println("ACTION_DOWN");
			imm.hideSoftInputFromWindow(contentEdit.getWindowToken(), 0);
			if ("".equals(contentEdit.getText().toString().trim())) {
				superId = -1;
				contentEdit.setHint("说几句吧");
				discussBtn.setText("评论");
				flag = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			System.out.println(superId);
			break;
		case MotionEvent.ACTION_UP:
			System.out.println("ACTION_UP");
			break;
		case MotionEvent.ACTION_CANCEL:
			System.out.println("ACTION_CANCEL");
			break;
		default:
			break;
		}

		return super.onTouchEvent(event);
	}

	private void dataJ() {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("activityId", activityId + "");
		xUtilsUtil.httpPost("common/queryActivityComment.html", params,
				new CallBackPost() {

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
							JSONArray dataList = jsonObject
									.optJSONArray("dataList");
							Gson gson = new Gson();
							Type type = new TypeToken<List<ActivityCommentModel>>() {
							}.getType();

							List<ActivityCommentModel> lists = gson.fromJson(
									dataList.toString(), type);
							if (pageIndex == 1) {
								list.clear();
								list.addAll(lists);
							} else {
								list.addAll(lists);
							}
							adapter.notifyDataSetChanged();

						} else {
							Toasts.show(RiderDiscussActivity.this,
									jsonObject.optString("message"), 0);

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

}
