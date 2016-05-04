package com.shopsy.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.shopsy.AllShop_ProductPage;
import com.shopsy.CartPage;
import com.shopsy.R;
import com.shopsy.Pojo.CartPojo;
import com.shopsy.Utils.ExpandableHeightListView;
import com.shopsy.Utils.ImageLoader;
import com.shopsy.Utils.RoundedImageView;
import com.squareup.picasso.Picasso;

public class CartPage_Adapter extends BaseAdapter 
{
	
	private ArrayList<CartPojo> data;
	private ArrayList<CartPojo> pay_data;
	private final ArrayList<ArrayList<CartPojo>> child_data;
	ImageLoader imageLoader;
	private LayoutInflater mInflater;
	private Activity context;
	Cart_Item_Adapter adapter;
	
	ArrayList<String> pay_list=new ArrayList<String>();
	
	public CartPage_Adapter(Activity c,ArrayList<CartPojo> d, ArrayList<ArrayList<CartPojo>> d1,ArrayList<CartPojo> pay_d) 
	{
		context=c;
		mInflater = LayoutInflater.from(context);
		data = d;
		pay_data=pay_d;
		child_data=d1;
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
		private RoundedImageView ownerimage;
		private TextView shopname;
		TextView readytoship,total_item,shipping,ordertotal,add_note;
		private Button checkout;
		//Spinner shipspinner;
		Spinner how_pay_spinner;
		ExpandableHeightListView listview;
		ImageView menuIcon;
		private RelativeLayout coupon_layout;
		private TextView coupon_code;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.cart_single, parent, false);
			holder = new ViewHolder();
			
			holder.ownerimage = (RoundedImageView) view.findViewById(R.id.cart_single_userimage);
			holder.shopname = (TextView) view.findViewById(R.id.cart_single_shopname);
			holder.readytoship = (TextView) view.findViewById(R.id.cart_single_readytoship_textview);
			holder.total_item = (TextView) view.findViewById(R.id.cart_single_itemTotal_price);
			holder.shipping = (TextView) view.findViewById(R.id.cart_single_shipping_price);
			holder.ordertotal = (TextView) view.findViewById(R.id.cart_single_ordertotal_price);
			holder.add_note = (TextView) view.findViewById(R.id.cart_single_addnote_editText);
			holder.coupon_layout=(RelativeLayout)view.findViewById(R.id.cart_single_addcoupon_relativelayout);
			holder.coupon_code=(TextView)view.findViewById(R.id.cart_single_addcoupon_textView);
			
			holder.checkout = (Button) view.findViewById(R.id.cart_single_checkout_button);
			
			//holder.shipspinner = (Spinner) view.findViewById(R.id.cart_single_shipping_spinner1);
			holder.how_pay_spinner = (Spinner) view.findViewById(R.id.cart_single_how_willpay_spinner1);
			holder.listview = (ExpandableHeightListView) view.findViewById(R.id.cart_item_listview);
			holder.menuIcon=(ImageView)view.findViewById(R.id.cart_single_menuicon);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		if(data.get(position).getCouponCode().length()>0)
		{
			holder.coupon_code.setText(data.get(position).getCouponCode());
		}
		else
		{
			holder.coupon_code.setText(context.getResources().getString(R.string.cart_label_addcoupon_code));
		}
		
		pay_list.clear();
		for(int i=0;i<pay_data.size();i++)
		{
			pay_list.add(pay_data.get(i).getPayment_type());
		}
		
		ArrayAdapter<String> PayAdapter = new ArrayAdapter<String>(context, R.layout.cart_spinner_text, pay_list);
		PayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		holder.how_pay_spinner.setAdapter(PayAdapter);
		
		
		holder.shopname.setText(data.get(position).getShopName());
		Picasso.with(context).load(String.valueOf(data.get(position).getShopOwnerImage())).placeholder(R.drawable.nouserimg).into(holder.ownerimage);
		
		holder.total_item.setText(data.get(position).getShopSubTotal());
		holder.shipping.setText(data.get(position).getShopShipping());
		holder.ordertotal.setText(data.get(position).getShopCartTotal());
		
		
		
		System.out.println("---------------data.get(position).getShopName()----------------"+data.get(position).getShopName());
		
		// code for list view
		if (position < data.size()) 
		{
			
			System.out.println("-------------inside cart item-----------------");
			adapter = new Cart_Item_Adapter(context, child_data.get(position));
			holder.listview.setAdapter(adapter);
			holder.listview.setExpanded(true);
			adapter.notifyDataSetChanged();
		}
		
		
		holder.menuIcon.setOnClickListener(new OnItemClickMenu(position));
		holder.checkout.setOnClickListener(new OnItemClickCheckOut(position,holder.how_pay_spinner.getSelectedItem().toString(),holder.add_note.getText().toString()));
		holder.coupon_layout.setOnClickListener(new OnItemClickCoupon(position));
		
		return view;
	}
	
	
	   // method to delete Shop
			private class OnItemClickMenu implements OnClickListener
			{
				private final int mPosition;
				
				OnItemClickMenu(int position)
				{
					mPosition = position;
				}
				@Override
				public void onClick(View view)
				{
					
					PopupMenu popupMenu = new PopupMenu(context, view) {
						@Override
						public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
							switch (item.getItemId()) {
								case R.id.cart_menu_removeitem:
									
									CartPage sct = (CartPage) context;
									sct.Shopdelete(mPosition);
									
									return true;
								
								case R.id.cart_menu_visitshop:
									
									Intent intent = new Intent(context,AllShop_ProductPage.class);
									intent.putExtra("shopurl", data.get(mPosition).getShopUrl());
									context.startActivity(intent);
									context.overridePendingTransition(R.anim.enter, R.anim.exit);
									
									return true;
								
								default:
									return super.onMenuItemSelected(menu, item);
							}
						}
					};
				
					popupMenu.inflate(R.menu.cart_menu);
					popupMenu.show();
				}
			}
			
			
			
		// method for checkout
		private class OnItemClickCheckOut implements OnClickListener {
			private int mPosition;
			private String pay="";
			private String note="";
			
			OnItemClickCheckOut(int position,String payType,String addnote) {
				mPosition = position;
				pay=payType;
				note=addnote;
			}
			@Override
			public void onClick(View view) 
			{
				
				if(pay.equalsIgnoreCase("Credit Card"))
				{
					pay="Credit-Card";
				}
				else if(pay.equalsIgnoreCase("Twocheckout"))
				{
					pay="twocheckout";
				}
				
				CartPage sct = (CartPage) context;
				sct.onItemClickcheckout(mPosition,pay,note);
			}
		}
		
		
	// method for Coupon
	private class OnItemClickCoupon implements OnClickListener {
		private final int mPosition;

		OnItemClickCoupon(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View view) {
			
			CartPage sct = (CartPage) context;
			sct.onItemClickCoupon(mPosition,data.get(mPosition).getCouponCode());
		}
	}
}

