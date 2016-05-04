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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.material_floatbutton_libraryprem.FloatingActionButton;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Pojo.ConversationDetail_Pojo;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.ConversationDetail_Adapter;
import com.squareup.picasso.Picasso;

public class ConversationDetail extends HockeyActivity 
{
	private ArrayList<ConversationDetail_Pojo> itemList;
	private TextView header_title;
	private ImageView header_image;
	
	private RelativeLayout loading,nointernet,mainlayout;
	private ListView listview;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private boolean show_progress_status=true;

	private String ConvId="",Conv_SenderName="",Conv_SenderImage="",type="";
	private String chat_messagerId="";
	
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	
	private Context context;
	private ActionBar actionBar;
	
	private SessionManager session;
	private String userid;
	private View view;
	private ImageView conversation_loading;
	
	JsonObjectRequest jsonObjReq;
	StringRequest postrequest;
	 
	private String asyntask_name="normal";
	private ConversationDetail_Adapter adapter;
	
	//-------Declaration for float button----
	FloatingActionButton fabButton;
	private boolean fab_hide_button=false;
	 
	//------Declaration for Chat Popup dialog-----
	private Dialog chat_dialog;
	private TextView chat_title,chat_send_label;
	private EditText chat_subject,chat_message;
	private RelativeLayout chat_send_layout;
	private ProgressBar chat_progressbar;
		
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation_detailpage);
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		floatbutton(); 
		initialize();
		
		
		
		fabButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				chatPopup();
			}
		});
		
		listview.setOnScrollListener(new OnScrollListener()
		{
			private int mPreviousVisibleItem;
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
				   
				   
				   
					   
					   if (firstVisibleItem > mPreviousVisibleItem) 
					   {
						   
						   if(fab_hide_button)
						   {
							   fabButton.hide(true);
						   }
						   else
						   {
							   fab_hide_button=true;
						   }
		               }
					   else if (firstVisibleItem < mPreviousVisibleItem) 
					   {
		                	 fabButton.show(true);
		               }
		                 mPreviousVisibleItem = firstVisibleItem;
				  
			}
		});
		
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() 
		{
		  @Override
			public void onRefresh() 
	        {
			  
			    ConnectionDetector cd = new ConnectionDetector(ConversationDetail.this);
				boolean isInternetPresent = cd.isConnectingToInternet();
				
						if (isInternetPresent) 
						{
							if(session.isLoggedIn())
							{
								asyntask_name="swipe";
								nointernet.setVisibility(View.GONE);
								actionBar.setDisplayShowCustomEnabled(true);
								
								JsonRequest(Iconstant.conversationDetail_url + userid+ "&commonId=" + userid + "&cID=" + ConvId+ "&pageId=0");
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
		itemList = new ArrayList<ConversationDetail_Pojo>();
		cd = new ConnectionDetector(ConversationDetail.this);
		isInternetPresent = cd.isConnectingToInternet();
		
		session = new SessionManager(ConversationDetail.this);

		listview=(ListView)findViewById(R.id.conversationdetail_listview);
		loading=(RelativeLayout)findViewById(R.id.conversationdetail_loading_layout);
		nointernet=(RelativeLayout)findViewById(R.id.conversationdetail_nointernet_layout);
		mainlayout=(RelativeLayout)findViewById(R.id.conversationdetail_main_layout);
		conversation_loading=(ImageView)findViewById(R.id.conversationdetail_loading_gif);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.conversationdetail_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(true);
	
				
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		
		// code to disable actionBar title
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ------------------code to add custom menu in action bar--------------------------------------
		
		actionBar.setDisplayShowCustomEnabled(true);
		view = View.inflate(context, R.layout.conversationdetail_header, null);
		actionBar.setCustomView(view);

		header_title=(TextView)view.findViewById(R.id.conversation_detail_header_title);
		header_image=(ImageView)view.findViewById(R.id.conversation_detail_header_userimage);
		 
		 Glide.with(context)
		    .load(R.drawable.loadinganimation)
		    .asGif()
		    .crossFade()
		    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
		    .into(conversation_loading);
		 
		 
		//-------code for Intent---------
		    Intent i = getIntent();
		    ConvId = i.getStringExtra("convId");
		    Conv_SenderName = i.getStringExtra("chat_name");
		    Conv_SenderImage = i.getStringExtra("chat_image");
		    type = i.getStringExtra("Type");
		    
		    
		    header_title.setText(Conv_SenderName);	
		    Picasso.with(context).load(String.valueOf(Conv_SenderImage)).placeholder(R.drawable.nouserimg).into(header_image);
		    
		if (isInternetPresent) 
		{
			if (session.isLoggedIn()) 
			{
				nointernet.setVisibility(View.GONE);
				JsonRequest(Iconstant.conversationDetail_url + userid+ "&commonId=" + userid + "&cID=" + ConvId+ "&pageId=0");
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
	
	
	
	private void floatbutton() 
	{

		fabButton=(FloatingActionButton)findViewById(R.id.conversationdetail_fabbutton);
       
		fabButton.hide(false);
	        new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	fabButton.show(true);
	            	fabButton.setShowAnimation(AnimationUtils.loadAnimation(ConversationDetail.this, R.anim.show_from_bottom));
	            	fabButton.setHideAnimation(AnimationUtils.loadAnimation(ConversationDetail.this, R.anim.hide_to_bottom));
	            }
	        }, 300);
	}
	
	
	
	
	private void chatPopup()
	{
		chat_dialog = new Dialog(ConversationDetail.this);
		chat_dialog.getWindow();
		chat_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		chat_dialog.setContentView(R.layout.chat_popup);
		chat_dialog.setCanceledOnTouchOutside(true);
		chat_dialog.getWindow().getAttributes().windowAnimations =R.style.Animations_categories_filter;
		chat_dialog.show();
		chat_dialog.getWindow().setGravity(Gravity.CENTER);
		
		 chat_title=(TextView)chat_dialog.findViewById(R.id.chat_popup_title_textview);
		 chat_send_label=(TextView)chat_dialog.findViewById(R.id.chat_popup_send_label);
		 chat_subject=(EditText)chat_dialog.findViewById(R.id.chat_popup_subject_edittext);
		 chat_message=(EditText)chat_dialog.findViewById(R.id.chat_popup_message_edittext);
		 chat_send_layout=(RelativeLayout)chat_dialog.findViewById(R.id.chat_popup_send);
		 chat_progressbar=(ProgressBar)chat_dialog.findViewById(R.id.chat_popup_progressBar);
		
		 chat_title.setText(Conv_SenderName);
		 
		 chat_send_layout.setOnClickListener(new OnClickListener()
		 {
			@Override
			public void onClick(View v) 
			{
				if(chat_subject.getText().toString().length()==0)
				{
					erroredit(chat_subject,"Subject can't be empty");
				}
				else if(chat_message.getText().toString().length()==0)
				{
					erroredit(chat_message,"Message can't be empty");
				}
				else
				{
					PostRequest(Iconstant.post_conversation_url);
				}
			}
		});
		 
		 
	}
	
	
	//--------------------Code to set error for EditText-----------------------
	
	private void erroredit(EditText editname,String msg)
	{
		Animation shake = AnimationUtils.loadAnimation(ConversationDetail.this, R.anim.shake);
		editname.startAnimation(shake);
		
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.parseColor("#ffffff"));
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(msg);
		ssbuilder.setSpan(fgcspan, 0, msg.length(), 0);
		editname.setError(ssbuilder);
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
			
			fabButton.show(true);
			
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
					
					System.out.println("--------------conversation detail url-------------------"+Url);
					
					 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response) 
								{
									System.out.println("--------------conversation detail response -----------"+response);

									try {

										JSONArray jarray = response.getJSONArray("view_conversation");

										if (jarray.length() > 0) 
										{
											
											for (int i = 0; i < jarray.length(); i++)
											{
												JSONObject object = jarray.getJSONObject(i);
												
												session.SetCartCount(object.getString("cartCount"));
												
												JSONArray conv_array = object.getJSONArray("conv_info");
												if (conv_array.length() > 0) 
												{
													for (int j = 0; j < conv_array.length(); j++)
													{
														JSONObject conv_object = conv_array.getJSONObject(j);
														
														chat_messagerId=conv_object.getString("messager_id");
													}
												}
												
												JSONArray msg_array = object.getJSONArray("messages");
												if (msg_array.length() > 0) 
												{
													itemList.clear();
													for (int k = 0; k < msg_array.length(); k++)
													{
														JSONObject msg_object = msg_array.getJSONObject(k);
														ConversationDetail_Pojo items = new ConversationDetail_Pojo();
														
														items.setMsg_id(msg_object.getString("msg_id"));
														items.setMsg_type(msg_object.getString("msg_type"));
														items.setSubject(msg_object.getString("subject"));
														items.setMessage(msg_object.getString("message"));
														items.setSender_name(msg_object.getString("sender_name"));
														items.setTime(msg_object.getString("time"));
														
														itemList.add(items);
													}
												}
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
									
									//code to refresh Adapter on ConversationPage 
									
									if(type.equalsIgnoreCase("User"))
									{
										ConversationPage.RefreshAdapter();
									}
									else
									{
										ConversationPage_Seller.RefreshAdapter();
									}
									
									
									if(show_progress_status)
									{
										adapter = new ConversationDetail_Adapter(ConversationDetail.this,itemList);
										listview.setAdapter(adapter);
									}
									else
									{
										mainlayout.setVisibility(View.VISIBLE);
										nointernet.setVisibility(View.GONE);
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
									//code to refresh Adapter on ConversationPage 
									ConversationPage.RefreshAdapter();
									
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
				
				
				
				
	 // -------------------------code for Conversation Post Request----------------------------------			

			private void PostRequest(String Url) 
			{
				
				chat_progressbar.setVisibility(View.VISIBLE);
				chat_send_label.setText(getResources().getString(R.string.chat_popup_label_sending));
				chat_dialog.setCanceledOnTouchOutside(false);
					
					 postrequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {

							System.out.println("--------------conversation post reponse-------------------"+response);

							String status="",message="",chat_subject="",chat_message="",messageId="",message_time="";
							try {
								
								JSONObject object=new JSONObject(response);
								
								status = object.getString("status");
								message = object.getString("message");
								chat_subject = object.getString("subject");
								chat_message = object.getString("msg");
								messageId = object.getString("msgId");
								message_time= object.getString("msgTime");
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							chat_dialog.setCanceledOnTouchOutside(true);
							
							if(status.equalsIgnoreCase("1"))
							{
								chat_progressbar.setVisibility(View.GONE);
								chat_send_label.setText(getResources().getString(R.string.chat_popup_label_send_successfully));
								chat_dialog.dismiss();
								
								ConversationDetail_Pojo items = new ConversationDetail_Pojo();
								
								items.setMsg_id(messageId);
								items.setMsg_type("1");
								items.setSubject(chat_subject);
								items.setMessage(chat_message);
								items.setSender_name("");
								items.setTime(message_time);
								
								itemList.add(items);
								
								adapter.notifyDataSetChanged();
							}
							else
							{
								chat_progressbar.setVisibility(View.GONE);
								chat_send_label.setText(message);
							}
			            	
							// close keyboard
							getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
							
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							
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
							
							jsonParams.put("userId", userid);
							jsonParams.put("receiverId", chat_messagerId);
							jsonParams.put("convId", ConvId);
							jsonParams.put("subject", chat_subject.getText().toString());
							jsonParams.put("message", chat_message.getText().toString());
							return jsonParams;
						}
						
					};
					AppController.getInstance().addToRequestQueue(postrequest);  
			}
			
			
			
			
			
			//-----------------Move Back on pressed phone back button------------------		
			
			@Override
			public boolean onKeyDown(int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
					ConversationDetail.this.finish();
					ConversationDetail.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
					return true;
				}
				return false;
			}
						
			
		// --------------------------code to destroy asynTask when another activity is called---------------------------
				@Override
				public void onDestroy() 
				{
					
					asyntask_name="normal";
					
					    if(jsonObjReq!=null)
						{
							jsonObjReq.cancel();
						}

					super.onDestroy();
				}			
}
