package com.shopsy.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopsy.R;
import com.shopsy.Pojo.AllShop_Product_Pojo;
import com.shopsy.StaggeredGridView.DynamicHeightImageView;
import com.shopsy.Utils.ImageLoader;
import com.shopsy.Utils.SessionManager_Settings;

public class AllShop_Product_Adapter extends BaseAdapter 
{

	private ArrayList<AllShop_Product_Pojo> data;
	private ImageLoader imageLoader;
	private LayoutInflater mInflater;
	private Context context;
	private SessionManager_Settings session_settings;

	public AllShop_Product_Adapter(Context c,ArrayList<AllShop_Product_Pojo> d)
	{
		context=c;
		mInflater = LayoutInflater.from(context);
		data = d;
		imageLoader = new ImageLoader(context);
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
		private DynamicHeightImageView image;
		private TextView tvName;
		private TextView tvprice;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.allshop_product_single, parent, false);
			holder = new ViewHolder();
			holder.tvName = (TextView) view.findViewById(R.id.allshop_product_productname);
			holder.tvprice = (TextView) view.findViewById(R.id.allshop_product_productprice);
			holder.image = (DynamicHeightImageView) view.findViewById(R.id.allshop_product_grid_image);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		HashMap<String, String> user = session_settings.getBandwidth();
		String Bandwidth = user.get(SessionManager_Settings.KEY_BANDWIDTH);
		
		holder.tvName.setText(data.get(position).getProductName().replace("&#8230;", "..."));
		holder.tvprice.setText(data.get(position).getPrice());
		ImageView image = holder.image;
		
		if(Bandwidth.equalsIgnoreCase("low"))
		{
			imageLoader.DisplayImage(String.valueOf(data.get(position).getProductImage()), image);
		}
		else
		{
			imageLoader.DisplayImage(String.valueOf(data.get(position).getProductImage()), image);
		}
		
		return view;
	}
}
