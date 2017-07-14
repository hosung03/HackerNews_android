package com.hosung.hackernews;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hosung.hackernews.model.HackerNews;

public class NewsViewActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar webViewProgress;

    private int acticle_position = -1;
    private String acticle_url;
    private boolean acticle_bookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        acticle_position = getIntent().getIntExtra("acticle_position",-1);
        String acticle_title = getIntent().getStringExtra("acticle_title");
        acticle_url = getIntent().getStringExtra("acticle_url");
        acticle_bookmark = getIntent().getBooleanExtra("acticle_bookmark",false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(acticle_title);
        webView = (WebView) findViewById(R.id.article_webview);
        webViewProgress = (ProgressBar) findViewById(R.id.article_progress);

        setupWebView();
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.setWebViewClient(new HackerNewsWebClient());
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                webViewProgress.setProgress(progress);
                if (webViewProgress.getProgress() >= 100) {
                    webViewProgress.setVisibility(View.GONE);
                }
            }
        });

        if(acticle_url!=null){
            webView.loadUrl(acticle_url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(!acticle_bookmark) getMenuInflater().inflate(R.menu.bookmark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_bookmark) {
            if (acticle_position > -1 && MainActivity.mNewsList.size()>0){
                MainActivity.addBookmark(NewsViewActivity.this, acticle_position);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    private class HackerNewsWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            webViewProgress.setVisibility(View.VISIBLE);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            //webViewProgress.setVisibility(View.GONE);
            String javascript = "javascript:document.getElementsByName('viewport')[0].setAttribute('content', 'initial-scale=1.0,maximum-scale=10.0');";
            view.loadUrl(javascript);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Toast.makeText(NewsViewActivity.this, "Cannot load page", Toast.LENGTH_SHORT).show();
            webViewProgress.setVisibility(View.GONE);
        }
    }
}
