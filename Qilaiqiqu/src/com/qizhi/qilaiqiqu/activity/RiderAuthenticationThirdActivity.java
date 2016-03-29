package com.qizhi.qilaiqiqu.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.ActivityCollectorUtil;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

public class RiderAuthenticationThirdActivity extends HuanxinLogOutActivity
		implements OnClickListener {

	private EditText signatureEdt;
	private TextView memoLengh;

	private LinearLayout backLayout;

	private TextView mileageTxt;
	private TextView bikeTypeTxt;
	private TextView beRiderTxt;

	private XUtilsUtil httpUtils;

	private SharedPreferences preferences;

	private ArrayList<String> photoList;
	private int uploadingNum = 0;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				photoList.add((String) msg.obj);
				uploadingNum = uploadingNum + 1;
				try {
					if (uploadingNum == RiderAuthenticationFirstActivity.bitList
							.size()) {
						StringBuffer s = new StringBuffer();
						for (int i = 0; i < photoList.size(); i++) {
							s.append(photoList.get(i));
							if (photoList.size() - 1 != i) {
								s.append(",");
							}
						}
						System.out.println("-----------aa----------");
						System.out.println(s.toString());
						System.out.println("-----------aa----------");
						riderAuthentication(s.toString());
						break;
					}
					new SystemUtil()
							.httpClient(
									saveMyBitmap(RiderAuthenticationFirstActivity.bitList
											.get(uploadingNum)), preferences,
									handler, "HD");
				} catch (IOException e) {
					e.printStackTrace();
				}
				;
				break;
			case 2:
				// Toasts.show(ReleaseActiveActivity.this, "图片发布出现问题", 0);
				uploadingNum = uploadingNum + 1;
				try {
					if (uploadingNum == RiderAuthenticationFirstActivity.bitList
							.size()) {
						StringBuffer s = new StringBuffer();
						for (int i = 0; i < photoList.size(); i++) {
							s.append(photoList.get(i));
							if (photoList.size() - 1 != i) {
								s.append(",");
							}
						}
						riderAuthentication(s.toString());
						break;
					}
					new SystemUtil()
							.httpClient(
									saveMyBitmap(RiderAuthenticationFirstActivity.bitList
											.get(uploadingNum)), preferences,
									handler, "HD");
				} catch (IOException e) {
					e.printStackTrace();
				}
				;
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
		setContentView(R.layout.activity_authentication_third_rider);
		initView();
		initEvent();
	}

	private void initView() {
		httpUtils = new XUtilsUtil();
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		beRiderTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_third_next);

		backLayout = (LinearLayout) findViewById(R.id.layout_riderAuthenticationActivity_third_back);

		mileageTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_third_mileage);
		bikeTypeTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_third_bikeType);

		memoLengh = (TextView) findViewById(R.id.txt_releaseActiveActivity_memoLengh);
		signatureEdt = (EditText) findViewById(R.id.edt_riderAuthenticationActivity_signature);
	}

	private void initEvent() {
		backLayout.setOnClickListener(this);
		mileageTxt.setOnClickListener(this);
		beRiderTxt.setOnClickListener(this);
		bikeTypeTxt.setOnClickListener(this);

		signatureEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				memoLengh.setText(s.length() + "/150");
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_riderAuthenticationActivity_third_mileage:
			showpopupMileage(v);
			break;

		case R.id.txt_riderAuthenticationActivity_third_bikeType:
			showpopupBikeType(v);
			break;

		case R.id.layout_riderAuthenticationActivity_third_back:
			finish();
			break;

		case R.id.txt_riderAuthenticationActivity_third_next:
			if (mileageTxt.getText().toString().trim().equals("")
					|| mileageTxt.getText().toString().trim().equals(null)
					|| bikeTypeTxt.getText().toString().trim().equals("")
					|| bikeTypeTxt.getText().toString().trim().equals(null)
					|| signatureEdt.getText().toString().trim().equals("")
					|| signatureEdt.getText().toString().trim().equals(null)) {
				Toasts.show(RiderAuthenticationThirdActivity.this, "你还有未填项哦", 0);
			} else {

				photoList = new ArrayList<String>();
				uploadingNum = 0;
				try {
					new SystemUtil()
							.httpClient(
									saveMyBitmap(RiderAuthenticationFirstActivity.bitList
											.get(uploadingNum)), preferences,
									handler, "HD");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	public String saveMyBitmap(Bitmap mBitmap) throws IOException {
		File outDir = null;
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			// 这个路径，在手机内存下创建一个pictures的文件夹，把图片存在其中。
			outDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			if (!outDir.exists()) {
				outDir.mkdirs();
			}
			outDir = new File(outDir, System.currentTimeMillis() + ".jpg");
			String s = outDir.toString();

			FileOutputStream fos = new FileOutputStream(outDir);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			return s;
		} else {
			Toasts.show(this, "请确认已经插入SD卡", 0);
		}
		return null;
	}

	int mileagePosition;
	String mileage = "0-500KM";

	private void showpopupMileage(View v) {
		View view = LayoutInflater.from(this).inflate(
				R.layout.item_riderauthentication_mileage, null);

		NumberPicker pricePicker = (NumberPicker) view
				.findViewById(R.id.authentication_second_mileagepicker);
		TextView mBtnConfirm = (TextView) view
				.findViewById(R.id.authentication_second_mileagepicker_ok);

		final String[] p = { "0-500KM", "500-1000KM", "1000以上" };
		pricePicker.setDisplayedValues(p);
		pricePicker.setMaxValue(p.length - 1);
		pricePicker.setMinValue(0);
		pricePicker.setValue(mileagePosition);

		pricePicker.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker arg0, int oldVal, int newVal) {
				mileagePosition = newVal;
				mileage = p[newVal];
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
				mileageTxt.setText(mileage);
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

	String bikeType = "山地车";
	int bikeTypePosition;

	private void showpopupBikeType(View v) {
		View view = LayoutInflater.from(this).inflate(
				R.layout.item_riderauthentication_biketype, null);

		NumberPicker pricePicker = (NumberPicker) view
				.findViewById(R.id.authentication_second_bikeTypepicker);
		TextView mBtnConfirm = (TextView) view
				.findViewById(R.id.authentication_second_bikeTypepicker_ok);

		final String[] p = { "山地车", "公路车", "旅行车", "轻便车", "其他" };
		pricePicker.setDisplayedValues(p);
		pricePicker.setMaxValue(p.length - 1);
		pricePicker.setMinValue(0);
		pricePicker.setValue(bikeTypePosition);

		pricePicker.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker arg0, int oldVal, int newVal) {
				bikeTypePosition = newVal;
				bikeType = p[newVal];
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
				bikeTypeTxt.setText(bikeType);
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

	private void riderAuthentication(String imagePath) {
		RequestParams params = new RequestParams("UTF-8");
		params.addQueryStringParameter("userId",
				preferences.getInt("userId", -1) + "");
		params.addQueryStringParameter("riderImage", imagePath);
		params.addQueryStringParameter("riderPhone",
				RiderAuthenticationFirstActivity.riderPhone);
		params.addQueryStringParameter("attendArea",
				RiderAuthenticationFirstActivity.attendArea);
		params.addQueryStringParameter("attendPrice",
				RiderAuthenticationFirstActivity.attendPrice);
		params.addQueryStringParameter("attendPeriod",
				RiderAuthenticationFirstActivity.attendPeriod);
		params.addQueryStringParameter("bicycleType", bikeType);
		params.addQueryStringParameter("rideCourse", mileage);
		params.addQueryStringParameter("riderMemo", signatureEdt.getText()
				.toString().trim());
		params.addQueryStringParameter("uniqueKey",
				preferences.getString("uniqueKey", null));

		httpUtils.httpPost("mobile/attendRider/insertAttendRider.html", params,
				new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(result);
							if (jsonObject.getBoolean("result")) {
								Toasts.show(
										RiderAuthenticationThirdActivity.this,
										"提交成功，请等待审核!", 0);

								RiderAuthenticationFirstActivity.instanceFirst
										.finish();
								RiderAuthenticationSecondActivity.instanceSecond
										.finish();
								RiderAuthenticationThirdActivity.this.finish();
							} else {
								Toasts.show(
										RiderAuthenticationThirdActivity.this,
										"提交认证失败:"
												+ jsonObject
														.getString("message")
												+ ",请重试!", 0);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						Toasts.show(RiderAuthenticationThirdActivity.this,
								"服务器无响应，请重试!", 0);
					}
				});

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
