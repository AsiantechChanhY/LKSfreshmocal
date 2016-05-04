package com.shopsy.app;

import android.annotation.SuppressLint;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopsy.R;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;

/**
 * @author Prithivi Raj
 *
 */
public class Add_Item_3_Edit extends HockeyActivity 
{
	LinearLayout Ly_Move, Ly_All;
	RelativeLayout Ly_MadeOrder;
	private ImageView Iv_Tick_1;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private int Id, Id_Add_Item_3;
	String shared_profileimage;
	ActionBar actionBar;
	ColorDrawable colorDrawable = new ColorDrawable();
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item_3);
		
		preferences = getSharedPreferences(Iconstant.sharedPre_Name,0);
        editor = preferences.edit();
		Id = preferences.getInt(Iconstant.shared_AddItem_Id_page_3, 0);
		shared_profileimage = preferences.getString(Iconstant.sharedPre_ProfileImage, "");
		Custom_ActionBar();
//		Log.d("Session_Id", ""+Id);
		
		init();
		Session_Validation();
		
		Ly_MadeOrder.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v)
			{
	/*			 Intent int_Additem1 = new Intent(Add_Item_3.this, Add_Item_2.class);
			     startActivity(int_Additem1);
			     overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);*/
				Id_Add_Item_3 = 2131099731;
				Log.d("Add_Item_1---------->",""+Id_Add_Item_3);
				editor.putInt(Iconstant.shared_AddItem_Id_page_3, Id_Add_Item_3);
				editor.commit();
				Iv_Tick_1.setVisibility(View.VISIBLE);
				Intent int_item_categories = new Intent(Add_Item_3_Edit.this, ItemCategoriesActivity_Edit.class);
				startActivity(int_item_categories);
//				overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
				overridePendingTransition(R.anim.enter_fast,R.anim.exit_fast);
				Add_Item_3_Edit.this.finish();
			}
		});
		
		Ly_Move.setOnTouchListener(new Gesture_Detector(Add_Item_3_Edit.this) 
		{
		    public void onSwipeRight() 
		    {
//		        Toast.makeText(Add_Item_3.this, "right", Toast.LENGTH_SHORT).show();
		    	Intent int_Additem1 = new Intent(Add_Item_3_Edit.this, Add_Item_2_Edit.class);
			    startActivity(int_Additem1);
//			    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
			    overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
			    Add_Item_3_Edit.this.finish();
		    }
		    public void onSwipeLeft() 
		    {
//		        Toast.makeText(Add_Item_3.this, "left", Toast.LENGTH_SHORT).show();
		    	if (Iv_Tick_1.getVisibility() == View.VISIBLE) 
		    	{
		    		Intent int_item_categories = new Intent(Add_Item_3_Edit.this, ItemCategoriesActivity_Edit.class);
					startActivity(int_item_categories);
//					overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
					overridePendingTransition(R.anim.enter_fast,R.anim.exit_fast);
					Add_Item_3_Edit.this.finish();
				}
		    	else
		    		Toast.makeText(Add_Item_3_Edit.this, "Select Made To Order !!!", Toast.LENGTH_SHORT).show();
		    		Animation shake = AnimationUtils.loadAnimation(Add_Item_3_Edit.this, R.anim.shake);
		    		Ly_All.startAnimation(shake);
		    		
		    }
		});
	}
	
	private void Custom_ActionBar() 
	{
		actionBar = getSupportActionBar();
		getSupportActionBar().show();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
		
        LayoutInflater mInflater = LayoutInflater.from(Add_Item_3_Edit.this);
		View mCustomView = mInflater.inflate(R.layout.custom_actionbar_empty, null);
		ImageView imageView = (ImageView) mCustomView.findViewById(R.id.imageView1_custom_action_bar);
		imageView.setImageBitmap(decodeBase64(shared_profileimage));
		TextView textView = (TextView) mCustomView.findViewById(R.id.textView1_custom_action_bar);
		textView.setText("Add Item");
		imageView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				Intent int_Additem1 = new Intent(Add_Item_3_Edit.this, Add_Item_2_Edit.class);
				 startActivity(int_Additem1);
				 overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
				 Add_Item_3_Edit.this.finish();
			}
		});
		textView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				Intent int_Additem1 = new Intent(Add_Item_3_Edit.this, Add_Item_2_Edit.class);
				 startActivity(int_Additem1);
				 overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
				 Add_Item_3_Edit.this.finish();
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
		/*getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle("Add Item");*/
		
		Ly_MadeOrder = (RelativeLayout) findViewById(R.id.layout_madetoOrder);
		Ly_Move = (LinearLayout) findViewById(R.id.linear_layout_add_item_move3);
		Ly_All = (LinearLayout) findViewById(R.id.linear_layout_shoppolicies_add_item_3);
		Iv_Tick_1 = (ImageView) findViewById(R.id.imageView_idid_add_item3_tick);
		Iv_Tick_1.setVisibility(View.GONE);
	}
	
	private void Session_Validation() 
	{
		if (Id == Iconstant.additem_page_3_1) 
		{
			Iv_Tick_1.setVisibility(View.VISIBLE);
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
		 	case android.R.id.home:
			/*Add_Item_3_Edit.this.finish();
	 		overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);*/
		 		Intent int_Additem1 = new Intent(Add_Item_3_Edit.this, Add_Item_2_Edit.class);
				 startActivity(int_Additem1);
				 overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
				 Add_Item_3_Edit.this.finish();

				break;
		}
		return false;
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
	    	   Intent int_Additem1 = new Intent(Add_Item_3_Edit.this, Add_Item_2_Edit.class);
			   startActivity(int_Additem1);
//			   overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
			   overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
			   Add_Item_3_Edit.this.finish();
	          return true;
	      }
	      return false;
	  }
}