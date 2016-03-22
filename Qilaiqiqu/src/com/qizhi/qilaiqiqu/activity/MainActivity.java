package com.qizhi.qilaiqiqu.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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

import cn.jpush.android.api.JPushInterface;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.NetUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.SearchResultAdapter;
import com.qizhi.qilaiqiqu.fragment.MenuLeftFragment;
import com.qizhi.qilaiqiqu.fragment.RidingAndActivityFragmentPagerAdapter;
import com.qizhi.qilaiqiqu.model.SearchResultModel;
import com.qizhi.qilaiqiqu.model.SearchResultModel.SearchDataList;
import com.qizhi.qilaiqiqu.receiver.EMReceiver;
import com.qizhi.qilaiqiqu.utils.ActivityCollectorUtil;
import com.qizhi.qilaiqiqu.utils.SplashView;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.Toasts;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("HandlerLeak")
public class MainActivity extends HuanxinLogOutActivity implements
		OnClickListener, OnOpenListener, OnCloseListener, CallBackPost,
		TextWatcher {

	public Set<String> chatUserList;
	public Set<String> groupChatUserList;
	private Gson gson;
	private Type type;
	// 定义action常量
	protected static final String ACTION = "com.qizhi.qilaiqiqu.receiver.LogoutReceiver";

	public static SplashView splashView;
	private FrameLayout frameLayout;

	private RelativeLayout titleLayout;
	private LinearLayout searchLayout;
	private ListView searchList;
	private TextView searchTxt;

	private EditText inputEdt;

	private float titleY;
	private float searchY;

	private TextView searchCancel;

	private ImageView photoImg;
	private RelativeLayout searchImg;
	private RelativeLayout addImg;

	private SearchResultAdapter adapterSearch;

	private SlidingMenu menu;

	private XUtilsUtil xUtilsUtil;

	public static boolean isForeground = false;

	public static String searchString;
	private int fragmentNum;

	private SharedPreferences preferences;

	private View dotView;

	private boolean isDot = false;

	boolean isdialog = true;

	private ViewPager viewPager;

	private TextView ridingTxt;
	private TextView activityTxt;

	private int pageIndex;

	private NewMessageBroadcastReceiver receiver;

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
		// setContentView(R.layout.activity_main_travelsandactive);
		ActivityCollectorUtil.addActivity(this);

		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_main_travelsandactive, null);
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
		initEMSDK();
		addListener();
		initEvent();
	}

	private void initView() {
		chatUserList = new HashSet<String>();
		groupChatUserList = new HashSet<String>();
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		gson = new Gson();
		type = new TypeToken<HashSet<String>>() {
		}.getType();
		String chat = preferences.getString(
				"Chat" + preferences.getString("uniqueKey", null), null);
		if (chat != null) {
			chatUserList = gson.fromJson(chat, type);
		}
		String groupChat = preferences.getString(
				"GroupChat" + preferences.getString("uniqueKey", null), null);
		if (groupChat != null) {
			groupChatUserList = gson.fromJson(groupChat, type);
		}

		/*
		 * chatUserList= preferences.getStringSet("Chat" +
		 * preferences.getString("uniqueKey", null), new HashSet<String>());
		 * groupChatUserList = preferences.getStringSet("GroupChat" +
		 * preferences.getString("uniqueKey", null), new HashSet<String>());
		 */

		searchCancel = (TextView) findViewById(R.id.txt_mainActivity_cancel);
		inputEdt = (EditText) findViewById(R.id.edt_mainActivity_searchInput);
		titleLayout = (RelativeLayout) findViewById(R.id.layout_mainActivity_title);
		searchLayout = (LinearLayout) findViewById(R.id.layout_mainActivity_searchLayout);
		searchList = (ListView) findViewById(R.id.list_mainActivity_searchResult);
		searchTxt = (TextView) findViewById(R.id.txt_mainActivity_searchView);

		photoImg = (ImageView) findViewById(R.id.img_mainActivity_photo);
		searchImg = (RelativeLayout) findViewById(R.id.img_mainActivity_search_photo);
		addImg = (RelativeLayout) findViewById(R.id.img_mainActivity_add_photo);
		addImg.setAlpha(204); // 透明度
		searchImg.setAlpha(204); // 透明度
		// slideShowList = (PullFreshListView)
		// findViewById(R.id.list_mainActivity_slideShow);
		dotView = findViewById(R.id.view_dot);

		xUtilsUtil = new XUtilsUtil();
		loginFlag = getIntent().getIntExtra("loginFlag", -1);

		viewPager = (ViewPager) findViewById(R.id.viewPager_mianactivity_ridingandactivity);
		ridingTxt = (TextView) findViewById(R.id.textView1);

		activityTxt = (TextView) findViewById(R.id.textView2);

		RidingAndActivityFragmentPagerAdapter adapter2 = new RidingAndActivityFragmentPagerAdapter(
				getSupportFragmentManager());// 需要继承FragmentActivity
		viewPager.setAdapter(adapter2);
		fragmentNum = getIntent().getIntExtra("fragmentNum", 0);
		setCurrent(fragmentNum);

		// 注册接收消息广播
		receiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		// 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
		intentFilter.setPriority(3);
		registerReceiver(receiver, intentFilter);
		EMChat.getInstance().setAppInited();
	}

	public void setCurrent(int fragmentNum) {
		viewPager.setCurrentItem(fragmentNum);
		if (fragmentNum == 0) {
			activityTxt.setTextColor(0xff6dbfed);
			ridingTxt.setTextColor(0xffffffff);
			activityTxt
					.setBackgroundResource(R.drawable.corners_mainactivity_right_down);
			ridingTxt
					.setBackgroundResource(R.drawable.corners_mainactivity_left_up);
		} else if (fragmentNum == 1) {
			ridingTxt.setTextColor(0xff6dbfed);
			activityTxt.setTextColor(0xffffffff);
			ridingTxt
					.setBackgroundResource(R.drawable.corners_mainactivity_left_down);
			activityTxt
					.setBackgroundResource(R.drawable.corners_mainactivity_right_up);
		}
	}

	private void addListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					fragmentNum = arg0;
					activityTxt.setTextColor(0xff6dbfed);
					ridingTxt.setTextColor(0xffffffff);
					activityTxt
							.setBackgroundResource(R.drawable.corners_mainactivity_right_down);
					ridingTxt
							.setBackgroundResource(R.drawable.corners_mainactivity_left_up);
					menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				case 1:
					fragmentNum = arg0;
					ridingTxt.setTextColor(0xff6dbfed);
					activityTxt.setTextColor(0xffffffff);
					ridingTxt
							.setBackgroundResource(R.drawable.corners_mainactivity_left_down);
					activityTxt
							.setBackgroundResource(R.drawable.corners_mainactivity_right_up);
					menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
					break;

				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initEvent() {
		ridingTxt.setOnClickListener(this);
		activityTxt.setOnClickListener(this);
		photoImg.setOnClickListener(this);
		searchCancel.setOnClickListener(this);
		searchImg.setOnClickListener(new donghua());
		addImg.setOnClickListener(this);
		menu.setOnOpenListener(this);
		menu.setOnCloseListener(this);
		inputEdt.addTextChangedListener(this);
		inputEdt.setOnClickListener(this);
		searchTxt.setOnClickListener(this);
	}

	// 注册接收ack回执消息的BroadcastReceiver
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					msg.isAcked = true;
				}
			}

		}
	};

	private void initEMSDK() {
		// 只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
		EMReceiver msgReceiver = new EMReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		EMChatManager.getInstance().getChatOptions().setRequireAck(true);
		// 如果用到已读的回执需要把这个flag设置成true

		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		EMChat.getInstance().setAppInited();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView1:
			fragmentNum = 0;
			activityTxt.setTextColor(0xff6dbfed);
			activityTxt
					.setBackgroundResource(R.drawable.corners_mainactivity_right_down);
			ridingTxt
					.setBackgroundResource(R.drawable.corners_mainactivity_left_up);
			ridingTxt.setTextColor(0xffffffff);
			viewPager.setCurrentItem(fragmentNum);
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			break;
		case R.id.textView2:
			fragmentNum = 1;
			ridingTxt.setTextColor(0xff6dbfed);
			ridingTxt
					.setBackgroundResource(R.drawable.corners_mainactivity_left_down);
			activityTxt.setTextColor(0xffffffff);
			activityTxt
					.setBackgroundResource(R.drawable.corners_mainactivity_right_up);
			viewPager.setCurrentItem(fragmentNum);
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			break;
		case R.id.img_mainActivity_add_photo:
			addImg.setVisibility(View.GONE);
			searchImg.setVisibility(View.GONE);
			showPopupWindow(v);

			// Toast.makeText(this, "点击添加", 0).show();
			break;
		case R.id.img_mainActivity_photo:
			if (!menu.isMenuShowing()) {
				menu.showMenu();
			}
			break;
		case R.id.txt_mainActivity_cancel:
			exitSearch();

			break;
		case R.id.txt_mainActivity_searchView:
			exitSearch();
			searchTxt.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	private void exitSearch() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(inputEdt.getWindowToken(), 0);

		Animation animationAlpha = new AlphaAnimation(1f, 0f);
		animationAlpha.setFillAfter(false);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
		animationAlpha.setDuration(500);// 动画持续时间1秒
		searchList.startAnimation(animationAlpha);// 是用ImageView来显示动画的
		animationAlpha.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				ObjectAnimator yBouncer = ObjectAnimator.ofFloat(searchLayout,
						"y", titleY, searchY);
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
				addImg.setVisibility(View.VISIBLE);
				searchImg.setVisibility(View.VISIBLE);
				// searchString = null;
				inputEdt.setText("");
				// viewPager.setCurrentItem(fragmentNum);
			}
		});
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
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
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

		Fragment leftFragment =MenuLeftFragment.newInstance("key", this, menu, preferences);
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

	/**
	 * 
	 * @param view
	 */
	private PopupWindow popupWindow;

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

		LinearLayout quxiao = (LinearLayout) mview.findViewById(R.id.quxiao);

		popupWindow = new PopupWindow(mview, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, false);

		popupWindow.setTouchable(true);

		popupWindow.setAnimationStyle(R.style.PopupAnimation);

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

		quxiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				addImg.setVisibility(View.VISIBLE);
				searchImg.setVisibility(View.VISIBLE);
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
					Toasts.show(MainActivity.this, "请登录", 0);
					// new SystemUtil().makeToast(MainActivity.this, "请登录");
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(intent);
					MainActivity.this.finish();
					// finish();
				}
				popupWindow.dismiss();
				addImg.setVisibility(View.VISIBLE);
				searchImg.setVisibility(View.VISIBLE);
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
					Toasts.show(MainActivity.this, "请登录", 0);
					// new SystemUtil().makeToast(MainActivity.this, "请登录");
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(intent);
					MainActivity.this.finish();
					// finish();
				}
				popupWindow.dismiss();
				addImg.setVisibility(View.VISIBLE);
				searchImg.setVisibility(View.VISIBLE);
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
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, Gravity.BOTTOM);

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
			searchTxt.setVisibility(View.VISIBLE);
			addImg.setVisibility(View.GONE);
			searchImg.setVisibility(View.GONE);
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
						editor.putString("riderId", null);
						editor.putString("userImage", null);
						editor.putString("uniqueKey", null);
						editor.putString("imPassword", null);
						editor.putString("userName", null);
						editor.putString("imUserName", null);
						editor.putString("mobilePhone", null);
						editor.commit();

						// 实例化Intent
						Intent intent = new Intent();
						// 设置Intent的action属性
						intent.setAction(ACTION);
						// 发出广播
						sendBroadcast(intent);
						isdialog = true;

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

	/**
	 * 消息广播接收者
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 记得把广播给终结掉
			abortBroadcast();
			// String from = intent.getStringExtra("from");
			String msgid = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgid);

			if (message.getChatType() == ChatType.Chat) {
				chatUserList.add(message.getFrom());
			} else if (message.getChatType() == ChatType.GroupChat) {
				groupChatUserList.add(message.getTo());
			}
			String chat = gson.toJson(chatUserList);
			String groupChat = gson.toJson(groupChatUserList);

			// 消息不是发给当前会话，return
			notifyNewMessage(message);

			SharedPreferences sharedPreferences = getSharedPreferences(
					"userLogin", Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();// 获取编辑器
			editor.putString(
					"Chat" + sharedPreferences.getString("uniqueKey", null),
					chat);
			editor.putString(
					"GroupChat"
							+ sharedPreferences.getString("uniqueKey", null),
					groupChat);
			editor.commit();

			try {
				message.getStringAttribute("IMUserNameExpand");
			} catch (EaseMobException e) {
				e.printStackTrace();
			}
			return;

		}
	}

	@Override
	public void afterTextChanged(Editable e) {
		/*
		 * if (!e.toString().equals("")) { searchString = e.toString();
		 * viewPager.setCurrentItem(2); }
		 */

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
								List<SearchDataList> searchResult = new ArrayList<SearchDataList>();
								for (int i = 0; i < dataList.size(); i++) {
									if (dataList.get(i).getType().toString()
											.equals("QYJ")) {
										searchResult.add(dataList.get(i));
									}
								}
								if (searchResult.size() == 0) {
									Toasts.show(MainActivity.this,
											"没有搜索到任何结果哦!", 0);
									/*
									 * new SystemUtil().makeToast(
									 * MainActivity.this, "没有搜索到任何结果哦!");
									 */
								}
								adapterSearch = new SearchResultAdapter(
										MainActivity.this, searchResult);
								searchList.setAdapter(adapterSearch);
								searchTxt.setVisibility(View.GONE);
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
							}
						}

						@Override
						public void onMyFailure(HttpException error, String msg) {
							Toasts.show(MainActivity.this, "网络请求失败，请检查网络", 0);
						}
					});
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	public static int loginFlag;

	private void loginHuanXin() {

		// final Editor userInfo_Editor = sp.edit();
		// 登录环信
		EMChatManager.getInstance().login(
				preferences.getString("imUserName", null),
				preferences.getString("imPassword", null), new EMCallBack() {

					@Override
					public void onSuccess() {

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
		splashView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 更新数据
				// 更新完后调用该方法结束刷新
				if (loginFlag == 1) {
					splashView.splashAndDisappear();
					loginFlag = 0;
				}
			}
		}, 1000);

		super.onResume();
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
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
						Toasts.show(MainActivity.this, "帐号已经被移除", 0);
						/*
						 * new SystemUtil() .makeToast(adsd.this, "帐号已经被移除");
						 */
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆
						Toasts.show(MainActivity.this, isdialog + "", 0);
						/*
						 * new SystemUtil().makeToast(adsd.this, isdialog + "");
						 */
						if (isdialog) {

							logOut();
							isdialog = false;
						}
					} else {
						if (NetUtils.hasNetwork(MainActivity.this)) {
							// 连接不到聊天服务器
							Toasts.show(MainActivity.this, "连接不到聊天服务器", 0);
							/*
							 * new SystemUtil().makeToast(adsd.this,
							 * "连接不到聊天服务器");
							 */
						} else {
							// 当前网络不可用，请检查网络设置
							Toasts.show(MainActivity.this, "当前网络不可用，请检查网络设置", 0);
							/*
							 * new SystemUtil().makeToast(adsd.this,
							 * "当前网络不可用，请检查网络设置");
							 */
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

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
		ActivityCollectorUtil.removeActivity(this);
	}

	/**
	 * 头像
	 */
	private void headPortrait() {
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
		isNews();
		if (preferences.getInt("userId", -1) != -1) {
			SystemUtil.Imagexutils(preferences.getString("userImage", null),
					photoImg, MainActivity.this);
		} else {
			photoImg.setImageResource(R.drawable.user_default);
			dotView.setVisibility(View.GONE);
		}

	}

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (addImg.getVisibility() == 8) {
				addImg.setVisibility(View.VISIBLE);
				searchImg.setVisibility(View.VISIBLE);
			}
			if (popupWindow != null) {
				popupWindow.dismiss();
				popupWindow = null;

				return false;
			}

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
			Toasts.show(this, "再按一次退出程序", 0);
			// Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
	
}
