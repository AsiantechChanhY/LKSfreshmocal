package com.shopsy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Pojo.Dispute_Pojo;
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.Dispute_Adapter;

public class DisputePage extends HockeyActivity
{
	private Context context;
	private ActionBar actionBar;
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	private SessionManager session;
	private String userid;
	
	private RelativeLayout loading,nointernet,mainlayout;
	private ListView listview;
	private SwipeRefreshLayout swipeRefreshLayout = null;
	private boolean show_progress_status=true;
	private ImageView dispute_loading;
	private View view;
	private EditText msg_edittext;
	private ImageView send_image,camera_image;
	
	
	private String asyntask_name="normal";
	
	JsonObjectRequest jsonObjReq;
	JsonObjectRequest loadmore_jsonObjReq;
	StringRequest postrequest;
	
	private Dispute_Adapter adapter;
	private ArrayList<Dispute_Pojo> itemList;
	private int checkpagepos = 0;
	
	private TextView header_title;
	private ImageView header_image;
	
	String OrderId="",Dispute_SenderName="",Dispute_SenderImage="";
	private View footerview;
	private boolean loadingMore = false;
	private ProgressBar loadmore_progressbar;
	
	//variable to add photo
		private static final int CAMERA_PICTURE = 1;
		private static final int GALLERY_PICTURE = 2;
		private String selectedPath = "";
		
		private HttpEntity resEntity;
		
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dispute_page);
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		initialize();
		
		
		send_image.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(msg_edittext.getText().length()==0)
				{
					Animation shake = AnimationUtils.loadAnimation(DisputePage.this, R.anim.shake);
					msg_edittext.startAnimation(shake);
				}
				else
				{
					AsynTask_PostMessage asyntask=new AsynTask_PostMessage();
					asyntask.execute(Iconstant.dispute_postmsg_url);
				}
			}
		});
		
		
		camera_image.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				ChooseImage();
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
								    ConnectionDetector cd = new ConnectionDetector(DisputePage.this);
									boolean isInternetPresent = cd.isConnectingToInternet();
								
										if (isInternetPresent) 
										{
											Loadmore_JsonRequest(Iconstant.dispute_url + userid+ "&orderId=" + OrderId+"&pageId="+checkpagepos);
										} 
										else 
										{
											Toast.makeText(DisputePage.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
			  
			    ConnectionDetector cd = new ConnectionDetector(DisputePage.this);
				boolean isInternetPresent = cd.isConnectingToInternet();
				
						if (isInternetPresent) 
						{
							if(session.isLoggedIn())
							{
								asyntask_name="swipe";
								nointernet.setVisibility(View.GONE);
								actionBar.setDisplayShowCustomEnabled(true);
								
							    JsonRequest(Iconstant.dispute_url + userid+ "&orderId=" + OrderId+"&pageId=0");
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
		cd = new ConnectionDetector(DisputePage.this);
		isInternetPresent = cd.isConnectingToInternet();
		session = new SessionManager(DisputePage.this);
		itemList = new ArrayList<Dispute_Pojo>();
		
		listview=(ListView)findViewById(R.id.dispute_listview);
		loading=(RelativeLayout)findViewById(R.id.dispute_loading_layout);
		nointernet=(RelativeLayout)findViewById(R.id.dispute_nointernet_layout);
		mainlayout=(RelativeLayout)findViewById(R.id.dispute_main_layout);
		dispute_loading=(ImageView)findViewById(R.id.dispute_loading_gif);
		send_image=(ImageView)findViewById(R.id.dispute_send_icon);
		camera_image=(ImageView)findViewById(R.id.dispute_camera_icon);
		msg_edittext=(EditText)findViewById(R.id.dispute_chat_editText);
		
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.dispute_swipe_refresh_layout);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setEnabled(true);
		
		msg_edittext.addTextChangedListener(EditorWatcher);
		
		
		//code for gridView header
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			footerview= inflater.inflate(R.layout.listview_dispute_loadmore, null);
			
			loadmore_progressbar=(ProgressBar)footerview.findViewById(R.id.listview_dispute_footer_progressbar);
				
				
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
		    .into(dispute_loading);
		 
		 
		//-------code for Intent---------
		    Intent i = getIntent();
		    OrderId = i.getStringExtra("OrderId");
		    Dispute_SenderName = i.getStringExtra("SenderName");
		    Dispute_SenderImage = i.getStringExtra("SenderImage");
		    
		    
		    header_title.setText(getResources().getString(R.string.dispute_header_title));	
		    header_image.setVisibility(View.GONE);
		   // Picasso.with(context).load(String.valueOf(Dispute_SenderImage)).placeholder(R.drawable.nouserimg).into(header_image);
		    
		if (isInternetPresent) 
		{
			if (session.isLoggedIn()) 
			{
				nointernet.setVisibility(View.GONE);
				JsonRequest(Iconstant.dispute_url + userid+ "&orderId="+ OrderId +"&pageId=0");
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
	
	
	private final TextWatcher EditorWatcher = new TextWatcher()
	{
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
		}
		@Override
		public void afterTextChanged(Editable s)
		{
			if(msg_edittext.getText().length()==0)
			{
				camera_image.setVisibility(View.VISIBLE);
				send_image.setVisibility(View.GONE);
			}
			else
			{
				camera_image.setVisibility(View.GONE);
				send_image.setVisibility(View.VISIBLE);
			}
		}
	};
	
	
	private void ChooseImage()
	{
		final CharSequence[] items = { "Take Photo", "Choose from Gallery","Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(DisputePage.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int item) 
			{
				if (items[item].equals("Take Photo"))
				{
	                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, CAMERA_PICTURE);
					dialog.dismiss();
				}
				else if (items[item].equals("Choose from Gallery"))
				{
	                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(galleryIntent, GALLERY_PICTURE);
					dialog.dismiss();
				} 
				else if (items[item].equals("Cancel")) 
				{
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) 
		{
			if (requestCode == CAMERA_PICTURE)
			{
				
				FileOutputStream fo = null;
				Bitmap photo = (Bitmap) data.getExtras().get("data");
				
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
				Random randomGenerator = new Random();
				randomGenerator.nextInt();
				String newimagename = randomGenerator.toString() + ".jpg";
				File f = new File(Environment.getExternalStorageDirectory()+File.separator + newimagename);
				try
				{
					f.createNewFile();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				
				// write the bytes in file
				try
				{
					fo = new FileOutputStream(f.getAbsoluteFile());
				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				
				try
				{
					fo.write(bytes.toByteArray());
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				
				selectedPath = f.getAbsolutePath();
				
				AsynTask_PostMessage asyntask=new AsynTask_PostMessage();
				asyntask.execute(Iconstant.dispute_postmsg_url);
			}
			if (requestCode == GALLERY_PICTURE)
			{

				Uri selectedImageUri = data.getData();
				selectedPath = getRealPathFromURI(DisputePage.this,selectedImageUri);
				
				AsynTask_PostMessage asyntask=new AsynTask_PostMessage();
				asyntask.execute(Iconstant.dispute_postmsg_url);
			}
			
		}
	}
	
	public String getRealPathFromURI(Context context, Uri contentUri) {
		  Cursor cursor = null;
		  try { 
		    String[] proj = { MediaStore.Images.Media.DATA };
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		  } finally {
		    if (cursor != null) {
		      cursor.close();
		    }
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
					swipeRefreshLayout.setRefreshing(false);
				}
			}
			
			
			
			
			// -------------------------code for JSon Request----------------------------------
			
			private void JsonRequest(String Url) 
			{
				startload();
				
				System.out.println("--------------Dispute url-------------------"+Url);
				
				 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) 
							{
								System.out.println("--------------Dispute response -----------"+response);

								try {

									session.SetCartCount(response.getString("cartCount"));
									checkpagepos = Integer.parseInt(response.getString("pagePos"));
									
									JSONArray jarray = response.getJSONArray("discusssionHistory");

									if (jarray.length() > 0) 
									{
										itemList.clear();
										
										for (int i = 0; i < jarray.length(); i++)
										{
											JSONObject object = jarray.getJSONObject(i);
											
											Dispute_Pojo items = new Dispute_Pojo();
											
											items.setPostedBy(object.getString("postedBy"));
											items.setPostedId(object.getString("posterId"));
											items.setUserimageImage(object.getString("posterImage"));
											items.setPosterName(object.getString("posterName"));
											items.setPostTime(object.getString("postTime"));
											items.setPostMsg(object.getString("postMsg"));
											items.setPostImg(object.getString("postImg_1"));
											
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
									adapter = new Dispute_Adapter(DisputePage.this,itemList);
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
				
				System.out.println("--------------Dispute loadmore url-------------------"+Url);
				
				 loadmore_jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) 
							{
								try {

									session.SetCartCount(response.getString("cartCount"));
									checkpagepos = Integer.parseInt(response.getString("pagePos"));
									
									JSONArray jarray = response.getJSONArray("discusssionHistory");

									if (jarray.length() > 0) 
									{
										for (int i = 0; i < jarray.length(); i++)
										{
											JSONObject object = jarray.getJSONObject(i);
											
											Dispute_Pojo items = new Dispute_Pojo();
											
											items.setPostedBy(object.getString("postedBy"));
											items.setPostedId(object.getString("posterId"));
											items.setUserimageImage(object.getString("posterImage"));
											items.setPosterName(object.getString("posterName"));
											items.setPostTime(object.getString("postTime"));
											items.setPostMsg(object.getString("postMsg"));
											items.setPostImg(object.getString("postImg_1"));
											
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

				 loadmore_jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000, 
			                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
			                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
				 
				// Adding request to request queue
				AppController.getInstance().addToRequestQueue(loadmore_jsonObjReq);
				
			}
			
			
			
			//---------------------code for Send message AsynTask---------------------
			class AsynTask_PostMessage extends AsyncTask<String, Void, Boolean> 
			{
				private int status;
				FileBody photo; 
				String response_str;
				 Dialog dialog;
			     private String statuscode;
				 
				@Override
				protected void onPreExecute() 
				{	
					super.onPreExecute();
					
					 dialog = new Dialog(DisputePage.this);
		             dialog.getWindow();
		             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);   
		             dialog.setContentView(R.layout.custom_progress_dialog);
		             dialog.setCanceledOnTouchOutside(false);
					 dialog.show();
				}

				@Override
				protected Boolean doInBackground(String... urls) 
				{
					
					System.out.println("selectedPath-----------------------------"+selectedPath);
					
					    File image_file = new File(selectedPath);
				        try
				        {
				             HttpClient client = new DefaultHttpClient();
				             HttpPost post = new HttpPost(urls[0]);

				             if(image_file.length()>0)
				             {
				            	  photo = new FileBody(image_file); 
				             }

				             MultipartEntity reqEntity = new MultipartEntity();
				             reqEntity.addPart("sellerId", new StringBody(userid));
				             reqEntity.addPart("orderId", new StringBody(OrderId));
				             reqEntity.addPart("post_message", new StringBody(msg_edittext.getText().toString()));

				             if(image_file.length()>0 )
				             {
				            	 reqEntity.addPart("photo", photo);
				             }
				             
				             post.setEntity(reqEntity);
				             HttpResponse response = client.execute(post);
				             resEntity = response.getEntity();
				             status = response.getStatusLine().getStatusCode();
				             response_str = EntityUtils.toString(resEntity);
				             
				             
				             if(status==200)
				             {
				            	 JSONObject json = new JSONObject(response_str);
				            	 statuscode = json.getString("status");
				            	 
					             if(statuscode.equalsIgnoreCase("1"))
								 {
					            	 JSONArray jarray = json.getJSONArray("discusssionHistory");
										if(jarray.length()>0)
										{
											for (int i = 0; i < jarray.length(); i++) 
											{
												JSONObject object = jarray.getJSONObject(i);
												Dispute_Pojo items = new Dispute_Pojo();
												
												items.setPostedBy(object.getString("postedBy"));
												items.setPostedId(object.getString("posterId"));
												items.setUserimageImage("");
												items.setPosterName("");
												items.setPostTime(object.getString("postTime"));
												items.setPostMsg(object.getString("postMsg"));
												items.setPostImg(object.getString("postImg_1"));
												
												itemList.add(items);
											}
										}
								 }
				             }
				        }
				        catch (Exception ex){
				        }
					return null;
				}

				@Override
				protected void onPostExecute(Boolean result)
				{
					
					 InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		             in.hideSoftInputFromWindow(msg_edittext.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
		                
		             dialog.dismiss();
		                
					if (status == 200) 
					{
						if(statuscode.equalsIgnoreCase("1"))
						{
							msg_edittext.setText("");
							
								adapter = new Dispute_Adapter(DisputePage.this,itemList);
								listview.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							
						}
						else
						{
							Toast.makeText(DisputePage.this,"Failed to post Message", Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						Toast.makeText(DisputePage.this,"Unable to fetch data from server", Toast.LENGTH_SHORT).show();
					}
					super.onPostExecute(result);
				}
			}
			
			
			
			//-----------------Move Back on pressed phone back button------------------		
			
			@Override
			public boolean onKeyDown(int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
					DisputePage.this.finish();
					DisputePage.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
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

					if(loadmore_jsonObjReq!=null)
					{
						loadmore_jsonObjReq.cancel();
					}

				super.onDestroy();
			}	
}
