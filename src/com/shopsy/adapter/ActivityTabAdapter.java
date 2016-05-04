package com.shopsy.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopsy.R;
import com.shopsy.Pojo.ActivityTab_Pojo;
import com.shopsy.Utils.ImageLoader_BandWidth;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class ActivityTabAdapter extends BaseAdapter 
{

	private ArrayList<ActivityTab_Pojo> data;
	ImageLoader_BandWidth imageLoader_bandwidth;
	private LayoutInflater mInflater;
	private Context context;

	public ActivityTabAdapter(Context c,ArrayList<ActivityTab_Pojo> d)
	{
		context=c;
		mInflater = LayoutInflater.from(context);
		data = d;
		imageLoader_bandwidth = new ImageLoader_BandWidth(context);
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
		private ImageView type_icon,product_image;
		private TextView title,description,price,time;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.activitytab_single, parent, false);
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.activitytab_single_title);
			holder.description = (TextView) view.findViewById(R.id.activitytab_single_description);
			holder.price = (TextView) view.findViewById(R.id.activitytab_single_productprice);
			holder.time = (TextView) view.findViewById(R.id.activitytab_single_time);
			holder.type_icon = (ImageView) view.findViewById(R.id.activitytab_single_type_icon);
			holder.product_image = (ImageView) view.findViewById(R.id.activitytab_single_productimage);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		holder.title.setText(data.get(position).getActivitytext());
		holder.description.setText(data.get(position).getName());
		holder.price.setText(data.get(position).getCurrencySymbol()+""+data.get(position).getPrice());
		holder.time.setText(data.get(position).getActivitydate());
		
		if(data.get(position).getPrice().length()>0)
		{
			holder.price.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.price.setVisibility(View.GONE);
		}
		
		if(data.get(position).getType().equalsIgnoreCase("Order"))
		{
			holder.type_icon.setImageResource(R.drawable.order_icon);
		}
		else if(data.get(position).getType().equalsIgnoreCase("Like"))
		{
			holder.type_icon.setImageResource(R.drawable.like_icon);
		}
		else if(data.get(position).getType().equalsIgnoreCase("Favorite"))
		{
			holder.type_icon.setImageResource(R.drawable.favorite_icon);
		}
		else if(data.get(position).getType().equalsIgnoreCase("Dispute"))
		{
			holder.type_icon.setImageResource(R.drawable.dispute_icon);
		}
		
		Picasso.with(context).load(String.valueOf(data.get(position).getImage())).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.no_image_grey).into(holder.product_image);
		//imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getImage()), holder.product_image, 4);
		
		return view;
	}
}

