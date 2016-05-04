package com.shopsy;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopsy.HockeyApp.HockeyFragment;
import com.shopsy.MaterialTab.SlidingTabLayout;
import com.shopsy.Utils.CustomViewPager;
import com.shopsy.Utils.SessionManager;
import com.shopsy.adapter.Home_ViewPagerAdapter;

public class HomePage extends HockeyFragment {
	private ActionBar actionBar;

	private static CustomViewPager pager;
	private Home_ViewPagerAdapter adapter;
	SlidingTabLayout tabs;
	private CharSequence Titles[] = { "RECOMMEND", "PICKS", "FEED" };
	private static int Numboftabs = 3;

	private RelativeLayout cart;
	private static TextView cartcount;
	private TextView header_title;
	private static RelativeLayout cartcountlayout;

	private static SessionManager session;
	View rootview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.homepage, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		ActionBarActivity actionBarActivity = (ActionBarActivity) getActivity();
		actionBar = actionBarActivity.getSupportActionBar();
		initialize(view);
	}

	private void initialize(View rootview) {
		session = new SessionManager(getActivity());

		// code to disable actionBar title
		actionBar.setDisplayShowTitleEnabled(false);

		// ------------------code to add custom menu in action
		// bar--------------------------------------
		actionBar.setDisplayShowCustomEnabled(true);
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.home_custom_header, null);
		actionBar.setCustomView(view);

		header_title = (TextView) view.findViewById(R.id.home_header_title);
		cart = (RelativeLayout) view.findViewById(R.id.cartrelativelayout);
		cartcount = (TextView) view.findViewById(R.id.homecartcounttext);
		cartcountlayout = (RelativeLayout) view
				.findViewById(R.id.homecartcountlayout);
		header_title.setText("Home");
		// Assiging the Sliding Tab Layout View
		tabs = (SlidingTabLayout) rootview.findViewById(R.id.homepage_tabs);
		tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true,
										// This makes the tabs Space Evenly in
										// Available width

		// Setting Custom Color for the Scroll bar indicator of the Tab View
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		});

		
		session.SetHomePage("Fragment");
		
		adapter = new Home_ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles, Numboftabs);
		pager = (CustomViewPager) rootview.findViewById(R.id.homepage_pager);
		pager.setOffscreenPageLimit(2);
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
		
		
		cart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CartPage.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.enter,
						R.anim.exit);
			}
		});

	}

	public static void cartcountRefresh() {
		// get cart count from session
		HashMap<String, String> count = session.getCartCount();
		String Scartcount = count.get(SessionManager.KEY_CARTCOUNT);

		if (Scartcount.length() > 0 && !Scartcount.equals("0")) {
			cartcountlayout.setVisibility(View.VISIBLE);
			cartcount.setText(Scartcount);
		} else {
			cartcountlayout.setVisibility(View.INVISIBLE);
		}
	}

	public static void swipeOn() {

		System.out.println("-----------inside swipe on-------------");
		pager.setSwipeable(true);
	}

	public static void swipeOff() {

		System.out.println("-----------inside swipe off-------------");
		pager.setSwipeable(false);
	}
}
