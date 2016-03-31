package com.qizhi.qilaiqiqu.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.qizhi.qilaiqiqu.R;

public class SplashView extends View {

	private float mRotationRadius = 90;
	private float mCircleRadius = 18;
	private int[] mCircleColor;
	private long mRotationDuration = 1200;
	private long mSplashDuration = 1200;
	private int mSplashBgColor = Color.WHITE;

	private float mHoleRadius = 0f;
	private float mCurrentRotationAngle = 0f;
	private float mCurrentRotationRadius = mRotationRadius;

	private Paint mPaint = new Paint();
	private Paint mPaintBackground = new Paint();

	private float mCenterX;
	private float mCenterY;

	private float mDiagonalDist;

	private SplashState mState = null;

	private Timer timer;
	private TimerTask task;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				Toasts.show(getContext(), "与服务器连接超时，请检查网络", 0);
				stopTime();
			}

		};
	};
	
	private abstract class SplashState {
		public abstract void drawState(Canvas canvas);
	}

	public SplashView(Context context) {
		super(context);
		startTime();
		initView(context);
	}

	private void initView(Context context) {
		if (mState == null) {
			mState = new RotationState();
		}
		mCircleColor = context.getResources().getIntArray(
				R.array.splash_circle_colors);
		mPaint.setAntiAlias(true);
		mPaintBackground.setAntiAlias(true);
		mPaintBackground.setStyle(Style.STROKE);
		mPaintBackground.setColor(mSplashBgColor);
	}

	private void startTime() {
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = handler.obtainMessage();
				message.arg1 = 1;
				handler.sendMessage(message);
			}
		};
		timer.schedule(task, 5000);
	}

	private void stopTime() {
		timer.cancel();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mCenterX = w / 2f;
		mCenterY = h / 2f;
		
		mDiagonalDist = (float) Math.sqrt(w * w + h * h) / 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mState.drawState(canvas);

	}

	private class RotationState extends SplashState {

		private ValueAnimator mAnimator;

		public RotationState() {
			mAnimator = ValueAnimator.ofFloat(0, (float) Math.PI * 2);
			mAnimator.setInterpolator(new LinearInterpolator());
			mAnimator.setDuration(mRotationDuration);
			mAnimator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					mCurrentRotationAngle = (Float) animation
							.getAnimatedValue();
					invalidate();
				}
			});
			mAnimator.setRepeatCount(ValueAnimator.INFINITE);
			mAnimator.start();
		}

		@Override
		public void drawState(Canvas canvas) {
			drawBackground(canvas);
			drawCircles(canvas);
		}

		public void cancel() {
			mAnimator.cancel();
		}

	}

	private class MergingState extends SplashState {

		private ValueAnimator mAnimator;

		public MergingState() {
			mAnimator = ValueAnimator.ofFloat(0, mRotationRadius);
			mAnimator.setInterpolator(new OvershootInterpolator(10));
			mAnimator.setDuration(mRotationDuration / 2);
			mAnimator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					mCurrentRotationRadius = (Float) animation
							.getAnimatedValue();
					invalidate();
				}
			});
			
			mAnimator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					mState = new ExpandingState();
					invalidate();
				}
			});

			mAnimator.reverse();
		}

		@Override
		public void drawState(Canvas canvas) {
			drawBackground(canvas);
			drawCircles(canvas);
		}

	}

	private class ExpandingState extends SplashState {

		private ValueAnimator mAnimator;

		public ExpandingState() {
			mAnimator = ValueAnimator.ofFloat(0, mDiagonalDist);
			mAnimator.setDuration(mRotationDuration / 2);
			mAnimator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					mHoleRadius = (Float) animation.getAnimatedValue();
					invalidate();
				}
			});

			mAnimator.start();

		}

		@Override
		public void drawState(Canvas canvas) {
			drawBackground(canvas);
		}

	}

	public void drawBackground(Canvas canvas) {
		if (mHoleRadius > 0f) {
			float strokeWidth = mDiagonalDist- mHoleRadius;
			mPaintBackground.setStrokeWidth(strokeWidth);
			float radius = mHoleRadius + strokeWidth/2;
			canvas.drawCircle(mCenterX, mCenterY, radius, mPaintBackground);
		} else {
			canvas.drawColor(mSplashBgColor);

		}

	}

	public void drawCircles(Canvas canvas) {
		float rottationAngle = (float) (2 * Math.PI / mCircleColor.length);
		for (int i = 0; i < mCircleColor.length; i++) {
			/**
			 * x = r * cos(a) + mCenterX y = r * sin(a) + mCenterY a = ��ת�Ƕ� +
			 * ����Ƕ� * i;
			 */

			double a = mCurrentRotationAngle + rottationAngle * i;
			float cx = (float) (mCurrentRotationRadius * Math.cos(a) + mCenterX);
			float cy = (float) (mCurrentRotationRadius * Math.sin(a) + mCenterY);

			mPaint.setColor(mCircleColor[i]);
			canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
		}

	}
	
	public void splashAndDisappear() {
		stopTime();
		RotationState rs = (RotationState) mState;
		rs.cancel();
		mState = new MergingState();
		invalidate();
	}
}
