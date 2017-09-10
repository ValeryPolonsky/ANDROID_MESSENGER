/**
 * 
 */
package design.chat.template.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * The class to write and read the preference data
 * 
 * @author ATV Apps
 * 
 */
public class SettingsPreferences  {

	private static final String CHAT_PREF = "chat_pref";
	private static final String ISLOGIN = "islogin";
	private static final String ISFIRSTTIME = "isfirsttime";
	private static final String THEME_INDEX = "themeindex";
	private static final String USERINFO = "user_info";
	private static final String FIRSTNAME = "firstname";
	private static final String LASTNAME = "lastname";
	private static final String USERNAME = "username";
	private static final String ISSOUND = "issound";
	private static final String ISVIBRATE = "isvibrate";
	private static final String ISNOTIFICATION = "isnotification";
	private static final String RINGTONENAME = "ringtonename";
	private static final String RINGTONEINDEX = "ringtoneindex";
	private static final String RINGTONEPATH="ringtonepath";
	/**
	 * Method to set login
	 * 
	 * @param context
	 * @param position
	 */
	public static void seThemeIndex(Context context, int position) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putInt(THEME_INDEX, position);
		prefEditor.commit();
	}

	/**
	 * Method to get ThemeIndex
	 * 
	 * @param context
	 * @return
	 */
	public static int getThemeIndex(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		return prefs.getInt(THEME_INDEX, AppConstants.DEFAULT_THEME_COLOR);
	}

	/**
	 * Method to set Login
	 * 
	 * @param context
	 * @param status
	 */
	public static void setLogin(Context context, boolean status) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(ISLOGIN, status);
		prefEditor.commit();
	}

	public static void setNotification(Context context, boolean status) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(ISNOTIFICATION, status);
		prefEditor.commit();
	}


	public static void setSound(Context context, boolean status) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(ISSOUND, status);
		prefEditor.commit();
	}

	public static boolean isSound(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		return prefs.getBoolean(ISSOUND, false);
	}

	public static boolean isNotification(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		return prefs.getBoolean(ISNOTIFICATION, false);
	}


	public static void setVibrate(Context context, boolean status) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(ISVIBRATE, status);
		prefEditor.commit();
	}



	public static boolean isVibrate(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		return prefs.getBoolean(ISVIBRATE, false);
	}





	public static boolean contains(Context context,String element){
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		return prefs.contains(element);
	}



	/**
	 * Method to set ThemeIndex
	 *
	 * @param context
	 * @param userName
	 */
	public static void setNewUserInfo(Context context,String firstName,String lastName,String userName) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putString(FIRSTNAME,firstName);
		prefEditor.putString(LASTNAME,lastName);
		prefEditor.putString(USERNAME,userName);
		prefEditor.commit();
	}

	public static void setFullName(Context context,String firstName,String lastName) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putString(FIRSTNAME,firstName);
		prefEditor.putString(LASTNAME,lastName);
		prefEditor.commit();
	}

	public static void setRingtoneIndex(Context context,int index) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putInt(RINGTONEINDEX,index);
		prefEditor.commit();
	}

	public static void setRingtoneName(Context context,String ringtoneName) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putString(RINGTONENAME,ringtoneName);
		prefEditor.commit();
	}

	public static void setRingtonePath(Context context,String ringtoneName){
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putString(RINGTONEPATH,ringtoneName);
		prefEditor.commit();
	}

	/**
	 * Method to check is login
	 *
	 * @param context
	 * @return true if logged in,else false
	 */
	public static boolean isLogin(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		return prefs.getBoolean(ISLOGIN, false);
	}


	/**
	 * Method to return element from SharedPreference
	 * @param context
	 * @param key
	 * @return the element
	 */
	public static String getElement(Context context,String key){
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		return prefs.getString(key,"");
	}

	public static int getRingtoneIndex(Context context){
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		return prefs.getInt(RINGTONEINDEX,0);
	}

	public static String getRingtoneName(Context context){
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		return prefs.getString(RINGTONENAME,"Ringtone 1");
	}


	public static String getFirstName(Context context){
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		return prefs.getString(FIRSTNAME,"Unknown");
	}

	public static String getLastName(Context context){
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		return prefs.getString(LASTNAME,"Unknown");
	}

	public static String getUserName(Context context){
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		return prefs.getString(USERNAME,"Unknown");
	}

	public static String getFullName(Context context){
		return getFirstName(context) + " " + getLastName(context);
	}

	public static String getRingtonePath(Context context){
		SharedPreferences prefs = context.getSharedPreferences(CHAT_PREF, Context.MODE_PRIVATE);
		return prefs.getString(RINGTONEPATH,"Unknown");
	}




	public static String getChatPref() {
		return CHAT_PREF;
	}

	public static String getISLOGIN() {
		return ISLOGIN;
	}

	public static String getISFirstTime() {
		return ISFIRSTTIME;
	}

	public static String getThemeIndex() {
		return THEME_INDEX;
	}

	public static String getUSERINFO() {
		return USERINFO;
	}




}