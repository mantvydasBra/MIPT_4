package com.example.mipt4;

import static com.example.mipt4.Information.information;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    LinearLayout mainLayout;

    private ListView lvNotes;
    FloatingActionButton btnAdd;


    // Used to update the Intent after returning from AddNoteActivity
    @Override
    protected void onResume() {
        super.onResume();
        setNoteAdapter();
        showSnackBar();
        Log.d("[ DEBUG ]", "Resumed");
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
            Log.d("[ DEBUG ]", "Edit Note Clicked!");
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
        mainLayout = findViewById(R.id.mainLayout);
        lvNotes = findViewById(R.id.lvNotes);
        btnAdd = findViewById(R.id.btnAddNote);
        Log.d("[ DEBUG ]", "Widgets initialised");
    }

    // Function to show snack bar with message
    private void showSnackBar() {
        if (!information.equals("")) {
            Log.d("[ DEBUG ]", "Information got: " + information);
            Snackbar.make(findViewById(R.id.mainLayout), information, Snackbar.LENGTH_SHORT).show();
            information = "";
        }
    }

    // Fills listView with nonDeleted Notes
    private void setNoteAdapter() {
        Log.d("[ DEBUG ]", "NoteAdapter called");
        NoteAdapter noteAdapter = new NoteAdapter(MainActivity.this, Note.nonDeletedNotes());
        lvNotes.setAdapter(noteAdapter);
    }
}