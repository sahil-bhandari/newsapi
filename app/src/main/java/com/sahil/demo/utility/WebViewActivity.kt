package com.sahil.demo.utility

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.sahil.demo.R

class WebViewActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var progress: ProgressBar? = null
    var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        progress = findViewById(R.id.progress)
        webView = findViewById(R.id.webview)
        setupToolbar()
        loadWeb()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWeb() {
        if (misc.isOnline()) {
            progress!!.visibility = View.VISIBLE
            webView!!.visibility = View.VISIBLE
            webView!!.settings.javaScriptEnabled = true
            webView!!.requestFocusFromTouch()
            webView!!.webChromeClient = WebChromeClient()
            webView!!.settings.allowFileAccess = true
            webView!!.settings.builtInZoomControls = true
            webView!!.settings.setSupportZoom(true)
            webView!!.settings.displayZoomControls = false
            webView!!.settings.useWideViewPort = true
            webView!!.settings.loadWithOverviewMode = true
            webView!!.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }

                override fun onLoadResource(view: WebView, url: String) {
                    super.onLoadResource(view, url)
                    println("URL on load resource: $url")
                }

                override fun onPageFinished(view: WebView, url: String) {
                    println("URL on page finished: $url")
                    progress!!.visibility = View.GONE
                    super.onPageFinished(view, url)
                }

                override fun onReceivedSslError(
                    view: WebView,
                    handler: SslErrorHandler,
                    error: SslError
                ) {
                    progress!!.visibility = View.GONE
                    val builder = AlertDialog.Builder(this@WebViewActivity)
                    var message = "SSL Certificate."
                    when (error.primaryError) {
                        SslError.SSL_UNTRUSTED -> message = "Untrusted SSL certificate."
                        SslError.SSL_EXPIRED -> message = "SSL certificate expired."
                        SslError.SSL_IDMISMATCH, SslError.SSL_NOTYETVALID, SslError.SSL_DATE_INVALID -> message =
                            "Invalid."
                        SslError.SSL_INVALID -> message = "Invalid SSL certificate."
                    }
                    message += "\n Do you wish to continue?"
                    builder.setTitle("SSL Certificate ")
                    builder.setMessage(message)
                    builder.setPositiveButton("Accept") { dialog, which -> handler.proceed() }
                    builder.setNegativeButton("Deny") { dialog, which ->
                        handler.cancel()
                        finish()
                        Toast.makeText(
                            this@WebViewActivity,
                            "SSL Certificate Denied.\nKindly Accept to continue.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
            }
            try {
                val b = intent.extras
                val loadingUrl = b!!.getString("link")
                webView!!.loadUrl(loadingUrl!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar?.apply {
            title="News"
            setNavigationIcon(R.drawable.ic_action_back)
        }
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener { finish() }
    }
}