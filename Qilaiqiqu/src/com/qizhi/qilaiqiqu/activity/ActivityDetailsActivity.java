package com.qizhi.qilaiqiqu.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.ActivityModel;
import com.qizhi.qilaiqiqu.model.ActivityModel.Activitys;
import com.qizhi.qilaiqiqu.utils.CircleImageViewUtil;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil.ImageInfo;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.squareup.picasso.Picasso;

public class ActivityDetailsActivity extends Activity implements CallBackPost,
		OnClickListener {

	private CircleImageViewUtil userImageImg;

	// private CircleImageViewUtil participant1;

	private int isMe = 2;// 1为自己，2为未报名的人，3为已报名

	private int articleMemoId1;
	private int articleMemoId2;

	private TextView likeTxt;
	private TextView mileageTxt;
	private TextView scanNumTxt;
	private TextView userNameTxt;
	private TextView durationTxt;
	private TextView startDateTxt;
	private TextView activityMemoTxt;
	private TextView articleMemoTxt1;
	private TextView articleMemoTxt2;
	private TextView activityTitleTxt;
	private TextView participantCountTxt;

	private TextView isSignTxt1;
	private TextView isSignTxt2;
	private TextView isSignTxt3;
	private LinearLayout isMelayout1;
	private LinearLayout isMelayout2;

	private LinearLayout participantLayout;

	private String imageUrl = "http://weride.oss-cn-hangzhou.aliyuncs.com/";

	private ImageCycleViewUtil mImageCycleView;

	List<ImageCycleViewUtil.ImageInfo> IClist = new ArrayList<ImageCycleViewUtil.ImageInfo>();

	private Activitys activity;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			setImageCycleViewUtil();
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_activity_details);
		initView();
		getActivityData();
		initEvent();
	}

	private void initView() {
		userImageImg = (CircleImageViewUtil) findViewById(R.id.img_activityDetails_photo);

		participantLayout = (LinearLayout) findViewById(R.id.layout_activityDetails_participant);

		likeTxt = (TextView) findViewById(R.id.txt_activityDetails_like);
		scanNumTxt = (TextView) findViewById(R.id.txt_activityDetails_scanNum);
		mileageTxt = (TextView) findViewById(R.id.txt_activityDetails_mileage);
		userNameTxt = (TextView) findViewById(R.id.txt_activityDetails_userName);
		durationTxt = (TextView) findViewById(R.id.txt_activityDetails_duration);
		startDateTxt = (TextView) findViewById(R.id.txt_activityDetails_startDate);
		articleMemoTxt1 = (TextView) findViewById(R.id.txt_activityDetails_articleMemo1);
		articleMemoTxt2 = (TextView) findViewById(R.id.txt_activityDetails_articleMemo2);
		activityMemoTxt = (TextView) findViewById(R.id.txt_activityDetails_activityMemo);
		activityTitleTxt = (TextView) findViewById(R.id.txt_activityDetails_activityTitle);
		participantCountTxt = (TextView) findViewById(R.id.txt_activityDetails_participantCount);

		mImageCycleView = (ImageCycleViewUtil) findViewById(R.id.icc_activityDetails);

		isSignTxt1 = (TextView) findViewById(R.id.txt_activityDetails_txt1);
		isSignTxt2 = (TextView) findViewById(R.id.txt_activityDetails_txt2);
		isSignTxt3 = (TextView) findViewById(R.id.txt_activityDetails_txt3);
		isMelayout1 = (LinearLayout) findViewById(R.id.layout_activityDetails_button1);
		isMelayout2 = (LinearLayout) findViewById(R.id.layout_activityDetails_button2);
	}

	private void initEvent() {
		isSignTxt1.setOnClickListener(this);
		isSignTxt2.setOnClickListener(this);
		isSignTxt3.setOnClickListener(this);
		articleMemoTxt1.setOnClickListener(this);
		articleMemoTxt2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_activityDetails_txt1:
			if (isMe == 1) {
				Toast.makeText(ActivityDetailsActivity.this, "我发起的", 0).show();
			} else if (isMe == 2) {
				Toast.makeText(ActivityDetailsActivity.this, "已报名", 0).show();
			}
			break;

		case R.id.txt_activityDetails_txt2:
			if (isMe == 3) {
				Toast.makeText(ActivityDetailsActivity.this, "去聊天isMe == 3", 0).show();
			} else if (isMe == 1) {
				Toast.makeText(ActivityDetailsActivity.this, "去聊天isMe == 2", 0).show();
			}
			break;

		case R.id.txt_activityDetails_txt3:
			if (isMe == 2) {
				Toast.makeText(ActivityDetailsActivity.this, "去报名", 0).show();
				isMelayout2.setVisibility(View.GONE);
				isSignTxt1.setText("已报名");
				isSignTxt2.setText("去聊天");
			}
			break;

		case R.id.layout_activityDetails_button1:
			startActivity(new Intent(ActivityDetailsActivity.this,
					RidingDetailsActivity.class).putExtra("", articleMemoId1));
			break;

		case R.id.layout_activityDetails_button2:

			break;

		default:
			break;
		}
	}

	private void getActivityData() {
		RequestParams params = new RequestParams("UTF-8");
		params.addQueryStringParameter("activityId", 2 + "");
		params.addQueryStringParameter("location", "");
		params.addQueryStringParameter("uniqueKey", "");

		new XUtilsUtil().httpPost("common/queryActivityByActivityId.html",
				params, this);
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		String result = responseInfo.result.toString();
		System.out.println(result);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getBoolean("result")) {
				Gson gson = new Gson();
				Type type = new TypeToken<ActivityModel>() {
				}.getType();

				final ActivityModel model = gson.fromJson(jsonObject
						.getJSONObject("data").toString(), type);

				activity = model.getActivitys();

				if (activity != null) {

					setView(model);
					// handler = new Handler();

					Message m = handler.obtainMessage();
					handler.sendMessage(m);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void setView(final ActivityModel model) {
		Picasso.with(ActivityDetailsActivity.this)
				.load(imageUrl + activity.getUserImage()).into(userImageImg);

		String[] split = activity.getActivityImage().split(",");

		for (int i = 0; i < split.length; i++) {
			IClist.add(new ImageCycleViewUtil.ImageInfo(
					"http://weride.oss-cn-hangzhou.aliyuncs.com/" + split[i],
					null, null, null));
		}

		likeTxt.setText(activity.getPraiseNum() + "");
		scanNumTxt.setText(activity.getScanNum() + "次浏览");
		userNameTxt.setText(activity.getUserName());
		activityTitleTxt.setText(activity.getActivityTitle());
		mileageTxt.setText(activity.getMileage());
		durationTxt.setText(activity.getDuration() + "小时");
		startDateTxt.setText(activity.getStartDate());
		activityMemoTxt.setText(activity.getActivityMemo());
		participantCountTxt.setText(model.getParticipantCount() + "人");
		for (int i = 0; i < model.getArticleMemoList().size() - 1; i++) {
			articleMemoTxt1.setText(model.getArticleMemoList().get(i)
					.getTitle());
			articleMemoId1 = model.getArticleMemoList().get(i).getArticleId();
			articleMemoTxt2.setText(model.getArticleMemoList().get(i + 1)
					.getTitle());
			articleMemoId2 = model.getArticleMemoList().get(i + 1)
					.getArticleId();
		}

		LinearLayout linearLayout = participantLayout;
		int size;
		if (model.getParticipantList().size() < 7) {
			size = model.getParticipantList().size();
		} else {
			size = 6;
		}

		for (int i = 0; i < size; i++) {
			CircleImageViewUtil imageView = new CircleImageViewUtil(this);

			linearLayout.addView(imageView);

			Picasso.with(ActivityDetailsActivity.this)
					.load(imageUrl
							+ model.getParticipantList().get(i).getUserImage())
					.resize(dp2px(ActivityDetailsActivity.this, 45f),
							dp2px(ActivityDetailsActivity.this, 45f))
					.into(imageView);
		}

		if (isMe == 1) {
			isSignTxt1.setText("我发起的");
			isSignTxt2.setText("去聊天");
			isMelayout2.setVisibility(View.GONE);
		} else if (isMe == 2) {
			isMelayout2.setVisibility(View.VISIBLE);
		} else if (isMe == 3) {
			isSignTxt1.setText("已报名");
			isSignTxt2.setText("去聊天");
			isMelayout2.setVisibility(View.GONE);
		}
	}

	/**
	 * dp转px
	 * 
	 * @param context
	 * @param val
	 * @return
	 */
	public static int dp2px(Context context, float dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, context.getResources().getDisplayMetrics());
	}

	private void setImageCycleViewUtil() {

		mImageCycleView.setCycleDelayed(3000);

		mImageCycleView
				.setOnPageClickListener(new ImageCycleViewUtil.OnPageClickListener() {

					@Override
					public void onClick(View imageView, ImageInfo imageInfo) {
						// if (imageInfo.type.toString().equals("URL")) {
						// Uri uri = Uri.parse(imageInfo.value.toString());
						// Intent intent = new Intent(Intent.ACTION_VIEW,
						// uri);
						// context.startActivity(intent);
						// } else if (imageInfo.type.toString().equals("APP")) {
						// if (imageInfo.bannerType.toString().equals(
						// "QYJ")) {
						//
						// context.startActivity(new Intent(context,
						// RidingDetailsActivity.class)
						// .putExtra("articleId", Integer
						// .parseInt(imageInfo.value
						// .toString())));
						// } else if (imageInfo.bannerType.toString()
						// .equals("PQS")) {
						//
						// } else if (imageInfo.bannerType.toString()
						// .equals("HD")) {
						//
						// }
						// }
					}
				});

		mImageCycleView.loadData(IClist,
				new ImageCycleViewUtil.LoadImageCallBack() {
					@Override
					public ImageView loadAndDisplay(
							ImageCycleViewUtil.ImageInfo imageInfo) {

						ImageView imageView = new ImageView(
								ActivityDetailsActivity.this);

						Picasso.with(ActivityDetailsActivity.this)
								.load(imageInfo.image.toString())
								.into(imageView);
						// imageView.setImageResource(R.drawable.demo);

						return imageView;

					}
				});
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {

	}

}
