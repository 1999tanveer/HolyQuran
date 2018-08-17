package com.example.tanveerahmad.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;

import static android.content.ContentValues.TAG;

/**
 * Created by TANVEER AHMAD on 28-05-2018.
 */

public class SettingsActivity extends PreferenceActivity  {
    CheckBox arab, eng, tam;
    String lang1, lang2;
    Context context;
    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled( true );
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getFragmentManager().beginTransaction()
                .replace( android.R.id.content, new SettingsFrag() )
                .commit();

        SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences( getBaseContext() );
        SharedPreferences.OnSharedPreferenceChangeListener listener=new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d( TAG, "onSharedPreferenceChanged: Entered" );
//                recreate();
            }
        };

        SP.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }



    public static class SettingsFrag extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate( savedInstanceState );
            addPreferencesFromResource( R.xml.pref_headers );
        }

    }

//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
//                                          String key) {
//        Log.d( TAG, "onSharedPreferenceChanged: Entered" );
//        if (key.equals( "pref_lang" )) {
//            Preference connectionPref = findPreference( key );
//            // Set summary to be the user-description for the selected value
//            connectionPref.setSummary( sharedPreferences.getString( key, "" ) );
//        }
//
//    }
}