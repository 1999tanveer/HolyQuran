package com.example.tanveerahmad.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

/**
 * Created by TANVEER AHMAD on 31-05-2018.
 */

public class SeekBar1 extends Preference implements android.widget.SeekBar.OnSeekBarChangeListener {
    SeekBar seekBar1;
    int progress;
    boolean check;
    ViewGroup parent;

    public SeekBar1(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
    }

    public SeekBar1(Context context, AttributeSet attrs) {
        super( context, attrs );
    }

    public SeekBar1(Context context) {
        super( context );
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        this.parent=parent;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View v= li.inflate(R.layout.seekbar, parent, false);
        SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences( getContext() );
//        TextView size=v.findViewById( R.id.size );
        seekBar1=(SeekBar) v.findViewById( R.id.seekbar);
        if(getTitle()=="ArabSize"||getTitle().equals( "ArabSize" )) {
//            size.setText( "Arabic Text Size" );
            seekBar1.setMax( 21 );
            int progress=Integer.parseInt(SP .getString( "pref_arab_size","24" ));
            seekBar1.setProgress( progress -18);
        }
        else if(getTitle()=="TransSize"||getTitle().equals( "TransSize" )) {
//            size.setText( "Translation Text Size" );
            seekBar1.setMax( 15 );
            int progress = Integer.parseInt( SP.getString( "pref_trans_size", "16" ));
            seekBar1.setProgress( progress -10);
        }
        seekBar1.setOnSeekBarChangeListener(this);
        return  v;
    }


    @Override
    public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
        if(getTitle()=="ArabSize"||getTitle().equals( "ArabSize" ))
            this.progress=progress+18;
        else if(getTitle()=="TransSize"||getTitle().equals( "TransSize" ))
            this.progress=progress+10;
        check=true;
    }

    @Override
    public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
//        Toast.makeText(getContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
        check=false;
    }

    @Override
    public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
//        Toast.makeText(getContext(),"seekbar touch stopped!"+progress, Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        if(check) {
            if (getTitle() == "ArabSize" || getTitle().equals( "ArabSize" ))
                editor.putString( "pref_arab_size", progress + "" );
            else if (getTitle() == "TransSize" || getTitle().equals( "TransSize" ))
                editor.putString( "pref_trans_size", progress + "" );
        }
        check=false;
        editor.apply();
    }
}