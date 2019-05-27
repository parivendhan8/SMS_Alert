package com.example.sms_alert;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;

public class myAlarm  {


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void  SetAlarm(Context context, long l, int code)
    {
        String c = String.valueOf(code);
        Intent myIntent = new Intent(context, BroadCast.class);
        myIntent.putExtra("code", c);
        PendingIntent pendingIntentam = PendingIntent.getBroadcast(context, code, myIntent,0);
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, l, pendingIntentam);
    }


    public static void Stop(Context context,int code)
    {
        Intent myIntent = new Intent(context, BroadCast.class);
        PendingIntent pendingIntentam = PendingIntent.getBroadcast(context, code, myIntent,0);
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.cancel(pendingIntentam);
    }



    public static void SetNotification(Context context)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher_background);

        Intent MyIntent = new Intent(Intent.ACTION_VIEW);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , MyIntent , 0);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle("Notifications Title");
        builder.setContentText("Success");
        builder.setPriority(Notification.PRIORITY_MAX);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

/*      String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancel(1);*/




    }

    public static void  SetMessage(String phone, String message)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, message, null, null);

    }
}
