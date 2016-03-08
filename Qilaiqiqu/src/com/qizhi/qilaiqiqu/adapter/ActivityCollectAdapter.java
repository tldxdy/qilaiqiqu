package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.RidingCollectAdapter.ZeroViewHolder;
import com.qizhi.qilaiqiqu.model.CollectModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityCollectAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<CollectModel> list;
	private ZeroViewHolder zeroholder;
	private SharedPreferences preferences;

	public ActivityCollectAdapter(Context context, List<CollectModel> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		preferences =  context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);

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
				zeroholder = new ZeroViewHolder();
				convertView = inflater.inflate(R.layout.item_list_manage_fragment, null);
				zeroholder.titleTxt = (TextView) convertView.findViewById(R.id.txt_managefragment_title);
				zeroholder.yearsTxt = (TextView) convertView.findViewById(R.id.txt_managefragment_years);
				zeroholder.timeTxt = (TextView) convertView.findViewById(R.id.txt_managefragment_time);
				zeroholder.applyTxt = (TextView) convertView.findViewById(R.id.txt_managefragment_apply);
				
				zeroholder.photoImg = (ImageView) convertView.findViewById(R.id.img_managefragment_photo);
				zeroholder.applyImg = (ImageView) convertView.findViewById(R.id.img_managefragment_apply);
				zeroholder.moneyTxt = (TextView) convertView.findViewById(R.id.txt_managefragment_money);
				convertView.setTag(zeroholder);
			}else{
				zeroholder = (ZeroViewHolder) convertView.getTag();
			}
		
		int duration = list.get(position).getDuration() / 60;
		if(duration >= 24){
			int days = duration / 24;
			duration = duration % 24;
			zeroholder.timeTxt.setText(days + "天" + duration + "小时");
		}else{
			if(duration == 0){
				zeroholder.timeTxt.setText("1小时");
			}else{
				zeroholder.timeTxt.setText(duration + "小时");
			}
		}
		zeroholder.titleTxt.setText(list.get(position).getActivityTitle());
		zeroholder.yearsTxt.setText(list.get(position).getStartDate().subSequence(0, 10));
		//zeroholder.titleTxt.setText(list.get(position).getCollectTitle());
		zeroholder.applyTxt.setTextColor(0xff000000);
		if(list.get(position).getState().equals("ACTEND")){
			zeroholder.applyImg.setImageResource(R.drawable.activity_finish);
			zeroholder.applyTxt.setText("已结束");
			zeroholder.applyTxt.setTextColor(0xff6dbfed);
		}else if(list.get(position).getState().equals("ACTIN")){
			if(list.get(position).isInvolved()){
				zeroholder.applyImg.setImageResource(R.drawable.take_parted);
				zeroholder.applyTxt.setText("已报名");
				zeroholder.applyTxt.setTextColor(0xff6dbfed);
			}else{
				zeroholder.applyImg.setImageResource(R.drawable.take_parting);
				zeroholder.applyTxt.setText("立即报名");
			}
		}else{
			zeroholder.applyImg.setImageResource(R.drawable.activity_irelease);
			zeroholder.applyTxt.setText("进行中");
			zeroholder.applyTxt.setTextColor(0xff6dbfed);
		}
		zeroholder.photoImg.setImageResource(R.drawable.bitmap_homepage);
		if(!"".equals(list.get(position).getDefaultImage()) && list.get(position).getDefaultImage() != null){
			SystemUtil.Imagexutils(list.get(position).getDefaultImage(), zeroholder.photoImg, context);
		}
		
		if(list.get(position).getOutlay() == null || "免费".equals(list.get(position).getOutlay()) || "null".equals(list.get(position).getOutlay()) || "0".equals(list.get(position).getOutlay())){
			zeroholder.moneyTxt.setVisibility(View.GONE);
			zeroholder.moneyTxt.setText("免费");
		}else{
			zeroholder.moneyTxt.setVisibility(View.VISIBLE);
			zeroholder.moneyTxt.setText("收费");
		}
		
		
		return convertView;
	}

	
	public class ZeroViewHolder {
		private TextView titleTxt;
		private TextView yearsTxt;
		private TextView timeTxt;
		private TextView applyTxt;
		private TextView moneyTxt;
		private ImageView photoImg;
		private ImageView applyImg;
	}
}
