package com.qizhi.qilaiqiqu.utils;




import java.text.SimpleDateFormat;
import java.util.Date;

import com.qizhi.qilaiqiqu.R;


import android.widget.AbsListView.OnScrollListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListViewUtil extends ListView implements OnScrollListener{


	private View headerView,footerView;
	private ImageView iv;
	private ProgressBar pb;
	private TextView tv1,tv2;
	
	
	private int headerHeight,footerHeight;//headerView和footerView的高度
	private int downY;//获取当前y的高度
	private final int PULL_REFRESH=0;	//下拉刷新的状态
	private final int RELEASE_REFRESH=1;//松开刷新的状态
	private final int REFRESHING=2;		//正在刷新的状态
	private int currentState=PULL_REFRESH;//默认下拉刷新状态
	private RotateAnimation upAnimation,downAnimation;//旋转
	
	private boolean isLoadingMore=false;//当前是否处于加载更多
	
	
	
	public ListViewUtil(Context context) {
		super(context);
		super.setOnScrollListener(this); 
		init();

	}

	public ListViewUtil(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setOnScrollListener(this); 
		init();
	}

	public ListViewUtil(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		super.setOnScrollListener(this); 
		init();
	}

	
	public void init(){
		initHeaderView();
		initRotateAnimation();
		initFooterView();
	}
	//初始化headerView
	private void initHeaderView() {
		headerView=View.inflate(getContext(), R.layout.pullrefresh_header, null);
		iv=(ImageView) headerView.findViewById(R.id.imageView);
		pb= (ProgressBar) headerView.findViewById(R.id.progressBar);
		tv1= (TextView) headerView.findViewById(R.id.textView1);
		tv2= (TextView) headerView.findViewById(R.id.textView2);
		
		
		headerView.measure(0, 0);//通知系统主动测量
		headerHeight=headerView.getMeasuredHeight();
		headerView.setPadding(0, -headerHeight, 0, 0);
		addHeaderView(headerView);
		
	}
	/**
	 * 箭头方向旋转改变
	 */
	private void initRotateAnimation() {
		upAnimation=new RotateAnimation(0, -180,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(300);
		upAnimation.setFillAfter(true);
		
		downAnimation=new RotateAnimation(-180, -360,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		downAnimation.setDuration(300);
		downAnimation.setFillAfter(true);
	}
	
	/**
	 * 触摸移动事件的处理
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY=(int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if(currentState==REFRESHING){
				break;
			}
			int deltaY=(int) (ev.getY()-downY);
			int paddingTop=-headerHeight+deltaY;
			if(paddingTop>-headerHeight && getFirstVisiblePosition()==0){
				System.out.println(paddingTop);
			headerView.setPadding(0, paddingTop, 0, 0);
			if(paddingTop>=0 && currentState==PULL_REFRESH){
				//下拉刷新状态进入松开刷新状态
				currentState=RELEASE_REFRESH;
				refreshHeaderView();
				return true;
			}else if (paddingTop<0 && currentState==RELEASE_REFRESH) {
				//松开刷新状态进入下拉刷新状态
				currentState=PULL_REFRESH;
				refreshHeaderView();
			}
			
			return true;
			}
			//return true;//拦截TouchMove,不让listView处理,会造成listView无法滑动
			
			break;
		case MotionEvent.ACTION_UP:
			if(currentState==PULL_REFRESH){
				headerView.setPadding(0, -headerHeight, 0, 0);
			}else if (currentState==RELEASE_REFRESH) {
				headerView.setPadding(0, 0, 0, 0);
				currentState=REFRESHING;
				refreshHeaderView();
				if(listener!=null){
					listener.onPullRefresh();
				}
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	/**
	 * 根据currentState来更新headerView
	 */
	private void refreshHeaderView(){
		switch (currentState) {
		case PULL_REFRESH:
			tv1.setText("下拉刷新");
			iv.startAnimation(downAnimation);	
			break;
		case RELEASE_REFRESH:
			tv1.setText("松开刷新");
			iv.startAnimation(upAnimation);
			break;
		case REFRESHING:
			tv1.setText("正在刷新...");
			iv.clearAnimation();//向上的旋转动画可能没有执行完
			iv.setVisibility(View.INVISIBLE);
			pb.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
	/**
	 * 完成刷新操作，重置状态,在获取完数据并更新完adapter之后，去在UI线程中调用
	 */
	public void completeRefresh(){
		if(isLoadingMore){
			//重置footerView状态
			footerView.setPadding(0, -footerHeight, 0, 0);
			isLoadingMore=false;
		}else{
			//重置headerView状态
			headerView.setPadding(0, -headerHeight, 0, 0);
			currentState=PULL_REFRESH;
			
			iv.setVisibility(View.INVISIBLE);
			pb.setVisibility(View.VISIBLE);
			tv1.setText("下拉刷新");
			tv2.setText("最后刷新:"+getCurrentTime());
			
			pb.setVisibility(View.INVISIBLE);
			iv.setVisibility(View.VISIBLE);
		}
		
	}
	private String getCurrentTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	private OnRefreshListener listener;
	public void setOnRefreshListener(OnRefreshListener listener){
		this.listener=listener;
	}
	public interface OnRefreshListener{
		void onPullRefresh();
		void onLoadingMore();
	}
	
	/**
	 * 尾部上拉加载
	 */
	private void initFooterView() {
		footerView=View.inflate(getContext(), R.layout.pullrefresh_footer, null);
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
			&& getLastVisiblePosition()==(getCount()-1) && !isLoadingMore){
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
}
