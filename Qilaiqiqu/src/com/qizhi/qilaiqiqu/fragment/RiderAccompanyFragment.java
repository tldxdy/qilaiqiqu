package com.qizhi.qilaiqiqu.fragment;

import java.util.List;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.PersonActivity;
import com.qizhi.qilaiqiqu.adapter.RiderAccompanyAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RiderAccompanyFragment extends Fragment implements OnItemClickListener{
	private View view;
	private ListView agreementList;
	private List<?> list;
	private RiderAccompanyAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.rider_frament_agreementandserve, null);
		agreementList = (ListView) view.findViewById(R.id.rider_fragment_agreementandserve);
		adapter = new RiderAccompanyAdapter(getActivity(), list);
		agreementList.setAdapter(adapter);
		agreementList.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//		Intent intent = new Intent(getActivity(), PersonActivity.class);
//		intent.putExtra("isRider", 1);
//		intent.putExtra("userId", 10027);
//		startActivity(intent);
		
		
	}
}
