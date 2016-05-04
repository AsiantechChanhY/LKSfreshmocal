package com.shopsy;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.FileCache;
import com.shopsy.Utils.SessionManager;

/**
 * @author Prem Kumar
 *
 */
public class SplashPage extends Activity 
{
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 2000;
	
	// Session Manager Class
	SessionManager session;
	CommonIDSessionManager commonsession;

	String commonid;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashpage);
		// Session class instance
		session = new SessionManager(getApplicationContext());
		commonsession = new CommonIDSessionManager(getApplicationContext());

		new Handler().postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				session.createUserlogedActivity("");

				//CLEAR IMAGE CACHE FROM PHONE SD CARD
				FileCache aa=new FileCache(SplashPage.this);
				aa.clear();
				
				
				if (session.isLoggedIn()) 
				{
					Random r = new Random(System.currentTimeMillis());
					commonsession.createcommonidSession(String.valueOf(1000000000 + r.nextInt(2000000000)));

					Intent i = new Intent(SplashPage.this, NavigationDrawerPage.class);
					startActivity(i);
					finish();
					overridePendingTransition(R.anim.enter,R.anim.exit);
				} 
				else
				{
					Random r = new Random(System.currentTimeMillis());
					commonsession.createcommonidSession(String.valueOf(1000000000 + r.nextInt(2000000000)));

					Intent i = new Intent(SplashPage.this, NavigationDrawerPage.class);
					startActivity(i);
					finish();
					overridePendingTransition(R.anim.enter,R.anim.exit);
				}
			}
		}, SPLASH_TIME_OUT);
	}
}
