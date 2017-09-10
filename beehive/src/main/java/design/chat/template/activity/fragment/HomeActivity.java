package design.chat.template.activity.fragment;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import design.chat.template.R;
import design.chat.template.SmartDrawer;
import design.chat.template.adapter.MenuAdapter;
import design.chat.template.adapter.ViewPagerAdapter;
import design.chat.template.manager.GPSTracker;
import design.chat.template.manager.ThemeContentManager;
import design.chat.template.model.ModelFirebase;
import design.chat.template.model.NavigationDrawerItem;
import design.chat.template.util.AppConstants;
import design.chat.template.model.Coordinates;
import design.chat.template.util.SettingsPreferences;

/**
 * The HomeActivity class to show home view contains
 * 
 * @author ATV Apps
 * 
 */
public class HomeActivity extends ActionBarActivity {

	private Context mContext;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerList;
	private ViewPager mViewPager;
	private Toolbar mToolbar;
	private TypedArray mNavigationMenuIcons;
	private String[] mNavigationMenuTitles;
	private SlidingTabLayout mSlidingTabLayout;
	private MenuAdapter mMenuAdapter;
	private ArrayList<NavigationDrawerItem> mNavigationDrawerItems;
	private Dialog mCustomDialog;
	private ModelFirebase modelFirebase;
	private GPSTracker gpsTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample);
		mContext = HomeActivity.this;
		gpsTracker = new GPSTracker(mContext);
		modelFirebase = new ModelFirebase();
		initView();
		showToolbar();//Loads menu bar


	}

	/**
	 * Init Components
	 */
	private void initView() {
		mActionBroadcastReceiver = new ActionBrodcastListener();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstants.UPDATE_BACKGROUND_THEME);
		registerReceiver(mActionBroadcastReceiver, intentFilter);
		if(gpsTracker.canGetLocation()){
			double latitude = gpsTracker.getLatitude();
			double longitude = gpsTracker.getLongitude();
			Coordinates coordinates = new Coordinates(Double.toString(latitude),Double.toString(longitude));
			modelFirebase.updateLocation(coordinates);

		} else{
		// can't get location
		// GPS or Network is not enabled
		// Ask user to enable GPS/network in settings
			gpsTracker.showSettingsAlert();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case android.R.id.home:
			mDrawerLayout.openDrawer(Gravity.START);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Method to show toolbar and drawer layout
	 */
	private void showToolbar() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.navdrawer);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
			mToolbar.setNavigationIcon(R.drawable.ic_ab_drawer);//Menu lines
		}
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),
				AppConstants.TITLE));

		mSlidingTabLayout.setViewPager(mViewPager);

		mSlidingTabLayout
				.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
					@Override
					public int getIndicatorColor(int position) {
						return Color.YELLOW;//Yellow line
					}
				});

		mDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this,
				mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				mMenuAdapter.notifyDataSetChanged();
				supportInvalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mNavigationMenuTitles = getResources().getStringArray(
				R.array.nav_drawer_items);
		mNavigationMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_icons);
		mNavigationDrawerItems = new ArrayList<NavigationDrawerItem>();
		mNavigationDrawerItems.add(new NavigationDrawerItem(
				mNavigationMenuTitles[0], mNavigationMenuIcons.getResourceId(0,
						-1)));
		mNavigationDrawerItems.add(new NavigationDrawerItem(
				mNavigationMenuTitles[1], mNavigationMenuIcons.getResourceId(1,
						-1)));
		mNavigationDrawerItems.add(new NavigationDrawerItem(
				mNavigationMenuTitles[2], mNavigationMenuIcons.getResourceId(2,
						-1)));
		mNavigationDrawerItems.add(new NavigationDrawerItem(
				mNavigationMenuTitles[3], mNavigationMenuIcons.getResourceId(3,
						-1)));
		mNavigationDrawerItems.add(new NavigationDrawerItem(
				mNavigationMenuTitles[4], mNavigationMenuIcons.getResourceId(4,
						-1)));
		mNavigationMenuIcons.recycle();
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		mMenuAdapter = new MenuAdapter(HomeActivity.this);
		mMenuAdapter.setList(mNavigationDrawerItems);
		mDrawerList.setAdapter(mMenuAdapter);
		mSlidingTabLayout.setDistributeEvenly(true);
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			onItemClickListerner(position, view);
		}
	}

	/**
	 * Item click listener for navigation items
	 * 
	 * @param position
	 */
	private void onItemClickListerner(int position, View view) {
		switch (position) {
		case 0:
			moreAppClicked();
			mDrawerLayout.closeDrawers();
			break;
		case 1:
			updateClicked();
			mDrawerLayout.closeDrawers();
			break;
		case 2:
			SmartDrawer smartDrawer = (SmartDrawer) view
					.findViewById(R.id.drawer);
			smartDrawer.animateToggle();
			break;
		case 3:
			aboutClicked();
			mDrawerLayout.closeDrawers();
			break;
		case 4:
			finish();
			mDrawerLayout.closeDrawers();
			break;
		}
	}

	/**
	 * Method to Show apps from Developer
	 */
	private void moreAppClicked() {
		try {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://search?q=pub:"
							+ AppConstants.PLAYSTORE_ID)));
		} catch (ActivityNotFoundException anfe) {
			startActivity(new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/developer?id="
							+ AppConstants.PLAYSTORE_ID)));
		}
	}

	/**
	 * Method to update/rate app
	 */
	private void updateClicked() {
		try {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id="
							+ mContext.getPackageName())));
		} catch (ActivityNotFoundException anfe) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id="
							+ mContext.getPackageName())));
		}
	}

	/**
	 * Method to show about
	 */
	private void aboutClicked() {
		try {
			String appname = null;
			appname = getString(R.string.app_name_message) + " "
					+ getString(R.string.app_name) + "" + "\n\n";
			String appversion = getString(R.string.app_version)
					+ " "
					+ mContext.getPackageManager().getPackageInfo(
							mContext.getPackageName(),
							PackageInfo.CONTENTS_FILE_DESCRIPTOR).versionCode
					+ "\n\n";
			String name = getString(R.string.response_message);

			showAboutCustomDialog(getString(R.string.about), appname
					+ appversion + name, getString(R.string.ok),
					getString(R.string.cancel), true);
		} catch (NameNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Method to show custom dialog.
	 * 
	 * @param title
	 * @param message
	 * @param okText
	 * @param cancelText
	 * @param singleButtonEnabled
	 */
	private void showAboutCustomDialog(String title, String message,
			String okText, String cancelText, boolean singleButtonEnabled) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(R.layout.about_dialog_view, null);
		RelativeLayout titlebarView = (RelativeLayout) dialogView
				.findViewById(R.id.title_bar_view);
		Button okButton = (Button) dialogView.findViewById(R.id.ok_button);
		Button cancelButton = (Button) dialogView
				.findViewById(R.id.cancel_button);
		titlebarView.setBackgroundColor(ThemeContentManager.getInstance()
				.getTheme(mContext));
		okButton.setBackgroundColor(ThemeContentManager.getInstance().getTheme(
				mContext));
		cancelButton.setBackgroundColor(ThemeContentManager.getInstance()
				.getTheme(mContext));

		TextView titleTextview = (TextView) dialogView
				.findViewById(R.id.title_textview);
		TextView messageTextview = (TextView) dialogView
				.findViewById(R.id.message_textview);

		TextView name = (TextView) dialogView.findViewById(R.id.email_textview);
		name.setText(AppConstants.EMAIL);
		if (mCustomDialog != null) {
			mCustomDialog.dismiss();
			mCustomDialog = null;
		}
		mCustomDialog = new Dialog(mContext,
				android.R.style.Theme_Translucent_NoTitleBar);
		mCustomDialog.setContentView(dialogView);
		mCustomDialog.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
					dialog.dismiss();
				}
				return false;
			}
		});
		mCustomDialog.setOnDismissListener(new OnDismissListener() {

			public void onDismiss(DialogInterface dialog) {
			}
		});

		mCustomDialog.setCanceledOnTouchOutside(false);

		titleTextview.setText(title);
		messageTextview.setText(message);
		if (singleButtonEnabled) {
			okButton.setText(okText);
			cancelButton.setVisibility(View.GONE);
		} else {
			okButton.setText(okText);
			cancelButton.setText(cancelText);
		}

		/**
		 * Listener for OK button click.
		 */
		okButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mCustomDialog.dismiss();
			}
		});

		/**
		 * Listener for Cancel button click.
		 */
		cancelButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mCustomDialog.dismiss();
			}
		});

		mCustomDialog.show();
	}

	private ActionBrodcastListener mActionBroadcastReceiver;

	/**
	 * Class to get theme change action broadcast
	 * 
	 * @author ATV Apps
	 * 
	 */
	public class ActionBrodcastListener extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(AppConstants.UPDATE_BACKGROUND_THEME)) {
				toolbarTheme(SettingsPreferences.getThemeIndex(mContext));
			}
		}
	}

	/**
	 * Method to set toolbar theme
	 * 
	 * @param position
	 */
	private void toolbarTheme(int position) {
		mDrawerList.setBackgroundColor(ThemeContentManager.getInstance()
				.getTheme(mContext));
		mToolbar.setBackgroundColor(ThemeContentManager.getInstance().getTheme(
				mContext));
		mSlidingTabLayout.setBackgroundColor(ThemeContentManager.getInstance()
				.getTheme(mContext));
	}

	@Override
	protected void onDestroy() {
		if (mContext != null) {
			if (mActionBroadcastReceiver != null) {
				unregisterReceiver(mActionBroadcastReceiver);
				mActionBroadcastReceiver = null;
			}
			mDrawerLayout = null;
			mDrawerToggle = null;
			mDrawerList = null;
			mViewPager = null;
			mToolbar = null;
			mSlidingTabLayout = null;
			mContext = null;
			super.onDestroy();
		}
	}

}
