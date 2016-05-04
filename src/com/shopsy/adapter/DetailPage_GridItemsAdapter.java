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
import com.shopsy.Pojo.Detail_shopItems_Pojo;
import com.shopsy.Utils.ImageLoader_BandWidth;
import com.shopsy.Utils.SessionManager_Settings;

public class DetailPage_GridItemsAdapter extends BaseAdapter 
{

	private ArrayList<Detail_shopItems_Pojo> data;
	private ImageLoader_BandWidth imageLoader_bandwidth;
	private LayoutInflater mInflater;
	private Context context;
	private SessionManager_Settings session_settings;

	public DetailPage_GridItemsAdapter(Context c,ArrayList<Detail_shopItems_Pojo> d)
	{
		context=c;
		mInflater = LayoutInflater.from(context);
		data = d;
		imageLoader_bandwidth = new ImageLoader_BandWidth(context);
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
		private ImageView image;
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
			view = mInflater.inflate(R.layout.detailpage_griditems_single, parent, false);
			holder = new ViewHolder();
			holder.tvName = (TextView) view.findViewById(R.id.detail_item_productname);
			holder.tvprice = (TextView) view.findViewById(R.id.detail_item_productprice);
			holder.image = (ImageView) view.findViewById(R.id.detail_item_grid_image);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		HashMap<String, String> user = session_settings.getBandwidth();
		String Bandwidth = user.get(SessionManager_Settings.KEY_BANDWIDTH);
		
		holder.tvName.setText(data.get(position).getShop_name().replace("&#8230;", "..."));
		holder.tvprice.setText(data.get(position).getShop_price());
		ImageView image = holder.image;
		
		if(Bandwidth.equalsIgnoreCase("low"))
		{
			imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getShop_image()), image, 2);
		}
		else
		{
			imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getShop_image()), image, 4);
		}
		
		return view;
	}
}
