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
import com.shopsy.Pojo.OrderOpen_Pojo;
import com.shopsy.Utils.ImageLoader_BandWidth;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class OrderOpen_Adapter extends BaseAdapter 
{

	private ArrayList<OrderOpen_Pojo> data;
	ImageLoader_BandWidth imageLoader_bandwidth;
	private LayoutInflater mInflater;
	private Context context;

	public OrderOpen_Adapter(Context c,ArrayList<OrderOpen_Pojo> d)
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
		private ImageView product_image;
		private TextView date,productname,quantity,orderid,price,status;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.order_opentab_single, parent, false);
			holder = new ViewHolder();
			holder.date = (TextView) view.findViewById(R.id.order_open_single_date);
			holder.productname = (TextView) view.findViewById(R.id.order_open_single_productname);
			holder.quantity = (TextView) view.findViewById(R.id.order_open_single_quantity);
			holder.orderid = (TextView) view.findViewById(R.id.order_open_single_orderid);
			holder.price = (TextView) view.findViewById(R.id.order_open_single_ordertotal_price);
			holder.status = (TextView) view.findViewById(R.id.order_open_single_status_textview);
			holder.product_image = (ImageView) view.findViewById(R.id.order_open_single_productimage);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		holder.date.setText(data.get(position).getOrderDate());
		holder.productname.setText(data.get(position).getProduct_name());
		holder.quantity.setText(context.getResources().getString(R.string.order_open_label_quantity)+" "+data.get(position).getQuantity());
		holder.orderid.setText(context.getResources().getString(R.string.order_open_label_orderid)+" "+data.get(position).getOrderId());
		holder.price.setText(data.get(position).getOrderTotal());
		holder.status.setText(data.get(position).getOrderStatus());
		
		Picasso.with(context).load(String.valueOf(data.get(position).getProductImage())).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.no_image_grey).into(holder.product_image);
		//imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getProductImage()), holder.product_image, 4);
		
		return view;
	}
}


