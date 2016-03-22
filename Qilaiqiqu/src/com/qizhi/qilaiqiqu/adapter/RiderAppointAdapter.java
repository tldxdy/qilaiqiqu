package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import com.qizhi.qilaiqiqu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RiderAppointAdapter extends BaseAdapter {
	
	private Context context;
	private List<?> list;
	private ViewHolder holder;
	private LayoutInflater inflater;
	
	
	public RiderAppointAdapter(Context context, List<?> list){
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_rider_appoint, null);
			holder.nameTxt = (TextView) convertView.findViewById(R.id.txt_riderFragment_name);
			holder.timeTxt = (TextView) convertView.findViewById(R.id.txt_riderFragment_time);
			holder.conditionTxt = (TextView) convertView.findViewById(R.id.txt_riderFragment_condition);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position % 3 == 0){
			holder.conditionTxt.setText("待处理");
			holder.conditionTxt.setTextColor(0xFFFFBF86);
		}else if(position % 3 == 1){
			holder.conditionTxt.setText("已同意");
			holder.conditionTxt.setTextColor(0xFF6DBFED);
		}else{
			holder.conditionTxt.setText("被拒绝");
			holder.conditionTxt.setTextColor(0xFFFF3030);
		}
		return convertView;
	}

	public class ViewHolder{
		private TextView nameTxt;
		private TextView timeTxt;
		private TextView conditionTxt;
	}
	
}
