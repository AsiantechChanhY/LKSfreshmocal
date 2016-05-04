package com.shopsy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.material_floatbutton_libraryprem.FloatingActionButton;
import com.example.material_floatbutton_libraryprem.FloatingActionMenu;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Pojo.YourItem_Pojo;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.JsonSuggestionParser;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.YourItem_Adapter;
import com.shopsy.app.Add_Item_1;
import com.shopsy.app.Add_Item_Edit_Final;
import com.shopsy.bean.SuggestGetSet;

public class YourItemPage extends HockeyActivity
{
	private static ArrayList<YourItem_Pojo> itemList;
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	
	private static Context context;
	private static Activity activity;
	private ActionBar actionBar;
	
	private SessionManager session;
	private static String userid;
	
	private static RelativeLayout loading;

	private RelativeLayout nointernet;

	private static RelativeLayout mainlayout;
	private static ListView listview;
	private AutoCompleteTextView search;
	private static SwipeRefreshLayout swipeRefreshLayout = null;

	
	private static int checkpagepos = 0;
	private static YourItem_Adapter adapter;
	private boolean loadingMore = false;
	private static boolean show_progress_status=true;

	private ImageView youritem_loading;
	private static String asyntask_name="normal";
	
	private ProgressBar loadmore_progressbar;
	private View headerView,footerview;
	
	static JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;

	private TextView header_title;
	private ImageView filter_icon;
	private static TextView emptyText;
	
		
		 List<SuggestGetSet> new_suggestions;
		 static String Loadmorecheck="";
		 
		 private String search_textname="";
		 private static String selected_filter_sorting="",selected_filter_status="";
	
		 
		 private FloatingActionMenu float_menu;
		 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.your_itempage);
		
		activity=YourItemPage.this;
		context = getApplicationContext();
  		ActionBarActivity actionBarActivity = (ActionBarActivity)YourItemPage.this;
  		actionBar = actionBarActivity.getSupportActionBar();
  		initialize();
  		initialize_FloatingMenu();
  		
  		
  		
  		filter_icon.setOnClickListener(new OnClickListener()
  		{
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(YourItemPage.this,YourItem_Filter.class);
 	        	startActivity(intent);
 	        	overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			}
		});
  		
  		
  		search.setOnItemClickListener(new OnItemClickListener()
  		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				search_textname = new_suggestions.get(position).getName();
				search_textname = search_textname.replaceAll("\\s","+");
				
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
			    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); 
				
			    search.setText("");
			    
			    Loadmorecheck="search";
			    JsonRequest(Iconstant.youritem_search_url+search_textname+"&sellerId="+ userid+ "&pageId=" + 1);
			}
		});
  		
  		
  		listview.setOnItemClickListener(new OnItemClickListener()
  		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				int new_position=position-1;
				
				cd = new ConnectionDetector(YourItemPage.this);
				isInternetPresent = cd.isConnectingToInternet();
				
				if (isInternetPresent) 
				{		
					String pro_id = itemList.get(new_position).getProductId().toString();				
					Intent int_Additem1 = new Intent(context, Add_Item_Edit_Final.class);
					int_Additem1.putExtra("EDIT_PROID", pro_id);
					startActivity(int_Additem1);
					overridePendingTransition(R.anim.enter,R.anim.exit);
					search.setText("");	
				} 
				else 
				{
					Toast.makeText(YourItemPage.this, "No internet connection", Toast.LENGTH_SHORT).show();
				}
			}
		});
  		
  		
  		listview.setOnScrollListener(new OnScrollListener()
  		{
  			 
  			@Override
  			public void onScrollStateChanged(AbsListView view, int scrollState)
  			{
  				int threshold = 1;
  				int count = listview.getCount();
  				
  				if (scrollState == SCROLL_STATE_IDLE)
  				{
  					if (listview.getLastVisiblePosition() >= count - threshold && !(loadingMore))
  					{
  						
  						if (swipeRefreshLayout.isRefreshing()) 
  						 {
  							 //nothing happen(code to block loadmore functionality when swipe to refresh is loading)
  						 }
  						 else
  						 {

  								if(show_progress_status)
  								{

  								    ConnectionDetector cd = new ConnectionDetector(YourItemPage.this);
  									boolean isInternetPresent = cd.isConnectingToInternet();
  								
  										if (isInternetPresent) 
  										{
  											if(Loadmorecheck.equalsIgnoreCase("search"))
  											{
  												Loadmore_JsonRequest(Iconstant.youritem_search_url + search_textname+"&sellerId="+userid+ "&pageId=" + (checkpagepos+1));	
  											}
  											else if(Loadmorecheck.equalsIgnoreCase("filter"))
  											{
  												Loadmore_JsonRequest(Iconstant.yourItem_url + userid+ selected_filter_sorting+selected_filter_status+"&pageId=" + (checkpagepos+1));
  											}
  											else
  											{
  												Loadmore_JsonRequest(Iconstant.yourItem_url + userid+ "&pageId=" + (checkpagepos+1));
  											}
  											
  										} 
  										else 
  										{
  											Toast.makeText(YourItemPage.this, "No internet connection", Toast.LENGTH_SHORT).show();
  										}
  								
  								}
  						 }
  				}
  			  }
  		    }
  			@Override
  			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
  			{
  				   boolean enable = false;
  				   
  				   if(isInternetPresent)
  				   {
  					   if(listview != null && listview.getChildCount() > 0)
  				        {
  				            // check if the first item of the list is visible
  				            boolean firstItemVisible = listview.getFirstVisiblePosition() == 0;
  				            // check if the top of the first item is visible
  				            boolean topOfFirstItemVisible = listview.getChildAt(0).getTop() == 0;
  				            // enabling or disabling the refresh layout
  				            enable = firstItemVisible && topOfFirstItemVisible;
  				        }
  				        swipeRefreshLayout.setEnabled(enable);
  				   }
  				   else
  				   {
  					   swipeRefreshLayout.setEnabled(true);
  				   }
  				
  			}
  		});
  		
  		
  		
  		
  		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() 
  		{
  		  @Override
  			public void onRefresh() 
  	        {
  			  
  			  if(loadingMore)
  			  {
  				  //nothing happen(code to block swipe functionality when loadmore is loading)
  				  swipeRefreshLayout.setRefreshing(false);
  			  }
  			  else
  			  {
  				    ConnectionDetector cd = new ConnectionDetector(YourItemPage.this);
  					boolean isInternetPresent = cd.isConnectingToInternet();
  					
  							if (isInternetPresent) 
  							{
  									asyntask_name="swipe";
  									nointernet.setVisibility(View.GONE);
  									JsonRequest(Iconstant.yourItem_url + userid+ "&pageId=" + 1);
  									
  									actionBar.setDisplayShowCustomEnabled(true);
  							} 
  							else 
  							{
  								swipeRefreshLayout.setRefreshing(false);
  								mainlayout.setVisibility(View.GONE);
  								nointernet.setVisibility(View.VISIBLE);
  								
  								// making hide custom menu and action bar title
  								actionBar.setDisplayShowCustomEnabled(false);
  								actionBar.setDisplayShowTitleEnabled(false);
  							}
  			  }
      		}
  		});
	}

	@SuppressLint("InflateParams") @SuppressWarnings("deprecation")
	private void initialize()
	{

		itemList = new ArrayList<YourItem_Pojo>();
		cd = new ConnectionDetector(YourItemPage.this);
		isInternetPresent = cd.isConnectingToInternet();
		session = new SessionManager(YourItemPage.this);

		listview=(ListView)findViewById(R.id.youritems_listview);
		loading=(RelativeLayout)findViewById(R.id.youritems_loading_layout);
		nointernet=(RelativeLayout)findViewById(R.id.youritems_nointernet_layout);
		mainlayout=(RelativeLayout)findViewById(R.id.youritems_main_layout);
		emptyText = (TextView)findViewById(R.id.youritems_list_empty);
		search=(AutoCompleteTextView)findViewById(R.id.youritems_search_editText);
		
		youritem_loading=(ImageView)findViewById(R.id.youritems_loading_gif);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.youritems_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(true);
		
		//code for listview header
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				headerView = inflater.inflate(R.layout.grid_blankspace_header, null);
				footerview= inflater.inflate(R.layout.gridview_footer_loading, null);
				
				loadmore_progressbar=(ProgressBar)footerview.findViewById(R.id.grid_footer_progressbar);
				
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		// code to disable actionbar title
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		// ------------------code to add custom menu in action bar--------------------------------------
		
		actionBar.setDisplayShowCustomEnabled(true);
		View view = YourItemPage.this.getLayoutInflater().inflate(R.layout.your_item_header, null);
		actionBar.setCustomView(view);

		filter_icon=(ImageView)view.findViewById(R.id.youritem_header_filter_icon);
		header_title=(TextView)view.findViewById(R.id.youritem_header_title);
		header_title.setText("Your Items");
		

	//----------AutoComplete textView-------
		search.setThreshold(1);	
		search.setAdapter(new YourItem_Suggestion_Adapter(YourItemPage.this, search.getText().toString()));
		
		
		Glide.with(context)
	    .load(R.drawable.loadinganimation)
	    .asGif()
	    .crossFade()
	    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
	    .into(youritem_loading);
		
		// to pass common id when user has not logged in
			if (isInternetPresent) 
			{
				if(session.isLoggedIn())
				{
					nointernet.setVisibility(View.GONE);
					JsonRequest(Iconstant.yourItem_url + userid+ "&pageId=" + 1);
				}
			} 
			else
			{
				mainlayout.setVisibility(View.GONE);
				nointernet.setVisibility(View.VISIBLE);

				// making hide custom menu and action bar title
				actionBar.setDisplayShowCustomEnabled(false);
				actionBar.setDisplayShowTitleEnabled(false);
			}
		
				
				listview.addHeaderView(headerView);
				listview.addFooterView(footerview);
	}
	
	private void initialize_FloatingMenu() 
	{
		float_menu=(FloatingActionMenu)findViewById(R.id.youritem_floating_menu);
		
		ContextThemeWrapper context = new ContextThemeWrapper(YourItemPage.this, R.style.float_MenuButtonsStyle);
		
		final FloatingActionButton Additem_Fab = new FloatingActionButton(context);
		Additem_Fab.setButtonSize(FloatingActionButton.SIZE_MINI);
		Additem_Fab.setLabelText(getResources().getString(R.string.activiy_tab_addproducts_label));
		Additem_Fab.setImageResource(R.drawable.fab_add);
		
		final FloatingActionButton Reviews_Fab = new FloatingActionButton(context);
		Reviews_Fab.setButtonSize(FloatingActionButton.SIZE_MINI);
		Reviews_Fab.setLabelText(getResources().getString(R.string.activiy_tab_reviews_label));
		Reviews_Fab.setImageResource(R.drawable.float_review_icon);
        
        final FloatingActionButton Orders_Fab = new FloatingActionButton(context);
        Orders_Fab.setButtonSize(FloatingActionButton.SIZE_MINI);
        Orders_Fab.setBackgroundColor(R.color.fab_blue_color);
        Orders_Fab.setLabelText(getResources().getString(R.string.activiy_tab_orders_label));
        Orders_Fab.setImageResource(R.drawable.menu_order_icon);
        
        final FloatingActionButton Dispute_Fab = new FloatingActionButton(context);
        Dispute_Fab.setButtonSize(FloatingActionButton.SIZE_MINI);
        Dispute_Fab.setBackgroundColor(R.color.fab_blue_color);
        Dispute_Fab.setLabelText(getResources().getString(R.string.activiy_tab_dispute_label));
        Dispute_Fab.setImageResource(R.drawable.menu_dispute_icon);
        
        final FloatingActionButton Conversation_Fab = new FloatingActionButton(context);
        Conversation_Fab.setButtonSize(FloatingActionButton.SIZE_MINI);
        Conversation_Fab.setBackgroundColor(R.color.fab_blue_color);
        Conversation_Fab.setLabelText(getResources().getString(R.string.activiy_tab_conversation_label));
        Conversation_Fab.setImageResource(R.drawable.conversation_icon_float);
        
        float_menu.addMenuButton(Additem_Fab);
        float_menu.addMenuButton(Reviews_Fab);
        float_menu.addMenuButton(Orders_Fab);
        float_menu.addMenuButton(Dispute_Fab);
        float_menu.addMenuButton(Conversation_Fab);
        
        
        float_menu.setClosedOnTouchOutside(true);
        
        Additem_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(YourItemPage.this,Add_Item_1.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.enter, R.anim.exit);
	        	
	        	float_menu.close(true);
			}
		});
        
        
        Reviews_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {

				Intent intent=new Intent(YourItemPage.this,ReviewsPage_Seller.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.enter, R.anim.exit);
	        	
	        	float_menu.close(true);
			}
		});
        
        
        Orders_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(YourItemPage.this,OrdersPage.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.enter, R.anim.exit);

	        	float_menu.close(true);
			}
		});
        
        Dispute_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(YourItemPage.this,DisputeOrders.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.enter, R.anim.exit);
	        	
	        	float_menu.close(true);
			}
		});
        
        Conversation_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(YourItemPage.this,ConversationPage_Seller.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.enter, R.anim.exit);
	        	
	        	float_menu.close(true);
			}
		});
      
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) {
			case android.R.id.home:

				onBackPressed();
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				finish();
			    return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	
	
	public static class FILTER
	{
		public static void sorting(String sorting, String item_status)
		{
			selected_filter_sorting=sorting;
			selected_filter_status=item_status;
			
			System.out.println("--------------sorting output-------------"+sorting);
			System.out.println("--------------item_status output-------------"+item_status);
			
			 Loadmorecheck="filter";
			JsonRequest(Iconstant.yourItem_url+userid+ selected_filter_sorting+selected_filter_status+"&pageId=" + 1);
		}
	}
	

	//--------load animation------
	private static void startload()
	{
		
		if(asyntask_name.equalsIgnoreCase("normal"))
		{
			mainlayout.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
		}
		else
		{
			swipeRefreshLayout.setRefreshing(true);
		}
	}
	
	private static void stopload()
	{
		
		if(asyntask_name.equalsIgnoreCase("normal"))
		{
			mainlayout.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
		}
		else
		{
			mainlayout.setVisibility(View.VISIBLE);
			swipeRefreshLayout.setRefreshing(false);
		}
	}
	
	
	
	// -------------------------code for JSon Request----------------------------------
	
		private static void JsonRequest(String Url) 
		{
			startload();
			
			System.out.println("--------------Your Items url-------------------"+Url);
			
			 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) 
						{

							System.out.println("--------------Your Items response -----------"+response);
							try {

									JSONArray jarray = response.getJSONArray("Listings");

									checkpagepos = Integer.parseInt(response.getString("pagePos"));
									
									if(jarray.length()>0)
									{
										itemList.clear();
										
										for (int j = 0; j < jarray.length(); j++) 
										{
											JSONObject object = jarray.getJSONObject(j);
											YourItem_Pojo items = new YourItem_Pojo();

											items.setProductName(object.getString("productName"));
											items.setPrice(response.getString("currencySymbol")+ object.getString("Price"));
											items.setProductId(object.getString("productId"));
											items.setImage(object.getString("Image"));
											items.setStatus(object.getString("Status"));
											items.setStock(object.getString("Stock"));
											items.setQuantity(object.getString("quantity"));
											
											itemList.add(items);
										}
										
										show_progress_status=true;
									}
									else
									{
											itemList.clear();

											show_progress_status=false;
									}
									
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
							
							
							//------on post execute-----
							
							stopload();
							
							if (show_progress_status) 
							{
								
								adapter = new YourItem_Adapter(activity,itemList);
								listview.setAdapter(adapter);
								
								mainlayout.setVisibility(View.VISIBLE);
								loading.setVisibility(View.GONE);
								emptyText.setVisibility(View.GONE);
							}
							else
							{
								emptyText.setVisibility(View.VISIBLE);
			                    listview.setEmptyView(emptyText);
							}
							
							if (swipeRefreshLayout.isRefreshing()) 
						    {
					            swipeRefreshLayout.setRefreshing(false);
					        }
							
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) 
						{
							 
							stopload();
							swipeRefreshLayout.setEnabled(true);
							
							if (swipeRefreshLayout.isRefreshing()) 
						    {
					            swipeRefreshLayout.setRefreshing(false);
					        }
							
							 if (error instanceof TimeoutError || error instanceof NoConnectionError) {
							        Toast.makeText(context,"Unable to fetch data from server",Toast.LENGTH_LONG).show();
							    } else if (error instanceof AuthFailureError) {
							        //TODO
							    	Toast.makeText(context,"AuthFailureError",Toast.LENGTH_LONG).show();
							    } else if (error instanceof ServerError) {
							       //TODO
							    	Toast.makeText(context,"ServerError",Toast.LENGTH_LONG).show();
							    } else if (error instanceof NetworkError) {
							    	
							    	Toast.makeText(context,"NetworkError",Toast.LENGTH_LONG).show();
							      //TODO
							    } else if (error instanceof ParseError) {
							       //TODO
							    	Toast.makeText(context,"ParseError",Toast.LENGTH_LONG).show();
							    }
						}
					});
			 
			 jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, 
		                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
		                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(jsonObjReq);
		}
		
		
		
		// -------------------------code for JSON Request LoadMore----------------------------------
		
		private void Loadmore_JsonRequest(String Url) 
		{
			if(show_progress_status)
			{
				loadmore_progressbar.setVisibility(View.VISIBLE);
			}
			
			loadingMore = true;
			
			System.out.println("--------------youritems loadmore url-------------------"+Url);
			
			 loadmore_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) 
						{
							try {

								JSONArray jarray = response.getJSONArray("Listings");
								checkpagepos = Integer.parseInt(response.getString("pagePos"));
								
								if(jarray.length()>0)
								{
									for (int j = 0; j < jarray.length(); j++) 
									{
										JSONObject object = jarray.getJSONObject(j);
										YourItem_Pojo items = new YourItem_Pojo();

										items.setProductName(object.getString("productName"));
										items.setPrice(response.getString("currencySymbol")+ object.getString("Price"));
										items.setProductId(object.getString("productId"));
										items.setImage(object.getString("Image"));
										items.setStatus(object.getString("Status"));
										items.setStock(object.getString("Stock"));
										items.setQuantity(object.getString("quantity"));
										
										itemList.add(items);
									}
									
									show_progress_status=true;
								}
								else
								{
										show_progress_status=false;
								}
								
						} catch (JSONException e) {
							e.printStackTrace();
						}
							
							
							//------on post execute-----
							
							loadingMore = false;
							loadmore_progressbar.setVisibility(View.GONE);
							
							adapter.notifyDataSetChanged();
							
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) 
						{
							 
							loadingMore = false;
							loadmore_progressbar.setVisibility(View.GONE);
							 
							 if (error instanceof TimeoutError || error instanceof NoConnectionError) {
							        Toast.makeText(context,"Unable to fetch data from server",Toast.LENGTH_LONG).show();
							    } else if (error instanceof AuthFailureError) {
							        //TODO
							    	Toast.makeText(context,"AuthFailureError",Toast.LENGTH_LONG).show();
							    } else if (error instanceof ServerError) {
							       //TODO
							    	Toast.makeText(context,"ServerError",Toast.LENGTH_LONG).show();
							    } else if (error instanceof NetworkError) {
							    	
							    	Toast.makeText(context,"NetworkError",Toast.LENGTH_LONG).show();
							      //TODO
							    } else if (error instanceof ParseError) {
							       //TODO
							    	Toast.makeText(context,"ParseError",Toast.LENGTH_LONG).show();
							    }
						}
					});

			 
			 loadmore_jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, 
		                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
		                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			 
			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(loadmore_jsonObjReq);
		}
		
		
		
		
		
		
		//----------------------Your Items Suggestion Adapter------------------
		public class YourItem_Suggestion_Adapter extends ArrayAdapter<String> 
		{		 
		    protected static final String TAG = "SuggestionAdapter";
		    private List<String> suggestions;
		   
		    
		    private SessionManager session;
			private String userid;
		    
		    public YourItem_Suggestion_Adapter(Context context, String nameFilter) 
		    {
		        super(context, android.R.layout.simple_dropdown_item_1line);
		        suggestions = new ArrayList<String>();
		        session = new SessionManager(context);
		    }
		 
		    @Override
		    public int getCount() 
		    {
		        return suggestions.size();
		    }
		 
		    @Override
		    public String getItem(int index) {
		        return suggestions.get(index);
		    }
		 
		    @Override
		    public Filter getFilter() {
		        Filter myFilter = new Filter() {
		            @Override
		            protected FilterResults performFiltering(CharSequence constraint) {
		                FilterResults filterResults = new FilterResults();
		                JsonSuggestionParser jp=new JsonSuggestionParser();
		                if (constraint != null) {
		                    // A class that queries a web API, parses the data and
		                    // returns an ArrayList<GoEuroGetSet>
		                	
		                	// get user data from session
		            		HashMap<String, String> user = session.getUserDetails();
		            		userid = user.get(SessionManager.KEY_USERID);
		                	
		                    new_suggestions =jp.getParseJsonWCF(constraint.toString(), userid);
		                    suggestions.clear();
		                    for (int i=0;i<new_suggestions.size();i++) 
		                    {
		                        suggestions.add(new_suggestions.get(i).getName());
		                    }
		 
		                    // Now assign the values and count to the FilterResults
		                    // object
		                    filterResults.values = suggestions;
		                    filterResults.count = suggestions.size();
		                }
		                return filterResults;
		            }
		 
		            @Override
		            protected void publishResults(CharSequence contraint,
		                    FilterResults results) {
		                if (results != null && results.count > 0) {
		                    notifyDataSetChanged();
		                } else {
		                    notifyDataSetInvalidated();
		                }
		            }
		        };
		        return myFilter;
		    }
		 
		}
		
		
		
		
		//-----------------Move Back on pressed phone back button------------------		
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) 
		{
			if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
				YourItemPage.this.finish();
				YourItemPage.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				return true;
			}
			return false;
		}	
		
		
		// --------------------------code to destroy asynTask when another activity is called---------------------------
		@Override
		public void onDestroy() 
		{
			
			asyntask_name="normal";
			
			if(jsonObjReq!=null)
			{
				jsonObjReq.cancel();
			}

			if(loadmore_jsonObjReq!=null)
			{
				loadmore_jsonObjReq.cancel();
			}
			
			super.onDestroy();
		}	
		
		
}
