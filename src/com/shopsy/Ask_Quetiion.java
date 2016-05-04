package com.shopsy;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Utils.AppController;

public class Ask_Quetiion  extends HockeyActivity
{
	
	
	StringRequest postrequest;
	private String UserId;
	private RelativeLayout tick;
	ActionBar actionBar;
	Context context;
	TextView productname,producturl;
	EditText message;
	String productname1=" ",producturl1=" ";
	private String convId="";
	private String msgId="";
	ColorDrawable colorDrawable = new ColorDrawable();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ask_question);
		
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		init();
		tick.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				PostRequest(Iconstant.post_conversation_url);
			}
		});
		
	}
	private void init()
	{
        actionBar.setDisplayShowTitleEnabled(false);
		
		// code to set actionBar background
		colorDrawable.setColor(0xff1A237E);
		actionBar.setBackgroundDrawable(colorDrawable);
		
		//code to set back arrow
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		actionBar.setDisplayShowCustomEnabled(true);
		View view = getLayoutInflater().inflate(R.layout.ask_question_header, null);
		actionBar.setCustomView(view);
		tick = (RelativeLayout) view.findViewById(R.id.ask_question_header_layout);
		
		
		
		productname = (TextView) findViewById(R.id.editText1);
		producturl = (TextView) findViewById(R.id.editText2);
		message = (EditText) findViewById(R.id.editText3);
//		message.getBackground().setColorFilter(getResources().getColor(R.color.card_background), Mode.SRC_ATOP);
		
		productname1 = getIntent().getStringExtra("productname");
		producturl1 = getIntent().getStringExtra("producturl");
		
		UserId = getIntent().getStringExtra("userid");
		convId = getIntent().getStringExtra("convid");
		msgId = getIntent().getStringExtra("msgid");
	    System.out.println("asfasfasfasfas----"+productname1);
		
		productname.setText(productname1);
		producturl.setText(producturl1);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// some variable statements...
		
		switch (item.getItemId()) {
			case android.R.id.home:

				onBackPressed();
				this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				finish();
			    return true;
				
			default:
				return super.onOptionsItemSelected(item);
				
		}
	}
	
	
	
	
	
	
	// -------------------------code for Conversation Post Request----------------------------------			

				private void PostRequest(String Url) 
				{
					
//					chat_progressbar.setVisibility(View.VISIBLE);
//					chat_send_label.setText(getResources().getString(R.string.chat_popup_label_sending));
//					chat_dialog.setCanceledOnTouchOutside(false);
						
						 postrequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {

							@Override
							public void onResponse(String response) {

								System.out.println("--------------conversation post reponse-------------------"+response);

								String status="";
								try {
									
									JSONObject object=new JSONObject(response);
									
									status = object.getString("status");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
//								chat_dialog.setCanceledOnTouchOutside(true);
								
								if(status.equalsIgnoreCase("1"))
								{
//									
									Toast.makeText(getApplicationContext(), "message sent", Toast.LENGTH_LONG).show();
									finish();
									
								}
								else
								{
									Toast.makeText(getApplicationContext(), "Failed to send", Toast.LENGTH_LONG).show();
								}
								
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
								
								jsonParams.put("userId", UserId);
								jsonParams.put("receiverId", msgId);
								jsonParams.put("convId", convId);
								jsonParams.put("subject", producturl.getText().toString());
								jsonParams.put("message", message.getText().toString());
								return jsonParams;
							}
							
						};
						AppController.getInstance().addToRequestQueue(postrequest);  
				}
				
				
				
	
	
	
	
	
	
	//-----------------Move Back on pressed phone back button------------------		
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
			Ask_Quetiion.this.finish();
			Ask_Quetiion.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			return true;
		}
		return false;
	}		

}
