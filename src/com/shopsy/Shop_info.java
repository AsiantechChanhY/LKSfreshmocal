package com.shopsy;

import java.io.UnsupportedEncodingException;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Shop_info extends ActionBarActivity{
	
	
	Button policy,announcement;
	RelativeLayout Policy_layout,announcement_layout;
	TextView t1,t2;
	
	
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
//	private RelativeLayout cart;
//	private RelativeLayout cartcountlayout;
//	private TextView cartcount;
	private String policy1,aboutus;
	private TextView header_title;
	
	
	
	@Override

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop);
		actionBar = getSupportActionBar();
		initialize();
		
//		cart.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v) 
//			{
//				Intent intent=new Intent(Shop_info.this,CartPage.class);
//				startActivity(intent);
//				overridePendingTransition(R.anim.enter, R.anim.exit);
//			}
//		});
          policy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Policy_layout.setVisibility(View.VISIBLE);
				announcement_layout.setVisibility(View.GONE);
				
			}
		});
		announcement.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Policy_layout.setVisibility(View.GONE);
				announcement_layout.setVisibility(View.VISIBLE);
				
			}
		});
		
	}
	private void initialize()
	{
		// code to set actionbar background
      	colorDrawable.setColor(0xff1A237E);
      	actionBar.setBackgroundDrawable(colorDrawable);
	
	   // code to disable actionbar title
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		// ------------------code to add custom menu in action bar--------------------------------------
		actionBar.setDisplayShowCustomEnabled(true);
					View view = Shop_info.this.getLayoutInflater().inflate(R.layout.shop_header, null);
					actionBar.setCustomView(view);

//					cart = (RelativeLayout) view.findViewById(R.id.category_header_cartrelativelayout);
//					cartcount = (TextView) view.findViewById(R.id.category_header_cartcounttext);
//					cartcountlayout = (RelativeLayout) view.findViewById(R.id.category_header_cartcountlayout);
					header_title=(TextView)view.findViewById(R.id.category_header_title);
		
		
		
		policy=(Button)findViewById(R.id.button1);
		announcement=(Button)findViewById(R.id.button2);
		Policy_layout=(RelativeLayout)findViewById(R.id.shop_info_policy_layout);
		announcement_layout=(RelativeLayout)findViewById(R.id.shop_info_announcment_layout);
		t1=(TextView)findViewById(R.id.textView1);
		t2=(TextView)findViewById(R.id.textView2);
		
		try {
			policy1 =  java.net.URLDecoder.decode(getIntent().getStringExtra("policy"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			aboutus =  java.net.URLDecoder.decode(getIntent().getStringExtra("aboutus"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		t1.setText(Html.fromHtml(policy1));
		t2.setText(Html.fromHtml(aboutus));
		
		header_title.setText("Shop Information");
		
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) {
			case android.R.id.home:

				onBackPressed();
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				finish();
			    return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	

	
	
	
		

}
