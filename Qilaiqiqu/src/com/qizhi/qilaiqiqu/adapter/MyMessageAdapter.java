package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.SystemMessageModel;
import com.qizhi.qilaiqiqu.utils.SideslipDeleteListView;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyMessageAdapter extends BaseAdapter {

	private ViewHolder holder;
	private LayoutInflater inflater;
	
	private List<SystemMessageModel> list;
	private Context context;
	private XUtilsUtil xUtilsUtil;
	private SharedPreferences preferences;
	
	public MyMessageAdapter(Context context, List<SystemMessageModel> list){
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_list_mymessageactivity, null);
			holder.contentTxt = (TextView) convertView.findViewById(R.id.txt_mymessageactivity_content);
			holder.systemMessageTxt = (TextView) convertView.findViewById(R.id.txt_mymessageactivity_system_message);
			holder.portraitImg = (ImageView) convertView.findViewById(R.id.img_mymessageactivity_portrait);
			holder.deleteTxt = (TextView) convertView.findViewById(R.id.delete);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.contentTxt.setText(list.get(position).getContent());
		if(list.get(position).getState().equals("NOVIEW")){
			holder.contentTxt.setTextColor(0xff6dbfed);
		}else{
			holder.contentTxt.setTextColor(0xff666666);
		}
		if(list.get(position).getMessageType().contains("QY")){
			holder.portraitImg.setImageResource(R.drawable.commert_news);
			holder.systemMessageTxt.setText("游记消息");
			
		}else if(list.get(position).getMessageType().contains("HD")){
			//活动
			holder.portraitImg.setImageResource(R.drawable.activity_news);
			holder.systemMessageTxt.setText("活动消息");
			
		}else if(list.get(position).getMessageType().contains("PQS")){
			holder.portraitImg.setImageResource(R.drawable.attend_news);
			holder.systemMessageTxt.setText("陪骑士消息");
		}
		holder.systemMessageTxt.setTextColor(0xff6dbfed);
		
		holder.deleteTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				delete(list.get(position).getSystemMessageId(),position);
				
			}


		});
		
		
		
		
		return convertView;
	}

	private void delete(Integer systemMessageId, final int position) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("systemMessageId", systemMessageId + "");
		params.addBodyParameter("uniqueKey", preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/systemMessage/deleteSystemMessage.html", params, new CallBackPost() {
			
			@Override
			public void onMySuccess(ResponseInfo<String> responseInfo) {
				String s = responseInfo.result;
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(s);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (jsonObject.optBoolean("result")) {
					list.remove(position);
					notifyDataSetChanged();
				}
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				
			}
		});
	}
	
	
	public class ViewHolder{
		private TextView contentTxt;
		private TextView deleteTxt;
		private TextView systemMessageTxt;
		private ImageView portraitImg;
	}
}
