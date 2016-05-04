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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Pojo.AllShop_Pojo;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.FileCache;
import com.shopsy.Utils.GridViewWithHeaderAndFooter;
import com.shopsy.Utils.ImageLoader;
import com.shopsy.Utils.RoundedImageView;
import com.shopsy.Utils.SessionManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class SeeAllFavoriteShop extends ActionBarActivity
{
	private ArrayList<AllShop_Pojo> itemList;
	private ArrayList<String> Items = new ArrayList<String>();
	private ArrayList<String> sellerid = new ArrayList<String>();
	
	private RelativeLayout cart;
	private RelativeLayout cartcountlayout;
	private TextView cartcount;
	private TextView header_title;
	private String itemcartcount;
	private AllShopAdapter adapter;
	
	private RelativeLayout loading,nointernet,mainlayout;
	private GridViewWithHeaderAndFooter gridview;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	
	private Context context;
	private ActionBar actionBar;
	
	private SessionManager session;
	private CommonIDSessionManager commonsession;
	private String userid, commonid,userName="";
	
	private int checkpagepos = 0;
	private boolean loadingMore = false;
	private boolean show_progress_status=true;
	private ProgressBar loadmore_progressbar;
	private View footerview;
	private boolean cart_onresume=false;
	
	JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;

	private String asyntask_name="normal";
	private ImageView allshop_loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userprofile_seefavourite_shop);
		
		context = getApplicationContext();
		ActionBarActivity actionBarActivity = (ActionBarActivity)SeeAllFavoriteShop.this;
  		actionBar = actionBarActivity.getSupportActionBar();
		Items.clear();

		//---clear cache of image----
		 clearImageCache();
		 
		initialize();
		
		cart.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent(SeeAllFavoriteShop.this,CartPage.class);
				startActivity(intent);
				overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});
		
		
		gridview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				Intent intent = new Intent(SeeAllFavoriteShop.this,AllShop_ProductPage.class);
				intent.putExtra("shopurl", itemList.get(position).getShopurl());
				startActivity(intent);
				overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});
		
		gridview.setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				int threshold = 1;
				int count = gridview.getCount();
				
				if (scrollState == SCROLL_STATE_IDLE)
				{
					if (gridview.getLastVisiblePosition() >= count - threshold && !(loadingMore))
					{
						
						 if (swipeRefreshLayout.isRefreshing()) 
						 {
							 //nothing happen(code to block loadmore functionality when swipe to refresh is loading)
						 }
						 else
						 {

								if(show_progress_status)
								{
								    ConnectionDetector cd = new ConnectionDetector(SeeAllFavoriteShop.this);
									boolean isInternetPresent = cd.isConnectingToInternet();
								
										if (isInternetPresent) 
										{
											if(session.isLoggedIn())
											{
												Loadmore_JsonRequest(Iconstant.userProfile_see_favorite_shop_url +userName+"?type=shop&pageId="+checkpagepos);
											}
											else
											{
												Loadmore_JsonRequest(Iconstant.userProfile_see_favorite_shop_url +userName+"?type=shop&pageId="+checkpagepos);
											}
											
										} 
										else 
										{
											Toast.makeText(SeeAllFavoriteShop.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
					   if(gridview != null && gridview.getChildCount() > 0)
				        {
				            // check if the first item of the list is visible
				            boolean firstItemVisible = gridview.getFirstVisiblePosition() == 0;
				            // check if the top of the first item is visible
				            boolean topOfFirstItemVisible = gridview.getChildAt(0).getTop() == 0;
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
				    ConnectionDetector cd = new ConnectionDetector(SeeAllFavoriteShop.this);
					boolean isInternetPresent = cd.isConnectingToInternet();
					
							if (isInternetPresent) 
							{
								
								if(session.isLoggedIn())
								{
									asyntask_name="swipe";
									nointernet.setVisibility(View.GONE);
									actionBar.setDisplayShowCustomEnabled(true);
									
									JsonRequest(Iconstant.userProfile_see_favorite_shop_url +userName+"?type=shop&pageId="+1);
								}
								else
								{
									asyntask_name="swipe";
									nointernet.setVisibility(View.GONE);
									// making hide custom menu and action bar title
									actionBar.setDisplayShowCustomEnabled(true);
									
									JsonRequest(Iconstant.userProfile_see_favorite_shop_url +userName+"?type=shop&pageId="+1);
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
    		}
		});
		
		
	}
	
	@SuppressWarnings("deprecation")
	private void initialize() 
	{
		itemList = new ArrayList<AllShop_Pojo>();
		cd = new ConnectionDetector(SeeAllFavoriteShop.this);
		isInternetPresent = cd.isConnectingToInternet();
		
		session = new SessionManager(SeeAllFavoriteShop.this);
		commonsession = new CommonIDSessionManager(SeeAllFavoriteShop.this);

		gridview=(GridViewWithHeaderAndFooter)findViewById(R.id.allshop_favouriteGridView);
		loading=(RelativeLayout)findViewById(R.id.allshop_favouriteloading_layout);
		nointernet=(RelativeLayout)findViewById(R.id.allshop_favouritenointernet_layout);
		mainlayout=(RelativeLayout)findViewById(R.id.allshop_favouritemain_layout);
		allshop_loading=(ImageView)findViewById(R.id.allshop_favouriteloading_gif);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.allshop_favouriteswipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(false);
		
		
		//code for gridView header
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerview= inflater.inflate(R.layout.gridview_footer_loading, null);
	
		loadmore_progressbar=(ProgressBar)footerview.findViewById(R.id.grid_footer_progressbar);
		
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);
		userName= user.get(SessionManager.KEY_USERNAME);

		// get user data from session
		HashMap<String, String> user1 = commonsession.getUserDetails();
		commonid = user1.get(CommonIDSessionManager.KEY_COMMONID);
		
		
		// code to disable actionbar title
				actionBar.setDisplayShowTitleEnabled(false);
				actionBar.setDisplayHomeAsUpEnabled(true);
				actionBar.setHomeButtonEnabled(true);

		// ------------------code to add custom menu in action bar--------------------------------------
		
		actionBar.setDisplayShowCustomEnabled(true);
		View view = getLayoutInflater().inflate(R.layout.home_custom_header, null);
		actionBar.setCustomView(view);

		cart = (RelativeLayout) view.findViewById(R.id.cartrelativelayout);
		cartcount = (TextView) view.findViewById(R.id.homecartcounttext);
		cartcountlayout = (RelativeLayout) view.findViewById(R.id.homecartcountlayout);
		header_title=(TextView)view.findViewById(R.id.home_header_title);

		cart.setVisibility(View.GONE);
		header_title.setText("All Shops");
		
		
		Glide.with(context)
	    .load(R.drawable.loadinganimation)
	    .asGif()
	    .crossFade()
	    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
	    .into(allshop_loading);
		
		    // to pass common id when user has not logged in

					if (isInternetPresent) 
					{
						if(session.isLoggedIn())
						{
							nointernet.setVisibility(View.GONE);
							JsonRequest(Iconstant.userProfile_see_favorite_shop_url +userName+"?type=shop&pageId="+1);
						}
						else
						{
							nointernet.setVisibility(View.GONE);
							mainlayout.setVisibility(View.GONE);
							JsonRequest(Iconstant.userProfile_see_favorite_shop_url +userName+"?type=shop&pageId="+1);
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
				
				
		        //code to align grid with equal spacing
					/*gridview.setNumColumns(GridView.AUTO_FIT); 
					//gridview.setHorizontalSpacing(10); 
					  gridview.setPadding(10, 0, 10, 0); 
					  gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);*/
		              gridview.setHorizontalSpacing(5); 
				      gridview.setVerticalSpacing(5);
				      gridview.setPadding(5, 0, 5, 0); 
				      gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH); 
		
				      
			     gridview.addFooterView(footerview);
	}
	
	//-------CLEAR IMAGE CACHE FROM PHONE SD CARD-----
	  private void clearImageCache()
	  {
			FileCache aa=new FileCache(SeeAllFavoriteShop.this);
			aa.clear();
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
	  
	
	//--------load animation------
		private void startload()
		{
			//---clear cache of image----
			 clearImageCache();
			 
			if(asyntask_name.equalsIgnoreCase("normal"))
			{
				mainlayout.setVisibility(View.GONE);
				nointernet.setVisibility(View.GONE);
				loading.setVisibility(View.VISIBLE);
				
			}
			else
			{
				//---clear cache of image----
				clearImageCache();
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
				loading.setVisibility(View.GONE);
				swipeRefreshLayout.setRefreshing(false);
			}
		}	
		
		
		
		// -------------------------code for JSon Request----------------------------------
		
				private void JsonRequest(String Url) 
				{
					startload();
					
					System.out.println("--------------All Shop url-------------------"+Url);
					
					 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) 
								{
									System.out.println("--------------All Shop response -----------"+response);

									try {

										itemcartcount = response.getString("cartCount");
										checkpagepos = Integer.parseInt(response.getString("pagePos"));
										
										session.SetCartCount(itemcartcount);
										
										JSONArray jarray = response.getJSONArray("favoriteShops");
										if(jarray.length()>0)
										{
											itemList.clear();
											sellerid.clear();
											
											for (int i = 0; i < jarray.length(); i++) 
											{
												JSONObject object = jarray.getJSONObject(i);
												AllShop_Pojo items = new AllShop_Pojo();

												items.setShopname(object.getString("shopName"));
												items.setShopurl(object.getString("ShopUrl"));
												items.setSellerid(object.getString("sellerId"));
												items.setSellerimage(object.getString("sellerImage"));
												items.setSellername(object.getString("sellerName"));
												items.setProductsCount(object.getString("productCount"));
												items.setFavstatus("1");
												
												itemList.add(items);
												sellerid.add(object.getString("sellerId"));
											}
											
											show_progress_status=true;
										}
										else
										{
											itemList.clear();
											sellerid.clear();
											show_progress_status=false;
										}
											
									} catch (JSONException e) {
										e.printStackTrace();
									}
									
									
									
									//------on post execute-----
									
									stopload();
									
									if (show_progress_status) 
									{
										
										if (itemcartcount.length() > 0 && !itemcartcount.equals("0")) 
										{
											cartcountlayout.setVisibility(View.VISIBLE);
											cartcount.setText(itemcartcount);
										} 
										else 
										{
											cartcountlayout.setVisibility(View.INVISIBLE);
										}
										
										adapter = new AllShopAdapter(itemList);
										gridview.setAdapter(adapter);
									}
									else 
									{
										// code to stop loading at bottom of screen, when data is not present
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

					 jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000, 
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
					
					System.out.println("--------------All Shop Loadmore url-------------------"+Url);
					
					loadmore_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) 
								{
									System.out.println("--------------All Shop Loadmore response -----------"+response);

									try {

										itemcartcount = response.getString("cartCount");
										checkpagepos = Integer.parseInt(response.getString("pagePos"));
										
										session.SetCartCount(itemcartcount);
										
										JSONArray jarray = response.getJSONArray("favoriteShops");
										if(jarray.length()>0)
										{
											for (int i = 0; i < jarray.length(); i++) 
											{
												JSONObject object = jarray.getJSONObject(i);
												AllShop_Pojo items = new AllShop_Pojo();

												items.setShopname(object.getString("shopName"));
												items.setShopurl(object.getString("ShopUrl"));
												items.setSellerid(object.getString("sellerId"));
												items.setSellerimage(object.getString("sellerImage"));
												items.setSellername(object.getString("sellerName"));
												items.setProductsCount(object.getString("productCount"));
												items.setFavstatus("1");
												
												itemList.add(items);
												sellerid.add(object.getString("sellerId"));
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
										if (itemcartcount.length() > 0 && !itemcartcount.equals("0")) 
										{
											cartcountlayout.setVisibility(View.VISIBLE);
											cartcount.setText(itemcartcount);
										} 
										else 
										{
											cartcountlayout.setVisibility(View.INVISIBLE);
										}
									}
									
									adapter.notifyDataSetChanged();
									
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

					loadmore_jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000, 
				                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
				                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
					// Adding request to request queue
					AppController.getInstance().addToRequestQueue(loadmore_jsonObjReq);
				}		
				
				
		// ----------------------------------code for Adapter-----------------------------------------------
				
				public class AllShopAdapter extends BaseAdapter 
				{
								
						private ArrayList<AllShop_Pojo> data;
						public ImageLoader imageLoader;
						private LayoutInflater mInflater;
						
						View view;
						ViewHolder holder;
								
						public AllShopAdapter(ArrayList<AllShop_Pojo> d) 
						{
							mInflater = LayoutInflater.from(context);
							data = d;
							imageLoader = new ImageLoader(SeeAllFavoriteShop.this);
						}
						@Override
						public int getCount() 
						{
							return data.size();
						}

						@Override
						public Object getItem(int position) {
							return position;
						}

						@Override
						public long getItemId(int position) {
							return position;
						}
						
						@Override
						public int getViewTypeCount() 
						{
							return 1;
						}
						
						public class ViewHolder 
						{
							private ImageView favoriteheart;
							private RoundedImageView shopowner_image;
							private TextView sellername;
							private TextView product_itemcount;
						}
						
						@Override
						public View getView(int position, View convertView, ViewGroup parent) 
						{
							
							if(convertView == null) 
							{
								view = mInflater.inflate(R.layout.allshoppage_single, parent, false);
								holder = new ViewHolder();
								holder.sellername = (TextView) view.findViewById(R.id.allshop_single_username);
								holder.product_itemcount = (TextView) view.findViewById(R.id.allshop_single_itemcount);
								holder.shopowner_image = (RoundedImageView) view.findViewById(R.id.allshop_single_userimage);
								holder.favoriteheart = (ImageView) view.findViewById(R.id.allshop_single_favourite);
								
								view.setTag(holder);
							}
							else
							{
								view = convertView;
								holder = (ViewHolder)view.getTag();
							}
							
							
							holder.sellername.setText(data.get(position).getSellername().replace("&#8230;", "..."));
							holder.product_itemcount.setText(data.get(position).getProductsCount()+" "+"items");
							
							Picasso.with(context).load(String.valueOf(data.get(position).getSellerimage())).placeholder(R.drawable.nouserimg).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.shopowner_image);
							//imageLoader.DisplayImage(String.valueOf(data.get(position).getSellerimage()), holder.shopowner_image);
							
							
							// code to heart color change when Url loads
							if (data.get(position).getFavstatus().contains("1")) 
							{
								holder.favoriteheart.setImageResource(R.drawable.favredtwo);
							}
							else
							{
								holder.favoriteheart.setImageResource(R.drawable.favtwo);
							}
								
							
							return view;
						}
							
						
				}	
					
						
				
				
			
		
		// -----------------------------code for on Resume------------------------------------------
		
		  @Override public void onResume() 
		  {
			  if(cart_onresume)
			  {
				  // get cart count from session
					HashMap<String, String> count = session.getCartCount();
				    String Scartcount = count.get(SessionManager.KEY_CARTCOUNT);
				  
				  if (Scartcount.length() > 0 && !Scartcount.equals("0")) 
					{
						cartcountlayout.setVisibility(View.VISIBLE);
						cartcount.setText(Scartcount);
					} 
					else 
					{
						cartcountlayout.setVisibility(View.INVISIBLE);
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
			 cart_onresume=false;
			
			//---clear cache of image----
			 clearImageCache();
			 
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
