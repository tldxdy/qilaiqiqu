package com.qizhi.qilaiqiqu.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.qizhi.qilaiqiqu.activity.RidingDetailsActivity;
import com.qizhi.qilaiqiqu.model.ArticleModel;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil.ImageInfo;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.squareup.picasso.Picasso;

/**
 * 
 * @author leiqian
 * 
 */

public class SlideShowListAdapter extends BaseAdapter {

	private ViewHolder holder;
	private LayoutInflater inflater;

	private List<ArticleModel> list;
	private Context context;

	// private boolean falg = false;

	private ImageCycleViewUtil mImageCycleView;

	private XUtilsUtil xUtilsUtil;
	private SharedPreferences preferences;

	List<ImageCycleViewUtil.ImageInfo> IClist = new ArrayList<ImageCycleViewUtil.ImageInfo>();

	public SlideShowListAdapter(Context context, List<ArticleModel> list,
			List<ImageCycleViewUtil.ImageInfo> IClist) {
		this.context = context;
		this.list = list;
		this.IClist = IClist;
		inflater = LayoutInflater.from(context);
		preferences = context.getSharedPreferences("userLogin",
				Context.MODE_PRIVATE);
		xUtilsUtil = new XUtilsUtil();
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

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		if (position == 0) {
			view = inflater.inflate(R.layout.item_list_mainactivity_header,
					null);
			mImageCycleView = (ImageCycleViewUtil) view
					.findViewById(R.id.icv_topView);
			mImageCycleView.setCycleDelayed(3000);

			mImageCycleView
					.setOnPageClickListener(new ImageCycleViewUtil.OnPageClickListener() {

						@Override
						public void onClick(View imageView, ImageInfo imageInfo) {
							if (imageInfo.type.toString().equals("URL")) {
								Uri uri = Uri.parse(imageInfo.value.toString());
								Intent intent = new Intent(Intent.ACTION_VIEW,
										uri);
								context.startActivity(intent);
							} else if (imageInfo.type.toString().equals("APP")) {
								if (imageInfo.bannerType.toString().equals(
										"QYJ")) {

									context.startActivity(new Intent(context,
											RidingDetailsActivity.class)
											.putExtra("articleId", Integer
													.parseInt(imageInfo.value
															.toString())));
								} else if (imageInfo.bannerType.toString()
										.equals("PQS")) {

								} else if (imageInfo.bannerType.toString()
										.equals("HD")) {

								}
							}
						}
					});

			mImageCycleView.loadData(IClist,
					new ImageCycleViewUtil.LoadImageCallBack() {
						@Override
						public ImageView loadAndDisplay(
								ImageCycleViewUtil.ImageInfo imageInfo) {

							ImageView imageView = new ImageView(context);

							Picasso.with(context)
									.load(imageInfo.image.toString())
									.into(imageView);
							// imageView.setImageResource(R.drawable.demo);

							return imageView;

						}
					});
			return view;
		}

		if (view == null || view.getTag() == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_list_mainactivity_body, null);
			holder.timeTxt = (TextView) view
					.findViewById(R.id.txt_mainList_time);
			holder.titleTxt = (TextView) view
					.findViewById(R.id.txt_mainList_title);
			holder.byBrowseTxt = (TextView) view
					.findViewById(R.id.txt_mainList_byBrowse);
			holder.likeTxt = (TextView) view
					.findViewById(R.id.txt_mainList_like);
			holder.likeImg = (ImageView) view
					.findViewById(R.id.img_mainList_like);
			holder.photoImg = (ImageView) view
					.findViewById(R.id.img_mainList_photo);
			holder.backgroundImg = (ImageView) view
					.findViewById(R.id.img_mainList_background);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.timeTxt.setText(list.get(position - 1).getCreateDate()
				.substring(0, 10));
		holder.titleTxt.setText(list.get(position - 1).getTitle());
		holder.byBrowseTxt.setText(list.get(position - 1).getScanNum()
				+ list.get(position - 1).getVirtualScan() + "次浏览");
		holder.likeTxt.setText((list.get(position - 1).getPraiseNum() + list
				.get(position - 1).getVirtualPraise()) + "");

		SystemUtil.loadImagexutils(list.get(position - 1).getUserImage(),
				holder.photoImg, context);
		holder.likeImg.setImageResource(R.drawable.like_unpress);
		// holder.likeImg.setTag(position);
		// holder.photoImg.setImageResource(R.drawable.lena);
		/*
		 * SystemUtil.loadImagexutils(list.get(position -
		 * 1).getDefaultShowImage() .split("\\@")[0], holder.backgroundImg,
		 * context);
		 */

		// holder.backgroundImg.setImageResource(R.drawable.bitmap_homepage);
		if (list.get(position - 1).getDefaultShowImage() != null) {

			/*
			 * String internetUrl =
			 * "http://weride.oss-cn-hangzhou.aliyuncs.com/" + list.get(position
			 * - 1).getDefaultShowImage().split("\\@")[0];
			 */

			SystemUtil.Imagexutils(
					list.get(position - 1).getDefaultShowImage(),
					holder.backgroundImg, context);
			/*
			 * Picasso.with(context).load(internetUrl).resize(800, 480)
			 * .centerInside().into(holder.backgroundImg);
			 */
		}

		// holder.backgroundImg.setBackgroundResource(R.drawable.demo3);
		holder.photoImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println(position);
				Intent intent = new Intent(context, PersonActivity.class);
				intent.putExtra("userId", list.get(position - 1).getUserId());
				context.startActivity(intent);

			}
		});
		if (list.get(position - 1).isPraised()) {
			holder.likeImg.setImageResource(R.drawable.like_press);
			holder.likeTxt.setTextColor(0xffffffff);

		} else {
			holder.likeImg.setImageResource(R.drawable.like_unpress);
			holder.likeTxt.setTextColor(0xffff0000);

		}

		holder.likeImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list.get(position - 1).isPraised()) {
					RequestParams params = new RequestParams();
					params.addBodyParameter("userId",
							preferences.getInt("userId", -1) + "");
					params.addBodyParameter("articleId", list.get(position - 1)
							.getArticleId() + "");
					params.addBodyParameter("uniqueKey",
							preferences.getString("uniqueKey", null));
					xUtilsUtil.httpPost(
							"mobile/articleMemo/unPraiseArticle.html", params,
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
										list.get(position - 1)
												.setPraised(false);
										list.get(position - 1).setPraiseNum(
												list.get(position - 1)
														.getPraiseNum() - 1);
										notifyDataSetChanged();
									}
								}

								@Override
								public void onMyFailure(HttpException error,
										String msg) {

								}
							});
				} else {
					RequestParams params = new RequestParams();
					params.addBodyParameter("userId",
							preferences.getInt("userId", -1) + "");
					params.addBodyParameter("articleId", list.get(position - 1)
							.getArticleId() + "");
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
											list.get(position - 1).setPraised(
													true);
											list.get(position - 1)
													.setPraiseNum(
															list.get(
																	position - 1)
																	.getPraiseNum() + 1);
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
		/*
		 * holder.likeImg.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View arg0, MotionEvent event) { if
		 * (event.getAction() == MotionEvent.ACTION_DOWN) {
		 * 
		 * // Create a system to run the physics loop for a set of springs.
		 * SpringSystem springSystem = SpringSystem.create(); // Add a spring to
		 * the system. Spring spring = springSystem.createSpring(); // Add a
		 * listener to observe the motion of the spring. spring.addListener(new
		 * SimpleSpringListener() {
		 * 
		 * @Override public void onSpringUpdate(Spring spring) { // You can
		 * observe the updates in the spring // state by asking its current
		 * value in // onSpringUpdate. float value = (float)
		 * spring.getCurrentValue(); float scale = 1f - (value * 0.85f);
		 * holder.likeImg.setScaleX(scale); holder.likeImg.setScaleY(scale); }
		 * });
		 * 
		 * // Set the spring in motion; moving from 0 to 1
		 * spring.setEndValue(1);
		 * 
		 * } else if (event.getAction() == MotionEvent.ACTION_UP) {
		 * 
		 * // Create a system to run the physics loop for a set of springs.
		 * SpringSystem springSystem = SpringSystem.create(); // Add a spring to
		 * the system. Spring spring = springSystem.createSpring(); // Add a
		 * listener to observe the motion of the spring. spring.addListener(new
		 * SimpleSpringListener() {
		 * 
		 * @Override public void onSpringUpdate(Spring spring) {
		 * System.out.println("----------------------------"); // You can
		 * observe the updates in the spring // state by asking its current
		 * value in // onSpringUpdate. float value = (float)
		 * spring.getCurrentValue(); float scale = (value * 1f);
		 * holder.likeImg.setScaleX(scale); holder.likeImg.setScaleY(scale); }
		 * }); // Set the spring in motion; moving from 0 to 1
		 * spring.setEndValue(1); }
		 * 
		 * return true; } });
		 */

		return view;
	}

	public class ViewHolder {

		private TextView titleTxt;
		private TextView timeTxt;
		private TextView byBrowseTxt;
		private ImageView likeImg;
		private ImageView photoImg;
		private ImageView backgroundImg;
		private TextView likeTxt;

	}

	/**
	 * 异步任务,获取数据
	 * 
	 */
	class GetListTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try {

				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
		}
	}

}
