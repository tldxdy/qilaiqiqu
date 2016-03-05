package com.qizhi.qilaiqiqu.application;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.qizhi.qilaiqiqu.activity.ChatActivity;

public class WeRideApplication extends Application {

	private static final String TAG = null;
	public static Context applicationContext;
	private static WeRideApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";

	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this;
		instance = this;

		EMChat.getInstance().init(applicationContext);
		EMChat.getInstance().setAutoLogin(true);
		setNotify();

		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果app启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
		// name就立即返回

		if (processAppName == null
				|| !processAppName.equalsIgnoreCase("com.easemob.chatuidemo")) {
			Log.e(TAG, "enter the service process!");
			// "com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名

			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}

		EMChat.getInstance().setDebugMode(true);
	}

	private String getAppName(int pid) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pid) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}

		return processName;
	}

	public void setNotify() {
		// 获取到配置options对象
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// 设置自定义的文字提示
		options.setNotifyText(new OnMessageNotifyListener() {

			@Override
			public String onNewMessageNotify(EMMessage message) {
				// 可以根据message的类型提示不同文字，这里为一个简单的示例
				return "你有新消息！";
			}

			@Override
			public String onLatestMessageNotify(EMMessage message,
					int fromUsersNum, int messageNum) {
				return fromUsersNum + "个骑友，发来了" + messageNum + "条消息";
			}

			@Override
			public String onSetNotificationTitle(EMMessage arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int onSetSmallIcon(EMMessage arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		
		//设置notification点击listener
		options.setOnNotificationClickListener(new OnNotificationClickListener() {
		 
			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(applicationContext, ChatActivity.class);
				ChatType chatType = message.getChatType();
				if(chatType == ChatType.Chat){ //单聊信息
					intent.putExtra("userId", message.getFrom());
				}else{ //群聊信息
					//message.getTo()为群聊id
					intent.putExtra("groupId", message.getTo());
				}
				startActivity(intent);
				
				return intent;
			}
		});
		
	}

}
