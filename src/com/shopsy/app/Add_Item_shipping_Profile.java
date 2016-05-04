package com.shopsy.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopsy.R;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.app.Add_Item_Edit_Final.ADD_ADDRESS_EDIT;
import com.shopsy.app.Add_Item_Edit_Final_Copy.ADD_ADDRESS_COPY;
import com.shopsy.app.Add_Item_Final.ADD_ADDRESS;
import com.shopsy.app.Add_Item_Shipping_Profile_Detail.ADDRESS_EDIT_DETAILS;

/**
 * @author Prithivi Raj
 *
 */
public class Add_Item_shipping_Profile extends HockeyActivity 
{
	static RelativeLayout Ly_shiplogo;
	static RelativeLayout relative_profile_details;
	RelativeLayout Ly_shipping_profile;
	private SharedPreferences preferences;
	@SuppressWarnings("unused")
	private SharedPreferences.Editor editor;	
	private static TextView Tv_tittle;
	private static TextView Tv_time;
	private static TextView Tv_dest;
	private static TextView Tv_from;
	private static TextView Tv_cost;
	private static TextView Tv_ano_cost;
	private TextView TV_profile;
	private static String s_tittle;
	private static String s_time;
	private static String s_dest;
	private static String s_dest_id;
	private static String s_from;
	private static String s_cost;
	private static String s_ano_cost;
	private ImageView Iv_Erase;
	String Add_Item_Type;
	String str_static_add = "add_item_ship";
	String str_static_add_edit = "add_item_ship_edit";
	String str_static_add_edit_copy = "add_item_ship_edit_copy";
	
	String shared_profileimage;
	ActionBar actionBar;
	ColorDrawable colorDrawable = new ColorDrawable();
	
	static String str_add_country="";
	static String str_add_country_Id="";
	static String str_add_cost="";
	static String str_add_ano_cost="";
	
	static String str_add_title="";
	static String str_add_from="";
	static String str_add_time="";
	
	String[] split_Array;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item_shippingprofile);
		preferences = getSharedPreferences(Iconstant.sharedPre_Name,0);
        editor = preferences.edit();
        str_add_country = preferences.getString(Iconstant.Session_Type_country, "");
        str_add_country_Id = preferences.getString(Iconstant.Session_Type_country_Id, "");
        str_add_cost = preferences.getString(Iconstant.Session_Type_cost, "");
        str_add_ano_cost = preferences.getString(Iconstant.Session_Type_ano_cost, "");        
        str_add_title = preferences.getString(Iconstant.Session_Type_title, "");
        str_add_time = preferences.getString(Iconstant.Session_Type_time, "");
        str_add_from = preferences.getString(Iconstant.Session_Type_shipfrom, "");

        
        s_tittle = preferences.getString(Iconstant.shared_s_tittle, "");
        s_time = preferences.getString(Iconstant.shared_s_time, "");
        s_dest = preferences.getString(Iconstant.shared_s_des, "");
        s_from = preferences.getString(Iconstant.shared_s_from, "");
        s_cost = preferences.getString(Iconstant.shared_s_cost, "");
        s_ano_cost = preferences.getString(Iconstant.shared_s_ano_cost, "");       
        Add_Item_Type = preferences.getString(Iconstant.Session_Type, "");        
		shared_profileimage = preferences.getString(Iconstant.sharedPre_ProfileImage, "");
		
		 Log.d("shared_s_ano_cost---------------->:",""+s_ano_cost);
		 
		Custom_ActionBar();
        
		init();
		
		Ly_shipping_profile.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{	
				if (Add_Item_Type.equals(str_static_add)) {
					ADDRESS_EDIT_DETAILS.ADDRESS_EDIT_DET(Add_Item_Type, s_cost, s_ano_cost, s_dest);
					Intent int_profile_details = new Intent(Add_Item_shipping_Profile.this, Add_Item_Shipping_Profile_Detail.class);
					startActivity(int_profile_details);
//					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.enter,R.anim.exit);
				}if (Add_Item_Type.equals(str_static_add_edit)) {
					
					ADDRESS_EDIT_DETAILS.ADDRESS_EDIT_DET(Add_Item_Type, str_add_cost, str_add_ano_cost, str_add_country);
					Intent int_profile_details = new Intent(Add_Item_shipping_Profile.this, Add_Item_Shipping_Profile_Detail.class);
					startActivity(int_profile_details);
//					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.enter,R.anim.exit);
			    	
				}if (Add_Item_Type.equals(str_static_add_edit_copy)) {
					
					ADDRESS_EDIT_DETAILS.ADDRESS_EDIT_DET(Add_Item_Type, str_add_cost, str_add_ano_cost, str_add_country);
					Intent int_profile_details = new Intent(Add_Item_shipping_Profile.this, Add_Item_Shipping_Profile_Detail.class);
					startActivity(int_profile_details);
//					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.enter,R.anim.exit);
				}
			}
		});
		TV_profile.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{		
				if (Add_Item_Type.equals(str_static_add)) {
					ADDRESS_EDIT_DETAILS.ADDRESS_EDIT_DET(Add_Item_Type, s_cost, s_ano_cost, s_dest);
					Intent int_profile_details = new Intent(Add_Item_shipping_Profile.this, Add_Item_Shipping_Profile_Detail.class);
					startActivity(int_profile_details);
//					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.enter,R.anim.exit);
				}if (Add_Item_Type.equals(str_static_add_edit)) {
					
					ADDRESS_EDIT_DETAILS.ADDRESS_EDIT_DET(Add_Item_Type, str_add_cost, str_add_ano_cost, str_add_country);
					Intent int_profile_details = new Intent(Add_Item_shipping_Profile.this, Add_Item_Shipping_Profile_Detail.class);
					startActivity(int_profile_details);
//					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.enter,R.anim.exit);
			    	
				}if (Add_Item_Type.equals(str_static_add_edit_copy)) {
					
					ADDRESS_EDIT_DETAILS.ADDRESS_EDIT_DET(Add_Item_Type, str_add_cost, str_add_ano_cost, str_add_country);
					Intent int_profile_details = new Intent(Add_Item_shipping_Profile.this, Add_Item_Shipping_Profile_Detail.class);
					startActivity(int_profile_details);
//					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.enter,R.anim.exit);
				}
			}
		});
		
		relative_profile_details.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				
				if (Add_Item_Type.equals(str_static_add)) {
					ADDRESS_EDIT_DETAILS.ADDRESS_EDIT_DET(Add_Item_Type, s_cost, s_ano_cost, s_dest);
					Intent int_profile_details = new Intent(Add_Item_shipping_Profile.this, Add_Item_Shipping_Profile_Detail.class);
					startActivity(int_profile_details);
//					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.enter,R.anim.exit);
				}if (Add_Item_Type.equals(str_static_add_edit)) {
					
					ADDRESS_EDIT_DETAILS.ADDRESS_EDIT_DET(Add_Item_Type, str_add_cost, str_add_ano_cost, str_add_country);
					Intent int_profile_details = new Intent(Add_Item_shipping_Profile.this, Add_Item_Shipping_Profile_Detail.class);
					startActivity(int_profile_details);
//					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.enter,R.anim.exit);
			    	
				}if (Add_Item_Type.equals(str_static_add_edit_copy)) {
					
					ADDRESS_EDIT_DETAILS.ADDRESS_EDIT_DET(Add_Item_Type, str_add_cost, str_add_ano_cost, str_add_country);
					Intent int_profile_details = new Intent(Add_Item_shipping_Profile.this, Add_Item_Shipping_Profile_Detail.class);
					startActivity(int_profile_details);
//					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.enter,R.anim.exit);
				}
			}
		});
		Iv_Erase.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				
				selectImage();
			}
		});
	}

	private void Custom_ActionBar() 
	{
		actionBar = getSupportActionBar();
		getSupportActionBar().show();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
		
        LayoutInflater mInflater = LayoutInflater.from(Add_Item_shipping_Profile.this);
		View mCustomView = mInflater.inflate(R.layout.custom_action_bar_additem, null);
		ImageView imageView = (ImageView) mCustomView.findViewById(R.id.imageView1_custom_action_bar_youritem_additem);
		imageView.setImageBitmap(decodeBase64(shared_profileimage));
		TextView textView = (TextView) mCustomView.findViewById(R.id.textView1_custom_action_bar_youritem_additem);
		TextView textView_save = (TextView) mCustomView.findViewById(R.id.imageButton_youritem_grid_save);
		textView.setText("Shipping Profiles");
		imageView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				if (Add_Item_Type.equals(str_static_add_edit)) {
			        
					ADD_ADDRESS_EDIT.ADDRESS_EDIT(str_add_title, str_add_time, str_add_country, str_add_from, str_add_cost, str_add_ano_cost, str_add_country_Id);
					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);
					
				}if (Add_Item_Type.equals(str_static_add_edit_copy)) {
					ADD_ADDRESS_COPY.ADDRESS_COPY(str_add_title, str_add_time, str_add_country, str_add_from, str_add_cost, str_add_ano_cost, str_add_country_Id);
					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);
				}
				Add_Item_shipping_Profile.this.finish();
		 		overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
			}
		});
		textView_save.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				
				Save_Validation();
				}			
		});
		textView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				if (Add_Item_Type.equals(str_static_add_edit)) {
			        
					ADD_ADDRESS_EDIT.ADDRESS_EDIT(str_add_title, str_add_time, str_add_country, str_add_from, str_add_cost, str_add_ano_cost, str_add_country_Id);
					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);
					
				}if (Add_Item_Type.equals(str_static_add_edit_copy)) {
					ADD_ADDRESS_COPY.ADDRESS_COPY(str_add_title, str_add_time, str_add_country, str_add_from, str_add_cost, str_add_ano_cost, str_add_country_Id);
					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);
				}
				Add_Item_shipping_Profile.this.finish();
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
	
	private void Save_Validation() 
	{
		if (Add_Item_Type.equals(str_static_add)) {
			if (s_cost.equals("")) 
			{
				relative_profile_details.setVisibility(View.GONE);
				Animation shake = AnimationUtils.loadAnimation(Add_Item_shipping_Profile.this, R.anim.shake);		
				TV_profile.startAnimation(shake);
				Toast.makeText(Add_Item_shipping_Profile.this, "Please add your shipping details first", Toast.LENGTH_SHORT).show();
			}else{
				ADD_ADDRESS.ADDRESS(s_tittle, s_time, s_dest, s_from, s_cost, s_ano_cost, s_dest_id);
		    	Add_Item_shipping_Profile.this.finish();
		    	overridePendingTransition(R.anim.left_right, R.anim.right_left);
			}
		}
		if (Add_Item_Type.equals(str_static_add_edit)) {
		        
			ADD_ADDRESS_EDIT.ADDRESS_EDIT(str_add_title, str_add_time, str_add_country, str_add_from, str_add_cost, str_add_ano_cost, str_add_country_Id);
			Add_Item_shipping_Profile.this.finish();
			overridePendingTransition(R.anim.left_right, R.anim.right_left);
			
		}if (Add_Item_Type.equals(str_static_add_edit_copy)) {
			ADD_ADDRESS_COPY.ADDRESS_COPY(str_add_title, str_add_time, str_add_country, str_add_from, str_add_cost, str_add_ano_cost, str_add_country_Id);
			Add_Item_shipping_Profile.this.finish();
			overridePendingTransition(R.anim.left_right, R.anim.right_left);
		}
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
		getSupportActionBar().setTitle("Shipping Profiles");*/	
		Ly_shiplogo = (RelativeLayout) findViewById(R.id.shippingprofile_createshipping);
		Ly_shipping_profile = (RelativeLayout) findViewById(R.id.relativelay_shippingprofile);
		relative_profile_details = (RelativeLayout) findViewById(R.id.linearlayout_shipping_layouts);
		Tv_tittle = (TextView) findViewById(R.id.textView_shipping_profile_tittle);
		Tv_time = (TextView) findViewById(R.id.textView_shipping_profile_time);
		TV_profile = (TextView) findViewById(R.id.shippingprofile_textViewnewshippingprofile);
		Tv_dest = (TextView) findViewById(R.id.textView_shipping_profile_desc);
		Tv_from = (TextView) findViewById(R.id.textView_shipping_profile_from);
		Tv_cost = (TextView) findViewById(R.id.textView_shipping_profile_cost);
		Tv_ano_cost = (TextView) findViewById(R.id.textView_shipping_profile_ano_cost);
		Iv_Erase = (ImageView) findViewById(R.id.imageView_shipping_profile_erase);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		relative_profile_details.setVisibility(View.GONE);
		Log.d("Add_Item_Type:",""+Add_Item_Type);
				
		if (Add_Item_Type.equals(str_static_add_edit)) {
			TV_profile.setText("EDIT SHIPPING PROFILE");   
	        str_add_cost =str_add_cost.substring(0, str_add_cost.length()-2);
	        str_add_ano_cost =str_add_ano_cost.substring(0, str_add_ano_cost.length()-2);
	        str_add_country =str_add_country.substring(0, str_add_country.length()-2);
	        str_add_country_Id =str_add_country_Id.substring(0, str_add_country_Id.length()-2);

	        Log.d("str_add_cost---------------->:",""+str_add_cost);
	        Log.d("str_add_ano_cost---------------->:",""+str_add_ano_cost);
			Log.d("str_add_addrsss---------------->:",""+str_add_country);
			Log.d("str_add_country_Id---------------->:",""+str_add_country_Id);
			
			
			Iv_Erase.setVisibility(View.GONE);
			Ly_shiplogo.setVisibility(View.GONE);
			relative_profile_details.setVisibility(View.VISIBLE);
			split_Array = str_add_country.split(",");
			Tv_dest.setText(str_add_country); 
			Tv_cost.setText(str_add_cost);
			Tv_ano_cost.setText(str_add_ano_cost);
			Tv_time.setText(str_add_time);
			Tv_from.setText(str_add_from);
			Tv_tittle.setText(str_add_title);
	        
	        ADDRESS_EDIT_DETAILS.ADDRESS_EDIT_DET(Add_Item_Type, str_add_cost, str_add_ano_cost, str_add_country);
		}if (Add_Item_Type.equals(str_static_add_edit_copy)) {
			TV_profile.setText("EDIT SHIPPING PROFILE");  
			
			str_add_cost =str_add_cost.substring(0, str_add_cost.length()-2);
			str_add_ano_cost =str_add_ano_cost.substring(0, str_add_ano_cost.length()-2);
			str_add_country =str_add_country.substring(0, str_add_country.length()-2);
			str_add_country_Id =str_add_country_Id.substring(0, str_add_country_Id.length()-2);

			Log.d("str_add_cost---------------->:",""+str_add_cost);
	        Log.d("str_add_ano_cost---------------->:",""+str_add_ano_cost);
			Log.d("str_add_addrsss---------------->:",""+str_add_country);
			Log.d("str_add_country_Id---------------->:",""+str_add_country_Id);
		
			Iv_Erase.setVisibility(View.GONE);
			Ly_shiplogo.setVisibility(View.GONE);
			relative_profile_details.setVisibility(View.VISIBLE);
			split_Array = str_add_country.split(",");
			Tv_dest.setText(str_add_country); 
			Tv_cost.setText(str_add_cost);
			Tv_ano_cost.setText(str_add_ano_cost);
			Tv_time.setText(str_add_time);
			Tv_from.setText(str_add_from);
			Tv_tittle.setText(str_add_title);
		     
			ADDRESS_EDIT_DETAILS.ADDRESS_EDIT_DET(Add_Item_Type, str_add_cost, str_add_ano_cost, str_add_country);
	}
		if(Add_Item_Type.equals(str_static_add)){
			Iv_Erase.setVisibility(View.VISIBLE);
			session_validation();
		}
	}
	
	private void session_validation() 
	{		    
		Log.d("SharedPrefrences_S_Profile:",""+s_tittle);
		
		if (s_cost.equals("")) 
		{
			TV_profile.setText("NEW SHIPPING PROFILE");
			relative_profile_details.setVisibility(View.GONE);		
		}
		else
		{	
			TV_profile.setText("EDIT SHIPPING PROFILE");
			Ly_shiplogo.setVisibility(View.GONE);
			relative_profile_details.setVisibility(View.VISIBLE);
			Tv_tittle.setText(s_tittle);
			Tv_time.setText(s_time);
			Tv_dest.setText(s_dest);
			Tv_from.setText(s_from);
			
/*			String cost_append="";
			String cost_ano_append="";
			Log.d("split_Array:",""+s_cost);
			String[] split_Array = s_cost.split(",");
			String[] split_ano_Array = s_ano_cost.split(",");
			for (int i = 0; i < split_Array.length; i++) {
				cost_append+= "$"+split_Array[i]+",";
				cost_ano_append+= split_ano_Array[i]+" "+"$"+","+" ";
			}*/		
			Tv_cost.setText(s_cost);
			Tv_ano_cost.setText(s_ano_cost);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			case R.id.shipping_save:

				break;
			
			case android.R.id.home:
				if (Add_Item_Type.equals(str_static_add_edit)) {
			        
					ADD_ADDRESS_EDIT.ADDRESS_EDIT(str_add_title, str_add_time, str_add_country, str_add_from, str_add_cost, str_add_ano_cost, str_add_country_Id);
					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);
					
				}if (Add_Item_Type.equals(str_static_add_edit_copy)) {
					ADD_ADDRESS_COPY.ADDRESS_COPY(str_add_title, str_add_time, str_add_country, str_add_from, str_add_cost, str_add_ano_cost, str_add_country_Id);
					Add_Item_shipping_Profile.this.finish();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);
				}
				Add_Item_shipping_Profile.this.finish();
				overridePendingTransition(R.anim.left_right, R.anim.right_left);

				break;
		}
		return false;
	}
	
	private Void selectImage() 
	{
		final CharSequence[] items = { "Edit Profile", "Delete Profile","Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(Add_Item_shipping_Profile.this);
		builder.setTitle("Shopsy");
		builder.setItems(items, new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int item) 
			{
				if (items[item].equals("Edit Profile"))
				{
					if (Add_Item_Type.equals(str_static_add)) {
						ADDRESS_EDIT_DETAILS.ADDRESS_EDIT_DET(Add_Item_Type, s_cost, s_ano_cost, s_dest);
						Intent int_profile_details = new Intent(Add_Item_shipping_Profile.this, Add_Item_Shipping_Profile_Detail.class);
						startActivity(int_profile_details);
//						Add_Item_shipping_Profile.this.finish();
						overridePendingTransition(R.anim.enter,R.anim.exit);
					}
					dialog.dismiss();
				}
				else if (items[item].equals("Delete Profile"))
				{					
					preferences= getSharedPreferences(Iconstant.sharedPre_Name, 0);
					preferences.edit().remove(Iconstant.shared_s_tittle).commit();
					preferences.edit().remove(Iconstant.shared_s_time).commit();
					preferences.edit().remove(Iconstant.shared_s_des).commit();
					preferences.edit().remove(Iconstant.shared_s_from).commit();
					preferences.edit().remove(Iconstant.shared_s_cost).commit();
					preferences.edit().remove(Iconstant.shared_s_ano_cost).commit();
					
					Intent intent = getIntent();
					overridePendingTransition(0, 0);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					finish();
					overridePendingTransition(0, 0);
					startActivity(intent);
		        	
				} else if (items[item].equals("Cancel")) 
				{
					dialog.dismiss();
				}
			}
		});
		builder.show();
		return null;
	}
/*	
	private void delete_profile() 
	{
		new AlertDialog.Builder(Add_Item_shipping_Profile.this)
		.setTitle("Shopsy")
		.setMessage("are you sure you want to decline this shipping profile ?")
		.setCancelable(false)
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
		{			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				
			}
		})
		.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{		
				relative_profile_details.setVisibility(View.GONE);
				preferences= getSharedPreferences(IConstant.sharedPre_Name, 0);
				preferences.edit().remove(IConstant.shared_s_tittle).commit();
				preferences.edit().remove(IConstant.shared_s_time).commit();
				preferences.edit().remove(IConstant.shared_s_des).commit();
				preferences.edit().remove(IConstant.shared_s_from).commit();
				preferences.edit().remove(IConstant.shared_s_cost).commit();
				preferences.edit().remove(IConstant.shared_s_ano_cost).commit();
			}
		}).show();
	}
	*/
	
	static class GET_ADDRESS_EDIT_DETAILS
	{
		static void GET_ADDRESS_EDIT_DET(String tittle, String shipping_time, String shipping_des_Id, String shipping_des, String shipping_from, String cost, String ano_cost)
		{
	//		s_tittle,s_time,s_dest ,s_from,s_cost,s_ano_cost 
			s_tittle = tittle;
			s_time = shipping_time;
			s_dest = shipping_des;
			s_from = shipping_from;			
			s_cost = cost;
			s_ano_cost = ano_cost;
			s_dest_id = shipping_des_Id;
			
			 str_add_country = shipping_des;
			 str_add_country_Id = shipping_des_Id;
		     str_add_cost = cost;
		     str_add_ano_cost = ano_cost;      
		     str_add_title = tittle;
		     str_add_time = shipping_time;
		     str_add_from = shipping_from;
		        
			
		     Log.d("title_edit", s_tittle);
		     Log.d("s_time_edit", s_time);
		     Log.d("s_dest_edit", s_dest);
		     Log.d("s_from_edit", s_from);
		     Log.d("s_cost_edit", s_cost);
		     Log.d("s_ano_cost_edit", s_ano_cost);
		     Log.d("s_des_id_edit", s_dest_id);
			
			Ly_shiplogo.setVisibility(View.GONE);
			relative_profile_details.setVisibility(View.VISIBLE);
			Tv_tittle.setText(tittle);
			Tv_time.setText(shipping_time);
			Tv_dest.setText(shipping_des);
			Tv_from.setText(shipping_from);
			Tv_cost.setText(cost);
			Tv_ano_cost.setText(ano_cost);
		}
	}
	static class GET_ADDRESS_DETAILS
	{
		static void GET_ADDRESS_DET(String tittle, String shipping_time, String shipping_des_Id, String shipping_des, String shipping_from, String cost, String ano_cost)
		{
	//		s_tittle,s_time,s_dest ,s_from,s_cost,s_ano_cost 
			s_tittle = tittle;
			s_time = shipping_time;
			s_dest = shipping_des;
			s_from = shipping_from;			
			s_cost = cost;
			s_ano_cost = ano_cost;
			s_dest_id = shipping_des_Id;	        
			
		     Log.d("title_edit", s_tittle);
		     Log.d("s_time_edit", s_time);
		     Log.d("s_dest_edit", s_dest);
		     Log.d("s_from_edit", s_from);
		     Log.d("s_cost_edit", s_cost);
		     Log.d("s_ano_cost_edit", s_ano_cost);
		     Log.d("s_des_id_edit", s_dest_id);
			
			Ly_shiplogo.setVisibility(View.GONE);
			relative_profile_details.setVisibility(View.VISIBLE);
			Tv_tittle.setText(tittle);
			Tv_time.setText(shipping_time);
			Tv_dest.setText(shipping_des);
			Tv_from.setText(shipping_from);
			Tv_cost.setText(cost);
			Tv_ano_cost.setText(ano_cost);
		}
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
	    	  if (Add_Item_Type.equals(str_static_add_edit)) {
			        
	  			ADD_ADDRESS_EDIT.ADDRESS_EDIT(str_add_title, str_add_time, str_add_country, str_add_from, str_add_cost, str_add_ano_cost, str_add_country_Id);
	  			Add_Item_shipping_Profile.this.finish();
	  			overridePendingTransition(R.anim.left_right, R.anim.right_left);
	  			
	  		}if (Add_Item_Type.equals(str_static_add_edit_copy)) {
	  			ADD_ADDRESS_COPY.ADDRESS_COPY(str_add_title, str_add_time, str_add_country, str_add_from, str_add_cost, str_add_ano_cost, str_add_country_Id);
	  			Add_Item_shipping_Profile.this.finish();
	  			overridePendingTransition(R.anim.left_right, R.anim.right_left);
	  		}
	    	  Add_Item_shipping_Profile.this.finish();
	    	  overridePendingTransition(R.anim.left_right, R.anim.right_left);
	          return true;
	      }
	      return false;
	  }
}
