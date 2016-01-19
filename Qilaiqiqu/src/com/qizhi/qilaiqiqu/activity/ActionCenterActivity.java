package com.qizhi.qilaiqiqu.activity;

import java.util.ArrayList;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.fragment.CareFragment;
import com.qizhi.qilaiqiqu.fragment.FansFragment;
import com.qizhi.qilaiqiqu.fragment.ManageFragment;
import com.qizhi.qilaiqiqu.fragment.MyFragmentPagerAdapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionCenterActivity extends FragmentActivity implements OnClickListener{

	private LinearLayout backLayout;
	private TextView manageTxt;
	private TextView historyTxt;
	
	private ViewPager viewPager;
	private ArrayList<Fragment> fragments;
	private FragmentPagerAdapter adapter;
	
	private SharedPreferences sp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_action_center);
		initView();
		addListener();
		initButton();
	}

	private void initView() {

		viewPager = (ViewPager) findViewById(R.id.viewPager_actioncenteractivity);
		manageTxt = (TextView) findViewById(R.id.txt_actioncenteractivity_manage);
		historyTxt = (TextView) findViewById(R.id.txt_actioncenteractivity_history);
		backLayout = (LinearLayout) findViewById(R.id.layout_actioncenteractivity_back);

		MyFragmentPagerAdapter adapter=new MyFragmentPagerAdapter(
				getSupportFragmentManager());//需要继承FragmentActivity
		viewPager.setAdapter(adapter);
		manageTxt.setTextColor(0xffffffff);
		manageTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
		historyTxt.setTextColor(0xff6dbfed);
		historyTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
		viewPager.setCurrentItem(0);
		
		
	}
	private void addListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					manageTxt.setTextColor(0xffffffff);
					manageTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
					historyTxt.setTextColor(0xff6dbfed);
					historyTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
					break;
				case 1:
					historyTxt.setTextColor(0xffffffff);
					historyTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_press);
					manageTxt.setTextColor(0xff6dbfed);
					manageTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
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
	private void initButton() {
		manageTxt.setOnClickListener(this);
		historyTxt.setOnClickListener(this);
		backLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_actioncenteractivity_manage:
			manageTxt.setTextColor(0xffffffff);
			manageTxt.setBackgroundResource(R.drawable.corners_fragment_manage_left_press);
			historyTxt.setTextColor(0xff6dbfed);
			historyTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
			viewPager.setCurrentItem(0);
			break;
		case R.id.txt_actioncenteractivity_history:
			historyTxt.setTextColor(0xffffffff);
			historyTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_press);
			manageTxt.setTextColor(0xff6dbfed);
			manageTxt.setBackgroundResource(R.drawable.corners_fragment_history_right_upspring);
			viewPager.setCurrentItem(1);
			break;
		case R.id.layout_actioncenteractivity_back:
			finish();
			break;
			
		default:
			break;
		}
	}
	
}