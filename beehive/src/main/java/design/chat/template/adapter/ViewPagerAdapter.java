package design.chat.template.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import design.chat.template.activity.fragment.ContactsFragment;
import design.chat.template.activity.fragment.MessagesFragment;
import design.chat.template.activity.fragment.SettingsFragment;

/**
 * The adapter class to bind the views on list view with contents.
 * 
 * @author ATV Apps
 * 
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 3;
	private String titles[];

	public ViewPagerAdapter(FragmentManager fm, String[] titles2) {
		super(fm);
		titles = titles2;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return MessagesFragment.newInstance(position);
		case 1:
			return ContactsFragment.newInstance(position);
		case 2:
			return SettingsFragment.newInstance(position);

		}
		return null;
	}

	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

}