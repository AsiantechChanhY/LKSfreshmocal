package com.shopsy.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopsy.R;
import com.shopsy.Pojo.YourItem_Pojo;
import com.shopsy.Utils.ImageLoader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class YourItem_Adapter extends BaseAdapter 
{

	private ArrayList<YourItem_Pojo> data;
	private LayoutInflater mInflater;
	private Activity context;
	ImageLoader imageLoader;

	public YourItem_Adapter(Activity c,ArrayList<YourItem_Pojo> d)
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
		private TextView productname;
		private TextView instock;
		private TextView price;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.your_item_single, parent, false);
			holder = new ViewHolder();
			
			holder.productimage = (ImageView) view.findViewById(R.id.your_item_single_productimage);
			holder.productname = (TextView) view.findViewById(R.id.your_item_single_productname);
			holder.instock = (TextView) view.findViewById(R.id.your_item_single_instock);
			holder.price = (TextView) view.findViewById(R.id.your_item_single_price);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		holder.productname.setText(data.get(position).getProductName());
		holder.instock.setText(data.get(position).getStock()+" "+":"+" "+data.get(position).getQuantity());
		holder.price.setText(data.get(position).getPrice());
		
		
		Picasso.with(context).load(String.valueOf(data.get(position).getImage())).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.no_image_background_profile).into(holder.productimage);
		//imageLoader.DisplayImage(String.valueOf(data.get(position).getImage()), holder.productimage);
		
		return view;
	}
	
}



