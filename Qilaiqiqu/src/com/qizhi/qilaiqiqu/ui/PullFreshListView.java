package com.qizhi.qilaiqiqu.ui;


import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qizhi.qilaiqiqu.R;
public class PullFreshListView extends ListView  implements OnScrollListener{
	private final static int RELEASE_To_REFRESH = 0; //拖动中，松手立即刷新
	private final static int PULL_To_REFRESH = 1;//拖动中，松手不刷新
	private final static int REFRESHING = 2;//松手后正在回缩中
	private final static int DONE = 3;//header隐藏状态
	private final static int LOADING = 4;//正在加载
	private int state = DONE;//当前状态
	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;
		
	private OnRefreshListener onRefreshListener;
	
	private LinearLayout header;
	private int downY;
	private int headContentHeight,footContentHeight;
	
	private int firstVisibleItem;//当前listview的首条记录
	// 只有在listview第一个item显示的时候（listview滑到了顶部）才进行下拉刷新， 否则此时的下拉只是滑动listview  
	
	private RotateAnimation animation, animationf;
	private RotateAnimation reverseAnimation, reverseAnimationf;
    
    private ImageView arrowIv, arrowIvf;
    private TextView headermsg, footermsg;
    private RotateImageView progressBar,progressBarf;
    public View footerView;
	
	private boolean isLoadingMore;
	
	public PullFreshListView(Context context) {  
        super(context);
	}
	public PullFreshListView(Context context, AttributeSet attrs) { 
        super(context, attrs);  
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        header = (LinearLayout)(inflate.inflate(R.layout.pullrefresh_header, null));  
        headermsg = (TextView)header.findViewById(R.id.pullrefresh_header_msg);
        arrowIv = (ImageView)header.findViewById(R.id.pullrefresh_header_arrow);
        progressBar = (RotateImageView) header.findViewById(R.id.progressBar);
        progressBar.setImageId(R.drawable.loading_small_icon);
        measureView(header);
        headContentHeight = header.getMeasuredHeight();
        addHeaderView(header, null, false);
        header.setPadding(0, -1 * headContentHeight, 0, 0);
        header.invalidate();  
        super.setOnScrollListener(this); 
          
        animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
		
		
		initFooterView();
    }  
	@Override  
    public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){		
		    downY = (int)event.getY(); 
		    System.out.println("onTouchEvent2 ACTION_DOWN="+state);
        }else if(event.getAction() == MotionEvent.ACTION_UP){
        	if (firstVisibleItem == 0 && state != REFRESHING && state != LOADING) {
        		if(state != DONE)
        		    new RefreshingHeaderTask().execute();
        	}
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){     
        	System.out.println("onTouchEvent2 ACTION_MOVE="+state);
        	int moveY = (int)event.getY();
        	System.out.println("moveY"+moveY);
        	int deltaY = (int) (event.getY()-downY);
        	if (firstVisibleItem == 0 && state != REFRESHING && state != LOADING && deltaY > 0) {System.out.println("onTouchEvent2 进入");
        		// done状态下
				if (state == DONE) {//System.out.println("onTouchEvent DONE");
					downY = moveY;
						state = PULL_To_REFRESH;
						changeHeaderViewByState();						
						changeTopPadding(downY, moveY);
				}else if(state == PULL_To_REFRESH){//System.out.println("onTouchEvent PULL_To_REFRESH");
					setSelection(0);
					// 下拉到可以进入RELEASE_TO_REFRESH的状态
					if ((moveY - downY) / RATIO >= headContentHeight) {
						state = RELEASE_To_REFRESH;
						changeHeaderViewByState();
						changeTopPadding(downY, moveY);
					}else if((moveY - downY) / RATIO < headContentHeight) {
						changeTopPadding(downY, moveY);
					}else if (moveY - downY <= 0) {// 一下子推到顶了
						state = DONE;
						changeHeaderViewByState();
						changeTopPadding(moveY, moveY);
					}
				}else if(state == RELEASE_To_REFRESH){//System.out.println("onTouchEvent RELEASE_To_REFRESH");
					setSelection(0);
					// 下拉到可以进入RELEASE_TO_REFRESH的状态
					if ((moveY - downY) / RATIO >= headContentHeight) {
						changeTopPadding(downY, moveY);
					}else if((moveY - downY) / RATIO < headContentHeight) {
						state = PULL_To_REFRESH;
						changeHeaderViewByState();
						changeTopPadding(downY, moveY);
					}else if (moveY - downY <= 0) {// 一下子推到顶了
						state = DONE;
						changeHeaderViewByState();
						changeTopPadding(moveY, moveY);
					}
				}
        	}/*else if(deltaY < 0 && getLastVisiblePosition()==(getCount()-1) && !isLoadingMore){
        		System.out.println(deltaY);
        	}*/	
        }
		return super.onTouchEvent(event); 
	}
	/**
	 * 当状态改变时候，调用该方法，以更新界面
	 */
	private void changeHeaderViewByState(){
		if(state == DONE){System.out.println("onTouchEvent changeHeaderViewByState DONE");
			headermsg.setText("");
			arrowIv.setVisibility(View.VISIBLE);
			arrowIv.clearAnimation();
			progressBar.setVisibility(View.GONE);
			return ;
		}
		if(state == PULL_To_REFRESH){System.out.println("onTouchEvent changeHeaderViewByState PULL_To_REFRESH");
			headermsg.setText("下拉刷新");
			arrowIv.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			arrowIv.clearAnimation();
			arrowIv.startAnimation(reverseAnimation);
			return ;
		}
		if(state == RELEASE_To_REFRESH){System.out.println("onTouchEvent changeHeaderViewByState RELEASE_To_REFRESH");
			headermsg.setText("释放刷新");
			arrowIv.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			arrowIv.clearAnimation();
			arrowIv.startAnimation(animation);
			return ;
		}
		if(state == LOADING){System.out.println("onTouchEvent changeHeaderViewByState LOADING");
			headermsg.setText("加载中...");
			arrowIv.setVisibility(View.GONE);
			arrowIv.clearAnimation();
			progressBar.setVisibility(View.VISIBLE);
		}
	}
	
	
	// 调整header的大小。其实调整的只是距离顶部的高度。  
    private void changeTopPadding(int downY, int moveY) {  
    	header.setPadding(0, -1 * headContentHeight + (moveY - downY) / RATIO, 0, 0);
        header.invalidate();  
    }  
   // 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
 	private void measureView(View child) {
 		ViewGroup.LayoutParams p = child.getLayoutParams();
 		if (p == null) {
 			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
 					ViewGroup.LayoutParams.WRAP_CONTENT);
 		}
 		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
 		int lpHeight = p.height;
 		int childHeightSpec;
 		if (lpHeight > 0) {
 			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
 					MeasureSpec.EXACTLY);
 		} else {
 			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
 					MeasureSpec.UNSPECIFIED);
 		}
 		child.measure(childWidthSpec, childHeightSpec);
 	}
 	/**
 	 * 松手后回缩
 	 * @author pc
 	 *
 	 */
 	class RefreshingHeaderTask extends AsyncTask<Void, Integer, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			int tempState = state;
			state = REFRESHING;

			int topPadding = header.getPaddingTop();
			while (true) {
				topPadding = topPadding -10;
				if(tempState == PULL_To_REFRESH){
					if(topPadding <= -headContentHeight){
						publishProgress(-headContentHeight);
						break;
					}
				}else if(tempState == RELEASE_To_REFRESH){
					if(topPadding <= 0){
						publishProgress(0);
						break;
					}
				}
				publishProgress(topPadding);
				sleep(10);
			}
			return topPadding;
		}
		@Override
		protected void onProgressUpdate(Integer... topMargin) {
			header.setPadding(0, topMargin[0], 0, 0);			
		}
		@Override
		protected void onPostExecute(Integer topMargin) {
			int topPadding = header.getPaddingTop();
			if(topPadding<0){
				finishRefreshing();
				return ;
			}
			if(onRefreshListener == null){
				finishRefreshing();
				return ;
			}
			state = LOADING;
			changeHeaderViewByState();
			onRefreshListener.onRefresh();
		}
	}
 	/**
	 * 隐藏header
	 * @param time
	 */
	public void finishRefreshing(){
		
		if(isLoadingMore){
			//重置footerView状态
			footerView.setPadding(0, -footContentHeight, 0, 0);
			isLoadingMore=false;
		}else{
			state = DONE;
			changeTopPadding(0, 0);
			changeHeaderViewByState();
		}
		
	}
	
	
	/**
	 * 直接让其处于loading状态
	 * 供外部调用
	 * @param time
	 */
	public void doLoading(OnRefreshListener onRefreshListener){
		if(onRefreshListener == null)
			return ;
		this.onRefreshListener = onRefreshListener;
		state = LOADING;
		changeHeaderViewByState();
		header.setPadding(0, 0, 0, 0);
		onRefreshListener.onRefresh();
	}
	
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		this.firstVisibleItem = arg1; 
		
		
		
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
				
				if(onRefreshListener!=null){
					onRefreshListener.onLoadingMore();
				}
				
			}
	}
	public interface  OnRefreshListener{
		public void onRefresh(); 
		public void onLoadingMore();
	}

	
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}
	
	/**
	 * 尾部上拉加载
	 */
	private void initFooterView() {
		footerView=View.inflate(getContext(), R.layout.pullrefresh_footer, null);
		footerView.measure(0, 0);//通知系统主动测量
		footContentHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, -1 * footContentHeight, 0, 0);
		/*footerHeight=footerView.getMeasuredHeight();
		footerView.setPadding(0, -footerHeight, 0, 0);*/
		footermsg = (TextView) footerView.findViewById(R.id.pullrefresh_footer_msg);
		progressBarf = (RotateImageView) footerView.findViewById(R.id.progressBar);
		progressBarf.setImageId(R.drawable.loading_small_icon);
		
	    arrowIvf = (ImageView)footerView.findViewById(R.id.pullrefresh_header_arrow);
	        
	    
	        measureView(footerView);
	       
	        addFooterView(footerView, null, false); 
	        
	        footerView.invalidate();  
	        
	        animationf = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			animationf.setInterpolator(new LinearInterpolator());
			animationf.setDuration(250);
			animation.setFillAfter(true);

			reverseAnimationf = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			reverseAnimationf.setInterpolator(new LinearInterpolator());
			reverseAnimationf.setDuration(200);
			reverseAnimationf.setFillAfter(true);
			
			footermsg.setText("正在加载");
			progressBarf.setVisibility(View.VISIBLE);
	}
	


	
}
