package com.novallc.foothillappmobile.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.ListViewAssets.BaseActivity;
import com.novallc.foothillappmobile.activity.MainActivity;
import com.novallc.foothillappmobile.activity.WebViewActivity;

import org.apache.http.util.EncodingUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.net.URL;
import java.net.URLEncoder;

public class WebViewFragment extends Fragment implements OnTouchListener, Callback {
    private static final String CONFIRMATION_URL = "https://parentnet.tustin.k12.ca.us/ParentPortal/default.aspx";
    public static final String ARG_URL = "url";
    public static final String ARG_PASS = "password";
    public static final String ARG_USER = "username";
    public static final String ARG_JS = "jsNav";
    private static final int CLICK_ON_URL = 2;
    private static final int CLICK_ON_WEBVIEW = 1;
    public static final int OBTAIN_PROGRESS_EVERY = 100;
    private WebViewClient client;
    private final Handler handler;
    private Handler mHandler;
    private ProgressBar mProgressBar;
    private Runnable mRunnable;
    public static WebView mWebView;
    private Boolean loginCredentialsEntered = false;
    public static int lCRootPageReach = -1;
    public static String mConfirmMessage = null;
//    public static Element mAuthFailed = null;
    private String str_CnfrmDoc = null;

    public WebViewFragment() {
        this.handler = new Handler(this);
        this.mHandler = new Handler();
        this.mRunnable = new Runnable() {
            public void run() {
                int progress = WebViewFragment.this.mWebView.getProgress() + ((100 - WebViewFragment.this.mWebView.getProgress()) / 10);
                WebViewFragment.this.mProgressBar.setProgress(progress);
                if (progress != WebViewFragment.OBTAIN_PROGRESS_EVERY) {
                    WebViewFragment.this.mHandler.postDelayed(this, 100);
                }if(progress == 100){
                    WebViewFragment.this.mProgressBar.setVisibility(View.GONE);
                }else
                    WebViewFragment.this.mProgressBar.setVisibility(View.VISIBLE);
            }
        };
    }

    public WebView getWebView() {
        return this.mWebView;
    }

    public void setProgressBarWebView() {
        this.mHandler = new Handler();
        this.mHandler.postDelayed(this.mRunnable, 100);
        this.mRunnable = new Runnable() {
            public void run() {
                int progress = WebViewFragment.this.mWebView.getProgress() + ((100 - WebViewFragment.this.mWebView.getProgress()) / 10);
                WebViewFragment.this.mProgressBar.setProgress(progress);
                if (progress != WebViewFragment.OBTAIN_PROGRESS_EVERY) {
                    WebViewFragment.this.mHandler.postDelayed(this, 100);
                }
            }
        };
    }

    public static WebViewFragment newInstance(String URL, String username, String password, Boolean javascriptNav) {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle args = new Bundle(CLICK_ON_WEBVIEW);
        args.putString(ARG_URL, URL);
        args.putString(ARG_USER, username);
        args.putString(ARG_PASS, password);
        args.putBoolean(ARG_JS, javascriptNav);
        webViewFragment.setArguments(args);
        return webViewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_webview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_exit:
                exitActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint({"SetJavaScriptEnabled"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.home));
        View root = inflater.inflate(R.layout.fragment_web_view, container, false);
        this.mWebView = (WebView) root.findViewById(R.id.web_view);
        this.mProgressBar = (ProgressBar) root.findViewById(R.id.progress_bar);
        this.mHandler.postDelayed(this.mRunnable, 100);
        this.mWebView.setWebViewClient(new WebViewClient());
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setAppCacheEnabled(true);
        this.mWebView.getSettings().setBuiltInZoomControls(true);
        this.mWebView.setWebViewClient(this.client);
        this.mWebView.setVerticalScrollBarEnabled(false);

        if(!getArguments().getString(ARG_URL).equals(getString(R.string.uri)))
            this.mWebView.loadUrl(getArguments().getString(ARG_URL));
        else {
            String mUsername = getArguments().getString(ARG_USER);
            String mPassword = getArguments().getString(ARG_PASS);
            if (mUsername != null && mPassword != null && !loginCredentialsEntered) {
            /*mWebView.loadUrl("javascript: {" +
                    "document.getElementById('portalAccountUsername').value = '"+mUsername+"';" +
                    "document.getElementById('portalAccountPassword').value = '"+mPassword+"';" +
                    "document.getElementById('LoginButton').click();" +
                    "};");*/
                mWebView.postUrl(getArguments().getString(ARG_URL), EncodingUtils.getBytes(
                        String.format(getString(R.string.url_postrequest),
                                mUsername, mPassword), "utf-8"));
                loginCredentialsEntered = true;

                /*
            REQUIRES SDK v17+, use for grabbing html content for document id/div element selection
        */
            /*this.client = new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                *//*if (url.startsWith("tel:")) {
                    WebViewFragment.this.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(url)));
                    return true;
                } else if (url.startsWith("mailto:")) {
                    WebViewFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    return true;
                } else if (url.startsWith("sms:")) {
                    WebViewFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    return true;
                } else if (url.startsWith("vnd:")) {
                    WebViewFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    return true;
                } else if (url.startsWith("https://www.youtube.com/") || url.startsWith("http://www.youtube.com/")) {
                    WebViewFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    return true;
                } else {
                    WebViewFragment.this.setProgressBarWebView();
                    return false;
                }*//*
                        return false;
                    }

                };*/
                mWebView.addJavascriptInterface(new JavascriptLoadInterface(), "HTMLOUT");
                this.mWebView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        Log.d("URL", url);
                        /*
                            Must be hard coded to prevent activity attachment exception
                         */
                        if(url.contains("ParentPortal/LoginParent.aspx?page=default.aspx") || url.contains("ParentPortal/m/loginparent.html#Home")){
                            lCRootPageReach = 0; Log.d("CHECK", "lcRootpage = 0");
                        }else if(url.contains("https://parentnet.tustin.k12.ca.us/ParentPortal/LoginParent.aspx?page=default.aspx/")){
                            lCRootPageReach = 1;
                        }else {
                            lCRootPageReach = -1;Log.d("CHECK", "lcRootpage = -1");
                        }
                        mWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                        super.onPageFinished(view, url);
                    }
                });
                /*
                DEBUGGING SOLELY
                 */
                this.mWebView.setWebChromeClient(new WebChromeClient(){
                    @Override
                    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                        Log.d("CHECK",consoleMessage.message());
                        //debugging purposes, only if chromium console enabled
                        try{
                            if(consoleMessage.message().toLowerCase().contains(getString(R.string.check_email_invalid))||consoleMessage.message().toLowerCase().contains(getString(R.string.check_password_invalid))){
                                lCRootPageReach=1; Log.d("CHECK", "lcRootpage = 1");
                            }

                        }catch (IllegalStateException e){
                            e.printStackTrace();
                        }


                        return super.onConsoleMessage(consoleMessage);
                    }
                });
            }
        }
        return root;
    }

    public boolean handleMessage(Message msg) {
        if (msg.what == CLICK_ON_URL) {
            this.handler.removeMessages(CLICK_ON_WEBVIEW);
            return true;
        } else if (msg.what != CLICK_ON_WEBVIEW) {
            return false;
        } else {
            Toast.makeText(getActivity(), "WebView clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    class JavascriptLoadInterface{

        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html){
            str_CnfrmDoc = html;
            Document mDoc = Jsoup.parse(str_CnfrmDoc);
            Element div = mDoc.getElementById("errorContainer");
            String attr = div.attr("style");
            if(attr.contains("display:block;")){
                lCRootPageReach = 1; Log.d("CHECK", "lcRootpage = 0");
            }/*else lCRootPageReach=-1;*/
        }
    }

    //Exit action bar event handler
    public void exitActivity(){
        getActivity().finish();
        mConfirmMessage = null;
    }

    @Override
    public void onDestroy() {
        /*mWebView.clearCache(true);
        mWebView.clearHistory();
        CookieSyncManager.createInstance(this.getActivity());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();*/
        super.onDestroy();
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.web_view && event.getAction() == 0) {
            this.handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500);
        }
        return false;
    }
}

