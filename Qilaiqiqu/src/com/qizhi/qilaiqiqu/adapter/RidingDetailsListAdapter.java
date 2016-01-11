package com.qizhi.qilaiqiqu.adapter;

import java.util.List;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.RidingDetailsActivity;
import com.qizhi.qilaiqiqu.model.ArticleModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RidingDetailsListAdapter extends BaseAdapter {

	private ViewHolder holder;
	private LayoutInflater inflater;

	private Context context;
	private List<ArticleModel> list;

	public RidingDetailsListAdapter(Context context, List<ArticleModel> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size() + 1;
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
		System.out.println("--------------------------------");
		System.out.println(list.size());
		System.out.println("--------------------------------");
		holder = new ViewHolder();
		if(position == 0){
			view = inflater.inflate(R.layout.item_list_riding_details_header,
					null);
			ImageView photoImg = (ImageView) view.findViewById(R.id.img_ridingDetailsList_photo);
			TextView title = (TextView) view.findViewById(R.id.txt_ridingDetailsList_ridingName);
			
			SystemUtil.Imagexutils(list.get(position).getUserImage(), photoImg, context);
			title.setText(list.get(position).getTitle());
			
			return view;
		}
		
		
		

		if (view == null || view.getTag() == null) {

			view = inflater.inflate(R.layout.item_list_riding_details_body,
					null);
			
			holder.pictureImg = (ImageView) view
					.findViewById(R.id.img_ridingDatilsList_picture);
			holder.cornerImg = (ImageView) view
					.findViewById(R.id.img_ridingDatilsList_corner);
			
			holder.ridingTitleTxt = (TextView) view
					.findViewById(R.id.txt_ridingDatilsList_ridingTitle);
			holder.locationTxt = (TextView) view
					.findViewById(R.id.txt_ridingDatilsList_location);
			holder.ridingContentTxt = (TextView) view
					.findViewById(R.id.txt_ridingDatilsList_ridingContent);
			
			holder.ridingContentLayout = (LinearLayout) view
					.findViewById(R.id.layout_ridingDatilsList_ridingContent);
			holder.ridingTitleLayout = (LinearLayout) view
					.findViewById(R.id.layout_ridingDatilsList_ridingTitle);
			holder.locationLayout = (LinearLayout) view
					.findViewById(R.id.layout_ridingDatilsList_location);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.ridingTitleTxt.setText("");
		holder.ridingContentTxt.setText("");
		holder.locationTxt.setText("");
		holder.ridingTitleLayout.setVisibility(View.GONE);
		holder.ridingContentLayout.setVisibility(View.GONE);
		holder.locationLayout.setVisibility(View.GONE);
		holder.cornerImg.setVisibility(View.GONE);

		String[] s = list.get(position-1).getImageMemo().split("\\|");
		String[] ss = list.get(position-1).getMemo().split("\\|");
		String[] sss = list.get(position-1).getAddress().split("\\|");
		if (position-1 < ss.length) {
			if (!"".equals(ss[position-1].trim())) {
				if(!"null".equals(ss[position-1].trim())){
					holder.ridingTitleLayout.setVisibility(View.VISIBLE);
				}
			}
			holder.ridingTitleTxt.setText(" \u3000" + ss[position-1]);
		}
		if (position-1 < s.length) {
			if (!"".equals(s[position-1].trim())) {
				if(!"null".equals(s[position-1].trim())){
					holder.ridingContentLayout.setVisibility(View.VISIBLE);
				}
			}
			holder.ridingContentTxt.setText(" \u3000" + s[position-1]);

		}
		if (position-1 < sss.length) {
			if (!"".equals(sss[position-1].trim())) {
				if(!"null".equals(sss[position-1].trim())){
					holder.locationLayout.setVisibility(View.VISIBLE);
					holder.cornerImg.setVisibility(View.VISIBLE);
				}
			}
			holder.locationTxt.setText(sss[position-1]);
		}

		SystemUtil.Imagexutils(
				list.get(position-1).getArticleImage().split("\\|")[position-1]
						.split("@")[0], holder.pictureImg, context);
		
		/*SystemUtil.loadImagexutils(
				list.get(position).getArticleImage().split("\\|")[position]
						.split("@")[0], holder.pictureImg, context);*/


		return view;
	}
	
	public class ViewHolder {
		private ImageView pictureImg;
		private ImageView cornerImg;
		private TextView ridingTitleTxt;
		private TextView locationTxt;
		private TextView ridingContentTxt;

		private LinearLayout ridingTitleLayout;
		private LinearLayout locationLayout;
		private LinearLayout ridingContentLayout;
	}

}
