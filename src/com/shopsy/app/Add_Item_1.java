package com.shopsy.app;

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

public class Add_Item_1 extends HockeyActivity implements OnClickListener
{	
	private RelativeLayout Ly_Idid, Ly_Member, Ly_Another;
	private LinearLayout Ly_Move, Ly_All;
	private ImageView Iv_Tick_1, Iv_Tick_2, Iv_Tick_3;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private int Id, Id_Add_Item_1;
	String shared_profileimage;
	ActionBar actionBar;
	ColorDrawable colorDrawable = new ColorDrawable();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item_1);
		
		preferences = getSharedPreferences(Iconstant.sharedPre_Name,0);
        editor = preferences.edit();
		Id = preferences.getInt(Iconstant.shared_AddItem_Id_page_1, 0);
		shared_profileimage = preferences.getString(Iconstant.sharedPre_ProfileImage, "");
		Custom_ActionBar();
//		Log.d("Session_Id", ""+Id);
		
		init();
		
		Session_Validation();
		
		Ly_Move.setOnTouchListener(new Gesture_Detector(Add_Item_1.this) 
		{
		    public void onSwipeLeft() 
		    {
		    	if (Iv_Tick_1.getVisibility() == View.VISIBLE) {
					Intent int_Additem1 = new Intent(Add_Item_1.this,Add_Item_2.class);
					startActivity(int_Additem1);
//					overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
					overridePendingTransition(R.anim.enter_fast,R.anim.exit_fast);
					Add_Item_1.this.finish();
				}
		    	else if (Iv_Tick_2.getVisibility() == View.VISIBLE) {
		    		Intent int_Additem1 = new Intent(Add_Item_1.this,Add_Item_2.class);
					startActivity(int_Additem1);
					overridePendingTransition(R.anim.enter_fast,R.anim.exit_fast);
					Add_Item_1.this.finish();
				}
		    	else if (Iv_Tick_3.getVisibility() == View.VISIBLE) {
		    		Intent int_Additem1 = new Intent(Add_Item_1.this,Add_Item_2.class);
					startActivity(int_Additem1);
					overridePendingTransition(R.anim.enter_fast,R.anim.exit_fast);
					Add_Item_1.this.finish();
				}
		    	else
		    	{
		    		Toast.makeText(Add_Item_1.this, "Select The Item !!!", Toast.LENGTH_SHORT).show();
		    		Animation shake = AnimationUtils.loadAnimation(Add_Item_1.this, R.anim.shake);
		    		Ly_All.startAnimation(shake);
		    	}
		    }
		    public void onSwipeBottom() 
		    {
		    }
		});
	}

	private void Session_Validation() 
	{
		if (Id == Iconstant.additem_page_1_1) {
			Iv_Tick_1.setVisibility(View.VISIBLE);
		}
		else if (Id == Iconstant.additem_page_1_2) {
			Iv_Tick_2.setVisibility(View.VISIBLE);
		}
		else if (Id == Iconstant.additem_page_1_3) {
			Iv_Tick_3.setVisibility(View.VISIBLE);
		}
	}

	private void Custom_ActionBar() 
	{
		actionBar = getSupportActionBar();
		getSupportActionBar().show();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
		
        LayoutInflater mInflater = LayoutInflater.from(Add_Item_1.this);
		View mCustomView = mInflater.inflate(R.layout.custom_actionbar_empty, null);
		ImageView imageView = (ImageView) mCustomView.findViewById(R.id.imageView1_custom_action_bar);
		imageView.setImageBitmap(decodeBase64(shared_profileimage));
		TextView textView = (TextView) mCustomView.findViewById(R.id.textView1_custom_action_bar);
		textView.setText("Add Item");
		imageView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				Add_Item_1.this.finish();
		 		overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
			}
		});
		textView.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				Add_Item_1.this.finish();
		 		overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
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
		/*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle("Add Item");*/
		
		Ly_Idid = (RelativeLayout) findViewById(R.id.layout_additem_idid);
		Ly_Member = (RelativeLayout) findViewById(R.id.layout_additem_memberofshop);
		Ly_Another = (RelativeLayout) findViewById(R.id.layout_additem_acompany);
		Ly_Move = (LinearLayout) findViewById(R.id.linearlayout_move_1);
		Ly_All = (LinearLayout) findViewById(R.id.linearlayout_shoppolicies_add_item_1);
		
		Iv_Tick_1 = (ImageView) findViewById(R.id.imageView_idid_add_item1_tick);
		Iv_Tick_2 = (ImageView) findViewById(R.id.imageView_memberof_add_item1_tick);
		Iv_Tick_3 = (ImageView) findViewById(R.id.imageView_another_add_item1_tick);
		
		Iv_Tick_1.setVisibility(View.GONE);
		Iv_Tick_2.setVisibility(View.GONE);
		Iv_Tick_3.setVisibility(View.GONE);
		
		Ly_Idid.setOnClickListener(this);
		Ly_Member.setOnClickListener(this);
		Ly_Another.setOnClickListener(this);
		Ly_All.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.layout_additem_idid:

			Id_Add_Item_1 = 2131099714;
			Log.d("Add_Item_1---------->",""+Id_Add_Item_1);
			editor.putInt(Iconstant.shared_AddItem_Id_page_1, Id_Add_Item_1);
			editor.commit();
			
			Iv_Tick_1.setVisibility(View.VISIBLE);
			Iv_Tick_2.setVisibility(View.GONE);			
			Iv_Tick_3.setVisibility(View.GONE);

			Intent int_Additem1 = new Intent(Add_Item_1.this, Add_Item_2.class);
		    startActivity(int_Additem1);
//		    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
		    overridePendingTransition(R.anim.enter_fast,R.anim.exit_fast);
		    Add_Item_1.this.finish();
			
			break;
		case R.id.layout_additem_memberofshop:
			
			Id_Add_Item_1 = 2131099717;
			Log.d("Add_Item_2---------->",""+Id_Add_Item_1);
			editor.putInt(Iconstant.shared_AddItem_Id_page_1, Id_Add_Item_1);
			editor.commit();
			
			Iv_Tick_1.setVisibility(View.GONE);
			Iv_Tick_2.setVisibility(View.VISIBLE);	
			Iv_Tick_3.setVisibility(View.GONE);
			Intent int_Additem11 = new Intent(Add_Item_1.this, Add_Item_2.class);
		    startActivity(int_Additem11);
//		    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
		    overridePendingTransition(R.anim.enter_fast,R.anim.exit_fast);
			Add_Item_1.this.finish();
			
			break;
		case R.id.layout_additem_acompany:

			Id_Add_Item_1 = 2131099720;
			Log.d("Add_Item_3---------->",""+Id_Add_Item_1);
			editor.putInt(Iconstant.shared_AddItem_Id_page_1, Id_Add_Item_1);
			editor.commit();
			
			Iv_Tick_1.setVisibility(View.GONE);	
			Iv_Tick_2.setVisibility(View.GONE);
			Iv_Tick_3.setVisibility(View.VISIBLE);
			Intent int_Additem12 = new Intent(Add_Item_1.this, Add_Item_2.class);
			startActivity(int_Additem12);
//    		overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
			overridePendingTransition(R.anim.enter_fast,R.anim.exit_fast);
			Add_Item_1.this.finish();
	
			break;

		default:
			break;
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
		 	case android.R.id.home:
			Add_Item_1.this.finish();
	 		overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);

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
			   Add_Item_1.this.finish();
			   overridePendingTransition(R.anim.left_right_fast, R.anim.right_left_fast);
	          return true;
	      }
	      return false;
	 }
}
