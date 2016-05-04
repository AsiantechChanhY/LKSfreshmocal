package com.shopsy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Pojo.CartPojo;
import com.shopsy.Subclass.ActionBarActivity_Subclass_CartPage;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.FileCache;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.CartPage_Adapter;

public class CartPage extends ActionBarActivity_Subclass_CartPage
{
	private Context context;
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	private SessionManager session;
	private CommonIDSessionManager commonsession;
	private String UserId, CommonId;
	
	private ListView listview;
	private RelativeLayout loading,nointernet,mainlayout;
	private ArrayList<CartPojo> itemList;
	private ArrayList<CartPojo> paymentType_List;
	
	ArrayList<ArrayList<CartPojo>> child_list = new ArrayList<ArrayList<CartPojo>>();
	private TextView emptyText;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private boolean show_progress_status=false;
	private ImageView categories_loading,category_empty;
	
	private String asyntask_name="normal";
	JsonObjectRequest jsonObjReq;
	StringRequest postrequest;
	int checkpagepos = 0;
	
	private CartPage_Adapter adapter;
	Dialog updating_dialog;
	Dialog Productdelete_dialog;
	Dialog coupon_dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cartpage);
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		initialize();
		
		
		listview.setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
			
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
				  ConnectionDetector cd = new ConnectionDetector(CartPage.this);
					boolean isInternetPresent = cd.isConnectingToInternet();
					
							if (isInternetPresent) 
							{
								if(session.isLoggedIn())
								{
									asyntask_name="swipe";
									nointernet.setVisibility(View.GONE);

									JsonRequest(Iconstant.cartpage_url + UserId+ "?commonId=" + UserId);
									
									actionBar.setDisplayShowCustomEnabled(true);
								}
								else
								{
									asyntask_name="swipe";
									nointernet.setVisibility(View.GONE);
									
									JsonRequest(Iconstant.cartpage_url + CommonId+ "?commonId=" + CommonId);
									
									// making hide custom menu and action bar title
									actionBar.setDisplayShowCustomEnabled(true);
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
		cd = new ConnectionDetector(CartPage.this);
		isInternetPresent = cd.isConnectingToInternet();
		session = new SessionManager(CartPage.this);
		commonsession = new CommonIDSessionManager(CartPage.this);
		itemList=new ArrayList<CartPojo>();
		paymentType_List=new ArrayList<CartPojo>();
		
		listview=(ListView)findViewById(R.id.cartpage_listView);
		loading=(RelativeLayout)findViewById(R.id.cartpage_loading_layout);
		nointernet=(RelativeLayout)findViewById(R.id.cartpage_nointernet_layout);
		mainlayout=(RelativeLayout)findViewById(R.id.cartpage_main_layout);
		emptyText = (TextView)findViewById(R.id.cartpage_list_empty);
		category_empty = (ImageView)findViewById(R.id.cartpage_list_empty_image);
		
		categories_loading=(ImageView)findViewById(R.id.cartpage_loading_gif);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.cartpage_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(true);
		
		// code to set actionBar background
      	colorDrawable.setColor(0xff1A237E);
      	actionBar.setBackgroundDrawable(colorDrawable);
	
      	// code to disable actionBar title
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ------------------code to add custom menu in action bar--------------------------------------
		
		actionBar.setDisplayShowCustomEnabled(true);
		View view = getLayoutInflater().inflate(R.layout.cart_header, null);
		actionBar.setCustomView(view);
		
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		UserId = user.get(SessionManager.KEY_USERID);

		// get user data from session
		HashMap<String, String> user1 = commonsession.getUserDetails();
		CommonId = user1.get(CommonIDSessionManager.KEY_COMMONID);

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
				JsonRequest(Iconstant.cartpage_url + UserId+ "?commonId=" + UserId);
			}
			else
			{
				nointernet.setVisibility(View.GONE);
				JsonRequest(Iconstant.cartpage_url + CommonId+ "?commonId=" + CommonId);
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
			FileCache aa=new FileCache(CartPage.this);
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
		else if(asyntask_name.equalsIgnoreCase("deleteproduct"))
		{
			updating_dialog = new Dialog(CartPage.this);
			updating_dialog.getWindow();
			updating_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);   
			updating_dialog.setContentView(R.layout.custom_loading_updating);
			updating_dialog.setCanceledOnTouchOutside(false);
			updating_dialog.show();
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
		else if(asyntask_name.equalsIgnoreCase("deleteproduct"))
		{
			updating_dialog.dismiss();
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
	
		
	
	
	//----------------------code for Checkout On click---------------------------
	
		public void onItemClickcheckout(int position,String pay,String note) 
		{
			if (session.isLoggedIn()) 
			{
				ConnectionDetector cd = new ConnectionDetector(CartPage.this);
				boolean isInternetPresent = cd.isConnectingToInternet();
				
				if (isInternetPresent) 
				{
					CheckOutRequest(Iconstant.checkout_url,String.valueOf(position),pay,note);
				} 
				else 
				{
					Toast.makeText(CartPage.this, "No internet connection", Toast.LENGTH_SHORT).show();
				}
			} 
			else
			{
				favourit_popup("cart_page");
			}
		}
		
		//----------------------code for Coupon Code On click---------------------------
		
			public void onItemClickCoupon(final int position,String coupon) 
			{
				if (session.isLoggedIn()) 
				{
					
					System.out.println("----------------inside the click of coupon----------------");
					
					
					coupon_dialog = new Dialog(CartPage.this);
					coupon_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					coupon_dialog.setContentView(R.layout.cart_coupon_dialog);
					
					final EditText coupon_code = (EditText) coupon_dialog.findViewById(R.id.cart_coupon_popup_edittext);
					final TextView tv_apply = (TextView) coupon_dialog.findViewById(R.id.cart_coupon_popup_text_apply);
					TextView tv_cancel = (TextView) coupon_dialog.findViewById(R.id.cart_coupon_popup_text_cancel);
					
					
					if(coupon.length()>0)
					{
						tv_apply.setText(getResources().getString(R.string.cart_label_coupon_remove));
						coupon_code.setText(coupon);
					}
					else
					{
						tv_apply.setText(getResources().getString(R.string.cart_label_coupon_apply));
					}
					
					tv_cancel.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) {
							coupon_dialog.dismiss();
						}
					});
					
					
					tv_apply.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) {
							
							if(tv_apply.getText().toString().equalsIgnoreCase(getResources().getString(R.string.cart_label_coupon_apply)))
							{
								CouponCodeApplyRequest(Iconstant.coupon_add_url,String.valueOf(position),coupon_code.getText().toString());
							}
							else
							{
								CouponCodeRemoveRequest(Iconstant.coupon_remove_url,String.valueOf(position));
							}
						}
					});
					
					coupon_dialog.show();
					
				} 
				else
				{
					favourit_popup("cart_page");
				}
			}
		
		
		//-----------------------code to delete cart product item--------------------------
		
		public void Productdelete(final String selectedcartid) 
		{
			Productdelete_dialog = new Dialog(CartPage.this);
			Productdelete_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			Productdelete_dialog.setContentView(R.layout.cart_delete_item_dialog);
			
			TextView tv_data = (TextView) Productdelete_dialog.findViewById(R.id.cart_delete_popup_text2);
			TextView tv_yes = (TextView) Productdelete_dialog.findViewById(R.id.cart_delete_popup_text_ok);
			TextView tv_no = (TextView) Productdelete_dialog.findViewById(R.id.cart_delete_popup_text_no);
			
			tv_data.setText(getResources().getString(R.string.cart_label_dialog_text));
			
			tv_yes.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					ConnectionDetector cd = new ConnectionDetector(CartPage.this);
					boolean isInternetPresent = cd.isConnectingToInternet();
					
					if (isInternetPresent) 
					{
						Delete_ProductJsonRequest(Iconstant.cartproductremove+selectedcartid);
					} 
					else 
					{
						Toast.makeText(CartPage.this, "No internet connection", Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			tv_no.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					Productdelete_dialog.cancel();	
				}
			});
			
			Productdelete_dialog.show();
		}
		
		
		//-----------------------code to delete cart shop--------------------------
		
				public void Shopdelete(final int position) 
				{
					Productdelete_dialog = new Dialog(CartPage.this);
					Productdelete_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					Productdelete_dialog.setContentView(R.layout.cart_delete_item_dialog);
					
					TextView tv_data = (TextView) Productdelete_dialog.findViewById(R.id.cart_delete_popup_text2);
					TextView tv_yes = (TextView) Productdelete_dialog.findViewById(R.id.cart_delete_popup_text_ok);
					TextView tv_no = (TextView) Productdelete_dialog.findViewById(R.id.cart_delete_popup_text_no);
					
					tv_data.setText(getResources().getString(R.string.cart_label_dialog_shopdelete));
					
					tv_yes.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							ConnectionDetector cd = new ConnectionDetector(CartPage.this);
							boolean isInternetPresent = cd.isConnectingToInternet();
							
							if (isInternetPresent) 
							{
								if(session.isLoggedIn())
								{
									Delete_ProductJsonRequest(Iconstant.cartshopremove+itemList.get(position).getShopId()+"&user="+UserId);
								}
								else
								{
									Delete_ProductJsonRequest(Iconstant.cartshopremove+itemList.get(position).getShopId()+"&user="+CommonId);
								}
								
							} 
							else 
							{
								Toast.makeText(CartPage.this, "No internet connection", Toast.LENGTH_SHORT).show();
							}
						}
					});
					
					tv_no.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							Productdelete_dialog.cancel();	
						}
					});
					
					Productdelete_dialog.show();
				}
				
				
				
		
	
	// -------------------------code for JSon Request----------------------------------
	
		private void JsonRequest(String Url) 
		{
			startload();
			
			System.out.println("--------------cart url-------------------"+Url);
			
			 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) 
						{
							System.out.println("--------------cart response -----------"+response);

							try {

								JSONArray jarray = response.getJSONArray("yourCart");
								//checkpagepos = Integer.parseInt(response.getString("pagePos"));

								if(jarray.length()>0)
								{
									
									itemList.clear();
									child_list.clear();
									
									for (int i = 0; i < jarray.length(); i++) 
									{
										JSONObject object = jarray.getJSONObject(i);
										
										JSONArray item_array = object.getJSONArray("cartItems");
										
										if(item_array.length()>0)
										{
											
											for (int j = 0; j < item_array.length(); j++) 
											{
												ArrayList<CartPojo> child_item = new ArrayList<CartPojo>();
												
												CartPojo items = new CartPojo();
												JSONObject item_object = item_array.getJSONObject(j);

												items.setShopId(item_object.getString("shopId"));
												items.setShopName(item_object.getString("shopName"));
												items.setShopUrl(item_object.getString("shopUrl"));
												items.setShopPaymentMode(item_object.getString("shopPaymentMode"));
												items.setShopSubTotal(item_object.getString("currencySymbol")+ item_object.getString("shopSubTotal"));
												items.setShopTotal(item_object.getString("currencySymbol")+ item_object.getString("shopTotal"));
												items.setShopShipping(item_object.getString("currencySymbol")+ item_object.getString("shopShipping"));
												items.setShopOwnerImage(item_object.getString("shopOwnerImage"));
												items.setCouponCode(item_object.getString("couponCode"));
												items.setShopCartTotal(item_object.getString("currencySymbol")+item_object.getString("shopCartTotal"));
												
												JSONArray product_array = item_object.getJSONArray("products");
												for (int k = 0; k < product_array.length(); k++) 
												{
													CartPojo items1 = new CartPojo();
													JSONObject product_object = product_array.getJSONObject(k);
													
													items1.setCartId(product_object.getString("cartId"));
													items1.setProductId(product_object.getString("productId"));
													items1.setProductChoice(product_object.getString("productChoice"));
													items1.setProductName(product_object.getString("productName"));
													items1.setProductUrl(product_object.getString("productUrl"));
													items1.setProductQty(product_object.getString("productQty"));
													items1.setProductMaxQty(product_object.getString("productMaxQty"));
													items1.setProductUnitPrice(product_object.getString("currencySymbol")+ product_object.getString("productUnitPrice"));
													items1.setShippingCost(product_object.getString("shippingCost"));
													items1.setProductImage(product_object.getString("productImage"));
													items1.setProductTotal(product_object.getString("productTotal"));
													
													child_item.add(items1);
												}
												
												itemList.add(items);
												child_list.add(child_item);
											}
											
											show_progress_status=true;
										}
										else
										{
											show_progress_status=false;
										}
										
									}
									
									
								}
								else
								{
									itemList.clear();
									child_list.clear();
								}
								
								
								
								JSONArray type_array = response.getJSONArray("payment_type");
								if(type_array.length()>0)
								{
									paymentType_List.clear();
									for (int  j= 0; j < type_array.length(); j++) 
									{
										JSONObject object = type_array.getJSONObject(j);
										CartPojo items = new CartPojo();
										
										items.setPayment_type(object.getString("type"));
										
										paymentType_List.add(items);
									}
								}
								else
								{
									paymentType_List.clear();
								}
								
									
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
							
							//------on post execute-----
							
							if(show_progress_status)
							{
								emptyText.setVisibility(View.GONE);
								category_empty.setVisibility(View.GONE);
							}
							else
							{
								mainlayout.setVisibility(View.VISIBLE);
								nointernet.setVisibility(View.GONE);
								//emptyText.setVisibility(View.VISIBLE);
								category_empty.setVisibility(View.VISIBLE);
			                    listview.setEmptyView(emptyText);
								
							}
							
		                    System.out.println("-------------no cart available-------------------"+show_progress_status);

							
							adapter = new CartPage_Adapter(CartPage.this, itemList,child_list,paymentType_List);
							listview.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							
							stopload();
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
	
		
		
		
		 // -------------------------code for Conversation Post Request----------------------------------			

		private void CheckOutRequest(final String Url,final String position,final String pay,final String note) 
		{
			
			final Dialog dialog = new Dialog(CartPage.this);
             dialog.getWindow();
             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);   
             dialog.setContentView(R.layout.custom_loading_dialog);
             dialog.setCanceledOnTouchOutside(false);
             dialog.show();
             
				 postrequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						System.out.println("--------------- checkout Url-----------------"+Url);
						
						System.out.println("--------------- checkout response-----------------"+response);
						
						String[] datasplit = response.split("\\|");
						
						if (response.contains("Success")) {
							Intent i1 = new Intent(getApplicationContext(),PaymentPage.class);
							i1.putExtra("data", datasplit[1]);
							startActivity(i1);
							overridePendingTransition(R.anim.enter, R.anim.exit);
							dialog.dismiss();
						}
						else
						{
							dialog.dismiss();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						
						dialog.dismiss();
						
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
				})  {

					@Override
					protected Map<String, String> getParams() throws AuthFailureError {
						Map<String, String> jsonParams = new HashMap<String, String>();
						
						
						
						System.out.println("----------------UserId--------------"+UserId);
						System.out.println("----------------sellerId--------------"+itemList.get(Integer.parseInt(position)).getShopId());
						System.out.println("----------------payment_value--------------"+pay);
						System.out.println("----------------note--------------"+note);
						
						
						jsonParams.put("userId", UserId);
						jsonParams.put("sellerId", itemList.get(Integer.parseInt(position)).getShopId());
						jsonParams.put("payment_value", pay);
						jsonParams.put("note", note);
						return jsonParams;
					}
					
				};
				AppController.getInstance().addToRequestQueue(postrequest);  
		}
		
		
		
		
		 // -------------------------code for Coupon add Post Request----------------------------------			

		private void CouponCodeApplyRequest(final String Url,final String position,final String Coupon_Code) 
		{
			
			final Dialog dialog = new Dialog(CartPage.this);
            dialog.getWindow();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);   
            dialog.setContentView(R.layout.custom_loading_dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            
				 postrequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						System.out.println("-----------CouponCodeApplyRequest url------------------"+Url);
						System.out.println("-----------CouponCodeApplyRequest response------------------"+response);
						
						JSONObject object;
						try {
							object = new JSONObject(response);
							String response_status=object.getString("status_code");
							String msg=object.getString("msg");
							
							if(response_status.equalsIgnoreCase("1"))
							{
								dialog.dismiss();
								coupon_dialog.dismiss();
								asyntask_name="deleteproduct";
								JsonRequest(Iconstant.cartpage_url + UserId+ "?commonId=" + UserId);
							}
							else
							{
								Toast.makeText(CartPage.this, msg, Toast.LENGTH_SHORT).show();
								dialog.dismiss();
							}
						} catch (JSONException e) {
							
							dialog.dismiss();
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						
						dialog.dismiss();
						
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
				})  {

					@Override
					protected Map<String, String> getParams() throws AuthFailureError {
						Map<String, String> jsonParams = new HashMap<String, String>();
						
						
						System.out.println("-------------------seller id------------------"+itemList.get(Integer.parseInt(position)).getShopId());
						
						jsonParams.put("userId", UserId);
						jsonParams.put("sellerId", itemList.get(Integer.parseInt(position)).getShopId());
						jsonParams.put("shipamount", itemList.get(Integer.parseInt(position)).getShopShipping());
						jsonParams.put("amount", itemList.get(Integer.parseInt(position)).getShopTotal());
						jsonParams.put("code", Coupon_Code);
						return jsonParams;
					}
					
				};
				AppController.getInstance().addToRequestQueue(postrequest);  
		}
		
		
		// -------------------------code for Coupon add Post Request----------------------------------			

				private void CouponCodeRemoveRequest(final String Url,final String position) 
				{
					
					final Dialog dialog = new Dialog(CartPage.this);
		            dialog.getWindow();
		            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);   
		            dialog.setContentView(R.layout.custom_loading_dialog);
		            dialog.setCanceledOnTouchOutside(false);
		            dialog.show();
		            
						 postrequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {

							@Override
							public void onResponse(String response) {

								JSONObject object;
								try {
									object = new JSONObject(response);
									String response_status=object.getString("status_code");
									String msg=object.getString("msg");
									
									if(response_status.equalsIgnoreCase("1"))
									{
										dialog.dismiss();
										coupon_dialog.dismiss();
										asyntask_name="deleteproduct";
										JsonRequest(Iconstant.cartpage_url + UserId+ "?commonId=" + UserId);
									}
									else
									{
										Toast.makeText(CartPage.this, msg, Toast.LENGTH_SHORT).show();
										dialog.dismiss();
									}
								} catch (JSONException e) {
									
									dialog.dismiss();
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								
								dialog.dismiss();
								
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
						})  {

							@Override
							protected Map<String, String> getParams() throws AuthFailureError {
								Map<String, String> jsonParams = new HashMap<String, String>();
								
								jsonParams.put("userId", UserId);
								jsonParams.put("sellerId", itemList.get(Integer.parseInt(position)).getShopId());
								return jsonParams;
							}
							
						};
						AppController.getInstance().addToRequestQueue(postrequest);  
				}
		
		// -------------------------code for JSon Request----------------------------------
		
			private void Delete_ProductJsonRequest(String Url) 
			{
				final Dialog dialog = new Dialog(CartPage.this);
				dialog.getWindow();
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);   
				dialog.setContentView(R.layout.custom_loading_dialog);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
	             
				System.out.println("--------------Delete_Product and shop url-------------------"+Url);
				
				 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) 
							{
								System.out.println("--------------Delete_Product and shop response -----------"+response);
								
								try {
									String status = response.getString("status_code");
									
									if(status.equalsIgnoreCase("Success"))
									{
										dialog.dismiss();
										Productdelete_dialog.dismiss();
										asyntask_name="deleteproduct";
										JsonRequest(Iconstant.cartpage_url + UserId+ "?commonId=" + UserId);
									}
									else
									{
										dialog.dismiss();
										Toast.makeText(context,"Failed to Delete",Toast.LENGTH_LONG).show();
									}
									
								} catch (JSONException e) {
									dialog.dismiss();
									e.printStackTrace();
								}
								
								//------on post execute-----
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) 
							{
								dialog.dismiss();
								 
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
	
		
		// --------------------------code to destroy asynTask when another activity is called---------------------------
				@Override
				public void onDestroy() 
				{
					asyntask_name="normal";
					
					//---clear cache of image----
					 clearImageCache();
					 
					   if(jsonObjReq!=null)
						{
							jsonObjReq.cancel();
						}
					
					super.onDestroy();
				}	
}
