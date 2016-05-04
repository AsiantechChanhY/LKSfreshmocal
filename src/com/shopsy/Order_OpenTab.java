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
import android.support.v7.app.ActionBarActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.shopsy.HockeyApp.HockeyFragment;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Pojo.OrderOpen_Pojo;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.OrderOpen_Adapter;
import com.shopsy.app.Add_Item_1;

public class Order_OpenTab extends HockeyFragment
{
	private Context context;
	private ActionBar actionBar;
	
	private SessionManager session;
	private String userid;
	
	private ArrayList<OrderOpen_Pojo> itemList;
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	
	private RelativeLayout loading,nointernet,mainlayout;
	private ListView listview;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private boolean show_progress_status=true;

	private ProgressBar loadmore_progressbar;
	private View footerview;
	private boolean loadingMore = false;
	private int checkpagepos = 0;
	
	
	JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;
	 
	private String asyntask_name="normal";
	private ImageView open_loading;
	
	private OrderOpen_Adapter adapter;
	private TextView emptyText;
	private String itemcartcount;
	
	
	private FloatingActionMenu float_menu;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview =inflater.inflate(R.layout.order_opentab,container,false);
        
        context = getActivity();
		ActionBarActivity actionBarActivity = (ActionBarActivity) getActivity();
		actionBar = actionBarActivity.getSupportActionBar();
		initialize(rootview);
		initialize_FloatingMenu(rootview);
		
		listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent=new Intent(getActivity(),OrderDetails_Seller.class);
				intent.putExtra("OrderID", itemList.get(position).getOrderId());
				intent.putExtra("OrderUserID", itemList.get(position).getUserId());
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});
		
		listview.setOnScrollListener(new OnScrollListener()
		{
			private int mPreviousVisibleItem;
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

								    ConnectionDetector cd = new ConnectionDetector(getActivity());
									boolean isInternetPresent = cd.isConnectingToInternet();
								
										if (isInternetPresent) 
										{
											if(session.isLoggedIn())
											{
												Loadmore_JsonRequest(Iconstant.order_open_url + userid+ "&pageId="+checkpagepos);
											}
										} 
										else 
										{
											Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
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
				   
				   
				   if (firstVisibleItem > mPreviousVisibleItem) 
				   {
					   
					      float_menu.hideMenu(true);
	               }
				   else if (firstVisibleItem < mPreviousVisibleItem) 
				   {
					      float_menu.hideMenu(true);
	               }
	                 mPreviousVisibleItem = firstVisibleItem;
			}
		});
		
		
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() 
		{
		  @Override
			public void onRefresh() 
	        {
			  
			    ConnectionDetector cd = new ConnectionDetector(getActivity());
				boolean isInternetPresent = cd.isConnectingToInternet();
				
						if (isInternetPresent) 
						{
							if(session.isLoggedIn())
							{
								asyntask_name="swipe";
								nointernet.setVisibility(View.GONE);
								actionBar.setDisplayShowCustomEnabled(true);
								
								JsonRequest(Iconstant.order_open_url + userid+ "&pageId=1");
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
		
		return rootview;
	}

	@SuppressWarnings("deprecation")
	private void initialize(View rootview) 
	{

		itemList = new ArrayList<OrderOpen_Pojo>();
		cd = new ConnectionDetector(getActivity());
		isInternetPresent = cd.isConnectingToInternet();
		
		session = new SessionManager(getActivity());	
		
		listview=(ListView)rootview.findViewById(R.id.order_opentab_listview);
		loading=(RelativeLayout)rootview.findViewById(R.id.order_opentab_loading_layout);
		nointernet=(RelativeLayout)rootview.findViewById(R.id.order_opentab_nointernet_layout);
		mainlayout=(RelativeLayout)rootview.findViewById(R.id.order_opentab_main_layout);
		open_loading=(ImageView)rootview.findViewById(R.id.order_opentab_loading_gif);
		emptyText = (TextView)rootview.findViewById(R.id.order_opentab_list_empty);
		
		swipeRefreshLayout = (SwipeRefreshLayout)rootview.findViewById(R.id.order_opentab_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(true);
		
		
		//code for gridView header
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			footerview= inflater.inflate(R.layout.gridview_footer_loading, null);
		
			loadmore_progressbar=(ProgressBar)footerview.findViewById(R.id.grid_footer_progressbar);
				
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		
		// code to disable actionBar title
		actionBar.setDisplayShowTitleEnabled(false);

		// ------------------code to add custom menu in action bar--------------------------------------
		
		actionBar.setDisplayShowCustomEnabled(true);

		 
		 Glide.with(context)
		    .load(R.drawable.loadinganimation)
		    .asGif()
		    .crossFade()
		    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
		    .into(open_loading);
		 
		 
		// to pass common id when user has not logged in
			
		if (isInternetPresent) 
		{
			if (session.isLoggedIn())
			{
				nointernet.setVisibility(View.GONE);
				JsonRequest(Iconstant.order_open_url + userid+ "&pageId=1");
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

	
	private void initialize_FloatingMenu(View rootview) 
	{
		float_menu=(FloatingActionMenu)rootview.findViewById(R.id.ordersopen_floating_menu);
		
		ContextThemeWrapper context = new ContextThemeWrapper(getActivity(), R.style.float_MenuButtonsStyle);
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
        
        float_menu.addMenuButton(YourItem_Fab);
        float_menu.addMenuButton(Additem_Fab);
        float_menu.addMenuButton(Reviews_Fab);
        float_menu.addMenuButton(Dispute_Fab);
        float_menu.addMenuButton(Conversation_Fab);
        
        float_menu.setClosedOnTouchOutside(true);
        
        YourItem_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),YourItemPage.class);
	        	startActivity(intent);
	        	getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
	        	
	        	float_menu.close(true);
			}
		});
        
        Additem_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),Add_Item_1.class);
	        	startActivity(intent);
	        	getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
	        	
	        	float_menu.close(true);
			}
		});
        
        
        Reviews_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {

				Intent intent=new Intent(getActivity(),ReviewsPage_Seller.class);
	        	startActivity(intent);
	        	getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
	        	
	        	float_menu.close(true);
			}
		});
       
        
        Dispute_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),DisputeOrders.class);
	        	startActivity(intent);
	        	getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
	        	
	        	float_menu.close(true);
			}
		});
        
        Conversation_Fab.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),ConversationPage_Seller.class);
	        	startActivity(intent);
	        	getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
	        	
	        	float_menu.close(true);
			}
		});
      
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
		
		System.out.println("--------------orderOpen tab url-------------------"+Url);
		
		 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) 
					{
						System.out.println("--------------orderOpen tab response -----------"+response);

						try {

							JSONArray jarray = response.getJSONArray("Orders");
							checkpagepos = Integer.parseInt(response.getString("pagePos"));
							itemcartcount = response.getString("cartCount");
							
							session.SetCartCount(itemcartcount);
							
							if (jarray.length() > 0) 
							{
								itemList.clear();
								
								for (int i = 0; i < jarray.length(); i++)
								{
									JSONObject object = jarray.getJSONObject(i);
									OrderOpen_Pojo items = new OrderOpen_Pojo();
							        	 
									items.setOrderDate(object.getString("orderDate"));
									items.setName(object.getString("Name"));
									items.setOrderId(object.getString("orderId"));
									items.setOrderTotal(response.getString("currencySymbol")+object.getString("orderTotal"));
									items.setOrderStatus(object.getString("orderStatus"));
									items.setUserId(object.getString("userId"));
									items.setUserImage(object.getString("userImage"));
									items.setProduct_name(object.getString("product_name"));
									items.setProduct_id(object.getString("product_id"));
									items.setProductImage(object.getString("Image"));
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
						
						if(show_progress_status)
						{
							
							adapter = new OrderOpen_Adapter(getActivity(),itemList);
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
				
				System.out.println("--------------orderOpen loadmore url-------------------"+Url);
				
				loadmore_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) 
							{
								System.out.println("--------------orderOpen loadmore response -----------"+response);

								try {

									JSONArray jarray = response.getJSONArray("Orders");
									checkpagepos = Integer.parseInt(response.getString("pagePos"));
									itemcartcount = response.getString("cartCount");
									
									session.SetCartCount(itemcartcount);
									
									if (jarray.length() > 0) 
									{
										for (int i = 0; i < jarray.length(); i++)
										{
											JSONObject object = jarray.getJSONObject(i);
											OrderOpen_Pojo items = new OrderOpen_Pojo();
								        	 
											items.setOrderDate(object.getString("orderDate"));
											items.setName(object.getString("Name"));
											items.setOrderId(object.getString("orderId"));
											items.setOrderTotal(response.getString("currencySymbol")+object.getString("orderTotal"));
											items.setOrderStatus(object.getString("orderStatus"));
											items.setUserId(object.getString("userId"));
											items.setUserImage(object.getString("userImage"));
											items.setProduct_name(object.getString("product_name"));
											items.setProduct_id(object.getString("product_id"));
											items.setProductImage(object.getString("Image"));
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
