package com.qizhi.qilaiqiqu.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.RiderDetailsActivity;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackGet;

public class UpdateManager {
	// 文件分隔符
	private static final String FILE_SEPARATOR = "/";
	// 外存sdcard存放路径
	private static final String FILE_PATH = Environment
			.getExternalStorageDirectory()
			+ FILE_SEPARATOR
			+ "autoupdate"
			+ FILE_SEPARATOR;
	// 下载应用存放全路径
	private static final String FILE_NAME = FILE_PATH + "qilaiqiqu.apk";
	// 更新应用版本标记
	private static final int UPDARE_TOKEN = 0x29;
	// 准备安装新版本应用标记
	private static final int INSTALL_TOKEN = 0x31;

	private Context context;
	private String message = "检测到本程序有新版本发布，建议您更新！";
	// 以华为天天聊hotalk.apk为例
	private String spec = "";
	// 下载应用的对话框
	private Dialog dialog;
	// 下载应用的进度条
	private ProgressBar progressBar;
	// 进度条的当前刻度值
	private int curProgress;
	// 用户是否取消下载
	private boolean isCancel;
	// 当前版本号
	private int nowVersionCode;
	// 当前版本名
	private String nowVersionName;

	private XUtilsUtil httpUtils;

	public UpdateManager(Context context) {
		this.context = context;
		httpUtils = new XUtilsUtil();
	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDARE_TOKEN:
				progressBar.setProgress(curProgress);
				break;

			case INSTALL_TOKEN:
				installApp();
				break;
			}
		}
	};

	/**
	 * 检测应用更新信息
	 */
	public void checkUpdateInfo() {
		String pName = "com.qizhi.qilaiqiqu";
		try {
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(
					pName, PackageManager.GET_CONFIGURATIONS);
			nowVersionName = pinfo.versionName;
			nowVersionCode = pinfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(nowVersionName.length()>0){
			getNewVersion();
		}
	}

	private void getNewVersion() {
		String url = "";
		httpUtils.httpGet(url, new CallBackGet() {

			@Override
			public void onMySuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				System.out.println("陪骑打分信息:" + result);

				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getInt("versionCode") > nowVersionCode) {
						spec = jsonObject.getString("");
						if (spec.equals("") || spec.equals(null)) {

						} else {
							showNoticeDialog();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onMyFailure(HttpException error, String msg) {
				Toasts.show(context, "获取更新失败", 0);
			}
		});

	}

	/**
	 * 显示提示更新对话框
	 */
	private void showNoticeDialog() {
		new AlertDialog.Builder(context).setTitle("软件版本更新").setMessage(message)
				.setPositiveButton("下载", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						showDownloadDialog();
					}
				}).setNegativeButton("以后再说", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create().show();
	}

	/**
	 * 显示下载进度对话框
	 */
	private void showDownloadDialog() {
		View view = LayoutInflater.from(context).inflate(R.layout.progress,
				null);
		progressBar = (ProgressBar) view.findViewById(R.id.progress);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("软件版本更新");
		builder.setView(view);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				isCancel = true;
			}
		});
		dialog = builder.create();
		dialog.show();
		downloadApp();
	}

	/**
	 * 下载新版本应用
	 */
	private void downloadApp() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				URL url = null;
				InputStream in = null;
				FileOutputStream out = null;
				HttpURLConnection conn = null;
				try {
					url = new URL(spec);
					conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					long fileLength = conn.getContentLength();
					in = conn.getInputStream();
					File filePath = new File(FILE_PATH);
					if (!filePath.exists()) {
						filePath.mkdir();
					}
					out = new FileOutputStream(new File(FILE_NAME));
					byte[] buffer = new byte[1024];
					int len = 0;
					long readedLength = 0l;
					while ((len = in.read(buffer)) != -1) {
						// 用户点击“取消”按钮，下载中断
						if (isCancel) {
							break;
						}
						out.write(buffer, 0, len);
						readedLength += len;
						curProgress = (int) (((float) readedLength / fileLength) * 100);
						handler.sendEmptyMessage(UPDARE_TOKEN);
						if (readedLength >= fileLength) {
							dialog.dismiss();
							// 下载完毕，通知安装
							handler.sendEmptyMessage(INSTALL_TOKEN);
							break;
						}
					}
					out.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
		}).start();
	}

	/**
	 * 安装新版本应用
	 */
	private void installApp() {
		File appFile = new File(FILE_NAME);
		if (!appFile.exists()) {
			return;
		}
		// 跳转到新版本应用安装页面
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + appFile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}