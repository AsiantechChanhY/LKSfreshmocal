package com.shopsy.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopsy.R;
import com.shopsy.SearchPage;
import com.shopsy.Pojo.Search_Pojo;
import com.shopsy.StaggeredGridView.DynamicHeightImageView;
import com.shopsy.Utils.ImageLoader_BandWidth;
import com.shopsy.Utils.SessionManager_Settings;

public class SearchAdapter extends BaseAdapter 
{

	private ArrayList<Search_Pojo> data;
	private ImageLoader_BandWidth imageLoader_bandwidth;
	private LayoutInflater mInflater;
	private Activity context;
	private SessionManager_Settings session_settings;

	public SearchAdapter(Activity c,ArrayList<Search_Pojo> d)
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
		private ImageView favoriteheart;
		private DynamicHeightImageView image;
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
			view = mInflater.inflate(R.layout.trending_gridsingle, parent, false);
			holder = new ViewHolder();
			holder.tvName = (TextView) view.findViewById(R.id.trending_grid_name);
			holder.tvprice = (TextView) view.findViewById(R.id.trending_grid_price);
			holder.tvstorename = (TextView) view.findViewById(R.id.trending_grid_storename);
			holder.image = (DynamicHeightImageView) view.findViewById(R.id.trending_grid_image);
			holder.favoriteheart = (ImageView) view.findViewById(R.id.trending_favheart);
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
		holder.tvstorename.setText(data.get(position).getStorename());
		ImageView image = holder.image;
		//imageLoader.DisplayImage(String.valueOf(data.get(position).getProductImage()), image);

		if(Bandwidth.equalsIgnoreCase("low"))
		{
			imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getProductImage()), image, 2);
		}
		else
		{
			imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getProductImage()), image, 4);
		}
		
		// code to heart color change when Url loads
		if (data.get(position).getFavstatus().contains("1")) 
		{
			holder.favoriteheart.setImageResource(R.drawable.favredtwo);
		}
		else
		{
			holder.favoriteheart.setImageResource(R.drawable.favtwo);
		}
			
		holder.favoriteheart.setOnClickListener(new OnItemClickListenerheart(position));
		
		return view;
	}

	private class OnItemClickListenerheart implements OnClickListener 
	{
		private final int mPosition;

		OnItemClickListenerheart(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) 
		{
			
			if (data.get(mPosition).getFavstatus().equalsIgnoreCase("1")) 
			{
				SearchPage click = (SearchPage) context;
				click.onItemClickheartremove(mPosition);
			} 
			else
			{
				SearchPage click = (SearchPage) context;
				click.onItemClickheartadd(mPosition);
			}
			
			notifyDataSetChanged();
		}
    }
}

