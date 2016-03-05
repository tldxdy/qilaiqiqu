package com.qizhi.qilaiqiqu.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.qizhi.qilaiqiqu.R;

public class PopupWindowUploading {
	
	private Context context;
	private PopupWindow popupWindow;
	
	public PopupWindowUploading(Context context){
		this.context = context;
		// 一个自定义的布局，作为显示的内容
		View mview = LayoutInflater.from(context).inflate(
				R.layout.uploading_cartoon, null);
		popupWindow = new PopupWindow(mview,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
	}
	
	public void show(View view) {


		popupWindow.setTouchable(true);
		
		popupWindow.setAnimationStyle(R.style.Popupfade_in_out);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, Gravity.CENTER);

	}
	public void dismiss(){
		popupWindow.dismiss();
	}
}
