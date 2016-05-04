package com.shopsy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.shopsy.Utils.AppController;
import com.shopsy.Utils.ConnectionDetector;
import com.shopsy.Utils.SessionManager;

public class StatsTab  extends HockeyFragment 
{
	
	private Context context;
	private ActionBar actionBar;
	
	private SessionManager session;
	private String userid;
	
	private RelativeLayout loading,nointernet,mainlayout;
	private boolean show_progress_status=true;

	private Dialog dialog;
	
	JsonObjectRequest jsonObjReq;
	private ImageView stats_loading;

	private TextView tv_tax,tv_favorite,tv_revenue,tv_orders;
	private Spinner month_spinner;
	private String Stax="",Sfavorite="",Srevenue="",Sorders="";
	private List<String> list;
	
	private String asyntask_name="normal";
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview =inflater.inflate(R.layout.statstab,container,false);
        
        context = getActivity();
		ActionBarActivity actionBarActivity = (ActionBarActivity) getActivity();
		actionBar = actionBarActivity.getSupportActionBar();
		initialize(rootview);
		
		month_spinner.setOnItemSelectedListener(
				new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View arg1,
							int position, long arg3) {

						String selected_item = parent.getItemAtPosition(position).toString();
						
						ConnectionDetector cd = new ConnectionDetector(getActivity());
						boolean isInternetPresent = cd.isConnectingToInternet();
						
						if(isInternetPresent)
						{
							if (selected_item.equalsIgnoreCase(getResources().getString(R.string.stats_label_today)))
							{
								JsonRequest(Iconstant.statsTab_url + userid+ "&viewby=today");
							}
							else if (selected_item.equals(getResources().getString(R.string.stats_label_last7days)))
							{
								JsonRequest(Iconstant.statsTab_url + userid+ "&viewby=last7");
							}
							else if (selected_item.equals(getResources().getString(R.string.stats_label_week)))
							{
								JsonRequest(Iconstant.statsTab_url + userid+ "&viewby=week");
							}
							else if (selected_item.equals(getResources().getString(R.string.stats_label_last30days)))
							{
								JsonRequest(Iconstant.statsTab_url + userid+ "&viewby=last30");
							}
							else if (selected_item.equals(getResources().getString(R.string.stats_label_thisMonth)))
							{
								JsonRequest(Iconstant.statsTab_url + userid+ "&viewby=month");
							}
							else if (selected_item.equals(getResources().getString(R.string.stats_label_viewall)))
							{
								JsonRequest(Iconstant.statsTab_url + userid+ "&viewby=all");
							}
						}
						else
						{
							Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
						}
					}
					
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				}
				);
		
		
        return rootview;
    }

	private void initialize(View rootview)
	{

		session = new SessionManager(getActivity());	
	
		loading=(RelativeLayout)rootview.findViewById(R.id.statstab_loading_layout);
		nointernet=(RelativeLayout)rootview.findViewById(R.id.statstab_nointernet_layout);
		mainlayout=(RelativeLayout)rootview.findViewById(R.id.statstab_main_layout);
		stats_loading=(ImageView)rootview.findViewById(R.id.statstab_loading_gif);
		
		tv_tax=(TextView)rootview.findViewById(R.id.stats_tab_tax_textview);
		tv_favorite=(TextView)rootview.findViewById(R.id.stats_tab_favorite_textview);
		tv_revenue=(TextView)rootview.findViewById(R.id.stats_tab_revenue_textview);
		tv_orders=(TextView)rootview.findViewById(R.id.stats_tab_order_textview);
		
		month_spinner=(Spinner)rootview.findViewById(R.id.stats_tab_spinner_date);
		
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

				
		// code to disable actionBar title
		actionBar.setDisplayShowTitleEnabled(false);

		// ---------code to add custom menu in action bar------------
			actionBar.setDisplayShowCustomEnabled(true);
				 
			 Glide.with(context)
				   .load(R.drawable.loadinganimation)
				   .asGif()
				   .crossFade()
				   .diskCacheStrategy(DiskCacheStrategy.SOURCE)
				   .into(stats_loading);
			 
			 
			 	list = new ArrayList<String>();
		        list.add(getResources().getString(R.string.stats_label_today));
		        list.add(getResources().getString(R.string.stats_label_last7days));
		        list.add(getResources().getString(R.string.stats_label_week));
		        list.add(getResources().getString(R.string.stats_label_last30days));
		        list.add(getResources().getString(R.string.stats_label_thisMonth));
		        list.add(getResources().getString(R.string.stats_label_viewall));
		        
		        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.stats_spinnertext, list);
				dayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_background);
				month_spinner.setAdapter(dayAdapter);
				month_spinner.setSelection(0);
				
				
				 dialog = new Dialog(getActivity());
	             dialog.getWindow();
	             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);   
	             dialog.setContentView(R.layout.custom_loading_dialog);
	             dialog.setCanceledOnTouchOutside(false);
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
					mainlayout.setVisibility(View.VISIBLE);
					nointernet.setVisibility(View.GONE);
					loading.setVisibility(View.GONE);
					
					 dialog.show();
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
					loading.setVisibility(View.GONE);
					
					dialog.dismiss();
				}
			}
	
	// -------------------------code for JSon Request----------------------------------
	
	private void JsonRequest(String Url) 
	{
		startload();
		
		System.out.println("--------------Stats tab url-------------------"+Url);
		
		 jsonObjReq = new JsonObjectRequest(Method.GET,Url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) 
					{
						System.out.println("--------------Stats tab response -----------"+response);

						try {

							JSONArray jarray = response.getJSONArray("stats");
							
							if (jarray.length() > 0) 
							{
								for (int i = 0; i < jarray.length(); i++)
								{
									JSONObject object = jarray.getJSONObject(i);
									
									Stax=object.getString("currencySymbol")+object.getString("Tax");
									Sfavorite=object.getString("Favorite");
									Srevenue=object.getString("currencySymbol")+object.getString("Revenue");
									Sorders=object.getString("Orders");
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
						
						 stopload();
						 
						 asyntask_name="spinner";
						 
						if(show_progress_status)
						{
							dataCheck(tv_tax,Stax);
							dataCheck(tv_favorite,Sfavorite);
							dataCheck(tv_revenue,Srevenue);
							dataCheck(tv_orders,Sorders);
						}
						else
						{
							mainlayout.setVisibility(View.VISIBLE);
							nointernet.setVisibility(View.GONE);
							
							tv_tax.setText(getResources().getString(R.string.stats_label_empty_novalue));
							tv_favorite.setText(getResources().getString(R.string.stats_label_empty_novalue));
							tv_revenue.setText(getResources().getString(R.string.stats_label_empty_novalue));
							tv_orders.setText(getResources().getString(R.string.stats_label_empty_novalue));
						} 
					
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) 
					{
						 asyntask_name="spinner";
						 stopload();
						 
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
	
	//--------methos to check data is present or not------
	private void dataCheck(TextView textview,String data)
	{
		if(data.length()>0)
		{
			textview.setText(data);
		}
		else
		{
			textview.setText(getResources().getString(R.string.stats_label_empty_novalue));
		}
	}
	
	// --------------------------code to destroy asynTask when another activity is called---------------------------
	@Override
	public void onDestroy() 
	{
		    if(jsonObjReq!=null)
			{
				jsonObjReq.cancel();
			}
		super.onDestroy();
	}	

}
