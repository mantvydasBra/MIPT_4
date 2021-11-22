package com.example.mipt4;

import static com.example.mipt4.Information.information;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddNoteActivity extends AppCompatActivity {

    private EditText titleText, descText;
    private Button btnSave, btnDelete;

    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("[ DEBUG ]", "AddNoteActivity: initiation started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_note);
        initWidgets();
        checkForEditNote();

        btnSave.setOnClickListener(view -> {
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            String title = String.valueOf(titleText.getText());
            String desc = String.valueOf(descText.getText());

            if (checkIfEmpty(title)) {
                return;
            }

            // If it's a new note
            if (selectedNote == null) {
                int id = Note.noteArrayList.size();

                Note newNote = new Note(id, title, desc);
                Note.noteArrayList.add(newNote);
                sqLiteManager.addNote(newNote);
                information = "Note created: " + title + " - " + desc;
            }
            // Updating existing note
            else {
                selectedNote.setTitle(title);
                selectedNote.setDescription(desc);
                sqLiteManager.updateNoteInDB(selectedNote);
                information = "Note edited: " + title + " - " + desc;
            }
            finish();
        });

        btnDelete.setOnClickListener(view -> deleteNote());
    }

    // Checking for empty title, since description CAN BE empty
    private boolean checkIfEmpty(String text) {
        if (TextUtils.isEmpty(text)) {
            titleText.setError("Don't leave this blank!");
            return true;
        }
        return false;
    }

    private void deleteNote() {
        int indexOfNote = Note.getNoteIndex(selectedNote.getId());
        // Check if there is such a note
        if (indexOfNote != -1) {
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            selectedNote.setDeleted();
            sqLiteManager.updateNoteInDB(selectedNote);
            information = "Note deleted";
            Log.d("[ DEBUG ]", "deleted from database: " + selectedNote.getTitle());
        }
        finish();
    }

    private void checkForEditNote() {
        Log.d("[ DEBUG ]", "AddNoteActivity: checkForEditNote");
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(Note.NOTE_EDIT_EXTRA, -1);
        selectedNote = Note.getNoteForID(passedNoteID);

        if (selectedNote != null) {
            titleText.setText(selectedNote.getTitle());
            descText.setText(selectedNote.getDescription());
        }
        // If it's a new note, hide delete button
        else {
            btnDelete.setVisibility(View.INVISIBLE);
        }
    }

    private void initWidgets() {
        titleText = findViewById(R.id.etTitle);
        descText = findViewById(R.id.etMessage);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        Log.d("[ DEBUG ]", "AddNoteActivity: initWidgets DONE!");
    }
}