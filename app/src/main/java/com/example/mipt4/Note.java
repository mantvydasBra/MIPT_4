package com.example.mipt4;

import android.util.Log;

import java.util.ArrayList;

public class Note {

    // Array list which holds all the Notes and presents to the user
    public static ArrayList<Note> noteArrayList = new ArrayList<>();
    public static String NOTE_EDIT_EXTRA = "noteEdit";

    private int id;
    private String title;
    private String description;
    private int deleted;

    // Constructor for note
    public Note(int id, String title, String description, int deleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deleted = deleted;
    }

    // Automatically set 0 for deleted on new notes
    public Note(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deleted = 0;
    }

    // Searches notes for its ID
    public static Note getNoteForID(int passedNoteID) {
        for (Note note : noteArrayList) {
            if (note.getId() == passedNoteID)
                return note;
        }
        return null;
    }

    // Gets note index in array list by note's ID
    public static int getNoteIndex(int passedNoteID) {
        Note tempNote;
        for (int i = 0; i < noteArrayList.size(); i++) {
            tempNote = noteArrayList.get(i);
            if (tempNote.getId() == passedNoteID)
                return i;
        }
        return -1;
    }

    // Print notes without delete = 1
    public static ArrayList<Note> nonDeletedNotes() {
        ArrayList<Note> nonDeleted = new ArrayList<>();
        for (Note note : noteArrayList) {
            if (note.deleted != 1) {
                nonDeleted.add(note);
            }
        }
        return nonDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted = 1;
        Log.d("[ DEBUG ]", "setDeleted CALLED");
    }
}
