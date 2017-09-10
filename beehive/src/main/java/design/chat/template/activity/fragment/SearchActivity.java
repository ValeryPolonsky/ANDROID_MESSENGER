package design.chat.template.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import design.chat.template.ProgressBarCircular;
import design.chat.template.R;
import design.chat.template.model.User;
import design.chat.template.adapter.SearchAdapter;
import design.chat.template.model.MessageItem;
import design.chat.template.model.ModelFirebase;
import design.chat.template.util.AppConstants;

/**
 * The SearchActivity class to show SearchActivity view contains. This
 * show search friends list.
 * 
 * @author ATV Apps
 * 
 */
public class SearchActivity extends Activity {

	private Context mContext;
	private EditText mSearchEditBox;
	private ImageView mSearchImageview;
	private ImageView mClearImageview;
	private SearchTask mSearchTask;
	private SearchAdapter mSearchAdapter;
	private ArrayList<MessageItem> mSearchItemsList;
	private ArrayList<MessageItem> mList;
	private ProgressBarCircular progressBarCircular;
	private ListView mListView;
	private ModelFirebase modelFirebase;
	private boolean searchDataHasBeenUpdated=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);
		mContext = SearchActivity.this;
		modelFirebase = new ModelFirebase();
		initViews();
	}

	/**
	 * inits components
	 */
	private void initViews() {
		mSearchEditBox = (EditText) findViewById(R.id.search_edittext);
		mSearchImageview= (ImageView) findViewById(R.id.search_imageview);
		mClearImageview = (ImageView) findViewById(R.id.cancel_imageview);
		progressBarCircular = (ProgressBarCircular) findViewById(R.id.msg_progress);
		mListView = (ListView) findViewById(R.id.msg_list_view);

		mSearchEditBox.setOnKeyListener(new EditText.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				//This is the filter
				if (event.getAction()!=KeyEvent.ACTION_DOWN)
					return true;

				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					Log.d("KEYCODE_ENTER","KEYCODE_ENTER");
					mSearchString = mSearchEditBox.getText().toString();
					if (!TextUtils.isEmpty(mSearchString) && mSearchString.length() > 0) {
						getSearchData();
					}
				}
				if(keyCode == KeyEvent.KEYCODE_BACK){
					onBackPressed();
				}
				return true;
			}
		});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				showLongList();
				return false;
			}
		});
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> sdapterView, View view,
//					int position, long arg3) {
//
//				MessageItem item = mSearchAdapter.getItem(position);
//				Intent intent = new Intent(mContext, MessageChatActivity.class);
//				intent.putExtra(AppConstants.EXTRA_MESSAGE_CHAT_ITEM, item);
//				startActivity(intent);
//			}
//		});
		mClearImageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(mSearchEditBox.getText().toString().equals("")) {
					finish();
				}else{
					mSearchEditBox.setText("");
				}
			}
		});
	}

	private void getSearchData(){

		Log.d("getSearchDataAA","getSearchData()");
		searchDataHasBeenUpdated=true;
		modelFirebase.searchUsersByName(mSearchString,new ModelFirebase.GetAllUsersCallback() {
			@Override
			public void onComplete(List<User> users) {
				mList=getSearchList(users);
				if(mList.size()==0){
					Toast.makeText(mContext, "No users found with that name", Toast.LENGTH_SHORT).show();
				}
				else{
					startSearchTask();
				}
			}
			@Override
			public void onCancel() {

			}
		});
	}

	private ArrayList<MessageItem> getSearchList(List<User> users) {
		ArrayList<MessageItem> list = new ArrayList<MessageItem>();
		for (int i = 0; i < users.size(); i++) {
			MessageItem item = new MessageItem();
			item.setName(users.get(i).getFirstName()+" "+users.get(i).getLastName());
			item.setMessage("Lorem pursur hey hey");
			item.setTime("12:00");
			item.setUserID(users.get(i).getUserID());
			item.setUserMessage(true);
			list.add(item);
		}
		return list;
	}
	
	/**
	 * Method to show long list option dialog
	 */
	private void showLongList() {
        new MaterialDialog.Builder(this)
                .title(R.string.conversation)
                .items(R.array.message_string)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Toast.makeText(mContext, which + ":" + text, Toast.LENGTH_SHORT).show();
                    }
                })
                .positiveText(android.R.string.ok)
                .show();
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Toast.makeText(mContext, keyCode, Toast.LENGTH_SHORT).show();
		return super.onKeyDown(keyCode, event);
	}


	private String mSearchString;

	/** Text Watcher for input text edit on EditText */
	TextWatcher mEdittextWatcher = new TextWatcher() {

		public void onTextChanged(CharSequence inputText, int start, int before, int count) {

			if (!TextUtils.isEmpty(mSearchString) && mSearchString.length() > 0) {
				mClearImageview.setVisibility(View.VISIBLE);
				mSearchImageview.setVisibility(View.GONE);
			} else {
				mSearchImageview.setVisibility(View.VISIBLE);
				mClearImageview.setVisibility(View.GONE);
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void afterTextChanged(Editable s) {
		}
	};
	/**
	 * Handler to post message for delay
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			mHandler.removeMessages(1);
			//startSearchTask();

		}
	};

	/**
	 * Method to start Fetch List load task.
	 */
	private void startSearchTask() {
		if (mSearchTask != null) {
			mSearchTask.cancel(true);
			mSearchTask = null;
		}
		mSearchTask = new SearchTask();
		mSearchTask.execute();
	}

	/**
	 * The class to load the contains List in background and shows the progress
	 * dialog.
	 * 
	 * @author ATVApps
	 * 
	 */
	public class SearchTask extends AsyncTask<Void, Void, Boolean> {

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
				Log.d("SearchTaskAA","SearchTask");
				mSearchItemsList=mList;

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

				if(searchDataHasBeenUpdated==true) {
					populateSearchContents();
					searchDataHasBeenUpdated=false;
				}
			}
		}
	}

	/**
	 * Method to populate search content
	 */
	private void populateSearchContents() {
		//Toast.makeText(mContext,"populateSearchContents()",Toast.LENGTH_SHORT).show();
		if (mSearchItemsList != null && mSearchItemsList.size() > 0) {
			if (mSearchAdapter == null) {
				mSearchAdapter = new SearchAdapter(mContext);
			}
			mListView.setVisibility(View.VISIBLE);
			mSearchAdapter.setList(mSearchItemsList);
			mListView.setAdapter(mSearchAdapter);

		} else {
			mListView.setVisibility(View.GONE);
		}
	}

	/**
	 * Method to get search list
	 * 
	 * @param arrayList
	 * @param searchString
	 * @return searchList
	 */
	private ArrayList<MessageItem> getSearchList1(
			ArrayList<MessageItem> arrayList, String searchString) {
		ArrayList<MessageItem> searchList = null;
		if (arrayList != null && arrayList.size() > 0) {
			String searchText = "";
			searchList = new ArrayList<MessageItem>();
			for (int i = 0; i < arrayList.size(); i++) {
				searchText = arrayList.get(i).getName();
				searchText = searchText.toLowerCase();
				if (!TextUtils.isEmpty(searchText)) {
					if (searchText.contains(mSearchString)) {
						MessageItem item = new MessageItem();
						item.setName(arrayList.get(i).getName());
						searchList.add(item);
					}
				}
			}
		}
		return searchList;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onDestroy() {
		if (mContext != null) {
			if (mSearchTask != null) {
				mSearchTask.cancel(true);
				mSearchTask = null;
			}
			mSearchAdapter = null;
			mSearchItemsList = null;
			mList = null;
			progressBarCircular = null;
			mListView = null;
			mSearchEditBox = null;
			mClearImageview = null;
			mContext = null;
			super.onDestroy();
		}
	}
}
