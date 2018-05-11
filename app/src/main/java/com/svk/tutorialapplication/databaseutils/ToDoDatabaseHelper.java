package com.svk.tutorialapplication.databaseutils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.svk.tutorialapplication.databaseutils.models.TodoModel;

/**
 * Created by SvK on 05-05-2018.
 */
public class ToDoDatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "ToDoDatabaseHelper";
    // Database Info
    private static final String DATABASE_NAME = "todoDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TODOS = "todos";

    // Post Table Columns
    private static final String KEY_TODO_ID = "id";
    private static final String KEY_TODO_TITLE = "title";
    private static final String KEY_TODO_DESCRIPTION = "description";
    private static final String KEY_TODO_NOTIFYTIME = "notifytime";

    //Singletone instance
    private static ToDoDatabaseHelper sInstance;

    /**
     *
     * @param context
     *  Use the application context, which will ensure that you
     *  don't accidentally leak an Activity's context.
     * @return
     * Singletone instance of ToDoDatabaseHelper
     */
    public static synchronized ToDoDatabaseHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new ToDoDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private ToDoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOS_TABLE = "CREATE TABLE " + TABLE_TODOS +
                "(" +
                KEY_TODO_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TODO_TITLE + " TEXT," +
                KEY_TODO_DESCRIPTION + " TEXT," +
                KEY_TODO_NOTIFYTIME + " INTEGER" + //supports long value
                ")";

        db.execSQL(CREATE_TODOS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
            onCreate(db);
        }
    }


    /**
     *
     *cRUD begin...
     */

    // Insert a post into the database
    public int addJob(TodoModel todoItem) {
        long rowid = 0;
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_TITLE, todoItem.getTitle());
            values.put(KEY_TODO_DESCRIPTION, todoItem.getDescription());
            values.put(KEY_TODO_NOTIFYTIME, todoItem.getNotifyTime());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            rowid = db.insertOrThrow(TABLE_TODOS, null, values);
            db.setTransactionSuccessful();
            Log.d(TAG, "addJob: id "+ rowid);
            return (int)rowid;
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
            return 0;
        } finally {
            db.endTransaction();
        }
    }
}