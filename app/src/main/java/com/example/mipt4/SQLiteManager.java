package com.example.mipt4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqlManager;
    private static final String DATABASE_NAME = "NotesDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Notes";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "Title";
    private static final String DESC_FIELD = "Description";
    private static final String DELETED_FIELD = "Deleted";

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creates database if it doesn't exist
    public static SQLiteManager instanceOfDatabase(Context context) {
        if (sqlManager == null)
            sqlManager = new SQLiteManager(context);

        return sqlManager;
    }

    // If first time opening app - create table
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
                .append(" TEXT, ")
                .append(DELETED_FIELD)
                .append(" INT)");
        Log.d("DbHelper", "CREATED TABLE!!!!!!!!!!!!!!!!!!!!!!!!");

        sqLiteDatabase.execSQL(sql.toString());

    }

    // dummy function to silence the error
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        switch (oldVersion) {
//            case 3:
//                sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + DELETED_FIELD + " INT");
//        }

    }

    // Basic function to add notes to database
    public void addNote(Note note) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, note.getId());
        contentValues.put(TITLE_FIELD, note.getTitle());
        contentValues.put(DESC_FIELD, note.getDescription());
        contentValues.put(DELETED_FIELD, 0);

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        Log.d("[ DEBUG ]", "SQLiteManager: Note added!");
    }

    // Great code for debugging SQL
//    @SuppressLint("Range")
//    public void printTableForTesting() {
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        Log.d("DbHelper", "getTableAsString called");
//        String tableString = String.format("Table %s:\n", TABLE_NAME);
//        Cursor allRows  = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//        if (allRows.moveToFirst() ){
//            String[] columnNames = allRows.getColumnNames();
//            do {
//                for (String name: columnNames) {
//                    tableString += String.format("%s: %s\n", name,
//                            allRows.getString(allRows.getColumnIndex(name)));
//                }
//                tableString += "\n";
//
//            } while (allRows.moveToNext());
//        }
//        System.out.println(tableString);
//    }

    // Calls on startup to populate listView with Notes from database (makes persistent notes)
    public void loadFromDB() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(1);
                    String title = result.getString(2);
                    Log.d("[ DEBUG ]", "ID: " + id + " title " + title);

                    String desc = result.getString(3);
                    int deleted = result.getInt(4);

                    Note note = new Note(id, title, desc, deleted);
                    Note.noteArrayList.add(note);
                }
            }
        }
    }

    // Function for debugging - used for deleting table records and dropping whole table
//    public void dropDBForTesting() {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_NAME);
//
//    }

    // Updates specific note in database
    public void updateNoteInDB(Note note) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, note.getId());
        contentValues.put(TITLE_FIELD, note.getTitle());
        contentValues.put(DESC_FIELD, note.getDescription());
        contentValues.put(DELETED_FIELD, note.getDeleted());

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =?",
                new String[]{String.valueOf(note.getId())});

        Log.d("[ DEBUG ]", "SQLiteManager: Note updated!");

    }

}
