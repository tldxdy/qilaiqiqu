package com.qizhi.qilaiqiqu.adapter;

import java.util.Iterator;
import java.util.List;

import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.exceptions.EaseMobException;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.MyMessageAdapter.ViewHolder;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatRecordAdapter extends BaseAdapter {

	private List<EMMessage> list;
	private Context context;
	private XUtilsUtil xUtilsUtil;
	private SharedPreferences preferences;
	private ViewHolder holder;
	private LayoutInflater inflater;
	
	public ChatRecordAdapter(Context context ,List<EMMessage> list){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		xUtilsUtil = new XUtilsUtil();
		preferences = context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);
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
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_chat_record, null);
			holder.contentTxt = (TextView) convertView.findViewById(R.id.txt_chatrecordframent_content);
			holder.nameTxt = (TextView) convertView.findViewById(R.id.txt_chatrecordframent_name);
			holder.portraitImg = (ImageView) convertView.findViewById(R.id.img_chatrecordframent_portrait);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			System.out.println("============");
			System.out.println(list.get(position).getBody().toString());
		if(list.get(position).getChatType() == ChatType.GroupChat){
				holder.nameTxt.setText(list.get(position).getStringAttribute("IMUserNameExpand"));
				if(list.get(position).getType().toString().equals("TXT")){
					String na = list.get(position).getBody().toString();
					String[] naa = na.split(":");
					String substring = na.substring(naa[0].length() + 2, na.length() - 1);
					holder.contentTxt.setText(substring);
				}else if(list.get(position).getType().toString().equals("IMAGE")){
					holder.contentTxt.setText("[图片]");
				}else{
					holder.contentTxt.setText("[语音]");
				}
				SystemUtil.Imagexutils(list.get(position).getStringAttribute("IMConversationUserImageExpand"), holder.portraitImg, context);
				
		}else{
			holder.nameTxt.setText(list.get(position).getStringAttribute("IMUserNameExpand"));
			if(list.get(position).getType().toString().equals("TXT")){
				String na = list.get(position).getBody().toString();
				String[] naa = na.split(":");
				String substring = na.substring(naa[0].length() + 2, na.length() - 1);
				holder.contentTxt.setText(substring);
			}else if(list.get(position).getType().toString().equals("IMAGE")){
				holder.contentTxt.setText("[图片]");
			}else{
				holder.contentTxt.setText("[语音]");
			}
			SystemUtil.Imagexutils(list.get(position).getStringAttribute("IMUserImageExpand"), holder.portraitImg, context);
		}
		} catch (EaseMobException e) {
			e.printStackTrace();
		}
		
		return convertView;
	}
	
	public class ViewHolder{
		private TextView contentTxt;
		private TextView nameTxt;
		private ImageView portraitImg;
	}
}
