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
import com.qizhi.qilaiqiqu.adapter.FansFragmentListAdapter;
import com.qizhi.qilaiqiqu.model.FansModel;
import com.qizhi.qilaiqiqu.model.FansModel.FansDataList;
import com.qizhi.qilaiqiqu.ui.PullFreshListView;
import com.qizhi.qilaiqiqu.ui.PullFreshListView.OnRefreshListener;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

public class FansFragment extends Fragment implements OnItemClickListener,OnRefreshListener, CallBackPost {

	private View view;

	private PullFreshListView fansList;

	private ArrayList<FansDataList> dataList = new ArrayList<FansDataList>();

	private FansFragmentListAdapter adapter;
	
	private int pageIndex = 1;
	private boolean isFirst = true;
	private static SharedPreferences ferences;

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_fans, container, false);
		Bundle args = getArguments();
		if (args != null) {
			dataList = (ArrayList<FansDataList>) args
					.getSerializable("dataList");
		}

		initView();
		initEvent();
		return view;
	}

	public static FansFragment newInstance(ArrayList<FansDataList> fansDataList, Context context) {
		
		FansFragment newFragment = new FansFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("dataList", fansDataList);
		newFragment.setArguments(bundle);
		return newFragment;

	}

	private void initView() {
		fansList = (PullFreshListView) view.findViewById(R.id.list_fansFragment);
		adapter = new FansFragmentListAdapter(getActivity(), dataList);
		fansList.setAdapter(adapter);
	}

	private void initEvent() {
		fansList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		int attentionId = dataList.get(position-1).getUserId();
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
		new XUtilsUtil().httpPost(url, params,FansFragment.this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		try {
			JSONObject object = new JSONObject(responseInfo.result);
			Gson gson = new Gson();
			FansModel fansModel = gson.fromJson(object.toString(),
					new TypeToken<FansModel>() {
					}.getType());
			ArrayList<FansDataList> fansDataList = fansModel.getDataList();
			if(isFirst){
				dataList = fansDataList;
			}else{
				dataList.addAll(fansDataList);
			}
			adapter.notifyDataSetChanged();
			fansList.finishRefreshing();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		
	}
	
}
