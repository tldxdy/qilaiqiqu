package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.RidingCommentModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DiscussListAdapter extends BaseAdapter {

	private Context context;
	
	private LayoutInflater inflater;
	
	private List<RidingCommentModel> list;
	
	private ViewHolder holder;
	
	public DiscussListAdapter(Context context, List<RidingCommentModel> list){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
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
		
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_list_discussactivity, null);
			holder.contentTxt = (TextView) convertView.findViewById(R.id.txt_discussactivity_content);
			holder.discusspeoTxt = (TextView) convertView.findViewById(R.id.txt_discussactivity_discusspeo);
			holder.portraitImg = (ImageView) convertView.findViewById(R.id.img_discussactivity_photo);
			holder.discussReturnLayout = (LinearLayout) convertView.findViewById(R.id.layout_discussactivity_discuss_return);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		SystemUtil.Imagexutils(list.get(position).getUserImage(), holder.portraitImg, context);
		
		
		
		String s = "<font color='#6dbfed'>"+list.get(position).getUserName()+"</font>评论";
		holder.discusspeoTxt.setText(Html.fromHtml(s));
		holder.contentTxt.setText(list.get(position).getCommentMemo());
		//holder.portraitImg
		
		holder.discussReturnLayout.removeAllViews();
		if(list.get(position).getSubCommentList().size() != 0){
			List<RidingCommentModel> lists = list.get(position).getSubCommentList();
			for (RidingCommentModel ridingCommentModel : lists) {
				TextView discussReturnTxt = (TextView) View.inflate(context, R.layout.text_discussactivity_discuss_return, null);
				
				String str1 = "<font color='#6dbfed'>"+ridingCommentModel.getUserName()+"</font>回复<font color='#6dbfed'>"+ridingCommentModel.getParentName()+"</font>"+ridingCommentModel.getCommentMemo();
				discussReturnTxt.setText(Html.fromHtml(str1));
				
				holder.discussReturnLayout.addView(discussReturnTxt);
			}
		}
		
		/*if(position == 1){
			for(int i=0;i<3;i++){
			TextView discussReturnTxt = (TextView) View.inflate(context, R.layout.text_discussactivity_discuss_return, null);
			
			String str1 = "<font color='#6dbfed'>独步天下</font>评论<font color='#6dbfed'>小伙子</font>：可以呀，一起才有意思！！";
			discussReturnTxt.setText(Html.fromHtml(str1));
			
			holder.discussReturnLayout.addView(discussReturnTxt);
			}
		}*/
		
		return convertView;
	}

	private class ViewHolder{
		private TextView contentTxt;
		private TextView discusspeoTxt;
		private ImageView portraitImg;
		private LinearLayout discussReturnLayout;
	}
	
	
	
}
