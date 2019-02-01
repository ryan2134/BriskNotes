package com.example.brisknotes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class NoteCursorAdapter extends CursorAdapter {
    public NoteCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /* Creates the LayoutInflater object
       Reading and inflating the layout for the note_list_item,
       and passing it back whenever the newView method is called. */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false);
    }

    /* Gets the text of the note to retrieve the data from the cursor object */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // 1: Could go and look at how to retrieve the data that all columns constant defines  the order
        // of the data as it's being returned.
        // A more effective way is to address the column by its name (DBOpenHelper.NOTE_TEXT)
        String noteText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
        // Integer to handle the notes that have line feeds
        // 10 for ASCII value of a linefeed
        int pos = noteText.indexOf(10);

        // Found a linefeed
        if(pos != -1){
            noteText = noteText.substring(0, pos) + " ...";
        }

        TextView tv = view.findViewById(R.id.tvNote);
        tv.setText(noteText);

    }
}
