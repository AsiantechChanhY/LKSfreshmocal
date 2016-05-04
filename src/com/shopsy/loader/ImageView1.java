package com.shopsy.loader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageView1 extends ImageView {

 

  public ImageView1(Context context) {

       super(context);

  }

 

  public ImageView1(Context context, AttributeSet attrs) {

       super(context, attrs);

  }

 
  public ImageView1(Context context, AttributeSet attrs, int defStyle) {

       super(context, attrs, defStyle);

  }

 

  @Override

  protected void onDraw(Canvas canvas) {

       float radius = 90.0f; // angle of round corners

       Path clipPath = new Path();

       RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());

       clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);

       canvas.clipPath(clipPath);

 

       super.onDraw(canvas);

   }

}
