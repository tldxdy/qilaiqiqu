package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.GroupMemberModel.Data.UserList;
import com.qizhi.qilaiqiqu.utils.CircleImageViewUtil;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.squareup.picasso.Picasso;

public class GroupMemberAdapter extends BaseAdapter {

	private List<UserList> list;
	private Context context;
	private ViewHolder holder;
	private LayoutInflater inflater;

	public GroupMemberAdapter(List<UserList> list, Context context) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_grid_append, null);
			holder.username = (TextView) convertView
					.findViewById(R.id.txt_appendGrid_itemText);
			holder.image = (CircleImageViewUtil) convertView
					.findViewById(R.id.img_appendGrid_itemImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.username.setText(list.get(position).getUserName());
		SystemUtil.Imagexutils(list.get(position).getUserImage(), holder.image, context);
		/*Picasso.with(context)
				.load(SystemUtil.IMGPHTH + list.get(position).getUserImage())
				.into(holder.image);*/

		return convertView;
	}

	private class ViewHolder {
		TextView username;
		CircleImageViewUtil image;
	}

}
