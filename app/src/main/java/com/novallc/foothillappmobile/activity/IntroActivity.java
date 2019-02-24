package com.novallc.foothillappmobile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.novallc.foothillappmobile.R;

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance(getString(R.string.introview_title1), getString(R.string.introview_desc1), R.drawable.screensnap1, Color.parseColor("#31343d")));
        addSlide(AppIntroFragment.newInstance(getString(R.string.introview_title3), getString(R.string.introview_desc3), R.drawable.screensnap3, Color.parseColor("#4B4E57")));
        addSlide(AppIntroFragment.newInstance(getString(R.string.introview_title4), getString(R.string.introview_desc4), R.drawable.screensnap4, Color.parseColor("#7E818A")));
        addSlide(AppIntroFragment.newInstance(getString(R.string.introview_title2), getString(R.string.introview_desc2), R.drawable.screensnap2, Color.parseColor("#B4861E")));
        addSlide(AppIntroFragment.newInstance(getString(R.string.introview_title7), getString(R.string.introview_desc7), R.drawable.screensnap7, getResources().getColor(R.color.colorYellowInset)));

        showSkipButton(false);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}