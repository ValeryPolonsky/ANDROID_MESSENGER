package design.chat.template.manager;

import android.content.Context;
import design.chat.template.R;
import design.chat.template.util.AppConstants;
import design.chat.template.util.SettingsPreferences;

/**
 * This class is a manager class to manage themes and provide the id of the color given by settings preferences
 * 
 * @author ATVApps
 * 
 */
public class ThemeContentManager {

	public static volatile ThemeContentManager contentManager;

	static {
		contentManager = new ThemeContentManager();
	}

	/**
	 * Method to get instance of ContentManager
	 * 
	 * @return contentManager
	 */
	public static ThemeContentManager getInstance() {
		if (contentManager == null) {
			contentManager = new ThemeContentManager();
		}
		return contentManager;
	}

	/**
	 * Hidden constructor
	 */
	private ThemeContentManager() {

	}

	/**
	 * Method to get theme
	 * 
	 * @param context
	 * @return color
	 */
	public int getTheme(Context context) {
		int color = context.getResources().getColor(
				R.color.material_deep_teal_500);
		switch (SettingsPreferences.getThemeIndex(context)) {
		case AppConstants.DEFAULT_COLOR:
			color = context.getResources().getColor(
					R.color.material_deep_teal_500);
			break;
		case AppConstants.RED:
			color = context.getResources().getColor(R.color.red);

			break;
		case AppConstants.BLUE:
			color = context.getResources().getColor(R.color.blue);

			break;
		case AppConstants.GREY:
			color = context.getResources().getColor(
					R.color.material_blue_grey_800);
			break;

		case AppConstants.BROWN:
			color = context.getResources().getColor(R.color.brown);
			break;
		case AppConstants.LIME:
			color = context.getResources().getColor(R.color.lime);
			break;
		case AppConstants.GREEN:
			color = context.getResources().getColor(R.color.material_green);
			break;
		case AppConstants.PINK:
			color = context.getResources().getColor(R.color.pink);
			break;
		case AppConstants.PURPLE:
			color = context.getResources().getColor(R.color.purple);
			break;
		case AppConstants.AMBER:
			color = context.getResources().getColor(R.color.amber);
			break;

		}
		return color;
	}

	/**
	 * Method to get floating button theme
	 * 
	 * @param context
	 * @return color
	 */
	public int getFloatingButtonLayout(Context context) {
		int layout = R.layout.main_promoted_action_button1;
		switch (SettingsPreferences.getThemeIndex(context)) {
		case 0:
			layout = R.layout.main_promoted_action_button1;
			break;
		case 1:
			layout = R.layout.main_promoted_action_button2;

			break;
		case 2:
			layout = R.layout.main_promoted_action_button3;

			break;
		case 3:
			layout = R.layout.main_promoted_action_button4;
			break;

		case 4:
			layout = R.layout.main_promoted_action_button5;
			break;
		case 5:
			layout = R.layout.main_promoted_action_button6;
			break;
		case 6:
			layout = R.layout.main_promoted_action_button7;
			break;
		case 7:
			layout = R.layout.main_promoted_action_button8;
			break;
		case 8:
			layout = R.layout.main_promoted_action_button9;
			break;
		case 9:
			layout = R.layout.main_promoted_action_button10;
			break;

		}
		return layout;
	}
}