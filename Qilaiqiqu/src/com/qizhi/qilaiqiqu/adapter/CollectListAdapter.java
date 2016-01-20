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

/**
 * 
 * @author leiqian
 * 
 */

public class CollectListAdapter extends BaseAdapter {

	private ViewHolder holder;
	private LayoutInflater inflater;

	private Context context;
	private List<CollectModel> list;

	public CollectListAdapter(Context context, List<CollectModel> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
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
	public View getView(int position, View view, ViewGroup arg2) {
		if (view == null || view.getTag() == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_list_collect,
					null);
			holder.titleTxt = (TextView) view
					.findViewById(R.id.txt_collectList_title);
			holder.timeTxt = (TextView) view
					.findViewById(R.id.txt_collectList_time);
			holder.numberTxt = (TextView) view
					.findViewById(R.id.txt_collectList_number);
			holder.photoImg = (ImageView) view
					.findViewById(R.id.img_collectList_photo);
			holder.pictureImg = (ImageView) view
					.findViewById(R.id.img_collectList_picture);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		new SystemUtil();
		holder.timeTxt.setText(list.get(position).getCreateDate().subSequence(0, 10));
		holder.titleTxt.setText(list.get(position).getTitle());
		holder.numberTxt.setText(list.get(position).getScanNum()+"次浏览");
		SystemUtil.Imagexutils(list.get(position).getUserImage(), holder.photoImg, context);
		//SystemUtil.loadImagexutils(list.get(position).getUserImage(), holder.photoImg, context);
		//holder.photoImg.setImageResource(R.drawable.lena);
		if(list.get(position).getDefaultShowImage() != null){
			SystemUtil.Imagexutils(list.get(position).getDefaultShowImage().split("\\@")[0], holder.pictureImg, context);
		}
		//SystemUtil.loadImagexutils(list.get(position).getDefaultShowImage().split("\\@")[0], holder.pictureImg, context);
		//holder.pictureImg.setBackgroundResource(R.drawable.demo2);
		return view;
	}

	public class ViewHolder {
		private ImageView pictureImg;
		private ImageView photoImg;
		private TextView titleTxt;
		private TextView timeTxt;
		private TextView numberTxt;
	}
}
