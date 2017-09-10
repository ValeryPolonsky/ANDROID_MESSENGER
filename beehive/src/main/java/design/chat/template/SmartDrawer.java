/*
 *    The MIT License (MIT)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */
package design.chat.template;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

public class SmartDrawer extends LinearLayout {
	private float mScale;

	private float mOriginHeight = -1;

	private boolean mOpened = true;

	private boolean mPendingOpened;

	private boolean mAnimating;

	private OnStateChangeListener mOnStateChangeListener;

	public SmartDrawer(Context context) {
		this(context, null);
	}

	public SmartDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SmartDrawer);
		mOriginHeight = a.getDimension(R.styleable.SmartDrawer_init_height, -1);
		mOpened = a.getBoolean(R.styleable.SmartDrawer_init_open, true);
		a.recycle();
	}

	public void setOnStateChangeListener(OnStateChangeListener listener) {
		this.mOnStateChangeListener = listener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (mOriginHeight == -1) {
							mOriginHeight = SmartDrawer.this
									.getMeasuredHeight();
						}
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							getViewTreeObserver().removeGlobalOnLayoutListener(
									this);
						} else {
							getViewTreeObserver().removeOnGlobalLayoutListener(
									this);
						}
						if (mOpened) {
							open();
						} else {
							close();
						}
					}
				});
	}


	//Starts opening animation of skins colors menu
	public void animateOpen() {
		mPendingOpened = true;
		if (mAnimating) {
			return;
		}
		mOpened = true;
		ObjectAnimator animator = ObjectAnimator.ofFloat(this, "scale", 1);
		animator.setDuration(200);
		animator.start();
		mAnimating = true;
		animator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mAnimating = false;

				if (mPendingOpened != mOpened) {
					animateClose();
				}
				if (mOnStateChangeListener != null) {
					mOnStateChangeListener.onStateChange(mOpened);
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
	}

	//Finishes opening animation of skins colors menu
	public void animateClose() {
		mPendingOpened = false;
		if (mAnimating) {
			return;
		}
		mOpened = false;
		ObjectAnimator animator = ObjectAnimator.ofFloat(this, "scale", 0);
		animator.setDuration(200);
		animator.start();

		mAnimating = true;
		animator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mAnimating = false;
				if (mPendingOpened != mOpened) {
					animateOpen();
				}
				if (mOnStateChangeListener != null) {
					mOnStateChangeListener.onStateChange(mOpened);
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
	}

	public void animateToggle() {
		if (mOpened) {
			animateClose();
		} else {
			animateOpen();
		}
	}

	public void toggle() {
		if (mOpened) {
			close();
		} else {
			open();
		}
	}

	public void close() {
		setScale(0);
		mOpened = false;
		if (mOnStateChangeListener != null) {
			mOnStateChangeListener.onStateChange(mOpened);
		}
	}

	public void open() {
		setScale(1);
		mOpened = true;
		if (mOnStateChangeListener != null) {
			mOnStateChangeListener.onStateChange(mOpened);
		}
	}

	void setScale(float scale) {
		mScale = scale;
		int scaleHeight = (int) (mOriginHeight * mScale);
		getLayoutParams().height = scaleHeight;
		requestLayout();
	}

	float getScale() {
		return mScale;
	}

	public static interface OnStateChangeListener {
		public void onStateChange(boolean opened);
	}
}
