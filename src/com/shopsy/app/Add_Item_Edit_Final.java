package com.shopsy.app;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ApiUtils;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.shopsy.R;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Utils.SessionManager;
import com.shopsy.loader.ImageLoader;
import com.shopsy.seller.adapter.ImageAdapter_Bitmap_Edit;
import com.shopsy.seller.adapter.ImageAdapter_Edit;
/**
 * @author Prem Kumar
 *
 */

@SuppressWarnings("deprecation")
public class Add_Item_Edit_Final extends HockeyActivity implements OnClickListener, OnShowcaseEventListener
{	
	static ShowcaseView sv;
	private final ApiUtils apiUtils = new ApiUtils();
	private static final float ALPHA_DIM_VALUE = 0.1f;
	View view;
	static String shared_tour="";
	RelativeLayout Rl_all;
	 
	static Activity context;
	static ViewPager viewPager;
	LinearLayout Ly_Camera, Ly_Description, Ly_Shipping,Ly_Add_Tag, Ly_Add_Material, Ly_Item_Tittle, Ly_product;
	RelativeLayout Ly_Price, Ly_Quantity;
	TextView Tv_Item_Title, Tv_Description;
	static TextView Tv_Shipping;
	TextView Tv_AddTag;
	TextView Tv_AddMaterial;
	static TextView Tv_MadeToMe;
	static TextView Tv_Access;
	static TextView Tv_Sub;
	static TextView Tv_Sub1; 
	TextView Tv_Manage;
	TextView Tv_Share;
	TextView Tv_price;
	TextView Tv_Quantity;
	TextView Tv_Price_Sym;
	TextView Tv_cat1, tv_cat2, tv_cat3;
	static ImageView Iv_image;
	private static SharedPreferences preferences;
	private static SharedPreferences.Editor editor;
	private JSONArray jsonarray_shipping;
	private JSONArray jsonarray_cat;
	JSONObject jsonObj_shiping;
	private String Item_Tittle = "", Price = "", Quantity = "", Desc = "",Add_tag = "", Add_Material = "";
	private static String s_tittle;
	private static String s_time;
	private static String s_dest=""; 
	private static String s_from;
	private static String s_cost="";
	private static String s_ano_cost="";
	private static String s_des_id="";
	@SuppressWarnings("unused")
	private String get_shared_photo;
	private static Bitmap bitmap_image_1=null;
	private static Bitmap bitmap_image_2=null;
	private static Bitmap bitmap_image_3=null;
	private static Bitmap bitmap_image_4=null;
	private static Bitmap bitmap_image_5=null;
	static byte[] byteArray1=null;
	static byte[] byteArray2=null;
	static byte[] byteArray3=null; 
	static byte[] byteArray4=null;
	static byte[] byteArray5=null;
	String seller_id;
	String Prod_Id;
	String shared_title, shared_price, shared_qua, shared_des, shared_tag, shared_mat;
	String shared_profileimage1="", shared_profileimage2="", shared_profileimage3="", shared_profileimage4="", shared_profileimage5="";
	String str_title, str_price, str_qua, str_des, str_tag, str_mat, str_ship;
	String str_editable;
	private static String Id_1;
	private static String Id_2;
	private static String Id_3;
	private static String Name_1;
	private static String Name_2;
	private static String Name_3;
	private static int Add_1, Add_2, Add_3;
	
	ArrayList<String> array_tag; 
	ArrayList<String> array_mat; 
	static ArrayList<String> array_image =new ArrayList<String>(); 
	static ArrayList<Bitmap> array_bitmap = new ArrayList<Bitmap>();
	
	HttpClient httpclient;
	HttpResponse response;
	HttpGet httppost;
	
	public ImageLoader imageLoader;
	static ImageAdapter_Edit adapter;
	static ImageAdapter_Bitmap_Edit adapter_bitmap;

	String p_pro_id, p_pro_title, p_productshareUrl, p_productshareTitle, p_price, p_displayPrice, p_currencySymbol, p_currencyCode,
	p_description, p_quantity, p_shipfrom, p_shopId, p_readytoShip, p_image1="", p_image2="", p_image3="", p_image4="", p_image5="",  p_shipping_country="", 
	p_shipping_cost="", p_shipping_cost_other="", p_tag, p_mat;
	
	static String about_1;
	static String about_2;
	static String about_3;
	static String cat_1;
	static String cat_2;
	static String cat_3;
	static String catName_1="";
	static String catName_2="";
	static String catName_3="";
	
	static String str_add_addrsss=""; 
	int count=1;
	
	String shared_profileimage;
	ActionBar actionBar;
	ColorDrawable colorDrawable = new ColorDrawable();
	
	private SessionManager session;
	
	public class RefreshReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.package.UPDATE_EDITPHOTOPLUS")) {

				IMAGE_GET_EDITNEW(intent.getByteArrayExtra("byte1"),intent.getByteArrayExtra("byte2"),intent.getByteArrayExtra("byte3"),intent.getByteArrayExtra("byte4"),intent.getByteArrayExtra("byte5"));
			}
		}
	}

	private RefreshReceiver updatePotoReceiver;
	
	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item_edit_final);
		context = Add_Item_Edit_Final.this;
		preferences = getSharedPreferences(Iconstant.sharedPre_Name,0);
        editor = preferences.edit();
		shared_profileimage = preferences.getString(Iconstant.sharedPre_ProfileImage, "");
        //seller_id = preferences.getInt(Iconstant.shared_SellerId, 0);
        shared_tour = preferences.getString(Iconstant.sharedPre_tour_edit, "");
		Custom_ActionBar();
		
		
		// -----code to refresh drawer using broadcast receiver-----
				updatePotoReceiver = new RefreshReceiver();
		     		IntentFilter intentFilter = new IntentFilter();
		     		intentFilter.addAction("com.package.UPDATE_EDITPHOTOPLUS");
		     		registerReceiver(updatePotoReceiver, intentFilter);
		
		init();
		
		if (isConnectingToInternet() ==true)
		{				
			Async_Dispute_Edit_Pro async_Dispute_Edit_Pro = new Async_Dispute_Edit_Pro();
			async_Dispute_Edit_Pro.execute();
		}
		else
		{
			alartmsg(Iconstant.Alart_Internet);
		}
		Log.d("shared_tour---->name",""+shared_tour);
		 //       Toast.makeText(context, "Activity", Toast.LENGTH_SHORT).show();
		        
					/*if (shared_tour.equals("")) {   
						RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						int margin = ((Number) (getResources().getDisplayMetrics().density * 12))
								.intValue();
						lps.setMargins(margin, margin, margin, margin);
						final View selectedView = findViewById(R.id.view_pager);
						ViewTarget target = new ViewTarget(selectedView);
						sv = new ShowcaseView.Builder(this, true)
								//.setTarget(target).setContentTitle("View product image")
								.setContentText("Long press the image to view large")
								.setShowcaseEventListener(this).build();
						sv.setButtonPosition(lps);
					}*/
		
		// somewhere where you setup your viewPager add this
				viewPager.setOnTouchListener(
						new View.OnTouchListener() {
							private boolean moved;
							
							@Override
							public boolean onTouch(View view, MotionEvent motionEvent) {
								if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
									moved = false;
								}
								if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
									moved = true;
								}
								if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
									if (!moved) {
										view.performClick();
									}
								}
								
								return false;
							}
						}
						);
				
				// then you can simply use the standard onClickListener ...
				viewPager.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view) {
						Intent int_add_photo1 = new Intent(Add_Item_Edit_Final.this,Add_item_photo.class);
						int_add_photo1.putExtra("ADD_ITEM_FINAL", "add_item_edit");
						startActivity(int_add_photo1);
						overridePendingTransition(R.anim.enter,R.anim.exit);
					}
				});
	}

	private void Custom_ActionBar() 
	{
		actionBar = getSupportActionBar();
		getSupportActionBar().show();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
		
        LayoutInflater mInflater = LayoutInflater.from(Add_Item_Edit_Final.this);
		View mCustomView = mInflater.inflate(R.layout.custom_action_bar_additem, null);
		ImageView imageView = (ImageView) mCustomView.findViewById(R.id.imageView1_custom_action_bar_youritem_additem);
		imageView.setImageBitmap(decodeBase64(shared_profileimage));
		TextView textView = (TextView) mCustomView.findViewById(R.id.textView1_custom_action_bar_youritem_additem);
		TextView textView_save = (TextView) mCustomView.findViewById(R.id.imageButton_youritem_grid_save);
		textView.setText("Listing");
		imageView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				Add_Item_Edit_Final.this.finish();
		 		overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
			}
		});
		textView_save.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				
				if (str_editable.equals("Yes")) 
				{
					// TODO Auto-generated method stub
/*				     Bitmap bitmap = Iv_image.getDrawingCache();
					 ByteArrayOutputStream stream = new ByteArrayOutputStream();
					 bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
					 byte[] byteArray = stream.toByteArray(); 
					 byteArray1 = byteArray;
*/
					Add_Json();
					str_title = Tv_Item_Title.getText().toString();
					str_price = Tv_price.getText().toString();
					str_qua = Tv_Quantity.getText().toString();
					str_des = Tv_Description.getText().toString();
					str_tag = Tv_AddTag.getText().toString();
					str_mat = Tv_AddMaterial.getText().toString();
					str_ship = Tv_Shipping.getText().toString();
					Log.d("str_title--------->", "" + str_title);
					Log.d("str_price--------->", "" + str_price);
					Log.d("str_qua--------->", "" + str_qua);
					Log.d("str_des--------->", "" + str_des);
					Log.d("str_tag--------->", "" + str_tag);
					Log.d("str_mat--------->", "" + str_mat);
					Log.d("str_ship--------->", "" + str_ship);
					
					if (str_title.equals("")) 
					{
						Toast.makeText(Add_Item_Edit_Final.this,"Title is Empty", Toast.LENGTH_SHORT).show();
					} else if (str_price.equals(""))
					{
						Toast.makeText(Add_Item_Edit_Final.this,"Price is Empty", Toast.LENGTH_SHORT).show();
					} else if (str_qua.equals(""))
					{
						Toast.makeText(Add_Item_Edit_Final.this,"Quantity is Empty", Toast.LENGTH_SHORT).show();
					} else if (str_des.equals("")) 
					{
						Toast.makeText(Add_Item_Edit_Final.this,"Description is Empty", Toast.LENGTH_SHORT).show();
					} else if (str_ship.equals("")) 
					{
						Toast.makeText(Add_Item_Edit_Final.this,"Shipping Address is Empty", Toast.LENGTH_SHORT).show();
					}
					else 
					{
						if (isConnectingToInternet() == true)
						{
							Async_Task_Profile async_Task_Profile = new Async_Task_Profile();
							async_Task_Profile.execute();
						} else 
						{
							alartmsg();
						}
					}
				}else
					Toast.makeText(Add_Item_Edit_Final.this,"This product is not editable", Toast.LENGTH_SHORT).show();
				
			}
		});
		textView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				Add_Item_Edit_Final.this.finish();
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
		getSupportActionBar().setTitle("Listing");*/
		
		session = new SessionManager(Add_Item_Edit_Final.this);
		
		imageLoader=new ImageLoader(Add_Item_Edit_Final.this);
		Rl_all = (RelativeLayout) findViewById(R.id.layout_price_add_item_final_all_final);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		Ly_product = (LinearLayout) findViewById(R.id.layoutproduct);
		Ly_Camera = (LinearLayout) findViewById(R.id.linearLayoutcamra_additem_final);
		Ly_Item_Tittle = (LinearLayout) findViewById(R.id.linear_layout_itemtitle_additem_final);
		Ly_Description = (LinearLayout) findViewById(R.id.linear_layout_description_additem_final);
		Ly_Shipping = (LinearLayout) findViewById(R.id.layout_shipping_additem_final);
		Ly_Add_Tag = (LinearLayout) findViewById(R.id.linear_layout_addtags_additem_final);
		Ly_Add_Material = (LinearLayout) findViewById(R.id.linear_layout_materials_additem_final);
		Ly_Price = (RelativeLayout) findViewById(R.id.layout_price_add_item_final);
		Ly_Quantity = (RelativeLayout) findViewById(R.id.layout_quantity_additem_final);
		Tv_Item_Title = (TextView) findViewById(R.id.textView_descrs_additem_final);
		Tv_price = (TextView) findViewById(R.id.textView_price_additem_final);
		Tv_Quantity = (TextView) findViewById(R.id.textView_quantity_additem_final);
		Tv_Price_Sym = (TextView) findViewById(R.id.textView_price_additem_final_currency_sum);
		Tv_Description = (TextView) findViewById(R.id.textView_decription_child_additem_final);
		Tv_Shipping = (TextView) findViewById(R.id.textView_shipping_child);
		Tv_AddTag = (TextView) findViewById(R.id.textView_addupto);
		Tv_AddMaterial = (TextView) findViewById(R.id.textView_adduptomaterials);
		Tv_MadeToMe = (TextView) findViewById(R.id.textView_madetoorder);
		Tv_Access = (TextView) findViewById(R.id.textView_category_additem_final);
		Tv_Sub = (TextView) findViewById(R.id.textView_subcate_additem_final);
		Tv_Sub1 = (TextView) findViewById(R.id.textView_subcate_1_additem_final);
		Tv_Manage = (TextView) findViewById(R.id.textview_manage_footer_additem_final1);
		Tv_Share = (TextView) findViewById(R.id.textview_share_footer_additem_final1);
		Iv_image = (ImageView) findViewById(R.id.imageview_camera_additem_final_additem_final);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
/*		preferences = getSharedPreferences(IConstant.sharedPre_Name,0);
        editor = preferences.edit();
        s_tittle = preferences.getString(IConstant.shared_s_tittle, "");
        s_time = preferences.getString(IConstant.shared_s_time, "");
        s_dest = preferences.getString(IConstant.shared_s_des, "");
        s_from = preferences.getString(IConstant.shared_s_from, "");
        s_cost = preferences.getString(IConstant.shared_s_cost, "");
        s_ano_cost = preferences.getString(IConstant.shared_s_ano_cost, "");*/

		
		
		// get user data from session
				HashMap<String, String> user = session.getUserDetails();
				seller_id = user.get(SessionManager.KEY_USERID);
		
		Log.d("Seller_Id--->", "" +seller_id);

		Ly_product.setOnClickListener(this);
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
		
//		Tv_Shipping.setText(s_tittle+""+ s_time+""+ s_dest+""+ s_from);
		
		Bundle bundle = getIntent().getExtras();
		
		if (bundle ==null) {
	
		}else{
			Prod_Id = bundle.getString("EDIT_PROID"); 
			Log.e("get_shared_photo------------------", "" + Prod_Id);
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
//		MenuInflater inflate = getSupportMenuInflater();
//		inflate.inflate(R.menu.add_item_done, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case R.id.done_item_menu:
			break;
		 case android.R.id.home:
				Add_Item_Edit_Final.this.finish();
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
		case R.id.layoutproduct:
			
			Remove_session();
			Intent int_additem = new Intent(Add_Item_Edit_Final.this, Add_Item_1_Edit.class);
			startActivity(int_additem);
			overridePendingTransition(R.anim.enter,R.anim.exit);
		
		break;
		
		case R.id.imageview_camera_additem_final_additem_final:
			
			Intent int_add_photo1 = new Intent(Add_Item_Edit_Final.this,Add_item_photo.class);
			int_add_photo1.putExtra("ADD_ITEM_FINAL", "add_item_edit");
			startActivity(int_add_photo1);
			overridePendingTransition(R.anim.enter,R.anim.exit);
			break;
			
		case R.id.linearLayoutcamra_additem_final:
			
			Intent int_add_photo = new Intent(Add_Item_Edit_Final.this,Add_item_photo.class);
			int_add_photo.putExtra("ADD_ITEM_FINAL", "add_item_edit");
			startActivity(int_add_photo);
			overridePendingTransition(R.anim.enter,R.anim.exit);
			
			break;
		case R.id.linear_layout_itemtitle_additem_final:
			custom_dialog_item_tittle();

			break;
		case R.id.linear_layout_description_additem_final:
			custom_dialog_description();

			break;
		case R.id.layout_shipping_additem_final:
			
			Log.d("Additem_edit_address--------------", str_add_addrsss);
			
			preferences.edit().remove(Iconstant.Session_Type).commit();
			preferences.edit().remove(Iconstant.Session_Type_country).commit();
			preferences.edit().remove(Iconstant.Session_Type_country_Id).commit();
			preferences.edit().remove(Iconstant.Session_Type_cost).commit();
			preferences.edit().remove(Iconstant.Session_Type_ano_cost).commit();
			preferences.edit().remove(Iconstant.Session_Type_title).commit();
			preferences.edit().remove(Iconstant.Session_Type_time).commit();
			preferences.edit().remove(Iconstant.Session_Type_shipfrom).commit();
			
			Log.e("count--------------",""+ count);
			if (count==1) {
				editor.putString(Iconstant.Session_Type, "add_item_ship_edit");
				editor.putString(Iconstant.Session_Type_country, s_dest);
				editor.putString(Iconstant.Session_Type_country_Id, s_des_id);
				editor.putString(Iconstant.Session_Type_cost, s_cost);
				editor.putString(Iconstant.Session_Type_ano_cost, s_ano_cost);
				editor.putString(Iconstant.Session_Type_title, "");
				editor.putString(Iconstant.Session_Type_time, s_time);
				editor.putString(Iconstant.Session_Type_shipfrom, s_from);
				editor.commit();
				count++;
			}else
			{
				editor.putString(Iconstant.Session_Type, "add_item_ship_edit");
				editor.putString(Iconstant.Session_Type_country, s_dest+", ");
				editor.putString(Iconstant.Session_Type_country_Id, s_des_id+", ");
				editor.putString(Iconstant.Session_Type_cost, s_cost+", ");
				editor.putString(Iconstant.Session_Type_ano_cost, s_ano_cost+", ");
				editor.putString(Iconstant.Session_Type_title, "");
				editor.putString(Iconstant.Session_Type_time, s_time);
				editor.putString(Iconstant.Session_Type_shipfrom, s_from);
				editor.commit();
			}
			Intent int_shipping = new Intent(Add_Item_Edit_Final.this,Add_Item_shipping_Profile.class);
			int_shipping.putExtra("ADD_ITEM_FINAL_SHIP_Details",s_dest);
			int_shipping.putExtra("ADD_ITEM_FINAL_SHIP_Details_cost",s_cost);
			int_shipping.putExtra("ADD_ITEM_FINAL_SHIP_Detailsano_cost",s_ano_cost);
			int_shipping.putExtra("ADD_ITEM_FINAL_SHIP", "add_item_ship_edit");
			startActivity(int_shipping);
			overridePendingTransition(R.anim.enter,R.anim.exit);
//			Add_Item_Edit_Final.this.finish();

			break;
		case R.id.linear_layout_addtags_additem_final:
			custom_dialog_addtag();

			break;
		case R.id.linear_layout_materials_additem_final:

			custom_dialog_addmaterial();

			break;
		case R.id.textView_descrs_additem_final:
			custom_dialog_item_tittle();

			break;
		case R.id.textView_decription_child_additem_final:
			custom_dialog_description();

			break;
		case R.id.textView_shipping_child:
			Log.d("Additem_edit_address--------------", str_add_addrsss);
			
			preferences.edit().remove(Iconstant.Session_Type).commit();
			preferences.edit().remove(Iconstant.Session_Type_country).commit();
			preferences.edit().remove(Iconstant.Session_Type_country_Id).commit();
			preferences.edit().remove(Iconstant.Session_Type_cost).commit();
			preferences.edit().remove(Iconstant.Session_Type_ano_cost).commit();
			preferences.edit().remove(Iconstant.Session_Type_title).commit();
			preferences.edit().remove(Iconstant.Session_Type_time).commit();
			preferences.edit().remove(Iconstant.Session_Type_shipfrom).commit();
      	
        	
			Log.e("count--------------",""+ count);
			if (count==1) {
				editor.putString(Iconstant.Session_Type, "add_item_ship_edit");
				editor.putString(Iconstant.Session_Type_country, s_dest);
				editor.putString(Iconstant.Session_Type_country_Id, s_des_id);
				editor.putString(Iconstant.Session_Type_cost, s_cost);
				editor.putString(Iconstant.Session_Type_ano_cost, s_ano_cost);
				editor.putString(Iconstant.Session_Type_title, "");
				editor.putString(Iconstant.Session_Type_time, s_time);
				editor.putString(Iconstant.Session_Type_shipfrom, s_from);
				editor.commit();
				count++;
			}else
			{
				editor.putString(Iconstant.Session_Type_country, s_dest+", ");
				editor.putString(Iconstant.Session_Type_country_Id, s_des_id+", ");
				editor.putString(Iconstant.Session_Type_cost, s_cost+", ");
				editor.putString(Iconstant.Session_Type_ano_cost, s_ano_cost+", ");
				editor.putString(Iconstant.Session_Type_title, "");
				editor.putString(Iconstant.Session_Type_time, s_time);
				editor.putString(Iconstant.Session_Type_shipfrom, s_from);
				editor.commit();
			}
			Intent int_shipping1 = new Intent(Add_Item_Edit_Final.this,Add_Item_shipping_Profile.class);
			int_shipping1.putExtra("ADD_ITEM_FINAL_SHIP", "add_item_ship_edit");
			int_shipping1.putExtra("ADD_ITEM_FINAL_SHIP_Details",str_add_addrsss);
			startActivity(int_shipping1);
			overridePendingTransition(R.anim.enter,R.anim.exit);
//			Add_Item_Edit_Final.this.finish();

			break;
		case R.id.textView_addupto:
			custom_dialog_addtag();

			break;
		case R.id.textView_adduptomaterials:
			custom_dialog_addmaterial();

			break;
		case R.id.textView_me:
//			Toast.makeText(Add_Item_Edit_Final.this, "Coming Soon",Toast.LENGTH_SHORT).show();

			break;
		case R.id.textView_category_additem_final:
//			Toast.makeText(Add_Item_Edit_Final.this, "Comming Soon",Toast.LENGTH_SHORT).show();

			break;

		case R.id.textView_subcate_additem_final:
//			Toast.makeText(Add_Item_Edit_Final.this, "Coming Soon",Toast.LENGTH_SHORT).show();

			break;
		case R.id.textView_subcate_1_additem_final:
//			Toast.makeText(Add_Item_Edit_Final.this, "Coming Soon",Toast.LENGTH_SHORT).show();

			break;
		case R.id.textview_manage_footer_additem_final1:
//			Toast.makeText(Add_Item_Edit_Final.this, "Coming Soon",Toast.LENGTH_SHORT).show();
			custom_dialog_copy();

			break;
		case R.id.textview_share_footer_additem_final1:
			Intent i=new Intent(android.content.Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(android.content.Intent.EXTRA_SUBJECT,p_productshareTitle);
			i.putExtra(android.content.Intent.EXTRA_TEXT, p_productshareUrl);
			startActivity(Intent.createChooser(i,"Share via"));

			break;
		case R.id.layout_price_add_item_final:
			custom_dialog_price();

			break;
		case R.id.layout_quantity_additem_final:
			custom_dialog_quantity();

			break;

		default:
			break;
		}
	}

	private void Remove_session() {
		// TODO Auto-generated method stub
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
		
		preferences.edit().remove(Iconstant.shared_AddItem_Id_page_1).commit();
		preferences.edit().remove(Iconstant.shared_AddItem_Id_page_2).commit();
		preferences.edit().remove(Iconstant.shared_AddItem_Id_page_3).commit();

		preferences.edit().remove(Iconstant.shared_AddItem_cat_1_id).commit();
		preferences.edit().remove(Iconstant.shared_AddItem_cat_2_id).commit();
		preferences.edit().remove(Iconstant.shared_AddItem_cat_3_id).commit();
		preferences.edit().remove(Iconstant.shared_AddItem_cat_1_name).commit();
		preferences.edit().remove(Iconstant.shared_AddItem_cat_2_name).commit();
		preferences.edit().remove(Iconstant.shared_AddItem_cat_3_name).commit();
	}

	private void custom_dialog_item_tittle() {
		// custom dialog
		final Dialog dialog = new Dialog(Add_Item_Edit_Final.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.popup_itemtittle);
		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.textView_popup_done_itemtitle);
		@SuppressWarnings("unused")
		ImageView image = (ImageView) dialog.findViewById(R.id.imageView_popup_itemtittle);
		final EditText Editext_PopUp = (EditText) dialog.findViewById(R.id.editText_popup_itemtitle);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(Editext_PopUp, InputMethodManager.SHOW_IMPLICIT);
		Editext_PopUp.setHint(Html.fromHtml("<small>" + "Give your listing a descriptive headline." + "<small>"));
		text.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Item_Tittle = Editext_PopUp.getText().toString();
				if (!Item_Tittle.equals("")) {	 	
					editor.putString(Iconstant.Session_AddTitle, Item_Tittle);
					editor.commit();
					Tv_Item_Title.setText(Item_Tittle);
					dialog.dismiss();
				}
			}
		});
		dialog.show();
	}

	private void custom_dialog_description() 
	{
		// custom dialog
		final Dialog dialog = new Dialog(Add_Item_Edit_Final.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.popup_description);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.textView_popup_done_desc);
		final EditText Editext_PopUp = (EditText) dialog.findViewById(R.id.editText_popup_desc_itemtitle);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(Editext_PopUp, InputMethodManager.SHOW_IMPLICIT);
		Editext_PopUp.setHint(Html.fromHtml("<small>" + "Try to answer the questions buyers will have.Tell the items story and explain why its special" + "<small>")); 
		text.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Desc = Editext_PopUp.getText().toString();
				if (!Desc.equals("")) {
					editor.putString(Iconstant.Session_AddDes, Desc);
					editor.commit();
					Tv_Description.setText(Desc);
					dialog.dismiss();
				}
			}
		});

		dialog.show();
	}

	private void custom_dialog_addtag() 
	{
		// custom dialog
		array_tag = new ArrayList<String>();
		final Dialog dialog = new Dialog(Add_Item_Edit_Final.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.popup_addtag);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.textView_addtag_done);
		ImageView iv_add = (ImageView)dialog.findViewById(R.id.imageView_pop_up_addtag);
		final LinearLayout rl = (LinearLayout) dialog.findViewById(R.id.relativelauy_pop_up_addtag_all);
		final EditText Editext_PopUp = (EditText) dialog.findViewById(R.id.editText_popup_addtag);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(Editext_PopUp, InputMethodManager.SHOW_IMPLICIT);
		text.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Add_tag = Editext_PopUp.getText().toString();	
				array_tag.add(Add_tag);
					String array_val;
					if (array_tag.size() > 1) {
						array_val = array_tag.toString();
						String array_val1 = array_val.substring(1,array_val.length() - 1);
						array_val = array_val1.substring(0,array_val1.length() - 2);
						Log.d("Value-before-if--->", array_val);
					} else {
						array_val = array_tag.toString();
						array_val = array_val.substring(1,array_val.length() - 1);
						Log.d("Value-before-else---->", array_val);
					}
					editor.putString(Iconstant.Session_AddTag, Add_tag);
					editor.commit();
					Tv_AddTag.setText(array_val);
					dialog.dismiss();				
			}
		});
		iv_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
/*				TextView tv1 = new TextView(Add_Item_Edit_Final.this);
				tv1.setText("This is tv1");
				rl.addView(tv1);*/
				Add_tag = Editext_PopUp.getText().toString();
				final Button button1 = new Button(Add_Item_Edit_Final.this);
				LinearLayout layout = new LinearLayout(Add_Item_Edit_Final.this);
				LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				if (!Add_tag.equals("")) 
				{		
					array_tag.add(Add_tag);
					
					Log.d("Value-add-->", array_tag.toString());
					layout.setOrientation(LinearLayout.VERTICAL);
					layout.setLayoutParams(params1);
					button1.setLayoutParams(params1);
					button1.setWidth(400);
					button1.setHeight(1);
//					params1.setMargins(left, top, right, bottom);
				    params1.setMargins(20, 0, 20, 0);
					button1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.erase, 0);
					button1.setText(Add_tag);
					Editext_PopUp.setText("");
					layout.addView(button1);
					rl.addView(layout);
				}
				
				button1.setOnClickListener(new OnClickListener() 
				{		
					@Override
					public void onClick(View view) {
						String str_tag_remove = button1.getText().toString();
						for (int i = 0; i < array_tag.size(); i++) {
							if (array_tag.contains(str_tag_remove)) 
							{
								array_tag.remove(str_tag_remove);
							}
						}
						String array_val = array_tag.toString();
						array_val = array_val.substring(1, array_val.length()-1);
						Log.d("Value--->", array_val);
						view.setVisibility(View.GONE);
					}
				});
			}
		});
		dialog.show();
	}

	private void custom_dialog_addmaterial() 
	{
		array_mat = new ArrayList<String>();
		final Dialog dialog = new Dialog(Add_Item_Edit_Final.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.popup_addmaterial);

		// set the custom dialog components - text, image and button
		ImageView iv_add = (ImageView)dialog.findViewById(R.id.imageView_pop_up_addmaterial);
		final LinearLayout rl = (LinearLayout) dialog.findViewById(R.id.relativelauy_pop_up_addmat_all);
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
				array_mat.add(Add_Material);
					String array_val;
					if (array_mat.size() > 1) {
						array_val = array_mat.toString();
						String array_val1 = array_val.substring(1,array_val.length() - 1);
						array_val = array_val1.substring(0,array_val1.length() - 2);
						Log.d("Value-before-if--->", array_val);
					} else {
						array_val = array_mat.toString();
						array_val = array_val.substring(1,array_val.length() - 1);
						Log.d("Value-before-else---->", array_val);
					}
					editor.putString(Iconstant.Session_AddMat, Add_Material);
					editor.commit();
					Tv_AddMaterial.setText(array_val);
					dialog.dismiss();
			}
		});
		iv_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Add_Material = Editext_PopUp.getText().toString();
				final Button button1 = new Button(Add_Item_Edit_Final.this);
				LinearLayout layout = new LinearLayout(Add_Item_Edit_Final.this);
				LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				if (!Add_Material.equals("")) 
				{		
					array_mat.add(Add_Material);
					
					Log.d("Value-add-->", array_mat.toString());
					layout.setOrientation(LinearLayout.VERTICAL);
					layout.setLayoutParams(params1);
					button1.setLayoutParams(params1);
					button1.setWidth(400);
					button1.setHeight(1);
//					params1.setMargins(left, top, right, bottom);
				    params1.setMargins(20, 0, 20, 0);
					button1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.erase, 0);
					button1.setText(Add_Material);
					Editext_PopUp.setText("");
					layout.addView(button1);
					rl.addView(layout);
				}
				
				button1.setOnClickListener(new OnClickListener() 
				{		
					@Override
					public void onClick(View view) {
						String str_tag_remove = button1.getText().toString();
						for (int i = 0; i < array_mat.size(); i++) {
							if (array_mat.contains(str_tag_remove)) 
							{
								array_mat.remove(str_tag_remove);
							}
						}
						String array_val = array_mat.toString();
						array_val = array_val.substring(1, array_val.length()-1);
						Log.d("Value--->", array_val);
						view.setVisibility(View.GONE);
					}
				});
			}
		});
		dialog.show();
	}

	private void custom_dialog_price() 
	{
		// custom dialog
		final Dialog dialog = new Dialog(Add_Item_Edit_Final.this);
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
				if (!Price.equals("")) {
					editor.putString(Iconstant.Session_AddPrice, Price);
					editor.commit();
					Tv_Price_Sym.setText(p_currencySymbol); 
					Tv_price.setText(Price);
					dialog.dismiss();
				}
			}
			
		});
		
		dialog.show();
	}

	private void custom_dialog_quantity() 
	{
		// custom dialog
		final Dialog dialog = new Dialog(Add_Item_Edit_Final.this);
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

				if (!Quantity.equals("")) { 
					editor.putString(Iconstant.Session_AddQua, Quantity);
					editor.commit();
					Tv_Quantity.setText(Quantity);
					dialog.dismiss();
				}
			}
		});
		dialog.show();
	}
	
	private void custom_dialog_copy() 
	{
		final Dialog dialog = new Dialog(Add_Item_Edit_Final.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.popup_copy_item);

		// set the custom dialog components - text, image and button
		RelativeLayout relative_lay_male = (RelativeLayout) dialog.findViewById(R.id.relative_edit_pop_up_edit_profile_gender_male);		
		relative_lay_male.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Log.d("AdddItem_pro_Id ---------Id", "" + p_pro_id);
				if (isConnectingToInternet()==true) 
				{				
					Intent int_copy = new Intent(Add_Item_Edit_Final.this, Add_Item_Edit_Final_Copy.class);
					int_copy.putExtra("INTENT_BUN_COPYITEM", p_pro_id);
					startActivity(int_copy);
					overridePendingTransition(R.anim.enter,R.anim.exit);
					dialog.dismiss();
				}else
				{
					dialog.dismiss();
					alartmsg(Iconstant.Alart_Internet);
				}
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
			progress = ProgressDialog.show(Add_Item_Edit_Final.this, null,"Editing Product !!!");
			if (!p_image1.equals("")) {
				p_image1 = p_image1.substring(3);
			}if (!p_image2.equals("")) {
				p_image2 = p_image2.substring(3);
			}if (!p_image3.equals("")) {
				p_image3 = p_image3.substring(3);
			}if (!p_image4.equals("")) {
				p_image4 = p_image4.substring(3);
			}if (!p_image5.equals("")) {
				p_image5 = p_image5.substring(3);
			}													
			Log.d("p_image1--------->", "" + p_image1);
			Log.d("p_image2--------->", "" + p_image2);
			Log.d("p_image3--------->", "" + p_image3);
			Log.d("p_image4--------->", "" + p_image4);
			Log.d("p_image5--------->", "" + p_image5);
			
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
						p_image1="";
						p_image2="";
						p_image3="";
						p_image4="";
						p_image5="";
						ByteArrayOutputStream bos_image_1 = new ByteArrayOutputStream();
						bitmap_image_1.compress(CompressFormat.JPEG, 90,bos_image_1);
						byte[] data_image_1 = bos_image_1.toByteArray();
						ByteArrayBody bab_image_1 = new ByteArrayBody(data_image_1, "forest1.jpg");
						reqEntity.addPart("photo1", bab_image_1);
						Log.d("data_image_1--------->", "" + data_image_1);
					}
					if (byteArray2 !=null) 
					{	
						p_image1="";
						p_image2="";
						p_image3="";
						p_image4="";
						p_image5="";
						ByteArrayOutputStream bos_image_2 = new ByteArrayOutputStream();
						bitmap_image_2.compress(CompressFormat.JPEG, 90,bos_image_2);
						byte[] data_image_2 = bos_image_2.toByteArray();
						ByteArrayBody bab_image_2 = new ByteArrayBody(data_image_2, "forest2.jpg");
						reqEntity.addPart("photo2", bab_image_2);
						Log.d("data_image_2--------->", "" + data_image_2);
					}
					if (byteArray3 !=null)
					{
						p_image1="";
						p_image2="";
						p_image3="";
						p_image4="";
						p_image5="";
						ByteArrayOutputStream bos_image_3 = new ByteArrayOutputStream();
						bitmap_image_3.compress(CompressFormat.JPEG, 90,bos_image_3);
						byte[] data_image_3 = bos_image_3.toByteArray();
						ByteArrayBody bab_image_3 = new ByteArrayBody(data_image_3, "forest3.jpg");
						reqEntity.addPart("photo3", bab_image_3);
						Log.d("data_image_3--------->", "" + data_image_3);
					}
					if (byteArray4 !=null)
					{
						p_image1="";
						p_image2="";
						p_image3="";
						p_image4="";
						p_image5="";
						ByteArrayOutputStream bos_image_4 = new ByteArrayOutputStream();
						bitmap_image_4.compress(CompressFormat.JPEG, 90,bos_image_4);
						byte[] data_image_4 = bos_image_4.toByteArray();
						ByteArrayBody bab_image_4 = new ByteArrayBody(data_image_4, "forest4.jpg");
						reqEntity.addPart("photo4", bab_image_4);
						Log.d("data_image_4--------->", "" + data_image_4);
					}
					if (byteArray5 !=null)
					{
						p_image1="";
						p_image2="";
						p_image3="";
						p_image4="";
						p_image5="";
						ByteArrayOutputStream bos_image_5 = new ByteArrayOutputStream();
						bitmap_image_5.compress(CompressFormat.JPEG, 90,bos_image_5);
						byte[] data_image_5 = bos_image_5.toByteArray();
						ByteArrayBody bab_image_5 = new ByteArrayBody(data_image_5, "forest3.jpg");
						reqEntity.addPart("photo5", bab_image_5);
						Log.d("data_image_5--------->", "" + data_image_5);
					}
					reqEntity.addPart("seller_id", new StringBody(String.valueOf(seller_id)));
					reqEntity.addPart("productId", new StringBody(p_pro_id));
					reqEntity.addPart("I_Tittle", new StringBody(str_title));
					reqEntity.addPart("I_Price", new StringBody(str_price));
					reqEntity.addPart("I_Quantity", new StringBody(str_qua));
					reqEntity.addPart("I_Desc", new StringBody(str_des));
					reqEntity.addPart("I_AddTag", new StringBody(str_tag));
					reqEntity.addPart("I_AddMat", new StringBody(str_mat));
					reqEntity.addPart("Add_Cat",new StringBody(jsonarray_cat.toString()));
					reqEntity.addPart("S_Profile",new StringBody(jsonObj_shiping.toString()));
					
					reqEntity.addPart("existphoto1", new StringBody(p_image1));
					reqEntity.addPart("existphoto2", new StringBody(p_image2));
					reqEntity.addPart("existphoto3", new StringBody(p_image3));
					reqEntity.addPart("existphoto4", new StringBody(p_image4));
					reqEntity.addPart("existphoto5", new StringBody(p_image5));
					
					Log.d("Values--------->",""+reqEntity);
					Log.d("Json--------->",""+jsonObj_shiping);
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
					byteArray1=null;
					byteArray2=null;
					byteArray3=null; 
					byteArray4=null;
					byteArray5=null;
					
					progress.dismiss();
					Toast.makeText(Add_Item_Edit_Final.this, "Item Edited Successfully", Toast.LENGTH_SHORT).show();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);
					Add_Item_Edit_Final.this.finish();
					
				} else 
				{
//					Toast.makeText(Add_Item_Edit_Final.this, s, Toast.LENGTH_SHORT).show();
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

	
	class Async_Dispute_Edit_Pro extends AsyncTask<Void, Void, Boolean>
	{
		private ProgressDialog progress = null;
		boolean status=false;
		boolean status_empty=false;
		int status1;
		String Url;
		
		@Override
		protected void onPreExecute() 
		{
			array_image.clear();
			Url = Iconstant.edit_pro+Prod_Id+Iconstant.edit_seller_Id+seller_id;  
			
			System.out.println("-------------Url------------------"+Url);
			
			progress = ProgressDialog.show(Add_Item_Edit_Final.this, null, "Checking for update !!!");
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setCancelable(true);
			progress.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                	if (httppost !=null)
            		{
            			httppost.abort();
            			httpclient.getConnectionManager().shutdown();
//            			async_Stats.cancel(true);
            		}
                    cancel(false);
                    alartmsg_reguest_canclled();
                }
            });
			
			s_dest="";
			s_cost="";
			s_ano_cost="";
			s_des_id="";
			
            progress.show();

			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) 
		{
			try 
			{
				Log.d("Edit_Product-Url--->",""+Url);
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
					JSONArray jarray = jsono.getJSONArray("prdDetails");
					Log.d("Json_Array_Edit_Product-History-------->","" + jarray);
					
					if (jarray.length() >0) 
					{
						for (int i = 0; i < jarray.length(); i++) 
						{
							JSONObject object = jarray.getJSONObject(i);														
							p_pro_id = object.getString("productid");
							p_productshareUrl = object.getString("productshareUrl");
							p_productshareTitle = object.getString("productshareTitle");
							p_price = object.getString("price");
							p_currencySymbol = object.getString("currencySymbol");
							p_currencyCode = object.getString("currencyCode");
							p_pro_title = object.getString("title");
							p_description = object.getString("description");
							p_quantity = object.getString("quantity");  
							p_tag = object.getString("tags");
							p_mat = object.getString("material");						
							s_from = object.getString("ship_from");
							s_time = object.getString("readytoShip");
							
							str_editable = object.getString("editable");
							
							about_1 = object.getString("about_1");
							about_2 = object.getString("about_2");
							about_3 = object.getString("about_3");							
							cat_1 = object.getString("cat1");
							cat_2 = object.getString("cat2");
							cat_3 = object.getString("cat3");	
							
							catName_1 = object.getString("catName1");
							catName_2 = object.getString("catName2");
							catName_3 = object.getString("catName3");
							
							p_image1 = object.getString("image1");	
							p_image2 = object.getString("image2");	
							p_image3 = object.getString("image3");	
							p_image4 = object.getString("image4");	
							p_image5 = object.getString("image5");	
							
							Log.d("s_from-------->","" + s_from);
							Log.d("s_time-------->","" + s_time);
							
							if (object!=null) {
								JSONArray prods = object.getJSONArray("shipping");
								Log.d("Json_Array_Edit_Product-Shipping-------->","" + prods);
								for (int j = 0; j < prods.length(); j++) {
									JSONObject object_data = prods.getJSONObject(j);
/*									p_shipping_country+= object_data.getString("country")+",";
									p_shipping_cost+= object_data.getString("cost")+",";
									p_shipping_cost_other+= object_data.getString("withother")+",";								
									str_add_addrsss = p_shipping_country+","+p_shipping_cost+","+p_shipping_cost_other;*/

									s_dest+= object_data.getString("country")+", ";
									s_des_id+= object_data.getString("id")+", ";
									s_cost+= object_data.getString("cost")+", ";
									s_ano_cost+= object_data.getString("withother")+", ";
									
/*									s_dest =s_dest.substring(0, s_dest.length()-2);
									s_cost =s_cost.substring(0, s_cost.length()-2);
									s_ano_cost =s_ano_cost.substring(0, s_ano_cost.length()-2);*/
								}
							}
						}
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
					Log.e("imagePath-------->","" + Iconstant.imageurl1+p_image1.toString());
					Log.e("str_editable-------->","" + str_editable);

					if (!p_image1.equals("")) {		
						imageLoader.DisplayImage(Iconstant.imageurl1+p_image1.toString(), Iv_image);
						array_image.add(Iconstant.imageurl1+p_image1.toString());
					}if (!p_image2.equals("")) {		
						array_image.add(Iconstant.imageurl1+p_image2.toString());
					}if (!p_image3.equals("")) {		
						array_image.add(Iconstant.imageurl1+p_image3.toString());
					}if (!p_image4.equals("")) {		
						array_image.add(Iconstant.imageurl1+p_image4.toString());
					}if (!p_image5.equals("")) {		
						array_image.add(Iconstant.imageurl1+p_image5.toString());
					}					
					
					if (!p_pro_title.equals("")) {
						Tv_Item_Title.setText(p_pro_title);
					}if (!p_price.equals("")) {
						Tv_Price_Sym.setText(p_currencySymbol);
						Tv_price.setText(p_price);
					}if (!p_description.equals("")) {
						Tv_Description.setText(p_description);
					}
					Tv_Quantity.setText(p_quantity);
					
					if (!p_tag.equals("")) {
						Tv_AddTag.setText(p_tag);
						
					}if (!p_mat.equals("")) {
						Tv_AddMaterial.setText(p_mat);
					}
					Tv_Shipping.setText(s_from+" "+s_time);
					
/*					if (!cat_1.equals("")) {
						Tv_Access.setText(cat_1);
					}if (!cat_2.equals("")) {
						Tv_Sub.setText(cat_2);
					}if (!cat_3.equals("")) {
						Tv_Sub1.setText(cat_3);
					}
*/						
					if (!catName_1.equals("")) {
						Tv_Access.setText(catName_1);
					}if (!catName_2.equals("")) {
						Tv_Sub.setText(catName_2);
					}if (!catName_3.equals("")) {
						Tv_Sub1.setText(catName_3);
					}
					
					if (!about_3.equals("")) {  
						Tv_MadeToMe.setText(about_3); 
					}
					progress.dismiss();

					adapter = new ImageAdapter_Edit(Add_Item_Edit_Final.this, array_image);
					viewPager.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
				else
				{
					progress.dismiss();
					alartmsg_no_data();
				}
			}
			else
			{
//				LoadingImage.clearAnimation();
//				LoadingImage.setVisibility(View.GONE);
				progress.dismiss();
				alartmsg(Iconstant.Alart_Status500);
			}
			super.onPostExecute(result);
		}	
	}
	
	
	private void Add_Json() 
	{
		try {
			jsonarray_shipping = new JSONArray();			
			jsonarray_cat = new JSONArray();
			String des_array[] = s_dest.split(",");
			String des_id[] = s_des_id.split(",");
			String cost_array[] = s_cost.split(",");
			String ano_cost_array[] = s_ano_cost.split(",");			
			jsonObj_shiping = new JSONObject();		
			JSONObject jsonObj1 = new JSONObject();
			
			jsonObj_shiping.put("s_tittle", s_tittle);
			jsonObj_shiping.put("readytoShip", s_time);
			jsonObj_shiping.put("ship_from", s_from);
			
			Log.d("des_array",""+ des_array.length);
			Log.d("des_id",""+ des_id.length);
			Log.d("cost_array",""+ cost_array.length);
			Log.d("ano_cost_array",""+ ano_cost_array.length);

			for (int i = 0; i < des_array.length; i++) {
				JSONObject list1 = new JSONObject();
				if (!s_dest.equals("")) {
					list1.put("country", des_array[i]);
				}
				if (!s_des_id.equals("")) { 
					list1.put("countryId", des_id[i]);
				}
				if (!s_cost.equals("")) {
					list1.put("cost", cost_array[i]);
				}
				if (!s_ano_cost.equals("")) {
					list1.put("withother", ano_cost_array[i]);
				}
				jsonarray_shipping.put(list1);
			}
				jsonObj_shiping.put("shipping", jsonarray_shipping);
				
				jsonObj1.put("about_1", about_1);
				jsonObj1.put("about_2", about_2);
				jsonObj1.put("about_3", about_3);
				jsonObj1.put("cat_1", cat_1);
				jsonObj1.put("cat_2", cat_2);
				jsonObj1.put("cat_3", cat_3);
				jsonarray_cat.put(jsonObj1);

				Log.d("Json_Array_Shipping", "" + jsonObj_shiping);
				Log.d("Json_Array_Cat", "" + jsonarray_cat);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void alartmsg_no_data() 
	{
		new AlertDialog.Builder(Add_Item_Edit_Final.this)
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
		new AlertDialog.Builder(Add_Item_Edit_Final.this)
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
	
	public Boolean isConnectingToInternet()    
	{
		Boolean isConnected=false;
		try
		{
			ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			isConnected = info != null && info.isAvailable() && info.isConnected();			
			return isConnected;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		Log.d("isConnected: ", ""+isConnected);
		return isConnected;   
	}
	
	private void alartmsg() 
	{
		new AlertDialog.Builder(Add_Item_Edit_Final.this)
		.setTitle("Shopsy")
		.setMessage("Check Your Internet Connection")
		.setCancelable(false)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{		
	//			getActivity().getFragmentManager().popBackStack();
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
	    	  Add_Item_Edit_Final.this.finish();
	    	  overridePendingTransition(R.anim.left_right, R.anim.right_left);
	          return true;
	      }
	      return false;
	  }
	 
	 
	 private void alartmsg(String alartStatus500) 
		{
			new AlertDialog.Builder(Add_Item_Edit_Final.this)
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
			new AlertDialog.Builder(Add_Item_Edit_Final.this)
			.setTitle(Iconstant.Alart_AppName)
			.setMessage("You cancelled the request try again !!!")
			.setCancelable(false)
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{	
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					Add_Item_Edit_Final.this.finish();
					overridePendingTransition(R.anim.left_right, R.anim.right_left);
				}
			})
			.setPositiveButton("Ok", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which) 
				{	
					Async_Dispute_Edit_Pro async_Dispute_Edit_Pro = new Async_Dispute_Edit_Pro();
					async_Dispute_Edit_Pro.execute();
				}
			}).show();
		}
	 
	 static class ADD_IMAGE_EDIT
	 {
		 static void IMAGE_GET_EDIT(byte[] image_Pick_1, byte[] image_Pick_2, byte[] image_Pick_3, byte[] image_Pick_4, byte[] image_Pick_5)
		 {
			byteArray1=null;
			byteArray2=null;
			byteArray3=null; 
			byteArray4=null;
			byteArray5=null;
			array_bitmap.clear();
			 if (image_Pick_1 !=null) 
				{
					bitmap_image_1 = BitmapFactory.decodeByteArray(image_Pick_1, 0, image_Pick_1.length);
					Iv_image.setImageBitmap(bitmap_image_1);
					Log.d("byteArray1--->", "" +image_Pick_1);
					byteArray1 = image_Pick_1;
					array_bitmap.add(bitmap_image_1);
				}  
	 			if (image_Pick_2 !=null) 
				{	
					bitmap_image_2 = BitmapFactory.decodeByteArray(image_Pick_2, 0, image_Pick_2.length);
					Iv_image.setImageBitmap(bitmap_image_2);
					Log.d("byteArray2--->", "" +image_Pick_2);
					byteArray2 = image_Pick_2;
					array_bitmap.add(bitmap_image_2);
				}  
				if (image_Pick_3 !=null) 
				{
					bitmap_image_3 = BitmapFactory.decodeByteArray(image_Pick_3, 0, image_Pick_3.length);
					Iv_image.setImageBitmap(bitmap_image_3);
					Log.d("byteArray3--->", "" +image_Pick_3);
					byteArray3 = image_Pick_3;
					array_bitmap.add(bitmap_image_3);
				}
				if (image_Pick_4 !=null) 
				{
					bitmap_image_4 = BitmapFactory.decodeByteArray(image_Pick_4, 0, image_Pick_4.length);
					Iv_image.setImageBitmap(bitmap_image_4);
					Log.d("byteArray4--->", "" +image_Pick_4);
					byteArray4 = image_Pick_4;
					array_bitmap.add(bitmap_image_4);
				}
				if (image_Pick_5 !=null) 
				{
					bitmap_image_5 = BitmapFactory.decodeByteArray(image_Pick_5, 0, image_Pick_5.length);
					Iv_image.setImageBitmap(bitmap_image_5);
					Log.d("byteArray5--->", "" +image_Pick_5);
					byteArray5 = image_Pick_5;
					array_bitmap.add(bitmap_image_5);
				}
				array_image.clear();
				adapter.notifyDataSetChanged();
				adapter_bitmap = new ImageAdapter_Bitmap_Edit(context, array_bitmap); 
				viewPager.setAdapter(adapter_bitmap);
		 }
	 }
	 
	 
	 
	 
	 private void IMAGE_GET_EDITNEW(byte[] image_Pick_1, byte[] image_Pick_2, byte[] image_Pick_3, byte[] image_Pick_4, byte[] image_Pick_5)
	 {
		byteArray1=null;
		byteArray2=null;
		byteArray3=null; 
		byteArray4=null;
		byteArray5=null;
		array_bitmap.clear();
		 if (image_Pick_1 !=null) 
			{
				bitmap_image_1 = BitmapFactory.decodeByteArray(image_Pick_1, 0, image_Pick_1.length);
				Iv_image.setImageBitmap(bitmap_image_1);
				Log.d("byteArray1--->", "" +image_Pick_1);
				byteArray1 = image_Pick_1;
				array_bitmap.add(bitmap_image_1);
			}  
 			if (image_Pick_2 !=null) 
			{	
				bitmap_image_2 = BitmapFactory.decodeByteArray(image_Pick_2, 0, image_Pick_2.length);
				Iv_image.setImageBitmap(bitmap_image_2);
				Log.d("byteArray2--->", "" +image_Pick_2);
				byteArray2 = image_Pick_2;
				array_bitmap.add(bitmap_image_2);
			}  
			if (image_Pick_3 !=null) 
			{
				bitmap_image_3 = BitmapFactory.decodeByteArray(image_Pick_3, 0, image_Pick_3.length);
				Iv_image.setImageBitmap(bitmap_image_3);
				Log.d("byteArray3--->", "" +image_Pick_3);
				byteArray3 = image_Pick_3;
				array_bitmap.add(bitmap_image_3);
			}
			if (image_Pick_4 !=null) 
			{
				bitmap_image_4 = BitmapFactory.decodeByteArray(image_Pick_4, 0, image_Pick_4.length);
				Iv_image.setImageBitmap(bitmap_image_4);
				Log.d("byteArray4--->", "" +image_Pick_4);
				byteArray4 = image_Pick_4;
				array_bitmap.add(bitmap_image_4);
			}
			if (image_Pick_5 !=null) 
			{
				bitmap_image_5 = BitmapFactory.decodeByteArray(image_Pick_5, 0, image_Pick_5.length);
				Iv_image.setImageBitmap(bitmap_image_5);
				Log.d("byteArray5--->", "" +image_Pick_5);
				byteArray5 = image_Pick_5;
				array_bitmap.add(bitmap_image_5);
			}
			array_image.clear();
			adapter.notifyDataSetChanged();
			adapter_bitmap = new ImageAdapter_Bitmap_Edit(context, array_bitmap); 
			viewPager.setAdapter(adapter_bitmap);
	 }
	 
	 
	 static class ADD_ADDRESS_EDIT
	 {
		 static void ADDRESS_EDIT(String s_tittle1, String s_time1, String s_dest1, String s_from1, String s_cost1, String s_ano_cost1, String s_dest_id1)
		 {
		        s_tittle = s_tittle1;
		        s_time = s_time1;
		        s_dest = s_dest1;
		        s_from = s_from1;
		        s_cost = s_cost1;
		        s_ano_cost = s_ano_cost1;
		        s_des_id = s_dest_id1;
		        Tv_Shipping.setText("");
		        Tv_Shipping.setText(s_time1+" "+ s_dest1+" "+ s_from1);
		        str_add_addrsss =  Tv_Shipping.getText().toString();
		        Log.d("title_add_item_edit", s_tittle);
		        Log.d("s_time_add_item_edit", s_time);
		        Log.d("s_dest_add_item_edit", s_dest);
		        Log.d("s_from_add_item_edit", s_from);
		        Log.d("s_cost_add_item_edit", s_cost);
		        Log.d("s_ano_cost_add_item_edit", s_ano_cost);
		        Log.d("s_des_id_add_item_edit", s_des_id);
		 }
	 }


	 @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	    private void dimView(View view) {
	        if (apiUtils.isCompatWithHoneycomb()) {
	            view.setAlpha(ALPHA_DIM_VALUE);
	        }
	    }

	 @SuppressLint("NewApi") @Override
	    public void onShowcaseViewHide(ShowcaseView showcaseView) {
	        if (apiUtils.isCompatWithHoneycomb()) {
	        	Rl_all.setAlpha(1f);
	        }
//	        btn_add.setTitle("button_show");
	        //buttonBlocked.setEnabled(false);
	    }

	    @Override
	    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
	    }

	    @Override
	    public void onShowcaseViewShow(ShowcaseView showcaseView) {
	        dimView(Rl_all);
//	        btn_add.setTitle("button_hide");
	        //buttonBlocked.setEnabled(true);
	    }
	    
	    public static class Tour
	    {
	    	public static void Tour_ssession()
	    	{
				editor.putString(Iconstant.sharedPre_tour_edit, "tittle_edit");
				editor.commit();
				
	    		if (shared_tour.equals("")) 
				{
					if (sv.isShown()) 
					{
//	                    sv.setStyle(R.style.CustomShowcaseTheme);
						sv.hide();
	                } else 
	                {
	                	if (shared_tour.equals("")) 
	                	{
	                		 sv.show();
	                	}
	                }
				}
	    	}
	    }
	    
	    public static class GET_Shared
	    {
	    	public static void GET_SH()
	    	{
	    		Id_1 = preferences.getString(Iconstant.shared_AddItem_cat_1_id, "");
	    		Id_2 = preferences.getString(Iconstant.shared_AddItem_cat_2_id, "");
	    		Id_3 = preferences.getString(Iconstant.shared_AddItem_cat_3_id, "");
	    		
	    		Name_1 = preferences.getString(Iconstant.shared_AddItem_cat_1_name, "");
	    		Name_2 = preferences.getString(Iconstant.shared_AddItem_cat_2_name, "");
	    		Name_3 = preferences.getString(Iconstant.shared_AddItem_cat_3_name, "");
	    		
	    		Add_1 = preferences.getInt(Iconstant.shared_AddItem_Id_page_1, 0);
	    		Add_2 = preferences.getInt(Iconstant.shared_AddItem_Id_page_2, 0);
	    		Add_3 = preferences.getInt(Iconstant.shared_AddItem_Id_page_3, 0);
	    		
	    		about_1 = String.valueOf(Add_1);
				about_2 = String.valueOf(Add_2);
				about_3 = String.valueOf(Add_3);	
				
				cat_1 =Id_1;
				cat_2 = Id_2;
				cat_3 = Id_3;	
				
				Tv_Access.setText("");
				Tv_Sub.setText("");
				Tv_Sub1.setText("");
				
				if (!Name_1.equals("")) {
					Tv_Access.setText(Name_1);
				}if (!Name_2.equals("")) {
					Tv_Sub.setText(Name_2);
				}if (!Name_3.equals("")) {
					Tv_Sub1.setText(Name_3);
				}	
				if (!about_3.equals("")) {  
					Tv_MadeToMe.setText("Made To Order");  
				}
	    		Log.d("get_shared_Categories Id--->", "" + cat_1+","+cat_2+","+cat_3);
	    		Log.e("get_shared_Add Id----------->", "" + about_1+","+about_2+","+about_3);
	    		Log.d("get_shared_Categories Name--->", "" + Name_1+","+Name_2+","+Name_3);
	    	}
	    }
	    
	    @Override
		public void onDestroy() {
			// Unregister the logout receiver
			unregisterReceiver(updatePotoReceiver);
			super.onDestroy();
		}
}