package com.shopsy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.material_contextmenu_library.ContextMenuDialogFragment;
import com.example.material_contextmenu_library.MenuObject;
import com.example.material_contextmenu_library.MenuParams;
import com.example.material_contextmenu_library.OnMenuItemClickListener_Prem;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Subclass.ActionBarActivity_Subclass_Homepage;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.HomeMenuListAdapter;

/**
 * @author Prem Kumar
 * 
 */
public class NavigationDrawerPage extends ActionBarActivity_Subclass_Homepage
		implements OnMenuItemClickListener_Prem {

	private ListView mDrawerList;
	private static RelativeLayout mDrawer;
	private static DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private Context context;
	private String[] title;
	private int[] icon;
	private HomeMenuListAdapter mMenuAdapter;
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
	private RelativeLayout openshop_layout;
	// calling login session
	private SessionManager session;

	/*
	 * Fragment user profile fragment = new UserProfile();
	 */
	
	Fragment userProfilefragment;
	Fragment homefragment;
	Fragment feedfragment;
	Fragment trendingfragment;
	Fragment allshopfragment;
	Fragment conversationfragment;
	Fragment categoriesfragment;
	Fragment settingsfragment;

	private String Selected_class = "trending";

	public static NavigationDrawerPage fregment_Homepage;

	private String IsSeller = "";

	private Boolean isInternetPresent = false;
	private ConnectionDetector cd;
	private TextView openshop_text;

	// ------------Initialization for Context menu-------------
	private DialogFragment mMenuDialogFragment;
	private FragmentManager fragmentManager;

	public class RefreshReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.package.ACTION_DRAWER_REFRESH")) {
				drawer_refresh();
			}
			else if (intent.getAction().equals("com.package.UPDATECLASS")) {
				
				class_refresh(intent.getStringExtra("SelectedClass"));
			}
		}
	}

	private RefreshReceiver refreshReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation_drawer_page);
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		fregment_Homepage = this;
		
		
		 userProfilefragment = new UserProfile();
		 homefragment = new HomePage();
		 feedfragment = new ActivityFeed();
		 trendingfragment = new TrendingPage();
		 allshopfragment = new AllShopPage();
		 conversationfragment = new ConversationPage();
		 categoriesfragment = new CategoryPage();
		 settingsfragment = new SettingsPage();
		
		
		// -----code to refresh drawer using broadcast receiver-----
		refreshReceiver = new RefreshReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.package.ACTION_DRAWER_REFRESH");
		intentFilter.addAction("com.package.UPDATECLASS");
		registerReceiver(refreshReceiver, intentFilter);
		// Session class instance
		session = new SessionManager(context);
		fragmentManager = getSupportFragmentManager();
		mDrawerList = (ListView) findViewById(R.id.navList);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawer = (RelativeLayout) findViewById(R.id.drawer);
		openshop_layout = (RelativeLayout) findViewById(R.id.drawer_openshop_layout);
		openshop_text=(TextView)findViewById(R.id.drawer_openshop_textview);
		
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		IsSeller = user.get(SessionManager.KEY_IS_SELLER);
		
		if (session.isLoggedIn()) 
		{
			if(IsSeller.equalsIgnoreCase("No"))
			{
				openshop_text.setText(getResources().getString(R.string.drawer_list_openshop_label));
			}
			else
			{
				openshop_text.setText(getResources().getString(R.string.drawer_list_myshop_label));
			}
			openshop_layout.setVisibility(View.VISIBLE);
		} else {
			openshop_layout.setVisibility(View.GONE);
		}

		addDrawerItems();
		setupDrawer();

		// -----context menu code-----
		initMenuFragment();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// code to set actionbar background
		colorDrawable.setColor(0xff1A237E);
		actionBar.setBackgroundDrawable(colorDrawable);

		if (savedInstanceState == null) {
			FragmentTransaction tx = getSupportFragmentManager()
					.beginTransaction();
			tx.replace(R.id.content_frame, homefragment);
			tx.commit();
		}

		openshop_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// get user data from session
				HashMap<String, String> user = session.getUserDetails();
				IsSeller = user.get(SessionManager.KEY_IS_SELLER);

				if (IsSeller.equalsIgnoreCase("No")) {
					Toast.makeText(NavigationDrawerPage.this,
							"Check Website To Shop Your Shop",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(NavigationDrawerPage.this,
							OpenShopPage.class);
					startActivity(intent);
					overridePendingTransition(R.anim.enter, R.anim.exit);
				}
			}
		});

	}

	// ------code to refresh drawer when login from activty[Access from
	// Fragment_Subclass]------

	public void drawer_refresh() {
		if (session.isLoggedIn()) {
			title = new String[] {
					"UserName",
					getResources().getString(R.string.drawer_list_home_label),
					getResources().getString(R.string.drawer_list_browse_label),
					getResources().getString(R.string.drawer_list_conversation_label),
					getResources().getString(R.string.drawer_list_purchases_label),
					getResources().getString(R.string.drawer_list_allshop_label),
					getResources().getString(R.string.drawer_list_settings_label) };
			icon = new int[] { R.drawable.userprofile, R.drawable.home_icon,
					R.drawable.categoryicon, R.drawable.chaticon, R.drawable.purchaseicon,
					R.drawable.allshopicon, R.drawable.settingsicon };
		} else {
			title = new String[] {
					"SignIn",
					getResources().getString(R.string.drawer_list_home_label),
					getResources().getString(R.string.drawer_list_browse_label),
					getResources()
							.getString(R.string.drawer_list_allshop_label),
					getResources().getString(
							R.string.drawer_list_settings_label) };
			icon = new int[] { R.drawable.signin, R.drawable.home_icon,
					R.drawable.categoryicon, R.drawable.allshopicon,
					R.drawable.settingsicon };
		}
		mMenuAdapter = new HomeMenuListAdapter(context, title, icon);
		mDrawerList.setAdapter(mMenuAdapter);
		mMenuAdapter.notifyDataSetChanged();
		// -----context menu code-----
		initMenuFragment();
		if (session.isLoggedIn()) {
			
			if(IsSeller.equalsIgnoreCase("No"))
			{
				openshop_text.setText(getResources().getString(R.string.drawer_list_openshop_label));
			}
			else
			{
				openshop_text.setText(getResources().getString(R.string.drawer_list_myshop_label));
			}
			
			openshop_layout.setVisibility(View.VISIBLE);
		} else {
			openshop_layout.setVisibility(View.GONE);
		}
	}
	// ----code to close drawer from Activity Tab page----
	public static void CloseDrawer() {
		if (mDrawerLayout!=null&&mDrawerLayout.isDrawerOpen(mDrawer)) {
			mDrawerLayout.closeDrawer(mDrawer);
		}
	}

	// ----code to refresh particular class after logedin----
	public void classname_refresh(String classname) {
		Selected_class = classname;
	}

	private void addDrawerItems() {
		if (session.isLoggedIn()) {
			title = new String[] {
					"UserName",
					getResources().getString(R.string.drawer_list_home_label),
					getResources().getString(R.string.drawer_list_browse_label),
					getResources().getString(R.string.drawer_list_conversation_label),
					getResources().getString(R.string.drawer_list_purchases_label),
					getResources().getString(R.string.drawer_list_allshop_label),
					getResources().getString(R.string.drawer_list_settings_label) };
			icon = new int[] { R.drawable.userprofile, R.drawable.home_icon,
					R.drawable.categoryicon, R.drawable.chaticon,R.drawable.purchaseicon,
					R.drawable.allshopicon, R.drawable.settingsicon };
		} else {
			title = new String[] {
					"SignIn",
					getResources().getString(R.string.drawer_list_home_label),
					getResources().getString(R.string.drawer_list_browse_label),
					getResources()
							.getString(R.string.drawer_list_allshop_label),
					getResources().getString(
							R.string.drawer_list_settings_label) };
			icon = new int[] { R.drawable.signin, R.drawable.home_icon,
					R.drawable.categoryicon, R.drawable.allshopicon,
					R.drawable.settingsicon };
		}
		mMenuAdapter = new HomeMenuListAdapter(NavigationDrawerPage.this,
				title, icon);
		mDrawerList.setAdapter(mMenuAdapter);
		mMenuAdapter.notifyDataSetChanged();
		mDrawerList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						FragmentTransaction ft = getSupportFragmentManager()
								.beginTransaction();

						if (session.isLoggedIn()) {
							switch (position) {

							case 0:
								ft.replace(R.id.content_frame,
										userProfilefragment);	
								break;
							case 1:
								//ft.replace(R.id.content_frame, homefragment);
								
								Intent intent = new Intent(NavigationDrawerPage.this,HomePage_Activity.class);
								startActivity(intent);
								overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
								
								break;
							case 2:
								ft.replace(R.id.content_frame,categoriesfragment);
								break;
							case 3:
								ft.replace(R.id.content_frame,
										conversationfragment);
								break;
								
							case 4:
								//ft.replace(R.id.content_frame,purchasefragment);
								
								Intent intent1 = new Intent(NavigationDrawerPage.this,PurchasePage.class);
								startActivity(intent1);
								overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
								
								break;
								
							case 5:
								ft.replace(R.id.content_frame, allshopfragment);
								break;
							case 6:
								ft.replace(R.id.content_frame, settingsfragment);
								break;
							}
						} else {
							switch (position) {
							case 0:
								Selected_class = "userprofile";
								favourit_popup(Selected_class);
								break;
							case 1:
								//Selected_class = "home";
								//ft.replace(R.id.content_frame, homefragment);
								Intent intent = new Intent(NavigationDrawerPage.this,HomePage_Activity.class);
								startActivity(intent);
								overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
								break;
							case 2:
								Selected_class = "categories";
								ft.replace(R.id.content_frame,
										categoriesfragment);
								break;
							case 3:
								Selected_class = "allshop";
								ft.replace(R.id.content_frame, allshopfragment);
								break;
							case 4:
								Selected_class = "home";
								ft.replace(R.id.content_frame, settingsfragment,Selected_class);
								break;

							}
						}
						
				       // FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
				       // ft.detach(mFragment);

						ft.commit();
						mDrawerList.setItemChecked(position, true);
						mDrawerLayout.closeDrawer(mDrawer);
					}
				});
	}

	private void setupDrawer() {
		mDrawerToggle = new ActionBarDrawerToggle(NavigationDrawerPage.this,
				mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				// getSupportActionBar().setTitle("Navigation!");
				getSupportActionBar().setIcon(R.drawable.logo);
				// invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
				supportInvalidateOptionsMenu();
			}

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				// getSupportActionBar().setTitle(mActivityTitle);
				getSupportActionBar().setIcon(R.drawable.logo);
				// invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
				supportInvalidateOptionsMenu();
			}
		};

		mDrawerToggle.setDrawerIndicatorEnabled(true);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();

		/*
		 * //noInspection SimplifiableIfStatement if (id ==
		 * R.id.action_settings) { return true; }
		 */

		// Activate the navigation drawer toggle
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.context_menu:
			if (fragmentManager
					.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
				mMenuDialogFragment.show(fragmentManager,
						ContextMenuDialogFragment.TAG);
			}
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	// ----------------------Context Menu Functionality
	// [Start]--------------------------

	private void initMenuFragment() {
		MenuParams menuParams = new MenuParams();
		menuParams.setActionBarSize((int) getResources().getDimension(
				R.dimen.tool_bar_height));
		menuParams.setMenuObjects(getMenuObjects());
		menuParams.setClosableOutside(false);
		mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
	}

	private List<MenuObject> getMenuObjects() {
		// You can use any [resource, bitmap, drawable, color] as image:
		// item.setResource(...)
		// item.setBitmap(...)
		// item.setDrawable(...)
		// item.setColor(...)
		// You can set image ScaleType:
		// item.setScaleType(ScaleType.FIT_XY)
		// You can use any [resource, drawable, color] as background:
		// item.setBgResource(...)
		// item.setBgDrawable(...)
		// item.setBgColor(...)
		// You can use any [color] as text color:
		// item.setTextColor(...)
		// You can set any [color] as divider color:
		// item.setDividerColor(...)

		List<MenuObject> menuObjects = new ArrayList<MenuObject>();

		if (session.isLoggedIn()) {
			MenuObject close = new MenuObject();
			close.setResource(R.drawable.icn_close);

			MenuObject search = new MenuObject("Search Products");
			search.setResource(R.drawable.search_icon_menu);
			/*
			 * MenuObject purchase = new MenuObject("My Purchases");
			 * BitmapDrawable bd = new BitmapDrawable(getResources(),
			 * BitmapFactory.decodeResource(getResources(),
			 * R.drawable.purchase_icon_menu)); purchase.setDrawable(bd);
			 * MenuObject review = new MenuObject("Reviews");
			 * review.setResource(R.drawable.review_icon_menu);
			 */

			MenuObject about = new MenuObject("About");
			about.setResource(R.drawable.about_icon_menu);

			MenuObject help = new MenuObject("Help");
			help.setResource(R.drawable.help_menu_icon);

			menuObjects.add(close);
			menuObjects.add(search);
			// menuObjects.add(purchase);
			// menuObjects.add(review);
			menuObjects.add(about);
			menuObjects.add(help);
		} else {
			MenuObject close = new MenuObject();
			close.setResource(R.drawable.icn_close);

			MenuObject search = new MenuObject("Search Products");
			search.setResource(R.drawable.search_icon_menu);

			MenuObject about = new MenuObject("About");
			about.setResource(R.drawable.about_icon_menu);

			MenuObject help = new MenuObject("Help");
			help.setResource(R.drawable.help_menu_icon);

			menuObjects.add(close);
			menuObjects.add(search);
			menuObjects.add(about);
			menuObjects.add(help);
		}

		return menuObjects;
	}

	public void onMenuItemClick(View clickedView, int position) {
		cd = new ConnectionDetector(NavigationDrawerPage.this);
		isInternetPresent = cd.isConnectingToInternet();

		if (session.isLoggedIn()) {

			if (position == 1) {
				Intent intent = new Intent(NavigationDrawerPage.this,
						SearchPage.class);
				startActivity(intent);
				overridePendingTransition(R.anim.enter, R.anim.exit);
			} else if (position == 2) {
				if (isInternetPresent) {
					Intent intent = new Intent(NavigationDrawerPage.this,
							WebviewPage.class);
					intent.putExtra("title", "About");
					intent.putExtra("weburl", Iconstant.aboutus_webview_url);
					startActivity(intent);
					overridePendingTransition(R.anim.enter, R.anim.exit);
				} else {
					Toast.makeText(NavigationDrawerPage.this,
							"No Internet Connection", Toast.LENGTH_SHORT)
							.show();
				}
			} else if (position == 3) {
				if (isInternetPresent) {
					Intent intent = new Intent(NavigationDrawerPage.this,
							WebviewPage.class);
					intent.putExtra("title", "Help");
					intent.putExtra("weburl", Iconstant.help_webview_url);
					startActivity(intent);
					overridePendingTransition(R.anim.enter, R.anim.exit);
				} else {
					Toast.makeText(NavigationDrawerPage.this,
							"No Internet Connection", Toast.LENGTH_SHORT)
							.show();
				}
			}
		} else {
			if (position == 1) {
				Intent intent = new Intent(NavigationDrawerPage.this,
						SearchPage.class);
				startActivity(intent);
				overridePendingTransition(R.anim.enter, R.anim.exit);
			} else if (position == 2) {
				if (isInternetPresent) {
					Intent intent = new Intent(NavigationDrawerPage.this,
							WebviewPage.class);
					intent.putExtra("title", "About");
					intent.putExtra("weburl", Iconstant.aboutus_webview_url);
					startActivity(intent);
					overridePendingTransition(R.anim.enter, R.anim.exit);
				} else {
					Toast.makeText(NavigationDrawerPage.this,
							"No Internet Connection", Toast.LENGTH_SHORT)
							.show();
				}
			} else if (position == 3) {
				if (isInternetPresent) {
					Intent intent = new Intent(NavigationDrawerPage.this,
							WebviewPage.class);
					intent.putExtra("title", "Help");
					intent.putExtra("weburl", Iconstant.help_webview_url);
					startActivity(intent);
					overridePendingTransition(R.anim.enter, R.anim.exit);
				} else {
					Toast.makeText(NavigationDrawerPage.this,
							"No Internet Connection", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}

	// ----------------------Context Menu Functionality [END]--------------------------
	
	
	public void class_refresh(String classname)
	{
		
		

		if(classname.equalsIgnoreCase("trending"))
		{
			Fragment fragment = new TrendingPage();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			ft.commit();	
		}
		else if(classname.equalsIgnoreCase("feed"))
		{
			Fragment fragment = new ActivityFeed();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			ft.commit();
		}
		else if(classname.equalsIgnoreCase("allshop"))
		{
			Fragment fragment = new AllShopPage();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			ft.commit();
		}
		else if(classname.equalsIgnoreCase("categories"))
		{
			Fragment fragment = new CategoryPage();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			ft.commit();
		}
		else if(classname.equalsIgnoreCase("openshop"))
		{
			Intent intent=new Intent(NavigationDrawerPage.this,OpenShopPage.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.enter, R.anim.exit);
		}
		else if(classname.equalsIgnoreCase("userprofile"))
		{
			  Fragment fragment = new UserProfile();
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.replace(R.id.content_frame, fragment);
				ft.commit();
		}
		else if(classname.equalsIgnoreCase("home"))
		{
			Fragment fragment = new HomePage();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			ft.commit();
		}
		
	}
	
	
	
	
	
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
		CloseDrawer();
		
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		// Unregister the logout receiver
		unregisterReceiver(refreshReceiver);
		super.onDestroy();
	}
}
