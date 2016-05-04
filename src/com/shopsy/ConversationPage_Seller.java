package com.shopsy;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.shopsy.Pojo.Conversation_Pojo;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.Conversation_Adapter;
import com.shopsy.app.Add_Item_1;

public class ConversationPage_Seller extends HockeyActivity
{
	private ArrayList<Conversation_Pojo> itemList;
	private TextView header_title;
	private RelativeLayout cartlayout;
	
	private RelativeLayout loading,nointernet,mainlayout;
	private ListView listview;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private boolean show_progress_status=true;

	private ProgressBar loadmore_progressbar;
	private View footerview;
	private boolean loadingMore = false;
	private int checkpagepos = 0;
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	
	private Context context;
	private ActionBar actionBar;
	
	private SessionManager session;
	private String userid;
	private View view;
	private ImageView conversation_loading;
	
	JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;
	 
	private String asyntask_name="normal";
	
	private static Conversation_Adapter adapter;
	private TextView emptyText;
	
	 private FloatingActionMenu float_menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversationpage_seller);
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		initialize();
		initialize_FloatingMenu();
		
		
		listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				itemList.get(position).setStatus("Read");
				itemList.get(position).setNewMsg("0");
				
				Intent intent=new Intent(ConversationPage_Seller.this,ConversationDetail.class);
				intent.putExtra("convId", itemList.get(position).getConv_id());
				intent.putExtra("chat_name", itemList.get(position).getSender_name());
				intent.putExtra("chat_image", itemList.get(position).getSender_image());
				intent.putExtra("Type", "Seller");
				startActivity(intent);
				ConversationPage_Seller.this.overridePendingTransition(R.anim.enter, R.anim.exit);
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

								    ConnectionDetector cd = new ConnectionDetector(ConversationPage_Seller.this);
									boolean isInternetPresent = cd.isConnectingToInternet();
								
										if (isInternetPresent) 
										{
											if(session.isLoggedIn())
											{
												Loadmore_JsonRequest(Iconstant.conversation_url + userid + "&commonId=" + userid +"&type=S"+"&pageId="+checkpagepos);
											}
										} 
										else 
										{
											Toast.makeText(ConversationPage_Seller.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
			  
			    ConnectionDetector cd = new ConnectionDetector(ConversationPage_Seller.this);
				boolean isInternetPresent = cd.isConnectingToInternet();
				
						if (isInternetPresent) 
						{
							if(session.isLoggedIn())
							{
								asyntask_name="swipe";
								nointernet.setVisibility(View.GONE);
								actionBar.setDisplayShowCustomEnabled(true);
								
								JsonRequest(Iconstant.conversation_url + userid + "&commonId=" + userid+"&type=S"+ "&pageId=0");
							}
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
		});
	}
	
	@SuppressWarnings("deprecation")
	private void initialize()
	{
		itemList = new ArrayList<Conversation_Pojo>();
		cd = new ConnectionDetector(ConversationPage_Seller.this);
		isInternetPresent = cd.isConnectingToInternet();
		
		session = new SessionManager(ConversationPage_Seller.this);

		listview=(ListView)findViewById(R.id.conversation_seller_listview);
		loading=(RelativeLayout)findViewById(R.id.conversation_seller_loading_layout);
		nointernet=(RelativeLayout)findViewById(R.id.conversation_seller_nointernet_layout);
		mainlayout=(RelativeLayout)findViewById(R.id.conversation_seller_main_layout);
		conversation_loading=(ImageView)findViewById(R.id.conversation_seller_loading_gif);
		emptyText = (TextView)findViewById(R.id.conversation_seller_list_empty);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.conversation_seller_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(true);
		
		
		//code for gridView header
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		view = View.inflate(ConversationPage_Seller.this, R.layout.home_custom_header, null);
		actionBar.setCustomView(view);

		cartlayout = (RelativeLayout) view.findViewById(R.id.cartrelativelayout);
		header_title=(TextView)view.findViewById(R.id.home_header_title);

		 cartlayout.setVisibility(View.GONE);
		 header_title.setText("Conversation");				
		
		 
		 Glide.with(context)
		    .load(R.drawable.loadinganimation)
		    .asGif()
		    .crossFade()
		    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
		    .into(conversation_loading);
		 
		 
		   // to pass common id when user has not logged in
			
					if (isInternetPresent) 
					{
						if(session.isLoggedIn())
						{
							nointernet.setVisibility(View.GONE);
							JsonRequest(Iconstant.conversation_url + userid + "&commonId=" + userid+"&type=S"+"&pageId=0");
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
		            
					
			listview.addFooterView(footerview);
	}
	
	
	private void initialize_FloatingMenu() 
	{
		float_menu=(FloatingActionMenu)findViewById(R.id.conversation_seller_floating_menu);
		
		ContextThemeWrapper context = new ContextThemeWrapper(ConversationPage_Seller.this, R.style.float_MenuButtonsStyle);
		final FloatingActionButton YourItem_Fab = new FloatingActionButton(context);
		YourItem_Fab.setButtonSize(FloatingActionButton.SIZE_MINI);
		YourItem_Fab.setLabelText(getResources().getString(R.string.activiy_tab_youritem_label));
		YourItem_Fab.setImageResource(R.drawable.your_item_menuicon);
		
		
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
        
        
        float_menu.addMenuButton(YourItem_Fab);
        float_menu.addMenuButton(Additem_Fab);
        float_menu.addMenuButton(Reviews_Fab);
        float_menu.addMenuButton(Orders_Fab);
        float_menu.addMenuButton(Dispute_Fab);
        
        
        float_menu.setClosedOnTouchOutside(true);
        
        YourItem_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ConversationPage_Seller.this,YourItemPage.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.enter, R.anim.exit);
	        	
	        	float_menu.close(true);
			}
		});
        
        Additem_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ConversationPage_Seller.this,Add_Item_1.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.enter, R.anim.exit);
	        	
	        	float_menu.close(true);
			}
		});
        
        
        Reviews_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {

				Intent intent=new Intent(ConversationPage_Seller.this,OrdersPage.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.enter, R.anim.exit);

	        	float_menu.close(true);
			}
		});
        
        
        Orders_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(ConversationPage_Seller.this,OrdersPage.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.enter, R.anim.exit);

	        	float_menu.close(true);
			}
		});
        
        Dispute_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ConversationPage_Seller.this,DisputeOrders.class);
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
	
	
	//----refresh this adapter from conversationPage Detail class-----
	public static void RefreshAdapter()
	{
		adapter.notifyDataSetChanged();
	}
	
	 //--------load animation------
	private void startload()
	{
		
		if(asyntask_name.equalsIgnoreCase("normal"))
		{
			mainlayout.setVisibility(View.GONE);
			nointernet.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
			
		}
		else
		{
			swipeRefreshLayout.setRefreshing(true);
		}
	}
	
	private void stopload()
	{
		
		if(asyntask_name.equalsIgnoreCase("normal"))
		{
			mainlayout.setVisibility(View.VISIBLE);
			nointernet.setVisibility(View.GONE);
			loading.setVisibility(View.GONE);
		}
		else
		{
			mainlayout.setVisibility(View.VISIBLE);
			swipeRefreshLayout.setRefreshing(false);
		}
	}
	
	
	// -------------------------code for JSon Request----------------------------------
	
			private void JsonRequest(String Url) 
			{
				startload();
				
				System.out.println("--------------conversation url-------------------"+Url);
				
				 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) 
							{
								System.out.println("--------------conversation response -----------"+response);

								try {

									JSONArray jarray = response.getJSONArray("conversation");
									checkpagepos = Integer.parseInt(response.getString("pagePos"));
									
									if (jarray.length() > 0) 
									{
										itemList.clear();
										
										for (int i = 0; i < jarray.length(); i++)
										{
											JSONObject object = jarray.getJSONObject(i);
											Conversation_Pojo items = new Conversation_Pojo();
											
											items.setConv_id(object.getString("conv_id"));
											items.setMessage(object.getString("message"));
											items.setNewMsg(object.getString("newMsg"));
											items.setSender_image(object.getString("sender_image"));
											items.setSender_name(object.getString("sender_name"));
											items.setStatus(object.getString("status"));
											items.setStarred(object.getString("starred"));
											items.setTime(object.getString("time"));
											
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
								
								if(show_progress_status)
								{
									adapter = new Conversation_Adapter(ConversationPage_Seller.this,itemList);
									listview.setAdapter(adapter);
								}
								else
								{
									mainlayout.setVisibility(View.VISIBLE);
									nointernet.setVisibility(View.GONE);
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

				// Adding request to request queue
				AppController.getInstance().addToRequestQueue(jsonObjReq);
				
				jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, 
		                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
		                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			}

			
			
			
	// -------------------------code for JSON Request LoadMore----------------------------------
			
					private void Loadmore_JsonRequest(String Url) 
					{
						if(show_progress_status)
						{
							loadmore_progressbar.setVisibility(View.VISIBLE);
						}
						
						loadingMore = true;
						
						System.out.println("--------------conversation loadmore url-------------------"+Url);
						
						loadmore_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
									@Override
									public void onResponse(JSONObject response) 
									{
										System.out.println("--------------conversation loadmore response -----------"+response);

										try {

											JSONArray jarray = response.getJSONArray("conversation");
											checkpagepos = Integer.parseInt(response.getString("pagePos"));
											
											if (jarray.length() > 0) 
											{
												for (int i = 0; i < jarray.length(); i++)
												{
													JSONObject object = jarray.getJSONObject(i);
													Conversation_Pojo items = new Conversation_Pojo();
													
													items.setConv_id(object.getString("conv_id"));
													items.setMessage(object.getString("message"));
													items.setNewMsg(object.getString("newMsg"));
													items.setSender_image(object.getString("sender_image"));
													items.setSender_name(object.getString("sender_name"));
													items.setStatus(object.getString("status"));
													items.setStarred(object.getString("starred"));
													items.setTime(object.getString("time"));
													
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
										
										if(show_progress_status)
										{
											adapter.notifyDataSetChanged();
										}
									}
								}, new Response.ErrorListener() {
									@Override
									public void onErrorResponse(VolleyError error) 
									{
										 
										loadingMore = false;
										loadmore_progressbar.setVisibility(View.GONE);
										
										swipeRefreshLayout.setEnabled(true);
										
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

						// Adding request to request queue
						AppController.getInstance().addToRequestQueue(loadmore_jsonObjReq);
						
						loadmore_jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, 
				                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
				                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
