package com.shopsy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shopsy.HockeyApp.HockeyActivity;
import com.shopsy.Iconstant.Iconstant;

public class PaymentPage extends HockeyActivity {

	private WebView webview;
	private ActionBar actionBar;
	private Context context;
	private String mobileId = "";
	private View mCustomView;
	private ProgressBar progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_page);
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		initialize();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		mCustomView = View.inflate(context, R.layout.payment_header, null);
		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);
		// Show the progress bar
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				// setProgress(progress * 100);
				if (progress < 100
						&& progressBar.getVisibility() == ProgressBar.GONE) {
					progressBar.setVisibility(ProgressBar.VISIBLE);
				}
				progressBar.setProgress(progress);
				if (progress == 100) {
					progressBar.setVisibility(ProgressBar.GONE);
				}
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			// Show loader on url load
			@Override
			public void onLoadResource(WebView view, String url) {
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				try {
					if (url.contains("pay-completed")) {
						Intent i1 = new Intent(PaymentPage.this, HomePage.class);
						startActivity(i1);
						finish();
					} else if (url.contains("pay-failed")) {
						Intent i1 = new Intent(PaymentPage.this, CartPage.class);
						startActivity(i1);
						finish();
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

		});

		// Call private class InsideWebViewClient
		webview.setWebViewClient(new InsideWebViewClient());
	}

	private void initialize() {
		// Locate the WebView in webview.xml
		webview = (WebView) findViewById(R.id.payment_webview);
		progressBar = (ProgressBar) findViewById(R.id.payment_progressbar);

		// Enable Javascript to run in WebView
		webview.getSettings().setJavaScriptEnabled(true);

		// Allow Zoom in/out controls
		webview.getSettings().setBuiltInZoomControls(true);

		// Zoom out the best fit your screen
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.getSettings().setUseWideViewPort(true);

		Intent i = getIntent();
		mobileId = i.getStringExtra("data");

		webview.loadUrl(Iconstant.paymenturl + mobileId);
	}

	private class InsideWebViewClient extends WebViewClient {
		@Override
		// Force links to be opened inside WebView and not in Default Browser
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	// ------------------------code for Back Press----------------------------
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			PaymentCancel();
			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}

	private void PaymentCancel() {
		final Dialog dialog = new Dialog(PaymentPage.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.cart_delete_item_dialog);

		TextView tv_data = (TextView) dialog
				.findViewById(R.id.cart_delete_popup_text2);
		TextView tv_yes = (TextView) dialog
				.findViewById(R.id.cart_delete_popup_text_ok);
		TextView tv_no = (TextView) dialog
				.findViewById(R.id.cart_delete_popup_text_no);

		tv_data.setText(getResources().getString(R.string.payment_dialog_text));

		tv_yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
				PaymentPage.this.overridePendingTransition(
						android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
				finish();
			}
		});

		tv_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		dialog.show();
	}

	@Override
	// Detect when the back button is pressed
	public void onBackPressed() {
		if (webview.canGoBack()) {
			webview.goBack();
		} else {
			// Let the system handle the back button
			super.onBackPressed();
		}
	}

}
