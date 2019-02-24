package com.novallc.foothillappmobile.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoStartReciever extends BroadcastReceiver {

    Intent NotifUpdateIntent;

    public void onReceive(Context context, Intent arg1)
    {
        NotifUpdateIntent = new Intent(context,
                NotifUpdater.class);
        context.startService(NotifUpdateIntent);
        Log.i("AUTOSTART", "startedDrawerHeaderIntent");
    }
}