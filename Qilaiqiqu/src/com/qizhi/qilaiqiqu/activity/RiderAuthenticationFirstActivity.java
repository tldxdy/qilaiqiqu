package com.qizhi.qilaiqiqu.activity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.ArrayWheelAdapter;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.widget.OnWheelChangedListener;
import com.qizhi.qilaiqiqu.widget.WheelView;
import com.umeng.analytics.MobclickAgent;

public class RiderAuthenticationFirstActivity extends BaseActivity implements
		OnClickListener, OnWheelChangedListener {

	public static Activity instanceFirst = null; 
	
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;

	private TextView cityTxt;
	private TextView provinceTxt;
	private TextView districtTxt;

	private TextView nextTxt;

	private EditText phoneTxt;

	private AssetManager asset;

	private Button mBtnConfirm;

	private LinearLayout layoutBack;
	private LinearLayout pictureLayout;
	private RelativeLayout addressLayout;

	private ImageView addImg;

	public static ArrayList<Bitmap> bitList;

	int num = 0;

	public static String riderPhone = null;
	public static String attendArea = null;
	public static String attendPrice = null;
	public static String attendPeriod = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_authentication_first_rider);
		asset = this.getAssets();
		instanceFirst = this;
		initView();
		initEvent();
	}

	private void initView() {
		addImg = (ImageView) findViewById(R.id.img_riderAuthenticationActivity_first_add);

		cityTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_first_city);
		provinceTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_first_province);
		districtTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_first_district);

		addressLayout = (RelativeLayout) findViewById(R.id.layout_riderAuthenticationActivity_first_address);

		phoneTxt = (EditText) findViewById(R.id.edt_riderAuthenticationActivity_first_phoneNum);

		nextTxt = (TextView) findViewById(R.id.txt_riderAuthenticationActivity_first_next);

		layoutBack = (LinearLayout) findViewById(R.id.layout_riderAuthenticationActivity_first_back);
		pictureLayout = (LinearLayout) findViewById(R.id.layout_riderAuthenticationActivity_first_picture);
		bitList = new ArrayList<Bitmap>();
	}

	private void initEvent() {
		addImg.setOnClickListener(this);
		nextTxt.setOnClickListener(this);
		layoutBack.setOnClickListener(this);
		addressLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_riderAuthenticationActivity_first_back:
			finish();
			break;

		case R.id.layout_riderAuthenticationActivity_first_address:
			showPopupWindow(v);
			break;

		case R.id.img_riderAuthenticationActivity_first_add:
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
			i.setType("image/*");
			startActivityForResult(i, 1);
			break;

		case R.id.txt_riderAuthenticationActivity_first_next:
			keepData();

			if (riderPhone.equals("") || attendArea.equals("")
					|| riderPhone.equals(null) || attendArea.equals(null)
					|| bitList.size() == 0) {
				Toasts.show(RiderAuthenticationFirstActivity.this, "你还有未填项哦", 0);
			} else {
				if (isMobileNO(riderPhone)) {
					startActivity(new Intent(
							RiderAuthenticationFirstActivity.this,
							RiderAuthenticationSecondActivity.class));
				} else {
					Toasts.show(RiderAuthenticationFirstActivity.this,
							"手机号码格式不正确", 0);
				}
			}
			break;

		default:
			break;
		}
	}

	private void keepData() {
		riderPhone = phoneTxt.getText().toString();
		attendArea = mCurrentProviceName + "" + mCurrentCityName + ""
				+ mCurrentDistrictName;
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
				} else {
					addImg.setVisibility(View.GONE);
				}
			}
		});
		delete.setImageResource(R.drawable.red_delete);

		frameLayout.addView(picture);
		frameLayout.addView(delete);
		linearLayout.addView(frameLayout);
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

	private void showPopupWindow(View view) {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.item_popup_address, null);

		mViewProvince = (WheelView) contentView.findViewById(R.id.id_province);
		mViewCity = (WheelView) contentView.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) contentView.findViewById(R.id.id_district);
		mBtnConfirm = (Button) contentView
				.findViewById(R.id.btn_addressPopup_confirm);

		mViewProvince.addChangingListener(this);
		mViewCity.addChangingListener(this);
		mViewDistrict.addChangingListener(this);

		setUpData();

		final PopupWindow popupWindow = new PopupWindow(contentView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

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

		mBtnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showSelectedResult();
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

	private void setUpData() {
		initProvinceDatas(asset);
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				RiderAuthenticationFirstActivity.this, mProvinceDatas));
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	/**
	 * @param mCurrentCityName
	 *            选择的城市名
	 * @param mCurrentProviceName
	 *            选择的省份名
	 * @param mCurrentDistrictName
	 *            选择的县份名
	 */
	private void showSelectedResult() {
		cityTxt.setText(mCurrentCityName);
		provinceTxt.setText(mCurrentProviceName);
		districtTxt.setText(mCurrentDistrictName);

	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("(^1(3[0-9]|5[0-35-9]|8[025-9])\\d{8}$)"
				+ "|(^1(34[0-8]|(3[5-9]|5[017-9]|8[278])\\d)\\d{7}$)"
				+ "|(^1(3[0-2]|5[256]|8[56])\\d{8}$)"
				+ "|(^1((33|53|8[09])[0-9]|349)\\d{7}$)"
				+ "|(^18[09]\\d{8}$)|(^(180|189|133|134|153)\\d{8}$)"
				+ "|(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)");
		Matcher m = p.matcher(mobiles);
		return m.matches();
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
