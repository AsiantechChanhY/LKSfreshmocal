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
import com.shopsy.SubCategories_Product;
import com.shopsy.Pojo.Subcategories_Product_Pojo;
import com.shopsy.StaggeredGridView.DynamicHeightImageView;
import com.shopsy.Utils.ImageLoader;
import com.shopsy.Utils.SessionManager_Settings;

public class SubCategories_ProductAdapter extends BaseAdapter 
{

	private ArrayList<Subcategories_Product_Pojo> data;
	private ImageLoader imageLoader;
	private LayoutInflater mInflater;
	private Activity context;
	private SessionManager_Settings session_settings;

	public SubCategories_ProductAdapter(Activity c,ArrayList<Subcategories_Product_Pojo> d)
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
			view = mInflater.inflate(R.layout.subcategories_product_single, parent, false);
			holder = new ViewHolder();
			holder.tvName = (TextView) view.findViewById(R.id.subcategories_product_grid_name);
			holder.tvprice = (TextView) view.findViewById(R.id.subcategories_product_grid_price);
			holder.tvstorename = (TextView) view.findViewById(R.id.subcategories_product_grid_storename);
			holder.image = (DynamicHeightImageView) view.findViewById(R.id.subcategories_product_grid_image);
			holder.favoriteheart = (ImageView) view.findViewById(R.id.subcategories_product_favheart);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		
		HashMap<String, String> user = session_settings.getBandwidth();
		String Bandwidth = user.get(SessionManager_Settings.KEY_BANDWIDTH);
		
		holder.tvName.setText(data.get(position).getSubcatproduct_productName().replace("&#8230;", "..."));
		holder.tvprice.setText(data.get(position).getSubcatproduct_price());
		holder.tvstorename.setText(data.get(position).getSubcatproduct_storename());
		
		ImageView image = holder.image;
		if(Bandwidth.equalsIgnoreCase("low"))
		{
			imageLoader.DisplayImage(String.valueOf(data.get(position).getSubcatproduct_productImage()), image);
		}
		else
		{
			imageLoader.DisplayImage(String.valueOf(data.get(position).getSubcatproduct_productImage()), image);
		}
		
		// code to heart color change when Url loads
		if (data.get(position).getSubcatproduct_favstatus().contains("1")) 
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
			
			if (data.get(mPosition).getSubcatproduct_favstatus().equalsIgnoreCase("1")) 
			{
				SubCategories_Product click = (SubCategories_Product) context;
				click.onItemClickheartremove(mPosition);
			} 
			else
			{
				SubCategories_Product click = (SubCategories_Product) context;
				click.onItemClickheartadd(mPosition);
			}
			
			notifyDataSetChanged();
		}
    }
}
