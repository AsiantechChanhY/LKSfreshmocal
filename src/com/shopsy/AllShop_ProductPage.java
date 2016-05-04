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
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.shopsy.Pojo.AllShop_Product_Pojo;
import com.shopsy.StaggeredGridView.StaggeredGridView;
import com.shopsy.Subclass.ActionBarActivity_Subclass_AllShop_Product;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.CircularImageView;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.FileCache;
import com.shopsy.Utils.ImageLoader;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.AllShop_Product_Adapter;

/**
 * @author Prem Kumar
 *
 */
public class AllShop_ProductPage extends ActionBarActivity_Subclass_AllShop_Product
{

	private ArrayList<AllShop_Product_Pojo> itemList;
	private ArrayList<String> productid = new ArrayList<String>();
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	
	private SessionManager session;
	private CommonIDSessionManager commonsession;
	private String userid, commonid;
	
	private Context context;
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
	
	private RelativeLayout loading,nointernet,mainlayout;
	private StaggeredGridView gridview;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private ProgressBar loadmore_progressbar;
	private TextView tv_shopname,tv_sellername,tv_location,tv_totalreview_count;
	private CircularImageView sellerimage;
	private ImageView fav_image;
	private RatingBar review_rating;
	private RelativeLayout location_layout,favorite_layout,contact_shoplayout,review_layout,shopinfo_layout;
	
	private TextView header_title;
	private RelativeLayout cart;
	private TextView cartcount;
	private RelativeLayout cartcountlayout;
	private String itemcartcount;
	private int checkpagepos = 0;
	private boolean loadingMore = false;
	private boolean show_progress_status=true;
	private String shopurl="";
	private AllShop_Product_Adapter adapter;
	private boolean cart_onresume=false;
	
	private String convId=" ";
	private String msgId="",policy="",aboutus="";
	
	JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;

	private ImageView allshop_product_loading;
		
	private String Sshopname="",Ssellername="",Ssellerid="",Sseller_location="",Sseller_image="",
			Sreview_rating="",Stotalreview="",Sfav_status="";	
	private ImageLoader imageLoader;
	private int favcount=0;
	
	private View headerView,footerview;
	private String asyntask_name="normal";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allshop_product);

		context = getApplicationContext();
		actionBar = getSupportActionBar();
		
		//---clear cache of image----
		 clearImageCache();
		 
		initialize();
		

	     cd = new ConnectionDetector(AllShop_ProductPage.this);
		 isInternetPresent = cd.isConnectingToInternet();
		
				if (isInternetPresent) 
				{
					JsonRequest1(Iconstant.pagecount);
					
				}
				else
				{
					Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
				}
	
		cart.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent(AllShop_ProductPage.this,CartPage.class);
				startActivity(intent);
				overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});
		
		gridview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				int new_position=position-1;
				
				Intent intent=new Intent(AllShop_ProductPage.this,DetailPage.class);
				intent.putExtra("productid", itemList.get(new_position).getProductId());
				startActivity(intent);
				overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});
		
		
		favorite_layout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				
				if(session.isLoggedIn())
				{
					if(favcount==0)
					{
						fav_image.setImageResource(R.drawable.favredtwo);
						
						FavoriteAsyncTask favasyntask=new FavoriteAsyncTask();
						favasyntask.execute(Iconstant.heartaddurl + "shop&mode=add&id="+Ssellerid+"&userId="+userid);
						favcount++;
					}
					else
					{
						fav_image.setImageResource(R.drawable.favtwo);
						
						FavoriteAsyncTask favasyntask=new FavoriteAsyncTask();
						favasyntask.execute(Iconstant.heartremoveurl + "shop&mode=remove&id="+Ssellerid+"&userId="+userid);
						favcount--;
					}
				}
				else
				{
					favourit_popup("allshop_product",shopurl);
				}
			}
		});
		
		contact_shoplayout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				
				if(session.isLoggedIn())
				{
				System.out.println("msgid--------------------------------"+msgId);
				Intent i1 = new Intent(AllShop_ProductPage.this, Contacts.class);
				i1.putExtra("shopId", Ssellerid);
				i1.putExtra("shopname", Sshopname);
				i1.putExtra("userid", userid);
				i1.putExtra("msgid", msgId);
				i1.putExtra("convid", convId);
				startActivity(i1);
				overridePendingTransition(R.anim.enter, R.anim.exit);
				}
				else
				{
					favourit_popup("allshop_product",shopurl);
				}
				
			}
		});
		
		review_layout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(Stotalreview.equalsIgnoreCase("0"))
				{
					//Do nothing
				}
				else
				{
					Intent i1 = new Intent(AllShop_ProductPage.this, SeeAllReviews.class);
					i1.putExtra("shopId", Ssellerid);
					startActivity(i1);
					overridePendingTransition(R.anim.enter, R.anim.exit);
				}
			}
		});
		
		shopinfo_layout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				
				Intent i12 = new Intent(AllShop_ProductPage.this, Shop_info.class);
				i12.putExtra("shopId", Ssellerid);
				i12.putExtra("policy", policy);
				i12.putExtra("aboutus", aboutus);
				startActivity(i12);
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

								    ConnectionDetector cd = new ConnectionDetector(AllShop_ProductPage.this);
									boolean isInternetPresent = cd.isConnectingToInternet();
								
										if (isInternetPresent) 
										{
											if(session.isLoggedIn())
											{
												Loadmore_JsonRequest(Iconstant.allshop_producturl + shopurl +"&pageId="+ checkpagepos + "&commonId=" + userid);
											}
											else
											{
												Loadmore_JsonRequest(Iconstant.allshop_producturl + shopurl +"&pageId="+ checkpagepos + "&commonId=" + commonid);
											}
										} 
										else 
										{
											Toast.makeText(AllShop_ProductPage.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
				    ConnectionDetector cd = new ConnectionDetector(AllShop_ProductPage.this);
					boolean isInternetPresent = cd.isConnectingToInternet();
					
							if (isInternetPresent) 
							{
								if(session.isLoggedIn())
								{
									asyntask_name="swipe";
									nointernet.setVisibility(View.GONE);
									actionBar.setDisplayShowCustomEnabled(true);
									
									JsonRequest(Iconstant.allshop_producturl + shopurl +"&pageId="+ 1 + "&commonId=" + userid);
								}
								else
								{
									asyntask_name="swipe";
									nointernet.setVisibility(View.GONE);
									actionBar.setDisplayShowCustomEnabled(true);
									
									JsonRequest(Iconstant.allshop_producturl + shopurl +"&pageId="+ 1 + "&commonId=" + commonid);
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
		
		
		//gridview.setExpanded(true);
		
		gridview.addHeaderView(headerView);
		gridview.addFooterView(footerview);
	}

	@SuppressWarnings("deprecation")
	private void initialize() 
	{
		itemList = new ArrayList<AllShop_Product_Pojo>();
		cd = new ConnectionDetector(AllShop_ProductPage.this);
		isInternetPresent = cd.isConnectingToInternet();
		imageLoader=new ImageLoader(AllShop_ProductPage.this);
		
		session = new SessionManager(AllShop_ProductPage.this);
		commonsession = new CommonIDSessionManager(AllShop_ProductPage.this);
		
		gridview=(StaggeredGridView)findViewById(R.id.allshop_product_gridView);
		loading=(RelativeLayout)findViewById(R.id.allshop_product_loading_layout);
		nointernet=(RelativeLayout)findViewById(R.id.allshop_product_nointernet_layout);
		mainlayout=(RelativeLayout)findViewById(R.id.allshop_product_mainlayout);
		allshop_product_loading=(ImageView)findViewById(R.id.allshop_product_loading_gif);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.allshop_product_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(true);
		
		
		//code for gridView header
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		headerView = inflater.inflate(R.layout.allshop_product_grid_header, null);
		footerview= inflater.inflate(R.layout.allshop_product_grid_footer, null);
		
		tv_sellername=(TextView)headerView.findViewById(R.id.allshop_product_username);
		tv_shopname=(TextView)headerView.findViewById(R.id.allshop_product_usershopname);
		tv_location=(TextView)headerView.findViewById(R.id.allshop_product_usershoplocation);
		tv_totalreview_count=(TextView)headerView.findViewById(R.id.allshop_product_review_count);
		
		sellerimage=(CircularImageView)headerView.findViewById(R.id.allshop_product_userimage);
		fav_image=(ImageView)headerView.findViewById(R.id.allshop_product_favourite_image);
		review_rating=(RatingBar)headerView.findViewById(R.id.allshop_product_review_ratingBar);
		location_layout=(RelativeLayout)headerView.findViewById(R.id.allshop_product_userlocation_layout);
		favorite_layout=(RelativeLayout)headerView.findViewById(R.id.allshop_product_favourite_layout);
		contact_shoplayout=(RelativeLayout)headerView.findViewById(R.id.allshop_product_contact_layout);
		review_layout=(RelativeLayout)headerView.findViewById(R.id.allshop_product_review_layout);
		shopinfo_layout=(RelativeLayout)headerView.findViewById(R.id.allshop_product_shopinfo_layout);
		
		loadmore_progressbar=(ProgressBar)footerview.findViewById(R.id.allshop_product_progressbar);
		
		
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		// get user data from session
		HashMap<String, String> user1 = commonsession.getUserDetails();
		commonid = user1.get(CommonIDSessionManager.KEY_COMMONID);
		
		
		// code to set actionbar background
      	colorDrawable.setColor(0xff1A237E);
      	actionBar.setBackgroundDrawable(colorDrawable);
	
	   // code to disable actionbar title
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		// ------------------code to add custom menu in action bar--------------------------------------
		actionBar.setDisplayShowCustomEnabled(true);
		View view = AllShop_ProductPage.this.getLayoutInflater().inflate(R.layout.allshop_product_header, null);
		actionBar.setCustomView(view);
		
		cart = (RelativeLayout) view.findViewById(R.id.allshop_product_header_cartrelativelayout);
		cartcount = (TextView) view.findViewById(R.id.allshop_product_header_cartcounttext);
		cartcountlayout = (RelativeLayout) view.findViewById(R.id.allshop_product_header_cartcountlayout);
		header_title=(TextView)view.findViewById(R.id.allshop_product_header_title);
		
		
		Intent intent = getIntent();
		shopurl = intent.getStringExtra("shopurl");
		
		
		/*//code to align grid with equal spacing
		gridview.setNumColumns(GridView.AUTO_FIT); 
		gridview.setHorizontalSpacing(10); 
		gridview.setVerticalSpacing(10); 
		gridview.setPadding(10, 0, 10, 0); 
		gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH); */
		    
		
		Glide.with(context)
	    .load(R.drawable.loadinganimation)
	    .asGif()
	    .crossFade()
	    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
	    .into(allshop_product_loading);
		
		 // to pass common id when user has not logged in

			if (isInternetPresent) 
			{
				if (session.isLoggedIn()) 
				{
					nointernet.setVisibility(View.GONE);
					JsonRequest(Iconstant.allshop_producturl + shopurl+ "&pageId=" + 1 + "&commonId=" + userid);
				}
				else 
				{
					nointernet.setVisibility(View.GONE);
					JsonRequest(Iconstant.allshop_producturl + shopurl+ "&pageId=" + 1 + "&commonId=" + commonid);
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
	
	//-------CLEAR IMAGE CACHE FROM PHONE SD CARD-----
	  private void clearImageCache()
	  {
			FileCache aa=new FileCache(AllShop_ProductPage.this);
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
				
				System.out.println("--------------All Shop product url-------------------"+Url);
				
				 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) 
							{
								System.out.println("--------------All Shop product response -----------"+response);

								try {

									Sshopname=response.getString("shop_name");
									Ssellername=response.getString("sellerName");
									Ssellerid=response.getString("shop_id");
									Sseller_location=response.getString("shop_location");
									Sseller_image=response.getString("sellerImage");
									Sreview_rating=response.getString("shop_ratting");
									Stotalreview=response.getString("review_count");
									Sfav_status=response.getString("favStatus");
									itemcartcount = response.getString("cartCount");
									checkpagepos = Integer.parseInt(response.getString("pagePos"));
									
									favcount=Integer.parseInt(Sfav_status);

									session.SetCartCount(itemcartcount);
									
									JSONArray jarray = response.getJSONArray("productDetails");
									if(jarray.length()>0)
									{
										itemList.clear();
										productid.clear();
										
										for (int i = 0; i < jarray.length(); i++) 
										{
											JSONObject object = jarray.getJSONObject(i);
											AllShop_Product_Pojo items = new AllShop_Product_Pojo();

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
										itemList.clear();
										productid.clear();
										show_progress_status=true;
									}
									
									
									JSONObject conversation = response.getJSONObject("conversation_details");
									if(conversation.length()>0)
									{
										
										msgId=conversation.getString("messager_id");
									}
									else
									{
										
										msgId=" ";
//										conversation_detail.clear();
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
									
									header_title.setText(Ssellername);
									tv_sellername.setText(Ssellername);
									tv_shopname.setText(Sshopname);
									tv_totalreview_count.setText("("+Stotalreview +" "+"reviews"+")");

									//code for location
									if(Sseller_location.length()>0)
									{
										location_layout.setVisibility(View.VISIBLE);
										tv_location.setText(Sseller_location);
									}
									else
									{
										location_layout.setVisibility(View.GONE);
									}
									
									//code for favorite status
									if(Sfav_status.length()>0)
									{
										if(Sfav_status.equalsIgnoreCase("1"))
										{
											fav_image.setImageResource(R.drawable.favredtwo);
										}
										else
										{
											fav_image.setImageResource(R.drawable.favtwo);
										}
									}

									//code for review rating
									if(Sreview_rating.length()>0)
									{
										review_rating.setRating(Float.valueOf(Sreview_rating));
									}
									else
									{
										review_rating.setRating(0);
									}
									
									imageLoader.DisplayImage(String.valueOf(Sseller_image), sellerimage);
									
									adapter = new AllShop_Product_Adapter(AllShop_ProductPage.this,itemList);
									gridview.setAdapter(adapter);
								} 
								else
								{
									mainlayout.setVisibility(View.VISIBLE);
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
	
		
		
			
			// -------------------------code for JSon Request----------------------------------
			
						private void Loadmore_JsonRequest(String Url) 
						{
							if(show_progress_status)
							{
								loadmore_progressbar.setVisibility(View.VISIBLE);
							}
							
							loadingMore = true;
							
							System.out.println("--------------All Shop product loadmore url-------------------"+Url);
							
							loadmore_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
										@Override
										public void onResponse(JSONObject response) 
										{
											System.out.println("--------------All Shop product loadmore response -----------"+response);

											try {

												Sshopname=response.getString("shop_name");
												Ssellername=response.getString("sellerName");
												Ssellerid=response.getString("shop_id");
												Sseller_location=response.getString("shop_location");
												Sseller_image=response.getString("sellerImage");
												Sreview_rating=response.getString("shop_ratting");
												Stotalreview=response.getString("review_count");
												Sfav_status=response.getString("favStatus");
												itemcartcount = response.getString("cartCount");
												checkpagepos = Integer.parseInt(response.getString("pagePos"));
												
												favcount=Integer.parseInt(Sfav_status);

												session.SetCartCount(itemcartcount);
												
												JSONArray jarray = response.getJSONArray("productDetails");
												if(jarray.length()>0)
												{
													for (int i = 0; i < jarray.length(); i++) 
													{
														JSONObject object = jarray.getJSONObject(i);
														AllShop_Product_Pojo items = new AllShop_Product_Pojo();

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
													show_progress_status=true;
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

							// Adding request to request queue
							AppController.getInstance().addToRequestQueue(loadmore_jsonObjReq);
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
				
				
				
				
				private void JsonRequest1(String Url) 
				{
					startload();
					
					System.out.println("--------------shop info url-------------------"+Url);
					
					 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) 
								{
									System.out.println("--------------Detail page response -----------"+response);

									try {

												
												policy=response.getString("privacy-policy");
												
					 							aboutus=response.getString("about-us");

											
										
									} catch (JSONException e) {
										e.printStackTrace();
									}
									
									
									
									
									
								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) 
								{
									 
								
									
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

				
				
				
		//-----------------Move Back on pressed phone back button------------------		
			
				@Override
				public boolean onKeyDown(int keyCode, KeyEvent event) 
				{
					if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
						AllShop_ProductPage.this.finish();
						AllShop_ProductPage.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
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
