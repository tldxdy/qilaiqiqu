package com.qizhi.qilaiqiqu.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RidingAndActivityFragmentPagerAdapter extends FragmentPagerAdapter {
	public final static int TAB_COUNT = 2;
	public RidingAndActivityFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	
	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			RidingFragment ridingFragment = new RidingFragment();
			return ridingFragment;
		case 1:
			ActivityFragment activityFragment = new ActivityFragment();
			return activityFragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return TAB_COUNT;
	}

}
