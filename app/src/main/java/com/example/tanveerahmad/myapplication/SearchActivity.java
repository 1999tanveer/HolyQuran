//package com.example.tanveerahmad.myapplication;
//
//import android.app.SearchManager;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.SearchView;
//import android.widget.TextView;
//
///**
// * Created by TANVEER AHMAD on 13-06-2018.
// */
//
//public class SearchActivity extends AppCompatActivity {
//
//    private static final String TAG = MainActivity.class.getSimpleName();
//
//    private TextView mTextView;
//    private EditText mEditWordView;
//    String word;
//    Button button;
//    Quran.DbHelper mdbhelper;
//    SQLiteDatabase db;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//        mdbhelper=new Quran.DbHelper( getApplicationContext() );
//        db=mdbhelper.getReadableDatabase();
////
////        mEditWordView = ((EditText) findViewById(R.id.search_word));
////        button = ((Button) findViewById(R.id.button));
////
//        mTextView = ((TextView) findViewById(R.id.search_result));
////        mdbhelper = new Quran.DbHelper(this);
////
////        button.setOnClickListener( new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                mTextView.setText( "Result\n" );
////                showResult(v);
////            }
////        } );
//        Log.d( TAG, "onCreate: Search Activity" );
//        Intent intent = getIntent();
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra( SearchManager.QUERY);
//            showResult(query);
//            Log.d( TAG, "onCreate: Search Activity if block" );
//        }
//        else{
//            String query = intent.getStringExtra( SearchManager.QUERY);
//            Log.d( TAG, "onCreate: Search Activity else block"+Intent.ACTION_SEARCH+"--"+intent.getAction());
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the options menu from XML
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_search, menu);
//
//        // Get the SearchView and set the searchable configuration
//        SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        // Assumes current activity is the searchable activity
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false);
//        searchView.setIconified( true );
//        searchView.findFocus();
//        searchView.setSubmitButtonEnabled(true);
//        searchView.setImeOptions( EditorInfo.IME_ACTION_GO);
//        searchView.setOnQueryTextListener(new QueryTextListener());// Do not iconify the widget; expand it by default
//        Log.d( TAG, "onCreateOptionsMenu: Search Activity" );
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==R.id.search_menu1);
//        return super.onOptionsItemSelected( item );
//
//    }
//
//    private void showResult(String word) {
////        word=mEditWordView.getText().toString();
//        mTextView.setText( "Result\n" );
//        Log.d( TAG, "showResult: Search Activity" );
//        if(word==null)
//            mTextView.append( "search: No word entered" +'\n' );
//        else
//            mTextView.append( "search: "+word +'\n' );
//        Cursor cursor = search(word);
//
//        if (cursor != null && cursor.getCount() > 0) {
//            // You must move the cursor to the first item.
//            cursor.moveToFirst();
//            int index=0;
//            String result;
//            // Iterate over the cursor, while there are entries.
//            do {
////                index = cursor.getColumnIndex( Quran.DbHelper.verse_no);
//                // Get the value from the column for the current cursor.
//                result = cursor.getString(index);
//                // Add result to what's already in the text view.
//                mTextView.append(result + "\n");
//            } while (cursor.moveToNext()); // Returns true or false
//            cursor.close();
//        } // You should add some handling of null case. Right now, nothing happens.
//
//    }
//
//    private Cursor search(String word) {
//
//            String[] columns = new String[]{"verse"};
//            String searchString = "%" + word + "%";
//            String where = "verse" + " LIKE ?",s;
//            String[]whereArgs = new String[]{searchString};
//            db = mdbhelper.getReadableDatabase();
//
//            Cursor cursor = null;
//
//            try {
//                cursor = db.query("quran_eng", columns, where, whereArgs, null, null, null);
//                s=cursor.getString( 0 );
//
//            } catch (Exception e) {
//                Log.d(TAG, "SEARCH EXCEPTION! " + e);
//            }
//
//             if(!(cursor != null && cursor.getCount() > 0)){
//
//                Log.d( TAG, "search: Entered tamil" );
//                try {
//                    cursor = db.query("quran_tam", columns, where, whereArgs, null, null, null);
//                } catch (Exception e) {
//                    Log.d(TAG, "SEARCH EXCEPTION! " + e);
//                }
//            }
//            if(!(cursor != null && cursor.getCount() > 0)){
//                     try {
//                        cursor = db.query("quran_ara", columns, where, whereArgs, null, null, null);
//                    } catch (Exception e) {
//                        Log.d(TAG, "SEARCH EXCEPTION! " + e);
//                    }
//                }
//
//                return cursor;
//            }
//
//    public class QueryTextListener implements SearchView.OnQueryTextListener {
//
//        boolean tc=false;
//        @Override
//        public boolean onQueryTextSubmit(String query) {
//            showResult( query );
//            tc=false;
//            return false;
//        }
//
//        @Override
//        public boolean onQueryTextChange(String newText) {
//            if(newText.length()>3) {
//                showResult( newText );
//                tc = true;
//            }
//            if( tc && newText.length()<4)
//                mTextView.setText( "Result\nSearch:\n" );
//
//            return false;
//        }
//
//    }
//
//}