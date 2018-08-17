package com.example.tanveerahmad.myapplication;

/**
 * Created by TANVEER AHMAD on 19-05-2018.
 */


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * We couldn't come up with a good name for this class. Then, we realized
 * that this lesson is about RecyclerView.
 *
 * RecyclerView... Recycling... Saving the planet? Being green? Anyone?
 * #crickets
 *
 * Avoid unnecessary garbage collection by using RecyclerView and ViewHolders.
 *
 * If you don't like our puns, we named this Adapter GreenAdapter because its
 * contents are green.
 */
public class GreenAdapter extends RecyclerView.Adapter<GreenAdapter.NumberViewHolder> {

    // TODO (1) Create a layout resource in res/layout/ called number_list_item.xml

    // Do steps 2 - 11 within number_list_item.xml
    // TODO (2) Make the root layout a FrameLayout
    // TODO (3) Make the width match_parent and the height wrap_content
    // TODO (4) Set the padding to 16dp
    // TODO (5) Add a TextView as the only child of the FrameLayout
    // TODO (6) Give the TextView an ID "@+id/tv_item_number"
    // TODO (7) Set the height and width to wrap_content
    // TODO (8) Align the TextView to the start of the parent
    // TODO (9) Center the TextView vertically in the layout
    // TODO (10) Set the font family to monospace
    // TODO (11) Set the text size to 42sp

    private static final String TAG = GreenAdapter.class.getSimpleName();
    private String searchWord;

    private int mNumberItems;
    private int whichActivity,id;
    private Context context;
    static SQLiteDatabase db;
    Quran.DbHelper mDbHelper;
    Cursor cursor,cursor1,search;
    boolean search_clicked=false;
    String[] l;
    MainActivity ma;
    int as,ts;
    int lr_sura;
    private int pos=0;
    GreenAdapter.NumberViewHolder viewHolder;
    View view;
    public int[] mv=new int[25];
//    public int[] mviews=new int[25];



    /**
     * Constructor for GreenAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener.
     *
     *   Number of items to display in list
     */
    public GreenAdapter(int id, Context context) {              //Only Main activity calls this constructor
        this.id=id;

        for(int k=0;k<25;k++){
            mv[k]=-1;
        }
        String[] selectionArgs = {id + ""};

        cursor = db.query(                  //getting no of views=mNumberItems
                "suralist",                  // The table to query
                null,                        // The array of columns to return (pass null to get all)
                "surano =?",                 // The columns for the WHERE clause
                selectionArgs,                          // The values for the WHERE clause
                null,                               // don't group the rows
                null,                                // don't filter by row groups
                "surano desc"                      // The sort order
        );
        cursor.moveToNext();
        mNumberItems=cursor.getInt(3);
        Log.d(TAG, "GreenAdapter: No of verses in sura clicked is"+mNumberItems);
        cursor.close();

        whichActivity=0;            //Only Main activity calls this constructor
        mDbHelper=new Quran.DbHelper(context);
        db = mDbHelper.getReadableDatabase();
        Log.d(TAG, "bind: Database path is"+context.getDatabasePath("database.db"));

        SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences( context );
        String lan=SP.getString( "pref_lang","arab,tam" );
        as=Integer.parseInt(  SP.getString( "pref_arab_size","26" ) );
        ts=Integer.parseInt(  SP.getString( "pref_trans_size","16" ) );

         l=lan.split( "," );


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
        if(l[0].equals( "arab")){
            cursor=db.rawQuery(selectQuery,null);
            Log.d( TAG, "GreenAdapter: lan 1=arab "+l[0] );}
        else if(l[0].equals("tam")) {
            cursor = db.rawQuery( selectQuery1, null );
            Log.d( TAG, "GreenAdapter: lan 1=tam" );}
        else if(l[0].equals( "eng")){
            cursor=db.rawQuery(selectQuery2,null);
            Log.d( TAG, "GreenAdapter: lan 1=eng" );}
//        else
//            cursor=null;
//            Log.d( TAG, "GreenAdapter: No lang1 l[0]="+l[0] );


        if(l[1].equals( "tam"))
            cursor1=db.rawQuery(selectQuery1,null);
        else if(l[1].equals( "eng"))
            cursor1=db.rawQuery(selectQuery2,null);
        else
            cursor1=null;

    }


    public GreenAdapter(int numberOfItems,Context context, int whichActivity,int lr_sura) {
        mNumberItems = numberOfItems;
        mDbHelper=new Quran.DbHelper(context);
        db = mDbHelper.getReadableDatabase();
        this.whichActivity=whichActivity;
        this.lr_sura=lr_sura;
//        SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences( context );
//        if(whichActivity==1)
//            lr_sura= SP.getInt("pref_sura_no",1);
//        Toast.makeText(context, "lr_sura="+lr_sura,
//                Toast.LENGTH_LONG).show();
//        Log.d( TAG, "GreenAdapter: lr_sura="+lr_sura );
    }

    public GreenAdapter(int numberOfItems) {
        mNumberItems = numberOfItems;
    }

    public GreenAdapter(Context context, int whichActivity, Cursor search, boolean search_clicked, String searchWord) {

        this.search=search;
        this.searchWord=searchWord;
        this.search_clicked=search_clicked;
        mNumberItems = search.getCount();
        Log.d( TAG, "GreenAdapter: NO if items="+mNumberItems );
        mDbHelper=new Quran.DbHelper(context);
        db = mDbHelper.getReadableDatabase();
        this.whichActivity=whichActivity;

        SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences( context );
        String lan=SP.getString( "pref_lang","arab,tam" );
        as=Integer.parseInt(  SP.getString( "pref_arab_size","26" ) );
        ts=Integer.parseInt(  SP.getString( "pref_trans_size","16" ) );
//        SharedPreferences SP= PreferenceManager.getDefaultSharedPreferences( context );
//        if(whichActivity==1)
//            lr_sura= SP.getInt("pref_sura_no",1);
//        Toast.makeText(context, "lr_sura="+lr_sura,
//                Toast.LENGTH_LONG).show();
//        Log.d( TAG, "GreenAdapter: lr_sura="+lr_sura );
    }



    /**
     *
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     */


    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        context = viewGroup.getContext();
        boolean shouldAttachToParentImmediately = false;
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdForListItem;
        if(search_clicked)
            layoutIdForListItem = R.layout.search_verse;
        else if(whichActivity==0)
            layoutIdForListItem = R.layout.verse;
        else
            layoutIdForListItem = R.layout.sura;


        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        viewHolder = new GreenAdapter.NumberViewHolder(view);
        hasStableIds();

        return viewHolder;
    }

    public NumberViewHolder returnViewHolder(View view)
    {
        return new GreenAdapter.NumberViewHolder(view);
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        pos=position;

        holder.bind(position,whichActivity, search_clicked,holder);

    }

//    public void onBindViewHolder(NumberViewHolder holder,int position,Boolean call) {

//        holder.bind(position,whichActivity, search_clicked, holder);

//        holder.setSelected( holder,position );
//        holder.bind(position,whichActivity, search_clicked,holder);


//    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView listItemNumberView,listItemView1,listItemView2,listItemSuraNov,lastRead,listItemSuraNo,listItemChapNo;
        LinearLayout v;
        Button listItemSura;
        int[] mviews=new int[25];

//        View text2;
        int i=1;

        public void addViews(int[] v){
                mviews=v;
            Log.d( TAG, "addViews: view "+v+" added"+v[0]+v[1]+"\nmviews="+mviews[0]+mviews[1] );
        }

        Boolean checkSelected(int position){
            for(int k=0;k<mviews.length;k++){
                if(mviews[k]==position){
                    Log.d( TAG, "view selected" );
                    return true;
                }
            }
                return false;
        }

        void setSelected(NumberViewHolder holder, int position) {
            if (checkSelected(position)) {
                holder.itemView.setBackgroundResource( R.color.colorSelect );
            } else {
                holder.itemView.setBackgroundResource( R.color.colorActivityBack );
            }
        }

        public NumberViewHolder(View itemView) {
            super(itemView);


            for(int k=0;k<25;k++){
                mviews[k]=-1;
            }

            if(search_clicked==true){
                listItemView1 = (TextView) itemView.findViewById(R.id.search_verse);
                listItemView1.setTextSize( ts  );
                listItemSuraNo = (TextView) itemView.findViewById(R.id.search_surano);
                listItemChapNo = (TextView) itemView.findViewById(R.id.search_chapno);
                listItemNumberView = (TextView) itemView.findViewById(R.id.search_verseno);

            }
            else if(whichActivity==0) {
                listItemNumberView = (TextView) itemView.findViewById(R.id.tv_verse_no);
                listItemView1 = (TextView) itemView.findViewById(R.id.tv_verse_1);
                listItemView2 = (TextView) itemView.findViewById(R.id.tv_verse_2);
                v=itemView.findViewById( R.id.verse );

//                text2= itemView.findViewById(R.id.tv_verse_2);
                listItemView1.setTextSize( as );
                listItemView2.setTextSize( ts );

            }
            else{

                listItemNumberView = (TextView) itemView.findViewById(R.id.tv_sura_no);
                listItemSuraNov = (TextView) itemView.findViewById(R.id.tv_sura_nov);
                lastRead=(TextView) itemView.findViewById(R.id.lr);

                listItemSura = (Button) itemView.findViewById(R.id.tv_sura);
                listItemSura.setOnClickListener(this);
            }

        }

        @SuppressLint("ResourceAsColor")
        public void bind(int position, int whichActivity, boolean search_clicked, NumberViewHolder holder)
        {

            if(search_clicked&&whichActivity==1) {
                search.moveToPosition( position );
                String res=search.getString( 0 );
//                res = res.replaceAll(searchWord,"<font color='red'>"+searchWord+"</font>");
                searchWord=( Character.toLowerCase( searchWord.charAt( 0 ) ) ) +searchWord.substring( 1 );
                res = res.replaceAll(searchWord,"<font color='red'>"+searchWord+"</font>");
                searchWord=( Character.toUpperCase( searchWord.charAt( 0 ) ) )+searchWord.substring( 1 );
                res = res.replaceAll(searchWord,"<font color='red'>"+searchWord+"</font>");


                listItemView1.setText( Html.fromHtml( res ) );
                String a=search.getString( 1);
                String selectQuery =
                        "SELECT suraversemap.surano, suraversemap.suraverseno " +
                                "FROM suraversemap " +
                                "WHERE suraversemap.verseno ="+a;
                Cursor search1=db.rawQuery( selectQuery,null );
                search1.moveToNext();
                listItemSuraNo.setText( context.getString(R.string.sura).toLowerCase()+':'+search1.getString( 0 )  );
                listItemNumberView.setText( search1.getString( 1 ) );
            }
            else if(search_clicked&&whichActivity==0) {
                search.moveToPosition( position );
                String res=search.getString( 1 );
//                res = res.replaceAll(searchWord,"<font color='red'>"+searchWord+"</font>");
                searchWord=( Character.toLowerCase( searchWord.charAt( 0 ) ) ) +searchWord.substring( 1 );
                res = res.replaceAll(searchWord,"<font color='red'>"+searchWord+"</font>");
                searchWord=( Character.toUpperCase( searchWord.charAt( 0 ) ) ) +searchWord.substring( 1 );
                res = res.replaceAll(searchWord,"<font color='red'>"+searchWord+"</font>");


                listItemView1.setText( Html.fromHtml( res ) );
                String a=search.getString( 0);
                String selectQuery =
                        "SELECT suraversemap.surano, suraversemap.suraverseno " +
                                "FROM suraversemap " +
                                "WHERE suraversemap.verseno =?";
                Cursor search1=db.rawQuery( selectQuery,new String[]{a} );
                search1.moveToNext();
                listItemSuraNo.setText( context.getString(R.string.sura).toLowerCase()+':'+search1.getString( 0 )  );
                listItemNumberView.setText( search1.getString( 1 ) );
                search1.close();
            }


            else if(whichActivity==0) {              // binding verses...       TEXT-1


//                if(l[0].equals( "tam")||l[0].equals( "eng")){
//                    if(listItemView1!=null)
//                        listItemView1.setTextSize( 16 );


//                if(l[1]==null)
//                    ((ViewGroup) text2.getParent()).removeView(text2);

//               setSelected(holder,getAdapterPosition());
                Boolean col=false;
                for(int k=0;k<25;k++)
                    if(getAdapterPosition() == mv[k])
                        col=true;
                if (col) {
                    holder.itemView.setBackgroundResource( R.color.colorSelect );
                    Log.d( TAG, "bind: colorSelect="+getAdapterPosition()+"mview="+mviews[0]+mviews[1] );
                } else {
                    holder.itemView.setBackgroundResource( R.color.colorActivityBack );
                    Log.d( TAG, "bind: Adapterpos="+getAdapterPosition()+"mview="+mviews[0]+mviews[1] );
                }


                if(cursor!=null) {
                    switch (l[0] ){
                        case "arab":
                            listItemView1.setTextSize( as );
                            break;
                        case "tam":
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                listItemView1.setFontFeatureSettings( "@style/TextAppearance.AppCompat.Title" );
                            }
                        case "eng":
                            listItemView1.setTextSize( ts );
                    }
                    cursor.moveToPosition( position );
//                    if(position==0)
//                        holder.v.setBackgroundColor( R.color.colorSelect );
                    String s = cursor.getString( 1 );
                    if (Integer.parseInt( s ) > 99)
                        listItemNumberView.setText( " " + s );
                    else
                        listItemNumberView.setText( "  " + s );

                    s = cursor.getString( 0 );

//                Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/Bamini.ttf");

//                listItemView.setTypeface(custom_font);
                    Typeface custom_font;
                    if(l[0].equals( "arab")){
                        custom_font = Typeface.createFromAsset( context.getAssets(), "fonts/noorehuda.ttf" );
                        listItemView1.setTypeface( custom_font );
                    }


                    listItemView1.setText( s );
                }
                Log.i(TAG, "bind: Database path is"+context.getDatabasePath("database.db"));


                // binding verses...       TEXT-2
                if(cursor1!=null) {
                     cursor1.moveToPosition( position );
                     String s = cursor1.getString( 0 );
                    ViewGroup.LayoutParams params = listItemView2.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    listItemView2.setLayoutParams( params );
                    listItemView2.setTextSize( ts );
                    listItemView2.setText( s );
                }
                else
                    listItemView2.setHeight( 0 );


                Log.d( TAG, "bind: as= "+as+"ts= "+ts );
            }

            else {                              //binding suras...
                String[] selectionArgs = {position + 1 + ""};
                Cursor cursor = db.query(
                        "suralist",   // The table to query
                        null,             // The array of columns to return (pass null to get all)
                        "surano =?",              // The columns for the WHERE clause
                        selectionArgs,          // The values for the WHERE clause
                        null,                   // don't group the rows
                        null,                   // don't filter by row groups
                        "surano desc"               // The sort order
                );
                cursor.moveToNext();
                String s = cursor.getString(0);
                listItemNumberView.setText("  "+s);

                s = cursor.getString(1)+'\n'+cursor.getString(2);
                listItemSura.setText(s);
                s=cursor.getString(3)+" "+context.getString( R.string.verses );
                listItemSuraNov.setText(s);
                int pix=35;

                if(lr_sura ==(position+1)) {
                    lastRead.setText( context.getString(R.string.lr) );
                    if(lastRead.getHeight()==0)
                        lastRead.setHeight( pix );
                    Log.d( TAG, "lr_debug:Entered if once" );
                }
                else{
                    pix=lastRead.getHeight();
                    lastRead.setHeight( 0 );
                    Log.d( TAG, "lr_debug:lr_sura= "+lr_sura+"pos="+position );}

                cursor.close();

//                listItemNumberView.setText(listItemSura.getId()+"");
            }

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d(TAG, "onClick:The position is"+ position);
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("id", (int) position +1);
            Log.d(TAG, "onClick: id is="+ position);
            context.startActivity(i );
        }
    }

    public int getpos()
    {
        return pos;
    }

}
