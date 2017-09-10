package design.chat.template.adapter;

import java.util.ArrayList;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import design.chat.template.R;
import design.chat.template.SmartDrawer;
import design.chat.template.model.NavigationDrawerItem;
import design.chat.template.util.AppConstants;
import design.chat.template.util.SettingsPreferences;

/**
 * The adapter class to bind the views on list view with contents.
 * 
 * @author ATV Apps
 * 
 */
public class MenuAdapter extends BaseAdapter {

	private Context mContext;

	/** Holds Layout Inflater to inflate list item. */
	private LayoutInflater mLayoutInflator;

	/** Holds the list */
	private ArrayList<NavigationDrawerItem> mListItems;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public MenuAdapter(Context context) {
		super();
		mContext = context;
		mLayoutInflator = (LayoutInflater) context.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Method to set the list.
	 * 
	 * @param list
	 */
	public void setList(ArrayList<NavigationDrawerItem> list) {
		mListItems = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final NavigationDrawerItem item = getItem(position);
		if (view == null) {
			view = mLayoutInflator.inflate(R.layout.row_menu, null);
			ViewHolder holder = new ViewHolder();
			holder.tvTitle = (TextView) view.findViewById(R.id.text_name);
			holder.icon = (ImageView) view.findViewById(R.id.image);
			holder.red_radio = (ImageView) view
					.findViewById(R.id.red_radio_imageview);
			holder.blue_radio = (ImageView) view
					.findViewById(R.id.blue_radio_imageview);
			holder.gray_radio = (ImageView) view
					.findViewById(R.id.grey_radio_imageview);
			holder.default_radio = (ImageView) view
					.findViewById(R.id.default_radio_imageview);

			holder.brown_radio_imageview = (ImageView) view
					.findViewById(R.id.brown_radio_imageview);
			holder.lime_radio_imageview = (ImageView) view
					.findViewById(R.id.lime_radio_imageview);
			holder.green_radio_imageview = (ImageView) view
					.findViewById(R.id.green_radio_imageview);
			holder.pink_radio_imageview = (ImageView) view
					.findViewById(R.id.pink_radio_imageview);
			holder.purple_radio_imageview = (ImageView) view
					.findViewById(R.id.purple_radio_imageview);
			holder.amber_radio_imageview = (ImageView) view
					.findViewById(R.id.amber_radio_imageview);

			holder.smartDrawer = (SmartDrawer) view.findViewById(R.id.drawer);

			holder.theme_default = (LinearLayout) view
					.findViewById(R.id.theme_default);
			holder.theme_red = (LinearLayout) view.findViewById(R.id.theme_red);
			holder.theme_blue = (LinearLayout) view
					.findViewById(R.id.theme_blue);
			holder.theme_grey = (LinearLayout) view
					.findViewById(R.id.theme_grey);

			holder.theme_brown = (LinearLayout) view
					.findViewById(R.id.theme_brown);
			holder.theme_lime = (LinearLayout) view
					.findViewById(R.id.theme_lime);
			holder.theme_green = (LinearLayout) view
					.findViewById(R.id.theme_green);
			holder.theme_pink = (LinearLayout) view
					.findViewById(R.id.theme_pink);
			holder.theme_purple = (LinearLayout) view
					.findViewById(R.id.theme_purple);
			holder.theme_amber = (LinearLayout) view
					.findViewById(R.id.theme_amber);

			view.setTag(holder);
		}

		final ViewHolder viewHolder = (ViewHolder) view.getTag();
		if (viewHolder != null && item != null) {
			setTheme(viewHolder);
			viewHolder.tvTitle.setText(item.getTitle());
			viewHolder.icon.setImageResource(item.getIcon());
			viewHolder.theme_red.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences
							.seThemeIndex(mContext, AppConstants.RED);
					setTheme(viewHolder);
					sendBroadcast();
					dismissSmartDrawer(view);
				}
			});
			viewHolder.theme_blue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences.seThemeIndex(mContext,
							AppConstants.BLUE);
					setTheme(viewHolder);
					sendBroadcast();
					dismissSmartDrawer(view);
				}
			});
			viewHolder.theme_grey.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences.seThemeIndex(mContext,
							AppConstants.GREY);
					setTheme(viewHolder);
					sendBroadcast();
					dismissSmartDrawer(view);
				}
			});
			viewHolder.theme_default.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences.seThemeIndex(mContext,
							AppConstants.DEFAULT_COLOR);
					setTheme(viewHolder);
					sendBroadcast();
					dismissSmartDrawer(view);
				}
			});

			viewHolder.theme_brown.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences.seThemeIndex(mContext,
							AppConstants.BROWN);
					setTheme(viewHolder);
					sendBroadcast();
					dismissSmartDrawer(view);
				}
			});

			viewHolder.theme_lime.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences.seThemeIndex(mContext,
							AppConstants.LIME);
					setTheme(viewHolder);
					sendBroadcast();
					dismissSmartDrawer(view);
				}
			});

			viewHolder.theme_green.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences.seThemeIndex(mContext,
							AppConstants.GREEN);
					setTheme(viewHolder);
					sendBroadcast();
					dismissSmartDrawer(view);
				}
			});

			viewHolder.theme_pink.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences.seThemeIndex(mContext,
							AppConstants.PINK);
					setTheme(viewHolder);
					sendBroadcast();
					dismissSmartDrawer(view);
				}
			});

			viewHolder.theme_purple.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences.seThemeIndex(mContext,
							AppConstants.PURPLE);
					setTheme(viewHolder);
					sendBroadcast();
					dismissSmartDrawer(view);
				}
			});

			viewHolder.theme_amber.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences.seThemeIndex(mContext,
							AppConstants.AMBER);
					setTheme(viewHolder);
					sendBroadcast();
					dismissSmartDrawer(view);
				}
			});
			YoYo.with(Techniques.DropOut).duration(500 * position + 1)
					.delay(100).playOn(view.findViewById(R.id.image));
		}
		return view;
	}

	/**
	 * Method to send broadcast to chnage theme
	 */
	private void sendBroadcast() {
		Intent intent = new Intent(AppConstants.UPDATE_BACKGROUND_THEME);
		mContext.sendBroadcast(intent);
	}

	/**
	 * Method to set theme
	 * 
	 * @param viewHolder
	 * @param view
	 */
	private void setTheme(ViewHolder viewHolder) {
		sendBroadcast();
		switch (SettingsPreferences.getThemeIndex(mContext)) {
		case AppConstants.DEFAULT_COLOR:
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);

			viewHolder.brown_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.lime_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.green_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.pink_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.purple_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.amber_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			break;

		case AppConstants.RED:
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.brown_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.lime_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.green_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.pink_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.purple_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.amber_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			break;
		case AppConstants.BLUE:
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.brown_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.lime_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.green_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.pink_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.purple_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.amber_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			break;
		case AppConstants.GREY:
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.brown_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.lime_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.green_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.pink_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.purple_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.amber_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			break;

		case AppConstants.BROWN:
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);

			viewHolder.brown_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			viewHolder.lime_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.green_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.pink_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.purple_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.amber_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			break;

		case AppConstants.LIME:
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);

			viewHolder.brown_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.lime_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			viewHolder.green_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.pink_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.purple_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.amber_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			break;

		case AppConstants.GREEN:
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);

			viewHolder.brown_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.lime_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.green_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			viewHolder.pink_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.purple_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.amber_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			break;

		case AppConstants.PINK:
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);

			viewHolder.brown_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.lime_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.green_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.pink_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			viewHolder.purple_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.amber_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			break;

		case AppConstants.PURPLE:
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);

			viewHolder.brown_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.lime_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.green_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.pink_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.purple_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			viewHolder.amber_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			break;

		case AppConstants.AMBER:
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);

			viewHolder.brown_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.lime_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.green_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.pink_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.purple_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.amber_radio_imageview
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			break;

		}
	}

	/**
	 * Method to dismiss smart drawer
	 * 
	 * @param v
	 */
	public void dismissSmartDrawer(View v) {
		final LinearLayout linearLayout = (LinearLayout) v;
		linearLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				SmartDrawer smartDrawer = (SmartDrawer) linearLayout
						.getParent().getParent();
				smartDrawer.animateClose();
			}
		}, 200);
	}

	@Override
	public int getCount() {
		if (mListItems != null) {
			return mListItems.size();
		}
		return 0;
	}

	@Override
	public NavigationDrawerItem getItem(int postion) {
		if (mListItems != null) {
			return mListItems.get(postion);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * ViewHolder class to hold the views to bind on listview.
	 * 
	 * @author ATV Apps
	 * 
	 */
	static class ViewHolder {
		TextView tvTitle;
		ImageView icon;
		ImageView red_radio;
		ImageView blue_radio;
		ImageView gray_radio;
		ImageView default_radio;

		ImageView brown_radio_imageview;
		ImageView lime_radio_imageview;
		ImageView green_radio_imageview;
		ImageView pink_radio_imageview;
		ImageView purple_radio_imageview;
		ImageView amber_radio_imageview;
		SmartDrawer smartDrawer;
		LinearLayout theme_default;
		LinearLayout theme_red;
		LinearLayout theme_blue;
		LinearLayout theme_grey;

		LinearLayout theme_brown;
		LinearLayout theme_lime;
		LinearLayout theme_green;
		LinearLayout theme_pink;
		LinearLayout theme_purple;
		LinearLayout theme_amber;

	}

	/**
	 * Method to used for freeing up the resources
	 */
	public void cleanUp() {
		mListItems = null;
		mLayoutInflator = null;
		mContext = null;
	}

}
