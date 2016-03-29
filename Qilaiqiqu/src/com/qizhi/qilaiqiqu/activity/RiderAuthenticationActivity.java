package com.qizhi.qilaiqiqu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.umeng.analytics.MobclickAgent;

public class RiderAuthenticationActivity extends HuanxinLogOutActivity
		implements OnClickListener {

	private LinearLayout backLayout;

	private TextView nextTxt;
	private TextView provisoTxt;
	private ImageView isAgreeImg;

	private TextView agreeTxt;

	private WebView webView;

	int flag = 1; // 1:未同意 ，2:同意

	private LinearLayout webtLayout;
	private LinearLayout contentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_authentication_rider);

		initView();
		iniEvnet();
	}

	private void initView() {
		backLayout = (LinearLayout) findViewById(R.id.layout_riderAuthenticationActivity_back);

		nextTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_next);
		provisoTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_proviso);
		isAgreeImg = (ImageView) findViewById(R.id.img_riderAuthenticationActivity_isAgree);

		agreeTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_agree);

		webtLayout = (LinearLayout) findViewById(R.id.layout_riderAuthenticationActivity_web);
		contentLayout = (LinearLayout) findViewById(R.id.layout_riderAuthenticationActivity_content);

		webView = (WebView) findViewById(R.id.riderAuthenticationActivity_webview);
		// 设置WebView属性，能够执行Javascript脚本
		webView.getSettings().setJavaScriptEnabled(true);
		// 加载需要显示的网页
		webView.loadUrl("http://mobile.weride.com.cn/attendRider/attendRiderAgr.html?authCode=admin");
		// 设置Web视图
		webView.setWebViewClient(new HelloWebViewClient());
	}

	private void iniEvnet() {
		backLayout.setOnClickListener(this);

		nextTxt.setOnClickListener(this);
		provisoTxt.setOnClickListener(this);
		isAgreeImg.setOnClickListener(this);
		agreeTxt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_riderAuthenticationActivity_back:
			finish();
			break;

		case R.id.txt_riderAuthenticationActivity_next:
			if (flag == 2) {
				startActivity(new Intent(RiderAuthenticationActivity.this,
						RiderAuthenticationFirstActivity.class));
				finish();
			} else {
				Toasts.show(RiderAuthenticationActivity.this, "请先阅读条款，并同意", 0);
			}

			break;

		case R.id.txt_riderAuthenticationActivity_proviso:
			webtLayout.setVisibility(View.VISIBLE);
			contentLayout.setVisibility(View.GONE);
			break;

		case R.id.txt_riderAuthenticationActivity_agree:
			webtLayout.setVisibility(View.GONE);
			contentLayout.setVisibility(View.VISIBLE);
			break;

		case R.id.img_riderAuthenticationActivity_isAgree:
			if (flag == 1) {
				isAgreeImg.setBackgroundResource(R.drawable.choose_rider);
				flag = 2;
			} else {
				isAgreeImg.setBackgroundResource(R.drawable.unchoen_rider);
				flag = 1;
			}

			break;

		default:
			break;
		}
	}

	// Web视图
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	// 设置回退
	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		webtLayout.setVisibility(View.GONE);
		contentLayout.setVisibility(View.VISIBLE);
		return false;
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
