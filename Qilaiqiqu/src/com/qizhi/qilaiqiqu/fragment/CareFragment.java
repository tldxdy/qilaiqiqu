package com.qizhi.qilaiqiqu.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.PersonActivity;
import com.qizhi.qilaiqiqu.adapter.CareFragmentListAdapter;
import com.qizhi.qilaiqiqu.model.CareModel;
import com.qizhi.qilaiqiqu.model.CareModel.CareDataList;
import com.qizhi.qilaiqiqu.ui.PullFreshListView;
import com.qizhi.qilaiqiqu.ui.PullFreshListView.OnRefreshListener;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

public class CareFragment extends Fragment implements OnItemClickListener,OnRefreshListener, CallBackPost{

	private View view;

	private PullFreshListView careList;


	private ArrayList<CareDataList> dataList = new ArrayList<CareDataList>();

	private CareFragmentListAdapter adapter;
	private int pageIndex = 1;
	private boolean isFirst = true;
	private static SharedPreferences ferences;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_care, container, false);
		Bundle args = getArguments();
		if (args != null) {
			dataList = (ArrayList<CareDataList>) args
					.getSerializable("dataList");
		}
		initView();
		initEvent();
		return view;
	}

	public static CareFragment newInstance(ArrayList<CareDataList> dataList2, Context context) {
		ferences = context.getSharedPreferences("userLogin", 0);
		CareFragment newFragment = new CareFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("dataList", dataList2);
		newFragment.setArguments(bundle);
		return newFragment;

	}

	private void initView() {
		careList = (PullFreshListView) view.findViewById(R.id.list_careFragment);
		pageIndex = 1;
		isFirst = true;
		httpQuery();
	}

	private void initEvent() {
		careList.setOnItemClickListener(this);
		careList.setOnRefreshListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		int attentionId = dataList.get(position - 1).getQuoteUserId();
		startActivity(new Intent(getActivity(), PersonActivity.class).putExtra("userId", attentionId));
	}

	
	
	@Override
	public void onRefresh() {
		pageIndex = 1;
		isFirst = true;
		httpQuery();
	}

	@Override
	public void onLoadingMore() {
		pageIndex = pageIndex + 1;
		isFirst = false;
		httpQuery();
	}
	private void httpQuery() {

		String url = "mobile/attention/queryMyAttentionPaginationList.html";
		RequestParams params = new RequestParams();
		int quoteUserId = ferences.getInt("userId", -1);
		String pageSize = "10";
		String uniqueKey = ferences.getString("uniqueKey", null);

		params.addBodyParameter("userId", quoteUserId + "");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize", pageSize);
		params.addBodyParameter("uniqueKey", uniqueKey);
		new XUtilsUtil().httpPost(url, params,CareFragment.this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		try {
			JSONObject object = new JSONObject(responseInfo.result);
			Gson gson = new Gson();
			CareModel careModel = gson.fromJson(object.toString(),
					new TypeToken<CareModel>() {
					}.getType());
			ArrayList<CareDataList> careDataList = careModel.getDataList();
			if(isFirst){
				dataList = careDataList;
				adapter = new CareFragmentListAdapter(getActivity(), dataList);
				careList.setAdapter(adapter);
			}else{
				dataList.addAll(careDataList);
			}
			adapter.notifyDataSetChanged();
			careList.finishRefreshing();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		
	}
}
