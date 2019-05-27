package com.example.sms_alert;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class sms_sender extends AppCompatActivity {

    TextView date_set, time_set, contact_choose;
    EditText message;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int mil_Year, mil_Month, mil_Day, mil_Hour, mil_Minute;
    private Intent myIntent;
    private PendingIntent pendingIntentam;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_sender);


        ActivityCompat.requestPermissions(sms_sender.this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                101
        );


        date_set = (TextView) findViewById(R.id.date_set);
        time_set = (TextView) findViewById(R.id.time_set);
        contact_choose = (TextView) findViewById(R.id.contact_choose);
        message = (EditText) findViewById(R.id.message);

        contact_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 100);
            }
        });

        date_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePicekr = new DatePickerDialog(sms_sender.this,

                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                mil_Year = year;
                                mil_Month = (month);
                                mil_Day = dayOfMonth;



                                Calendar calendar = Calendar.getInstance();
//                                calendar.set(Calendar.YEAR, year);
//                                calendar.set(Calendar.MONTH, month);
//                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                                calendar.set(year, month, dayOfMonth);


                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                String strDate = format.format(calendar.getTime());


//                                Log.d("simpleDateFormat" , strDate);

                                date_set.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

                            }
                        }, mYear , mMonth , mDay );
                datePicekr.show();



            }
        });


        time_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(sms_sender.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @TargetApi(Build.VERSION_CODES.M)
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                Time time = new Time(hourOfDay, minute, 0);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
                                String s = simpleDateFormat.format(time);

//                                time_set.setText(hourOfDay + ":" + minute);
                                time_set.setText(s);

                               /* long l = hourOfDay * 60 * 60;
                                long l1 = minute* 60;
                                long t = l + l1;*/

                                long mil = 1000;
                                long mil_min = 1000;

                                long l = hourOfDay * mil * 60 * 60;
                                long l1 = minute* mil * 60;
                                long t = l + l1;


                                final Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.YEAR, mil_Year);
                                calendar.add(Calendar.MONTH, mil_Month);
                                calendar.add(Calendar.DAY_OF_MONTH, mil_Day);
                                calendar.add(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.add(Calendar.MINUTE, minute);


//                                Log.d("getTimeInMillis", t + "");

                                Calendar c  = Calendar.getInstance();
                                c.set(mil_Year, mil_Month,mil_Day, hourOfDay, minute);

                                SimpleDateFormat sdf = new SimpleDateFormat(" dd/MM/yyyy - h:mm a");
                                String test = sdf.format(c.getTime());

                                Log.d("getTimeInMillis" , c.getTimeInMillis() + " == test : " + test);

                                myIntent = new Intent(sms_sender.this, BroadCast.class);
                                myIntent.putExtra("kPhone",contact_choose.getText().toString());
                                myIntent.putExtra("kMessage",message.getText().toString());
                                pendingIntentam = PendingIntent.getBroadcast(sms_sender.this, 0, myIntent,0);

                                myAlarm.SetAlarm(sms_sender.this, c.getTimeInMillis(), 0);



//                                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                                manager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() , pendingIntentam);

//                                AlarmManager alarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC, 3600000, pendingIntentam);
//                                }


                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


         if (resultCode == RESULT_OK)
        {
            if (requestCode == 100)
            {
                Uri uri = data.getData();
                String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String number = cursor.getString(numberColumnIndex);
                String name  = cursor.getString(nameColumnIndex);

                contact_choose.setText(number);
            }
        }









    }
}
