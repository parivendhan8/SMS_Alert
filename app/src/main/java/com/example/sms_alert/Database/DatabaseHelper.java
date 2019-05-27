
package com.example.sms_alert.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    static final int DB_VERSION = 8;
    public static final String DB_NAME = "route.db";

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DB_TitleAdapter.CREATE_TABLE_TITLE);
        db.execSQL(DB_ContactAdapter.CREATE_TABLE_CONTACT);
        db.execSQL(DB_ScheduleAdapter.CREATE_TABLE_SCHEDUEL);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DB_TitleAdapter.TABLE_TITLE);
        db.execSQL("DROP TABLE IF EXISTS " + DB_ContactAdapter.TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + DB_ScheduleAdapter.TABLE_SCHEDUEL);

        onCreate(db);

    }
}
