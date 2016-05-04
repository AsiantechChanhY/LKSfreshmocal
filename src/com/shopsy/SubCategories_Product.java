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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Pojo.Subcategories_Product_Pojo;
import com.shopsy.StaggeredGridView.StaggeredGridView;
import com.shopsy.Subclass.ActionBarActivity_Subclass_SubCategoriesProduct;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.FileCache;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.SubCategories_ProductAdapter;
import com.shopsy.seekbar.RangeSeekBar;
import com.shopsy.seekbar.RangeSeekBar.OnCursorChangeListener;

public class SubCategories_Product extends ActionBarActivity_Subclass_SubCategoriesProduct implements OnItemSelectedListener
{
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	private SessionManager session;
	private CommonIDSessionManager commonsession;
	private String userid, commonid;
	
	private RelativeLayout cart;
	private RelativeLayout cartcountlayout;
	private TextView cartcount;
	private String itemcartcount="",Stotal_itemCount="";
	private Spinner spinner;
	
	private Context context;
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
	
	private RelativeLayout loading,nointernet,mainlayout;
	private TextView total_itemcount;
	private RelativeLayout filter_layout,sort_layout;
	private StaggeredGridView gridview;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private boolean cart_onresume=false;
	private ImageView subcat_product_loading;
	
	private ArrayList<String> categoriesspinnerlist = new ArrayList<String>();
	private ArrayList<String> subcategoriesspinnerlist = new ArrayList<String>();
	private ArrayList<Subcategories_Product_Pojo> itemList;
	ArrayList<String> totalitemid = new ArrayList<String>();
	private ArrayList<String> productid = new ArrayList<String>();
	
	private SubCategories_ProductAdapter adapter;
	private int checkpagepos = 0;
	private boolean show_progress_status=true;
	private boolean loadingMore = false;
	private String find_loadmore="normal";
	
	JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;

	private String asyntask_name="normal";
	 
	private String catid="";
	private int position=0;
	
	//declaration for FILTER dialog
	private Dialog filter_dialog;
	private RangeSeekBar seekbar;
	private RelativeLayout filter_apply,filter_clear;
	private EditText filter_maxprice,filter_minprice;
	private String Sfilter_maxpricevalue="",Sfilter_minpricevalue="";
	
	//Declaration for sorting
	private Dialog sorting_dialog;
	private RelativeLayout sorting_cancel,sorting_apply,sorting_mostrecent,sorting_highprice,sorting_lowprice;
	private ImageView sorting_checkedone,sorting_checkedtwo,sorting_checkedthree;
	private String Sselected_sorting="";
	
	private ProgressBar loadmore_progressbar;
	private View headerView,footerview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subcategories_product);

		context = getApplicationContext();
		actionBar = getSupportActionBar();

		initialize();
		
		cart.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent(SubCategories_Product.this,CartPage.class);
				startActivity(intent);
				overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});
		
		filter_layout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				filter();
			}
		});
		
		sort_layout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				sorting();
			}
		});
		
		gridview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{
				int new_position=position-1;
				
				Intent intent=new Intent(SubCategories_Product.this,DetailPage.class);
				intent.putExtra("productid", itemList.get(new_position).getSubcatproduct_productId());
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
						if(show_progress_status)
						{
						    ConnectionDetector cd = new ConnectionDetector(SubCategories_Product.this);
							boolean isInternetPresent = cd.isConnectingToInternet();
						
								if (isInternetPresent) 
								{
									if(session.isLoggedIn())
									{
										if(find_loadmore.equalsIgnoreCase("normal"))
										{
											Loadmore_JsonRequest(Iconstant.subcategories_producturl + catid + "&pageId="+(checkpagepos) + "&commonId=" + userid);
										}
										else if(find_loadmore.equalsIgnoreCase("filter"))
										{
											Loadmore_JsonRequest(Iconstant.subcategories_filterurl + (checkpagepos) + "&min_price=" + Sfilter_minpricevalue + "&max_price=" + Sfilter_maxpricevalue + "&catid=" + catid+ "&commonId=" + userid);
										}
										else if(find_loadmore.equalsIgnoreCase("sorting_recent"))
										{
											Loadmore_JsonRequest(Iconstant.subcategories_sortingurl + (checkpagepos) + "&min_price=0&max_price=50050&catid=" + catid + "&order=date_asc"+"&commonId="+userid);
										}
										else if(find_loadmore.equalsIgnoreCase("sorting_price_high"))
										{
											Loadmore_JsonRequest(Iconstant.subcategories_sortingurl + (checkpagepos) + "&min_price=0&max_price=50050&catid=" + catid + "&order=price_desc"+"&commonId="+userid);
										}
										else if(find_loadmore.equalsIgnoreCase("sorting_price_low"))
										{
											Loadmore_JsonRequest(Iconstant.subcategories_sortingurl + (checkpagepos) + "&min_price=0&max_price=50050&catid=" + catid + "&order=price_asc"+"&commonId="+userid);
										}
									}
									else
									{
										if(find_loadmore.equalsIgnoreCase("normal"))
										{
											Loadmore_JsonRequest(Iconstant.subcategories_producturl + catid + "&pageId="+(checkpagepos) + "&commonId=" + commonid);
										}
										else if(find_loadmore.equalsIgnoreCase("filter"))
										{
											Loadmore_JsonRequest(Iconstant.subcategories_filterurl + (checkpagepos) + "&min_price=" + Sfilter_minpricevalue + "&max_price=" + Sfilter_maxpricevalue + "&catid=" + catid+ "&commonId=" + commonid);
										}
										else if(find_loadmore.equalsIgnoreCase("sorting_recent"))
										{
											Loadmore_JsonRequest(Iconstant.subcategories_sortingurl + (checkpagepos) + "&min_price=0&max_price=50050&catid=" + catid + "&order=date_asc"+"&commonId="+commonid);
										}
										else if(find_loadmore.equalsIgnoreCase("sorting_price_high"))
										{
											Loadmore_JsonRequest(Iconstant.subcategories_sortingurl + (checkpagepos) + "&min_price=0&max_price=50050&catid=" + catid + "&order=price_desc"+"&commonId="+commonid);
										}
										else if(find_loadmore.equalsIgnoreCase("sorting_price_low"))
										{
											Loadmore_JsonRequest(Iconstant.subcategories_sortingurl + (checkpagepos) + "&min_price=0&max_price=50050&catid=" + catid + "&order=price_asc"+"&commonId="+commonid);
										}
									}
								} 
								else 
								{
									Toast.makeText(SubCategories_Product.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
			    ConnectionDetector cd = new ConnectionDetector(SubCategories_Product.this);
				boolean isInternetPresent = cd.isConnectingToInternet();
				
						if (isInternetPresent) 
						{
							if(session.isLoggedIn())
							{
								asyntask_name="swipe";
								nointernet.setVisibility(View.GONE);
								actionBar.setDisplayShowCustomEnabled(true);

								JsonRequest(Iconstant.subcategories_producturl + catid + "&pageId=1" + "&commonId=" + userid);
							}
							else
							{
								asyntask_name="swipe";
								nointernet.setVisibility(View.GONE);
								// making hide custom menu and action bar title
								actionBar.setDisplayShowCustomEnabled(true);
								
								JsonRequest(Iconstant.subcategories_producturl + catid + "&pageId=1" + "&commonId=" + commonid);
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
		cd = new ConnectionDetector(SubCategories_Product.this);
		isInternetPresent = cd.isConnectingToInternet();
		session = new SessionManager(SubCategories_Product.this);
		commonsession = new CommonIDSessionManager(SubCategories_Product.this);
		itemList=new ArrayList<Subcategories_Product_Pojo>();
		
		gridview=(StaggeredGridView)findViewById(R.id.subcategory_product_GridView);
		loading=(RelativeLayout)findViewById(R.id.subcategory_product_loading_layout);
		nointernet=(RelativeLayout)findViewById(R.id.subcategory_product_nointernet_layout);
		mainlayout=(RelativeLayout)findViewById(R.id.subcategory_product_main_layout);
		total_itemcount=(TextView)findViewById(R.id.subcategory_product_itemcount_textview);
		filter_layout=(RelativeLayout)findViewById(R.id.subcategory_product_filterlayout);
		sort_layout=(RelativeLayout)findViewById(R.id.subcategory_product_sortlayout);
		subcat_product_loading=(ImageView)findViewById(R.id.subcat_product_loading_gif);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.subcategory_product_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(false);
		
		
		 //code for gridView header
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			headerView = inflater.inflate(R.layout.grid_blankspace_header, null);
			footerview= inflater.inflate(R.layout.gridview_footer_loading, null);
		
			loadmore_progressbar=(ProgressBar)footerview.findViewById(R.id.grid_footer_progressbar);
		
	    // code to set actionBar background
	      	colorDrawable.setColor(0xff1A237E);
	      	actionBar.setBackgroundDrawable(colorDrawable);
		
		// code to disable actionBar title
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
		    .into(subcat_product_loading);
		 
		 
		 //-------code for Intent---------
		    Intent i = getIntent();
			position = i.getIntExtra("catposition", 0);
			catid = i.getStringExtra("itemid");
			categoriesspinnerlist = i.getStringArrayListExtra("spinnerlist");
			totalitemid = i.getStringArrayListExtra("totalitemid");
			
			
		//--------code for spinner-------	

		    ArrayAdapter<String> subCategoriesAdapter = new ArrayAdapter<String>(SubCategories_Product.this,R.layout.spinnertext, categoriesspinnerlist);
			subCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(subCategoriesAdapter);
			spinner.setSelection(position);
			spinner.setOnItemSelectedListener(SubCategories_Product.this);
			
			
		/*//code to align grid with equal spacing
			//gridview.setNumColumns(GridView.AUTO_FIT); 
			gridview.setHorizontalSpacing(13); 
			gridview.setVerticalSpacing(13);
			gridview.setPadding(10, 0, 10, 0); 
			gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);	*/
			
			
			 gridview.addHeaderView(headerView);
	    	 gridview.addFooterView(footerview);
		
	}
	
	//-------CLEAR IMAGE CACHE FROM PHONE SD CARD-----
	  private void clearImageCache()
	  {
			FileCache aa=new FileCache(SubCategories_Product.this);
			aa.clear();
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
	
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int arg2,
			long click_id) {
		// TODO Auto-generated method stub
		
		int inposition = spinner.getSelectedItemPosition();
		position = arg2;

		subcategoriesspinnerlist.clear();
		productid.clear();
		itemList.clear();
		catid = totalitemid.get(inposition);
		
		find_loadmore="normal";
		
			if (isInternetPresent) 
			{
				if(session.isLoggedIn())
				{
					nointernet.setVisibility(View.GONE);
					JsonRequest(Iconstant.subcategories_producturl+catid +"&pageId=1"+ "&commonId=" + userid);
				}
				else
				{
					nointernet.setVisibility(View.GONE);
					JsonRequest(Iconstant.subcategories_producturl+catid +"&pageId=1"+ "&commonId=" + commonid);
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
	
	
	

	
	
	// onClick for add heart
	public void onItemClickheartadd(int position1) 
	{
		if (session.isLoggedIn()) 
		{
			// code to set the status to "true" so that the heart become "red"
			itemList.get(position1).setSubcatproduct_favstatus("1");
			
			FavoriteAsyncTask favasyntask=new FavoriteAsyncTask();
			favasyntask.execute(Iconstant.heartaddurl + "product&mode=add&id="+productid.get(position1)+"&userId="+userid);
		}
		else 
		{
			// showing popup for login
			favourit_popup("subcategories_product",position,catid,categoriesspinnerlist,totalitemid);
		}
	}


	// onClick for Remove heart
	public void onItemClickheartremove(int position1)
	{
		if (session.isLoggedIn()) 
		{
			// code to set the status to "false" so that the heart become "white"
			itemList.get(position1).setSubcatproduct_favstatus("0");

			FavoriteAsyncTask favasyntask=new FavoriteAsyncTask();
			favasyntask.execute(Iconstant.heartremoveurl+"product&mode=remove&id="+productid.get(position1)+"&userId="+userid);
		} 
		else 
		{
			favourit_popup("subcategories_product",position,catid,categoriesspinnerlist,totalitemid);
		}
	}	
	
	
	
	
	private void filter()
	{
		filter_dialog = new Dialog(SubCategories_Product.this);
		filter_dialog.getWindow();
		filter_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		filter_dialog.setContentView(R.layout.subcategories_product_filterdialog);
		filter_dialog.setCanceledOnTouchOutside(true);
		filter_dialog.getWindow().getAttributes().windowAnimations =R.style.Animations_categories_filter;
		filter_dialog.show();
		filter_dialog.getWindow().setGravity(Gravity.CENTER);
		
		seekbar=(RangeSeekBar)filter_dialog.findViewById(R.id.subcategories_filter_seekbar);
		filter_apply=(RelativeLayout)filter_dialog.findViewById(R.id.subcategories_filter_apply);
		filter_clear=(RelativeLayout)filter_dialog.findViewById(R.id.subcategories_filter_clearlayout);
		filter_minprice=(EditText)filter_dialog.findViewById(R.id.subcategories_filter_minprice_editText);
		filter_maxprice=(EditText)filter_dialog.findViewById(R.id.subcategories_filter_maxprice_editText);
		
		Sfilter_minpricevalue="";
		Sfilter_maxpricevalue="";
		
		seekbar.setOnCursorChangeListener(new OnCursorChangeListener()
		{
			@Override
			public void onRightCursorChanged(int location, String maxprice) 
			{
				Sfilter_maxpricevalue=maxprice;
			}
			@Override
			public void onLeftCursorChanged(int location, String minprice) 
			{
				Sfilter_minpricevalue=minprice;
			}
		});
		
		
		filter_apply.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				
				find_loadmore="filter";
				
				if(Sfilter_minpricevalue.length()>0)
				{
				}
				else
				{
					Sfilter_minpricevalue="0";
				}
				
				
				if(Sfilter_maxpricevalue.length()>0)
				{
				}
				else
				{
					Sfilter_maxpricevalue="100000000000";
				}
				
				 ConnectionDetector cd = new ConnectionDetector(SubCategories_Product.this);
				 boolean isInternetPresent = cd.isConnectingToInternet();
				 if (isInternetPresent) 
					{
					 
					  asyntask_name="normal";
					 
					  if (session.isLoggedIn())
						{
								if(filter_minprice.getText().length()>0||filter_maxprice.getText().toString().length()>0)
								{
									JsonRequest(Iconstant.subcategories_filterurl + 1 + "&min_price=" + filter_minprice.getText().toString() + "&max_price=" + filter_maxprice.getText().toString().replace("+", "") + "&catid=" + catid+"&commonId="+userid);
									
									Sfilter_minpricevalue=filter_minprice.getText().toString();
									Sfilter_maxpricevalue=filter_maxprice.getText().toString().replace("+", "");
								}
								else
								{
								//	Sfilter_minpricevalue=filter_minprice.getText().toString();
								//	Sfilter_maxpricevalue=filter_maxprice.getText().toString().replace("+", "");
									JsonRequest(Iconstant.subcategories_filterurl + 1 + "&min_price=" + Sfilter_minpricevalue + "&max_price=" + Sfilter_maxpricevalue.replace("+", "") + "&catid=" + catid+"&commonId="+userid);
								}
						}
						else
						{
							if(filter_minprice.getText().length()>0||filter_maxprice.getText().toString().length()>0)
							{
								JsonRequest(Iconstant.subcategories_filterurl + 1 + "&min_price=" + filter_minprice.getText().toString() + "&max_price=" + filter_maxprice.getText().toString().replace("+", "") + "&catid=" + catid+"&commonId="+commonid);
								System.out.println("1");
								Sfilter_minpricevalue=filter_minprice.getText().toString();
								Sfilter_maxpricevalue=filter_maxprice.getText().toString().replace("+", "");
							}
							else
							{
								System.out.println("2");
							//	Sfilter_minpricevalue=filter_minprice.getText().toString();
							//filter_maxprice.getText().toString().replace("+", "");
								JsonRequest(Iconstant.subcategories_filterurl + 1 + "&min_price=" + Sfilter_minpricevalue + "&max_price=" + Sfilter_maxpricevalue.replace("+", "") + "&catid=" + catid+"&commonId="+commonid);
							}
						}
					}
					else
					{
						Toast.makeText(SubCategories_Product.this, "No internet connection", Toast.LENGTH_SHORT).show();
					}
				
				 filter_dialog.dismiss();
			}
		});
		
		filter_clear.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				filter_minprice.setText("");
				filter_maxprice.setText("");
				seekbar.setLeftSelection(0);
				seekbar.setRightSelection(5);
			}
		});
		
	}
		private void sorting()
	{
		sorting_dialog = new Dialog(SubCategories_Product.this);
		sorting_dialog.getWindow();
		sorting_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		sorting_dialog.setContentView(R.layout.subcategories_product_sortingdialog);
		sorting_dialog.setCanceledOnTouchOutside(true);
		sorting_dialog.getWindow().getAttributes().windowAnimations =R.style.Animations_categories_filter;
		sorting_dialog.show();
		sorting_dialog.getWindow().setGravity(Gravity.CENTER);
		
		sorting_cancel=(RelativeLayout)sorting_dialog.findViewById(R.id.subcategories_sorting_clearlayout);
		sorting_apply=(RelativeLayout)sorting_dialog.findViewById(R.id.subcategories_sorting_apply_layout);
		sorting_mostrecent=(RelativeLayout)sorting_dialog.findViewById(R.id.subcategories_sorting_mostrecent_layout);
		sorting_highprice=(RelativeLayout)sorting_dialog.findViewById(R.id.subcategories_sorting_priceascending_layout);
		sorting_lowprice=(RelativeLayout)sorting_dialog.findViewById(R.id.subcategories_sorting_pricedescending_layout);
		sorting_checkedone=(ImageView)sorting_dialog.findViewById(R.id.subcategories_sorting_checkedone);
		sorting_checkedtwo=(ImageView)sorting_dialog.findViewById(R.id.subcategories_sorting_checkedtwo);
		sorting_checkedthree=(ImageView)sorting_dialog.findViewById(R.id.subcategories_sorting_checkedthree);
		
		sorting_mostrecent.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				sorting_checkedone.setVisibility(View.VISIBLE);
				sorting_checkedtwo.setVisibility(View.GONE);
				sorting_checkedthree.setVisibility(View.GONE);
				
				Sselected_sorting="mostrecent";
			}
		});
		
		sorting_highprice.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				sorting_checkedone.setVisibility(View.GONE);
				sorting_checkedtwo.setVisibility(View.VISIBLE);
				sorting_checkedthree.setVisibility(View.GONE);
				
				Sselected_sorting="highprice";
			}
		});
		
		sorting_lowprice.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				sorting_checkedone.setVisibility(View.GONE);
				sorting_checkedtwo.setVisibility(View.GONE);
				sorting_checkedthree.setVisibility(View.VISIBLE);
				
				Sselected_sorting="lowprice";
			}
		});
		
		
		sorting_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				sorting_dialog.dismiss();
			}
		});
		
		
		sorting_apply.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				sorting_dialog.dismiss();
				
				 ConnectionDetector cd = new ConnectionDetector(SubCategories_Product.this);
				 boolean isInternetPresent = cd.isConnectingToInternet();
				 
				if(isInternetPresent) 
				{
					
					asyntask_name="normal";
					
					if(Sselected_sorting.equalsIgnoreCase("mostrecent"))
					{
						   if (session.isLoggedIn())
							{
							   JsonRequest(Iconstant.subcategories_sortingurl + 1 + "&min_price=0&max_price=50050&catid=" + catid + "&order=date_asc"+"&commonId="+userid);
							}
							else
							{
								JsonRequest(Iconstant.subcategories_sortingurl + 1 + "&min_price=0&max_price=50050&catid=" + catid + "&order=date_asc"+"&commonId="+commonid);
							}
						   
						   find_loadmore="sorting_recent";
					}
					else if(Sselected_sorting.equalsIgnoreCase("highprice"))
					{
						    if(session.isLoggedIn())
							{
						    	JsonRequest(Iconstant.subcategories_sortingurl + 1 + "&min_price=0&max_price=50050&catid=" + catid + "&order=price_desc"+"&commonId="+userid);
							}
							else
							{
								JsonRequest(Iconstant.subcategories_sortingurl + 1 + "&min_price=0&max_price=50050&catid=" + catid + "&order=price_desc"+"&commonId="+commonid);
							}
						    
						    find_loadmore="sorting_price_high";
					}
					else if(Sselected_sorting.equalsIgnoreCase("lowprice"))
					{
						    if(session.isLoggedIn())
							{
						    	JsonRequest(Iconstant.subcategories_sortingurl + 1 + "&min_price=0&max_price=50050&catid=" + catid + "&order=price_asc"+"&commonId="+userid);
							}
							else
							{
								JsonRequest(Iconstant.subcategories_sortingurl + 1 + "&min_price=0&max_price=50050&catid=" + catid + "&order=price_asc"+"&commonId="+commonid);
							}
						    
						    find_loadmore="sorting_price_low";
					}
				}
				else
				{
					Toast.makeText(SubCategories_Product.this, "No internet connection", Toast.LENGTH_SHORT).show();
				}
				
				Sselected_sorting="";
			}
		});
		
	}
	
	
	
	
	
	// -------------------------code for JSon Request----------------------------------
	
	private void JsonRequest(String Url) 
	{
		startload();
		
		System.out.println("--------------subcat product url-------------------"+Url);
		
		 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) 
					{
						System.out.println("--------------subcat product response -----------"+response);

						try {

							JSONArray jarray = response.getJSONArray("productDetails");

							itemcartcount = response.getString("cartCount");
							Stotal_itemCount = response.getString("itemCount");
							checkpagepos = Integer.parseInt(response.getString("pagePos"));

							session.SetCartCount(itemcartcount);
							
							if(jarray.length()>0)
							{
								productid.clear();
								itemList.clear();
								subcategoriesspinnerlist.clear();
								
								for (int i = 0; i < jarray.length(); i++) 
								{
									JSONObject object = jarray.getJSONObject(i);
									Subcategories_Product_Pojo items = new Subcategories_Product_Pojo();

									items.setSubcatproduct_productName(object.getString("productName"));
									items.setSubcatproduct_price(response.getString("currencySymbol")+ object.getString("Price"));
									items.setSubcatproduct_productId(object.getString("productId"));
									items.setSubcatproduct_productImage(object.getString("Image"));
									items.setSubcatproduct_storename(object.getString("storeName"));
									items.setSubcatproduct_favstatus(object.getString("favStatus"));
									
									itemList.add(items);
									productid.add(object.getString("productId"));
								}
								
								show_progress_status=true;
							}
							else
							{
								productid.clear();
								itemList.clear();
								subcategoriesspinnerlist.clear();
								show_progress_status=false;
							}
								
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						
						
						//------on post execute-----
						
						stopload();
						
						if(show_progress_status)
						{
							if(Stotal_itemCount.length()>0)
							{
								total_itemcount.setText(Stotal_itemCount +" "+"items Available");
							}
							else
							{
								total_itemcount.setText("No items available");
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
							
							adapter = new SubCategories_ProductAdapter(SubCategories_Product.this,itemList);
							gridview.setAdapter(adapter);
						}
						else
						{
							total_itemcount.setText("No items available");
							
							adapter = new SubCategories_ProductAdapter(SubCategories_Product.this,itemList);
							gridview.setAdapter(adapter);
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
			
			System.out.println("--------------subcat product loadmore url-------------------"+Url);
			
			loadmore_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) 
						{
							System.out.println("--------------subcat product loadmore response -----------"+response);

							try {

								JSONArray jarray = response.getJSONArray("productDetails");

								itemcartcount = response.getString("cartCount");
								Stotal_itemCount = response.getString("itemCount");
								checkpagepos = Integer.parseInt(response.getString("pagePos"));

								session.SetCartCount(itemcartcount);
								
								if(jarray.length()>0)
								{
									for (int i = 0; i < jarray.length(); i++) 
									{
										JSONObject object = jarray.getJSONObject(i);
										Subcategories_Product_Pojo items = new Subcategories_Product_Pojo();

										items.setSubcatproduct_productName(object.getString("productName"));
										items.setSubcatproduct_price(response.getString("currencySymbol")+ object.getString("Price"));
										items.setSubcatproduct_productId(object.getString("productId"));
										items.setSubcatproduct_productImage(object.getString("Image"));
										items.setSubcatproduct_storename(object.getString("storeName"));
										items.setSubcatproduct_favstatus(object.getString("favStatus"));
										
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
								if(Stotal_itemCount.length()>0)
								{
									total_itemcount.setText(Stotal_itemCount +" "+"items Available");
								}
								else
								{
									total_itemcount.setText("No items available");
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
		
		
		
		//-----------------Move Back on pressed phone back button------------------		
		
				@Override
				public boolean onKeyDown(int keyCode, KeyEvent event) 
				{
					if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
						SubCategories_Product.this.finish();
						SubCategories_Product.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
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
