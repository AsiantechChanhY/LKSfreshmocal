package com.shopsy;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.material_contextmenu_library.ContextMenuDialogFragment;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.MaterialTab.SlidingTabLayout;
import com.shopsy.adapter.OpenShop_ViewPagerAdapter;


public class OpenShopPage extends HockeyActivity
{
	private ViewPager pager;
	private OpenShop_ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    private CharSequence Titles[]={"ACTIVITY","STATS"};
    private int Numboftabs =2;
    
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
	private TextView header_title;
	
	 //------------Initialization for Context menu-------------
    private DialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.openshop);
		setupToolbar();
		initialize();
		//initMenuFragment();
		
	}

	private void initialize() 
	{
		 adapter =  new OpenShop_ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
		 pager = (ViewPager) findViewById(R.id.openshop_pager);
	     pager.setAdapter(adapter);
	     
	      // Assiging the Sliding Tab Layout View
	        tabs = (SlidingTabLayout) findViewById(R.id.openshop_tabs);
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
        
		Toolbar toolbar = (Toolbar) findViewById(R.id.openshop_toolbar);
        setSupportActionBar(toolbar);
        
        actionBar = getSupportActionBar();
        fragmentManager = getSupportFragmentManager();
        
        // code to set actionBar background
      	colorDrawable.setColor(0xff1A237E);
      	actionBar.setBackgroundDrawable(colorDrawable);
	
	   // code to disable actionBar title
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		// ------------------code to add custom menu in action bar--------------------------------------
		actionBar.setDisplayShowCustomEnabled(true);
		View view = OpenShopPage.this.getLayoutInflater().inflate(R.layout.openshop_header, null);
		actionBar.setCustomView(view);
		
		header_title=(TextView)view.findViewById(R.id.openshop_header_title);
		header_title.setText(getResources().getString(R.string.openshop_label_dashboard));
    }
	
	/*@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.home_menu, menu);
	        return true;
	    }*/
	 
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
				
			 case R.id.context_menu:
		            if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) 
		            {
		                mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
		            } 
			default:
				return super.onOptionsItemSelected(item);
				
		}
	}
	
	
//----------------------Context Menu Functionality [Start]--------------------------
    
   /* private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }
    
    private List<MenuObject> getMenuObjects() 
    {
		List<MenuObject> menuObjects = new ArrayList<MenuObject>();

		MenuObject close = new MenuObject();
		close.setResource(R.drawable.icn_close);

		MenuObject youritem = new MenuObject("Your Items");
		youritem.setResource(R.drawable.your_item_menuicon);

		MenuObject orders = new MenuObject("Orders");
		orders.setResource(R.drawable.menu_order_icon);

		MenuObject dispute = new MenuObject("Dispute");
		dispute.setResource(R.drawable.menu_dispute_icon);

		menuObjects.add(close);
		menuObjects.add(youritem);
		menuObjects.add(orders);
		menuObjects.add(dispute);

		return menuObjects;
    }
    
    
    public void onMenuItemClick(View clickedView, int position) 
    {
        if(position==0)
        {
        	
        }
        else if(position==1)
        {
        	Intent intent=new Intent(OpenShopPage.this,YourItemPage.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        else if(position==2)
        {
        	Intent intent=new Intent(OpenShopPage.this,OrdersPage.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        else if(position==3)
        {
        	Intent intent=new Intent(OpenShopPage.this,DisputeOrders.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        
    }*/
	
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