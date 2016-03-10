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
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.fragment.FansAndCareFragmentPagerAdapter;

public class FriendActivity extends FragmentActivity implements OnClickListener{

	private LinearLayout backLayout;
	private TextView fansTxt;
	private TextView careTxt;
	private int friendFlag;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friend);
		initView();
		addListener();
		initEvent();
	}

	private void initView() {
		friendFlag = getIntent().getIntExtra("friendFlag", -1);

		viewPager = (ViewPager) findViewById(R.id.viewPager_friendActivity);
		fansTxt = (TextView) findViewById(R.id.txt_friendActivity_fans);
		careTxt = (TextView) findViewById(R.id.txt_friendActivity_care);
		backLayout = (LinearLayout) findViewById(R.id.layout_FriendActivity_back);
		
		
		FansAndCareFragmentPagerAdapter adapter = new FansAndCareFragmentPagerAdapter(
				getSupportFragmentManager());// 需要继承FragmentActivity
		viewPager.setAdapter(adapter);
		if(friendFlag == 0){
			fansTxt.setTextColor(0xffffffff);
			fansTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
			careTxt.setTextColor(0xff6dbfed);
			careTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);	
		}else{
			careTxt.setTextColor(0xffffffff);
			careTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_press);
			fansTxt.setTextColor(0xff6dbfed);
			fansTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_upspring);
		}
		
		viewPager.setCurrentItem(friendFlag);


		
	}
	private void addListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					friendFlag = 0;
					fansTxt.setTextColor(0xffffffff);
					fansTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
					careTxt.setTextColor(0xff6dbfed);
					careTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
					break;
				case 1:
					friendFlag = 1;
					careTxt.setTextColor(0xffffffff);
					careTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_press);
					fansTxt.setTextColor(0xff6dbfed);
					fansTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_upspring);
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
		fansTxt.setOnClickListener(this);
		careTxt.setOnClickListener(this);
		backLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_friendActivity_fans:
			friendFlag = 0;
			fansTxt.setTextColor(0xffffffff);
			careTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
			careTxt.setTextColor(0xff6dbfed);
			viewPager.setCurrentItem(friendFlag);
			break;

		case R.id.txt_friendActivity_care:
			friendFlag = 1;
			careTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_press);
			careTxt.setTextColor(0xffffffff);
			fansTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_upspring);
			fansTxt.setTextColor(0xff6dbfed);
			viewPager.setCurrentItem(friendFlag);
			break;

		case R.id.layout_FriendActivity_back:
			finish();
			break;

		default:
			break;
		}
	}
}
