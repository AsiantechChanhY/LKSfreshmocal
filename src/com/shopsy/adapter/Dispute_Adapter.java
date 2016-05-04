package com.shopsy.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopsy.R;
import com.shopsy.Pojo.Dispute_Pojo;
import com.squareup.picasso.Picasso;

public class Dispute_Adapter extends BaseAdapter 
{

	private ArrayList<Dispute_Pojo> data;
	private LayoutInflater mInflater;
	private Activity context;

	public Dispute_Adapter(Activity c,ArrayList<Dispute_Pojo> d)
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
		private RelativeLayout left_layout;
		private TextView left_message;
		private TextView left_time;
		private ImageView left_image;
		private TextView left_username;
		
		private FrameLayout right_layout;
		private TextView right_message;
		private TextView right_time;
		private ImageView right_image;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.dispute_single, parent, false);
			holder = new ViewHolder();
			
			holder.left_layout = (RelativeLayout) view.findViewById(R.id.dispute_left_side_layout);
			holder.left_message = (TextView) view.findViewById(R.id.dispute_left_side_message);
			holder.left_time = (TextView) view.findViewById(R.id.dispute_left_side_time);
			holder.left_image = (ImageView) view.findViewById(R.id.dispute_left_side_image);
			holder.left_username = (TextView) view.findViewById(R.id.dispute_left_side_username);
			
			
			holder.right_layout = (FrameLayout) view.findViewById(R.id.dispute_right_side_layout);
			holder.right_message = (TextView) view.findViewById(R.id.dispute_right_side_message);
			holder.right_time = (TextView) view.findViewById(R.id.dispute_right_side_time);
			holder.right_image = (ImageView) view.findViewById(R.id.dispute_right_side_image);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		

		if(data.get(position).getPostedBy().equalsIgnoreCase("You"))
		{
			holder.left_layout.setVisibility(View.GONE);
			holder.right_layout.setVisibility(View.VISIBLE);
			
			if(data.get(position).getPostMsg().length()>0)
			{
				holder.right_message.setVisibility(View.VISIBLE);
				holder.right_image.setVisibility(View.GONE);
				
				holder.right_message.setText(data.get(position).getPostMsg());
			}
			else
			{
				holder.right_message.setVisibility(View.GONE);
				holder.right_image.setVisibility(View.VISIBLE);
				
				Picasso.with(context).load(String.valueOf(data.get(position).getPostImg())).placeholder(R.drawable.no_image_grey).into(holder.right_image);
			}
			
			holder.right_time.setText(data.get(position).getPostTime());
		}
		else
		{
			holder.left_layout.setVisibility(View.VISIBLE);
			holder.right_layout.setVisibility(View.GONE);
			
			if(data.get(position).getPostMsg().length()>0)
			{
				holder.left_message.setVisibility(View.VISIBLE);
				holder.left_image.setVisibility(View.GONE);
				
				holder.left_username.setText(data.get(position).getPosterName());
				holder.left_message.setText(data.get(position).getPostMsg());
			}
			else
			{
				holder.left_message.setVisibility(View.GONE);
				holder.left_image.setVisibility(View.VISIBLE);
				
				holder.left_username.setText(data.get(position).getPosterName());
				Picasso.with(context).load(String.valueOf(data.get(position).getPostImg())).placeholder(R.drawable.no_image_grey).into(holder.left_image);
			}
			
			holder.left_time.setText(data.get(position).getPostTime());
		}
		
		
		
		return view;
	}
	
}
