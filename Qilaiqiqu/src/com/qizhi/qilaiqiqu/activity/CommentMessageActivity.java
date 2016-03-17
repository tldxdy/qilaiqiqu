package com.qizhi.qilaiqiqu.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.CommentModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;
/**
 * 
 * @author hujianbo
 *
 */
public class CommentMessageActivity extends HuanxinLogOutActivity implements OnClickListener,TextWatcher{

	private LinearLayout backLayout;		//返回图片
	
	private TextView nameTxt;	//回复人
	private TextView stateTxt;	//回复
	private TextView returnTitleTxt;	//回复标题
	private TextView returnContentTxt;	//回复内容
	private TextView ReturnNumTxt;	//回复字数
	private TextView ReturnReturnTxt;	
	private EditText myReturnEdt; //我的回复
	private Button affirmBtn;	//确定回复
	private CommentModel commentModel;
	private int isComment;
	
	private SharedPreferences preferences;
	private XUtilsUtil xUtilsUtil;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_comment_message);
		
		initView();
		initEvent();
	}



	private void initView() {
		backLayout = (LinearLayout) findViewById(R.id.layout_commentmessageactivity_back);
		
		returnTitleTxt = (TextView) findViewById(R.id.txt_commentmessageactivity_return_title);
		returnContentTxt = (TextView) findViewById(R.id.txt_commentmessageactivity_return_content);
		ReturnNumTxt = (TextView) findViewById(R.id.txt_commentmessageactivity_return_num);
		nameTxt = (TextView) findViewById(R.id.txt_commentmessageactivity_return_name);
		stateTxt = (TextView) findViewById(R.id.txt_commentmessageactivity_return_state);
		myReturnEdt = (EditText) findViewById(R.id.edt_commentmessageactivity_my_return);
		ReturnReturnTxt = (TextView) findViewById(R.id.txt_commentmessageactivity_return_return);
		affirmBtn = (Button) findViewById(R.id.btn_commentmessageactivity_affirm);
		
		xUtilsUtil = new XUtilsUtil();
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		
		commentModel = (CommentModel) getIntent().getSerializableExtra("commentModel");
		isComment = getIntent().getIntExtra("isComment", -1);
		if(isComment == 1){
			stateTxt.setText("评论了你的骑游记");
		}else if(isComment == 2){
			stateTxt.setText("回复了你的");
			ReturnReturnTxt.setVisibility(View.VISIBLE);
			ReturnReturnTxt.setText("游记评论");
		}
		
		
		nameTxt.setText(commentModel.getUserName());
		returnTitleTxt.setText("《"+commentModel.getTitle()+"》");
		returnContentTxt.setText(commentModel.getMemo());
	}

	private void initEvent() {
		backLayout.setOnClickListener(this);
		myReturnEdt.addTextChangedListener(this);
		affirmBtn.setOnClickListener(this);
		nameTxt.setOnClickListener(this);
		returnTitleTxt.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_commentmessageactivity_back:
			finish();
			break;
		case R.id.btn_commentmessageactivity_affirm:
			comment();
			break;
		case R.id.txt_commentmessageactivity_return_name:
			Intent intent1 = new Intent(this, PersonActivity.class);
			intent1.putExtra("userId", commentModel.getUserId());
			startActivity(intent1);
			break;
		case R.id.txt_commentmessageactivity_return_title:
			Intent intent2 = new Intent(this, RidingDetailsActivity.class);
			intent2.putExtra("articleId", commentModel.getArticleId());
			startActivity(intent2);
			break;
		}
	}

	private void comment() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("articleId",commentModel.getArticleId() + "");
		params.addBodyParameter("commentMemo",myReturnEdt.getText().toString().trim());
		if(isComment == 1){
			params.addBodyParameter("parentId",commentModel.getCommentId()+ "");
			params.addBodyParameter("superId",commentModel.getCommentId() + "");
		}else if(isComment == 2){
			params.addBodyParameter("superId",commentModel.getSuperId()+ "");
			params.addBodyParameter("parentId",commentModel.getCommentId() + "");
		}
		xUtilsUtil.httpPost("mobile/comment/insertComment.html", params, new CallBackPost() {
			
			@Override
			public void onMySuccess(ResponseInfo<String> responseInfo) {
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(responseInfo.result);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (jsonObject.optBoolean("result")) {
					if(isComment == 1){
						new SystemUtil().makeToast(CommentMessageActivity.this, "评论成功");
					}else if(isComment == 2){
						new SystemUtil().makeToast(CommentMessageActivity.this, "回复成功");
					}
				}else{
					new SystemUtil().makeToast(CommentMessageActivity.this, jsonObject.optString("message"));
				}
				myReturnEdt.setText("");
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				
			}
		});
	}



	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		ReturnNumTxt.setText(Html.fromHtml(s.length()+"<font color='#9d9d9e'>/50</font>"));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
}
