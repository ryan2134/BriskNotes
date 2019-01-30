 package com.example.brisknotes;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

 public class EditorActivity extends AppCompatActivity {

     private String action;
     //Represents the edit text object; the edit control that the user's typing into
     private EditText editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editor = (EditText) findViewById(R.id.editText);
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(NoteProvider.CONTENT_ITEM_TYPE);
        //If the URI is passed in it won't be null, if "insert" button pressed it will be null so
        //we know to insert a new note
        if(uri == null){
            action = Intent.ACTION_INSERT;
            setTitle(R.string.new_note);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()){
            //When the user presses the up button, the ID is always the same
            case android.R.id.home:
                finishEditing();
                break;
        }

        return true;
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
        }
        finish();
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

