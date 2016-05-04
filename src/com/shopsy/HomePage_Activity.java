package com.shopsy;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.MaterialTab.SlidingTabLayout;
import com.shopsy.Utils.CustomViewPager;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.Home_ViewPagerAdapter;

public class HomePage_Activity extends HockeyActivity {
	private ActionBar actionBar;

	private static CustomViewPager pager;
	private Home_ViewPagerAdapter adapter;
	SlidingTabLayout tabs;
	private CharSequence Titles[] = { "RECOMMEND", "PICKS", "FEED" };
	private static int Numboftabs = 3;

	private RelativeLayout cart;
	private static TextView cartcount;
	private TextView header_title;
	private static RelativeLayout cartcountlayout;

	private static SessionManager session;
	View rootview;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		
  		ActionBarActivity actionBarActivity = (ActionBarActivity)HomePage_Activity.this;
  		actionBar = actionBarActivity.getSupportActionBar();
  		
  		initialize();
  		
	}

	private void initialize() {
		session = new SessionManager(HomePage_Activity.this);

		// code to disable actionBar title
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);

		// ------------------code to add custom menu in action bar--------------------------------------
		actionBar.setDisplayShowCustomEnabled(true);
		View view = HomePage_Activity.this.getLayoutInflater().inflate(R.layout.home_custom_header, null);
		actionBar.setCustomView(view);

		header_title = (TextView) view.findViewById(R.id.home_header_title);
		cart = (RelativeLayout) view.findViewById(R.id.cartrelativelayout);
		cartcount = (TextView) view.findViewById(R.id.homecartcounttext);
		cartcountlayout = (RelativeLayout) view.findViewById(R.id.homecartcountlayout);
		header_title.setText("Home");
		
		// Assiging the Sliding Tab Layout View
		tabs = (SlidingTabLayout)findViewById(R.id.homepage_tabs);
		tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true,
										// This makes the tabs Space Evenly in
										// Available width

		// Setting Custom Color for the Scroll bar indicator of the Tab View
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		});

		
		session.SetHomePage("Activity");
		
		
		adapter = new Home_ViewPagerAdapter(HomePage_Activity.this.getSupportFragmentManager(), Titles, Numboftabs);
		pager = (CustomViewPager)findViewById(R.id.homepage_pager);
		pager.setOffscreenPageLimit(2);
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
		
		
		cart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomePage_Activity.this, CartPage.class);
				startActivity(intent);
				overridePendingTransition(R.anim.enter,R.anim.exit);
			}
		});

	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// some variable statements...
		
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

	public static void cartcountRefresh() {
		// get cart count from session
		HashMap<String, String> count = session.getCartCount();
		String Scartcount = count.get(SessionManager.KEY_CARTCOUNT);

		if (Scartcount.length() > 0 && !Scartcount.equals("0")) {
			cartcountlayout.setVisibility(View.VISIBLE);
			cartcount.setText(Scartcount);
		} else {
			cartcountlayout.setVisibility(View.INVISIBLE);
		}
	}

	public static void swipeOn() {

		System.out.println("-----------inside swipe on-------------");
		pager.setSwipeable(true);
	}

	public static void swipeOff() {

		System.out.println("-----------inside swipe off-------------");
		pager.setSwipeable(false);
	}
}

