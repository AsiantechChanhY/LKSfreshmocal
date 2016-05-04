package com.shopsy.seller.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shopsy.R;
import com.shopsy.app.Add_Item_Edit_Final.Tour;
import com.shopsy.app.Add_item_photo;
import com.shopsy.bean.TouchImageView;
import com.shopsy.bean.TouchImageView.OnTouchImageViewListener;
import com.shopsy.loader.ImageLoader;
@SuppressWarnings("deprecation")
public class ImageAdapter_Edit extends PagerAdapter
{
	Context ctx;
	Activity Activity;
	private ArrayList<String> GalImages;
	public ImageLoader imageLoader;
	
	public ImageAdapter_Edit(Context context, ArrayList<String> images) {
		this.ctx = context;
		this.GalImages = images;
		imageLoader=new ImageLoader(ctx);
//		Log.d("GalImages-------------------------->",""+ GalImages);
	}
	
	@Override
	public int getCount() 
	{
		return GalImages.size();
	}

	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageView) object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		final ImageView imageView = new ImageView(ctx);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
//		int padding = ctx.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
//		imageView.setPadding(padding, padding, padding, padding);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		
		Log.d("GalImages-------------------------->",""+ GalImages.get(position));
		imageLoader.DisplayImage(GalImages.get(position), imageView);
		((ViewPager) container).addView(imageView);
		
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent int_add_photo1 = new Intent(ctx, Add_item_photo.class);
				int_add_photo1.putExtra("ADD_ITEM_FINAL", "add_item_edit");
				ctx.startActivity(int_add_photo1);
				((Activity) ctx).overridePendingTransition(R.anim.enter,R.anim.exit);
			}
		});
		imageView.setOnLongClickListener(new OnLongClickListener() 
		{	
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
		        final Bitmap imgbitmap = drawable.getBitmap();	        
//		        Bitmap imgnew;
//		        imgnew = getResizedBitmap(imgbitmap, 600);
				showImage(imgbitmap);
				
				Tour.Tour_ssession();
				
				return true;
			}
		});
		
		return imageView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}
	
	public void showImage(Bitmap imgnew) 
	{
		int width = 20;
		int hight = 40;
	    final Dialog builder = new Dialog(ctx);
	    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	    builder.setOnDismissListener(new DialogInterface.OnDismissListener() 
	    {
	        @Override
	        public void onDismiss(DialogInterface dialogInterface) {
	        	builder.dismiss();
	        }
	    });	
		Display display = ((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();		
		screenWidth = screenWidth - width;
		screenHeight = screenHeight - hight;
		
		Log.d("ScreenResoluction------->",+ screenWidth+","+screenHeight);
		
		final TouchImageView imageView = new TouchImageView(ctx);
	    imageView.setImageBitmap(imgnew);
//	    builder.addContentView(imageView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	    builder.addContentView(imageView, new RelativeLayout.LayoutParams(screenWidth, screenHeight));
	    
	    imageView.setOnClickListener(new OnClickListener() 
	    {			
			@Override
			public void onClick(View v) 
			{
				builder.dismiss();
			}
		});
	    
	    imageView.setOnTouchImageViewListener(new OnTouchImageViewListener() {
			
			@Override
			public void onMove() {
/*				PointF point = imageView.getScrollPosition();
				RectF rect = imageView.getZoomedRect();
				float currentZoom = imageView.getCurrentZoom();
				boolean isZoomed = imageView.isZoomed();*/
			}
		});

	    builder.show();
	}
}
