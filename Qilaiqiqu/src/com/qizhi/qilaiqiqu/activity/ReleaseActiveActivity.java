package com.qizhi.qilaiqiqu.activity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

public class ReleaseActiveActivity extends Activity implements OnClickListener {

	private TextView timeTxt;
	private TextView dateTxt;

	private EditText themeEdt;
	private EditText signatureEdt;
	private EditText moneyTxt;

	private TextView signatureTxt;
	private TextView releaseTxt;
	private TextView memoTxt;

	private LinearLayout dateLayout;
	private LinearLayout timeLayout;
	private LinearLayout backLayout;

	private LinearLayout pictureLayout;

	// private GridView pictureGrid;

	XUtilsUtil xUtilsUtil;

	SharedPreferences preferences;

	// private ReleaseActiveGridAdapter adapter;

	private ImageView addImg;

	private ArrayList<String> photoList;
	private boolean isFirst = false;

	private String data;
	private String duration;

	int num = 0;

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
		signatureEdt = (EditText) findViewById(R.id.edt_releaseActiveActivity_signature);

		signatureTxt = (TextView) findViewById(R.id.txt_releaseActiveActivity_textLengh);
		releaseTxt = (TextView) findViewById(R.id.txt_releaseActiveActivity_release);
		memoTxt = (TextView) findViewById(R.id.txt_releaseActiveActivity_memoLengh);

		dateLayout = (LinearLayout) findViewById(R.id.layout_releaseActiveActivity_date);
		timeLayout = (LinearLayout) findViewById(R.id.layout_releaseActiveActivity_time);
		pictureLayout = (LinearLayout) findViewById(R.id.layout_releaseActiveActivity_picture);

		backLayout = (LinearLayout) findViewById(R.id.layout_releaseActiveActivity_back);

		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		addImg = (ImageView) findViewById(R.id.img_releaseActiveActivity_add);

		photoList = new ArrayList<String>();
		// isFirst = getIntent().getBooleanExtra("isFirst", false);
		// if (isFirst) {
		// photoList = getIntent().getStringArrayListExtra("photoList");
		// }

		// pictureGrid = (GridView)
		// findViewById(R.id.grid_releaseActiveActivity_picture);
		// adapter = new ReleaseActiveGridAdapter(photoList, this);
		// pictureGrid.setAdapter(adapter);
		// Toast.makeText(this, "list.size()" + photoList.size(), 0).show();
	}

	private void initEvent() {

		addImg.setOnClickListener(this);

		dateLayout.setOnClickListener(this);
		timeLayout.setOnClickListener(this);

		backLayout.setOnClickListener(this);

		releaseTxt.setOnClickListener(this);
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
			if (themeEdt.getText() == null
					|| timeTxt.getText().equals("请选择天数  请选择小时数")
					|| dateTxt.getText().equals("请选择日期  请选择时间")
					|| signatureEdt.getText() == null) {
				new SystemUtil().makeToast(this, "必填项不能为空");
			} else {
				releasePictue();
			}
			break;

		case R.id.layout_releaseActiveActivity_date:
			showPopupWindowData(v);
			break;

		case R.id.layout_releaseActiveActivity_time:
			showPopupWindowTime(v);
			break;

		case R.id.img_releaseActiveActivity_add:
			/*
			 * startActivity(new Intent(ReleaseActiveActivity.this,
			 * SelectImagesActivity.class));
			 */
			if (num >= 3) {
				addImg.setVisibility(View.GONE);
				break;
			}

			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
			i.setType("image/*");
			startActivityForResult(i, 1);
			break;

		case R.id.layout_releaseActiveActivity_back:
			finish();
			break;

		default:
			break;
		}
	}

	private void setAddView(String uri, Bitmap photo) {
		LinearLayout linearLayout = pictureLayout;
		final FrameLayout frameLayout = new FrameLayout(this);
		LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(dp2px(
				this, 100f), LinearLayout.LayoutParams.WRAP_CONTENT);
		fp.setMargins(dp2px(this, 16f), 0, 0, 0);
		frameLayout.setLayoutParams(fp);

		ImageView picture = new ImageView(this);
		picture.setScaleType(ScaleType.CENTER_CROP);
		picture.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		picture.setScaleType(ImageView.ScaleType.CENTER_CROP);

		Toast.makeText(this, uri + "", 0).show();
		picture.setImageBitmap(photo);

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
	private void releasePictue() {
		RequestParams params = new RequestParams();

		params.addBodyParameter("files", "");
		params.addBodyParameter("type", "HD");
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));

		xUtilsUtil.httpPost("common/uploadImage.html", params,
				new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {

						// 图片名称
						String picName = "";

						postAll(picName);
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {

					}
				});

	}

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
		params.addBodyParameter("duration", timeTxt.getText().toString());
		params.addBodyParameter("lanInfo", "");
		params.addBodyParameter("lanName", "");
		params.addBodyParameter("mileage", "");

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

					}

					@Override
					public void onMyFailure(HttpException error, String msg) {

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
						try {
							Bitmap photo = extras.getParcelable("data");
							setAddView(new SystemUtil().saveMyBitmap(photo),
									photo);
							num++;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
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

		((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0))
				.getChildAt(0).setVisibility(View.GONE);
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
					datas = String.valueOf(datePicker.getYear()) + "-"
							+ (String.valueOf(datePicker.getMonth()) + 1) + "-"
							+ String.valueOf(datePicker.getDayOfMonth()) + " ";
				}
				if (time == null) {
					time = String.valueOf(timePicker.getCurrentHour()) + ":"
							+ String.valueOf(timePicker.getCurrentMinute());
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
				day = newVal + "天";
			}
		});

		hourPicker.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker arg0, int oldVal, int newVal) {
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
					day = "0天  ";
				}
				if (hour == null) {
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

		super.onResume();
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

}
