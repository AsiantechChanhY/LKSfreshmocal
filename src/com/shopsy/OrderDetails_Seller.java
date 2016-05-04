package com.shopsy;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Pojo.OrderDetail_Pojo;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.FileCache;
import com.shopsy.Utils.RoundedImageView;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.OrderDetail_Adapter;
import com.squareup.picasso.Picasso;

public class OrderDetails_Seller extends HockeyActivity
{
	private Context context;
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
	private TextView header_title;
	
	private SessionManager session;
	private String userid;
	private String order_Userid="",order_OrderId="";
	
	private ArrayList<OrderDetail_Pojo> itemList;
	private ArrayList<OrderDetail_Pojo> OrderList;
	private ArrayList<OrderDetail_Pojo> PayList;
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	
	private RelativeLayout loading,nointernet,mainlayout;
	private ListView listview;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private boolean order_progress_status=true;
	private boolean pay_progress_status=true;

	private View headerView;
	
	JsonObjectRequest jsonObjReq;
	JsonObjectRequest manage_jsonObjReq;
	 
	private String asyntask_name="normal";
	private ImageView order_loading;
	
	private OrderDetail_Adapter adapter;
	private String itemcartcount;
	
	private TextView TorderDate, TorderId, TorderTotal, TpayStatus, TorderStatus, TuserName,Treshipment_Status;
	private TextView TsubTotal, TcouponDiscount, TgiftDiscount, TshippingCost, Ttax, TgrandTotal;
	private RelativeLayout subTotal_layout, couponDiscount_layout, giftDiscount_layout, shippingCost_layout, tax_layout, grandTotal_layout;
	private RoundedImageView userImage;
	private Button manageOrder,solveDispute;
	private RelativeLayout sendMesage,Header_mainlayout;
	
	//---Declaration for sorting---
		private Dialog manage_dialog;
		private RelativeLayout manage_cancel,manage_apply,manage_shipped,manage_delivered,manage_cancelled;
		private ImageView manage_checkedone,manage_checkedtwo,manage_checkedthree;
		private RelativeLayout manage_loading_layout;
		private String Sselected_manage="";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderdetail_seller);
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		initialize();
		
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() 
		{
		  @Override
			public void onRefresh() 
	        {
			  
			    ConnectionDetector cd = new ConnectionDetector(OrderDetails_Seller.this);
				boolean isInternetPresent = cd.isConnectingToInternet();
				
						if (isInternetPresent) 
						{
							if(session.isLoggedIn())
							{
								asyntask_name="swipe";
								nointernet.setVisibility(View.GONE);
								actionBar.setDisplayShowCustomEnabled(true);
								
								JsonRequest(Iconstant.order_detail_url + userid+"&userId="+order_Userid+"&orderId="+order_OrderId);
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
		
		manageOrder.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				manageOrder();
			}
		});
		
		sendMesage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent(OrderDetails_Seller.this,DisputePage.class);
				intent.putExtra("OrderId", order_OrderId);
				intent.putExtra("SenderName", OrderList.get(0).getUserName());
				intent.putExtra("SenderImage", OrderList.get(0).getUserImage());
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});
	}
	@SuppressWarnings("deprecation")
	private void initialize()
	{
		itemList = new ArrayList<OrderDetail_Pojo>();
		OrderList= new ArrayList<OrderDetail_Pojo>();
		PayList= new ArrayList<OrderDetail_Pojo>();
		cd = new ConnectionDetector(OrderDetails_Seller.this);
		isInternetPresent = cd.isConnectingToInternet();
		
		session = new SessionManager(OrderDetails_Seller.this);
		
		listview=(ListView)findViewById(R.id.order_detail_seller_listview);
		loading=(RelativeLayout)findViewById(R.id.order_detail_seller_loading_layout);
		nointernet=(RelativeLayout)findViewById(R.id.order_detail_seller_nointernet_layout);
		mainlayout=(RelativeLayout)findViewById(R.id.order_detail_seller_main_layout);
		order_loading=(ImageView)findViewById(R.id.order_detail_seller_loading_gif);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.order_detail_seller_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(true);
		
		
		//code for gridView header
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		headerView = inflater.inflate(R.layout.orderdetails_seller_single_header, null);
		
		TorderDate=(TextView)headerView.findViewById(R.id.orderdetail_seller_header_orderon_textview);
		TorderId=(TextView)headerView.findViewById(R.id.orderdetail_seller_header_orderid);
		TpayStatus=(TextView)headerView.findViewById(R.id.orderdetail_seller_header_paid);
		TorderTotal=(TextView)headerView.findViewById(R.id.orderdetail_seller_header_orderTotal_textview);
		TorderStatus=(TextView)headerView.findViewById(R.id.orderdetail_seller_header_orderstatus);
		TuserName=(TextView)headerView.findViewById(R.id.orderdetail_seller_header_buyername);
		userImage=(RoundedImageView)headerView.findViewById(R.id.orderdetail_seller_header_buyerimage);
		manageOrder=(Button)headerView.findViewById(R.id.orderdetail_seller_header_manageOrder);
		solveDispute=(Button)headerView.findViewById(R.id.orderdetail_seller_header_manageOrder_two);
		Treshipment_Status=(TextView)headerView.findViewById(R.id.orderdetail_seller_header_reshipment_status);
		sendMesage=(RelativeLayout)headerView.findViewById(R.id.orderdetail_seller_header_sendmessage_layout);
		Header_mainlayout=(RelativeLayout)headerView.findViewById(R.id.orderdetail_seller_header_mainlayout);
		
		subTotal_layout=(RelativeLayout)headerView.findViewById(R.id.orderdetail_seller_header_itemtotal_layout);
		couponDiscount_layout=(RelativeLayout)headerView.findViewById(R.id.orderdetail_seller_header_coupon_layout);
		giftDiscount_layout=(RelativeLayout)headerView.findViewById(R.id.orderdetail_seller_header_gift_layout);
		shippingCost_layout=(RelativeLayout)headerView.findViewById(R.id.orderdetail_seller_header_shipping_layout);
		tax_layout=(RelativeLayout)headerView.findViewById(R.id.orderdetail_seller_header_tax_layout);
		grandTotal_layout=(RelativeLayout)headerView.findViewById(R.id.orderdetail_seller_header_grandTotal_layout);
		
		TsubTotal=(TextView)headerView.findViewById(R.id.orderdetail_seller_header_itemtotal_textview);
		TcouponDiscount=(TextView)headerView.findViewById(R.id.orderdetail_seller_header_coupon_textview);
		TgiftDiscount=(TextView)headerView.findViewById(R.id.orderdetail_seller_header_gift_textview);
		TshippingCost=(TextView)headerView.findViewById(R.id.orderdetail_seller_header_shipping_textview);
		Ttax=(TextView)headerView.findViewById(R.id.orderdetail_seller_header_tax_textview);
		TgrandTotal=(TextView)headerView.findViewById(R.id.orderdetail_seller_header_grandTotal_textview);
		
		
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		// code to set actionbar background
      	colorDrawable.setColor(0xff1A237E);
      	actionBar.setBackgroundDrawable(colorDrawable);
	
	   // code to disable actionbar title
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		// ------------------code to add custom menu in action bar--------------------------------------
		actionBar.setDisplayShowCustomEnabled(true);
		View view = OrderDetails_Seller.this.getLayoutInflater().inflate(R.layout.openshop_header, null);
		actionBar.setCustomView(view);
		
		header_title=(TextView)view.findViewById(R.id.openshop_header_title);
		header_title.setText(getResources().getString(R.string.order_detail_label_orderDetail));

		Glide.with(context)
		.load(R.drawable.loadinganimation)
		.asGif()
		.crossFade()
		.diskCacheStrategy(DiskCacheStrategy.SOURCE)
		.into(order_loading);
		
		
		
		Intent intent = getIntent();
		order_Userid = intent.getStringExtra("OrderUserID");
		order_OrderId = intent.getStringExtra("OrderID");
		
		// to pass common id when user has not logged in
		
		if (isInternetPresent) 
		{
			if (session.isLoggedIn()) 
			{
				nointernet.setVisibility(View.GONE);
				JsonRequest(Iconstant.order_detail_url + userid+"&userId="+order_Userid+"&orderId="+order_OrderId);
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

		listview.addHeaderView(headerView);
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
	
	//-------CLEAR IMAGE CACHE FROM PHONE SD CARD-----
	  private void clearImageCache()
	  {
			FileCache aa=new FileCache(OrderDetails_Seller.this);
			aa.clear();
	  }
	  
	  
	  private void manageOrder()
	  {
		    manage_dialog = new Dialog(OrderDetails_Seller.this);
			manage_dialog.getWindow();
			manage_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			manage_dialog.setContentView(R.layout.order_detials_manageorder_dialog);
			manage_dialog.setCanceledOnTouchOutside(true);
			manage_dialog.getWindow().getAttributes().windowAnimations =R.style.Animations_categories_filter;
			manage_dialog.show();
			manage_dialog.getWindow().setGravity(Gravity.CENTER);
			
			manage_cancel=(RelativeLayout)manage_dialog.findViewById(R.id.manage_order_dialog_cancellayout);
			manage_apply=(RelativeLayout)manage_dialog.findViewById(R.id.manage_order_dialog_apply_layout);
			manage_shipped=(RelativeLayout)manage_dialog.findViewById(R.id.manage_order_dialog_shipped_layout);
			manage_delivered=(RelativeLayout)manage_dialog.findViewById(R.id.manage_order_dialog_delivered_layout);
			manage_cancelled=(RelativeLayout)manage_dialog.findViewById(R.id.manage_order_dialog_cancelled_layout);
			manage_checkedone=(ImageView)manage_dialog.findViewById(R.id.manage_order_dialog_checkedone);
			manage_checkedtwo=(ImageView)manage_dialog.findViewById(R.id.manage_order_dialog_checkedtwo);
			manage_checkedthree=(ImageView)manage_dialog.findViewById(R.id.manage_order_dialog_checkedthree);
			
			manage_loading_layout=(RelativeLayout)manage_dialog.findViewById(R.id.manage_order_dialog_loading_layout);
			
			
			if(TorderStatus.getText().toString().equalsIgnoreCase("Processed"))
			{
				manage_shipped.setVisibility(View.VISIBLE);
				manage_delivered.setVisibility(View.GONE);
				manage_cancelled.setVisibility(View.VISIBLE);
			}
			else if(TorderStatus.getText().toString().equalsIgnoreCase("shipped"))
			{
				manage_shipped.setVisibility(View.GONE);
				manage_delivered.setVisibility(View.VISIBLE);
				manage_cancelled.setVisibility(View.GONE);
			}
			
			manage_shipped.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					manage_checkedone.setVisibility(View.VISIBLE);
					manage_checkedtwo.setVisibility(View.GONE);
					manage_checkedthree.setVisibility(View.GONE);
					
					Sselected_manage="shipped";
				}
			});
			
			manage_delivered.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					manage_checkedone.setVisibility(View.GONE);
					manage_checkedtwo.setVisibility(View.VISIBLE);
					manage_checkedthree.setVisibility(View.GONE);
					
					Sselected_manage="delivered";
				}
			});
			
			manage_cancelled.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					manage_checkedone.setVisibility(View.GONE);
					manage_checkedtwo.setVisibility(View.GONE);
					manage_checkedthree.setVisibility(View.VISIBLE);
					
					Sselected_manage="cancelled";
				}
			});
			
			
			manage_cancel.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					manage_dialog.dismiss();
				}
			});
			
			manage_apply.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					 ConnectionDetector cd = new ConnectionDetector(OrderDetails_Seller.this);
					 boolean isInternetPresent = cd.isConnectingToInternet();
					 
					if(isInternetPresent) 
					{
						if(Sselected_manage.equalsIgnoreCase("shipped"))
						{
							ManageOrder_JsonRequest(Iconstant.manage_order_url + userid+"&orderId="+order_OrderId+"&status=1","Shipped");
						}
						else if(Sselected_manage.equalsIgnoreCase("delivered"))
						{
							ManageOrder_JsonRequest(Iconstant.manage_order_url + userid+"&orderId="+order_OrderId+"&status=2","Delivered");
						}
						else if(Sselected_manage.equalsIgnoreCase("cancelled"))
						{
							ManageOrder_JsonRequest(Iconstant.manage_order_url + userid+"&orderId="+order_OrderId+"&status=3","Cancelled");
						}
					}
					else
					{
						Toast.makeText(OrderDetails_Seller.this, "No internet connection", Toast.LENGTH_SHORT).show();
					}
					
					Sselected_manage="";
				}
			});
	  }
	  
	  
	  
	  private void ManagerOrder_ButtonChange()
	  {
		  
		  String Processed=getResources().getString(R.string.label_manage_order_Processed);
		  String Notreceivedyet=getResources().getString(R.string.label_manage_order_notreceived_yet);
		  String request_cancel=getResources().getString(R.string.label_manage_order_RequestedCancel);
		  String cancelled=getResources().getString(R.string.label_manage_order_Cancelled);
		  String shipped=getResources().getString(R.string.label_manage_order_Shipped);
		  String delivered=getResources().getString(R.string.label_manage_order_Delivered);
		  String product_received=getResources().getString(R.string.label_manage_order_Productreceived);
		  String reshipment=getResources().getString(R.string.label_manage_order_ReShipment);
		  String refund=getResources().getString(R.string.label_manage_order_Refund);
		  String reship_a_new_product=getResources().getString(R.string.label_manage_order_reship_a_new_product);
		  
		  
		  String manage_order=getResources().getString(R.string.order_detail_label_manageOrder);
		  String Requested_refund=getResources().getString(R.string.label_manage_order_RequestedRefund);
		  String order_cancelled=getResources().getString(R.string.label_manage_order_OrderCancelled);
		  String estimate_delivery_date=getResources().getString(R.string.label_manage_order_EstimedDeliveryDate);
		  String ShippingId=getResources().getString(R.string.label_manage_order_ShippingId);
		 
		  if(OrderList.get(0).getShipping_status().equalsIgnoreCase(Processed) && OrderList.get(0).getReceived_status().equalsIgnoreCase(Notreceivedyet))
		  {
			  manageOrder.setVisibility(View.VISIBLE);
			  solveDispute.setVisibility(View.GONE);
			  Treshipment_Status.setVisibility(View.INVISIBLE);
			  
			  manageOrder.setEnabled(true);
			  manageOrder.setText(manage_order);
			  manageOrder.setBackgroundColor(Color.parseColor("#2BA8C6"));
		  }
		  else if(OrderList.get(0).getShipping_status().equalsIgnoreCase(Processed) && OrderList.get(0).getReceived_status().equalsIgnoreCase(request_cancel))
		  {
			  manageOrder.setVisibility(View.VISIBLE);
			  solveDispute.setVisibility(View.GONE);
			  Treshipment_Status.setVisibility(View.INVISIBLE);
			  
			  manageOrder.setEnabled(false);
			  manageOrder.setText(Requested_refund);
			  manageOrder.setBackgroundColor(Color.parseColor("#F35D00"));
		  }
		  else if(OrderList.get(0).getShipping_status().equalsIgnoreCase(cancelled) && OrderList.get(0).getReceived_status().equalsIgnoreCase(request_cancel))
		  {
			  manageOrder.setVisibility(View.VISIBLE);
			  solveDispute.setVisibility(View.GONE);
			  Treshipment_Status.setVisibility(View.INVISIBLE);
			  
			  manageOrder.setEnabled(false);
			  manageOrder.setText(order_cancelled);
			  manageOrder.setBackgroundColor(Color.parseColor("#F35D00"));
		  }
		  else if(OrderList.get(0).getShipping_status().equalsIgnoreCase(cancelled) && OrderList.get(0).getReceived_status().equalsIgnoreCase(Notreceivedyet))
		  {
			  manageOrder.setVisibility(View.VISIBLE);
			  solveDispute.setVisibility(View.GONE);
			  Treshipment_Status.setVisibility(View.INVISIBLE);
			  
			  manageOrder.setEnabled(true);
			  manageOrder.setText(cancelled);
			  manageOrder.setBackgroundColor(Color.parseColor("#2BA8C6"));
		  }
		  else if(OrderList.get(0).getShipping_status().equalsIgnoreCase(shipped) && OrderList.get(0).getReceived_status().equalsIgnoreCase(Notreceivedyet))
		  {
			  manageOrder.setVisibility(View.VISIBLE);
			  solveDispute.setVisibility(View.GONE);
			  Treshipment_Status.setVisibility(View.INVISIBLE);
			  
			  manageOrder.setEnabled(true);
			  manageOrder.setText(shipped);
			  manageOrder.setBackgroundColor(Color.parseColor("#2BA8C6"));
		  }
		  else if(OrderList.get(0).getShipping_status().equalsIgnoreCase(Processed) && OrderList.get(0).getReceived_status().equalsIgnoreCase(Notreceivedyet))
		  {
			  manageOrder.setVisibility(View.VISIBLE);
			  solveDispute.setVisibility(View.GONE);
			  Treshipment_Status.setVisibility(View.INVISIBLE);
			  
			  manageOrder.setEnabled(true);
			  manageOrder.setText(Processed);
			  manageOrder.setBackgroundColor(Color.parseColor("#2BA8C6"));
		  }
		  else if(OrderList.get(0).getShipping_status().equalsIgnoreCase(delivered) && OrderList.get(0).getReceived_status().equalsIgnoreCase(Notreceivedyet))
		  {
			  manageOrder.setVisibility(View.VISIBLE);
			  solveDispute.setVisibility(View.GONE);
			  Treshipment_Status.setVisibility(View.INVISIBLE);
			  
			  manageOrder.setEnabled(true);
			  manageOrder.setText(delivered);
			  manageOrder.setBackgroundColor(Color.parseColor("#2BA8C6"));
		  }
		  else if(OrderList.get(0).getShipping_status().equalsIgnoreCase(delivered) && OrderList.get(0).getReceived_status().equalsIgnoreCase(product_received))
		  {
			  manageOrder.setVisibility(View.VISIBLE);
			  solveDispute.setVisibility(View.GONE);
			  Treshipment_Status.setVisibility(View.INVISIBLE);
			  
			  manageOrder.setEnabled(true);
			  manageOrder.setText(delivered);
			  manageOrder.setBackgroundColor(Color.parseColor("#2BA8C6"));
		  }
		  else if(OrderList.get(0).getShipping_status().equalsIgnoreCase(delivered) && OrderList.get(0).getReceived_status().equalsIgnoreCase(request_cancel))
		  {
			  manageOrder.setVisibility(View.VISIBLE);
			  solveDispute.setVisibility(View.VISIBLE);
			  Treshipment_Status.setVisibility(View.INVISIBLE);
			  
			  manageOrder.setEnabled(true);
			  manageOrder.setText(manage_order);
			  manageOrder.setBackgroundColor(Color.parseColor("#2BA8C6"));
		  }
		  else if(OrderList.get(0).getShipping_status().equalsIgnoreCase(reshipment) && OrderList.get(0).getReceived_status().equalsIgnoreCase(request_cancel) && OrderList.get(0).getReshipmentDate().equalsIgnoreCase("0000-00-00"))
		  {
			  manageOrder.setVisibility(View.VISIBLE);
			  solveDispute.setVisibility(View.VISIBLE);
			  Treshipment_Status.setVisibility(View.INVISIBLE);
			  
			  manageOrder.setEnabled(true);
			  manageOrder.setText(reship_a_new_product);
			  manageOrder.setBackgroundColor(Color.parseColor("#2BA8C6"));
		  }
		  else if(OrderList.get(0).getShipping_status().equalsIgnoreCase(reshipment) && OrderList.get(0).getReceived_status().equalsIgnoreCase(request_cancel) && !OrderList.get(0).getReshipmentDate().equalsIgnoreCase("0000-00-00"))
		  {
			  manageOrder.setVisibility(View.VISIBLE);
			  solveDispute.setVisibility(View.VISIBLE);
			  Treshipment_Status.setVisibility(View.VISIBLE);
			  
			  manageOrder.setEnabled(true);
			  manageOrder.setText(reshipment);
			  manageOrder.setBackgroundColor(Color.parseColor("#2BA8C6"));
			  Treshipment_Status.setText(estimate_delivery_date+" "+OrderList.get(0).getEstDate()+" "+ShippingId+" "+OrderList.get(0).getReshipId());
		  }
		  else if(OrderList.get(0).getShipping_status().equalsIgnoreCase(refund) && OrderList.get(0).getReceived_status().equalsIgnoreCase(request_cancel))
		  {
			  manageOrder.setVisibility(View.VISIBLE);
			  solveDispute.setVisibility(View.GONE);
			  Treshipment_Status.setVisibility(View.INVISIBLE);
			  
			  manageOrder.setEnabled(false);
			  manageOrder.setText(refund);
			  manageOrder.setBackgroundColor(Color.parseColor("#2BA8C6"));
		  }
		  
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
				nointernet.setVisibility(View.GONE);
				swipeRefreshLayout.setRefreshing(false);
			}
		}
		
		
		// -------------------------code for JSon Request----------------------------------
		
		private void JsonRequest(String Url) 
		{
			startload();
			
			System.out.println("--------------order detail url-------------------"+Url);
			
			 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) 
						{
							System.out.println("--------------order detail response -----------"+response);

							try {

								JSONArray jarray = response.getJSONArray("orderInfo");
								itemcartcount = response.getString("cartCount");
								
								session.SetCartCount(itemcartcount);
								
								if (jarray.length() > 0) 
								{
									OrderList.clear();
									
									for (int i = 0; i < jarray.length(); i++)
									{
										JSONObject object = jarray.getJSONObject(i);
										OrderDetail_Pojo items = new OrderDetail_Pojo();
								        	 
										items.setOrderDate(object.getString("orderDate"));
										items.setOrderId(object.getString("orderId"));
										items.setOrderTotal(response.getString("currencySymbol")+object.getString("orderTotal"));
										items.setOrderStatus(object.getString("orderStatus"));
										items.setPayStatus(object.getString("payStatus"));
										items.setUserId(object.getString("userId"));
										items.setUserName(object.getString("userName"));
										items.setUserImage(object.getString("userImage"));
										items.setShipping_status(object.getString("shipping_status"));
										items.setReceived_status(object.getString("received_status"));
										items.setReshipmentDate(object.getString("reshipmentDate"));
										items.setEstDate(object.getString("estDate"));
										items.setReshipId(object.getString("reshipId"));
										
										OrderList.add(items);
									}
									
									order_progress_status=true;
								}
								else
								{
									OrderList.clear();
									order_progress_status=false;
								}
								
								JSONArray item_array = response.getJSONArray("itemsInfo");
								if (item_array.length() > 0) 
								{
									itemList.clear();
									
									for (int j = 0; j < item_array.length(); j++)
									{
										JSONObject item_object = item_array.getJSONObject(j);
										OrderDetail_Pojo items = new OrderDetail_Pojo();
								        	 
										items.setId(item_object.getString("Id"));
										items.setName(item_object.getString("Name"));
										items.setQuantity(item_object.getString("Quantity"));
										items.setItemTotal(response.getString("currencySymbol")+item_object.getString("ItemTotal"));
										items.setUnitPrice(response.getString("currencySymbol")+item_object.getString("UnitPrice"));
										items.setImage(item_object.getString("Image"));
										
										itemList.add(items);
									}
								}
								else
								{
									itemList.clear();
								}
								
								JSONArray pay_array = response.getJSONArray("paySummary");
								if (pay_array.length() > 0) 
								{
									PayList.clear();
									
									for (int k = 0; k < pay_array.length(); k++)
									{
										JSONObject pay_object = pay_array.getJSONObject(k);
										OrderDetail_Pojo items = new OrderDetail_Pojo();
								        	 
										items.setSubTotal(response.getString("currencySymbol")+pay_object.getString("subTotal"));
										items.setCouponDiscount(response.getString("currencySymbol")+pay_object.getString("couponDiscount"));
										items.setGiftDiscount(response.getString("currencySymbol")+pay_object.getString("giftDiscount"));
										items.setShippingCost(response.getString("currencySymbol")+pay_object.getString("shippingCost"));
										items.setTax(response.getString("currencySymbol")+pay_object.getString("tax"));
										items.setGrandTotal(response.getString("currencySymbol")+pay_object.getString("grandTotal"));
										
										PayList.add(items);
									}
									
									pay_progress_status=true;
								}
								else
								{
									PayList.clear();	
									pay_progress_status=false;
								}
								
									
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
							
							
							//------on post execute-----
							
							stopload();
							
							if(order_progress_status)
							{
								Header_mainlayout.setVisibility(View.VISIBLE);
								
								TorderDate.setText(OrderList.get(0).getOrderDate());
								TorderId.setText(context.getResources().getString(R.string.order_detail_label_order)+" "+"#"+OrderList.get(0).getOrderId());
								TpayStatus.setText(OrderList.get(0).getPayStatus());
								TorderTotal.setText(OrderList.get(0).getOrderTotal());
								TuserName.setText(OrderList.get(0).getUserName());
								Picasso.with(context).load(String.valueOf(OrderList.get(0).getUserImage())).placeholder(R.drawable.nouserimg).into(userImage);
								
								if(OrderList.get(0).getOrderStatus().equalsIgnoreCase("shipped"))
								{
									manageOrder.setVisibility(View.VISIBLE);
									
									TorderStatus.setText(OrderList.get(0).getOrderStatus());
									TorderStatus.setBackgroundColor(Color.parseColor("#802D91"));
								}
								else if(OrderList.get(0).getOrderStatus().equalsIgnoreCase("Processed"))
								{
									manageOrder.setVisibility(View.VISIBLE);
									
									TorderStatus.setText(OrderList.get(0).getOrderStatus());
									TorderStatus.setBackgroundColor(Color.parseColor("#4E4E4E"));
								}
								else
								{
									manageOrder.setVisibility(View.GONE);
									
									TorderStatus.setText(OrderList.get(0).getOrderStatus());
									TorderStatus.setBackgroundColor(Color.parseColor("#4E4E4E"));
								}
								
								ManagerOrder_ButtonChange();
								
							}
							else
							{
								Header_mainlayout.setVisibility(View.GONE);
							}
							
							if(pay_progress_status)
							{
								Header_mainlayout.setVisibility(View.VISIBLE);
								
								DataCheck(subTotal_layout,TsubTotal,PayList.get(0).getSubTotal());
								DataCheck(couponDiscount_layout,TcouponDiscount,PayList.get(0).getCouponDiscount());
								DataCheck(giftDiscount_layout,TgiftDiscount,PayList.get(0).getGiftDiscount());
								DataCheck(shippingCost_layout,TshippingCost,PayList.get(0).getShippingCost());
								DataCheck(tax_layout,Ttax,PayList.get(0).getTax());
								DataCheck(grandTotal_layout,TgrandTotal,PayList.get(0).getGrandTotal());
							}
							else
							{
								Header_mainlayout.setVisibility(View.GONE);
							}
								
								adapter = new OrderDetail_Adapter(OrderDetails_Seller.this,itemList);
								listview.setAdapter(adapter);
							
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
		
		
		//-----code to check the data is available or not----
		private void DataCheck(RelativeLayout layout,TextView textview,String data)
		{
			if(data.length()>0)
			{
				layout.setVisibility(View.VISIBLE);
				textview.setText(data);
			}
			else
			{
				layout.setVisibility(View.GONE);
			}
		}
		
		
		
		
		// -------------------------code for JSon Request----------------------------------
		
		private void ManageOrder_JsonRequest(String Url,final String Sorderstatus) 
		{
			System.out.println("--------------manage order url-------------------"+Url);
			
			 manage_loading_layout.setVisibility(View.VISIBLE);
			 manage_apply.setVisibility(View.GONE);
			 manage_dialog.setCanceledOnTouchOutside(false);
			 manage_cancel.setClickable(false);
			
			 manage_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) 
						{
							System.out.println("--------------manage order response -----------"+response);
							
							try {

							String	Status = response.getString("status");
							String	message = response.getString("message");
							
							if(Status.equalsIgnoreCase("1"))
							{
								OrderList.get(0).setOrderStatus(Sorderstatus);
								
								if(OrderList.get(0).getOrderStatus().equalsIgnoreCase("shipped"))
								{
									manageOrder.setVisibility(View.VISIBLE);
									
									TorderStatus.setText(OrderList.get(0).getOrderStatus());
									TorderStatus.setBackgroundColor(Color.parseColor("#802D91"));
								}
								else if(OrderList.get(0).getOrderStatus().equalsIgnoreCase("Processed"))
								{
									manageOrder.setVisibility(View.VISIBLE);
									
									TorderStatus.setText(OrderList.get(0).getOrderStatus());
									TorderStatus.setBackgroundColor(Color.parseColor("#4E4E4E"));
								}
								else
								{
									manageOrder.setVisibility(View.GONE);
									
									TorderStatus.setText(OrderList.get(0).getOrderStatus());
									TorderStatus.setBackgroundColor(Color.parseColor("#4E4E4E"));
								}
								
								
								ManagerOrder_ButtonChange();
								
								Toast.makeText(OrderDetails_Seller.this, message, Toast.LENGTH_SHORT).show();
							}
							else
							{
								Toast.makeText(OrderDetails_Seller.this, message, Toast.LENGTH_SHORT).show();
							}
									
							} catch (JSONException e) {
								e.printStackTrace();
							}
							 manage_loading_layout.setVisibility(View.GONE);
							 manage_apply.setVisibility(View.VISIBLE);
							 manage_dialog.setCanceledOnTouchOutside(true);
							 manage_cancel.setClickable(true);
							 manage_dialog.dismiss();							
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) 
						{
							
							 manage_loading_layout.setVisibility(View.GONE);
							 manage_apply.setVisibility(View.VISIBLE);
							 manage_dialog.setCanceledOnTouchOutside(true);
							 manage_cancel.setClickable(true);
							 manage_dialog.dismiss();
							
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
			AppController.getInstance().addToRequestQueue(manage_jsonObjReq);
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

				if(manage_jsonObjReq!=null)
				{
					manage_jsonObjReq.cancel();
				}
			
			super.onDestroy();
		}	
	

}

