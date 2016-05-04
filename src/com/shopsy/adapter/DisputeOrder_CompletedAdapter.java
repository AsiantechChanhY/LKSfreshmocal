package com.shopsy.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shopsy.R;
import com.shopsy.Pojo.DisputeOrder_ClosedPojo;
import com.shopsy.Utils.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class DisputeOrder_CompletedAdapter  extends BaseAdapter 
{

	private ArrayList<DisputeOrder_ClosedPojo> data;
	private LayoutInflater mInflater;
	private Activity context;

	public DisputeOrder_CompletedAdapter(Activity c,ArrayList<DisputeOrder_ClosedPojo> d)
	{
		context=c;
		mInflater = LayoutInflater.from(context);
		data = d;
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
		private TextView orderId;
		private TextView buyername;
		private RoundedImageView buyer_image;
		private TextView price;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.dispute_order_closed_single, parent, false);
			holder = new ViewHolder();
			
			holder.orderId = (TextView) view.findViewById(R.id.dispute_order_completed_orderno_textview);
			holder.buyername = (TextView) view.findViewById(R.id.dispute_order_completed_buyer_textview);
			holder.buyer_image = (RoundedImageView) view.findViewById(R.id.dispute_order_completed_userimage);
			holder.price = (TextView) view.findViewById(R.id.dispute_order_completed_claim_textview);
			
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		holder.orderId.setText(data.get(position).getOrderId());
		holder.buyername.setText(data.get(position).getBuyerName());
		holder.price.setText(data.get(position).getClaimAmt());
		Picasso.with(context).load(String.valueOf(data.get(position).getBuyerImage())).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.no_image_grey).into(holder.buyer_image);
		
		return view;
	}
	
}


