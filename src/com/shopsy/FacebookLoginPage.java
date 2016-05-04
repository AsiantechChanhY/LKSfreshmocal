package com.shopsy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.material_dialog_library.MaterialDialog;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.SessionManager;

public class FacebookLoginPage extends ActionBarActivity
{
	//------------------FaceBook Initialization------------------
	 
	 private UiLifecycleHelper uiHelper;
	 LoginButton Facebook_LoginButton;

	//------------Social login variable declaration--------------
	 private String Ssocial_id="",Semail="",Sprofile_image="",Sfacebookid="",Sfullname="",Susername="";
	 
	// Session Manager Class
		private SessionManager session_manager;
		private CommonIDSessionManager commonsession;
		private String commonid="",GCM_Id="";
	 
		private String Selected_class="";
		private String SextraData="";
		
		private String catid="";
		private int position=0;
		private ArrayList<String> categoriesspinnerlist = new ArrayList<String>();
		ArrayList<String> totalitemid = new ArrayList<String>();
		
	 @Override
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.facebook_login);
			//getSupportActionBar().hide();
			Intent i1=getIntent();
			GCM_Id=i1.getStringExtra("GCM_Id");
			Selected_class=i1.getStringExtra("SelectedClass");
			
			Bundle extras = i1.getExtras();
			if(extras.containsKey("ExtradataID")) 
			{
				SextraData=i1.getStringExtra("ExtradataID");
			}
			if(extras.containsKey("catposition"))
			{
				position = i1.getIntExtra("catposition", 0);
				catid = i1.getStringExtra("itemid");
				categoriesspinnerlist = i1.getStringArrayListExtra("spinnerlist");
				totalitemid = i1.getStringArrayListExtra("totalitemid");
			}
			
			
			
		     // Session class instance
				session_manager = new SessionManager(FacebookLoginPage.this);
				commonsession = new CommonIDSessionManager(FacebookLoginPage.this);
				
				// get user data from session
				HashMap<String, String> user = commonsession.getUserDetails();
				commonid = user.get(CommonIDSessionManager.KEY_COMMONID);
			
				 logoutFacebook(); 
				
			uiHelper = new UiLifecycleHelper(this, statusCallback);
	        uiHelper.onCreate(savedInstanceState);
	        
	        Facebook_LoginButton=(LoginButton)findViewById(R.id.facebooklogin_button);
	        
	        Facebook_LoginButton.setReadPermissions(Arrays.asList("public_profile", "email",
	                "user_location", "user_birthday", "user_hometown",
	                "user_about_me"));
	        
	        Facebook_LoginButton.performClick();
		}

	 
		//----------------------FaceBook Functionality[Start]--------------------------------
		
		
		
		
		
		private Session.StatusCallback statusCallback = new Session.StatusCallback() {
			@Override
			public void call(final Session session, SessionState state,
					Exception exception) {
				
					onSessionStateChange(session, state, exception);
			}
		};
		

		private void onSessionStateChange(final Session session, SessionState state,Exception exception) 
		{
			if (state.isOpened()) {

				Request.newMeRequest(session, new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {

						
						System.out.println("facebook response---------------------->" + response);
						
						if (user != null) 
						{

							//String access_token=session.getAccessToken();

							GraphObject graphObject = response.getGraphObject();
							if (graphObject != null) 
							{
								JSONObject jsonObject = graphObject.getInnerJSONObject();

								System.out.println("---facebook jsonObject------------------------"+jsonObject);
								
								System.out.println("------------jsonObject.has--------------------"+jsonObject.has("email"));
								
								
								if (jsonObject.has("email")) 
								{
									Ssocial_id=user.getId();
									Susername = user.getName();
									Semail = user.asMap().get("email").toString();
									Sfacebookid = user.getId();
									Sfullname=user.getFirstName()+" "+user.getLastName();
									Sprofile_image = "https://graph.facebook.com/"+ user.getId()+ "/picture?type=large";

									if (Susername.length() > 0&& Semail.length() > 0&& Sfacebookid.length() > 0 && Sprofile_image.length() > 0) 
									{
										
											FaceBookLogin_AsyncTask facebook = new FaceBookLogin_AsyncTask();
											facebook.execute(Iconstant.facebookurl+commonid);
									} else {
										Toast.makeText(FacebookLoginPage.this,getResources().getString(R.string.facebookauthintication),Toast.LENGTH_SHORT).show();
									}
								}
								else
								{
									Ssocial_id=user.getId();
									Sfacebookid = user.getId();
									Susername = user.getName();
									Semail = Susername+Ssocial_id+"@facebook.com";
									Sfullname=user.getFirstName()+" "+user.getLastName();
									Sprofile_image = "https://graph.facebook.com/"+ user.getId()+ "/picture?type=large";
									
									if (Susername.length() > 0&& Semail.length() > 0&& Sfacebookid.length() > 0 && Sprofile_image.length() > 0) 
									{
										
										
											FaceBookLogin_AsyncTask facebook = new FaceBookLogin_AsyncTask();
											facebook.execute(Iconstant.facebookurl+commonid);
										
									} else {
										Toast.makeText(FacebookLoginPage.this,getResources().getString(R.string.facebookauthintication),Toast.LENGTH_SHORT).show();
									}
								}
							}
						}
					}
				}).executeAsync();

			} else if (state.isClosed()) {
				
			}
		}
		
		/*
		public boolean checkPermissions() {
	        Session s = Session.getActiveSession();
	        if (s != null) {
	            return s.getPermissions().contains("publish_actions");
	        } else
	            return false;
	    }

	    public void requestPermissions() {
	        Session s = Session.getActiveSession();
	        if (s != null)
	            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
	    }*/
		

	    @Override
	    public void onResume() {
	        super.onResume();
	        
	        	Session session = Session.getActiveSession();
	  		    if (session != null && (session.isOpened() || session.isClosed()) )
	  		    {
	  		        onSessionStateChange(session, session.getState(), null);
	  		    }
	  		    
	  	        uiHelper.onResume();
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        
	        	uiHelper.onPause();
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        	uiHelper.onDestroy();
	    }

	    @Override
	    public void onSaveInstanceState(Bundle savedState) {
	        super.onSaveInstanceState(savedState);
	        
	        	 uiHelper.onSaveInstanceState(savedState);
	    }

	    //--------- code for logout FaceBook---------
	    public void logoutFacebook() 
	    {
	    	if (Session.getActiveSession() != null) 
			{
			    Session.getActiveSession().closeAndClearTokenInformation();
			}
			Session.setActiveSession(null);	
	    }

	    
	    
	    @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    		// -----------code for faceBook----------
		        uiHelper.onActivityResult(requestCode, resultCode, data);
	      
			super.onActivityResult(requestCode, resultCode, data);
		}
	    
	    // -----------------Functionality for FaceBook[Stop]--------------------

	    
	    
	    
	    
	    
	  //------------------------------code for FaceBook asynTask----------------------------------
	  		class FaceBookLogin_AsyncTask extends AsyncTask<String, Void, Boolean> 
	  		{
	  	    	String message="null",status_code="",response_status="";
	  			ProgressDialog progress;
	  			String Response = null;
	  			int status;
	  			private String SUsername="",Sfullname="",SEmail="",Suserid="",Suserimage="";
	  			String load_class="";
	  			@Override
	  			protected void onPreExecute() 
	  			{
	  				super.onPreExecute();
	  				  progress = new ProgressDialog(FacebookLoginPage.this);
	  				  progress.setMessage(getResources().getString(R.string.pleasewait));
	  			      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	  			      progress.setIndeterminate(false);
	  			      progress.setCanceledOnTouchOutside(false);
	  			      progress.show();
	  			}
	  			
	  			@Override
	  			protected Boolean doInBackground(String... urls) 
	  			{
	  				
	  				 System.out.println("-------------------Semail-----------------------"+Semail);
	  				 System.out.println("-------------------Susername-----------------------"+Susername);
	  				 System.out.println("-------------------Sprofile_image-----------------------"+Sprofile_image);
	  				 System.out.println("-------------------GCM_Id-----------------------"+GCM_Id);
	  				
	  				 try {
	  					HttpClient httpclient = new DefaultHttpClient();
	  					HttpPost httppost = new HttpPost(urls[0]);
	  					
	  					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	  					nameValuePairs.clear();
	  					
	  					nameValuePairs.add(new BasicNameValuePair("email", Semail.replaceAll("\\s","").replace(" ", "")));
	  					nameValuePairs.add(new BasicNameValuePair("username", Susername.replaceAll("\\s","").replace(" ", "")));
	  					nameValuePairs.add(new BasicNameValuePair("profile_image", Sprofile_image));
	  					nameValuePairs.add(new BasicNameValuePair("uu_id", GCM_Id));
	  					
	  					
	  					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	  					HttpResponse responsepost = httpclient.execute(httppost);
	  					HttpEntity entity = responsepost.getEntity();
	  					Response = EntityUtils.toString(entity);
	  					status = responsepost.getStatusLine().getStatusCode();
	  					
	  					System.out.println("social login Response---------------------------"+Response);

	  					if(status==200)
	  					{
	  						 JSONObject object=new JSONObject(Response);
	  						 response_status=object.getString("status");
	  						 message=object.getString("message");
	  						 
	  						 if(response_status.equalsIgnoreCase("1"))
	  						 {
	  							 SUsername=object.getString("user_name");
	  							 Sfullname=object.getString("user_fullname");
	  							 Suserid=object.getString("user_id");
	  							 SEmail=object.getString("email");
	  							 Suserimage=object.getString("user_image");
	  						 }
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
	  			protected void onPostExecute(Boolean result) 
	  			{
	  				progress.dismiss();
	  				
	  				Log.e("register response--------------------------", Response);
	  				
	  				if (response_status.equalsIgnoreCase("0")) 
	  				{
	  					
	  					logoutFacebook();
	  					
	  					final MaterialDialog alertDialog= new MaterialDialog(FacebookLoginPage.this);
	  					alertDialog.setTitle("Error");
	  					alertDialog
	  							.setMessage(message)
	  							.setCanceledOnTouchOutside(false)
	  							.setPositiveButton(
	  				                    "OK", new View.OnClickListener() {
	  				                        @Override
	  				                        public void onClick(View v) {
	  				                        	alertDialog.dismiss();
	  				                        }
	  				                    }
	  				                ).show();
	  					
	  				} 
	  				else if (response_status.equalsIgnoreCase("1")) 
	  				{
	  					
	  					session_manager.createLoginSession(Sfullname, SEmail.replaceAll("\\s","").replace(" ", ""), Suserid, SUsername.replaceAll("\\s","").replace(" ", ""), Suserimage,"No");
	  					session_manager.SetGoogleSignout("1");
	  					Intent returnIntent = new Intent();
	  		            returnIntent.putExtra("SelectedClass", Selected_class);
	  		            returnIntent.putExtra("ExtradataID", SextraData);

	  		            //---------intent for subCategoriesProduct---------
	  		            returnIntent.putExtra("catposition", position);
	  		            returnIntent.putExtra("spinnerlist", categoriesspinnerlist);
	  		            returnIntent.putExtra("itemid", catid);
	  		            returnIntent.putExtra("totalitemid", totalitemid);
	  		            setResult(RESULT_OK, returnIntent);
	  		            onBackPressed();
	  		            finish();
	  		            
	  				} 
	  				else
	  				{
	  					logoutFacebook();
	  					Toast.makeText(FacebookLoginPage.this, "Server Problem", Toast.LENGTH_SHORT).show();
	  				}
	  			}
	  		}
}
