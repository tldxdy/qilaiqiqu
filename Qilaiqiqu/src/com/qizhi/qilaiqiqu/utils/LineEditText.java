package com.qizhi.qilaiqiqu.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

public class LineEditText extends EditText {

	private Paint mPaint; 
	
	public LineEditText(Context context) {
		super(context);
	}

	public LineEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();  
        
        mPaint.setStyle(Paint.Style.STROKE);  
        mPaint.setColor(Color.WHITE); 
	}

	public LineEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);  
//      画底线  
        canvas.drawLine(0,this.getHeight()-1,  this.getWidth()-1, this.getHeight()-1, mPaint);
	}

}
