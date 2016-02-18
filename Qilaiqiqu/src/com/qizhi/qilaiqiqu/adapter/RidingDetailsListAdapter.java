package com.qizhi.qilaiqiqu.adapter;

import java.util.ArrayList;
import java.util.List;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.ActivityListRecommendModel;
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
	
	private static final int TYPE_ZERO = 0;
	private static final int TYPE_ONE = 1;
	private static final int TYPE_TWO = 2;
	private static final int TYPE_THREE = 3;
	private int currentType;//当前item类型

	private LayoutInflater inflater;

	private Context context;
	private List<ArticleModel> list;
	
	private List<ActivityListRecommendModel> lists;

	public RidingDetailsListAdapter(Context context, List<ArticleModel> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		lists = new ArrayList<ActivityListRecommendModel>();
	}
	
	public RidingDetailsListAdapter(Context context, List<ArticleModel> list,List<ActivityListRecommendModel> lists) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return list.size() + 1 + lists.size() + 1;
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
	public int getItemViewType(int position) {
		if(position == 0){
			return TYPE_ZERO;
		}
		
		if(position < list.size() + 1){
			return TYPE_ONE;
		}
		if(position == list.size() + 1){
			return TYPE_TWO;
		}
		if(position < list.size() + 1 + lists.size() + 1){
			return TYPE_THREE;
		}
		
		return super.getItemViewType(position);
	}
	
	  //返回样式的数量
    @Override
    public int getViewTypeCount() {
    	
        return 4;
    }
	
	
	
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ZeroViewHolder zeroholder = null;
		OneViewHolder oneholder = null;
		TwoViewHolder twoholder = null;
		ThreeViewHolder threeholder = null;
		currentType = getItemViewType(position);
		
		if(view == null){
			switch (currentType) {
			case TYPE_ZERO:
				zeroholder = new ZeroViewHolder();
				view = inflater.inflate(R.layout.item_list_riding_details_header,
						null);
				zeroholder.photoImg = (ImageView) view.findViewById(R.id.img_ridingDetailsList_photo);
				zeroholder.title = (TextView) view.findViewById(R.id.txt_ridingDetailsList_ridingName);
				
				view.setTag(zeroholder);
				
				break;
			case TYPE_ONE:
				oneholder = new OneViewHolder();
				view = inflater.inflate(R.layout.item_list_riding_details_body,
						null);
				oneholder.pictureImg = (ImageView) view
						.findViewById(R.id.img_ridingDatilsList_picture);
				oneholder.cornerImg = (ImageView) view
						.findViewById(R.id.img_ridingDatilsList_corner);
				
				oneholder.ridingTitleTxt = (TextView) view
						.findViewById(R.id.txt_ridingDatilsList_ridingTitle);
				oneholder.locationTxt = (TextView) view
						.findViewById(R.id.txt_ridingDatilsList_location);
				oneholder.ridingContentTxt = (TextView) view
						.findViewById(R.id.txt_ridingDatilsList_ridingContent);
				
				oneholder.ridingContentLayout = (LinearLayout) view
						.findViewById(R.id.layout_ridingDatilsList_ridingContent);
				oneholder.ridingTitleLayout = (LinearLayout) view
						.findViewById(R.id.layout_ridingDatilsList_ridingTitle);
				oneholder.locationLayout = (LinearLayout) view
						.findViewById(R.id.layout_ridingDatilsList_location);
				oneholder.view = view.findViewById(R.id.v_ridingDatilsList_location);
				
				view.setTag(oneholder);
				
				break;
			case TYPE_TWO:
				twoholder = new TwoViewHolder();
				view = inflater.inflate(R.layout.item_list_riding_details_recommend,
						null);
				twoholder.title = (TextView) view.findViewById(R.id.txt_riding_details_activity_recommend);

				view.setTag(twoholder);
				
	
				break;
			case TYPE_THREE:
				threeholder = new ThreeViewHolder();
				view = inflater.inflate(R.layout.item_list_riding_details_fooder,
						null);

				threeholder.headphotoImg = (ImageView) view
						.findViewById(R.id.img_riding_details_headphoto);
				threeholder.showphotoImg = (ImageView) view
						.findViewById(R.id.img_riding_details_showphoto);
				
				threeholder.activityTitleTxt = (TextView) view
						.findViewById(R.id.txt_riding_details_title);
				threeholder.activitynameTxt = (TextView) view
						.findViewById(R.id.txt_riding_details_name);
				
				view.setTag(threeholder);
				
				break;
			}
		}else{
			switch (currentType) {
			case TYPE_ZERO:
				zeroholder = (ZeroViewHolder) view.getTag();
				break;
			case TYPE_ONE:
				oneholder = (OneViewHolder) view.getTag();
				break;
			case TYPE_TWO:
				twoholder = (TwoViewHolder) view.getTag();
				break;
			case TYPE_THREE:
				threeholder = (ThreeViewHolder) view.getTag();
				break;
			}
		}
		
		switch (currentType) {
		case TYPE_ZERO:
			SystemUtil.Imagexutils(list.get(position).getUserImage(), zeroholder.photoImg, context);
			zeroholder.title.setText(list.get(position).getTitle());
			
			break;
		case TYPE_ONE:
			oneholder.ridingTitleTxt.setText("");
			oneholder.ridingContentTxt.setText("");
			oneholder.locationTxt.setText("");
			oneholder.ridingTitleLayout.setVisibility(View.GONE);
			oneholder.ridingContentLayout.setVisibility(View.GONE);
			oneholder.locationLayout.setVisibility(View.GONE);
			oneholder.cornerImg.setVisibility(View.GONE);
			oneholder.view.setVisibility(View.GONE);
			
			if(!list.get(position - 1).getArticleDetailList().get(position - 1).getMemo().equals("")){
				oneholder.ridingTitleLayout.setVisibility(View.VISIBLE);
				oneholder.view.setVisibility(View.VISIBLE);
				oneholder.ridingTitleTxt.setText(" \u3000" + list.get(position - 1).getArticleDetailList().get(position - 1).getMemo());
			}
			if(!list.get(position - 1).getArticleDetailList().get(position - 1).getAddress().equals("")){
				oneholder.locationLayout.setVisibility(View.VISIBLE);
				oneholder.cornerImg.setVisibility(View.VISIBLE);
				oneholder.locationTxt.setText(list.get(position - 1).getArticleDetailList().get(position - 1).getAddress());
			}
			if(!list.get(position - 1).getArticleDetailList().get(position - 1).getImageMemo().equals("")){
				oneholder.ridingContentLayout.setVisibility(View.VISIBLE);
				oneholder.ridingContentTxt.setText(list.get(position - 1).getArticleDetailList().get(position - 1).getImageMemo());
			}

			SystemUtil.Imagexutils(
					list.get(position - 1).getArticleDetailList().get(position - 1).getArticleImage(), oneholder.pictureImg, context);
			
			
			
			break;
		case TYPE_TWO:

			twoholder.title.setText("推荐活动");
			
			break;
		case TYPE_THREE:

			SystemUtil.Imagexutils(lists.get(position - list.size() - 2).getUserImage(), threeholder.headphotoImg, context);
			
			if(lists.get(position - list.size() - 2).getDefaultImage() == null || "null".equals(lists.get(position - list.size() - 2))
					|| "".equals(lists.get(position - list.size() - 2))){
				threeholder.showphotoImg.setImageResource(R.drawable.bitmap_homepage);
			}else{
				SystemUtil.Imagexutils(lists.get(position - list.size() - 2).getDefaultImage(), threeholder.showphotoImg, context);
			}
			
			threeholder.activityTitleTxt.setText(lists.get(position - list.size() - 2).getActivityTitle());
			threeholder.activitynameTxt.setText(lists.get(position - list.size() - 2).getUserName());
			
			break;
		}
		
		
		return view;
	}
	
	public class ZeroViewHolder{
		private ImageView photoImg;
		private TextView title;
	}
	
	
	public class OneViewHolder {
		private ImageView pictureImg;
		private ImageView cornerImg;
		private TextView ridingTitleTxt;
		private TextView locationTxt;
		private TextView ridingContentTxt;

		private LinearLayout ridingTitleLayout;
		private LinearLayout locationLayout;
		private LinearLayout ridingContentLayout;
		
		private View view;
		
	}
	
	public class TwoViewHolder{
		private TextView title;
	}
	
	public class ThreeViewHolder {
		private ImageView headphotoImg;
		private ImageView showphotoImg;
		private TextView activityTitleTxt;
		private TextView activitynameTxt;
	  }

}
