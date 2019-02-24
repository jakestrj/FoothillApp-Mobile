package com.novallc.foothillappmobile.activity.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;

import com.novallc.foothillappmobile.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Utilities {
    public static final String DEFAULT_COUNTRY_CODE = "ZZ";
    private static final String PHONE_CALL_INTENT_STRING = "tel:%s";
    private static Handler mainHandler;
    private static final Calendar reusableCalendar;

    static class AnonymousClass1 implements Target {
        final /* synthetic */ int val$id;
        final /* synthetic */ Builder val$notificationBuilder;
        final /* synthetic */ NotificationManagerCompat val$notifications;
        final /* synthetic */ String val$tag;

        AnonymousClass1(String str, NotificationManagerCompat notificationManagerCompat, int i, Builder builder) {
            this.val$tag = str;
            this.val$notifications = notificationManagerCompat;
            this.val$id = i;
            this.val$notificationBuilder = builder;
        }

        public void onBitmapLoaded(Bitmap bitmap, LoadedFrom loadFrom) {
            if (this.val$tag == null) {
                this.val$notifications.notify(this.val$id, this.val$notificationBuilder.setLargeIcon(bitmap).build());
            } else {
                this.val$notifications.notify(this.val$tag, this.val$id, this.val$notificationBuilder.setLargeIcon(bitmap).build());
            }
        }

        public void onBitmapFailed(Drawable errorDrawable) {
        }

        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    }

    static {
        reusableCalendar = Calendar.getInstance();
    }

    public static void dispatchToMainThread(Runnable runnable) {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        mainHandler.post(runnable);
    }

    public static Bitmap drawText(Bitmap bitmap, String text, int textSize, int textColor, float padding) {
        TextPaint textPaint = new TextPaint(1);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setColor(textColor);
        textPaint.setTextSize((float) textSize);
        float textHeight = textPaint.descent() - textPaint.ascent();
        int height = (int) ((2.0f * padding) + textHeight);
        int width = (int) ((2.0f * padding) + textPaint.measureText(text));
        Bitmap drawnBitmap = Bitmap.createBitmap(width, (int) (((float) height) + padding), Config.ARGB_8888);
        Canvas canvas = new Canvas(drawnBitmap);
        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false), 0.0f, 0.0f, new Paint(1));
        canvas.drawText(text, padding, (((((float) height) - textHeight) / 2.0f) - textPaint.ascent()) - 2.0f, textPaint);
        return drawnBitmap;
    }

    public static int modulus(int a, int b) {
        return ((a % b) + b) % b;
    }

    @SuppressLint({"InlinedApi"})
    public static int getActionBarSize(Context context) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.actionBarSize, value, true);
        return context.getResources().getDimensionPixelSize(value.resourceId);
    }

    public static int[] getScreenSize(Activity activity) {
        int width;
        int height;
        Display display = activity.getWindowManager().getDefaultDisplay();
        if (VERSION.SDK_INT >= 13) {
            Point dimensions = new Point();
            display.getSize(dimensions);
            width = dimensions.x;
            height = dimensions.y;
        } else {
            width = display.getWidth();
            height = display.getHeight();
        }
        return new int[]{width, height};
    }

    public static int getStatusBarHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return activity.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @SuppressLint({"NewApi"})
    public static void setBackgroundCompat(View view, Drawable background) {
        if (VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(background);
        } else {
            view.setBackground(background);
        }
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static String getReferrerString(Activity activity) {
        Uri referrerUri;
        if (VERSION.SDK_INT >= 22) {
            referrerUri = activity.getReferrer();
        } else {
            referrerUri = getReferrerCompatible(activity.getIntent());
        }
        if (referrerUri != null) {
            return referrerUri.toString();
        }
        if (activity.getIntent().getBooleanExtra("com.google.android.appstreaming.intent.extra.ON_APP_STREAMING", false)) {
            return "app_streaming";
        }
        return null;
    }

    private static Uri getReferrerCompatible(Intent intent) {
        Uri referrerUri = (Uri) intent.getParcelableExtra("android.intent.extra.REFERRER");
        if (referrerUri != null) {
            return referrerUri;
        }
        String referrer = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        if (referrer == null) {
            return null;
        }
        try {
            return Uri.parse(referrer);
        } catch (ParseException e) {
            return null;
        }
    }

    public static int parseIntSafely(String intString, int defaultValue) {
        try {
            defaultValue = Integer.parseInt(intString, 10);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public static boolean shouldUseCalligraphy() {
        return VERSION.SDK_INT < 21;
    }

    public static <T> ArrayList<T> asArrayList(List<T> list) {
        if (ArrayList.class.isAssignableFrom(list.getClass())) {
            return (ArrayList) list;
        }
        return new ArrayList(list);
    }
}
