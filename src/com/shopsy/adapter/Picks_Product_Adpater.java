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
import com.shopsy.Pojo.Picks_Pojo;
import com.shopsy.Utils.ImageLoader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class Picks_Product_Adpater extends BaseAdapter 
{
	
	private ArrayList<Picks_Pojo> data;
	ImageLoader imageLoader;
	private LayoutInflater mInflater;
	private Context context;
			
	public Picks_Product_Adpater(Context c,ArrayList<Picks_Pojo> d) 
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
		//private ImageView favoriteheart;
		private ImageView image;
		private TextView tvName;
		private TextView tvprice;
		private TextView tvstorename;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;

		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.recommended_single, parent, false);
			holder = new ViewHolder();
			holder.tvName = (TextView) view.findViewById(R.id.recommended_grid_name);
			holder.tvprice = (TextView) view.findViewById(R.id.recommended_grid_price);
			holder.tvstorename = (TextView) view.findViewById(R.id.recommended_grid_storename);
			holder.image = (ImageView) view.findViewById(R.id.recommended_grid_image);
			//holder.favoriteheart = (ImageView) view.findViewById(R.id.recommended_favheart);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		holder.tvName.setText(data.get(position).getProductName().replace("&#8230;", "..."));
		holder.tvprice.setText(data.get(position).getPrice());
		holder.tvstorename.setText(data.get(position).getStoreName());
		ImageView image = holder.image;
		Picasso.with(context).load(String.valueOf(data.get(position).getImage())).placeholder(R.drawable.no_image_grey).memoryPolicy(MemoryPolicy.NO_CACHE).into(image);
		//imageLoader.DisplayImage(String.valueOf(data.get(position).getFeature_product_Image()), image);
		
		
		
		return view;
	}
}


