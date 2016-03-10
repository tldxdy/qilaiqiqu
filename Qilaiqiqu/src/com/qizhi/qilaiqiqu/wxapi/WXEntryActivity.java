package com.qizhi.qilaiqiqu.wxapi;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.HuanxinLogOutActivity;
import com.qizhi.qilaiqiqu.activity.LoginActivity;
import com.qizhi.qilaiqiqu.activity.MainActivity;
import com.qizhi.qilaiqiqu.utils.ConstantsUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends HuanxinLogOutActivity implements
		IWXAPIEventHandler, CallBackPost {

	private IWXAPI api;
	private String openid;
	private String access_token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wx_entry);
		api = WXAPIFactory.createWXAPI(this, ConstantsUtil.APP_ID_WX, false);
		api.registerApp(ConstantsUtil.APP_ID_WX);
		api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onResp(BaseResp resp) {
		Bundle bundle = new Bundle();
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:

			String code = ((SendAuth.Resp) resp).code;
			// 上面的code就是接入指南里要拿到的code
			wxRequest(code);
			
			break;

		default:
			break;
		}
	}

	public void wxRequest(String code) {
		String url = "common/weixinLogin.html";

		RequestParams params = new RequestParams();
		params.addBodyParameter("code", code);
		params.addBodyParameter("pushToken",
				JPushInterface.getRegistrationID(WXEntryActivity.this));
		params.addBodyParameter("adviceType", "ANDROID");

		new XUtilsUtil().httpPost(url, params, this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		System.out.println("!!!!!!!!!!responseInfo.result"
				+ responseInfo.result);
		String result = responseInfo.result;
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getBoolean("result")) {
				JSONObject data = jsonObject.getJSONObject("data");

				/**
				 * SharedPreferences存储用户Id和uniqueKey
				 */
				SharedPreferences sharedPreferences = getSharedPreferences(
						"userLogin", Context.MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();// 获取编辑器
				editor.putInt("userId", data.getInt("userId"));
				editor.putString("imId", data.getString("imId"));
				editor.putString("riderId", data.getString("riderId"));
				editor.putString("uniqueKey", data.getString("uniqueKey"));
				editor.putString("userName", data.getString("userName"));
				editor.putString("userImage", data.getString("userImage"));
				editor.putString("imUserName", data.getString("imUserName"));
				editor.putString("imPassword", data.getString("imPassword"));
				editor.putString("mobilePhone", data.getString("mobilePhone"));
				editor.putInt("loginFlag", 1);
				editor.commit();

				
			}else {
				Toasts.show(this, "微信登录失败", 0);
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		startActivity(new Intent(WXEntryActivity.this,
				MainActivity.class).putExtra("loginFlag", 1));

	}

	@Override
	public void onMyFailure(HttpException error, String msg) {

	}

}
