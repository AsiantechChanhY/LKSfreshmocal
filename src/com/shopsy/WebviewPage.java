package com.shopsy;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebviewPage extends ActionBarActivity {
	
	private WebView webview;
	private ActionBar actionBar;
	private ColorDrawable colorDrawable = new ColorDrawable();
	
	private Context context;
	private String WebUrl="",title="";
	
	private View mCustomView;
	private ProgressBar progressBar;
	
	private TextView tv_title;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webviewpage);
		
		context = getApplicationContext();
		actionBar = getSupportActionBar();
		setupToolbar();
		initialize();
		
		// Show the progress bar
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				// setProgress(progress * 100);
				
				if (progress < 100 && progressBar.getVisibility() == ProgressBar.GONE)
				{
					progressBar.setVisibility(ProgressBar.VISIBLE);
				}
				progressBar.setProgress(progress);
				
				if (progress == 100)
				{
					progressBar.setVisibility(ProgressBar.GONE);
				}
			}
		});
		
		
		// Call private class InsideWebViewClient
		webview.setWebViewClient(new InsideWebViewClient());
	}
	
	private void initialize()
	{
		// Locate the WebView in webview.xml
		webview = (WebView) findViewById(R.id.webview);
		progressBar = (ProgressBar) findViewById(R.id.webview_progressbar);
		
		// Enable Javascript to run in WebView
		webview.getSettings().setJavaScriptEnabled(true);
		
		// Allow Zoom in/out controls
		webview.getSettings().setBuiltInZoomControls(true);
		
		// Zoom out the best fit your screen
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.getSettings().setUseWideViewPort(true);
		
		
		Intent i = getIntent();
		WebUrl = i.getStringExtra("weburl");
		title = i.getStringExtra("title");
		
		tv_title.setText(title);
		
		webview.loadUrl(WebUrl);
		
		
	}
	
	private void setupToolbar()
	{
        actionBar = getSupportActionBar();
        
        // code to set actionBar background
      	colorDrawable.setColor(0xff1A237E);
      	actionBar.setBackgroundDrawable(colorDrawable);
	
      	actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		mCustomView = View.inflate(context, R.layout.payment_header, null);
		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		tv_title=(TextView)mCustomView.findViewById(R.id.payment_header_title);
    }
	
	private class InsideWebViewClient extends WebViewClient {
		@Override
		// Force links to be opened inside WebView and not in Default Browser
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
	
	@Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if(webview.canGoBack()) {
        	webview.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }
	
	
	//------------------------code for Back Press----------------------------
		@Override
		public boolean onOptionsItemSelected(MenuItem item) 
		{
			switch (item.getItemId()) 
			{
				case android.R.id.home:
					
					onBackPressed();
					setResult(RESULT_OK);
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
					finish();	
				    return true;
					
				default:
					return super.onOptionsItemSelected(item);
					
			}
		}
	
}


