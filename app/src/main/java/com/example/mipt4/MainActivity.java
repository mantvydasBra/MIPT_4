package com.example.mipt4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    private ListView lvNotes;
    FloatingActionButton btnAdd;

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

        btnAdd = findViewById(R.id.btnAddNote);
        btnAdd.setOnClickListener(view -> {
            Intent newNoteIntent = new Intent(MainActivity.this, AddNoteActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(newNoteIntent);
            Log.d("TESTINGTESTING", "onCLickListener: DONE!");
        });
    }

    private void setOnClickListener() {
        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Note selectedNote = (Note) lvNotes.getItemAtPosition(position);
                Intent editNoteIntent = new Intent(getApplicationContext(), AddNoteActivity.class);
                editNoteIntent.putExtra(Note.NOTE_EDIT_EXTRA, selectedNote.getId());
                startActivity(editNoteIntent);
            }
        });
    }

    private void loadFromDB() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
//        sqLiteManager.dropDBForTesting();
        sqLiteManager.loadFromDB();
        Log.d("TESTINGTESTING", "LOADFROMDB: DONE!");
    }


    private void initWidgets() {
        lvNotes = findViewById(R.id.lvNotes);
    }

    private void setNoteAdapter() {
        Log.d("TESTINGTESTING", "NoteAdapter called");
        NoteAdapter noteAdapter = new NoteAdapter(MainActivity.this, Note.nonDeletedNotes());
        lvNotes.setAdapter(noteAdapter);
    }
}