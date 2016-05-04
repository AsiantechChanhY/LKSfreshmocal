package com.shopsy.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v7.app.ActionBar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopsy.R;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.app.Add_Item_Edit_Final.ADD_IMAGE_EDIT;
import com.shopsy.app.Add_Item_Edit_Final_Copy.ADD_IMAGE_COPY;
import com.shopsy.app.Add_Item_Final.ADD_IMAGE;

/**
 * @author Prithivi Raj
 *
 */
public class Add_item_photo extends HockeyActivity implements OnClickListener 
{
	LinearLayout Ly_addphoto_main, Ly_photo_1, Ly_photo_2, Ly_photo_3, Ly_photo_4;
	ImageView Iv_Main, Iv_1, Iv_2, Iv_3, Iv_4;	
	
	byte[] Image_Pick_1, Image_Pick_2, Image_Pick_3, Image_Pick_4, Image_Pick_5;
	ArrayList<String> image_final = new ArrayList<String>();
	int img_capture_count, img_pick_count; 
	private static int PICK_IMAGE = 1;
	private static int CAMERA_REQUEST_2 = 22;
	String Add_Item_Type;
	String str_static_add = "add_item";
	String str_static_add_edit = "add_item_edit";
	String str_static_add_edit_copy = "add_item_edit_copy";
	
	private SharedPreferences preferences;
	@SuppressWarnings("unused")
	private SharedPreferences.Editor editor;
	String shared_profileimage;
	ActionBar actionBar;
	ColorDrawable colorDrawable = new ColorDrawable();
	
	private String mApiKey;
	Uri mImageUri;
// your aviary secret key
	private static final String API_SECRET = "XXXXX";
	private String mSessionId;
	String mOutputFilePath;
	public static final String LOG_TAG = "aviary-launcher";
	private static final int EXTERNAL_STORAGE_UNAVAILABLE = 1;
	int imageWidth, imageHeight;
	private File mGalleryFolder;
	private static final String FOLDER_NAME = "Shopsy_Seller";
	private File captured_image;
	String imagePath;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item_photo);

		preferences = getSharedPreferences(Iconstant.sharedPre_Name,0);
        editor = preferences.edit();
		shared_profileimage = preferences.getString(Iconstant.sharedPre_ProfileImage, "");
		Custom_ActionBar();
		
		captured_image = new File(Environment.getExternalStorageDirectory(),"temp.jpg");
		
		init();
	}

	private void Custom_ActionBar() 
	{
		actionBar = getSupportActionBar();
		getSupportActionBar().show();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
		
        LayoutInflater mInflater = LayoutInflater.from(Add_item_photo.this);
		View mCustomView = mInflater.inflate(R.layout.custom_action_bar_additem, null);
		ImageView imageView = (ImageView) mCustomView.findViewById(R.id.imageView1_custom_action_bar_youritem_additem);
		imageView.setImageBitmap(decodeBase64(shared_profileimage));
		TextView textView = (TextView) mCustomView.findViewById(R.id.textView1_custom_action_bar_youritem_additem);
		TextView textView_save = (TextView) mCustomView.findViewById(R.id.imageButton_youritem_grid_save);
		textView.setText("Add Photos");
		imageView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				Add_item_photo.this.finish();
		 		overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
			}
		});
		textView_save.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Add_Item_Type.equals(str_static_add)) {
					
					System.out.println("----------Image_Pick_1-------------");
					
					if(Image_Pick_1!=null)
					{
						
						//code to refresh drawer Class
						Intent broadcastIntent1 = new Intent();
						broadcastIntent1.setAction("com.package.UPDATEPHOTOPLUS");
						broadcastIntent1.putExtra("byte1", Image_Pick_1);
						broadcastIntent1.putExtra("byte2", Image_Pick_2);
						broadcastIntent1.putExtra("byte3", Image_Pick_3);
						broadcastIntent1.putExtra("byte4", Image_Pick_4);
						broadcastIntent1.putExtra("byte5", Image_Pick_5);
						sendBroadcast(broadcastIntent1);
						
						
					//ADD_IMAGE.IMAGE_GET(Image_Pick_1, Image_Pick_2, Image_Pick_3,Image_Pick_4, Image_Pick_5);
					Add_item_photo.this.finish();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);
					}
					else
					{
						Toast.makeText(Add_item_photo.this, "No Photo Selected", Toast.LENGTH_SHORT).show();
					}
						
				}if (Add_Item_Type.equals(str_static_add_edit)) {
					
					if(Image_Pick_1!=null)
					{
						
						//code to refresh drawer Class
						Intent broadcastIntent1 = new Intent();
						broadcastIntent1.setAction("com.package.UPDATE_EDITPHOTOPLUS");
						broadcastIntent1.putExtra("byte1", Image_Pick_1);
						broadcastIntent1.putExtra("byte2", Image_Pick_2);
						broadcastIntent1.putExtra("byte3", Image_Pick_3);
						broadcastIntent1.putExtra("byte4", Image_Pick_4);
						broadcastIntent1.putExtra("byte5", Image_Pick_5);
						sendBroadcast(broadcastIntent1);
						
					Add_item_photo.this.finish();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);
					}
					else
					{
						Toast.makeText(Add_item_photo.this, "No Photo Selected", Toast.LENGTH_SHORT).show();
					}
					
					/*ADD_IMAGE_EDIT.IMAGE_GET_EDIT(Image_Pick_1, Image_Pick_2, Image_Pick_3,Image_Pick_4, Image_Pick_5);
					Add_item_photo.this.finish();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);*/
				}if (Add_Item_Type.equals(str_static_add_edit_copy)) {
					
					
					if(Image_Pick_1!=null)
					{
						
						//code to refresh drawer Class
						Intent broadcastIntent1 = new Intent();
						broadcastIntent1.setAction("com.package.UPDATE_EDITPHOTO_FINALPLUS");
						broadcastIntent1.putExtra("byte1", Image_Pick_1);
						broadcastIntent1.putExtra("byte2", Image_Pick_2);
						broadcastIntent1.putExtra("byte3", Image_Pick_3);
						broadcastIntent1.putExtra("byte4", Image_Pick_4);
						broadcastIntent1.putExtra("byte5", Image_Pick_5);
						sendBroadcast(broadcastIntent1);
						
					Add_item_photo.this.finish();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);
					}
					else
					{
						Toast.makeText(Add_item_photo.this, "No Photo Selected", Toast.LENGTH_SHORT).show();
					}
					
					/*ADD_IMAGE_COPY.IMAGE_GET_COPY(Image_Pick_1, Image_Pick_2, Image_Pick_3,Image_Pick_4, Image_Pick_5);
					Add_item_photo.this.finish();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);*/
				}
			}
		});
		textView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				Add_item_photo.this.finish();
				overridePendingTransition(R.anim.left_right, R.anim.right_left);
			}
		});
		getSupportActionBar().setCustomView(mCustomView);
/*		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(false);*/
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(android.R.color.transparent);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		colorDrawable.setColor(0xffffffff);
		actionBar.setBackgroundDrawable(colorDrawable);
	}
	
	public static Bitmap decodeBase64(String input) 
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
	
	private void init()
	{
/*		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle("Add Photos");*/
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
		
		Bundle bundle = getIntent().getExtras();
		if (bundle ==null) {
			
		}	
		else{
			Add_Item_Type = bundle.getString("ADD_ITEM_FINAL"); 
		}	
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		imageWidth = (int) ( (float) metrics.widthPixels / 1.5 );
		imageHeight = (int) ( (float) metrics.heightPixels / 1.5 );
		
		mGalleryFolder = createFolders();
		
		
		
//		registerForContextMenu( Ly_addphoto_main );
	}
	
	@Override
	protected void onResume() {
		Log.e(LOG_TAG, "onResume");
		super.onResume();

		if ( getIntent() != null ) {
			handleIntent( getIntent());
			setIntent( new Intent());
		}
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
				
				
				
				System.out.println("-----------------save_photo_item_menu-----------------");
/*				
			if (!Image_Pick_1.equals("") && !Image_Pick_2.equals("") && !Image_Pick_3.equals("")) 
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
			}
				Intent int_add_item_final = new Intent(Add_item_photo_Edit.this, Add_Item_Edit_Final.class);
				int_add_item_final.putExtra("img1", Image_Pick_1);
				int_add_item_final.putExtra("img2", Image_Pick_2);
				int_add_item_final.putExtra("img3", Image_Pick_3);
				int_add_item_final.putExtra("img4", Image_Pick_4);
				int_add_item_final.putExtra("img5", Image_Pick_5);
		    	startActivity(int_add_item_final);
		    	
			if (Add_Item_Type.equals(str_static_add)) {
				ADD_IMAGE.IMAGE_GET(Image_Pick_1, Image_Pick_2, Image_Pick_3,Image_Pick_4, Image_Pick_5);
				Add_item_photo.this.finish();
				overridePendingTransition(R.anim.left_right, R.anim.right_left);
			}else if (Add_Item_Type.equals(str_static_add_edit)) {
				ADD_IMAGE_EDIT.IMAGE_GET_EDIT(Image_Pick_1, Image_Pick_2, Image_Pick_3,Image_Pick_4, Image_Pick_5);
				Add_item_photo.this.finish();
				overridePendingTransition(R.anim.left_right, R.anim.right_left);
			}*/
			break;
			
			case android.R.id.home:
				Add_item_photo.this.finish();
		 		overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);

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

		AlertDialog.Builder builder = new AlertDialog.Builder(Add_item_photo.this);
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
						
						
						Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(captured_image));
						intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
			            startActivityForResult(intent, CAMERA_REQUEST_2);
						
						
					/*	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
	                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
	                    startActivityForResult(intent, CAMERA_REQUEST_2);*/
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
				 
	                try 
	                {
	                	
	                	imagePath = captured_image.getAbsolutePath();
						
						BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);

						if (bitmap !=null) 
						{
							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
							byte[] byteArray = stream.toByteArray();
							
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
						else
						{
							Toast.makeText(Add_item_photo.this,"Image Not Found",Toast.LENGTH_SHORT).show();
						}
	                	
					} catch (Exception e) {
						e.printStackTrace();
					}
	            }
			 else if (requestCode == PICK_IMAGE) 
			{
//	            Aviary_Edit(data);
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
	    	  Add_item_photo.this.finish();
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
	

	
	private void handleIntent( Intent intent ) {

		String action = intent.getAction();

		if ( null != action ) {

			if ( Intent.ACTION_SEND.equals( action ) ) {

				Bundle extras = intent.getExtras();
				if ( extras != null && extras.containsKey( Intent.EXTRA_STREAM ) ) {
					Uri uri = (Uri) extras.get( Intent.EXTRA_STREAM );
				loadAsync( uri );
				}
			} else if ( Intent.ACTION_VIEW.equals( action ) ) {
				Uri data = intent.getData();
				Log.e(LOG_TAG, "data: " + data );
				loadAsync( data );
			}
		}
	}
	
	private void loadAsync( final Uri uri ) {
		Log.e(LOG_TAG, "loadAsync: " + uri );

		Drawable toRecycle = Iv_Main.getDrawable();
		if ( toRecycle != null && toRecycle instanceof BitmapDrawable ) {
			if ( ( (BitmapDrawable) Iv_Main.getDrawable() ).getBitmap() != null )
				( (BitmapDrawable) Iv_Main.getDrawable() ).getBitmap().recycle();
		}
		Iv_Main.setImageDrawable( null );
		mImageUri = null;

	}
	
	
	
	
	private File createFolders() {

		File baseDir;

		if ( android.os.Build.VERSION.SDK_INT < 8 ) {
			baseDir = Environment.getExternalStorageDirectory();
		} else {
			baseDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
		}

		if ( baseDir == null ) return Environment.getExternalStorageDirectory();

		Log.d( LOG_TAG, "Pictures folder: " + baseDir.getAbsolutePath() );
		File aviaryFolder = new File( baseDir, FOLDER_NAME );

		if ( aviaryFolder.exists() ) return aviaryFolder;
		if ( aviaryFolder.mkdirs() ) return aviaryFolder;

		return Environment.getExternalStorageDirectory();
	}

	private boolean setImageURI( final Uri uri, final Bitmap bitmap ) {

		Log.e( LOG_TAG, "image size: " + bitmap.getWidth() + "x" + bitmap.getHeight() );
		Iv_Main.setImageBitmap( bitmap );
		Iv_Main.setBackgroundDrawable( null );

		mImageUri = uri;
		return true;
	}
	
	
	
	
	
	 public static int calculateInSampleSize(
	            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	        // Raw height and width of image
	        final int height = options.outHeight;
	        final int width = options.outWidth;
	        int inSampleSize = 1;
	        if (height > reqHeight || width > reqWidth) {
	            final int halfHeight = height / 2;
	            final int halfWidth = width / 2;
	            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	            // height and width larger than the requested height and width.
	            while ((halfHeight / inSampleSize) > reqHeight
	                    && (halfWidth / inSampleSize) > reqWidth) {
	                inSampleSize *= 2;
	            }
	        }
	        return inSampleSize;
	    }

	    public static Bitmap decodeSampledBitmapFromBitmap(Resources res, Bitmap bitmap,
	                                                         int reqWidth, int reqHeight) {
	        // First decode with inJustDecodeBounds=true to check dimensions
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	        byte[] b = baos.toByteArray();
	        BitmapFactory.decodeByteArray(b,0,b.length,options);
	        // Calculate inSampleSize
	        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	        // Decode bitmap with inSampleSize set
	        options.inJustDecodeBounds = false;
	        return BitmapFactory.decodeByteArray(b,0,b.length,options);
	    }
}