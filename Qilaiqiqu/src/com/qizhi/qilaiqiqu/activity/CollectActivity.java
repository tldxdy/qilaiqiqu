package com.qizhi.qilaiqiqu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.fragment.CollectFragmentPagerAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author leiqian
 * 
 */
public class CollectActivity extends HuanxinLogOutActivity implements OnClickListener{
	private LinearLayout backLayout;
	private TextView manageTxt;
	private TextView historyTxt;
	private TextView titleTxt;
	
	private ViewPager viewPager;
	private int several;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_action_center);
		initView();
		addListener();
		initEvent();
	}
	private void initView() {

		viewPager = (ViewPager) findViewById(R.id.viewPager_actioncenteractivity);
		titleTxt = (TextView) findViewById(R.id.text_actioncenteractivity_title);
		manageTxt = (TextView) findViewById(R.id.txt_actioncenteractivity_manage);
		historyTxt = (TextView) findViewById(R.id.txt_actioncenteractivity_history);
		backLayout = (LinearLayout) findViewById(R.id.layout_actioncenteractivity_back);
		titleTxt.setText("我的收藏");
		manageTxt.setText("游记收藏");
		historyTxt.setText("活动收藏");
		CollectFragmentPagerAdapter adapter=new CollectFragmentPagerAdapter(
				getSupportFragmentManager());//需要继承FragmentActivity
		viewPager.setAdapter(adapter);
		manageTxt.setTextColor(0xffffffff);
		manageTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
		historyTxt.setTextColor(0xff6dbfed);
		historyTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
		several = 0;
		viewPager.setCurrentItem(several);
		
		
	}
	private void addListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					several = 0;
					manageTxt.setTextColor(0xffffffff);
					manageTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
					historyTxt.setTextColor(0xff6dbfed);
					historyTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
					break;
				case 1:
					several = 1;
					historyTxt.setTextColor(0xffffffff);
					historyTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_press);
					manageTxt.setTextColor(0xff6dbfed);
					manageTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_upspring);
					break;
					
				}
			}
			
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
	}
	private void initEvent() {
		manageTxt.setOnClickListener(this);
		historyTxt.setOnClickListener(this);
		backLayout.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_actioncenteractivity_manage:
			several = 0;
			manageTxt.setTextColor(0xffffffff);
			manageTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
			historyTxt.setTextColor(0xff6dbfed);
			historyTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
			viewPager.setCurrentItem(several);
			break;
		case R.id.txt_actioncenteractivity_history:
			several = 1;
			historyTxt.setTextColor(0xffffffff);
			historyTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_press);
			manageTxt.setTextColor(0xff6dbfed);
			manageTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_upspring);
			viewPager.setCurrentItem(several);
			break;
		case R.id.layout_actioncenteractivity_back:
			finish();
			break;
			
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}
}
