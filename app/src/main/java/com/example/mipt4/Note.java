package com.example.mipt4;

import java.util.ArrayList;

public class Note {

    public static ArrayList<Note> noteArrayList = new ArrayList<>();
    public static String NOTE_EDIT_EXTRA = "noteEdit";

    private int id;
    private String title;
    private String description;

    public Note(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public static Note getNoteForID(int passedNoteID) {
        for (Note note : noteArrayList) {
            if (note.getId() == passedNoteID)
                return note;
        }
        return null;
    }

    public static int getNoteIndex(int passedNoteID) {
        Note tempNote = null;
        for (int i = 0; i < noteArrayList.size(); i++) {
            tempNote = noteArrayList.get(i);
            if (tempNote.getId() == passedNoteID)
                return i;
        }
        return -1;
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
}
