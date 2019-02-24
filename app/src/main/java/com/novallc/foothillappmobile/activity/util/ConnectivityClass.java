package com.novallc.foothillappmobile.activity.util;

import android.app.Activity;
import android.app.Application;

public class ConnectivityClass extends Application {

    private static ConnectivityClass mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized ConnectivityClass getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}