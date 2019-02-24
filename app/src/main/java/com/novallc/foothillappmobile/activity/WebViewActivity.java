package com.novallc.foothillappmobile.activity;

//Ensure extension of webview BaseActivity
import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.WebViewAssets.BaseActivity;
import com.novallc.foothillappmobile.activity.WebViewAssets.LeftMenuAdapter;
import com.novallc.foothillappmobile.activity.model.LeftMenuItem;
import com.novallc.foothillappmobile.fragments.WebViewFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.novallc.foothillappmobile.fragments.WebViewFragment.ARG_URL;
import static com.novallc.foothillappmobile.fragments.WebViewFragment.lCRootPageReach;
import static com.novallc.foothillappmobile.fragments.WebViewFragment.mWebView;

public class WebViewActivity extends BaseActivity {
    private static final int TIME_OUT_DISMISS = 60000;
    private static final int MSG_DISMISS_DIALOG = 0;
    Fragment fragment;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<LeftMenuItem> mLeftMenuItems;
    private Toolbar mToolbar;
    private boolean mShouldFinish;
    private boolean mShouldSetContentView;
    private String mTitle;
    private TextView mToolbarTitle;
    private String mUsername;
    private String mPassword;
    private String mURL;
    private int timeElapsedR;
    public static int bool_connectionTimeout = -1;
    final Handler mHandler = new Handler();
    Runnable r;
    private Boolean userClickState = false;

    private class DrawerItemClickListener implements OnItemClickListener {
        private DrawerItemClickListener() {
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            WebViewActivity.this.selectItem(id);
            parent.setSelection(position);
        }
    }

    class AnonymousClass1 extends ActionBarDrawerToggle {
        AnonymousClass1(Activity $anonymous0, DrawerLayout $anonymous1, Toolbar $anonymous2, int $anonymous3, int $anonymous4) {
            super($anonymous0, $anonymous1, $anonymous2, $anonymous3, $anonymous4);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            WebViewActivity.this.setMainTitle(WebViewActivity.this.getResources().getString(R.string.action_aeries));
            WebViewActivity.this.supportInvalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            WebViewActivity.this.setMainTitle(WebViewActivity.this.mTitle);
            if(mTitle==null||mTitle==""){WebViewActivity.this.setMainTitle(WebViewActivity.this.getResources().getString(R.string.home));}
            WebViewActivity.this.supportInvalidateOptionsMenu();
            WebViewActivity.this.mShouldFinish = false;
//            if(mToolbar.getTitle()==null){
//                mToolbarTitle.setText(R.string.home);
//            }
        }
    }

    public WebViewActivity() {
        this.mShouldSetContentView = true;
        this.mShouldFinish = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mUsername = getIntent().getStringExtra("mUsername");
        mPassword = getIntent().getStringExtra("mPassword");
        mURL = getIntent().getStringExtra("mURL");

        mToolbar = (Toolbar)findViewById(R.id.toolbar_webview);
        mToolbarTitle = (TextView)findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(R.string.home);
        setMainViews();
        this.fragment = WebViewFragment.newInstance(mURL, mUsername, mPassword, false);
        getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_frame, this.fragment).commitAllowingStateLoss();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.black));}
        if(!(mURL.equals(getString(R.string.url_create_new_account)))){
            Log.d("CHECK", "creating dialog...");
            createDialog();
            mShouldFinish = true;
        }else {
            this.mDrawerToggle.setDrawerIndicatorEnabled(false);
            this.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mToolbarTitle.setText(getString(R.string.craete_account_headertitle));
        }
    }

    protected void onDestroy() {
        //Inherited webview from WebViewFragment.class
        mWebView.clearCache(true);
        mWebView.clearHistory();
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    private void createDialog(){

        final Dialog mDialogSignin = new Dialog(WebViewActivity.this);
        mDialogSignin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogSignin.setContentView(R.layout.dialog_signin_webview);
        TextView dialogButton = (TextView)mDialogSignin.findViewById(R.id.dialog_cancel_signin);
        mDialogSignin.setCancelable(false);

        //Event handlers, back-pressed, cancel-click and timeout handler
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogSignin.dismiss();
                finish();
                WebViewFragment.mConfirmMessage = null;
                userClickState = true;
            }
        });
        mDialogSignin.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mDialogSignin.dismiss();
                    finish();
                    WebViewFragment.mConfirmMessage = null;
                    userClickState = true;
                }
                return true;
            }
        });
        mDialogSignin.show();

        r = new Runnable() {
            public void run() {
                int lCConfirmed = WebViewFragment.lCRootPageReach;
                /*
                    lcconfirmed false, exit runnnable, remove callbacks and recycler elements
                    bool_connectionTimeout: -1 = null, 0 = loginauthfailed, 1 = connectiontimeout
                    if needed to exit run, switch generates unreachable statements
                 */

                if(lCConfirmed==0){
                    Log.d("CHECK", "Runnable Destroyed with Exit Code, lCConfirmed = " + lCConfirmed);
                    mDialogSignin.dismiss();
                    bool_connectionTimeout = -1;
                    mHandler.removeCallbacksAndMessages(r);
                    lCConfirmed = -1; WebViewFragment.lCRootPageReach = -1;
                    return;
                }
                else if(lCConfirmed==1){
                    Log.d("CHECK", "Runnable Destroyed with Exit Code, lCConfirmed = " + lCConfirmed);
                    mHandler.removeCallbacksAndMessages(r);
                    bool_connectionTimeout = 0;
                    lCConfirmed = -1; WebViewFragment.lCRootPageReach = -1;
                    mDialogSignin.dismiss();
                    finish();
                    return;
                }/*else if(lCConfirmed==-1){
                    Log.d("CHECK", "Runnable Destroyed with Exit Code, lCConfirmed = " + lCConfirmed);
                    mHandler.removeCallbacksAndMessages(r);
                    bool_connectionTimeout = 0;
                    lCConfirmed = -1; WebViewFragment.lCRootPageReach = -1;
                    mDialogSignin.dismiss();
                    finish();
                    return;
                } */else if(timeElapsedR>=TIME_OUT_DISMISS){
                    Log.d("CHECK", "Runnable Destroyed with Exit Code, lCConfirmed = " + lCConfirmed);
                    mHandler.removeCallbacksAndMessages(r);
                    bool_connectionTimeout = 1;
                    lCConfirmed = -1; WebViewFragment.lCRootPageReach = -1;
                    mDialogSignin.dismiss();
                    finish();
                    return;
                }else if(userClickState){
                    Log.d("CHECK", "Runnable Destroyed with Exit Code, lCConfirmed = " + lCConfirmed);
                    mHandler.removeCallbacksAndMessages(r);
                    bool_connectionTimeout = -1;
                    lCConfirmed = -1; WebViewFragment.lCRootPageReach = -1;
                    return;
                }
                timeElapsedR += 750;
                mHandler.postDelayed(this, 750);
            }
        };
        mHandler.postDelayed(r,750);
    }

    @SuppressLint({"NewApi"})
    private void setMainViews() {
        mToolbar = (Toolbar)findViewById(R.id.toolbar_webview);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_aeriesicon_actionbar);
        getSupportActionBar().setTitle(getString(R.string.home));
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mDrawerList = (ListView) findViewById(R.id.left_drawer);
        this.mDrawerLayout.setFocusableInTouchMode(false);
        initLeftMenuItem();
        this.mDrawerList.setAdapter(new LeftMenuAdapter(this, this.mLeftMenuItems));
        this.mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        this.mDrawerToggle = new AnonymousClass1(this, this.mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        this.mDrawerLayout.addDrawerListener(this.mDrawerToggle);
    }


    private void initLeftMenuItem() {
        this.mLeftMenuItems = new ArrayList();
        this.mLeftMenuItems.add(new LeftMenuItem(R.drawable.ic_leftmenu_home, getString(R.string.home), 11));
        this.mLeftMenuItems.add(new LeftMenuItem(R.drawable.ic_leftmenu_gradebook, getString(R.string.gradebook), 12));
        this.mLeftMenuItems.add(new LeftMenuItem(R.drawable.ic_leftmenu_profile, getString(R.string.profile), 13));
        this.mLeftMenuItems.add(new LeftMenuItem(R.drawable.ic_leftmenu_attendance, getString(R.string.attendance), 14));
        this.mLeftMenuItems.add(new LeftMenuItem(R.drawable.ic_leftmenu_courses, getString(R.string.course_requests), 15));
        this.mLeftMenuItems.add(new LeftMenuItem(R.drawable.ic_leftmenu_transcripts, getString(R.string.transcripts), 16));
    }

    private void selectItem(long tag) {
        this.mDrawerLayout.closeDrawer(this.mDrawerList);
        this.fragment = WebViewFragment.newInstance(getString(R.string.uri), mUsername, mPassword, false);
        long pos = ((LeftMenuItem) this.mLeftMenuItems.get((int) tag)).getTag();
        //Currently 4th parameter is useless, default to false
        switch ((int)pos){
            case 11:
                this.fragment = WebViewFragment.newInstance(getString(R.string.url_home), null, null, false);
                this.mTitle = getString(R.string.home);
                break;
            case 12:
                this.fragment = WebViewFragment.newInstance(getString(R.string.url_gradebook), null, null, false);
                this.mTitle = getString(R.string.gradebook);
                break;
            case 13:
                this.fragment = WebViewFragment.newInstance(getString(R.string.url_profile), null, null, false);
                this.mTitle = getString(R.string.profile);
                break;
            case 14:
                this.fragment = WebViewFragment.newInstance(getString(R.string.url_attendence), null, null, false);
                this.mTitle = getString(R.string.attendance);
                break;
            case 15:
                this.fragment = WebViewFragment.newInstance(getString(R.string.url_course_requests), null, null, false);
                this.mTitle = getString(R.string.course_requests);
                break;
            case 16:
                this.fragment = WebViewFragment.newInstance(getString(R.string.url_transcripts), null, null, false);
                this.mTitle = getString(R.string.transcripts);
                break;
        }
        getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_frame, this.fragment).commitAllowingStateLoss();
    }

    public void onBackPressed() {
        if ((this.fragment instanceof WebViewFragment) && ((WebViewFragment) this.fragment).getWebView().canGoBack()) {
            ((WebViewFragment) this.fragment).getWebView().goBack();
            ((WebViewFragment) this.fragment).setProgressBarWebView();
        } else if (!this.mShouldFinish && !this.mDrawerLayout.isDrawerOpen(this.mDrawerList) && !mURL.equals(getString(R.string.url_create_new_account))) {
            makeToast((int) R.string.confirm_exit);
            this.mDrawerLayout.openDrawer(this.mDrawerList);
            this.mShouldFinish = true;
        } else if (this.mShouldFinish || !this.mDrawerLayout.isDrawerOpen(this.mDrawerList)) {
            super.onBackPressed();
        } else {
            this.mDrawerLayout.closeDrawer(this.mDrawerList);
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (this.mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        switch (item.getItemId()) {
//            case R.id.action_exit:
//                finishActivity(107);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @SuppressLint({"NewApi"})
    public void setMainTitle(String title) {
        mToolbarTitle.setText(title);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mDrawerToggle.onConfigurationChanged(newConfig);
    }
}