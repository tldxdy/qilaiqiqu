package com.qizhi.qilaiqiqu.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ActivityCenterFragmentPagerAdapter extends FragmentPagerAdapter {
	public final static int TAB_COUNT = 2;
	public ActivityCenterFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			ManageFragment manageFragment = new ManageFragment();
			return manageFragment;
		case 1:
			HistoryFragment historyFragment = new HistoryFragment();
			return historyFragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return TAB_COUNT;
	}


}
