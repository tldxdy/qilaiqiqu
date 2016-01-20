package com.qizhi.qilaiqiqu.activity;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
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
import com.qizhi.qilaiqiqu.model.TravelsinformationModel;
import com.qizhi.qilaiqiqu.progress.FileUploadAsyncTask;
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

	private XUtilsUtil xUtilsUtil;

	private SharedPreferences preferences;

	private ArrayList<String> photoList;
	private boolean falg = false; // 判断图片哪里传过来的
	private int articleId;

	private int num = 0;
	
	private int updateListSum;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String s = (String) msg.obj;
				imgListUrl.add(s);
				if (list.size() - 1 != num) {
					num = num + 1;
					File file = new File(list.get(num).getArticleImage());
					new FileUploadAsyncTask(ReleaseActivity.this, (num + 1),
							list.size(), preferences, "QYJ", handler)
							.execute(file);
					// new
					// SystemUtil().httpClient(list.get(num).getArticleImage(),
					// preferences, handler, "QYJ");
					// photoUploading();
				} else {
					publishTravels();
				}
				break;
			
			 case 2: 
				 int p = (Integer) msg.obj;
				 if(falg){
					 if(p < updateListSum){
						 updateListSum = updateListSum - 1;
					 }
				 }
				 list.remove(p);
				 adapter.notifyDataSetChanged();
			
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
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		xUtilsUtil = new XUtilsUtil();

		backLayout = (LinearLayout) findViewById(R.id.layout_releaseactivity_back);

		browseTxt = (TextView) findViewById(R.id.txt_releaseactivity_browse);
		publishTxt = (TextView) findViewById(R.id.txt_releaseactivity_publish);
		titleTxt = (TextView) findViewById(R.id.txt_releaseactivity_title);

		releaseList = (ListView) findViewById(R.id.list_releaseactivity_release);

		initHeaderView();
		initFooterView();

		falg = getIntent().getBooleanExtra("falg", false);
		if (!falg) {
			photoList = getIntent().getStringArrayListExtra("photoList");
			for (String string : photoList) {
				TravelsinformationModel rt = new TravelsinformationModel();
				rt.setArticleImage(string);
				list.add(rt);
			}
		} else {
			list = (ArrayList<TravelsinformationModel>) getIntent()
					.getSerializableExtra("list");
			updateListSum = list.size();
			titleEdt.setText(list.get(0).getTitle());
			titleNumTxt.setText(list.get(0).getTitle().length() + "/15");
			browseTxt.setVisibility(View.GONE);
			articleId = getIntent().getIntExtra("articleId", -1);
			publishTxt.setText("修改");
			titleTxt.setText("修改骑游记");
		}

		adapter = new ReleaseListAdapter(this, list, falg,handler,updateListSum);
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
		releaseList.addFooterView(footerView);
	}

	private void initEvent() {
		backLayout.setOnClickListener(this);
		browseTxt.setOnClickListener(this);
		publishTxt.setOnClickListener(this);
		titleEdt.addTextChangedListener(this);
		
		releaseAddImg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_releaseactivity_back:
			ReleaseActivity.this.finish();
			break;
		case R.id.txt_releaseactivity_browse:
			if (list.size() != 0) {
				Intent intents = new Intent(this, RidingDetailsActivity.class);
				list.get(0).setTitle(titleEdt.getText().toString().trim());
				intents.putExtra("previewList", (Serializable) list);
				intents.putExtra("ReleaseActivityfalg", true);
				startActivityForResult(intents, 3);
			}
			break;
		case R.id.txt_releaseactivity_publish:
			// new SystemUtil().makeToast(this, "发表");
			// 图片上传
			if (!falg) {
				imgListUrl = new ArrayList<String>();
				num = 0;
				// photoUploading();

				if (list.size() != 0) {
					File file = new File(list.get(num).getArticleImage());
					new FileUploadAsyncTask(this, num + 1, list.size(),
							preferences, "QYJ", handler).execute(file);
					// new
					// SystemUtil().httpClient(list.get(num).getArticleImage(),
					// preferences, handler, "QYJ");
				}
			} else {
				imgListUrl = new ArrayList<String>();
				num = 0;
				// photoUploading();

				if (list.size() > updateListSum) {
					num = updateListSum;
					File file = new File(list.get(num).getArticleImage());
					new FileUploadAsyncTask(this, num + 1, list.size() - updateListSum,
							preferences, "QYJ", handler).execute(file);
				}else{
					publishTravels();
				}
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
			} else if (requestCode == 2) {
				int n = data.getIntExtra("position", -1);
				if (n != -1) {
					list.get(n).setAddress(data.getStringExtra("address"));
				}
				adapter.notifyDataSetChanged();
			} else if (requestCode == 3) {
				finish();
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
		/*
		 * new Thread(){
		 * 
		 * @Override public void run() { super.run();
		 */
		/*
		 * String httpUrl; if(!falg){ httpUrl =
		 * "http://120.55.195.170:80/mobile/articleMemo/insertArticle.html";
		 * }else{ httpUrl =
		 * "http://120.55.195.170:80/mobile/articleMemo/updateArticle.html"; }
		 * HttpPost httpRequest=new HttpPost(httpUrl);
		 */
		// 使用NameValuePair来保存要传递的Post参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uniqueKey", preferences.getString(
				"uniqueKey", null)));
		params.add(new BasicNameValuePair("userId", preferences.getInt(
				"userId", -1) + ""));
		params.add(new BasicNameValuePair("title", titleEdt.getText()
				.toString().trim()));

		if (falg) {
			params.add(new BasicNameValuePair("articleId", articleId + ""));
		}
		for (int i = 0; i < list.size(); i++) {

			// 需要注意的是 如果某个图片的说明 为空 时 请传递空 不能 少一个属性 每一个数组必须保证相同的长度
			if (!falg) {
				params.add(new BasicNameValuePair("articleImage", imgListUrl
						.get(i)));
			} else {
				if(i < updateListSum){
					params.add(new BasicNameValuePair("articleImage", list.get(i)
							.getArticleImage()));
				}else{
					params.add(new BasicNameValuePair("articleImage", imgListUrl
							.get(i - updateListSum)));
				}
			}

			if (list.get(i).getImageMemo() == null) {
				params.add(new BasicNameValuePair("imageMemo", ""));
			} else {
				params.add(new BasicNameValuePair("imageMemo", list.get(i)
						.getImageMemo()));
			}

			if (list.get(i).getAddress() == null) {
				params.add(new BasicNameValuePair("address", ""));
			} else {
				params.add(new BasicNameValuePair("address", list.get(i)
						.getAddress()));
			}

			if (list.get(i).getMemo() == null) {
				params.add(new BasicNameValuePair("memo", ""));
			} else {
				params.add(new BasicNameValuePair("memo", list.get(i).getMemo()));
			}

		}
		System.out.println("------------------------------------");
		System.out.println(params.toString());
		RequestParams params2 = new RequestParams();
		try {
			params2.setBodyEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			String url;
			if (!falg) {
				url = "mobile/articleMemo/insertArticle.html";
			} else {
				url = "mobile/articleMemo/updateArticle.html";
			}
			xUtilsUtil.httpPost(url, params2, new CallBackPost() {

				@Override
				public void onMySuccess(ResponseInfo<String> responseInfo) {
					JSONObject jsonObject = null;
					System.out.println("------------------------------------");
					System.out.println(responseInfo.result);
					System.out.println("------------------------------------");
					try {
						jsonObject = new JSONObject(responseInfo.result);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (jsonObject.optBoolean("result")) {
						if (!falg) {
							new SystemUtil().makeToast(ReleaseActivity.this,
									"发表成功");
							ReleaseActivity.this.finish();
						} else {
							new SystemUtil().makeToast(ReleaseActivity.this,
									"修改成功");
							ReleaseActivity.this.finish();
						}
					} else {
						System.out.println(jsonObject.optString("message"));
						new SystemUtil().makeToast(ReleaseActivity.this,
								jsonObject.optString("message"));
					}
				}

				@Override
				public void onMyFailure(HttpException error, String msg) {

				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

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
