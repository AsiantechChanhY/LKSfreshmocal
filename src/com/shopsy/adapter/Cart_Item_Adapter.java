package com.shopsy.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.shopsy.CartPage;
import com.shopsy.R;
import com.shopsy.Pojo.CartPojo;
import com.shopsy.Utils.ImageLoader;
import com.squareup.picasso.Picasso;

public class Cart_Item_Adapter extends BaseAdapter 
{
	
	private ArrayList<CartPojo> data;
	ImageLoader imageLoader;
	private LayoutInflater mInflater;
	private Context context;
	String[] spliteddata;
	private ArrayList<String> Quantity = new ArrayList<String>();		
	
	
	public Cart_Item_Adapter(Context c,ArrayList<CartPojo> d) 
	{
		context=c;
		mInflater = LayoutInflater.from(context);
		data = d;
		imageLoader = new ImageLoader(context);
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
		private ImageView productimage;
		private TextView productname,productprice;
		
		private RelativeLayout spinner2_layout,spinner3_layout;
		private TextView spinner1_label,spinner2_label,spinner3_label;
		private Spinner spinner1;
		private TextView spinner2_text,spinner3_text;
		private RelativeLayout delete_layout;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.cart_item_single, parent, false);
			holder = new ViewHolder();
			
			holder.productimage = (ImageView) view.findViewById(R.id.cart_single_productimage);
			holder.productname = (TextView) view.findViewById(R.id.cart_single_productname);
			holder.productprice = (TextView) view.findViewById(R.id.cart_single_productprice);
			holder.delete_layout = (RelativeLayout) view.findViewById(R.id.cart_single_delete_layout);
			
			holder.spinner2_layout = (RelativeLayout) view.findViewById(R.id.cart_single_spinner2_layout);
			holder.spinner3_layout = (RelativeLayout) view.findViewById(R.id.cart_single_spinner3_layout);
			
			holder.spinner1_label = (TextView) view.findViewById(R.id.cart_single_spinner1_label);
			holder.spinner2_label = (TextView) view.findViewById(R.id.cart_single_spinner2_label);
			holder.spinner3_label = (TextView) view.findViewById(R.id.cart_single_spinner3_label);
			
			holder.spinner1 = (Spinner) view.findViewById(R.id.cart_spinner1);
			holder.spinner2_text = (TextView) view.findViewById(R.id.cart_spinner2_textview);
			holder.spinner3_text = (TextView) view.findViewById(R.id.cart_spinner3_textview);
			
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		Quantity.clear();
		// code for quantity spinner
		Quantity.add("Select Quantity");
		
		if(data.get(position).getProductMaxQty().length()>0)
		{
			for(int i=0;i<Integer.parseInt(data.get(position).getProductMaxQty());i++)
			{
				Quantity.add(String.valueOf(i+1));
			}
		}
		
		
		ArrayAdapter<String> QtyAdapter = new ArrayAdapter<String>(context, R.layout.cart_spinner_text, Quantity);
		QtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		holder.spinner1.setAdapter(QtyAdapter);
		
		if(data.get(position).getProductQty().length()>0)
		{
			 holder.spinner1.setSelection(Integer.parseInt(data.get(position).getProductQty()));
		}
	   
		
		Picasso.with(context).load(String.valueOf(data.get(position).getProductImage())).placeholder(R.drawable.no_image_grey).into(holder.productimage);
		
		holder.productname.setText(data.get(position).getProductName());
		holder.productprice.setText(data.get(position).getProductUnitPrice());
		holder.spinner1_label.setText(context.getResources().getString(R.string.cart_label_quantity));
		
		
		
		//----------------code to set choice in cart<Start>----------------------------
		
		
		if(data.get(position).getProductChoice().length()>0)
		{
			holder.spinner2_layout.setVisibility(View.VISIBLE);
			holder.spinner3_layout.setVisibility(View.VISIBLE);
			
			spliteddata = data.get(position).getProductChoice().split(",");
			
			
			if (spliteddata.length==1)
			{
				if (spliteddata[0].contains("size")||spliteddata[0].contains("color")||spliteddata[0].contains("length")||spliteddata[0].contains("weight"))
				{
					String[] choicedata1 = spliteddata[0].split(":");
					holder.spinner2_layout.setVisibility(View.VISIBLE);
					holder.spinner3_layout.setVisibility(View.GONE);
					holder.spinner2_label.setText(choicedata1[0]);
					holder.spinner2_text.setText(choicedata1[1]);
				}
				else
				{
					holder.spinner2_layout.setVisibility(View.GONE);
					holder.spinner3_layout.setVisibility(View.GONE);
				}
				
			}
			
			if(spliteddata.length==2)
			{
				
				if (spliteddata[0].contains("size")||spliteddata[0].contains("color")||spliteddata[0].contains("length")||spliteddata[0].contains("weight"))
				{
					String[] choicedata1 = spliteddata[0].split(":");
					holder.spinner2_layout.setVisibility(View.VISIBLE);
					holder.spinner2_label.setText(choicedata1[0]);
					holder.spinner2_text.setText(choicedata1[1]);
				}
				else
				{
					holder.spinner2_layout.setVisibility(View.GONE);
				}
				
				if (spliteddata[1].contains("size")||spliteddata[1].contains("color")||spliteddata[1].contains("length")||spliteddata[1].contains("weight"))
				{
					String[] choicedata2 = spliteddata[1].split(":");
					holder.spinner3_layout.setVisibility(View.VISIBLE);
					holder.spinner3_label.setText(choicedata2[0]);
					holder.spinner3_text.setText(choicedata2[1]);
				}
				else
				{
					holder.spinner3_layout.setVisibility(View.GONE);
				}
			}
			
			//----------------code to set choice in cart<End>----------------------------
		}
		else
		{
			holder.spinner2_layout.setVisibility(View.GONE);
			holder.spinner3_layout.setVisibility(View.GONE);
		}
				
		
				
		holder.delete_layout.setOnClickListener(new OnItemClickListenerItemDelete(position));
				
		return view;
	}
	
	
	// method to delete product
		private class OnItemClickListenerItemDelete implements OnClickListener
		{
			private final int mPosition;
			
			OnItemClickListenerItemDelete(int position)
			{
				mPosition = position;
			}
			@Override
			public void onClick(View view)
			{
				
				CartPage sct = (CartPage) context;
				sct.Productdelete(data.get(mPosition).getCartId());
			}
		}
}

