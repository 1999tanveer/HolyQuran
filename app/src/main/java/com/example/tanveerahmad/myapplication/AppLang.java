package com.example.tanveerahmad.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Locale;

public class AppLang extends Preference {

    RadioButton eng,tam;
    String lang1,lang2,lan;
    String[] l=new String[2];
    Context context;

    public AppLang(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public AppLang(){
        super(null );

    }

    public AppLang(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppLang(Context context) {
        super(context);
    }

    @Override
    public View getView(View convertView, ViewGroup parent) {
        return super.getView(convertView, parent);
    }

    @Override
    protected View onCreateView(ViewGroup parent ) {

        super.onCreateView(parent);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v= li.inflate(R.layout.app_language, parent, false);
        context=getContext();
        tam=(RadioButton) v.findViewById(R.id.che2);
        eng=(RadioButton) v.findViewById(R.id.che3);

        SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences( getContext() );
        lan=SP.getString( "pref_app_lang","en_US" );

        switch (lan){
//            case "arab":arab.setChecked( true );
//                break;
            case "tam":tam.setChecked( true );
                break;
            case "eng":eng.setChecked( true );
        }
        addListenerOnTam();
        addListenerOnEng();
        return v;

    }

    private void addListenerOnEng() {
        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(( (RadioButton)v).isChecked()){
                    lan="en_US";
                    if(tam.isChecked()){
                        tam.setChecked( false );
                    }
                    String result="App Lan ="+lan;
                    SharedPreferences.Editor editor = getSharedPreferences().edit();
                    editor.putString("pref_app_lang", lan);
                    editor.apply();
                    Configuration configuration = getContext().getResources().getConfiguration();
                    configuration.setLocale(new Locale( "en_US" ));
                    getContext().getResources().updateConfiguration(configuration,
                            getContext().getResources().getDisplayMetrics());

//                    Toast.makeText(getContext(), result,
//                            Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), "Select any lang...",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addListenerOnTam() {
        tam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(( (RadioButton)v).isChecked()){
                    lan="ta_IN";
                    if(eng.isChecked()){
                        eng.setChecked( false );
                    }
                    String result="App Lan ="+lan;
                    SharedPreferences.Editor editor = getSharedPreferences().edit();
                    editor.putString("pref_app_lang", lan);
                    editor.apply();
                    Configuration configuration = getContext().getResources().getConfiguration();
                    configuration.setLocale(new Locale( lan ));
                    getContext().getResources().updateConfiguration(configuration,
                            getContext().getResources().getDisplayMetrics());

//                    Toast.makeText(getContext(), result,
//                            Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getContext(), "Select any lang...",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}