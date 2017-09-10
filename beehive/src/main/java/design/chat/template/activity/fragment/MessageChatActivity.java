package design.chat.template.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import design.chat.template.ProgressBarCircular;
import design.chat.template.R;
import design.chat.template.adapter.MessageChatAdapter;
import design.chat.template.manager.SoundManger;
import design.chat.template.model.MessageItem;
import design.chat.template.model.ModelFirebase;
import design.chat.template.util.AppConstants;
import design.chat.template.util.SettingsPreferences;

/**
 * The MessageChatActivity class to show  MessageChat view contains. This is message chat screen.
 * @author ATV Apps
 *
 */
public class MessageChatActivity extends Activity {

	private Context mContext;
	private InitLoadTask mInitLoadTask;
	private MessageChatAdapter mMessageChatAdapter;
	private ArrayList<MessageItem> mList;
	private ArrayList<MessageItem> tempList;
	private ProgressBarCircular progressBarCircular;
	private ListView mListView;
	private ImageView mLocationImageview;
	private TextView mUserNameTextview;
	private MessageItem mMessageItem;
	private EditText mNewMessageEdittext;
	private boolean isLocationEnabled;
	private ModelFirebase modelFirebase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_chat_layout);
		mContext = MessageChatActivity.this;
		modelFirebase=new ModelFirebase();
		Log.d("MessageChatActivity","MessageChatActivity");
		initViews();
		Bundle b = getIntent().getExtras();
		if (b != null) {
			mMessageItem = (MessageItem) b.get(AppConstants.EXTRA_MESSAGE_CHAT_ITEM);
			setUserInfo();
		}
		mMessageChatAdapter = new MessageChatAdapter(mContext);
		mList = new ArrayList<MessageItem>();

	}

	private void setBitmapAvatar(){
		byte[]byteArray=getIntent().getByteArrayExtra(AppConstants.BITMAP_AVATAR);
		Bitmap bitmap= BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
		mMessageItem.setAvatarBitmap(bitmap);
	}




	private void setContactListener(int count){
		modelFirebase.setNewMessageListener(count, mMessageItem.getUserID(), new ModelFirebase.GetNewMessageEvent() {
			@Override
			public void onChildAdded(MessageItem messageItem) {
				ArrayList<MessageItem> list = new ArrayList<MessageItem>();
				list.add(messageItem);
				if(!messageItem.isSentByMe())
					showMessage(list);
			}
		});
	}

	private void addSoundNotification(){
		try {
			SoundManger.getInstance(mContext).playSound(getAssets().openFd(SettingsPreferences.getRingtonePath(mContext)));
			SoundManger.getInstance(mContext).vibrate();
			Log.d("SettingPreferences",""+SettingsPreferences.getRingtoneIndex(mContext));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	/**
	 * Method to set user information
	 */
	private void setUserInfo() {
		mUserNameTextview.setText(mMessageItem.getName());
		modelFirebase.getMessages(mMessageItem.getUserID(), new ModelFirebase.GetMessagesCallback() {

			@Override
			public void onComplete(ArrayList<MessageItem> items) {

				tempList=items;
				if(tempList!=null)
					setContactListener(tempList.size());
				else
					setContactListener(0);

				startLoadTask();
			}
		});
	}

	private void scrollDown() {
		mListView.setSelection(mListView.getCount() - 1);
		mListView.requestFocus();
	}

	/**
	 * inits components
	 */
	private void initViews() {
		isLocationEnabled = true;
		mNewMessageEdittext = (EditText) findViewById(R.id.new_message_edittext);
		mUserNameTextview = (TextView) findViewById(R.id.user_name_textview);
		mLocationImageview = (ImageView) findViewById(R.id.location_imageview);
		progressBarCircular = (ProgressBarCircular) findViewById(R.id.msg_progress);
		mListView = (ListView) findViewById(R.id.msg_list_view);
	}

	/**
	 * Method to register click listener
	 * 
	 * @param view
	 */
	public void viewClikedHandler(View view) {
		if (view.getId() == R.id.back_imageview) {
			finish();
		} else if (view.getId() == R.id.text_parent) {

		} else if (view.getId() == R.id.camera_parent) {

		} else if (view.getId() == R.id.picture_parent) {

		} else if (view.getId() == R.id.smiley_parent) {

		} else if (view.getId() == R.id.recording_parent) {

		} else if (view.getId() == R.id.send_parent) {
			if (!TextUtils.isEmpty(mNewMessageEdittext.getText().toString())) {


				String lastMsg = (String.valueOf(mNewMessageEdittext.getText().toString()));
				ArrayList<MessageItem> list = new ArrayList<MessageItem>();
				MessageItem item = new MessageItem();
				item.setUserID(modelFirebase.getCurrentUserID());
				item.setName(SettingsPreferences.getFullName(mContext));
				item.setMessage(lastMsg);
				item.setTime(String.valueOf(Calendar.getInstance().getTime().getHours()+":"+Calendar.getInstance().getTime().getMinutes()));
				item.setAvatarBitmap(null);
				list.add(item);
				Log.d("itemAA",item.toString());
				//Toast.makeText(mContext,mMessageItem.getUserID(),Toast.LENGTH_SHORT).show();
				Log.d("settingPrefernce",SettingsPreferences.getFullName(mContext));
				modelFirebase.addMessage(mMessageItem.getUserID(),item.getName(),mMessageItem.getName(),item);
				showMessage(list);
			}
		} else if (view.getId() == R.id.location_parent) {
			isLocationEnabled = !isLocationEnabled;
			if (isLocationEnabled) {
				mLocationImageview.setBackgroundResource(R.drawable.ic_action_location);
			} else {
				mLocationImageview.setBackgroundResource(R.drawable.ic_action_location_blue);
			}
		}
	}

	/**
	 * Method to show user send message in list
	 * 
	 * @param messages
	 */
	public void showMessage(ArrayList<MessageItem> messages) {
		if (messages != null) {
			for (int i = 0; i < messages.size(); i++) {

				//if(!messages.get(0).isSentByMe())
					addSoundNotification();

				mMessageChatAdapter.add(messages);
				mMessageChatAdapter.notifyDataSetChanged();
				scrollDown();
				mNewMessageEdittext.setText("");
			}
		}
	}

	/**
	 * Method to start Fetch List load task.
	 */
	private void startLoadTask() {
		if (mInitLoadTask != null) {
			mInitLoadTask.cancel(true);
			mInitLoadTask = null;
		}
		mInitLoadTask = new InitLoadTask();
		mInitLoadTask.execute();
	}

	/**
	 * The class to load the contains List in background and shows the progress
	 * dialog.
	 * 
	 * @author ATVApps
	 * 
	 */
	public class InitLoadTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (progressBarCircular != null) {
				progressBarCircular.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				//mList = ReasourcesContentManager.getInstance().getDummyList(mContext);
				//mList.addAll(ReasourcesContentManager.getInstance().getDummyList1(mContext));
				if(tempList!=null)
					mList=tempList;

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (mContext != null) {
				super.onPostExecute(result);
				if (progressBarCircular != null) {
					progressBarCircular.setVisibility(View.GONE);
				}
				populateSearchContents();
			}
		}
	}

	/**
	 * Method to populate  content and show on screen
	 */
	private void populateSearchContents() {
	//	if (mList != null && mList.size() > 0) {
//			if (mMessageChatAdapter == null) {
//				mMessageChatAdapter = new MessageChatAdapter(mContext);
//			}
			//Collections.shuffle(mList);
		if (mList != null ) {
			mListView.setVisibility(View.VISIBLE);
			mMessageChatAdapter.setList(mList);
			mListView.setAdapter(mMessageChatAdapter);
			scrollDown();

		} else {
			mListView.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		if (mContext != null) {
			if (mInitLoadTask != null) {
				mInitLoadTask.cancel(true);
				mInitLoadTask = null;
			}
			mUserNameTextview = null;
			mLocationImageview = null;
			mMessageChatAdapter = null;
			mList = null;
			progressBarCircular = null;
			mListView = null;
			mContext = null;
			modelFirebase.removeNewMessageListener(mMessageItem.getUserID());
			super.onDestroy();
		}
	}


}
