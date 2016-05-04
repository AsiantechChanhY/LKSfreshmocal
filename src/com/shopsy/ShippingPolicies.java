package com.shopsy;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ShippingPolicies extends ActionBarActivity
{
	ActionBar actionBar;
	Context context;
	TextView welcome, payment, shipping, refund, companynametext, additionalinfo, sellerinfo;
	TextView welcometitle, paymenttitle, shippingtitle, refundtitle, companynametexttitle, additionalinfotitle, sellerinfotitle;
	String companyname;
	ArrayList<String> policylist = new ArrayList<String>();
	ArrayList<String> policylistdata = new ArrayList<String>();
	
	ColorDrawable colorDrawable = new ColorDrawable();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shipping_policies);
		
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		init();
	}
	
	private void init()
	{
		welcome = (TextView) findViewById(R.id.policywelcome);
		payment = (TextView) findViewById(R.id.policypayment);
		shipping = (TextView) findViewById(R.id.policyshipping);
		refund = (TextView) findViewById(R.id.policyrefund);
		companynametext = (TextView) findViewById(R.id.policycompanyname);
		additionalinfo = (TextView) findViewById(R.id.policyAdditional);
		sellerinfo = (TextView) findViewById(R.id.policyseller);
		
		welcometitle = (TextView) findViewById(R.id.welcometitle);
		paymenttitle = (TextView) findViewById(R.id.paymenttitle);
		shippingtitle = (TextView) findViewById(R.id.shippingtitle);
		refundtitle = (TextView) findViewById(R.id.refundtitle);
		additionalinfotitle = (TextView) findViewById(R.id.Additionaltitle);
		sellerinfotitle = (TextView) findViewById(R.id.sellertitle);
		
		companyname = getIntent().getStringExtra("companyname");
		policylist = getIntent().getStringArrayListExtra("policy");
		
		companynametext.setText(companyname +" "+ context.getResources().getString(R.string.ship_label_shipping_policies_header));
		
		for (int i = 0; i < policylist.size(); i++)
		{
			policylistdata.add(policylist.get(i));
		}
		
		if (policylistdata.get(0).length() > 0)
		{
			welcome.setText(policylistdata.get(0));
		}
		else
		{
			welcome.setVisibility(View.GONE);
			welcometitle.setVisibility(View.GONE);
		}
		
		if (policylistdata.get(1).length() > 0)
		{
			payment.setText(policylistdata.get(1));
		}
		else
		{
			payment.setVisibility(View.GONE);
			paymenttitle.setVisibility(View.GONE);
		}
		
		if (policylistdata.get(2).length() > 0)
		{
			shipping.setText(policylistdata.get(2));
		}
		else
		{
			shipping.setVisibility(View.GONE);
			shippingtitle.setVisibility(View.GONE);
		}
		
		if (policylistdata.get(3).length() > 0)
		{
			refund.setText(policylistdata.get(3));
		}
		else
		{
			refund.setVisibility(View.GONE);
			refundtitle.setVisibility(View.GONE);
		}
		
		if (policylistdata.get(4).length() > 0)
		{
			additionalinfo.setText(policylistdata.get(4));
		}
		else
		{
			additionalinfo.setVisibility(View.GONE);
			additionalinfotitle.setVisibility(View.GONE);
		}
		
		if (policylistdata.get(5).length() > 0)
		{
			sellerinfo.setText(policylistdata.get(5));
		}
		else
		{
			sellerinfo.setVisibility(View.GONE);
			sellerinfotitle.setVisibility(View.GONE);
		}
		
		
		
		//code to disable actionBar title
		actionBar.setDisplayShowTitleEnabled(false);
		
		// code to set actionBar background
		colorDrawable.setColor(0xff1A237E);
		actionBar.setBackgroundDrawable(colorDrawable);
		
		//code to set back arrow
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		actionBar.setDisplayShowCustomEnabled(true);
		View view = getLayoutInflater().inflate(R.layout.shipping_policies_header, null);
		actionBar.setCustomView(view);
		
	}
	
	
	//------------------------code for Back Press----------------------------
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
		
		
		//-----------------Move Back on pressed phone back button------------------		
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) 
		{
			if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
				ShippingPolicies.this.finish();
				ShippingPolicies.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				return true;
			}
			return false;
		}		
}

