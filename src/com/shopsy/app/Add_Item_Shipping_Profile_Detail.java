package com.shopsy.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shopsy.R;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.app.Add_Item_shipping_Profile.GET_ADDRESS_DETAILS;
import com.shopsy.app.Add_Item_shipping_Profile.GET_ADDRESS_EDIT_DETAILS;
import com.shopsy.db.DBHelper;

/**
 * @author Prithivi Raj
 *
 */
public class Add_Item_Shipping_Profile_Detail extends HockeyActivity implements OnItemSelectedListener 
{
	LinearLayout rl;
	static FrameLayout Fl_1, Fl_2, Fl_3, Fl_4, Fl_5;
	RelativeLayout Rl_add;
	ImageView Iv_Erase1, Iv_Erase2, Iv_Erase3, Iv_Erase4, Iv_Erase5;
	Spinner Sp_Shipping_Dest_1, Sp_Shipping_Dest_2, Sp_Shipping_Dest_3, Sp_Shipping_Dest_4, Sp_Shipping_Dest_5;
	EditText Edtext_shipping_ano_cost_1, Edtext_shipping_ano_cost_2, Edtext_shipping_ano_cost_3, Edtext_shipping_ano_cost_4, Edtext_shipping_ano_cost_5;
	EditText Edtext_Shipping_cost_1, Edtext_Shipping_cost_2,Edtext_Shipping_cost_3,Edtext_Shipping_cost_4,Edtext_Shipping_cost_5;
	
	static EditText Edtext_profile;
	static EditText Edtext_Shipping_cost;
	static EditText Edtext_shipping_ano_cost;
	Spinner Sp_Processing_Time, Sp_Shipping_From, Sp_Shipping_Dest, Sp_Shipping_DestId;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	ArrayList<String> arrayList_country = new ArrayList<String>();
	ArrayList<String> arrayList_countryId = new ArrayList<String>();
	ArrayList<String> arrayList_country_db = new ArrayList<String>();
	ArrayList<String> arrayList_countryId_db = new ArrayList<String>();
	String str_static_add = "add_item_ship";	
	
	ArrayAdapter<String> dataAdapter1;				
	ArrayAdapter<String> dataAdapter2, dataAdapter3;
	
	List<String> list = new ArrayList<String>();
	private DBHelper helper;
	private SQLiteDatabase sql_db;
	HttpClient httpclient;
	HttpResponse response;
	HttpGet httppost;
	
	String shipping_time, shipping_from;
	static String shipping_des;
	String shipping_des_1;
	String shipping_des_2;
	String shipping_des_3;
	String shipping_des_4;
	String shipping_des_5;
	String shipping_from_Id;
	static String shipping_des_Id="";
	String shipping_des_1_Id="";
	String shipping_des_2_Id="";
	String shipping_des_3_Id="";
	String shipping_des_4_Id="";
	String shipping_des_5_Id="";
	@SuppressWarnings("unused")
	private String[] countries_list;
	
	String shared_profileimage, shared_countrylist, shared_title, shared_time, shared_ship_from, 
	shared_ship_to, shared_cost, shared_ano_cost;
	String cost_1, cost_2, cost_3, cost_4, cost_5;
	String cost_ano_1, cost_ano_2, cost_ano_3, cost_ano_4, cost_ano_5;
	ActionBar actionBar;
	ColorDrawable colorDrawable = new ColorDrawable();
	
	String Add_Item_Type;
	static String Add_Item_Type_1="";
	static String str_add_addrsss="";
	static String[] Cost_Array;
	static String[] Ano_Cost_Array;
	static String[] Country_Array;
	int i=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item_shippingprofile_details);
		preferences = getSharedPreferences(Iconstant.sharedPre_Name,0);
        editor = preferences.edit();      
        Add_Item_Type = preferences.getString(Iconstant.Session_Type, ""); 
        Log.d("Add_Item_Type_Session----->", Add_Item_Type);
        
		shared_profileimage = preferences.getString(Iconstant.sharedPre_ProfileImage, "");
		shared_countrylist = preferences.getString(Iconstant.shared_countrylist, "");
		
		shared_title = preferences.getString(Iconstant.shared_s_tittle, "");
		shared_time = preferences.getString(Iconstant.shared_s_time, "");
		shared_ship_from = preferences.getString(Iconstant.shared_s_from, "");
		shared_ship_to = preferences.getString(Iconstant.shared_s_des, "");
		shared_cost = preferences.getString(Iconstant.shared_s_cost, "");
		shared_ano_cost = preferences.getString(Iconstant.shared_s_ano_cost, "");
		
		helper = new DBHelper(Add_Item_Shipping_Profile_Detail.this);
		
		Custom_ActionBar();
		init();
//		Session_Validation(); 
		
		if (isConnectingToInternet() ==true)
		{		
			if (shared_countrylist.equals("")) {
				Async_CountryList async_CountryList = new Async_CountryList();
				async_CountryList.execute();
			} else {
				Async_CountryList_Db async_CountryList_Db = new Async_CountryList_Db();
				async_CountryList_Db.execute();
//				process();
			}
		}
		else
		{
			alartmsg(Iconstant.Alart_Internet);
		}
		list.add("Select Duration");
		list.add("1 business days");
		list.add("1-2 business days");
		list.add("1-3 business days");
		list.add("3-5 business days");
		list.add("1-2 weeks");
		list.add("2-3 weeks");
		list.add("3-4 weeks");
		list.add("4-6 weeks");
		list.add("6-8 weeks");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Sp_Processing_Time.setAdapter(dataAdapter);
		Sp_Processing_Time.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> parent,View view, int position, long id)
			{
				
				shipping_time = parent.getItemAtPosition(position).toString();
//			 Toast.makeText(Add_Item_Shipping_Profile_Detail.this, shipping_time, Toast.LENGTH_SHORT).show();
			}

			public void onNothingSelected(AdapterView<?> parent) 
			{
				// showToast("Spinner1: unselected");
			}
		});
		
		Sp_Shipping_From.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@SuppressWarnings("static-access")
			public void onItemSelected(AdapterView<?> parent,View view, int position, long id)
			{
				
				shipping_from = parent.getItemAtPosition(position).toString();
				String selectQuery1 = "SELECT * FROM " + helper.TABLE + " WHERE name='" + shipping_from + "'";
				sql_db = helper.getWritableDatabase();
				Cursor cursor1 = sql_db.rawQuery(selectQuery1, null);
				if (cursor1.moveToFirst()) {
					do {
						shipping_from_Id = cursor1.getString(cursor1.getColumnIndex("id"));
						Log.d("DataBase_Value_Get_Id", ""+ shipping_from_Id);
					} while (cursor1.moveToNext());
				}
			}

			public void onNothingSelected(AdapterView<?> parent) 
			{
						// showToast("Spinner1: unselected");
			}
		});
		
		Sp_Shipping_Dest.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@SuppressWarnings("static-access")
			public void onItemSelected(AdapterView<?> parent,View view, int position, long id)
			{
				shipping_des = parent.getItemAtPosition(position).toString();
				String selectQuery1 = "SELECT * FROM " + helper.TABLE + " WHERE name='" + shipping_des + "'";
				sql_db = helper.getWritableDatabase();
				Cursor cursor1 = sql_db.rawQuery(selectQuery1, null);
				if (cursor1.moveToFirst()) {
					do {
						shipping_des_Id = cursor1.getString(cursor1.getColumnIndex("id"));
						Log.d("DataBase_Value_Get_Id", ""+ shipping_des_Id);
					} while (cursor1.moveToNext());
				}
				shipping_des = shipping_des.replace(",", "");
				Log.d("shipping_des", ""+ shipping_des);
			}

			public void onNothingSelected(AdapterView<?> parent) 
			{
			}
		});

		Sp_Shipping_Dest_1.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> parent,View view, int position, long id)
			{
				shipping_des_1 = parent.getItemAtPosition(position).toString();
				@SuppressWarnings("static-access")
				String selectQuery1 = "SELECT * FROM " + helper.TABLE + " WHERE name='" + shipping_des_1 + "'";
				sql_db = helper.getWritableDatabase();
				Cursor cursor1 = sql_db.rawQuery(selectQuery1, null);
				if (cursor1.moveToFirst()) {
					do {
						shipping_des_1_Id = cursor1.getString(cursor1.getColumnIndex("id"));
						Log.d("DataBase_Value_Get_Id", ""+ shipping_des_1_Id);
					} while (cursor1.moveToNext());
				}
				shipping_des_1 = shipping_des_1.replace(",", "");
			}

			public void onNothingSelected(AdapterView<?> parent) 
			{
			}
		});
		Sp_Shipping_Dest_2.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> parent,View view, int position, long id)
			{
				shipping_des_2 = parent.getItemAtPosition(position).toString();
				@SuppressWarnings("static-access")
				String selectQuery1 = "SELECT * FROM " + helper.TABLE + " WHERE name='" + shipping_des_2 + "'";
				sql_db = helper.getWritableDatabase();
				Cursor cursor1 = sql_db.rawQuery(selectQuery1, null);
				if (cursor1.moveToFirst()) {
					do {
						shipping_des_2_Id = cursor1.getString(cursor1.getColumnIndex("id"));
						Log.d("DataBase_Value_Get_Id", ""+ shipping_des_2_Id);
					} while (cursor1.moveToNext());
				}
				shipping_des_2 = shipping_des_2.replace(",", "");
			}

			public void onNothingSelected(AdapterView<?> parent) 
			{
			}
		});
		Sp_Shipping_Dest_3.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> parent,View view, int position, long id)
			{
				shipping_des_3 = parent.getItemAtPosition(position).toString();
				@SuppressWarnings("static-access")
				String selectQuery1 = "SELECT * FROM " + helper.TABLE + " WHERE name='" + shipping_des_3 + "'";
				sql_db = helper.getWritableDatabase();
				Cursor cursor1 = sql_db.rawQuery(selectQuery1, null);
				if (cursor1.moveToFirst()) {
					do {
						shipping_des_3_Id = cursor1.getString(cursor1.getColumnIndex("id"));
						Log.d("DataBase_Value_Get_Id", ""+ shipping_des_3_Id);
					} while (cursor1.moveToNext());
				}
				shipping_des_3 = shipping_des_3.replace(",", "");
			}

			public void onNothingSelected(AdapterView<?> parent) 
			{
			}
		});
		Sp_Shipping_Dest_4.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> parent,View view, int position, long id)
			{
				shipping_des_4 = parent.getItemAtPosition(position).toString();
				@SuppressWarnings("static-access")
				String selectQuery1 = "SELECT * FROM " + helper.TABLE + " WHERE name='" + shipping_des_4 + "'";
				sql_db = helper.getWritableDatabase();
				Cursor cursor1 = sql_db.rawQuery(selectQuery1, null);
				if (cursor1.moveToFirst()) {
					do {
						shipping_des_4_Id = cursor1.getString(cursor1.getColumnIndex("id"));
						Log.d("DataBase_Value_Get_Id", ""+ shipping_des_4_Id);
					} while (cursor1.moveToNext());
				}
				shipping_des_4 = shipping_des_4.replace(",", "");
			}

			public void onNothingSelected(AdapterView<?> parent) 
			{
			}
		});
		Sp_Shipping_Dest_5.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> parent,View view, int position, long id)
			{
				shipping_des_5 = parent.getItemAtPosition(position).toString();
				@SuppressWarnings("static-access")
				String selectQuery1 = "SELECT * FROM " + helper.TABLE + " WHERE name='" + shipping_des_5 + "'";
				sql_db = helper.getWritableDatabase();
				Cursor cursor1 = sql_db.rawQuery(selectQuery1, null);
				if (cursor1.moveToFirst()) {
					do {
						shipping_des_5_Id = cursor1.getString(cursor1.getColumnIndex("id"));
						Log.d("DataBase_Value_Get_Id", ""+ shipping_des_5_Id);
					} while (cursor1.moveToNext());
				}
				shipping_des_5 = shipping_des_5.replace(",", "");
			}

			public void onNothingSelected(AdapterView<?> parent) 
			{
			}
		});
		
		Iv_Erase1.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				if (Fl_1.getVisibility()==View.VISIBLE) {
					Fl_1.setVisibility(View.GONE); 
					Edtext_Shipping_cost_1.setText("");
					Edtext_shipping_ano_cost_1.setText("");
				/*	shipping_des="";
					shipping_des_Id="";*/
				}
			}
		});
		Iv_Erase2.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				if (Fl_2.getVisibility()==View.VISIBLE) {
					Fl_2.setVisibility(View.GONE);
					Edtext_Shipping_cost_2.setText("");
					 Edtext_shipping_ano_cost_2.setText("");
			/*		 shipping_des="";
						shipping_des_Id="";*/
				}
			}
		});
		Iv_Erase3.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				if (Fl_3.getVisibility()==View.VISIBLE) {
					Fl_3.setVisibility(View.GONE); 
					Edtext_Shipping_cost_3.setText("");
					 Edtext_shipping_ano_cost_3.setText("");
			/*		 shipping_des="";
						shipping_des_Id="";*/
				}
			}
		});
		Iv_Erase4.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				if (Fl_4.getVisibility()==View.VISIBLE) {
					Fl_4.setVisibility(View.GONE); 
					Edtext_Shipping_cost_4.setText("");
					 Edtext_shipping_ano_cost_4.setText("");
			/*		 shipping_des="";
						shipping_des_Id="";*/
				}
			}
		});
		Iv_Erase5.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				if (Fl_5.getVisibility()==View.VISIBLE) {
					Fl_5.setVisibility(View.GONE); 
					Edtext_Shipping_cost_5.setText("");
					 Edtext_shipping_ano_cost_5.setText("");
				/*	 shipping_des="";
						shipping_des_Id="";*/
				}
			}
		});
		
		Rl_add.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (i==1) {
					i++;
					Fl_1.setVisibility(View.VISIBLE);
				}else if (i==2) {
					i++;
					Fl_2.setVisibility(View.VISIBLE);
				}else if (i==3) {
					i++;
					Fl_3.setVisibility(View.VISIBLE);
				}else if (i==4) {
					i++;
					Fl_4.setVisibility(View.VISIBLE);
				}else if (i==5) {
					i++;
					Fl_5.setVisibility(View.VISIBLE);
				}				
			}
		});
	}
	
/*	private void Session_Validation() {
		Log.d("Add_Item_Type:",""+Add_Item_Type);
		if (Add_Item_Type.equals("add_item_ship_edit")) {

		}else{
			Edtext_profile.setText(shared_title);
			Edtext_Shipping_cost.setText(shared_cost);
			Edtext_shipping_ano_cost.setText(shared_ano_cost); 
		}
	}*/

	private void Custom_ActionBar() 
	{
		actionBar = getSupportActionBar();
		getSupportActionBar().show();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
		
        LayoutInflater mInflater = LayoutInflater.from(Add_Item_Shipping_Profile_Detail.this);
		View mCustomView = mInflater.inflate(R.layout.custom_action_bar_additem, null);
		ImageView imageView = (ImageView) mCustomView.findViewById(R.id.imageView1_custom_action_bar_youritem_additem);
		imageView.setImageBitmap(decodeBase64(shared_profileimage));
		TextView textView = (TextView) mCustomView.findViewById(R.id.textView1_custom_action_bar_youritem_additem);
		TextView textView_save = (TextView) mCustomView.findViewById(R.id.imageButton_youritem_grid_save);
		textView.setText("New Shipping Profile");
		imageView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
/*				Intent int_profile_details = new Intent(Add_Item_Shipping_Profile_Detail.this, Add_Item_shipping_Profile.class);
		    	startActivity(int_profile_details);*/
		    	Add_Item_Shipping_Profile_Detail.this.finish();
		    	overridePendingTransition(R.anim.left_right, R.anim.right_left);
			}
		});
		textView_save.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
			    
				Validation();
			}
		});
		textView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
/*				Intent int_profile_details = new Intent(Add_Item_Shipping_Profile_Detail.this, Add_Item_shipping_Profile.class);
		    	startActivity(int_profile_details);*/
		    	Add_Item_Shipping_Profile_Detail.this.finish();
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
/*		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle("New Shipping Profile");*/
		rl = (LinearLayout) findViewById(R.id.relativelauy_pop_shiping_all);
		Iv_Erase1 = (ImageView) findViewById(R.id.imageView1_shipping_cost_another_shippingporfil1);
		Fl_1 = (FrameLayout) findViewById(R.id.layout_frame_newShippingprofille1);	
		Sp_Shipping_Dest_1 = (Spinner) findViewById(R.id.spinner_des_newshippingprofileshipsfrom1);
		Edtext_Shipping_cost_1 = (EditText) findViewById(R.id.editText_shipping_cost_shippingporfil1);
		Edtext_shipping_ano_cost_1 = (EditText) findViewById(R.id.editText_shipping_cost_another_shippingporfil1);
		
		Iv_Erase2 = (ImageView) findViewById(R.id.imageView1_shipping_cost_another_shippingporfil2);
		Fl_2 = (FrameLayout) findViewById(R.id.layout_frame_newShippingprofille2);	
		Sp_Shipping_Dest_2 = (Spinner) findViewById(R.id.spinner_des_newshippingprofileshipsfrom2);
		Edtext_Shipping_cost_2 = (EditText) findViewById(R.id.editText_shipping_cost_shippingporfil2);
		Edtext_shipping_ano_cost_2 = (EditText) findViewById(R.id.editText_shipping_cost_another_shippingporfil2);
		
		Iv_Erase3 = (ImageView) findViewById(R.id.imageView1_shipping_cost_another_shippingporfil3);
		Fl_3 = (FrameLayout) findViewById(R.id.layout_frame_newShippingprofille3);	
		Sp_Shipping_Dest_3 = (Spinner) findViewById(R.id.spinner_des_newshippingprofileshipsfrom3);
		Edtext_Shipping_cost_3 = (EditText) findViewById(R.id.editText_shipping_cost_shippingporfil3);
		Edtext_shipping_ano_cost_3 = (EditText) findViewById(R.id.editText_shipping_cost_another_shippingporfil3);
		
		Iv_Erase4 = (ImageView) findViewById(R.id.imageView1_shipping_cost_another_shippingporfil4);
		Fl_4 = (FrameLayout) findViewById(R.id.layout_frame_newShippingprofille4);	
		Sp_Shipping_Dest_4 = (Spinner) findViewById(R.id.spinner_des_newshippingprofileshipsfrom4);
		Edtext_Shipping_cost_4 = (EditText) findViewById(R.id.editText_shipping_cost_shippingporfil4);
		Edtext_shipping_ano_cost_4 = (EditText) findViewById(R.id.editText_shipping_cost_another_shippingporfil4);
		
		Iv_Erase5 = (ImageView) findViewById(R.id.imageView1_shipping_cost_another_shippingporfil5);
		Fl_5 = (FrameLayout) findViewById(R.id.layout_frame_newShippingprofille5);	
		Sp_Shipping_Dest_5 = (Spinner) findViewById(R.id.spinner_des_newshippingprofileshipsfrom5);
		Edtext_Shipping_cost_5 = (EditText) findViewById(R.id.editText_shipping_cost_shippingporfil5);
		Edtext_shipping_ano_cost_5 = (EditText) findViewById(R.id.editText_shipping_cost_another_shippingporfil5);
		
		Rl_add = (RelativeLayout) findViewById(R.id.layout_newshippingAddlocation);
		Edtext_profile = (EditText) findViewById(R.id.editText_shippingporfil);
		Edtext_Shipping_cost = (EditText) findViewById(R.id.editText_shipping_cost_shippingporfil);
		Edtext_shipping_ano_cost = (EditText) findViewById(R.id.editText_shipping_cost_another_shippingporfil);
		Sp_Processing_Time = (Spinner) findViewById(R.id.spinner_newshippingprofileprocesstime);
		Sp_Shipping_From = (Spinner) findViewById(R.id.spinner_newshippingprofileshipsfrom);
		Sp_Shipping_Dest = (Spinner) findViewById(R.id.spinner_des_newshippingprofileshipsfrom);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		preferences = getSharedPreferences(Iconstant.sharedPre_Name,0);
        editor = preferences.edit();
		countries_list = getResources().getStringArray(R.array.countries_list);
		
		Sp_Processing_Time.setOnItemSelectedListener(this);
		
		if (Add_Item_Type_1.equals("add_item_ship_edit"))  
		{
			Log.d("Add_Item_Type_1------------>",""+Add_Item_Type_1);
			Log.d("Cost_Array.length------->", ""+ Cost_Array.length);
			
			for (int i = 0; i < Cost_Array.length; i++) {
				 if (Cost_Array.length == 1) 
				 {
					 Edtext_Shipping_cost.setText(Cost_Array[0]);
					 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
//					 Fl_1.setVisibility(View.VISIBLE);
				 }if (Cost_Array.length == 2) 
				 {
					 Edtext_Shipping_cost.setText(Cost_Array[0]);
					 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
					 
					 Edtext_Shipping_cost_1.setText(Cost_Array[1]);
					 Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
					 Fl_1.setVisibility(View.VISIBLE);
				 } if (Cost_Array.length == 3) 
				 {
					 Edtext_Shipping_cost.setText(Cost_Array[0]);
					 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
					 Edtext_Shipping_cost_1.setText(Cost_Array[1]);
					 Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
					 
					 Edtext_Shipping_cost_2.setText(Cost_Array[2]);
					 Edtext_shipping_ano_cost_2.setText(Ano_Cost_Array[2]);
					 
					 Fl_1.setVisibility(View.VISIBLE);
					 Fl_2.setVisibility(View.VISIBLE);
				 } if (Cost_Array.length == 4) 
				 {
					 if (!Cost_Array[0].equals("")) {
						 Edtext_Shipping_cost.setText(Cost_Array[0]);
						 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
					}
					 if (!Cost_Array[1].equals("")) {
						Edtext_Shipping_cost_1.setText(Cost_Array[1]);
						Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
					}
					 Edtext_Shipping_cost_2.setText(Cost_Array[2]);
					 Edtext_shipping_ano_cost_2.setText(Ano_Cost_Array[2]);
					 
					 Edtext_Shipping_cost_3.setText(Cost_Array[3]);
					 Edtext_shipping_ano_cost_3.setText(Ano_Cost_Array[3]);
					 
					 Fl_1.setVisibility(View.VISIBLE);
					 Fl_2.setVisibility(View.VISIBLE);
					 Fl_3.setVisibility(View.VISIBLE);
				 } if (Cost_Array.length == 5) 
				 {
					 Edtext_Shipping_cost.setText(Cost_Array[0]);
					 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
					 Edtext_Shipping_cost_1.setText(Cost_Array[1]);
					 Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
					 Edtext_Shipping_cost_2.setText(Cost_Array[2]);
					 Edtext_shipping_ano_cost_2.setText(Ano_Cost_Array[2]);
					 Edtext_Shipping_cost_3.setText(Cost_Array[3]);
					 Edtext_shipping_ano_cost_3.setText(Ano_Cost_Array[3]);
					 
					 Edtext_Shipping_cost_4.setText(Cost_Array[4]);
					 Edtext_shipping_ano_cost_4.setText(Ano_Cost_Array[4]);
					 
					 Fl_1.setVisibility(View.VISIBLE);
					 Fl_2.setVisibility(View.VISIBLE);
					 Fl_3.setVisibility(View.VISIBLE);
					 Fl_4.setVisibility(View.VISIBLE);
				 }if (Cost_Array.length == 6) 
				 {
					 Edtext_Shipping_cost.setText(Cost_Array[0]);
					 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
					 Edtext_Shipping_cost_1.setText(Cost_Array[1]);
					 Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
					 Edtext_Shipping_cost_2.setText(Cost_Array[2]);
					 Edtext_shipping_ano_cost_2.setText(Ano_Cost_Array[2]);
					 Edtext_Shipping_cost_3.setText(Cost_Array[3]);
					 Edtext_shipping_ano_cost_3.setText(Ano_Cost_Array[3]);				 
					 Edtext_Shipping_cost_4.setText(Cost_Array[4]);
					 Edtext_shipping_ano_cost_4.setText(Ano_Cost_Array[4]);
					 
					 Edtext_Shipping_cost_5.setText(Cost_Array[5]);
					 Edtext_shipping_ano_cost_5.setText(Ano_Cost_Array[5]);
					 
					 Fl_1.setVisibility(View.VISIBLE);
					 Fl_2.setVisibility(View.VISIBLE);
					 Fl_3.setVisibility(View.VISIBLE);
					 Fl_4.setVisibility(View.VISIBLE);
					 Fl_5.setVisibility(View.VISIBLE);
				 }
			}
		}
			if (Add_Item_Type_1.equals("add_item_ship_edit_copy"))  
			{
				Log.d("Add_Item_Type_1------------>",""+Add_Item_Type_1);
				Log.d("Cost_Array.length------->", ""+ Cost_Array.length);
				
				for (int i = 0; i < Cost_Array.length; i++) {
					 if (Cost_Array.length == 1) 
					 {
						 Edtext_Shipping_cost.setText(Cost_Array[0]);
						 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
//						 Fl_1.setVisibility(View.VISIBLE);
					 }if (Cost_Array.length == 2) 
					 {
						 Edtext_Shipping_cost.setText(Cost_Array[0]);
						 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
						 
						 Edtext_Shipping_cost_1.setText(Cost_Array[1]);
						 Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
						 Fl_1.setVisibility(View.VISIBLE);
					 } if (Cost_Array.length == 3) 
					 {
						 Edtext_Shipping_cost.setText(Cost_Array[0]);
						 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
						 Edtext_Shipping_cost_1.setText(Cost_Array[1]);
						 Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
						 
						 Edtext_Shipping_cost_2.setText(Cost_Array[2]);
						 Edtext_shipping_ano_cost_2.setText(Ano_Cost_Array[2]);
						 
						 Fl_1.setVisibility(View.VISIBLE);
						 Fl_2.setVisibility(View.VISIBLE);
					 } if (Cost_Array.length == 4) 
					 {
						 if (!Cost_Array[0].equals("")) {
							 Edtext_Shipping_cost.setText(Cost_Array[0]);
							 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
						}
						 if (!Cost_Array[1].equals("")) {
							Edtext_Shipping_cost_1.setText(Cost_Array[1]);
							Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
						}
						 Edtext_Shipping_cost_2.setText(Cost_Array[2]);
						 Edtext_shipping_ano_cost_2.setText(Ano_Cost_Array[2]);
						 
						 Edtext_Shipping_cost_3.setText(Cost_Array[3]);
						 Edtext_shipping_ano_cost_3.setText(Ano_Cost_Array[3]);
						 
						 Fl_1.setVisibility(View.VISIBLE);
						 Fl_2.setVisibility(View.VISIBLE);
						 Fl_3.setVisibility(View.VISIBLE);
					 } if (Cost_Array.length == 5) 
					 {
						 Edtext_Shipping_cost.setText(Cost_Array[0]);
						 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
						 Edtext_Shipping_cost_1.setText(Cost_Array[1]);
						 Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
						 Edtext_Shipping_cost_2.setText(Cost_Array[2]);
						 Edtext_shipping_ano_cost_2.setText(Ano_Cost_Array[2]);
						 Edtext_Shipping_cost_3.setText(Cost_Array[3]);
						 Edtext_shipping_ano_cost_3.setText(Ano_Cost_Array[3]);
						 
						 Edtext_Shipping_cost_4.setText(Cost_Array[4]);
						 Edtext_shipping_ano_cost_4.setText(Ano_Cost_Array[4]);
						 
						 Fl_1.setVisibility(View.VISIBLE);
						 Fl_2.setVisibility(View.VISIBLE);
						 Fl_3.setVisibility(View.VISIBLE);
						 Fl_4.setVisibility(View.VISIBLE);
					 }if (Cost_Array.length == 6) 
					 {
						 Edtext_Shipping_cost.setText(Cost_Array[0]);
						 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
						 Edtext_Shipping_cost_1.setText(Cost_Array[1]);
						 Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
						 Edtext_Shipping_cost_2.setText(Cost_Array[2]);
						 Edtext_shipping_ano_cost_2.setText(Ano_Cost_Array[2]);
						 Edtext_Shipping_cost_3.setText(Cost_Array[3]);
						 Edtext_shipping_ano_cost_3.setText(Ano_Cost_Array[3]);				 
						 Edtext_Shipping_cost_4.setText(Cost_Array[4]);
						 Edtext_shipping_ano_cost_4.setText(Ano_Cost_Array[4]);
						 
						 Edtext_Shipping_cost_5.setText(Cost_Array[5]);
						 Edtext_shipping_ano_cost_5.setText(Ano_Cost_Array[5]);
						 
						 Fl_1.setVisibility(View.VISIBLE);
						 Fl_2.setVisibility(View.VISIBLE);
						 Fl_3.setVisibility(View.VISIBLE);
						 Fl_4.setVisibility(View.VISIBLE);
						 Fl_5.setVisibility(View.VISIBLE);
					 }
				}
			}
			if (Add_Item_Type_1.equals("add_item_ship"))  
			{
				Log.d("Add_Item_Type_1------------>",""+Add_Item_Type_1);
				Log.d("Cost_Array.length------->", ""+ Cost_Array.length);
				
				for (int i = 0; i < Cost_Array.length; i++) {
					 if (Cost_Array.length == 1) 
					 {
						 Edtext_Shipping_cost.setText(Cost_Array[0]);
						 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
//						 Fl_1.setVisibility(View.VISIBLE);
					 }if (Cost_Array.length == 2) 
					 {
						 Edtext_Shipping_cost.setText(Cost_Array[0]);
						 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
						 
						 Edtext_Shipping_cost_1.setText(Cost_Array[1]);
						 Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
						 Fl_1.setVisibility(View.VISIBLE);
					 } if (Cost_Array.length == 3) 
					 {
						 Edtext_Shipping_cost.setText(Cost_Array[0]);
						 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
						 Edtext_Shipping_cost_1.setText(Cost_Array[1]);
						 Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
						 
						 Edtext_Shipping_cost_2.setText(Cost_Array[2]);
						 Edtext_shipping_ano_cost_2.setText(Ano_Cost_Array[2]);
						 
						 Fl_1.setVisibility(View.VISIBLE);
						 Fl_2.setVisibility(View.VISIBLE);
					 } if (Cost_Array.length == 4) 
					 {
						 if (!Cost_Array[0].equals("")) {
							 Edtext_Shipping_cost.setText(Cost_Array[0]);
							 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
						}
						 if (!Cost_Array[1].equals("")) {
							Edtext_Shipping_cost_1.setText(Cost_Array[1]);
							Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
						}
						 Edtext_Shipping_cost_2.setText(Cost_Array[2]);
						 Edtext_shipping_ano_cost_2.setText(Ano_Cost_Array[2]);
						 
						 Edtext_Shipping_cost_3.setText(Cost_Array[3]);
						 Edtext_shipping_ano_cost_3.setText(Ano_Cost_Array[3]);
						 
						 Fl_1.setVisibility(View.VISIBLE);
						 Fl_2.setVisibility(View.VISIBLE);
						 Fl_3.setVisibility(View.VISIBLE);
					 } if (Cost_Array.length == 5) 
					 {
						 Edtext_Shipping_cost.setText(Cost_Array[0]);
						 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
						 Edtext_Shipping_cost_1.setText(Cost_Array[1]);
						 Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
						 Edtext_Shipping_cost_2.setText(Cost_Array[2]);
						 Edtext_shipping_ano_cost_2.setText(Ano_Cost_Array[2]);
						 Edtext_Shipping_cost_3.setText(Cost_Array[3]);
						 Edtext_shipping_ano_cost_3.setText(Ano_Cost_Array[3]);
						 
						 Edtext_Shipping_cost_4.setText(Cost_Array[4]);
						 Edtext_shipping_ano_cost_4.setText(Ano_Cost_Array[4]);
						 
						 Fl_1.setVisibility(View.VISIBLE);
						 Fl_2.setVisibility(View.VISIBLE);
						 Fl_3.setVisibility(View.VISIBLE);
						 Fl_4.setVisibility(View.VISIBLE);
					 }if (Cost_Array.length == 6) 
					 {
						 Edtext_Shipping_cost.setText(Cost_Array[0]);
						 Edtext_shipping_ano_cost.setText(Ano_Cost_Array[0]);
						 Edtext_Shipping_cost_1.setText(Cost_Array[1]);
						 Edtext_shipping_ano_cost_1.setText(Ano_Cost_Array[1]);
						 Edtext_Shipping_cost_2.setText(Cost_Array[2]);
						 Edtext_shipping_ano_cost_2.setText(Ano_Cost_Array[2]);
						 Edtext_Shipping_cost_3.setText(Cost_Array[3]);
						 Edtext_shipping_ano_cost_3.setText(Ano_Cost_Array[3]);				 
						 Edtext_Shipping_cost_4.setText(Cost_Array[4]);
						 Edtext_shipping_ano_cost_4.setText(Ano_Cost_Array[4]);
						 
						 Edtext_Shipping_cost_5.setText(Cost_Array[5]);
						 Edtext_shipping_ano_cost_5.setText(Ano_Cost_Array[5]);
						 
						 Fl_1.setVisibility(View.VISIBLE);
						 Fl_2.setVisibility(View.VISIBLE);
						 Fl_3.setVisibility(View.VISIBLE);
						 Fl_4.setVisibility(View.VISIBLE);
						 Fl_5.setVisibility(View.VISIBLE);
					 }
				}
			}
		}	
	
	private void process() 
	{
		
		arrayList_country_db.add("select Country");
		arrayList_countryId_db.add("select Country");
		 @SuppressWarnings("static-access")
			String selectQuery = "SELECT * FROM "+ helper.TABLE;
	        sql_db = helper.getWritableDatabase();
	        Cursor cursor = null;
	         try 
	         {
				cursor = sql_db.rawQuery(selectQuery, null);        
				if (cursor.moveToFirst()) 
				{
					do 
					{
						arrayList_country_db.add(cursor.getString(cursor.getColumnIndex("name"))); 
						arrayList_countryId_db.add(cursor.getString(cursor.getColumnIndex("id")));
//		            	Log.d("arrayList_db_countrylist:",""+ arrayList_countryId_db);
//		            	Log.d("arrayList_db_countrylist--size:",""+ arrayList_country_db.size());
					} 
					while (cursor.moveToNext());
				}
	         }  finally 
	         {
			    // this gets called even if there is an exception somewhere above
			    if(cursor != null)
			        cursor.close();
	         }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
//		MenuInflater inflate = getSupportMenuInflater();
//		inflate.inflate(R.menu.menu_shipping_details_save, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId())
		{
		case R.id.shipping_save:
			
			Validation();
			break;
		case android.R.id.home:
/*			Intent int_profile_details = new Intent(Add_Item_Shipping_Profile_Detail.this, Add_Item_shipping_Profile.class);
	    	startActivity(int_profile_details);*/
	    	Add_Item_Shipping_Profile_Detail.this.finish();
	    	overridePendingTransition(R.anim.left_right, R.anim.right_left);

			break;
		}
		return false;
	}

	private void Validation() 
	{
		String tittle= Edtext_profile.getText().toString();
		String cost = Edtext_Shipping_cost.getText().toString();
		String ano_cost = Edtext_shipping_ano_cost.getText().toString();
		
		if (Fl_1.getVisibility()==View.VISIBLE) {
			cost_1 = Edtext_Shipping_cost_1.getText().toString();
			cost_ano_1 = Edtext_shipping_ano_cost_1.getText().toString();
			if (!cost_1.equals("")) {
				cost+=","+cost_1;
				shipping_des+=","+shipping_des_1;
				shipping_des_Id+=","+shipping_des_1_Id;
				
				if (!cost_ano_1.equals("")) {
					ano_cost+=","+cost_ano_1;
				}else
					ano_cost+=","+" ";
			}
			
		}if (Fl_2.getVisibility()==View.VISIBLE) {
			cost_2 = Edtext_Shipping_cost_2.getText().toString();
			cost_ano_2 = Edtext_shipping_ano_cost_2.getText().toString();
			if (!cost_2.equals("")) {
				cost+=","+cost_2;
				shipping_des+=","+shipping_des_2;
				shipping_des_Id+=","+shipping_des_2_Id;
				
				if (!cost_ano_2.equals("")) {
					ano_cost+=","+cost_ano_2;
				}else
					ano_cost+=","+" ";
			}
			
		}if (Fl_3.getVisibility()==View.VISIBLE) {
			cost_3 = Edtext_Shipping_cost_3.getText().toString();
			cost_ano_3 = Edtext_shipping_ano_cost_3.getText().toString();
			if (!cost_3.equals("")) {
				cost+=","+cost_3;
				shipping_des+=","+shipping_des_3;
				shipping_des_Id+=","+shipping_des_3_Id;
				
				if (!cost_ano_3.equals("")) {
					ano_cost+=","+cost_ano_3;
				}else
					ano_cost+=","+" ";
			}
			
		}if (Fl_4.getVisibility()==View.VISIBLE) {
			cost_4 = Edtext_Shipping_cost_4.getText().toString();
			cost_ano_4 = Edtext_shipping_ano_cost_4.getText().toString();
			if (!cost_4.equals("")) {
				cost+=","+cost_4;
				shipping_des+=","+shipping_des_4;
				shipping_des_Id+=","+shipping_des_4_Id;
				
				if (!cost_ano_4.equals("")) {
					ano_cost+=","+cost_ano_4;
				}else
					ano_cost+=","+" ";
			}
			
		}if (Fl_5.getVisibility()==View.VISIBLE) {
			cost_5 = Edtext_Shipping_cost_5.getText().toString();
			cost_ano_5 = Edtext_shipping_ano_cost_5.getText().toString();
			if (!cost_5.equals("")) {
				cost+=","+cost_5;			
				shipping_des+=","+shipping_des_5;
				shipping_des_Id+=","+shipping_des_5_Id;
				
				if (!cost_ano_5.equals("")) {
					ano_cost+=","+cost_ano_5;
				}else
					ano_cost+=","+" ";
			}
		}
		if(!shipping_time.equalsIgnoreCase("Select Duration"))
		{
			if(!shipping_des.equalsIgnoreCase("select Country"))
			{
				if(!shipping_from.equalsIgnoreCase("select Country"))
				{
		
		
		if (!cost.equals("")) {
				if (Add_Item_Type.equals(str_static_add)) {

					editor.putString(Iconstant.shared_s_tittle, tittle);
					editor.putString(Iconstant.shared_s_time, shipping_time);
					editor.putString(Iconstant.shared_s_des, shipping_des);
					editor.putString(Iconstant.shared_s_desId, shipping_des_Id);
					editor.putString(Iconstant.shared_s_from, shipping_from);
					editor.putString(Iconstant.shared_s_cost, cost);
					editor.putString(Iconstant.shared_s_ano_cost, ano_cost);
					editor.commit();
					
					GET_ADDRESS_DETAILS.GET_ADDRESS_DET(tittle, shipping_time, shipping_des_Id, shipping_des,shipping_from, cost, ano_cost);
					Log.d("Shipping-add_item-Profile---->", "" + tittle + ","+ shipping_time + "," + shipping_des_Id + ","+ shipping_des + "," + shipping_from + "," + cost+ "," + ano_cost);
					
					Add_Item_Shipping_Profile_Detail.this.finish();
					overridePendingTransition(R.anim.left_right,R.anim.right_left);
				}
				if (Add_Item_Type_1.equals("add_item_ship_edit")) {
					GET_ADDRESS_EDIT_DETAILS.GET_ADDRESS_EDIT_DET(tittle, shipping_time, shipping_des_Id, shipping_des,shipping_from, cost, ano_cost);
					Log.d("Shipping-edit-Profile---->", "" + tittle + ","+ shipping_time + "," + shipping_des_Id + ","+ shipping_des + "," + shipping_from + "," + cost+ "," + ano_cost);
					Log.d("title", tittle);
					Log.d("s_time", shipping_time);
					Log.d("s_dest", shipping_des);
					Log.d("s_from", shipping_from);
					Log.d("s_cost", cost);
					Log.d("s_ano_cost", ano_cost);
					Log.d("s_des_id", shipping_des_Id);
					Add_Item_Shipping_Profile_Detail.this.finish();
					overridePendingTransition(R.anim.left_right,R.anim.right_left);
					
				}if (Add_Item_Type_1.equals("add_item_ship_edit_copy")) {
					GET_ADDRESS_EDIT_DETAILS.GET_ADDRESS_EDIT_DET(tittle, shipping_time, shipping_des_Id, shipping_des,shipping_from, cost, ano_cost);
					Log.d("Shipping--edit--copy--Profile---->", "" + tittle + ","+ shipping_time + "," + shipping_des_Id + ","+ shipping_des + "," + shipping_from + "," + cost+ "," + ano_cost);
					Log.d("title", tittle);
					Log.d("s_time", shipping_time);
					Log.d("s_dest", shipping_des);
					Log.d("s_from", shipping_from);
					Log.d("s_cost", cost);
					Log.d("s_ano_cost", ano_cost);
					Log.d("s_des_id", shipping_des_Id);
					Add_Item_Shipping_Profile_Detail.this.finish();
					overridePendingTransition(R.anim.left_right,R.anim.right_left);
				}
			}else
			{
				Toast.makeText(Add_Item_Shipping_Profile_Detail.this, "Please add the cost", Toast.LENGTH_SHORT).show();
		}
				}
				else
				{
					Toast.makeText(Add_Item_Shipping_Profile_Detail.this, "Please add the shipping Country", Toast.LENGTH_SHORT).show();
			}
			}
			else
			{
				Toast.makeText(Add_Item_Shipping_Profile_Detail.this, "Please add the shipping Country", Toast.LENGTH_SHORT).show();
		}
		}
	else
	{
		Toast.makeText(Add_Item_Shipping_Profile_Detail.this, "Please add the shipping time", Toast.LENGTH_SHORT).show();
}
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
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
/*	    	  Intent int_profile_details = new Intent(Add_Item_Shipping_Profile_Detail.this, Add_Item_shipping_Profile.class);
	    	  startActivity(int_profile_details);*/
	    	  Add_Item_Shipping_Profile_Detail.this.finish();
	    	  overridePendingTransition(R.anim.left_right, R.anim.right_left);
	          return true;
	      }
	      return false;
	  }
	 
	 class Async_CountryList extends AsyncTask<Void, Void, Boolean>
	 {
			private ProgressDialog progress = null;
			boolean status=false;
			boolean status_empty=false;
			int status1;
			String Url  = Iconstant.country_list;
			
			@Override
			protected void onPreExecute() 
			{
				arrayList_country.clear();
				arrayList_country.add("select Country");
				arrayList_countryId.add("select Country");
				
				progress = ProgressDialog.show(Add_Item_Shipping_Profile_Detail.this, null, "Loading CountryList !!!");
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.setCancelable(false);
				progress.setOnCancelListener(new DialogInterface.OnCancelListener()
	            {
	                @Override
	                public void onCancel(DialogInterface dialog)
	                {
	                	if (httppost !=null)
	            		{
	            			httppost.abort();
	            			httpclient.getConnectionManager().shutdown();
//	            			async_Stats.cancel(true);
	            		}
	                    cancel(false);
	                    alartmsg_reguest_canclled();
	                }
	            });
	            progress.show();

				super.onPreExecute();
			}
			
			@Override
			protected Boolean doInBackground(Void... params) 
			{
				try 
				{
					Log.e("Country-Url--------->",""+Url);
					httppost = new HttpGet(Url);
					httpclient = new DefaultHttpClient();
					httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "shopsymobileapp");
					response = httpclient.execute(httppost);
					status1 = response.getStatusLine().getStatusCode();

					if (status1 == 200) 
					{
						HttpEntity entity = response.getEntity();
						String data = EntityUtils.toString(entity);

						JSONObject jsono = new JSONObject(data);
						JSONArray jarray = jsono.getJSONArray("countryList");
						Log.d("Json_Array_countryList-------->","" + jarray);
						
						if (jarray.length() >0) 
						{
							for (int i = 0; i < jarray.length(); i++) 
							{
								JSONObject object = jarray.getJSONObject(i);	
								arrayList_country.add(object.getString("country_name"));
								arrayList_countryId.add(object.getString("id"));
								String country_name = object.getString("country_name");
								String countryId = object.getString("id");
								ContentValues values = new ContentValues();
								values.put(DBHelper.C_NAME, country_name);	
								values.put(DBHelper.C_id, countryId);
								sql_db = helper.getWritableDatabase();
								sql_db.insert(DBHelper.TABLE, null, values);
							}
							Log.d("arrayList_countrylist--size:",""+ arrayList_country.size());
							status = true;	
							status_empty = true;	
						}					
						else
						{
							status = true;
							status_empty = false;
						}
					}
					else
						status = false;
				} 
				catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return status;
			}

			@Override
			protected void onPostExecute(Boolean result) 
			{
				if (result==true) 
				{
					if (status_empty) 
					{
						ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country);
						dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						Sp_Shipping_From.setAdapter(dataAdapter1);
						dataAdapter1.notifyDataSetChanged();
												
						ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country);
						dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						Sp_Shipping_Dest.setAdapter(dataAdapter2);
						dataAdapter2.notifyDataSetChanged();
						
						ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country);
						dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						Sp_Shipping_Dest_1.setAdapter(dataAdapter3);
						dataAdapter3.notifyDataSetChanged();
						
						ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country);
						dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						Sp_Shipping_Dest_2.setAdapter(dataAdapter4);
						dataAdapter4.notifyDataSetChanged();
						
						ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country);
						dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						Sp_Shipping_Dest_3.setAdapter(dataAdapter5);
						dataAdapter5.notifyDataSetChanged();						
						
						ArrayAdapter<String> dataAdapter6 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country);
						dataAdapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						Sp_Shipping_Dest_4.setAdapter(dataAdapter6);
						dataAdapter6.notifyDataSetChanged();
						
						ArrayAdapter<String> dataAdapter7 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country);
						dataAdapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						Sp_Shipping_Dest_5.setAdapter(dataAdapter7);
						dataAdapter7.notifyDataSetChanged();
						
						editor.putString(Iconstant.shared_countrylist, "country_list"); 
						editor.commit();
						progress.dismiss();
					}
					else
					{
						progress.dismiss();
						alartmsg_no_data();
					}
				}
				else
				{
					progress.dismiss();
					alartmsg(Iconstant.Alart_Status500);
				}
				super.onPostExecute(result);
			}	
		}
	 
	 class Async_CountryList_Db extends AsyncTask<Void, Void, Boolean>
	 {
			private ProgressDialog progress = null;
			boolean status=false;
			boolean status_empty=false;
			int status1;
			
			@Override
			protected void onPreExecute() 
			{
				progress = ProgressDialog.show(Add_Item_Shipping_Profile_Detail.this, null, "Getting CountryList !!!");
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.setCancelable(false);
	            progress.show();

				super.onPreExecute();
			}
			
			@Override
			protected Boolean doInBackground(Void... params) 
			{
				process();
				status = true;
				return status;
			}

			@Override
			protected void onPostExecute(Boolean result) 
			{
				if (result==true) 
				{
					dataAdapter1 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country_db);
					dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					Sp_Shipping_From.setAdapter(dataAdapter1);
					dataAdapter1.notifyDataSetChanged();					
					
					dataAdapter2 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country_db);
					dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					Sp_Shipping_Dest.setAdapter(dataAdapter2);
					dataAdapter2.notifyDataSetChanged();
					
					ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country_db);
					dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					Sp_Shipping_Dest_1.setAdapter(dataAdapter3);
					dataAdapter3.notifyDataSetChanged();
					
					ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country_db);
					dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					Sp_Shipping_Dest_2.setAdapter(dataAdapter4);
					dataAdapter4.notifyDataSetChanged();
					
					ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country_db);
					dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					Sp_Shipping_Dest_3.setAdapter(dataAdapter5);
					dataAdapter5.notifyDataSetChanged();						
					
					ArrayAdapter<String> dataAdapter6 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country_db);
					dataAdapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					Sp_Shipping_Dest_4.setAdapter(dataAdapter6);
					dataAdapter6.notifyDataSetChanged();
					
					ArrayAdapter<String> dataAdapter7 = new ArrayAdapter<String>(Add_Item_Shipping_Profile_Detail.this, android.R.layout.simple_spinner_item, arrayList_country_db);
					dataAdapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					Sp_Shipping_Dest_5.setAdapter(dataAdapter7);
					dataAdapter7.notifyDataSetChanged();
					
					progress.dismiss();
				}
				else
				{
					progress.dismiss();
				}
				super.onPostExecute(result);
			}	
		}
	 
	 public Boolean isConnectingToInternet()
		{
			Boolean isConnected = false;
			try 
			{
				ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				isConnected = info != null && info.isAvailable()&& info.isConnected();
				return isConnected;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			Log.d("isConnected: ", "" + isConnected);
			return isConnected;
		}
	 
	 private void alartmsg(String alartStatus500) 
		{
			new AlertDialog.Builder(Add_Item_Shipping_Profile_Detail.this)
			.setIcon(R.drawable.smile_sad)
			.setTitle(Iconstant.Alart_AppName)
			.setMessage(alartStatus500)
			.setCancelable(false)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
							// getActivity().getFragmentManager().popBackStack();
				}
			}).show();
		}

	 private void alartmsg_reguest_canclled() 
		{
			new AlertDialog.Builder(Add_Item_Shipping_Profile_Detail.this)
			.setTitle(Iconstant.Alart_AppName)
			.setMessage("You cancelled the request try again !!!")
			.setCancelable(false)
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{	
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					Add_Item_Shipping_Profile_Detail.this.finish();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);
				}
			})
			.setPositiveButton("Ok", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which) 
				{	
					Async_CountryList async_CountryList = new Async_CountryList();
					async_CountryList.execute();
				}
			}).show();
		}
		
		private void alartmsg_no_data() 
		{
			new AlertDialog.Builder(Add_Item_Shipping_Profile_Detail.this)
			.setTitle("Shopsy")
			.setMessage("Sorry No data for countryList")
			.setCancelable(false)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which) 
				{		
//					Add_Item_Shipping_Profile_Detail.this.finish();
				}
			}).show();
		}
		
		static class ADDRESS_EDIT_DETAILS
		{
			static void ADDRESS_EDIT_DET(String add_Item_Type, String str_add_cost, String str_add_ano_cost, String str_add_country) 
			{
				shipping_des="";
				shipping_des_Id="";
				Add_Item_Type_1 = add_Item_Type;
				 Log.d("str_type----shipdetail-------->",""+Add_Item_Type_1);
				 Log.d("str_cost-----shipdetail------->",""+str_add_cost);
				 Log.d("str_add_ano_cost-----shipdetail------->",""+str_add_ano_cost);
				 Log.d("str_add_country-----shipdetail------->",""+str_add_country);
				 
				 Cost_Array = str_add_cost.split(",");
				 Ano_Cost_Array = str_add_ano_cost.split(",");
				 Country_Array = str_add_country.split(",");
/*				 Log.d("Country_Array------------>",""+Cost_Array);
				 Log.d("Ano_Cost_Array------------>",""+Ano_Cost_Array);
				 Log.d("Country_Array------------>",""+Country_Array);
				 
				 for (int i = 0; i < Country_Array.length; i++) {
					 if (Country_Array.length == 2) 
					 {
						 Fl_2.setVisibility(View.VISIBLE);
					 }
				}
*/
			}
		}
}