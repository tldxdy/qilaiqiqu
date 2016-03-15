package com.qizhi.qilaiqiqu.adapter;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.RidingDraftModel;
import com.qizhi.qilaiqiqu.model.RidingModelList;
import com.qizhi.qilaiqiqu.model.TravelsinformationModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.squareup.picasso.Picasso;

/**
 * 
 * @author leiqian
 *
 */

public class RidingListAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	
	private List<RidingModelList> list;
	private List<RidingDraftModel> rDraftModels;
	private Context context;
	private static final int TYPE_ZERO = 0;
	private static final int TYPE_ONE = 1;
	private int currentType;//当前item类型
	
	public RidingListAdapter(Context context, List<RidingModelList> list){
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	
	public RidingListAdapter(Context context, List<RidingModelList> list ,List<RidingDraftModel> rDraftModels){
		this.context = context;
		this.list = list;
		this.rDraftModels = rDraftModels;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return list.size() + rDraftModels.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {

		if(position < rDraftModels.size()){
			return TYPE_ZERO;
		}
		if(position < list.size() + rDraftModels.size()){
			return TYPE_ONE;
		}
		return super.getItemViewType(position);
	}
	
	  //返回样式的数量
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
				view = inflater.inflate(R.layout.item_list_riding_caogao, null);
				zeroholder.timeTxt = (TextView) view.findViewById(R.id.txt_ridingList_time);
				zeroholder.titleTxt = (TextView) view.findViewById(R.id.txt_ridingList_title);
				zeroholder.pictureImg = (ImageView) view.findViewById(R.id.img_ridingList_picture);
				view.setTag(zeroholder);
				break;
			case TYPE_ONE:
				oneholder = new OneViewHolder();
				view = inflater.inflate(R.layout.item_list_riding, null);
				oneholder.timeTxt = (TextView) view.findViewById(R.id.txt_ridingList_time);
				oneholder.titleTxt = (TextView) view.findViewById(R.id.txt_ridingList_title);
				oneholder.numberTxt = (TextView) view.findViewById(R.id.txt_ridingList_number);
				oneholder.pictureImg = (ImageView) view.findViewById(R.id.img_ridingList_picture);
				view.setTag(oneholder);
				break;
			default:
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
			Gson gson = new Gson();
			Type type = new TypeToken<List<TravelsinformationModel>>(){}.getType();
			List<TravelsinformationModel> trList = gson.fromJson(rDraftModels.get(position).getJsonString(), type);
			System.out.println(trList.toString());
			zeroholder.timeTxt.setText(trList.get(0).getTime());
			zeroholder.titleTxt.setText(trList.get(0).getTitle());
			if(trList.get(position).getArticleImage() != null){
				
				Picasso.with(context).load(new File(trList.get(0).getArticleImage()))
				.resize(800, 400).centerInside()
				.placeholder(R.drawable.bitmap_homepage)
				.error(R.drawable.bitmap_homepage)
				.into(zeroholder.pictureImg);
			}
			
			break;
		case TYPE_ONE:
			oneholder.timeTxt.setText(list.get(position - rDraftModels.size()).getCreateDate().subSequence(0, 10));
			oneholder.titleTxt.setText(list.get(position - rDraftModels.size()).getTitle());
			oneholder.numberTxt.setText(list.get(position - rDraftModels.size()).getScanNum()+"次浏览");
			if(list.get(position - rDraftModels.size()).getDefaultShowImage() != null){
				SystemUtil.Imagexutils(list.get(position - rDraftModels.size()).getDefaultShowImage(), oneholder.pictureImg, context);
			}
			
			
			
			break;
		}
		
		return view;
	}

	public class ZeroViewHolder{
		private TextView timeTxt;
		private TextView titleTxt;
		private ImageView pictureImg;
	}
	public class OneViewHolder{
		private TextView timeTxt;
		private TextView titleTxt;
		private TextView numberTxt;
		private ImageView pictureImg;
	}
}
