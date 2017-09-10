package design.chat.template.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;
import java.util.List;

import design.chat.template.R;
import design.chat.template.RoundedImageView;
import design.chat.template.model.MessageItem;
import design.chat.template.model.ModelFirebase;
import design.chat.template.util.AppUtils;

/**
 * The adapter class to bind the views on list view with contents.
 * 
 * @author ATV Apps
 * 
 */
public class MessageAdapter extends BaseAdapter {

	private List<MessageItem> mDataList;
	private final Activity mContext;
	private static LayoutInflater mInflater = null;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public MessageAdapter(Activity context) {
		this.mContext = context;
		mInflater = this.mContext.getLayoutInflater();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.messages_item_layout, null);
			ViewHolder holder = new ViewHolder();
			holder.fullName = (TextView) view.findViewById(R.id.name_textview);
			holder.avatar = (RoundedImageView) view.findViewById(R.id.personal_profile_imageview);
			holder.time = (TextView) view.findViewById(R.id.time_textview);
			holder.lastMessage = (TextView) view.findViewById(R.id.message_textview);
			view.setTag(holder);
		}

		final ViewHolder viewHolder = (ViewHolder) view.getTag();

		final MessageItem item = mDataList.get(position);
		final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.load_contact_avatar_progressBar);

		Log.d("itemName",item.getName());
		viewHolder.fullName.setText(item.getName());
		viewHolder.lastMessage.setText(item.getMessage());
		viewHolder.time.setText(item.getTime());



		ModelFirebase firebaseModel=new ModelFirebase();
		progressBar.setVisibility(ProgressBar.VISIBLE);

		if(item.getAvatarBitmap()==null) {
			firebaseModel.getAvatarFromStorageAccordingToUserID(getItem(position).getUserID(), new ModelFirebase.GetImageCallback() {

				@Override
				public void onComplete(byte[] bytes) {
					viewHolder.avatar.setImageBitmap(AppUtils.getBitMapFromByteArray(bytes));
					item.setAvatarBitmap(AppUtils.getBitMapFromByteArray(bytes));
					progressBar.setVisibility(ProgressBar.GONE);
				}

				@Override
				public void onFailed(@NonNull Exception e) {
					progressBar.setVisibility(ProgressBar.GONE);
					viewHolder.avatar.setImageResource(R.drawable.avatar_default_user);
					Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.avatar_default_user);
					item.setAvatarBitmap(bitmap);
				}
			});

		}else{
			progressBar.setVisibility(ProgressBar.GONE);
			viewHolder.avatar.setImageBitmap(item.getAvatarBitmap());

		}





		// Animation

		YoYo.with(Techniques.BounceIn).duration(2000)
				.withListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
						viewHolder.avatar.setVisibility(View.VISIBLE);



					}

					@Override
					public void onAnimationEnd(Animator animation) {

					}

					@Override
					public void onAnimationCancel(Animator animation) {
						Log.d("YoYoAAA","uyuuyihgjhj");
					}

					@Override
					public void onAnimationRepeat(Animator animation) {

					}
				}).playOn(viewHolder.avatar);

		return view;
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


	/**
	 * ViewHolder class to hold the views to bind on listview.
	 *
	 * @author ATV Apps
	 *
	 */
	static class ViewHolder {
		TextView fullName;
		RoundedImageView avatar;
		TextView lastMessage;
		TextView time;
	}



}
