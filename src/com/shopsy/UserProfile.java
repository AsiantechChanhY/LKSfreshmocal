package com.shopsy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import android.widget.ImageView;
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
import com.shopsy.Pojo.UserProfile_Pojo;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.RoundedImageView;
import com.shopsy.Utils.SessionManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class UserProfile extends HockeyFragment
{
	private Context context;
	private ActionBar actionBar;
	
	private RelativeLayout loading,nointernet,mainlayout;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private boolean show_progress_status=true;
	private boolean show_favitem_status=true;
	private boolean show_favshop_status=true;
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	
	private ArrayList<UserProfile_Pojo> itemList;
	private SessionManager session;
	private String UserId,UserName;
	private View view;
	private ImageView userprofile_loading;
	
	private RelativeLayout cart;
	private TextView cartcount;
	private TextView header_title;
	private RelativeLayout cartcountlayout;
	private String itemcartcount;
	
	JsonObjectRequest jsonObjReq;
	private String asyntask_name="normal";
	private boolean cart_onresume=false;
	
	private RoundedImageView userImage;
	private TextView Tv_username,Tv_followers,Tv_followings,Tv_gender,Tv_joined;
	private ImageView fav_shopImage1,fav_shopImage2,fav_shopImage3;
	private ImageView fav_itemImage1,fav_itemImage2,fav_itemImage3;
	private TextView fav_itemCount;
	private ImageView shopimage;
	private TextView shopname;
	private RelativeLayout seeAllShop_Layout;
	private RelativeLayout fav_item_layout,fav_shop_layout;
	
	private String Sfav_itemImageUrl1="",Sfav_itemImageUrl2="",Sfav_itemImageUrl3="",Sfav_shopImageUrl1="",Sfav_shopImageUrl2="",Sfav_shopImageUrl3="";
	private String Sfav_itemcount="";
	private String Sfav_shopTotalcount="0";
	private String Sfav_shopownerimageUrl="",Sfav_shopname="",Sfav_shopname1="";
	private int Screenwidth=0;
	private boolean isDataPresent=false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View rootview = inflater.inflate(R.layout.userprofile, container,false);

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
		
		
		fav_shop_layout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				System.out.println("shop name"+Sfav_shopname1);
				Intent intent = new Intent(getActivity(),AllShop_ProductPage.class);
				intent.putExtra("shopurl", Sfav_shopname1);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
			}
		});
		
		
		seeAllShop_Layout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(getActivity(),SeeAllFavoriteShop.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
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

									JsonRequest(Iconstant.userProfile_url + UserName + "?commonId=" + UserId);
									
									actionBar.setDisplayShowCustomEnabled(true);
								}
								else
								{
									asyntask_name="swipe";
									
									nointernet.setVisibility(View.GONE);
									mainlayout.setVisibility(View.GONE);
									
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
		
		return rootview;
	}

	@SuppressWarnings("deprecation")
	private void initialize(View rootview) 
	{
		itemList = new ArrayList<UserProfile_Pojo>();
		cd = new ConnectionDetector(getActivity());
		isInternetPresent = cd.isConnectingToInternet();
		
		session = new SessionManager(getActivity());

		loading=(RelativeLayout)rootview.findViewById(R.id.userprofile_loading_layout);
		nointernet=(RelativeLayout)rootview.findViewById(R.id.userprofile_nointernet_layout);
		mainlayout=(RelativeLayout)rootview.findViewById(R.id.userprofile_main_layout);
		userprofile_loading=(ImageView)rootview.findViewById(R.id.userprofile_loading_gif);
		
		userImage=(RoundedImageView)rootview.findViewById(R.id.userprofile_userimage);
		Tv_username=(TextView)rootview.findViewById(R.id.userprofile_username);
		Tv_followers=(TextView)rootview.findViewById(R.id.userprofile_follower_count);
		Tv_followings=(TextView)rootview.findViewById(R.id.userprofile_following_count);
		Tv_gender=(TextView)rootview.findViewById(R.id.userprofile_about_gender);
		Tv_joined=(TextView)rootview.findViewById(R.id.userprofile_about_joined_date);
		
		fav_itemImage1=(ImageView)rootview.findViewById(R.id.userprofile_favoriteitem_imageview1);
		fav_itemImage2=(ImageView)rootview.findViewById(R.id.userprofile_favoriteitem_imageview2);
		fav_itemImage3=(ImageView)rootview.findViewById(R.id.userprofile_favoriteitem_imageview3);
		
		fav_shopImage1=(ImageView)rootview.findViewById(R.id.userprofile_favoriteshop_imageview1);
		fav_shopImage2=(ImageView)rootview.findViewById(R.id.userprofile_favoriteshop_imageview2);
		fav_shopImage3=(ImageView)rootview.findViewById(R.id.userprofile_favoriteshop_imageview3);
		
		fav_itemCount=(TextView)rootview.findViewById(R.id.userprofile_favoriteitem_itemlove_textview);
		shopname=(TextView)rootview.findViewById(R.id.userprofile_favoriteshop_shopname);
		shopimage=(ImageView)rootview.findViewById(R.id.userprofile_favoriteshop_shopimage);
		seeAllShop_Layout=(RelativeLayout)rootview.findViewById(R.id.userprofile_seeallreview_layout);
		fav_item_layout=(RelativeLayout)rootview.findViewById(R.id.userprofile_favoriteitem_mainlayout);
		fav_shop_layout=(RelativeLayout)rootview.findViewById(R.id.userprofile_favoriteshop_mainlayout);
		
		swipeRefreshLayout = (SwipeRefreshLayout)rootview.findViewById(R.id.userprofile_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(true);
		
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		UserId = user.get(SessionManager.KEY_USERID);
		
		try {
			UserName = URLEncoder.encode(user.get(SessionManager.KEY_USERNAME), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
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

		header_title.setText(UserName);				
		
		 
		 Glide.with(context)
		    .load(R.drawable.loadinganimation)
		    .asGif()
		    .crossFade()
		    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
		    .into(userprofile_loading);
		 
		 
		   // to pass common id when user has not logged in
			
					if (isInternetPresent) 
					{
						if(session.isLoggedIn())
						{
							nointernet.setVisibility(View.GONE);
							JsonRequest(Iconstant.userProfile_url + UserName + "?commonId=" + UserId);
						}
						else
						{
							nointernet.setVisibility(View.GONE);
							mainlayout.setVisibility(View.GONE);
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
					
					
					
					
					int displayWidth = getActivity().getWindowManager().getDefaultDisplay().getHeight();
					Screenwidth=(displayWidth/6);
					System.out.println("---------displayWidth----------------"+Screenwidth);
		
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
				
				System.out.println("--------------userprofile url-------------------"+Url);
				
				 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) 
							{
								System.out.println("--------------userprofile response -----------"+response);

								try {

									JSONArray jarray = response.getJSONArray("userProfile");
									itemcartcount = response.getString("cartCount");
									
									session.SetCartCount(itemcartcount);
									
									if (jarray.length() > 0) 
									{
										
										for (int i = 0; i < jarray.length(); i++)
										{
											JSONObject object = jarray.getJSONObject(i);
											
											JSONArray userInfo_array = object.getJSONArray("userInfo");
											
											Sfav_shopTotalcount=object.getString("favShopCount");
											
											if (userInfo_array.length() > 0) 
											{
												itemList.clear();
												
												for (int j = 0; j < userInfo_array.length(); j++)
												{
													JSONObject userInfo_object = userInfo_array.getJSONObject(j);
													
													UserProfile_Pojo items = new UserProfile_Pojo();
													
										            items.setUsername(userInfo_object.getString("user_name"));
													items.setUserFullname(userInfo_object.getString("userFullName"));
													items.setUserimage(userInfo_object.getString("userImage"));
													items.setUserlocation(userInfo_object.getString("userLocation"));
													items.setUsergender(userInfo_object.getString("userGender"));
													items.setUserjoined(userInfo_object.getString("userJoined"));
													items.setFollowersTotal(userInfo_object.getString("followersTotal"));
													items.setFollowingTotal(userInfo_object.getString("followingTotal"));
													
													itemList.add(items);
												}
												
												show_progress_status=true;
											}
											else
											{
												itemList.clear();
												show_progress_status=false;
											}
											
											
											
											JSONArray favProduct_array = object.getJSONArray("favProduct");
											if (favProduct_array.length() > 0) 
											{
												for (int k = 0; k < favProduct_array.length(); k++)
												{
													JSONObject favProduct_object = favProduct_array.getJSONObject(k);
													
													Sfav_itemImageUrl1=favProduct_object.getString("image1");
													Sfav_itemImageUrl2=favProduct_object.getString("image2");
													Sfav_itemImageUrl3=favProduct_object.getString("image3");
													Sfav_itemcount=favProduct_object.getString("favCount");
												}
												
												show_favitem_status=true;
											}
											else
											{
												show_favitem_status=false;
											}
											
											JSONArray favShop_array = object.getJSONArray("favShop");
											if (favShop_array.length() > 0) 
											{
												for (int l = 0; l < favShop_array.length(); l++)
												{
													JSONObject favShop_object = favShop_array.getJSONObject(0);
													
													Sfav_shopImageUrl1=favShop_object.getString("image1");
													Sfav_shopImageUrl2=favShop_object.getString("image2");
													Sfav_shopImageUrl3=favShop_object.getString("image3");
													Sfav_shopownerimageUrl=favShop_object.getString("sellerImage");
													Sfav_shopname=favShop_object.getString("shopName");
													Sfav_shopname1=favShop_object.getString("shopseourl");
												}
												show_favshop_status=true;
											}
											else
											{
												show_favshop_status=false;
											}
										}
										
										isDataPresent=true;
										
									}
									else
									{
										isDataPresent=false;
									}
										
								} catch (JSONException e) {
									e.printStackTrace();
								}
								
								
								
								//------on post execute-----
								
								stopload();
								
								if(isDataPresent)
								{
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
										
										
										if(show_progress_status)
										{
											
											Picasso.with(context).load(String.valueOf(itemList.get(0).getUserimage())).placeholder(R.drawable.nouserimg).memoryPolicy(MemoryPolicy.NO_CACHE).into(userImage);
											Tv_username.setText(itemList.get(0).getUsername());
											Tv_followers.setText(itemList.get(0).getFollowersTotal());
											Tv_followings.setText(itemList.get(0).getFollowingTotal());
											Tv_gender.setText(itemList.get(0).getUsergender());
											Tv_joined.setText(getResources().getString(R.string.userprofile_joined_label)+" "+itemList.get(0).getUserjoined());
										}
										

										if(show_favitem_status)
										{
											fav_item_layout.setVisibility(View.VISIBLE);
											
											if(Sfav_itemImageUrl1.length()>0)
											{
												fav_itemImage1.setVisibility(View.VISIBLE);
												Picasso.with(context).load(String.valueOf(Sfav_itemImageUrl1)).resize(Screenwidth, Screenwidth-40).placeholder(R.drawable.no_image_background_profile).memoryPolicy(MemoryPolicy.NO_CACHE).into(fav_itemImage1);
											}
											else
											{
												fav_itemImage1.setVisibility(View.GONE);
											}
											
											
											if(Sfav_itemImageUrl2.length()>0)
											{
												fav_itemImage2.setVisibility(View.VISIBLE);
												Picasso.with(context).load(String.valueOf(Sfav_itemImageUrl2)).resize(Screenwidth, Screenwidth-40).resize(Screenwidth, Screenwidth-40).placeholder(R.drawable.no_image_background_profile).memoryPolicy(MemoryPolicy.NO_CACHE).into(fav_itemImage2);
											}
											else
											{
												fav_itemImage2.setVisibility(View.GONE);
											}
											
											
											if(Sfav_itemImageUrl3.length()>0)
											{
												fav_itemImage3.setVisibility(View.VISIBLE);
												Picasso.with(context).load(String.valueOf(Sfav_itemImageUrl3)).resize(Screenwidth, Screenwidth-40).placeholder(R.drawable.no_image_background_profile).memoryPolicy(MemoryPolicy.NO_CACHE).into(fav_itemImage3);
											}
											else
											{
												fav_itemImage3.setVisibility(View.GONE);
											}
											
											fav_itemCount.setText(Sfav_itemcount+" "+getResources().getString(R.string.userprofile_items_label));
										}
										else
										{
											fav_item_layout.setVisibility(View.GONE);
										}
										if(show_favshop_status)
										{
											fav_shop_layout.setVisibility(View.VISIBLE);
											
											if(Sfav_shopImageUrl1.length()>0)
											{
												fav_shopImage1.setVisibility(View.VISIBLE);
												Picasso.with(context).load(String.valueOf(Sfav_shopImageUrl1)).resize(Screenwidth, Screenwidth-40).placeholder(R.drawable.no_image_background_profile).memoryPolicy(MemoryPolicy.NO_CACHE).into(fav_shopImage1);
											}
											else
											{
												fav_shopImage1.setVisibility(View.GONE);
											}
											
											if(Sfav_shopImageUrl2.length()>0)
											{
												fav_shopImage2.setVisibility(View.VISIBLE);
												Picasso.with(context).load(String.valueOf(Sfav_shopImageUrl2)).resize(Screenwidth, Screenwidth-40).placeholder(R.drawable.no_image_background_profile).memoryPolicy(MemoryPolicy.NO_CACHE).into(fav_shopImage2);
											}
											else
											{
												fav_shopImage2.setVisibility(View.GONE);
											}
											
											if(Sfav_shopImageUrl3.length()>0)
											{
												fav_shopImage3.setVisibility(View.VISIBLE);
												Picasso.with(context).load(String.valueOf(Sfav_shopImageUrl3)).resize(Screenwidth, Screenwidth-40).placeholder(R.drawable.no_image_background_profile).memoryPolicy(MemoryPolicy.NO_CACHE).into(fav_shopImage3);
											}
											else
											{
												fav_shopImage3.setVisibility(View.GONE);
											}
											
											shopname.setText(Sfav_shopname);
											Picasso.with(context).load(String.valueOf(Sfav_shopownerimageUrl)).placeholder(R.drawable.nouserimg).memoryPolicy(MemoryPolicy.NO_CACHE).into(shopimage);
											
										}
										else
										{
											fav_shop_layout.setVisibility(View.GONE);
										}
										
										if(Integer.parseInt(Sfav_shopTotalcount)>1)
										{
											seeAllShop_Layout.setVisibility(View.VISIBLE);
										}
										else
										{
											seeAllShop_Layout.setVisibility(View.GONE);
										}
										
										
									}
									else
									{
										mainlayout.setVisibility(View.GONE);
										nointernet.setVisibility(View.GONE);
									} 
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
								 
								if(asyntask_name.equalsIgnoreCase("normal"))
								{
									mainlayout.setVisibility(View.GONE);
									nointernet.setVisibility(View.GONE);
									loading.setVisibility(View.GONE);
								}
								else
								{
									mainlayout.setVisibility(View.GONE);
									swipeRefreshLayout.setRefreshing(false);
								}
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

					super.onDestroy();
				}	
			
}
