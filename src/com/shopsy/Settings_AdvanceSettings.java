package com.shopsy;

import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.SessionManager_Settings;

public class Settings_AdvanceSettings extends ActionBarActivity implements OnClickListener
{
	private Context context;
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
	private View view;
	private TextView header_title;
	
	 private SessionManager_Settings session;
	 private RelativeLayout bandwidth_layout;
	 private TextView bandwidth_text;
	 private ImageView question;
	 
	 private Boolean isInternetPresent = false;
	 private ConnectionDetector cd;
	 
	 private String Bandwidth="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advance_settings);
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		initialize();
	}

	private void initialize()
	{
		    session = new SessionManager_Settings(context);
		
		// code to set actionBar background
      		colorDrawable.setColor(0xff1A237E);
      		actionBar.setBackgroundDrawable(colorDrawable);
      	
		// code to disable actionBar title
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);

			
		// code to add custom menu in action bar
			actionBar.setDisplayShowCustomEnabled(true);
			view = View.inflate(context, R.layout.settings_header, null);
			actionBar.setCustomView(view);
			
			header_title=(TextView)view.findViewById(R.id.settings_header_title);
			header_title.setText("Advance Settings");
			
			bandwidth_layout=(RelativeLayout)findViewById(R.id.advance_settings_bandwidth_layout);
			bandwidth_text=(TextView)findViewById(R.id.advance_settings_bandwidth_textview);
			question=(ImageView)findViewById(R.id.imageView_notification_setbandwidth);
			
			bandwidth_layout.setOnClickListener(this);
			question.setOnClickListener(this);
			
			HashMap<String, String> user = session.getBandwidth();
			Bandwidth = user.get(SessionManager_Settings.KEY_BANDWIDTH);
			
			if (Bandwidth.equalsIgnoreCase("auto")) {
				bandwidth_text.setText("Auto");
			}else if (Bandwidth.equalsIgnoreCase("high")) {
				bandwidth_text.setText("High");
			}else if (Bandwidth.equalsIgnoreCase("low")) {
				bandwidth_text.setText("Low");
			}else{
				bandwidth_text.setText("Auto");
			}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) {
			case android.R.id.home:

				onBackPressed();
				setResult(RESULT_OK);
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				finish();
			    return true;
				
			default:
				return super.onOptionsItemSelected(item);
				
		}
	}
	
	@Override
	public void onClick(View v)
	{
		cd = new ConnectionDetector(Settings_AdvanceSettings.this);
		isInternetPresent = cd.isConnectingToInternet();
		
		if(v==bandwidth_layout)
		{
			if(isInternetPresent)
			{
				Bandwidth_Dialog();
			}
			else
			{
				Toast.makeText(Settings_AdvanceSettings.this, "You are currently offline.", Toast.LENGTH_SHORT).show();
			}
		}
		else if(v==question)
		{
			Bandwidth_information();
		}
	}
	
	private void Bandwidth_Dialog()
	{
		final Dialog dialog = new Dialog(Settings_AdvanceSettings.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.advance_settings_bandwidth_popup);
		
		RelativeLayout auto=(RelativeLayout)dialog.findViewById(R.id.advance_settings_bandwidth_auto_layout);
		RelativeLayout high=(RelativeLayout)dialog.findViewById(R.id.advance_settings_bandwidth_high_layout);
		RelativeLayout low=(RelativeLayout)dialog.findViewById(R.id.advance_settings_bandwidth_low_layout);
		TextView tv_cancel = (TextView) dialog.findViewById(R.id.advance_settings_bandwidth_cancel);
		
		final RadioButton rb_auto = (RadioButton) dialog.findViewById(R.id.advance_settings_bandwidth_auto_checkbox);
		final RadioButton rb_high = (RadioButton) dialog.findViewById(R.id.advance_settings_bandwidth_high_checkbox);	
		final RadioButton rb_low = (RadioButton) dialog.findViewById(R.id.advance_settings_bandwidth_low_checkbox);

		rb_auto.setClickable(false);
		rb_high.setClickable(false);
		rb_low.setClickable(false);
		
		if (Bandwidth.equalsIgnoreCase("auto")) {
			bandwidth_text.setText("Auto");
			rb_auto.setChecked(true);
		}else if (Bandwidth.equalsIgnoreCase("high")) {
			bandwidth_text.setText("High");
			rb_high.setChecked(true);
		}else if (Bandwidth.equalsIgnoreCase("low")) {
			bandwidth_text.setText("Low");
			rb_low.setChecked(true);
		}else{
			bandwidth_text.setText("Auto");
			rb_auto.setChecked(true);
		}
		
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		
		auto.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				session.setBandwidth("auto");
				rb_auto.setChecked(true);
				rb_high.setChecked(false);
				rb_low.setChecked(false);
				dialog.dismiss();
				
				Intent_Home();
			}
		});
		
		high.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				session.setBandwidth("high");
				rb_auto.setChecked(false);
				rb_high.setChecked(true);
				rb_low.setChecked(false);
				dialog.dismiss();
				
				Intent_Home();
			}
		});
		
		low.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				session.setBandwidth("low");
				rb_auto.setChecked(false);
				rb_high.setChecked(false);
				rb_low.setChecked(true);
				dialog.dismiss();
				
				Intent_Home();
			}
		});
		
		dialog.show();
	}

	
	private void Bandwidth_information()
	{
		final Dialog dialog = new Dialog(Settings_AdvanceSettings.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.advance_settings_bandwidth_info_popup);
		
		TextView tv_cancel = (TextView) dialog.findViewById(R.id.advance_settings_info_popup_text_ok);

		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	
	private void Intent_Home()
	{
		Intent int_settingg = new Intent(Settings_AdvanceSettings.this, NavigationDrawerPage.class);
		int_settingg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(int_settingg);
		NavigationDrawerPage.fregment_Homepage.finish();
		Settings_AdvanceSettings.this.finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
}
