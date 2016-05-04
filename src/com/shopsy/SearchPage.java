package com.shopsy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
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
import com.shopsy.Pojo.Search_Pojo;
import com.shopsy.Subclass.ActionBarActivity_Subclass_SearchPage;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.FileCache;
import com.shopsy.Utils.GridViewWithHeaderAndFooter;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.SearchAdapter;

public class SearchPage extends ActionBarActivity_Subclass_SearchPage {
	private Context context;
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
	private TextView header_title;
	private RelativeLayout cart;
	private TextView cartcount;
	private RelativeLayout cartcountlayout;
	private String itemcartcount;
	private EditText search_edit;
	private RelativeLayout voice_layout, back_layout;

	private Boolean isInternetPresent = false;
	private ConnectionDetector cd;
	private SessionManager session;
	private CommonIDSessionManager commonsession;
	private String UserId, CommonId;
	private ImageView search_loading;

	private ArrayList<Search_Pojo> itemList;

	JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;

	private String asyntask_name = "normal";
	private ProgressBar loadmore_progressbar;
	private TextView total_item_Available;
	private View headerView, footerview;

	private RelativeLayout loading, nointernet, mainlayout;
	private GridViewWithHeaderAndFooter gridview;
	private SwipeRefreshLayout swipeRefreshLayout = null;

	private int checkpagepos = 0;
	private boolean show_progress_status = true;
	private boolean loadingMore = false;
	private ArrayList<String> productid = new ArrayList<String>();
	private SearchAdapter adapter;
	private String Search_string = "";
	protected static final int RESULT_SPEECH = 1;
	private boolean cart_onresume = false;
	private String itemcount = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchpage);
		context = getApplicationContext();
		setupToolbar();
		initialize();

		back_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(search_edit.getWindowToken(),
						InputMethodManager.RESULT_UNCHANGED_SHOWN);

				onBackPressed();
				setResult(RESULT_OK);
				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
				finish();
			}
		});

		search_edit
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {

							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									search_edit.getWindowToken(),
									InputMethodManager.RESULT_UNCHANGED_SHOWN);
							
							Searchdata(search_edit.getText().toString());
							return true;
						} else if (event != null
								&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									search_edit.getWindowToken(),
									InputMethodManager.RESULT_UNCHANGED_SHOWN);
							
							Searchdata(search_edit.getText().toString());
						}
						return false;
					}
				});

		voice_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
				try {
					startActivityForResult(intent, RESULT_SPEECH);
				} catch (ActivityNotFoundException a) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Ops! Your device doesn't support Speech to Text",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		});
		cart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SearchPage.this, CartPage.class);
				startActivity(intent);
				overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					int new_position = position - 2;
					Intent intent = new Intent(SearchPage.this,
							DetailPage.class);
					intent.putExtra("productid", itemList.get(new_position)
							.getProductId());
					startActivity(intent);
					overridePendingTransition(R.anim.enter, R.anim.exit);
				} catch (Exception e) {
				}
			}
		});

		gridview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				int threshold = 1;
				int count = gridview.getCount();
				if (scrollState == SCROLL_STATE_IDLE) {
					if (gridview.getLastVisiblePosition() >= count - threshold
							&& !(loadingMore)) {

						if (swipeRefreshLayout.isRefreshing()) {
							// nothing happen(code to block loadmore
							// functionality when swipe to refresh is loading)
						} else {

							if (show_progress_status) {

								ConnectionDetector cd = new ConnectionDetector(
										SearchPage.this);
								boolean isInternetPresent = cd
										.isConnectingToInternet();

								if (isInternetPresent) {
									if (session.isLoggedIn()) {
										Loadmore_JsonRequest(Iconstant.search_pageurl
												+ Search_string
												+ "&pageId="
												+ checkpagepos
												+ "&commonId="
												+ UserId);
									} else {
										Loadmore_JsonRequest(Iconstant.search_pageurl
												+ Search_string
												+ "&pageId="
												+ checkpagepos
												+ "&commonId="
												+ CommonId);
									}
								} else {
									Toast.makeText(SearchPage.this,
											"No internet connection",
											Toast.LENGTH_SHORT).show();
								}

							}
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				boolean enable = false;

				if (isInternetPresent) {
					if (gridview != null && gridview.getChildCount() > 0) {
						// check if the first item of the list is visible
						boolean firstItemVisible = gridview
								.getFirstVisiblePosition() == 0;
						// check if the top of the first item is visible
						boolean topOfFirstItemVisible = gridview.getChildAt(0)
								.getTop() == 0;
						// enabling or disabling the refresh layout
						enable = firstItemVisible && topOfFirstItemVisible;
					}
					swipeRefreshLayout.setEnabled(enable);
				} else {
					swipeRefreshLayout.setEnabled(true);
				}
			}
		});

		swipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {

						if (loadingMore) {
							// nothing happen(code to block swipe functionality
							// when loadmore is loading)
							swipeRefreshLayout.setRefreshing(false);
						} else {
							ConnectionDetector cd = new ConnectionDetector(
									SearchPage.this);
							boolean isInternetPresent = cd
									.isConnectingToInternet();

							if (isInternetPresent) {
								if (session.isLoggedIn()) {
									asyntask_name = "swipe";
									nointernet.setVisibility(View.GONE);
									actionBar.setDisplayShowCustomEnabled(true);

									JsonRequest(Iconstant.search_pageurl
											+ Search_string + "&pageId=0"
											+ "&commonId=" + UserId);
								} else {
									asyntask_name = "swipe";
									nointernet.setVisibility(View.GONE);
									actionBar.setDisplayShowCustomEnabled(true);

									JsonRequest(Iconstant.search_pageurl
											+ Search_string + "&pageId=0"
											+ "&commonId=" + CommonId);
								}
							} else {
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

	private void setupToolbar() {

		Toolbar toolbar = (Toolbar) findViewById(R.id.searchpage_custom_toolbar);
		setSupportActionBar(toolbar);

		actionBar = getSupportActionBar();

		// code to set actionBar background
		colorDrawable.setColor(0xff1A237E);
		actionBar.setBackgroundDrawable(colorDrawable);

		// code to disable actionBar title
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(false);

		// ------------------code to add custom menu in action
		// bar--------------------------------------
		actionBar.setDisplayShowCustomEnabled(true);
		View view = SearchPage.this.getLayoutInflater().inflate(
				R.layout.searchpage_header, null);
		actionBar.setCustomView(view);

		cart = (RelativeLayout) view
				.findViewById(R.id.search_header_cartrelativelayout);
		cartcount = (TextView) view
				.findViewById(R.id.search_header_cartcounttext);
		cartcountlayout = (RelativeLayout) view
				.findViewById(R.id.search_header_cartcountlayout);
		header_title = (TextView) view.findViewById(R.id.search_header_title);

		search_edit = (EditText) view
				.findViewById(R.id.search_header_search_editText);
		voice_layout = (RelativeLayout) view
				.findViewById(R.id.search_header_search_mike_layout);
		back_layout = (RelativeLayout) view
				.findViewById(R.id.search_header_backlayout);

		header_title.setText("Product Search");
	}

	@SuppressWarnings("deprecation")
	private void initialize() {
		cd = new ConnectionDetector(SearchPage.this);
		isInternetPresent = cd.isConnectingToInternet();
		session = new SessionManager(SearchPage.this);
		commonsession = new CommonIDSessionManager(SearchPage.this);
		itemList = new ArrayList<Search_Pojo>();

		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		UserId = user.get(SessionManager.KEY_USERID);

		HashMap<String, String> user1 = commonsession.getUserDetails();
		CommonId = user1.get(CommonIDSessionManager.KEY_COMMONID);

		gridview = (GridViewWithHeaderAndFooter) findViewById(R.id.searchpage_GridView);
		loading = (RelativeLayout) findViewById(R.id.searchpage_loading_layout);
		nointernet = (RelativeLayout) findViewById(R.id.searchpage_nointernet_layout);
		mainlayout = (RelativeLayout) findViewById(R.id.searchpage_main_layout);
		search_loading = (ImageView) findViewById(R.id.search_loading_gif);

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.search_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark,
				android.R.color.holo_blue_light,
				android.R.color.holo_green_light,
				android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(true);

		// code for gridView header
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		headerView = inflater.inflate(R.layout.grid_search_header, null);
		footerview = inflater.inflate(R.layout.gridview_footer_loading, null);

		total_item_Available = (TextView) headerView
				.findViewById(R.id.grid_search_header_item_found);
		loadmore_progressbar = (ProgressBar) footerview
				.findViewById(R.id.grid_footer_progressbar);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Searchdata(extras.getString("search_data"));
		}

		// code to align grid with equal spacing
		gridview.setNumColumns(GridView.AUTO_FIT);
		gridview.setHorizontalSpacing(8);
		gridview.setVerticalSpacing(8);
		gridview.setPadding(8, 0, 8, 0);
		gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

		gridview.addHeaderView(headerView);
		gridview.addFooterView(footerview);

		Glide.with(context).load(R.drawable.loadinganimation).asGif()
				.crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.into(search_loading);
	}

	// ---------------AsynTask Method-------------
	private void Searchdata(String data) {

		asyntask_name = "normal";

		String encodedstring = null;
		try {
			encodedstring = URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Search_string = encodedstring;

		if (isInternetPresent) {
			nointernet.setVisibility(View.GONE);

			if (session.isLoggedIn()) {
				JsonRequest(Iconstant.search_pageurl + encodedstring
						+ "&pageId=0" + "&commonId=" + UserId);
			} else {
				JsonRequest(Iconstant.search_pageurl + encodedstring
						+ "&pageId=0" + "&commonId=" + CommonId);
			}
		} else {
			mainlayout.setVisibility(View.GONE);
			nointernet.setVisibility(View.VISIBLE);

			// making hide custom menu and action bar title
			actionBar.setDisplayShowCustomEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);
		}

	}

	// -------CLEAR IMAGE CACHE FROM PHONE SD CARD-----
	private void clearImageCache() {
		FileCache aa = new FileCache(SearchPage.this);
		aa.clear();
	}

	// --------load animation------
	private void startload() {
		// ---clear cache of image----
		clearImageCache();

		if (asyntask_name.equalsIgnoreCase("normal")) {
			mainlayout.setVisibility(View.GONE);
			nointernet.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);

		} else {
			// ---clear cache of image----
			clearImageCache();
			swipeRefreshLayout.setRefreshing(true);
		}
	}

	private void stopload() {

		if (asyntask_name.equalsIgnoreCase("normal")) {
			mainlayout.setVisibility(View.VISIBLE);
			nointernet.setVisibility(View.GONE);
			loading.setVisibility(View.GONE);
		} else {
			mainlayout.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
			swipeRefreshLayout.setRefreshing(false);
		}
	}

	// onClick for add heart
	public void onItemClickheartadd(int position) {
		if (session.isLoggedIn()) {
			// code to set the status to "true" so that the heart become "red"
			itemList.get(position).setFavstatus("1");

			FavoriteAsyncTask favasyntask = new FavoriteAsyncTask();
			favasyntask.execute(Iconstant.heartaddurl + "product&mode=add&id="
					+ productid.get(position) + "&userId=" + UserId);
		} else {
			// showing popUp for login
			favourit_popup("search_page", Search_string);
		}
	}

	// onClick for Remove heart
	public void onItemClickheartremove(int position) {
		if (session.isLoggedIn()) {
			// code to set the status to "false" so that the heart become
			// "white"
			itemList.get(position).setFavstatus("0");

			FavoriteAsyncTask favasyntask = new FavoriteAsyncTask();
			favasyntask.execute(Iconstant.heartremoveurl
					+ "product&mode=remove&id=" + productid.get(position)
					+ "&userId=" + UserId);
		} else {
			// showing popUp for login
			favourit_popup("search_page", Search_string);
		}
	}

	// -------------------------code for JSon
	// Request----------------------------------

	private void JsonRequest(String Url) {
		startload();

		System.out.println("--------------search url-------------------" + Url);

		jsonObjReq = new JsonObjectRequest(Method.GET, Url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						System.out
								.println("--------------search response -----------"
										+ response);

						try {

							JSONArray jarray = response
									.getJSONArray("productDetails");

							itemcount = response.getString("itemCount");
							itemcartcount = response.getString("cartCount");
							checkpagepos = Integer.parseInt(response
									.getString("pagePos"));

							session.SetCartCount(itemcartcount);

							if (jarray.length() > 0) {
								itemList.clear();
								productid.clear();

								for (int i = 0; i < jarray.length(); i++) {
									final JSONObject object = jarray
											.getJSONObject(i);
									final Search_Pojo items = new Search_Pojo();

									items.setProductName(object
											.getString("productName"));
									items.setPrice(response
											.getString("currencySymbol")
											+ object.getString("Price"));
									items.setProductId(object
											.getString("productId"));
									items.setProductImage(object
											.getString("Image"));
									items.setStorename(object
											.getString("storeName"));
									items.setFavstatus(object
											.getString("favStatus"));

									itemList.add(items);
									productid.add(object.getString("productId"));
								}

								show_progress_status = true;
							} else {
								itemList.clear();
								productid.clear();

								show_progress_status = false;
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						// ------on post execute-----

						stopload();

						if (itemcartcount.length() > 0
								&& !itemcartcount.equals("0")) {
							cartcountlayout.setVisibility(View.VISIBLE);
							cartcount.setText(itemcartcount);
						} else {
							cartcountlayout.setVisibility(View.INVISIBLE);
						}

						total_item_Available.setText(itemcount + " "
								+ "Items Found");

						adapter = new SearchAdapter(SearchPage.this, itemList);
						gridview.setAdapter(adapter);

						if (swipeRefreshLayout.isRefreshing()) {
							swipeRefreshLayout.setRefreshing(false);
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						stopload();
						swipeRefreshLayout.setEnabled(true);

						if (swipeRefreshLayout.isRefreshing()) {
							swipeRefreshLayout.setRefreshing(false);
						}

						if (error instanceof TimeoutError
								|| error instanceof NoConnectionError) {
							Toast.makeText(context,
									"Unable to fetch data from server",
									Toast.LENGTH_LONG).show();
						} else if (error instanceof AuthFailureError) {
							// TODO
							Toast.makeText(context, "AuthFailureError",
									Toast.LENGTH_LONG).show();
						} else if (error instanceof ServerError) {
							// TODO
							Toast.makeText(context, "ServerError",
									Toast.LENGTH_LONG).show();
						} else if (error instanceof NetworkError) {

							Toast.makeText(context, "NetworkError",
									Toast.LENGTH_LONG).show();
							// TODO
						} else if (error instanceof ParseError) {
							// TODO
							Toast.makeText(context, "ParseError",
									Toast.LENGTH_LONG).show();
						}
					}
				});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

	// -------------------------code for JSON Request
	// LoadMore----------------------------------

	private void Loadmore_JsonRequest(String Url) {
		if (show_progress_status) {
			loadmore_progressbar.setVisibility(View.VISIBLE);
		}

		loadingMore = true;

		System.out
				.println("--------------search loadmore url-------------------"
						+ Url);

		loadmore_jsonObjReq = new JsonObjectRequest(Method.GET, Url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						System.out
								.println("--------------search more response -----------"
										+ response);

						try {

							JSONArray jarray = response
									.getJSONArray("productDetails");

							itemcount = response.getString("itemCount");
							itemcartcount = response.getString("cartCount");
							checkpagepos = Integer.parseInt(response
									.getString("pagePos"));

							session.SetCartCount(itemcartcount);

							if (jarray.length() > 0) {
								for (int i = 0; i < jarray.length(); i++) {
									final JSONObject object = jarray
											.getJSONObject(i);
									final Search_Pojo items = new Search_Pojo();

									items.setProductName(object
											.getString("productName"));
									items.setPrice(response
											.getString("currencySymbol")
											+ object.getString("Price"));
									items.setProductId(object
											.getString("productId"));
									items.setProductImage(object
											.getString("Image"));
									items.setStorename(object
											.getString("storeName"));
									items.setFavstatus(object
											.getString("favStatus"));

									itemList.add(items);
									productid.add(object.getString("productId"));
								}

								show_progress_status = true;
							} else {
								show_progress_status = false;
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						// ------on post execute-----

						loadingMore = false;
						loadmore_progressbar.setVisibility(View.GONE);

						if (show_progress_status) {
							if (itemcartcount.length() > 0
									&& !itemcartcount.equals("0")) {
								cartcountlayout.setVisibility(View.VISIBLE);
								cartcount.setText(itemcartcount);
							} else {
								cartcountlayout.setVisibility(View.INVISIBLE);
							}
						}

						adapter.notifyDataSetChanged();

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						loadingMore = false;
						loadmore_progressbar.setVisibility(View.GONE);
						swipeRefreshLayout.setEnabled(true);

						if (error instanceof TimeoutError
								|| error instanceof NoConnectionError) {
							Toast.makeText(context,
									"Unable to fetch data from server",
									Toast.LENGTH_LONG).show();
						} else if (error instanceof AuthFailureError) {
							// TODO
							Toast.makeText(context, "AuthFailureError",
									Toast.LENGTH_LONG).show();
						} else if (error instanceof ServerError) {
							// TODO
							Toast.makeText(context, "ServerError",
									Toast.LENGTH_LONG).show();
						} else if (error instanceof NetworkError) {

							Toast.makeText(context, "NetworkError",
									Toast.LENGTH_LONG).show();
							// TODO
						} else if (error instanceof ParseError) {
							// TODO
							Toast.makeText(context, "ParseError",
									Toast.LENGTH_LONG).show();
						}
					}
				});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(loadmore_jsonObjReq);
	}

	// ------------------------------Favorite
	// AsynTask---------------------------------

	class FavoriteAsyncTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... urls) {

			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
					"shopsymobileapp");
			HttpPost httppost = new HttpPost(urls[0].replace("\n", ""));
			try {
				ResponseHandler<String> responsestring = new BasicResponseHandler();
				client.execute(httppost, responsestring);

			} catch (IOException ex2) {
			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
		}
	}

	// -----------------Move Back on pressed phone back button------------------

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
			onBackPressed();
			setResult(RESULT_OK);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			finish();
			return true;
		}
		return false;
	}

	// --------------OnActivity Result for Speech Search----------------------
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				search_edit.setText(text.get(0));
				Searchdata(search_edit.getText().toString());
			}
			break;
		}
		}
	}

	// -----------------------------code for on
	// Resume------------------------------------------

	@Override
	public void onResume() {
		if (cart_onresume) {
			// get cart count from session
			HashMap<String, String> count = session.getCartCount();
			String Scartcount = count.get(SessionManager.KEY_CARTCOUNT);

			if (Scartcount.length() > 0 && !Scartcount.equals("0")) {
				cartcountlayout.setVisibility(View.VISIBLE);
				cartcount.setText(Scartcount);
			} else {
				cartcountlayout.setVisibility(View.INVISIBLE);
			}
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		cart_onresume = true;
		super.onPause();
	}

	// --------------------------code to destroy asynTask when another activity
	// is called---------------------------
	@Override
	public void onDestroy() {
		cart_onresume = false;
		asyntask_name = "normal";

		// ---clear cache of image----
		clearImageCache();

		if (jsonObjReq != null) {
			jsonObjReq.cancel();
		}

		if (loadmore_jsonObjReq != null) {
			loadmore_jsonObjReq.cancel();
		}

		super.onDestroy();
	}
}
