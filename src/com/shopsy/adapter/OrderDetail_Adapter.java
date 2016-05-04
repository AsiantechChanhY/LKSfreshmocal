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
import com.shopsy.Pojo.OrderDetail_Pojo;
import com.shopsy.Utils.ImageLoader_BandWidth;

public class OrderDetail_Adapter extends BaseAdapter 
{

	private ArrayList<OrderDetail_Pojo> data;
	private ImageLoader_BandWidth imageLoader_bandwidth;
	private LayoutInflater mInflater;
	private Context context;

	public OrderDetail_Adapter(Context c,ArrayList<OrderDetail_Pojo> d)
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
		private TextView productname,quantity,price,itemTotal;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.orderdetail_single_footer, parent, false);
			holder = new ViewHolder();
			holder.productname = (TextView) view.findViewById(R.id.orderdetail_footer_productname);
			holder.quantity = (TextView) view.findViewById(R.id.orderdetail_footer_quantity);
			holder.itemTotal = (TextView) view.findViewById(R.id.orderdetail_footer_itemTotal);
			holder.price = (TextView) view.findViewById(R.id.orderdetail_footer_price_textview);
			holder.product_image = (ImageView) view.findViewById(R.id.orderdetail_footer_productimage);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		holder.productname.setText(data.get(position).getName());
		holder.quantity.setText(context.getResources().getString(R.string.order_detail_label_quantity)+" "+data.get(position).getQuantity());
		holder.itemTotal.setText(context.getResources().getString(R.string.order_detail_label_footer_itemTotal)+" "+data.get(position).getItemTotal());
		holder.price.setText(data.get(position).getUnitPrice());
		
		imageLoader_bandwidth.DisplayImage(String.valueOf(data.get(position).getImage()), holder.product_image, 4);
		
		return view;
	}
}




