package com.qizhi.qilaiqiqu.ui;

import com.qizhi.qilaiqiqu.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class RotateImageView extends View{
	private int imageId;
    private Context context;
    private Bitmap bitmap;
    private int currentdegrees;
    
    private boolean loopFlag = true;
	
	public RotateImageView(Context context) {
		super(context);
		this.context = context;
	}
	public RotateImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	public RotateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(this.imageId == 0)
			this.imageId = R.drawable.loading;
		bitmap = BitmapFactory.decodeResource(context.getResources(), imageId);
		int specWidthSize = bitmap.getWidth();
		int specHeightSize = bitmap.getHeight();
		setMeasuredDimension(specWidthSize, specHeightSize);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(!loopFlag){
			canvas.drawBitmap(bitmap, 0, 0, null);
			return ;			
		}
		currentdegrees +=5;
		if(currentdegrees == 360)
			currentdegrees = 0;
		canvas.rotate(currentdegrees, this.getWidth()/2, this.getHeight()/2);
	    canvas.drawBitmap(bitmap, 0, 0, null);
	    this.invalidate();
	}
	
	public void stopRotate(){
		loopFlag = false;
	}
	public void startRotate(){
		loopFlag = true;
		this.invalidate();
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
}

