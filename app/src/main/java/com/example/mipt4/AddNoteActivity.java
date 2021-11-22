package com.example.mipt4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class AddNoteActivity extends AppCompatActivity {

    private EditText titleText, descText;
    private Button btnSave, btnDelete;

    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_note);
        initWidgets();
        checkForEditNote();

        btnSave.setOnClickListener(view -> {
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            String title = String.valueOf(titleText.getText());
            String desc = String.valueOf(descText.getText());

            System.out.println(title);
            System.out.println(desc);

            if (selectedNote == null) {
                int id = Note.noteArrayList.size();
                Log.d("TESTINGTESTING", "noteArrayListSize: " + Note.noteArrayList.size());

                Note newNote = new Note(id, title, desc);
                Note.noteArrayList.add(newNote);
                System.out.println(Note.noteArrayList.size());
                sqLiteManager.addNote(newNote);
            }

            else {
                selectedNote.setTitle(title);
                selectedNote.setDescription(desc);
                sqLiteManager.updateNoteInDB(selectedNote);
            }
            finish();
        });

        btnDelete.setOnClickListener(view -> deleteNote());
    }

    private void deleteNote() {
        System.out.println(Note.noteArrayList.get(0).getTitle());
        int indexOfNote = Note.getNoteIndex(selectedNote.getId());
        if (indexOfNote != -1) {
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            sqLiteManager.printTableForTesting();
//            sqLiteManager.deleteNoteFromDB(selectedNote.getId());
            selectedNote.setDeleted();
            sqLiteManager.updateNoteInDB(selectedNote);
            Log.d("TESTINGTESTING", "deleted from database: " + selectedNote.getTitle());
//            Note.noteArrayList.remove(indexOfNote);
            sqLiteManager.printTableForTesting();
//            Log.d("TESTINGTESTING", "deleted from arrayList: " + selectedNote.getTitle());
        }
        finish();
    }
    private void checkForEditNote() {
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(Note.NOTE_EDIT_EXTRA, -1);
        selectedNote = Note.getNoteForID(passedNoteID);

        if (selectedNote != null) {
            titleText.setText(selectedNote.getTitle());
            descText.setText(selectedNote.getDescription());
        }
        else {
            btnDelete.setVisibility(View.INVISIBLE);
        }
    }

    private void initWidgets() {
        titleText = findViewById(R.id.etTitle);
        descText = findViewById(R.id.etMessage);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
    }
}