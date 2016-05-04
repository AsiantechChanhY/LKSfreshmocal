package com.shopsy.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shopsy.R;
import com.shopsy.HockeyApp.HockeyActivity;
/**
 * @author Prithivi Raj
 *
 */
public class Add_item_photo_Edit extends HockeyActivity implements OnClickListener 
{
	LinearLayout Ly_addphoto_main, Ly_photo_1, Ly_photo_2, Ly_photo_3, Ly_photo_4;
	ImageView Iv_Main, Iv_1, Iv_2, Iv_3, Iv_4;	
	
	byte[] Image_Pick_1, Image_Pick_2, Image_Pick_3, Image_Pick_4, Image_Pick_5;
	ArrayList<String> image_final = new ArrayList<String>();
	int img_capture_count, img_pick_count; 
	private static int PICK_IMAGE = 1;
	private static int CAMERA_REQUEST_2 = 22;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item_photo);

		init();
	}

	private void init()
	{
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle("Add Photos");
		Ly_addphoto_main = (LinearLayout) findViewById(R.id.linear_layout_additem_photo);
		Ly_photo_1 = (LinearLayout) findViewById(R.id.linear_layout_itemtitle_additem_final_all_image_1);
		Ly_photo_2 = (LinearLayout) findViewById(R.id.linear_layout_itemtitle_additem_final_all_image_2);
		Ly_photo_3 = (LinearLayout) findViewById(R.id.linear_layout_itemtitle_additem_final_all_image_3);
		Ly_photo_4 = (LinearLayout) findViewById(R.id.linear_layout_itemtitle_additem_final_all_image_4);
		Iv_Main = (ImageView) findViewById(R.id.imageView_main_add_item_add_photo);
		Iv_1 = (ImageView) findViewById(R.id.imageView_1_add_item_add_photo);
		Iv_2 = (ImageView) findViewById(R.id.imageView_2_add_item_add_photo);
		Iv_3 = (ImageView) findViewById(R.id.imageView_3_add_item_add_photo);
		Iv_4 = (ImageView) findViewById(R.id.imageView_4_add_item_add_photo);
		
		Ly_addphoto_main.setOnClickListener(this);
		Ly_photo_1.setOnClickListener(this);
		Ly_photo_2.setOnClickListener(this);
		Ly_photo_3.setOnClickListener(this);
		Ly_photo_4.setOnClickListener(this);
		Iv_Main.setOnClickListener(this);
		Iv_1.setOnClickListener(this);
		Iv_2.setOnClickListener(this);
		Iv_3.setOnClickListener(this);
		Iv_4.setOnClickListener(this);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		 getMenuInflater().inflate(R.menu.add_item_photo, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			case R.id.save_photo_item_menu:
				
	/*		if (!Image_Pick_1.equals("") && !Image_Pick_2.equals("") && !Image_Pick_3.equals("")) 
			{
				Intent int_add_item_final = new Intent(Add_item_photo.this, Add_Item_Final.class);
				int_add_item_final.putExtra("img1", Image_Pick_1);
				int_add_item_final.putExtra("img2", Image_Pick_2);
				int_add_item_final.putExtra("img3", Image_Pick_3);
				int_add_item_final.putExtra("img4", Image_Pick_4);
		    	startActivity(int_add_item_final);
		    	Add_item_photo.this.finish();
			} else 
			{
				Toast.makeText(Add_item_photo.this,"Please Select Minimun 3 Pictures", Toast.LENGTH_SHORT).show();
			}*/
/*				Intent int_add_item_final = new Intent(Add_item_photo_Edit.this, Add_Item_Edit_Final.class);
				int_add_item_final.putExtra("img1", Image_Pick_1);
				int_add_item_final.putExtra("img2", Image_Pick_2);
				int_add_item_final.putExtra("img3", Image_Pick_3);
				int_add_item_final.putExtra("img4", Image_Pick_4);
				int_add_item_final.putExtra("img5", Image_Pick_5);
		    	startActivity(int_add_item_final);*/
		    	Add_item_photo_Edit.this.finish();
		    	overridePendingTransition(R.anim.left_right, R.anim.right_left);
		    	
//		    	ADD_IMAGE.IMAGE_GET(Image_Pick_1,Image_Pick_2, Image_Pick_3, Image_Pick_4, Image_Pick_5);
			break;
			
			case android.R.id.home:
/*				 Intent int_add_item_final1 = new Intent(Add_item_photo_Edit.this, Add_Item_Edit_Final.class);
		    	 startActivity(int_add_item_final1);*/
		    	 Add_item_photo_Edit.this.finish();
		    	 overridePendingTransition(R.anim.left_right, R.anim.right_left);

				break;
		}
		return false;
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.linear_layout_additem_photo:

			selectImage(1);
			break;
		case R.id.linear_layout_itemtitle_additem_final_all_image_1:

			selectImage(2);
			break;
		case R.id.linear_layout_itemtitle_additem_final_all_image_2:

			selectImage(3);
			break;
		case R.id.linear_layout_itemtitle_additem_final_all_image_3:

			selectImage(4);
			break;
		case R.id.linear_layout_itemtitle_additem_final_all_image_4:

			selectImage(5);
			break;
		case R.id.imageView_main_add_item_add_photo:

			selectImage(1);
			break;
		case R.id.imageView_1_add_item_add_photo:

			selectImage(2);
			break;
		case R.id.imageView_2_add_item_add_photo:

			selectImage(3);
			break;
		case R.id.imageView_3_add_item_add_photo:

			selectImage(4);
			break;
		case R.id.imageView_4_add_item_add_photo:

			selectImage(5);
			break;

		default:
			break;
		}
	}

	private Void selectImage(final int i) 
	{
		final CharSequence[] items = { "Take Photo", "Choose from Gallery","Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(Add_item_photo_Edit.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int item) 
			{
				if (items[item].equals("Take Photo"))
				{
					/*	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(cameraIntent, CAMERA_REQUEST_2);*/
						img_capture_count =i;
						Log.d("Int", ""+i);
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
	                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
	                    startActivityForResult(intent, CAMERA_REQUEST_2);
				}
				else if (items[item].equals("Choose from Gallery"))
				{
					Intent ia = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);	           
	                startActivityForResult(ia, PICK_IMAGE);
	                img_pick_count = i;
				} else if (items[item].equals("Cancel")) 
				{
					dialog.dismiss();
				}
			}
		});
		builder.show();
		return null;
	}
	
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) 
		{
		/*	if (requestCode == CAMERA_REQUEST_2) 
			{  
				 File file1 = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
				 Bitmap photo = (Bitmap) data.getExtras().get("data"); 
	            Toast.makeText(Add_item_photo.this, file1.getAbsolutePath(), Toast.LENGTH_SHORT).show();
	            path = file1.getAbsolutePath();
	            Log.d("count", ""+img_capture_count);
	            if (img_capture_count==1) 
	            {
	            	Iv_Main.setImageBitmap(photo);
				}
	            else if (img_capture_count==2) 
	            {
					Iv_1.setImageBitmap(photo);
				}
	            else if (img_capture_count==3) 
	            {
					Iv_2.setImageBitmap(photo);
				}
	            else if (img_capture_count==4) 
	            {
					Iv_3.setImageBitmap(photo);
				}
	            else if (img_capture_count==5) 
	            {
					Iv_4.setImageBitmap(photo);
				}	            
	        }*/ 
			 if (requestCode == CAMERA_REQUEST_2)
			 {
	                File f = new File(Environment.getExternalStorageDirectory().toString());
	                for (File temp : f.listFiles())
	                {
	                    if (temp.getName().equals("temp.jpg"))
	                    {
	                        f = temp;
	                        break;
	                    }
	                }
	                try 
	                {
	                    Bitmap bitmap1 = null;
	                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();	 
	                    bitmap1 = BitmapFactory.decodeFile(f.getAbsolutePath(),bitmapOptions); 	
	                    Bitmap bitmap = getResizedBitmap(bitmap1, 600);
	                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            		bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

	                    byte[] byteArray = stream.toByteArray();
//	                    String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
	                    
	                    Log.d("count", ""+img_capture_count);
	    	            if (img_capture_count==1) 
	    	            {
	    	            	Iv_Main.setImageBitmap(bitmap);    	            	
	    	            	Image_Pick_1= byteArray;
	    	            	Log.d("image_pick_1----->", ""+Image_Pick_1);
	    				}
	    	            else if (img_capture_count==2) 
	    	            {
	    					Iv_1.setImageBitmap(bitmap);	    	          	
	    	            	Image_Pick_2= byteArray;
	    	            	Log.d("image_pick_2----->", ""+Image_Pick_2);
	    				}
	    	            else if (img_capture_count==3) 
	    	            {
	    					Iv_2.setImageBitmap(bitmap);	    	            	
	    	            	Image_Pick_3= byteArray;
	    	            	Log.d("image_pick_3----->", ""+Image_Pick_3);
	    				}
	    	            else if (img_capture_count==4) 
	    	            {
	    					Iv_3.setImageBitmap(bitmap);	    	            	
	    	            	Image_Pick_4= byteArray;
	    	            	Log.d("Image-Array----->", ""+Image_Pick_4);
	    				}
	    	            else if (img_capture_count==5) 
	    	            {
	    					Iv_4.setImageBitmap(bitmap);
	    	            	Image_Pick_5= byteArray;
	    	            	Log.d("image_pick_5----->", ""+Image_Pick_5);
	    				}
	    	            }	 
	    	            catch (Exception e) {
	                        e.printStackTrace();
	                    }
	            }
			else if (requestCode == PICK_IMAGE) 
			{
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();
	 
	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String picturePath = cursor.getString(columnIndex);
	            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
	            Bitmap bitmap1 = getResizedBitmap(bitmap, 600);
	            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            bitmap1.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] byteArray = stream.toByteArray();
	            cursor.close();
	             
	            Log.d("img_capture_count", ""+img_pick_count);
	            if (img_pick_count==1) 
	            {
	            	Iv_Main.setImageBitmap(bitmap1);	            	
	            	Image_Pick_1= byteArray;
	            	Log.d("image_pick_1----->", ""+Image_Pick_1);
				}
	            else if (img_pick_count==2) 
	            {
					Iv_1.setImageBitmap(bitmap1);            	
	            	Image_Pick_2= byteArray;
	            	Log.d("Image_Pick_2----->", ""+Image_Pick_2);
				}
	            else if (img_pick_count==3) 
	            {
					Iv_2.setImageBitmap(bitmap1);	            	
	            	Image_Pick_3= byteArray;
	            	Log.d("Image_Pick_3----->", ""+Image_Pick_3);
				}
	            else if (img_pick_count==4) 
	            {
					Iv_3.setImageBitmap(bitmap1);	            	
	            	Image_Pick_4= byteArray;
	            	Log.d("Image_Pick_4----->", ""+Image_Pick_4);
				}
	            else if (img_pick_count==5) 
	            {
					Iv_4.setImageBitmap(bitmap1);
	            	Image_Pick_5= byteArray;
	            	Log.d("Image_Pick_5----->", ""+Image_Pick_5);
				}	                  
	        }
		}
	}

	public String getPath(Uri uri, Activity activity)
	{
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if ((keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0))
	      {
	    	  Toast.makeText(getApplicationContext(), "Home_Pressed", Toast.LENGTH_SHORT).show();
	          return true;
	      }
	      if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) 
	      {
/*	    	  Intent int_add_item_final = new Intent(Add_item_photo_Edit.this, Add_Item_Edit_Final.class);
	    	  startActivity(int_add_item_final);*/
	    	  Add_item_photo_Edit.this.finish();
	    	  overridePendingTransition(R.anim.left_right, R.anim.right_left);
	          return true;
	      }
	      return false;
	}
	
	public Bitmap getResizedBitmap(Bitmap image, int maxSize)
	{
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) 
        {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else 
        {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
	
	
}