package com.qizhi.qilaiqiqu.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.ReleaseListAdapter;
import com.qizhi.qilaiqiqu.model.PublishTravelsModel;
import com.qizhi.qilaiqiqu.model.TravelsinformationModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author hujianbo
 * 
 */
public class ReleaseActivity extends Activity implements OnClickListener,
		TextWatcher {

	private LinearLayout backLayout; // 返回

	private TextView browseTxt;
	private TextView publishTxt;
	private TextView titleNumTxt; // 主题字数

	private TextView titleTxt;
	
	private ListView releaseList;

	private ImageView releaseAddImg;

	private EditText titleEdt; // 主题

	private ReleaseListAdapter adapter;

	private List<TravelsinformationModel> list;

	private List<String> imgListUrl;

	private PublishTravelsModel ptm;

	private XUtilsUtil xUtilsUtil;

	private SharedPreferences preferences;
	
	private ArrayList<String> photoList;
	private boolean falg = false; //判断图片哪里传过来的
	private int articleId;
	
	private int num = 0;
	private LinearLayout waitLayout;
	private TextView waitTxt;
	
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String s = (String) msg.obj;
				imgListUrl.add(s);
				if(list.size()-1 != num){
					num = num + 1;
					new SystemUtil().httpClient(list.get(num).getArticleImage(), preferences, handler, "QYJ");
					//photoUploading();
				}else{
					publishTravels();
				}
				break;

			default:
				break;
			}
		}
		
	};
	
	
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_release);

		initView();
		initEvent();
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		list = new ArrayList<TravelsinformationModel>();
		ptm = new PublishTravelsModel();
		preferences = getSharedPreferences("userLogin",
				Context.MODE_PRIVATE);
		xUtilsUtil = new XUtilsUtil();

		backLayout = (LinearLayout) findViewById(R.id.layout_releaseactivity_back);
		waitLayout = (LinearLayout) findViewById(R.id.layout_releaseactivity_wait);
		
		browseTxt = (TextView) findViewById(R.id.txt_releaseactivity_browse);
		publishTxt = (TextView) findViewById(R.id.txt_releaseactivity_publish);
		titleTxt = (TextView) findViewById(R.id.txt_releaseactivity_title);
		waitTxt = (TextView) findViewById(R.id.txt_releaseactivity_wait);
		
		releaseList = (ListView) findViewById(R.id.list_releaseactivity_release);
		
		initHeaderView();
		initFooterView();
		
		falg = getIntent().getBooleanExtra("falg", false);
		if(!falg){
		photoList = getIntent().getStringArrayListExtra("photoList");
		
		for (String string : photoList) {
			TravelsinformationModel rt = new TravelsinformationModel();
			rt.setArticleImage(string);
			list.add(rt);
		}
		}else{
			list =(ArrayList<TravelsinformationModel>) getIntent().getSerializableExtra("list");
			titleEdt.setText(list.get(0).getTitle());
			titleNumTxt.setText(list.get(0).getTitle().length()  + "/15");
			browseTxt.setVisibility(View.GONE);
			articleId = getIntent().getIntExtra("articleId", -1);
			publishTxt.setText("修改");
			titleTxt.setText("修改骑游记");
		}
		
		adapter = new ReleaseListAdapter(this, list, ptm, falg);
		releaseList.setAdapter(adapter);
		
	}

	private void initHeaderView() {
		View headerView = View.inflate(releaseList.getContext(),
				R.layout.item_list_releaseactivity_header, null);
		titleEdt = (EditText) headerView
				.findViewById(R.id.edt_releaseactivity_title);
		titleNumTxt = (TextView) headerView
				.findViewById(R.id.txt_releaseactivity_title_num);

		releaseList.addHeaderView(headerView);
	}

	private void initFooterView() {
		View footerView = View.inflate(releaseList.getContext(),
				R.layout.item_list_releaseactivity_footer, null);
		releaseAddImg = (ImageView) footerView
				.findViewById(R.id.img_release_add);
		releaseAddImg.setOnClickListener(this);
		releaseList.addFooterView(footerView);
	}

	private void initEvent() {
		backLayout.setOnClickListener(this);
		browseTxt.setOnClickListener(this);
		publishTxt.setOnClickListener(this);
		titleEdt.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_releaseactivity_back:
			finish();
			break;
		case R.id.txt_releaseactivity_browse:
			new SystemUtil().makeToast(this, "预览");
			
			break;
		case R.id.txt_releaseactivity_publish:
			//new SystemUtil().makeToast(this, "发表");
			// 图片上传
			if(!falg){
				imgListUrl = new ArrayList<String>();
				num = 0;
				//photoUploading();
				
				if(list.size() != 0){
					new SystemUtil().httpClient(list.get(num).getArticleImage(), preferences, handler, "QYJ");
				}
			}else{
				publishTravels();
			}
			
			// 发布
			// publishTravels();

			break;
		case R.id.img_release_add:
			Intent intent = new Intent(this, NativeImagesActivity.class);
			intent.putExtra("falg", true);
			startActivityForResult(intent, 1);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED) {
			if (requestCode == 1) {
				ArrayList<String> photoList = data
						.getStringArrayListExtra("photoList");
				for (String string : photoList) {
					TravelsinformationModel rt = new TravelsinformationModel();
					rt.setArticleImage(string);
					list.add(rt);
				}
				adapter.notifyDataSetChanged();
			}else if(requestCode == 2){
				int n = data.getIntExtra("position", -1);
				if(n != -1){
					list.get(n).setAddress(data.getStringExtra("address"));
				}
				adapter.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

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
		titleNumTxt.setText(s.length() + "/15");
	}

	private void publishTravels() {
		StringBuffer sbMemo = new StringBuffer();
		StringBuffer sbArticleImage = new StringBuffer();
		StringBuffer sbImageMemo = new StringBuffer();
		StringBuffer sbAddress = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getMemo() != null) {
				sbMemo.append(list.get(i).getMemo());
			}
			if(!falg){
				sbArticleImage.append(imgListUrl.get(i));
			}else{
				sbArticleImage.append(list.get(i).getArticleImage());
			}
			if (list.get(i).getImageMemo() != null) {
				sbImageMemo.append(list.get(i).getImageMemo());
			}
			
			if (list.get(i).getAddress() != null) {
				sbAddress.append(list.get(i).getAddress());
			}
				sbMemo.append("|");
				sbArticleImage.append("|");
				sbImageMemo.append("|");
				sbAddress.append("|");
		}
		ptm.setTitle(titleEdt.getText().toString().trim());
		ptm.setMemo(sbMemo.toString().substring(0, sbMemo.toString().length()-1));
		ptm.setArticleImage(sbArticleImage.toString().substring(0, sbArticleImage.toString().length()-1));
		ptm.setImageMemo(sbImageMemo.toString().substring(0, sbImageMemo.toString().length()-1));
		ptm.setAddress(sbAddress.toString().substring(0, sbAddress.toString().length()-1));
		RequestParams params = new RequestParams("UTF-8");
		String url;
		if(falg){
			params.addBodyParameter("articleId", articleId + "");
			url = "mobile/articleMemo/updateArticle.html";
		}else{
			url = "mobile/articleMemo/insertArticle.html";
		}
		params.addBodyParameter("userId", preferences.getInt("userId", 0)
				+ "");
		params.addBodyParameter("title", ptm.getTitle());
		params.addBodyParameter("memo", ptm.getMemo());
		params.addBodyParameter("address", ptm.getAddress());
		params.addBodyParameter("articleImage", ptm.getArticleImage());
		params.addBodyParameter("imageMemo", ptm.getImageMemo());
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost(url , params,
				new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(responseInfo.result);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (jsonObject.optBoolean("result")) {
							if(!falg){
								new SystemUtil().makeToast(ReleaseActivity.this,
										"发表成功");
								ReleaseActivity.this.finish();
							}else{
								new SystemUtil().makeToast(ReleaseActivity.this,
										"修改成功");
								ReleaseActivity.this.finish();
							}
						}else{
							new SystemUtil().makeToast(ReleaseActivity.this,
									jsonObject.optString("message"));
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {

					}
				});
	}

/*	private void photoUploading() {
		File file;
		imgListUrl = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			imgListUrl.add(null);
		}
		if (list.size() != 0) {
			RequestParams params = new RequestParams("UTF-8");
			file = new File(list.get(num).getArticleImage());
			params.addBodyParameter("files",file);
			params.addBodyParameter("type", "QYJ");
			params.addBodyParameter("uniqueKey",
					preferences.getString("uniqueKey", "admin"));
			xUtilsUtil.httpPost("common/uploadImage.html", params,
					new CallBackPost() {
						@Override
						public void onMySuccess(
								ResponseInfo<String> responseInfo) {
							System.out.println("返回结果：" + responseInfo.result);
							JSONObject jsonObject = null;
							try {
								jsonObject = new JSONObject(responseInfo.result);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							if (jsonObject.optBoolean("result")) {
								JSONArray jsonArray = jsonObject
										.optJSONArray("dataList");
								String s = jsonArray.optString(0);
									Message msg = new Message();
									msg.what = 1;
									msg.obj = s;
									handler.handleMessage(msg);
									System.out.println("图片：" + imgListUrl);
									//publishTravels();

							}

						}

						@Override
						public void onMyFailure(HttpException error, String msg) {
							System.out.println("msg:" + msg);
						}
					});

		}

	}*/

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
