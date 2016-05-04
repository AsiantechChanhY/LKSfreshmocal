package com.shopsy.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.shopsy.R;

public class ImageLoader_BandWidth 
{
	
	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;
	Context mContext;

	Bitmap bitmap;
	AnimationDrawable frameAnimation;
	private int i_size = 320;
	public ImageLoader_BandWidth(Context context)
	{
		fileCache = new FileCache(context);
		mContext = context;
		executorService = Executors.newFixedThreadPool(5);
	}

	private int mTag = 0;

	/**
	 * LoadImage method for load image from web
	 *@param url
	 *@param imageView
	 *@param tag
	 *				pass int value (1,2,3) for<br/>
	 *				1 = thumbImage (ScaleType.FIT_XY),<br/>
	 *				2 = fullImage (ScaleType.FIT_XY),<br/>
	 *				3 = fullImage (ScaleType.FIT_START)
	 */
	public synchronized void DisplayImage(String url, ImageView imageView, int tag)
	{
		this.mTag = tag;
		if(mTag == 1 || mTag == 4){
//			i_size =Constant.MAX_ThumbImage_SIZE;
			i_size =Constant.MAX_Image_SIZE;
		}
		else {
			i_size =Constant.MAX_ThumbImage_SIZE;
		}
		imageViews.put(imageView, url);
		bitmap = memoryCache.get(url);
		if (bitmap != null)
		{			
			Log.i("memoryCache", "bitmap");
			imageView.setImageBitmap(bitmap);
			imageView.setScaleType(ScaleType.FIT_XY);
		}
		else
		{
			Log.i("memoryCache_touch", "bitmap null");
			queuePhoto(url, imageView);

			frameAnimation = (AnimationDrawable) mContext.getResources().getDrawable(R.anim.animation_loding);
			imageView.setScaleType(ScaleType.CENTER);
			imageView.setImageDrawable(frameAnimation);

			Timer timer = new Timer();
			timer.schedule(new TimerTask()
			{
				@Override
				public void run() 
				{
					frameAnimation.start();
				}
			}, 0);
		}
	}

	private void queuePhoto(String url, ImageView imageView)
	{
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		//executor.execute(new PhotosLoader(p));
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(String url)
	{
		File f = fileCache.getFile(url);
		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
			.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		}
		catch (Throwable ex)
		{
			ex.printStackTrace();
			if(ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}

	}

	// decodes image and scales it to reduce memory consumption
	public Bitmap decodeFile(File f)
	{
		try 
		{
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1=new FileInputStream(f);
			BitmapFactory.decodeStream(stream1,null,o);
			stream1.close();

			//Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE=i_size;
			int width_tmp=o.outWidth, height_tmp=o.outHeight;
			int scale=1;
			while(true){
				if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
					break;
				width_tmp/=2;
				height_tmp/=2;
				scale*=2;
			}
			Log.i("ImageSize------------>",""+ scale);
			//decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize=scale;
			FileInputStream stream2=new FileInputStream(f);
			Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		}catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
			if(e instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

	// Task for the queue
	private class PhotoToLoad
	{
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i)
		{
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable
	{
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad)
		{
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run()
		{
			if (imageViewReused(photoToLoad))
				return;

			Bitmap bmp = getBitmap(photoToLoad.url);
			if(bmp != null)
			{
				memoryCache.put(photoToLoad.url, bmp);
				if (imageViewReused(photoToLoad))
					return;
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				Activity a = (Activity) photoToLoad.imageView.getContext();
				a.runOnUiThread(bd);
			}
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad)
	{
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable
	{
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p)
		{
			bitmap = b;
			photoToLoad = p;
		}

		public void run()
		{
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
			{
				photoToLoad.imageView.setBackgroundResource(0);
				photoToLoad.imageView.setImageBitmap(bitmap);
			/*	if(mTag==1 || mTag == 2)
				{
					photoToLoad.imageView.setScaleType(ScaleType.FIT_XY);	
				}
				else if (mTag == 4)
				{
					photoToLoad.imageView.setScaleType(ScaleType.FIT_CENTER);
				}
				else if (mTag == 5)
				{
					photoToLoad.imageView.setScaleType(ScaleType.CENTER_CROP);	
				}
				else 
				{
					photoToLoad.imageView.setScaleType(ScaleType.FIT_START);
				}*/	
				if(mTag==1 || mTag == 2)
				{
					photoToLoad.imageView.setScaleType(ScaleType.FIT_XY);	
				}
				else if (mTag == 4)
				{
					photoToLoad.imageView.setScaleType(ScaleType.FIT_XY);
				}
				else if (mTag == 5)
				{
					photoToLoad.imageView.setScaleType(ScaleType.FIT_XY);	
				}
				else 
				{
					photoToLoad.imageView.setScaleType(ScaleType.FIT_XY);
				}
			}
			else
			{
				Drawable drawable = mContext.getResources().getDrawable(R.drawable.no_image_grey);
				photoToLoad.imageView.setImageDrawable(drawable);
			}
		}
	}

	public void clearCache()
	{
		memoryCache.clear();
		fileCache.clear();
	}

}
