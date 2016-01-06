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
import com.qizhi.qilaiqiqu.adapter.CareFragmentListAdapter;
import com.qizhi.qilaiqiqu.model.CareModel.CareDataList;

public class CareFragment extends Fragment implements OnItemClickListener {

	private View view;

	private ListView careList;


	private ArrayList<CareDataList> dataList = new ArrayList<CareDataList>();

	private CareFragmentListAdapter adapter;

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

	public static CareFragment newInstance(ArrayList<CareDataList> dataList2) {
		CareFragment newFragment = new CareFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("dataList", dataList2);
		newFragment.setArguments(bundle);
		return newFragment;

	}

	private void initView() {
		careList = (ListView) view.findViewById(R.id.list_careFragment);
		adapter = new CareFragmentListAdapter(getActivity(), dataList);
		careList.setAdapter(adapter);
	}

	private void initEvent() {
		careList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		int attentionId = dataList.get(position).getQuoteUserId();
		startActivity(new Intent(getActivity(), PersonActivity.class).putExtra("userId", attentionId));
	}

}
