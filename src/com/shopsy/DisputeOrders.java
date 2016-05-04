package com.shopsy;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.MaterialTab.SlidingTabLayout;
import com.shopsy.adapter.DisputeOrder_ViewPagerAdapter;

public class DisputeOrders extends HockeyActivity
{
	private ViewPager pager;
	private DisputeOrder_ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    private CharSequence Titles[]={"OPEN","CLOSED"};
    private int Numboftabs =2;
    
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
	private TextView header_title;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);
		setupToolbar();
		initialize();
	}

	private void initialize() 
	{
		 adapter =  new DisputeOrder_ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
		 pager = (ViewPager) findViewById(R.id.order_pager);
	     pager.setAdapter(adapter);
	     
	      // Assiging the Sliding Tab Layout View
	        tabs = (SlidingTabLayout) findViewById(R.id.order_tabs);
	        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

	        // Setting Custom Color for the Scroll bar indicator of the Tab View
	        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
	            @Override
	            public int getIndicatorColor(int position) {
	                return getResources().getColor(R.color.tabsScrollColor);
	            }
	        });

	        // Setting the ViewPager For the SlidingTabsLayout
	        tabs.setViewPager(pager);

	}
	
	private void setupToolbar()
	{
        
		Toolbar toolbar = (Toolbar) findViewById(R.id.order_toolbar);
        setSupportActionBar(toolbar);
        
        actionBar = getSupportActionBar();
        
        // code to set actionBar background
      	colorDrawable.setColor(0xff1A237E);
      	actionBar.setBackgroundDrawable(colorDrawable);
	
	   // code to disable actionBar title
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		// ------------------code to add custom menu in action bar--------------------------------------
		actionBar.setDisplayShowCustomEnabled(true);
		View view = DisputeOrders.this.getLayoutInflater().inflate(R.layout.openshop_header, null);
		actionBar.setCustomView(view);
		
		header_title=(TextView)view.findViewById(R.id.openshop_header_title);
		header_title.setText(getResources().getString(R.string.disputeorder_label_dispute));
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

	
	//-----------------Move Back on pressed phone back button------------------		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) 
		{
			if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0))
			{
				onBackPressed();
				setResult(RESULT_OK);
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				finish();	
				return true;
			}
			return false;
		}
}

