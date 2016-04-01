package com.qizhi.qilaiqiqu.activity;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import com.qizhi.qilaiqiqu.utils.ConstantsUtil;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil.ImageInfo;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.squareup.picasso.Picasso;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

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
	// private LinearLayout isMelayout1;
	private LinearLayout isMelayout2;

	private LinearLayout backLayout;
	private LinearLayout chatSingleLayout;
	private LinearLayout appendLayout;
	private LinearLayout seeLinelayout;

	private ImageView likeImg;
	private ImageView cllectionImg;
	private ImageView shareImg;

	private Animation animation;

	private LinearLayout participantLayout;

	private String imageUrl = SystemUtil.IMGPHTH;

	private ImageCycleViewUtil mImageCycleView;

	List<ImageCycleViewUtil.ImageInfo> IClist = new ArrayList<ImageCycleViewUtil.ImageInfo>();

	private Activitys activity;

	private SharedPreferences sharedPreferences;
	public static int activityId;
	private int integral;

	private int markPointInt;
	private ImageView popup_mark0;
	private ImageView popup_mark1;
	private ImageView popup_mark2;
	private ImageView popup_mark3;
	private ImageView popup_mark4;
	private ImageView popup_mark5;
	private ImageView popup_mark6;
	private ImageView popup_mark7;
	private ImageView popup_mark8;
	private ImageView popup_mark9;
	private TextView popup_ok;
	private TextView markPointTxt;
	private TextView popup_cancel;

	private XUtilsUtil xUtilsUtil;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			setImageCycleViewUtil();
			super.handleMessage(msg);
		}

	};

	private ActivityModel model;

	private Tencent mTencent;

	private IUiListener baseUiListener; // 监听器

	private IWXAPI api;

	public static int userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_activity_details);
		initView();
		initEvent();
		getActivityData();
	}

	private void initView() {
		sharedPreferences = getSharedPreferences("userLogin",
				Context.MODE_PRIVATE);
		xUtilsUtil = new XUtilsUtil();
		userImageImg = (CircleImageViewUtil) findViewById(R.id.img_activityDetails_photo);

		participantLayout = (LinearLayout) findViewById(R.id.layout_activityDetails_participant);
		chatSingleLayout = (LinearLayout) findViewById(R.id.layout_activityDetails_chatSingle);
		seeLinelayout = (LinearLayout) findViewById(R.id.layout_activityDetails_seeLine);

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
		// isMelayout1 = (LinearLayout)
		// findViewById(R.id.layout_activityDetails_button1);
		isMelayout2 = (LinearLayout) findViewById(R.id.layout_activityDetails_button2);

		appendLayout = (LinearLayout) findViewById(R.id.layout_activityDetails_append);
		backLayout = (LinearLayout) findViewById(R.id.layout_activityDetailsActivity_back);
		shareImg = (ImageView) findViewById(R.id.img_activityDetails_share);

		activityId = getIntent().getIntExtra("activityId", -1);
		integral = getIntent().getIntExtra("integral", -1);

		mTencent = Tencent.createInstance(ConstantsUtil.APP_ID_TX,
				this.getApplicationContext());

		baseUiListener = new IUiListener() {

			@Override
			public void onError(UiError arg0) {

			}

			@Override
			public void onComplete(Object arg0) {

			}

			@Override
			public void onCancel() {

			}
		};
		api = WXAPIFactory.createWXAPI(this, ConstantsUtil.APP_ID_WX);

	}

	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			if (integral != -1) {
				showJPush(integral);
			}
		}
	}

	/**
	 * 打赏弹窗
	 * 
	 * @param view
	 *            popup所依附的布局
	 */
	private void showJPush(int i) {

		// 一个自定义的布局，作为显示的内容
		View v = LayoutInflater.from(this).inflate(R.layout.item_popup_jpush,
				null);
		markPointTxt = (TextView) v.findViewById(R.id.txt_JpushPopup_message);
		popup_cancel = (TextView) v.findViewById(R.id.txt_JpushPopup_cancel);
		LinearLayout quxiao = (LinearLayout) v.findViewById(R.id.quxiao);
		final PopupWindow popupWindow = new PopupWindow(v,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		popupWindow.setTouchable(true);

		popupWindow.setAnimationStyle(R.style.PopupAnimation);

		String userName = getIntent().getStringExtra("userName");
		int praiseNum = getIntent().getIntExtra("sumIntegral", -1);
		markPointTxt.setText(Html.fromHtml("骑友 " + "<font color='#6dbfed'>"
				+ userName + "</font>" + " 觉得您的活动写得不错哟!给您打赏了"
				+ "<font color='#ff0000'>" + integral + "</font>" + ",你现在的总积分是"
				+ "<font color='#ff0000'>" + praiseNum + "</font>" + "分"));

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				integral = -1;
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		quxiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				integral = -1;
			}
		});

		popup_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
				integral = -1;
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(ActivityDetailsActivity.this
				.findViewById(R.id.layout_ActivityDetailsActivity),
				Gravity.CENTER, 0, Gravity.CENTER);
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
		chatSingleLayout.setOnClickListener(this);
		seeLinelayout.setOnClickListener(this);
		shareImg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_activityDetailsActivity_back:
			finish();
			break;
		case R.id.txt_activityDetails_txt1:
			if (activity.getState().equals("ACTEND")) {
				showPopupWindow(v);
				break;
			}
			if (isMe == 1) {
				Toasts.show(this, "我发起的", 0);
			} else if (isMe == 2) {
				Toasts.show(this, "已报名", 0);
			}
			break;

		case R.id.txt_activityDetails_chat:
			if (activity.getState().equals("ACTEND")) {
				Intent intent = new Intent(this, ActivityDiscussActivity.class);
				intent.putExtra("activityId", activityId);
				startActivity(intent);
				break;
			}

			startActivity(new Intent(ActivityDetailsActivity.this,
					ChatActivity.class).putExtra("Group", "Group")
					.putExtra("username", activity.getImGroupId())
					.putExtra("groupName", activity.getActivityTitle()));
			break;

		case R.id.txt_activityDetails_txt3:
			if (sharedPreferences.getInt("userId", -1) == -1) {
				Toasts.show(this, "请登录", 0);
				// new SystemUtil().makeToast(this, "请登录");
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				this.finish();
				break;
			}

			if ("ACTEND".equals(activity.getState())) {
				if (activity.getUserId() == sharedPreferences.getInt("userId",
						-1)) {
					Intent intent = new Intent(this,
							ActivityDiscussActivity.class);
					intent.putExtra("activityId", activityId);
					startActivity(intent);
				}
				break;
			}

			showPopupWindow2(v);
			break;

		case R.id.layout_activityDetails_button1:
			/*
			 * startActivity(new Intent(ActivityDetailsActivity.this,
			 * RidingDetailsActivity.class).putExtra("", articleMemoId1));
			 */
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
				this.finish();
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
				this.finish();
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
										activity.setUserPraised(true);
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

		case R.id.layout_activityDetails_chatSingle:

			if (sharedPreferences.getInt("userId", -1) != -1) {
				startActivity(new Intent(this, ChatSingleActivity.class)
						.putExtra("username", activity.getImUserName())
						.putExtra("otherUserName", activity.getUserName())
						.putExtra("otherUserImage", activity.getUserName())
						.putExtra("otherUserId", activity.getUserId()));
			} else {
				Toasts.show(ActivityDetailsActivity.this, "请登录", 0);
				Intent intent1 = new Intent(ActivityDetailsActivity.this,
						LoginActivity.class);
				startActivity(intent1);
				this.finish();
			}

			break;

		case R.id.layout_activityDetails_seeLine:
			startActivity(new Intent(this, ShowLineActivity.class).putExtra(
					"LanInfo", activity.getLanInfo()));
			System.err.println("activity.getLanInfo()" + activity.getLanInfo());
			break;
		case R.id.img_activityDetails_share:
			showPopupWindow3(v);
			break;
		case R.id.txt_activityDetails_articleMemo1:
			if (sharedPreferences.getInt("userId", -1) != -1
					&& articleMemoId1 != 0) {
				Intent intent2 = new Intent(ActivityDetailsActivity.this,
						RidingDetailsActivity.class);
				intent2.putExtra("articleId", articleMemoId1);
				startActivity(intent2);
			} else if (sharedPreferences.getInt("userId", -1) == -1) {
				Toasts.show(ActivityDetailsActivity.this, "请登录", 0);
				Intent intent1 = new Intent(ActivityDetailsActivity.this,
						LoginActivity.class);
				startActivity(intent1);
				this.finish();
			}
			break;
		case R.id.txt_activityDetails_articleMemo2:
			if (sharedPreferences.getInt("userId", -1) != -1
					&& articleMemoId2 != 0) {
				Intent intent2 = new Intent(ActivityDetailsActivity.this,
						RidingDetailsActivity.class);
				intent2.putExtra("articleId", articleMemoId2);
				startActivity(intent2);
			} else if (sharedPreferences.getInt("userId", -1) == -1) {
				Toasts.show(ActivityDetailsActivity.this, "请登录", 0);
				Intent intent1 = new Intent(ActivityDetailsActivity.this,
						LoginActivity.class);
				startActivity(intent1);
				this.finish();
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
		confirmBtn.setText("确定");
		cancelBtn.setText("取消");
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
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getBoolean("result")) {
				Gson gson = new Gson();
				Type type = new TypeToken<ActivityModel>() {
				}.getType();

				model = gson.fromJson(jsonObject.getJSONObject("data")
						.toString(), type);

				activity = model.getActivitys();
				userId = activity.getUserId();
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
			SystemUtil.Imagexutils(SystemUtil.UrlSize(activity.getUserImage()),
					userImageImg, ActivityDetailsActivity.this);

			/*
			 * Picasso.with(ActivityDetailsActivity.this) .load(imageUrl+
			 * SystemUtil.UrlSize(activity.getUserImage())).into(userImageImg);
			 */
		}

		if (activity.getActivityImage() == null
				|| "null".equals(activity.getActivityImage())
				|| "".equals(activity.getActivityImage())) {
			IClist.add(new ImageCycleViewUtil.ImageInfo(SystemUtil
					.UrlSize(activity.getDefaultImage()), null, null, null));
		} else {

			String[] split = activity.getActivityImage().split(",");

			for (int i = 0; i < split.length; i++) {
				IClist.add(new ImageCycleViewUtil.ImageInfo(SystemUtil
						.UrlSize(split[i]), null, null, null));

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
		if (activity.getMileage() == null || activity.getMileage() == "") {
			findViewById(R.id.layout_activityDetails_rideLine).setVisibility(
					View.GONE);
			findViewById(R.id.layout_activityDetails_rideLine2).setVisibility(
					View.GONE);
		} else {
			mileageTxt.setText(new DecimalFormat("0.00").format(Double
					.parseDouble(activity.getMileage())) + "KM");
		}
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
			dianTxt.setVisibility(View.GONE);
		} else {
			size = 6;
		}
		for (int i = 0; i < size; i++) {
			CircleImageViewUtil imageView = new CircleImageViewUtil(this);

			linearLayout.addView(imageView);
			if (model.getParticipantList().get(i).getUserImage()
					.indexOf("http") != -1) {
				Picasso.with(ActivityDetailsActivity.this)
						.load(model.getParticipantList().get(i).getUserImage())
						.resize(dp2px(ActivityDetailsActivity.this, 45f),
								dp2px(ActivityDetailsActivity.this, 45f))
						.into(imageView);
			} else {
				Picasso.with(ActivityDetailsActivity.this)
						.load(imageUrl
								+ model.getParticipantList().get(i)
										.getUserImage())
						.resize(dp2px(ActivityDetailsActivity.this, 45f),
								dp2px(ActivityDetailsActivity.this, 45f))
						.into(imageView);
			}
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
				isSignTxt3.setText("立即报名");
			}
		}

		if (sharedPreferences.getInt("userId", -1) == activity.getUserId()) {
			if ("ACTEND".equals(activity.getState())) {
				isMelayout2.setVisibility(View.VISIBLE);
				isSignTxt3.setText("查看评论");
			} else if ("UNDERWAY".equals(activity.getState())) {
				isMelayout2.setVisibility(View.VISIBLE);
				isSignTxt3.setText("活动正在进行中");
			}
		} else {
			if (!activity.isInvolved()) {
				if ("ACTEND".equals(activity.getState())) {
					isMelayout2.setVisibility(View.VISIBLE);
					isSignTxt3.setText("活动已结束");
				} else if ("UNDERWAY".equals(activity.getState())) {
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
						SystemUtil.Imagexutils(imageInfo.image.toString(),
								imageView, ActivityDetailsActivity.this);
						/*
						 * Picasso.with(ActivityDetailsActivity.this)
						 * .load(imageInfo.image.toString()) .into(imageView);
						 */
						// imageView.setImageResource(R.drawable.demo);

						return imageView;

					}
				});
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {

	}

	/**
	 * 弹窗
	 * 
	 * @param view
	 *            popup所依附的布局
	 */
	private void showPopupWindow(View view) {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.item_popup_grade, null);

		popup_mark0 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark0);
		popup_mark1 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark1);
		popup_mark2 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark2);
		popup_mark3 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark3);
		popup_mark4 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark4);
		popup_mark5 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark5);
		popup_mark6 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark6);
		popup_mark7 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark7);
		popup_mark8 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark8);
		popup_mark9 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark9);
		popup_ok = (TextView) contentView.findViewById(R.id.txt_gradePopup_ok);
		markPointTxt = (TextView) contentView
				.findViewById(R.id.txt_gradePopup_markPoint);
		popup_cancel = (TextView) contentView
				.findViewById(R.id.txt_gradePopup_cancel);
		selectMark();

		final PopupWindow popupWindow = new PopupWindow(contentView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

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

		popup_mark0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				markPointInt = 1;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				markPointInt = 2;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				markPointInt = 3;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				markPointInt = 4;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				popup_mark4.setImageResource(R.drawable.award_yellow);
				markPointInt = 5;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				popup_mark4.setImageResource(R.drawable.award_yellow);
				popup_mark5.setImageResource(R.drawable.award_yellow);
				markPointInt = 6;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				popup_mark4.setImageResource(R.drawable.award_yellow);
				popup_mark5.setImageResource(R.drawable.award_yellow);
				popup_mark6.setImageResource(R.drawable.award_yellow);
				markPointInt = 7;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				popup_mark4.setImageResource(R.drawable.award_yellow);
				popup_mark5.setImageResource(R.drawable.award_yellow);
				popup_mark6.setImageResource(R.drawable.award_yellow);
				popup_mark7.setImageResource(R.drawable.award_yellow);
				markPointInt = 8;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				popup_mark4.setImageResource(R.drawable.award_yellow);
				popup_mark5.setImageResource(R.drawable.award_yellow);
				popup_mark6.setImageResource(R.drawable.award_yellow);
				popup_mark7.setImageResource(R.drawable.award_yellow);
				popup_mark8.setImageResource(R.drawable.award_yellow);
				markPointInt = 9;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				popup_mark4.setImageResource(R.drawable.award_yellow);
				popup_mark5.setImageResource(R.drawable.award_yellow);
				popup_mark6.setImageResource(R.drawable.award_yellow);
				popup_mark7.setImageResource(R.drawable.award_yellow);
				popup_mark8.setImageResource(R.drawable.award_yellow);
				popup_mark9.setImageResource(R.drawable.award_yellow);
				markPointInt = 10;
				markPointTxt.setText(markPointInt + "分");
			}
		});

		popup_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				rewardIntegral(markPointInt);
				popupWindow.dismiss();
			}
		});

		popup_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Toasts.show(ActivityDetailsActivity.this, "您未打赏积分", 0);
				popupWindow.dismiss();
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

	}

	/**
	 * 设置所有评分图片暗色
	 */
	public void selectMark() {
		popup_mark0.setImageResource(R.drawable.award_gray);
		popup_mark1.setImageResource(R.drawable.award_gray);
		popup_mark2.setImageResource(R.drawable.award_gray);
		popup_mark3.setImageResource(R.drawable.award_gray);
		popup_mark4.setImageResource(R.drawable.award_gray);
		popup_mark5.setImageResource(R.drawable.award_gray);
		popup_mark6.setImageResource(R.drawable.award_gray);
		popup_mark7.setImageResource(R.drawable.award_gray);
		popup_mark8.setImageResource(R.drawable.award_gray);
		popup_mark9.setImageResource(R.drawable.award_gray);
		markPointInt = 0;
		markPointTxt.setText(markPointInt + "分");
	}

	public void rewardIntegral(final int markPointInt) {
		if (sharedPreferences.getInt("userId", -1) != -1) {
			RequestParams params = new RequestParams("UTF-8");
			params.addBodyParameter("userId",
					sharedPreferences.getInt("userId", -1) + "");
			params.addBodyParameter("activityId", activityId + "");
			params.addBodyParameter("integral", markPointInt + "");
			params.addBodyParameter("uniqueKey",
					sharedPreferences.getString("uniqueKey", null));
			xUtilsUtil.httpPost("mobile/activity/activityReward.html", params,
					new CallBackPost() {

						@Override
						public void onMySuccess(
								ResponseInfo<String> responseInfo) {
							String s = responseInfo.result;
							JSONObject jsonObject = null;
							try {
								jsonObject = new JSONObject(s);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							if (jsonObject.optBoolean("result")) {

								Toasts.show(ActivityDetailsActivity.this,
										"您打赏了" + markPointInt + "分!", 0);
							} else {
								Toasts.show(ActivityDetailsActivity.this,
										jsonObject.optString("message"), 0);
							}
						}

						@Override
						public void onMyFailure(HttpException error, String msg) {

						}
					});
		} else {
			Toasts.show(this, "请登录", 0);
			startActivity(new Intent(this, LoginActivity.class));
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	private void showPopupWindow3(View view) {

		// 一个自定义的布局，作为显示的内容
		View mview = LayoutInflater.from(this).inflate(R.layout.share, null);

		LinearLayout qq = (LinearLayout) mview.findViewById(R.id.qq);
		LinearLayout wx = (LinearLayout) mview.findViewById(R.id.wx);
		LinearLayout pyq = (LinearLayout) mview.findViewById(R.id.pyq);
		LinearLayout wb = (LinearLayout) mview.findViewById(R.id.wb);
		LinearLayout qx = (LinearLayout) mview.findViewById(R.id.qx);

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

		qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onClickQQShare();
				popupWindow.dismiss();
			}
		});

		wx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onClickWXShare();
				popupWindow.dismiss();
			}
		});
		pyq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onClickWXPYQShare();
				popupWindow.dismiss();
			}
		});
		wb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});
		qx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 50);

	}

	// 分享到QQ与QQ空间
	private void onClickQQShare() {
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, model.getActivitys()
				.getActivityTitle());
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY,
				"http://www.weride.com.cn/page/activityDetail.html?activityId="
						+ model.getActivitys().getActivityId());
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, SystemUtil.IMGPHTH
				+ model.getActivitys().getDefaultImage());
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, SystemUtil.IMGPHTH
				+ model.getActivitys().getDefaultImage());
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "骑来骑去");
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, model.getActivitys()
				.getActivityTitle());// 必填
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, model.getActivitys()
				.getActivityMemo());// 选填
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
				"http://www.weride.com.cn/page/activityDetail.html?activityId="
						+ model.getActivitys().getActivityId());// 必填
		params.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, SystemUtil.IMGPHTH
				+ model.getActivitys().getDefaultImage());
		mTencent.shareToQQ(ActivityDetailsActivity.this, params, baseUiListener);
	}

	// 分享到微信
	private void onClickWXShare() {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = "http://www.weride.com.cn/page/activityDetail.html?activityId="
				+ model.getActivitys().getActivityId();
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = model.getActivitys().getActivityTitle();
		msg.description = model.getActivitys().getActivityMemo();

		try {
			Bitmap bmp = SystemUtil.compressImageFromFile(SystemUtil.IMGPHTH
					+ model.getActivitys().getDefaultImage(), 300);
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
			bmp.recycle();
			msg.thumbData = Bitmap2Bytes(thumbBmp);
			// msg.setThumbImage(thumbBmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("图文链接");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	// 分享到微信
	private void onClickWXPYQShare() {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = "http://www.weride.com.cn/page/activityDetail.html?activityId="
				+ model.getActivitys().getActivityId();
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = model.getActivitys().getActivityTitle();
		msg.description = model.getActivitys().getActivityMemo();

		try {
			Bitmap bmp = SystemUtil.compressImageFromFile(SystemUtil.IMGPHTH
					+ model.getActivitys().getDefaultImage(), 300);
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
			bmp.recycle();
			msg.thumbData = Bitmap2Bytes(thumbBmp);
			// msg.setThumbImage(thumbBmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("图文链接");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);
	}

	/**
	 * 构造一个用于请求的唯一标识
	 * 
	 * @param type
	 *            分享的内容类型
	 * @return
	 */
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	/*
	 * // sina微博 private void onClickWBShare() { }
	 */

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
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
