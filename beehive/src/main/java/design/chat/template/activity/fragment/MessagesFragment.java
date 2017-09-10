package design.chat.template.activity.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import design.chat.template.ProgressBarCircular;
import design.chat.template.PromotedActionsLibrary;
import design.chat.template.R;
import design.chat.template.adapter.MessageAdapter;
import design.chat.template.manager.ThemeContentManager;
import design.chat.template.model.MessageItem;
import design.chat.template.model.ModelFirebase;
import design.chat.template.util.AppConstants;
import design.chat.template.util.AppUtils;

/**
 * The MessagesFragment class to show MessagesFragment view contains. This
 * contains new messages list.
 * 
 * @author ATV Apps
 * 
 */
public class MessagesFragment extends Fragment {

	private static final String ARG_POSITION = "position";
	private InitContentTask mInitContentTask;
	private ProgressBarCircular progressBarCircular;
	private View rootView;
	private ArrayList<MessageItem> mList;
	private MessageAdapter mMessageAdapter;
	private ListView mListView;
	private Context mContext;
	private FrameLayout mFrameLayout;
	private PromotedActionsLibrary promotedActionsLibrary;
	private ModelFirebase modelFirebase;
	private ArrayList<MessageItem>tempList;

	public static MessagesFragment newInstance(int position) {
		MessagesFragment f = new MessagesFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


		rootView = inflater.inflate(R.layout.messages_layout, container, false);
		progressBarCircular = (ProgressBarCircular) rootView.findViewById(R.id.msg_progress);
		mListView = (ListView) rootView.findViewById(R.id.msg_list_view);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				showLongList();
				return false;
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> sdapterView, View view, int position, long arg3) {
				Bundle b = new Bundle();
				MessageItem item = mMessageAdapter.getItem(position);
				Intent intent = new Intent(mContext, MessageChatActivity.class);
				item.setAvatarBitmap(null);
				b.putSerializable(AppConstants.EXTRA_MESSAGE_CHAT_ITEM, item);
				intent.putExtras(b);
				startActivity(intent);
			}
		});


		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		modelFirebase = new ModelFirebase();
		modelFirebase.getLastMessageItem(new ModelFirebase.GetLastMessageEvent() {
			@Override
			public void onComplete(ArrayList<MessageItem> items) {
				for(int i=0;i<items.size();i++){
					Log.d("itemNameA",items.get(i).getName());
				}

				tempList=items;
				initView();
				startLoadTask();
			}
		});

	}

	/**
	 * Init Components
	 */
	private void initView() {
		mFrameLayout = (FrameLayout) rootView.findViewById(R.id.message_container);
		//promotedActionsLibrary = new PromotedActionsLibrary();
		//promotedActionsLibrary.setup(getActivity(), mFrameLayout);
		mActionBroadcastReceiver = new ActionBrodcastListener();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstants.UPDATE_BACKGROUND_THEME);
		getActivity().registerReceiver(mActionBroadcastReceiver, intentFilter);
	}

	/**
	 * Method to show long list option dialog
	 */
	private void showLongList() {
		new MaterialDialog.Builder(getActivity()).title(R.string.conversation)
				.items(R.array.message_string)
				.itemsCallback(new MaterialDialog.ListCallback() {
					@Override
					public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
						Toast.makeText(getActivity(), which + ": " + text,Toast.LENGTH_SHORT).show();
					}
				}).positiveText(android.R.string.ok).show();
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

		}
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
			if (mMessageAdapter == null) {
				mMessageAdapter = new MessageAdapter(getActivity());
			}
			//Collections.shuffle(mList);
			mListView.setVisibility(View.VISIBLE);
			mMessageAdapter.setList(mList);
			mListView.setAdapter(mMessageAdapter);
			//mListView.setRefreshing(true);
			//mHandler.sendEmptyMessage(1);
		}
	}

	@Override
	public void onDestroy() {
		if (mInitContentTask != null) {
			mInitContentTask.cancel(true);
			mInitContentTask = null;
		}
//		if (mHandler != null) {
//			mHandler.removeMessages(1);
//			mHandler.removeMessages(2);
//			mHandler = null;
//		}
		if (mActionBroadcastReceiver != null) {
			getActivity().unregisterReceiver(mActionBroadcastReceiver);
			mActionBroadcastReceiver = null;
		}
		progressBarCircular = null;
		mList = null;
		mMessageAdapter = null;
		mListView = null;
		mFrameLayout = null;
		rootView = null;
		mContext = null;
		super.onDestroy();
	}

}