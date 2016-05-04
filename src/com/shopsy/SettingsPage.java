package com.shopsy;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.shopsy.Utils.SessionManager;

public class SettingsPage extends Fragment implements OnClickListener,ConnectionCallbacks, OnConnectionFailedListener
{
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
	private RelativeLayout account_layout,notification_layout,advancesettings_layout,logout_layout;
	private View view;
	private Context context;
	private TextView header_title;
	
	 private SessionManager session;
	 
	// Google client to interact with Google API
	    private GoogleApiClient mGoogleApiClient;
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View rootview = inflater.inflate(R.layout.settings, container,false);
		context = getActivity();
		ActionBarActivity actionBarActivity = (ActionBarActivity) getActivity();
		actionBar = actionBarActivity.getSupportActionBar();
		
		//hide the menu at actionBar for fragment
		setHasOptionsMenu(false);
		initialize(rootview);
		
		
		 // -------------------Function for GooglePlus----------------------
        mGoogleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
		
		return rootview;
	}

	private void initialize(View rootview)
	{
			session = new SessionManager(context);
		
		// code to set actionBar background
      		colorDrawable.setColor(0xff1A237E);
      		actionBar.setBackgroundDrawable(colorDrawable);
      	
		// code to disable actionBar title
			actionBar.setDisplayShowTitleEnabled(false);
			
		// code to add custom menu in action bar
			actionBar.setDisplayShowCustomEnabled(true);
			view = View.inflate(context, R.layout.settings_header, null);
			actionBar.setCustomView(view);
			
			header_title=(TextView)view.findViewById(R.id.settings_header_title);
			header_title.setText("Settings");
		
			account_layout=(RelativeLayout)rootview.findViewById(R.id.settings_account_layout);
			notification_layout=(RelativeLayout)rootview.findViewById(R.id.settings_notification_layout);
			advancesettings_layout=(RelativeLayout)rootview.findViewById(R.id.settings_advanceSettings_layout);
			logout_layout=(RelativeLayout)rootview.findViewById(R.id.settings_logout_layout);
			
			
			account_layout.setOnClickListener(this);
			notification_layout.setOnClickListener(this);
			advancesettings_layout.setOnClickListener(this);
			logout_layout.setOnClickListener(this);
			
			if(session.isLoggedIn())
			{
				logout_layout.setVisibility(View.VISIBLE);
			}
			else
			{
				logout_layout.setVisibility(View.GONE);
			}
	}

	@Override
	public void onClick(View v) 
	{
		if(v==account_layout)
		{
		}
		else if(v==notification_layout)
		{
		}
		else if(v==advancesettings_layout)
		{
			Intent intent = new Intent(context, Settings_AdvanceSettings.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
		}
		else if(v==logout_layout)
		{
			logout();
			logoutFacebook();
			//signOutFromGplus();
			
			//code to refresh drawer Class
			Intent broadcastIntent1 = new Intent();
			broadcastIntent1.setAction("com.package.SIGNOUTGOOGLEPLUS");
			getActivity().sendBroadcast(broadcastIntent1);
			
			signOutFromGplus();
		}		
	}
	
	private void logout()
    {
    	getActivity().finish();
    	session.logoutUser();
    	
    	//code to refresh drawer list
		NavigationDrawerPage home = (NavigationDrawerPage) getActivity();
		home.drawer_refresh();
    }
	
	 //--------code for logout FaceBook---------
    public void logoutFacebook() 
    {
    	if (Session.getActiveSession() != null) 
		{
		    Session.getActiveSession().closeAndClearTokenInformation();
		}
		Session.setActiveSession(null);	
    }

    
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }
    
    
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}


}
