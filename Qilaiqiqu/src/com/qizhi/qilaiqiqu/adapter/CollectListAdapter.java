package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.RidingDetailsListAdapter.OneViewHolder;
import com.qizhi.qilaiqiqu.adapter.RidingDetailsListAdapter.ThreeViewHolder;
import com.qizhi.qilaiqiqu.adapter.RidingDetailsListAdapter.TwoViewHolder;
import com.qizhi.qilaiqiqu.adapter.RidingDetailsListAdapter.ZeroViewHolder;
import com.qizhi.qilaiqiqu.model.CollectModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author leiqian
 * 
 */

public class CollectListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	
	private static final int TYPE_ZERO = 0;
	private static final int TYPE_ONE = 1;
	private int currentType;//当前item类型

	private Context context;
	private List<CollectModel> list;
	private SharedPreferences preferences;

	public CollectListAdapter(Context context, List<CollectModel> list) {
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
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	
	
	
	

	@Override
	public int getItemViewType(int position) {
		if("HD".equals(list.get(position).getIsType())){
			return TYPE_ZERO;
		}
		
		if("QYJ".equals(list.get(position).getIsType())){
			return TYPE_ONE;
		}
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ZeroViewHolder zeroholder = null;
		OneViewHolder oneholder = null;
		
		
		currentType = getItemViewType(position);
		
		if(view == null){
			switch (currentType) {
			case TYPE_ZERO:
				zeroholder = new ZeroViewHolder();
				view = inflater.inflate(R.layout.item_list_manage_fragment, null);
				zeroholder.titleTxt = (TextView) view.findViewById(R.id.txt_managefragment_title);
				zeroholder.yearsTxt = (TextView) view.findViewById(R.id.txt_managefragment_years);
				zeroholder.timeTxt = (TextView) view.findViewById(R.id.txt_managefragment_time);
				zeroholder.applyTxt = (TextView) view.findViewById(R.id.txt_managefragment_apply);
				
				zeroholder.photoImg = (ImageView) view.findViewById(R.id.img_managefragment_photo);
				zeroholder.applyImg = (ImageView) view.findViewById(R.id.img_managefragment_apply);
				zeroholder.moneyTxt = (TextView) view.findViewById(R.id.txt_managefragment_money);
				
				
				view.setTag(zeroholder);
				
				break;
			case TYPE_ONE:
				oneholder = new OneViewHolder();
				view = inflater.inflate(R.layout.item_list_collect,
						null);
				oneholder.titleTxt = (TextView) view
						.findViewById(R.id.txt_collectList_title);
				oneholder.timeTxt = (TextView) view
						.findViewById(R.id.txt_collectList_time);
				oneholder.numberTxt = (TextView) view
						.findViewById(R.id.txt_collectList_number);
				oneholder.photoImg = (ImageView) view
						.findViewById(R.id.img_collectList_photo);
				oneholder.pictureImg = (ImageView) view
						.findViewById(R.id.img_collectList_picture);
				view.setTag(oneholder);
				
				break;
			}
		}else{
			switch (currentType) {
			case TYPE_ZERO:
				zeroholder = (ZeroViewHolder) view.getTag();
				break;
			case TYPE_ONE:
				oneholder = (OneViewHolder) view.getTag();
				break;
			}
		}
		
		switch (currentType) {
		case TYPE_ZERO:
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
			
			zeroholder.yearsTxt.setText(list.get(position).getStartDate().subSequence(0, 10));
			zeroholder.titleTxt.setText(list.get(position).getCollectTitle());
			zeroholder.applyTxt.setTextColor(0xff000000);
			if(preferences.getInt("userId", -1) == list.get(position).getUserId()){
				zeroholder.applyImg.setImageResource(R.drawable.activity_irelease);
				zeroholder.applyTxt.setText("我发起");
			}else{
				zeroholder.applyImg.setImageResource(R.drawable.take_parted);
				if(list.get(position).isInvolved()){
					zeroholder.applyTxt.setText("已报名");
					zeroholder.applyTxt.setTextColor(0xff6dbfed);
				}else{
					zeroholder.applyImg.setImageResource(R.drawable.take_parting);
					zeroholder.applyTxt.setText("立即报名");
				}
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
			
			break;
		case TYPE_ONE:
			oneholder.timeTxt.setText(list.get(position).getCollectDate().subSequence(0, 10));
			oneholder.titleTxt.setText(list.get(position).getCollectTitle());
			//oneholder.numberTxt.setText(list.get(position).getScanNum()+"次浏览");
			oneholder.numberTxt.setVisibility(View.GONE);
			SystemUtil.Imagexutils(list.get(position).getUserImage(), oneholder.photoImg, context);
			if(list.get(position).getDefaultImage() != null){
				SystemUtil.Imagexutils(list.get(position).getDefaultImage(), oneholder.pictureImg, context);
			}
			break;
		}
		
		
		
		/*
		if (view == null || view.getTag() == null) {
			oneholder = new OneViewHolder();
			view = inflater.inflate(R.layout.item_list_collect,
					null);
			oneholder.titleTxt = (TextView) view
					.findViewById(R.id.txt_collectList_title);
			oneholder.timeTxt = (TextView) view
					.findViewById(R.id.txt_collectList_time);
			oneholder.numberTxt = (TextView) view
					.findViewById(R.id.txt_collectList_number);
			oneholder.photoImg = (ImageView) view
					.findViewById(R.id.img_collectList_photo);
			oneholder.pictureImg = (ImageView) view
					.findViewById(R.id.img_collectList_picture);
			view.setTag(oneholder);
		} else {
			oneholder = (OneViewHolder) view.getTag();
		}

		oneholder.timeTxt.setText(list.get(position).getCreateDate().subSequence(0, 10));
		oneholder.titleTxt.setText(list.get(position).getTitle());
		oneholder.numberTxt.setText(list.get(position).getScanNum()+"次浏览");
		SystemUtil.Imagexutils(list.get(position).getUserImage(), oneholder.photoImg, context);
		if(list.get(position).getDefaultShowImage() != null){
			SystemUtil.Imagexutils(list.get(position).getDefaultShowImage(), oneholder.pictureImg, context);
		}*/
		return view;
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
	

	public class OneViewHolder {
		private ImageView pictureImg;
		private ImageView photoImg;
		private TextView titleTxt;
		private TextView timeTxt;
		private TextView numberTxt;
	}
}
