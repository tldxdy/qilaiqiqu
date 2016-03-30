package com.qizhi.qilaiqiqu.activity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.RiderDetailsAdapter;
import com.qizhi.qilaiqiqu.model.RiderDetailsModel;
import com.qizhi.qilaiqiqu.model.RiderDetailsModel.AttendRider;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil.ImageInfo;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

public class RiderDetailsActivity extends Activity implements OnClickListener {

	private String riderId;

	private ListView commentList;
	private RiderDetailsAdapter riderDetailsAdapter;

	private LinearLayout hearderViewLayout;
	private LinearLayout backLayout;
	private LinearLayout phoneLayout;
	private LinearLayout buttonLayout;

	private TextView button1;
	private TextView button2;
	private TextView riderNameTxt;

	private TextView memoTxt;
	private TextView priceTxt;
	private TextView phoneTxt;
	private TextView numberTxt;
	private TextView mileageTxt;
	private TextView addressTxt;
	private TextView bikeTypeTxt;

	private TextView mondayTxt;
	private TextView tuesdayTxt;
	private TextView wednesdayTxt;
	private TextView thursdayTxt;
	private TextView fridayTxt;
	private TextView saturdayTxt;
	private TextView sundayTxt;

	private XUtilsUtil httpUtils;
	private SharedPreferences preferences;

	private LinearLayout timeLayout;

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

	private String monday[] = { "0", "0", "0", "0", "0", "0", "0", "0", "0",
			"0", "0", "0", "0", "0" };
	private String tuesday[] = { "0", "0", "0", "0", "0", "0", "0", "0", "0",
			"0", "0", "0", "0", "0" };
	private String wednesday[] = { "0", "0", "0", "0", "0", "0", "0", "0", "0",
			"0", "0", "0", "0", "0" };
	private String thursday[] = { "0", "0", "0", "0", "0", "0", "0", "0", "0",
			"0", "0", "0", "0", "0" };
	private String friday[] = { "0", "0", "0", "0", "0", "0", "0", "0", "0",
			"0", "0", "0", "0", "0" };
	private String saturday[] = { "0", "0", "0", "0", "0", "0", "0", "0", "0",
			"0", "0", "0", "0", "0" };
	private String sunday[] = { "0", "0", "0", "0", "0", "0", "0", "0", "0",
			"0", "0", "0", "0", "0" };

	// private String day[];
	private HashMap<String, String[]> dayMap = new HashMap<String, String[]>();

	private List<Integer> idList = new ArrayList<Integer>();

	private String monDayStr = getTimes(0) + "|0,0,0,0,0,0,0,0,0,0,0,0,0,0";
	private String tuesDayStr = getTimes(1) + "|0,0,0,0,0,0,0,0,0,0,0,0,0,0";
	private String wednesDayStr = getTimes(2) + "|0,0,0,0,0,0,0,0,0,0,0,0,0,0";
	private String thursDayStr = getTimes(3) + "|0,0,0,0,0,0,0,0,0,0,0,0,0,0";
	private String FriDayStr = getTimes(4) + "|0,0,0,0,0,0,0,0,0,0,0,0,0,0";
	private String saturDayStr = getTimes(5) + "|0,0,0,0,0,0,0,0,0,0,0,0,0,0";
	private String sunDayStr = getTimes(6) + "|0,0,0,0,0,0,0,0,0,0,0,0,0,0";

	String attendTime = "";// 用户选择的约骑时间

	private List<TextView> list = new ArrayList<TextView>();
	private HashMap<String, List<TextView>> hashMap = new HashMap<String, List<TextView>>();
	private HashMap<String, List<Integer>> idMap = new HashMap<String, List<Integer>>();

	private ImageCycleViewUtil mImageCycleView;
	List<ImageCycleViewUtil.ImageInfo> IClist = new ArrayList<ImageCycleViewUtil.ImageInfo>();

	private RiderDetailsModel model;
	private AttendRider attendRider;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			check();
			setView();
			setImageCycleViewUtil();
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rider_details);
		initView();
		getRiderDate();
		initEvent();
	}

	private void initView() {
		riderId = getIntent().getIntExtra("riderId", -1) + "";
		httpUtils = new XUtilsUtil();
		riderDetailsAdapter = new RiderDetailsAdapter(this);

		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		hearderViewLayout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.activity_rider_details_list_header, null);

		commentList = (ListView) findViewById(R.id.list_riderDetails_comment);
		buttonLayout = (LinearLayout) findViewById(R.id.layout_riderDetails_button);
		backLayout = (LinearLayout) findViewById(R.id.layout_riderDetailsActivity_back);
		phoneLayout = (LinearLayout) hearderViewLayout
				.findViewById(R.id.layout_riderDetailsActivity_phone);

		button1 = (TextView) findViewById(R.id.txt_riderDetails_button1);
		button2 = (TextView) findViewById(R.id.txt_riderDetails_button2);
		riderNameTxt = (TextView) findViewById(R.id.txt_riderDetails_riderName);

		mImageCycleView = (ImageCycleViewUtil) hearderViewLayout
				.findViewById(R.id.icc_riderDetails);

		memoTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_memo);
		priceTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_price);
		phoneTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_phone);
		numberTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_number);
		mileageTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_mileage);
		addressTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_address);
		bikeTypeTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_bikeType);

		timeLayout = (LinearLayout) hearderViewLayout
				.findViewById(R.id.layout_riderDetailsActivity_time);
		mondayTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_monday);
		tuesdayTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_tuesday);
		wednesdayTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_wednesday);
		thursdayTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_thuresday);
		fridayTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_friday);
		saturdayTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_saturday);
		sundayTxt = (TextView) hearderViewLayout
				.findViewById(R.id.txt_riderDetailsActivity_sunday);

		idMap.put("monday", new ArrayList<Integer>());
		idMap.put("tuesday", new ArrayList<Integer>());
		idMap.put("wednesday", new ArrayList<Integer>());
		idMap.put("thursday", new ArrayList<Integer>());
		idMap.put("friday", new ArrayList<Integer>());
		idMap.put("saturday", new ArrayList<Integer>());
		idMap.put("sunday", new ArrayList<Integer>());

	}

	private void initEvent() {
		backLayout.setOnClickListener(this);

		button1.setOnClickListener(this);
		button2.setOnClickListener(this);

		mondayTxt.setOnClickListener(this);
		tuesdayTxt.setOnClickListener(this);
		wednesdayTxt.setOnClickListener(this);
		thursdayTxt.setOnClickListener(this);
		fridayTxt.setOnClickListener(this);
		saturdayTxt.setOnClickListener(this);
		sundayTxt.setOnClickListener(this);

		mondayTxt.setText("今天");
		tuesdayTxt.setText(getWeekOfDate(1));
		wednesdayTxt.setText(getWeekOfDate(2));
		thursdayTxt.setText(getWeekOfDate(3));
		fridayTxt.setText(getWeekOfDate(4));
		saturdayTxt.setText(getWeekOfDate(5));
		sundayTxt.setText(getWeekOfDate(6));

		commentList.addHeaderView(hearderViewLayout);
		commentList.setAdapter(riderDetailsAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_riderDetails_button1:
			if (preferences.getInt("userId", -1) == -1) {
				Toasts.show(this, "请登录", 0);
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				this.finish();
				break;
			}

			if (button2.getText().toString().equals("发消息")) {
				String[] split = attendRider.getRiderImage().split(",");
				startActivity(new Intent(this, ChatSingleActivity.class)
						.putExtra("username", model.getImUserName())
						.putExtra("otherUserName", attendRider.getUserName())
						.putExtra("otherUserImage", split[0])
						.putExtra("otherUserId", attendRider.getUserId()));
			} else if (button2.getText().toString().equals("评论")) {
				startActivity(new Intent(RiderDetailsActivity.this,
						RiderDiscussActivity.class).putExtra("riderId",
						attendRider.getRiderId() + ""));
			}

			break;

		case R.id.txt_riderDetails_button2:
			if (preferences.getInt("userId", -1) == -1) {
				Toasts.show(this, "请登录", 0);
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				this.finish();
				break;
			}

			if (button2.getText().toString().equals("约骑")) {
				if (!attendTime.equals(null) || !attendTime.equals("")) {
					System.out.println("attendTime:" + attendTime);
					setRiderTime();
				} else {
					Toasts.show(this, "请先选择约骑时间！", 0);
				}
			} else if (button2.getText().toString().equals("待处理")) {
				Toasts.show(RiderDetailsActivity.this, "正在等待陪骑士处理约骑申请", 0);
			} else if (button2.getText().toString().equals("约骑成功")) {
				Toasts.show(RiderDetailsActivity.this, "陪骑士已同意您的约骑申请", 0);
			} else if (button2.getText().toString().equals("打分")) {
				showPopupWindow(v);
			}

			break;

		case R.id.layout_riderDetailsActivity_back:
			finish();
			break;
		case R.id.txt_riderDetailsActivity_monday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(0)), monday,
					1, getWeekOfDate(0));
			break;
		case R.id.txt_riderDetailsActivity_tuesday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(1)), tuesday,
					2, getWeekOfDate(1));

			break;
		case R.id.txt_riderDetailsActivity_wednesday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(2)),
					wednesday, 3, getWeekOfDate(2));

			break;
		case R.id.txt_riderDetailsActivity_thuresday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(3)), thursday,
					4, getWeekOfDate(3));

			break;
		case R.id.txt_riderDetailsActivity_friday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(4)), friday,
					5, getWeekOfDate(4));

			break;
		case R.id.txt_riderDetailsActivity_saturday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(5)), saturday,
					6, getWeekOfDate(5));

			break;
		case R.id.txt_riderDetailsActivity_sunday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(6)), sunday,
					7, getWeekOfDate(6));

			break;
		default:
			break;
		}
	}

	/**
	 * 验证陪骑状态
	 */
	String timeType = "";

	private void check() {
		userApplyState = model.getUserApplyState();
		if (userApplyState.equals("ATTEND")) {// 已约骑
			phoneLayout.setVisibility(View.VISIBLE);
			button1.setText("发消息");
			button2.setText("约骑成功");
		} else if (userApplyState.equals("APPLY")) {// 待处理
			phoneLayout.setVisibility(View.GONE);
			button1.setText("发消息");
			button2.setText("待处理");
		} else if (userApplyState.equals("NOTHING")) {// 没约
			phoneLayout.setVisibility(View.GONE);
			button1.setText("发消息");
			button2.setText("约骑");
		} else if (userApplyState.equals("DELETE")) {// 拒绝
			phoneLayout.setVisibility(View.GONE);
			button1.setText("发消息");
			button2.setText("约骑");
		}

		if (preferences.getInt("userId", -1) == attendRider.getUserId()) {
			getTime(model.getAttendPeriod());
			timeType = "AttendPeriod";
			buttonLayout.setVisibility(View.GONE);
		} else {
			getTime(model.getCanApplyPeriod());
			buttonLayout.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 陪骑士打赏
	 */
	private void attendRiderReward(final int markPointInt) {
		RequestParams params = new RequestParams("UTF-8");
		params.addQueryStringParameter("userId",
				preferences.getInt("userId", -1) + "");
		params.addQueryStringParameter("riderId", attendRider.getRiderId() + "");
		params.addQueryStringParameter("integral", markPointInt + "");
		params.addQueryStringParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		httpUtils.httpPost("mobile/attendRider/attendRiderReward.html", params,
				new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						System.out.println("陪骑打分信息:" + result);

						try {
							JSONObject jsonObject = new JSONObject(result);
							if (jsonObject.getBoolean("result")) {
								Toasts.show(RiderDetailsActivity.this, "您打赏了"
										+ markPointInt + "分!", 0);

							} else {
								Toasts.show(RiderDetailsActivity.this, "打赏失败:"
										+ jsonObject.getString("message")
										+ ",请重试!", 0);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						Toasts.show(RiderDetailsActivity.this, "未知错误" + error
								+ ":" + msg, 0);
					}
				});
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
				attendRiderReward(markPointInt);
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

	/**
	 * 获取陪骑士资料
	 */
	private void getRiderDate() {
		RequestParams params = new RequestParams("UTF-8");
		params.addQueryStringParameter("riderId", riderId);
		params.addQueryStringParameter("loginUserId",
				preferences.getInt("userId", -1) + "");
		params.addQueryStringParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		httpUtils.httpPost("common/queryAttendRider.html", params,
				new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						System.out.println("陪骑士信息:" + result);

						try {
							JSONObject jsonObject = new JSONObject(result);
							if (jsonObject.getBoolean("result")) {
								Gson gson = new Gson();
								Type type = new TypeToken<RiderDetailsModel>() {
								}.getType();

								model = gson.fromJson(
										jsonObject.getJSONObject("data")
												.toString(), type);
								attendRider = model.getAttendRider();
								Message m = handler.obtainMessage();
								handler.sendMessage(m);
							} else {
								Toasts.show(
										RiderDetailsActivity.this,
										"获取陪骑士资料失败:"
												+ jsonObject
														.getString("message")
												+ ",请重试!", 0);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						Toasts.show(RiderDetailsActivity.this, "未知错误" + error
								+ ":" + msg, 0);
					}

				});
	}

	/**
	 * 获取时间
	 */
	private void getTime(String timeStr) {
		String s[] = timeStr.split(";");
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s[i].length(); j++) {
				String[] split = s[i].split("\\|");
				dayMap.put(split[0], split[1].split(","));
			}
		}

		Iterator it = dayMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			String[] value = (String[]) entry.getValue();
			String dasda;

			for (int i = 0; i < value.length; i++) {
				dasda = value[i] + ",";
			}
		}

		onClick(mondayTxt);

	}

	/**
	 * 赋值控件
	 */
	private void setView() {
		riderNameTxt.setText(attendRider.getUserName());
		memoTxt.setText(attendRider.getRiderMemo());
		priceTxt.setText(attendRider.getAttendPrice() + "元");
		phoneTxt.setText(attendRider.getRiderPhone());
		numberTxt.setText(attendRider.getAttendTimes() + "次");
		mileageTxt.setText(attendRider.getRideCourse());
		addressTxt.setText(attendRider.getAttendArea());
		bikeTypeTxt.setText(attendRider.getBicycleType());

		String[] split = attendRider.getRiderImage().split(",");
		for (int i = 0; i < split.length; i++) {
			IClist.add(new ImageCycleViewUtil.ImageInfo(SystemUtil
					.UrlSize(split[i]), null, null, null));
		}

	}

	/**
	 * 约骑时间
	 */
	private void setRiderTime() {

		String[] monDayime = monDayStr.split("\\|");
		String[] tuesDayime = tuesDayStr.split("\\|");
		String[] wednesDayime = wednesDayStr.split("\\|");
		String[] thursDayime = thursDayStr.split("\\|");
		String[] FriDayime = FriDayStr.split("\\|");
		String[] saturDayime = saturDayStr.split("\\|");
		String[] sunDayime = sunDayStr.split("\\|");

		if (!monDayime[1].equals("0,0,0,0,0,0,0,0,0,0,0,0,0,0")) {
			attendTime = attendTime + monDayStr + ";";
		}
		if (!tuesDayime[1].equals("0,0,0,0,0,0,0,0,0,0,0,0,0,0")) {
			attendTime = attendTime + tuesDayStr + ";";
		}
		if (!wednesDayime[1].equals("0,0,0,0,0,0,0,0,0,0,0,0,0,0")) {
			attendTime = attendTime + wednesDayStr + ";";
		}
		if (!thursDayime[1].equals("0,0,0,0,0,0,0,0,0,0,0,0,0,0")) {
			attendTime = attendTime + thursDayStr + ";";
		}
		if (!FriDayime[1].equals("0,0,0,0,0,0,0,0,0,0,0,0,0,0")) {
			attendTime = attendTime + FriDayStr + ";";
		}
		if (!saturDayime[1].equals("0,0,0,0,0,0,0,0,0,0,0,0,0,0")) {
			attendTime = attendTime + saturDayStr + ";";
		}
		if (!sunDayime[1].equals("0,0,0,0,0,0,0,0,0,0,0,0,0,0")) {
			attendTime = attendTime + sunDayStr + ";";
		}

		attendTime.substring(0, attendTime.length() - 1);
		appointRider();
	}

	/**
	 * 
	 * @param v
	 */
	private void appointRider() {
		
		RequestParams params = new RequestParams("UTF-8");
		params.addQueryStringParameter("riderId", riderId);
		params.addQueryStringParameter("datas", attendTime);
		params.addQueryStringParameter("userId",
				preferences.getInt("userId", -1) + "");
		params.addQueryStringParameter("uniqueKey",
				preferences.getString("uniqueKey", null));

		httpUtils.httpPost("mobile/attendRider/appointRider.html", params,
				new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						System.out.println("约骑信息:" + result);

						try {
							JSONObject jsonObject = new JSONObject(result);
							if (jsonObject.getBoolean("result")) {
								Toasts.show(RiderDetailsActivity.this, "约骑成功",
										0);
								button2.setText("待处理");
							} else {
								Toasts.show(RiderDetailsActivity.this, "约骑失败:"
										+ jsonObject.getString("message")
										+ ",请重试!", 0);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						Toasts.show(RiderDetailsActivity.this, "未知错误" + error
								+ ":" + msg, 0);
					}
				});

	}

	/**
	 * 设置所有星期为暗色
	 */
	private void initTextView(View v) {
		mondayTxt.setBackgroundResource(R.drawable.corners_rider_time_white);
		tuesdayTxt.setBackgroundResource(R.drawable.corners_rider_time_white);
		wednesdayTxt.setBackgroundResource(R.drawable.corners_rider_time_white);
		thursdayTxt.setBackgroundResource(R.drawable.corners_rider_time_white);
		fridayTxt.setBackgroundResource(R.drawable.corners_rider_time_white);
		saturdayTxt.setBackgroundResource(R.drawable.corners_rider_time_white);
		sundayTxt.setBackgroundResource(R.drawable.corners_rider_time_white);
		v.setBackgroundResource(R.drawable.corners_rider_time_blue);
	}

	/**
	 * 设置轮播图
	 */
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
								RiderDetailsActivity.this);
						SystemUtil.Imagexutils(imageInfo.image.toString(),
								imageView, RiderDetailsActivity.this);
						/*
						 * Picasso.with(ActivityDetailsActivity.this)
						 * .load(imageInfo.image.toString()) .into(imageView);
						 */
						// imageView.setImageResource(R.drawable.demo);

						return imageView;

					}
				});
	}

	/**
	 * 遍历TextView控件
	 * 
	 * @param viewGroup
	 */
	int id;
	private TextView newDtv;
	private int minId;
	private int maxId;

	private String ss = null;

	private void getTextView(ViewGroup viewGroup, final String str,
			final String day[], final int num, final String today) {

		if (ss == null) {
			ss = str;
		}

		if (viewGroup == null) {
			return;
		}
		int count = viewGroup.getChildCount();
		for (int i = 0; i < count; i++) {
			View view = viewGroup.getChildAt(i);

			if (view instanceof TextView) { // 若是TextView记录下
				newDtv = (TextView) view;
				list.add(newDtv);
				hashMap.put(str, list);

				if (!timeType.equals("AttendPeriod")) {
					newDtv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							TextView t = (TextView) v;
							id = Integer.parseInt(t.getHint().toString());

							System.out.println("id:" + t.getHint().toString());
							System.out.println("dayMap.get(str)[id]:"
									+ dayMap.get(str)[id]);

							if (!dayMap.get(str)[id].equals("0")) {

								panduan(id, v, str, day, today, idMap.get(str));

							}

							switch (num) {
							case 1:
								monDayStr = keepData(day, getTimes(0));
								break;
							case 2:
								tuesDayStr = keepData(day, getTimes(1));
								break;
							case 3:
								wednesDayStr = keepData(day, getTimes(2));

								break;
							case 4:
								thursDayStr = keepData(day, getTimes(3));
								break;
							case 5:
								FriDayStr = keepData(day, getTimes(4));

								break;
							case 6:
								saturDayStr = keepData(day, getTimes(5));

								break;
							case 7:
								sunDayStr = keepData(day, getTimes(6));
								break;

							default:
								break;
							}

						}
					});
				}
			} else if (view instanceof LinearLayout) {
				// 若是布局控件（LinearLayout或RelativeLayout）,继续查询子View
				this.getTextView((ViewGroup) view, str, day, num, today);
			}
		}

		initTime(str, day, today);

	}

	private void initTime(final String str, final String[] day, String today) {
		if (hashMap.get(str).size() == dayMap.get(str).length) {
			for (int j = 0; j < dayMap.get(str).length; j++) {
				if (dayMap.get(str)[j].equals("1")) {
					if (preferences.getInt("userId", -1) == attendRider
							.getUserId()) {
						hashMap.get(str)
								.get(j)
								.setBackground(
										getResources()
												.getDrawable(
														R.drawable.corners_rider_time_blue));
					} else {
						if (getWeekOfDate(0).equals(today)) {
							if (getHour() - 6 > j) {
								hashMap.get(str)
										.get(j)
										.setBackground(
												getResources()
														.getDrawable(
																R.drawable.corners_rider_time_gray));
							} else {
								hashMap.get(str)
										.get(j)
										.setBackground(
												getResources()
														.getDrawable(
																R.drawable.corners_rider_time_white));
							}
						} else {
							hashMap.get(str)
									.get(j)
									.setBackground(
											getResources()
													.getDrawable(
															R.drawable.corners_rider_time_white));
						}
					}
				} else if (dayMap.get(str)[j].equals("2")) {
					hashMap.get(str)
							.get(j)
							.setBackground(
									getResources().getDrawable(
											R.drawable.corners_rider_time_blue));
				} else {
					hashMap.get(str)
							.get(j)
							.setBackground(
									getResources().getDrawable(
											R.drawable.corners_rider_time_gray));
				}
			}
		}
	}

	/**
	 * 
	 * @param id
	 * @param v
	 * @param str
	 * @param day
	 *            判断时间选择是否符合规则
	 */
	private void panduan(int id, View v, String str, String day[],
			String today, List<Integer> l) {

		if (l.size() == 0) {
			l.add(id);
			minId = id;
			maxId = id;

			dayMap.get(str)[id] = "2";
			day[id] = "1";
			v.setBackground(getResources().getDrawable(
					R.drawable.corners_rider_time_blue));

			return;
		} else if (l.size() == 1) {
			minId = l.get(0);
			maxId = l.get(0);
		} else {

			int minIds = l.get(0);
			int maxIds = l.get(0);
			for (int i = 0; i < l.size(); i++) {

				minId = l.get(i) < minIds ? l.get(i) : minIds;
				minIds = minId;
				maxId = l.get(i) > maxIds ? l.get(i) : maxIds;
				maxIds = maxId;
			}
			maxId = maxIds;
			minId = minIds;

		}

		System.out.println("idList.size():" + l.size());
		System.out.println("id:" + id);
		System.out.println("minId:" + minId);
		System.out.println("maxId:" + maxId);

		if (minId - 1 == id || maxId + 1 == id) {
			if (dayMap.get(str)[id].equals("2")) {
				dayMap.get(str)[id] = "1";
				day[id] = "0";
				v.setBackground(getResources().getDrawable(
						R.drawable.corners_rider_time_white));

				l.remove((Integer) id);

			} else if (dayMap.get(str)[id].equals("1")) {
				dayMap.get(str)[id] = "2";
				day[id] = "1";
				v.setBackground(getResources().getDrawable(
						R.drawable.corners_rider_time_blue));

				l.add(id);

			}

		} else if (minId == id || maxId == id) {

			if (dayMap.get(str)[id].equals("2")) {
				dayMap.get(str)[id] = "1";
				day[id] = "0";
				v.setBackground(getResources().getDrawable(
						R.drawable.corners_rider_time_white));

				l.remove((Integer) id);

			} else if (dayMap.get(str)[id].equals("1")) {
				dayMap.get(str)[id] = "2";
				day[id] = "1";
				v.setBackground(getResources().getDrawable(
						R.drawable.corners_rider_time_blue));

				l.add(id);

			}
		} else {

			for (int j = 0; j < day.length; j++) {

				if (getWeekOfDate(0).equals(today)) {
					if (getHour() - 6 > j) {
						hashMap.get(str)
								.get(j)
								.setBackground(
										getResources()
												.getDrawable(
														R.drawable.corners_rider_time_gray));
						dayMap.get(str)[j] = "0";
						day[j] = "0";

					} else {
						if (!dayMap.get(str)[j].equals("0")) {
							list.get(j)
									.setBackground(
											getResources()
													.getDrawable(
															R.drawable.corners_rider_time_white));
							dayMap.get(str)[j] = "1";
							day[j] = "0";
						}
					}
				} else {
					if (!dayMap.get(str)[j].equals("0")) {
						list.get(j).setBackground(
								getResources().getDrawable(
										R.drawable.corners_rider_time_white));
						dayMap.get(str)[j] = "1";
						day[j] = "0";
					}
				}

			}
			l.clear();
			l.add(id);
			day[id] = "1";
			dayMap.get(str)[id] = "2";

			v.setBackground(getResources().getDrawable(
					R.drawable.corners_rider_time_blue));

		}
	}

	/**
	 * 
	 * @param day
	 * @param content
	 * @return 拼接约骑时间
	 */
	private String keepData(String[] day, String content) {

		String str1 = content + "|";
		for (int i = 0; i < day.length; i++) {
			if (i < day.length - 1) {
				str1 = str1 + day[i] + ",";
			} else {
				str1 = str1 + day[i];
			}
		}
		return str1;
	}

	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */

	private HashMap<String, String> dayHashMap = new HashMap<String, String>();

	private String userApplyState;

	public String getWeekOfDate(int i) {

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendar.add(Calendar.DAY_OF_YEAR, i);
		Date date = calendar.getTime();

		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		if (weekDays[w].equals("星期一")) {
			dayHashMap.put(weekDays[w], "monday");
		} else if (weekDays[w].equals("星期二")) {
			dayHashMap.put(weekDays[w], "tuesday");
		} else if (weekDays[w].equals("星期三")) {
			dayHashMap.put(weekDays[w], "wednesday");
		} else if (weekDays[w].equals("星期四")) {
			dayHashMap.put(weekDays[w], "thursday");
		} else if (weekDays[w].equals("星期五")) {
			dayHashMap.put(weekDays[w], "friday");
		} else if (weekDays[w].equals("星期六")) {
			dayHashMap.put(weekDays[w], "saturday");
		} else if (weekDays[w].equals("星期日")) {
			dayHashMap.put(weekDays[w], "sunday");
		}

		return weekDays[w];
	}

	/**
	 * 获取当前日期
	 * 
	 */
	private String getTimes(int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.get(Calendar.HOUR_OF_DAY);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendar.add(Calendar.DAY_OF_YEAR, i);
		Date date = calendar.getTime();
		return sdf.format(date);
	}

	/**
	 * 获取当前小时
	 * 
	 */
	private static Integer getHour() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		Date date = calendar.getTime();
		return Integer.parseInt(sdf.format(date));
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
