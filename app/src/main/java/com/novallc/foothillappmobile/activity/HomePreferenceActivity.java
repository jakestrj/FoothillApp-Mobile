package com.novallc.foothillappmobile.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.novallc.foothillappmobile.R;

public class HomePreferenceActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_actionbar);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_pref);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new _Pref_HOME()).commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public static class _Pref_HOME extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Settings");
            addPreferencesFromResource(R.xml.pref_home);
            Preference _notifPref = (Preference)findPreference("notif_pref");
            Preference _feedbackPref = (Preference)findPreference("feedback_pref");
            if(_notifPref!=null) {
                _notifPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        startActivity(new Intent(getActivity(), NotificationActivity.class));
                        return false;
                    }
                });
            }
            if(_feedbackPref!=null){
                _feedbackPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent mEmailIntent = new Intent(Intent.ACTION_VIEW);
                        mEmailIntent.setData(Uri.parse("mailto:" + getString(R.string.feedback_email)));
                        mEmailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject));
                        startActivity(mEmailIntent);
                        return false;
                    }
                });
            }
        }
    }

}