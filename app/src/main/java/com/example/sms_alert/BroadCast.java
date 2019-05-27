package com.example.sms_alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.sms_alert.Database.DB_ContactAdapter;
import com.example.sms_alert.Database.DB_ScheduleAdapter;
import com.example.sms_alert.Model.ContactModel;
import com.example.sms_alert.Model.ScheduleModel;

import java.util.ArrayList;

public class BroadCast extends BroadcastReceiver {

    DB_ContactAdapter db_contactAdapter;
    DB_ScheduleAdapter db_scheduleAdapter;

    ArrayList<ContactModel> list_contact = new ArrayList<ContactModel>();
    private String subject = "";


    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String code = intent.getStringExtra("code");

        Log.d("code100", code + "");

        db_contactAdapter = new DB_ContactAdapter(context);
        list_contact = db_contactAdapter.getAll();

        db_scheduleAdapter = new DB_ScheduleAdapter(context);

        ScheduleModel model = new ScheduleModel();
        model = db_scheduleAdapter.get(code);
        subject = model.getSubject();

//        Toast.makeText(context, ""+ code + " : " + model.getSubject() , Toast.LENGTH_SHORT).show();

        myAlarm.SetNotification(context);

        for (ContactModel c : list_contact)
        {
            String message = "Hi " + c.getName() + " " +
                             "today you have Exam on " + subject ;
            myAlarm.SetMessage(c.getPhone(), message);

        }




    }
}
