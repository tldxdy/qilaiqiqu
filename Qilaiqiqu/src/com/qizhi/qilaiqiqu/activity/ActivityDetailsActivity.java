package com.qizhi.qilaiqiqu.activity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.ActivityModel;
import com.qizhi.qilaiqiqu.model.ActivityModel.Activitys;
import com.qizhi.qilaiqiqu.utils.CircleImageViewUtil;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil.ImageInfo;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.squareup.picasso.Picasso;

public class ActivityDetailsActivity extends HuanxinLogOutActivity implements
		CallBackPost, OnClickListener {

	private CircleImageViewUtil userImageImg;

	// private CircleImageViewUtil participant1;

	private int isMe = 2;// 1为自己，2为未报名的人，3为已报名

	private int articleMemoId1;
	private int articleMemoId2;

	private TextView likeTxt;
	private TextView mileageTxt;
	private TextView scanNumTxt;
	private TextView userNameTxt;
	private TextView durationTxt;
	private TextView startDateTxt;
	private TextView activityMemoTxt;
	private TextView articleMemoTxt1;
	private TextView articleMemoTxt2;
	private TextView activityTitleTxt;
	private TextView participantCountTxt;
	private TextView moneyTxt;

	private TextView dianTxt;

	private TextView likeNumTxt;
	private TextView isSignTxt1;
	private TextView isSignTxt2;
	private TextView isSignTxt3;
	private LinearLayout isMelayout1;
	private LinearLayout isMelayout2;

	private LinearLayout backLayout;

	private LinearLayout appendLayout;

	private ImageView likeImg;
	private ImageView cllectionImg;

	private Animation animation;

	private LinearLayout participantLayout;

	private String imageUrl = SystemUtil.IMGPHTH;

	private ImageCycleViewUtil mImageCycleView;

	List<ImageCycleViewUtil.ImageInfo> IClist = new ArrayList<ImageCycleViewUtil.ImageInfo>();

	private Activitys activity;

	private SharedPreferences sharedPreferences;
	private int activityId;
	private int activity_userId;
	private String activity_state;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			setImageCycleViewUtil();
			super.handleMessage(msg);
		}

	};

	private ActivityModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_activity_details);
		initView();
		getActivityData();
		initEvent();
	}

	private void initView() {
		sharedPreferences = getSharedPreferences("userLogin",
				Context.MODE_PRIVATE);

		userImageImg = (CircleImageViewUtil) findViewById(R.id.img_activityDetails_photo);

		participantLayout = (LinearLayout) findViewById(R.id.layout_activityDetails_participant);

		dianTxt = (TextView) findViewById(R.id.txt_activityDetails_dian);

		likeTxt = (TextView) findViewById(R.id.txt_activityDetails_like);
		scanNumTxt = (TextView) findViewById(R.id.txt_activityDetails_scanNum);
		mileageTxt = (TextView) findViewById(R.id.txt_activityDetails_mileage);
		likeNumTxt = (TextView) findViewById(R.id.txt_activityDetails_animation);
		userNameTxt = (TextView) findViewById(R.id.txt_activityDetails_userName);
		durationTxt = (TextView) findViewById(R.id.txt_activityDetails_duration);
		startDateTxt = (TextView) findViewById(R.id.txt_activityDetails_startDate);
		articleMemoTxt1 = (TextView) findViewById(R.id.txt_activityDetails_articleMemo1);
		articleMemoTxt2 = (TextView) findViewById(R.id.txt_activityDetails_articleMemo2);
		activityMemoTxt = (TextView) findViewById(R.id.txt_activityDetails_activityMemo);
		activityTitleTxt = (TextView) findViewById(R.id.txt_activityDetails_activityTitle);
		participantCountTxt = (TextView) findViewById(R.id.txt_activityDetails_participantCount);
		moneyTxt = (TextView) findViewById(R.id.txt_activityDetails_money);

		likeImg = (ImageView) findViewById(R.id.img_activityDetails_like);
		cllectionImg = (ImageView) findViewById(R.id.img_activityDetails_cllection);

		animation = AnimationUtils
				.loadAnimation(this, R.anim.applaud_animation);

		mImageCycleView = (ImageCycleViewUtil) findViewById(R.id.icc_activityDetails);

		isSignTxt1 = (TextView) findViewById(R.id.txt_activityDetails_txt1);
		isSignTxt2 = (TextView) findViewById(R.id.txt_activityDetails_chat);
		isSignTxt3 = (TextView) findViewById(R.id.txt_activityDetails_txt3);
		isMelayout1 = (LinearLayout) findViewById(R.id.layout_activityDetails_button1);
		isMelayout2 = (LinearLayout) findViewById(R.id.layout_activityDetails_button2);

		appendLayout = (LinearLayout) findViewById(R.id.layout_activityDetails_append);
		backLayout = (LinearLayout) findViewById(R.id.layout_activityDetailsActivity_back);

		activityId = getIntent().getIntExtra("activityId", -1);
		activity_userId = getIntent().getIntExtra("userId", -1);
		if (activity_userId != -1) {
			activity_state = getIntent().getStringExtra("state");
		}
	}

	private void initEvent() {
		likeImg.setOnClickListener(this);
		isSignTxt1.setOnClickListener(this);
		isSignTxt2.setOnClickListener(this);
		isSignTxt3.setOnClickListener(this);
		appendLayout.setOnClickListener(this);
		articleMemoTxt1.setOnClickListener(this);
		articleMemoTxt2.setOnClickListener(this);
		backLayout.setOnClickListener(this);
		cllectionImg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_activityDetailsActivity_back:
			finish();
			break;
		case R.id.txt_activityDetails_txt1:
			if (activity.getState().equals("ACTEND")) {
				Toasts.show(this, "打分", 0);
				break;
			}
			if (isMe == 1) {
				Toasts.show(this, "我发起的", 0);
				// new SystemUtil().makeToast(this, "我发起的");
			} else if (isMe == 2) {
				Toasts.show(this, "已报名", 0);
				// new SystemUtil().makeToast(this, "已报名");
			}
			break;

		case R.id.txt_activityDetails_chat:
			startActivity(new Intent(ActivityDetailsActivity.this,
					ChatActivity.class).putExtra("Group", "Group")
					.putExtra("username", activity.getImGroupId())
					.putExtra("activityId", activityId)
					.putExtra("groupName", activity.getActivityTitle()));

			break;

		case R.id.txt_activityDetails_txt3:
			if (sharedPreferences.getInt("userId", -1) == -1) {
				Toasts.show(this, "请登录", 0);
				// new SystemUtil().makeToast(this, "请登录");
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				break;
			}

			showPopupWindow2(v);
			break;

		case R.id.layout_activityDetails_button1:
			startActivity(new Intent(ActivityDetailsActivity.this,
					RidingDetailsActivity.class).putExtra("", articleMemoId1));
			break;

		case R.id.layout_activityDetails_append:
			Intent intent = new Intent();
			intent.setClass(ActivityDetailsActivity.this, AppendActivity.class);
			intent.putExtra("append", (Serializable) model.getParticipantList());
			startActivity(intent);
			break;
		case R.id.img_activityDetails_cllection:
			if (sharedPreferences.getInt("userId", -1) == -1) {
				Toasts.show(this, "请登录", 0);
				// new SystemUtil().makeToast(this, "请登录");
				Intent intent2 = new Intent(this, LoginActivity.class);
				startActivity(intent2);
				break;
			}

			if (activity.isUserCollected()) {
				RequestParams params = new RequestParams("UTF-8");
				params.addQueryStringParameter("activityId", activityId + "");
				params.addQueryStringParameter("userId",
						sharedPreferences.getInt("userId", -1) + "");
				params.addQueryStringParameter("uniqueKey",
						sharedPreferences.getString("uniqueKey", null));
				new XUtilsUtil().httpPost(
						"mobile/activity/deleteActivityCollect.html", params,
						new CallBackPost() {

							@Override
							public void onMySuccess(
									ResponseInfo<String> responseInfo) {
								String result = responseInfo.result;
								JSONObject object;
								try {
									object = new JSONObject(result);
									if (object.getBoolean("result")) {
										cllectionImg
												.setImageResource(R.drawable.cllection_unchosen);
										activity.setUserCollected(false);
									}
								} catch (JSONException e) {
									e.printStackTrace();

								}
							}

							@Override
							public void onMyFailure(HttpException error,
									String msg) {

							}
						});
			} else {
				RequestParams params = new RequestParams("UTF-8");
				params.addQueryStringParameter("activityId", activityId + "");
				params.addQueryStringParameter("userId",
						sharedPreferences.getInt("userId", -1) + "");
				params.addQueryStringParameter("uniqueKey",
						sharedPreferences.getString("uniqueKey", null));
				new XUtilsUtil().httpPost(
						"mobile/activity/appendActivityCollect.html", params,
						new CallBackPost() {

							@Override
							public void onMySuccess(
									ResponseInfo<String> responseInfo) {
								String result = responseInfo.result;
								JSONObject object;
								try {
									object = new JSONObject(result);
									if (object.getBoolean("result")) {
										cllectionImg
												.setImageResource(R.drawable.clection_chosen);
										activity.setUserCollected(true);
									}
								} catch (JSONException e) {
									e.printStackTrace();

								}
							}

							@Override
							public void onMyFailure(HttpException error,
									String msg) {

							}
						});
			}

			break;
		case R.id.img_activityDetails_like:
			if (sharedPreferences.getInt("userId", -1) == -1) {
				new SystemUtil().makeToast(this, "请登录");
				Intent intent3 = new Intent(this, LoginActivity.class);
				startActivity(intent3);
				break;
			}
			if (activity.isUserPraised()) {
				RequestParams params = new RequestParams("UTF-8");
				params.addQueryStringParameter("activityId", activityId + "");
				params.addQueryStringParameter("userId",
						sharedPreferences.getInt("userId", -1) + "");
				params.addQueryStringParameter("uniqueKey",
						sharedPreferences.getString("uniqueKey", null));
				new XUtilsUtil().httpPost(
						"mobile/activity/cancelPraiseActivity.html", params,
						new CallBackPost() {

							@Override
							public void onMySuccess(
									ResponseInfo<String> responseInfo) {
								String result = responseInfo.result;
								JSONObject object;
								try {
									object = new JSONObject(result);
									if (object.getBoolean("result")) {
										likeImg.setImageResource(R.drawable.admire_unchosen);
										likeTxt.setText(object.getInt("data")
												+ "");
										activity.setUserPraised(false);
									}
								} catch (JSONException e) {
									e.printStackTrace();

								}
							}

							@Override
							public void onMyFailure(HttpException error,
									String msg) {

							}
						});
			} else {
				RequestParams params = new RequestParams("UTF-8");
				params.addQueryStringParameter("activityId", activityId + "");
				params.addQueryStringParameter("userId",
						sharedPreferences.getInt("userId", -1) + "");
				params.addQueryStringParameter("uniqueKey",
						sharedPreferences.getString("uniqueKey", null));
				new XUtilsUtil().httpPost(

				"mobile/activity/praiseActivity.html", params,
						new CallBackPost() {
							@Override
							public void onMySuccess(
									ResponseInfo<String> responseInfo) {
								String result = responseInfo.result;
								try {
									JSONObject object = new JSONObject(result);
									if (object.getBoolean("result")) {
										likeNumTxt.setVisibility(View.VISIBLE);
										likeNumTxt.startAnimation(animation);
										new Handler().postDelayed(
												new Runnable() {
													public void run() {
														likeNumTxt
																.setVisibility(View.GONE);
													}
												}, 1000);
										likeImg.setImageResource(R.drawable.admire_chose);
										likeTxt.setText(object.getInt("data")
												+ "");
										System.out.println(activity
												.isUserPraised());
										activity.setUserPraised(true);
										System.out.println(activity
												.isUserPraised());
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onMyFailure(HttpException error,
									String msg) {

								/*
								 * Toast.makeText(ActivityDetailsActivity.this,
								 * activity.isUserPraised() + "", 0) .show();
								 */
							}
						});
			}
			break;

		default:
			break;
		}
	}

	private void isApply() {
		if (isMe == 2) {

			RequestParams params = new RequestParams("UTF-8");
			params.addQueryStringParameter("activityId", activityId + "");
			params.addQueryStringParameter("userId",
					sharedPreferences.getInt("userId", -1) + "");
			params.addQueryStringParameter("uniqueKey",
					sharedPreferences.getString("uniqueKey", null));
			new XUtilsUtil().httpPost(
					"mobile/participant/appendActivityParticipant.html",
					params, new CallBackPost() {

						@Override
						public void onMySuccess(
								ResponseInfo<String> responseInfo) {
							String result = responseInfo.result;
							JSONObject object;
							try {
								object = new JSONObject(result);
								if (object.getBoolean("result")) {
									isSignTxt1.setText("已报名");
									isSignTxt2.setText("去聊天");
									isMelayout2.setVisibility(View.GONE);
									Toasts.show(ActivityDetailsActivity.this,
											"已成功报名", 0);
									/*
									 * new SystemUtil().makeToast(
									 * ActivityDetailsActivity.this, "已成功报名");
									 */
								} else {
									Toasts.show(ActivityDetailsActivity.this,
											object.getString("message"), 0);
									/*
									 * new SystemUtil().makeToast(
									 * ActivityDetailsActivity.this,
									 * object.getString("message"));
									 */
								}
							} catch (JSONException e) {
								e.printStackTrace();

							}
						}

						@Override
						public void onMyFailure(HttpException error, String msg) {
							new SystemUtil().makeToast(
									ActivityDetailsActivity.this, msg);
						}
					});
		}
	}

	private void showPopupWindow2(View view) {

		// 一个自定义的布局，作为显示的内容
		View mview = LayoutInflater.from(this).inflate(
				R.layout.popup_delete_releaseactivity, null);

		TextView textView = (TextView) mview
				.findViewById(R.id.txt_dialog_box_cancel);
		Button confirmBtn = (Button) mview
				.findViewById(R.id.btn_dialog_box_confirm);
		Button cancelBtn = (Button) mview
				.findViewById(R.id.btn_dialog_box_cancel);

		textView.setText("你确定报名，您将自动加入活动群组？");
		final PopupWindow popupWindow = new PopupWindow(mview,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		popupWindow.setTouchable(true);

		popupWindow.setAnimationStyle(R.style.PopupAnimation);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				isApply();
				popupWindow.dismiss();
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

	}

	private void getActivityData() {
		RequestParams params = new RequestParams("UTF-8");
		params.addQueryStringParameter("activityId", activityId + "");
		params.addQueryStringParameter("location", "");
		params.addQueryStringParameter("uniqueKey",
				sharedPreferences.getString("uniqueKey", null));

		new XUtilsUtil().httpPost("common/queryActivityByActivityId.html",
				params, this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		String result = responseInfo.result.toString();
		System.out.println(result);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getBoolean("result")) {
				Gson gson = new Gson();
				Type type = new TypeToken<ActivityModel>() {
				}.getType();

				model = gson.fromJson(jsonObject.getJSONObject("data")
						.toString(), type);

				activity = model.getActivitys();

				if (activity != null) {

					setView(model);
					// handler = new Handler();

					Message m = handler.obtainMessage();
					handler.sendMessage(m);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void setView(final ActivityModel model) {

		if (activity.getUserImage() == null
				|| "null".equals(activity.getUserImage())
				|| "".equals(activity.getUserImage().trim())) {
			Picasso.with(ActivityDetailsActivity.this)
					.load(R.drawable.bitmap_homepage).into(userImageImg);
		} else {
			Picasso.with(ActivityDetailsActivity.this)
					.load(imageUrl + activity.getUserImage().split("@")[0])
					.into(userImageImg);
		}

		if (activity.getActivityImage() == null
				|| "null".equals(activity.getActivityImage())
				|| "".equals(activity.getActivityImage())) {
			IClist.add(new ImageCycleViewUtil.ImageInfo(SystemUtil.IMGPHTH
					+ activity.getDefaultImage(), null, null, null));
		} else {

			String[] split = activity.getActivityImage().split(",");

			for (int i = 0; i < split.length; i++) {
				IClist.add(new ImageCycleViewUtil.ImageInfo(SystemUtil.IMGPHTH
						+ split[i].split("@")[0], null, null, null));
			}
		}
		if (activity.isUserPraised()) {
			likeImg.setImageResource(R.drawable.admire_chose);
		} else {
			likeImg.setImageResource(R.drawable.admire_unchosen);
		}
		if (activity.isUserCollected()) {
			cllectionImg.setImageResource(R.drawable.clection_chosen);
		} else {
			cllectionImg.setImageResource(R.drawable.cllection_unchosen);
		}

		likeTxt.setText(activity.getPraiseNum() + "");
		scanNumTxt.setText(activity.getScanNum() + "次浏览");
		userNameTxt.setText(activity.getUserName());
		activityTitleTxt.setText(activity.getActivityTitle());
		mileageTxt.setText(activity.getMileage());
		int hour = activity.getDuration() / 60;
		if (hour / 24 > 0) {
			int days = hour / 24;
			hour = hour % 24;
			if (hour == 0) {
				durationTxt.setText(days + "天");
			} else {
				durationTxt.setText(days + "天" + hour + "小时");
			}
		} else {
			durationTxt.setText(hour + "小时");
		}
		if (activity.getOutlay() == null || "".equals(activity.getOutlay())
				|| "免费".equals(activity.getOutlay())) {
			moneyTxt.setText("免费");
		} else {
			moneyTxt.setText(activity.getOutlay() + "元");
		}

		startDateTxt.setText(activity.getStartDate().substring(0,
				activity.getStartDate().length() - 3));
		activityMemoTxt.setText(activity.getActivityMemo());
		participantCountTxt.setText(model.getParticipantCount() + "人");
		if (model.getParticipantCount() == 0) {
			dianTxt.setVisibility(View.GONE);
		} else {
			dianTxt.setVisibility(View.VISIBLE);
		}

		for (int i = 0; i < model.getArticleMemoList().size() - 1; i++) {
			articleMemoTxt1.setText(model.getArticleMemoList().get(i)
					.getTitle());
			articleMemoId1 = model.getArticleMemoList().get(i).getArticleId();
			articleMemoTxt2.setText(model.getArticleMemoList().get(i + 1)
					.getTitle());
			articleMemoId2 = model.getArticleMemoList().get(i + 1)
					.getArticleId();
		}

		LinearLayout linearLayout = participantLayout;
		int size;
		if (model.getParticipantList().size() < 7) {
			size = model.getParticipantList().size();
		} else {
			size = 6;
		}
		for (int i = 0; i < size; i++) {
			CircleImageViewUtil imageView = new CircleImageViewUtil(this);

			linearLayout.addView(imageView);

			Picasso.with(ActivityDetailsActivity.this)
					.load(imageUrl
							+ model.getParticipantList().get(i).getUserImage())
					.resize(dp2px(ActivityDetailsActivity.this, 45f),
							dp2px(ActivityDetailsActivity.this, 45f))
					.into(imageView);
		}

		if (sharedPreferences.getInt("userId", -1) == activity.getUserId()) {
			isMe = 1;
			isSignTxt1.setText("我发起的");
			isSignTxt2.setText("去聊天");
			isMelayout2.setVisibility(View.GONE);
		} else {
			if (activity.isInvolved()) {
				isMe = 3;
				isSignTxt1.setText("已报名");
				isSignTxt2.setText("去聊天");
				isMelayout2.setVisibility(View.GONE);
			} else {
				isMelayout2.setVisibility(View.VISIBLE);
			}
		}

		if (sharedPreferences.getInt("userId", -1) == activity.getUserId()) {
			if ("ACTEND".equals(activity.getState())) {
				isMelayout2.setVisibility(View.VISIBLE);
				isSignTxt3.setText("活动已结束");
			}
		} else {
			if (!activity.isInvolved()) {
				if ("ACTEND".equals(activity.getState())) {
					isMelayout2.setVisibility(View.VISIBLE);
					isSignTxt3.setText("活动已结束");
				} else if (!"ACTIN".equals(activity.getState())) {
					isMelayout2.setVisibility(View.VISIBLE);
					isSignTxt3.setText("活动正在进行中");
				}
			} else {
				if ("ACTEND".equals(activity.getState())) {
					isMe = 3;
					isSignTxt1.setText("打分");
					isSignTxt2.setText("评论");
					isMelayout2.setVisibility(View.GONE);
				}
			}
		}
	}

	/**
	 * dp转px
	 * 
	 * @param context
	 * @param val
	 * @return
	 */
	public static int dp2px(Context context, float dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, context.getResources().getDisplayMetrics());
	}

	private void setImageCycleViewUtil() {

		mImageCycleView.setCycleDelayed(3000);

		mImageCycleView
				.setOnPageClickListener(new ImageCycleViewUtil.OnPageClickListener() {

					@Override
					public void onClick(View imageView, ImageInfo imageInfo) {

					}
				});

		mImageCycleView.loadData(IClist,
				new ImageCycleViewUtil.LoadImageCallBack() {
					@Override
					public ImageView loadAndDisplay(
							ImageCycleViewUtil.ImageInfo imageInfo) {

						ImageView imageView = new ImageView(
								ActivityDetailsActivity.this);

						Picasso.with(ActivityDetailsActivity.this)
								.load(imageInfo.image.toString())
								.into(imageView);
						// imageView.setImageResource(R.drawable.demo);

						return imageView;

					}
				});
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {

	}

	@Override
	protected void onResume() {
		getActivityData();
		super.onResume();
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

}
