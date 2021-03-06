package com.shopsy.app;

import java.io.IOException;
import java.util.ArrayList;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopsy.R;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.bean.Categories_Bean;
import com.shopsy.seller.adapter.Castrgories_Adapter;


/**
 * @author Prithivi Raj
 *
 */
public class ItemCategoriesActivity_Edit extends HockeyActivity
{
	private RelativeLayout Rl_slide, Rl_No_Internet;
	ProgressBar bar;
	private ListView listView;
	private ImageView LoadingImage, Iv_Refresh;
	private Animation anim;	
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	Castrgories_Adapter adapter;
	ArrayList<Categories_Bean> ItemList_Bean = new ArrayList<Categories_Bean>();
	Async_Categories async_Categories;
	String shared_profileimage;
	ActionBar actionBar;
	ColorDrawable colorDrawable = new ColorDrawable();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemcategories);
		preferences = getSharedPreferences(Iconstant.sharedPre_Name,0);
        editor = preferences.edit();
		shared_profileimage = preferences.getString(Iconstant.sharedPre_ProfileImage, "");
		Custom_ActionBar();
		init();
		
		if (isConnectingToInternet()==true) 
		{		
			Rl_No_Internet.removeAllViews();
			async_Categories = new Async_Categories();
			async_Categories.execute();
		}
		else
		{
			listView.setFastScrollAlwaysVisible(false);
			Rl_No_Internet.setVisibility(View.VISIBLE);
//			alartmsg(IConstant.Alart_Internet);
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> listview, View v, int position, long id) 
			{
				String Id = ItemList_Bean.get(position).getId();
				String Name = ItemList_Bean.get(position).getCategoryName();
//				Toast.makeText(ItemCategoriesActivity.this, Id, Toast.LENGTH_SHORT).show();
				editor.putString(Iconstant.shared_AddItem_cat_1_id, Id);
				editor.putString(Iconstant.shared_AddItem_cat_1_name, Name);
				editor.commit();
				Intent int_sub_cat = new Intent(ItemCategoriesActivity_Edit.this, ItemCategories_SubActivity_Edit.class);
				startActivity(int_sub_cat);
				ItemCategoriesActivity_Edit.this.finish();
//				overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
				overridePendingTransition(R.anim.enter_fast,R.anim.exit_fast);
			}
		});
		
		listView.setOnTouchListener(new Gesture_Detector(ItemCategoriesActivity_Edit.this) 
		{
		    public void onSwipeTop() 
		    { 	
	//	        Toast.makeText(Add_Item.this, "top", Toast.LENGTH_SHORT).show();
		    }
		    public void onSwipeRight() 
		    {
//		        Toast.makeText(ItemCategoriesActivity.this, "right", Toast.LENGTH_SHORT).show();
		    	ItemCategoriesActivity_Edit.this.finish();
		    	Intent int_Additem1 = new Intent(ItemCategoriesActivity_Edit.this, Add_Item_3_Edit.class);
			    startActivity(int_Additem1);
//			    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
			    overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
		    }
		    public void onSwipeLeft() 
		    {
		    	Toast.makeText(ItemCategoriesActivity_Edit.this, "Please Select The Categorie", Toast.LENGTH_SHORT).show();
		    	Animation shake = AnimationUtils.loadAnimation(ItemCategoriesActivity_Edit.this, R.anim.shake);
		    	listView.startAnimation(shake);
		    }
		    public void onSwipeBottom() 
		    {
	//	        Toast.makeText(Add_Item.this, "bottom", Toast.LENGTH_SHORT).show();
		    }
		});
		
		Rl_slide.setOnTouchListener(new Gesture_Detector(ItemCategoriesActivity_Edit.this) 
		{
		    public void onSwipeTop() 
		    { 	
	//	        Toast.makeText(Add_Item.this, "top", Toast.LENGTH_SHORT).show();
		    }
		    public void onSwipeRight() 
		    {
//		        Toast.makeText(ItemCategoriesActivity.this, "right", Toast.LENGTH_SHORT).show();
		    	ItemCategoriesActivity_Edit.this.finish();
		    	Intent int_Additem1 = new Intent(ItemCategoriesActivity_Edit.this, Add_Item_3_Edit.class);
			    startActivity(int_Additem1);
//			    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
			    overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
		    }
		    public void onSwipeLeft() 
		    {
		    	
		    }
		    public void onSwipeBottom() 
		    {
	//	        Toast.makeText(Add_Item.this, "bottom", Toast.LENGTH_SHORT).show();
		    }
		});
		
		Iv_Refresh.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				v.startAnimation(AnimationUtils.loadAnimation(ItemCategoriesActivity_Edit.this, R.anim.image_click));
				if (isConnectingToInternet()==true) 
				{
					Rl_No_Internet.removeAllViews();
					listView.setFastScrollAlwaysVisible(true);
					ItemList_Bean.clear();
					async_Categories = new Async_Categories();
					async_Categories.execute();
				}else
				{
					listView.setFastScrollAlwaysVisible(false);
					Rl_No_Internet.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void Custom_ActionBar() 
	{
		actionBar = getSupportActionBar();
		getSupportActionBar().show();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
		
        LayoutInflater mInflater = LayoutInflater.from(ItemCategoriesActivity_Edit.this);
		View mCustomView = mInflater.inflate(R.layout.custom_actionbar_empty, null);
		ImageView imageView = (ImageView) mCustomView.findViewById(R.id.imageView1_custom_action_bar);
		imageView.setImageBitmap(decodeBase64(shared_profileimage));
		TextView textView = (TextView) mCustomView.findViewById(R.id.textView1_custom_action_bar);
		textView.setText("Add Item");
		imageView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				Intent int_Additem1 = new Intent(ItemCategoriesActivity_Edit.this, Add_Item_3_Edit.class);
				 startActivity(int_Additem1);
				 overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
				 ItemCategoriesActivity_Edit.this.finish();
			}
		});
		textView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				Intent int_Additem1 = new Intent(ItemCategoriesActivity_Edit.this, Add_Item_3_Edit.class);
				 startActivity(int_Additem1);
				 overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
				 ItemCategoriesActivity_Edit.this.finish();
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
		getSupportActionBar().setTitle("Add Item");*/
		
		bar = (ProgressBar) findViewById(R.id.loading_image_activetab_progress6);
		bar.setVisibility(View.GONE);
		
		listView = (ListView) findViewById(R.id.category_list);
		listView.setFastScrollAlwaysVisible(true);
		listView.setFastScrollEnabled(true);
		Rl_No_Internet = (RelativeLayout) findViewById(R.id.relativelayout_itemcategories_nointernet);
		Iv_Refresh = (ImageView) findViewById(R.id.imageView1_itemcategories_connection_refresh);
		Rl_slide = (RelativeLayout) findViewById(R.id.relative_layout_item_cate);
		LoadingImage = (ImageView) findViewById(R.id.loading_image_item_categories);
		anim = AnimationUtils.loadAnimation(this, R.drawable.rotate);
		LoadingImage.setVisibility(View.GONE);
	}
	
	class Async_Categories extends AsyncTask<Void, Void, Boolean>
	{
		boolean status = false;
		
		@Override
		protected void onPreExecute() 
		{
			bar.setVisibility(View.VISIBLE);
//			LoadingImage.setVisibility(View.VISIBLE);
			LoadingImage.startAnimation(anim);
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) 
		{
			try 
			{
				HttpGet httppost = new HttpGet(Iconstant.category_url);
				HttpClient httpclient = new DefaultHttpClient();
				httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "shopsymobileapp");
				HttpResponse response = httpclient.execute(httppost);
				int status1 = response.getStatusLine().getStatusCode();

				if (status1 == 200) 
				{
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);

					JSONObject jsono = new JSONObject(data);
					JSONArray jarray = jsono.getJSONArray("categoryDetails");
					
					for (int i = 0; i < jarray.length(); i++) 
					{
						JSONObject object = jarray.getJSONObject(i);
						Categories_Bean bean = new Categories_Bean();
						bean.setId(object.getString("id"));
						bean.setCategoryName(object.getString("categoryName"));
						
						Log.d("CategoryName-------->",""+object.getString("categoryName"));
						Log.d("CategoryId-------->",""+object.getString("id"));
						ItemList_Bean.add(bean);
					}
					status = true;
					adapter = new Castrgories_Adapter(ItemCategoriesActivity_Edit.this, R.layout.itemcategories, ItemList_Bean);
					return status;
				}
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
			status = false;
			return status;
		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			if (result) 
			{
				
		        listView.setAdapter(adapter);
		        
		        bar.setVisibility(View.GONE);
				LoadingImage.clearAnimation();
				LoadingImage.setVisibility(View.GONE);
//				listView.setAdapter(adapter);
			}
			else
			{
				bar.setVisibility(View.GONE);
				LoadingImage.clearAnimation();
				LoadingImage.setVisibility(View.GONE);
				alartmsg(Iconstant.Alart_Status500);
			}
			super.onPostExecute(result);
		}	
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
	
	private void alartmsg(String alart) 
	{
		new AlertDialog.Builder(ItemCategoriesActivity_Edit.this)
		.setTitle(Iconstant.Alart_AppName)
		.setMessage(alart)
		.setCancelable(false)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
						// getActivity().getFragmentManager().popBackStack();
			}
		}).show();
	}
	
	 @Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) 
	  {
	      if ((keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0))
	      {
//	    	  Toast.makeText(getApplicationContext(), "Home_Pressed", Toast.LENGTH_SHORT).show();
	          return true;
	      }
	      if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) 
	      {
	    	   Intent int_Additem1 = new Intent(ItemCategoriesActivity_Edit.this, Add_Item_3_Edit.class);
			   startActivity(int_Additem1);
//			   overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
			   overridePendingTransition(R.anim.left_right, R.anim.right_left);
			   ItemCategoriesActivity_Edit.this.finish();
	          return true;
	      }
	      return false;
	  }
	 
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) 
		{
//			MenuInflater inflate = getSupportMenuInflater();
//			inflate.inflate(R.menu.add_item_done, menu);
			return super.onCreateOptionsMenu(menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) 
		{
			switch (item.getItemId()) 
			{	
			 	case android.R.id.home:
			 		 Intent int_Additem1 = new Intent(ItemCategoriesActivity_Edit.this, Add_Item_3_Edit.class);
					 startActivity(int_Additem1);
//					 overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
					 overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
					 ItemCategoriesActivity_Edit.this.finish();

					break;
			}
			return false;
		}

		
}