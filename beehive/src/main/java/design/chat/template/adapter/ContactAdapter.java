package design.chat.template.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import design.chat.template.R;
import design.chat.template.RoundedImageView;
import design.chat.template.activity.fragment.ContactsFragment;
import design.chat.template.activity.fragment.MapsActivity;
import design.chat.template.model.MessageItem;
import design.chat.template.model.ModelFirebase;
import design.chat.template.util.AppUtils;
import design.chat.template.model.Coordinates;

/**
 * The adapter class to bind the views on list view with contents.
 * 
 * @author ATV Apps
 * 
 */
public class ContactAdapter extends BaseAdapter {

	private List<MessageItem> mDataList;
	private final Context mContext;
	private static LayoutInflater mInflater = null;
	private ModelFirebase firebaseModel=new ModelFirebase();
	private ContactsFragment contactsFragment;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public ContactAdapter(Context context) {
		this.mContext = context;
		mInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Method to set the list.
	 * 
	 * @param list
	 */
	public void setList(ArrayList<MessageItem> list) {
		mDataList = list;
		notifyDataSetChanged();
	}


	public void setContactFragment(ContactsFragment contactsFragment){
		this.contactsFragment=contactsFragment;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {


		View view = convertView;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.contact_item_layout, null);
			ViewHolder holder = new ViewHolder();
			holder.avatar = (RoundedImageView) view.findViewById(R.id.contact_personal_profile_imageview);
			holder.fullName = (TextView) view.findViewById(R.id.contact_name_textview);
			holder.map = (ImageView) view.findViewById(R.id.gps_imageview);
			holder.popup_options = (ImageView) view.findViewById(R.id.contact_pop_option_imageview);

			holder.map.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					firebaseModel.getCoordinatesByID(getItem(position).getUserID(), new ModelFirebase.GetCoordinatesCallback() {
						@Override
						public void onComplete(Coordinates coordinates) {
							viewMap(coordinates);
						}
					});
				}
			});

			holder.popup_options.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showLongList(getItem(position).getUserID(),position);
				}
			});

			view.setTag(holder);

		}
		final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.load_avatar_progressBar);
		final ViewHolder viewHolder = (ViewHolder) view.getTag();
		final MessageItem item = getItem(position);

		viewHolder.fullName.setText(item.getName());

		//Ours


		if(item.getAvatarBitmap()==null) {
			progressBar.setVisibility(ProgressBar.VISIBLE);
			firebaseModel.getAvatarFromStorageAccordingToUserID(item.getUserID(), new ModelFirebase.GetImageCallback() {
				@Override
				public void onComplete(byte[] bytes) {

					viewHolder.avatar.setImageBitmap(AppUtils.getBitMapFromByteArray(bytes));
					item.setAvatarBitmap(AppUtils.getBitMapFromByteArray(bytes));
					progressBar.setVisibility(ProgressBar.GONE);
				}

				@Override
				public void onFailed(@NonNull Exception e) {
					//image.setImageResource(R.drawable.avatar_default_user);
					viewHolder.avatar.setImageResource(R.drawable.avatar_default_user);
					Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.avatar_default_user);
					item.setAvatarBitmap(bitmap);
					progressBar.setVisibility(ProgressBar.GONE);
				}
			});
		}else{

			viewHolder.avatar.setImageBitmap(item.getAvatarBitmap());
		}

		return view;
	}
	
	/**
	 * Method to show long list option dialog
	 */
	private void showLongList(final String userID, final int position) {
		new MaterialDialog.Builder(mContext).items(R.array.contacts_option_string).itemsCallback(new MaterialDialog.ListCallback() {
					@Override
					public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

						switch (which){
							case 1:
								Log.d("mDataList","Length is: "+mDataList.size());

                                contactsFragment.removeContactFromList(position);
								firebaseModel.removeContact(userID);
								Toast.makeText(mContext,R.string.contact_removed, Toast.LENGTH_SHORT).show();
						}
					}
				}).positiveText(android.R.string.ok).show();
	}

	@Override
	public int getCount() {
		if (mDataList != null) {
			return mDataList.size();
		}
		return 0;
	}

	@Override
	public MessageItem getItem(int postion) {
		if (mDataList != null) {
			return mDataList.get(postion);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	private void viewMap(Coordinates coordinates){

		Intent intent = new Intent(mContext, MapsActivity.class);
		intent.putExtra("coordinates",coordinates);
		mContext.startActivity(intent);
	}


	/**
	 * ViewHolder class to hold the views to bind on listview.
	 *
	 * @author ATV Apps
	 *
	 */
	static class ViewHolder {
		TextView fullName;
		RoundedImageView avatar;
		ImageView map;
		ImageView popup_options;
	}



}




