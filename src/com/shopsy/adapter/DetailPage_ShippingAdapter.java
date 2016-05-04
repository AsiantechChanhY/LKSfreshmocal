package com.shopsy.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shopsy.R;
import com.shopsy.Pojo.Detail_Ship_Pojo;

public class DetailPage_ShippingAdapter extends BaseAdapter 
{

	private ArrayList<Detail_Ship_Pojo> data;
	private LayoutInflater mInflater;
	private Context context;

	public DetailPage_ShippingAdapter(Context c,ArrayList<Detail_Ship_Pojo> d)
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
		private TextView tvshiptp;
		private TextView tvcost;
		private TextView tvwithanother;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.detailpage_shipping_single, parent, false);
			holder = new ViewHolder();
			holder.tvshiptp = (TextView) view.findViewById(R.id.detailpage_shipping_single_shipto);
			holder.tvcost = (TextView) view.findViewById(R.id.detailpage_shipping_single_cost);
			holder.tvwithanother = (TextView) view.findViewById(R.id.detailpage_shipping_single_withanother);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		holder.tvshiptp.setText(data.get(position).getShipto());
		holder.tvcost.setText(data.get(position).getCost());
		holder.tvwithanother.setText(data.get(position).getWithanother());
		
		return view;
	}
}
