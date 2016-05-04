package com.shopsy.adapter;

import java.util.ArrayList;

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
import com.shopsy.Utils.RoundedImageView;
import com.squareup.picasso.Picasso;

public class DetailPage_Review_Adapter extends BaseAdapter 
{

	private ArrayList<Detail_Reviews_Pojo> data;
	private LayoutInflater mInflater;
	private Context context;

	public DetailPage_Review_Adapter(Context c,ArrayList<Detail_Reviews_Pojo> d)
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
		private RoundedImageView image;
		private TextView tvName;
		private TextView productname;
		private ImageView productimage;
		private TextView tvcomment,date;
		private RatingBar rating;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.detailpage_reviews_single, parent, false);
			holder = new ViewHolder();
			holder.tvName = (TextView) view.findViewById(R.id.detailpage_review_reviewername);
			holder.tvcomment = (TextView) view.findViewById(R.id.detailpage_review_comment);
			holder.date = (TextView) view.findViewById(R.id.detailpage_review_reviewdate);
			holder.image = (RoundedImageView) view.findViewById(R.id.detailpage_review_reviewerimage);
			holder.rating = (RatingBar) view.findViewById(R.id.detailpage_review_ratingBar);
			holder.productimage = (ImageView) view.findViewById(R.id.detailpage_review_productimage);
			holder.productname = (TextView) view.findViewById(R.id.detailpage_review_productname);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		holder.tvName.setText(data.get(position).getReviewer_name());
		holder.date.setText(data.get(position).getReviewer_time());
		holder.tvcomment.setText(data.get(position).getReviewer_comment());
		holder.rating.setRating(Float.parseFloat(data.get(position).getReviewer_rating()));
		holder.productname.setText(data.get(position).getReviewer_productname());

		Picasso.with(context).load(String.valueOf(data.get(position).getReviewer_image())).placeholder(R.drawable.nouserimg).into(holder.image);
		Picasso.with(context).load(String.valueOf(data.get(position).getReviewer_productimage())).placeholder(R.drawable.no_image_background).into(holder.productimage);
		
		return view;
	}
}

