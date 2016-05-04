package com.shopsy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
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
import com.shopsy.Pojo.TrendingPojo;
import com.shopsy.StaggeredGridView.DynamicHeightImageView;
import com.shopsy.StaggeredGridView.StaggeredGridView;
import com.shopsy.Subclass.ActionBarActivity_Subclass_AllRecentProduct;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.FileCache;
import com.shopsy.Utils.ImageLoader;
import com.shopsy.Utils.ImageLoader_BandWidth;
import com.shopsy.Utils.SessionManager;
import com.shopsy.Utils.SessionManager_Settings;

public class AllRecentProducts extends ActionBarActivity_Subclass_AllRecentProduct 
{
	
	private ArrayList<TrendingPojo> itemList;
	private ArrayList<String> productid = new ArrayList<String>();
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	
	private Context context;
	private ActionBar actionBar;
	
	private SessionManager session;
	SessionManager_Settings session_settings;
	private CommonIDSessionManager commonsession;
	private String userid, commonid;
	
	private RelativeLayout loading,nointernet,mainlayout;
	private StaggeredGridView gridview;
	private SwipeRefreshLayout swipeRefreshLayout = null;

	private RelativeLayout cart;
	private TextView cartcount;
	private TextView header_title;
	private RelativeLayout cartcountlayout;
	private String itemcartcount;
	private int checkpagepos = 0;
	private TrendingAdapter adapter;
	private boolean loadingMore = false;
	private boolean show_progress_status=true;

	private ImageView trending_loading;
	private String asyntask_name="normal";
	
	private ProgressBar loadmore_progressbar;
	private View headerView,footerview;
	private boolean cart_onresume=false;
	
	JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trendingpage);
		
		context = getApplicationContext();
  		ActionBarActivity actionBarActivity = (ActionBarActivity)AllRecentProducts.this;
  		actionBar = actionBarActivity.getSupportActionBar();
  		
  		//---clear cache of image----
  		clearImageCache();
  		initialize();
  		
  		cart.setOnClickListener(new OnClickListener()
  		{
  			@Override
  			public void onClick(View v) 
  			{
  				Intent intent=new Intent(AllRecentProducts.this,CartPage.class);
  				startActivity(intent);
  				AllRecentProducts.this.overridePendingTransition(R.anim.enter, R.anim.exit);
  			}
  		});
  		
  		
  		gridview.setOnItemClickListener(new OnItemClickListener()
  		{
  			@Override
  			public void onItemClick(AdapterView<?> parent, View view,
  					int position, long id) {
  				
  				int new_position=position-1;
  				
  				Intent intent=new Intent(AllRecentProducts.this,DetailPage.class);
  				intent.putExtra("productid", itemList.get(new_position).getProductId());
  				startActivity(intent);
  				AllRecentProducts.this.overridePendingTransition(R.anim.enter, R.anim.exit);
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

  								    ConnectionDetector cd = new ConnectionDetector(AllRecentProducts.this);
  									boolean isInternetPresent = cd.isConnectingToInternet();
  								
  										if (isInternetPresent) 
  										{
  											
  											if(session.isLoggedIn())
  											{
  												Loadmore_JsonRequest(Iconstant.trending_url+(checkpagepos)+"&commonId=" + userid);
  											}
  											else
  											{
  												Loadmore_JsonRequest(Iconstant.trending_url+(checkpagepos)+"&commonId=" + commonid);
  											}
  										} 
  										else 
  										{
  											Toast.makeText(AllRecentProducts.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
  				    ConnectionDetector cd = new ConnectionDetector(AllRecentProducts.this);
  					boolean isInternetPresent = cd.isConnectingToInternet();
  					
  							if (isInternetPresent) 
  							{
  								
  								if(session.isLoggedIn())
  								{
  									asyntask_name="swipe";
  									nointernet.setVisibility(View.GONE);
  									JsonRequest(Iconstant.trending_url + 1+ "&commonId=" + userid);
  									
  									actionBar.setDisplayShowCustomEnabled(true);
  								}
  								else
  								{
  									asyntask_name="swipe";
  									nointernet.setVisibility(View.GONE);
  									JsonRequest(Iconstant.trending_url + 1+ "&commonId=" + commonid);
  									
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
      		}
  		});
	}


	@SuppressWarnings("deprecation")
	private void initialize() 
	{
		itemList = new ArrayList<TrendingPojo>();
		cd = new ConnectionDetector(AllRecentProducts.this);
		isInternetPresent = cd.isConnectingToInternet();
		
		
		session = new SessionManager(AllRecentProducts.this);
		session_settings= new SessionManager_Settings(AllRecentProducts.this);
		commonsession = new CommonIDSessionManager(AllRecentProducts.this);

		gridview=(StaggeredGridView)findViewById(R.id.trending_GridView);
		loading=(RelativeLayout)findViewById(R.id.trending_loading_layout);
		nointernet=(RelativeLayout)findViewById(R.id.trending_nointernet_layout);
		mainlayout=(RelativeLayout)findViewById(R.id.trending_main_layout);
		
		trending_loading=(ImageView)findViewById(R.id.trending_loading_gif);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.trending_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(true);
		
		//code for gridView header
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				headerView = inflater.inflate(R.layout.grid_blankspace_header, null);
				footerview= inflater.inflate(R.layout.gridview_footer_loading, null);
				
				loadmore_progressbar=(ProgressBar)footerview.findViewById(R.id.grid_footer_progressbar);
				
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		// get user data from session
		HashMap<String, String> user1 = commonsession.getUserDetails();
		commonid = user1.get(CommonIDSessionManager.KEY_COMMONID);
		
		
		// code to disable actionbar title
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		// ------------------code to add custom menu in action bar--------------------------------------
		
		actionBar.setDisplayShowCustomEnabled(true);
		View view = AllRecentProducts.this.getLayoutInflater().inflate(R.layout.home_custom_header, null);
		actionBar.setCustomView(view);

		header_title=(TextView)view.findViewById(R.id.home_header_title);
		cart = (RelativeLayout) view.findViewById(R.id.cartrelativelayout);
		cartcount = (TextView) view.findViewById(R.id.homecartcounttext);
		cartcountlayout = (RelativeLayout) view.findViewById(R.id.homecartcountlayout);

		header_title.setText("Trending");
		
		
		Glide.with(context)
	    .load(R.drawable.loadinganimation)
	    .asGif()
	    .crossFade()
	    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
	    .into(trending_loading);
		
		// to pass common id when user has not logged in
			if (isInternetPresent) 
			{
				if(session.isLoggedIn())
				{
					nointernet.setVisibility(View.GONE);
					JsonRequest(Iconstant.trending_url + 1+ "&commonId=" + userid);
				}
				else
				{
					nointernet.setVisibility(View.GONE);
					JsonRequest(Iconstant.trending_url + 1+ "&commonId=" + commonid);
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
		
		
		/*//code to align grid with equal spacing
				gridview.setNumColumns(GridView.AUTO_FIT); 
				gridview.setHorizontalSpacing(8); 
				gridview.setVerticalSpacing(8); 
				gridview.setPadding(8, 0, 8, 0); 
				gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH); */
		
				
				gridview.addHeaderView(headerView);
				gridview.addFooterView(footerview);
	}
	
	//-------CLEAR IMAGE CACHE FROM PHONE SD CARD-----
	  private void clearImageCache()
	  {
			FileCache aa=new FileCache(AllRecentProducts.this);
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
		
		if(asyntask_name.equalsIgnoreCase("normal"))
		{
			mainlayout.setVisibility(View.GONE);
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
		
		System.out.println("--------------Trending url-------------------"+Url);
		
		 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) 
					{

						System.out.println("--------------trending response -----------"+response);
						try {

								JSONArray jarray = response.getJSONArray("productDetails");

								itemcartcount = response.getString("cartCount");
								checkpagepos = Integer.parseInt(response.getString("pagePos"));
								session.SetCartCount(itemcartcount);
								
								if(jarray.length()>0)
								{
									itemList.clear();
									productid.clear();
									
									for (int j = 0; j < jarray.length(); j++) 
									{
										final JSONObject object = jarray.getJSONObject(j);
										final TrendingPojo items = new TrendingPojo();

										items.setProductName(object.getString("productName"));
										items.setPrice(response.getString("currencySymbol")+ object.getString("Price"));
										items.setProductId(object.getString("productId"));
										items.setProductImage(object.getString("Image"));
										items.setStorename(object.getString("storeName"));
										items.setFavstatus(object.getString("favStatus"));
										
										Log.e("image url---------------------------", object.getString("Image"));
										
										itemList.add(items);
										productid.add(object.getString("productId"));
									}
									
									show_progress_status=true;
								}
								else
								{
										itemList.clear();
										productid.clear();

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

							
							adapter = new TrendingAdapter(itemList);
							gridview.setAdapter(adapter);
							
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
		
		System.out.println("--------------Trending loadmore url-------------------"+Url);
		
		 loadmore_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) 
					{
						try {

								JSONArray jarray = response.getJSONArray("productDetails");

								itemcartcount = response.getString("cartCount");
								checkpagepos = Integer.parseInt(response.getString("pagePos"));
								session.SetCartCount(itemcartcount);
								
								if(jarray.length()>0)
								{
									
									for (int j = 0; j < jarray.length(); j++) 
									{
										final JSONObject object = jarray.getJSONObject(j);
										final TrendingPojo items = new TrendingPojo();

										items.setProductName(object.getString("productName"));
										items.setPrice(response.getString("currencySymbol")+ object.getString("Price"));
										items.setProductId(object.getString("productId"));
										items.setProductImage(object.getString("Image"));
										items.setStorename(object.getString("storeName"));
										items.setFavstatus(object.getString("favStatus"));
										
										itemList.add(items);
										productid.add(object.getString("productId"));
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
	

	
	
	
	
	// -------------------------------------code for adapter-----------------------------------------

			class TrendingAdapter extends BaseAdapter 
			{

				private ArrayList<TrendingPojo> data;
				ImageLoader imageLoader;
				ImageLoader_BandWidth imageLoader_bandwidth;
				private LayoutInflater mInflater;

				public TrendingAdapter(ArrayList<TrendingPojo> d)
				{
					mInflater = LayoutInflater.from(context);
					this.data = d;
					imageLoader = new ImageLoader(AllRecentProducts.this);
					imageLoader_bandwidth= new ImageLoader_BandWidth(AllRecentProducts.this);
				}

				@Override
				public int getCount() {
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
					private DynamicHeightImageView image;
					private TextView tvName;
					private TextView tvprice;
					private TextView tvstorename;
				}
				
				@Override
				public View getView(int position, View convertView, ViewGroup parent) 
				{
					View view;
					ViewHolder holder;
					if(convertView == null) 
					{
						view = mInflater.inflate(R.layout.trending_gridsingle, parent, false);
						holder = new ViewHolder();
						holder.tvName = (TextView) view.findViewById(R.id.trending_grid_name);
						holder.tvprice = (TextView) view.findViewById(R.id.trending_grid_price);
						holder.tvstorename = (TextView) view.findViewById(R.id.trending_grid_storename);
						holder.image = (DynamicHeightImageView) view.findViewById(R.id.trending_grid_image);
						holder.favoriteheart = (ImageView) view.findViewById(R.id.trending_favheart);
						
						view.setTag(holder);
					}
					else
					{
						view = convertView;
						holder = (ViewHolder)view.getTag();
					}
					
					
					holder.tvName.setText(data.get(position).getProductName().replace("&#8230;", "..."));
					holder.tvprice.setText(data.get(position).getPrice());
					holder.tvstorename.setText(data.get(position).getStorename());
					ImageView image = holder.image;
					imageLoader.DisplayImage(String.valueOf(data.get(position).getProductImage()), image);

					/*if(Bandwidth.equalsIgnoreCase("low"))
					{
						imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getProductImage()), image, 2);
					}
					else
					{
						imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getProductImage()), image, 4);
					}*/
					
					// code to heart color change when Url loads
					if (data.get(position).getFavstatus().contains("1")) 
					{
						holder.favoriteheart.setImageResource(R.drawable.favredtwo);
					}
					else
					{
						holder.favoriteheart.setImageResource(R.drawable.favtwo);
					}
						
					holder.favoriteheart.setOnClickListener(new OnItemClickListenerheart(position));
					
					return view;
				}

				private class OnItemClickListenerheart implements OnClickListener 
				{
					private final int mPosition;

					OnItemClickListenerheart(int position) {
						mPosition = position;
					}

					@Override
					public void onClick(View arg0) 
					{
						
						if (data.get(mPosition).getFavstatus().equalsIgnoreCase("1")) 
						{
							onItemClickheartremove(mPosition);
						} 
						else
						{
							onItemClickheartadd(mPosition);
						}
						
						notifyDataSetChanged();
					}
	            }
			
			
			// onClick for add heart
			public void onItemClickheartadd(int position) 
			{
				if (session.isLoggedIn()) 
				{
					// code to set the status to "true" so that the heart become "red"
					data.get(position).setFavstatus("1");
					
					FavoriteAsyncTask favasyntask=new FavoriteAsyncTask();
					favasyntask.execute(Iconstant.heartaddurl + "product&mode=add&id="+productid.get(position)+"&userId="+userid);
				}
				else 
				{
					// showing popup for login
					favourit_popup("allrecentproducts");
				}
			}
			
			
			// onClick for Remove heart
			public void onItemClickheartremove(int position)
		    {
				if (session.isLoggedIn()) 
				{
					// code to set the status to "false" so that the heart become "white"
					data.get(position).setFavstatus("0");

					FavoriteAsyncTask favasyntask=new FavoriteAsyncTask();
					favasyntask.execute(Iconstant.heartremoveurl+"product&mode=remove&id="+productid.get(position)+"&userId="+userid);
				} 
				else 
				{
					favourit_popup("allrecentproducts");
				}
			}		
	
	}
			
			
			
			//------------------------------Favorite AsynTask---------------------------------
			
			class FavoriteAsyncTask extends AsyncTask<String, Void, Boolean> 
			{
				@Override
				protected void onPreExecute() 
				{
					super.onPreExecute();
				}
				@Override
				protected Boolean doInBackground(String... urls) 
				{
				
					HttpClient client = new DefaultHttpClient();
					client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "shopsymobileapp");
					HttpPost httppost = new HttpPost(urls[0].replace("\n", ""));
					try {
						ResponseHandler<String> responsestring = new BasicResponseHandler();
						client.execute(httppost, responsestring);

					} catch (IOException ex2) {
					}
					
					return false;
				}

				@Override
				protected void onPostExecute(Boolean result) 
				{
				}
			}
			
			
			
			//-----------------Move Back on pressed phone back button------------------		
			
			@Override
			public boolean onKeyDown(int keyCode, KeyEvent event) 
			{
				if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
					AllRecentProducts.this.finish();
					AllRecentProducts.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
					return true;
				}
				return false;
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
