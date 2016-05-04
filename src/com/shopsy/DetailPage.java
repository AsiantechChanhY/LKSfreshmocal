package com.shopsy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.material_dialog_library.MaterialDialog;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.MaterialDesign.Progressbar.FloatProgressbarCircular_White;
import com.shopsy.Pojo.DetailPage_Pojo;
import com.shopsy.Pojo.Detail_Reviews_Pojo;
import com.shopsy.Pojo.Detail_Ship_Pojo;
import com.shopsy.Pojo.Detail_shopItems_Pojo;
import com.shopsy.Subclass.ActionBarActivity_Subclass_DetailPage;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.CircularImageView;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.ExpandableHeightGridView;
import com.shopsy.Utils.ExpandableHeightListView;
import com.shopsy.Utils.FileCache;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.DetailPage_GridItemsAdapter;
import com.shopsy.adapter.DetailPage_Review_Adapter;
import com.shopsy.adapter.DetailPage_ShippingAdapter;
import com.squareup.picasso.Picasso;

public class DetailPage extends ActionBarActivity_Subclass_DetailPage implements
		BaseSliderView.OnSliderClickListener, OnClickListener,
		OnItemSelectedListener {
	private Context context;
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();

	private Boolean isInternetPresent = false;
	private ConnectionDetector cd;
	private SessionManager session;
	private CommonIDSessionManager commonsession;
	private String UserId, CommonId, conId = " ", msgId = " ";;
	private String productID = "";

	private RelativeLayout cart;
	private RelativeLayout cartcountlayout;
	private TextView cartcount;
	private ImageView refresh_icon, share_icon;
	private FloatProgressbarCircular_White refresh_progressbar;
	private boolean isheaderRefresh = false;
	private boolean cart_onresume = false;

	private RelativeLayout loading, nointernet, mainlayout;
	private ArrayList<DetailPage_Pojo> itemList;
	private ArrayList<Detail_shopItems_Pojo> shop_itemList;
	private ArrayList<Detail_Reviews_Pojo> review_itemList;
	private ArrayList<Detail_Ship_Pojo> shippingitem;

	private ArrayList<String> itemchoice;
	private ArrayList<String> productimage_array = new ArrayList<String>();
	private ArrayList<String> Quantity = new ArrayList<String>();
	ArrayList<String> policystatuslist = new ArrayList<String>();

	JsonObjectRequest jsonObjReq;

	private String itemcartcount = "";
	private boolean show_progress_status = true;

	private SliderLayout mSlider;

	private ImageView favourite_icon;
	private CircularImageView owner_image;
	private TextView productPrice, ownername, product_title;
	private RelativeLayout color_layout, size_layout;
	private Spinner quantity_spinner, color_spinner, size_spinner;
	private RelativeLayout addtocart_layout, added_tocart_layout;
	private FloatProgressbarCircular_White addtocart_progressbar;
	private TextView color_label, size_label;

	private RelativeLayout review_main_layout, review_sub_layout,
			seeallreview_layout, ask_question;
	private ExpandableHeightListView review_listview;
	private RatingBar review_rating;
	private TextView total_review_count, seeall_review_count;
	private ImageView review_expand_image;
	private int review_count = 0;

	private RelativeLayout overview_main_layout, overview_sub_layout;
	private TextView overview_homemade, overview_materials,
			overview_madeto_order, overview_ship_from, overview_favorite;
	private ImageView overview_expand_image;
	private int overview_count = 0;

	private RelativeLayout item_main_layout, item_sub_layout;
	private TextView item_description;
	private ImageView item_expand_image;
	private int item_count = 0;

	private RelativeLayout shipping_main_layout, shipping_sub_layout;
	private ImageView shipping_expand_image;
	private TextView business_days;
	private ExpandableHeightListView shippig_listview;
	private RelativeLayout policies_layout;
	private int shipping_count = 0;

	private TextView madeBy, grid_shopname;
	private CircularImageView grid_ownerimage;
	private ExpandableHeightGridView gridview;
	private RelativeLayout visitshop_layout;
	private TextView listedon, list_views;

	private DetailPage_GridItemsAdapter grid_adapter;
	private DetailPage_Review_Adapter review_adapter;
	private DetailPage_ShippingAdapter shipping_adapter;

	private ImageView detailpage_loading;

	private String productshareUrl = "", productshareTitle = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailpage);
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		initialize();
		refresh_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isInternetPresent) {
					isheaderRefresh = true;
					Json();
				} else {
					Toast.makeText(DetailPage.this, "No internet connection",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(DetailPage.this, DetailPage.class);
				intent.putExtra("productid", shop_itemList.get(position)
						.getShop_id());
				startActivity(intent);
				overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});
		share_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String shareBody = productshareUrl;
				Intent sharingIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						productshareTitle);
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						shareBody);
				startActivity(Intent
						.createChooser(sharingIntent, "Share using"));
			}
		});

		gridview.setExpanded(true);
		review_listview.setExpanded(true);
		shippig_listview.setExpanded(true);
	}

	private void initialize() {
		cd = new ConnectionDetector(DetailPage.this);
		isInternetPresent = cd.isConnectingToInternet();
		session = new SessionManager(DetailPage.this);
		commonsession = new CommonIDSessionManager(DetailPage.this);
		itemList = new ArrayList<DetailPage_Pojo>();
		shop_itemList = new ArrayList<Detail_shopItems_Pojo>();
		review_itemList = new ArrayList<Detail_Reviews_Pojo>();
		shippingitem = new ArrayList<Detail_Ship_Pojo>();
		itemchoice = new ArrayList<String>();

		// code to set actionBar background
		colorDrawable.setColor(0xff1A237E);
		actionBar.setBackgroundDrawable(colorDrawable);

		// code to disable actionBar title
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ------------------code to add custom menu in action
		// bar--------------------------------------

		actionBar.setDisplayShowCustomEnabled(true);
		View view = getLayoutInflater().inflate(R.layout.detail_page_header,
				null);
		actionBar.setCustomView(view);

		cart = (RelativeLayout) view
				.findViewById(R.id.detailpage_header_cartrelativelayout);
		cartcount = (TextView) view
				.findViewById(R.id.detailpage_header_cartcounttext);
		cartcountlayout = (RelativeLayout) view
				.findViewById(R.id.detailpage_header_cartcountlayout);
		refresh_icon = (ImageView) view
				.findViewById(R.id.detailpage_header_refresh_icon);
		refresh_progressbar = (FloatProgressbarCircular_White) view
				.findViewById(R.id.detailpage_header_refresh_progressbar);
		share_icon = (ImageView) view
				.findViewById(R.id.detailpage_header_share_icon);

		loading = (RelativeLayout) findViewById(R.id.detailpage_loading_layout);
		nointernet = (RelativeLayout) findViewById(R.id.detailpage_nointernet_layout);
		mainlayout = (RelativeLayout) findViewById(R.id.detailpage_main_layout);
		detailpage_loading = (ImageView) findViewById(R.id.detailpage_loading_gif);

		mSlider = (SliderLayout) findViewById(R.id.detailpage_image_slider);
		favourite_icon = (ImageView) findViewById(R.id.detailpage_favourite_image);
		owner_image = (CircularImageView) findViewById(R.id.detailpage_ownerimage);
		productPrice = (TextView) findViewById(R.id.detailpage_productprice);
		ownername = (TextView) findViewById(R.id.detailpage_ownername);
		product_title = (TextView) findViewById(R.id.detailpage_product_title);
		color_label = (TextView) findViewById(R.id.detail_spinner_color_label);
		size_label = (TextView) findViewById(R.id.detail_spinner_size_label);
		color_layout = (RelativeLayout) findViewById(R.id.detail_spinner_color_layout);
		size_layout = (RelativeLayout) findViewById(R.id.detail_spinner_size_layout);
		quantity_spinner = (Spinner) findViewById(R.id.detail_spinner_quantity);
		color_spinner = (Spinner) findViewById(R.id.detail_spinner_color);
		size_spinner = (Spinner) findViewById(R.id.detail_spinner_size);
		addtocart_layout = (RelativeLayout) findViewById(R.id.detail_page_addtocart_layout);
		added_tocart_layout = (RelativeLayout) findViewById(R.id.detail_page_addedtocart_layout);
		addtocart_progressbar = (FloatProgressbarCircular_White) findViewById(R.id.detailpage_addtocart_progressbar);

		review_main_layout = (RelativeLayout) findViewById(R.id.detail_rating_layout_main);
		review_sub_layout = (RelativeLayout) findViewById(R.id.detail_rating_layout_sub);
		seeallreview_layout = (RelativeLayout) findViewById(R.id.detail_page_seeallreview_layout);
		review_rating = (RatingBar) findViewById(R.id.detailpage_ratingBar);
		total_review_count = (TextView) findViewById(R.id.detailpage_reviewcount);
		seeall_review_count = (TextView) findViewById(R.id.detailpage_seeallreview_count_text);
		review_expand_image = (ImageView) findViewById(R.id.detailpage_review_expand_imageView);
		review_listview = (ExpandableHeightListView) findViewById(R.id.detail_page_review_listView);

		overview_main_layout = (RelativeLayout) findViewById(R.id.detail_overview_layout_main);
		overview_sub_layout = (RelativeLayout) findViewById(R.id.detail_overview_layout_sub);
		overview_homemade = (TextView) findViewById(R.id.detail_page_overview_text1);
		overview_materials = (TextView) findViewById(R.id.detail_page_overview_text2);
		overview_madeto_order = (TextView) findViewById(R.id.detail_page_overview_text3);
		overview_ship_from = (TextView) findViewById(R.id.detail_page_overview_text4);
		overview_favorite = (TextView) findViewById(R.id.detail_page_overview_text5);
		overview_expand_image = (ImageView) findViewById(R.id.detailpage_overview_expand_imageView);

		item_main_layout = (RelativeLayout) findViewById(R.id.detail_item_details_layout_main);
		item_sub_layout = (RelativeLayout) findViewById(R.id.detail_item_details_layout_sub);
		item_description = (TextView) findViewById(R.id.detail_page_item_details_text1);
		item_expand_image = (ImageView) findViewById(R.id.detailpage_item_details_expand_imageView);

		shipping_main_layout = (RelativeLayout) findViewById(R.id.detail_shipping_layout_main);
		shipping_sub_layout = (RelativeLayout) findViewById(R.id.detail_shipping_layout_sub);
		policies_layout = (RelativeLayout) findViewById(R.id.detail_page_policies_layout);
		shipping_expand_image = (ImageView) findViewById(R.id.detailpage_shipping_expand_imageView);
		business_days = (TextView) findViewById(R.id.detail_page_readytoship_text);
		shippig_listview = (ExpandableHeightListView) findViewById(R.id.detail_page_paymentmethod_listView);

		madeBy = (TextView) findViewById(R.id.detailpage_madeby_textview);
		grid_ownerimage = (CircularImageView) findViewById(R.id.detailpage_grid_ownerimage);
		grid_shopname = (TextView) findViewById(R.id.detailpage_grid_shopname);
		visitshop_layout = (RelativeLayout) findViewById(R.id.detailpage_visitshop_layout);
		gridview = (ExpandableHeightGridView) findViewById(R.id.detailpage_gridView);
		listedon = (TextView) findViewById(R.id.detailpage_itemwas_listedon_text);
		list_views = (TextView) findViewById(R.id.detailpage_itemwas_view_text);

		ask_question = (RelativeLayout) findViewById(R.id.detailpage_ask_question_layout);

		review_main_layout.setOnClickListener(this);
		overview_main_layout.setOnClickListener(this);
		item_main_layout.setOnClickListener(this);
		shipping_main_layout.setOnClickListener(this);
		visitshop_layout.setOnClickListener(this);
		policies_layout.setOnClickListener(this);
		cart.setOnClickListener(this);
		seeallreview_layout.setOnClickListener(this);
		addtocart_layout.setOnClickListener(this);
		favourite_icon.setOnClickListener(this);
		ask_question.setOnClickListener(this);

		quantity_spinner.setOnItemSelectedListener(this);
		size_spinner.setOnItemSelectedListener(this);
		color_spinner.setOnItemSelectedListener(this);

		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		UserId = user.get(SessionManager.KEY_USERID);

		// get user data from session
		HashMap<String, String> user1 = commonsession.getUserDetails();
		CommonId = user1.get(CommonIDSessionManager.KEY_COMMONID);

		productID = getIntent().getStringExtra("productid");

		// code for quantity spinner
		Quantity.add("Select Quantity");
		Quantity.add("1");
		Quantity.add("2");
		Quantity.add("3");
		Quantity.add("4");
		Quantity.add("5");

		ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this,
				R.layout.detailspinnertext, Quantity);
		dayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		quantity_spinner.setAdapter(dayAdapter);

		Glide.with(context).load(R.drawable.loadinganimation).asGif()
				.crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.into(detailpage_loading);

		Json();

		// code to align grid with equal spacing
		gridview.setNumColumns(GridView.AUTO_FIT);
		gridview.setHorizontalSpacing(20);
		gridview.setVerticalSpacing(20);
		gridview.setPadding(20, 0, 20, 0);
		gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
	}

	@Override
	public void onClick(View v) {

		if (v == review_main_layout) {
			if (!itemList.get(0).getTotalreviewCount().equalsIgnoreCase("0")) {
				if (review_count == 0) {
					review_count = 1;
					LayoutShowing(0, review_sub_layout, review_expand_image);
				} else {
					review_count = 0;
					LayoutShowing(1, review_sub_layout, review_expand_image);
				}
			}
		} else if (v == seeallreview_layout) {
			Intent i1 = new Intent(DetailPage.this, SeeAllReviews.class);
			i1.putExtra("shopId", itemList.get(0).getShopId());
			startActivity(i1);
			overridePendingTransition(R.anim.enter, R.anim.exit);
		} else if (v == overview_main_layout) {
			if (overview_count == 0) {
				overview_count = 1;
				LayoutShowing(0, overview_sub_layout, overview_expand_image);
			} else {
				overview_count = 0;
				LayoutShowing(1, overview_sub_layout, overview_expand_image);
			}
		} else if (v == item_main_layout) {
			if (item_count == 0) {
				item_count = 1;
				LayoutShowing(0, item_sub_layout, item_expand_image);
			} else {
				item_count = 0;
				LayoutShowing(1, item_sub_layout, item_expand_image);
			}
		} else if (v == ask_question) {
			if (session.isLoggedIn()) {
				System.out.println("tiltl----------------"
						+ itemList.get(0).getProductshareTitle());
				System.out.println("urlfor title----------------"
						+ itemList.get(0).getProductshareUrl());
				Intent i1 = new Intent(DetailPage.this, Ask_Quetiion.class);
				i1.putExtra("productname", itemList.get(0).getCompanyname());
				i1.putExtra("producturl", itemList.get(0).getProductName());
				i1.putExtra("userid", UserId);
				i1.putExtra("msgid", msgId);
				i1.putExtra("convid", conId);
				startActivity(i1);
				overridePendingTransition(R.anim.enter, R.anim.exit);
			} else {
				favourit_popup("detail_page", productID);
			}
		}

		else if (v == shipping_main_layout) {
			if (shipping_count == 0) {
				shipping_count = 1;
				LayoutShowing(0, shipping_sub_layout, shipping_expand_image);
			} else {
				shipping_count = 0;
				LayoutShowing(1, shipping_sub_layout, shipping_expand_image);
			}
		} else if (v == visitshop_layout) {
			Intent intent = new Intent(DetailPage.this,
					AllShop_ProductPage.class);
			intent.putExtra("shopurl", itemList.get(0).getVisitshop());
			startActivity(intent);
			overridePendingTransition(R.anim.enter, R.anim.exit);
		} else if (v == policies_layout) {
			if (itemList.get(0).getPolicyStatus().contains("1")) {
				Intent i1 = new Intent(DetailPage.this, ShippingPolicies.class);
				i1.putExtra("companyname", itemList.get(0).getCompanyname()
						.toString());
				i1.putExtra("policy", policystatuslist);
				startActivity(i1);
				overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		} else if (v == cart) {
			Intent intent = new Intent(DetailPage.this, CartPage.class);
			startActivity(intent);
			overridePendingTransition(R.anim.enter, R.anim.exit);
		} else if (v == addtocart_layout) {
			AddtoCart();
		} else if (v == favourite_icon) {
			if (session.isLoggedIn()) {
				if (itemList.get(0).getFavorites_status().equalsIgnoreCase("1")) {
					// code to unfavorite
					favourite_icon.setImageResource(R.drawable.favtwo);
					itemList.get(0).setFavorites_status("0");

					FavoriteAsyncTask favasyntask = new FavoriteAsyncTask();
					favasyntask.execute(Iconstant.heartremoveurl
							+ "product&mode=remove&id=" + productID
							+ "&userId=" + UserId);
				} else {
					// code to unfavorite
					favourite_icon.setImageResource(R.drawable.favredtwo);
					itemList.get(0).setFavorites_status("1");

					FavoriteAsyncTask favasyntask = new FavoriteAsyncTask();
					favasyntask.execute(Iconstant.heartaddurl
							+ "product&mode=add&id=" + productID + "&userId="
							+ UserId);
				}
			} else {
				favourit_popup("detail_page", productID);
			}
		}

	}

	// -------Method for Showing and Hiding layout--------
	private void LayoutShowing(int count, RelativeLayout sub_layout,
			ImageView arrow) {

		if (count == 0) {
			sub_layout.setVisibility(View.VISIBLE);
			arrow.setImageResource(R.drawable.collapse_arrow);
		} else {
			sub_layout.setVisibility(View.GONE);
			arrow.setImageResource(R.drawable.expand_arrow);
		}
	}

	// -------CLEAR IMAGE CACHE FROM PHONE SD CARD-----
	private void clearImageCache() {
		FileCache aa = new FileCache(DetailPage.this);
		aa.clear();
	}

	private void Json() {

		if (isInternetPresent) {
			nointernet.setVisibility(View.GONE);

			if (session.isLoggedIn()) {
				JsonRequest(Iconstant.detailpageurl + productID + "&commonId="
						+ UserId);
			} else {
				JsonRequest(Iconstant.detailpageurl + productID + "&commonId="
						+ CommonId);
			}
		} else {
			mainlayout.setVisibility(View.GONE);
			nointernet.setVisibility(View.VISIBLE);
		}

	}

	// ------Method for AddtoCart---------
	private void AddtoCart() {

		String selected_Qty = quantity_spinner.getSelectedItem().toString();
		String selected_choicedata = "";
		String selected_pricelist = "";

		if (selected_Qty.contains("Select")) {
			Toast.makeText(getApplicationContext(),
					"Please Select an option for Quantity", Toast.LENGTH_SHORT)
					.show();
		} else if (size_layout.getVisibility() == View.VISIBLE
				&& size_spinner.getSelectedItem().toString().contains("Select")) {
			Toast.makeText(
					getApplicationContext(),
					"Please Select an option for "
							+ size_label.getText().toString(),
					Toast.LENGTH_SHORT).show();
		} else if (color_layout.getVisibility() == View.VISIBLE
				&& color_spinner.getSelectedItem().toString()
						.contains("Select")) {
			Toast.makeText(
					getApplicationContext(),
					"Please Select an option for "
							+ color_label.getText().toString(),
					Toast.LENGTH_SHORT).show();
		} else {
			// code to get price
			if (itemchoice.size() > 0) {
				if (itemchoice.get(2).length() > 0) {
					selected_pricelist = itemchoice.get(2).toString()
							.replace(itemList.get(0).getCurrencySymbol(), "");

				} else if (itemchoice.get(5).length() > 0) {
					selected_pricelist = itemchoice.get(5).toString()
							.replace(itemList.get(0).getCurrencySymbol(), "");
				} else {
					selected_pricelist = itemList.get(0).getPrice().toString();
				}
			} else {
				selected_pricelist = itemList.get(0).getPrice().toString();
			}

			// code to get choice list
			if (size_layout.getVisibility() == View.VISIBLE
					&& color_layout.getVisibility() == View.GONE) {
				String type = size_label.getText().toString();
				String choicedata = size_spinner.getSelectedItem().toString();

				selected_choicedata = type + ":" + choicedata;
			} else if (size_layout.getVisibility() == View.GONE
					&& color_layout.getVisibility() == View.VISIBLE) {
				String type = color_label.getText().toString();
				String choicedata = color_spinner.getSelectedItem().toString();

				selected_choicedata = type + ":" + choicedata;
			} else if (size_layout.getVisibility() == View.VISIBLE
					&& color_layout.getVisibility() == View.VISIBLE) {

				String type = size_label.getText().toString();
				String choicedata = size_spinner.getSelectedItem().toString();

				String type1 = color_label.getText().toString();
				String choicedata1 = color_spinner.getSelectedItem().toString();

				selected_choicedata = type + ":" + choicedata + "," + type1
						+ ":" + choicedata1;
			} else {
				selected_choicedata = "";
			}

			if (session.isLoggedIn()) {
				AddToCartAsyncTask asyntask = new AddToCartAsyncTask();
				asyntask.execute(Iconstant.addtocarturl, selected_Qty, UserId,
						selected_pricelist, selected_choicedata);
			} else {
				AddToCartAsyncTask asyntask = new AddToCartAsyncTask();
				asyntask.execute(Iconstant.addtocarturl, selected_Qty,
						CommonId, selected_pricelist, selected_choicedata);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// some variable statements...

		switch (item.getItemId()) {
		case android.R.id.home:

			onBackPressed();
			setResult(RESULT_OK);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}

	// ---------------load animation------------

	private void startload() {
		// ---clear cache of image----
		clearImageCache();

		if (isheaderRefresh) {
			refresh_progressbar.setVisibility(View.VISIBLE);
			refresh_icon.setVisibility(View.GONE);
		} else {
			mainlayout.setVisibility(View.GONE);
			nointernet.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
		}
	}

	private void stopload() {
		if (isheaderRefresh) {
			refresh_progressbar.setVisibility(View.GONE);
			refresh_icon.setVisibility(View.VISIBLE);
		} else {
			mainlayout.setVisibility(View.VISIBLE);
			nointernet.setVisibility(View.GONE);
			loading.setVisibility(View.GONE);
		}
	}

	// -------------------------code for JSon
	// Request----------------------------------

	private void JsonRequest(String Url) {
		startload();

		System.out.println("--------------Detail page url-------------------"
				+ Url);

		jsonObjReq = new JsonObjectRequest(Method.GET, Url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						System.out
								.println("--------------Detail page response -----------"
										+ response);

						try {

							JSONArray jarray = response
									.getJSONArray("detailspage");

							itemcartcount = response.getString("cartCount");
							session.SetCartCount(itemcartcount);

							if (jarray.length() > 0) {
								itemList.clear();
								for (int i = 0; i < jarray.length(); i++) {
									JSONObject object = jarray.getJSONObject(i);
									DetailPage_Pojo items = new DetailPage_Pojo();

									productshareUrl = object
											.getString("productshareUrl");
									productshareTitle = object
											.getString("productshareTitle");

									items.setProductId(object
											.getString("productid"));
									items.setProductName(object
											.getString("productName"));
									items.setProductshareUrl(object
											.getString("productshareUrl"));
									items.setProductshareTitle(object
											.getString("productshareTitle"));
									items.setPrice(object.getString("price"));
									items.setDisplayPrice(object
											.getString("displayPrice"));
									items.setCurrencySymbol(object
											.getString("currencySymbol"));
									items.setCurrencyCode(object
											.getString("currencyCode"));
									items.setFavorites_status(object
											.getString("favStatus"));

									items.setDescription(object
											.getString("description"));
									items.setCompanyname(object
											.getString("companyname"));
									items.setVisitshop(object
											.getString("visitshop"));
									items.setProfile_image(object
											.getString("profile_image"));
									items.setShopId(object.getString("shopId"));
									items.setTotalreviewCount(object
											.getString("totalreviewCount"));
									items.setTotalreview_rating(object
											.getString("reviewAvg"));
									items.setReadytoShip(object
											.getString("readytoShip"));

									items.setPolicyStatus(object
											.getString("policyStatus"));
									items.setListed_on(object
											.getString("listed_on"));
									items.setViews(object.getString("views"));
									items.setFavorites(object
											.getString("favorites"));

									items.setMade_by(object
											.getString("made_by"));
									items.setMaterials(object
											.getString("materials"));
									items.setFeedback(object
											.getString("favorited_by"));
									items.setShip_from(object
											.getString("ship_from"));
									items.setMade_to_order(object
											.getString("made_to_order"));

									JSONArray image = object
											.getJSONArray("image");
									if (image.length() > 0) {
										productimage_array.clear();

										for (int j = 0; j < image.length(); j++) {
											JSONObject image_object = image
													.getJSONObject(j);

											productimage_array.add(image_object
													.getString("image"));
										}
									} else {
										productimage_array.clear();
									}

									JSONArray shopItems_array = object
											.getJSONArray("shopItems");
									if (shopItems_array.length() > 0) {
										shop_itemList.clear();

										for (int k = 0; k < shopItems_array
												.length(); k++) {
											JSONObject shopItems_object = shopItems_array
													.getJSONObject(k);
											Detail_shopItems_Pojo shop_items = new Detail_shopItems_Pojo();

											shop_items
													.setShop_id(shopItems_object
															.getString("id"));
											shop_items
													.setShop_name(shopItems_object
															.getString("name"));
											shop_items
													.setShop_image(shopItems_object
															.getString("image"));
											shop_items.setShop_price(object
													.getString("currencySymbol")
													+ shopItems_object
															.getString("price"));

											shop_itemList.add(shop_items);
										}
									} else {
										shop_itemList.clear();
									}

									JSONArray reviwes = object
											.getJSONArray("reviwes");
									if (shopItems_array.length() > 0) {
										review_itemList.clear();
										for (int l = 0; l < reviwes.length(); l++) {
											JSONObject reviwes_object = reviwes
													.getJSONObject(l);
											Detail_Reviews_Pojo review_items = new Detail_Reviews_Pojo();

											review_items
													.setReviewer_name(reviwes_object
															.getString("reviewername"));
											review_items
													.setReviewer_image(reviwes_object
															.getString("reviewerphoto"));
											review_items
													.setReviewer_time(reviwes_object
															.getString("reviewerdate"));
											review_items
													.setReviewer_comment(reviwes_object
															.getString("reviewercomment"));
											review_items
													.setReviewer_rating(reviwes_object
															.getString("reviewerating"));
											review_items
													.setReviewer_productname(reviwes_object
															.getString("revieweproductName"));
											review_items
													.setReviewer_productimage(reviwes_object
															.getString("reviewProduct"));

											review_itemList.add(review_items);
										}
									} else {
										review_itemList.clear();
									}

									JSONArray policy = object
											.getJSONArray("policy");
									if (policy.length() > 0) {
										policystatuslist.clear();
										for (int k2 = 0; k2 < policy.length(); k2++) {
											JSONObject c3 = policy
													.getJSONObject(k2);

											policystatuslist.add(c3
													.getString("welcome"));
											policystatuslist.add(c3
													.getString("Payment"));
											policystatuslist.add(c3
													.getString("Shipping"));
											policystatuslist.add(c3
													.getString("refund"));
											policystatuslist.add(c3
													.getString("additionalInformation"));
											policystatuslist.add(c3
													.getString("sellerInformation"));
										}
									} else {
										policystatuslist.clear();
									}

									JSONArray choice = object
											.getJSONArray("choice");
									if (choice.length() > 0) {
										itemchoice.clear();
										for (int k1 = 0; k1 < choice.length(); k1++) {
											JSONObject c1 = choice
													.getJSONObject(k1);

											itemchoice.add(c1
													.getString("choicename1"));
											itemchoice.add(c1
													.getString("choicelist1"));
											itemchoice.add(c1
													.getString("choiceprice1"));
											itemchoice.add(c1
													.getString("choicename2"));
											itemchoice.add(c1
													.getString("choicelist2"));
											itemchoice.add(c1
													.getString("choiceprice2"));
										}
									} else {
										itemchoice.clear();
									}

									JSONArray shipping = object
											.getJSONArray("shipping");
									if (shipping.length() > 0) {
										shippingitem.clear();

										for (int k1 = 0; k1 < shipping.length(); k1++) {
											JSONObject c1 = shipping
													.getJSONObject(k1);
											Detail_Ship_Pojo shippojo = new Detail_Ship_Pojo();

											shippojo.setShipto(c1
													.getString("country"));
											shippojo.setCost(object
													.getString("currencySymbol")
													+ c1.getString("cost")
													+ object.getString("currencyCode"));
											shippojo.setWithanother(object
													.getString("currencySymbol")
													+ c1.getString("withother")
													+ object.getString("currencyCode"));

											shippingitem.add(shippojo);
										}
									} else {
										shippingitem.clear();
									}

									JSONObject conversation = object
											.getJSONObject("conversation_details");
									if (conversation.length() > 0) {
										conId = conversation
												.getString("convId");
										msgId = conversation
												.getString("messager_id");
									} else {
										conId = " ";
										msgId = " ";
										// conversation_detail.clear();
									}

									itemList.add(items);
								}

								show_progress_status = true;
							} else {
								itemList.clear();
								show_progress_status = false;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						// ------on post execute-----

						stopload();

						if (show_progress_status) {

							if (itemcartcount.length() > 0
									&& !itemcartcount.equals("0")) {
								cartcountlayout.setVisibility(View.VISIBLE);
								cartcount.setText(itemcartcount);
							} else {
								cartcountlayout.setVisibility(View.GONE);
							}

							// ----code to set owner productPrice,Favorite
							// Status----

							productPrice.setText(itemList.get(0)
									.getCurrencySymbol()
									+ ""
									+ itemList.get(0).getDisplayPrice());
							if (itemList.get(0).getFavorites_status()
									.equalsIgnoreCase("1")) {
								favourite_icon
										.setImageResource(R.drawable.favredtwo);
							} else {
								favourite_icon
										.setImageResource(R.drawable.favtwo);
							}

							// ---code for image slider----

							for (int i = 0; i < productimage_array.size(); i++) {
								HashMap<String, String> productimage_maps = new HashMap<String, String>();

								productimage_maps.put(itemList.get(0)
										.getProductName(), productimage_array
										.get(i));

								for (String name : productimage_maps.keySet()) {

									TextSliderView textSliderView = new TextSliderView(
											DetailPage.this);
									// initialize a SliderLayout
									textSliderView
											.description(name)
											.image(productimage_maps.get(name))
											.setScaleType(
													BaseSliderView.ScaleType.CenterInside)
											.setOnSliderClickListener(
													DetailPage.this);

									// add your extra information
									textSliderView.getBundle().putString(
											"extra", name);
									mSlider.addSlider(textSliderView);
								}
								mSlider.setPresetTransformer(SliderLayout.Transformer.Tablet);
								mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
								mSlider.setCustomAnimation(new DescriptionAnimation());
								mSlider.setDuration(3500);
							}

							// ----code to set owner
							// image,ownerName,productTitle----

							Picasso.with(context)
									.load(String.valueOf(itemList.get(0)
											.getProfile_image()))
									.placeholder(R.drawable.nouserimg)
									.into(owner_image);
							ownername.setText(itemList.get(0).getCompanyname()
									.toUpperCase());
							product_title.setText(itemList.get(0)
									.getProductName());

							// ----code to set choice spinner----

							if (itemchoice.size() > 0) {

								if (itemchoice.get(0).length() > 0) {
									size_layout.setVisibility(View.VISIBLE);
									size_label.setText(itemchoice.get(0)
											.toString());

									String[] spinsplit = itemchoice.get(1)
											.toString().split(",");
									ArrayList<String> arraysplit = new ArrayList<String>();
									arraysplit.add("Select "
											+ itemchoice.get(0).toString());
									for (int i = 0; i < spinsplit.length; i++) {
										arraysplit.add(spinsplit[i]);
									}

									ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(
											DetailPage.this,
											R.layout.detailspinnertext,
											arraysplit);
									dayAdapter
											.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									size_spinner.setAdapter(dayAdapter);
								} else {
									size_layout.setVisibility(View.GONE);
								}

								if (itemchoice.get(3).length() > 0) {
									color_layout.setVisibility(View.VISIBLE);
									color_label.setText(itemchoice.get(3)
											.toString());

									String[] spinsplit = itemchoice.get(4)
											.toString().split(",");
									ArrayList<String> arraysplit = new ArrayList<String>();
									arraysplit.add("Select "
											+ itemchoice.get(3).toString());

									for (int i = 0; i < spinsplit.length; i++) {
										arraysplit.add(spinsplit[i]);
									}

									ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(
											DetailPage.this,
											R.layout.detailspinnertext,
											arraysplit);
									dayAdapter
											.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									color_spinner.setAdapter(dayAdapter);

								} else {
									color_layout.setVisibility(View.GONE);
								}

							}

							// ----code to set rating----
							if (itemList.get(0).getTotalreview_rating()
									.length() > 0) {
								review_rating.setRating(Float
										.parseFloat(itemList.get(0)
												.getTotalreview_rating()));
							}

							// ----code to set rating count----
							total_review_count.setText("("
									+ itemList.get(0).getTotalreviewCount()
									+ " "
									+ context.getResources().getString(
											R.string.detail_label_reviews)
									+ ")");
							seeall_review_count
									.setText(context
											.getResources()
											.getString(
													R.string.detail_label_seeallreviews)
											+ " "
											+ "("
											+ itemList.get(0)
													.getTotalreviewCount()
											+ ")");

							// ----code to set overview Text----
							overview_check(itemList.get(0).getMade_by(),
									overview_homemade);
							overview_check(itemList.get(0).getMaterials(),
									overview_materials);
							overview_check(itemList.get(0).getMade_to_order(),
									overview_madeto_order);
							overview_check(itemList.get(0).getShip_from(),
									overview_ship_from);
							overview_check(itemList.get(0).getFeedback(),
									overview_favorite);

							// ----code to set Description----
							item_description.setText(itemList.get(0)
									.getDescription());

							// ----code to set shipping status----
							business_days.setText(context.getResources()
									.getString(
											R.string.detail_label_readytoship)
									+ " " + itemList.get(0).getMade_to_order());

							// ----code to set Made By Grid----
							madeBy.setText(context.getResources().getString(
									R.string.detail_label_madeby_caps)
									+ " "
									+ itemList.get(0).getCompanyname()
											.toUpperCase());
							grid_shopname.setText(itemList.get(0)
									.getCompanyname().toUpperCase());
							Picasso.with(context)
									.load(String.valueOf(itemList.get(0)
											.getProfile_image()))
									.placeholder(R.drawable.nouserimg)
									.into(grid_ownerimage);
							listedon.setText(context.getResources().getString(
									R.string.detail_label_listedon)
									+ " " + itemList.get(0).getListed_on());
							list_views.setText(itemList.get(0).getViews()
									+ " "
									+ context.getResources().getString(
											R.string.detail_label_views)
									+ " * "
									+ itemList.get(0).getFavorites()
									+ " "
									+ context.getResources().getString(
											R.string.detail_label_favorites));

							// ---------code for shop items adapter----
							grid_adapter = new DetailPage_GridItemsAdapter(
									DetailPage.this, shop_itemList);
							gridview.setAdapter(grid_adapter);

							// ---------code for shop items adapter----
							review_adapter = new DetailPage_Review_Adapter(
									DetailPage.this, review_itemList);
							review_listview.setAdapter(review_adapter);

							// ---------code for shipping adapter----
							shipping_adapter = new DetailPage_ShippingAdapter(
									DetailPage.this, shippingitem);
							shippig_listview.setAdapter(shipping_adapter);

						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						stopload();

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

	// --------------------------code for AddToCart
	// AsynTask-------------------------------
	class AddToCartAsyncTask extends AsyncTask<String, Void, Boolean> {

		String msg = "", response_status = "";
		int status;
		String Response = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			addtocart_layout.setVisibility(View.VISIBLE);
			addtocart_progressbar.setVisibility(View.VISIBLE);
			added_tocart_layout.setVisibility(View.GONE);
		}

		@Override
		protected Boolean doInBackground(String... urls) {

			try {
				HttpClient httpclient = new DefaultHttpClient();
				httpclient.getParams().setParameter(
						CoreProtocolPNames.USER_AGENT, "shopsymobileapp");
				HttpPost httppost = new HttpPost(urls[0]);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						4);
				nameValuePairs.add(new BasicNameValuePair("id", productID));
				nameValuePairs.add(new BasicNameValuePair("qty", urls[1]));
				nameValuePairs.add(new BasicNameValuePair("user", urls[2]));
				nameValuePairs.add(new BasicNameValuePair("price", urls[3]));
				nameValuePairs.add(new BasicNameValuePair("attrVal", urls[4]));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				Response = EntityUtils.toString(entity);
				status = response.getStatusLine().getStatusCode();

				if (status == 200) {
					JSONObject object = new JSONObject(Response);
					response_status = object.getString("status");
					msg = object.getString("message");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			System.out.println("---------Response--------------------"
					+ Response);

			if (response_status.equalsIgnoreCase("0")) {
				final MaterialDialog alertDialog = new MaterialDialog(
						DetailPage.this);
				alertDialog.setTitle("Error");
				alertDialog.setMessage(msg).setCanceledOnTouchOutside(false)
						.setPositiveButton("OK", new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								alertDialog.dismiss();
							}
						}).show();

			} else if (response_status.equalsIgnoreCase("1")) {
				addtocart_layout.setVisibility(View.GONE);
				addtocart_progressbar.setVisibility(View.GONE);
				added_tocart_layout.setVisibility(View.VISIBLE);

				cartcountlayout.setVisibility(View.VISIBLE);
				cartcount.setText(msg);

				// storing cartCount on session
				session.SetCartCount(msg);
			} else {
				Toast.makeText(DetailPage.this, "Server Problem",
						Toast.LENGTH_SHORT).show();
			}
		}
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

	// -------------Method to check overView Data is present or not-------
	private void overview_check(String data, TextView textview) {
		if (data.length() > 0) {
			textview.setVisibility(View.VISIBLE);
			textview.setText("" + data);
		} else {
			textview.setVisibility(View.GONE);
		}
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		addtocart_layout.setVisibility(View.VISIBLE);
		addtocart_progressbar.setVisibility(View.GONE);
		added_tocart_layout.setVisibility(View.GONE);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	// -----------------Move Back on pressed phone back button------------------

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
			DetailPage.this.finish();
			DetailPage.this.overridePendingTransition(
					android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			return true;
		}
		return false;
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
	protected void onStop() {
		// To prevent a memory leak on rotation, make sure to call
		// stopAutoCycle() on the slider before activity or fragment is
		// destroyed
		mSlider.stopAutoCycle();
		super.onStop();
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

		// ---clear cache of image----
		clearImageCache();

		if (jsonObjReq != null) {
			jsonObjReq.cancel();
		}

		super.onDestroy();
	}

}
