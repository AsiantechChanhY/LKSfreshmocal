package com.shopsy.adapter;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopsy.R;
import com.shopsy.Utils.CircularImageView;
import com.shopsy.Utils.CommonIDSessionManager;
import com.shopsy.Utils.ProfileImageloader;
import com.shopsy.Utils.SessionManager;
import com.squareup.picasso.Picasso;

public class HomeMenuListAdapter extends BaseAdapter 
{
	Context context;
	String[] mTitle;
	int[] mIcon;
	LayoutInflater inflater;
	//calling login session
  	public SessionManager session;
 	CommonIDSessionManager commonsession;
 	public ProfileImageloader imageLoader;
 	View itemView;

	public HomeMenuListAdapter(Context context, String[] title, int[] icon) 
	{
		this.context = context;
		this.mTitle = title;
		this.mIcon = icon;
		imageLoader = new ProfileImageloader(context);
	}

	@Override
	public int getCount() 
	{
		return mTitle.length;
	}

	@Override
	public Object getItem(int position) 
	{
		return mTitle[position];
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// Declare Variables
		TextView txtTitle,profile_title,profile_email;
		CircularImageView profile_icon;
		ImageView imgIcon;
		RelativeLayout general_layout,profile_layout;
		View drawer_view;
		
		// Session class instance
		session = new SessionManager(context);
		commonsession = new CommonIDSessionManager(context);

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		itemView = inflater.inflate(R.layout.drawer_list_item, parent, false);

		txtTitle = (TextView) itemView.findViewById(R.id.title);
		profile_email= (TextView) itemView.findViewById(R.id.profile_email_title);
		imgIcon = (ImageView) itemView.findViewById(R.id.icon);
		profile_title = (TextView) itemView.findViewById(R.id.profile_title);
		profile_icon = (CircularImageView) itemView.findViewById(R.id.profile_icon);
		general_layout = (RelativeLayout) itemView.findViewById(R.id.drawer_list_item_normal_layout);
		profile_layout = (RelativeLayout) itemView.findViewById(R.id.drawer_list_item_profile_layout);
		drawer_view=(View)itemView.findViewById(R.id.drawer_list_view);
		


		if (session.isLoggedIn())
		{
			if(position==0)
			{
				HashMap<String, String> user = session.getUserDetails();
		        String UserprofileImage=user.get(SessionManager.KEY_USERIMAGE);
		        String User_fullname=user.get(SessionManager.KEY_NAME);
		        String Email=user.get(SessionManager.KEY_EMAIL);
		        
		        profile_layout.setVisibility(View.VISIBLE);
		        general_layout.setVisibility(View.GONE);
		        drawer_view.setVisibility(View.GONE);
		        
		        Picasso.with(context).load(String.valueOf(UserprofileImage)).placeholder(R.drawable.nouserimg).into(profile_icon);
		        profile_title.setText(User_fullname);
		        profile_email.setText(Email);
			}
			else
			{
				
				if(position==3)
				{
					drawer_view.setVisibility(View.VISIBLE);
				}
				else
				{
					drawer_view.setVisibility(View.GONE);
				}
				   
				profile_layout.setVisibility(View.GONE);
			    general_layout.setVisibility(View.VISIBLE);
			        
			    imgIcon.setImageResource(mIcon[position]);
			    txtTitle.setText(mTitle[position]);
			}
		}
		else
		{

			if(position==0)
			{
		        profile_layout.setVisibility(View.VISIBLE);
		        general_layout.setVisibility(View.GONE);
		        drawer_view.setVisibility(View.GONE);
		        
		        profile_title.setText(context.getResources().getString(R.string.drawer_list_signin_label));
		        profile_email.setVisibility(View.GONE);
			}
			else
			{
				
				if(position==2)
				{
					drawer_view.setVisibility(View.VISIBLE);
				}
				else
				{
					drawer_view.setVisibility(View.GONE);
				}
				   
				profile_layout.setVisibility(View.GONE);
			    general_layout.setVisibility(View.VISIBLE);
			        
			    imgIcon.setImageResource(mIcon[position]);
			    txtTitle.setText(mTitle[position]);
			}
		}
			
		

		return itemView;
	}
}
