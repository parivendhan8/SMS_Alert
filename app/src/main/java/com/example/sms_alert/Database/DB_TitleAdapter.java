package com.example.sms_alert.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sms_alert.Model.TitleModel;
import com.example.sms_alert.TitleActivity;

import java.util.ArrayList;

public class DB_TitleAdapter {

    public static final String ID = "id";
    public static final String TITLE_NAME = "title_name";
    public static final String MESSAGE = "message";
    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";
    public static final String TABLE_TITLE = "title";

    public static final String CREATE_TABLE_TITLE =
            "create table " + TABLE_TITLE +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_NAME + " TEXT, "
                + HOUR + " TEXT, "
                + MINUTE + " TEXT, "
                + MESSAGE + " TEXT "  + " ); ";


    private DatabaseHelper dataBaseHelper;
    private SQLiteDatabase db;
    Context context;

    public DB_TitleAdapter(Context context) {
        this.dataBaseHelper = new DatabaseHelper(context);
        this.context = context;
    }


    public Boolean insert(TitleModel obj)
    {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TITLE_NAME,obj.getTitle_name());
        long result = db.insert(TABLE_TITLE, null, cv);

        if (result > 0)
            return true;
        else
            return false;
    }

    public ArrayList<TitleModel> getAll()
    {
        db = dataBaseHelper.getReadableDatabase();

        String q = "select * from " + TABLE_TITLE ;
        Cursor c = db.rawQuery(q, null);

        ArrayList<TitleModel> list = new ArrayList<TitleModel>();


        while (c.moveToNext())
        {
            TitleModel obj = new TitleModel();
            obj.setTitle_name(c.getString(c.getColumnIndex(TITLE_NAME)));
            obj.setId(c.getString(c.getColumnIndex(ID)));
            list.add(obj);
        }

        return list;
    }

//    public void update(Source_Destination obj, String id)
//    {
//        db = dataBaseHelper.getReadableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(STOP_NAME,obj.getSource());
//        db.update(TABLE_STOPS, cv, "id=" + id, null);
//    }

    public int getCount( String s, String id)
    {
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        String q = "select * from " + TABLE_TITLE + " WHERE src_des_id= '"+ id + "' AND stop_name = '"+ s +"'" ;
        Cursor c = db.rawQuery(q, null);

        int count = c.getCount();
        return count;
    }

    public Integer delete(String id)
    {
        int i = db.delete(TABLE_TITLE, "id=?", new String[]{String.valueOf(id)});

//        new DB_ScheduleAdapter(context).delete_titleID(id);


//        DELETE FROM schedule WHERE title_id = "4"

        return i;
    }



}
