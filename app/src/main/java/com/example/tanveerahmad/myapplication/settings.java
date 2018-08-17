package com.example.tanveerahmad.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

public class settings extends Preference {

    CheckBox arab,eng,tam;
    String lang1,lang2,lan;
    String[] l=new String[2];
    Context context;

    public settings(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public settings(){
        super(null );

    }

    public settings(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public settings(Context context) {
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
        View v= li.inflate(R.layout.setting, parent, false);
        context=getContext();
        arab=(CheckBox) v.findViewById(R.id.che1);
        tam=(CheckBox) v.findViewById(R.id.che2);
        eng=(CheckBox) v.findViewById(R.id.che3);

        SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences( getContext() );
        lan=SP.getString( "pref_lang","arab,tam" );
        l=lan.split( "," );
        switch (l[0]){
            case "arab":arab.setChecked( true );
                break;
            case "tam":tam.setChecked( true );
                break;
            case "eng":eng.setChecked( true );
        }
        switch (l[1]){
            case "arab":arab.setChecked( true );
                break;
            case "tam":tam.setChecked( true );
                break;
            case "eng":eng.setChecked( true );
        }

        addListenerOnArab();
        addListenerOnTam();
        addListenerOnEng();
        return v;

    }

    private void addListenerOnEng() {
        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(( (CheckBox)v).isChecked()){
                    if (arab.isChecked()&&tam.isChecked()){
                        tam.setChecked(false);
                        lang1="arab";
                        lang2="eng";
                    }
                    else if(arab.isChecked()){
                        lang1="arab";
                        lang2="eng";
                    }
                    else if(tam.isChecked()){
                        lang1="tam";
                        lang2="eng";
                    }
                    else{
                        lang1="eng";
                        lang2=null;
                    }
                }
                else{
                    if(tam.isChecked()){
                        lang1="tam";
                        lang2="null";
                    }
                    else if(arab.isChecked()){
                        lang1="arab";
                        lang2="null";
                    }
                    else {
                        Toast.makeText(getContext(), "Select any lang...",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                String result="lang1 is "+lang1+"\nlang2 is "+lang2;
                SharedPreferences.Editor editor = getSharedPreferences().edit();
                editor.putString("pref_lang", lang1+','+lang2);
                editor.apply();


//                Toast.makeText(getContext(), result,
//                        Toast.LENGTH_LONG).show();

            }
        });


    }

    private void addListenerOnTam() {
        tam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(( (CheckBox)v).isChecked()){
                    if ( arab.isChecked() && eng.isChecked() ) {
                        eng.setChecked(false);
                        lang1="arab";
                        lang2="tam";
                    }
                    else if(arab.isChecked()){
                        lang1="arab";
                        lang2="tam";
                    }
                    else if(eng.isChecked()){
                        lang1="eng";
                        lang2="tam";
                    }
                    else {
                        lang1="tam";
                        lang2=null;
                    }

                }
                else{
                    if(eng.isChecked()){
                        lang1="eng";
                        lang2="null";
                    }
                    else if(arab.isChecked()){
                        lang1="arab";
                        lang2="null";
                    }
                    else {
                        Toast.makeText(getContext(), "Select any lang...",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                String result="lang1 is "+lang1+"\nlang2 is "+lang2;

                SharedPreferences.Editor editor = getSharedPreferences().edit();
                editor.putString("pref_lang", lang1+','+lang2);
                editor.apply();

//                Toast.makeText(getContext(), result,
//                        Toast.LENGTH_LONG).show();

            }


        });

    }

    private void addListenerOnArab() {
        arab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(( (CheckBox)v).isChecked()){
                    if (tam.isChecked()&&eng.isChecked()){
                        eng.setChecked(false);
                        tam.setChecked(false);
                        lang1="arab";
                        lang2=null;
                    }
                    else if(tam.isChecked()){
                        lang1="arab";
                        lang2="tam";
                    }
                    else if(eng.isChecked()){
                        lang1="arab";
                        lang2="eng";
                    }
                    else {
                        lang1="arab";
                        lang2=null;
                    }
                }
                else{
                    if(tam.isChecked()){
                        lang1="tam";
                        lang2="null";
                    }
                    else if(eng.isChecked()){
                        lang1="eng";
                        lang2="null";
                    }
                    else {
                        Toast.makeText(getContext(), "Select any lang...",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                String result="Reading Mode \nlang1 is "+lang1+"\nlang2 is "+lang2;

                SharedPreferences.Editor editor = getSharedPreferences().edit();
                editor.putString("pref_lang", lang1+','+lang2);
                editor.apply();

//                Toast.makeText(getContext(), result,
//                        Toast.LENGTH_LONG).show();

            }
        });
    }


}