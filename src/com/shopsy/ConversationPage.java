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
import com.shopsy.HockeyApp.HockeyFragment;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Pojo.Conversation_Pojo;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.Conversation_Adapter;

public class ConversationPage extends HockeyFragment
{
	private ArrayList<Conversation_Pojo> itemList;
	private RelativeLayout cart;
	private TextView cartcount;
	private TextView header_title;
	private RelativeLayout cartcountlayout;
	private String itemcartcount;
	
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
	private boolean cart_onresume=false;
	
	private static Conversation_Adapter adapter;
	private TextView emptyText;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View rootview = inflater.inflate(R.layout.conversationpage, container,false);

		context = getActivity();
		ActionBarActivity actionBarActivity = (ActionBarActivity) getActivity();
		actionBar = actionBarActivity.getSupportActionBar();
		initialize(rootview);
		
		cart.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent(getActivity(),CartPage.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				itemList.get(position).setStatus("Read");
				itemList.get(position).setNewMsg("0");
				
				Intent intent=new Intent(getActivity(),ConversationDetail.class);
				intent.putExtra("convId", itemList.get(position).getConv_id());
				intent.putExtra("chat_name", itemList.get(position).getSender_name());
				intent.putExtra("chat_image", itemList.get(position).getSender_image());
				intent.putExtra("Type", "User");
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
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

								    ConnectionDetector cd = new ConnectionDetector(getActivity());
									boolean isInternetPresent = cd.isConnectingToInternet();
								
										if (isInternetPresent) 
										{
											if(session.isLoggedIn())
											{
												Loadmore_JsonRequest(Iconstant.conversation_url + userid + "&commonId=" + userid +"&type=U"+"&pageId="+checkpagepos);
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
								
								JsonRequest(Iconstant.conversation_url + userid + "&commonId=" + userid+"&type=U"+ "&pageId=0");
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
		itemList = new ArrayList<Conversation_Pojo>();
		cd = new ConnectionDetector(getActivity());
		isInternetPresent = cd.isConnectingToInternet();
		
		session = new SessionManager(getActivity());

		listview=(ListView)rootview.findViewById(R.id.conversation_listview);
		loading=(RelativeLayout)rootview.findViewById(R.id.conversation_loading_layout);
		nointernet=(RelativeLayout)rootview.findViewById(R.id.conversation_nointernet_layout);
		mainlayout=(RelativeLayout)rootview.findViewById(R.id.conversation_main_layout);
		conversation_loading=(ImageView)rootview.findViewById(R.id.conversation_loading_gif);
		emptyText = (TextView)rootview.findViewById(R.id.conversation_list_empty);
		
		swipeRefreshLayout = (SwipeRefreshLayout)rootview.findViewById(R.id.conversation_swipe_refresh_layout);
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
		view = View.inflate(context, R.layout.home_custom_header, null);
		actionBar.setCustomView(view);

		cart = (RelativeLayout) view.findViewById(R.id.cartrelativelayout);
		cartcount = (TextView) view.findViewById(R.id.homecartcounttext);
		cartcountlayout = (RelativeLayout) view.findViewById(R.id.homecartcountlayout);
		header_title=(TextView)view.findViewById(R.id.home_header_title);

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
							JsonRequest(Iconstant.conversation_url + userid + "&commonId=" + userid+"&type=U"+"&pageId=0");
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
									itemcartcount = response.getString("cartCount");
									checkpagepos = Integer.parseInt(response.getString("pagePos"));
									
									session.SetCartCount(itemcartcount);
									
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
									if (itemcartcount.length() > 0 && !itemcartcount.equals("0"))
									{
										cartcountlayout.setVisibility(View.VISIBLE);
										cartcount.setText(itemcartcount);
									}
									else
									{
										cartcountlayout.setVisibility(View.GONE);
									}
									
									adapter = new Conversation_Adapter(getActivity(),itemList);
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
											itemcartcount = response.getString("cartCount");
											checkpagepos = Integer.parseInt(response.getString("pagePos"));
											
											session.SetCartCount(itemcartcount);
											
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
											if (itemcartcount.length() > 0 && !itemcartcount.equals("0"))
											{
												cartcountlayout.setVisibility(View.VISIBLE);
												cartcount.setText(itemcartcount);
											}
											else
											{
												cartcountlayout.setVisibility(View.GONE);
											}
											
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
				
				asyntask_name="normal";
				cart_onresume=false;
				
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
