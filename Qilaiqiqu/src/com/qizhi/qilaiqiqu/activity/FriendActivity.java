package com.qizhi.qilaiqiqu.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.fragment.CareFragment;
import com.qizhi.qilaiqiqu.fragment.FansFragment;
import com.qizhi.qilaiqiqu.model.CareModel;
import com.qizhi.qilaiqiqu.model.CareModel.CareDataList;
import com.qizhi.qilaiqiqu.model.FansModel;
import com.qizhi.qilaiqiqu.model.FansModel.FansDataList;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

public class FriendActivity extends FragmentActivity implements OnClickListener, CallBackPost {

	private LinearLayout backLayout;
	private TextView fansTxt;
	private TextView careTxt;

	private int friendFlag;

	private ViewPager viewPager;
	private ArrayList<Fragment> fragments;
	private FragmentPagerAdapter adapter;

	private SharedPreferences sp;
	private CareModel careModel;
	private FansModel fansModel;
	
	private ArrayList<CareDataList> careDataList = new ArrayList<CareDataList>();
	private ArrayList<FansDataList> fansDataList = new ArrayList<FansDataList>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friend);
		getfans();
	}

	private void initView() {
		friendFlag = getIntent().getIntExtra("friendFlag", -1);

		viewPager = (ViewPager) findViewById(R.id.viewPager_friendActivity);
		fansTxt = (TextView) findViewById(R.id.txt_friendActivity_fans);
		careTxt = (TextView) findViewById(R.id.txt_friendActivity_care);
		backLayout = (LinearLayout) findViewById(R.id.layout_FriendActivity_back);

		fragments = new ArrayList<Fragment>();
		Fragment fansFragment = FansFragment.newInstance(fansDataList, this);
		Fragment careFragment = CareFragment.newInstance(careDataList, this);
		fragments.add(fansFragment);
		fragments.add(careFragment);

		adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return fragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return fragments.get(arg0);
			}
		};
		viewPager.setAdapter(adapter);
	}

	private void initEvent() {
		fansTxt.setOnClickListener(this);
		careTxt.setOnClickListener(this);
		backLayout.setOnClickListener(this);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int currentItem = viewPager.getCurrentItem();
				restImageAndText();
				switch (currentItem) {
				case 0:
					fansTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
					fansTxt.setTextColor(getResources().getColor(R.color.white));
					careTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
					careTxt.setTextColor(getResources().getColor(R.color.bule));
					break;
				case 1:
					careTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_press);
					careTxt.setTextColor(getResources().getColor(R.color.white));
					fansTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_upspring);
					fansTxt.setTextColor(getResources().getColor(R.color.bule));
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_friendActivity_fans:
			setSelect(0);
			break;

		case R.id.txt_friendActivity_care:
			setSelect(1);
			break;

		case R.id.layout_FriendActivity_back:
			finish();
			break;

		default:
			break;
		}
	}

	private void setSelect(int i) {
		restImageAndText();
		switch (i) {
		case 0:
			fansTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
			fansTxt.setTextColor(getResources().getColor(R.color.white));
			careTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
			careTxt.setTextColor(getResources().getColor(R.color.bule));
			break;
		case 1:
			careTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_press);
			careTxt.setTextColor(getResources().getColor(R.color.white));
			fansTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_upspring);
			fansTxt.setTextColor(getResources().getColor(R.color.bule));
			break;
		}
		viewPager.setCurrentItem(i);
	}

	private void restImageAndText() {
		fansTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
		fansTxt.setTextColor(getResources().getColor(R.color.white));
		careTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
		careTxt.setTextColor(getResources().getColor(R.color.bule));
	}

	public void getCare() {
		sp = getSharedPreferences("userLogin", 0);

		String url = "mobile/attention/queryMyAttentionPaginationList.html";
		RequestParams params = new RequestParams();
		int quoteUserId = sp.getInt("userId", -1);
		String pageSize = "10";
		String uniqueKey = sp.getString("uniqueKey", null);

		params.addBodyParameter("userId", quoteUserId + "");
		params.addBodyParameter("pageIndex", 1 + "");
		params.addBodyParameter("pageSize", pageSize);
		params.addBodyParameter("uniqueKey", uniqueKey);
		new XUtilsUtil().httpPost(url, params, this);

	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		try {
			JSONObject object = new JSONObject(responseInfo.result);
			Gson gson = new Gson();
			careModel = gson.fromJson(object.toString(),
					new TypeToken<CareModel>() {
					}.getType());
			careDataList = careModel.getDataList();
			initView();
			initEvent();
			setSelect(friendFlag);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		
	}

	
	public void getfans(){
		sp = getSharedPreferences("userLogin", 0);

		String url = "mobile/attention/queryCoverAttentionPaginationList.html";
		RequestParams params = new RequestParams();
		int quoteUserId = sp.getInt("userId", -1);
		String pageSize = "10";
		String uniqueKey = sp.getString("uniqueKey", null);

		params.addBodyParameter("quoteUserId", quoteUserId + "");
		params.addBodyParameter("pageIndex", 1 + "");
		params.addBodyParameter("pageSize", pageSize);
		params.addBodyParameter("uniqueKey", uniqueKey);
		
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, "http://120.55.195.170:80/" + url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						System.out.println("开始请求");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							System.out.println("加载中");
						} else {
							System.out.println("未加载");
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject object = new JSONObject(responseInfo.result);
							Gson gson = new Gson();
							fansModel = gson.fromJson(object.toString(),
									new TypeToken<FansModel>() {
									}.getType());
							fansDataList = fansModel.getDataList();
							getCare();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
					}
				});
	}
	
}
