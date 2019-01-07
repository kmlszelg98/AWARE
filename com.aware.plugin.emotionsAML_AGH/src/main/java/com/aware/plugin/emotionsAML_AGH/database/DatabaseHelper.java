package com.aware.plugin.emotionsAML_AGH.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aware.plugin.emotionsAML_AGH.results.EmotionsVariables;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "EMOTIONS_DETECTED";

    // Table columns
    public static final String _ID = "_id";

    // Database Information
    static final String DB_NAME = "EMOTIONS.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EmotionsVariables.photo + " TEXT,"
            + EmotionsVariables.anger + " TEXT,"
            + EmotionsVariables.contempt + " TEXT,"
            + EmotionsVariables.disgust + " TEXT,"
            + EmotionsVariables.fear + " TEXT,"
            + EmotionsVariables.happiness + " TEXT,"
            + EmotionsVariables.neutral + " TEXT,"
            + EmotionsVariables.sadness + " TEXT,"
            + EmotionsVariables.surprise + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
