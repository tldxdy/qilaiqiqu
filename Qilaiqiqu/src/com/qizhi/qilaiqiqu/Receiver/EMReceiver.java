package com.qizhi.qilaiqiqu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;

public class EMReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 消息id
		String msgId = intent.getStringExtra("msgid");
		// 发消息的人的username(userid)
		String msgFrom = intent.getStringExtra("from");
		// 消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
		// 所以消息type实际为是enum类型
		int msgType = intent.getIntExtra("type", 0);
		Log.d("main", "new message id:" + msgId + " from:" + msgFrom + " type:"
				+ msgType);
		// 更方便的方法是通过msgId直接获取整个message
		EMMessage message = EMChatManager.getInstance().getMessage(msgId);
		Log.d("main", "MESSAGE:" + message);
		
		System.out.println("EMReceiver-----MESSAGE:" + message);
	}

}
