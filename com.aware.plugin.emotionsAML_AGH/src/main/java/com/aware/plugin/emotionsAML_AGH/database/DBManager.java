package com.aware.plugin.emotionsAML_AGH.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.aware.plugin.emotionsAML_AGH.results.EmotionsVariables;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(ContentValues contentValue) {
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    /*public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.SUBJECT, DatabaseHelper.DESC };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }*/

    /*public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, name);
        contentValues.put(DatabaseHelper.DESC, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }*/

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    public ContentValues get() {
        Cursor c = database.rawQuery("select * from EMOTIONS_DETECTED ", new String[]{});
        if (c.moveToLast()) {
            ContentValues contentValues = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(c,contentValues);
            c.close();
            updateContentBySum(contentValues);
            return contentValues;
        }
        return new ContentValues();
    }

    public void  updateContentBySum(ContentValues contentValues) {
        contentValues.put(EmotionsVariables.anger+"_SUM",0);
        contentValues.put(EmotionsVariables.contempt+"_SUM",0);
        contentValues.put(EmotionsVariables.disgust+"_SUM",0);
        contentValues.put(EmotionsVariables.fear+"_SUM",0);
        contentValues.put(EmotionsVariables.happiness+"_SUM",0);
        contentValues.put(EmotionsVariables.neutral+"_SUM",0);
        contentValues.put(EmotionsVariables.sadness+"_SUM",0);
        contentValues.put(EmotionsVariables.surprise+"_SUM",0);

        Cursor cursor = database.rawQuery("select EMOTION, count(*) as EMOTION_SUM from EMOTIONS_DETECTED group by EMOTION", new String[]{});
        if (cursor.moveToFirst()) {
            do {
                String emotion = cursor.getString(cursor.getColumnIndex("EMOTION"));
                int count = cursor.getInt(cursor.getColumnIndex("EMOTION_SUM"));
                contentValues.remove(emotion+"_SUM");
                contentValues.put(emotion+"_SUM", count);
            } while (cursor.moveToNext());
        }
    }

}
