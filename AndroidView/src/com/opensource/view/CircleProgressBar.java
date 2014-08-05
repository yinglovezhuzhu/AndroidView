/*******************************************************************************
 * Copyright (C) ${year}.year The Android Open Source Project.
 *
 *        yinglovezhuzhu@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.opensource.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Use：Circle ProgressBar
 * 
 * @author yinglovezhuzhu@gmail.com
 */
public class CircleProgressBar extends View {
	
	private static final int MIN_PROGRESS_WIDTH = 2;
	
//	private static final int 
	
//	private int mMinWidth;
//	private int mMinHeight;
//	private int mMaxWidth;
//	private int mMaxHeight;
	
	private float mMaxProgress = 100f;
	private float mProgress = 0f;
	private int mProgressWidth = 0;
	private int mTextHeight = 0;
	private int mStartAngle = 0;
	
	/** Background color */
	private int mBackgroundColor = Color.TRANSPARENT;
	/** Progress an text color */
	private int mProgressColor = Color.argb(0xff, 0x57, 0x87, 0xb6);
	/** Text color */
	private int mTextColor = Color.argb(0xff, 0x57, 0x87, 0xb6);
	/** Progress background */
	private int mProgressBackgroundColor = Color.WHITE;
	/** Circle center part background color */
	private int mCenterBackgroundColor = Color.TRANSPARENT;
	
	private boolean mShowNumber = true;
	
	private RectF mOval;
	private Paint mPaint;
	
	private long mUiThreadId;
	
	public CircleProgressBar(Context context) {
		this(context, null);
	}
	
	public CircleProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs, defStyle, 0);
	}
	
	/**
	 * Hided constructor
	 * @hide 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 * @param styleRes
	 */
	public CircleProgressBar(Context context,  AttributeSet attrs, int defStyle, int styleRes) {
		super(context, attrs, defStyle);
		mUiThreadId = Thread.currentThread().getId();
		initProgressBar();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyle, 0);

		setBackgroundColor(a.getColor(R.styleable.CircleProgressBar_backgroundColor, mBackgroundColor));
		setProgressBackgroundColor(a.getColor(R.styleable.CircleProgressBar_progressBackgroundColor, mProgressBackgroundColor));
		setProgressColor(a.getColor(R.styleable.CircleProgressBar_progressColor, mProgressColor));
		setCenterBackgroundColor(a.getColor(R.styleable.CircleProgressBar_centerBackgroundColor, mCenterBackgroundColor));
		setProgressWidth(a.getDimensionPixelSize(R.styleable.CircleProgressBar_progressWidth, 0));
		mShowNumber = a.getBoolean(R.styleable.CircleProgressBar_showNumber, false);
		setMaxProgress(a.getFloat(R.styleable.CircleProgressBar_max, mMaxProgress));
		setProgress(a.getFloat(R.styleable.CircleProgressBar_progress, mProgress));
		setStartAngle(a.getInt(R.styleable.CircleProgressBar_startAngle, mStartAngle));
		a.recycle();
	}
 
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawColor(mBackgroundColor); //Draw background color.
		
		int width = this.getWidth();
		int height = this.getHeight();
 
		if(width != height) { //Make sure progress is square
			int min = Math.min(width, height);
			width = min;
			height = min;
		}

		//Draw progress and progress background
		drawProgress(canvas, width, height);
		
		//Draw progress and progress background
		drawCenter(canvas, width, height);
		
		//Draw number text
		if(mShowNumber) {
			drawText(canvas, width, height);
		}
 
	}
 
 
 
	/**
	 * Get max progress
	 * @return
	 */
	public float getMaxProgress() {
		return mMaxProgress;
	}
 
	/**
	 * Set max progress
	 * @param maxProgress
	 */
	public void setMaxProgress(float maxProgress) {
		this.mMaxProgress = maxProgress;
	}
 
	/**
	 * Set progress
	 * @param progress
	 */
	public synchronized void setProgress(float progress) {
		if(progress < 0) {
			progress = 0;
		}
		if(progress > mMaxProgress) {
			progress = mMaxProgress;
		}
		if(mProgress != progress) {
			this.mProgress = progress;
			refreshProgress();
		}
	}

    /**
     * Get current progress
     * @return
     */
    public synchronized float getProgress() {
        return mProgress;
    }
 
	/**
	 * 
	 */
	public void setProgressNotInUiThread(int progress) {
		this.mProgress = progress;
		refreshProgress();
	}
	
	/**
	 * Get background color
	 * @return
	 */
	public int getBackgroundColor() {
		return mBackgroundColor;
	}
	
	/**
	 * Set background color
	 */
	public void setBackgroundColor(int color) {
		this.mBackgroundColor = color;
		refreshProgress();
	}
	
	/**
	 * Get progress color
	 * @return
	 */
	public int getProgressColor() {
		return mProgressColor;
	}
	
	/**
	 * Set progress color
	 * @param color
	 */
	public void setProgressColor(int color) {
		this.mProgressColor = color;
		refreshProgress();
	}
	
	/**
	 * Set progress width.
	 * @param width
	 */
	public void setProgressWidth(int width) {
		this.mProgressWidth = width;
		refreshProgress();
	}
	
	/**
	 * Get progress width.
	 * @return
	 */
	public int getProgressWidth() {
		return mProgressWidth;
	}
	
	/**
	 * Get number text color
	 * @return
	 */
	public int getTextColor() {
		return mTextColor;
	}
	
	/**
	 * Set number text color
	 * @param color
	 */
	public void setTextColor(int color) {
		this.mTextColor = color;
		refreshProgress();
	}
	
	/**
	 * Get progress background color
	 * @return
	 */
	public int getProgressBackgroundColor() {
		return mProgressBackgroundColor;
	}
	
	/**
	 * Set progress background color
	 * @param color
	 */
	public void setProgressBackgroundColor(int color) {
		this.mProgressBackgroundColor = color;
		refreshProgress();
	}
	
	/**
	 * Get center part background color
	 * @return
	 */
	public int getCenterBackgroundColor() {
		return mCenterBackgroundColor;
	}
	
	/**
	 * Set center part background color
	 * @param color
	 */
	public void setCenterBackgroundColor(int color) {
		this.mCenterBackgroundColor = color;
		refreshProgress();
	}
	
	public void setStartAngle(int angle) {
		this.mStartAngle = angle;
		refreshProgress();
	}

	private void initProgressBar() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		mOval = new RectF();
	}
	
	/**
	 * Draw progress and progress background
	 * @param canvas
	 * @param width
	 * @param height
	 */
	private void drawProgress(Canvas canvas, int width, int height) {
		//Set progress stroke
		if(mProgressWidth < 1) {
			int pwidth = width / 14;
			mProgressWidth = pwidth < MIN_PROGRESS_WIDTH ? MIN_PROGRESS_WIDTH : pwidth;
		}
		mPaint.setStrokeWidth(mProgressWidth); //ProgressWidth
		mPaint.setStyle(Style.STROKE);
 
		mOval.left = mProgressWidth / 2; // Left 
		mOval.top = mProgressWidth / 2; // Top
		mOval.right = width - mProgressWidth / 2; // Right
		mOval.bottom = height - mProgressWidth / 2; // Bottom
		
		//Draw progress background
		mPaint.setColor(mProgressBackgroundColor);
		canvas.drawArc(mOval, mStartAngle, 360, false, mPaint); // Draw progress background
		
		//Draw progress
		mPaint.setColor(mProgressColor);
		canvas.drawArc(mOval, mStartAngle, ((float) mProgress / mMaxProgress) * 360, false, mPaint); // 绘制进度圆弧，这里是蓝色
	}
	
	/**
	 * Draw progress center part
	 * @param canvas
	 * @param width
	 * @param height
	 */
	private void drawCenter(Canvas canvas, int width, int height) {
		mOval.left = mProgressWidth; // Left 
		mOval.top = mProgressWidth; // Top
		mOval.right = width - mProgressWidth; // Right
		mOval.bottom = height - mProgressWidth; // Bottom
		mPaint.setColor(mCenterBackgroundColor);
		mPaint.setStyle(Style.FILL);
		canvas.drawArc(mOval, mStartAngle, 360, false, mPaint);
	}
	
	/**
	 * Draw progress number text
	 * @param canvas
	 * @param width
	 * @param height
	 */
	private void drawText(Canvas canvas, int width, int height) {
		mPaint.setColor(mTextColor);
		mPaint.setStrokeWidth(1);
		String text = mProgress + "%";
		mTextHeight = mTextHeight == 0 ? height / 4 : mTextHeight;
		mPaint.setTextSize(mTextHeight);
		int textWidth = (int) mPaint.measureText(text, 0, text.length());
		mPaint.setStyle(Style.FILL);
		canvas.drawText(text, width / 2 - textWidth / 2, height / 2 + mTextHeight / 2, mPaint);
	}
	
	private synchronized void refreshProgress() {
		if (mUiThreadId == Thread.currentThread().getId()) {
			this.postInvalidate();
		}
	}
}
