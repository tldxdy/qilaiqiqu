package com.qizhi.qilaiqiqu.adapter;

import java.util.List;
import com.lidroid.xutils.BitmapUtils;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.TravelsinformationModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Previewadapter extends BaseAdapter {

	private ViewHolder holder;
	private LayoutInflater inflater;

	private Context context;
	private List<TravelsinformationModel> list;
	private BitmapUtils bitmapUtils;
	private SharedPreferences preferences;
	

	public Previewadapter(Context context, List<TravelsinformationModel> list, SharedPreferences preferences) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		bitmapUtils = new BitmapUtils(context);
		this.preferences = preferences;
	}

	@Override
	public int getCount() {
		return list.size() + 1;
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
	public View getView(int position, View view, ViewGroup arg2) {
		holder = new ViewHolder();
		if(position == 0){
			view = inflater.inflate(R.layout.item_list_riding_details_header,
					null);
			ImageView photoImg = (ImageView) view.findViewById(R.id.img_ridingDetailsList_photo);
			TextView title = (TextView) view.findViewById(R.id.txt_ridingDetailsList_ridingName);
			ImageView oneImg = (ImageView) view.findViewById(R.id.img_ridingDatilsList_one);
			RelativeLayout oneLayout = (RelativeLayout) view.findViewById(R.id.layout_ridingDatilsList_one);
			
			
			SystemUtil.Imagexutils(preferences.getString("userImage", null), photoImg, context);
			title.setText(list.get(position).getTitle());
			bitmapUtils.display(oneImg, list.get(position).getArticleImage());			
			
			if(RidingDetailsListAdapter.height == 0){
				oneImg.measure(0, 0);
				RidingDetailsListAdapter.height = oneLayout.getMeasuredHeight();
			}
			
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

		
		if(list.get(position-1).getImageMemo() != null && !"".equals(list.get(position-1).getImageMemo().trim())){
			holder.ridingContentLayout.setVisibility(View.VISIBLE);
			holder.ridingContentTxt.setText(" \u3000" + list.get(position-1).getImageMemo());
		}
		if(list.get(position-1).getMemo() != null && !"".equals(list.get(position-1).getMemo().trim())){
			holder.ridingTitleLayout.setVisibility(View.VISIBLE);
			holder.ridingTitleTxt.setText(" \u3000" + list.get(position-1).getMemo());
		}
		if(list.get(position-1).getAddress() != null && !"".equals(list.get(position-1).getAddress().trim())){
			holder.locationLayout.setVisibility(View.VISIBLE);
			holder.cornerImg.setVisibility(View.VISIBLE);
			holder.locationTxt.setText(" \u3000" + list.get(position-1).getAddress());
		}
	bitmapUtils.display(holder.pictureImg, list.get(position-1).getArticleImage());	


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
