package com.novallc.foothillappmobile.activity.WebViewAssets;


import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

//fragmentactivity
public class BaseActivity extends AppCompatActivity {
    public static Typeface sRobotoBlack;
    public static Typeface sRobotoBlackItalic;
    public static Typeface sRobotoLight;
    public static Typeface sRobotoLightItalic;
    public static Typeface sRobotoThin;
    private Toast mToast;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTypefaces();
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mToast != null) {
            this.mToast.cancel();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void makeToast(int resId) {
        makeToast(getString(resId));
    }

    public void makeToast(String text) {
        if (this.mToast == null) {
            this.mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            this.mToast.setText(text);
        }
        this.mToast.show();
    }

    private void initTypefaces() {
        if (sRobotoBlack == null) {
            sRobotoBlack = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf");
        }
        if (sRobotoBlackItalic == null) {
            sRobotoBlackItalic = Typeface.createFromAsset(getAssets(), "fonts/Roboto-BlackItalic.ttf");
        }
        if (sRobotoLight == null) {
            sRobotoLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        }
        if (sRobotoLightItalic == null) {
            sRobotoLightItalic = Typeface.createFromAsset(getAssets(), "fonts/Roboto-LightItalic.ttf");
        }
        if (sRobotoThin == null) {
            sRobotoThin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        }
    }
}
