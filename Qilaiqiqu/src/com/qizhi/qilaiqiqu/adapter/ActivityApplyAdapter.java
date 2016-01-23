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

public class ActivityApplyAdapter extends BaseAdapter{

	private Context context;
	private List<?> list;
	private ViewHolder holder;
	private LayoutInflater inflater;
	
	public ActivityApplyAdapter(Context context){
		this.context = context;
		inflater = LayoutInflater.from(context);
		
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
			convertView = inflater.inflate(R.layout.item_grid_acticity_apply, null);
			holder = new ViewHolder();
			holder.nameTxt = (TextView) convertView.findViewById(R.id.txt_activityactivityapply_name);
			holder.photoImg = (ImageView) convertView.findViewById(R.id.img_activityactivityapply_photo);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		return convertView;
	}

	private class ViewHolder{
		private TextView nameTxt;
		private ImageView photoImg;
	}
}
