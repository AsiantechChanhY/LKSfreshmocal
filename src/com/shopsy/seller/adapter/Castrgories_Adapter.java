package com.shopsy.seller.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shopsy.R;
import com.shopsy.bean.Categories_Bean;
/**
 * @author Prithivi Raj
 *
 */
public class Castrgories_Adapter extends BaseAdapter
{
	LayoutInflater mInflater;
	Context ctx;
	ArrayList<Categories_Bean> arrayList;
	int row;

	public Castrgories_Adapter(Context context ,int itemcategories, ArrayList<Categories_Bean> itemList_Bean) 
	{
		this.mInflater = LayoutInflater.from(context);
		this.ctx = context;
		this.arrayList = itemList_Bean;
		this.row = itemcategories;
	}


	@Override
	public int getCount()
	{
		return arrayList.size();
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public int getViewTypeCount() 
	{                 
		//Count=Size of ArrayList.
		return arrayList.size();
    }
	
	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	 public View getView(final int position, View convertView, ViewGroup parent) 
	    {
	    	ViewHolder1 holder;
	        if (convertView == null) 
	        {
	            holder = new ViewHolder1();
	            convertView = mInflater.inflate(R.layout.itemcategories_adapter, null);
	            holder.tv_name = (TextView) convertView.findViewById(R.id.adapter_categortext);
	            holder.tv_name.setTag(position);
	            convertView.setTag(holder);
	        }
	        else 
	        {
	            holder = (ViewHolder1) convertView.getTag();
	        }    
	        holder.tv_name.setText(arrayList.get(position).getCategoryName());
	        return convertView;
	    }
	
	class ViewHolder1 
	{
	    TextView tv_name;
	}
}
