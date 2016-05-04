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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
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
import com.shopsy.Pojo.FeedPojo;
import com.shopsy.Subclass.Fragment_subclass;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.CircularImageView;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.FileCache;
import com.shopsy.Utils.GridViewWithHeaderAndFooter;
import com.shopsy.Utils.ImageLoader;
import com.shopsy.Utils.ImageLoader_BandWidth;
import com.shopsy.Utils.SessionManager;
import com.shopsy.Utils.SessionManager_Settings;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * @author Prem Kumar
 * 
 */
public class ActivityFeed extends Fragment_subclass 
{
	private ArrayList<FeedPojo> itemList;
	private ArrayList<String> Items = new ArrayList<String>();
	private ArrayList<String> productid = new ArrayList<String>();
	
	private String itemcartcount;
	private FeedAdapter adapter;
	
	private RelativeLayout loading,nointernet,mainlayout,signin_layout,signin_submit,seetrending_layout,seetrending_submit;
	private GridViewWithHeaderAndFooter gridview;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private boolean show_progress_status=true;

	private ProgressBar loadmore_progressbar;
	private View headerView,footerview;
	private boolean loadingMore = false;
	private int checkpagepos = 0;
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	
	private Context context;
	private ActionBar actionBar;
	
	private SessionManager session;
	SessionManager_Settings session_settings;
	private String userid;
	private ImageView feed_loading;
	private String homePage_identify;
	
	JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;
	 
	private String asyntask_name="normal";
	private boolean cart_onresume=false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View rootview = inflater.inflate(R.layout.activityfeed, container,false);

		context = getActivity();
		ActionBarActivity actionBarActivity = (ActionBarActivity) getActivity();
		actionBar = actionBarActivity.getSupportActionBar();
		Items.clear();
		initialize(rootview);
		
		signin_submit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				favourit_popup("feed");
			}
		});
		
		
		gridview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{
				int new_position=position-1;
				
				Intent intent=new Intent(getActivity(),DetailPage.class);
				intent.putExtra("productid", itemList.get(new_position).getProductId());
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});
		
		
		seetrending_submit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Fragment fragment = new TrendingPage();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.replace(R.id.content_frame, fragment);
				ft.commit();
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

								    ConnectionDetector cd = new ConnectionDetector(getActivity());
									boolean isInternetPresent = cd.isConnectingToInternet();
								
										if (isInternetPresent) 
										{
											Loadmore_JsonRequest(Iconstant.yourfeedpageurl+ userid + "?commonId=" + userid+"&pageId="+checkpagepos);
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
				  ConnectionDetector cd = new ConnectionDetector(getActivity());
					boolean isInternetPresent = cd.isConnectingToInternet();
					
							if (isInternetPresent) 
							{
								if(session.isLoggedIn())
								{
									asyntask_name="swipe";
									nointernet.setVisibility(View.GONE);

									JsonRequest(Iconstant.yourfeedpageurl + userid + "?commonId=" + userid+"&pageId=1");
									
									actionBar.setDisplayShowCustomEnabled(true);
								}
								else
								{
									asyntask_name="swipe";
									
									signin_layout.setVisibility(View.VISIBLE);
									nointernet.setVisibility(View.GONE);
									mainlayout.setVisibility(View.GONE);
									seetrending_layout.setVisibility(View.GONE);
									
									// making hide custom menu and action bar title
									actionBar.setDisplayShowCustomEnabled(true);
								}
								
							} 
							else
							{
								swipeRefreshLayout.setRefreshing(false);
								mainlayout.setVisibility(View.GONE);
								nointernet.setVisibility(View.VISIBLE);
								signin_layout.setVisibility(View.GONE);
								seetrending_layout.setVisibility(View.GONE);

								// making hide custom menu and action bar title
								actionBar.setDisplayShowCustomEnabled(false);
								actionBar.setDisplayShowTitleEnabled(false);
							}
						
			  }
    		}
		});
		
		return rootview;
	}

	@SuppressWarnings("deprecation")
	private void initialize(View rootview) 
	{
		itemList = new ArrayList<FeedPojo>();
		cd = new ConnectionDetector(getActivity());
		isInternetPresent = cd.isConnectingToInternet();
		
		session = new SessionManager(getActivity());
		session_settings= new SessionManager_Settings(getActivity());

		gridview=(GridViewWithHeaderAndFooter)rootview.findViewById(R.id.feed_GridView);
		loading=(RelativeLayout)rootview.findViewById(R.id.feed_loading_layout);
		nointernet=(RelativeLayout)rootview.findViewById(R.id.feed_nointernet_layout);
		mainlayout=(RelativeLayout)rootview.findViewById(R.id.feed_main_layout);
		signin_layout=(RelativeLayout)rootview.findViewById(R.id.feed_signin_layout);
		signin_submit=(RelativeLayout)rootview.findViewById(R.id.feed_signin_submitlayout);
		seetrending_layout=(RelativeLayout)rootview.findViewById(R.id.feed_seetrending_layout);
		seetrending_submit=(RelativeLayout)rootview.findViewById(R.id.feed_seetrendinglayout_submit);
		feed_loading=(ImageView)rootview.findViewById(R.id.feed_loading_gif);
		
		swipeRefreshLayout = (SwipeRefreshLayout)rootview.findViewById(R.id.feed_swipe_refresh_layout);
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

		HashMap<String, String> home = session.getHomepage();
		homePage_identify = home.get(SessionManager.KEY_HOMEPAGE_IDENTIFY);
		
		// code to disable actionBar title
		actionBar.setDisplayShowTitleEnabled(false);

		 
		 Glide.with(context)
		    .load(R.drawable.loadinganimation)
		    .asGif()
		    .crossFade()
		    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
		    .into(feed_loading);
		 
		 
		   // to pass common id when user has not logged in
			
					if (isInternetPresent) 
					{
						if(session.isLoggedIn())
						{
							nointernet.setVisibility(View.GONE);
							JsonRequest(Iconstant.yourfeedpageurl + userid + "?commonId=" + userid+"&pageId=1");
						}
						else
						{
							
							 if(homePage_identify.equalsIgnoreCase("Activity"))
								{
							    	HomePage_Activity.cartcountRefresh();
								}
							    else
							    {
							    	HomePage.cartcountRefresh();
							    }
							
							signin_layout.setVisibility(View.VISIBLE);
							nointernet.setVisibility(View.GONE);
							mainlayout.setVisibility(View.GONE);
							seetrending_layout.setVisibility(View.GONE);
						}
					} 
					else
					{
						mainlayout.setVisibility(View.GONE);
						nointernet.setVisibility(View.VISIBLE);
						signin_layout.setVisibility(View.GONE);
						seetrending_layout.setVisibility(View.GONE);

						// making hide custom menu and action bar title
						actionBar.setDisplayShowCustomEnabled(false);
						actionBar.setDisplayShowTitleEnabled(false);
					}
				
				
		        //code to align grid with equal spacing
					/*gridview.setNumColumns(GridView.AUTO_FIT); 
					//gridview.setHorizontalSpacing(10); 
					gridview.setPadding(10, 0, 10, 0); 
				 	gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);*/
		            gridview.setPadding(0, 0, 0, 0);
		            gridview.setHorizontalSpacing(10); 
		            
		            
		            gridview.addHeaderView(headerView);
		    		gridview.addFooterView(footerview);
		
	}
	
	//-------CLEAR IMAGE CACHE FROM PHONE SD CARD-----
	  private void clearImageCache()
	  {
			FileCache aa=new FileCache(getActivity());
			aa.clear();
	  }
	
	
    //--------load animation------
		private void startload()
		{
			
			if(asyntask_name.equalsIgnoreCase("normal"))
			{
				mainlayout.setVisibility(View.GONE);
				nointernet.setVisibility(View.GONE);
				signin_layout.setVisibility(View.GONE);
				seetrending_layout.setVisibility(View.GONE);
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
				signin_layout.setVisibility(View.GONE);
				seetrending_layout.setVisibility(View.GONE);
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
			
			System.out.println("--------------Feed url-------------------"+Url);
			
			 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) 
						{
							System.out.println("--------------Feed response -----------"+response);

							try {

								JSONArray jarray = response.getJSONArray("yourFeed");
								itemcartcount = response.getString("cartCount");
								checkpagepos = Integer.parseInt(response.getString("pagePos"));
								
								session.SetCartCount(itemcartcount);
								
								if (jarray.length() > 0) 
								{
									itemList.clear();
									productid.clear();
									
									for (int i = 0; i < jarray.length(); i++)
									{
										JSONObject object = jarray.getJSONObject(i);
										FeedPojo items = new FeedPojo();
										
										items.setFeedProductStatus(object.getString("productId"));
										items.setProductId(object.getString("productId"));
										items.setProductName(object.getString("productName"));
										items.setProductUrl(object.getString("productUrl"));
										items.setProductPrice(response.getString("currencySymbol") + object.getString("productPrice"));
										items.setProductImage(object.getString("productImage"));
										items.setUserImage(Iconstant.yourfeeduserimageurl + object.getString("userImage"));
										items.setFeeduser(object.getString("feeduser"));
										items.setFeeduserLink(object.getString("feeduserLink"));
										items.setFeedType(object.getString("feedType"));
										items.setFeedItem(object.getString("feedItem"));
										items.setFavStatus(object.getString("favStatus"));
										items.setStoreName(object.getString("storeName"));
										
										productid.add(object.getString("productId"));
										
										itemList.add(items);
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
							
							if(show_progress_status)
							{
								 if(homePage_identify.equalsIgnoreCase("Activity"))
									{
								    	HomePage_Activity.cartcountRefresh();
									}
								    else
								    {
								    	HomePage.cartcountRefresh();
								    }
								
								if (itemList.size() == 0)
								{
									seetrending_layout.setVisibility(View.VISIBLE);
									mainlayout.setVisibility(View.GONE);
									nointernet.setVisibility(View.GONE);
									signin_layout.setVisibility(View.GONE);
								}
								else
								{
									seetrending_layout.setVisibility(View.GONE);
									mainlayout.setVisibility(View.VISIBLE);
									nointernet.setVisibility(View.GONE);
									signin_layout.setVisibility(View.GONE);
								}
								
								
								adapter = new FeedAdapter(itemList);
								gridview.setAdapter(adapter);
							}
							else
							{
								seetrending_layout.setVisibility(View.VISIBLE);
								mainlayout.setVisibility(View.GONE);
								nointernet.setVisibility(View.GONE);
								signin_layout.setVisibility(View.GONE);
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
			
			jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000, 
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
			
			System.out.println("--------------Feed loadmore url-------------------"+Url);
			
			 loadmore_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) 
						{
							try {

								JSONArray jarray = response.getJSONArray("yourFeed");
								itemcartcount = response.getString("cartCount");
								checkpagepos = Integer.parseInt(response.getString("pagePos"));
								
								session.SetCartCount(itemcartcount);
								
								if (jarray.length() > 0) 
								{
									for (int i = 0; i < jarray.length(); i++)
									{
										JSONObject object = jarray.getJSONObject(i);
										FeedPojo items = new FeedPojo();
										
										items.setFeedProductStatus(object.getString("productId"));
										items.setProductId(object.getString("productId"));
										items.setProductName(object.getString("productName"));
										items.setProductUrl(object.getString("productUrl"));
										items.setProductPrice(response.getString("currencySymbol") + object.getString("productPrice"));
										items.setProductImage(object.getString("productImage"));
										items.setUserImage(Iconstant.yourfeeduserimageurl + object.getString("userImage"));
										items.setFeeduser(object.getString("feeduser"));
										items.setFeeduserLink(object.getString("feeduserLink"));
										items.setFeedType(object.getString("feedType"));
										items.setFeedItem(object.getString("feedItem"));
										items.setFavStatus(object.getString("favStatus"));
										items.setStoreName(object.getString("storeName"));
										
										productid.add(object.getString("productId"));
										
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
								 if(homePage_identify.equalsIgnoreCase("Activity"))
									{
								    	HomePage_Activity.cartcountRefresh();
									}
								    else
								    {
								    	HomePage.cartcountRefresh();
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

			 loadmore_jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000, 
		                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
		                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			 
			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(loadmore_jsonObjReq);
			
		}
		
	
	 // ----------------------------------code for Adapter-----------------------------------------------
					
		public class FeedAdapter extends BaseAdapter 
		{
						
				private ArrayList<FeedPojo> data;
				public ImageLoader imageLoader;
				public ImageLoader_BandWidth imageLoader_bandwidth;
				private LayoutInflater mInflater;
				
				View view;
				ViewHolder holder;
						
				public FeedAdapter(ArrayList<FeedPojo> d) 
				{
					mInflater = LayoutInflater.from(context);
					this.data = d;
					imageLoader = new ImageLoader(getActivity());
					imageLoader_bandwidth= new ImageLoader_BandWidth(getActivity());
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
					private CircularImageView userimage;
					private ImageView productimage;
					private TextView productname;
					private TextView tvprice;
					private TextView tvproductlink;
				}
				
				@Override
				public View getView(int position, View convertView, ViewGroup parent) 
				{
					
					if(convertView == null) 
					{
						view = mInflater.inflate(R.layout.activityfeed_single, parent, false);
						holder = new ViewHolder();
						holder.productname = (TextView) view.findViewById(R.id.feed_single_productname);
						holder.tvprice = (TextView) view.findViewById(R.id.feed_single_productprice);
						holder.tvproductlink = (TextView) view.findViewById(R.id.feed_single_productlink);
						holder.userimage = (CircularImageView) view.findViewById(R.id.feed_single_userimage);
						holder.productimage = (ImageView) view.findViewById(R.id.feed_single_productimage);
						holder.favoriteheart = (ImageView) view.findViewById(R.id.feed_single_favourite_heart);
						
						view.setTag(holder);
					}
					else
					{
						view = convertView;
						holder = (ViewHolder)view.getTag();
					}
					
					
					//HashMap<String, String> user = session_settings.getBandwidth();
					//String Bandwidth = user.get(SessionManager_Settings.KEY_BANDWIDTH);
					
					holder.productname.setText(data.get(position).getProductName().replace("&#8230;", "..."));
					holder.tvprice.setText(data.get(position).getProductPrice());
					holder.tvproductlink.setText(data.get(position).getStoreName());
	
					Picasso.with(context).load(String.valueOf(data.get(position).getUserImage())).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.nouserimg).into(holder.userimage);

					
					
					Picasso.with(context).load(String.valueOf(data.get(position).getProductImage())).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.no_image_grey).into(holder.productimage);
					
					
					
					
					/*if(Bandwidth.equalsIgnoreCase("low"))
					{
						imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getProductImage()), holder.productimage, 2);
					}
					else
					{
						Glide.with(context)
						.load(data.get(position).getProductImage())
						.diskCacheStrategy(DiskCacheStrategy.NONE)
						.placeholder(R.drawable.image_loading_background)
						//.error(R.drawable.failed)
						//.fallback(R.drawable.empty)
						.into(holder.productimage);
						
						imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getProductImage()), holder.productimage, 4);
					}*/
					
					// code to heart color change when Url loads
					if (data.get(position).getFavStatus().contains("1")) 
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

					OnItemClickListenerheart(int position) 
					{
						mPosition = position;
					}

					@Override
					public void onClick(View arg0) 
					{
						if (data.get(mPosition).getFavStatus().equalsIgnoreCase("1")) 
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
						data.get(position).setFavStatus("1");
						
						FavoriteAsyncTask favasyntask=new FavoriteAsyncTask();
						favasyntask.execute(Iconstant.heartaddurl + "product&mode=add&id="+productid.get(position)+"&userId="+userid);
					}
				}
				
				
				// onClick for Remove heart
				public void onItemClickheartremove(int position)
			    {
					if (session.isLoggedIn()) 
					{
						// code to set the status to "false" so that the heart become "white"
						data.get(position).setFavStatus("0");
						
						FavoriteAsyncTask favasyntask=new FavoriteAsyncTask();
						favasyntask.execute(Iconstant.heartremoveurl+"product&mode=remove&id="+productid.get(position)+"&userId="+userid);
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
						
						//---clear cache of image----
						 clearImageCache();
						 
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