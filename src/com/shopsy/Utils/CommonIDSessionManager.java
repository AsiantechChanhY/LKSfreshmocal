package com.shopsy.Utils;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class CommonIDSessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file name
	private static final String PREF_NAME = "AndroidHivePref1";
	

	
	public static final String KEY_COMMONID = "commonid";
	public static final String KEY_USERCURRENCY = "usercurrency";
	
	
	
	
	// Constructor
	@SuppressLint("CommitPrefEdits") public CommonIDSessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createcommonidSession(String commonid){


		 // Storing email in pref
	    editor.putString(KEY_COMMONID, commonid.replace("-", ""));
		
	
		// commit changes
		editor.commit();
	}	
	
	
	public void createcommonidSessioncurrency(String currencyselected){


		 // Storing email in pref
	    editor.putString(KEY_USERCURRENCY, currencyselected);
		
	
		// commit changes
		editor.commit();
	}	
	
	
	
	
	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_COMMONID, pref.getString(KEY_COMMONID, null));
		user.put(KEY_USERCURRENCY, pref.getString(KEY_USERCURRENCY, null));
		
		
		
		// return user
		return user;
	}
	
	
	
	
}