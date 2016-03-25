package com.qizhi.qilaiqiqu.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.fragment.RiderManageCenterPagetAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author Administrator
 *			陪骑与约骑界面
 *
 */
public class RiderManageCenterActivity extends HuanxinLogOutActivity implements OnClickListener{
	private LinearLayout backLayout;
	private TextView accompanyrideTxt;
	private TextView arrangerideTxt;
	
	private ViewPager viewPager;
	private int several;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ridermanage_center);
		initView();
		addListener();
		initEvent();
	}
	private void initView() {

		viewPager = (ViewPager) findViewById(R.id.viewPager_ridermanagecenter);
		accompanyrideTxt = (TextView) findViewById(R.id.txt_ridermanagecenter_accompanyride);
		arrangerideTxt = (TextView) findViewById(R.id.txt_ridermanagecenter_arrangeride);
		backLayout = (LinearLayout) findViewById(R.id.layout_ridermanagecenter_back);
		
		RiderManageCenterPagetAdapter adapter=new RiderManageCenterPagetAdapter(
				getSupportFragmentManager());//需要继承FragmentActivity
		viewPager.setAdapter(adapter);
		accompanyrideTxt.setTextColor(0xffffffff);
		accompanyrideTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
		arrangerideTxt.setTextColor(0xff6dbfed);
		arrangerideTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
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
					accompanyrideTxt.setTextColor(0xffffffff);
					accompanyrideTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
					arrangerideTxt.setTextColor(0xff6dbfed);
					arrangerideTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
					break;
				case 1:
					several = 1;
					arrangerideTxt.setTextColor(0xffffffff);
					arrangerideTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_press);
					accompanyrideTxt.setTextColor(0xff6dbfed);
					accompanyrideTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_upspring);
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
		accompanyrideTxt.setOnClickListener(this);
		arrangerideTxt.setOnClickListener(this);
		backLayout.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_ridermanagecenter_accompanyride:
			several = 0;
			accompanyrideTxt.setTextColor(0xffffffff);
			accompanyrideTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
			arrangerideTxt.setTextColor(0xff6dbfed);
			arrangerideTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
			viewPager.setCurrentItem(several);
			break;
		case R.id.txt_ridermanagecenter_arrangeride:
			several = 1;
			arrangerideTxt.setTextColor(0xffffffff);
			arrangerideTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_press);
			accompanyrideTxt.setTextColor(0xff6dbfed);
			accompanyrideTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_upspring);
			viewPager.setCurrentItem(several);
			break;
		case R.id.layout_ridermanagecenter_back:
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
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
}
