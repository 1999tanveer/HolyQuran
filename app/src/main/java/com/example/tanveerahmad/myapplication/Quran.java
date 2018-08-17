package com.example.tanveerahmad.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import static android.content.ContentValues.TAG;

/**
 * Created by TANVEER AHMAD on 19-05-2018.
 */

public final class Quran {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private Quran() {}
    static int NO_OF_VERSES=0;


    public static int getNoOfVerses() {
        return NO_OF_VERSES;
    }




    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "quran_eng";
        public static final String VERSE_NO = "verseno";
        public static final String VERSE = "verse";
//        public static final String SURA_NO = "surano";
//        public static final String SURA_VERSE_NO = "suraverseno";
    }
    public static class FeedEntry2 implements BaseColumns {
        public static final String TABLE_NAME = "suralist";
        public static final String SURA_NO = "surano";
        public static final String SURA_ENG = "sura_eng";
        public static final String SURA_TAM = "sura_tam";
        public static final String NO_OF_VERSES_SURA = "noofverses";
    }
    public static class FeedEntry3 implements BaseColumns {
        public static final String TABLE_NAME = "suraversemap";
        public static final String SURA_NO = "surano";
        public static final String VERSE_NO = "verseno";
        public static final String SURA_VERSE_NO = "suraverseno";
    }
    public static class FeedEntry4 implements BaseColumns {
        public static final String TABLE_NAME = "quran_tam";
        public static final String VERSE_NO = "verseno";
        public static final String VERSE = "verse";
//        public static final String SURA_NO = "surano";
//        public static final String SURA_VERSE_NO = "suraverseno";
    }
    public static class FeedEntry5 implements BaseColumns {
        public static final String TABLE_NAME = "quran_ara";
        public static final String VERSE_NO = "verseno";
        public static final String VERSE = "verse";
//        public static final String SURA_NO = "surano";
//        public static final String SURA_VERSE_NO = "suraverseno";
    }

//    private static final String SQL_CREATE_ENTRIES =
//            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
//                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
//                    FeedEntry.VERSE_NO+" INT,"+
//                    FeedEntry.VERSE + " TEXT,"+
//                    FeedEntry.SURA_NO+ " INT,"+
//                    FeedEntry.SURA_VERSE_NO+ " INT)";

    private static final String SQL_CREATE_ENG_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry.VERSE_NO+" INTEGER PRIMARY KEY,"+
                    FeedEntry.VERSE + " TEXT)";

    private static final String SQL_CREATE_TAM_ENTRIES =
            "CREATE TABLE " + FeedEntry4.TABLE_NAME + " (" +
                    FeedEntry4.VERSE_NO+" INTEGER PRIMARY KEY,"+
                    FeedEntry4.VERSE + " TEXT)";

    private static final String SQL_CREATE_ARABIC_ENTRIES =
            "CREATE TABLE " + FeedEntry5.TABLE_NAME + " (" +
                    FeedEntry5.VERSE_NO+" INTEGER PRIMARY KEY,"+
                    FeedEntry5.VERSE + " TEXT)";

    private static final String SQL_CREATE_SURA_NAME =
            "CREATE TABLE " + FeedEntry2.TABLE_NAME + " (" +
                    FeedEntry2.SURA_NO+" INTEGER PRIMARY KEY,"+
                    FeedEntry2.SURA_ENG + " TEXT,"+
                    FeedEntry2.SURA_TAM + " TEXT,"+
                    FeedEntry2.NO_OF_VERSES_SURA+" INT)";

    private static final String SQL_CREATE_SURA_VERSE_MAP =
            "CREATE TABLE " + FeedEntry3.TABLE_NAME + " (" +
                    FeedEntry3.VERSE_NO+" INTEGER PRIMARY KEY,"+
                    FeedEntry3.SURA_NO + " INT,"+
                    FeedEntry3.SURA_VERSE_NO+" INT)";

//    private static final String SQL_CREATE_SURA_NAME =
//            "CREATE TABLE " + FeedEntry2.TABLE_NAME + " (" +
//                    FeedEntry2._ID + " INTEGER PRIMARY KEY," +
//                    FeedEntry2.SURA_NO+" INT,"+
//                    FeedEntry2.SURA + " TEXT,"+
//                    FeedEntry2.NO_OF_VERSES_SURA+" INT)";



    public static class DbHelper extends SQLiteOpenHelper {


        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "database.db";
        private Context context1;

        public DbHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.d(TAG, "DbHelper: Entered the constructor");
            context1=context;
        }



        public void onCreate(SQLiteDatabase db) {

            db.execSQL(SQL_CREATE_ENG_ENTRIES);
            db.execSQL(SQL_CREATE_TAM_ENTRIES);
            db.execSQL(SQL_CREATE_ARABIC_ENTRIES);
            db.execSQL(SQL_CREATE_SURA_NAME);
            db.execSQL(SQL_CREATE_SURA_VERSE_MAP);

            writedb(db);
            write_tam(db);
            write_arab(db);

        }




        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        protected void writedb(SQLiteDatabase db) {

            String verse="",suraname="";
            int surano,nov,svn;

            ContentValues values1 = new ContentValues();
            ContentValues values2 = new ContentValues();
            ContentValues values3 = new ContentValues();

            InputStream is1= context1.getResources().openRawResource(R.raw.english);
            InputStream is2= context1.getResources().openRawResource(R.raw.quran_surah_map);
            InputStream is3= context1.getResources().openRawResource(R.raw.quran_seq_chap_verse);

            BufferedReader reader1=new BufferedReader(
                    new InputStreamReader(is1, Charset.forName("UTF-8"))
            );
            BufferedReader reader2=new BufferedReader(
                    new InputStreamReader(is2, Charset.forName("UTF-8"))
            );
            BufferedReader reader3=new BufferedReader(
                    new InputStreamReader(is3, Charset.forName("UTF-8"))
            );


            //code to read sura name and write it on db.  Writing table 2
            for (int i=0;i<114;i++) {
                try {
                    suraname = reader2.readLine();
                    String[] s=suraname.split(",");
                    surano=Integer.parseInt(s[0]);
                    suraname=s[2];
                    nov=Integer.parseInt(s[1]);

                    values2.put(FeedEntry2.SURA_NO, surano);

                    values2.put(FeedEntry2.SURA_ENG, suraname);

                    suraname=s[4];
                    values2.put(FeedEntry2.SURA_TAM, suraname);

                    values2.put(FeedEntry2.NO_OF_VERSES_SURA, nov);

                    long newRowId = db.insert(Quran.FeedEntry2.TABLE_NAME, null, values2);
                    if (newRowId == -1)
                        Log.d(TAG, "write db: Suralist table" + (i+1) + " row not created " + newRowId);
                    else
                        Log.d(TAG, "write db: Sura list table" + (i+1)+" row created " + newRowId);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            //Writing table 1 verse and verse no &table 3
            do{

                try {
                    verse=reader1.readLine();
                    suraname=reader3.readLine();
                    if (verse!=null&&suraname!=null) {
                        if(verse.charAt(0)=='"')
                        {
                            verse=verse.substring(1,verse.length()-2);
                            Log.d(TAG, "writedb: Entered if block  once");
                        }
                        String[] s=suraname.split(",");
                        nov=Integer.parseInt(s[0]);
                        surano=Integer.parseInt(s[1]);
                        svn=Integer.parseInt(s[2]);
                        values1.put(Quran.FeedEntry.VERSE_NO, NO_OF_VERSES+1);
                        values1.put(Quran.FeedEntry.VERSE, verse);
                        values3.put(Quran.FeedEntry3.SURA_NO, surano);
                        values3.put(FeedEntry3.VERSE_NO, nov);
                        values3.put(Quran.FeedEntry3.SURA_VERSE_NO,svn);
                        long newRowId = db.insert(Quran.FeedEntry.TABLE_NAME, null, values1);
                        if (newRowId == -1)
                            Log.d(TAG, "write db: Table 1" + (NO_OF_VERSES+1) + " row not created " + newRowId);
                        else
                            Log.d(TAG, "write db: Table 1" + (NO_OF_VERSES+1) + " row created " + newRowId);

                        newRowId = db.insert(Quran.FeedEntry3.TABLE_NAME, null, values3);
                        if (newRowId == -1)
                            Log.d(TAG, "write db: Table 3" + (NO_OF_VERSES+1) + " row not created " + newRowId);
                        else
                            Log.d(TAG, "write db: Table 3" + (NO_OF_VERSES+1) + " row created " + newRowId);

                    }
                } catch (IOException e) {
                    Log.wtf(TAG, "Error in reading" + NO_OF_VERSES + " row from csv file ");
                    e.printStackTrace();
                }

                NO_OF_VERSES++;

            }while(verse!=null);

        }

        private void write_tam(SQLiteDatabase db) {
            String verse=new String();
            int length=0;
            ContentValues values4 = new ContentValues();
            InputStream is4= context1.getResources().openRawResource(R.raw.tamil_new);
            BufferedReader reader4=new BufferedReader(
                    new InputStreamReader(is4, Charset.forName("UTF-8"))
            );


            int NO_OF_VERSES=0;

            do{
                try {
                    verse=reader4.readLine();

                    if(verse!=null) {

//                        length = verse.length();
                        Log.d(TAG, "write_tam: Entered if block once...");
//                        for (int i = 0; i < length; i++) {
//                            if (Character.isDigit(verse.charAt(i)) && verse.substring(length - 2, length) != "ரு")
//                            verse.replace(verse.charAt(i) + "", "");
//                            }
//                        if(verse.substring(length - 5, length-3).equals( "ரு") | verse.substring(length - 4, length-2) .equals("ரு" ))
//                        {
//
//                            String verse1,verse2;
//                            verse1 = verse.substring(0,length-3);
//                            verse1=verse1.replaceAll ("[0-9]","");
//                            verse2=verse.substring(length-2,length-1);
//                            verse=verse1+verse2;
//                            Log.d(TAG, "write_tam: Entered if block..."+verse);
//                        }
//
//                        else  {
//                            Log.d(TAG, "write_tam: "+verse.substring(length - 4, length-2));
//                            Log.d(TAG, "write_tam: "+verse.substring(length - 5, length-3));
                            verse=verse.replaceAll ("[0-9]","");

//                        }

                    }

                    values4.put(Quran.FeedEntry.VERSE_NO, NO_OF_VERSES+1);
                    values4.put(Quran.FeedEntry.VERSE, verse);

                    long newRowId = db.insert(Quran.FeedEntry4.TABLE_NAME, null, values4);
                    if (newRowId == -1)
                        Log.d(TAG, "write db:Tamil-Table 4" + (NO_OF_VERSES+1) + " row not created " + newRowId);
                    else
                        Log.d(TAG, "write db:Tamil-Table 4" + (NO_OF_VERSES+1) + " row created " + newRowId);

                    } catch (IOException e) {
                    e.printStackTrace();
                }

                NO_OF_VERSES++;

            }while(verse!=null);
        }


        private void write_arab(SQLiteDatabase db) {

            String verse=new String();
            int length=0;
            ContentValues values5 = new ContentValues();
            InputStream is5= context1.getResources().openRawResource(R.raw.arabic_unicode);
            BufferedReader reader5=new BufferedReader(
                    new InputStreamReader(is5, Charset.forName("UNICODE"))
            );
            int NO_OF_VERSES=0;

            do{
                try {
                    verse=reader5.readLine();
                    if (verse!=null)
                    verse=verse.replaceAll("[۰-۹]"," ");
//                    verse = new StringBuilder(verse).insert(verse.length()-2, id).toString();
                    values5.put(Quran.FeedEntry.VERSE_NO, NO_OF_VERSES+1);
                    values5.put(Quran.FeedEntry.VERSE, verse);

                    Log.d(TAG, "write_arab: verse="+ verse);

                    long newRowId = db.insert(Quran.FeedEntry5.TABLE_NAME, null, values5);
                    if (newRowId == -1)
                        Log.d(TAG, "write db:Arabic-Table 5" + (NO_OF_VERSES+1) + " row not created " + newRowId);
                    else
                        Log.d(TAG, "write db:Arabic-Table 5" + (NO_OF_VERSES+1) + " row created " + newRowId);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                NO_OF_VERSES++;

            }while(verse!=null);
        }
    }
}
