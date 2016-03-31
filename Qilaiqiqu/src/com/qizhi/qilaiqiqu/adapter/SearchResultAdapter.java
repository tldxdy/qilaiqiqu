package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.PersonActivity;
import com.qizhi.qilaiqiqu.model.SearchResultModel.SearchDataList;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

public class SearchResultAdapter extends BaseAdapter {

	private List<SearchDataList> list;
	private Context context;
	private QYJViewHolder QYJHolder;
	private HDViewHolder HDHolder;
	private LayoutInflater inflater;
	private int fragmentNum;
	private XUtilsUtil xUtilsUtil;
	private SharedPreferences preferences;
	private int userId;

	public SearchResultAdapter(Context context, List<SearchDataList> list,
			int fragmentNum , int id) {
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
		preferences = context.getSharedPreferences("userLogin",
				Context.MODE_PRIVATE);
		xUtilsUtil = new XUtilsUtil();
		this.userId = id;
		this.fragmentNum = fragmentNum;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		if (fragmentNum == 0) {
			if (convertView == null || convertView.getTag() == null) {
				QYJHolder = new QYJViewHolder();
				convertView = inflater.inflate(
						R.layout.item_list_mainactivity_body, null);
				QYJHolder.timeTxt = (TextView) convertView
						.findViewById(R.id.txt_mainList_time);
				QYJHolder.likeTxt = (TextView) convertView
						.findViewById(R.id.txt_ridinglist_like);
				QYJHolder.likeImg = (ImageView) convertView
						.findViewById(R.id.img_ridinglist_like);
				QYJHolder.titleTxt = (TextView) convertView
						.findViewById(R.id.txt_mainList_title);
				QYJHolder.photoImg = (ImageView) convertView
						.findViewById(R.id.img_mainList_photo);
				QYJHolder.byBrowseTxt = (TextView) convertView
						.findViewById(R.id.txt_mainList_byBrowse);
				QYJHolder.backgroundImg = (ImageView) convertView
						.findViewById(R.id.img_mainList_background);

				convertView.setTag(QYJHolder);
			} else {
				QYJHolder = (QYJViewHolder) convertView.getTag();
			}
			if (list.get(position).getType().toString().equals("QYJ")) {
				System.out.println(list.get(position).getType());
				QYJHolder.timeTxt.setText(list.get(position).getCreateDate()
						.substring(0, 10));
				QYJHolder.titleTxt.setText(list.get(position).getTitle());
				QYJHolder.byBrowseTxt.setText(list.get(position).getScanNum()
						+ list.get(position).getScanNum() + "次浏览");
				QYJHolder.likeTxt.setTextColor(R.color.white);
				QYJHolder.likeTxt
						.setText((list.get(position).getPraiseNum() + list.get(
								position).getPraiseNum())
								+ "");
				SystemUtil.loadImagexutils(list.get(position).getUserImage(),
						QYJHolder.photoImg, context);
				QYJHolder.likeImg.setImageResource(R.drawable.like_unpress);
				// QYJHolder.likeImg.setTag(position);
				// QYJHolder.photoImg.setImageResource(R.drawable.lena);

				SystemUtil.Imagexutils(list.get(position).getDefaultImage(),
						QYJHolder.backgroundImg, context);

				/*
				 * String internetUrl =
				 * "http://weride.oss-cn-hangzhou.aliyuncs.com/" +
				 * list.get(position).getDefaultImage().split("\\@")[0];
				 * Picasso.with
				 * (context).load(internetUrl).into(QYJHolder.backgroundImg);
				 */

				// QYJHolder.backgroundImg.setBackgroundResource(R.drawable.demo3);
				QYJHolder.photoImg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						System.out.println(position);
						Intent intent = new Intent(context,
								PersonActivity.class);
						intent.putExtra("userId", list.get(position)
								.getUserId());
						context.startActivity(intent);

					}
				});
				if (list.get(position).isPraised()) {
					QYJHolder.likeImg.setImageResource(R.drawable.like_press);
					QYJHolder.likeTxt.setTextColor(0xffffffff);

				} else {
					QYJHolder.likeImg.setImageResource(R.drawable.like_unpress);
					QYJHolder.likeTxt.setTextColor(0xffff0000);

				}

				QYJHolder.likeImg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (list.get(position).isPraised()) {
							unPraise(position);
						} else {
							praise(position);
						}
					}

				});
			}
		} else {
			if (convertView == null || convertView.getTag() == null) {
				HDHolder = new HDViewHolder();
				convertView = inflater.inflate(
						R.layout.item_list_manage_fragment, null);
				HDHolder.titleTxt = (TextView) convertView
						.findViewById(R.id.txt_managefragment_title);
				HDHolder.detailedInformationTxt = (TextView) convertView
						.findViewById(R.id.txt_managefragment_detailed_information);
				HDHolder.yearsTxt = (TextView) convertView
						.findViewById(R.id.txt_managefragment_years);
				HDHolder.timeTxt = (TextView) convertView
						.findViewById(R.id.txt_managefragment_time);
				HDHolder.applyTxt = (TextView) convertView
						.findViewById(R.id.txt_managefragment_apply);

				HDHolder.photoImg = (ImageView) convertView
						.findViewById(R.id.img_managefragment_photo);
				HDHolder.applyImg = (ImageView) convertView
						.findViewById(R.id.img_managefragment_apply);
				HDHolder.moneyTxt = (TextView) convertView
						.findViewById(R.id.txt_managefragment_money);
				convertView.setTag(HDHolder);
			} else {
				HDHolder = (HDViewHolder) convertView.getTag();
			}
			
			if (list.get(position).getType().toString().equals("HD")) {
				int duration = Integer.parseInt(list.get(position).getDuration())/60;
				if(duration >= 24){
					int days = duration / 24;
					duration = duration % 24;
					HDHolder.timeTxt.setText(days + "天" + duration + "小时");
				}else{
					if(duration == 0){
						HDHolder.timeTxt.setText("1小时");
					}else{
						HDHolder.timeTxt.setText(duration + "小时");
					}
				}
				
				HDHolder.yearsTxt.setText(list.get(position).getStartDate().subSequence(0, 10));
				HDHolder.titleTxt.setText(list.get(position).getTitle());
				HDHolder.applyTxt.setTextColor(0xff000000);
				if(userId == list.get(position).getUserId()){
					HDHolder.applyImg.setImageResource(R.drawable.activity_irelease);
					HDHolder.applyTxt.setText("我发起");
				}else{
					HDHolder.applyImg.setImageResource(R.drawable.take_parted);
					if(list.get(position).isInvolved()){
						HDHolder.applyTxt.setText("已报名");
						HDHolder.applyTxt.setTextColor(0xff6dbfed);
					}else{
						HDHolder.applyImg.setImageResource(R.drawable.take_parting);
						HDHolder.applyTxt.setText("立即报名");
					}
				}
				HDHolder.photoImg.setImageResource(R.drawable.bitmap_homepage);
				if(!"".equals(list.get(position).getDefaultImage()) && list.get(position).getDefaultImage() != null){
					SystemUtil.Imagexutils(list.get(position).getDefaultImage(), HDHolder.photoImg, context);
				}
				
				if(list.get(position).getOutlay() == null || "免费".equals(list.get(position).getOutlay()) || "null".equals(list.get(position).getOutlay()) || "0".equals(list.get(position).getOutlay())){
					HDHolder.moneyTxt.setVisibility(View.GONE);
					HDHolder.moneyTxt.setText("免费");
				}else{
					HDHolder.moneyTxt.setVisibility(View.VISIBLE);
					HDHolder.moneyTxt.setText("收费");
				}
			}
			
		}
		return convertView;
	}

	private class QYJViewHolder {
		private TextView titleTxt;
		private TextView timeTxt;
		private TextView byBrowseTxt;
		private ImageView likeImg;
		private ImageView photoImg;
		private ImageView backgroundImg;
		private TextView likeTxt;

	}

	private class HDViewHolder {
		private TextView titleTxt;
		private TextView detailedInformationTxt;
		private TextView yearsTxt;
		private TextView timeTxt;
		private TextView applyTxt;
		private TextView moneyTxt;
		private ImageView photoImg;
		private ImageView applyImg;
	}

	private void praise(final int position) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("articleId", list.get(position).getId() + "");
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		if (preferences.getInt("userId", -1) != -1) {
			xUtilsUtil.httpPost("mobile/articleMemo/praiseArticle.html",
					params, new CallBackPost() {

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
								list.get(position).setPraised(true);
								list.get(position).setPraiseNum(
										list.get(position).getPraiseNum() + 2);
								notifyDataSetChanged();
							}
						}

						@Override
						public void onMyFailure(HttpException error, String msg) {

						}
					});
		}
	}

	private void unPraise(final int position) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("articleId", list.get(position).getId() + "");
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/articleMemo/unPraiseArticle.html", params,
				new CallBackPost() {

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
							list.get(position).setPraised(false);
							list.get(position).setPraiseNum(
									list.get(position).getPraiseNum() - 2);
							notifyDataSetChanged();
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {

					}
				});
	}

}
