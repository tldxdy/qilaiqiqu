package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.RidingModelList;
import com.qizhi.qilaiqiqu.utils.SystemUtil;

/**
 * 
 * @author leiqian
 *
 */

public class RidingListAdapter extends BaseAdapter{
	private ViewHolder holder;
	private LayoutInflater inflater;
	
	private List<RidingModelList> list;
	private Context context;
	
	public RidingListAdapter(Context context, List<RidingModelList> list){
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
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		if (view == null || view.getTag() == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_list_riding, null);
			holder.timeTxt = (TextView) view.findViewById(R.id.txt_ridingList_time);
			holder.titleTxt = (TextView) view.findViewById(R.id.txt_ridingList_title);
			holder.numberTxt = (TextView) view.findViewById(R.id.txt_ridingList_number);
			holder.pictureImg = (ImageView) view.findViewById(R.id.img_ridingList_picture);
			view.setTag(holder);
		}else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.timeTxt.setText(list.get(position).getCreateDate().subSequence(0, 10));
		holder.titleTxt.setText(list.get(position).getTitle());
		holder.numberTxt.setText(list.get(position).getScanNum()+"次浏览");
		
		
		holder.pictureImg.setImageResource(R.drawable.bitmap_homepage);
		if(list.get(position).getDefaultShowImage() != null){
			
			SystemUtil.Imagexutils(list.get(position).getDefaultShowImage().split("\\@")[0], holder.pictureImg, context);
		}
		//SystemUtil.loadImagexutils(list.get(position).getDefaultShowImage().split("\\@")[0], holder.pictureImg, context);
		
		return view;
	}

	public class ViewHolder{
		private TextView timeTxt;
		private TextView titleTxt;
		private TextView numberTxt;
		private ImageView pictureImg;
	}
}
