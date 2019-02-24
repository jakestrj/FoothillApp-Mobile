package com.novallc.foothillappmobile.activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.ListViewAssets.BaseActivity;
import com.novallc.foothillappmobile.activity.util.Utilities;
import com.novallc.foothillappmobile.activity.util.WeakRunnable;

import org.w3c.dom.Text;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {
    static final long APP_LAUNCH_DELAY_THRESHOLD;
    private static final int MILLISECONDS_TO_KEEP_SPLASH_SCREEN = 300;
    private BreathingAnimatorListener mBreathingAnimatorListener;
    private GotoNextScreenRunnable mGotoNextScreen;
    private SharedPreferences mPrefs = null;
    private final Handler mHandler;
    private boolean mHasStateSaved;
    private boolean mIsFirstLaunch;
    private boolean mIsSoftLaunch;
    private CountDownLatch mLatch;
    private long mStartTimeMillis;
    private TextView mQuoteText;

    /* renamed from: com.android.prod.activity.SplashActivity.1 */
    class AnonymousClass1 implements AnimatorUpdateListener {
        final /* synthetic */ ImageView val$logoView;

        AnonymousClass1(ImageView imageView) {
            this.val$logoView = imageView;
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            float value = ((Float) animation.getAnimatedValue()).floatValue();
            this.val$logoView.setScaleX(value);
            this.val$logoView.setScaleY(value);
        }
    }

    /* renamed from: com.android.prod.activity.SplashActivity.2 */
    class AnonymousClass2 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$fakeBackgroundHeight;
        final /* synthetic */ Intent val$intent;

        /* renamed from: com.android.prod.activity.SplashActivity.2.1 */
        class AnonymousClass1 implements AnimatorUpdateListener {
            final /* synthetic */ View val$fakeBackground;

            AnonymousClass1(View view) {
                this.val$fakeBackground = view;
            }

            public void onAnimationUpdate(ValueAnimator animation) {
                LayoutParams lp = this.val$fakeBackground.getLayoutParams();
                lp.height = ((Integer) animation.getAnimatedValue()).intValue();
                this.val$fakeBackground.setLayoutParams(lp);
            }
        }

        AnonymousClass2(int i, Intent intent) {
            this.val$fakeBackgroundHeight = i;
            this.val$intent = intent;
        }

        public void onAnimationEnd(Animator animation) {
            View fakeBackground = SplashActivity.this.findViewById(R.id.fake_background);
            ValueAnimator heightAnim = ValueAnimator.ofInt(new int[]{0, this.val$fakeBackgroundHeight});
            heightAnim.addUpdateListener(new AnonymousClass1(fakeBackground));
            heightAnim.setDuration(200);
            heightAnim.start();
            heightAnim.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    SplashActivity.this.startActivity(AnonymousClass2.this.val$intent);
                    SplashActivity.this.finish();
                    SplashActivity.this.overridePendingTransition(0, 0);
                }
            });
        }
    }

    /* renamed from: com.android.prod.activity.SplashActivity.3 */
    class AnonymousClass3 implements Runnable {
        final /* synthetic */ boolean val$isValid;

        AnonymousClass3(boolean z) {
            this.val$isValid = z;
        }

        public void run() {
            try {
                SplashActivity.this.mLatch.await(2000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {

            } finally {
                SplashActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (AnonymousClass3.this.val$isValid) {
                            SplashActivity.this.goToNextScreen(false);
                            return;
                        }
                    }
                });
            }
        }
    }

    private class BreathingAnimatorListener extends AnimatorListenerAdapter {
        private boolean mComplete;
        private AnimatorListener mOnCompleteListener;
        private Animator mScaleDown;
        private Animator mScaleUp;
        private View mView;

        public BreathingAnimatorListener(View view, Animator scaleUp, Animator scaleDown) {
            this.mView = view;
            this.mScaleUp = scaleUp;
            this.mScaleDown = scaleDown;
        }

        public void onAnimationEnd(Animator animation) {
            if (this.mComplete) {
                this.mView.animate().setInterpolator(new AnticipateInterpolator(4.0f)).scaleX(0.0f).scaleY(0.0f).setDuration(400).setListener(this.mOnCompleteListener).start();
            } else if (animation == this.mScaleUp) {
                this.mScaleDown.setStartDelay(SplashActivity.APP_LAUNCH_DELAY_THRESHOLD);
                this.mScaleDown.start();
            } else {
                this.mScaleUp.setStartDelay(SplashActivity.APP_LAUNCH_DELAY_THRESHOLD);
                this.mScaleUp.start();
            }
        }

        public void complete(AnimatorListener onCompleteListener) {
            this.mOnCompleteListener = onCompleteListener;
            this.mComplete = true;
        }
    }

    public SplashActivity() {
        this.mHandler = new Handler();
        this.mIsSoftLaunch = false;
        this.mIsFirstLaunch = false;
    }

    private static class GotoNextScreenRunnable extends WeakRunnable<SplashActivity> {
        public GotoNextScreenRunnable(SplashActivity activity) {
            super(activity);
        }

        public void run() {
            SplashActivity activity = (SplashActivity) this.weakRef.get();
            if (activity != null) {
                activity.goToNextScreen(false);
            }
        }
    }

    static {
        APP_LAUNCH_DELAY_THRESHOLD = TimeUnit.SECONDS.toMillis(10);
    }

    @SuppressLint({"InlinedApi"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
        setContentView((int) R.layout.splash_activity);
        //this.mLatch = new CountDownLatch(1);
        this.mStartTimeMillis = SystemClock.elapsedRealtime();
        /*if (savedInstanceState != null) {
            this.mIsSoftLaunch = true;
        } else if (!getHotelTonightApplication().isFirstLaunchLogged()) {
            this.mIsFirstLaunch = true;
        }*/
        this.mGotoNextScreen = new GotoNextScreenRunnable(this);
        if (VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        }
    }

    public void onStart() {
        super.onStart();
        this.mQuoteText = (TextView) findViewById(R.id.splash_message);
        this.mQuoteText.setTypeface(com.novallc.foothillappmobile.activity.WebViewAssets.BaseActivity.sRobotoLight);
        this.mQuoteText.animate().alpha(1.0f).setStartDelay(200).setDuration(700);
        ImageView logoView = (ImageView) findViewById(R.id.logo);
        ValueAnimator logoScaleUp = ValueAnimator.ofFloat(new float[]{1.0f, 1.1f});
        logoScaleUp.setInterpolator(new DecelerateInterpolator());
        logoScaleUp.setDuration(750);
        logoScaleUp.setStartDelay(200);
        AnimatorUpdateListener updateListener = new AnonymousClass1(logoView);
        logoScaleUp.addUpdateListener(updateListener);
        ValueAnimator logoScaleDown = ValueAnimator.ofFloat(new float[]{1.1f, 1.0f});
        logoScaleDown.setInterpolator(new AccelerateInterpolator());
        logoScaleDown.setDuration(750);
        logoScaleDown.addUpdateListener(updateListener);
        this.mBreathingAnimatorListener = new BreathingAnimatorListener(logoView, logoScaleUp, logoScaleDown);
        logoScaleUp.addListener(this.mBreathingAnimatorListener);
        logoScaleDown.addListener(this.mBreathingAnimatorListener);
        logoScaleUp.start();

        goToNextScreenAfterDelay(MILLISECONDS_TO_KEEP_SPLASH_SCREEN);
    }

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.LOCATION_SERVICES : //21
                Analytics.breadcrumb("location services " + (resultCode == -1 ? "on" : "off"));
                if (resultCode == -1) {
                    initLocation();
                    this.mHandler.postDelayed(this.mGotoNextScreen, 8000);
                    return;
                }
            default:
        }
    }*/

    private void goToNextScreenAfterDelay(int delayTime) {
        this.mHandler.removeCallbacks(this.mGotoNextScreen);
        this.mHandler.postDelayed(this.mGotoNextScreen, (long) delayTime);
    }

    private void goToNextScreen(boolean isGeoLocated) {
        if (!isFinishing()) {
            startActivity(isGeoLocated);
        }
    }

    private void startActivity(boolean isGeoLocated) {
        Intent intent;
        //mPrefs.edit().putBoolean("isFirstLaunch", true).apply();
        if(mPrefs.getBoolean("isFirstLaunch", true)) {
            intent = new Intent(this, IntroActivity.class);
            mPrefs.edit().putBoolean("isFirstLaunch", false).apply();
        }
        else intent = new Intent(this, MainActivity.class);
        int actionBarSize = Utilities.getActionBarSize(this);
        int fakeBackgroundHeight = (Utilities.getScreenSize(this)[1] - actionBarSize) - Utilities.getStatusBarHeight(this);
        //this.mQuoteText.animate().alpha(0.0f).setDuration(200);
        if (this.mBreathingAnimatorListener != null) {
            this.mBreathingAnimatorListener.complete(new AnonymousClass2(fakeBackgroundHeight, intent));
            return;
        }
        startActivity(intent);
        finish();
    }
}
