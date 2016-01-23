package com.qizhi.qilaiqiqu.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.ActionCenterActivity;
import com.qizhi.qilaiqiqu.activity.CollectActivity;
import com.qizhi.qilaiqiqu.activity.LoginActivity;
import com.qizhi.qilaiqiqu.activity.MessageCenterActivity;
import com.qizhi.qilaiqiqu.activity.PersonalDataActivity;
import com.qizhi.qilaiqiqu.activity.RidingActivity;
import com.qizhi.qilaiqiqu.activity.SetActivity;
import com.qizhi.qilaiqiqu.model.PersonageUserInformationModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

/**
 * 
 * @author hujianbo
 * 
 */
@SuppressLint("ValidFragment")
public class MenuLeftFragment extends Fragment implements OnClickListener {

	private LinearLayout layout_personal_message; // 个人信息
	private RelativeLayout layout_my_message; // 我的消息
	private RelativeLayout layout_my_travel_notes; // 我的骑游记
	private RelativeLayout layout_my_collect_press; // 我的收藏
	private RelativeLayout layout_my_set; // 设置
	private RelativeLayout layout_my_action_center; // 活动中心
	private LinearLayout layout_back; // f返回

	private TextView userNameTxt;
	private TextView userIntegralTxt; // 积分
	private TextView userFansTxt; // 粉丝
	private TextView userConcernTxt; // 用户关注
	private TextView userMemoTxt; // 用户个性
	private TextView informationNumTxt;		//系统消息数

	private ImageView userSexImg;
	private ImageView userImageImg;

	private Context context;

	private View view;

	private SlidingMenu menu;
	
	private PersonageUserInformationModel puim;
	
	private SharedPreferences preferences;
	
	private XUtilsUtil xUtilsUtil;

	public MenuLeftFragment(Context context, SlidingMenu menu,
			SharedPreferences preferences) {
		this.context = context;
		this.menu = menu;
		this.preferences = preferences;
		

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.fragment_personal_center,
				null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		layout_personal_message = (LinearLayout) view
				.findViewById(R.id.layout_personalfragment_personal_message);
		layout_my_travel_notes = (RelativeLayout) view
				.findViewById(R.id.layout_personalfragment_my_travel_notes);
		layout_my_message = (RelativeLayout) view
				.findViewById(R.id.layout_personalfragment_my_message);
		layout_my_collect_press = (RelativeLayout) view
				.findViewById(R.id.layout_personalfragment_my_collect_press);
		layout_my_action_center = (RelativeLayout) view
				.findViewById(R.id.layout_personalfragment_my_activity_center);
		layout_my_set = (RelativeLayout) view
				.findViewById(R.id.layout_personalfragment_my_set);
		layout_back = (LinearLayout) view
				.findViewById(R.id.layout_personalfragment_back);
		userNameTxt = (TextView) view
				.findViewById(R.id.txt_personalfragment_name);
		userIntegralTxt = (TextView) view
				.findViewById(R.id.txt_personalfragment_integral);
		userFansTxt = (TextView) view
				.findViewById(R.id.txt_personalfragment_fans);
		userConcernTxt = (TextView) view
				.findViewById(R.id.txt_personalfragment_concern);
		userMemoTxt = (TextView) view
				.findViewById(R.id.txt_personalfragment_signature);
		informationNumTxt = (TextView) view
				.findViewById(R.id.txt_fragment_mainactivity_informationnum);

		userSexImg = (ImageView) view
				.findViewById(R.id.img_personalfragment_sex);
		userImageImg = (ImageView) view
				.findViewById(R.id.img_personalfragment_head_photo);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_personalfragment_personal_message:
			if(preferences.getInt("userId", -1) != -1){
			Intent intent = new Intent(context, PersonalDataActivity.class);
			intent.putExtra("PersonageUserInformationModel", puim);
			startActivity(intent);
			}else{
				new SystemUtil().makeToast(getActivity(), "请登录");
				Intent intent = new Intent(context,LoginActivity.class);
				startActivity(intent);
//				getActivity().finish();
			}
			break;
		case R.id.layout_personalfragment_my_message:
			if(preferences.getInt("userId", -1) != -1){
			Intent intent2 = new Intent(context, MessageCenterActivity.class);
			startActivity(intent2);
			}else{
				new SystemUtil().makeToast(getActivity(), "请登录");
				Intent intent = new Intent(context,LoginActivity.class);
				startActivity(intent);
//				getActivity().finish();
				
			}
			break;
		case R.id.layout_personalfragment_my_travel_notes:
			if(preferences.getInt("userId", -1) != -1){
			Intent intent3 = new Intent(context, RidingActivity.class);
			startActivity(intent3);
			}else{
				new SystemUtil().makeToast(getActivity(), "请登录");
				Intent intent = new Intent(context,LoginActivity.class);
				startActivity(intent);
//				getActivity().finish();
			}
			break;
		case R.id.layout_personalfragment_my_activity_center:
			if(preferences.getInt("userId", -1) != -1){
			Intent intent3 = new Intent(context, ActionCenterActivity.class);
			startActivity(intent3);
			}else{
				new SystemUtil().makeToast(getActivity(), "请登录");
				Intent intent = new Intent(context,LoginActivity.class);
				startActivity(intent);
			}
			break;
			
		case R.id.layout_personalfragment_my_collect_press:
			if(preferences.getInt("userId", -1) != -1){
			Intent intent4 = new Intent(context, CollectActivity.class);
			startActivity(intent4);
			}else{
				new SystemUtil().makeToast(getActivity(), "请登录");
				Intent intent = new Intent(context,LoginActivity.class);
				startActivity(intent);
//				getActivity().finish();
			}
			break;
		case R.id.layout_personalfragment_my_set:
		//	if(userLoginClass != null){
			Intent intent5 = new Intent(context, SetActivity.class);
			startActivity(intent5);
/*			}else{
				new SystemUtil().makeToast(getActivity(), "请登录");
				Intent intent = new Intent(context,LoginActivity.class);
				startActivity(intent);
			}*/
			break;
		case R.id.layout_personalfragment_back:
			menu.toggle();
			break;
		}
	}

	@Override
	public void onStart() {
		Data();
		super.onStart();
	}

	private void Data() {
		//系统统计数
		informationNumTxt.setVisibility(View.GONE);
		if(preferences.getInt("userId", -1) != -1){
			xUtilsUtil = new XUtilsUtil();
			personalData();
			systemData();

		} else {
			userMemoTxt.setText("");
			userNameTxt.setText("游客");
			userImageImg.setImageResource(R.drawable.homepage_picture);
			userFansTxt.setText("0");
			userIntegralTxt.setText("0");
			userConcernTxt.setText("0");
			//系统统计数
			informationNumTxt.setVisibility(View.GONE);
		}

		layout_personal_message.setOnClickListener(this);
		layout_my_travel_notes.setOnClickListener(this);
		layout_my_message.setOnClickListener(this);
		layout_my_collect_press.setOnClickListener(this);
		layout_my_set.setOnClickListener(this);
		layout_back.setOnClickListener(this);
		layout_my_action_center.setOnClickListener(this);
	}

	private void systemData() {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userId", preferences.getInt("userId", -1)
				+ "");
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/systemMessage/countUserMessage.html", params, new CallBackPost() {
			
			@Override
			public void onMySuccess(ResponseInfo<String> responseInfo) {
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(responseInfo.result);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (jsonObject.optBoolean("result")) {
					int num = jsonObject.optInt("data");
					if(num != 0){
						//系统统计数
						informationNumTxt.setVisibility(View.VISIBLE);
						informationNumTxt.setText(num + "");
					}
				}
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				
			}
		});
	}

	private void personalData() {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userId", preferences.getInt("userId", -1)
				+ "");
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/user/querySingleUser.html", params, new CallBackPost() {
			
			@Override
			public void onMySuccess(ResponseInfo<String> responseInfo) {
				try {
					JSONObject jsonObject = new JSONObject(
							responseInfo.result);
					JSONObject data = jsonObject
							.getJSONObject("data");
					JSONObject user = data
							.getJSONObject("user");
					
					userConcernTxt.setText(data
							.getInt("concernNum") + "");
					if ("null".equals(user.getString("userName")) || "".equals(user.getString("userName"))) {
						userNameTxt.setText("骑来骑去");
					} else {
						userNameTxt.setText(user
								.getString("userName"));
					}
					userFansTxt.setText(data
							.getInt("fansNum") + "");
					userIntegralTxt.setText(user
							.getInt("integral") + "");
					if ("null".equals(user.getString("userMemo")) || "".equals(user.getString("userMemo")) || user.getString("userMemo") == null) {
						userMemoTxt.setText("");
						userMemoTxt.setHint("这个人很懒，什么也没留下!");
					} else {
						userMemoTxt.setText(user
								.getString("userMemo"));
					}
					if ("F".equals(user.getString("sex"))) {
						userSexImg
								.setImageResource(R.drawable.female);
					} else if("M".equals(user.getString("sex"))){
						userSexImg
								.setImageResource(R.drawable.male);
					}else{
						userSexImg.setImageBitmap(null);
					}
					SystemUtil.loadImagexutils(user.getString("userImage"), userImageImg,context);
					
					puim = new PersonageUserInformationModel();
					puim.setAddress(user.getString("address"));
					puim.setMobilePhone(user.getString("mobilePhone"));
					puim.setSex(user.getString("sex"));
					puim.setUniqueKey(user.getString("uniqueKey"));
					puim.setUserId(user.getInt("userId"));
					puim.setUserImage(user.getString("userImage"));
					puim.setUserMemo(user.getString("userMemo"));
					puim.setUserName(user.getString("userName"));
					puim.setFansNum(data.optInt("fansNum"));
					puim.setConcernNum(data.optInt("concernNum"));

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				
			}
		});
	}

	
	
}
