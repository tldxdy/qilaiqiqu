package com.qizhi.qilaiqiqu.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.PersonActivity;
import com.qizhi.qilaiqiqu.adapter.FansFragmentListAdapter;
import com.qizhi.qilaiqiqu.model.FansModel.FansDataList;

public class FansFragment extends Fragment implements OnItemClickListener {

	private View view;

	private ListView fansList;

	private ArrayList<FansDataList> dataList = new ArrayList<FansDataList>();

	private FansFragmentListAdapter adapter;

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

	public static FansFragment newInstance(ArrayList<FansDataList> fansDataList) {
		FansFragment newFragment = new FansFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("dataList", fansDataList);
		newFragment.setArguments(bundle);
		return newFragment;

	}

	private void initView() {
		fansList = (ListView) view.findViewById(R.id.list_fansFragment);
		adapter = new FansFragmentListAdapter(getActivity(), dataList);
		fansList.setAdapter(adapter);
	}

	private void initEvent() {
		fansList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		int attentionId = dataList.get(position).getUserId();
		startActivity(new Intent(getActivity(), PersonActivity.class).putExtra("userId", attentionId));
	}
	
}
