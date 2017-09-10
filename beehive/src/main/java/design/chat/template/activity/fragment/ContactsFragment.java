package design.chat.template.activity.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import design.chat.template.ProgressBarCircular;
import design.chat.template.PromotedActionsLibrary;
import design.chat.template.R;
import design.chat.template.RoundedImageView;
import design.chat.template.model.User;
import design.chat.template.adapter.ContactAdapter;
import design.chat.template.manager.ThemeContentManager;
import design.chat.template.model.GlobalBus;
import design.chat.template.model.MessageItem;
import design.chat.template.model.ModelFirebase;
import design.chat.template.util.AppConstants;
import design.chat.template.util.AppUtils;
import design.chat.template.util.SettingsPreferences;

//import android.support.v7.widget.SwitchCompat;

/**
 * The ContactsFragment class to show ContactsFragment view contains. This show contacts list.
 * @author ATV Apps
 * 
 */
public class ContactsFragment extends Fragment {

	private View rootView;
	private int position;
	private Context mContext;
	private ListView mListView;
	private TextView mUserName;
	private FrameLayout mFrameLayout;
	private ContactAdapter mContactAdapter;
	private ArrayList<MessageItem> mList;
	private ArrayList<MessageItem> tempList;
	private RoundedImageView mUserImageview;
	private InitContentTask mInitContentTask;
	private ProgressBarCircular progressBarCircular;
	private static final String ARG_POSITION = "position";
	private PromotedActionsLibrary promotedActionsLibrary;//Design of button (Rotation and item added)
	private ModelFirebase firebaseModel;

	public static ContactsFragment newInstance(int position) {
		ContactsFragment f = new ContactsFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}


	@Subscribe
	public void getMessage(MessageItem item) {
		//Toast.makeText(mContext,"Received data",Toast.LENGTH_SHORT).show();
		//mList.add(item);
		//mList.notify();
		tempList.add(item);
		startLoadTask();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		GlobalBus.getBus().register(this);
		rootView = inflater.inflate(R.layout.contcats_layout, container, false);
		firebaseModel = new ModelFirebase();
		progressBarCircular = (ProgressBarCircular) rootView.findViewById(R.id.online_progress);
		mListView = (ListView) rootView.findViewById(R.id.online_list_view);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> sdapterView, View view, int position, long arg3) {

				MessageItem item = mContactAdapter.getItem(position);
				Intent intent = new Intent(mContext, MessageChatActivity.class);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				Bitmap bitmap;
				byte[] byteArray=null;
				if(item.getAvatarBitmap()!=null) {
					bitmap = item.getAvatarBitmap();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					byteArray = stream.toByteArray();
				}
				item.setAvatarBitmap(null);
				intent.putExtra(AppConstants.EXTRA_MESSAGE_CHAT_ITEM, item);
				intent.putExtra(AppConstants.BITMAP_AVATAR, byteArray);
				startActivity(intent);
			}
		});
		position++;
		mUserImageview = (RoundedImageView) rootView.findViewById(R.id.personal_profile_imageview);
		firebaseModel.getAvatarFromStorage(new ModelFirebase.GetImageCallback() {
			@Override
			public void onComplete(byte[] bytes) {
				mUserImageview.setImageBitmap(AppUtils.getBitMapFromByteArray(bytes));
			}

			@Override
			public void onFailed(@NonNull Exception e) {

				mUserImageview.setImageResource(R.drawable.avatar_default_user);
			}
		});
		mUserName = (TextView) rootView.findViewById(R.id.name_textview);
        mUserName.setText(SettingsPreferences.getFullName(getContext()));


		getContactsList();

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		setFloatingButton();
	}

	/**
	 * Init components
	 */
	private void initView() {
		mFrameLayout = (FrameLayout) rootView.findViewById(R.id.online_container);
		promotedActionsLibrary = new PromotedActionsLibrary();
		promotedActionsLibrary.setup(getActivity(), mFrameLayout);
		mActionBroadcastReceiver = new ActionBrodcastListener();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstants.UPDATE_BACKGROUND_THEME);
		getActivity().registerReceiver(mActionBroadcastReceiver, intentFilter);
	}




	/**
	 * Method to initialize floating buttons
	 */
	private void setFloatingButton() {
		//PromotedActionsLibrary promotedActionsLibrary = new PromotedActionsLibrary();

		//promotedActionsLibrary.setup(getActivity(), mFrameLayout);

		View.OnClickListener onSearchClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(mContext, SearchActivity.class));
			}
		};

		promotedActionsLibrary.addItem(
				getResources().getDrawable(R.drawable.ic_action_search),
				onSearchClickListener, ThemeContentManager.getInstance()
						.getFloatingButtonLayout(getActivity()));

		promotedActionsLibrary.addMainItem(
				getResources().getDrawable(R.drawable.plus),
				ThemeContentManager.getInstance().getFloatingButtonLayout(
						getActivity()));
	}

	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	/**
	 * This method used to start the data task
	 */
	public void startLoadTask() {
		if (mInitContentTask != null) {
			mInitContentTask.cancel(true);
			mInitContentTask = null;
		}
		mInitContentTask = new InitContentTask();
		mInitContentTask.execute();
	}

	/**
	 * Class to load data
	 * 
	 * @author ATV Apps
	 * 
	 */



	private void getContactsList() {

		tempList=new ArrayList<MessageItem>();
		firebaseModel.getContactsID(new ModelFirebase.GetContactsCallback() {
			@Override
			public void onComplete(String[] contacts) {

				final int length=contacts.length;
				for (int i = 0; i < length; i++) {

					firebaseModel.getUserByID(contacts[i], new ModelFirebase.GetUserByIDCallback() {

						@Override
						public void onComplete(User user) {
							MessageItem item = new MessageItem();
							item.setUserID(user.getUserID());
							item.setName(user.getFirstName() + " " + user.getLastName());
							item.setMessage("message");
							item.setTime("12:00");
							item.setUserMessage(true);
							tempList.add(item);
							startMyTask(length);
						}

					});

				}
			}
		});

	}


	//Validates that the whole list has been received
	private void startMyTask(int length)
	{
		if(tempList.size()==length){
			Log.d("startLoadTask","I'm inside of startMyTask()");
			startLoadTask();
		}
	}


	//Runs the given list of contacts
	public class InitContentTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				mList=tempList;
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (progressBarCircular != null) {
				progressBarCircular.setVisibility(View.GONE);
			}
			populateContents();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (progressBarCircular != null) {
				progressBarCircular.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * Method to populate content and show on screen
	 */
	private void populateContents() {
		if (mList != null && mList.size() > 0) {
			if (mContactAdapter == null) {
				mContactAdapter = new ContactAdapter(getActivity());
				mContactAdapter.setContactFragment(ContactsFragment.this);
			}
			//mListView.setVisibility(View.VISIBLE);
			mContactAdapter.setList(mList);
			mListView.setAdapter(mContactAdapter);
		}
	}


	public void removeContactFromList(int position){
		tempList.remove(position);
		startLoadTask();

		//mList=tempList;
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
				promotedActionsLibrary.ClearButton();
				setFloatingButton();
			}
		}
	}

	@Override
	public void onDestroy() {
		if (mInitContentTask != null) {
			mInitContentTask.cancel(true);
			mInitContentTask = null;
		}
		if (mActionBroadcastReceiver != null) {
			getActivity().unregisterReceiver(mActionBroadcastReceiver);
			mActionBroadcastReceiver = null;
		}
		progressBarCircular = null;
		mList = null;
		mContactAdapter = null;
		mListView = null;
		mFrameLayout = null;
		rootView = null;
		mContext = null;
		super.onDestroy();
	}
}