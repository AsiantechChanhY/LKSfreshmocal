package com.shopsy.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopsy.R;
import com.shopsy.Pojo.ConversationDetail_Pojo;

public class ConversationDetail_Adapter extends BaseAdapter 
{

	private ArrayList<ConversationDetail_Pojo> data;
	private LayoutInflater mInflater;
	private Activity context;

	public ConversationDetail_Adapter(Activity c,ArrayList<ConversationDetail_Pojo> d)
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
		private RelativeLayout left_layout;
		private TextView left_message;
		private TextView left_time;
		
		private FrameLayout right_layout;
		private TextView right_message;
		private TextView right_time;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view;
		ViewHolder holder;
		if(convertView == null) 
		{
			view = mInflater.inflate(R.layout.conversationdetail_single, parent, false);
			holder = new ViewHolder();
			
			holder.left_layout = (RelativeLayout) view.findViewById(R.id.chat_left_side_layout);
			holder.left_message = (TextView) view.findViewById(R.id.chat_left_side_message);
			holder.left_time = (TextView) view.findViewById(R.id.chat_left_side_time);
			
			holder.right_layout = (FrameLayout) view.findViewById(R.id.chat_right_side_layout);
			holder.right_message = (TextView) view.findViewById(R.id.chat_right_side_message);
			holder.right_time = (TextView) view.findViewById(R.id.chat_right_side_time);
			
			view.setTag(holder);
		}
		else
		{
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		

		if(data.get(position).getMsg_type().equalsIgnoreCase("1"))
		{
			holder.left_layout.setVisibility(View.GONE);
			holder.right_layout.setVisibility(View.VISIBLE);
			
			holder.right_message.setText(data.get(position).getMessage());
			holder.right_time.setText(data.get(position).getTime());
		}
		else
		{
			holder.left_layout.setVisibility(View.VISIBLE);
			holder.right_layout.setVisibility(View.GONE);
			
			holder.left_message.setText(data.get(position).getMessage());
			holder.left_time.setText(data.get(position).getTime());
		}
		
		
		
		return view;
	}
	
}



