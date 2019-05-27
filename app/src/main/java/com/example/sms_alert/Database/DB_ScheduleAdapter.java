package com.example.sms_alert.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sms_alert.Model.ContactModel;
import com.example.sms_alert.Model.ScheduleModel;
import com.example.sms_alert.Model.TitleModel;
import com.example.sms_alert.ScheduleActivity;

import java.util.ArrayList;

public class DB_ScheduleAdapter {



    public static final String ID = "id";
    public static final String SUBJECT_NAME = "subject_name";
    public static final String DATE = "mDate";
    public static final String YEAR = "mYear";
    public static final String MONTH = "mMonth";
    public static final String DAY = "mDay";
    public static final String HOUR = "mHour";
    public static final String MINUTE = "mMinute";
    public static final String TITLE_ID = "title_id";
    public static final String TABLE_SCHEDUEL = "schedule";

    public static final String CREATE_TABLE_SCHEDUEL =
            "create table " + TABLE_SCHEDUEL +
                    "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SUBJECT_NAME + " TEXT, "
                    + DATE + " TEXT, "
                    + YEAR + " TEXT, "
                    + MONTH + " TEXT, "
                    + DAY + " TEXT, "
                    + HOUR + " TEXT, "
                    + MINUTE + " TEXT, "
                    + TITLE_ID + " TEXT " + " ); ";


    private DatabaseHelper dataBaseHelper;
    private SQLiteDatabase db;

    public DB_ScheduleAdapter(Context context) {
        this.dataBaseHelper = new DatabaseHelper(context);
    }


    public Boolean insert(ScheduleModel obj)
    {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(SUBJECT_NAME,obj.getSubject());
        cv.put(YEAR,obj.getmYear());
        cv.put(MONTH,obj.getmMonth());
        cv.put(DAY,obj.getmDay());
        cv.put(HOUR,obj.getmHour());
        cv.put(MINUTE,obj.getmMinute());
        cv.put(TITLE_ID,obj.getTitle_id());
        cv.put(DATE,obj.getDate());
        long result = db.insert(TABLE_SCHEDUEL, null, cv);

        if (result > 0)
            return true;
        else
            return false;
    }

    public ArrayList<ScheduleModel> getAll(String id)
    {
        db = dataBaseHelper.getReadableDatabase();

        String q = "select * from " + TABLE_SCHEDUEL + " WHERE title_id= '"+ id + "'" ;
        Cursor c = db.rawQuery(q, null);

        ArrayList<ScheduleModel> list = new ArrayList<ScheduleModel>();


        while (c.moveToNext())
        {
            ScheduleModel obj = new ScheduleModel();

            obj.setId(c.getString(c.getColumnIndex(ID)));
            obj.setTitle_id(c.getString(c.getColumnIndex(TITLE_ID)));
            obj.setSubject(c.getString(c.getColumnIndex(SUBJECT_NAME)));
            obj.setmYear(c.getString(c.getColumnIndex(YEAR)));
            obj.setmMonth(c.getString(c.getColumnIndex(MONTH)));
            obj.setmDay(c.getString(c.getColumnIndex(DAY)));
            obj.setmHour(c.getString(c.getColumnIndex(HOUR)));
            obj.setmMinute(c.getString(c.getColumnIndex(MINUTE)));
            obj.setDate(c.getString(c.getColumnIndex(DATE)));

            list.add(obj);
        }

        return list;
    }


    public ScheduleModel get(String id)
    {
        db = dataBaseHelper.getReadableDatabase();

        String q = "select * from " + TABLE_SCHEDUEL + " WHERE id= '"+ id + "'" ;
        Cursor c = db.rawQuery(q, null);

        ScheduleModel obj = new ScheduleModel();

        while (c.moveToNext())
        {
            obj.setId(c.getString(c.getColumnIndex(ID)));
            obj.setTitle_id(c.getString(c.getColumnIndex(TITLE_ID)));
            obj.setSubject(c.getString(c.getColumnIndex(SUBJECT_NAME)));
            obj.setmYear(c.getString(c.getColumnIndex(YEAR)));
            obj.setmMonth(c.getString(c.getColumnIndex(MONTH)));
            obj.setmDay(c.getString(c.getColumnIndex(DAY)));
            obj.setmHour(c.getString(c.getColumnIndex(HOUR)));
            obj.setmMinute(c.getString(c.getColumnIndex(MINUTE)));
            obj.setDate(c.getString(c.getColumnIndex(DATE)));

        }

        return obj;
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
        String q = "select * from " + TABLE_SCHEDUEL + " WHERE title_id= '"+ id + "' AND stop_name = '"+ s +"'" ;
        Cursor c = db.rawQuery(q, null);

        int count = c.getCount();
        return count;
    }

    public Integer delete(String id)
    {
        int i = db.delete(TABLE_SCHEDUEL, "id=?", new String[]{String.valueOf(id)});

        return i;
    }

    public Integer delete_titleID(String id)
    {
        String q = "DELETE FROM schedule WHERE title_id ="+ "'" + id + "'";
//        int i = db.rawQuery(q, )
//        db.execSQL(q);

        return 0;
    }

}
