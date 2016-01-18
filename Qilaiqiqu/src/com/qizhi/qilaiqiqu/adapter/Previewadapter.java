package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.TravelsinformationModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Previewadapter extends BaseAdapter {

	private ViewHolder holder;
	private LayoutInflater inflater;

	private Context context;
	private List<TravelsinformationModel> list;
	private BitmapUtils bitmapUtils;
	private SharedPreferences preferences;
	private XUtilsUtil xUtilsUtil;
	

	public Previewadapter(Context context, List<TravelsinformationModel> list, SharedPreferences preferences) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		bitmapUtils = new BitmapUtils(context);
		this.preferences = preferences;
		xUtilsUtil = new XUtilsUtil();
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
			final ImageView photoImg = (ImageView) view.findViewById(R.id.img_ridingDetailsList_photo);
			TextView title = (TextView) view.findViewById(R.id.txt_ridingDetailsList_ridingName);
			
			//SystemUtil.Imagexutils(list.get(position)., photoImg, context);
			title.setText(list.get(position).getTitle());
			if (preferences.getInt("userId", -1) != -1) {
				RequestParams params = new RequestParams("UTF-8");
				params.addBodyParameter("userId", preferences.getInt("userId", -1)
						+ "");
				xUtilsUtil.httpPost("common/queryCertainUser.html", params,
						new CallBackPost() {

							@Override
							public void onMySuccess(
									ResponseInfo<String> responseInfo) {
								String s = responseInfo.result;
								JSONObject jsonObject = null;
								try {
									jsonObject = new JSONObject(s);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								if (jsonObject.optBoolean("result")) {
									JSONObject json = jsonObject
											.optJSONObject("data");
									SystemUtil.Imagexutils(
											json.optString("userImage"), photoImg,
											context);
								}
							}

							@Override
							public void onMyFailure(HttpException error, String msg) {

							}
						});
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
	/*Picasso.with(context).load(list.get(position-1).getArticleImage()).resize(800, 480)
	.centerInside().into(holder.pictureImg);*/
	bitmapUtils.display(holder.pictureImg, list.get(position-1).getArticleImage());	
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
