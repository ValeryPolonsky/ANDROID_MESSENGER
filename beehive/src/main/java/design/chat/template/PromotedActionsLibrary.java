package design.chat.template;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class PromotedActionsLibrary {

	Context context;

	FrameLayout frameLayout;

	ImageButton mainImageButton;

	RotateAnimation rotateOpenAnimation;

	RotateAnimation rotateCloseAnimation;

	ArrayList<ImageButton> promotedActions;

	ObjectAnimator objectAnimator[];

	private int px;
	private static final int ANIMATION_TIME = 200;
	private boolean isMenuOpened;

	public void setup(Context activityContext, FrameLayout layout) {
		context = activityContext;
		promotedActions = new ArrayList<ImageButton>();
		frameLayout = layout;
		px = (int) context.getResources().getDimension(R.dimen.dim56dp) + 10;
		openRotation();
		closeRotation();
	}

	public void ClearButton() {
		frameLayout.removeAllViews();
		promotedActions.clear();

	}

	public ImageButton addMainItem(Drawable drawable, int id) {
		ImageButton button = (ImageButton) LayoutInflater.from(context)
				.inflate(id, frameLayout, false);
		button.setImageDrawable(drawable);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (isMenuOpened) {
					closePromotedActions().start();
					isMenuOpened = false;
				} else {
					isMenuOpened = true;
					openPromotedActions().start();
				}
			}
		});

		frameLayout.addView(button);

		mainImageButton = button;

		return button;
	}

	public void addItem(Drawable drawable,
			View.OnClickListener onClickListener, int id) {
		ImageButton button = (ImageButton) LayoutInflater.from(context)
				.inflate(id, frameLayout, false);

		button.setImageDrawable(drawable);

		button.setOnClickListener(onClickListener);

		promotedActions.add(button);

		frameLayout.addView(button);

		return;
	}

	/**
	 * Set close animation for promoted actions
	 */
	public AnimatorSet closePromotedActions() {

		if (objectAnimator == null) {
			objectAnimatorSetup();
		}

		AnimatorSet animation = new AnimatorSet();

		for (int i = 0; i < promotedActions.size(); i++) {

			objectAnimator[i] = setCloseAnimation(promotedActions.get(i), i);
		}

		if (objectAnimator.length == 0) {
			objectAnimator = null;
		}

		animation.playTogether(objectAnimator);
		animation.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
				mainImageButton.startAnimation(rotateCloseAnimation);
				mainImageButton.setClickable(false);
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				mainImageButton.setClickable(true);
				hidePromotedActions();
			}

			@Override
			public void onAnimationCancel(Animator animator) {
				mainImageButton.setClickable(true);
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}
		});

		return animation;
	}

	public AnimatorSet openPromotedActions() {

		if (objectAnimator == null) {
			objectAnimatorSetup();
		}

		AnimatorSet animation = new AnimatorSet();

		for (int i = 0; i < promotedActions.size(); i++) {

			objectAnimator[i] = setOpenAnimation(promotedActions.get(i), i);
		}

		if (objectAnimator.length == 0) {
			objectAnimator = null;
		}

		animation.playTogether(objectAnimator);
		animation.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
				mainImageButton.startAnimation(rotateOpenAnimation);
				mainImageButton.setClickable(false);
				showPromotedActions();
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				mainImageButton.setClickable(true);
			}

			@Override
			public void onAnimationCancel(Animator animator) {
				mainImageButton.setClickable(true);
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}
		});

		return animation;
	}

	private void objectAnimatorSetup() {

		objectAnimator = new ObjectAnimator[promotedActions.size()];
	}

	/**
	 * Set close animation for single button
	 * 
	 * @param promotedAction
	 * @param position
	 * @return objectAnimator
	 */
	private ObjectAnimator setCloseAnimation(ImageButton promotedAction,
			int position) {

		ObjectAnimator objectAnimator;

		objectAnimator = ObjectAnimator.ofFloat(promotedAction,
				View.TRANSLATION_X, -px * (promotedActions.size() - position),
				0f);
		objectAnimator.setRepeatCount(0);
		objectAnimator.setDuration(ANIMATION_TIME
				* (promotedActions.size() - position));

		return objectAnimator;
	}

	/**
	 * Set open animation for single button
	 * 
	 * @param promotedAction
	 * @param position
	 * @return objectAnimator
	 */
	private ObjectAnimator setOpenAnimation(ImageButton promotedAction,
			int position) {

		ObjectAnimator objectAnimator;

		objectAnimator = ObjectAnimator.ofFloat(promotedAction,
				View.TRANSLATION_X, 0f, -px
						* (promotedActions.size() - position));
		objectAnimator.setRepeatCount(0);
		objectAnimator.setDuration(ANIMATION_TIME
				* (promotedActions.size() - position));

		return objectAnimator;
	}

	private void hidePromotedActions() {

		for (int i = 0; i < promotedActions.size(); i++) {
			promotedActions.get(i).setVisibility(View.GONE);
		}
	}

	private void showPromotedActions() {

		for (int i = 0; i < promotedActions.size(); i++) {
			promotedActions.get(i).setVisibility(View.VISIBLE);
		}
	}

	private void openRotation() {

		rotateOpenAnimation = new RotateAnimation(0, 45,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateOpenAnimation.setFillAfter(true);
		rotateOpenAnimation.setFillEnabled(true);
		rotateOpenAnimation.setDuration(ANIMATION_TIME);
	}

	public void closeRotation() {

		rotateCloseAnimation = new RotateAnimation(45, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateCloseAnimation.setFillAfter(true);
		rotateCloseAnimation.setFillEnabled(true);
		rotateCloseAnimation.setDuration(ANIMATION_TIME);
	}

}
