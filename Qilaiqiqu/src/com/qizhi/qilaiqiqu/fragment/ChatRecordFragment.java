package com.qizhi.qilaiqiqu.fragment;

import java.util.ArrayList;
import java.util.List;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.ChatRecordAdapter;
import com.qizhi.qilaiqiqu.adapter.ManageAdapter;
import com.qizhi.qilaiqiqu.model.StartAndParticipantActivityModel;
import com.qizhi.qilaiqiqu.utils.RefreshLayout;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.RefreshLayout.OnLoadListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ChatRecordFragment extends Fragment implements OnItemClickListener,OnRefreshListener,
OnLoadListener{
	private ListView chatList;
	private View view;
	private List<?> list;
	private ChatRecordAdapter adapter;
	private Context context;
	private SharedPreferences preferences;
	private XUtilsUtil xUtilsUtil;
	private int pageIndex = 1;
	
	private RefreshLayout swipeLayout;
	private View header;
	
	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_chat_record,null);
		chatList = (ListView) view.findViewById(R.id.list_fragment_chat_record);
		context = getActivity();
		preferences = context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		xUtilsUtil = new XUtilsUtil();
		header = View.inflate(getActivity(),R.layout.header, null);
		swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		chatList.addHeaderView(header);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setOnLoadListener(this);
		data();
		return view;
	}

	private void data() {
		adapter = new ChatRecordAdapter(context, list);
		chatList.setAdapter(adapter);
	}

	@Override
	public void onRefresh() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				pageIndex = 1;
				//dataJ();
				swipeLayout.setRefreshing(false);
				// 更新数据
				// 更新完后调用该方法结束刷新
				
			}
		}, 1000);

	}

	@Override
	public void onLoad() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 更新数据
				// 更新完后调用该方法结束刷新
				pageIndex = pageIndex + 1;
				//dataJ();
				swipeLayout.setLoading(false);
			}
		}, 1000);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
	}
}
