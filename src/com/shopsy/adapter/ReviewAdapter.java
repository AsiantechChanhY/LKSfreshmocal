package com.shopsy.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopsy.R;
import com.shopsy.ReviewsPage_Seller;
import com.shopsy.Pojo.Reviews_Pojo;
import com.shopsy.Utils.RoundedImageView;
import com.squareup.picasso.Picasso;

public class ReviewAdapter extends BaseAdapter 
{

	private ArrayList<Reviews_Pojo> data;
	private LayoutInflater mInflater;
	private Activity context;

	public ReviewAdapter(Activity c,ArrayList<Reviews_Pojo> d)
	{
		context=c;
		mInflater = LayoutInflater.from(context);
		data = d;
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
		private RoundedImageView userimage;
		private TextView message;
		private TextView time;
		private RatingBar rating;
		private RelativeLayout report;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.reviews_single, parent, false);
			holder = new ViewHolder();
			
			holder.userimage = (RoundedImageView) view.findViewById(R.id.review_single_userimage);
			holder.message = (TextView) view.findViewById(R.id.review_single_content);
			holder.time = (TextView) view.findViewById(R.id.review_single_date);
			holder.time = (TextView) view.findViewById(R.id.review_single_date);
			holder.rating = (RatingBar) view.findViewById(R.id.review_single_rating);
			holder.report = (RelativeLayout) view.findViewById(R.id.review_single_reply);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		holder.message.setText(data.get(position).getDescription());
		holder.time.setText(data.get(position).getReviewDate());
		holder.rating.setRating(Float.valueOf(data.get(position).getStarRating()));
		Picasso.with(context).load(String.valueOf(data.get(position).getVoterImage())).placeholder(R.drawable.nouserimg).into(holder.userimage);
		
		holder.report.setOnClickListener(new RepostOnclick(position));
		
		return view;
	}
	
	
	public class RepostOnclick implements OnClickListener 
	{
		int mposition;
		public RepostOnclick(int position) 
		{
			mposition=position;
		}
		@Override
		public void onClick(View v) 
		{
			ReviewsPage_Seller report=(ReviewsPage_Seller)context;
			report.reportMessage(mposition,data.get(mposition).getVoteId());
			
		}
	}
	
}

