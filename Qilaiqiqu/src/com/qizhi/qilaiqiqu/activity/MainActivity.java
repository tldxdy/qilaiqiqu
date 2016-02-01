package com.qizhi.qilaiqiqu.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.NetUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.SearchResultAdapter;
import com.qizhi.qilaiqiqu.adapter.SlideShowListAdapter;
import com.qizhi.qilaiqiqu.fragment.MenuLeftFragment;
import com.qizhi.qilaiqiqu.model.ArticleModel;
import com.qizhi.qilaiqiqu.model.CarouselModel;
import com.qizhi.qilaiqiqu.model.SearchResultModel;
import com.qizhi.qilaiqiqu.model.SearchResultModel.SearchDataList;
import com.qizhi.qilaiqiqu.ui.PullFreshListView;
import com.qizhi.qilaiqiqu.ui.PullFreshListView.OnRefreshListener;
import com.qizhi.qilaiqiqu.utils.ActivityCollectorUtil;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil;
import com.qizhi.qilaiqiqu.utils.SplashView;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */
public class MainActivity extends FragmentActivity implements OnClickListener,
		OnOpenListener, OnCloseListener, OnItemClickListener, CallBackPost,
		OnRefreshListener, TextWatcher {

	// 定义action常量
	protected static final String ACTION = "com.qizhi.qilaiqiqu.receiver.LogoutReceiver";

	private SplashView splashView;
	private FrameLayout frameLayout;

	private RelativeLayout titleLayout;
	private LinearLayout searchLayout;
	private ListView searchList;

	private EditText inputEdt;

	private float titleY;
	private float searchY;

	private TextView searchCancel;

	private ImageView photoImg;
	private RelativeLayout searchImg;
	private RelativeLayout addImg;

	private PullFreshListView slideShowList;

	private SlideShowListAdapter adapter;
	private SearchResultAdapter adapterSearch;

	private SlidingMenu menu;

	private List<ArticleModel> Articlelist;

	private XUtilsUtil xUtilsUtil;

	private Integer pageIndex = 1;

	private List<CarouselModel> cmList;

	public static boolean isForeground = false;

	private SharedPreferences preferences;

	List<ImageCycleViewUtil.ImageInfo> IClist = new ArrayList<ImageCycleViewUtil.ImageInfo>();

	private View dotView;

	private boolean isDot = false;

	boolean isdialog = true;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				logoutService();
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

		ActivityCollectorUtil.addActivity(this);

		View view = LayoutInflater.from(this).inflate(R.layout.activity_main,
				null);
		frameLayout = new FrameLayout(this);
		splashView = new SplashView(this);
		loginFlag = getIntent().getIntExtra("loginFlag", -1);
		if (loginFlag == 1) {
			frameLayout.addView(view);
			frameLayout.addView(splashView);
			setContentView(frameLayout);
		} else {
			frameLayout.addView(view);
			setContentView(frameLayout);
		}

		initView();
		initLeft();
		initEvent();
		imageUrl();
		data();

	}

	private void initView() {
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		searchCancel = (TextView) findViewById(R.id.txt_mainActivity_cancel);
		inputEdt = (EditText) findViewById(R.id.edt_mainActivity_searchInput);
		titleLayout = (RelativeLayout) findViewById(R.id.layout_mainActivity_title);
		searchLayout = (LinearLayout) findViewById(R.id.layout_mainActivity_searchLayout);
		searchList = (ListView) findViewById(R.id.list_mainActivity_searchResult);

		photoImg = (ImageView) findViewById(R.id.img_mainActivity_photo);
		searchImg = (RelativeLayout) findViewById(R.id.img_mainActivity_search_photo);
		addImg = (RelativeLayout) findViewById(R.id.img_mainActivity_add_photo);

		slideShowList = (PullFreshListView) findViewById(R.id.list_mainActivity_slideShow);
		dotView = findViewById(R.id.view_dot);

		xUtilsUtil = new XUtilsUtil();
		Articlelist = new ArrayList<ArticleModel>();
	}

	private void initEvent() {
		photoImg.setOnClickListener(this);
		searchCancel.setOnClickListener(this);
		searchImg.setOnClickListener(new donghua());
		addImg.setOnClickListener(this);
		menu.setOnOpenListener(this);
		menu.setOnCloseListener(this);
		inputEdt.addTextChangedListener(this);
		inputEdt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_mainActivity_photo:
			if (!menu.isMenuShowing()) {
				menu.showMenu();
			}
			break;

		case R.id.edt_mainActivity_searchInput:

			break;

		case R.id.txt_mainActivity_cancel:
			shouqijianpan();

			Animation animationAlpha = new AlphaAnimation(1f, 0f);
			animationAlpha.setFillAfter(false);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
			animationAlpha.setDuration(500);// 动画持续时间1秒
			searchList.startAnimation(animationAlpha);// 是用ImageView来显示动画的
			animationAlpha.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation arg0) {
					ObjectAnimator yBouncer = ObjectAnimator.ofFloat(
							searchLayout, "y", titleY, searchY);
					yBouncer.setDuration(500);
					// 设置插值器(用于调节动画执行过程的速度)
					// 设置重复次数(缺省为0,表示不重复执行)
					yBouncer.setRepeatCount(0);
					// 设置动画开始的延时时间(200ms)
					// yBouncer.setStartDelay(200);
					// 开始动画
					yBouncer.start();

				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					searchList.setVisibility(View.GONE);
				}
			});

			break;
		case R.id.img_mainActivity_add_photo:
			addImg.setVisibility(View.GONE);
			searchImg.setVisibility(View.GONE);
			showPopupWindow(v);

			// Toast.makeText(this, "点击添加", 0).show();
			break;
		}
	}

	/**
	 * 左边侧拉
	 */
	private void initLeft() {
		menu = new SlidingMenu(this);
		// 设置侧滑方向
		menu.setMode(SlidingMenu.LEFT);
		// 设置触摸屏幕的模式
		/**
		 * SlidingMenu.TOUCHMODE_MARGIN 边缘拖拽 SlidingMenu.TOUCHMODE_FULLSCREEN
		 * 全屏拖拽 SlidingMenu.TOUCHMODE_NONE 无法拖拽
		 */
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// 根据dimension资源文件的ID来设置阴影的宽度
		menu.setShadowWidthRes(R.dimen.shadow_width);
		// 根据Drawable来设置滑动菜单的阴影效果
		menu.setShadowDrawable(R.drawable.shadow);
		// 设置滑动菜单视图的宽度
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		menu.setFadeDegree(0.35f);
		// 把滑动菜单添加进所有的Activity中
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// 为侧滑菜单设置布局
		menu.setMenu(R.layout.left_menu_personal_center);

		Fragment leftFragment = new MenuLeftFragment(this, menu, preferences);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.left_menu, leftFragment, "Left").commit();
	}

	@Override
	public void onOpen() {
		dotView.setVisibility(View.GONE);
		photoImg.setVisibility(View.GONE);
	}

	@Override
	public void onClose() {
		if (isDot) {
			dotView.setVisibility(View.VISIBLE);
		} else {
			dotView.setVisibility(View.GONE);
		}
		photoImg.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onStart() {
		if (preferences.getInt("userId", -1) != -1) {
			loginHuanXin();

		}
		// data();
		headPortrait();

		super.onStart();
	}

	@Override
	protected void onStop() {
		if (menu.isMenuShowing()) {
			menu.toggle();
		}
		super.onStop();
	}

	/**
	 * 头像
	 */
	private void headPortrait() {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		xUtilsUtil.httpPost("common/queryCertainUser.html", params,
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
							JSONObject json = jsonObject.optJSONObject("data");
							Picasso.with(MainActivity.this)
									.load("http://weride.oss-cn-hangzhou.aliyuncs.com/"
											+ json.optString("userImage"))
									.into(photoImg);
							/*
							 * SystemUtil.loadImagexutils(
							 * json.optString("userImage"), photoImg,
							 * MainActivity.this);
							 */
						} else {
							photoImg.setImageResource(R.drawable.homepage_picture);
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {

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
		xUtilsUtil.httpPost("common/articleMemoList.html", params,
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
									MainActivity.this, Articlelist, IClist);
							slideShowList.setAdapter(adapter);
							slideShowList
									.setOnItemClickListener(MainActivity.this);
							slideShowList
									.setOnRefreshListener(MainActivity.this);
							if (loginFlag == 1) {
								splashView.splashAndDisappear();
								loginFlag = 0;
							}
							// 更新UI
							adapter.notifyDataSetChanged();
							slideShowList.finishRefreshing();

						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						new SystemUtil().makeToast(MainActivity.this,
								"网络请求失败，请检查网络");
						slideShowList.finishRefreshing();
					}
				});
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

							for (int i = 0; i < c.size(); i++) {
								IClist.add(new ImageCycleViewUtil.ImageInfo(
										"http://weride.oss-cn-hangzhou.aliyuncs.com/"
												+ c.get(i).getImageAdd(), c
												.get(i).getValue(), c.get(i)
												.getOpenType(), c.get(i)
												.getBannerType()));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(HttpException error, String msg) {
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		if (position != 0 && pageIndex < Articlelist.size() + 2) {
			Intent intent = new Intent(this, RidingDetailsActivity.class);
			intent.putExtra("isMe", false);
			intent.putExtra("articleId", Articlelist.get(position - 2)
					.getArticleId());
			startActivity(intent);
		}
	}

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
			System.exit(0);
		}
	}

	// 实现ConnectionListener接口
	private class MyConnectionListener implements EMConnectionListener {
		@Override
		public void onConnected() {
			// 已连接到服务器
		}

		@Override
		public void onDisconnected(final int error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (error == EMError.USER_REMOVED) {
						// 显示帐号已经被移除
						new SystemUtil()
								.makeToast(MainActivity.this, "帐号已经被移除");
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆
						new SystemUtil().makeToast(MainActivity.this, isdialog
								+ "");
						if (isdialog) {

							logOut();
							isdialog = false;
						}
					} else {
						if (NetUtils.hasNetwork(MainActivity.this)) {
							// 连接不到聊天服务器
							new SystemUtil().makeToast(MainActivity.this,
									"连接不到聊天服务器");
						} else {
							// 当前网络不可用，请检查网络设置
							new SystemUtil().makeToast(MainActivity.this,
									"当前网络不可用，请检查网络设置");
						}
					}
				}
			});
		}
	}

	private void logOut() {
		EMChatManager.getInstance().logout(new EMCallBack() {

			@Override
			public void onSuccess() {
				Log.d("LOGOUT", "环信登出成功!");
				System.err.println("环信登出成功!");

				SharedPreferences sp = getSharedPreferences("userInfo",
						Context.MODE_PRIVATE);
				Editor userInfo_Editor = sp.edit();
				userInfo_Editor.putBoolean("isLogin", false);
				userInfo_Editor.commit();
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, String message) {
				Log.d("LOGOUT", "环信登出失败!" + message);
				System.err.println("环信登出失败!" + message);
			}
		});
	}

	private void logoutService() {
		/**
		 * SharedPreferences清空用户Id和uniqueKey
		 */
		SharedPreferences sharedPreferences = getSharedPreferences("userLogin",
				Context.MODE_PRIVATE);
		final Editor editor = sharedPreferences.edit();// 获取编辑器

		String url = "mobile/push/releaseToken.html";
		RequestParams params = new RequestParams();

		params.addBodyParameter("userId",
				sharedPreferences.getInt("userId", -1) + "");
		params.addBodyParameter("uniqueKey",
				sharedPreferences.getString("uniqueKey", null));

		new XUtilsUtil().httpPost(url, params, new CallBackPost() {

			@Override
			public void onMySuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				try {

					JSONObject jsonObject = new JSONObject(result);

					if (jsonObject.getBoolean("result")) {
						editor.putInt("userId", -1);
						editor.putString("uniqueKey", null);
						editor.putString("imUserName", null);
						editor.putString("imPassword", null);
						editor.putString("mobilePhone", null);
						editor.putString("riderId", null);
						editor.commit();

						// 实例化Intent
						Intent intent = new Intent();
						// 设置Intent的action属性
						intent.setAction(ACTION);
						// 发出广播
						sendBroadcast(intent);
						isdialog = true;

						// MainActivity.this.finish();
						/*
						 * startActivity(new Intent(MainActivity.this,
						 * LoginActivity.class));
						 */
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onMyFailure(HttpException error, String msg) {

			}
		});
	}

	private int loginFlag;

	private void loginHuanXin() {

		// final Editor userInfo_Editor = sp.edit();
		// 登录环信
		EMChatManager.getInstance().login(
				preferences.getString("imUserName", null),
				preferences.getString("imPassword", null), new EMCallBack() {

					@Override
					public void onSuccess() {

						// // 保存用户名密码
						// userInfo_Editor.putString(
						// "USER_NAME",
						// usernameEdt.getText()
						// .toString());
						// userInfo_Editor.putString(
						// "USER_PASSWORD",
						// passwordEdt.getText()
						// .toString());
						// userInfo_Editor.putBoolean(
						// "isLogin", true);
						// userInfo_Editor.commit();

						// 更新环信用户昵称
						// EMChatManager
						// .getInstance()
						// .updateCurrentUserNick(
						// usernameEdt
						// .getText()
						// .toString());

						EMGroupManager.getInstance().loadAllGroups();

						Log.d("main", "环信登录成功！");
						System.out.println("环信登录成功！");

						// 注册一个监听连接状态的listener
						EMChatManager.getInstance().addConnectionListener(
								new MyConnectionListener());

					}

					@Override
					public void onProgress(int code, String status) {

					}

					@Override
					public void onError(int code, String message) {
						Log.d("main", "环信登录失败！" + message);
						System.out.println("环信登录失败！" + message);
					}
				});
	}

	@Override
	public void onMySuccess(ResponseInfo<String> responseInfo) {
		System.out.println("主页向服务器提交CID成功!" + responseInfo.result);
		Log.i("qilaiqiqu", "主页向服务器提交CID成功!" + responseInfo.result);
	}

	@Override
	public void onMyFailure(HttpException error, String msg) {
		System.out.println("主页向服务器提交CID出错:" + msg + "!");
		Log.i("qilaiqiqu", "主页向服务器提交CID出错:" + msg + "!");
	}

	@Override
	protected void onResume() {
		isNews();
		super.onResume();
		MobclickAgent.onResume(this);

	}

	private void isNews() {
		// if (preferences.getInt("userId", -1) != -1) {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("mobile/systemMessage/countUserMessage.html",
				params, new CallBackPost() {

					@Override
					public void onMySuccess(ResponseInfo<String> responseInfo) {
						JSONObject jsonObject = null;
						try {
							jsonObject = new JSONObject(responseInfo.result);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (jsonObject.optBoolean("result")) {
							int num = jsonObject.optInt("data");
							if (num == 0) {
								// 系统统计数
								dotView.setVisibility(View.GONE);
								isDot = false;
							} else {
								isDot = true;
								if (!menu.isMenuShowing()) {
									dotView.setVisibility(View.VISIBLE);
								} else {
									dotView.setVisibility(View.GONE);
								}
							}
						} else {
							dotView.setVisibility(View.GONE);
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {

					}
				});
		// }
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
		ActivityCollectorUtil.removeActivity(this);
	}

	@Override
	public void onRefresh() {
		data();
	}

	@Override
	public void onLoadingMore() {
		dataJ();
	}

	private void dataJ() {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("pageIndex", (++pageIndex) + "");
		params.addBodyParameter("pageSize", "10");
		params.addBodyParameter("uniqueKey",
				preferences.getString("uniqueKey", null));
		xUtilsUtil.httpPost("common/articleMemoList.html", params,
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

							JSONArray jsonArray = jsonObject
									.optJSONArray("dataList");
							// 数据获取
							Gson gson = new Gson();
							Type type = new TypeToken<List<ArticleModel>>() {
							}.getType();
							List<ArticleModel> lists = gson.fromJson(
									jsonArray.toString(), type);
							Articlelist.addAll(lists);
							pageIndex = jsonObject.optInt("pageIndex");
							if (lists.size() == 0) {
								new SystemUtil().makeToast(MainActivity.this,
										"已显示全部内容");
							} else {
								/*
								 * new SystemUtil().makeToast(MainActivity.this,
								 * "加载成功");
								 */
							}

							// 更新UI
							adapter.notifyDataSetChanged();

							slideShowList.finishRefreshing();
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						new SystemUtil().makeToast(MainActivity.this,
								"网络请求失败，请检查网络");
						slideShowList.finishRefreshing();
					}
				});
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

	class donghua implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			titleY = titleLayout.getY();
			searchY = searchLayout.getY();

			ObjectAnimator yBouncer = ObjectAnimator.ofFloat(searchLayout, "y",
					searchY, titleY);
			yBouncer.setDuration(500);
			// 设置插值器(用于调节动画执行过程的速度)
			// 设置重复次数(缺省为0,表示不重复执行)
			yBouncer.setRepeatCount(0);
			// 设置动画开始的延时时间(200ms)
			// yBouncer.setStartDelay(200);
			// 开始动画
			yBouncer.start();

			searchList.setVisibility(View.VISIBLE);
			searchList.getBackground().setAlpha(170);
			Animation animationAlpha = new AlphaAnimation(0f, 1f);
			animationAlpha.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
			animationAlpha.setDuration(500);// 动画持续时间0.2秒
			searchList.startAnimation(animationAlpha);// 是用ImageView来显示动画的

			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.showSoftInput(inputEdt, 0);

		}
	}

	@Override
	public void afterTextChanged(Editable e) {
		RequestParams params = new RequestParams("UTF-8");
		if (!e.toString().equals("")) {
			pageIndex = 1;
			params.addBodyParameter("searchValue", e.toString());
			params.addBodyParameter("pageIndex", pageIndex + "");
			params.addBodyParameter("pageSize", "10");
			params.addBodyParameter("userId", preferences.getInt("userId", -1)
					+ "");

			xUtilsUtil.httpPost("common/fuzzyQueryResultPaginationList.html",
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

								// 数据获取
								Gson gson = new Gson();
								Type type = new TypeToken<SearchResultModel>() {
								}.getType();
								final SearchResultModel model = gson.fromJson(
										jsonObject.toString(), type);
								List<SearchDataList> dataList = model
										.getDataList();
								List<SearchDataList> d = new ArrayList<SearchDataList>();
								for (int i = 0; i < dataList.size(); i++) {
									if (dataList.get(i).getType().toString()
											.equals("QYJ")) {
										d.add(dataList.get(i));
									}
								}
								if (d.size() == 0) {
									new SystemUtil().makeToast(
											MainActivity.this, "没有搜索到任何结果哦!");
								}
								adapterSearch = new SearchResultAdapter(
										MainActivity.this, d);
								searchList.setAdapter(adapterSearch);
								searchList
										.setOnItemClickListener(new OnItemClickListener() {
											@Override
											public void onItemClick(
													AdapterView<?> arg0,
													View arg1, int position,
													long arg3) {

												Intent intent = new Intent(
														MainActivity.this,
														RidingDetailsActivity.class);
												intent.putExtra("isMe", false);
												intent.putExtra("articleId",
														model.getDataList()
																.get(position)
																.getId());
												startActivity(intent);
											}
										});

								// 更新UI
								adapter.notifyDataSetChanged();

							}
						}

						@Override
						public void onMyFailure(HttpException error, String msg) {
							new SystemUtil().makeToast(MainActivity.this,
									"网络请求失败，请检查网络" + msg);
						}
					});
		}

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	public void shouqijianpan() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(inputEdt.getWindowToken(), 0);
	}

	private void showPopupWindow(View view) {

		// 一个自定义的布局，作为显示的内容
		View mview = LayoutInflater.from(this).inflate(
				R.layout.item_popup_release, null);

		TextView youjiTxt = (TextView) mview
				.findViewById(R.id.txt_releasePopup_youji);
		TextView activeTxt = (TextView) mview
				.findViewById(R.id.txt_releasePopup_active);
		TextView cancelTxt = (TextView) mview
				.findViewById(R.id.txt_releasePopup_cancel);

		final PopupWindow popupWindow = new PopupWindow(mview,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupWindow.setTouchable(true);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				addImg.setVisibility(View.VISIBLE);
				searchImg.setVisibility(View.VISIBLE);
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		youjiTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (preferences.getInt("userId", -1) != -1) {
					Intent intent = new Intent(MainActivity.this,
							NativeImagesActivity.class);
					intent.putExtra("falg", false);
					startActivity(intent);
				} else {
					new SystemUtil().makeToast(MainActivity.this, "请登录");
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(intent);
					// finish();
				}
				popupWindow.dismiss();
			}
		});

		activeTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (preferences.getInt("userId", -1) != -1) {
					Intent intent = new Intent(MainActivity.this,
							ReleaseActiveActivity.class);
					startActivity(intent);
				} else {
					new SystemUtil().makeToast(MainActivity.this, "请登录");
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(intent);
					// finish();
				}
				popupWindow.dismiss();
			}
		});

		cancelTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				addImg.setVisibility(View.VISIBLE);
				searchImg.setVisibility(View.VISIBLE);
				popupWindow.dismiss();
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 63);

	}


}
