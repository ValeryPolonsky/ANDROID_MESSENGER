package design.chat.template.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MessageItem implements Serializable {

	private String name;
	private String message;
	private String time;
	private String userID;
	private Bitmap avatar;
	private boolean userMessage;
	private boolean sentByMe;

	public MessageItem(MessageItem message) {
		setName(message.getName());
		setMessage((message.getMessage()));
		setUserID(message.getUserID());
		setAvatarBitmap(message.getAvatarBitmap());
		setTime(message.getTime());
		setSentByMe(message.isSentByMe());
		setUserMessage(message.isUserMessage());
	}

    public MessageItem() {

    }

	public void setAvatarBitmap(Bitmap avatar){
		this.avatar=avatar;
	}

	public Bitmap getAvatarBitmap(){
		return avatar;
	}


	/**
	 * @return the usermessage
	 */
	public boolean isUserMessage() {
		return userMessage;
	}

	/**
	 * @param usermessage
	 *            the usermessage to set
	 */
	public void setUserMessage(boolean usermessage) {
		this.userMessage = usermessage;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}


	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public boolean isSentByMe() {
		return sentByMe;
	}

	public void setSentByMe(boolean sentByMe) {
		this.sentByMe = sentByMe;
	}

	@Override
	public String toString(){
		String str="";
		str=str+"Name: "+name+",";
		str=str+"Time: "+time+",";
		str=str+"UserID: "+userID+",";
		str=str+"AvatarBitmap: "+avatar+",";
		str=str+"Message: "+message+",";
		return str;

	}

}