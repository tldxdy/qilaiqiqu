package com.qizhi.qilaiqiqu.activity;

import java.io.File;
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

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
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

	private ListView releaseList;

	private ImageView releaseAddImg;

	private EditText titleEdt; // 主题

	private ReleaseListAdapter adapter;

	private List<TravelsinformationModel> list;

	private List<String> imgListUrl;

	private PublishTravelsModel ptm;

	private XUtilsUtil xUtilsUtil;

	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_release);

		initView();
		initEvent();
	}

	private void initView() {
		list = new ArrayList<TravelsinformationModel>();
		ptm = new PublishTravelsModel();
		sharedPreferences = getSharedPreferences("userLogin",
				Context.MODE_PRIVATE);
		xUtilsUtil = new XUtilsUtil();

		backLayout = (LinearLayout) findViewById(R.id.layout_releaseactivity_back);

		browseTxt = (TextView) findViewById(R.id.txt_releaseactivity_browse);
		publishTxt = (TextView) findViewById(R.id.txt_releaseactivity_publish);

		releaseList = (ListView) findViewById(R.id.list_releaseactivity_release);
		initHeaderView();
		initFooterView();

		ArrayList<String> photoList = getIntent().getStringArrayListExtra(
				"photoList");
		for (String string : photoList) {
			TravelsinformationModel rt = new TravelsinformationModel();
			rt.setArticleImage(string);
			list.add(rt);
		}
		adapter = new ReleaseListAdapter(this, list, ptm);
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
			ReleaseActivity.this.finish();
			break;
		case R.id.txt_releaseactivity_browse:
			new SystemUtil().makeToast(this, "预览");
			break;
		case R.id.txt_releaseactivity_publish:
			new SystemUtil().makeToast(this, "发表");
			// 图片上传
			photoUploading();
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
		System.out.println(requestCode);
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
			sbArticleImage.append(imgListUrl.get(i));
			if (list.get(i).getImageMemo() != null) {
				sbImageMemo.append(list.get(i).getImageMemo());
			}
			if (list.get(i).getAddress() != null) {
				sbAddress.append(list.get(i).getAddress());
			}
			if (i != list.size() - 1) {
				sbMemo.append("|");
				sbArticleImage.append("|");
				sbImageMemo.append("|");
				sbAddress.append("|");
			}
		}
		ptm.setTitle(titleEdt.getText().toString().trim());
		ptm.setMemo(sbMemo.toString());
		ptm.setArticleImage(sbArticleImage.toString());
		ptm.setImageMemo(sbImageMemo.toString());
		ptm.setAddress(sbAddress.toString());
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userId", sharedPreferences.getInt("userId", 0)
				+ "");
		params.addBodyParameter("title", ptm.getTitle());
		params.addBodyParameter("memo", ptm.getMemo());
		params.addBodyParameter("address", ptm.getAddress());
		params.addBodyParameter("articleImage", ptm.getArticleImage());
		params.addBodyParameter("imageMemo", ptm.getImageMemo());
		params.addBodyParameter("uniqueKey",
				sharedPreferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/articleMemo/insertArticle.html", params,
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
							new SystemUtil().makeToast(ReleaseActivity.this,
									"发表成功");
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {

					}
				});
	}

	private void photoUploading() {
		File file;
		imgListUrl = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			imgListUrl.add(null);
		}
		for (int i = 0; i < list.size(); i++) {
			final int num = i;
			RequestParams params = new RequestParams("UTF-8");
			file = new File(list.get(i).getArticleImage());
			params.addBodyParameter("files", file);
			params.addBodyParameter("type", "QYJ");
			params.addBodyParameter("uniqueKey",
					sharedPreferences.getString("uniqueKey", "admin"));
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
								// String[] ss = s.split("@");
								imgListUrl.set(num, s);
								boolean falg = true;
								for (int j = 0; j < imgListUrl.size() && falg; j++) {
									if (imgListUrl.get(j) == null) {
										falg = false;
									}
								}
								if (falg) {
									System.out.println("图片：" + imgListUrl);
									publishTravels();
								}

							}

						}

						@Override
						public void onMyFailure(HttpException error, String msg) {
							System.out.println("msg:" + msg);
						}
					});

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