package com.shopsy.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shopsy.R;
import com.shopsy.Pojo.Detail_Reviews_Pojo;
import com.shopsy.Utils.ImageLoader_BandWidth;
import com.shopsy.Utils.RoundedImageView;
import com.shopsy.Utils.SessionManager_Settings;
import com.squareup.picasso.Picasso;

public class SeeAllReview_Adapter extends BaseAdapter 
{

	private ArrayList<Detail_Reviews_Pojo> data;
	private LayoutInflater mInflater;
	private Context context;
	public ImageLoader_BandWidth imageLoader_bandwidth;
	private SessionManager_Settings session_settings;

	public SeeAllReview_Adapter(Context c,ArrayList<Detail_Reviews_Pojo> d)
	{
		context=c;
		mInflater = LayoutInflater.from(context);
		data = d;
		imageLoader_bandwidth= new ImageLoader_BandWidth(context);
		session_settings= new SessionManager_Settings(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() 
	{
		return 1;
	}
	

	public class ViewHolder 
	{
		private RoundedImageView image;
		private TextView tvName;
		private TextView tvcomment,date,productname;
		private ImageView productimage;
		private RatingBar rating;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.see_all_review_single, parent, false);
			holder = new ViewHolder();
			holder.tvName = (TextView) view.findViewById(R.id.seeall_review_single_reviewername);
			holder.tvcomment = (TextView) view.findViewById(R.id.seeall_review_single_comment);
			holder.date = (TextView) view.findViewById(R.id.seeall_review_single_reviewdate);
			holder.image = (RoundedImageView) view.findViewById(R.id.seeall_review_single_reviewerimage);
			holder.rating = (RatingBar) view.findViewById(R.id.seeall_review_single_ratingBar);
			holder.productimage = (ImageView) view.findViewById(R.id.seeall_review_single_productimage);
			holder.productname = (TextView) view.findViewById(R.id.seeall_review_single_productname);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		holder.tvName.setText(data.get(position).getReviewer_name());
		holder.productname.setText(data.get(position).getReviewer_productname());
		holder.date.setText(data.get(position).getReviewer_time());
		holder.tvcomment.setText(data.get(position).getReviewer_comment());
		holder.rating.setRating(Float.parseFloat(data.get(position).getReviewer_rating()));

		Picasso.with(context).load(String.valueOf(data.get(position).getReviewer_image())).placeholder(R.drawable.nouserimg).into(holder.image);
		
		HashMap<String, String> user = session_settings.getBandwidth();
		String Bandwidth = user.get(SessionManager_Settings.KEY_BANDWIDTH);
		
		if(Bandwidth.equalsIgnoreCase("low"))
		{
			imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getReviewer_productimage()), holder.productimage, 2);
		}
		else
		{
			imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getReviewer_productimage()), holder.productimage, 4);
		}
		return view;
	}
}

