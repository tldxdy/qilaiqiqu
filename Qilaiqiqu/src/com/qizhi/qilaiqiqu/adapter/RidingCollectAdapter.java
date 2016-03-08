package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.CollectModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RidingCollectAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<CollectModel> list;
	private ZeroViewHolder oneholder;

	public RidingCollectAdapter(Context context, List<CollectModel> list) {
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

		
		if (convertView == null || convertView.getTag() == null) {
			oneholder = new ZeroViewHolder();
			convertView = inflater.inflate(R.layout.item_list_collect,
					null);
			oneholder.titleTxt = (TextView) convertView
					.findViewById(R.id.txt_collectList_title);
			oneholder.timeTxt = (TextView) convertView
					.findViewById(R.id.txt_collectList_time);
			oneholder.numberTxt = (TextView) convertView
					.findViewById(R.id.txt_collectList_number);
			oneholder.photoImg = (ImageView) convertView
					.findViewById(R.id.img_collectList_photo);
			oneholder.pictureImg = (ImageView) convertView
					.findViewById(R.id.img_collectList_picture);
			convertView.setTag(oneholder);
		} else {
			oneholder = (ZeroViewHolder) convertView.getTag();
		}

		oneholder.timeTxt.setText(list.get(position).getCreateDate().subSequence(0, 10));
		oneholder.titleTxt.setText(list.get(position).getTitle());
		oneholder.numberTxt.setText(list.get(position).getScanNum()+"次浏览");
		oneholder.timeTxt.setTextColor(0xffffffff);
		oneholder.titleTxt.setTextColor(0xffffffff);
		oneholder.numberTxt.setTextColor(0xffffffff);
		SystemUtil.Imagexutils(list.get(position).getUserImage(), oneholder.photoImg, context);
		if(list.get(position).getDefaultShowImage() != null){
			SystemUtil.Imagexutils(list.get(position).getDefaultShowImage(), oneholder.pictureImg, context);
		}
		
		
		return convertView;
	}
	public class ZeroViewHolder {
		private ImageView pictureImg;
		private ImageView photoImg;
		private TextView titleTxt;
		private TextView timeTxt;
		private TextView numberTxt;
	}
}
