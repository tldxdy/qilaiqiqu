package com.qizhi.qilaiqiqu.wxapi;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.UserLoginModel;
import com.qizhi.qilaiqiqu.model.WXUserInfoModel;
import com.qizhi.qilaiqiqu.utils.ConstantsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackGet;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler,
		CallBackGet {

	private IWXAPI api;
	private String openid;
	private String access_token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wx_entry);
		api = WXAPIFactory.createWXAPI(this, ConstantsUtil.APP_ID, false);
		api.registerApp(ConstantsUtil.APP_ID);
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
			// 可用以下两种方法获得code
			// resp.toBundle(bundle);
			// Resp sp = new Resp(bunde);
			// String code = sp.code;
			// 或者
			String code = ((SendAuth.Resp) resp).code;
			// 上面的code就是接入指南里要拿到的code
			System.out.println("000000000000000000000000:" + code);
			wxRequest(code);
			break;

		default:
			break;
		}
	}

	public void wxRequest(String code) {
		String url = "http://120.55.195.170:80/common/weixinLogin.html?code="
				+ code;
		new XUtilsUtil().httpGet(url, WXEntryActivity.this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		String result = responseInfo.result;
		System.out.println(result);
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		System.out.println("失败了!!!!!!:"+msg);
	}

}
