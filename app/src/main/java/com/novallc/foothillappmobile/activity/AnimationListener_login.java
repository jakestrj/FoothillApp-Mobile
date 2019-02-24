package com.novallc.foothillappmobile.activity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.novallc.foothillappmobile.R;

class AnimationListener_login implements AnimationListener {
    private final /* synthetic */ LinearLayout mLinearLayout;
    private final /* synthetic */ Button mButton;
    private final /* synthetic */ Login_anim mloginAnim;
    private final /* synthetic */ TextView mTextView;
    private final /* synthetic */ CheckBox mCheckBox;

    AnimationListener_login(Login_anim login_anim, LinearLayout linearLayout, TextView textView, CheckBox checkBox, Button button) {
        this.mloginAnim = login_anim;
        this.mLinearLayout = linearLayout;
        this.mTextView = textView;
        this.mButton = button;
        this.mCheckBox = checkBox;
    }

    public void onAnimationEnd(Animation animation) {
        this.mLinearLayout.setVisibility(View.VISIBLE);
        this.mTextView.setVisibility(View.VISIBLE);
        this.mButton.setVisibility(View.VISIBLE);
        this.mCheckBox.setVisibility(View.VISIBLE);
        Animation loadAnimation = AnimationUtils.loadAnimation(this.mloginAnim, R.anim.fade);
        this.mLinearLayout.startAnimation(loadAnimation);
        this.mTextView.startAnimation(loadAnimation);
        this.mButton.startAnimation(loadAnimation);
        this.mCheckBox.startAnimation(loadAnimation);

        loadAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                try {
                    //mloginAnim.handleLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }
}