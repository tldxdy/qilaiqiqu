package com.qizhi.qilaiqiqu.receiver;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.qizhi.qilaiqiqu.activity.ActivityDetailsActivity;
import com.qizhi.qilaiqiqu.activity.DiscussActivity;
import com.qizhi.qilaiqiqu.activity.FriendActivity;
import com.qizhi.qilaiqiqu.activity.RidingDetailsActivity;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private String key;
	private String value;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...
			System.out.println("[MyReceiver] 接收Registration Id : " + regId);
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// processCustomMessage(context, bundle);
			System.out.println("[MyReceiver] 接收到推送下来的自定义消息: "
					+ bundle.getString(JPushInterface.EXTRA_MESSAGE));

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			System.out.println("[MyReceiver] 用户点击打开了通知");
			String EXTRA = bundle.getString(JPushInterface.EXTRA_EXTRA);
			System.out.println("EXTRA:" + EXTRA + "|||||||||||||||||||||||");

			try {
				JSONObject jsonObject = new JSONObject(EXTRA);
				key = jsonObject.getString("pushType");
				JSONObject pushValue = null;
				if (!key.equals("YHGZ")) {
					pushValue = new JSONObject(
							jsonObject.getString("pushValue"));
				}

				if (key.equals("QYJPL")) {
					value = pushValue.getString("articleId");
					Intent i = new Intent(context, DiscussActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("articleId", Integer.parseInt(value));
					context.startActivity(i);

				} else if (key.equals("YHGZ")) {

					Intent i = new Intent(context, FriendActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("friendFlag", 0);
					context.startActivity(i);

				} else if (key.equals("QYJDZ")) {
					String praiseNum = pushValue.getString("praiseNum");
					String title = pushValue.getString("title");
					String userName = pushValue.getString("userName");
					value = pushValue.getString("articleId");
					Intent i = new Intent(context, RidingDetailsActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("jpushFlag", "JPushDZ");
					i.putExtra("praiseNum", praiseNum);
					i.putExtra("title", title);
					i.putExtra("userName", userName);
					i.putExtra("articleId", Integer.parseInt(value));
					context.startActivity(i);

				} else if (key.equals("QYJDS")) {
					String integral = pushValue.getString("integral");
					String sumIntegral = pushValue.getString("sumIntegral");
					String title = pushValue.getString("title");
					String userName = pushValue.getString("userName");
					value = pushValue.getString("articleId");
					Intent i = new Intent(context, RidingDetailsActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);

					i.putExtra("jpushFlag", "JPushDS");
					i.putExtra("sumIntegral", sumIntegral);
					i.putExtra("integral", integral);
					i.putExtra("title", title);
					i.putExtra("userName", userName);
					i.putExtra("articleId", Integer.parseInt(value));
					context.startActivity(i);

				} else if (key.equals("QYJHF")) {
					value = pushValue.getString("articleId");
					Intent i = new Intent(context, DiscussActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("articleId", Integer.parseInt(value));
					context.startActivity(i);

				} else if (key.equals("QYJ")) {
					value = pushValue.getString("articleId");
					Intent i = new Intent(context, RidingDetailsActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("articleId", Integer.parseInt(value));
					context.startActivity(i);
				} else if (key.equals("FQHD")) {
					value = pushValue.getString("articleId");
					Intent i = new Intent(context,
							ActivityDetailsActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("activityId", Integer.parseInt(value));
					context.startActivity(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// 打开自定义的Activity
			// Intent i = new Intent(context, TestActivity.class);
			// i.putExtras(bundle);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
			// Intent.FLAG_ACTIVITY_CLEAR_TOP );
			// context.startActivity(i);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..
			System.out.println("[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
					+ bundle.getString(JPushInterface.EXTRA_EXTRA));
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
			System.out.println("[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			System.out.println("[MyReceiver] Unhandled intent - "
					+ intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				// if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
				// Log.i(TAG, "This message has no Extra data");
				// continue;
				// }

				try {
					JSONObject json = new JSONObject(
							bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it = json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" + myKey + " - "
								+ json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MainActivity
	// private void processCustomMessage(Context context, Bundle bundle) {
	// if (MainActivity.isForeground) {
	// String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
	// String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
	// Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
	// msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
	// if (!ExampleUtil.isEmpty(extras)) {
	// try {
	// JSONObject extraJson = new JSONObject(extras);
	// if (null != extraJson && extraJson.length() > 0) {
	// msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
	// }
	// } catch (JSONException e) {
	//
	// }
	//
	// }
	// context.sendBroadcast(msgIntent);
	// }
	// }

}
