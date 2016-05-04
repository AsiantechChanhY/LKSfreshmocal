package com.shopsy;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
import com.shopsy.Pojo.Recommended_Pojo;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.ExpandableHeightGridView;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.Recent_ProductAdapter;
import com.shopsy.adapter.Recommended_Banner_Adapter;
import com.shopsy.adapter.Recommended_ProductAdapter;
import com.shopsy.adapter.Recommended_Shop_Adapter;
import com.shopsy.viewPagerIndicator.CirclePageIndicator;

public class RecommendTab extends HockeyFragment
{
	private Context context;
	private ActionBar actionBar;
	private ArrayList<Recommended_Pojo> products_itemList;
	private ArrayList<Recommended_Pojo> shop_itemList;
	private ArrayList<Recommended_Pojo> recent_itemList;
	private ArrayList<Recommended_Pojo> banner_itemList;
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	private SessionManager session;
	private CommonIDSessionManager commonsession;
	private String userid, commonid;
	private RelativeLayout loading,nointernet,mainlayout,serverError;
	private ExpandableHeightGridView product_gridview;
	private ExpandableHeightGridView recent_gridview;
	private ExpandableHeightGridView shop_gridview;
	private RelativeLayout recent_viewAll;
	private RelativeLayout product_layout,shop_layout,recent_layout;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private String itemcartcount;
	
	private Recommended_ProductAdapter adapter;
	private Recent_ProductAdapter recent_adapter;
	private Recommended_Shop_Adapter shop_adapter;
	
	
	private boolean show_progress_status=true;
	private boolean show_product_status=true;
	private boolean show_shop_status=true;
	private boolean show_recent_status=true;
	private boolean show_banner_status=true;

	private ImageView recommended_loading;
	private String asyntask_name="normal";
	
	private boolean cart_onresume=false;
	
	JsonObjectRequest jsonObjReq;

	 private ViewPager viewPager;
     private CirclePageIndicator pageindicator;
     private Recommended_Banner_Adapter viewpager_adapter;
     private String homePage_identify;
     
	@Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.recommended_tab, container, false);
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
       		
       		
       		product_gridview.setOnItemClickListener(new OnItemClickListener()
       		{
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					Intent intent=new Intent(getActivity(),DetailPage.class);
    				intent.putExtra("productid", products_itemList.get(position).getFeature_product_productId());
    				startActivity(intent);
    				getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
				}
			});
       		
       		
       		recent_gridview.setOnItemClickListener(new OnItemClickListener()
       		{
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					Intent intent=new Intent(getActivity(),DetailPage.class);
    				intent.putExtra("productid", recent_itemList.get(position).getRecent_product_productId());
    				startActivity(intent);
    				getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
				}
			});
       		
       		shop_gridview.setOnItemClickListener(new OnItemClickListener()
       		{
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					Intent intent = new Intent(getActivity(),AllShop_ProductPage.class);
					intent.putExtra("shopurl", shop_itemList.get(position).getFeature_shop_seourl());
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
				}
			});
       		
	       	 recent_viewAll.setOnClickListener(new OnClickListener()
	    	 {
				@Override
				public void onClick(View v) {
	
					Intent intent=new Intent(getActivity(),AllRecentProducts.class);
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
      									JsonRequest(Iconstant.recommended_url + userid);
      									
      									actionBar.setDisplayShowCustomEnabled(true);
      								}
      								else
      								{
      									asyntask_name="swipe";
      									nointernet.setVisibility(View.GONE);
      									JsonRequest(Iconstant.recommended_url + commonid);
      									
      									actionBar.setDisplayShowCustomEnabled(true);
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
       		
       		product_gridview.setExpanded(true);
       		recent_gridview.setExpanded(true);
       		shop_gridview.setExpanded(true);
         }
	 }

	@SuppressWarnings("deprecation")
	private void initialize() 
	{
		products_itemList = new ArrayList<Recommended_Pojo>();
		shop_itemList= new ArrayList<Recommended_Pojo>();
		recent_itemList= new ArrayList<Recommended_Pojo>();
		banner_itemList= new ArrayList<Recommended_Pojo>();
		
		cd = new ConnectionDetector(getActivity());
		isInternetPresent = cd.isConnectingToInternet();
		
		
		session = new SessionManager(getActivity());
		commonsession = new CommonIDSessionManager(getActivity());

		product_gridview=(ExpandableHeightGridView)getView().findViewById(R.id.recommended_tab_featureproducts_gridView);
		recent_gridview=(ExpandableHeightGridView)getView().findViewById(R.id.recommended_tab_recentproducts_gridView);
		shop_gridview=(ExpandableHeightGridView)getView().findViewById(R.id.recommended_tab_featureshop_gridView);
		loading=(RelativeLayout)getView().findViewById(R.id.recommended_loading_layout);
		nointernet=(RelativeLayout)getView().findViewById(R.id.recommended_nointernet_layout);
		mainlayout=(RelativeLayout)getView().findViewById(R.id.recommended_main_layout);
		serverError=(RelativeLayout)getView().findViewById(R.id.recommended_server_error_layout);
		
		recent_viewAll=(RelativeLayout)getView().findViewById(R.id.recommended_tab_recentproducts_viewAll_layout);
		
		product_layout=(RelativeLayout)getView().findViewById(R.id.recommended_tab_featureproducts_layout);
		shop_layout=(RelativeLayout)getView().findViewById(R.id.recommended_tab_featureshop_layout);
		recent_layout=(RelativeLayout)getView().findViewById(R.id.recommended_tab_recentproducts_layout);
		
		recommended_loading=(ImageView)getView().findViewById(R.id.recommended_loading_gif);
		
		viewPager = (ViewPager)getView().findViewById(R.id.recommended_tab_view_pager_header);
        pageindicator = (CirclePageIndicator)getView().findViewById(R.id.recommended_tab_view_pager_indicator_header);
		
		swipeRefreshLayout = (SwipeRefreshLayout)getView().findViewById(R.id.recommended_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(true);
		
				
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		// get user data from session
		HashMap<String, String> user1 = commonsession.getUserDetails();
		commonid = user1.get(CommonIDSessionManager.KEY_COMMONID);
		
		HashMap<String, String> home = session.getHomepage();
		homePage_identify = home.get(SessionManager.KEY_HOMEPAGE_IDENTIFY);
		
		// code to disable actionbar title
		actionBar.setDisplayShowTitleEnabled(false);

		
		Glide.with(context)
	    .load(R.drawable.loadinganimation)
	    .asGif()
	    .crossFade()
	    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
	    .into(recommended_loading);
		
		// to pass common id when user has not logged in
			if (isInternetPresent) 
			{
				if(session.isLoggedIn())
				{
					nointernet.setVisibility(View.GONE);
					JsonRequest(Iconstant.recommended_url + userid);
				}
				else
				{
					nointernet.setVisibility(View.GONE);
					JsonRequest(Iconstant.recommended_url + commonid);
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
			
			System.out.println("--------------Recommended url-------------------"+Url);
			
			 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) 
						{

							System.out.println("--------------Recommended response -----------"+response);
							try {

									itemcartcount = response.getString("cartCount");
									session.SetCartCount(itemcartcount);
									
									JSONArray jarray = response.getJSONArray("featured_product_details");
									if(jarray.length()>0)
									{
										products_itemList.clear();
										
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
											Recommended_Pojo items = new Recommended_Pojo();

											items.setFeature_product_productName(object.getString("productName"));
											items.setFeature_product_Price(response.getString("currencySymbol")+ object.getString("Price"));
											items.setFeature_product_productId(object.getString("productId"));
											items.setFeature_product_Image(object.getString("Image"));
											items.setFeature_product_storeName(object.getString("storeName"));
											items.setFeature_product_favStatus(object.getString("favStatus"));
											
											products_itemList.add(items);
										}
										
										show_product_status=true;
									}
									else
									{
										    products_itemList.clear();
										    show_product_status=false;
									}
									
									
									
									JSONArray recent_array = response.getJSONArray("recent_product_details");
									if(recent_array.length()>0)
									{
										recent_itemList.clear();
										for (int j = 0; j < recent_array.length(); j++) 
										{
											JSONObject recent_object = recent_array.getJSONObject(j);
											Recommended_Pojo items = new Recommended_Pojo();
											
											items.setRecent_product_productName(recent_object.getString("productName"));
											items.setRecent_product_Price(response.getString("currencySymbol")+ recent_object.getString("Price"));
											items.setRecent_product_productId(recent_object.getString("productId"));
											items.setRecent_product_Image(recent_object.getString("Image"));
											items.setRecent_product_storeName(recent_object.getString("storeName"));
											items.setRecent_product_favStatus(recent_object.getString("favStatus"));
											
											recent_itemList.add(items);
										}
										
										show_recent_status=true;
									}
									else
									{
										recent_itemList.clear();
										show_recent_status=false;
									}
									
									
									JSONArray shop_array = response.getJSONArray("featured_shop_details");
									if(shop_array.length()>0)
									{
										shop_itemList.clear();
										
										int length=0 ;
										if(shop_array.length()==3)
										{
											length=2;
										}
										else
										{
											length=shop_array.length();
										}
										
										for (int  k= 0; k<length ; k++) 
										{
											JSONObject shop_object = shop_array.getJSONObject(k);
											Recommended_Pojo items = new Recommended_Pojo();

											items.setFeature_shop_seourl(shop_object.getString("shopseourl"));
											items.setFeature_shop_seller_banner(shop_object.getString("seller_banner"));
											items.setFeature_shop_profilePic(shop_object.getString("profile_pic"));
											items.setFeature_shop_username(shop_object.getString("user_name"));
											items.setFeature_shop_fullName(shop_object.getString("full_name"));
											
											shop_itemList.add(items);
										}
										
										show_shop_status=true;
									}
									else
									{
										shop_itemList.clear();
										show_shop_status=false;
									}
									
									
									
									JSONArray banner_array = response.getJSONArray("slider_details");
									if(banner_array.length()>0)
									{
										banner_itemList.clear();
										for (int l = 0; l < banner_array.length(); l++) 
										{
											JSONObject banner_object = banner_array.getJSONObject(l);
											Recommended_Pojo items = new Recommended_Pojo();

											items.setBanner_description(banner_object.getString("description"));
											items.setBanner_Image(banner_object.getString("image"));
											items.setBanner_title(banner_object.getString("title"));
											items.setBanner_link(banner_object.getString("link"));
											items.setBanner_id(banner_object.getString("id"));
											
											banner_itemList.add(items);
										}
										
										show_banner_status=true;
									}
									else
									{
										banner_itemList.clear();
										show_banner_status=false;
									}
									
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
							
							
							//------on post execute-----
							
							stopload();
							
							if (show_progress_status) 
							{
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
									adapter = new Recommended_ProductAdapter(context,products_itemList);
									product_gridview.setAdapter(adapter);
								}
								else
								{
									product_layout.setVisibility(View.GONE);
								}
								
								
								if(show_recent_status)
								{
									recent_layout.setVisibility(View.VISIBLE);
									recent_adapter = new Recent_ProductAdapter(context,recent_itemList);
									recent_gridview.setAdapter(recent_adapter);
								}
								else
								{
									recent_layout.setVisibility(View.GONE);
								}
								
								
								if(show_shop_status)
								{
									shop_layout.setVisibility(View.VISIBLE);
									shop_adapter = new Recommended_Shop_Adapter(context,shop_itemList);
									shop_gridview.setAdapter(shop_adapter);
								}
								else
								{
									shop_layout.setVisibility(View.GONE);
								}
								
								
								if(show_banner_status)
								{
									viewPager.setVisibility(View.VISIBLE);
									pageindicator.setVisibility(View.VISIBLE);
									System.out.println("length of banner"+banner_itemList.size());
									viewpager_adapter = new Recommended_Banner_Adapter(getActivity(),banner_itemList);
				                    viewPager.setAdapter(viewpager_adapter);
				                    pageindicator.setViewPager(viewPager);
				                    pageindicator.notifyDataSetChanged();
								}
								else
								{
									viewPager.setVisibility(View.GONE);
									pageindicator.setVisibility(View.GONE);
								}
								
								mainlayout.setVisibility(View.VISIBLE);
								loading.setVisibility(View.GONE);
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
