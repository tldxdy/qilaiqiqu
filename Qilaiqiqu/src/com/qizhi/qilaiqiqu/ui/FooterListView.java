package com.qizhi.qilaiqiqu.ui;


import com.qizhi.qilaiqiqu.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class FooterListView extends ListView implements OnScrollListener{
	private View footerView/*,headerView*/;
	private int footerHeight;//footerView的高度
	private boolean isLoadingMore=false;//当前是否处于加载更多
	
	
	
	TextView post_peo,post_time,post_name,post_content;
	ImageView image_touxiang;
	Context context;
	
	public FooterListView(Context context) {
		super(context);
		init();
	}

	public FooterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	
	private void init() {
		setOnScrollListener(this);
		initFooterView();
	}
	//尾部上拉加载
		private void initFooterView() {
			footerView=View.inflate(getContext(), R.layout.layout_footer, null);
			footerView.measure(0, 0);//通知系统主动测量
			footerHeight=footerView.getMeasuredHeight();
			footerView.setPadding(0, -footerHeight, 0, 0);
			
			addFooterView(footerView);
			
		}
	/**
	 * 滚动状态监听
	 * SCROLL_STATE_IDLE:闲置状态，松开状态
	 * SCROLL_STATE_TOUCH_SCROLL：手指触摸滑动，手指按下，滑动状态
	 * SCROLL_STATE_FLING：快速滑动后松开
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState==OnScrollListener.SCROLL_STATE_IDLE
				&& getLastVisiblePosition()==(getCount()-1) && !isLoadingMore && (getCount()-1)>=10){
				isLoadingMore=true;
				footerView.setPadding(0, 0, 0, 0);//显示出加载更多
				setSelection(getCount());//让listView最后一条显示出来
				
				if(listener!=null){
					listener.onLoadingMore();
				}
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}
	private OnfreshListener listener;
	public void setOnfreshListener(OnfreshListener listener){
		this.listener=listener;
	}
	public interface OnfreshListener{
		void onLoadingMore();
	}
	
	/**
	 * 完成刷新操作，重置状态,在获取完数据并更新完adapter之后，去在UI线程中调用
	 */
	public void completeRefresh(){
		if(isLoadingMore){
			//重置footerView状态
			footerView.setPadding(0, -footerHeight, 0, 0);
			isLoadingMore=false;
		}
	}
}
