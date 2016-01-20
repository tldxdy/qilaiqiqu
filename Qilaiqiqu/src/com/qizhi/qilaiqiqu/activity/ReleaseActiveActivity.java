package com.qizhi.qilaiqiqu.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
import android.view.Window;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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

public class ReleaseActiveActivity extends Activity implements OnClickListener,
		CallBackPost {

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

	private LinearLayout pictureLayout;

	// private GridView pictureGrid;

	XUtilsUtil xUtilsUtil;

	SharedPreferences preferences;

	// private ReleaseActiveGridAdapter adapter;

	private ImageView addImg;

	private ArrayList<String> photoList;
	private boolean isFirst = false;

	private String data;

	boolean isPut = false;

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

			break;

		case R.id.layout_releaseActiveActivity_date:
			showPopupWindowData(v);
			break;

		case R.id.layout_releaseActiveActivity_time:

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
		picture.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		picture.setScaleType(ImageView.ScaleType.CENTER_CROP);

		// Picasso.with(this).load("file//"+uri).into(picture);
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
			}
		});
		delete.setImageResource(R.drawable.red_delete);

		frameLayout.addView(picture);
		frameLayout.addView(delete);
		linearLayout.addView(frameLayout);
	}

	private void release() {
		RequestParams params2 = new RequestParams();
		params2.addBodyParameter("userId", preferences.getInt("userId", -1)
				+ "");
		params2.addBodyParameter("activityTitle", themeEdt.getText().toString());
		params2.addBodyParameter("activityMemo", signatureEdt.getText()
				.toString());
		params2.addBodyParameter("startDate", "");
		params2.addBodyParameter("duration", "");
		params2.addBodyParameter("lanInfo", "");
		params2.addBodyParameter("lanName", "");
		params2.addBodyParameter("mileage", "");
		params2.addBodyParameter("activityImage", "");
		params2.addBodyParameter("location", "");
		params2.addBodyParameter("outlay", moneyTxt.getText().toString());
		params2.addBodyParameter("uniqueKey", "");

		xUtilsUtil.httpPost("mobile/activity/publishActivity.html", params2,
				this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {

	}

	@Override
	public void onMyFailure(HttpException error, String msg) {

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
						} catch (IOException e) {
							// TODO Auto-generated catch block
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
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 4.1);
		intent.putExtra("aspectY", 4);
		// outputX outputY 是裁剪图片宽高
		// intent.putExtra("outputX", 480);
		// intent.putExtra("outputY", 480);
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

		DatePicker datePicker = (DatePicker) contentView
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

		timePicker.setIs24HourView(true);
		datePicker.init(2016, 1, 1, new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, monthOfYear, dayOfMonth);
				SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
				datas = format.format(calendar.getTime());

			}
		});

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

						time = hourOfDay + ":" + minute;
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
					datas = "请选择日期  ";
				}
				if (time == null) {
					time = "请选择时间";
				}
				if (!datas.equals("请选择日期") && !time.equals("请选择时间")) {
					isPut = true;
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

	private void showPopupWindowTime(View view) {
		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.item_popup_datatimepicker, null);

		DatePicker datePicker = (DatePicker) contentView
				.findViewById(R.id.dpPicker);
		TimePicker timePicker = (TimePicker) contentView
				.findViewById(R.id.tpPicker);
		TextView mBtnConfirm = (TextView) contentView
				.findViewById(R.id.datatimepicker_ok);

		Calendar minCalendar = Calendar.getInstance();
		minCalendar.set(Calendar.HOUR_OF_DAY, 0);
		minCalendar.set(Calendar.MINUTE, 0);
		minCalendar.set(Calendar.SECOND, 0);
		datePicker.setMinDate(minCalendar.getTimeInMillis());
		datePicker.init(2016, 1, 1, new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, monthOfYear, dayOfMonth);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
				datas = format.format(calendar.getTime());
			}
		});
		timePicker.setIs24HourView(true);
		timePicker
				.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
					@Override
					public void onTimeChanged(TimePicker view, int hourOfDay,
							int minute) {

						time = hourOfDay + ":" + minute;
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
					datas = "请选择日期  ";
				}
				if (time == null) {
					datas = "请选择时间";
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
