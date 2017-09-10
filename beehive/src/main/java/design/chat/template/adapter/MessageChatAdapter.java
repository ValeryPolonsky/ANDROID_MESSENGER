package design.chat.template.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import design.chat.template.R;
import design.chat.template.RoundedImageView;
import design.chat.template.model.MessageItem;
import design.chat.template.model.ModelFirebase;
import design.chat.template.util.AppUtils;

/**
 * 
 * This adapter class to bind the views on the list.
 * 
 * @author ATV Apps
 * 
 */
public class MessageChatAdapter extends BaseAdapter {

	/** Context object */
	private Context mContext;

	/** Holds Layout Inflater to inflate list item. */
	private LayoutInflater mLayoutInflator;

	/** Holds the list */
	private ArrayList<MessageItem> mListItems = new ArrayList<MessageItem>();

	private ModelFirebase firebaseModel;
	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public MessageChatAdapter(Context context) {
		super();
		mContext = context;
		mLayoutInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		firebaseModel = new ModelFirebase();
	}

	/**
	 * Method to set the list.
	 * 
	 * @param list
	 */
	public void setList(ArrayList<MessageItem> list) {
		mListItems = list;
		notifyDataSetChanged();
	}

	/**
	 * Method to add chat message in list
	 * @param message
	 */
	public void add(MessageItem message) {
		mListItems.add(message);
	}

	/**
	 * Method to add chat message in list
	 * @param ArrayList<MessageItem>
	 */
	public void add(ArrayList<MessageItem> messages) {
		mListItems.addAll(messages);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = mLayoutInflator.inflate(R.layout.messages_chat_item_layout,
					null);
			ViewHolder holder = new ViewHolder();
			holder.left_message = (TextView) view.findViewById(R.id.txtMessage_left);
			holder.right_message = (TextView) view.findViewById(R.id.txtMessage_right);
			holder.left_time = (TextView) view.findViewById(R.id.time_left);
			holder.right_time = (TextView) view.findViewById(R.id.time_right);
			holder.left_user_imageview = (RoundedImageView) view.findViewById(R.id.userImage_left);
			holder.right_user_imageview = (RoundedImageView) view.findViewById(R.id.userImage_right);
			holder.left_mms_imageview = (ImageView) view.findViewById(R.id.picture_imageview_left);
			holder.right_mms_imageview = (ImageView) view.findViewById(R.id.picture_imageview_right);
			holder.left_message_view = (RelativeLayout) view.findViewById(R.id.content_left);
			holder.right_message_view = (RelativeLayout) view.findViewById(R.id.content_right);
			view.setTag(holder);
		}

		final MessageItem item = getItem(position);
		final ViewHolder viewHolder = (ViewHolder) view.getTag();
		if (viewHolder != null && item != null) {
			if (item.isUserMessage()) {
				viewHolder.left_message_view.setVisibility(View.VISIBLE);
				viewHolder.right_message_view.setVisibility(View.GONE);
				viewHolder.left_message.setText(item.getMessage());
				viewHolder.left_time.setText(item.getTime());

				if(item.getAvatarBitmap()==null) {
					firebaseModel.getAvatarFromStorageAccordingToUserID(item.getUserID(), new ModelFirebase.GetImageCallback() {
						@Override
						public void onComplete(byte[] bytes) {
							viewHolder.left_user_imageview.setImageBitmap(AppUtils.getBitMapFromByteArray(bytes));
							item.setAvatarBitmap(AppUtils.getBitMapFromByteArray(bytes));
						}

						@Override
						public void onFailed(@NonNull Exception e) {
							viewHolder.left_user_imageview.setImageResource(R.drawable.avatar_default_user);
							Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.avatar_default_user);
							item.setAvatarBitmap(bitmap);
						}
					});
				}else{
					viewHolder.left_user_imageview.setImageBitmap(item.getAvatarBitmap());
				}

			} else {
				viewHolder.left_message_view.setVisibility(View.GONE);
				viewHolder.right_message_view.setVisibility(View.VISIBLE);
				viewHolder.right_message.setText(item.getMessage());
				viewHolder.right_time.setText(item.getTime());


				if(item.getAvatarBitmap()==null) {
					firebaseModel.getAvatarFromStorageAccordingToUserID(firebaseModel.getCurrentUserID(), new ModelFirebase.GetImageCallback() {
						@Override
						public void onComplete(byte[] bytes) {
							viewHolder.right_user_imageview.setImageBitmap(AppUtils.getBitMapFromByteArray(bytes));
							item.setAvatarBitmap(AppUtils.getBitMapFromByteArray(bytes));
						}

						@Override
						public void onFailed(@NonNull Exception e) {
							viewHolder.right_user_imageview.setImageResource(R.drawable.avatar_default_user);
							Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.avatar_default_user);
							item.setAvatarBitmap(bitmap);

						}
					});
				}else{
					viewHolder.right_user_imageview.setImageBitmap(item.getAvatarBitmap());
				}
			}
		}
		return view;
	}

	@Override
	public int getCount() {
		if (mListItems != null) {
			return mListItems.size();
		}
		return 0;
	}

	@Override
	public MessageItem getItem(int postion) {
		if (mListItems != null) {
			return mListItems.get(postion);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * ViewHolder class to hold the views to bind on listview.
	 * 
	 * @author ATV Apps
	 * 
	 */
	static class ViewHolder {
		TextView left_message;
		TextView right_message;
		TextView left_time;
		TextView right_time;
		RoundedImageView left_user_imageview;
		RoundedImageView right_user_imageview;
		ImageView left_mms_imageview;
		ImageView right_mms_imageview;
		RelativeLayout left_message_view;
		RelativeLayout right_message_view;
	}

	/**
	 * 
	 * For Freeing up the resources
	 */
	public void cleanUp() {
		mListItems = null;
		mLayoutInflator = null;
		mContext = null;
	}

}
