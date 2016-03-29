package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.RiderRecommendModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RiderRecommendAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
	private List<RiderRecommendModel> list;
	private ViewHolder holder;
	
	public RiderRecommendAdapter(Context context, List<RiderRecommendModel> list){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
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
			convertView = inflater.inflate(R.layout.list_rider_recommend, null);
			holder.photoImg = (ImageView) convertView.findViewById(R.id.img_riderrecommend_photo);
			holder.weeknnumTxt = (TextView) convertView.findViewById(R.id.txt_riderrecommend_weeknnum);
			holder.historynumTxt = (TextView) convertView.findViewById(R.id.txt_riderrecommend_historynum);
			holder.pTxt = (TextView) convertView.findViewById(R.id.txt_riderrecommend_p);
			holder.cTxt = (TextView) convertView.findViewById(R.id.txt_riderrecommend_c);
			holder.dTxt = (TextView) convertView.findViewById(R.id.txt_riderrecommend_d);
			holder.describeTxt = (TextView) convertView.findViewById(R.id.txt_riderrecommend_describe);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.pTxt.setText(list.get(position).getAttendArea());
		holder.cTxt.setText("");
		holder.dTxt.setText("");
		SystemUtil.Imagexutils(list.get(position).getRiderImage().split(",")[0], holder.photoImg, context);
		
		if(list.get(position).getWeekTimes() == null){
			holder.weeknnumTxt.setText("0");
		}else {
			holder.weeknnumTxt.setText(list.get(position).getWeekTimes() + "");
		}
		holder.historynumTxt.setText(list.get(position).getAttendTimes() + "");
		
		holder.describeTxt.setText(list.get(position).getRiderMemo());
		
		return convertView;
	}
	private class ViewHolder{
		private ImageView photoImg;
		private TextView weeknnumTxt;
		private TextView historynumTxt;
		private TextView pTxt;
		private TextView cTxt;
		private TextView dTxt;
		private TextView describeTxt;
	}
}
