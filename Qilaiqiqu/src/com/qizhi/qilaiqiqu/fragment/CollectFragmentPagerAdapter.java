package com.qizhi.qilaiqiqu.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CollectFragmentPagerAdapter extends FragmentPagerAdapter{
	public final static int TAB_COUNT = 2;
	public CollectFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			
			RidingCollectFragment ridingCollectFragment = new RidingCollectFragment();
			return ridingCollectFragment;
		case 1:
			ActivityCollectFragment activityCollectFragment = new ActivityCollectFragment();
			return activityCollectFragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return TAB_COUNT;
	}

}
