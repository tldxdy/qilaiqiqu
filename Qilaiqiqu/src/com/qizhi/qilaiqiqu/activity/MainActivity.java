package com.qizhi.qilaiqiqu.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
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
import com.qizhi.qilaiqiqu.adapter.SlideShowListAdapter;
import com.qizhi.qilaiqiqu.fragment.MenuLeftFragment;
import com.qizhi.qilaiqiqu.model.ArticleModel;
import com.qizhi.qilaiqiqu.model.CarouselModel;
import com.qizhi.qilaiqiqu.model.UserLoginModel;
import com.qizhi.qilaiqiqu.utils.ImageCycleViewUtil;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;

/**
 * 
 * @author leiqian
 * 
 */
public class MainActivity extends FragmentActivity implements OnClickListener,
		OnOpenListener, OnCloseListener, OnItemClickListener {

	private ImageView photoImg;
	private ImageView searchImg;
	private ImageView addImg;

	private ListView slideShowList;

	private SlideShowListAdapter adapter;

	private SlidingMenu menu;

	private List<ArticleModel> list;

	private UserLoginModel userLogin = null;

	private XUtilsUtil xUtilsUtil;

	private Integer pageIndex = 1;

	private List<CarouselModel> cmList;

	public static boolean isForeground = false;

	private SharedPreferences preferences;

	List<ImageCycleViewUtil.ImageInfo> IClist = new ArrayList<ImageCycleViewUtil.ImageInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();
		initLeft();
		initEvent();
		imageUrl();
		
		//注册一个监听连接状态的listener
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);

		photoImg = (ImageView) findViewById(R.id.img_mainActivity_photo);
		searchImg = (ImageView) findViewById(R.id.img_mainActivity_search_photo);
		addImg = (ImageView) findViewById(R.id.img_mainActivity_add_photo);

		slideShowList = (ListView) findViewById(R.id.list_mainActivity_slideShow);

		addImg.setAlpha(204); // 透明度
		searchImg.setAlpha(204); // 透明度

		xUtilsUtil = new XUtilsUtil();
		list = new ArrayList<ArticleModel>();
		userLogin = (UserLoginModel) getIntent().getSerializableExtra(
				"userLogin");

	}

	private void initEvent() {
		photoImg.setOnClickListener(this);
		searchImg.setOnClickListener(this);
		addImg.setOnClickListener(this);
		menu.setOnOpenListener(this);
		menu.setOnCloseListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_mainActivity_photo:
			if (!menu.isMenuShowing()) {
				menu.showMenu();
			}
			break;
		case R.id.img_mainActivity_search_photo:
			new SystemUtil().makeToast(MainActivity.this, "点击搜索");
			break;
		case R.id.img_mainActivity_add_photo:
			if (userLogin != null) {
				Intent intent = new Intent(this, NativeImagesActivity.class);
				intent.putExtra("falg", false);
				startActivity(intent);
			} else {
				new SystemUtil().makeToast(MainActivity.this, "请登录");

				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
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
		photoImg.setVisibility(View.GONE);
	}

	@Override
	public void onClose() {
		photoImg.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onStart() {
		if(preferences.getInt("userId", -1) != -1){
			RequestParams params = new RequestParams("UTF-8");
			params.addBodyParameter("userId", preferences.getInt("userId", -1) + "");
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
								SystemUtil.loadImagexutils(
										json.optString("userImage"), photoImg,
										MainActivity.this);
							}
						}

						@Override
						public void onMyFailure(HttpException error, String msg) {

						}
					});
		}
		RequestParams params = new RequestParams("UTF-8");
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
							JSONArray jsonArray = jsonObject
									.optJSONArray("dataList");
							// 数据获取
							Gson gson = new Gson();
							Type type = new TypeToken<List<ArticleModel>>() {
							}.getType();
							list = gson.fromJson(jsonArray.toString(), type);

							adapter = new SlideShowListAdapter(
									MainActivity.this, list, IClist);
							slideShowList.setAdapter(adapter);
							slideShowList
									.setOnItemClickListener(MainActivity.this);
						}
					}

					@Override
					public void onMyFailure(HttpException error, String msg) {
						new SystemUtil().makeToast(MainActivity.this, "请求失败"
								+ error + ":" + msg);
					}
				});

		super.onStart();
	}

	public void imageUrl() {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET,
				"http://120.55.195.170:80/common/querySysImageByIsOrder.html",
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
							for (int i = 0; i < cmList.size(); i++) {
								IClist.add(new ImageCycleViewUtil.ImageInfo(
										"http://weride.oss-cn-hangzhou.aliyuncs.com/"
												+ cmList.get(i).getImageAdd(),
										cmList.get(i).getValue(), cmList.get(i)
												.getOpenType(), cmList.get(i)
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
		if (position != 0) {
			Intent intent = new Intent(this, RidingDetailsActivity.class);
			intent.putExtra("isMe", false);
			intent.putExtra("articleId", list.get(position - 1).getArticleId());
			startActivity(intent);
		}
	}

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
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
						new SystemUtil().makeToast(MainActivity.this, "帐号已经被移除");
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆
						new SystemUtil().makeToast(MainActivity.this, "帐号在其他设备登陆,请重新登录");
						logOut();
					} else {
						if (NetUtils.hasNetwork(MainActivity.this)) {
							// 连接不到聊天服务器
							new SystemUtil().makeToast(MainActivity.this, "连接不到聊天服务器");
						} else {
							// 当前网络不可用，请检查网络设置
							new SystemUtil().makeToast(MainActivity.this, "当前网络不可用，请检查网络设置");
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

				/**
				 * SharedPreferences清空用户Id和uniqueKey
				 */
				SharedPreferences sharedPreferences = getSharedPreferences(
						"userLogin", Context.MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();// 获取编辑器
				editor.putInt("userId", -1);
				editor.putString("uniqueKey", null);
				editor.commit();

				SharedPreferences sp = getSharedPreferences("userInfo",
						Context.MODE_PRIVATE);
				Editor userInfo_Editor = sp.edit();
				userInfo_Editor.putBoolean("isLogin", false);
				userInfo_Editor.commit();

				MainActivity.this.finish();
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
	
}
