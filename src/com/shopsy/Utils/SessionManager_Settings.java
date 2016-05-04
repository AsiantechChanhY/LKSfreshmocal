package com.shopsy.Utils;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager_Settings 
{
		SharedPreferences pref;
		Editor editor;
		Context _context;
		
		// Shared pref mode
		int PRIVATE_MODE = 0;
		
		// Sharedpref file name
		private static final String PREF_NAME = "Shopsy_Prem";
		public static final String KEY_BANDWIDTH = "bandwidth";
		
		
		// Constructor
		public SessionManager_Settings(Context context) {
			this._context = context;
			pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
			editor = pref.edit();
		}
		
		public void setBandwidth(String bandwidth)
		{
			editor.putString(KEY_BANDWIDTH, bandwidth);
			editor.commit();
		}
		
		
		/**
		 * Get stored session data
		 * */
		public HashMap<String, String> getBandwidth() 
		{
			HashMap<String, String> user = new HashMap<String, String>();
			user.put(KEY_BANDWIDTH, pref.getString(KEY_BANDWIDTH, ""));
			return user;
		}
}
