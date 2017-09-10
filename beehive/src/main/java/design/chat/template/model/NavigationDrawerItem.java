package design.chat.template.model;

/**
 * The model class to hold navigation menus
 * 
 * @author ATV Apps
 * 
 */
public class NavigationDrawerItem {

	private String title;
	private int icon;
	private int id;
	
	

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public NavigationDrawerItem() {
	}

	public NavigationDrawerItem(String title, int icon) {
		this.title = title;
		this.icon = icon;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
 	 *
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the icon
	 */
	public int getIcon() {
		return icon;
	}

	/**
	 * @param icon  the icon to set
	 *
	 */
	public void setIcon(int icon) {
		this.icon = icon;
	}

}
