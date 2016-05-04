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
import com.shopsy.Utils.SessionManager_Settings;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class Picks_Categories_Adapter extends BaseAdapter 
{
	
	private ArrayList<Picks_Pojo> data;
	ImageLoader imageLoader;
	private LayoutInflater mInflater;
	private Context context;
	SessionManager_Settings session_settings;
			
	public Picks_Categories_Adapter(Context c,ArrayList<Picks_Pojo> d) 
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
		private ImageView cat_image;
		private TextView cat_name;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;

		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.picks_categories_single, parent, false);
			holder = new ViewHolder();
			holder.cat_name = (TextView) view.findViewById(R.id.picks_category_name);
			holder.cat_image = (ImageView) view.findViewById(R.id.picks_category_image);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		/*HashMap<String, String> user = session_settings.getBandwidth();
		String Bandwidth = user.get(SessionManager_Settings.KEY_BANDWIDTH);*/
		
		//imageLoader.DisplayImage(String.valueOf(data.get(position).getCatimage()), holder.cat_image);
				/*if(Bandwidth.equalsIgnoreCase("low"))
				{
					imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getCatimage()), holder.cat_image, 2);
				}
				else
				{
					imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getCatimage()), holder.cat_image, 4);
				}*/
		
		holder.cat_name.setText(data.get(position).getCategoryName().replace("&#8230;", "..."));
		Picasso.with(context).load(String.valueOf(data.get(position).getCatimage())).placeholder(R.drawable.no_image_grey).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.cat_image);
		
		return view;
	}
}

