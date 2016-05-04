package com.shopsy.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopsy.R;
import com.shopsy.Pojo.Conversation_Pojo;
import com.shopsy.Utils.RoundedImageView;
import com.squareup.picasso.Picasso;

public class Conversation_Adapter extends BaseAdapter 
{

	private ArrayList<Conversation_Pojo> data;
	private LayoutInflater mInflater;
	private Activity context;

	public Conversation_Adapter(Activity c,ArrayList<Conversation_Pojo> d)
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
		private RoundedImageView userimage;
		private TextView username;
		private TextView message;
		private TextView time;
		private TextView newmessage;
		private ImageView read_status;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.conversation_single, parent, false);
			holder = new ViewHolder();
			
			holder.userimage = (RoundedImageView) view.findViewById(R.id.conversation_single_userimage);
			holder.username = (TextView) view.findViewById(R.id.conversation_single_username);
			holder.message = (TextView) view.findViewById(R.id.conversation_single_message);
			holder.time = (TextView) view.findViewById(R.id.conversation_single_message_time);
			holder.newmessage = (TextView) view.findViewById(R.id.conversation_single_new_message);
			holder.read_status = (ImageView) view.findViewById(R.id.conversation_single_read_status);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		
		holder.message.setText(data.get(position).getMessage());
		holder.time.setText(data.get(position).getTime());
		holder.username.setText(data.get(position).getSender_name());
		Picasso.with(context).load(String.valueOf(data.get(position).getSender_image())).placeholder(R.drawable.nouserimg).into(holder.userimage);
		
		if(data.get(position).getStatus().equalsIgnoreCase("Read"))
		{
			holder.username.setTextColor(Color.parseColor("#9B9A98"));
			
			Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
			holder.username.setTypeface(tf);
			
			holder.time.setTextColor(Color.parseColor("#9B9A98"));
			
			holder.read_status.setVisibility(View.INVISIBLE);
		}
		else
		{
			holder.username.setTextColor(Color.parseColor("#171717"));
			holder.username.setTypeface(Typeface.DEFAULT_BOLD);
			
			holder.time.setTextColor(Color.parseColor("#4988F3"));

			holder.read_status.setVisibility(View.VISIBLE);
		}
		
		
		if(data.get(position).getNewMsg().equalsIgnoreCase("0"))
		{
			holder.newmessage.setVisibility(View.INVISIBLE);
		}
		else
		{
			holder.newmessage.setVisibility(View.VISIBLE);
			holder.newmessage.setText(data.get(position).getNewMsg()+" "+context.getResources().getString(R.string.label_conversation_morereplies));
		}
		
		
		return view;
	}
	
}


