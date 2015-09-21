package com.mobile.oxi.xscan;

/**
 * Created by aabdel on 14/09/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amatellanes
 *
 */
public class VisorFragmentAdapter extends FragmentPagerAdapter {

	// List of fragments which are going to set in the view pager widget
	List<Fragment> fragments;

	/**
	 * Constructor
	 *
	 * @param fm
	 *            interface for interacting with Fragment objects inside of an
	 *            Activity
	 */
	public VisorFragmentAdapter(FragmentManager fm) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
	}

	/**
	 * Add a new fragment in the list.
	 *
	 * @param fragment
	 *            a new fragment
	 */
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
