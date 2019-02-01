 package com.example.brisknotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

 public class EditorActivity extends AppCompatActivity {

     private String action;
     //Represents the edit text object; the edit control that the user's typing into
     private EditText editor;
     private String noteFilter;
     //Will contain the existing text of the selected note
     private String oldText;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editor = findViewById(R.id.editText);
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(NoteProvider.CONTENT_ITEM_TYPE);
        //If the URI is passed in it won't be null, if "insert" button pressed it will be null so
        //we know to insert a new note
        if(uri == null){
            action = Intent.ACTION_INSERT;
            setTitle(R.string.new_note);
        }
        //URI is not null, we now know what we want to edit a note
        else{
            action = Intent.ACTION_EDIT;
            noteFilter = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();
            //Retrieving the one row from the Database
            //Cursor that gives access to the one record that matched the requested PK value
            Cursor cursor = getContentResolver().query(uri, DBOpenHelper.ALL_COLUMNS, noteFilter,
                    null, null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
            editor.setText(oldText);
            //Moves the cursor to the end of the existing text
            editor.requestFocus();
        }
     }


     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds the delete icon to the action bar for existing notes
        if(action.equals(Intent.ACTION_EDIT)){
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            //When the user presses the up button, the ID is always the same
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteNote();
                break;
        }


        return true;
     }

    //Method that only deletes one note that the user has selected
     private void deleteNote() {
        getContentResolver().delete(NoteProvider.CONTENT_URI, noteFilter, null);
        Toast.makeText(this, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
     }

     private void finishEditing(){
        //trim to eliminate any leading whitespace
        String newText = editor.getText().toString().trim();

        switch (action){
            case Intent.ACTION_INSERT:
                if(newText.length() == 0){
                    setResult(RESULT_CANCELED);
                }
                else{
                    insertNote(newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if(newText.length() == 0){
                    deleteNote();
                }
                else if(oldText.equals(newText)){
                    setResult(RESULT_CANCELED);
                }
                else{
                    updateNote(newText);
                }
        }
        finish();
    }

     private void updateNote(String noteText) {
         ContentValues values = new ContentValues();
         values.put(DBOpenHelper.NOTE_TEXT, noteText);
         //noteFilter value used to only update the one selected row
         getContentResolver().update(NoteProvider.CONTENT_URI, values, noteFilter, null);
         Toast.makeText(this, getString(R.string.note_updated), Toast.LENGTH_SHORT).show();
         setResult(RESULT_OK);
     }

     private void insertNote(String noteText) {
         ContentValues values = new ContentValues();
         values.put(DBOpenHelper.NOTE_TEXT, noteText);
         getContentResolver().insert(NoteProvider.CONTENT_URI, values);
         setResult(RESULT_OK);
     }

     //Called when user touches the back button on the device
     @Override
     public void onBackPressed(){
        finishEditing();
     }

 }

