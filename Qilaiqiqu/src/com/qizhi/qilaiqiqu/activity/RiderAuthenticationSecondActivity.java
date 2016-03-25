package com.qizhi.qilaiqiqu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.umeng.analytics.MobclickAgent;

public class RiderAuthenticationSecondActivity extends HuanxinLogOutActivity
		implements OnClickListener {

	private LinearLayout timeLayout;
	private LinearLayout backLayout;

	private TextView nextTxt;
	private TextView priceTxt;

	private TextView mondayTxt;
	private TextView tuesdayTxt;
	private TextView wednesdayTxt;
	private TextView thursdayTxt;
	private TextView fridayTxt;
	private TextView saturdayTxt;
	private TextView sundayTxt;

	private int monday[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int tuesday[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int wednesday[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int thursday[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int friday[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int saturday[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int sunday[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, List<TextView>> hashMap = new HashMap<Integer, List<TextView>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_authentication_second_rider);
		initView();
		initEvent();
	}

	private void initView() {
		nextTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_second_next);
		priceTxt = (TextView) findViewById(R.id.edt_riderAuthenticationActivity_second_price);
		timeLayout = (LinearLayout) findViewById(R.id.layout_riderAuthenticationActivity_second_time);
		backLayout = (LinearLayout) findViewById(R.id.layout_riderAuthenticationActivity_second_back);

		mondayTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_second_monday);
		tuesdayTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_second_tuesday);
		wednesdayTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_second_wednesday);
		thursdayTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_second_thuresday);
		fridayTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_second_friday);
		saturdayTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_second_saturday);
		sundayTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_second_sunday);

	}

	private void initEvent() {
		nextTxt.setOnClickListener(this);
		priceTxt.setOnClickListener(this);
		backLayout.setOnClickListener(this);

		mondayTxt.setOnClickListener(this);
		tuesdayTxt.setOnClickListener(this);
		wednesdayTxt.setOnClickListener(this);
		thursdayTxt.setOnClickListener(this);
		fridayTxt.setOnClickListener(this);
		saturdayTxt.setOnClickListener(this);
		sundayTxt.setOnClickListener(this);
		onClick(mondayTxt);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_riderAuthenticationActivity_second_next:

			RiderAuthenticationFirstActivity.attendPeriod = monDayStr + ";"
					+ tuesDayStr + ";" + wednesDayStr + ";" + thursDayStr + ";"
					+ FriDayStr + ";" + saturDayStr + ";" + sunDayStr;

			if (RiderAuthenticationFirstActivity.attendPeriod
					.equals("monday|0,0,0,0,0,0,0,0,0,0,0,0,0,0;tuesday|0,0,0,0,0,0,0,0,0,0,0,0,0,0;wednesday|0,0,0,0,0,0,0,0,0,0,0,0,0,0;thursday|0,0,0,0,0,0,0,0,0,0,0,0,0,0;friday|0,0,0,0,0,0,0,0,0,0,0,0,0,0;saturday|0,0,0,0,0,0,0,0,0,0,0,0,0,0;sunday|0,0,0,0,0,0,0,0,0,0,0,0,0,0")) {
				Toasts.show(RiderAuthenticationSecondActivity.this, "请选择陪骑时间",
						0);
			} else {
				if (priceTxt.getText().toString().trim().equals("")
						|| priceTxt.getText().toString().trim().equals(null)) {
					RiderAuthenticationFirstActivity.attendPrice = 0 + "";
				}
				startActivity(new Intent(
						RiderAuthenticationSecondActivity.this,
						RiderAuthenticationThirdActivity.class));

			}

			break;
		case R.id.layout_riderAuthenticationActivity_second_back:
			finish();
			break;
		case R.id.edt_riderAuthenticationActivity_second_price:
			showPopupPrice(v);
			break;

		case R.id.txt_riderAuthenticationActivity_second_monday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, monday, 1);
			break;
		case R.id.txt_riderAuthenticationActivity_second_tuesday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, tuesday, 2);

			break;
		case R.id.txt_riderAuthenticationActivity_second_wednesday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, wednesday, 3);

			break;
		case R.id.txt_riderAuthenticationActivity_second_thuresday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, thursday, 4);

			break;
		case R.id.txt_riderAuthenticationActivity_second_friday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, friday, 5);

			break;
		case R.id.txt_riderAuthenticationActivity_second_saturday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, saturday, 6);

			break;
		case R.id.txt_riderAuthenticationActivity_second_sunday:
			initTextView(v);
			list.clear();
			getTextView(timeLayout, sunday, 7);

			break;

		default:
			break;
		}
	}

	String monDayStr = "monday|0,0,0,0,0,0,0,0,0,0,0,0,0,0";
	String tuesDayStr = "tuesday|0,0,0,0,0,0,0,0,0,0,0,0,0,0";
	String wednesDayStr = "wednesday|0,0,0,0,0,0,0,0,0,0,0,0,0,0";
	String thursDayStr = "thursday|0,0,0,0,0,0,0,0,0,0,0,0,0,0";
	String FriDayStr = "friday|0,0,0,0,0,0,0,0,0,0,0,0,0,0";
	String saturDayStr = "saturday|0,0,0,0,0,0,0,0,0,0,0,0,0,0";
	String sunDayStr = "sunday|0,0,0,0,0,0,0,0,0,0,0,0,0,0";

	private String keepData(int[] day, String content) {

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

	String price;
	int position = 0;

	private void showPopupPrice(View v) {
		View view = LayoutInflater.from(this).inflate(
				R.layout.item_riderauthentication_price, null);

		NumberPicker pricePicker = (NumberPicker) view
				.findViewById(R.id.authentication_second_pricepicker);
		TextView mBtnConfirm = (TextView) view
				.findViewById(R.id.authentication_second_pricepicker_ok);

		final String[] p = { "0", "10", "20", "30", "40", "50", "60", "70",
				"80", "90", "100", "110", "120", "130", "140", "150" };
		pricePicker.setDisplayedValues(p);
		pricePicker.setMaxValue(p.length - 1);
		pricePicker.setMinValue(0);
		pricePicker.setValue(position);

		pricePicker.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker arg0, int oldVal, int newVal) {
				position = newVal;
				price = p[newVal];
				Toasts.show(RiderAuthenticationSecondActivity.this, price, 0);
			}
		});

		final PopupWindow popupWindow = new PopupWindow(view,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupWindow.setTouchable(true);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		mBtnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				priceTxt.setText(price);
				RiderAuthenticationFirstActivity.attendPrice = price;
				popupWindow.dismiss();
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.color.f8f8));
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

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

	/**
	 * 遍历TextView控件
	 * 
	 * @param viewGroup
	 */
	int id;
	private TextView newDtv;

	List<TextView> list = new ArrayList<TextView>();

	private void getTextView(ViewGroup viewGroup, final int[] day, final int num) {

		if (viewGroup == null) {
			return;
		}
		int count = viewGroup.getChildCount();
		for (int i = 0; i < count; i++) {
			View view = viewGroup.getChildAt(i);

			if (view instanceof TextView) { // 若是TextView记录下
				newDtv = (TextView) view;
				list.add(newDtv);
				hashMap.put(num, list);
				newDtv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						id = v.getId() % 100;

						System.out.println("id"+id);
						
						if (day[id] == 1) {
							day[id] = 0;
							v.setBackground(getResources().getDrawable(
									R.drawable.corners_rider_time_white));

						} else {
							day[id] = 1;
							v.setBackground(getResources().getDrawable(
									R.drawable.corners_rider_time_blue));
						}

						switch (num) {
						case 1:
							monDayStr = keepData(monday, "monday");
							break;
						case 2:
							tuesDayStr = keepData(tuesday, "tuesday");

							break;
						case 3:
							wednesDayStr = keepData(wednesday, "wednesday");

							break;
						case 4:
							thursDayStr = keepData(thursday, "thursday");

							break;
						case 5:
							FriDayStr = keepData(friday, "friday");

							break;
						case 6:
							saturDayStr = keepData(saturday, "saturday");

							break;
						case 7:
							sunDayStr = keepData(sunday, "sunday");
							break;

						default:
							break;
						}

					}
				});

			} else if (view instanceof LinearLayout) {
				// 若是布局控件（LinearLayout或RelativeLayout）,继续查询子View
				this.getTextView((ViewGroup) view, day, num);
			}
		}

		if (hashMap.get(num).size() == day.length) {
			for (int j = 0; j < day.length; j++) {
				if (day[j] == 1) {
					hashMap.get(num)
							.get(j)
							.setBackground(
									getResources().getDrawable(
											R.drawable.corners_rider_time_blue));
				} else {
					hashMap.get(num)
							.get(j)
							.setBackground(
									getResources()
											.getDrawable(
													R.drawable.corners_rider_time_white));
				}
			}
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
