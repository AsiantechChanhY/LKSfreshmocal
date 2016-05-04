package com.shopsy.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shopsy.R;
import com.shopsy.Pojo.Recommended_Pojo;
import com.shopsy.Utils.ImageLoader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class Recommended_Banner_Adapter extends PagerAdapter {
 
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Recommended_Pojo> data;
    ImageLoader imageLoader;
 
    public Recommended_Banner_Adapter(Activity context,ArrayList<Recommended_Pojo> d) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data=d;
        imageLoader = new ImageLoader(mContext.getApplicationContext());
    }
 
    @Override
    public int getCount() {
        return data.size();
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==((LinearLayout) object);
    }
 
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.banner_image_single, container, false);
 
        ImageView imageView = (ImageView) itemView.findViewById(R.id.recommended_viewpager_single_bannerimage);
        
        
        
        Picasso.with(mContext).load(String.valueOf(data.get(position).getBanner_Image())).placeholder(R.drawable.no_image_grey).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
        //imageLoader.DisplayImage(String.valueOf(data.get(position).getSeller_banner()), imageView);
        
        container.addView(itemView);
        
        return itemView;
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

