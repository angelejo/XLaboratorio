package com.mobile.oxi.xscan;

/**
 * Created by aabdel on 14/09/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class VisorFragmentAdapter extends FragmentPagerAdapter {

	// List of fragments which are going to set in the view pager widget
	List<Fragment> fragments;

	public VisorFragmentAdapter(FragmentManager fm) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
	}


	public void addFragment(Fragment fragment) {
		this.fragments.add(fragment);
	}

	@Override
	public Fragment getItem(int arg0) {
		return this.fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}

}