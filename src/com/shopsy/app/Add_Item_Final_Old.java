package com.shopsy.app;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopsy.R;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
/**
 * @author Prithivi Raj
 *
 */
@SuppressLint("WrongViewCast") 
public class Add_Item_Final_Old extends HockeyActivity implements OnClickListener
{
	LinearLayout Ly_Camera, Ly_Item_Tittle, Ly_Description, Ly_Shipping,Ly_Add_Tag, Ly_Add_Material, Ly_Price, Ly_Quantity;
	TextView Tv_Item_Title, Tv_Description, Tv_Shipping, Tv_AddTag,
			Tv_AddMaterial, Tv_MadeToMe, Tv_Access, Tv_Sub, Tv_Sub1, Tv_Manage,Tv_Share, Tv_price, Tv_Quantity;
	ImageView Iv_image;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private JSONArray jsonarray_shipping = new JSONArray();
	private JSONArray jsonarray_cat = new JSONArray();
	private String Item_Tittle = "", Price = "", Quantity = "", Desc = "",Add_tag = "", Add_Material = "";
	private String s_tittle, s_time, s_dest, s_from, s_cost, s_ano_cost;
	@SuppressWarnings("unused")
	private String get_shared_photo;
	private Bitmap bitmap_image_1=null, bitmap_image_2=null, bitmap_image_3=null, bitmap_image_4=null, bitmap_image_5=null;
	private String Id_1, Id_2, Id_3, Name_1, Name_2, Name_3;
	byte[] byteArray1=null,  byteArray2=null, byteArray3=null, byteArray4=null, byteArray5=null;
	int seller_id;
	String shared_title, shared_price, shared_qua, shared_des, shared_tag, shared_mat;
	String shared_profileimage1="", shared_profileimage2="", shared_profileimage3="", shared_profileimage4="", shared_profileimage5="";
	String str_title, str_price, str_qua, str_des, str_tag, str_mat;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item_final);

		init();
		Add_Json();
		
		Session_Value();
	}

	
	private void Remove_Shared_Value() 
	{
		preferences= getSharedPreferences(Iconstant.sharedPre_Name, 0);
		preferences.edit().remove(Iconstant.Session_AddImage1).commit();
		preferences.edit().remove(Iconstant.Session_AddImage2).commit();
		preferences.edit().remove(Iconstant.Session_AddImage3).commit();
		preferences.edit().remove(Iconstant.Session_AddImage4).commit();
		preferences.edit().remove(Iconstant.Session_AddImage5).commit();
		
		preferences.edit().remove(Iconstant.Session_AddTitle).commit();
		preferences.edit().remove(Iconstant.Session_AddPrice).commit();
		preferences.edit().remove(Iconstant.Session_AddQua).commit();
		preferences.edit().remove(Iconstant.Session_AddDes).commit();
		preferences.edit().remove(Iconstant.Session_AddTag).commit();
		preferences.edit().remove(Iconstant.Session_AddMat).commit();
	}


	private void Session_Value() 
	{
		shared_profileimage1 = preferences.getString(Iconstant.Session_AddImage1, ""); 
		shared_profileimage2 = preferences.getString(Iconstant.Session_AddImage2, ""); 
		shared_profileimage3 = preferences.getString(Iconstant.Session_AddImage3, ""); 
		shared_profileimage4 = preferences.getString(Iconstant.Session_AddImage4, ""); 
		shared_profileimage5 = preferences.getString(Iconstant.Session_AddImage5, ""); 
		
		shared_title = preferences.getString(Iconstant.Session_AddTitle, "");
        shared_price = preferences.getString(Iconstant.Session_AddPrice, "");
        shared_qua = preferences.getString(Iconstant.Session_AddQua, "");
        shared_des = preferences.getString(Iconstant.Session_AddDes, "");
        shared_tag = preferences.getString(Iconstant.Session_AddTag, "");
        shared_mat = preferences.getString(Iconstant.Session_AddMat, "");
        
        if (!shared_profileimage1.equals("")) {
        	
        	bitmap_image_1 = decodeBase64_1(shared_profileimage1);
        	Iv_image.setImageBitmap(bitmap_image_1);
        	
        	ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        	bitmap_image_1.compress(Bitmap.CompressFormat.JPEG, 90, stream1);
        	byteArray1 = stream1.toByteArray();             	
		}       
        if (!shared_title.equals("")) {
            Tv_Item_Title.setText(shared_title);
		}
        if (!shared_price.equals("")) {
        	Tv_price.setText(shared_price);
		}
        if (!shared_qua.equals("")) {
        	Tv_Quantity.setText(shared_qua);
		}
        if (!shared_des.equals("")) {     
            Tv_Description.setText(shared_des);
		}
        if (!shared_tag.equals("")) {               
            Tv_AddTag.setText(shared_tag);
		}
        if (!shared_mat.equals("")) {               
            Tv_AddMaterial.setText(shared_mat);
		}     
 //       Log.d("get_shared_photo_session", "" + shared_profileimage1+","+shared_profileimage2+","+shared_profileimage3+","+shared_profileimage4+","+shared_profileimage5);
		Log.d("get_shared_photo_session", "" + byteArray1+","+byteArray2+","+byteArray3+","+byteArray4+","+byteArray5);
		
		str_title = Tv_Item_Title.getText().toString();
		str_price = Tv_price.getText().toString();
		str_qua = Tv_Quantity.getText().toString();
		str_des = Tv_Description.getText().toString();
		str_tag = Tv_AddTag.getText().toString();
		str_mat = Tv_AddMaterial.getText().toString();
	}


	@SuppressWarnings("unused")
	private void Session_Insert() 
	{
		editor.putString(Iconstant.Session_AddTitle, Item_Tittle);
		editor.putString(Iconstant.Session_AddPrice, Price);
		editor.putString(Iconstant.Session_AddQua, Quantity);
		editor.putString(Iconstant.Session_AddDes, Desc);
		editor.putString(Iconstant.Session_AddTag, Add_tag);
		editor.putString(Iconstant.Session_AddMat, Add_Material);
		editor.commit();
	}

	public String encodeTobase64(Bitmap image) 
	{	
	    Bitmap immage = image;
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    immage.compress(Bitmap.CompressFormat.JPEG, 90, baos);
	    byte[] b = baos.toByteArray();
	    String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

//	    Log.d("Image Log:", imageEncoded);
	    return imageEncoded;
	}
	
	public Bitmap decodeBase64_1(String input) 
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

	@SuppressLint("WrongViewCast") 
	private void init()
	{
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle("Add Product");

		Ly_Camera = (LinearLayout) findViewById(R.id.linearLayoutcamra_additem_final);
		Ly_Item_Tittle = (LinearLayout) findViewById(R.id.linear_layout_itemtitle_additem_final);
		Ly_Description = (LinearLayout) findViewById(R.id.linear_layout_description_additem_final);
		Ly_Shipping = (LinearLayout) findViewById(R.id.layout_shipping_additem_final);
		Ly_Add_Tag = (LinearLayout) findViewById(R.id.linear_layout_addtags_additem_final);
		Ly_Add_Material = (LinearLayout) findViewById(R.id.linear_layout_materials_additem_final);
		Ly_Price = (LinearLayout) findViewById(R.id.layout_price_add_item_final);
		Ly_Quantity = (LinearLayout) findViewById(R.id.layout_quantity_additem_final);
		Tv_Item_Title = (TextView) findViewById(R.id.textView_descrs_additem_final);
		Tv_price = (TextView) findViewById(R.id.textView_price_additem_final);
		Tv_Quantity = (TextView) findViewById(R.id.textView_quantity_additem_final);
		Tv_Description = (TextView) findViewById(R.id.textView_decription_child_additem_final);
		Tv_Shipping = (TextView) findViewById(R.id.textView_shipping_child);
		Tv_AddTag = (TextView) findViewById(R.id.textView_addupto);
		Tv_AddMaterial = (TextView) findViewById(R.id.textView_adduptomaterials);
		Tv_MadeToMe = (TextView) findViewById(R.id.textView_me);
		Tv_Access = (TextView) findViewById(R.id.textView_category_additem_final);
		Tv_Sub = (TextView) findViewById(R.id.textView_subcate_additem_final);
		Tv_Sub1 = (TextView) findViewById(R.id.textView_subcate_1_additem_final);
		Tv_Manage = (TextView) findViewById(R.id.textview_manage_footer_additem_final1);
		Tv_Share = (TextView) findViewById(R.id.textview_share_footer_additem_final1);
		Iv_image = (ImageView) findViewById(R.id.imageview_camera_additem_final_additem_final);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		preferences = getSharedPreferences(Iconstant.sharedPre_Name,0);
        editor = preferences.edit();
        seller_id = preferences.getInt(Iconstant.shared_SellerId, 0);
        s_tittle = preferences.getString(Iconstant.shared_s_tittle, "");
        s_time = preferences.getString(Iconstant.shared_s_time, "");
        s_dest = preferences.getString(Iconstant.shared_s_des, "");
        s_from = preferences.getString(Iconstant.shared_s_from, "");
        s_cost = preferences.getString(Iconstant.shared_s_cost, "");
        s_ano_cost = preferences.getString(Iconstant.shared_s_ano_cost, "");
        
        Id_1 = preferences.getString(Iconstant.shared_AddItem_cat_1_id, "");
		Id_2 = preferences.getString(Iconstant.shared_AddItem_cat_2_id, "");
		Id_3 = preferences.getString(Iconstant.shared_AddItem_cat_3_id, "");
		Name_1 = preferences.getString(Iconstant.shared_AddItem_cat_1_name, "");
		Name_2 = preferences.getString(Iconstant.shared_AddItem_cat_2_name, "");
		Name_3 = preferences.getString(Iconstant.shared_AddItem_cat_3_name, "");
		Log.d("Seller_Id--->", "" +seller_id);
		Log.d("get_shared_Categories Id--->", "" + Id_1+","+Id_2+","+Id_3);
		Log.d("get_shared_Categories Name--->", "" + Name_1+","+Name_2+","+Name_3);
		Tv_Access.setText(Name_1);
		Tv_Sub.setText(Name_2);
		Tv_Sub1.setText(Name_3);

		Ly_Price.setOnClickListener(this);
		Ly_Quantity.setOnClickListener(this);
		Ly_Camera.setOnClickListener(this);
		Ly_Item_Tittle.setOnClickListener(this);
		Ly_Description.setOnClickListener(this);
		Ly_Shipping.setOnClickListener(this);
		Ly_Add_Tag.setOnClickListener(this);
		Ly_Add_Material.setOnClickListener(this);
		Tv_Item_Title.setOnClickListener(this);
		Tv_Description.setOnClickListener(this);
		Tv_Shipping.setOnClickListener(this);
		Tv_AddTag.setOnClickListener(this);
		Tv_AddMaterial.setOnClickListener(this);
		Tv_MadeToMe.setOnClickListener(this);
		Tv_Access.setOnClickListener(this);
		Tv_Sub.setOnClickListener(this);
		Tv_Sub1.setOnClickListener(this);
		Tv_Manage.setOnClickListener(this);
		Tv_Share.setOnClickListener(this);
		Iv_image.setOnClickListener(this);
		
		Tv_Shipping.setText(s_tittle+""+ s_time+""+ s_dest+""+ s_from);
		
		Bundle bundle = getIntent().getExtras();
		
		if (bundle ==null) 
		{
//			Toast.makeText(Add_Item_Final.this, "Shared_Photo is Empty",Toast.LENGTH_SHORT).show();
		}	
		else
		{
			byteArray1 = bundle.getByteArray("img1");
			byteArray2 = bundle.getByteArray("img2");
			byteArray3 = bundle.getByteArray("img3");
			byteArray4 = bundle.getByteArray("img4");
			byteArray5 = bundle.getByteArray("img5");
			if (byteArray1 !=null) 
			{
				bitmap_image_1 = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length);
				Iv_image.setImageBitmap(bitmap_image_1);
				editor.putString(Iconstant.Session_AddImage1, encodeTobase64(bitmap_image_1));
				editor.commit();
				Log.d("byteArray1--->", "" +byteArray1);
			}  
 			if (byteArray2 !=null) 
			{	
				bitmap_image_2 = BitmapFactory.decodeByteArray(byteArray2, 0, byteArray2.length);
				Iv_image.setImageBitmap(bitmap_image_2);
				editor.putString(Iconstant.Session_AddImage2, encodeTobase64(bitmap_image_2));
				editor.commit();
				Log.d("byteArray2--->", "" +byteArray2);
			}  
			if (byteArray3 !=null) 
			{
				bitmap_image_3 = BitmapFactory.decodeByteArray(byteArray3, 0, byteArray3.length);
				Iv_image.setImageBitmap(bitmap_image_3);
				editor.putString(Iconstant.Session_AddImage3, encodeTobase64(bitmap_image_3));
				editor.commit();
				Log.d("byteArray3--->", "" +byteArray3);
			}
			if (byteArray4 !=null) 
			{
				bitmap_image_4 = BitmapFactory.decodeByteArray(byteArray4, 0, byteArray4.length);
				Iv_image.setImageBitmap(bitmap_image_4);
				editor.putString(Iconstant.Session_AddImage4, encodeTobase64(bitmap_image_4));
				editor.commit();
				Log.d("byteArray4--->", "" +byteArray4);
			}
			if (byteArray5 !=null) 
			{
				bitmap_image_5 = BitmapFactory.decodeByteArray(byteArray5, 0, byteArray5.length);
				Iv_image.setImageBitmap(bitmap_image_5);
				editor.putString(Iconstant.Session_AddImage5, encodeTobase64(bitmap_image_5));
				editor.commit();
				Log.d("byteArray5--->", "" +byteArray5);
			}
			Log.d("get_shared_photo", "" + byteArray1+","+byteArray2+","+byteArray3+","+byteArray4+","+byteArray5);
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.add_item_done, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case R.id.done_item_menu:
/*			String str_title = Tv_Item_Title.getText().toString();
			String str_price = Tv_price.getText().toString();
			String str_qua = Tv_Quantity.getText().toString();
			String str_des = Tv_Description.getText().toString();
			String str_tag = Tv_AddTag.getText().toString();
			String str_mat = Tv_AddMaterial.getText().toString();*/
			Session_Value();
	        
			if (str_title.equals("")) 
			{
				Toast.makeText(Add_Item_Final_Old.this, "Tittle is Empty",Toast.LENGTH_SHORT).show();
			} else if (str_price.equals("")) 
			{
				Toast.makeText(Add_Item_Final_Old.this, "Price is Empty",Toast.LENGTH_SHORT).show();
			} else if (shared_qua.equals("")) 
			{
				Toast.makeText(Add_Item_Final_Old.this, "Quantity is Empty",Toast.LENGTH_SHORT).show();
			} else if (str_des.equals("")) 
			{
				Toast.makeText(Add_Item_Final_Old.this, "Description is Empty",Toast.LENGTH_SHORT).show();
			} else if (str_tag.equals("")) 
			{
				Toast.makeText(Add_Item_Final_Old.this, "Add Tag is Empty",Toast.LENGTH_SHORT).show();
			} else if (str_mat.equals("")) 
			{
				Toast.makeText(Add_Item_Final_Old.this, "Add Material is Empty",Toast.LENGTH_SHORT).show();
			} 
			else if (s_tittle.equals("")) 
			{
				Toast.makeText(Add_Item_Final_Old.this, "Shipping Address is Empty",Toast.LENGTH_SHORT).show();
			}
			else 
			{
				if (byteArray1 !=null) 
				{				
					Async_Task_Profile async_Task_Profile = new Async_Task_Profile();
					async_Task_Profile.execute();				
				}  
				else if (byteArray2 !=null) 
				{						
					Async_Task_Profile async_Task_Profile = new Async_Task_Profile();
					async_Task_Profile.execute();
				}  
				else if (byteArray3 !=null) 
				{		
					Async_Task_Profile async_Task_Profile = new Async_Task_Profile();
					async_Task_Profile.execute();
				}
				else if (byteArray4 !=null) 
				{				
					Async_Task_Profile async_Task_Profile = new Async_Task_Profile();
					async_Task_Profile.execute();
				}
				else if (byteArray5 !=null) 
				{				
					Async_Task_Profile async_Task_Profile = new Async_Task_Profile();
					async_Task_Profile.execute();
				}
				else
				{
					Toast.makeText(Add_Item_Final_Old.this, "Please Select The Image To Upload !!!",Toast.LENGTH_LONG).show();
				}
			}
			break;
		 case android.R.id.home:
				Remove_Shared_Value();
				Add_Item_Final_Old.this.finish();
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
		case R.id.imageview_camera_additem_final_additem_final:
			preferences= getSharedPreferences(Iconstant.sharedPre_Name, 0);
			preferences.edit().remove(Iconstant.Session_AddImage1).commit();
			preferences.edit().remove(Iconstant.Session_AddImage2).commit();
			preferences.edit().remove(Iconstant.Session_AddImage3).commit();
			preferences.edit().remove(Iconstant.Session_AddImage4).commit();
			preferences.edit().remove(Iconstant.Session_AddImage5).commit();
			
			Intent int_add_photo1 = new Intent(Add_Item_Final_Old.this,Add_item_photo.class);
			startActivity(int_add_photo1);
			overridePendingTransition(R.anim.enter,R.anim.exit);
			Add_Item_Final_Old.this.finish();
			break;
			
		case R.id.linearLayoutcamra_additem_final:
			// Toast.makeText(Add_Item_Final.this, "Coming Soon",
			// Toast.LENGTH_SHORT).show();
			preferences= getSharedPreferences(Iconstant.sharedPre_Name, 0);
			preferences.edit().remove(Iconstant.Session_AddImage1).commit();
			preferences.edit().remove(Iconstant.Session_AddImage2).commit();
			preferences.edit().remove(Iconstant.Session_AddImage3).commit();
			preferences.edit().remove(Iconstant.Session_AddImage4).commit();
			preferences.edit().remove(Iconstant.Session_AddImage5).commit();
			
			Intent int_add_photo = new Intent(Add_Item_Final_Old.this,Add_item_photo.class);
			startActivity(int_add_photo);
			overridePendingTransition(R.anim.enter,R.anim.exit);
			Add_Item_Final_Old.this.finish();
			
			break;
		case R.id.linear_layout_itemtitle_additem_final:
			// Toast.makeText(Add_Item_Final.this, "Coming Soon",
			// Toast.LENGTH_SHORT).show();
			custom_dialog_item_tittle();

			break;
		case R.id.linear_layout_description_additem_final:
			// Toast.makeText(Add_Item_Final.this, "Coming Soon",
			// Toast.LENGTH_SHORT).show();
			custom_dialog_description();

			break;
		case R.id.layout_shipping_additem_final:
			// Toast.makeText(Add_Item_Final.this, "Comming Soon",
			// Toast.LENGTH_SHORT).show();			
//			Session_Insert();
			Intent int_shipping = new Intent(Add_Item_Final_Old.this,Add_Item_shipping_Profile.class);
			startActivity(int_shipping);
			overridePendingTransition(R.anim.enter,R.anim.exit);
			Add_Item_Final_Old.this.finish();

			break;
		case R.id.linear_layout_addtags_additem_final:
			// Toast.makeText(Add_Item_Final.this, "Coming Soon",
			// Toast.LENGTH_SHORT).show();
			custom_dialog_addtag();

			break;
		case R.id.linear_layout_materials_additem_final:
			// Toast.makeText(Add_Item_Final.this, "Coming Soon",
			// Toast.LENGTH_SHORT).show();
			custom_dialog_addmaterial();

			break;
		case R.id.textView_descrs_additem_final:
			// Toast.makeText(Add_Item_Final.this, "Comming Soon",
			// Toast.LENGTH_SHORT).show();
			custom_dialog_item_tittle();

			break;
		case R.id.textView_decription_child_additem_final:
			// Toast.makeText(Add_Item_Final.this, "Comming Soon",
			// Toast.LENGTH_SHORT).show();
			custom_dialog_description();

			break;
		case R.id.textView_shipping_child:
			// Toast.makeText(Add_Item_Final.this, "Coming Soon",
			// Toast.LENGTH_SHORT).show();
//			Session_Insert();
			Intent int_shipping1 = new Intent(Add_Item_Final_Old.this,Add_Item_shipping_Profile.class);
			startActivity(int_shipping1);
			overridePendingTransition(R.anim.enter,R.anim.exit);
			Add_Item_Final_Old.this.finish();

			break;
		case R.id.textView_addupto:
			// Toast.makeText(Add_Item_Final.this, "Coming Soon",
			// Toast.LENGTH_SHORT).show();
			custom_dialog_addtag();

			break;
		case R.id.textView_adduptomaterials:
			// Toast.makeText(Add_Item_Final.this, "Coming Soon",
			// Toast.LENGTH_SHORT).show();
			custom_dialog_addmaterial();

			break;
		case R.id.textView_me:
			Toast.makeText(Add_Item_Final_Old.this, "Coming Soon",Toast.LENGTH_SHORT).show();

			break;
		case R.id.textView_category_additem_final:
			Toast.makeText(Add_Item_Final_Old.this, "Comming Soon",Toast.LENGTH_SHORT).show();

			break;

		case R.id.textView_subcate_additem_final:
			Toast.makeText(Add_Item_Final_Old.this, "Coming Soon",Toast.LENGTH_SHORT).show();

			break;
		case R.id.textView_subcate_1_additem_final:
			Toast.makeText(Add_Item_Final_Old.this, "Coming Soon",Toast.LENGTH_SHORT).show();

			break;
		case R.id.textview_manage_footer_additem_final1:
			Toast.makeText(Add_Item_Final_Old.this, "Coming Soon",Toast.LENGTH_SHORT).show();

			break;
		case R.id.textview_share_footer_additem_final1:
			Toast.makeText(Add_Item_Final_Old.this, "Coming Soon",Toast.LENGTH_SHORT).show();

			break;
		case R.id.layout_price_add_item_final:
			// Toast.makeText(Add_Item_Final.this, "Coming Soon",
			// Toast.LENGTH_SHORT).show();
			custom_dialog_price();

			break;
		case R.id.layout_quantity_additem_final:
			// Toast.makeText(Add_Item_Final.this, "Coming Soon",
			// Toast.LENGTH_SHORT).show();
			custom_dialog_quantity();

			break;

		default:
			break;
		}
	}

	private void custom_dialog_item_tittle() {
		// custom dialog
		final Dialog dialog = new Dialog(Add_Item_Final_Old.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.popup_itemtittle);
		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.textView_popup_done_itemtitle);
		@SuppressWarnings("unused")
		ImageView image = (ImageView) dialog.findViewById(R.id.imageView_popup_itemtittle);
		final EditText Editext_PopUp = (EditText) dialog.findViewById(R.id.editText_popup_itemtitle);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(Editext_PopUp, InputMethodManager.SHOW_IMPLICIT);
		
		text.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Item_Tittle = Editext_PopUp.getText().toString();
				// Toast.makeText(Add_Item_Final.this, Item_Tittle,
				// Toast.LENGTH_SHORT).show();			
				editor.putString(Iconstant.Session_AddTitle, Item_Tittle);
				editor.commit();
				Tv_Item_Title.setText(Item_Tittle);
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void custom_dialog_description() 
	{
		// custom dialog
		final Dialog dialog = new Dialog(Add_Item_Final_Old.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.popup_description);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.textView_popup_done_desc);
		final EditText Editext_PopUp = (EditText) dialog.findViewById(R.id.editText_popup_desc_itemtitle);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(Editext_PopUp, InputMethodManager.SHOW_IMPLICIT);
		text.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Desc = Editext_PopUp.getText().toString();
				// Toast.makeText(Add_Item_Final.this, Desc,
				// Toast.LENGTH_SHORT).show();
				editor.putString(Iconstant.Session_AddDes, Desc);
				editor.commit();
				Tv_Description.setText(Desc);
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void custom_dialog_addtag() 
	{
		// custom dialog
		final Dialog dialog = new Dialog(Add_Item_Final_Old.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.popup_addtag);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.textView_addtag_done);
		final EditText Editext_PopUp = (EditText) dialog.findViewById(R.id.editText_popup_addtag);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(Editext_PopUp, InputMethodManager.SHOW_IMPLICIT);
		text.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Add_tag = Editext_PopUp.getText().toString();
				// Toast.makeText(Add_Item_Final.this, Add_tag,
				// Toast.LENGTH_SHORT).show();
				editor.putString(Iconstant.Session_AddTag, Add_tag);
				editor.commit();
				Tv_AddTag.setText(Add_tag);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void custom_dialog_addmaterial() 
	{
		// custom dialog
		final Dialog dialog = new Dialog(Add_Item_Final_Old.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.popup_addmaterial);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.textView_addmaterial_done);
		final EditText Editext_PopUp = (EditText) dialog.findViewById(R.id.editText_popup_addmaterial);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(Editext_PopUp, InputMethodManager.SHOW_IMPLICIT);
		text.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Add_Material = Editext_PopUp.getText().toString();
				// Toast.makeText(Add_Item_Final.this, Add_Material,
				// Toast.LENGTH_SHORT).show();
				editor.putString(Iconstant.Session_AddMat, Add_Material);
				editor.commit();
				Tv_AddMaterial.setText(Add_Material);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void custom_dialog_price() 
	{
		// custom dialog
		final Dialog dialog = new Dialog(Add_Item_Final_Old.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.popup_price);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.textView__price_done);
		final EditText Editext_PopUp = (EditText) dialog.findViewById(R.id.editText_popup__price);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(Editext_PopUp, InputMethodManager.SHOW_IMPLICIT);
		text.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Price = Editext_PopUp.getText().toString();
				// Toast.makeText(Add_Item_Final.this, Price,
				// Toast.LENGTH_SHORT).show();
				editor.putString(Iconstant.Session_AddPrice, Price);
				editor.commit();
				Tv_price.setText(Price+" "+"$");
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void custom_dialog_quantity() 
	{
		// custom dialog
		final Dialog dialog = new Dialog(Add_Item_Final_Old.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.popup_quantity);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.textView__quantity_done);
		final EditText Editext_PopUp = (EditText) dialog.findViewById(R.id.editText_popup__quantity);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(Editext_PopUp, InputMethodManager.SHOW_IMPLICIT);
		text.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Quantity = Editext_PopUp.getText().toString();
				// Toast.makeText(Add_Item_Final.this, Quantity,
				// Toast.LENGTH_SHORT).show();
				editor.putString(Iconstant.Session_AddQua, Quantity);
				editor.commit();
				Tv_Quantity.setText(Quantity);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	class Async_Task_Profile extends AsyncTask<Object, Void, Boolean>
	{
		boolean status=false;
		String Response_Message="data", result;
		private String Response = "";
		@SuppressWarnings("unused")
		private int status_code;
		StringBuilder sbr;
		private ProgressDialog progress = null;
		private StringBuilder s;
		ArrayList<String> possibleEmail = new ArrayList<String>();

		@Override
		protected void onPreExecute() 
		{
			progress = ProgressDialog.show(Add_Item_Final_Old.this, null,"Adding Product !!!");
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Object... obj) 
		{
			try 
			{
				HttpPost postRequest = new HttpPost(Iconstant.profile_url);
				HttpClient httpClient = new DefaultHttpClient();
				httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "shopsymobileapp");
				MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				try 
				{
					if (byteArray1 !=null) 
					{
						ByteArrayOutputStream bos_image_1 = new ByteArrayOutputStream();
						bitmap_image_1.compress(CompressFormat.JPEG, 90,bos_image_1);
						byte[] data_image_1 = bos_image_1.toByteArray();
						ByteArrayBody bab_image_1 = new ByteArrayBody(data_image_1, "forest1.jpg");
						reqEntity.addPart("photo1", bab_image_1);
						Log.d("data_image_1--------->", "" + data_image_1);
					}
					if (byteArray2 !=null) 
					{
						ByteArrayOutputStream bos_image_2 = new ByteArrayOutputStream();
						bitmap_image_2.compress(CompressFormat.JPEG, 90,bos_image_2);
						byte[] data_image_2 = bos_image_2.toByteArray();
						ByteArrayBody bab_image_2 = new ByteArrayBody(data_image_2, "forest2.jpg");
						reqEntity.addPart("photo2", bab_image_2);
						Log.d("data_image_2--------->", "" + data_image_2);
					}
					if (byteArray3 !=null)
					{
						ByteArrayOutputStream bos_image_3 = new ByteArrayOutputStream();
						bitmap_image_3.compress(CompressFormat.JPEG, 90,bos_image_3);
						byte[] data_image_3 = bos_image_3.toByteArray();
						ByteArrayBody bab_image_3 = new ByteArrayBody(data_image_3, "forest3.jpg");
						reqEntity.addPart("photo3", bab_image_3);
						Log.d("data_image_3--------->", "" + data_image_3);
					}
					if (byteArray4 !=null)
					{
						ByteArrayOutputStream bos_image_4 = new ByteArrayOutputStream();
						bitmap_image_4.compress(CompressFormat.JPEG, 90,bos_image_4);
						byte[] data_image_4 = bos_image_4.toByteArray();
						ByteArrayBody bab_image_4 = new ByteArrayBody(data_image_4, "forest4.jpg");
						reqEntity.addPart("photo4", bab_image_4);
						Log.d("data_image_4--------->", "" + data_image_4);
					}
					if (byteArray5 !=null)
					{
						ByteArrayOutputStream bos_image_5 = new ByteArrayOutputStream();
						bitmap_image_5.compress(CompressFormat.JPEG, 90,bos_image_5);
						byte[] data_image_5 = bos_image_5.toByteArray();
						ByteArrayBody bab_image_5 = new ByteArrayBody(data_image_5, "forest3.jpg");
						reqEntity.addPart("photo5", bab_image_5);
						Log.d("data_image_5--------->", "" + data_image_5);
					}
					reqEntity.addPart("seller_id", new StringBody(String.valueOf(seller_id)));
					reqEntity.addPart("I_Tittle", new StringBody(str_title));
					reqEntity.addPart("I_Price", new StringBody(str_price));
					reqEntity.addPart("I_Quantity", new StringBody(str_qua));
					reqEntity.addPart("I_Desc", new StringBody(str_des));
					reqEntity.addPart("I_AddTag", new StringBody(str_tag));
					reqEntity.addPart("I_AddMat", new StringBody(str_mat));
					reqEntity.addPart("Add_Cat",new StringBody(jsonarray_cat.toString()));
					reqEntity.addPart("S_Profile",new StringBody(jsonarray_shipping.toString()));
					
					Log.d("Values--------->",""+reqEntity);
					Log.d("Json--------->",""+jsonarray_shipping);
					status = true;
				} 
				catch (Exception e) 
				{
					Log.d("Exception in Image_Upload", "" + e);
					reqEntity.addPart("picture", new StringBody(""));
				}
				postRequest.setEntity(reqEntity);
				HttpResponse response = httpClient.execute(postRequest);
				int status1 = response.getStatusLine().getStatusCode();
				Log.d("status1-------------->", "" + status1);

			    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			    s = new StringBuilder();
				while ((Response = reader.readLine()) != null) 
				{
					s = s.append(Response);
				}
				Log.d("Response--------->", "" + s);
				result = s.toString();
			    JSONObject jObject = new JSONObject(result);
			    Response_Message = jObject.getString("message");
			    Log.d("Response--Msg------->", "" + Response_Message);
				status=true;			
			} catch (Exception e)
			{
				e.getStackTrace();
			}
			return status;
		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			if (result==true) 
			{				
				if (Response_Message.equals(Iconstant.AddItemFinal_Response)) 
				{
					progress.dismiss();
					Toast.makeText(Add_Item_Final_Old.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);
					Add_Item_Final_Old.this.finish();
					
				} else 
				{
//					Toast.makeText(Add_Item_Final.this, s, Toast.LENGTH_SHORT).show();
					progress.dismiss();
					alartmsg_data_not_sent();
				}
			}
			else
			{
				progress.dismiss();
				alartmsg_no_data();
			}
			super.onPostExecute(result);
		}
	}

	private void Add_Json() 
	{
//		bitmap = BitmapFactory.decodeResource(Add_Item_Final.this.getResources(), R.drawable.ic_launcher);
		try {
			JSONObject jsonObj = new JSONObject();
			JSONObject jsonObj1 = new JSONObject();
				jsonObj.put("s_tittle", s_tittle);
				jsonObj.put("s_time", s_time);
				jsonObj.put("s_from", s_from);
				jsonObj.put("s_dest", s_dest);
				jsonObj.put("s_cost", s_cost);
				jsonObj.put("s_ano_cost", s_ano_cost);
				jsonarray_shipping.put(jsonObj);
				
				jsonObj1.put("about_1", "1");
				jsonObj1.put("about_2", "2");
				jsonObj1.put("about_3", "1");
				jsonObj1.put("cat_1", Id_1);
				jsonObj1.put("cat_2", Id_2);
				jsonObj1.put("cat_3", Id_3);
				jsonarray_cat.put(jsonObj1);

				Log.d("Json_Array_Shipping", "" + jsonarray_shipping);
				Log.d("Json_Array_Cat", "" + jsonarray_cat);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void alartmsg_no_data() 
	{
		new AlertDialog.Builder(Add_Item_Final_Old.this)
		.setTitle(Iconstant.Alart_AppName)
		.setMessage(Iconstant.Alart_Status500)
		.setCancelable(false)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{		
				
			}
		}).show();
	}
	
	private void alartmsg_data_not_sent() 
	{
		new AlertDialog.Builder(Add_Item_Final_Old.this)
		.setTitle(Iconstant.Alart_AppName)		
		.setMessage("Please Try Again")
		.setCancelable(false)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{		
				
			}
		}).show();
	}
	
	 @Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) 
	  {
	      if ((keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0))
	      {
	//    	  Toast.makeText(getApplicationContext(), "Home_Pressed", Toast.LENGTH_SHORT).show();
	          return true;
	      }
	      if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) 
	      {
	    	  Remove_Shared_Value();
	    	  overridePendingTransition(R.anim.left_right, R.anim.right_left);
	    	  Add_Item_Final_Old.this.finish();
	          return true;
	      }
	      return false;
	  }
}