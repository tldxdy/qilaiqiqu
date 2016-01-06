package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import com.qizhi.qilaiqiqu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyMessageAdapter extends BaseAdapter {

	private ViewHolder holder;
	private LayoutInflater inflater;
	
	private List<?> list;
	private Context context;
	
	public MyMessageAdapter(Context context){
		this.context = context;
		inflater = LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_list_mymessageactivity, null);
			holder.contentTxt = (TextView) convertView.findViewById(R.id.txt_mymessageactivity_content);
			holder.systemMessageTxt = (TextView) convertView.findViewById(R.id.txt_mymessageactivity_system_message);
			holder.portraitImg = (ImageView) convertView.findViewById(R.id.img_mymessageactivity_portrait);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		return convertView;
	}

	public class ViewHolder{
		private TextView contentTxt;
		private TextView systemMessageTxt;
		private ImageView portraitImg;
	}
}
