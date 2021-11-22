package com.example.mipt4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
//            Intent home = new Intent(this, MainActivity.class);
//                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(home);
            finish();
//            System.out.println("HEYYY");
//            ModifyNoteActivity.this.finish();
        });

        btnDelete.setOnClickListener(view -> deleteNote());
    }

    private void deleteNote() {
        System.out.println(Note.noteArrayList.get(0).getTitle());
        int indexOfNote = Note.getNoteIndex(selectedNote.getId());
        if (indexOfNote != -1) {
            Note.noteArrayList.remove(indexOfNote);
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            sqLiteManager.deleteNoteFromDB(selectedNote.getId());
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