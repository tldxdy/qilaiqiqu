package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.ActivityModel.ParticipantList;
import com.qizhi.qilaiqiqu.utils.CircleImageViewUtil;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.squareup.picasso.Picasso;

public class GroupGridAdapter extends BaseAdapter {

	private List<ParticipantList> list;
	private Context context;
	private ViewHolder holder;
	private LayoutInflater inflater;

	private String imageUrl = "http://weride.oss-cn-hangzhou.aliyuncs.com/";

	public GroupGridAdapter(List<ParticipantList> list, Context context) {
		this.list = list;
		this.context = context;
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
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_grid_append, null);
			holder.userClick = (TextView) view
					.findViewById(R.id.txt_appendGrid_click);
			holder.userName = (TextView) view
					.findViewById(R.id.txt_appendGrid_itemText);
			holder.image = (CircleImageViewUtil) view
					.findViewById(R.id.img_appendGrid_itemImage);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.userClick.setVisibility(View.GONE);
		holder.userName.setText(list.get(position).getUserName());
		SystemUtil.Imagexutils(list.get(position).getUserImage(), holder.image, context);
		Picasso.with(context)
				.load(imageUrl + list.get(position).getUserImage())
				.into(holder.image);
		return view;
	}

	private class ViewHolder {
		TextView userName;
		TextView userClick;
		CircleImageViewUtil image;

	}

}
