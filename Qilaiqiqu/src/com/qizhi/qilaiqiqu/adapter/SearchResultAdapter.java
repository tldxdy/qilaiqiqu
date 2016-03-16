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
import com.squareup.picasso.Picasso;

public class SearchResultAdapter extends BaseAdapter {

	private List<SearchDataList> list;
	private Context context;
	private ViewHolder holder;
	private LayoutInflater inflater;

	private XUtilsUtil xUtilsUtil;
	private SharedPreferences preferences;

	public SearchResultAdapter(Context context, List<SearchDataList> list) {
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
		preferences = context.getSharedPreferences("userLogin",
				Context.MODE_PRIVATE);
		xUtilsUtil = new XUtilsUtil();
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
	public View getView(final int position, View v, ViewGroup arg2) {
		if (v == null || v.getTag() == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.item_list_mainactivity_body, null);
			holder.timeTxt = (TextView) v.findViewById(R.id.txt_mainList_time);
			holder.likeTxt = (TextView) v
					.findViewById(R.id.txt_ridinglist_like);
			holder.likeImg = (ImageView) v
					.findViewById(R.id.img_ridinglist_like);
			holder.titleTxt = (TextView) v
					.findViewById(R.id.txt_mainList_title);
			holder.photoImg = (ImageView) v
					.findViewById(R.id.img_mainList_photo);
			holder.byBrowseTxt = (TextView) v
					.findViewById(R.id.txt_mainList_byBrowse);
			holder.backgroundImg = (ImageView) v
					.findViewById(R.id.img_mainList_background);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		if (list.get(position).getType().toString().equals("QYJ")) {
			System.out.println(list.get(position).getType());
			holder.timeTxt.setText(list.get(position).getCreateDate()
					.substring(0, 10));
			holder.titleTxt.setText(list.get(position).getTitle());
			holder.byBrowseTxt.setText(list.get(position).getScanNum()
					+ list.get(position).getScanNum() + "次浏览");
			holder.likeTxt.setTextColor(R.color.white);
			holder.likeTxt.setText((list.get(position).getPraiseNum() + list
					.get(position).getPraiseNum()) + "");
			SystemUtil.loadImagexutils(list.get(position).getUserImage(),
					holder.photoImg, context);
			holder.likeImg.setImageResource(R.drawable.like_unpress);
			// holder.likeImg.setTag(position);
			// holder.photoImg.setImageResource(R.drawable.lena);

			SystemUtil.Imagexutils(list.get(position).getDefaultImage(),
					holder.backgroundImg, context);

			/*
			 * String internetUrl =
			 * "http://weride.oss-cn-hangzhou.aliyuncs.com/" +
			 * list.get(position).getDefaultImage().split("\\@")[0];
			 * Picasso.with
			 * (context).load(internetUrl).into(holder.backgroundImg);
			 */

			// holder.backgroundImg.setBackgroundResource(R.drawable.demo3);
			holder.photoImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					System.out.println(position);
					Intent intent = new Intent(context, PersonActivity.class);
					intent.putExtra("userId", list.get(position).getUserId());
					context.startActivity(intent);

				}
			});
			if (list.get(position).isPraised()) {
				holder.likeImg.setImageResource(R.drawable.like_press);
				holder.likeTxt.setTextColor(0xffffffff);

			} else {
				holder.likeImg.setImageResource(R.drawable.like_unpress);
				holder.likeTxt.setTextColor(0xffff0000);

			}

			holder.likeImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (list.get(position).isPraised()) {
						RequestParams params = new RequestParams();
						params.addBodyParameter("userId",
								preferences.getInt("userId", -1) + "");
						params.addBodyParameter("articleId", list.get(position)
								.getId() + "");
						params.addBodyParameter("uniqueKey",
								preferences.getString("uniqueKey", null));
						xUtilsUtil.httpPost(
								"mobile/articleMemo/unPraiseArticle.html",
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
											list.get(position)
													.setPraised(false);
											list.get(position)
													.setPraiseNum(
															list.get(position)
																	.getPraiseNum() - 2);
											notifyDataSetChanged();
										}
									}

									@Override
									public void onMyFailure(
											HttpException error, String msg) {

									}
								});
					} else {
						RequestParams params = new RequestParams();
						params.addBodyParameter("userId",
								preferences.getInt("userId", -1) + "");
						params.addBodyParameter("articleId", list.get(position)
								.getId() + "");
						params.addBodyParameter("uniqueKey",
								preferences.getString("uniqueKey", null));
						if (preferences.getInt("userId", -1) != -1) {
							xUtilsUtil.httpPost(
									"mobile/articleMemo/praiseArticle.html",
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
												list.get(position).setPraised(
														true);
												list.get(position)
														.setPraiseNum(
																list.get(
																		position)
																		.getPraiseNum() + 2);
												notifyDataSetChanged();
											}
										}

										@Override
										public void onMyFailure(
												HttpException error, String msg) {

										}
									});
						}
					}
				}
			});
		}
		return v;
	}

	private class ViewHolder {
		private TextView titleTxt;
		private TextView timeTxt;
		private TextView byBrowseTxt;
		private ImageView likeImg;
		private ImageView photoImg;
		private ImageView backgroundImg;
		private TextView likeTxt;

	}

}
