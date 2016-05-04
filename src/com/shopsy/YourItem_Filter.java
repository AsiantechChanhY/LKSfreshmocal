package com.shopsy;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.YourItemPage.FILTER;
import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;

public class YourItem_Filter extends HockeyActivity implements OnClickListener
{
	private ActionBar actionBar;
	private TextView apply;
	
	RelativeLayout Ly_All, Ly_Active, Ly_Draft, Ly_Expired, Ly_Sold_out, Ly_In_Active;
	TextView Tv_All, Tv_Active, Tv_Draft, Tv_Expired, Tv_Slod_Out, Tv_In_Active;
	ImageView Iv_All, Iv_Active, Iv_Draft, Iv_Expried, Iv_Sold_out, Iv_In_Active;
	Spinner sort_spinner;
	private String[] sort_list;
	public String Sort_Item="";
	public String Item_status="";
	
	private	Boolean isInternetPresent = false;
	private	ConnectionDetector cd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youritem_filter);
		
  		ActionBarActivity actionBarActivity = (ActionBarActivity)YourItem_Filter.this;
  		actionBar = actionBarActivity.getSupportActionBar();
  		initialize();
  		
  		apply.setOnClickListener(new OnClickListener()
  		{
			@Override
			public void onClick(View v) 
			{
				
				cd = new ConnectionDetector(YourItem_Filter.this);
				isInternetPresent = cd.isConnectingToInternet();
				
				if(isInternetPresent)
				{
					if (Sort_Item.equals(Iconstant.Tittle_A_to_Z))
					{
						FILTER.sorting(Iconstant.List_Tittle_Asc, Item_status);
					} 
					else if (Sort_Item.equals(Iconstant.Tittle_Z_to_A)) 
					{
						FILTER.sorting(Iconstant.List_Tittle_Dsc, Item_status);
					}
					else if (Sort_Item.equals(Iconstant.Stock_Low_to_High))
					{
						FILTER.sorting(Iconstant.List_Stock_Asc, Item_status);
					}
					else if (Sort_Item.equals(Iconstant.Stock_High_to_Low)) 
					{
						FILTER.sorting(Iconstant.List_Stock_Dsc, Item_status);
					} 
					else if (Sort_Item.equals(Iconstant.Price_Low_to_High))
					{
						FILTER.sorting(Iconstant.List_Price_Asc, Item_status);
					} 
					else if (Sort_Item.equals(Iconstant.Price_High_to_Low))
					{
						FILTER.sorting(Iconstant.List_Price_Dsc, Item_status);
					}
					
					YourItem_Filter.this.finish();
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				}
				else
				{
					Toast.makeText(YourItem_Filter.this, "No internet connection", Toast.LENGTH_SHORT).show();
				}
			}
		});
  		
  		sort_spinner.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			public void onItemSelected(AdapterView<?> parent,View view, int position, long id)
			{
				Sort_Item = parent.getItemAtPosition(position).toString();
			}
			public void onNothingSelected(AdapterView<?> parent) 
			{
			}
		});
	}

	private void initialize()
	{

		// code to disable ActionBar title
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
				
		actionBar.setDisplayShowCustomEnabled(true);
		View view = YourItem_Filter.this.getLayoutInflater().inflate(R.layout.youritem_filter_header, null);
		actionBar.setCustomView(view);
		
		apply=(TextView)view.findViewById(R.id.youritem_filter_header_apply);
		
		
		Ly_All = (RelativeLayout) findViewById(R.id.linear_filter_all);
		Ly_Active = (RelativeLayout) findViewById(R.id.linear_filter_active);
		Ly_Draft = (RelativeLayout) findViewById(R.id.linear_filter_draft);
		Ly_Expired = (RelativeLayout) findViewById(R.id.linear_filter_expired);
		Ly_Sold_out = (RelativeLayout) findViewById(R.id.linear_filter_sold);
		Ly_In_Active = (RelativeLayout) findViewById(R.id.linear_filter_inactive);
		sort_spinner = (Spinner) findViewById(R.id.spinner_filter);
		
		Tv_All = (TextView) findViewById(R.id.textView_filter_all);
		Tv_Active = (TextView) findViewById(R.id.textView_filter_active);
		Tv_Draft = (TextView) findViewById(R.id.textView_filter_draft);
		Tv_Expired = (TextView) findViewById(R.id.textView_filter_expired);
		Tv_Slod_Out = (TextView) findViewById(R.id.textView_filter_sold);
		Tv_In_Active = (TextView) findViewById(R.id.textView_filter_inactive);
		sort_list = getResources().getStringArray(R.array.youritem_sort_list);
		
		Iv_All = (ImageView) findViewById(R.id.imageView_filter_all);
		Iv_Active = (ImageView) findViewById(R.id.imageView_filter_active);
		Iv_Draft = (ImageView) findViewById(R.id.imageView_filter_draft);
		Iv_Expried = (ImageView) findViewById(R.id.imageView_filter_expired);
		Iv_Sold_out = (ImageView) findViewById(R.id.imageView_filter_sold);
		Iv_In_Active = (ImageView) findViewById(R.id.imageView_filter_inactive);
		
		Iv_All.setVisibility(View.GONE);
		Iv_Active.setVisibility(View.GONE);
		Iv_Draft.setVisibility(View.GONE);
		Iv_Expried.setVisibility(View.GONE);
		Iv_Sold_out.setVisibility(View.GONE);
		Iv_In_Active.setVisibility(View.GONE);
		
		
		sort_list = getResources().getStringArray(R.array.youritem_sort_list);
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sort_list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sort_spinner.setAdapter(dataAdapter);
		
		
		Ly_All.setOnClickListener(this);
		Ly_Active.setOnClickListener(this);
		Ly_Draft.setOnClickListener(this);
		Ly_Expired.setOnClickListener(this);
		Ly_Sold_out.setOnClickListener(this);
		Ly_In_Active.setOnClickListener(this);
		
		Tv_All.setOnClickListener(this);
		Tv_Active.setOnClickListener(this);
		Tv_Draft.setOnClickListener(this);
		Tv_Expired.setOnClickListener(this);
		Tv_Slod_Out.setOnClickListener(this);
		Tv_In_Active.setOnClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) {
			case android.R.id.home:

				onBackPressed();
				overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
				finish();
			    return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onClick(View v) 
	{
		
		switch (v.getId()) 
		{
		case R.id.linear_filter_all:
			
			Item_status=Iconstant.List_All;
			
			Iv_All.setVisibility(View.VISIBLE);
			Iv_Active.setVisibility(View.GONE);
			Iv_Draft.setVisibility(View.GONE);
			Iv_Expried.setVisibility(View.GONE);
			Iv_Sold_out.setVisibility(View.GONE);
			Iv_In_Active.setVisibility(View.GONE);
			
			break;
		case R.id.linear_filter_active:
			
			Item_status=Iconstant.List_Active;
			
			Iv_All.setVisibility(View.GONE);
			Iv_Active.setVisibility(View.VISIBLE);
			Iv_Draft.setVisibility(View.GONE);
			Iv_Expried.setVisibility(View.GONE);
			Iv_Sold_out.setVisibility(View.GONE);
			Iv_In_Active.setVisibility(View.GONE);
			
			break;
			
		case R.id.linear_filter_sold:
			
			Item_status=Iconstant.List_SoldOut;
			
			Iv_All.setVisibility(View.GONE);
			Iv_Active.setVisibility(View.GONE);
			Iv_Draft.setVisibility(View.GONE);
			Iv_Expried.setVisibility(View.GONE);
			Iv_Sold_out.setVisibility(View.VISIBLE);
			Iv_In_Active.setVisibility(View.GONE);
			
			break;		
		case R.id.linear_filter_inactive:
			
			Item_status=Iconstant.List_InActive;
			
			Iv_All.setVisibility(View.GONE);
			Iv_Active.setVisibility(View.GONE);
			Iv_Draft.setVisibility(View.GONE);
			Iv_Expried.setVisibility(View.GONE);
			Iv_Sold_out.setVisibility(View.GONE);
			Iv_In_Active.setVisibility(View.VISIBLE);
	
			break;
		
		default:
			break;
		}
		
	}
	
	
	//-----------------Move Back on pressed phone back button------------------		
	
			@Override
			public boolean onKeyDown(int keyCode, KeyEvent event) 
			{
				if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
					YourItem_Filter.this.finish();
					YourItem_Filter.this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
					return true;
				}
				return false;
			}	
}
