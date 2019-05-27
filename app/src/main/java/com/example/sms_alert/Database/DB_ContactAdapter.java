package com.example.sms_alert.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sms_alert.Model.ContactModel;
import com.example.sms_alert.Model.TitleModel;

import java.util.ArrayList;

public class DB_ContactAdapter {

    public static final String ID = "id";
    public static final String CONTACT_NAME = "contact_name";
    public static final String PHONE = "phone";
    public static final String TABLE_CONTACT = "contact";

    public static final String CREATE_TABLE_CONTACT =
            "create table " + TABLE_CONTACT +
                    "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CONTACT_NAME + " TEXT, "
                    + PHONE + " TEXT " + " ); ";


    private DatabaseHelper dataBaseHelper;
    private SQLiteDatabase db;

    public DB_ContactAdapter(Context context) {
        this.dataBaseHelper = new DatabaseHelper(context);
    }


    public Boolean insert(ContactModel obj) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(CONTACT_NAME, obj.getName());
        cv.put(PHONE, obj.getPhone());
        long result = db.insert(TABLE_CONTACT, null, cv);

        if (result > 0)
            return true;
        else
            return false;
    }

    public ArrayList<ContactModel> getAll() {
        db = dataBaseHelper.getReadableDatabase();

        String q = "select * from " + TABLE_CONTACT;
        Cursor c = db.rawQuery(q, null);

        ArrayList<ContactModel> list = new ArrayList<ContactModel>();

        while (c.moveToNext()) {
            ContactModel obj = new ContactModel();
            obj.setName(c.getString(c.getColumnIndex(CONTACT_NAME)));
            obj.setPhone(c.getString(c.getColumnIndex(PHONE)));
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

    public int getCount(String s, String id) {
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        String q = "select * from " + TABLE_CONTACT + " WHERE src_des_id= '" + id + "' AND stop_name = '" + s + "'";
        Cursor c = db.rawQuery(q, null);

        int count = c.getCount();
        return count;
    }

    public Integer delete(String id) {
        int i = db.delete(TABLE_CONTACT, "id=?", new String[]{String.valueOf(id)});

        return i;
    }


}
