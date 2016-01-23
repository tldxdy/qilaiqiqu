package com.qizhi.qilaiqiqu.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MessageCenterFragmentPagerAdapter extends FragmentPagerAdapter {
	public final static int TAB_COUNT = 2;
	public MessageCenterFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			
			return new Fragment();
			/*ManageFragment manageFragment = new ManageFragment();
			return manageFragment;*/
		case 1:
			MessageFragment messageFragment = new MessageFragment();
			return messageFragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return TAB_COUNT;
	}


}
