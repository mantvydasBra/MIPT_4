package com.example.mipt4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    private ListView lvNotes;
    FloatingActionButton btnAdd;

    // Used to update the Intent after returning from AddNoteActivity
    @Override
    protected void onResume() {
        super.onResume();
        setNoteAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        setNoteAdapter();
        loadFromDB();
        setOnClickListener();

        btnAdd.setOnClickListener(view -> {
            Intent newNoteIntent = new Intent(MainActivity.this, AddNoteActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(newNoteIntent);
            Log.d("[ DEBUG ]", "onCLickListener: DONE!");
        });
    }

    // Edit note function
    private void setOnClickListener() {
        lvNotes.setOnItemClickListener((adapterView, view, position, l) -> {
            Note selectedNote = (Note) lvNotes.getItemAtPosition(position);
            Intent editNoteIntent = new Intent(getApplicationContext(), AddNoteActivity.class);
            editNoteIntent.putExtra(Note.NOTE_EDIT_EXTRA, selectedNote.getId());
            startActivity(editNoteIntent);
        });
    }

    // Populates listView from database
    private void loadFromDB() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
//        sqLiteManager.dropDBForTesting();
        sqLiteManager.loadFromDB();
        Log.d("[ DEBUG ]", "loadFromDb: DONE!");
    }


    private void initWidgets() {
        lvNotes = findViewById(R.id.lvNotes);
        btnAdd = findViewById(R.id.btnAddNote);
    }

    // Fills listView with nonDeleted Notes
    private void setNoteAdapter() {
        Log.d("[ DEBUG ]", "NoteAdapter called");
        NoteAdapter noteAdapter = new NoteAdapter(MainActivity.this, Note.nonDeletedNotes());
        lvNotes.setAdapter(noteAdapter);
    }
}