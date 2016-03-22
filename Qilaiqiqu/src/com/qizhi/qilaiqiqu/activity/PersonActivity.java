package com.qizhi.qilaiqiqu.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.CertainUserModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */

public class PersonActivity extends HuanxinLogOutActivity implements
		OnClickListener, CallBackPost {

	private LinearLayout backLayout;// 返回按钮

	private ImageView photoImg;// 头像
	private ImageView sexImg;// 性别

	private TextView nickTxt;// 昵称
	private TextView pointTxt;// 积分数

	private TextView careNumTxt;// 关注数
	private TextView fansNumTxt;// 粉丝数
	private TextView cityTxt;// 市
	private TextView provinceTxt;// 省
	private TextView districtTet;// 区
	private TextView introduceTxt;// 个人简介

	private int careFlagInt = 1;
	private TextView careTxt;// 关注按钮
	private TextView personal_letterTxt;// 私信

	private int userId;
	private XUtilsUtil xUtilsUtil;
	private SharedPreferences preferences;
	private int fansNum;
	private CertainUserModel certainUserModel;
	
	
	private TextView txt1;	//同意
	private TextView txt2;	//拒绝
	private TextView txt3;	//处理状态
	private LinearLayout buttonLayout1;	//	未处理
	private LinearLayout buttonLayout2;	//	已处理
	
	private TextView ridingNumTxt;		//发表骑游数
	private TextView joinNumTxt;		//参加活动数
	private TextView activityNumTxt;	//发布活动数
	
	private TextView attendRiderTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_person);
		initView();
		initEvent();
	}

	private void initView() {
		xUtilsUtil = new XUtilsUtil();
		certainUserModel = new CertainUserModel();

		backLayout = (LinearLayout) findViewById(R.id.layout_personalActivity_back);

		nickTxt = (TextView) findViewById(R.id.txt_personActivity_nick);
		pointTxt = (TextView) findViewById(R.id.txt_personActivity_point);
		careNumTxt = (TextView) findViewById(R.id.txt_personActivity_careNum);
		fansNumTxt = (TextView) findViewById(R.id.txt_personActivity_fansNum);
		cityTxt = (TextView) findViewById(R.id.txt_personActivity_city);
		provinceTxt = (TextView) findViewById(R.id.txt_personActivity_province);
		districtTet = (TextView) findViewById(R.id.txt_personActivity_district);
		introduceTxt = (TextView) findViewById(R.id.txt_personActivity_introduce);
		careTxt = (TextView) findViewById(R.id.txt_personActivity_care);
		personal_letterTxt = (TextView) findViewById(R.id.txt_personActivity_personal_letter);

		sexImg = (ImageView) findViewById(R.id.img_personActivity_sex);
		photoImg = (ImageView) findViewById(R.id.img_personActivity_photo);
		
		
		ridingNumTxt = (TextView) findViewById(R.id.txt_personActivity_ridingNum);
		joinNumTxt = (TextView) findViewById(R.id.txt_personActivity_joinNum);
		activityNumTxt = (TextView) findViewById(R.id.txt_personActivity_activityNum);
		
		attendRiderTxt = (TextView) findViewById(R.id.txt_personActivity_attendRider);
		
		
		txt1 = (TextView) findViewById(R.id.txt_personActivity_txt1);
		txt2 = (TextView) findViewById(R.id.txt_personActivity_txt2);
		txt3 = (TextView) findViewById(R.id.txt_personActivity_txt3);
		buttonLayout1 = (LinearLayout) findViewById(R.id.layout_personActivity_button1);
		buttonLayout2 = (LinearLayout) findViewById(R.id.layout_personActivity_button2);

		userId = getIntent().getIntExtra("userId", -1);
		isRider = getIntent().getIntExtra("isRider", -1);
		if(isRider != -1){
			buttonLayout1.setVisibility(View.VISIBLE);
			buttonLayout2.setVisibility(View.GONE);
		}
	}
	
	private int isRider = -1;

	private void initEvent() {
		backLayout.setOnClickListener(this);
		nickTxt.setOnClickListener(this);
		photoImg.setOnClickListener(this);
		careTxt.setOnClickListener(this);
		personal_letterTxt.setOnClickListener(this);
		txt1.setOnClickListener(this);
		txt2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_personalActivity_back:
			finish();
			break;

		case R.id.txt_personActivity_care:
			if (preferences.getInt("userId", -1) == -1) {
				Toasts.show(this, "请登录", 0);
				startActivity(new Intent(this, LoginActivity.class));
				PersonActivity.this.finish();
				break;
			}

			if (careFlagInt == 1) {
				// 添加关注
				RequestParams params2 = new RequestParams();
				params2.addBodyParameter("userId",
						preferences.getInt("userId", -1) + "");
				params2.addBodyParameter("quoteUserId", userId + "");
				params2.addBodyParameter("uniqueKey",
						preferences.getString("uniqueKey", null));
				xUtilsUtil.httpPost("mobile/attention/appendAttention.html",
						params2, this);
			} else {
				RequestParams params3 = new RequestParams();
				params3.addBodyParameter("userId",
						preferences.getInt("userId", -1) + "");
				params3.addBodyParameter("quoteUserId", userId + "");
				params3.addBodyParameter("uniqueKey",
						preferences.getString("uniqueKey", null));
				xUtilsUtil.httpPost("mobile/attention/cancelAttention.html",
						params3, this);
			}

			break;
		case R.id.txt_personActivity_personal_letter:
			if (preferences.getInt("userId", -1) == -1) {
				Toasts.show(this, "请登录", 0);
				startActivity(new Intent(this, LoginActivity.class));
				PersonActivity.this.finish();
				break;
			}
			startActivity(new Intent(this, ChatSingleActivity.class)
					.putExtra("username", certainUserModel.getImUserName())
					.putExtra("otherUserName", certainUserModel.getUserName())
					.putExtra("otherUserImage", certainUserModel.getUserImage())
					.putExtra("otherUserId", certainUserModel.getUserId()));
			break;

		case R.id.txt_personActivity_txt1:
			Toasts.show(this, "同意", 0);
			break;
		case R.id.txt_personActivity_txt2:
			Toasts.show(this, "拒绝", 0);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStart() {
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", userId + "");
		if (preferences.getInt("userId", -1) != -1) {
			params.addBodyParameter("loginUserId",
					preferences.getInt("userId", -1) + "");
		}
		xUtilsUtil.httpPost("common/queryCertainUser.html", params,
				new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						String s = responseInfo.result;
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.optBoolean("result")) {
								JSONObject jo = jsonObject
										.optJSONObject("data");
								Gson gson = new Gson();
								certainUserModel = gson.fromJson(jo.toString(),
										CertainUserModel.class);
								careNumTxt.setText(certainUserModel
										.getConcernNum() + "");
								fansNum = jo.optInt("fansNum");
								fansNumTxt.setText(certainUserModel
										.getFansNum() + "");
								pointTxt.setText(certainUserModel.getIntegral()
										+ "");
								nickTxt.setText(certainUserModel.getUserName());
								if ("null".equals(certainUserModel
										.getUserMemo())
										|| "".equals(certainUserModel
												.getUserMemo())) {

								} else {
									introduceTxt.setText(certainUserModel
											.getUserMemo());
								}
								if ("".equals(certainUserModel.getAddress())
										|| "null".equals(certainUserModel
												.getAddress())
										|| certainUserModel.getAddress() == null) {
									cityTxt.setText("");
									provinceTxt.setText("");
									districtTet.setText("");
								} else {
									String[] ss = certainUserModel.getAddress()
											.split(",|，|\\|");
									System.out.println(ss.length);
									if (ss.length > 2) {
										provinceTxt.setText(ss[0]);
										cityTxt.setText(ss[1]);
										districtTet.setText(ss[2]);
									}
								}
								if ("M".equals(certainUserModel.getSex())) {
									sexImg.setImageResource(R.drawable.male);
								} else if ("F".equals(certainUserModel.getSex())) {
									sexImg.setImageResource(R.drawable.female);
								} else {
									sexImg.setImageBitmap(null);
								}
								SystemUtil.Imagexutils(
										certainUserModel.getUserImage(),
										photoImg, PersonActivity.this);
								if (certainUserModel.isAttentionFlag()) {
									careFlagInt = 2;
									careTxt.setBackgroundResource(R.drawable.corners_bg_orange);
									careTxt.setText("已关注");
								}

							}
							if(certainUserModel.isAttendRiderFlag()){
								attendRiderTxt.setText("已认证陪骑士");
							}else{
								attendRiderTxt.setText("未认证陪骑士");
							}
							
							ridingNumTxt.setText(certainUserModel.getArticleMemoNum() + "");
							joinNumTxt.setText(certainUserModel.getParticipantNum()+"");
							activityNumTxt.setText(certainUserModel.getActivityNum()+"");
								
							
							
							if (preferences.getInt("userId", -1) == certainUserModel
									.getUserId()) {
								personal_letterTxt.setVisibility(View.GONE);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						Toasts.show(PersonActivity.this, msg, 0);
					}
				});

		super.onStart();
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
		if (jsonObject.optBoolean("result")) {
			if (careFlagInt == 1) {
				careFlagInt = 2;
				careTxt.setBackgroundResource(R.drawable.corners_bg_orange);
				careTxt.setText("已关注");
				fansNumTxt.setText((++fansNum) + "");
			} else {
				careFlagInt = 1;
				careTxt.setBackgroundResource(R.drawable.corners_bg_blue);
				careTxt.setText("关注一下");
				fansNumTxt.setText((--fansNum) + "");
			}
		} else {
			Toasts.show(this, jsonObject.optString("message"), 0);
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		Toasts.show(this, msg, 0);
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
