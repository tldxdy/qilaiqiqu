package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.StartAndParticipantActivityModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ManageAdapter extends BaseAdapter {

	private Context context;
	private List<StartAndParticipantActivityModel> list;
	private ViewHolder holder;
	private LayoutInflater inflater;
	private int userId;
	
	public ManageAdapter(Context context, List<StartAndParticipantActivityModel> list, int userId){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.userId = userId;
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
		holder = new ViewHolder();
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_list_manage_fragment, null);
			holder.titleTxt = (TextView) convertView.findViewById(R.id.txt_managefragment_title);
			holder.detailedInformationTxt = (TextView) convertView.findViewById(R.id.txt_managefragment_detailed_information);
			holder.yearsTxt = (TextView) convertView.findViewById(R.id.txt_managefragment_years);
			holder.timeTxt = (TextView) convertView.findViewById(R.id.txt_managefragment_time);
			holder.applyTxt = (TextView) convertView.findViewById(R.id.txt_managefragment_apply);
			
			holder.photoImg = (ImageView) convertView.findViewById(R.id.img_managefragment_photo);
			holder.applyImg = (ImageView) convertView.findViewById(R.id.img_managefragment_apply);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.yearsTxt.setText(list.get(position).getStartDate().subSequence(0, 10));
		holder.titleTxt.setText(list.get(position).getActivityTitle());
		
		if(userId == list.get(position).getUserId()){
			holder.applyImg.setImageResource(R.drawable.activity_irelease);
			holder.applyTxt.setText("我发起");
		}else{
			holder.applyImg.setImageResource(R.drawable.activity_apply);
			if(list.get(position).isInvolved()){
				holder.applyTxt.setText("已报名");
			}else{
				holder.applyTxt.setText("为报名");
			}
		}
		holder.photoImg.setImageResource(R.drawable.bitmap_homepage);
		if(!"".equals(list.get(position).getDefaultImage()) && list.get(position).getDefaultImage() != null){
			SystemUtil.Imagexutils(list.get(position).getDefaultImage(), holder.photoImg, context);
		}
		
		
		return convertView;
	}
	private class ViewHolder{
		private TextView titleTxt;
		private TextView detailedInformationTxt;
		private TextView yearsTxt;
		private TextView timeTxt;
		private TextView applyTxt;
		private ImageView photoImg;
		private ImageView applyImg;
	}
}
