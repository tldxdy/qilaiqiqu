package com.qizhi.qilaiqiqu.adapter;

import java.util.ArrayList;
import java.util.List;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.RidingDetailsActivity;
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
	
	
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		View zeroView;
		View oneView;
		View twoView;
		View threeView;
		
		currentType = getItemViewType(position);
		if(currentType == TYPE_ZERO){
			zeroView = inflater.inflate(R.layout.item_list_riding_details_header,
					null);
			ImageView photoImg = (ImageView) zeroView.findViewById(R.id.img_ridingDetailsList_photo);
			TextView title = (TextView) zeroView.findViewById(R.id.txt_ridingDetailsList_ridingName);
			
			SystemUtil.Imagexutils(list.get(position).getUserImage(), photoImg, context);
			title.setText(list.get(position).getTitle());
			view = zeroView;
		}else if(currentType == TYPE_ONE){
			ViewHolder oneholder = null;
			//if(view == null){
				oneholder = new ViewHolder();
				oneView = inflater.inflate(R.layout.item_list_riding_details_body,
						null);
				oneholder.pictureImg = (ImageView) oneView
						.findViewById(R.id.img_ridingDatilsList_picture);
				oneholder.cornerImg = (ImageView) oneView
						.findViewById(R.id.img_ridingDatilsList_corner);
				
				oneholder.ridingTitleTxt = (TextView) oneView
						.findViewById(R.id.txt_ridingDatilsList_ridingTitle);
				oneholder.locationTxt = (TextView) oneView
						.findViewById(R.id.txt_ridingDatilsList_location);
				oneholder.ridingContentTxt = (TextView) oneView
						.findViewById(R.id.txt_ridingDatilsList_ridingContent);
				
				oneholder.ridingContentLayout = (LinearLayout) oneView
						.findViewById(R.id.layout_ridingDatilsList_ridingContent);
				oneholder.ridingTitleLayout = (LinearLayout) oneView
						.findViewById(R.id.layout_ridingDatilsList_ridingTitle);
				oneholder.locationLayout = (LinearLayout) oneView
						.findViewById(R.id.layout_ridingDatilsList_location);

				//oneView.setTag(oneholder);
				view = oneView;
			//}else{
		//		oneholder = (ViewHolder) view.getTag();
		//	}
			oneholder.ridingTitleTxt.setText("");
			oneholder.ridingContentTxt.setText("");
			oneholder.locationTxt.setText("");
			oneholder.ridingTitleLayout.setVisibility(View.GONE);
			oneholder.ridingContentLayout.setVisibility(View.GONE);
			oneholder.locationLayout.setVisibility(View.GONE);
			oneholder.cornerImg.setVisibility(View.GONE);
			
			if(!list.get(position-1).getArticleDetailList().get(position-1).getMemo().equals("")){
				oneholder.ridingTitleLayout.setVisibility(View.VISIBLE);
				oneholder.ridingTitleTxt.setText(" \u3000" + list.get(position-1).getArticleDetailList().get(position-1).getMemo());
			}
			if(!list.get(position-1).getArticleDetailList().get(position-1).getAddress().equals("")){
				oneholder.locationLayout.setVisibility(View.VISIBLE);
				oneholder.cornerImg.setVisibility(View.VISIBLE);
				oneholder.locationTxt.setText(list.get(position-1).getArticleDetailList().get(position-1).getAddress());
			}
			if(!list.get(position-1).getArticleDetailList().get(position-1).getImageMemo().equals("")){
				oneholder.ridingContentLayout.setVisibility(View.VISIBLE);
				oneholder.ridingContentTxt.setText(" \u3000" + list.get(position-1).getArticleDetailList().get(position-1).getImageMemo());
			}

			SystemUtil.Imagexutils(
					list.get(position-1).getArticleDetailList().get(position-1).getArticleImage(), oneholder.pictureImg, context);
			
			
		}else if(currentType == TYPE_TWO){
			twoView = inflater.inflate(R.layout.item_list_riding_details_recommend,
					null);
			TextView title = (TextView) twoView.findViewById(R.id.txt_riding_details_activity_recommend);
			
			title.setText("推荐活动");
			view = twoView;
		}else if(currentType == TYPE_THREE){
			ActivityHolder twoholder = null;
			//if(view == null){
				twoholder = new ActivityHolder();
				threeView = inflater.inflate(R.layout.item_list_riding_details_fooder,
						null);

				twoholder.headphotoImg = (ImageView) threeView
						.findViewById(R.id.img_riding_details_headphoto);
				twoholder.showphotoImg = (ImageView) threeView
						.findViewById(R.id.img_riding_details_showphoto);
				
				twoholder.activityTitleTxt = (TextView) threeView
						.findViewById(R.id.txt_riding_details_title);
				twoholder.activitynameTxt = (TextView) threeView
						.findViewById(R.id.txt_riding_details_name);

				SystemUtil.Imagexutils(lists.get(position - list.size() - 2).getUserImage(), twoholder.headphotoImg, context);
				
				if(lists.get(position - list.size() - 2).getDefaultImage() == null || "null".equals(lists.get(position - list.size() - 2))
						|| "".equals(lists.get(position - list.size() - 2))){
					twoholder.showphotoImg.setImageResource(R.drawable.bitmap_homepage);
				}else{
					SystemUtil.Imagexutils(lists.get(position - list.size() - 2).getDefaultImage(), twoholder.showphotoImg, context);
				}
				
				twoholder.activityTitleTxt.setText(lists.get(position - list.size() - 2).getActivityTitle());
				twoholder.activitynameTxt.setText(lists.get(position - list.size() - 2).getUserName());
				
				
				
				
			//	threeView.setTag(twoholder);
				view = threeView;
			//}else{
	//			twoholder = (ActivityHolder) view.getTag();
		//	}
			
			
			
		}
		
		
		
/*		holder = new ViewHolder();
		if(position == 0){
			view = inflater.inflate(R.layout.item_list_riding_details_header,
					null);
			ImageView photoImg = (ImageView) view.findViewById(R.id.img_ridingDetailsList_photo);
			TextView title = (TextView) view.findViewById(R.id.txt_ridingDetailsList_ridingName);
			
			SystemUtil.Imagexutils(list.get(position).getUserImage(), photoImg, context);
			title.setText(list.get(position).getTitle());
			
			return view;
		}*/
		
		
		

/*		if (view == null || view.getTag() == null) {

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
		
		if(!list.get(position-1).getArticleDetailList().get(position-1).getMemo().equals("")){
			holder.ridingTitleLayout.setVisibility(View.VISIBLE);
			holder.ridingTitleTxt.setText(" \u3000" + list.get(position-1).getArticleDetailList().get(position-1).getMemo());
		}
		if(!list.get(position-1).getArticleDetailList().get(position-1).getAddress().equals("")){
			holder.locationLayout.setVisibility(View.VISIBLE);
			holder.cornerImg.setVisibility(View.VISIBLE);
			holder.locationTxt.setText(list.get(position-1).getArticleDetailList().get(position-1).getAddress());
		}
		if(!list.get(position-1).getArticleDetailList().get(position-1).getImageMemo().equals("")){
			holder.ridingContentLayout.setVisibility(View.VISIBLE);
			holder.ridingContentTxt.setText(" \u3000" + list.get(position-1).getArticleDetailList().get(position-1).getImageMemo());
		}

		SystemUtil.Imagexutils(
				list.get(position-1).getArticleDetailList().get(position-1).getArticleImage()
						.split("@")[0], holder.pictureImg, context);
		*/
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
	
	public class ActivityHolder {
		private ImageView headphotoImg;
		private ImageView showphotoImg;
		private TextView activityTitleTxt;
		private TextView activitynameTxt;
	  }

}
