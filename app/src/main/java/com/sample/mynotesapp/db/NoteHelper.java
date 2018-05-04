package com.sample.mynotesapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sample.mynotesapp.entity.Note;
import static com.sample.mynotesapp.db.DatabaseContract.NoteColumns.*;
import static com.sample.mynotesapp.db.DatabaseContract.TABLE_NOTE;

import java.util.ArrayList;

/**
 * Created by 0231 on 4/25/2018.
 */

public class NoteHelper {
    private static String DATABASE_TABLE = TABLE_NOTE;
    private Context context;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public NoteHelper(Context context) {
        this.context = context;
    }

    public NoteHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    public ArrayList<Note> query() {
        ArrayList<Note> arrayList = new ArrayList<Note>();
        Cursor cursor = database.query(DATABASE_TABLE, null, null, null, null, null, _ID + " DESC", null);
        cursor.moveToFirst();
        Note note;
        if (cursor.getCount()>0) {
            do {
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));

                arrayList.add(note);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Note note) {
        ContentValues initialValue = new ContentValues();
        initialValue.put(TITLE, note.getTitle());
        initialValue.put(DESCRIPTION, note.getDescription());
        initialValue.put(DATE, note.getDate());
        return database.insert(DATABASE_TABLE, null, initialValue);
    }

    public int update(Note note){
        ContentValues args = new ContentValues();
        args.put(TITLE, note.getTitle());
        args.put(DESCRIPTION, note.getDescription());
        args.put(DATE, note.getDate());
        return database.update(DATABASE_TABLE, args, _ID +"= '"+note.getId()+"'", null);
    }

    public int delete(int id) {
        return database.delete(TABLE_NOTE, _ID + " = '"+id+"'", null);
    }
}
