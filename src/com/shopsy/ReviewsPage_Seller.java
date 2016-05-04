package com.shopsy;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
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
import com.example.material_dialog_library.MaterialDialog;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Pojo.Reviews_Pojo;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.ReviewAdapter;

public class ReviewsPage_Seller extends HockeyActivity
{
	private ArrayList<Reviews_Pojo> itemList;
	private TextView header_title;
	
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
	private ImageView reviews_loading;
	
	JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;
	 
	private String asyntask_name="normal";
	
	private static ReviewAdapter adapter;
	private TextView emptyText;
	
	//----Declaration for Report Dialog-------
	RelativeLayout report_progress;
	TextView tv_report;
	MaterialDialog report_dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reviewspage);
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		initialize();
		
		
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

								    ConnectionDetector cd = new ConnectionDetector(ReviewsPage_Seller.this);
									boolean isInternetPresent = cd.isConnectingToInternet();
								
										if (isInternetPresent) 
										{
											if(session.isLoggedIn())
											{
												Loadmore_JsonRequest(Iconstant.reviews_list + userid +"&pageId="+checkpagepos);
											}
										} 
										else 
										{
											Toast.makeText(ReviewsPage_Seller.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
			  
			    ConnectionDetector cd = new ConnectionDetector(ReviewsPage_Seller.this);
				boolean isInternetPresent = cd.isConnectingToInternet();
				
						if (isInternetPresent) 
						{
							if(session.isLoggedIn())
							{
								asyntask_name="swipe";
								nointernet.setVisibility(View.GONE);
								actionBar.setDisplayShowCustomEnabled(true);
								
								JsonRequest(Iconstant.reviews_list + userid + "&pageId=0");
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
		itemList = new ArrayList<Reviews_Pojo>();
		cd = new ConnectionDetector(ReviewsPage_Seller.this);
		isInternetPresent = cd.isConnectingToInternet();
		
		session = new SessionManager(ReviewsPage_Seller.this);

		listview=(ListView)findViewById(R.id.reviews_listview);
		loading=(RelativeLayout)findViewById(R.id.reviews_loading_layout);
		nointernet=(RelativeLayout)findViewById(R.id.reviews_nointernet_layout);
		mainlayout=(RelativeLayout)findViewById(R.id.reviews_main_layout);
		reviews_loading=(ImageView)findViewById(R.id.reviews_loading_gif);
		emptyText = (TextView)findViewById(R.id.reviews_list_empty);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.reviews_swipe_refresh_layout);
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
		view = View.inflate(ReviewsPage_Seller.this, R.layout.home_custom_header, null);
		actionBar.setCustomView(view);

		RelativeLayout cart=(RelativeLayout)view.findViewById(R.id.cartrelativelayout);
		header_title=(TextView)view.findViewById(R.id.home_header_title);

		cart.setVisibility(View.GONE);
		header_title.setText("Reviews");				
		
		 
		 Glide.with(context)
		    .load(R.drawable.loadinganimation)
		    .asGif()
		    .crossFade()
		    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
		    .into(reviews_loading);
		 
		 
		   // to pass common id when user has not logged in
			
					if (isInternetPresent) 
					{
						if(session.isLoggedIn())
						{
							nointernet.setVisibility(View.GONE);
							JsonRequest(Iconstant.reviews_list + userid +"&pageId=0");
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
	
	
	
	//----------------OnClick for report------------
	public void reportMessage(int position,final String voteid)
	{

		report_dialog = new MaterialDialog(ReviewsPage_Seller.this);
		View view = LayoutInflater.from(ReviewsPage_Seller.this).inflate(R.layout.review_seller_report_dialog, null);
		
		tv_report=(TextView)view.findViewById(R.id.report_dialog_report_textview);
		 report_progress=(RelativeLayout)view.findViewById(R.id.report_dialog_progress_layout);
		final EditText et_report=(EditText)view.findViewById(R.id.editText_popup_desc_itemtitle_report);
		
		cd = new ConnectionDetector(ReviewsPage_Seller.this);
		isInternetPresent = cd.isConnectingToInternet();
		
		tv_report.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				
				if(et_report.getText().toString().length()==0)
				{
					Animation shake = AnimationUtils.loadAnimation(ReviewsPage_Seller.this, R.anim.shake);
					et_report.startAnimation(shake);
				}
				else
				{
					if(isInternetPresent)
					{
						Report_JsonRequest(Iconstant.report_review+userid+"&voteId="+voteid);
					}
					else
					{
						Toast.makeText(ReviewsPage_Seller.this, getResources().getString(R.string.nointernet_data), Toast.LENGTH_SHORT).show();
					}
				}
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
					
					System.out.println("--------------reviews url-------------------"+Url);
					
					 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) 
								{
									System.out.println("--------------reviews response -----------"+response);

									try {

										JSONArray jarray = response.getJSONArray("Reviews");
										checkpagepos = Integer.parseInt(response.getString("pagePos"));
										
										if (jarray.length() > 0) 
										{
											itemList.clear();
											
											for (int i = 0; i < jarray.length(); i++)
											{
												JSONObject object = jarray.getJSONObject(i);
												Reviews_Pojo items = new Reviews_Pojo();
												
												items.setReviewDate(object.getString("reviewDate"));
												items.setVoteId(object.getString("voteId"));
												items.setVoterId(object.getString("voterId"));
												items.setVoterName(object.getString("voterName"));
												items.setVoterImage(object.getString("voterImage"));
												items.setVoterEmail(object.getString("voterEmail"));
												items.setProductId(object.getString("productId"));
												items.setProductImage(object.getString("productImage"));
												items.setProductName(object.getString("productName"));
												items.setStarRating(object.getString("starRating"));
												items.setDescription(object.getString("description"));
												items.setReportCount(object.getString("reportCount"));
												
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
										adapter = new ReviewAdapter(ReviewsPage_Seller.this,itemList);
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
							
							System.out.println("--------------reviews loadmore url-------------------"+Url);
							
							loadmore_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
										@Override
										public void onResponse(JSONObject response) 
										{
											System.out.println("--------------reviews loadmore response -----------"+response);

											try {

												JSONArray jarray = response.getJSONArray("Reviews");
												checkpagepos = Integer.parseInt(response.getString("pagePos"));
												
												if (jarray.length() > 0) 
												{
													for (int i = 0; i < jarray.length(); i++)
													{
														JSONObject object = jarray.getJSONObject(i);
														Reviews_Pojo items = new Reviews_Pojo();
														
														items.setReviewDate(object.getString("reviewDate"));
														items.setVoteId(object.getString("voteId"));
														items.setVoterId(object.getString("voterId"));
														items.setVoterName(object.getString("voterName"));
														items.setVoterImage(object.getString("voterImage"));
														items.setVoterEmail(object.getString("voterEmail"));
														items.setProductId(object.getString("productId"));
														items.setProductImage(object.getString("productImage"));
														items.setProductName(object.getString("productName"));
														items.setStarRating(object.getString("starRating"));
														items.setDescription(object.getString("description"));
														items.setReportCount(object.getString("reportCount"));
														
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
						
						
						
						// -------------------------code for JSon Request----------------------------------
						
						private void Report_JsonRequest(String Url) 
						{
							System.out.println("--------------report url-------------------"+Url);
							
							tv_report.setVisibility(View.GONE);
							report_progress.setVisibility(View.VISIBLE);
							
							 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
										@Override
										public void onResponse(JSONObject response) 
										{
											System.out.println("--------------report response -----------"+response);
											
											try {
												String message = response.getString("message");
												
												if(message.equalsIgnoreCase("Reported Successfully"))
												{
													Toast.makeText(ReviewsPage_Seller.this, message, Toast.LENGTH_SHORT).show();
												}
												else
												{
													Toast.makeText(ReviewsPage_Seller.this, "Failed to Report", Toast.LENGTH_SHORT).show();
												}
												
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
											tv_report.setVisibility(View.VISIBLE);
											report_progress.setVisibility(View.GONE);
											report_dialog.dismiss();
										}
									}, new Response.ErrorListener() {
										@Override
										public void onErrorResponse(VolleyError error) 
										{
											
											tv_report.setVisibility(View.VISIBLE);
											report_progress.setVisibility(View.GONE);
											 
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
						
						
				// --------------------------code to destroy asynTask when another activity is called---------------------------
				@Override
				public void onDestroy() 
				{
							
					asyntask_name="normal";
							
					if (jsonObjReq != null) {
						jsonObjReq.cancel();
					}
			
					if (loadmore_jsonObjReq != null) {
						loadmore_jsonObjReq.cancel();
					}

					super.onDestroy();
				}		
}
