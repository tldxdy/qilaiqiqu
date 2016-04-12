package com.qizhi.qilaiqiqu.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.Previewadapter;
import com.qizhi.qilaiqiqu.adapter.RidingDetailsListAdapter;
import com.qizhi.qilaiqiqu.model.ActivityListRecommendModel;
import com.qizhi.qilaiqiqu.model.ArticleMemoDetailModel;
import com.qizhi.qilaiqiqu.model.ArticleModel;
import com.qizhi.qilaiqiqu.model.TravelsinformationModel;
import com.qizhi.qilaiqiqu.service.PhotoUploadingService;
import com.qizhi.qilaiqiqu.utils.ConstantsUtil;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */

public class RidingDetailsActivity extends HuanxinLogOutActivity implements
		OnClickListener, CallBackPost, OnItemClickListener,OnScrollListener {

	private LinearLayout backLayout;
	private RelativeLayout titleLayout;

	private ImageView likeImg;
	private ImageView rewardImg;
	private ImageView commentImg;

	private ImageView shareImg;
	private ImageView cllectionImg;

	private TextView likeTxt;
	private TextView revamTxt;
	private TextView deleteTxt;

	private int cllectionFlag = 1;// 是否收藏标识:1为未点击,2为已点击
	private int cllectionLike = 1;// 是否点赞标识:1为未点击,2为已点击
	private int articleId;

	private ListView ridingList;
	private RidingDetailsListAdapter adapter;

	private int markPointInt;

	private ImageView popup_mark0;
	private ImageView popup_mark1;
	private ImageView popup_mark2;
	private ImageView popup_mark3;
	private ImageView popup_mark4;
	private ImageView popup_mark5;
	private ImageView popup_mark6;
	private ImageView popup_mark7;
	private ImageView popup_mark8;
	private ImageView popup_mark9;
	private TextView popup_ok;
	private TextView markPointTxt;
	private TextView popup_cancel;

	// private LinearLayout layout_gradePopup_bg;
	private LinearLayout layout_isShow;

	private XUtilsUtil xUtilsUtil;
	private SharedPreferences preferences;
	private List<ArticleModel> list;

	private List<TravelsinformationModel> listTravels;

	private List<TravelsinformationModel> previewList;

	private Previewadapter previewadapter;

	private ArticleMemoDetailModel aDetailModel;
	private ArticleModel articleModel;

	private TextView numTxt;
	private Animation animation;

	private boolean isMe = false;

	private boolean ReleaseActivityfalg = false;

	private String jpushFlag;

	private int num = 0;

	private int _id = 0;

	private List<String> imgListUrl;
	private int height;

	private List<ActivityListRecommendModel> recommendList;

	// private PopupWindowUploading pUploading;

	private Tencent mTencent;

	private IUiListener baseUiListener; // 监听器

	private IWXAPI api;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String s = (String) msg.obj;
				imgListUrl.add(s);
				if (previewList.size() - 1 != num) {
					num = num + 1;
					/*
					 * File file = new
					 * File(previewList.get(num).getArticleImage()); new
					 * FileUploadAsyncTask(RidingDetailsActivity.this, (num +
					 * 1), previewList.size(), preferences, "QYJ",
					 * handler).execute(file);
					 */
					// new
					// SystemUtil().httpClient(list.get(num).getArticleImage(),
					// preferences, handler, "QYJ");
					// photoUploading();
				} else {
					publishTravels();
				}
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_riding_details);
		jpushFlag = getIntent().getStringExtra("jpushFlag");
		mTencent = Tencent.createInstance(ConstantsUtil.APP_ID_TX,
				this.getApplicationContext());
		initView();
		initEvent();
		if (!ReleaseActivityfalg) {
			uoloadData();
		}
	}

	// private boolean f = true;

	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			if ("JPushDZ".equals(jpushFlag) || "JPushDS".equals(jpushFlag)) {
				showJPush(jpushFlag);
			}
			/*
			 * if(f){ f = false;
			 * pUploading.show(this.findViewById(R.id.layout_riding_top)); }
			 */
		}
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		xUtilsUtil = new XUtilsUtil();
		aDetailModel = new ArticleMemoDetailModel();
		list = new ArrayList<ArticleModel>();
		articleModel = new ArticleModel();
		// pUploading = new PopupWindowUploading(this);

		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		backLayout = (LinearLayout) findViewById(R.id.layout_ridingDetailsActivity_back);

		ridingList = (ListView) findViewById(R.id.list_ridingDetailsActivity_riding);
		likeImg = (ImageView) findViewById(R.id.img_ridingDetailsActivity_like);
		rewardImg = (ImageView) findViewById(R.id.img_ridingDetailsActivity_reward);
		commentImg = (ImageView) findViewById(R.id.img_ridingDetailsActivity_comment);

		shareImg = (ImageView) findViewById(R.id.img_ridingDetailsActivity_share);
		cllectionImg = (ImageView) findViewById(R.id.img_ridingDetailsActivity_cllection);
		layout_isShow = (LinearLayout) findViewById(R.id.layout_ridingDetailsActivity_isShow);
		likeTxt = (TextView) findViewById(R.id.txt_ridingDetailsActivity_like);
		numTxt = (TextView) findViewById(R.id.animation);
		revamTxt = (TextView) findViewById(R.id.img_ridingDetailsActivity_revamp);
		deleteTxt = (TextView) findViewById(R.id.txt_ridingDetailsActivity_delete);
		titleLayout = (RelativeLayout) findViewById(R.id.layout_ridingDetailsActivity_title);
		titleLayout.measure(0, 0);
		height = titleLayout.getMeasuredHeight();
		animation = AnimationUtils
				.loadAnimation(this, R.anim.applaud_animation);

		articleId = getIntent().getIntExtra("articleId", -1);
		isMe = getIntent().getBooleanExtra("isMe", false);
		ReleaseActivityfalg = getIntent().getBooleanExtra(
				"ReleaseActivityfalg", false);

		_id = getIntent().getIntExtra("_id", 0);
		if (isMe) {
			cllectionImg.setVisibility(View.GONE);
			layout_isShow.setVisibility(View.GONE);
			revamTxt.setVisibility(View.VISIBLE);
			shareImg.setVisibility(View.GONE);
			deleteTxt.setVisibility(View.VISIBLE);
		} else if (ReleaseActivityfalg) {
			previewList = (List<TravelsinformationModel>) getIntent()
					.getSerializableExtra("previewList");

			cllectionImg.setVisibility(View.GONE);
			layout_isShow.setVisibility(View.GONE);
			revamTxt.setVisibility(View.VISIBLE);
			shareImg.setVisibility(View.GONE);
			deleteTxt.setVisibility(View.GONE);
			revamTxt.setText("发布");
			previewadapter = new Previewadapter(this, previewList, preferences);
			ridingList.setAdapter(previewadapter);
			ridingList.setDividerHeight(0);
		}
		baseUiListener = new IUiListener() {

			@Override
			public void onError(UiError arg0) {

			}

			@Override
			public void onComplete(Object arg0) {

			}

			@Override
			public void onCancel() {

			}
		};
		api = WXAPIFactory.createWXAPI(this, ConstantsUtil.APP_ID_WX);
	}

	private void initEvent() {
		backLayout.setOnClickListener(this);
		likeImg.setOnClickListener(this);
		rewardImg.setOnClickListener(this);
		commentImg.setOnClickListener(this);
		shareImg.setOnClickListener(this);
		cllectionImg.setOnClickListener(this);
		revamTxt.setOnClickListener(this);
		deleteTxt.setOnClickListener(this);
		ridingList.setOnScrollListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_ridingDetailsActivity_back:
			finish();
			break;

		case R.id.img_ridingDetailsActivity_share:

			showPopupWindow3(v);
			break;
		case R.id.img_ridingDetailsActivity_cllection:
			if (cllectionFlag == 1) {
				RequestParams params2 = new RequestParams();
				params2.addBodyParameter("userId",
						preferences.getInt("userId", -1) + "");
				params2.addBodyParameter("articleId", articleId + "");
				params2.addBodyParameter("uniqueKey",
						preferences.getString("uniqueKey", null));
				xUtilsUtil.httpPost(
						"mobile/articleMemo/appendArticleMemoCollect.html",
						params2, this);

			} else {
				RequestParams params3 = new RequestParams();
				params3.addBodyParameter("userId",
						preferences.getInt("userId", -1) + "");
				params3.addBodyParameter("articleId", articleId + "");
				params3.addBodyParameter("uniqueKey",
						preferences.getString("uniqueKey", null));
				xUtilsUtil.httpPost(
						"mobile/articleMemo/deleteArticleMemoCollect.html",
						params3, this);
			}
			break;

		case R.id.img_ridingDetailsActivity_like:
			if (cllectionLike == 1) {
				likeChosen();

			} else {
				likeUnChosen();
			}
			break;

		case R.id.img_ridingDetailsActivity_reward:
			showPopupWindow(v);
			break;

		case R.id.img_ridingDetailsActivity_comment:
			if (preferences.getInt("userId", -1) != -1) {
				Intent intent = new Intent(this, DiscussActivity.class);
				intent.putExtra("articleId", articleId);
				startActivity(intent);
			} else {
				new SystemUtil().makeToast(RidingDetailsActivity.this, "请登录");
				startActivity(new Intent(RidingDetailsActivity.this,
						LoginActivity.class));
				RidingDetailsActivity.this.finish();
			}
			break;
		case R.id.img_ridingDetailsActivity_revamp:
			if (!ReleaseActivityfalg) {
				listTravels = new ArrayList<TravelsinformationModel>();
				for (int i = 0; i < list.size(); i++) {
					TravelsinformationModel tm = new TravelsinformationModel();
					tm.setTitle(list.get(i).getTitle());
					tm.setAddress(list.get(i).getArticleDetailList().get(i)
							.getAddress());
					tm.setImageMemo(list.get(i).getArticleDetailList().get(i)
							.getImageMemo());
					tm.setMemo(list.get(i).getArticleDetailList().get(i)
							.getMemo());
					tm.setArticleImage(list.get(i).getArticleDetailList()
							.get(i).getArticleImage());
					listTravels.add(tm);
				}
				Intent intent = new Intent(this, ReleaseActivity.class);
				intent.putExtra("falg", true);
				intent.putExtra("list", (Serializable) listTravels);
				intent.putExtra("articleId", articleId);
				startActivity(intent);
			} else {
				if (PhotoUploadingService.isStart) {
					Toasts.show(this, "你有游记正在发布，请稍后再发布", 0);
					// new SystemUtil().makeToast(this, "你有游记正在发布，请稍后再发布");
					break;
				}

				imgListUrl = new ArrayList<String>();
				num = 0;
				// photoUploading();

				if (previewList.size() != 0) {
					Intent intent = new Intent();
					intent.putExtra("list", (Serializable) previewList);
					intent.putExtra("title", previewList.get(0).getTitle()
							.toString());
					intent.setAction("com.qizhi.qilaiqiqu.service.photoUploadingService");// 你定义的service的action
					intent.setPackage(getPackageName());// 这里你需要设置你应用的包名
					startService(intent);

					Intent intent2 = new Intent();
					intent.putExtra("_id", _id);
					setResult(100, intent2);
					finish();

					// loadingLayout.setVisibility(View.VISIBLE);
					/*
					 * if (previewList.size() != 0) { File file = new
					 * File(previewList.get(num).getArticleImage()); new
					 * FileUploadAsyncTask(this, num + 1, previewList.size(),
					 * preferences, "QYJ", handler).execute(file);
					 */
				}
			}
			break;
		case R.id.txt_ridingDetailsActivity_delete:
			showPopupWindow2(v);
			break;
		default:
			break;
		}
	}

	// 分享到QQ与QQ空间
	private void onClickQQShare() {
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, articleModel.getTitle());
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY,
				"http://www.weride.com.cn/page/articleDetail.html?articleId="
						+ articleModel.getArticleId());
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, SystemUtil.IMGPHTH
				+ articleModel.getDefaultShowImage());
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, SystemUtil.IMGPHTH
				+ articleModel.getDefaultShowImage());
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "骑来骑去");
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, articleModel.getTitle());// 必填
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
				"每一篇游记都是骑友分享的美好骑行时光，让幸福传递下去吧！");// 选填
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
				"http://www.weride.com.cn/page/articleDetail.html?articleId="
						+ articleModel.getArticleId());// 必填
		params.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, SystemUtil.IMGPHTH
				+ articleModel.getDefaultShowImage());
		mTencent.shareToQQ(RidingDetailsActivity.this, params, baseUiListener);
	}

	// 分享到微信
	private void onClickWXShare() {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = "http://www.weride.com.cn/page/articleDetail.html?articleId="
				+ articleModel.getArticleId();
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = articleModel.getTitle();
		msg.description = "每一篇游记都是骑友分享的美好骑行时光，让幸福传递下去吧！";

		try {
			Bitmap bmp = SystemUtil.compressImageFromFile(SystemUtil.IMGPHTH
					+ articleModel.getDefaultShowImage(), 300);
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
			bmp.recycle();
			msg.thumbData = Bitmap2Bytes(thumbBmp);
			// msg.setThumbImage(thumbBmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("图文链接");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	// 分享到微信
	private void onClickWXPYQShare() {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = "http://www.weride.com.cn/page/articleDetail.html?articleId="
				+ articleModel.getArticleId();
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = articleModel.getTitle();
		msg.description = "每一篇游记都是骑友分享的美好骑行时光，让幸福传递下去吧！";
		try {
			Bitmap bmp = SystemUtil.compressImageFromFile(SystemUtil.IMGPHTH
					+ articleModel.getDefaultShowImage(), 300);
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
			bmp.recycle();
			msg.thumbData = Bitmap2Bytes(thumbBmp);
			// msg.setThumbImage(thumbBmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("图文链接");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);
	}

	/**
	 * 构造一个用于请求的唯一标识
	 * 
	 * @param type
	 *            分享的内容类型
	 * @return
	 */
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	// sina微博
	  //private void onClickWBShare(){}
	 

	private void deleteRiding() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("articleId", articleId + "");
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/articleMemo/deleteArticle.html", params,
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
							new SystemUtil().makeToast(
									RidingDetailsActivity.this, "删除成功");
							RidingDetailsActivity.this.finish();
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {

					}
				});
	}

	private void likeChosen() {
		RequestParams params2 = new RequestParams();
		params2.addBodyParameter("userId", preferences.getInt("userId", -1)
				+ "");
		params2.addBodyParameter("articleId", articleId + "");
		params2.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/articleMemo/praiseArticle.html", params2,
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
							cllectionLike = 2;
							numTxt.setVisibility(View.VISIBLE);
							numTxt.startAnimation(animation);
							new Handler().postDelayed(new Runnable() {
								public void run() {
									numTxt.setVisibility(View.GONE);
								}
							}, 1000);
							likeImg.setImageResource(R.drawable.like_chosen);
							articleModel.setPraiseNum(jsonObject.optInt("data"));
							likeTxt.setText(articleModel.getPraiseNum()
									+ articleModel.getVirtualPraise() + "");
						} else {
							if (preferences.getInt("userId", -1) == -1) {
								new SystemUtil().makeToast(
										RidingDetailsActivity.this, "请登录");
								startActivity(new Intent(
										RidingDetailsActivity.this,
										LoginActivity.class));
								RidingDetailsActivity.this.finish();
							}
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
					}
				});
	}

	private void likeUnChosen() {
		RequestParams params3 = new RequestParams();
		params3.addBodyParameter("userId", preferences.getInt("userId", -1)
				+ "");
		params3.addBodyParameter("articleId", articleId + "");
		params3.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/articleMemo/unPraiseArticle.html", params3,
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
							cllectionLike = 1;
							likeImg.setImageResource(R.drawable.like_unchosen);
							articleModel.setPraiseNum(jsonObject.optInt("data"));
							likeTxt.setText(articleModel.getPraiseNum()
									+ articleModel.getVirtualPraise() + "");

						} else {
							if (preferences.getInt("userId", -1) == -1) {
								new SystemUtil().makeToast(
										RidingDetailsActivity.this, "请登录");
								startActivity(new Intent(
										RidingDetailsActivity.this,
										LoginActivity.class));
								RidingDetailsActivity.this.finish();
							}
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {

					}
				});
	}

	/**
	 * 弹窗
	 * 
	 * @param view
	 *            popup所依附的布局
	 */
	private void showPopupWindow(View view) {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.item_popup_grade, null);

		popup_mark0 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark0);
		popup_mark1 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark1);
		popup_mark2 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark2);
		popup_mark3 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark3);
		popup_mark4 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark4);
		popup_mark5 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark5);
		popup_mark6 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark6);
		popup_mark7 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark7);
		popup_mark8 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark8);
		popup_mark9 = (ImageView) contentView
				.findViewById(R.id.img_gradePopup_mark9);
		popup_ok = (TextView) contentView.findViewById(R.id.txt_gradePopup_ok);
		markPointTxt = (TextView) contentView
				.findViewById(R.id.txt_gradePopup_markPoint);
		popup_cancel = (TextView) contentView
				.findViewById(R.id.txt_gradePopup_cancel);
		selectMark();

		final PopupWindow popupWindow = new PopupWindow(contentView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

		popupWindow.setTouchable(true);

		popupWindow.setAnimationStyle(R.style.PopupAnimation);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		popup_mark0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				markPointInt = 1;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				markPointInt = 2;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				markPointInt = 3;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				markPointInt = 4;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				popup_mark4.setImageResource(R.drawable.award_yellow);
				markPointInt = 5;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				popup_mark4.setImageResource(R.drawable.award_yellow);
				popup_mark5.setImageResource(R.drawable.award_yellow);
				markPointInt = 6;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				popup_mark4.setImageResource(R.drawable.award_yellow);
				popup_mark5.setImageResource(R.drawable.award_yellow);
				popup_mark6.setImageResource(R.drawable.award_yellow);
				markPointInt = 7;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				popup_mark4.setImageResource(R.drawable.award_yellow);
				popup_mark5.setImageResource(R.drawable.award_yellow);
				popup_mark6.setImageResource(R.drawable.award_yellow);
				popup_mark7.setImageResource(R.drawable.award_yellow);
				markPointInt = 8;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				popup_mark4.setImageResource(R.drawable.award_yellow);
				popup_mark5.setImageResource(R.drawable.award_yellow);
				popup_mark6.setImageResource(R.drawable.award_yellow);
				popup_mark7.setImageResource(R.drawable.award_yellow);
				popup_mark8.setImageResource(R.drawable.award_yellow);
				markPointInt = 9;
				markPointTxt.setText(markPointInt + "分");
			}
		});
		popup_mark9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectMark();
				popup_mark0.setImageResource(R.drawable.award_yellow);
				popup_mark1.setImageResource(R.drawable.award_yellow);
				popup_mark2.setImageResource(R.drawable.award_yellow);
				popup_mark3.setImageResource(R.drawable.award_yellow);
				popup_mark4.setImageResource(R.drawable.award_yellow);
				popup_mark5.setImageResource(R.drawable.award_yellow);
				popup_mark6.setImageResource(R.drawable.award_yellow);
				popup_mark7.setImageResource(R.drawable.award_yellow);
				popup_mark8.setImageResource(R.drawable.award_yellow);
				popup_mark9.setImageResource(R.drawable.award_yellow);
				markPointInt = 10;
				markPointTxt.setText(markPointInt + "分");
			}
		});

		popup_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				rewardIntegral(markPointInt);
				popupWindow.dismiss();
			}
		});

		popup_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new SystemUtil()
						.makeToast(RidingDetailsActivity.this, "您未打赏积分");
				popupWindow.dismiss();
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

	}

	public void rewardIntegral(final int markPointInt) {
		if (preferences.getInt("userId", -1) != -1) {
			RequestParams params = new RequestParams("UTF-8");
			params.addBodyParameter("userId", preferences.getInt("userId", -1)
					+ "");
			params.addBodyParameter("articleId", articleId + "");
			params.addBodyParameter("integral", markPointInt + "");
			params.addBodyParameter("uniqueKey",
					preferences.getString("uniqueKey", null));
			xUtilsUtil.httpPost("mobile/articleMemo/articleMemoReward.html",
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
								new SystemUtil().makeToast(
										RidingDetailsActivity.this, "您打赏了"
												+ markPointInt + "分!");
							} else {
								new SystemUtil().makeToast(
										RidingDetailsActivity.this,
										jsonObject.optString("message"));
							}
						}

						@Override
						public void onMyFailure(HttpException error, String msg) {

						}
					});
		} else {
			new SystemUtil().makeToast(this, "请登录");
			startActivity(new Intent(this, LoginActivity.class));
			RidingDetailsActivity.this.finish();
		}

	}

	/**
	 * 设置所有评分图片暗色
	 */
	public void selectMark() {
		popup_mark0.setImageResource(R.drawable.award_gray);
		popup_mark1.setImageResource(R.drawable.award_gray);
		popup_mark2.setImageResource(R.drawable.award_gray);
		popup_mark3.setImageResource(R.drawable.award_gray);
		popup_mark4.setImageResource(R.drawable.award_gray);
		popup_mark5.setImageResource(R.drawable.award_gray);
		popup_mark6.setImageResource(R.drawable.award_gray);
		popup_mark7.setImageResource(R.drawable.award_gray);
		popup_mark8.setImageResource(R.drawable.award_gray);
		popup_mark9.setImageResource(R.drawable.award_gray);
		markPointInt = 0;
		markPointTxt.setText(markPointInt + "分");
	}

	private void uoloadData() {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("articleId", articleId + "");
		if (preferences.getString("uniqueKey", null) != null) {
			params.addBodyParameter("uniqueKey",
					preferences.getString("uniqueKey", null));
		}
		xUtilsUtil.httpPost("common/articleMemoDetail.html", params,
				new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						// pUploading.dismiss();
						String s = responseInfo.result;
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(s);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (jsonObject.optBoolean("result")) {
							Gson gson = new Gson();
							Type type = new TypeToken<ArticleMemoDetailModel>() {
							}.getType();
							aDetailModel = gson.fromJson(
									jsonObject.optString("data"), type);

							articleModel = aDetailModel.getArticleMemo();

							list.removeAll(list);
							for (int i = 0; i < articleModel
									.getArticleDetailList().size(); i++) {
								list.add(articleModel);
							}
							recommendList = aDetailModel.getActivityList();
							if (aDetailModel.isUserCollected()) {
								cllectionImg
										.setImageResource(R.drawable.clection_chosen);
								cllectionFlag = 2;
							}
							if (articleModel.isPraised()) {
								likeImg.setImageResource(R.drawable.like_chosen);
								cllectionLike = 2;
							}
							likeTxt.setText(articleModel.getPraiseNum()
									+ articleModel.getVirtualPraise() + "");

							adapter = new RidingDetailsListAdapter(
									RidingDetailsActivity.this, list,
									recommendList);
							ridingList.setAdapter(adapter);
							ridingList
									.setOnItemClickListener(RidingDetailsActivity.this);

						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						// pUploading.dismiss();
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if ((position - list.size() - 2) >= 0) {
			recommendList.get((position - list.size() - 2));
			Intent intent = new Intent(this, ActivityDetailsActivity.class);
			intent.putExtra("activityId",
					recommendList.get((position - list.size() - 2))
							.getActivityId());
			startActivity(intent);
			// new SystemUtil().makeToast(this, "" + (position - list.size() -
			// 2) );
		}
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
			if (cllectionFlag == 1) {
				cllectionImg.setImageResource(R.drawable.clection_chosen);
				cllectionFlag = 2;
			} else {
				cllectionFlag = 1;
				cllectionImg.setImageResource(R.drawable.cllection_unchosen);
			}
		} else {
			new SystemUtil().makeToast(this, "请登录");
			startActivity(new Intent(this, LoginActivity.class));
			RidingDetailsActivity.this.finish();
		}
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {

	}

	/**
	 * 弹窗
	 * 
	 * @param view
	 *            popup所依附的布局
	 */
	private void showJPush(String jf) {
		String userName = null;
		String title = null;
		String sumIntegral = null;
		String integral = null;

		String praiseNum = null;

		// 一个自定义的布局，作为显示的内容
		View v = LayoutInflater.from(this).inflate(R.layout.item_popup_jpush,
				null);
		markPointTxt = (TextView) v.findViewById(R.id.txt_JpushPopup_message);
		popup_cancel = (TextView) v.findViewById(R.id.txt_JpushPopup_cancel);
		LinearLayout quxiao = (LinearLayout) v.findViewById(R.id.quxiao);
		final PopupWindow popupWindow = new PopupWindow(v,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		popupWindow.setTouchable(true);

		popupWindow.setAnimationStyle(R.style.PopupAnimation);

		if (jf.equals("JPushDZ")) {
			praiseNum = getIntent().getStringExtra("praiseNum");
			title = getIntent().getStringExtra("title");
			userName = getIntent().getStringExtra("userName");
			markPointTxt.setText(Html.fromHtml("骑友 " + "<font color='#6dbfed'>"
					+ userName + "</font>" + " 给您的游记《"
					+ "<font color='#6dbfed'>" + title + "</font>"
					+ "》点了赞哟!当前被点赞量为" + "<font color='#ff0000'>" + praiseNum
					+ "</font>"));
		} else if (jf.equals("JPushDS")) {
			sumIntegral = getIntent().getStringExtra("sumIntegral");
			title = getIntent().getStringExtra("title");
			integral = getIntent().getStringExtra("integral");
			userName = getIntent().getStringExtra("userName");
			markPointTxt.setText(Html.fromHtml("骑友 " + "<font color='#6dbfed'>"
					+ userName + "</font>" + " 觉得您的游记《"
					+ "<font color='#6dbfed'>" + title + "</font>"
					+ "》写得不错哟!给您打赏了" + "<font color='#ff0000'>" + integral
					+ "</font>" + ",你现在的总积分是" + "<font color='#ff0000'>"
					+ sumIntegral + "</font>" + "分"));
		}

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				jpushFlag = "";
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		quxiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				jpushFlag = "";
			}
		});

		popup_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
				jpushFlag = "";
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(RidingDetailsActivity.this
				.findViewById(R.id.layout_ridingDetailsActivity),
				Gravity.CENTER, 0, 50);
	}

	private void showPopupWindow2(View view) {

		// 一个自定义的布局，作为显示的内容
		View mview = LayoutInflater.from(this).inflate(
				R.layout.popup_delete_releaseactivity, null);

		TextView textView = (TextView) mview
				.findViewById(R.id.txt_dialog_box_cancel);
		Button confirmBtn = (Button) mview
				.findViewById(R.id.btn_dialog_box_confirm);
		Button cancelBtn = (Button) mview
				.findViewById(R.id.btn_dialog_box_cancel);

		textView.setText("你确定要删除这篇游记吗？");
		confirmBtn.setText("确定");
		cancelBtn.setText("取消");
		final PopupWindow popupWindow = new PopupWindow(mview,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		popupWindow.setTouchable(true);
		popupWindow.setAnimationStyle(R.style.PopupAnimation);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				deleteRiding();
				popupWindow.dismiss();
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

	}

	private void showPopupWindow3(View view) {

		// 一个自定义的布局，作为显示的内容
		View mview = LayoutInflater.from(this).inflate(R.layout.share, null);

		LinearLayout qq = (LinearLayout) mview.findViewById(R.id.qq);
		LinearLayout wx = (LinearLayout) mview.findViewById(R.id.wx);
		LinearLayout pyq = (LinearLayout) mview.findViewById(R.id.pyq);
		LinearLayout wb = (LinearLayout) mview.findViewById(R.id.wb);
		LinearLayout qx = (LinearLayout) mview.findViewById(R.id.qx);

		final PopupWindow popupWindow = new PopupWindow(mview,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		popupWindow.setTouchable(true);
		popupWindow.setAnimationStyle(R.style.PopupAnimation);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onClickQQShare();
				popupWindow.dismiss();
			}
		});

		wx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onClickWXShare();
				popupWindow.dismiss();
			}
		});
		pyq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onClickWXPYQShare();
				popupWindow.dismiss();
			}
		});
		wb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});
		qx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 50);

	}

	/**
	 * 预览发布
	 */
	private void publishTravels() {

		// 使用NameValuePair来保存要传递的Post参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uniqueKey", preferences.getString(
				"uniqueKey", null)));
		params.add(new BasicNameValuePair("userId", preferences.getInt(
				"userId", -1) + ""));
		params.add(new BasicNameValuePair("title", previewList.get(0)
				.getTitle()));

		for (int i = 0; i < previewList.size(); i++) {

			// 需要注意的是 如果某个图片的说明 为空 时 请传递空 不能 少一个属性 每一个数组必须保证相同的长度
			params.add(new BasicNameValuePair("articleImage", imgListUrl.get(i)));

			if (previewList.get(i).getImageMemo() == null) {
				params.add(new BasicNameValuePair("imageMemo", ""));
			} else {
				params.add(new BasicNameValuePair("imageMemo", previewList.get(
						i).getImageMemo()));
			}

			if (previewList.get(i).getAddress() == null) {
				params.add(new BasicNameValuePair("address", ""));
			} else {
				params.add(new BasicNameValuePair("address", previewList.get(i)
						.getAddress()));
			}

			if (previewList.get(i).getMemo() == null) {
				params.add(new BasicNameValuePair("memo", ""));
			} else {
				params.add(new BasicNameValuePair("memo", previewList.get(i)
						.getMemo()));
			}
		}
		RequestParams params2 = new RequestParams();
		try {
			params2.setBodyEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			xUtilsUtil.httpPost("mobile/articleMemo/insertArticle.html",
					params2, new CallBackPost() {

						@Override
						public void onMySuccess(
								ResponseInfo<String> responseInfo) {
							JSONObject jsonObject = null;
							try {
								jsonObject = new JSONObject(responseInfo.result);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							if (jsonObject.optBoolean("result")) {
								new SystemUtil().makeToast(
										RidingDetailsActivity.this, "发表成功");
								Intent intentConfirm = new Intent();
								setResult(3, intentConfirm);
								RidingDetailsActivity.this.finish();
							} else {
								new SystemUtil().makeToast(
										RidingDetailsActivity.this,
										jsonObject.optString("message"));
								RidingDetailsActivity.this.finish();
							}
						}

						@Override
						public void onMyFailure(HttpException error, String msg) {

						}
					});

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	private float moveY;
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		moveY = getScrollY();
			if(moveY == 0){
				titleLayout.setBackgroundResource(R.drawable.background_shade);
			}else if(moveY/(RidingDetailsListAdapter.height - height) < 1){
				titleLayout.setBackgroundColor(0xff6dbfed);
				titleLayout.getBackground().setAlpha((int) (moveY/(RidingDetailsListAdapter.height - height) * 255));
			}else{
				titleLayout.setBackgroundColor(0xff6dbfed);
			}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	                
	                
		
	}
	public int getScrollY() {
	    View c = ridingList.getChildAt(0);
	    if (c == null) {
	        return 0;
	    }
	    int firstVisiblePosition = ridingList.getFirstVisiblePosition();
	    int top = c.getTop();
	    return -top + firstVisiblePosition * c.getHeight() ;
	}

}
