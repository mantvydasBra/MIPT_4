package com.example.mipt4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqlManager;
    private static final String DATABASE_NAME = "NotesDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Notes";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "Title";
    private static final String DESC_FIELD = "Desc";



    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context) {
        if (sqlManager == null)
            sqlManager = new SQLiteManager(context);

        return sqlManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql;

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(TITLE_FIELD)
                .append(" TEXT, ")
                .append(DESC_FIELD)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        switch (oldVersion) {
//            case 1:
//                sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + NEW_COLUMN + " TEXT");
//            case 2:
//                sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + NEW_COLUMN + " TEXT");
//        }

    }

    public void addNote(Note note) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, note.getId());
        contentValues.put(TITLE_FIELD, note.getTitle());
        contentValues.put(DESC_FIELD, note.getDescription());

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void deleteNoteFromDB(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + " id = " + id);
    }

    public void loadFromDB() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(1);
                    String title = result.getString(2);
                    String desc = result.getString(3);

                    Note note = new Note(id, title, desc);
                    Note.noteArrayList.add(note);
                }
            }
        }
    }

    public void dropDBForTesting() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME);

    }

    public void updateNoteInDB(Note note) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, note.getId());
        contentValues.put(TITLE_FIELD, note.getTitle());
        contentValues.put(DESC_FIELD, note.getDescription());

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =?",
                new String[]{String.valueOf(note.getId())});

    }
}
