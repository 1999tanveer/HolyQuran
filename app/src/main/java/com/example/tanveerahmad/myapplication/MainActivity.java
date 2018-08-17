package com.example.tanveerahmad.myapplication;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ClipData;
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

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private static final String TAG ="";
    private GreenAdapter mAdapter;
    private RecyclerView mNumbersList;
    public boolean isClicked=false,select=true;
    Quran.DbHelper mdbHelper;
    SQLiteDatabase db;
    int NO_OF_VERSES,n=0,p[]=new int[25];//  id_a[]=new int[25];
    TextView text1,text2;
    String lan,suraName;
    String[] l;
    int verse,as,ts,lr_sura,pos=0;
    boolean lr_sura_selected=false;
    public  int id=0;
    private int lr_verse;
    ArrayList<View> views;
    private boolean call_from_goto=false;
    private boolean search_clicked=false;
    private RecyclerView searchNumberList;
    private GreenAdapter searchAdapter;
    private Cursor cursor;
    private boolean searchtomain=false;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences S = getSharedPreferences( "pref_verse", Context.MODE_PRIVATE );
        Log.d( TAG, "onDestroy: lr_verse="+mAdapter.getpos() );
        SharedPreferences.Editor editor = S.edit();
        editor.putInt( "pref_verse_no",mAdapter.getpos());
        editor.apply();

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences S = getSharedPreferences( "pref_verse", Context.MODE_PRIVATE );
        Log.d( TAG, "onDestroy: lr_verse="+mAdapter.getpos() );
        SharedPreferences.Editor editor = S.edit();
        editor.putInt( "pref_verse_no",mAdapter.getpos());
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for(int k=0;k<25;k++)
            p[k]=-1;
        views = new ArrayList<>();
        setContentView(R.layout.activity_main);
        id=(int)(getIntent().getIntExtra("id",0));
        verse=(int)(getIntent().getIntExtra("verse",0));
        if(verse!=0)
            call_from_goto=true;

        mNumbersList = (RecyclerView) findViewById(R.id.rv_verses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNumbersList.setLayoutManager(layoutManager);
        mNumbersList.setHasFixedSize(true);
        mAdapter = new GreenAdapter(id ,getBaseContext());
        mNumbersList.setAdapter(mAdapter);

        Log.d(TAG, "onCreate: id is"+id);

        SharedPreferences S= getSharedPreferences( "pref_sura",Context.MODE_PRIVATE );
        lr_sura = S.getInt( "pref_sura_no",0 );
        if(id==lr_sura) {
            lr_sura_selected = true;
            SharedPreferences S1 = getSharedPreferences( "pref_verse", Context.MODE_PRIVATE );
            mAdapter.getpos();

            lr_verse = S1.getInt( "pref_verse_no", 0 );
            Log.d( TAG, "onCreate: pos is "+pos+" lr_verse= "+lr_verse );
        }


//        SharedPreferences S= getSharedPreferences( "pref_sura",Context.MODE_PRIVATE );
        else {
            SharedPreferences.Editor editor = S.edit();

            editor.putInt( "pref_sura_no", id );
            editor.apply();
        }

        SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences( getBaseContext() );
        lan=SP.getString( "pref_lang","arab,tam" );
        as=Integer.parseInt(  SP.getString( "pref_arab_size","26" ) );
        ts=Integer.parseInt(  SP.getString( "pref_trans_size","16" ) );
//        SharedPreferences S= getSharedPreferences( "pref_last_read_sura",MODE_PRIVATE );
        lr_sura= S.getInt( "pref_sura_no",1  );
//        lr_sura= SP.getString(pref_lr_sura,"1");
        Log.d( TAG, "onCreate: lr_sura="+lr_sura );


        final Context context=getBaseContext();
        mdbHelper =new Quran.DbHelper(context);
        db= mdbHelper.getReadableDatabase();



        Cursor cursor_title = db.query(
                "suralist",   // The table to query
                null,             // The array of columns to return (pass null to get all)
                "surano =?",              // The columns for the WHERE clause
                new String[] {id + "" },          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                "surano asc"               // The sort order
        );
        cursor_title.moveToNext();
        suraName=cursor_title.getString(1);
        NO_OF_VERSES = Integer.parseInt(  cursor_title.getString( 3 ) );
        suraName=suraName.toUpperCase();

        getSupportActionBar().setTitle("  " + suraName);


//        Toast.makeText(getBaseContext(), "Entered Oncreate  "+id   ,
//                Toast.LENGTH_LONG).show();

//        TextView text1 =(TextView) findViewById(R.id.tv_verse_1);
//        text1.setTextSize( as );
//        TextView text2 =(TextView) findViewById(R.id.tv_verse_2);
//        text2.setTextSize( ts );


        l=lan.split( "," );

        mNumbersList.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), mNumbersList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // ...
//                Toast.makeText(context, "Item Clicked Pos No:"+position,
//                        Toast.LENGTH_SHORT).show();
                if(isClicked){
                    onItemLongClick( view ,position );
                }
            }
            View v;

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
                if(!searchtomain){
                    for(int i=0;i<n;i++)
                        if(p[i]==position) {
                            select = false;
                              view.setBackgroundResource( R.color.colorActivityBack );    //DESELECTING
    //                        v=view.getRootView();
    //                        v.setSelected( false );
    //                        id_a=delete( id_a,i );
                            views.remove( i );
                            p[i]=-1;
                            if(i!=n-1) {
                                p[i] = p[i+1];
                                p[i+1] = -1;
                            }
                            mAdapter.returnViewHolder(view).addViews( p );
                            mAdapter.mv=p;
    //                        mAdapter.mviews=p;

    //                        mAdapter.onBindViewHolder( mAdapter.returnViewHolder(view),position,false );
                            n--;
                            if(n==0)
                                isClicked = false;

                        };
                    if(select) {
                        view.setBackgroundResource( R.color.colorSelect );          //SELECTING

                        //                    v=view.getRootView();
                        //                    v.setSelected( true );
                        //                    id_a[n]=view.getId();
                        views.add(view);
                        Log.d( TAG, "onItemLongClick: position is" + position );
                        p[n] = position;
                        mAdapter.returnViewHolder(view).addViews( p );
                        mAdapter.mv=p;
    //                    mAdapter.mviews=p;
    //                    mAdapter.onBindViewHolder( mAdapter.returnViewHolder(view),position,true );

                        n++;
                        isClicked=true;
                    }

                    select=true;

                    Toast.makeText(getBaseContext(), n+" Verses selected"   ,
                            Toast.LENGTH_SHORT).show();
                    invalidateOptionsMenu();
                }
            }
        }
        ));

        if(lr_sura_selected)
            mNumbersList.scrollToPosition( lr_verse-1 );
        if(call_from_goto) {
            if (verse > NO_OF_VERSES) {
                Toast.makeText( getBaseContext(), "Verse " + verse + " doesnt exist in Sura " + id + "-" + suraName, Toast.LENGTH_SHORT ).show();
            }
            mNumbersList.scrollToPosition( verse - 1 );
        }


    }
    private int[] delete(int[] p, int i) {
        int[] a=new int[25];
        for (int j=0;j<i;j++)
            a[j]=p[j];
        for (int j=i;j<n;j++)
            a[j]=p[j+1];
        return a;
    }

    protected void onResume() {
        super.onResume();
        SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences( getBaseContext() );
        String lan=SP.getString( "pref_lang","arab,tam");
        int as=Integer.parseInt( SP.getString( "pref_arab_size","26" ) );
        int ts=Integer.parseInt( SP.getString( "pref_trans_size","16" ) );
        if( lan!=this.lan || !lan.equals( this.lan ) || as!=this.as || ts!=this.ts  )
            recreate();
        Log.d( TAG, "onResume: Main Activity"+lan+"  "+this.lan );
//        Toast.makeText(getBaseContext(), lan,
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        final MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu1,menu);
        if (isClicked)
        {
            menu.add(1, 0, Menu.NONE, "Copy").setIcon(R.drawable.ic_content_copy_white_24dp)
                    .setShowAsAction( MenuItem.SHOW_AS_ACTION_ALWAYS);

//            menu.add(1, 1, Menu.NONE, "Bookmark").setIcon(R.drawable.ic_grade_white_24dp)
//                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            menu.add(1,2 , Menu.NONE, "Share").setIcon(R.drawable.ic_share_white_24dp)
                    .setShowAsAction( MenuItem.SHOW_AS_ACTION_ALWAYS);

            menu.removeItem( R.id.Goto );
            menu.removeItem( R.id.search_menu1 );


        }
        else {
            menu.removeGroup( 1 );
            SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE);
            final SearchView searchView = (SearchView) menu.findItem(R.id.search_menu1).getActionView();
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
            searchView.requestFocus();
            searchView.setSubmitButtonEnabled(true);
            searchView.setImeOptions( EditorInfo.IME_ACTION_GO);;
            searchView.setOnQueryTextListener(new MainActivity.QueryTextListener());// Do not iconify the widget; expand it b

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

            MenuItem menuItem = menu.findItem(R.id.search_menu1);
            menuItem.setOnActionExpandListener( new MenuItem.OnActionExpandListener()
            {

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item)
                {
                    // Do something when collapsed
                    Log.i(TAG, "onMenuItemActionCollapse " + item.getItemId());
                    search_clicked=false;
                    Goto.setVisible( true );
                    setting.setVisible( true );


                    mAdapter = new GreenAdapter(id ,getBaseContext());
                    mNumbersList.setAdapter(mAdapter);
                    mNumbersList.scrollToPosition( lr_verse-1 );

//                    invalidateOptionsMenu();
//                    create();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);

//                    invalidateOptionsMenu();

                    return true; // Return true to collapse action view
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item)
                {
                    search_clicked=true;
                    searchtomain=true;
                    //writing lr_verse
                    SharedPreferences S = getSharedPreferences( "pref_verse", Context.MODE_PRIVATE );
                    Log.d( TAG, "onDestroy: lr_verse="+mAdapter.getpos() );
                    SharedPreferences.Editor editor = S.edit();
                    editor.putInt( "pref_verse_no",mAdapter.getpos());
                    editor.apply();

                    Log.i(TAG, "onMenuItemActionExpand " + item.getItemId());
                    searchNumberList = (RecyclerView) findViewById( R.id.rv_verses );
                    LinearLayoutManager layoutManager = new LinearLayoutManager( getBaseContext() );
                    searchNumberList.setLayoutManager( layoutManager );
                    searchNumberList.setHasFixedSize( true );
                    searchNumberList.addOnItemTouchListener( new RecyclerItemClickListener(getBaseContext(), searchNumberList, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if(cursor!=null&&cursor.getCount()>0&&searchtomain){
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
                                int i=cursor1.getInt( 1 );
                                Log.d( TAG, "onItemClick: "+i );

                                mAdapter = new GreenAdapter(id ,getBaseContext());
                                mNumbersList.setAdapter(mAdapter);
                                mNumbersList.scrollToPosition( i-1 );
                                searchtomain=false;
                                search_clicked=false;
                                menu.clear();
//                                inflater.inflate(R.menu.menu1,menu);
                                onCreateOptionsMenu( menu );

//                                search_clicked=false;
//                                invalidateOptionsMenu();

                            }
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    }));

                        //                    searchAdapter=new GreenAdapter( 0);
//                    mNumbersList.setAdapter(searchAdapter);
                    invalidateOptionsMenu();
                    return true;
                }
            });
        }


//        if(search_clicked){
//            Goto.setVisible( false );
//            setting.setVisible( false );
//            search_clicked=false;
//            searchView.requestFocus();
//            Log.d( TAG, "onCreateOptionsMenu: Entered if" );
//        }

        MenuItem menuItem = menu.findItem(R.id.search_menu1);

        return true;
    }

    private class QueryTextListener implements SearchView.OnQueryTextListener {
        boolean tc=false;
        @Override
        public boolean onQueryTextSubmit(String query) {
            showResult( query );
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(newText.length()>0) {
                showResult( newText );
                tc = true;
            }

//            if( newText.length()<3){
//                tc=false;
////                create();
//                searchAdapter=new GreenAdapter( 0);
//                mNumbersList.setAdapter(searchAdapter);
//            }
            if( newText.length()<1){
                tc=false;
//                create();
                mAdapter = new GreenAdapter(id ,getBaseContext());
                mNumbersList.setAdapter(mAdapter);
                mNumbersList.scrollToPosition( lr_verse-1 );
            }
            return false;
        }
    }

    private void showResult(String word) {
//
        try {

//
            cursor = search( word );

            if (cursor != null && cursor.getCount() > 0) {
//                 You must move the cursor to the first item.
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
                Log.d( TAG, "search: No of items="+cursor.getCount() );
            searchAdapter=new GreenAdapter( getBaseContext(),0,cursor,true ,word);
            searchNumberList.setAdapter( searchAdapter );
            } // You should add some handling of null case. Right now, nothing happens.
            else{
                searchAdapter=new GreenAdapter( 0);
                mNumbersList.setAdapter(searchAdapter);
            }
        }
        catch (Exception e){
            Log.d( TAG, "showResult: Excetion e occured" );
            searchAdapter=new GreenAdapter( 0 );
            searchNumberList.setAdapter( searchAdapter );
            e.printStackTrace();
            invalidateOptionsMenu();
        }
    }

    private Cursor search(String word) {

        String[] columns = null;
        String searchString = "%" + word + "%";
        String where = "verse" + " LIKE ?";
        String[]whereArgs = new String[]{searchString};
        db = mdbHelper.getReadableDatabase();

        Cursor cursor1 = null;

        Log.d( TAG, "search: Entered english" );
        try {
//            cursor = db.query("quran_eng", columns, where, whereArgs, null, null, null);
            cursor1  = db.query("quran_eng, suraversemap", columns, "(suraversemap.surano = ?) AND (quran_eng.verse LIKE ?) AND suraversemap.verseno =quran_eng.verseno", new String[]{id+"", searchString
            }, null, null, null);
//            cursor=db.rawQuery(selectQuery,null);
            Log.d( TAG, "search: No of items="+cursor1.getCount() );

        } catch (Exception e) {
            Log.d(TAG, "SEARCH EXCEPTION ENGLISH! " + e);
        }

        if(!(cursor1 != null && cursor1.getCount() > 0)){

            Log.d( TAG, "search: Entered tamil" );
            try {
                cursor1 = db.query("quran_tam", columns, where, whereArgs, null, null, null);
            } catch (Exception e) {
                Log.d(TAG, "SEARCH EXCEPTION TAMIL! " + e);
            }
        }

        if(!(cursor1 != null && cursor1.getCount() > 0)){
            Log.d( TAG, "search: Entered arabic" );
            try {
                cursor1 = db.query("quran_ara", columns, where, whereArgs, null, null, null);
            } catch (Exception e) {
                Log.d(TAG, "SEARCH EXCEPTION ARABIC! " + e);
            }
        }

        return cursor1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch((int)item.getItemId()) {
        case 0:                                     //COPYING...
            //add the function to perform here
            String text=getSelectedVerses();
            android.content.ClipboardManager clipboardMgr = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied text", text);
            clipboardMgr.setPrimaryClip(clip);

            Toast.makeText(getBaseContext(),"Verses copied...",
                        Toast.LENGTH_SHORT).show();
            p=new int[25];n=0;isClicked=false;
            for (int k=0;k<25;k++)
                p[k]=-1;
            mAdapter.mv=p;
            invalidateOptionsMenu();

            if ( views!= null) {
                for (int i = 0; i < views.size(); i++) {
                    View view = views.get(i);
                    view.setBackgroundResource(R.color.colorActivityBack);
                }
            }

            return(true);

        case 2:
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, suraName);
            text=getSelectedVerses();
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            p=new int[25];n=0;isClicked=false;invalidateOptionsMenu();
            if ( views!= null) {
                for (int i = 0; i < views.size(); i++) {
                    View view = views.get(i);
                    view.setBackgroundResource(R.color.colorActivityBack);
                }
            }
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            return true;

        case R.id.settings:
            //add the function to perform here
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
            return(true);
        case R.id.Goto:
            final Dialog dialog = new Dialog(this);

            dialog.setContentView(R.layout.goto_popup);
            dialog.setTitle("Custom Alert Dialog");
            final EditText gosura = (EditText) dialog.findViewById(R.id.go_sura);
            gosura.setText( id+"" );
            final EditText goverse = (EditText) dialog.findViewById(R.id.go_verse);
            goverse.requestFocus();
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

                    if(sura==id && verse==0){
                        dialog.hide();
                    }
                    else if(sura==id) {
                        if (verse > NO_OF_VERSES) {
                            Toast.makeText( getBaseContext(), "Verse " + verse + " doesnt exist in Sura " + id + "-" + suraName, Toast.LENGTH_SHORT ).show();
                        }
                        mNumbersList.scrollToPosition( verse - 1 );
                    }

                    else if(!(sura==0) && !(sura>114) ){
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
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
//            Intent i1 = new Intent(getBaseContext(),MoreInfo .class);
//            startActivity(i1);
//            return true;
        default:
            Log.i( TAG, "onOptionsItemSelected: Error not entered both switch" );
//            Toast.makeText(getBaseContext(), p[0]+", "+p[1]+"copied...",
//                    Toast.LENGTH_SHORT).show();

        }

        return(super.onOptionsItemSelected(item));
    }


    private String getSelectedVerses() {
        String text="";
        String pre="";
        //Query Text-1 as Cursor for rv.
        String selectQuery =
                "SELECT quran_ara.verse, suraversemap.suraverseno " +
                        "FROM quran_ara, suraversemap " +
                        "WHERE quran_ara.verseno =suraversemap.verseno " +
                        "AND suraversemap.surano ="+id;

        //Query Text-1 as Cursor for rv.
        String selectQuery1 =
                "SELECT quran_tam.verse, suraversemap.suraverseno " +
                        "FROM quran_tam, suraversemap " +
                        "WHERE quran_tam.verseno =suraversemap.verseno " +
                        "AND suraversemap.surano ="+id;

        String selectQuery2 =
                "SELECT quran_eng.verse, suraversemap.suraverseno " +
                        "FROM quran_eng, suraversemap " +
                        "WHERE quran_eng.verseno =suraversemap.verseno " +
                        "AND suraversemap.surano ="+id;
        Cursor cursor=null,cursor1=null;
        if(l[0].equals( "arab")){
            cursor=db.rawQuery(selectQuery,null);
            Log.d( TAG, "GreenAdapter: lan 1=arab "+l[0] );}
        else if(l[0].equals("tam")) {
            pre="திருக்குர்ஆன் ";
            cursor = db.rawQuery( selectQuery1, null );
            Log.d( TAG, "GreenAdapter: lan 1=tam" );}
        else if(l[0].equals( "eng")){
            cursor=db.rawQuery(selectQuery2,null);
            pre="Holy Quran ";
            Log.d( TAG, "GreenAdapter: lan 1=eng" );}
//        else
//            cursor=null;
//            Log.d( TAG, "GreenAdapter: No lang1 l[0]="+l[0] );


        if(l[1].equals( "tam")){
            cursor1=db.rawQuery(selectQuery1,null);
            pre="திருக்குர்ஆன் ";
        }
        else if(l[1].equals( "eng")) {
            cursor1 = db.rawQuery( selectQuery2, null );
            pre = "Holy Quran ";
        }
        else
            cursor1=null;

        for (int i=0;i<n-1;i++)
            for(int j=i+1;j<n;j++)
            {
                if(p[i]>p[j])
                {
                    int k;
                    k=p[j];
                    p[j]=p[i];
                    p[i]=k;
                    Log.d( TAG, "getSelectedVerses: swap"+i+" "+j +"swapped");
                }
            }

        Boolean isCont=true;
        String  first_verse="0",last_verse="0";
        for (int i=0;i<n;i++) {
            int position = p[i];
            if(i>0&&position!=p[i-1]+1)
                isCont=false;
            if (cursor != null) {
                cursor.moveToPosition( position );
                String s = cursor.getString( 1 );
                if(i==0)
                    first_verse=s;
                if(i==n-1)
                    last_verse=s;
                text = text + s + ". ";
                s = cursor.getString( 0 );
                text = text + s + "\n";
            }
//                Log.i(TAG, "bind: Database path is"+getBaseContext().getDatabasePath("database.db"));


            // binding verses...       TEXT-2
            if (cursor1 != null) {
                cursor1.moveToPosition( position );
                String s = cursor1.getString( 0 );
                text=text + s +"\n";
            }

        }
        if(isCont && (  first_verse.equals( last_verse ) ))
            text=text+"( "+pre + id+": "+first_verse+" )";
        else if(isCont && (first_verse!=last_verse || !first_verse.equals( last_verse ) ))
            text=text+"( "+pre + id+": "+first_verse+"-"+last_verse+" )";
        Log.d( TAG, "getSelectedVerses: isCont="+isCont );
        Log.d( TAG, "getSelectedVerses: firstVerse="+first_verse );
        Log.d( TAG, "getSelectedVerses: lastVerse="+last_verse );
        cursor.close();
        if (cursor1 != null )
        cursor1.close();
        return text;
    }

}