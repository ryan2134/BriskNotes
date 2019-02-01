package com.example.brisknotes;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NoteProvider extends ContentProvider{

    private static final String AUTHORITY = "com.example.plainolnotes.notesprovider";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    //Used to indicate that we are updating an existing code
    public static final String CONTENT_ITEM_TYPE = "Note";

    //static initializer executed first time anything is called from this class
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH +  "/#", NOTES_ID);
    }
    private SQLiteDatabase database;

    //Called method when the Note Provider is launched
    @Override
    public boolean onCreate() {

        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    /* Obtains the data from the database table notes, all notes or just a single row
       Need to specify which columns to retrieve */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        //We got a URI ending with a numeric value, we only want a single row
        if(uriMatcher.match(uri) == NOTES_ID){
            //getLastPathSegment gets the numeric value just after the '/'; PK value
            selection = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();

        }

        return database.query(DBOpenHelper.TABLE_NOTES, DBOpenHelper.ALL_COLUMNS,
                selection, null, null, null,
                DBOpenHelper.NOTE_CREATED + " DESC");
    }

    //Standard override method that needs to be implemented
    @Override
    public String getType(Uri uri) {
        return null;
    }

    /* Returns a URI
       Parse method allows for us to put together a string and return the equivalent URI */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(DBOpenHelper.TABLE_NOTES, null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    /* Returns the result of calling the Delete method from the database object
       Returns an int which represents the number of rows deleted */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBOpenHelper.TABLE_NOTES, selection, selectionArgs);
    }

    //Essentially the same as the Delete method but instead just updates the items in the database
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_NOTES, values, selection, selectionArgs);
    }
}
