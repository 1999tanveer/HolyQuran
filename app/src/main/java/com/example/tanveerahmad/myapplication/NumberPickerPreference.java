package com.example.tanveerahmad.myapplication;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by TANVEER AHMAD on 07-06-2018.
 */


public class NumberPickerPreference extends Preference {
    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
    }

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super( context, attrs );
    }

    public NumberPickerPreference(Context context) {
        super( context );
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView( parent );
        LayoutInflater li = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View v= li.inflate(R.layout.setting_toolbar, parent, false);

    return v;
    }
}
