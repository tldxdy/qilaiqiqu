package com.qizhi.qilaiqiqu.adapter;

import java.util.Iterator;
import java.util.List;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.RiderApplyModle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RiderAppointAdapter extends BaseAdapter {
	
	private Context context;
	private List<RiderApplyModle> list;
	private ViewHolder holder;
	private LayoutInflater inflater;
	
	
	public RiderAppointAdapter(Context context, List<RiderApplyModle> list){
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
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
		
		holder.nameTxt.setText(list.get(position).getRiderName());
		String[] times = list.get(position).getTime().split(",");
		
		String s = list.get(position).getDate() + " " + 
					times[0] + ":00-" + (Integer.parseInt(times[times.length - 1]) + 1) + ":00";
		holder.timeTxt.setText(s);
		
		
		if(list.get(position).getIsAgree().equals("normal")){
			holder.conditionTxt.setText("待处理");
			holder.conditionTxt.setTextColor(0xFFFFBF86);
		}else if(list.get(position).getIsAgree().equals("true")){
			holder.conditionTxt.setText("已同意");
			holder.conditionTxt.setTextColor(0xFF6DBFED);
		}else if(list.get(position).getIsAgree().equals("false")){
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
