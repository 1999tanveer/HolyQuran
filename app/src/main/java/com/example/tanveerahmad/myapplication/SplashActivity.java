package com.example.tanveerahmad.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by TANVEER AHMAD on 07-07-2018.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Quran.DbHelper mdbhelper=new Quran.DbHelper( getApplicationContext() );
        SQLiteDatabase db=mdbhelper.getReadableDatabase();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent( SplashActivity.this, SuraListActivity.class );
                startActivity( intent );
                finish();
            }
        },2000);
    }
}
