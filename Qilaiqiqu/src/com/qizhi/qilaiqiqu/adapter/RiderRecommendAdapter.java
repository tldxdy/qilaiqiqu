package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import com.qizhi.qilaiqiqu.R;
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
	private List<?> list;
	private ViewHolder holder;
	
	public RiderRecommendAdapter(Context context, List<?> list){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return 5;
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
		Picasso.with(context).load("http://img.weride.com.cn/USER_201603171720553150_160_160_14.jpg").into(holder.photoImg);
		
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
