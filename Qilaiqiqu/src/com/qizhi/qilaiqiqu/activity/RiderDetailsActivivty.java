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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class RiderDetailsActivivty extends Activity implements OnClickListener {

	private ListView commentList;
	private RiderDetailsAdapter riderDetailsAdapter;

	LinearLayout hearderViewLayout;

	private LinearLayout backLayout;

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

	String attendTime;// 用户选择的约骑时间

	private List<TextView> list = new ArrayList<TextView>();
	private HashMap<String, List<TextView>> hashMap = new HashMap<String, List<TextView>>();

	private ImageCycleViewUtil mImageCycleView;
	List<ImageCycleViewUtil.ImageInfo> IClist = new ArrayList<ImageCycleViewUtil.ImageInfo>();

	private RiderDetailsModel model;
	private AttendRider attendRider;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			getTime(model.getCanApplyPeriod());
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
		httpUtils = new XUtilsUtil();
		riderDetailsAdapter = new RiderDetailsAdapter(this);

		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		hearderViewLayout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.activity_rider_details_list_header, null);

		commentList = (ListView) findViewById(R.id.list_riderDetails_comment);
		backLayout = (LinearLayout) findViewById(R.id.layout_riderDetailsActivity_back);

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

			break;

		case R.id.txt_riderDetails_button2:
			attendTime.substring(0, attendTime.length() - 1);
			break;

		case R.id.layout_riderDetailsActivity_back:
			finish();
			break;
		case R.id.txt_riderDetailsActivity_monday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(0)), monday, 1);
			break;
		case R.id.txt_riderDetailsActivity_tuesday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(1)), tuesday,
					2);

			break;
		case R.id.txt_riderDetailsActivity_wednesday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(2)),
					wednesday, 3);

			break;
		case R.id.txt_riderDetailsActivity_thuresday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(3)), thursday,
					4);

			break;
		case R.id.txt_riderDetailsActivity_friday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(4)), friday, 5);

			break;
		case R.id.txt_riderDetailsActivity_saturday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(5)), saturday,
					6);

			break;
		case R.id.txt_riderDetailsActivity_sunday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, dayHashMap.get(getWeekOfDate(6)), sunday, 7);

			break;
		default:
			break;
		}
	}

	/**
	 * 获取陪骑士资料
	 */
	private void getRiderDate() {
		RequestParams params = new RequestParams("UTF-8");
		params.addQueryStringParameter("riderId", "10000");
		params.addQueryStringParameter("loginUserId", "10025");
		params.addQueryStringParameter("uniqueKey", "k272kPkeOhH9qTxS7/zv9g==");
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
										RiderDetailsActivivty.this,
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
				System.out.println(dasda);
			}
			System.out.println("key=" + key + " value=" + value);
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
	private void setRiderTime(String str) {
		String[] time =  str.split("|");
		if(!time[1].equals("0,0,0,0,0,0,0,0,0,0,0,0,0,0")){
			attendTime = str + ";";
		}
	}
	
	/**
	 * 
	 * @param v
	 */
	private void appointRider(){
		RequestParams params = new RequestParams("UTF-8");
		params.addQueryStringParameter("riderId", "riderId");
		params.addQueryStringParameter("userId", "riderId");
		params.addQueryStringParameter("datas", "riderId");
		params.addQueryStringParameter("uniqueKey", "riderId");
	}
	

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
								RiderDetailsActivivty.this);
						SystemUtil.Imagexutils(imageInfo.image.toString(),
								imageView, RiderDetailsActivivty.this);
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
			final String day[], final int num) {

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
				newDtv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						id = (v.getId() % 100) - 14;

						if (!dayMap.get(str)[id - 1].equals("0")) {
							panduan(id, v, str, day);

						}

						// if (dayMap.get(str)[id - 1].equals("1")) {//
						// .equals("1")表示用户可约的时间;.equals("2")表示用户选择约的时间;.equals("0")表示用户不可约的时间;
						// } else if (dayMap.get(str)[id - 1].equals("2")) {
						// idList.remove((Integer) id);
						//
						// System.out.println("idList.size() - 1:"
						// + idList.size());
						//
						// panduan(id, v, str, day);
						// }

						switch (num) {
						case 1:
							monDayStr = keepData(day, getTimes(0));
							setRiderTime(monDayStr);
							break;
						case 2:
							tuesDayStr = keepData(day, getTimes(1));
							setRiderTime(tuesDayStr);
							break;
						case 3:
							wednesDayStr = keepData(day, getTimes(2));
							setRiderTime(wednesDayStr);

							break;
						case 4:
							thursDayStr = keepData(day, getTimes(3));
							setRiderTime(thursDayStr);
							break;
						case 5:
							FriDayStr = keepData(day, getTimes(4));
							setRiderTime(FriDayStr);

							break;
						case 6:
							saturDayStr = keepData(day, getTimes(5));
							setRiderTime(saturDayStr);

							break;
						case 7:
							sunDayStr = keepData(day, getTimes(6));
							setRiderTime(sunDayStr);
							break;

						default:
							break;
						}

					}
				});
			} else if (view instanceof LinearLayout) {
				// 若是布局控件（LinearLayout或RelativeLayout）,继续查询子View
				this.getTextView((ViewGroup) view, str, day, num);
			}
		}

		if (hashMap.get(str).size() == dayMap.get(str).length) {
			for (int j = 0; j < dayMap.get(str).length; j++) {
				if (dayMap.get(str)[j].equals("1")) {
					if (getHour() - 7 > j) {
						hashMap.get(str)
								.get(j)
								.setBackground(
										getResources()
												.getDrawable(
														R.drawable.corners_rider_time_gray));
						dayMap.get(str)[j] = "0";
						day[j] = "0";
					} else {
						hashMap.get(str)
								.get(j)
								.setBackground(
										getResources()
												.getDrawable(
														R.drawable.corners_rider_time_white));
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

	private void panduan(int id, View v, String str, String day[]) {

		if (idList.size() == 0) {
			idList.add(id);
			minId = id;
			maxId = id;

			for (int i = 0; i < dayMap.get(str).length; i++) {
				System.out.println("dayMap.get(str)[i]:" + dayMap.get(str)[i]);
			}

			dayMap.get(str)[id - 1] = "2";
			day[id - 1] = "1";
			v.setBackground(getResources().getDrawable(
					R.drawable.corners_rider_time_blue));

			return;
		} else if (idList.size() == 1) {
			minId = idList.get(0);
			maxId = idList.get(0);
		} else {

			int minIds = idList.get(0);
			int maxIds = idList.get(0);
			for (int i = 0; i < idList.size(); i++) {

				minId = idList.get(i) < minIds ? idList.get(i) : minIds;
				minIds = minId;
				maxId = idList.get(i) > maxIds ? idList.get(i) : maxIds;
				maxIds = maxId;
			}
			maxId = maxIds;
			minId = minIds;

		}

		System.out.println("minId:" + minId);
		System.out.println("maxId:" + maxId);
		System.out.println("id:" + id);

		if (minId - 1 == id || maxId + 1 == id) {
			if (dayMap.get(str)[id - 1].equals("2")) {
				dayMap.get(str)[id - 1] = "1";
				day[id - 1] = "0";
				v.setBackground(getResources().getDrawable(
						R.drawable.corners_rider_time_white));

				idList.remove((Integer) id);

				System.out.println("==+-1 day[id - 1]:" + day[id - 1]);
				System.out.println("==+-1 dayMap.get(str)[id - 1]:"
						+ dayMap.get(str)[id - 1]);

			} else if (dayMap.get(str)[id - 1].equals("1")) {
				dayMap.get(str)[id - 1] = "2";
				day[id - 1] = "1";
				v.setBackground(getResources().getDrawable(
						R.drawable.corners_rider_time_blue));

				idList.add(id);

				System.out.println("==+-1 day[id - 1]:" + day[id - 1]);
				System.out.println("==+-1 dayMap.get(str)[id - 1]:"
						+ dayMap.get(str)[id - 1]);

			}

		} else if (minId == id || maxId == id) {
			if (dayMap.get(str)[id - 1].equals("2")) {
				dayMap.get(str)[id - 1] = "1";
				day[id - 1] = "0";
				v.setBackground(getResources().getDrawable(
						R.drawable.corners_rider_time_white));

				idList.remove((Integer) id);

				System.out.println("== day[id - 1]:" + day[id - 1]);
				System.out.println("== dayMap.get(str)[id - 1]:"
						+ dayMap.get(str)[id - 1]);

			} else if (dayMap.get(str)[id - 1].equals("1")) {
				dayMap.get(str)[id - 1] = "2";
				day[id - 1] = "1";
				v.setBackground(getResources().getDrawable(
						R.drawable.corners_rider_time_blue));

				idList.add(id);

				System.out.println("== day[id - 1]:" + day[id - 1]);
				System.out.println("== dayMap.get(str)[id - 1]:"
						+ dayMap.get(str)[id - 1]);

			}
		} else {

			for (int j = 0; j < day.length; j++) {
				if (getHour() - 7 > j) {
					hashMap.get(str)
							.get(j)
							.setBackground(
									getResources().getDrawable(
											R.drawable.corners_rider_time_gray));
					dayMap.get(str)[j] = "0";
					day[j] = "0";

				} else {
					list.get(j).setBackground(
							getResources().getDrawable(
									R.drawable.corners_rider_time_white));
					dayMap.get(str)[j] = "1";
					day[j] = "0";
				}

			}
			idList.clear();
			idList.add(id);
			day[id - 1] = "1";
			dayMap.get(str)[id - 1] = "2";
			v.setBackground(getResources().getDrawable(
					R.drawable.corners_rider_time_blue));

		}
	}

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
