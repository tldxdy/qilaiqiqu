package com.qizhi.qilaiqiqu.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.qizhi.qilaiqiqu.R;

public class SplashView extends View {

	// ��Բ(�����кܶ�СԲ)�İ뾶
	private float mRotationRadius = 90;
	// ÿһ��СԲ�İ뾶
	private float mCircleRadius = 18;
	// СԲ����ɫ
	private int[] mCircleColor;
	// ��Բ��СԲ��ת��ʱ��
	private long mRotationDuration = 1200; // ��λ����
	// �ڶ����ֶ���ִ�е���ʱ��
	private long mSplashDuration = 1200; // ��λ����
	// ����ı�����ɫ
	private int mSplashBgColor = Color.WHITE;

	// ����Բ��ʼ�뾶
	private float mHoleRadius = 0f;
	// ��ǰ��԰��ת�Ƕ�
	private float mCurrentRotationAngle = 0f;
	// ��ǰ��Բ�İ뾶
	private float mCurrentRotationRadius = mRotationRadius;

	// ����԰�Ļ���
	private Paint mPaint = new Paint();
	// ���Ʊ����Ļ���
	private Paint mPaintBackground = new Paint();

	// ��Ļ�����ĵ�����
	private float mCenterX;
	private float mCenterY;

	// ��Ļ�Խ���һ��
	private float mDiagonalDist;

	private SplashState mState = null;

	private abstract class SplashState {
		public abstract void drawState(Canvas canvas);
	}

	public SplashView(Context context) {
		super(context);

		initView(context);
	}

	private void initView(Context context) {
		mCircleColor = context.getResources().getIntArray(
				R.array.splash_circle_colors);
		mPaint.setAntiAlias(true);
		mPaintBackground.setAntiAlias(true);
		mPaintBackground.setStyle(Style.STROKE);
		mPaintBackground.setColor(mSplashBgColor);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// �˷������� View ��ʾ�������º󱻵���
		super.onSizeChanged(w, h, oldw, oldh);

		// ��ʼ����������
		mCenterX = w / 2f;
		mCenterY = h / 2f;
		// �Խ��ߵ�һ��
		mDiagonalDist = (float) Math.sqrt(w * w + h * h) / 2;
	}

	// ���������� -- ����������������
	public void splashAndDisappear() {
		// ��Ҫȡ����һ������
		RotationState rs = (RotationState) mState;
		rs.cancel();

		mState = new MergingState();
		// ����View���»��ơ���������onDraw()����
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ���ģ��
		// ʵ��һ���򵥵Ļ����¼��ַ�
		if (mState == null) {
			// ��һ��ִ�ж���
			mState = new RotationState();
		}
		mState.drawState(canvas);

	}

	/*
	 * ��ת����
	 */
	private class RotationState extends SplashState {

		private ValueAnimator mAnimator;

		public RotationState() {
			// СԲ������ ����> ��԰�İ뾶,��԰��ת�˶��ٽǶ�
			// ��ֵ��,1200ms ʱ���ڼ���ĳ��ʱ�̵�ǰ�Ƕ��� : 0~2��
			mAnimator = ValueAnimator.ofFloat(0, (float) Math.PI * 2);
			// �������Բ�ֵ��
			mAnimator.setInterpolator(new LinearInterpolator());
			mAnimator.setDuration(mRotationDuration);
			mAnimator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// �õ�ĳ��ʱ���Ľ��������ǰ��Բ��ת�ĽǶ�
					mCurrentRotationAngle = (Float) animation
							.getAnimatedValue();
					// �������»��ơ���onDraw()
					invalidate();
				}
			});
			// ������ת���� INFINITE:����
			mAnimator.setRepeatCount(ValueAnimator.INFINITE);
			mAnimator.start();
		}

		@Override
		public void drawState(Canvas canvas) {
			// ִ����ת����
			// ����СԲ����ת����
			// 1.��ջ���
			drawBackground(canvas);
			// 2.����СԲ
			drawCircles(canvas);
		}

		public void cancel() {
			mAnimator.cancel();
		}

	}

	/*
	 * �ۺ϶���
	 */
	private class MergingState extends SplashState {

		private ValueAnimator mAnimator;

		public MergingState() {
			// ���ƾۺ϶���
			// ��ֵ��,1200ms ʱ���ڼ���ĳ��ʱ�̵�ǰ��Բ�İ뾶�� : r~0
			mAnimator = ValueAnimator.ofFloat(0, mRotationRadius);
			// ���õ��Լ�����
			mAnimator.setInterpolator(new OvershootInterpolator(10));
			mAnimator.setDuration(mRotationDuration / 2);
			mAnimator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// ������ǰ��Բ�İ뾶
					mCurrentRotationRadius = (Float) animation
							.getAnimatedValue();
					invalidate();
				}
			});
			
			mAnimator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					// �����ۺ϶����������
					super.onAnimationEnd(animation);
					mState = new ExpandingState();
					invalidate();
				}
			});

			// �������
			mAnimator.reverse();
		}

		@Override
		public void drawState(Canvas canvas) {
			// ����СԲ�ľۺ϶���
			// 1.��ջ���
			drawBackground(canvas);
			// 2.����СԲ
			drawCircles(canvas);
		}

	}

	/*
	 * ��ɢ����
	 */
	private class ExpandingState extends SplashState {

		private ValueAnimator mAnimator;

		public ExpandingState() {
			// ������ɢ����
			// ��ֵ��,1200ms ʱ���ڼ���ĳ��ʱ�̵�ǰ����԰�İ뾶�� : 0~�Խ��ߵ�һ��
			mAnimator = ValueAnimator.ofFloat(0, mDiagonalDist);
			// ���õ��Լ�����
			mAnimator.setDuration(mRotationDuration / 2);
			mAnimator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// ������ǰ����Բ�İ뾶
					mHoleRadius = (Float) animation.getAnimatedValue();
					invalidate();
				}
			});

			mAnimator.start();

		}

		@Override
		public void drawState(Canvas canvas) {
			// ���Ʊ���
			drawBackground(canvas);
		}

	}

	public void drawBackground(Canvas canvas) {
		if (mHoleRadius > 0f) {
			// ���ƿ���԰
			// ����:ʹ��һ���ǳ��ֵĻ��ʣ����ϵļ�С�Ŀ��
			
			//���û�abide��� = �Խ��ߵ�һ��/2-���Ĳ��ֵİ뾶
			float strokeWidth = mDiagonalDist- mHoleRadius;
			mPaintBackground.setStrokeWidth(strokeWidth);
			float radius = mHoleRadius + strokeWidth/2;
			canvas.drawCircle(mCenterX, mCenterY, radius, mPaintBackground);
		} else {
			// ��ջ���
			canvas.drawColor(mSplashBgColor);

		}

	}

	public void drawCircles(Canvas canvas) {
		// ����СԲ
		// ���ƽǶ�
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
}
