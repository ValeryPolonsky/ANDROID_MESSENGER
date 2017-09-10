package design.chat.template.adapter;

import android.content.Context;
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
import design.chat.template.model.GlobalBus;
import design.chat.template.model.MessageItem;
import design.chat.template.model.ModelFirebase;
import design.chat.template.util.AppUtils;

/**
 * The adapter class to bind the views on list view with contents.
 * 
 * @author ATV Apps
 * 
 */
public class SearchAdapter extends BaseAdapter {

	private List<MessageItem> mDataList;
	private final Context mContext;
	private static LayoutInflater mInflater = null;
	private ModelFirebase firebaseModel=new ModelFirebase();

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public SearchAdapter(Context context) {
		this.mContext = context;
		mInflater = (LayoutInflater) context.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.search_item_layout, null);

			ViewHolder holder = new ViewHolder();
			holder.avatar = (RoundedImageView) view.findViewById(R.id.personal_profile_imageview);
			holder.fullName = (TextView) view.findViewById(R.id.name_textview);
			holder.add_contact = (ImageView) view.findViewById(R.id.add_contact_imageview);

			holder.add_contact.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
                    if(getItem(position).getUserID().equals(firebaseModel.getCurrentUserID())){
                        Toast.makeText(mContext,R.string.contact_failed,Toast.LENGTH_SHORT).show();
                        return;
                    }
					firebaseModel.isContactExists(getItem(position).getUserID(), new ModelFirebase.GetCallbackResult() {
						@Override
						public void onComplete(boolean flag) {
                            Log.d("OnClickz","Clicked");
							Log.d("flagA",""+flag);
							if(!flag){
								GlobalBus.getBus().post(getItem(position));
								firebaseModel.addContactToContactsList(getItem(position).getUserID());
								Toast.makeText(mContext,R.string.contact_added, Toast.LENGTH_SHORT).show();
							}
							else{
                                Toast.makeText(mContext,R.string.contact_exists,Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			});



			view.setTag(holder);
		}



		final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.load_avatar_progressBar);
		final ViewHolder viewHolder = (ViewHolder) view.getTag();
		final MessageItem item = getItem(position);

		viewHolder.fullName.setText(item.getName());


		Log.d("ItemsInSearchAdapter: ",""+item.toString());
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
	private void showLongList(final MessageItem item) {
		new MaterialDialog.Builder(mContext)
				.items(R.array.search_option_string)
				.itemsCallback(new MaterialDialog.ListCallback() {
					@Override
					public void onSelection(MaterialDialog dialog, View view,
							int which, CharSequence text) {

						switch(which){
							case 1: if(!firebaseModel.getCurrentUserID().equals(item.getUserID())){
								firebaseModel.addContactToContactsList(item.getUserID());
								GlobalBus.getBus().post(item);
								Toast.makeText(mContext,R.string.contact_added, Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(mContext,R.string.contact_failed, Toast.LENGTH_SHORT).show();
							}

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


	static class ViewHolder {
		TextView fullName;
		RoundedImageView avatar;
		ImageView popup_options;
		ImageView add_contact;
	}
}
