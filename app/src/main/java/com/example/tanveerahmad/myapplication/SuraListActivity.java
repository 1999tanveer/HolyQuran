package com.example.tanveerahmad.myapplication;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by TANVEER AHMAD on 20-05-2018.
 */


public class SuraListActivity extends AppCompatActivity{
    private static final String TAG ="" ;
    private RecyclerView mNumbersList,searchNumberList;
    private int lr_sura;
    private GreenAdapter mAdapter,searchAdapter;
    private Context context;
    Boolean search_clicked=false, tc=false;
    Button sura;
    Quran.DbHelper mdbhelper;
    SQLiteDatabase db;
    TextView mTextView;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sura_list);
        mdbhelper=new Quran.DbHelper( getApplicationContext() );
        db=mdbhelper.getReadableDatabase();

//        Toast.makeText(getBaseContext(), "Entered Oncreate",
//                Toast.LENGTH_LONG).show();

        create();

    }

    private void create() {
        SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences( getBaseContext() );
        String lan=SP.getString( "pref_lang","arab,tam" );
        String app_lan=SP.getString( "pref_app_lang","en_US" );

//        Toast.makeText(getBaseContext(), lan,
//                Toast.LENGTH_LONG).show();
        SharedPreferences S= getSharedPreferences( "pref_sura",Context.MODE_PRIVATE );
        lr_sura = S.getInt( "pref_sura_no",0 );
        Log.d( TAG, "Sura list Activity-onCreate: lr_Sura=" +lr_sura);

//        Configuration configuration = getResources().getConfiguration();
//        configuration.setLocale(new Locale( app_lan ));
//        getBaseContext().getResources().updateConfiguration(configuration,
//                getBaseContext().getResources().getDisplayMetrics());
//        Log.d( TAG, "create: app_lang="+app_lan );

        mNumbersList = (RecyclerView) findViewById(R.id.rv_sura);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNumbersList.setLayoutManager(layoutManager);
        mNumbersList.setHasFixedSize(true);

        context=getBaseContext();
//        mAdapter = new GreenAdapter(114 ,context,1);
////        Log.d(TAG, "At Suralist Activity:NoOf sura ="+Quran.NO_OF_VERSES);
//        mNumbersList.setAdapter(mAdapter);

        mAdapter = new GreenAdapter(114 ,context,1,lr_sura);
        mNumbersList.setAdapter(mAdapter);
        if(lr_sura!=0)
            mNumbersList.scrollToPosition( lr_sura-1 );
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences( getBaseContext() );
        String lan=SP.getString( "pref_lang","arab,tam" );
//        Toast.makeText(getBaseContext(), lan,
//                Toast.LENGTH_LONG).show();

        SharedPreferences S= getSharedPreferences( "pref_sura",Context.MODE_PRIVATE );
        int lr_sura = S.getInt( "pref_sura_no",0 );
        Log.d( TAG, "Sura list Activity-onCreate: lr_Sura=" +lr_sura);
        if(lr_sura!=this.lr_sura){
            Log.d( TAG, "Sura list Act onResume: Entered if recreating lr_sura="+lr_sura+"this.lr_sura="+this.lr_sura );
            recreate();}
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu( menu );
        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu1).getActionView();
        searchView.requestFocus();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//            menu.add( Menu.NONE, 0, Menu.NONE, "Share" ).setIcon( R.drawable.ic_share_white_24dp )
//                    .setShowAsAction( MenuItem.SHOW_AS_ACTION_ALWAYS );


        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu1, menu );


        SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu1).getActionView();
//        SearchView searchView = (SearchView) menu.findItem(R.xml.searchable).getActionView();


        Log.d( TAG, "onCreateOptionsMenu: Entered Iconified="+searchView.isIconified() );
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        if(search_clicked) {
            searchView.requestFocus();
            Log.d( TAG, "onCreateOptionsMenu: Focus Set" );
        }
        searchView.setSubmitButtonEnabled(true);
        searchView.setImeOptions( EditorInfo.IME_ACTION_GO);
        searchView.setOnQueryTextListener(new SuraListActivity.QueryTextListener());// Do not iconify the widget; expand it b
//        if(searchView.hasFocus()&&search_clicked){
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
//        }

        searchView.setOnCloseListener( new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setContentView( R.layout.activity_sura_list );
                Log.d( TAG, "onClose: called" );
                search_clicked=false;
                return false;
            }
        } );
        final MenuItem Goto = menu.findItem(R.id.Goto);
        final MenuItem setting = menu.findItem(R.id.settings);
        if(search_clicked) {
            Goto.setVisible( false );
            setting.setVisible( false );
        }
        else {
            Goto.setVisible( true );
            setting.setVisible( true );
        }

//        if(search_clicked){
//            Goto.setVisible( false );
//            setting.setVisible( false );
//            search_clicked=false;
//            searchView.requestFocus();
//            Log.d( TAG, "onCreateOptionsMenu: Entered if" );
//        }

        MenuItem menuItem = menu.findItem(R.id.search_menu1);

        menuItem.setOnActionExpandListener( new MenuItem.OnActionExpandListener()
        {

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item)
            {
                // Do something when collapsed
                Log.i(TAG, "onMenuItemActionCollapse " + item.getItemId());
                search_clicked=false;
                setContentView( R.layout.activity_sura_list );
                create();
                Goto.setVisible( true );
                setting.setVisible( true );
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);


//                invalidateOptionsMenu();
                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item)
            {
//                setContentView( R.layout.activity_main );
//                searchNumberList = (RecyclerView) findViewById(R.id.rv_verses);
//                LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
//                searchNumberList.setLayoutManager(layoutManager);
//                searchNumberList.setHasFixedSize(true);
//                mTextView=findViewById( R.id.search_result );
                search_clicked=true;
                Log.i(TAG, "onMenuItemActionExpand " + item.getItemId());
                invalidateOptionsMenu();
                return true;
            }
        });
        return true;
    }
    private class QueryTextListener implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(String query) {
            setContentView( R.layout.activity_main );
            searchNumberList = (RecyclerView) findViewById(R.id.rv_verses);
            searchNumberList.addOnItemTouchListener( new RecyclerItemClickListener(getBaseContext(), searchNumberList, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(cursor!=null&&cursor.getCount()>0){
                        cursor.moveToPosition( position );
                        String a=cursor.getString( 0);
                        String[] s={a},column={"suraverseno"};
                        Log.d( TAG, "onItemClick: verse no="+s );
//                                Cursor cursor1 = db.query("suraversemap", column, "verseno", s, null, null, null);
                        String selectQuery =
                                "SELECT suraversemap.surano, suraversemap.suraverseno " +
                                        "FROM suraversemap " +
                                        "WHERE suraversemap.verseno ="+a;
                        Cursor cursor1=db.rawQuery( selectQuery,null );
                        cursor1.moveToNext();
//                        int i=cursor1.getInt( 1 );
//                        Log.d( TAG, "onItemClick: "+i );


//                        searchtomain=false;
                        search_clicked=false;

                        Intent i = new Intent(context, MainActivity.class);
                        i.putExtra("id",cursor1.getInt( 0 )  );
                        i.putExtra("verse",cursor1.getInt( 1 )  );
                        startActivity(i);

//                        menu.clear();
//                                inflater.inflate(R.menu.menu1,menu);

//                        onCreateOptionsMenu( menu );

//                                search_clicked=false;
//                                invalidateOptionsMenu();

                    }
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            }));
            LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
            searchNumberList.setLayoutManager(layoutManager);
            searchNumberList.setHasFixedSize(true);
            showResult( query );
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.d( TAG, "onQueryTextChange: Text Changed Entered" );
            if(newText.length()>0) {
                    setContentView( R.layout.activity_main );
                    searchNumberList = (RecyclerView) findViewById( R.id.rv_verses );
                searchNumberList.addOnItemTouchListener( new RecyclerItemClickListener(getBaseContext(), searchNumberList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(cursor!=null&&cursor.getCount()>0){
                            cursor.moveToPosition( position );
                            String a=cursor.getString( 1);
                            String[] s={a},column={"suraverseno"};
                            Log.d( TAG, "onItemClick: verse no="+s );
//                                Cursor cursor1 = db.query("suraversemap", column, "verseno", s, null, null, null);
                            String selectQuery =
                                    "SELECT suraversemap.surano, suraversemap.suraverseno " +
                                            "FROM suraversemap " +
                                            "WHERE suraversemap.verseno ="+a;
                            Cursor cursor1=db.rawQuery( selectQuery,null );
                            cursor1.moveToNext();
//                        int i=cursor1.getInt( 1 );
//                        Log.d( TAG, "onItemClick: "+i );


//                        searchtomain=false;
                            search_clicked=false;

                            Intent i = new Intent(context, MainActivity.class);
                            i.putExtra("id",cursor1.getInt( 0 )  );
                            i.putExtra("verse",cursor1.getInt( 1 )  );
                            startActivity(i);

//                        menu.clear();
//                                inflater.inflate(R.menu.menu1,menu);

//                        onCreateOptionsMenu( menu );

//                                search_clicked=false;
//                                invalidateOptionsMenu();

                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));

                LinearLayoutManager layoutManager = new LinearLayoutManager( getBaseContext() );
                    searchNumberList.setLayoutManager( layoutManager );
                    searchNumberList.setHasFixedSize( true );

                showResult( newText );
                Log.d( TAG, "onQueryTextChange: Text Changed >3" );
                tc = true;
            }

//            else if( newText.length()<2){
//                tc=false;
//                setContentView( R.layout.activity_sura_list );
//                TextView s=findViewById( R.id.rv_title_sura );
//                s.setVisibility( View.INVISIBLE );
//                Log.d( TAG, "onQueryTextChange: Text Changed <4" );
//
//            }
            if( newText.length()<1){
                setContentView( R.layout.activity_sura_list );
                create();
            }
            return false;
        }
    }

    private void showResult(String word) {
//        word=mEditWordView.getText().toString();
        try {

//            mTextView = (findViewById( R.id.search_result ));
//            mTextView.setText( "Result\n" );
//            Log.d( TAG, "showResult: Search Activity" );
//            if (word == null)
//                mTextView.append( "search: No word entered" + '\n' );
//            else
//                mTextView.append( "search: " + word + '\n' );
            cursor = search( word );

            if (cursor != null && cursor.getCount() > 0) {
                // You must move the cursor to the first item.
//                cursor.moveToFirst();
//                int index = 0;
//                String result;
//                // Iterate over the cursor, while there are entries.
//                do {
////                index = cursor.getColumnIndex( Quran.DbHelper.verse_no);
//                    // Get the value from the column for the current cursor.
//                    result = cursor.getString( index );
//                    // Add result to what's already in the text view.
//                    mTextView.append( result + "\n" );
//                } while (cursor.moveToNext()); // Returns true or false
                searchAdapter=new GreenAdapter( context,1,cursor,true, word );
                searchNumberList.setAdapter( searchAdapter );
            } // You should add some handling of null case. Right now, nothing happens.
            else{
                tc=false;
                setContentView( R.layout.activity_sura_list );
                TextView s=findViewById( R.id.rv_title_sura );
                s.setVisibility( View.INVISIBLE );
            }
        }
        catch (Exception e){
            tc=false;
            Log.d( TAG, "showResult: Excetion e occured" );
            invalidateOptionsMenu();
        }


    }

    private Cursor search(String word) {

        String[] columns = new String[]{"verse","verseno"};
        String searchString = "%" + word + "%";
        String where = "verse" + " LIKE ?",s;
        String[]whereArgs = new String[]{searchString};
        db = mdbhelper.getReadableDatabase();

        Cursor cursor = null;

        Log.d( TAG, "search: Entered english" );
        try {
            cursor = db.query("quran_eng", columns, where, whereArgs, null, null, null);
            s=cursor.getString( 0 );

        } catch (Exception e) {
            Log.d(TAG, "SEARCH EXCEPTION ENGLISH! " + e);
        }

        if(!(cursor != null && cursor.getCount() > 0)){

            Log.d( TAG, "search: Entered tamil" );
            try {
                cursor = db.query("quran_tam", columns, where, whereArgs, null, null, null);
            } catch (Exception e) {
                Log.d(TAG, "SEARCH EXCEPTION TAMIL! " + e);
            }
        }

        if(!(cursor != null && cursor.getCount() > 0)){
            Log.d( TAG, "search: Entered arabic" );
            try {
                cursor = db.query("quran_ara", columns, where, whereArgs, null, null, null);
            } catch (Exception e) {
                Log.d(TAG, "SEARCH EXCEPTION ARABIC! " + e);
            }
        }

        return cursor;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        
        switch(item.getItemId()) {
        case R.id.settings:
            //add the function to perform here
            i = new Intent(SuraListActivity.this, SettingsActivity.class);
            startActivity(i);
            return(true);

//        case R.id.search_menu1:
//            //add the function to perform here
//            setContentView(R.layout.activity_search);
//            mTextView = (findViewById(R.id.search_result));
//
////            i = new Intent(SuraListActivity.this, SearchActivity.class);
////            startActivity(i);
//            search_clicked=true;
////
//            invalidateOptionsMenu();
//            return(true);

//            Log.d( TAG, "onOptionsItemSelected: search_clicked="+search_clicked );


//            return true;

        case R.id.Goto:
            final Dialog dialog = new Dialog(this);

            dialog.setContentView(R.layout.goto_popup);
            dialog.setTitle("GoTo Dialogue");
            final EditText gosura = (EditText) dialog.findViewById(R.id.go_sura);

            final EditText goverse = (EditText) dialog.findViewById(R.id.go_verse);

            final Button btngo= (Button) dialog.findViewById(R.id.go);
            dialog.show();
            btngo.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.hide();
                    int sura=0,verse=0;
                    String s=String.valueOf( gosura.getText() );
                    if(!(s.isEmpty())) {
                        sura = Integer.parseInt( s );
//                        Toast.makeText( getBaseContext(), "Enter Sura Number", Toast.LENGTH_SHORT ).show();
                    }
                    s=String.valueOf( goverse.getText() );
                    if(!s.isEmpty())
                        verse=Integer.parseInt( s );


                    if(!(sura==0) && !(sura>114) ){
                        Intent i = new Intent(context, MainActivity.class);
                        i.putExtra("id",sura  );
                        if(!(verse==0) )
                        i.putExtra("verse",verse  );
                        startActivity(i);
                    }
                    else{
                        Toast.makeText( getBaseContext(), "Enter a valid Sura Number", Toast.LENGTH_SHORT ).show();
                        dialog.show();
                    }
                }
            } );
            return true;
//        case R.id.info:
//            Intent i1 = new Intent(context,MoreInfo .class);
//            startActivity(i1);
//            return true;
    }
        return(super.onOptionsItemSelected(item));
    }
}
