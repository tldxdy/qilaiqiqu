package com.qizhi.qilaiqiqu.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.ArrayWheelAdapter;
import com.qizhi.qilaiqiqu.model.CertainUserModel;
import com.qizhi.qilaiqiqu.ui.ActionSheetDialog;
import com.qizhi.qilaiqiqu.ui.ActionSheetDialog.OnSheetItemClickListener;
import com.qizhi.qilaiqiqu.ui.ActionSheetDialog.SheetItemColor;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.qizhi.qilaiqiqu.widget.OnWheelChangedListener;
import com.qizhi.qilaiqiqu.widget.WheelView;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * 
 * @author leiqian
 * 
 */

public class PersonalDataActivity extends BaseActivity implements
		OnClickListener, OnWheelChangedListener, TextWatcher, CallBackPost {

	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;

	private Button mBtnConfirm;

	private TextView confirmTxt;
	private TextView provinceTxt;
	private TextView cityTxt;
	private TextView DistrictTxt;
	private TextView signatureTxt;
	private TextView usernameTxt;
	private TextView fansTxt;
	private TextView careTxt;

	private EditText nickEdt;
	private EditText signatureEdt;

	private ImageView maleImg;
	private ImageView famaleImg;
	private ImageView photoImg;

	private RelativeLayout addressLayout;
	private RelativeLayout bindPhoneLayout;
	private LinearLayout backLayout;
	private LinearLayout photoLayout;
	private LinearLayout fansLayout;
	private LinearLayout careLayout;

	private AssetManager asset;

	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int IMAGE_REQUEST_CODE = 2;
	private static final int RESULT_REQUEST_CODE = 3;
	private static final int BING_PHONE = 4;

	private XUtilsUtil xUtilsUtil;

	private String sexString = null;

	private String img_path = null;

	private SharedPreferences preferences;

	private CertainUserModel certainUserModel;
	
	private boolean path = true;

	private boolean falg = true;
	private ProgressDialog pDialog;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String s = (String) msg.obj;
				certainUserModel.setUserImage(s);
				img_path = null;
				informationUpdate();
				break;
			case 2:
				img_path = null;
				// finish();
				path = false;
				falg = true;
				informationUpdate();
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
		setContentView(R.layout.activity_personal_data);
		asset = this.getAssets();
		initView();
		initEvnet();
		sdfsfsdfsd();
		data();
	}

	private void sdfsfsdfsd() {
		// 将输入法隐藏
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(signatureEdt.getWindowToken(), 0);
	}

	private void initView() {
		xUtilsUtil = new XUtilsUtil();
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		certainUserModel = new CertainUserModel();

		confirmTxt = (TextView) findViewById(R.id.txt_personalDataActivity_confirm);
		provinceTxt = (TextView) findViewById(R.id.txt_personalDataActivity_province);
		cityTxt = (TextView) findViewById(R.id.txt_personalDataActivity_city);
		DistrictTxt = (TextView) findViewById(R.id.txt_personalDataActivity_district);
		signatureTxt = (TextView) findViewById(R.id.txt_personalDataActivity_signature);
		careTxt = (TextView) findViewById(R.id.txt_personalDataActivity_care);
		fansTxt = (TextView) findViewById(R.id.txt_personalDataActivity_fans);

		nickEdt = (EditText) findViewById(R.id.edt_personalDataActivity_nick);
		usernameTxt = (TextView) findViewById(R.id.txt_personalDataActivity_usernametxt);
		signatureEdt = (EditText) findViewById(R.id.edt_personalDataActivity_signature);

		maleImg = (ImageView) findViewById(R.id.img_personalDataActivity_male);
		famaleImg = (ImageView) findViewById(R.id.img_personalDataActivity_famale);
		photoImg = (ImageView) findViewById(R.id.img_personalDataActivity_photo);

		fansLayout = (LinearLayout) findViewById(R.id.layout_personalDataActivity_fans);
		careLayout = (LinearLayout) findViewById(R.id.layout_personalDataActivity_care);
		backLayout = (LinearLayout) findViewById(R.id.layout_personalDataActivity_back);
		photoLayout = (LinearLayout) findViewById(R.id.layout_personalDataActivity_photo);
		addressLayout = (RelativeLayout) findViewById(R.id.layout_personalDataActivity_address);
		bindPhoneLayout = (RelativeLayout) findViewById(R.id.layout_personalDataActivity_bindphone);

	}

	private void initEvnet() {
		backLayout.setOnClickListener(this);
		maleImg.setOnClickListener(this);
		famaleImg.setOnClickListener(this);
		confirmTxt.setOnClickListener(this);
		addressLayout.setOnClickListener(this);
		signatureEdt.addTextChangedListener(this);
		photoLayout.setOnClickListener(this);
		careLayout.setOnClickListener(this);
		fansLayout.setOnClickListener(this);
		bindPhoneLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_personalDataActivity_male:
			sexString = "M";
			maleImg.setImageResource(R.drawable.male_chosen);
			famaleImg.setImageResource(R.drawable.famale_unchosen);
			break;

		case R.id.img_personalDataActivity_famale:
			sexString = "F";
			famaleImg.setImageResource(R.drawable.famale_chosen);
			maleImg.setImageResource(R.drawable.male_unchosen);
			break;

		case R.id.txt_personalDataActivity_confirm:
			setViewData();
			break;

		case R.id.layout_personalDataActivity_address:
			showPopupWindow(v);
			break;

		case R.id.layout_personalDataActivity_back:
			finish();
			break;

		case R.id.layout_personalDataActivity_photo:
			popCheck();
			//showPopupWindow2(v);
			break;
			
		case R.id.layout_personalDataActivity_fans:
			startActivity(new Intent(PersonalDataActivity.this,
					FriendActivity.class).putExtra("friendFlag", 0));
			break;

		case R.id.layout_personalDataActivity_care:
			startActivity(new Intent(PersonalDataActivity.this,
					FriendActivity.class).putExtra("friendFlag", 1));
			break;

		case R.id.layout_personalDataActivity_bindphone:
			if(preferences.getInt("userId", -1) != -1 && "".equals(usernameTxt.getText().toString().trim())){
				 Intent intent = new Intent(PersonalDataActivity.this,
						 BindPhoneActivity.class); intent.putExtra("userId",
						 preferences.getInt("userId", -1)); 
						 intent.putExtra("uniqueKey",preferences.getString("uniqueKey", null));
						 startActivityForResult(intent, BING_PHONE);
			}
			break;

		default:
			break;
		}
	}

	public void popCheck() {

		new ActionSheetDialog(this).builder().setCancelable(false).setCanceledOnTouchOutside(false)
				.addSheetItem("拍照", SheetItemColor.Blue, new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						openCamera();
						
					}
				}).addSheetItem("打开相册", SheetItemColor.Blue, new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						
						skipPic();

					}
				}).show();
	}
	

	/** 打开相册 */
	private void skipPic() {
		Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
		pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(pickIntent, IMAGE_REQUEST_CODE);
	}

	/** 指定拍摄图片文件位置避免获取到缩略图 */
	File outFile;

	/** 打开相机 */
	private void openCamera() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File outDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			if (!outDir.exists()) {
				outDir.mkdirs();
			}
			outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, CAMERA_REQUEST_CODE);
		} else {
			Toasts.show(this, "请确认已经插入SD卡", 0);
		}
	}
	
	
	
	

	private void setViewData() {
		pDialog = ProgressDialog.show(this, "请稍等", "正在修改");
		if (!falg) {
			return;
		}
		falg = false;
		if (img_path != null) {
			new SystemUtil().httpClient(img_path, preferences, handler, "USER");
			//photoUploading();
		} else {
			informationUpdate();
		}
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
				PersonalDataActivity.this, mProvinceDatas));
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

	private void showSelectedResult() {
		provinceTxt.setText(mCurrentProviceName);
		cityTxt.setText(mCurrentCityName);
		DistrictTxt.setText(mCurrentDistrictName);

	}

	@Override
	public void afterTextChanged(Editable e) {
		signatureTxt.setText(Html.fromHtml(e.length()
				+ "<font color='#9d9d9e'>/50</font>"));

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}
	/**
	 * 回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case CAMERA_REQUEST_CODE:
				startPhotoZoom(Uri.fromFile(outFile));
				break;
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					setImageToView(data);
				}
				break;
			case BING_PHONE:
				if (data != null) {
					usernameTxt.setText(data.getStringExtra("mobilePhone"));
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
		Intent intent = new Intent("com.android.camera.action.CROP")
		.setDataAndType(uri, "image/*").putExtra("crop", "true")
		.putExtra("return-data", true);
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(getResources(), photo);
			photoImg.setImageDrawable(drawable);
			try {
				img_path = saveMyBitmap(photo);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// photoImg.setImageDrawable(drawable);
			/*BitmapUtils bitmapUtils = new BitmapUtils(this);
			bitmapUtils.display(photoImg, img_path);*/
			// photoImg.setImageBitmap(photo);
		}
	}
	
	
	@SuppressLint("NewApi")
	public String saveMyBitmap(Bitmap mBitmap) throws IOException {
		File outDir = null;
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			// 这个路径，在手机内存下创建一个pictures的文件夹，把图片存在其中。
			outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
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
		}else{
			Toasts.show(this, "请确认已经插入SD卡", 0);
		}
		return null;
	}
	private void informationUpdate() {
		if (!"".equals(nickEdt.getText().toString().trim())
				&& !"".equals(certainUserModel.getUserImage())) {
			RequestParams params = new RequestParams("UTF-8");
			params.addBodyParameter("userId", preferences.getInt("userId", -1)
					+ "");
			params.addBodyParameter("uniqueKey",
					preferences.getString("uniqueKey", null));
			params.addBodyParameter("userName", nickEdt.getText().toString()
					.trim());
			params.addBodyParameter("address", provinceTxt.getText().toString()
					.trim()
					+ ","
					+ cityTxt.getText().toString().trim()
					+ ","
					+ DistrictTxt.getText().toString().trim());
			params.addBodyParameter("userMemo", signatureEdt.getText()
					.toString().trim());
			params.addBodyParameter("sex", sexString);
			params.addBodyParameter("mobilePhone", usernameTxt.getText()
					.toString().trim());
			params.addBodyParameter("userImage",
					certainUserModel.getUserImage());
			xUtilsUtil.httpPost("mobile/user/updateUserInfo.html", params,
					new CallBackPost() {

						@Override
						public void onMySuccess(
								ResponseInfo<String> responseInfo) {
							
							final Editor editor = preferences.edit();// 获取编辑器
							editor.putString("userImage", certainUserModel.getUserImage());
							editor.commit();
							if(path){
								Toasts.show(PersonalDataActivity.this, "修改成功", 0);
							}else{
								Toasts.show(PersonalDataActivity.this, "用户头像修改失败", 0);
								path = true;
							}
							falg = true;
							// 更新环信用户昵称
							EMChatManager.getInstance().updateCurrentUserNick(
									nickEdt.getText().toString().trim());
							new Handler().postDelayed(new Runnable() {

								@Override
								public void run() {
									pDialog.dismiss();
									PersonalDataActivity.this.finish();
								}
							}, 500);
							
						}

						@Override
						public void onMyFailure(HttpException error, String msg) {
							pDialog.dismiss();
							Toasts.show(PersonalDataActivity.this, "请求失败"
									+ error + ":" + msg, 0);
							falg = true;
						}
					});
		}
	}

	private void data() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		xUtilsUtil.httpPost("common/queryCertainUser.html", params, this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		String s = responseInfo.result;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(s);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (jsonObject.optBoolean("result")) {
			certainUserModel = new Gson().fromJson(
					jsonObject.optJSONObject("data").toString(),
					CertainUserModel.class);
			nickEdt.setText(certainUserModel.getUserName());
			usernameTxt.setText(certainUserModel.getMobilePhone());

			fansTxt.setText(certainUserModel.getFansNum() + "");
			careTxt.setText(certainUserModel.getConcernNum() + "");
			if ("null".equals(certainUserModel.getUserMemo())
					|| "".equals(certainUserModel.getUserMemo())) {

			} else {
				signatureEdt.setText(certainUserModel.getUserMemo());
				int n = 0;
				if (certainUserModel.getUserMemo() != null) {
					n = certainUserModel.getUserMemo().length();
				}
				signatureTxt.setText(Html.fromHtml(n
						+ "<font color='#9d9d9e'>/50</font>"));
			}
			if ("M".equals(certainUserModel.getSex())) {
				sexString = "M";
				maleImg.setImageResource(R.drawable.male_chosen);
				famaleImg.setImageResource(R.drawable.famale_unchosen);
			} else if ("F".equals(certainUserModel.getSex())) {
				sexString = "F";
				famaleImg.setImageResource(R.drawable.famale_chosen);
				maleImg.setImageResource(R.drawable.male_unchosen);
			} else {
				maleImg.setImageResource(R.drawable.male_unchosen);
				famaleImg.setImageResource(R.drawable.famale_unchosen);
				sexString = "Q";
			}
			if ("null".equals(certainUserModel.getAddress())
					|| "".equals(certainUserModel.getAddress())
					|| certainUserModel.getAddress() == null) {
				provinceTxt.setText("");
				cityTxt.setText("");
				DistrictTxt.setText("");
			} else {
				String[] ss = certainUserModel.getAddress().split(",|，|\\|");
				if (ss.length > 2) {
					provinceTxt.setText(ss[0]);
					cityTxt.setText(ss[1]);
					DistrictTxt.setText(ss[2]);
				}
			}
			SystemUtil.Imagexutils(certainUserModel.getUserImage(),
					photoImg, this);
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}
	
/*
	private void showPopupWindow2(View view) {

		// 一个自定义的布局，作为显示的内容
		View mview = LayoutInflater.from(this).inflate(
				R.layout.popup_personaldataactivity, null);

		cancelBtn = (Button) mview
				.findViewById(R.id.btn_personaldataactivity_cancel);
		photographBtn = (Button) mview
				.findViewById(R.id.btn_personaldataactivity_photograph);
		phoneBtn = (Button) mview
				.findViewById(R.id.btn_personaldataactivity_phone);
		LinearLayout quxiao = (LinearLayout) mview.findViewById(R.id.quxiao);

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
		phoneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
				i.setType("image/*");
				startActivityForResult(i, IMAGE_REQUEST_CODE);
			}
		});
		photographBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				//openCamera();
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
					startActivityForResult(intent, CAMERA_REQUEST_CODE);
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupWindow.dismiss();
			}
		});
		
		quxiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, Gravity.BOTTOM);

	}*/
}
