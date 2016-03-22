package com.qizhi.qilaiqiqu.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("SimpleDateFormat")
public class ReleaseActiveActivity extends HuanxinLogOutActivity implements OnClickListener {

	private TextView timeTxt;
	private TextView dateTxt;

	private EditText themeEdt;
	private EditText signatureEdt;
	private EditText moneyTxt;

	private TextView signatureTxt;
	private TextView releaseTxt;
	private TextView memoTxt;
	private TextView mileageTxt;

	private LinearLayout dateLayout;
	private LinearLayout timeLayout;
	private LinearLayout backLayout;
	private LinearLayout pictureLayout;
	private LinearLayout routeOverlayLayout;

	// private GridView pictureGrid;

	private XUtilsUtil xUtilsUtil;

	SharedPreferences preferences;

	// private ReleaseActiveGridAdapter adapter;

	private ImageView addImg;

	private ArrayList<String> photoList;
	private ArrayList<Bitmap> bitList;

	private boolean isFirst = false;

	private String data;
	private String duration;

	int num = 0;

	private int days;
	private int hours;
	private ProgressDialog pDialog;

	protected static String lanInfo = null;
	protected static String lanName = null;
	protected static String mileage = null;

	private boolean falg = true;

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
					if (uploadingNum == bitList.size()) {
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
						postAll(s.toString());
						break;
					}
					new SystemUtil().httpClient(
							saveMyBitmap(bitList.get(uploadingNum)),
							preferences, handler, "HD");
				} catch (IOException e) {
					e.printStackTrace();
				}
				;
				break;
			case 2:
				// Toasts.show(ReleaseActiveActivity.this, "图片发布出现问题", 0);
				uploadingNum = uploadingNum + 1;
				try {
					if (uploadingNum == bitList.size()) {
						StringBuffer s = new StringBuffer();
						for (int i = 0; i < photoList.size(); i++) {
							s.append(photoList.get(i));
							if (photoList.size() - 1 != i) {
								s.append(",");
							}
						}
						postAll(s.toString());
						break;
					}
					new SystemUtil().httpClient(
							saveMyBitmap(bitList.get(uploadingNum)),
							preferences, handler, "HD");
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_activity_release);
		initView();
		initEvent();
	}

	private void initView() {
		dateTxt = (TextView) findViewById(R.id.txt_releaseActiveActivity_date);
		timeTxt = (TextView) findViewById(R.id.txt_releaseActiveActivity_time);
		themeEdt = (EditText) findViewById(R.id.edt_releaseActiveActivity_theme);
		moneyTxt = (EditText) findViewById(R.id.edt_releaseActiveActivity_money);
		mileageTxt = (TextView) findViewById(R.id.txt_releaseActiveActivity_mileage);
		signatureEdt = (EditText) findViewById(R.id.edt_releaseActiveActivity_signature);
		signatureTxt = (TextView) findViewById(R.id.txt_releaseActiveActivity_textLengh);
		releaseTxt = (TextView) findViewById(R.id.txt_releaseActiveActivity_release);
		memoTxt = (TextView) findViewById(R.id.txt_releaseActiveActivity_memoLengh);
		dateLayout = (LinearLayout) findViewById(R.id.layout_releaseActiveActivity_date);
		timeLayout = (LinearLayout) findViewById(R.id.layout_releaseActiveActivity_time);
		pictureLayout = (LinearLayout) findViewById(R.id.layout_releaseActiveActivity_picture);
		routeOverlayLayout = (LinearLayout) findViewById(R.id.layout_releaseActiveActivity_routeOverlay);
		backLayout = (LinearLayout) findViewById(R.id.layout_releaseActiveActivity_back);
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		addImg = (ImageView) findViewById(R.id.img_releaseActiveActivity_add);
		photoList = new ArrayList<String>();
		bitList = new ArrayList<Bitmap>();
		xUtilsUtil = new XUtilsUtil();
	}

	private void initEvent() {
		addImg.setOnClickListener(this);
		dateLayout.setOnClickListener(this);
		timeLayout.setOnClickListener(this);
		backLayout.setOnClickListener(this);
		releaseTxt.setOnClickListener(this);
		routeOverlayLayout.setOnClickListener(this);
		themeEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable e) {
				signatureTxt.setText(Html.fromHtml(e.length()
						+ "<font color='#9d9d9e'>/10</font>"));
			}
		});

		signatureEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable e) {
				memoTxt.setText(Html.fromHtml(e.length()
						+ "<font color='#9d9d9e'>/150</font>"));
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_releaseActiveActivity_release:
			if (preferences.getString("mobilePhone", null) == null
					|| preferences.getString("mobilePhone", null).equals("")
					|| preferences.getString("mobilePhone", null)
							.equals("null")) {
				Toasts.show(this, "请先绑定手机号码", 0);
				Intent intent = new Intent(ReleaseActiveActivity.this,
						BindPhoneActivity.class);
				intent.putExtra("userId", preferences.getInt("userId", -1));
				intent.putExtra("uniqueKey",
						preferences.getString("uniqueKey", null));
				startActivityForResult(intent, 4);
				break;
			}

			isReleaseOK();
			break;

		case R.id.layout_releaseActiveActivity_date:
			View view = getWindow().peekDecorView();
			if (view != null) {
				InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
			showPopupWindowData(v);
			break;

		case R.id.layout_releaseActiveActivity_time:
			View view2 = getWindow().peekDecorView();
			if (view2 != null) {
				InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputmanger.hideSoftInputFromWindow(view2.getWindowToken(), 0);
			}
			showPopupWindowTime(v);
			break;

		case R.id.img_releaseActiveActivity_add:
			/*
			 * startActivity(new Intent(ReleaseActiveActivity.this,
			 * SelectImagesActivity.class));
			 */

			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
			i.setType("image/*");
			startActivityForResult(i, 1);

			break;

		case R.id.layout_releaseActiveActivity_back:
			finish();
			break;

		case R.id.layout_releaseActiveActivity_routeOverlay:
			startActivity(new Intent(ReleaseActiveActivity.this,
					RouteOverlayActivity.class).putExtra("isFrom", "activity"));
			break;

		default:
			break;
		}
	}

	private void isReleaseOK() {
		if (themeEdt.getText() == null
				|| timeTxt.getText().equals("请选择天数  请选择小时数")
				|| dateTxt.getText().equals("请选择日期  请选择时间")
				|| signatureEdt.getText() == null) {
			new SystemUtil().makeToast(this, "必填项不能为空");
		} else {

			if (!falg) {
				Toasts.show(this, "正在发布，请稍候", 0);
				// new SystemUtil().makeToast(this, "正在发布，请稍候");
			}
			falg = false;
			pDialog = ProgressDialog.show(this, "请稍等", "正在发布");
			// Toasts.show(this, "正在发布，请稍候", 0);
			// new SystemUtil().makeToast(this, "正在发布，请稍候");
			if (bitList.size() != 0) {
				photoList = new ArrayList<String>();
				uploadingNum = 0;
				try {
					new SystemUtil().httpClient(
							saveMyBitmap(bitList.get(uploadingNum)),
							preferences, handler, "HD");
				} catch (IOException e) {
					e.printStackTrace();
				}
				// releasePictue();
			} else {
				postAll("");
			}
		}
	}

	private void setAddView(final Bitmap photo, final int number) {
		LinearLayout linearLayout = pictureLayout;
		final FrameLayout frameLayout = new FrameLayout(this);
		LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(dp2px(
				this, 100f), LinearLayout.LayoutParams.MATCH_PARENT);
		fp.setMargins(dp2px(this, 16f), 0, 0, 0);
		frameLayout.setLayoutParams(fp);

		ImageView picture = new ImageView(this);
		picture.setScaleType(ScaleType.CENTER_CROP);
		picture.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		picture.setScaleType(ImageView.ScaleType.CENTER_CROP);

		// Toast.makeText(this, uri + "", 0).show();
		picture.setImageBitmap(photo);
		bitList.add(photo);

		ImageView delete = new ImageView(this);
		FrameLayout.LayoutParams deleteParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		deleteParams.gravity = Gravity.RIGHT;
		deleteParams.setMargins(0, dp2px(this, 6f), dp2px(this, 6f), 0);
		delete.setLayoutParams(deleteParams);

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				bitList.remove(photo);
				frameLayout.setVisibility(View.GONE);

				num--;
				if (num != 3) {
					addImg.setVisibility(View.VISIBLE);
				}
			}
		});
		delete.setImageResource(R.drawable.red_delete);

		frameLayout.addView(picture);
		frameLayout.addView(delete);
		linearLayout.addView(frameLayout);
	}

	/**
	 * 上传图片
	 */
	/*
	 * private void releasePictue() { RequestParams params = new
	 * RequestParams();
	 * 
	 * params.addBodyParameter("files", ""); params.addBodyParameter("type",
	 * "HD"); params.addBodyParameter("uniqueKey",
	 * preferences.getString("uniqueKey", null));
	 * 
	 * xUtilsUtil.httpPost("common/uploadImage.html", params, new CallBackPost()
	 * {
	 * 
	 * @Override public void onMySuccess(ResponseInfo<String> responseInfo) {
	 * 
	 * // 图片名称 String picName = "";
	 * 
	 * postAll(picName); }
	 * 
	 * @Override public void onMyFailure(HttpException error, String msg) {
	 * 
	 * } });
	 * 
	 * }
	 */
	/**
	 * 上传所有信息
	 */
	private void postAll(String picName) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("activityTitle", themeEdt.getText().toString());
		params.addBodyParameter("activityMemo", signatureEdt.getText()
				.toString());
		params.addBodyParameter("startDate", dateTxt.getText().toString()
				+ ":00");
		params.addBodyParameter("duration", (days * 24 + hours) * 60 + "");
		params.addBodyParameter("lanInfo", lanInfo);
		params.addBodyParameter("lanName", lanName);
		if (mileage != null) {
			params.addBodyParameter("mileage", new DecimalFormat("#0.00")
					.format(Double.parseDouble(mileage)));
		}
		// activityImage 图片(多张用逗号隔开)
		params.addBodyParameter("activityImage", picName);
		params.addBodyParameter("location", "");
		params.addBodyParameter("outlay", moneyTxt.getText().toString());
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));

		xUtilsUtil.httpPost("mobile/activity/publishActivity.html", params,
				new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						String s = responseInfo.result;
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(s);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						falg = true;
						pDialog.dismiss();
						if (jsonObject.optBoolean("result")) {
							Toasts.show(getApplicationContext(), "发布成功", 0);

							// new
							// SystemUtil().makeToast(ReleaseActiveActivity.this,
							// "发布成功");
							ReleaseActiveActivity.this.finish();
						} else {
							Toasts.show(getApplicationContext(), "result:"
									+ jsonObject.optString("message"), 0);
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						pDialog.dismiss();
						falg = true;
						Toasts.show(getApplicationContext(), "Xutils发送失败:"
								+ msg, 0);
					}
				});
	}

	/**
	 * 回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case 1:
				if (null != data) {
					startPhotoZoom(data.getData());
				}
				break;
			case 2:
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap photo = extras.getParcelable("data");
						setAddView(photo, num);
						num++;
						if (num == 3) {
							addImg.setVisibility(View.GONE);
							break;
						}
					}
				}
				break;
			case 4:
				isReleaseOK();
				Toasts.show(this, "绑定成功", 0);
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		if (uri == null) {
			return;
		}
		/*
		 * String[] proj = { MediaStore.Images.Media.DATA };
		 * 
		 * @SuppressWarnings("deprecation") Cursor actualimagecursor =
		 * managedQuery(uri, proj, null, null, null); int
		 * actual_image_column_index = actualimagecursor
		 * .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		 * actualimagecursor.moveToFirst(); img_path =
		 * actualimagecursor.getString(actual_image_column_index);
		 */
		// System.out.println(img_path);

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		// 设置裁剪
		intent.putExtra("crop", true);
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
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

	String time;
	String datas;
	boolean isSelect = false;

	private void showPopupWindowData(View view) {
		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.item_popup_datatimepicker, null);

		final DatePicker datePicker = (DatePicker) contentView
				.findViewById(R.id.dpPicker);
		final TimePicker timePicker = (TimePicker) contentView
				.findViewById(R.id.tpPicker);
		TextView mBtnConfirm = (TextView) contentView
				.findViewById(R.id.datatimepicker_ok);

		Calendar minCalendar = Calendar.getInstance();
		minCalendar.set(Calendar.HOUR_OF_DAY, 0);
		minCalendar.set(Calendar.MINUTE, 0);
		minCalendar.set(Calendar.SECOND, 0);
		datePicker.setMinDate(minCalendar.getTimeInMillis());

		final Time t = new Time();
		t.setToNow();

		LayoutParams params1 = new LayoutParams(100, LayoutParams.WRAP_CONTENT);
		params1.leftMargin = 10;
		params1.rightMargin = 10;
		LayoutParams params2 = new LayoutParams(76, LayoutParams.WRAP_CONTENT);
		params2.rightMargin = 10;

		((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0))
				.getChildAt(0).setLayoutParams(params1);
		((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0))
				.getChildAt(1).setLayoutParams(params2);
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		datePicker.init(minCalendar.get(Calendar.YEAR),
				minCalendar.get(Calendar.MONTH),
				minCalendar.get(Calendar.DATE), new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Calendar calendar = Calendar.getInstance();
						calendar.set(year, monthOfYear, dayOfMonth);
						SimpleDateFormat format = new SimpleDateFormat(
								"yyyy-MM-dd");
						datas = format.format(calendar.getTime()) + " ";

						try {
							Date now = df.parse(nowDate());
							Date date = df.parse(format.format(calendar
									.getTime()));
							if (now.getTime() < date.getTime()) {
								isSelect = true;
							} else {
								isSelect = false;
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}

					}
				});

		timePicker.setIs24HourView(true);
		timePicker
				.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
					@Override
					public void onTimeChanged(TimePicker view, int hourOfDay,
							int minute) {
						if (!isSelect) {
							if (hourOfDay < t.hour) {
								timePicker.setCurrentHour(t.hour);
							}
							if (hourOfDay == t.hour && minute < t.minute) {
								timePicker.setCurrentMinute(t.minute);
							}
						}
						if (minute < 10) {
							time = hourOfDay + ":0" + minute;
						} else {
							time = hourOfDay + ":" + minute;
						}
					}
				});

		final PopupWindow popupWindow = new PopupWindow(contentView,
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

				if (datas == null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					datas = format.format(new Date()) + " ";
					/*
					 * datas = String.valueOf(datePicker.getYear()) + "-" +
					 * (String.valueOf(datePicker.getMonth()) + 1) + "-" +
					 * String.valueOf(datePicker.getDayOfMonth()) + " ";
					 */
				}
				if (time == null) {
					SimpleDateFormat format = new SimpleDateFormat("HH:mm");
					time = format.format(new Date().getTime());

					/*
					 * time = String.valueOf(timePicker.getCurrentHour()) + ":"
					 * + String.valueOf(timePicker.getCurrentMinute());
					 */
				}
				data = datas + time;

				dateTxt.setText(data);
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

	String day;
	String hour;

	private void showPopupWindowTime(View view) {
		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.item_popup_timepicker, null);

		NumberPicker dayPicker = (NumberPicker) contentView
				.findViewById(R.id.daypicker);
		NumberPicker hourPicker = (NumberPicker) contentView
				.findViewById(R.id.houricker);
		TextView mBtnConfirm = (TextView) contentView
				.findViewById(R.id.timepicker_ok);

		dayPicker.setMaxValue(30);
		dayPicker.setMinValue(0);
		dayPicker.setValue(0);

		hourPicker.setMaxValue(24);
		hourPicker.setMinValue(0);
		hourPicker.setValue(0);

		dayPicker.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker arg0, int oldVal, int newVal) {
				days = newVal;
				day = newVal + "天";
			}
		});

		hourPicker.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker arg0, int oldVal, int newVal) {
				hours = newVal;
				hour = newVal + "小时";
			}
		});

		final PopupWindow popupWindow = new PopupWindow(contentView,
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
				if (day == null) {
					days = 0;
					day = "0天  ";
				}
				if (hour == null) {
					hours = 0;
					hour = "0小时";
				}
				duration = day + hour;
				timeTxt.setText(duration);
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

	/**
	 * 
	 * 获取系统时间
	 * 
	 */
	public String nowDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		return df.format(new Date());
	}

	@Override
	protected void onResume() {

		isFirst = getIntent().getBooleanExtra("isFirst", false);
		if (isFirst) {
			photoList = getIntent().getStringArrayListExtra("photoList");
			isFirst = false;
		}

		if (mileage != null) {
			mileageTxt.setText(new DecimalFormat("0.00").format(Double
					.parseDouble(mileage)) + "KM");

			findViewById(R.id.txt_releaseActiveActivity_reOverlay)
					.setVisibility(View.VISIBLE);
		} else {
			mileageTxt.setText("0.00" + "KM");
			findViewById(R.id.txt_releaseActiveActivity_reOverlay)
					.setVisibility(View.GONE);
		}

		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	
}
