package com.sahil.daggerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {
    Toolbar toolbar;
    ProgressBar progress;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        progress = findViewById(R.id.progress);
        webView = findViewById(R.id.webview);
        setupToolbar();
        loadWeb();
    }

    private void loadWeb() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        System.out.println("connection : " + isConnected);

        if (isConnected == true) {
            progress.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.requestFocusFromTouch();
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setAllowFileAccess(true);
            webView.requestFocusFromTouch();
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setDisplayZoomControls(false);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    super.onLoadResource(view, url);
                    System.out.println("URL on load resource: " + url);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, final String url) {
                    System.out.println("URL on page finished: " + url);
                    progress.setVisibility(View.GONE);
                    super.onPageFinished(view, url);
                }

                @Override
                public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                    progress.setVisibility(View.GONE);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
                    String message = "SSL Certificate.";
                    switch (error.getPrimaryError()) {
                        case SslError.SSL_UNTRUSTED:
                            message = "Untrusted SSL certificate.";
                            break;
                        case SslError.SSL_EXPIRED:
                            message = "SSL certificate expired.";
                            break;
                        case SslError.SSL_IDMISMATCH:
                            message = "";
                            break;
                        case SslError.SSL_NOTYETVALID:
                            message = "";
                            break;
                        case SslError.SSL_DATE_INVALID:
                            message = "";
                            break;
                        case SslError.SSL_INVALID:
                            message = "Invalid SSL certificate.";
                            break;
                    }
                    message += "\n Do you wish to continue?";

                    builder.setTitle("SSL Certificate ");
                    builder.setMessage(message);
                    builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.proceed();
                        }
                    });
                    builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.cancel();
                            finish();
                            Toast.makeText(WebViewActivity.this, "SSL Certificate Denied.\nKindly Accept to continue.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            try {
                Bundle b = new Bundle();
                b = getIntent().getExtras();
                String loadingUrl = b.getString("link");
                webView.loadUrl(loadingUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("News");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
