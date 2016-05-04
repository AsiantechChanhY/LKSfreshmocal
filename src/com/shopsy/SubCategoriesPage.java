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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Pojo.Subcategories_Pojo;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.FileCache;
import com.shopsy.Utils.GridViewWithHeaderAndFooter;
import com.shopsy.Utils.ImageLoader;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.SubcategoriesAdapter;

public class SubCategoriesPage extends HockeyActivity implements OnItemSelectedListener
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
	private Spinner spinner;
	
	private Context context;
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
	
	private RelativeLayout loading,nointernet,mainlayout;
	private GridViewWithHeaderAndFooter gridview;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private boolean show_progress_status=true;
	private ImageView bannerimage;
	private TextView bannername;
	private boolean cart_onresume=false;
	private ImageView subcategories_loading;
	
	private ArrayList<String> categoriesspinnerlist = new ArrayList<String>();
	private ArrayList<String> subcategoriesspinnerlist = new ArrayList<String>();
	private ArrayList<Subcategories_Pojo> itemList;
	private ArrayList<String> id = new ArrayList<String>();
	ArrayList<String> totalitemid = new ArrayList<String>();
	private SubcategoriesAdapter adapter;
	private ImageLoader imageLoader;
	
	JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;
	
	private ProgressBar loadmore_progressbar;
	private View headerView,footerview;
	private boolean loadingMore = false;
	private int checkpagepos = 0;
	
	private String asyntask_name="normal";
	
	private String itemid="";
	private int position=0;
	
	private String Sbannerid="",Sbannerimage="",Sbannername="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subcategories);
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		initialize();
		
		cart.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent(SubCategoriesPage.this,CartPage.class);
				startActivity(intent);
				overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});
		
		
		bannerimage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(SubCategoriesPage.this, SubCategories_Product.class);
				intent.putExtra("catposition", 0);
				intent.putStringArrayListExtra("spinnerlist", subcategoriesspinnerlist);
				intent.putExtra("itemid", id.get(0));
				intent.putStringArrayListExtra("totalitemid", id);
				startActivity(intent);
				overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});
		
		gridview.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				int new_position=position-2;
				
				Intent intent = new Intent(SubCategoriesPage.this, SubCategories_Product.class);
				intent.putExtra("catposition", (new_position+1));
				intent.putStringArrayListExtra("spinnerlist", subcategoriesspinnerlist);
				intent.putExtra("itemid", id.get(new_position+1));
				intent.putStringArrayListExtra("totalitemid", id);
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

								    ConnectionDetector cd = new ConnectionDetector(SubCategoriesPage.this);
									boolean isInternetPresent = cd.isConnectingToInternet();
								
										if (isInternetPresent) 
										{
											if(session.isLoggedIn())
											{
												Loadmore_JsonRequest(Iconstant.subcategoriesurl+itemid + "&commonId=" + userid+"&pageId="+checkpagepos);
											}
											else
											{
												Loadmore_JsonRequest(Iconstant.subcategoriesurl+itemid + "&commonId=" + commonid+"&pageId="+checkpagepos);
											}
										} 
										else 
										{
											Toast.makeText(SubCategoriesPage.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
			  
			    ConnectionDetector cd = new ConnectionDetector(SubCategoriesPage.this);
				boolean isInternetPresent = cd.isConnectingToInternet();
				
						if (isInternetPresent) 
						{
							
							if(session.isLoggedIn())
							{
								asyntask_name="swipe";
								nointernet.setVisibility(View.GONE);
								actionBar.setDisplayShowCustomEnabled(true);
								
								JsonRequest(Iconstant.subcategoriesurl+itemid + "&commonId=" + userid+"&pageId=1");
							}
							else
							{
								asyntask_name="swipe";
								nointernet.setVisibility(View.GONE);
								// making hide custom menu and action bar title
								actionBar.setDisplayShowCustomEnabled(true);
								
								JsonRequest(Iconstant.subcategoriesurl+itemid + "&commonId=" + commonid+"&pageId=1");
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
		cd = new ConnectionDetector(SubCategoriesPage.this);
		isInternetPresent = cd.isConnectingToInternet();
		session = new SessionManager(SubCategoriesPage.this);
		commonsession = new CommonIDSessionManager(SubCategoriesPage.this);
		itemList=new ArrayList<Subcategories_Pojo>();
		imageLoader = new ImageLoader(context);
		
		gridview=(GridViewWithHeaderAndFooter)findViewById(R.id.subcategory_GridView);
		loading=(RelativeLayout)findViewById(R.id.subcategory_loading_layout);
		nointernet=(RelativeLayout)findViewById(R.id.subcategory_nointernet_layout);
		mainlayout=(RelativeLayout)findViewById(R.id.subcategory_main_layout);
		subcategories_loading=(ImageView)findViewById(R.id.subcategories_loading_gif);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.subcategory_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(false);
		

		//code for gridView header
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		headerView = inflater.inflate(R.layout.subcategories_gridview_header, null);
		footerview= inflater.inflate(R.layout.gridview_footer_loading, null);
	
		loadmore_progressbar=(ProgressBar)footerview.findViewById(R.id.grid_footer_progressbar);
		bannerimage=(ImageView)headerView.findViewById(R.id.subcategory_banner_image);
		bannername=(TextView)headerView.findViewById(R.id.subcategory_banner_name);
		
		
	      	// code to set actionbar background
	      	colorDrawable.setColor(0xff1A237E);
	      	actionBar.setBackgroundDrawable(colorDrawable);
		
		// code to disable actionbar title
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);

		// ------------------code to add custom menu in action bar--------------------------------------

			actionBar.setDisplayShowCustomEnabled(true);
			View view = getLayoutInflater().inflate(R.layout.subcategories_header, null);
			actionBar.setCustomView(view);

			cart = (RelativeLayout) view.findViewById(R.id.subcategories_header_cartrelativelayout);
			cartcount = (TextView) view.findViewById(R.id.subcategories_header_cartcounttext);
			cartcountlayout = (RelativeLayout) view.findViewById(R.id.subcategories_header_cartcountlayout);
			spinner = (Spinner) view.findViewById(R.id.subcategories_header_spinner);
			
		
		// get user data from session
		 HashMap<String, String> user = session.getUserDetails();
		 userid = user.get(SessionManager.KEY_USERID);

		// get user data from session
		 HashMap<String, String> user1 = commonsession.getUserDetails();
		 commonid = user1.get(CommonIDSessionManager.KEY_COMMONID);	
		 
		 
		 Glide.with(context)
 	    .load(R.drawable.loadinganimation)
 	    .asGif()
 	    .crossFade()
 	    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
 	    .into(subcategories_loading);
		 
		 
		 //-------code for Intent---------
		    Intent i = getIntent();
			position = i.getIntExtra("catposition", 0);
			itemid = i.getStringExtra("itemid");
			categoriesspinnerlist = i.getStringArrayListExtra("spinnerlist");
			totalitemid = i.getStringArrayListExtra("totalitemid");
			
			
		//--------code for spinner-------	

		    ArrayAdapter<String> subCategoriesAdapter = new ArrayAdapter<String>(SubCategoriesPage.this,R.layout.spinnertext, categoriesspinnerlist);
			subCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(subCategoriesAdapter);
			spinner.setSelection(position);
			spinner.setOnItemSelectedListener(SubCategoriesPage.this);
			
			
		//code to align grid with equal spacing
			//gridview.setNumColumns(GridView.AUTO_FIT); 
			gridview.setHorizontalSpacing(15); 
			gridview.setVerticalSpacing(15);
			gridview.setPadding(15, 0, 15, 15); 
			gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);	
			
			 gridview.addHeaderView(headerView);
	    	 gridview.addFooterView(footerview);
		
	}
	
	
	//-------CLEAR IMAGE CACHE FROM PHONE SD CARD-----
	  private void clearImageCache()
	  {
			FileCache aa=new FileCache(SubCategoriesPage.this);
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// some variable statements...
		
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
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int arg2,
			long click_id) {
		// TODO Auto-generated method stub
		
		int inposition = spinner.getSelectedItemPosition();
		position = arg2;

		subcategoriesspinnerlist.clear();
		id.clear();
		itemList.clear();
		itemid = totalitemid.get(inposition);
		
			if (isInternetPresent) 
			{
				if(session.isLoggedIn())
				{
					asyntask_name="normal";
					nointernet.setVisibility(View.GONE);
					
					JsonRequest(Iconstant.subcategoriesurl+itemid + "&commonId=" + userid+"&pageId=1");
				}
				else
				{
					asyntask_name="normal";
					nointernet.setVisibility(View.GONE);
					
					JsonRequest(Iconstant.subcategoriesurl+itemid + "&commonId=" + commonid+"&pageId=1");
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
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	
	
		
	
	
	
	
	// -------------------------code for JSon Request----------------------------------
	
				private void JsonRequest(String Url) 
				{
					startload();
					
					System.out.println("--------------subCategories url-------------------"+Url);
					
					 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) 
								{
									System.out.println("--------------subCategories response -----------"+response);

									try {

										JSONArray jarray = response.getJSONArray("categoryDetails");
										
										itemcartcount = response.getString("cartCount");
										Sbannerid=response.getString("bannerId");
										Sbannername=response.getString("bannerName");
										Sbannerimage=response.getString("bannerImage");
										
										session.SetCartCount(itemcartcount);
										
										checkpagepos = Integer.parseInt(response.getString("pagePos"));
										
										if (jarray.length() > 0) 
										{
											id.clear();
											itemList.clear();
											subcategoriesspinnerlist.clear();
											
											id.add(Sbannerid);
											subcategoriesspinnerlist.add(Sbannername);
											
											for (int i = 0; i < jarray.length(); i++) 
											{
												JSONObject object = jarray.getJSONObject(i);
												Subcategories_Pojo items = new Subcategories_Pojo();
												
													items.setSubcat_categoryName(object.getString("categoryName"));
													items.setSubcat_catId(object.getString("catId"));
													items.setSubcat_id(object.getString("id"));
													items.setSubcat_catimage(object.getString("image"));
													
													id.add(object.getString("id"));
													itemList.add(items);
													subcategoriesspinnerlist.add(object.getString("categoryName"));
										    }
											
											show_progress_status=true;
										}
										else
										{
											
											id.clear();
											itemList.clear();
											subcategoriesspinnerlist.clear();
												
											id.add(Sbannerid);
											subcategoriesspinnerlist.add(Sbannername);
											
											show_progress_status=false;
										}
											
									} catch (JSONException e) {
										e.printStackTrace();
									}
									
									
									
									//------on post execute-----
									
									stopload();
									
									if(show_progress_status)
									{
										bannername.setText(Sbannername);
										if(Sbannerimage.length()>0)
										{
											imageLoader.DisplayImage(String.valueOf(Sbannerimage), bannerimage);
										}
										
										if (itemcartcount.length() > 0 && !itemcartcount.equals("0"))
										{
											cartcountlayout.setVisibility(View.VISIBLE);
											cartcount.setText(itemcartcount);
										}
										else
										{
											cartcountlayout.setVisibility(View.GONE);
										}
										
										adapter = new SubcategoriesAdapter(SubCategoriesPage.this,itemList);
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
					
					System.out.println("--------------subCategories loadmore url-------------------"+Url);
					
					loadmore_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) 
								{
									System.out.println("--------------subCategories loadmore response -----------"+response);

									try {

										JSONArray jarray = response.getJSONArray("categoryDetails");
										
										itemcartcount = response.getString("cartCount");
										Sbannerid=response.getString("bannerId");
										Sbannername=response.getString("bannerName");
										Sbannerimage=response.getString("bannerImage");
										
										session.SetCartCount(itemcartcount);
										
										checkpagepos = Integer.parseInt(response.getString("pagePos"));
										
										if (jarray.length() > 0) 
										{
											for (int i = 0; i < jarray.length(); i++) 
											{
												JSONObject object = jarray.getJSONObject(i);
												Subcategories_Pojo items = new Subcategories_Pojo();
												
													items.setSubcat_categoryName(object.getString("categoryName"));
													items.setSubcat_catId(object.getString("catId"));
													items.setSubcat_id(object.getString("id"));
													items.setSubcat_catimage(object.getString("image"));
													
													id.add(object.getString("id"));
													itemList.add(items);
													subcategoriesspinnerlist.add(object.getString("categoryName"));
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
		public boolean onKeyDown(int keyCode, KeyEvent event) 
		{
			if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
				SubCategoriesPage.this.finish();
				SubCategoriesPage.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
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
