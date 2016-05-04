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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.shopsy.Pojo.Categories_Pojo;
import com.shopsy.Subclass.Fragment_subclass;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.FileCache;
import com.shopsy.Utils.GridViewWithHeaderAndFooter;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.CategoriesAdapter;

public class CategoryPage extends Fragment_subclass
{
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	private SessionManager session;
	private CommonIDSessionManager commonsession;
	private String userid, commonid;
	
	private RelativeLayout cart;
	private RelativeLayout cartcountlayout;
	private TextView cartcount;
	private String itemcartcount;
	
	private ArrayList<String> categoriesspinnerlist = new ArrayList<String>();
	private ArrayList<Categories_Pojo> itemList;
	private ArrayList<String> id = new ArrayList<String>();
	private CategoriesAdapter adapter;
	
	private RelativeLayout loading,nointernet,mainlayout;
	private GridViewWithHeaderAndFooter gridview;
	private TextView emptyText;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private boolean show_progress_status=true;
	private boolean cart_onresume=false;
	
	JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;
	
	private Context context;
	private ActionBar actionBar;
	
	private ProgressBar loadmore_progressbar;
	private View headerView,footerview;
	private boolean loadingMore = false;
	private int checkpagepos = 0;
	
	private ImageView categories_loading;
	private String asyntask_name="normal";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View rootview = inflater.inflate(R.layout.categoriespage, container,false);

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
		
		gridview.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				int new_position=position-2;
				
				Intent intent = new Intent(context, SubCategoriesPage.class);
				intent.putExtra("catposition", new_position);
				intent.putStringArrayListExtra("spinnerlist", categoriesspinnerlist);
				intent.putExtra("itemid", itemList.get(new_position).getId());
				intent.putStringArrayListExtra("totalitemid", id);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
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
											if(session.isLoggedIn())
											{
												Loadmore_JsonRequest(Iconstant.categoriespageurl+ userid+"&pageId="+checkpagepos);
											}
											else
											{
												Loadmore_JsonRequest(Iconstant.categoriespageurl+ commonid+"&pageId="+checkpagepos);
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
			  
			    ConnectionDetector cd = new ConnectionDetector(getActivity());
				boolean isInternetPresent = cd.isConnectingToInternet();
				
						if (isInternetPresent) 
						{
							if(session.isLoggedIn())
							{
								asyntask_name="swipe";
								nointernet.setVisibility(View.GONE);
								actionBar.setDisplayShowCustomEnabled(true);
								
								JsonRequest(Iconstant.categoriespageurl+ userid+"&pageId=1");
							}
							else
							{
								asyntask_name="swipe";
								nointernet.setVisibility(View.GONE);
								// making hide custom menu and action bar title
								actionBar.setDisplayShowCustomEnabled(true);

								JsonRequest(Iconstant.categoriespageurl+ commonid+"&pageId=1");
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
		cd = new ConnectionDetector(getActivity());
		isInternetPresent = cd.isConnectingToInternet();
		session = new SessionManager(getActivity());
		commonsession = new CommonIDSessionManager(getActivity());
		itemList=new ArrayList<Categories_Pojo>();
		
		gridview=(GridViewWithHeaderAndFooter)rootview.findViewById(R.id.category_GridView);
		loading=(RelativeLayout)rootview.findViewById(R.id.category_loading_layout);
		nointernet=(RelativeLayout)rootview.findViewById(R.id.category_nointernet_layout);
		mainlayout=(RelativeLayout)rootview.findViewById(R.id.category_main_layout);
		emptyText = (TextView)rootview.findViewById(R.id.category_list_empty);
		
		categories_loading=(ImageView)rootview.findViewById(R.id.categories_loading_gif);
		
		swipeRefreshLayout = (SwipeRefreshLayout)rootview.findViewById(R.id.category_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(false);
		
		
		
		// code to disable actionbar title
			actionBar.setDisplayShowTitleEnabled(false);

		// ------------------code to add custom menu in action bar--------------------------------------

			actionBar.setDisplayShowCustomEnabled(true);
			View view = getActivity().getLayoutInflater().inflate(R.layout.categoryheader, null);
			actionBar.setCustomView(view);

			cart = (RelativeLayout) view.findViewById(R.id.category_header_cartrelativelayout);
			cartcount = (TextView) view.findViewById(R.id.category_header_cartcounttext);
			cartcountlayout = (RelativeLayout) view.findViewById(R.id.category_header_cartcountlayout);
			
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
		
			
		 // code to align grid with equal spacing
	        gridview.setNumColumns(GridView.AUTO_FIT);
	        gridview.setHorizontalSpacing(10);
	        gridview.setVerticalSpacing(10);
	        gridview.setPadding(10, 0, 10, 10);
	        gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		
		
	        gridview.addHeaderView(headerView);
    		gridview.addFooterView(footerview);
	        
    		Glide.with(context)
    	    .load(R.drawable.loadinganimation)
    	    .asGif()
    	    .crossFade()
    	    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
    	    .into(categories_loading);
    		
			if (isInternetPresent) 
			{
				if(session.isLoggedIn())
				{
					nointernet.setVisibility(View.GONE);
					JsonRequest(Iconstant.categoriespageurl+ userid+"&pageId=1");
				}
				else
				{
					nointernet.setVisibility(View.GONE);
					JsonRequest(Iconstant.categoriespageurl+ commonid+"&pageId=1");
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
					swipeRefreshLayout.setRefreshing(false);
				}
			}
	
	
	
	
	
	// -------------------------code for JSon Request----------------------------------
	
			private void JsonRequest(String Url) 
			{
				startload();
				
				System.out.println("--------------Categories url-------------------"+Url);
				
				 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) 
							{
								System.out.println("--------------Categories response -----------"+response);

								try {

									JSONArray jarray = response.getJSONArray("categoryDetails");
									
									itemcartcount = response.getString("cartCount");
									checkpagepos = Integer.parseInt(response.getString("pagePos"));
									
									session.SetCartCount(itemcartcount);
									
									if (jarray.length() > 0) 
									{
										id.clear();
										itemList.clear();
										categoriesspinnerlist.clear();
										
										for (int i = 0; i < jarray.length(); i++) 
										{
											JSONObject object = jarray.getJSONObject(i);
											Categories_Pojo items = new Categories_Pojo();
											
											if (Integer.parseInt(object.getString("catId")) == 0) 
											{
												items.setCategoryName(object.getString("categoryName"));
												items.setCatId(object.getString("catId"));
												items.setId(object.getString("id"));
												items.setCatimage(object.getString("image"));
												
												id.add(object.getString("id"));
												itemList.add(items);
												categoriesspinnerlist.add(object.getString("categoryName"));
											}
									    }
										
										show_progress_status=true;
									}
									else
									{
										id.clear();
										itemList.clear();
										categoriesspinnerlist.clear();
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
									
									adapter = new CategoriesAdapter(context,itemList);
									gridview.setAdapter(adapter);
								}
								else
								{
				                    emptyText.setVisibility(View.VISIBLE);
				                    gridview.setEmptyView(emptyText);
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
						
						System.out.println("--------------Categories loadmore url-------------------"+Url);
						
						loadmore_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
									@Override
									public void onResponse(JSONObject response) 
									{
										System.out.println("--------------Categories loadmore response -----------"+response);

										try {

											JSONArray jarray = response.getJSONArray("categoryDetails");
											
											itemcartcount = response.getString("cartCount");
											checkpagepos = Integer.parseInt(response.getString("pagePos"));
											
											session.SetCartCount(itemcartcount);
											
											if (jarray.length() > 0) 
											{
												for (int i = 0; i < jarray.length(); i++) 
												{
													JSONObject object = jarray.getJSONObject(i);
													Categories_Pojo items = new Categories_Pojo();
													
													if (Integer.parseInt(object.getString("catId")) == 0) 
													{
														items.setCategoryName(object.getString("categoryName"));
														items.setCatId(object.getString("catId"));
														items.setId(object.getString("id"));
														items.setCatimage(object.getString("image"));
														
														id.add(object.getString("id"));
														itemList.add(items);
														categoriesspinnerlist.add(object.getString("categoryName"));
													}
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

						// Adding request to request queue
						AppController.getInstance().addToRequestQueue(loadmore_jsonObjReq);
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
