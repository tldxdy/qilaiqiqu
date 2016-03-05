package com.qizhi.qilaiqiqu.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 * 
 * @author mrsimple
 */
public class Refresh extends SwipeRefreshLayout{
	public Refresh(Context context) {
		this(context, null);
	}

	public Refresh(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

	}
}