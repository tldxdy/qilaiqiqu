package com.qizhi.qilaiqiqu.activity;

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
import com.qizhi.qilaiqiqu.adapter.DiscussListAdapter;
import com.qizhi.qilaiqiqu.model.CommentPaginationListModel;
import com.qizhi.qilaiqiqu.model.RidingCommentModel;
import com.qizhi.qilaiqiqu.ui.PullFreshListView;
import com.qizhi.qilaiqiqu.ui.PullFreshListView.OnRefreshListener;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

/**
 * 
 * @author hujianbo
 * 
 */
public class DiscussActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnTouchListener, OnRefreshListener{

	private LinearLayout backLayout;

	private PullFreshListView discussList;

	private EditText contentEdit;

	private Button discussBtn;

	private DiscussListAdapter adapter;

	private int articleId;
	private XUtilsUtil xUtilsUtil;
	private SharedPreferences preferences;

	private CommentPaginationListModel commentPaginationListModel;
	private List<RidingCommentModel> list;

	private boolean flag = true;

	private int superId = -1;
	private InputMethodManager imm;
	
	
	
	private int pageIndex = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_discuss);

		initView();
		initEvent();
	}

	private void initView() {
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		articleId = getIntent().getIntExtra("articleId", -1);
		xUtilsUtil = new XUtilsUtil();
		commentPaginationListModel = new CommentPaginationListModel();
		list = new ArrayList<RidingCommentModel>();
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		backLayout = (LinearLayout) findViewById(R.id.layout_discussactivity_back);

		discussBtn = (Button) findViewById(R.id.btn_discussactivity_discuss);

		contentEdit = (EditText) findViewById(R.id.edt__discussactivity_content);

		discussList = (PullFreshListView) findViewById(R.id.list_discussactivity_discuss);
		superId =getIntent().getIntExtra("commentId", -1);
		if(superId != -1){
			//contentEdit.setHint("回复" + list.get(position).getUserName() + ":");
			contentEdit.setFocusable(true);
			imm.showSoftInputFromInputMethod(contentEdit.getWindowToken(), 0);
			imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		}
	}

	private void initEvent() {
		
		backLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_discussactivity_discuss:
			//if (!"".equals(contentEdit.getText().toString().trim())) {
				commentRiding();
		/*	}else{
				
			}*/
			break;
		case R.id.layout_discussactivity_back:
			finish();
			break;
		}
	}

	private void commentRiding() {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("articleId", articleId + "");
		if(superId != -1){
			params.addBodyParameter("superId", superId + "");
			params.addBodyParameter("parentId", superId + "");
		}
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		params.addBodyParameter("commentMemo", contentEdit.getText().toString());
		xUtilsUtil.httpPost("mobile/comment/insertComment.html", params,
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
							if(flag){
								new SystemUtil().makeToast(DiscussActivity.this,
										"评论成功");
							}else{
								new SystemUtil().makeToast(DiscussActivity.this,
										"回复成功");
							}
							discussBtn.setText("评论");
							
							imm.hideSoftInputFromWindow(contentEdit.getWindowToken(), 0);
						}else{
							new SystemUtil().makeToast(DiscussActivity.this,jsonObject.optString("message"));
						}
						flag = true;
						queryCommentPaginationList();
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						
					}
				});
	}

	private void queryCommentPaginationList() {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("articleId", articleId + "");
		params.addBodyParameter("superId", "0");
		xUtilsUtil.httpPost("common/queryCommentPaginationList.html", params,
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
							Gson gson = new Gson();
							Type type = new TypeToken<CommentPaginationListModel>() {
							}.getType();
							commentPaginationListModel = gson.fromJson(
									jsonObject.toString(), type);

							list = commentPaginationListModel.getDataList();
							adapter = new DiscussListAdapter(
									DiscussActivity.this, list);
							discussList.setAdapter(adapter);
							discussList.setOnTouchListener(DiscussActivity.this);
							
							discussList.setOnItemClickListener(DiscussActivity.this);
							discussList.setOnRefreshListener(DiscussActivity.this);
							
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {

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
	 * ACTION_DOWN: 表示用户开始触摸
	 * ACTION_MOVE: 表示用户在移动(手指或者其他) 
	 * ACTION_UP:表示用户抬起了手指
	 * ACTION_CANCEL:表示手势被取消了
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			System.out.println("ACTION_DOWN");
			imm.hideSoftInputFromWindow(contentEdit.getWindowToken(), 0);
			if("".equals(contentEdit.getText().toString().trim())){
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
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("articleId", articleId + "");
		params.addBodyParameter("superId", "0");
		xUtilsUtil.httpPost("common/queryCommentPaginationList.html", params,
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
							Gson gson = new Gson();
							Type type = new TypeToken<CommentPaginationListModel>() {
							}.getType();
							commentPaginationListModel = gson.fromJson(
									jsonObject.toString(), type);

							List<RidingCommentModel> lists = commentPaginationListModel.getDataList();
							if(pageIndex == 1){
								list = lists;
							}else{
								list.addAll(lists);
							}
						adapter.notifyDataSetChanged();
						discussList.finishRefreshing();
						
							
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {

					}
				});
	}
}
