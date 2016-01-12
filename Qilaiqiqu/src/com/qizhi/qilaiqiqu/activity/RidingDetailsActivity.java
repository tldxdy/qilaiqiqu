package com.qizhi.qilaiqiqu.activity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.RidingDetailsListAdapter;
import com.qizhi.qilaiqiqu.model.ArticleMemoDetailModel;
import com.qizhi.qilaiqiqu.model.ArticleModel;
import com.qizhi.qilaiqiqu.model.TravelsinformationModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */

public class RidingDetailsActivity extends Activity implements OnClickListener,
		CallBackPost {

	private LinearLayout backLayout;

	private ImageView likeImg;
	private ImageView rewardImg;
	private ImageView commentImg;

	private ImageView shareImg;
	private ImageView cllectionImg;

	private TextView likeTxt;
	private TextView revamTxt;

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

	private ArticleMemoDetailModel aDetailModel;
	private ArticleModel articleModel;

	private TextView numTxt;
	private Animation animation;

	private boolean isMe = false;

	private String jpushFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_riding_details);
		jpushFlag = getIntent().getStringExtra("jpushFlag");
		initView();
		initEvent();
		
	}
	
	public void onWindowFocusChanged(boolean hasFocus) { 
        super.onWindowFocusChanged(hasFocus); 
        if(hasFocus){ 
        	if ("JPushDZ".equals(jpushFlag) || "JPushDS".equals(jpushFlag)) {
				showJPush(jpushFlag);
			}
        }
	}
	

	private void initView() {
		xUtilsUtil = new XUtilsUtil();
		aDetailModel = new ArticleMemoDetailModel();
		list = new ArrayList<ArticleModel>();
		articleModel = new ArticleModel();
		listTravels = new ArrayList<TravelsinformationModel>();
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

		animation = AnimationUtils
				.loadAnimation(this, R.anim.applaud_animation);

		articleId = getIntent().getIntExtra("articleId", -1);
		isMe = getIntent().getBooleanExtra("isMe", false);
		if (isMe) {
			cllectionImg.setVisibility(View.GONE);
			layout_isShow.setVisibility(View.GONE);
			revamTxt.setVisibility(View.VISIBLE);
			shareImg.setVisibility(View.GONE);
		}

	}

	private void initEvent() {
		backLayout.setOnClickListener(this);
		likeImg.setOnClickListener(this);
		rewardImg.setOnClickListener(this);
		commentImg.setOnClickListener(this);
		shareImg.setOnClickListener(this);
		cllectionImg.setOnClickListener(this);
		revamTxt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_ridingDetailsActivity_back:
			finish();
			break;

		case R.id.img_ridingDetailsActivity_share:

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
				startActivityForResult(intent, 1);
			}
			break;
		case R.id.img_ridingDetailsActivity_revamp:

			for (int i = 0; i < list.size(); i++) {
				TravelsinformationModel tm = new TravelsinformationModel();
				tm.setTitle(list.get(i).getTitle());
				String[] addresss = list.get(i).getAddress().split("\\|");
				String[] imageMemos = list.get(i).getImageMemo().split("\\|");
				String[] memos = list.get(i).getMemo().split("\\|");
				if (i < addresss.length) {
					tm.setAddress(addresss[i]);
				}
				if (i < imageMemos.length) {
					tm.setImageMemo(imageMemos[i]);
				}
				if (i < memos.length) {
					tm.setMemo(memos[i]);
				}
				tm.setArticleImage(list.get(i).getArticleImage().split("\\|")[i]);
				listTravels.add(tm);
			}
			Intent intent = new Intent(this, ReleaseActivity.class);
			intent.putExtra("falg", true);
			intent.putExtra("list", (Serializable) listTravels);
			intent.putExtra("articleId", articleId);
			startActivity(intent);
		default:
			break;
		}
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
							articleModel.setPraiseNum(articleModel
									.getPraiseNum() + 1);
							likeTxt.setText(articleModel.getPraiseNum()
									+ articleModel.getVirtualPraise() + "");
						} else {
							if (preferences.getInt("userId", -1) == -1) {
								new SystemUtil().makeToast(
										RidingDetailsActivity.this, "请登录");
								startActivity(new Intent(
										RidingDetailsActivity.this,
										LoginActivity.class));
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
							articleModel.setPraiseNum(articleModel
									.getPraiseNum() - 1);
							likeTxt.setText(articleModel.getPraiseNum()
									+ articleModel.getVirtualPraise() + "");

						} else {
							if (preferences.getInt("userId", -1) == -1) {
								new SystemUtil().makeToast(
										RidingDetailsActivity.this, "请登录");
								startActivity(new Intent(
										RidingDetailsActivity.this,
										LoginActivity.class));
								finish();
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

	@Override
	protected void onStart() {
		uoloadData();
		super.onStart();
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

							String[] articleImagess = articleModel
									.getArticleImage().split("\\|");
							list.removeAll(list);
							for (int i = 0; i < articleImagess.length; i++) {
								list.add(articleModel);
							}
							System.out.println(aDetailModel.isUserCollected()
									+ ":" + aDetailModel.isUserPraised());
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
									RidingDetailsActivity.this, list);
							ridingList.setAdapter(adapter);
							ridingList.setDividerHeight(0);

						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {

					}
				});
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
	private void showJPush(String jpushFlag) {
		String userName = null;
		String title = null;
		String praiseNum = null;

		

		// 一个自定义的布局，作为显示的内容
		View v = LayoutInflater.from(this).inflate(R.layout.item_popup_jpush,
				null);
		popup_ok = (TextView) v.findViewById(R.id.txt_JpushPopup_ok);
		markPointTxt = (TextView) v.findViewById(R.id.txt_JpushPopup_message);
		popup_cancel = (TextView) v.findViewById(R.id.txt_JpushPopup_cancel);
		final PopupWindow popupWindow = new PopupWindow(v,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

		popupWindow.setTouchable(true);
		
		if (jpushFlag.equals("JPushDZ")) {
			praiseNum = getIntent().getStringExtra("praiseNum");
			title = getIntent().getStringExtra("title");
			userName = getIntent().getStringExtra("userName");
			markPointTxt.setText(Html.fromHtml("骑友 " + "<font color='#6dbfed'>"
					+ userName + "</font>" + " 给您的游记《" + "<font color='#6dbfed'>"
					+ title + "</font>" + "》点了赞哟!当前被点赞量为"
					+ "<font color='#ff0000'>" + praiseNum + "</font>"));
		} else if (jpushFlag.equals("JPushDS")) {
			
		}
		
		

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(RidingDetailsActivity.this.findViewById(R.id.layout_ridingDetailsActivity), Gravity.CENTER, 0, 50);
	}

}
