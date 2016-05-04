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
import com.shopsy.Pojo.Recommended_Pojo;
import com.shopsy.Utils.CircularImageView;
import com.shopsy.Utils.ImageLoader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class Recommended_Shop_Adapter extends BaseAdapter 
{
	
	private ArrayList<Recommended_Pojo> data;
	ImageLoader imageLoader;
	private LayoutInflater mInflater;
	private Context context;
			
	public Recommended_Shop_Adapter(Context c,ArrayList<Recommended_Pojo> d) 
	{
		context=c;
		mInflater = LayoutInflater.from(context);
		data = d;
		imageLoader = new ImageLoader(context);
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
		private ImageView productimage;
		private CircularImageView userimage;
		private TextView username;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;

		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.recommended_shop_single, parent, false);
			holder = new ViewHolder();
			holder.username = (TextView) view.findViewById(R.id.recommended_shop_single_name);
			holder.productimage = (ImageView) view.findViewById(R.id.recommended_shop_single_image);
			holder.userimage = (CircularImageView) view.findViewById(R.id.recommended_shop_single_userimage);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		holder.username.setText(data.get(position).getFeature_shop_fullName());
		ImageView image = holder.productimage;
		//imageLoader.DisplayImage(String.valueOf(data.get(position).getFeature_shop_seller_banner()), image);

		Picasso.with(context).load(String.valueOf(data.get(position).getFeature_shop_seller_banner())).placeholder(R.drawable.no_image_grey).memoryPolicy(MemoryPolicy.NO_CACHE).into(image);
		Picasso.with(context).load(String.valueOf(data.get(0).getFeature_shop_profilePic())).placeholder(R.drawable.nouserimg).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.userimage);
		
		
		return view;
	}
}


