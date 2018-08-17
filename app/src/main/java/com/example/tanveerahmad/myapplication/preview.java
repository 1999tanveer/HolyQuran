package com.example.tanveerahmad.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

/**
 * Created by TANVEER AHMAD on 01-06-2018.
 */

public class preview extends Preference {


        TextView text1,text2,no;
        int as,ts;
        String arab="بِسۡمِ اللّٰہِ الرَّحۡمٰنِ الرَّحِیۡمِ ﴿ ﴾",
                tamil="அளவற்ற அருளாளனும் மேன்மேலும் கருணை கட்டுபவனுமாகிய அல்லாஹ்வின் பெயரால் (ஓதுகின்றேன்)",
                english="In the name of Allah, the Gracious, the Merciful.",lan;
        String[] l;
        Context context;


        public preview(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            this.context=context;
        }
        public preview(){
            super(null );
            this.context=context;
        }

        public preview(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public preview(Context context) {
            super(context);
            this.context=context;
        }

        @Override
        public View getView(View convertView, ViewGroup parent) {
            return super.getView(convertView, parent);
        }



        @Override
        protected View onCreateView(final ViewGroup parent ) {

            super.onCreateView(parent);
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View v= li.inflate(R.layout.preview, parent, false);
            context=getContext();
            text1=(TextView) v.findViewById(R.id.tv_verse_1);
            text2=(TextView) v.findViewById(R.id.tv_verse_2);
            no=(TextView) v.findViewById(R.id.tv_verse_no);
            no.setText( "1" );

            final SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences( getContext() );
            lan=SP.getString( "pref_lang","arab,eng" );
            as=Integer.parseInt(  SP.getString( "pref_arab_size","26" ) );
            ts=Integer.parseInt(  SP.getString( "pref_trans_size","16" ) );
            l=lan.split( "," );
            switch (l[0]){
                case "arab":
                    Typeface custom_font;
                    custom_font = Typeface.createFromAsset( context.getAssets(), "fonts/noorehuda.ttf" );
                    TextView text=(TextView) v.findViewById(R.id.tv_verse_1);
                    text.setTypeface( custom_font );
                    text.setText( arab );
                    text.setTextSize( as );
                    break;
                case "tam":

                    text1.setText( tamil );
                    text1.setTextSize( ts );
                    break;
                case "eng":

                    text1.setText( english );
                    text1.setTextSize( ts );
            }
            switch (l[1]){
                case "tam":
                    text2.setText( tamil );
                    text2.setTextSize( ts );
                    break;
                case "eng":
                    text2.setText( english );
                    text2.setTextSize( ts );
                    break;
                default:text2.setText( null );
            }
            SharedPreferences.OnSharedPreferenceChangeListener listener=new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    Log.d( TAG, "onSharedPreferenceChanged: Entered" );
                    lan=SP.getString( "pref_lang","arab,eng" );
                    as=Integer.parseInt(  SP.getString( "pref_arab_size","26" ) );
                    ts=Integer.parseInt(  SP.getString( "pref_trans_size","16" ) );
                    l=lan.split( "," );

                    switch (l[0]){
                        case "arab":
                            Typeface custom_font;
                            custom_font = Typeface.createFromAsset( context.getAssets(), "fonts/noorehuda.ttf" );
                            TextView text=(TextView) v.findViewById(R.id.tv_verse_1);
                            text.setTypeface( custom_font );
                            text.setText( arab );
                            text.setTextSize( as );
                            break;
                        case "tam":text1.setText( tamil );
                            text1.setTextSize( ts );
                            break;
                        case "eng":
                            text1.setText( english );
                            text1.setTextSize( ts );
                    }
                    switch (l[1]){
                        case "tam":
                            text2.setText( tamil );
                            text2.setTextSize( ts );
                            break;
                        case "eng":
                            text2.setText( english );
                            text2.setTextSize( ts );
                            break;
                        default:text2.setText( null );
                    }

                }
            };

            SP.registerOnSharedPreferenceChangeListener(listener);

            return v;

        }

    }
