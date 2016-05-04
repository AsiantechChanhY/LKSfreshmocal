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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.shopsy.HockeyApp.HockeyFragment;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Pojo.Picks_Pojo;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.ExpandableHeightGridView;
import com.shopsy.Utils.HorizontalListView;
import com.shopsy.Utils.HorizontalListView.OnScrollStateChangedListener;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.Picks_Categories_Adapter;
import com.shopsy.adapter.Picks_Product_Adpater;

public class ShopsyPicks extends HockeyFragment
{
	private Context context;
	private ActionBar actionBar;
	
	private ArrayList<Picks_Pojo> categories_itemList;
	private ArrayList<Picks_Pojo> product_itemList;
		
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	
	private SessionManager session;
	private CommonIDSessionManager commonsession;
	private String userid, commonid;
	private String homePage_identify;
	
	private RelativeLayout loading,nointernet,mainlayout,serverError;
	private RelativeLayout recent_viewAll;
	private ExpandableHeightGridView gridview;
	private HorizontalListView listview;
	private RelativeLayout product_layout,categories_layout;
	
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private String itemcartcount;
	
	private Picks_Product_Adpater adapter;
	private Picks_Categories_Adapter cat_adapter;
	
	private ArrayList<String> categoriesspinnerlist = new ArrayList<String>();
	private ArrayList<String> id = new ArrayList<String>();
	
	private boolean show_categories_status=true;
	private boolean show_product_status=true;

	private ImageView picks_loading;
	private String asyntask_name="normal";
	
	JsonObjectRequest jsonObjReq;
	private boolean cart_onresume=false;

	@Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.shopsypicks, container, false);
	}
	
	 @Override
     public void onActivityCreated(final Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);
         
         if (savedInstanceState == null)
         {
        	 context = getActivity();
        	 ActionBarActivity actionBarActivity = (ActionBarActivity)getActivity();
        	 actionBar = actionBarActivity.getSupportActionBar();
        		
        	 initialize();
        	
        	 
        	 recent_viewAll.setOnClickListener(new OnClickListener()
        	 {
				@Override
				public void onClick(View v) {

					Intent intent=new Intent(getActivity(),AllRecentProducts.class);
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
				}
			});
        	 
        	 
        	gridview.setOnItemClickListener(new OnItemClickListener()
    		{
    			@Override
    			public void onItemClick(AdapterView<?> parent, View view,
    					int position, long id) 
    			{
    				
    				Intent intent=new Intent(getActivity(),DetailPage.class);
    				intent.putExtra("productid", product_itemList.get(position).getProductId());
    				startActivity(intent);
    				getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
    			}
    		});
        	
        	listview.setOnItemClickListener(new OnItemClickListener()
        	{
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long long_id) {
					
					Intent intent = new Intent(context, SubCategoriesPage.class);
					intent.putExtra("catposition", position);
					intent.putStringArrayListExtra("spinnerlist", categoriesspinnerlist);
					intent.putExtra("itemid", categories_itemList.get(position).getId());
					intent.putStringArrayListExtra("totalitemid", id);
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
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
      									JsonRequest(Iconstant.picks_url + userid);
      									
      									actionBar.setDisplayShowCustomEnabled(true);
      								}
      								else
      								{
      									asyntask_name="swipe";
      									nointernet.setVisibility(View.GONE);
      									JsonRequest(Iconstant.picks_url + commonid);
      									
      									actionBar.setDisplayShowCustomEnabled(true);
      								}
    							} 
    							else
    							{
    								swipeRefreshLayout.setRefreshing(false);
    								mainlayout.setVisibility(View.GONE);
    								nointernet.setVisibility(View.VISIBLE);
    							}
        		}
    		});
        	
        	listview.setOnScrollStateChangedListener(new OnScrollStateChangedListener()
        	{
				@Override
				public void onScrollStateChanged(ScrollState scrollState) {
					
					if (scrollState == ScrollState.SCROLL_STATE_IDLE)
    				{
						
						System.out.println("----------------SCROLL_STATE_IDLE----------------");
						
						if(homePage_identify.equalsIgnoreCase("Activity"))
						{
							HomePage_Activity.swipeOn();
						}
						else
						{
							HomePage.swipeOn();
						}
    					swipeRefreshLayout.setEnabled(true);
    			    }
					else if (scrollState == ScrollState.SCROLL_STATE_FLING)
    				{
						System.out.println("----------------SCROLL_STATE_FLING----------------");
						
						if(homePage_identify.equalsIgnoreCase("Activity"))
						{
							HomePage_Activity.swipeOff();
						}
						else
						{
							HomePage.swipeOff();
						}
    					swipeRefreshLayout.setEnabled(false);
    			    }
    				else if(scrollState == ScrollState.SCROLL_STATE_TOUCH_SCROLL)
    				{
    					System.out.println("----------------SCROLL_STATE_TOUCH_SCROLL----------------");
    					if(homePage_identify.equalsIgnoreCase("Activity"))
						{
							HomePage_Activity.swipeOff();
						}
						else
						{
							HomePage.swipeOff();
						}
    					swipeRefreshLayout.setEnabled(false);
    				}
    				
				}
			});

        	gridview.setExpanded(true);
         }
	 }
	 
	 @SuppressWarnings("deprecation")
		private void initialize() 
		{
		 	product_itemList = new ArrayList<Picks_Pojo>();
			categories_itemList= new ArrayList<Picks_Pojo>();
			
			cd = new ConnectionDetector(getActivity());
			isInternetPresent = cd.isConnectingToInternet();
			
			session = new SessionManager(getActivity());
			commonsession = new CommonIDSessionManager(getActivity());

			gridview=(ExpandableHeightGridView)getView().findViewById(R.id.picks_tab_recentproducts_gridView);
			listview=(HorizontalListView)getView().findViewById(R.id.picks_tab_categories_listview);
			loading=(RelativeLayout)getView().findViewById(R.id.picks_loading_layout);
			nointernet=(RelativeLayout)getView().findViewById(R.id.picks_nointernet_layout);
			mainlayout=(RelativeLayout)getView().findViewById(R.id.picks_main_layout);
			serverError=(RelativeLayout)getView().findViewById(R.id.picks_server_error_layout);
			
			recent_viewAll=(RelativeLayout)getView().findViewById(R.id.picks_tab_recentproducts_viewall);
			categories_layout=(RelativeLayout)getView().findViewById(R.id.picks_tab_categoties_layout);
			product_layout=(RelativeLayout)getView().findViewById(R.id.picks_tab_products_layout);
			
			picks_loading=(ImageView)getView().findViewById(R.id.picks_loading_gif);
			
			swipeRefreshLayout = (SwipeRefreshLayout)getView().findViewById(R.id.picks_swipe_refresh_layout);
			swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
			swipeRefreshLayout.setEnabled(true);
			
					
			// get user data from session
			HashMap<String, String> user = session.getUserDetails();
			userid = user.get(SessionManager.KEY_USERID);
			
			HashMap<String, String> home = session.getHomepage();
			homePage_identify = home.get(SessionManager.KEY_HOMEPAGE_IDENTIFY);

			// get user data from session
			HashMap<String, String> user1 = commonsession.getUserDetails();
			commonid = user1.get(CommonIDSessionManager.KEY_COMMONID);
			
			
			// code to disable actionbar title
			actionBar.setDisplayShowTitleEnabled(false);

			Glide.with(context)
		    .load(R.drawable.loadinganimation)
		    .asGif()
		    .crossFade()
		    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
		    .into(picks_loading);
			
			// to pass common id when user has not logged in
				if (isInternetPresent) 
				{
					if(session.isLoggedIn())
					{
						nointernet.setVisibility(View.GONE);
						JsonRequest(Iconstant.picks_url + userid);
					}
					else
					{
						nointernet.setVisibility(View.GONE);
						JsonRequest(Iconstant.picks_url + commonid);
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
			
		}
	 
	 
	   //--------load animation------
			private void startload()
			{
				
				if(asyntask_name.equalsIgnoreCase("normal"))
				{
					mainlayout.setVisibility(View.GONE);
					serverError.setVisibility(View.GONE);
					loading.setVisibility(View.VISIBLE);
				}
				else
				{
					swipeRefreshLayout.setRefreshing(true);
					serverError.setVisibility(View.GONE);
				}
			}
			
			private void stopload()
			{
				
				if(asyntask_name.equalsIgnoreCase("normal"))
				{
					mainlayout.setVisibility(View.VISIBLE);
					serverError.setVisibility(View.GONE);
					loading.setVisibility(View.GONE);
				}
				else
				{
					mainlayout.setVisibility(View.VISIBLE);
					serverError.setVisibility(View.GONE);
					swipeRefreshLayout.setRefreshing(false);
				}
			}
			
			
			
			// -------------------------code for JSon Request----------------------------------
			
			private void JsonRequest(String Url) 
			{
				startload();
				
				System.out.println("--------------picks url-------------------"+Url);
				
				 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) 
							{

								System.out.println("--------------picks response -----------"+response);
								try {

										itemcartcount = response.getString("cartCount");
										session.SetCartCount(itemcartcount);
										
										JSONArray jarray = response.getJSONArray("productDetails");
										if(jarray.length()>0)
										{
											product_itemList.clear();
											
											int length=0 ;
											if(jarray.length()==3)
											{
												length=2;
											}
											else
											{
												length=jarray.length();
											}
											
											for (int i = 0; i < length; i++) 
											{ 
												JSONObject object = jarray.getJSONObject(i);
												Picks_Pojo items = new Picks_Pojo();

												items.setProductName(object.getString("productName"));
												items.setPrice(response.getString("currencySymbol")+ object.getString("Price"));
												items.setProductId(object.getString("productId"));
												items.setImage(object.getString("Image"));
												items.setStoreName(object.getString("storeName"));
												items.setFavStatus(object.getString("favStatus"));
												
												product_itemList.add(items);
											}
											
											show_product_status=true;
										}
										else
										{
											    product_itemList.clear();
											    show_product_status=false;
										}
										
										
										
										JSONArray cat_array = response.getJSONArray("categoryDetails");
										if(cat_array.length()>0)
										{
											categories_itemList.clear();
											id.clear();
											categoriesspinnerlist.clear();
											
											for (int j = 0; j < cat_array.length(); j++) 
											{
												JSONObject cat_object = cat_array.getJSONObject(j);
												 Picks_Pojo items = new Picks_Pojo();
												
												items.setId(cat_object.getString("id"));
												items.setCategoryName(cat_object.getString("categoryName"));
												items.setCatimage(cat_object.getString("image"));
												items.setCatId(cat_object.getString("catId"));
												
												categories_itemList.add(items);
												id.add(cat_object.getString("id"));
												categoriesspinnerlist.add(cat_object.getString("categoryName"));
											}
											
											show_categories_status=true;
										}
										else
										{
											categories_itemList.clear();
											id.clear();
											categoriesspinnerlist.clear();
											show_categories_status=false;
										}
										
										
								} catch (JSONException e) {
									e.printStackTrace();
								}
								
								
								
								//------on post execute-----
								
								    stopload();
								
								    if(homePage_identify.equalsIgnoreCase("Activity"))
									{
								    	HomePage_Activity.cartcountRefresh();
									}
								    else
								    {
								    	HomePage.cartcountRefresh();
								    }
								    

									
									if(show_product_status)
									{
										product_layout.setVisibility(View.VISIBLE);
										adapter = new Picks_Product_Adpater(context,product_itemList);
										gridview.setAdapter(adapter);
									}
									else
									{
										product_layout.setVisibility(View.GONE);
									}
									
									
									if(show_categories_status)
									{
										categories_layout.setVisibility(View.VISIBLE);
										cat_adapter = new Picks_Categories_Adapter(context,categories_itemList);
										listview.setAdapter(cat_adapter);
									}
									else
									{
										categories_layout.setVisibility(View.GONE);
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
								 
								mainlayout.setVisibility(View.GONE);
								serverError.setVisibility(View.VISIBLE);
								loading.setVisibility(View.GONE);
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
				 
				 jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000, 
			                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
			                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

				// Adding request to request queue
				AppController.getInstance().addToRequestQueue(jsonObjReq);
			}
			
			
			
			// -----------------------------code for on Resume------------------------------------------
			
			  @Override public void onResume() 
			  {
				  if(cart_onresume)
				  {
					   if(homePage_identify.equalsIgnoreCase("Activity"))
						{
					    	HomePage_Activity.cartcountRefresh();
						}
					    else
					    {
					    	HomePage.cartcountRefresh();
					    }
				  }
		     	super.onResume(); 
			  }
			  
			  
			  
			  @Override
			  public void onPause()
			  {
				  cart_onresume=true;
				  super.onPause();
			  }
			
			// --------------------------code to destroy asynTask when another activity is called---------------------------
			@Override
			public void onDestroy() 
			{
				
				asyntask_name="normal";
				cart_onresume=false;
				
				    if(jsonObjReq!=null)
					{
						jsonObjReq.cancel();
					}

				super.onDestroy();
			}		
}
