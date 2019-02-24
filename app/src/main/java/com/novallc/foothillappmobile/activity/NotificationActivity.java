package com.novallc.foothillappmobile.activity;

import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.cgollner.unclouded.preferences.SwitchPreferenceCompat;
import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.metrics.Analytics;

import org.jsoup.nodes.BooleanAttribute;

public class NotificationActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_actionbar);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_pref);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new _Pref_NOTIFICATIONS()).commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public static class _Pref_NOTIFICATIONS extends PreferenceFragment
    {
        SharedPreferences mPrefs;
        private Boolean isNotifEnabled;

        @Override
        public void onStart(){
            super.onStart();
        }

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Notifications");
            addPreferencesFromResource(R.xml.pref_notif);
            final SwitchPreferenceCompat switchPref = (SwitchPreferenceCompat) getPreferenceManager().findPreference("pushnotif_checkbox");
            mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            isNotifEnabled = mPrefs.getBoolean("mNotifSwitchPref", true);
            if(isNotifEnabled)
                mPrefs.edit().putBoolean("mNotifSwitchPref", true).apply();
            else
                mPrefs.edit().putBoolean("mNotifSwitchPref", false).apply();
            switchPref.setChecked(isNotifEnabled);

            switchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("CHECK", "onPreferenceChange: ");
                    isNotifEnabled = ((Boolean) newValue).booleanValue();
                    mPrefs.edit().remove("mNotifSwitchPref").apply();
                    mPrefs.edit().putBoolean("mNotifSwitchPref", isNotifEnabled).apply();
                    if(isNotifEnabled)
                        NotifUpdater.mNotifyMgr.notify(0, NotifUpdater.mNotifBuilder.build());
                    else
                        NotifUpdater.mNotifyMgr.cancel(0);
                    return true;
                }
            });

//            if(isNotifEnabled!=null && isNotifEnabled)
//                switchPref.setChecked(true);
//            else
//                switchPref.setChecked(false);
        }
    }

}