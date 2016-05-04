package com.shopsy.Subclass;

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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.material_dialog_library.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.shopsy.DetailPage;
import com.shopsy.FacebookLoginPage;
import com.shopsy.R;
import com.shopsy.WebviewPage;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.PushNotification.GCMIntializer;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.SessionManager;

public class ActionBarActivity_Subclass_DetailPage extends HockeyActivity implements ConnectionCallbacks, OnConnectionFailedListener
{
	
	private RelativeLayout googleplus,register,signin;
	private EditText sign_username,sign_password;
	private RelativeLayout sign_login,sign_forgotpwd,sign_register;
	private Dialog favorite_dialog,signin_dialog,register_dialog,forgot_dialog;
	private EditText reg_firstname,reg_lastname,reg_email,reg_password,reg_confirm_password,reg_username,for_email;
	private RelativeLayout reg_submit,reg_terms,reg_privacy,reg_signin,for_submit;
	private ProgressDialog progressDialog;
	
	// Session Manager Class
	private SessionManager session_manager;
	private CommonIDSessionManager commonsession;
	private String commonid="";
	String GCM_Id="";
	private String ProductId="";
	
	int FaebookCode=123;
	
	// Buttons
		RelativeLayout FbLogin;
		
		
		
		public class RefreshReceiver extends BroadcastReceiver {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals("com.package.SIGNOUTGOOGLEPLUS")) {
					signOutFromGplus();
				}
			}
		}

		private RefreshReceiver googleSignoutReceiver;
		
		private String SselectedClass="";
		
		// --------------------Declaration for GooglePlus[START]-------------------------
		
		private static final int RC_SIGN_IN = 0;
		
		// Google client to interact with Google API
		private GoogleApiClient mGoogleApiClient;
		
		private boolean mIntentInProgress;
		
		private boolean mSignInClicked;
		
		private ConnectionResult mConnectionResult;
		
		// --------------------Declaration for GooglePlus[END]-------------------------
	
		
		
		@Override
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			
	     // Session class instance
			session_manager = new SessionManager(ActionBarActivity_Subclass_DetailPage.this);
			commonsession = new CommonIDSessionManager(ActionBarActivity_Subclass_DetailPage.this);
			
			 // -------------------Function for GooglePlus----------------------
	        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
	                .addOnConnectionFailedListener(this).addApi(Plus.API)
	                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
	        
	       
	     // -----code to refresh drawer using broadcast receiver-----
	        googleSignoutReceiver = new RefreshReceiver();
	     		IntentFilter intentFilter = new IntentFilter();
	     		intentFilter.addAction("com.package.SIGNOUTGOOGLEPLUS");
	     		registerReceiver(googleSignoutReceiver, intentFilter);
	        
		}
	
	public void favourit_popup(final String classname,String productid)
	{
		favorite_dialog = new Dialog(ActionBarActivity_Subclass_DetailPage.this);
		favorite_dialog.getWindow();
		favorite_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		favorite_dialog.setContentView(R.layout.favorite_popup);
		favorite_dialog.setCanceledOnTouchOutside(true);
		favorite_dialog.getWindow().getAttributes().windowAnimations =R.style.Animations_SlideWindow_FavouritePopup;
		favorite_dialog.show();
		favorite_dialog.getWindow().setGravity(Gravity.CENTER);
		favorite_dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  
		
		googleplus=(RelativeLayout)favorite_dialog.findViewById(R.id.favourite_popup_googlepluslayout);
		register=(RelativeLayout)favorite_dialog.findViewById(R.id.favourite_popup_registerlayout);
		signin=(RelativeLayout)favorite_dialog.findViewById(R.id.favourite_popup_signinlayout);
		FbLogin=(RelativeLayout)favorite_dialog.findViewById(R.id.favourite_popup_facebooklayout);
		
		ProductId=productid;
		
		SselectedClass=classname;
		
		FbLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//---------Getting GCM Id----------
                GCMIntializer initializer = new GCMIntializer(ActionBarActivity_Subclass_DetailPage.this, new GCMIntializer.CallBack()
                {
                    @Override
                    public void onRegisterComplete(String registerationId) {

                        GCM_Id = registerationId;
                       
                        Intent intent = new Intent(ActionBarActivity_Subclass_DetailPage.this, FacebookLoginPage.class);
                        intent.putExtra("GCM_Id", GCM_Id);
                        intent.putExtra("SelectedClass", classname);
                        intent.putExtra("ExtradataID", ProductId);
                        startActivityForResult(intent, FaebookCode);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    }

                    @Override
                    public void onError(String errorMsg)
                    {
                    	  Intent intent = new Intent(ActionBarActivity_Subclass_DetailPage.this, FacebookLoginPage.class);
                          intent.putExtra("GCM_Id", GCM_Id);
                          intent.putExtra("SelectedClass", classname);
                          intent.putExtra("ExtradataID", ProductId);
                          startActivityForResult(intent, FaebookCode);
                          overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                });
                initializer.init();
			}
		});
		
		googleplus.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				
				//---------Getting GCM Id----------
                GCMIntializer initializer = new GCMIntializer(ActionBarActivity_Subclass_DetailPage.this, new GCMIntializer.CallBack()
                {
                    @Override
                    public void onRegisterComplete(String registerationId) {

                        GCM_Id = registerationId;
                        signInWithGplus();
                    }

                    @Override
                    public void onError(String errorMsg)
                    {
                    	signInWithGplus();
                    }
                });
                initializer.init();
				
			}
		});
		signin.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				signin_popup(classname);
			}
		});
		
		
		register.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Register_popup(classname);
			}
		});
		
	}
	
	
	
	private void Forgot_popup(final String classname)
	{
		forgot_dialog = new Dialog(ActionBarActivity_Subclass_DetailPage.this);
		forgot_dialog.getWindow();
		forgot_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		forgot_dialog.setContentView(R.layout.forgotpassword);
		forgot_dialog.setCanceledOnTouchOutside(true);
		forgot_dialog.getWindow().getAttributes().windowAnimations =R.style.Animations_SlideWindow_FavouritePopup;
		forgot_dialog.show();
		forgot_dialog.getWindow().setGravity(Gravity.CENTER);
		
		 for_email=(EditText)forgot_dialog.findViewById(R.id.forgot_popup_email);
		 for_submit=(RelativeLayout)forgot_dialog.findViewById(R.id.forgotpopup_account_relativelayout);
		 for_email.addTextChangedListener(forgotEditorWatcher);
		
		 
		 
		//code to make password editText as dot
		 
		 
		 for_submit.setOnClickListener(new OnClickListener()
		 {
			@Override
			public void onClick(View v)
			{
				
				if(for_email.getText().length()==0)
				{
					erroredit(for_email,"email can't be empty");
				}
				else if(!isValidEmail(for_email.getText().toString()))
				{
					erroredit(for_email,"Enter valid Email");
				}
				else
				{
					ConnectionDetector cd = new ConnectionDetector(ActionBarActivity_Subclass_DetailPage.this);
					boolean isInternetPresent = cd.isConnectingToInternet();
						
					if (isInternetPresent) 
					{
						 
						ForgotPasswordAsyncTask async_register = new ForgotPasswordAsyncTask();
						async_register.execute(Iconstant.forgotpasswordurl,classname);
					} 
					else 
					{
						Toast.makeText(ActionBarActivity_Subclass_DetailPage.this, "No Internet Connection", Toast.LENGTH_SHORT).show();						
					}
				}
				
			}
		});
		
	}
	
	private final TextWatcher forgotEditorWatcher = new TextWatcher()
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
			
			//clear error symbol after entering text
			if (for_email.getText().length() > 0)
			{
				for_email.setError(null);
			}
			
		}
	};
	
	public void signin_popup(final String classname)
	{
		signin_dialog = new Dialog(ActionBarActivity_Subclass_DetailPage.this);
		signin_dialog.getWindow();
		signin_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		signin_dialog.setContentView(R.layout.signin_popup);
		signin_dialog.setCanceledOnTouchOutside(true);
		signin_dialog.getWindow().getAttributes().windowAnimations =R.style.Animations_SlideWindow_FavouritePopup;
		signin_dialog.show();
		signin_dialog.getWindow().setGravity(Gravity.CENTER);
		
		sign_username=(EditText)signin_dialog.findViewById(R.id.signin_popup_usernameeditText);
		sign_password=(EditText)signin_dialog.findViewById(R.id.signin_popup_pwdeditText);
		sign_login=(RelativeLayout)signin_dialog.findViewById(R.id.signinpopup_signinlayout);
		sign_forgotpwd=(RelativeLayout)signin_dialog.findViewById(R.id.signinpopup_forgotpwdlayout);
		sign_register=(RelativeLayout)signin_dialog.findViewById(R.id.signinpopup_account_registerlayout);
		
		//code to make password editText as dot
		sign_password.setTransformationMethod(new PasswordTransformationMethod());
		
		sign_username.addTextChangedListener(LoginEditorWatcher);
		sign_password.addTextChangedListener(LoginEditorWatcher);
		
		sign_register.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				signin_dialog.dismiss();
				Register_popup(classname);
			}
		});
		
		sign_forgotpwd.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Forgot_popup(classname);
			//	ForgotPasswordAsyncTask async_register = new ForgotPasswordAsyncTask();
				//async_register.execute(Iconstant.forgotpasswordurl,classname);
			}
		});
		
		sign_login.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(sign_username.getText().toString().length()==0)
				{
					erroredit(sign_username,"Username can't be empty");
				}
				else if(sign_password.getText().toString().length()==0)
				{
					erroredit(sign_password,"Password can't be empty");
				}
				else
				{
					ConnectionDetector cd = new ConnectionDetector(ActionBarActivity_Subclass_DetailPage.this);
					boolean isInternetPresent = cd.isConnectingToInternet();
						
					if (isInternetPresent) 
					{
						 
						//---------Getting GCM Id----------
                        GCMIntializer initializer = new GCMIntializer(ActionBarActivity_Subclass_DetailPage.this, new GCMIntializer.CallBack()
                        {
                            @Override
                            public void onRegisterComplete(String registerationId) {

                                GCM_Id = registerationId;
                                LoginAsyncTask async_Event_Post = new LoginAsyncTask();
        						async_Event_Post.execute(Iconstant.loginurl+commonid,classname);
                            }

                            @Override
                            public void onError(String errorMsg)
                            {
                            	LoginAsyncTask async_Event_Post = new LoginAsyncTask();
        						async_Event_Post.execute(Iconstant.loginurl+commonid,classname);
                            }
                        });
                        initializer.init();
					} 
					else 
					{
						Toast.makeText(ActionBarActivity_Subclass_DetailPage.this, "No Internet Connection", Toast.LENGTH_SHORT).show();						
					}
				}
			}
		});
		
	}
	
	
	private void Register_popup(final String classname)
	{
		register_dialog = new Dialog(ActionBarActivity_Subclass_DetailPage.this);
		register_dialog.getWindow();
		register_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		register_dialog.setContentView(R.layout.register_popup);
		register_dialog.setCanceledOnTouchOutside(true);
		register_dialog.getWindow().getAttributes().windowAnimations =R.style.Animations_SlideWindow_FavouritePopup;
		register_dialog.show();
		register_dialog.getWindow().setGravity(Gravity.CENTER);
		
		
		 reg_firstname=(EditText)register_dialog.findViewById(R.id.register_popup_firsteditText);
		 reg_lastname=(EditText)register_dialog.findViewById(R.id.register_popup_lasteditText);
		 reg_email=(EditText)register_dialog.findViewById(R.id.register_popup_emaileditText);
		 reg_password=(EditText)register_dialog.findViewById(R.id.register_popup_pwdeditText);
		 reg_confirm_password=(EditText)register_dialog.findViewById(R.id.register_popup_confirm_pwdeditText);
		 reg_username=(EditText)register_dialog.findViewById(R.id.register_popup_usernameeditText);
		 
		 reg_submit=(RelativeLayout)register_dialog.findViewById(R.id.register_popup_submit);
		 reg_terms=(RelativeLayout)register_dialog.findViewById(R.id.register_popup_terms_layout);
		 reg_privacy=(RelativeLayout)register_dialog.findViewById(R.id.register_popup_policy_layout);
		 reg_signin=(RelativeLayout)register_dialog.findViewById(R.id.register_popup_signinaccount);
		 
		 
		 reg_firstname.addTextChangedListener(registerEditorWatcher);
		 reg_lastname.addTextChangedListener(registerEditorWatcher);
		 reg_email.addTextChangedListener(registerEditorWatcher);
		 reg_password.addTextChangedListener(registerEditorWatcher);
		 reg_confirm_password.addTextChangedListener(registerEditorWatcher);
		 reg_username.addTextChangedListener(registerEditorWatcher);
		 
		 
		//code to make password editText as dot
		 reg_password.setTransformationMethod(new PasswordTransformationMethod());
		 reg_confirm_password.setTransformationMethod(new PasswordTransformationMethod());
		 
		 
		 reg_terms.setOnClickListener(new OnClickListener()
		 {
			@Override
			public void onClick(View v) 
			{
				ConnectionDetector cd = new ConnectionDetector(ActionBarActivity_Subclass_DetailPage.this);
				boolean isInternetPresent = cd.isConnectingToInternet();
				if (isInternetPresent) {
					Intent intent = new Intent(ActionBarActivity_Subclass_DetailPage.this,
							WebviewPage.class);
					intent.putExtra("title", "Terms and Condition");
					intent.putExtra("weburl", Iconstant.terms_and_conditions);
					startActivity(intent);
					overridePendingTransition(R.anim.enter, R.anim.exit);
				} else {
					Toast.makeText(ActionBarActivity_Subclass_DetailPage.this,
							"No Internet Connection", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		 
		 reg_privacy.setOnClickListener(new OnClickListener()
		 {
			@Override
			public void onClick(View v) 
			{
				ConnectionDetector cd = new ConnectionDetector(ActionBarActivity_Subclass_DetailPage.this);
				boolean isInternetPresent = cd.isConnectingToInternet();
				if (isInternetPresent) {
					Intent intent = new Intent(ActionBarActivity_Subclass_DetailPage.this,
							WebviewPage.class);
					intent.putExtra("title", "Privacy");
					intent.putExtra("weburl", Iconstant.terms_and_conditions);
					startActivity(intent);
					overridePendingTransition(R.anim.enter, R.anim.exit);
				} else {
					Toast.makeText(ActionBarActivity_Subclass_DetailPage.this,
							"No Internet Connection", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		 
		 reg_signin.setOnClickListener(new OnClickListener()
		 {
			@Override
			public void onClick(View v) 
			{
				register_dialog.dismiss();
				signin_popup(classname);
			}
		});
		 
		 reg_submit.setOnClickListener(new OnClickListener()
		 {
			@Override
			public void onClick(View v)
			{
				
				if(reg_firstname.getText().length()==0)
				{
					erroredit(reg_firstname,"Firstname can't be empty");
				}
				else if(reg_firstname.getText().length()>=10)
				{
					erroredit(reg_firstname,"first name can't be more than 10 digit");
				}
				else if(reg_lastname.getText().length()==0)
				{
					erroredit(reg_lastname,"Lastname can't be empty");
				}
				else if(reg_lastname.getText().length()>=10)
				{
					erroredit(reg_lastname,"last name can't be more than 10 digit");
				}
				else if(!isValidEmail(reg_email.getText().toString()))
				{
					erroredit(reg_email,"Enter valid Email");
				}
				else if(reg_password.getText().length()==0)
				{
					erroredit(reg_password,"Password can't be empty");
				}
				else if(reg_password.getText().length()<3)
				{
					erroredit(reg_password,"Password should be maximum 8 and minimum 3 digit");
				}
				else if(reg_password.getText().length()>8)
				{
					erroredit(reg_password,"Password should be maximum 8 and minimum 3 digit");
				}
				else if(reg_confirm_password.getText().length()==0)
				{
					erroredit(reg_confirm_password,"Confirm password can't be empty");
				}
				else if(reg_confirm_password.getText().length()<3)
				{
					erroredit(reg_confirm_password,"Password should be maximum 8 and minimum 3 digit");
				}
				else if(reg_confirm_password.getText().length()>8)
				{
					erroredit(reg_confirm_password,"Password should be maximum 8 and minimum 3 digit");
				}
				else if(!reg_password.getText().toString().equals(reg_confirm_password.getText().toString()))
				{
					erroredit(reg_confirm_password,"Password do not match");
				}
				else if(reg_username.getText().toString().length()==0)
				{
					erroredit(reg_username,"Username can't be empty");
				}
				else if(reg_username.getText().length()>=10)
				{
					erroredit(reg_username,"user name can't be more than 10 digit");
				}
				else
				{
					ConnectionDetector cd = new ConnectionDetector(ActionBarActivity_Subclass_DetailPage.this);
					boolean isInternetPresent = cd.isConnectingToInternet();
						
					if (isInternetPresent) 
					{
						 
						//---------Getting GCM Id----------
                        GCMIntializer initializer = new GCMIntializer(ActionBarActivity_Subclass_DetailPage.this, new GCMIntializer.CallBack()
                        {
                            @Override
                            public void onRegisterComplete(String registerationId) {

                                GCM_Id = registerationId;
                                RegisterAsyncTask async_register = new RegisterAsyncTask();
        						async_register.execute(Iconstant.registerurl+commonid,classname);
                            }

                            @Override
                            public void onError(String errorMsg)
                            {
                            	RegisterAsyncTask async_register = new RegisterAsyncTask();
        						async_register.execute(Iconstant.registerurl+commonid,classname);
                            }
                        });
                        initializer.init();
					} 
					else 
					{
						Toast.makeText(ActionBarActivity_Subclass_DetailPage.this, "No Internet Connection", Toast.LENGTH_SHORT).show();						
					}
				}
				
			}
		});
		
	}
	
	
	//-----------------------Code for Loading Animation----------------------------------
	
			@SuppressWarnings("static-access")
			public void waveAnimation(TextView loadTvOne,TextView loadTvTwo,TextView loadTvThree,TextView loadTvFour,TextView loadTvFive,ObjectAnimator waveOneAnimator,ObjectAnimator waveTwoAnimator,ObjectAnimator waveThreeAnimator,ObjectAnimator waveFourAnimator,ObjectAnimator waveFiveAnimator,String animationstart) 
			{
				PropertyValuesHolder tvOne_Y = PropertyValuesHolder.ofFloat(loadTvOne.TRANSLATION_Y, -40.0f);
				PropertyValuesHolder tvOne_X = PropertyValuesHolder.ofFloat(loadTvOne.TRANSLATION_X, 0);
				waveOneAnimator = ObjectAnimator.ofPropertyValuesHolder(loadTvOne, tvOne_X, tvOne_Y);
				waveOneAnimator.setRepeatCount(-1);
				waveOneAnimator.setRepeatMode(ValueAnimator.REVERSE);
				waveOneAnimator.setDuration(300);
				

				PropertyValuesHolder tvTwo_Y = PropertyValuesHolder.ofFloat(loadTvTwo.TRANSLATION_Y, -40.0f);
				PropertyValuesHolder tvTwo_X = PropertyValuesHolder.ofFloat(loadTvTwo.TRANSLATION_X, 0);
				waveTwoAnimator = ObjectAnimator.ofPropertyValuesHolder(loadTvTwo, tvTwo_X, tvTwo_Y);
				waveTwoAnimator.setRepeatCount(-1);
				waveTwoAnimator.setRepeatMode(ValueAnimator.REVERSE);
				waveTwoAnimator.setDuration(300);
				waveTwoAnimator.setStartDelay(100);
				

				PropertyValuesHolder tvThree_Y = PropertyValuesHolder.ofFloat(loadTvThree.TRANSLATION_Y, -40.0f);
				PropertyValuesHolder tvThree_X = PropertyValuesHolder.ofFloat(loadTvThree.TRANSLATION_X, 0);
				waveThreeAnimator = ObjectAnimator.ofPropertyValuesHolder(loadTvThree, tvThree_X, tvThree_Y);
				waveThreeAnimator.setRepeatCount(-1);
				waveThreeAnimator.setRepeatMode(ValueAnimator.REVERSE);
				waveThreeAnimator.setDuration(300);
				waveThreeAnimator.setStartDelay(200);
				
				
				PropertyValuesHolder tvfour_Y = PropertyValuesHolder.ofFloat(loadTvFour.TRANSLATION_Y, -40.0f);
				PropertyValuesHolder tvfour_X = PropertyValuesHolder.ofFloat(loadTvFour.TRANSLATION_X, 0);
				waveFourAnimator = ObjectAnimator.ofPropertyValuesHolder(loadTvFour, tvfour_X, tvfour_Y);
				waveFourAnimator.setRepeatCount(-1);
				waveFourAnimator.setRepeatMode(ValueAnimator.REVERSE);
				waveFourAnimator.setDuration(300);
				waveFourAnimator.setStartDelay(300);
				
				
				
				PropertyValuesHolder tvfive_Y = PropertyValuesHolder.ofFloat(loadTvFive.TRANSLATION_Y, -40.0f);
				PropertyValuesHolder tvfive_X = PropertyValuesHolder.ofFloat(loadTvFive.TRANSLATION_X, 0);
				waveFiveAnimator = ObjectAnimator.ofPropertyValuesHolder(loadTvFive, tvfive_X, tvfive_Y);
				waveFiveAnimator.setRepeatCount(-1);
				waveFiveAnimator.setRepeatMode(ValueAnimator.REVERSE);
				waveFiveAnimator.setDuration(300);
				waveFiveAnimator.setStartDelay(400);
				
				
				
				//code to start and stop animation
				if(animationstart.equalsIgnoreCase("start"))
				{
					waveOneAnimator.start();
					waveTwoAnimator.start();
					waveThreeAnimator.start();
					waveFourAnimator.start();
					waveFiveAnimator.start();
				}
				else
				{
					waveOneAnimator.end();
					waveTwoAnimator.end();
					waveThreeAnimator.end();
					waveFourAnimator.end();
					waveFiveAnimator.end();
				}
			}
	
	
	//----------------------Code for TextWatcher-------------------------
	
		private final TextWatcher registerEditorWatcher = new TextWatcher()
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
				
				//clear error symbol after entering text
				if (reg_firstname.getText().length() > 0)
				{
					reg_firstname.setError(null);
				}
				if (reg_lastname.getText().length() > 0)
				{
					reg_lastname.setError(null);
				}
				if (reg_email.getText().length() > 0)
				{
					reg_email.setError(null);
				}
				if (reg_password.getText().length() > 0)
				{
					reg_password.setError(null);
				}
				if (reg_confirm_password.getText().length() > 0)
				{
					reg_confirm_password.setError(null);
				}
				if (reg_username.getText().length() > 0)
				{
					reg_username.setError(null);
				}
			}
		};
		
		private final TextWatcher LoginEditorWatcher = new TextWatcher()
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
				
				if (sign_username.getText().length() > 0)
				{
					sign_username.setError(null);
				}
				if (sign_password.getText().length() > 0)
				{
					sign_password.setError(null);
				}
				
			}
		};
	
	
	//--------------------Code to set error for EditText-----------------------
	
			private void erroredit(EditText editname,String msg)
			{
				Animation shake = AnimationUtils.loadAnimation(ActionBarActivity_Subclass_DetailPage.this, R.anim.shake);
				editname.startAnimation(shake);
				
				ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.parseColor("#ffffff"));
				SpannableStringBuilder ssbuilder = new SpannableStringBuilder(msg);
				ssbuilder.setSpan(fgcspan, 0, msg.length(), 0);
				editname.setError(ssbuilder);
			}
			
	//-------------------------code to Check Email Validation-----------------------
			
		public final static boolean isValidEmail(CharSequence target) 
		{
			return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}		
		
	
		
  //-------------------------GooglePlus Functionality [STARTS]-------------------------
	    
	    
	    @Override
	    protected void onStart() {
	        super.onStart();
	        mGoogleApiClient.connect();
	    }

	    @Override
	    protected void onStop() {
	        super.onStop();
	        if (mGoogleApiClient.isConnected()) {
	            mGoogleApiClient.disconnect();
	        }
	    }

	    
	    /**
		 * Method to resolve any signin errors
		 * */
		private void resolveSignInError() {
			
			if(mGoogleApiClient.isConnected())
			{
				System.out.println("------------------Already connected---------------");
			}
			else
			{
				if (mConnectionResult.hasResolution()) {
					try {
						mIntentInProgress = true;
						mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
					} catch (SendIntentException e) {
						mIntentInProgress = false;
						mGoogleApiClient.connect();
					}
				}
			}
			
		}

		@Override
	    public void onConnectionFailed(ConnectionResult result) {
	        if (!result.hasResolution()) {
	            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
	            return;
	        }

	        if (!mIntentInProgress) {
	            // Store the ConnectionResult for later usage
	            mConnectionResult = result;

	            if (mSignInClicked) {
	                // The user has already clicked 'sign-in' so we attempt to
	                // resolve all
	                // errors until the user is signed in, or they cancel.
	                resolveSignInError();
	            }
	        }

	    }

	    public void onConnected(Bundle arg0) {
	        mSignInClicked = false;

	    	// get user data from session
			HashMap<String, String> user = session_manager.GetGoogleSignout();
			String issigned = user.get(SessionManager.KEY_SIGNOUT_GOOGLEPLUS);
	        
	        if(issigned.length()>0)
	        {
	        	System.out.println("------------connected------------");
	        }
	        else
	        {
	        	System.out.println("------------not connected------------");
	        	
	        	GooglePlusLogin_AsyncTask googleplus_asyntask = new GooglePlusLogin_AsyncTask();
	  	  	    googleplus_asyntask.execute(Iconstant.googleplusurl+commonid);
	        }
	    }

	    public void onConnectionSuspended(int arg0) {
	        mGoogleApiClient.connect();
	    }

	    /**
	     * Sign-in into google
	     */
	    private void signInWithGplus() {
	        if (!mGoogleApiClient.isConnecting()) {
	            mSignInClicked = true;
	            resolveSignInError();
	        }
	    }

	    private void signOutFromGplus() {
	        if (mGoogleApiClient.isConnected()) {
	            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
	            mGoogleApiClient.disconnect();
	            mGoogleApiClient.connect();
	        }
	    }
	    
	
	    @Override
		public void onDestroy() {
			// Unregister the logout receiver
			unregisterReceiver(googleSignoutReceiver);
			super.onDestroy();
		}
		
		  @Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		    	 if (requestCode == FaebookCode && resultCode == Activity.RESULT_OK && data != null) {
		             String MySelectedClass = data.getStringExtra("SelectedClass");
		             String SproductID = data.getStringExtra("ExtradataID");
		             
		             favorite_dialog.dismiss();
		             
		              //code to refresh drawer list
						Intent broadcastIntent = new Intent();
						broadcastIntent.setAction("com.package.ACTION_DRAWER_REFRESH");
						sendBroadcast(broadcastIntent);
						
						System.out.println("-------------MySelectedClass-----------------"+MySelectedClass);
						
						finish();
						Intent intent = new Intent(ActionBarActivity_Subclass_DetailPage.this, DetailPage.class);
						intent.putExtra("productid", SproductID);
						startActivity(intent);
						overridePendingTransition(R.anim.enter, R.anim.exit);
		    	 }
		    	 
		    	 if (requestCode == RC_SIGN_IN) {
		    	        if (resultCode != RESULT_OK) {
		    	            mSignInClicked = false;
		    	        }
		    	 
		    	        mIntentInProgress = false;
		    	 
		    	        if (!mGoogleApiClient.isConnecting()) {
		    	            mGoogleApiClient.connect();
		    	        }
		    	    }
		    	 

		    	 super.onActivityResult(requestCode, resultCode, data);
			}
		
		

	//------------------------------code for Login AsynTask----------------------------------
		class LoginAsyncTask extends AsyncTask<String, Void, Boolean> {
			
			String msg="",response_status="";
			int status;
			String[] val;
			String load_class="";
			String Response = null;
			private String SUsername="",Sfullname="",Semail="",Suserid="",Suserimage="",Sseller="";
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				
				progressDialog = ProgressDialog.show(ActionBarActivity_Subclass_DetailPage.this, "", "Logging in...", false);
			}
			@Override
			protected Boolean doInBackground(String... urls) 
			{
				load_class=urls[1];
				
				// Session class instance
				session_manager = new SessionManager(ActionBarActivity_Subclass_DetailPage.this);
				commonsession = new CommonIDSessionManager(ActionBarActivity_Subclass_DetailPage.this);
				
				// get user data from session
				HashMap<String, String> user = commonsession.getUserDetails();
				commonid = user.get(CommonIDSessionManager.KEY_COMMONID);
				
				// code to post data to server
				
				try {
					HttpClient httpclient = new DefaultHttpClient();
					httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "shopsymobileapp");
					HttpPost httppost = new HttpPost(urls[0]);
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
					
					nameValuePairs.add(new BasicNameValuePair("username",sign_username.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("password",sign_password.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("uu_id",GCM_Id));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					Response = EntityUtils.toString(entity);
					status = response.getStatusLine().getStatusCode();

					if(status==200)
					{
						 JSONObject object=new JSONObject(Response);
						 response_status=object.getString("status");
						 msg=object.getString("message");
						 
						 if(response_status.equalsIgnoreCase("1"))
						 {
							 SUsername=object.getString("user_name");
							 Sfullname=object.getString("user_fullname");
							 Suserid=object.getString("user_id");
							 Semail=object.getString("email");
							 Suserimage=object.getString("user_image");
							 Sseller=object.getString("seller");
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
				
				progressDialog.dismiss();
				
				if (response_status.equalsIgnoreCase("0")) 
				{
					
					sign_username.setText("");
					sign_password.setText("");
					
					sign_username.setError(Html.fromHtml("<font color='White'>Invalid Login</font>"));
					sign_password.setError(Html.fromHtml("<font color='White'>Invalid Login</font>"));
					
					Animation shake = AnimationUtils.loadAnimation(ActionBarActivity_Subclass_DetailPage.this,R.anim.shake);
					sign_username.startAnimation(shake);
					
					Animation shakepwd = AnimationUtils.loadAnimation(ActionBarActivity_Subclass_DetailPage.this, R.anim.shake);
					sign_password.startAnimation(shakepwd);
								
				} 
				else if (response_status.equalsIgnoreCase("1")) 
				{
					signin_dialog.dismiss();
					favorite_dialog.dismiss();
					
					// Use user real data
					session_manager.createLoginSession(Sfullname, Semail, Suserid, SUsername, Suserimage,Sseller);
					 session_manager.SetGoogleSignout("1");
					//code to refresh drawer list
					Intent broadcastIntent = new Intent();
					broadcastIntent.setAction("com.package.ACTION_DRAWER_REFRESH");
					sendBroadcast(broadcastIntent);
					
					
					if(load_class.equalsIgnoreCase("detail_page"))
					{
						finish();
						Intent intent = new Intent(ActionBarActivity_Subclass_DetailPage.this, DetailPage.class);
						intent.putExtra("productid", ProductId);
						startActivity(intent);
						overridePendingTransition(R.anim.enter, R.anim.exit);
					}
				}
				else
				{
					Toast.makeText(ActionBarActivity_Subclass_DetailPage.this, "Server Problem", Toast.LENGTH_SHORT).show();
				}
				
			}
		}	
		
		//------------------------------code for forgotPAssword asyntask----------------------------------
		class ForgotPasswordAsyncTask extends AsyncTask<String, Void, Boolean> {
			
			String msg="",response_status="";
			int status;
			String[] val;
			String load_class="";
			String Response = null;
			//private String SUsername="",Sfullname="",Semail="",Suserid="",Suserimage="",Sseller="";
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				
				progressDialog = ProgressDialog.show(ActionBarActivity_Subclass_DetailPage.this, "", "Please Wait ...", false);
			}
			@Override
			protected Boolean doInBackground(String... urls) 
			{
				
				load_class=urls[1];
				
				// Session class instance
				session_manager = new SessionManager(ActionBarActivity_Subclass_DetailPage.this);
				commonsession = new CommonIDSessionManager(ActionBarActivity_Subclass_DetailPage.this);
				
				// get user data from session
				HashMap<String, String> user = commonsession.getUserDetails();
				
				// code to post data to server
				
				try {
					HttpClient httpclient = new DefaultHttpClient();
					httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "shopsymobileapp");
					HttpPost httppost = new HttpPost(urls[0]);
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
					
					
					nameValuePairs.add(new BasicNameValuePair("email",user.get(SessionManager.KEY_EMAIL)));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					Response = EntityUtils.toString(entity);
					status = response.getStatusLine().getStatusCode();

					if(status==200)
					{
						 JSONObject object=new JSONObject(Response);
						 response_status=object.getString("status");
						 System.out.println("Response message"+response_status);
						 msg=object.getString("msg");
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
				
			     progressDialog.dismiss();
					
					Log.e("output----------------->", String.valueOf(Response));
					
					if (response_status.equalsIgnoreCase("0")) 
					{
						final MaterialDialog alertDialog= new MaterialDialog(ActionBarActivity_Subclass_DetailPage.this);
						alertDialog.setTitle("Error");
						alertDialog
								.setMessage(msg)
								.setCanceledOnTouchOutside(false)
								.setPositiveButton(
					                    "OK", new View.OnClickListener() {
					                        @Override
					                        public void onClick(View v) {
					                        	alertDialog.dismiss();
					                        	forgot_dialog.dismiss();
												signin_dialog.dismiss();
					                        	
					                        }
					                    }
					                ).show();
						
					} 
					else if (response_status.equalsIgnoreCase("1")) 
					{
						
						
						final MaterialDialog alertDialog= new MaterialDialog(ActionBarActivity_Subclass_DetailPage.this);
						alertDialog.setTitle("Sucesss");
						alertDialog
								.setMessage(msg)
								.setCanceledOnTouchOutside(false)
								.setPositiveButton(
					                    "OK", new View.OnClickListener() {
					                        @Override
					                        public void onClick(View v) {
					                        	alertDialog.dismiss();
					                        	forgot_dialog.dismiss();
												signin_dialog.dismiss();
					                        	
					                        }
					                    }
					                ).show();
						
					} 
					else
					{
						Toast.makeText(ActionBarActivity_Subclass_DetailPage.this, "Server Problem", Toast.LENGTH_SHORT).show();
					}
					
					
				
			}
		}	
		
	
		
		
		//--------------------------code for Register Asyntask-------------------------------
		class RegisterAsyncTask extends AsyncTask<String, Void, Boolean> {
			
			String msg="",response_status="";
			int status;
			String[] val;
			String load_class="";
			String Response = null;
			private String SUsername="",Sfullname="",Semail="",Suserid="",Suserimage="";
			
			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();
				
				progressDialog = ProgressDialog.show(ActionBarActivity_Subclass_DetailPage.this, "", "Signing up...", false);
			}
			@Override
			protected Boolean doInBackground(String... urls) 
			{
				load_class=urls[1];
				
				// Session class instance
				session_manager = new SessionManager(ActionBarActivity_Subclass_DetailPage.this);
				commonsession = new CommonIDSessionManager(ActionBarActivity_Subclass_DetailPage.this);
				
				// get user data from session
				HashMap<String, String> user = commonsession.getUserDetails();
				commonid = user.get(CommonIDSessionManager.KEY_COMMONID);
				
					
					try {
						HttpClient httpclient = new DefaultHttpClient();
						httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "shopsymobileapp");
						HttpPost httppost = new HttpPost(urls[0]);
								
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
						
						nameValuePairs.add(new BasicNameValuePair("firstname",reg_firstname.getText().toString()));
						nameValuePairs.add(new BasicNameValuePair("lastname",reg_lastname.getText().toString()));
						nameValuePairs.add(new BasicNameValuePair("email",reg_email.getText().toString()));
						nameValuePairs.add(new BasicNameValuePair("password",reg_password.getText().toString()));
						nameValuePairs.add(new BasicNameValuePair("username",reg_username.getText().toString()));
						nameValuePairs.add(new BasicNameValuePair("uu_id",GCM_Id));
						httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						Response = EntityUtils.toString(entity);
						status = response.getStatusLine().getStatusCode();

						if(status==200)
						{
							 JSONObject object=new JSONObject(Response);
							 response_status=object.getString("status");
							 msg=object.getString("message");
							 
							 if(response_status.equalsIgnoreCase("1"))
							 {
								 SUsername=object.getString("user_name");
								 Sfullname=object.getString("user_fullname");
								 Suserid=object.getString("user_id");
								 Semail=object.getString("email");
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
				progressDialog.dismiss();
				
				if (response_status.equalsIgnoreCase("0")) 
				{
					final MaterialDialog alertDialog= new MaterialDialog(ActionBarActivity_Subclass_DetailPage.this);
					alertDialog.setTitle("Error");
					alertDialog
							.setMessage(msg)
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
					register_dialog.dismiss();
					favorite_dialog.dismiss();
					
					session_manager.createLoginSession(Sfullname, Semail, Suserid, SUsername, Suserimage,"No");
					 session_manager.SetGoogleSignout("1");
					//code to refresh drawer list
					Intent broadcastIntent = new Intent();
					broadcastIntent.setAction("com.package.ACTION_DRAWER_REFRESH");
					sendBroadcast(broadcastIntent);
					
					if(load_class.equalsIgnoreCase("detail_page"))
					{
						finish();
						Intent intent = new Intent(ActionBarActivity_Subclass_DetailPage.this, DetailPage.class);
						intent.putExtra("productid", ProductId);
						startActivity(intent);
						overridePendingTransition(R.anim.enter, R.anim.exit);
					}
					
				} 
				else
				{
					Toast.makeText(ActionBarActivity_Subclass_DetailPage.this, "Server Problem", Toast.LENGTH_SHORT).show();
				}
			}
		}	

		
		
		
		//------------------------------code for GooglePlus asynTask----------------------------------
		class GooglePlusLogin_AsyncTask extends AsyncTask<String, Void, Boolean> 
		{
	    	String message="null",status_code="",response_status="";
			ProgressDialog progress;
			String Response = null;
			int status;
			
			 String googleuserImageUrl = "";
			 
				private String SUsername="",Sfullname="",Semail="",Suserid="",Suserimage="";
			
			@Override
			protected void onPreExecute() 
			{
				super.onPreExecute();
				  progress = new ProgressDialog(ActionBarActivity_Subclass_DetailPage.this);
				  progress.setMessage(getResources().getString(R.string.googleplus_authintication));
			      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			      progress.setIndeterminate(false);
			      progress.setCanceledOnTouchOutside(false);
			      progress.show();
			}
			
			@Override
			protected Boolean doInBackground(String... urls) 
			{
				
				 try {
				
					 
					 if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
	                        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
	                        String googleName = currentPerson.getDisplayName();
	                        googleuserImageUrl = currentPerson.getImage().getUrl();

	                        // String location = currentPerson.getCurrentLocation();

	                        // Log.e("google plus current location----------------->",
	                        // location);

	                        // String personGooglePlusProfile =
	                        // currentPerson.getUrl();
	                        String googleEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);

	                        HttpClient httpclient = new DefaultHttpClient();
	                        HttpPost httppost = new HttpPost(Iconstant.googleplusurl);

	                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	    					nameValuePairs.clear();
	    					
	    					nameValuePairs.add(new BasicNameValuePair("email", googleEmail.replaceAll("\\s","").replace(" ", "")));
	    					nameValuePairs.add(new BasicNameValuePair("username", googleName.replaceAll("\\s","").replace(" ", "")));
	    					nameValuePairs.add(new BasicNameValuePair("profile_image", googleuserImageUrl));
	    					nameValuePairs.add(new BasicNameValuePair("uu_id", GCM_Id));

	                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	                        HttpResponse responsepost = httpclient.execute(httppost);
	                        HttpEntity entity = responsepost.getEntity();
	                        Response = EntityUtils.toString(entity);
	                        status = responsepost.getStatusLine().getStatusCode();
	    					
	    					System.out.println("social Google plus login Response---------------------------"+Response);

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
	    							 Semail=object.getString("email");
	    							 Suserimage=object.getString("user_image");
	    						 }
	    					}

	                    } else {
	                        Toast.makeText(getApplicationContext(), "Person information is null",
	                                Toast.LENGTH_LONG).show();
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
					
					signOutFromGplus();
					
					final MaterialDialog alertDialog= new MaterialDialog(ActionBarActivity_Subclass_DetailPage.this);
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
					favorite_dialog.dismiss();
					
					session_manager.createLoginSession(Sfullname, Semail, Suserid, SUsername, Suserimage,"No");
					
					 session_manager.SetGoogleSignout("1");
					
					//code to refresh drawer list
						Intent broadcastIntent = new Intent();
						broadcastIntent.setAction("com.package.ACTION_DRAWER_REFRESH");
						sendBroadcast(broadcastIntent);
						
						if(SselectedClass.equalsIgnoreCase("detail_page"))
						{
							finish();
							Intent intent = new Intent(ActionBarActivity_Subclass_DetailPage.this, DetailPage.class);
							intent.putExtra("productid", ProductId);
							startActivity(intent);
							overridePendingTransition(R.anim.enter, R.anim.exit);
						}
				} 
				else
				{
					signOutFromGplus();
					Toast.makeText(ActionBarActivity_Subclass_DetailPage.this, "Server Problem", Toast.LENGTH_SHORT).show();
				}
			}
		}

}

