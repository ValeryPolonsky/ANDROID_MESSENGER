package design.chat.template.activity.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import java.io.IOException;
import design.chat.template.PromotedActionsLibrary;
import design.chat.template.R;
import design.chat.template.RoundedImageView;
import design.chat.template.manager.SoundManger;
import design.chat.template.manager.ThemeContentManager;
import design.chat.template.model.ModelFirebase;
import design.chat.template.util.AppConstants;
import design.chat.template.util.AppUtils;
import design.chat.template.util.SettingsPreferences;

/**
 * The SettingsFragment class to show SettingsFragment view contains. This
 * show app settings.
 * 
 * @author ATV Apps
 * 
 */
public class SettingsFragment extends Fragment {

	private static final int GALLERY_REQUEST = 1;
	private static final int REQUEST_IMAGE_CAPTURE = 111;
	private ModelFirebase firebaseModel;
	private static final String ARG_POSITION = "position";
	private View rootView;
	private int position;
	private FrameLayout mFrameLayout;
	private RoundedImageView mUserImageview;
	private RelativeLayout mLogoutView;
	private RelativeLayout mHelpParent;
	private RelativeLayout mLocationParent;
	private RelativeLayout mRingtoneParent;
	private RelativeLayout mVibrateParent;
	private RelativeLayout mSoundParent;
	private RelativeLayout mNotificationParent;
	private RelativeLayout mPrivacyParent;
	private RelativeLayout mReportProblemParent;
	private RelativeLayout mTermofServiceParent;
	private RelativeLayout mLicensesParent;
	private TextView mUserName;
	private TextView mRingtoneSelectedTextview;
	private CheckBox mSoundCheckbox;
	private CheckBox mVibrateCheckbox;
	private CheckBox mNotificationCheckbox;
	private PromotedActionsLibrary promotedActionsLibrary;



	public static SettingsFragment newInstance(int position) {
		SettingsFragment f = new SettingsFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		position++;
		rootView = inflater.inflate(R.layout.settings_layout, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		firebaseModel = new ModelFirebase();
		initView();
		setFloatingButton();
	}

	/**
	 * Inits components
	 */
	private void initView() {

		mFrameLayout = (FrameLayout) rootView.findViewById(R.id.settings_container);
		promotedActionsLibrary = new PromotedActionsLibrary();
		promotedActionsLibrary.setup(getActivity(), mFrameLayout);
		mActionBroadcastReceiver = new ActionBrodcastListener();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstants.UPDATE_BACKGROUND_THEME);
		getActivity().registerReceiver(mActionBroadcastReceiver, intentFilter);
		mReportProblemParent = (RelativeLayout) getActivity().findViewById(R.id.report_problem_parent);
		mTermofServiceParent = (RelativeLayout) getActivity().findViewById(R.id.term_of_service_parent);
		mLicensesParent = (RelativeLayout) getActivity().findViewById(R.id.licenses_parent);
		mPrivacyParent = (RelativeLayout) getActivity().findViewById(R.id.privacy_parent);
		mNotificationParent = (RelativeLayout) getActivity().findViewById(R.id.notification_parent);
		mVibrateParent = (RelativeLayout) getActivity().findViewById(R.id.vibrate_parent);
		mSoundParent = (RelativeLayout) getActivity().findViewById(R.id.sound_parent);
		mRingtoneParent = (RelativeLayout) getActivity().findViewById(R.id.ringtone_parent);
		mHelpParent = (RelativeLayout) getActivity().findViewById(R.id.help_parent);
		mLocationParent = (RelativeLayout) getActivity().findViewById(R.id.location_parent);
		mLogoutView = (RelativeLayout) getActivity().findViewById(R.id.logout_view);
		mUserImageview = (RoundedImageView) getActivity().findViewById(R.id.settings_personal_profile_imageview);
		mUserImageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				pickProfileImage();
			}
		});
		mRingtoneSelectedTextview = (TextView) getActivity().findViewById(R.id.ringtone_selected_textview);
		mSoundCheckbox = (CheckBox) getActivity().findViewById(R.id.sound_cb_imageview);
		mVibrateCheckbox = (CheckBox) getActivity().findViewById(R.id.vibrate_cb_imageview);
		mNotificationCheckbox = (CheckBox) getActivity().findViewById(R.id.notification_cb_imageview);
		mSoundCheckbox.setChecked(SettingsPreferences.isSound(getContext()));
		mVibrateCheckbox.setChecked(SettingsPreferences.isVibrate(getContext()));
		mNotificationCheckbox.setChecked(SettingsPreferences.isNotification(getContext()));
		mRingtoneSelectedTextview.setText(SettingsPreferences.getRingtoneName(getContext()));


		mVibrateCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				SoundManger.getInstance(getContext()).setVibrate(b);
				SettingsPreferences.setVibrate(getContext(),b);
				SoundManger.getInstance(getContext()).vibrate(400);
			}
		});

		mNotificationCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				SettingsPreferences.setNotification(getContext(),b);
			}
		});



		mSoundCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				SoundManger.getInstance(getContext()).setSound(b);
				SettingsPreferences.setSound(getContext(),b);
			}
		});

		final ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.settings_load_avatar_progressBar);
		progressBar.setVisibility(rootView.VISIBLE);
		firebaseModel.getAvatarFromStorage(new ModelFirebase.GetImageCallback() {
			@Override
			public void onComplete(byte[] bytes) {

				mUserImageview.setImageBitmap(AppUtils.getBitMapFromByteArray(bytes));
				progressBar.setVisibility(rootView.GONE);
			}

			@Override
			public void onFailed(@NonNull Exception e) {
				mUserImageview.setImageResource(R.drawable.avatar_default_user);
				progressBar.setVisibility(rootView.GONE);
			}
		});


		mUserName = (TextView) rootView.findViewById(R.id.name_textview);
		mUserName.setText(SettingsPreferences.getFullName(getContext()));

		mUserName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final EditText input = new EditText(getContext());
				input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
				alertDialogBuilder.setView(input);
				alertDialogBuilder
						.setTitle(R.string.setName)
						.setMessage(R.string.setNameMessage)
						.setCancelable(false)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										String inputText = input.getText().toString();
										String[] name = null;
										if(inputText.matches("[a-zA-Z]*[\\s]{1}[a-zA-Z].*")) {
											name = inputText.split(" ");
											SettingsPreferences.setFullName(getContext(),name[0],name[1]);
											mUserName.setText(inputText);
											firebaseModel.updateFirstNameAndLastName(name[0],name[1]);
											Toast.makeText(getContext(),R.string.full_name_updated,Toast.LENGTH_SHORT).show();
										}
										else{
											Toast.makeText(getContext(),R.string.full_name_not_updated,Toast.LENGTH_SHORT).show();
										}
									}
								})
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.cancel();
									}
								});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}
		});



		mLogoutView.setOnClickListener(clickListener);
		mTermofServiceParent.setOnClickListener(clickListener);
		mLicensesParent.setOnClickListener(clickListener);
		mPrivacyParent.setOnClickListener(clickListener);
		mNotificationParent.setOnClickListener(clickListener);
		mVibrateParent.setOnClickListener(clickListener);
		mSoundParent.setOnClickListener(clickListener);
		mLocationParent.setOnClickListener(clickListener);
		mHelpParent.setOnClickListener(clickListener);
		mReportProblemParent.setOnClickListener(clickListener);
		mRingtoneParent.setOnClickListener(clickListener);
	}



	/**
	 * OnClicked listener register
	 */
	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if(view.getId() == R.id.settings_personal_profile_imageview){
				pickProfileImage();
			}

			if (view.getId() == R.id.report_problem_parent) {
				showContent(getResources().getString(R.string.report_a_problem));

			} else if (view.getId() == R.id.term_of_service_parent) {

				showContent(getResources().getString(R.string.term_of_service));

			} else if (view.getId() == R.id.licenses_parent) {
				showContent(getResources().getString(R.string.licenses));

			} else if (view.getId() == R.id.privacy_parent) {
				showContent(getResources().getString(R.string.privacy));
			} else if (view.getId() == R.id.notification_parent) {

			} else if (view.getId() == R.id.vibrate_parent) {




			} else if (view.getId() == R.id.sound_parent) {


			} else if (view.getId() == R.id.ringtone_parent) {
				showRingtoneChoice(getResources().getString(R.string.ringtone));
			} else if (view.getId() == R.id.help_parent) {
				showContent(getResources().getString(R.string.help));
			} else if (view.getId() == R.id.location_parent) {
				showLocationContent();
			} else if (view.getId() == R.id.logout_view) {
				firebaseModel.signOut();
				SettingsPreferences.setLogin(getActivity(), false);
				startActivity(new Intent(getActivity(), LoginActivity.class));
				//System.exit(0);

			}

		}
	};


	/**
	 * Method to set profile pic
	 */
	private void pickProfileImage() {

		new MaterialDialog.Builder(getActivity())
				.title(R.string.upload_image)
				.items(R.array.upload_photo_string)
				.itemsCallbackSingleChoice(1, new MaterialDialog.ListCallback() {
					@Override
					public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
						switch (which){
							case 0:
								onLaunchGallery();
								break;
							case 1:
								onLaunchCamera();
								break;
							case 2:
								Intent intent = new Intent(getContext(),ProfileImageActivity.class);
								startActivity(intent);
								break;
						}
					}
				})
				.positiveText(R.string.choose)
				.show();



	}

	public void onLaunchCamera() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	public void onLaunchGallery(){
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK)
			switch (requestCode){
				case GALLERY_REQUEST:
					Uri selectedImage = data.getData();
					try {
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
						mUserImageview.setImageBitmap(bitmap);
						firebaseModel.saveImageToFirebase(bitmap,firebaseModel.getCurrentUser().getUid()+".jpeg", new ModelFirebase.SaveIamgeCallback() {
							@Override
							public void onComplete(String url) {
								firebaseModel.setDataField("avatar",url);
								Toast.makeText(getActivity(), R.string.upload_avatar_success, Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFailed() {
								Toast.makeText(getActivity(), R.string.upload_avatar_failed, Toast.LENGTH_SHORT).show();
							}
						});
					} catch (IOException e) {
						Log.i("GalleryFailed", "Some exception " + e);
					}
					break;
				case REQUEST_IMAGE_CAPTURE:
					Bundle extras = data.getExtras();
					Bitmap imageBitmap = (Bitmap) extras.get("data");
					mUserImageview.setImageBitmap(imageBitmap);
					firebaseModel.saveImageToFirebase(imageBitmap, firebaseModel.getCurrentUser().getUid() + ".jpeg", new ModelFirebase.SaveIamgeCallback() {
						@Override
						public void onComplete(String url) {
							firebaseModel.setDataField("avatar",url);
							Toast.makeText(getActivity(), R.string.upload_avatar_success, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onFailed() {
							Toast.makeText(getActivity(), R.string.upload_avatar_failed, Toast.LENGTH_SHORT).show();
						}
					});
					break;
			}
	}
	/**
	 * Method to share app on social network
	 */
	private void shareClicked(String subject, String text) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		startActivity(Intent.createChooser(intent,getString(R.string.share_title)));
	}

	/**
	 * Method to show location dialog
	 */
	private void showLocationContent() {
		new MaterialDialog.Builder(getActivity())
				.title(R.string.useGoogleLocationServices)
				.content(R.string.useGoogleLocationServicesPrompt)
				.positiveText(R.string.agree).negativeText(R.string.disagree)
				.show();
	}

	/**
	 * Method to show location dialog
	 */
	private void showContent(String title) {
		new MaterialDialog.Builder(getActivity()).title(title)
				.content(R.string.loremIpsum).positiveText(R.string.agree)
				.negativeText(R.string.disagree).show();
	}

	private void showRingtoneChoice(String title) {
		new MaterialDialog.Builder(getActivity())
				.title(title)
				.items(R.array.ringtone_string)
				.alwaysCallSingleChoiceCallback()
				.itemsCallbackSingleChoice(1,new MaterialDialog.ListCallback() {
							@Override
							public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
								try{
								switch (which){
									case 0:

										SoundManger.getInstance(getContext()).playSound(getContext().getAssets().openFd( getResources().getString(R.string.ring1)));
										SettingsPreferences.setRingtoneIndex(getContext(),R.string.ring1);
										SettingsPreferences.setRingtonePath(getContext(),"sounds/ring1.mp3");
										SettingsPreferences.setRingtoneName(getContext(),getString(R.string.ringtone_1));
										break;
									case 1:
										SoundManger.getInstance(getContext()).playSound(getContext().getAssets().openFd( getResources().getString(R.string.ring2)));
										SettingsPreferences.setRingtonePath(getContext(),"sounds/ring2.mp3");
										SettingsPreferences.setRingtoneIndex(getContext(),R.string.ring2);
										SettingsPreferences.setRingtoneName(getContext(),getString(R.string.ringtone_2));
										break;
									case 2:
										SoundManger.getInstance(getContext()).playSound(getContext().getAssets().openFd( getResources().getString(R.string.ring3)));
										SettingsPreferences.setRingtonePath(getContext(),"sounds/ring3.mp3");
										SettingsPreferences.setRingtoneIndex(getContext(),R.string.ring3);
										SettingsPreferences.setRingtoneName(getContext(),getString(R.string.ringtone_3));
										break;
									case 3:
										SoundManger.getInstance(getContext()).playSound(getContext().getAssets().openFd( getResources().getString(R.string.ring4)));
										SettingsPreferences.setRingtonePath(getContext(),"sounds/ring4.mp3");
										SettingsPreferences.setRingtoneIndex(getContext(),R.string.ring4);
										SettingsPreferences.setRingtoneName(getContext(),getString(R.string.ringtone_4));
										break;
									case 4:
										SoundManger.getInstance(getContext()).playSound(getContext().getAssets().openFd( getResources().getString(R.string.ring5)));
										SettingsPreferences.setRingtonePath(getContext(),"sounds/ring5.mp3");
										SettingsPreferences.setRingtoneIndex(getContext(),R.string.ring5);
										SettingsPreferences.setRingtoneName(getContext(),getString(R.string.ringtone_5));
										break;
									case 5:
										SoundManger.getInstance(getContext()).playSound(getContext().getAssets().openFd( getResources().getString(R.string.ring6)));
										SettingsPreferences.setRingtonePath(getContext(),"sounds/ring6.mp3");
										SettingsPreferences.setRingtoneIndex(getContext(),R.string.ring6);
										SettingsPreferences.setRingtoneName(getContext(),getString(R.string.ringtone_6));
										break;
									case 6:
										SoundManger.getInstance(getContext()).playSound(getContext().getAssets().openFd( getResources().getString(R.string.ring7)));
										SettingsPreferences.setRingtonePath(getContext(),"sounds/ring7.mp3");
										SettingsPreferences.setRingtoneIndex(getContext(),R.string.ring7);
										SettingsPreferences.setRingtoneName(getContext(),getString(R.string.ringtone_7));
										break;

								}
								} catch (IOException e) {
									e.printStackTrace();
								}
								mRingtoneSelectedTextview.setText(SettingsPreferences.getRingtoneName(getContext()));
							}
						}).positiveText(R.string.choose).show();
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

	/**
	 * Method to initialize floating buttons
	 */
	private void setFloatingButton() {
		mFrameLayout = (FrameLayout) rootView
				.findViewById(R.id.settings_container);

		PromotedActionsLibrary promotedActionsLibrary = new PromotedActionsLibrary();

		promotedActionsLibrary.setup(getActivity(), mFrameLayout);

		View.OnClickListener onShareClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				shareClicked(getString(R.string.share_subject),
						AppConstants.PLAYSTORE_URL
								+ getActivity().getPackageName());
			}
		};

		promotedActionsLibrary.addItem(
				getResources().getDrawable(R.drawable.ic_share),
				onShareClickListener, ThemeContentManager.getInstance()
				.getFloatingButtonLayout(getActivity()));

		promotedActionsLibrary.addMainItem(
				getResources().getDrawable(R.drawable.plus),
				ThemeContentManager.getInstance()
				.getFloatingButtonLayout(getActivity()));
	}

	@Override
	public void onDestroy() {
		if (mActionBroadcastReceiver != null) {
			getActivity().unregisterReceiver(mActionBroadcastReceiver);
			mActionBroadcastReceiver = null;
		}
		mFrameLayout = null;
		rootView = null;
		mUserImageview = null;
		mUserName = null;
		mUserImageview = null;
		mHelpParent = null;
		mLocationParent = null;
		mRingtoneParent = null;
		mVibrateParent = null;
		mSoundParent = null;
		mNotificationParent = null;
		mPrivacyParent = null;
		mReportProblemParent = null;
		mTermofServiceParent = null;
		mLicensesParent = null;
		mUserName = null;
		mRingtoneSelectedTextview = null;
		mLogoutView = null;
		super.onDestroy();
	}

}