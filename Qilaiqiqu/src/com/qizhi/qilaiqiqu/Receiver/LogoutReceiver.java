package com.qizhi.qilaiqiqu.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

import com.qizhi.qilaiqiqu.activity.LoginActivity;
import com.qizhi.qilaiqiqu.utils.ActivityCollectorUtil;

public class LogoutReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent arg1) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setTitle("警告");
		dialogBuilder
				.setMessage("帐号在其他设备登陆,请重新登录!");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ActivityCollectorUtil.finishAll(); // 销毁所有活动
						Intent intent = new Intent(context, LoginActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent); // 重新启动LoginActivity
					}
				});
		AlertDialog alertDialog = dialogBuilder.create();
		// 需要设置AlertDialog的类型，保证在广播接收器中可以正常弹出
		alertDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alertDialog.show();
	}

}
