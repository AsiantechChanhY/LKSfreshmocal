package com.shopsy;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.shopsy.Pojo.Detail_Reviews_Pojo;
import com.shopsy.Subclass.ActionBarActivity_Subclass_DetailPage;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.FileCache;
import com.shopsy.Utils.GridViewWithHeaderAndFooter;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.SeeAllReview_Adapter;
import com.squareup.picasso.Picasso;

public class SeeAllReviews extends ActionBarActivity_Subclass_DetailPage
{
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	private SessionManager session;
	private CommonIDSessionManager commonsession;
	private String userid, commonid;
	
	private RelativeLayout cart;
	private TextView cartcount;
	private RelativeLayout cartcountlayout;
	private String itemcartcount;
	private boolean cart_onresume=false;
	
	private ArrayList<Detail_Reviews_Pojo> itemList;
	
	private Context context;
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
	
	private RelativeLayout loading,nointernet,mainlayout;
	private GridViewWithHeaderAndFooter gridview;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private boolean show_progress_status=true;
	
	private ProgressBar loadmore_progressbar;
	private View footerview;
	private boolean loadingMore = false;
	private int checkpagepos = 0;
	
	private String asyntask_name="normal";
	
	JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;
	
	private ImageView seeall_review_loading;
		
	private String ShopID="",ownerImage="",companyName="",rating="",totalReviews="";
	private SeeAllReview_Adapter adapter;
	private TextView top_shopname,top_totaltreview;
	private ImageView top_ownerimage;
	private RatingBar top_ratingbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.see_all_review);
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		initialize();
		
		cart.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent(SeeAllReviews.this,CartPage.class);
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

								    ConnectionDetector cd = new ConnectionDetector(SeeAllReviews.this);
									boolean isInternetPresent = cd.isConnectingToInternet();
								
										if (isInternetPresent) 
										{
											if(session.isLoggedIn())
											{
												Loadmore_JsonRequest(Iconstant.seeallreview_url + ShopID + "&commonId=" + userid+"&pageId="+checkpagepos);
											}
											else
											{
												Loadmore_JsonRequest(Iconstant.seeallreview_url + ShopID + "&commonId=" + commonid+"&pageId="+checkpagepos);
											}
										} 
										else 
										{
											Toast.makeText(SeeAllReviews.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
			  
			    ConnectionDetector cd = new ConnectionDetector(SeeAllReviews.this);
				boolean isInternetPresent = cd.isConnectingToInternet();
				
				
						if (isInternetPresent) 
						{
							 if (session.isLoggedIn()) 
							 {
								 	asyntask_name="swipe";
									nointernet.setVisibility(View.GONE);
									actionBar.setDisplayShowCustomEnabled(true);
									
									JsonRequest(Iconstant.seeallreview_url + ShopID + "&commonId=" + userid+"&pageId=1");
							 }
							 else
							 {
								    asyntask_name="swipe";
									nointernet.setVisibility(View.GONE);
									// making hide custom menu and action bar title
									actionBar.setDisplayShowCustomEnabled(true);
									
									JsonRequest(Iconstant.seeallreview_url + ShopID + "&commonId=" + commonid+"&pageId=1");
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
		cd = new ConnectionDetector(SeeAllReviews.this);
		isInternetPresent = cd.isConnectingToInternet();
		session = new SessionManager(SeeAllReviews.this);
		commonsession = new CommonIDSessionManager(SeeAllReviews.this);
		itemList=new ArrayList<Detail_Reviews_Pojo>();
		
		gridview=(GridViewWithHeaderAndFooter)findViewById(R.id.seeall_review_GridView);
		loading=(RelativeLayout)findViewById(R.id.seeall_review_loading_layout);
		nointernet=(RelativeLayout)findViewById(R.id.seeall_review_nointernet_layout);
		mainlayout=(RelativeLayout)findViewById(R.id.seeall_review_main_layout);
		seeall_review_loading=(ImageView)findViewById(R.id.seeall_review_loading_gif);
		
		top_shopname = (TextView) findViewById(R.id.seeall_review_gridtop_shopname);
		top_totaltreview = (TextView) findViewById(R.id.seeall_review_gridtop_totalreviewcount);
		top_ownerimage = (ImageView) findViewById(R.id.seeall_review_gridtop_ownerimage);
		top_ratingbar = (RatingBar) findViewById(R.id.seeall_review_gridtop_ratingbar);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.seeall_review_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(false);

		//code for gridView header
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerview= inflater.inflate(R.layout.gridview_footer_loading, null);
	
		loadmore_progressbar=(ProgressBar)footerview.findViewById(R.id.grid_footer_progressbar);
		
		
	   	// code to set actionbar background
	    colorDrawable.setColor(0xff1A237E);
	    actionBar.setBackgroundDrawable(colorDrawable);
		
		// code to disable actionbar title
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ------------------code to add custom menu in action bar--------------------------------------

		actionBar.setDisplayShowCustomEnabled(true);
		View view = getLayoutInflater().inflate(R.layout.see_all_review_header, null);
		actionBar.setCustomView(view);

		cart = (RelativeLayout) view.findViewById(R.id.seeall_review_header_cartrelativelayout);
		cartcount = (TextView) view.findViewById(R.id.seeall_review_header_cartcounttext);
		cartcountlayout = (RelativeLayout) view.findViewById(R.id.seeall_review_header_cartcountlayout);
			
		
		// get user data from session
		 HashMap<String, String> user = session.getUserDetails();
		 userid = user.get(SessionManager.KEY_USERID);

		// get user data from session
		 HashMap<String, String> user1 = commonsession.getUserDetails();
		 commonid = user1.get(CommonIDSessionManager.KEY_COMMONID);	
		 
		 ShopID = getIntent().getStringExtra("shopId");
		
		//code to align grid with equal spacing
		//gridview.setNumColumns(GridView.AUTO_FIT); 
		gridview.setHorizontalSpacing(0); 
		gridview.setPadding(0, 0, 0, 0); 
		gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);	
			
	    gridview.addFooterView(footerview);
	    	
	    Glide.with(context)
	    .load(R.drawable.loadinganimation)
	    .asGif()
	    .crossFade()
	    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
	    .into(seeall_review_loading);
	    	 
	    	 
		if (isInternetPresent) 
		{
			nointernet.setVisibility(View.GONE);

			if (session.isLoggedIn()) 
			{
				JsonRequest(Iconstant.seeallreview_url + ShopID	+ "&commonId=" + userid + "&pageId=1");
			}
			else
			{
				JsonRequest(Iconstant.seeallreview_url + ShopID	+ "&commonId=" + commonid + "&pageId=1");
			}
		} 
		else 
		{
			mainlayout.setVisibility(View.GONE);
			nointernet.setVisibility(View.VISIBLE);
		}
	}
	
	//-------CLEAR IMAGE CACHE FROM PHONE SD CARD-----
	  private void clearImageCache()
	  {
			FileCache aa=new FileCache(SeeAllReviews.this);
			aa.clear();
	  }
	  
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
				
				System.out.println("--------------see all review url-------------------"+Url);
				
				 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) 
							{
								System.out.println("--------------see all review response -----------"+response);

								try {

									JSONArray jarray = response.getJSONArray("shopReview");
									
									itemcartcount = response.getString("cartCount");
									checkpagepos = Integer.parseInt(response.getString("pagePos"));
									
									session.SetCartCount(itemcartcount);

									if (jarray.length() > 0) 
									{
										for (int i = 0; i < jarray.length(); i++)
										{
											JSONObject object = jarray.getJSONObject(i);
											
											ownerImage = object.getString("sellerImage");
											companyName = object.getString("shopName");
											rating = object.getString("shop_ratting");
											totalReviews = object.getString("totalreviewCount");
											
											JSONArray review_array = object.getJSONArray("reviwes");
											if(review_array.length()>0)
											{
												itemList.clear();
												for (int l = 0; l < review_array.length(); l++)
												{
													JSONObject reviwes_object = review_array.getJSONObject(l);
													Detail_Reviews_Pojo review_items = new Detail_Reviews_Pojo();
										            	   
													review_items.setReviewer_name(reviwes_object.getString("reviewername"));
													review_items.setReviewer_image(reviwes_object.getString("reviewerphoto"));
													review_items.setReviewer_time(reviwes_object.getString("reviewerdate"));
													review_items.setReviewer_comment(reviwes_object.getString("reviewercomment"));
													review_items.setReviewer_rating(reviwes_object.getString("reviewerating"));
													review_items.setReviewer_productname(reviwes_object.getString("reviewProductName"));
													review_items.setReviewer_productimage(reviwes_object.getString("reviewProduct"));
													
													itemList.add(review_items);
												}
												
												show_progress_status=true;
											}
											else
											{
												show_progress_status=false;
											}
											
										}
									}
										
								} catch (JSONException e) {
									e.printStackTrace();
								}
								
								
								
								//------on post execute-----
								
								stopload();
								
								if(show_progress_status)
								{

									if (itemcartcount.length() > 0 && !itemcartcount.equals("0"))
									{
										cartcountlayout.setVisibility(View.VISIBLE);
										cartcount.setText(itemcartcount);
									}
									else
									{
										cartcountlayout.setVisibility(View.GONE);
									}
									
									top_shopname.setText(companyName);
									top_totaltreview.setText(totalReviews+" "+context.getResources().getString(R.string.seeall_review_label_review));
									top_ratingbar.setRating(Float.parseFloat(rating));
									Picasso.with(context).load(String.valueOf(ownerImage)).placeholder(R.drawable.nouserimg).into(top_ownerimage);
									 
									
									adapter = new SeeAllReview_Adapter(SeeAllReviews.this,itemList);
									gridview.setAdapter(adapter);
								}
								else
								{
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
			}
			
	
	
			
			
		// -------------------------code for JSON Request LoadMore----------------------------------
			
			private void Loadmore_JsonRequest(String Url) 
			{
				if(show_progress_status)
				{
					loadmore_progressbar.setVisibility(View.VISIBLE);
				}
				
				loadingMore = true;
				
				System.out.println("--------------see all review loadmore url-------------------"+Url);
				
				loadmore_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) 
							{
								System.out.println("--------------see all review loadmore response -----------"+response);

								try {

									JSONArray jarray = response.getJSONArray("shopReview");
									
									itemcartcount = response.getString("cartCount");
									checkpagepos = Integer.parseInt(response.getString("pagePos"));

									session.SetCartCount(itemcartcount);
									
									if (jarray.length() > 0) 
									{
										for (int i = 0; i < jarray.length(); i++)
										{
											JSONObject object = jarray.getJSONObject(i);
											
											JSONArray review_array = object.getJSONArray("reviwes");
											if(review_array.length()>0)
											{
												for (int l = 0; l < review_array.length(); l++)
												{
													JSONObject reviwes_object = review_array.getJSONObject(l);
													Detail_Reviews_Pojo review_items = new Detail_Reviews_Pojo();
										            	   
													review_items.setReviewer_name(reviwes_object.getString("reviewername"));
													review_items.setReviewer_image(reviwes_object.getString("reviewerphoto"));
													review_items.setReviewer_time(reviwes_object.getString("reviewerdate"));
													review_items.setReviewer_comment(reviwes_object.getString("reviewercomment"));
													review_items.setReviewer_rating(reviwes_object.getString("reviewerating"));
													review_items.setReviewer_productname(reviwes_object.getString("reviewProductName"));
													review_items.setReviewer_productimage(reviwes_object.getString("reviewProduct"));
													
													itemList.add(review_items);
												}
												
												show_progress_status=true;
											}
											else
											{
												show_progress_status=false;
											}
											
										}
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
										cartcountlayout.setVisibility(View.GONE);
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

				// Adding request to request queue
				AppController.getInstance().addToRequestQueue(loadmore_jsonObjReq);
			}
	
	
	
	//-----------------Move Back on pressed phone back button------------------		
	
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
				SeeAllReviews.this.finish();
				SeeAllReviews.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
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
				asyntask_name="normal";
				
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
