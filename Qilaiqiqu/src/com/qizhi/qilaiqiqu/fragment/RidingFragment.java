package com.qizhi.qilaiqiqu.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.RidingDetailsActivity;
import com.qizhi.qilaiqiqu.activity.MainActivity;
import com.qizhi.qilaiqiqu.adapter.SlideShowListAdapter;
import com.qizhi.qilaiqiqu.model.ArticleModel;
import com.qizhi.qilaiqiqu.model.CarouselModel;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil;
import com.qizhi.qilaiqiqu.utils.RefreshLayout;
import com.qizhi.qilaiqiqu.utils.RefreshLayout.OnLoadListener;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil.ImageInfo;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.squareup.picasso.Picasso;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RidingFragment extends Fragment implements OnItemClickListener,CallBackPost,OnRefreshListener,OnLoadListener{
	
	private ListView manageList;
	private View view;
	private List<ArticleModel> Articlelist;
	private SlideShowListAdapter adapter;
	private Context context;
	private SharedPreferences preferences;
	private XUtilsUtil xUtilsUtil;
	private int pageIndex = 1;
	private List<CarouselModel> cmList;
	List<ImageCycleViewUtil.ImageInfo> IClist;
	private RefreshLayout swipeLayout;
	private View header;
	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_riding,null);
		manageList = (ListView) view.findViewById(R.id.list_mainActivity_slideShow);
		context = getActivity();
		preferences = context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		xUtilsUtil = new XUtilsUtil();
		Articlelist = new ArrayList<ArticleModel>();
		IClist = new ArrayList<ImageCycleViewUtil.ImageInfo>();
		swipeLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeLayout.setOnRefreshListener(this);
		
		imageUrl();
		return view;
		
	}
	private void initViewHeader() {
		header = View.inflate(getActivity(),R.layout.item_list_mainactivity_header, null);
		manageList.addHeaderView(header);
		ImageCycleViewUtil mImageCycleView = (ImageCycleViewUtil) view
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
								.resize(800, 400)
								.placeholder(R.drawable.bitmap_homepage)
								.error(R.drawable.bitmap_homepage)
								.into(imageView);

						return imageView;

					}
				});
		
	}
	@Override
	public void onResume() {
		super.onResume();
	}

	public void imageUrl() {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET, XUtilsUtil.URL
				+ "common/querySysImageByIsOrder.html",
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							String data = responseInfo.result;
							JSONObject jo = new JSONObject(data);
							JSONArray dataList = jo.getJSONArray("dataList");
							Gson gson = new Gson();
							Type type = new TypeToken<List<CarouselModel>>() {
							}.getType();
							cmList = new ArrayList<CarouselModel>();
							cmList = gson.fromJson(dataList.toString(), type);

							List<CarouselModel> c = new ArrayList<CarouselModel>();
							for (int i = 0; i < cmList.size(); i++) {
								if (!cmList.get(i).getOpenType().equals("APP")) {
									c.add(cmList.get(i));
								} else {
									if (cmList.get(i).getBannerType()
											.equals("QYJ")) {
										c.add(cmList.get(i));
									}
								}
							}
							IClist = new ArrayList<ImageCycleViewUtil.ImageInfo>();
							for (int i = 0; i < c.size(); i++) {
								IClist.add(new ImageCycleViewUtil.ImageInfo(
										SystemUtil.IMGPHTH
												+ c.get(i).getImageAdd(), c
												.get(i).getValue(), c.get(i)
												.getOpenType(), c.get(i)
												.getBannerType()));
							}
							if(IClist.size() == 0){
								IClist.add(new ImageCycleViewUtil.ImageInfo(SystemUtil.IMGPHTH + "", "https://www.baidu.com", "URL", 0));
							}
							initViewHeader();
							data();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						if (MainActivity.loginFlag == 1) {
							MainActivity.splashView.splashAndDisappear();
							MainActivity.loginFlag = 0;
						}
					}
				});
	}
	
	
	
	private void data() {
		RequestParams params = new RequestParams("UTF-8");
		pageIndex = 1;
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize", "10");
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("common/articleMemoList.html", params,this);
	}
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
			pageIndex = jsonObject.optInt("pageIndex");

			JSONArray jsonArray = jsonObject
					.optJSONArray("dataList");
			// 数据获取
			Gson gson = new Gson();
			Type type = new TypeToken<List<ArticleModel>>() {
			}.getType();
			Articlelist = gson.fromJson(jsonArray.toString(),
					type);

			adapter = new SlideShowListAdapter(
					context, Articlelist);
			manageList.setAdapter(adapter);
			manageList
					.setOnItemClickListener(RidingFragment.this);
			swipeLayout.setOnRefreshListener(this);
			swipeLayout.setOnLoadListener(this);
			/*manageList
					.setOnRefreshListener(RidingFragment.this);*/
			if (MainActivity.loginFlag == 1) {
				MainActivity.splashView.splashAndDisappear();
				MainActivity.loginFlag = 0;
			}
		}
	}
	@Override
	public void onMyFailure(HttpException error, String msg) {
		if (MainActivity.loginFlag == 1) {
			MainActivity.splashView.splashAndDisappear();
			MainActivity.loginFlag = 0;
		}
	}
/*	@Override
	public void onRefresh() {
		pageIndex = 1;
		dataJ();
	}
	@Override
	public void onLoadingMore() {
		pageIndex = pageIndex + 1;
		dataJ();
	}*/
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		//Toasts.show(context, "点击" + position, 0);
		if (position != 0 && pageIndex < Articlelist.size() + 1) {
			Intent intent = new Intent(context, RidingDetailsActivity.class);
			intent.putExtra("isMe", false);
			intent.putExtra("articleId", Articlelist.get(position - 1)
					.getArticleId());
			startActivity(intent);
		}
	}
	
	
	private void dataJ() {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize", "10");
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("common/articleMemoList.html", params,new CallBackPost() {
			
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
					pageIndex = jsonObject.optInt("pageIndex");
					int pageCount = jsonObject.optInt("pageCount");

					JSONArray jsonArray = jsonObject
							.optJSONArray("dataList");
					// 数据获取
					Gson gson = new Gson();
					Type type = new TypeToken<List<ArticleModel>>() {
					}.getType();
					List<ArticleModel> lists = gson.fromJson(
							jsonArray.toString(), type);
					pageIndex = jsonObject.optInt("pageIndex");
					if(pageIndex == 1){
						Articlelist.clear();
						Articlelist.addAll(lists);
						Toasts.show(context, "刷新成功", 0);
					}else if(1 < pageIndex && pageIndex <= pageCount){
						Articlelist.addAll(lists);
						Toasts.show(context, "加载成功", 0);
					}else{
						pageIndex = jsonObject.optInt("pageIndex");
						Toasts.show(context, "已显示全部内容", 0);
					}
				}
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
					
			}
		});
	}
	@Override
	public void onRefresh() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				swipeLayout.setRefreshing(false);
				pageIndex = 1;
				if(IClist == null){
					imageUrl();
				}else{
					dataJ();
				}
				// 更新数据
				// 更新完后调用该方法结束刷新
				
			}
		}, 1500);

	}

	@Override
	public void onLoad() {
		swipeLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				swipeLayout.setLoading(false);
				pageIndex = pageIndex + 1;
				dataJ();
			}
		}, 1500);
	}
}
