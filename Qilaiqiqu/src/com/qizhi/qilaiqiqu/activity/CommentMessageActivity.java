package com.qizhi.qilaiqiqu.activity;

import android.app.Activity;
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
import android.widget.Toast;

import com.qizhi.qilaiqiqu.R;
import com.umeng.analytics.MobclickAgent;
/**
 * 
 * @author hujianbo
 *
 */
public class CommentMessageActivity extends Activity implements OnClickListener,TextWatcher{

	private LinearLayout backLayout;		//返回图片
	
	private TextView returnTitleTxt;	//回复标题
	private TextView returnContentTxt;	//回复内容
	private TextView ReturnNumTxt;	//回复字数
	
	private EditText myReturnEdt; //我的回复
	private Button affirmBtn;	//确定回复
	
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
		
		myReturnEdt = (EditText) findViewById(R.id.edt_commentmessageactivity_my_return);
		
		affirmBtn = (Button) findViewById(R.id.btn_commentmessageactivity_affirm);
	}

	private void initEvent() {
		String str1 = "独步天涯";
		String str2 = "《大西北骑行》";
		String s = "<font color='#6dbfed'>"+str1+"</font>"+"评论了你的骑游记"+"<font color='#6dbfed'>"+str2+"</font>";
		returnTitleTxt.setText(Html.fromHtml(s));
		ReturnNumTxt.setText(Html.fromHtml(0+"<font color='#9d9d9e'>/50</font>"));
		backLayout.setOnClickListener(this);
		myReturnEdt.addTextChangedListener(this);
		affirmBtn.setOnClickListener(this);
		
		/* //此行必须有  
		returnTitleTxt.setMovementMethod(LinkMovementMethod.getInstance());
		
		 SpannableString sp = new SpannableString(returnTitleTxt.getText().toString());  
		 sp.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				System.out.println("点击了名字");
			}
		}, 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);   */
	     
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_commentmessageactivity_back:
			CommentMessageActivity.this.finish();
			//Toast.makeText(this, "点击返回", 0).show();
			break;
		case R.id.btn_commentmessageactivity_affirm:
			Toast.makeText(this, "点击确认", 0).show();
			break;
		}
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
