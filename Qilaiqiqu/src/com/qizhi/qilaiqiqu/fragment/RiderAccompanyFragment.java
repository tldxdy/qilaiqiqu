package com.qizhi.qilaiqiqu.fragment;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.PersonActivity;
import com.qizhi.qilaiqiqu.adapter.RiderAccompanyAdapter;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


/**
 * 
 * @author Administrator
 *			陪骑
 *
 */
public class RiderAccompanyFragment extends Fragment implements OnItemClickListener, CallBackPost{
	private View view;
	private ListView agreementList;
	private List<?> list;
	private RiderAccompanyAdapter adapter;
	private int pageIndex = 1;
	private SharedPreferences preferences;
	private XUtilsUtil xUtilsUtil;
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.rider_frament_agreementandserve, null);
		agreementList = (ListView) view.findViewById(R.id.rider_fragment_agreementandserve);
		adapter = new RiderAccompanyAdapter(getActivity(), list);
		agreementList.setAdapter(adapter);
		agreementList.setOnItemClickListener(this);
		
		xUtilsUtil = new XUtilsUtil();
		preferences = getActivity().getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		
		return view;
	}
	
	
	private void data() {
		pageIndex = 1;
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize",  "10");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/attendRider/queryAppointRiderApplyByRiderId.html", params, this);
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
			
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		
	}
	
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//		Intent intent = new Intent(getActivity(), PersonActivity.class);
//		intent.putExtra("isRider", 1);
//		intent.putExtra("userId", 10027);
//		startActivity(intent);
		
		
	}
}
