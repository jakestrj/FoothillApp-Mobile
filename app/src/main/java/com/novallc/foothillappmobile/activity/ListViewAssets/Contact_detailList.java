package com.novallc.foothillappmobile.activity.ListViewAssets;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.searchviewanimations.JJSearchView;
import com.novallc.foothillappmobile.searchviewanimations.anim.controller.JJChangeArrowController;

import java.net.Inet4Address;
import java.util.Arrays;
import java.util.List;

import static com.novallc.foothillappmobile.activity.ListViewAssets.Contact_Model.getItem;
import static java.lang.Math.abs;

public class Contact_detailList extends Activity implements ObservableScrollViewCallbacks{

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private View mImageView;
    private View mOverlayView;
    private ObservableScrollView mScrollView;
    private TextView mTitleView;
    private TextView mSubtitleView;
    private TextView mTvNumber3;
    private TextView mTvNumber4;
    private TextView mTvNumber5;
    private TextView mTvNumber6;
    private View mFab;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private boolean mFabIsShown;

    public static String t_emaAddress;
    private String t_name;
    private String t_clsLocation;
    private String t_division;
    public final static String ID = "ID";
    public Contact_Model mContact;
    private Context mContext;
    private ImageView i_compose_msg;


    //Split variables for string modification
    private List<String> _nameSplit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail_list);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        TypedValue tv = new TypedValue();
        mActionBarSize = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());

        mImageView = findViewById(R.id.image);
        mOverlayView = findViewById(R.id.overlay);
        mTvNumber3 = (TextView)findViewById(R.id.tvNumber3);
//        mTvNumber4 = (TextView)findViewById(R.id.tvNumber4);
        mTvNumber5 = (TextView)findViewById(R.id.tvNumber5);
        mTvNumber6 = (TextView)findViewById(R.id.tvNumber6);
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mTitleView = (TextView) findViewById(R.id.title);
        mSubtitleView = (TextView)findViewById(R.id.subtitle);
        setTitle(null);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode e = new Explode();
            e.setDuration(500);
            e.excludeTarget(android.R.id.statusBarBackground, true);
            e.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setReturnTransition(e);
            getWindow().setEnterTransition(e);
            getWindow().setExitTransition(e);
            mImageView.setTransitionName(getString(R.string.transition_name_circle));
            //getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_target_transition));
        }

        mFab = findViewById(R.id.fab);
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
//                mScrollView.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);
                mScrollView.smoothScrollTo(0,0);
                // The initial scrollY is 0, so it won't invoke onScrollChanged()
                onScrollChanged(0, false, false);
            }
        });

        mContext = getApplicationContext();
        mContact = Contact_Model.getItem(getIntent().getIntExtra(ID, 0));
        Log.d("ID", Integer.toString(getIntent().getIntExtra(ID, 0)));
        Log.d("NAME", mContact.get(Contact_Model.Field.NAME));

        t_clsLocation = mContact.get(Contact_Model.Field.CLASS);
        t_emaAddress = mContact.get(Contact_Model.Field.EMAIL);
        t_name = mContact.get(Contact_Model.Field.NAME);
        t_division = mContact.get(Contact_Model.Field.DIVISION);
//        t_clsLocation = getIntent().getIntExtra("clsLocation", 000);
//        t_emaAddress = getIntent().getStringExtra("emaAddress");
//        t_name = getIntent().getStringExtra("tName");
//        t_division = getIntent().getStringExtra("division");
//        _nameSplit = Arrays.asList(t_name.split(","));
        mTitleView.setText(t_name); //_nameSplit.get(1) + " " + _nameSplit.get(0)
        mSubtitleView.setText(t_division);
        mTvNumber3.setText(t_emaAddress);
        mTvNumber5.setText(String.format(getResources().getString(R.string.room_number), t_clsLocation));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Contact_detailList.this, "FAB clicked", Toast.LENGTH_SHORT).show();
                Intent mEmailIntent = new Intent(Intent.ACTION_VIEW);
                mEmailIntent.setData(Uri.parse("mailto:" + Contact_detailList.this.t_emaAddress));
                startActivity(mEmailIntent);
                Log.d("CHECK", Contact_detailList.this.t_emaAddress);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hideFab();
        ViewPropertyAnimator.animate(mFab).cancel();
        ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(1000).start();
        mFabIsShown = true;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        ViewHelper.setPivotX(mSubtitleView, 0);
        ViewHelper.setPivotY(mSubtitleView, 0);
        ViewHelper.setScaleX(mSubtitleView, scale);
        ViewHelper.setScaleY(mSubtitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);
        ViewHelper.setTranslationY(mSubtitleView, titleTranslationY);

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
            lp.leftMargin = mOverlayView.getWidth() - mFabMargin - mFab.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFab.requestLayout();
        } else {
            ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
            ViewHelper.setTranslationY(mFab, fabTranslationY);
        }

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }

}
