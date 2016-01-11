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

/**
 * 
 * @author leiqian
 * 
 */

public class PersonMessageListAdapter extends BaseAdapter {

	private viewHolder holder;
	private LayoutInflater mInflater;

	private Context context;
	private List<?> list;
	private int flag;

	public PersonMessageListAdapter(Context context, int flag) {
		this.flag = flag;
		this.context = context;
	}

	@Override
	public int getCount() {
		return 5;
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
			holder = new viewHolder();
			// flag==1 view 为 personMessage
			if (flag == 1) {
				view = mInflater.from(context).inflate(
						R.layout.item_list_message_person, null);
				holder.photoImg = (ImageView) view
						.findViewById(R.id.img_messageList_person_photo);
				holder.messageTxt = (TextView) view
						.findViewById(R.id.txt_messageList_person_message);
				holder.messageTypeTxt = (TextView) view
						.findViewById(R.id.txt_messageList_person_messagetype);
				view.setTag(holder);
			}
			// flag==2 view 为 systemMessage
			else if (flag == 2) {
				view = mInflater.from(context).inflate(
						R.layout.item_list_message_system, null);
				holder.photoImg = (ImageView) view
						.findViewById(R.id.img_messageList_system_photo);
				holder.messageTxt = (TextView) view
						.findViewById(R.id.txt_messageList_person_message);
				holder.messageTypeTxt = (TextView) view
						.findViewById(R.id.txt_messageList_person_messagetype);
				view.setTag(holder);
			}
		} else {
			holder = (viewHolder) view.getTag();
		}
		if (flag == 1) {
			holder.messageTypeTxt.setText("消息种类-个人");
			holder.messageTxt
					.setText("测试消息.................测试消息测试消息测试消息测试消息测试消息测试消息");
		} else if (flag == 2) {
			holder.messageTxt.setText("消息种类-系统");
			holder.messageTxt
					.setText("测试消息.................测试消息测试消息测试消息测试消息测试消息测试消息");
		}
		return view;
	}

	public class viewHolder {
		private ImageView photoImg;
		private TextView messageTxt;
		private TextView messageTypeTxt;

	}
}
