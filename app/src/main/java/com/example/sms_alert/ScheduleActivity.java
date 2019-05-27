package com.example.sms_alert;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sms_alert.Database.DB_ScheduleAdapter;
import com.example.sms_alert.Model.ScheduleModel;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleActivity extends AppCompatActivity {

    Dialog dialog;
    Dialog dialogStart;

    DB_ScheduleAdapter db;
    ScheduleAdapter adapter;
    ArrayList<ScheduleModel> list = new ArrayList<ScheduleModel>();
    ListView listView;
    FloatingActionButton fab_add;
    private EditText add_tile;
    private TextView date;
    private String title_id = "";
    private String temp_message = "";
    private String date_time = "";
    private int temp_hour;
    private int temp_minute;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mil_Year;
    private int mil_Month;
    private int mil_Day;
    private int mil_Hour;
    private int mil_Miunte;
    private TextView txt_time;
    Boolean time_click = false;
    CheckBox checkBox;
    Boolean isChecked = false;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiyt_schedule);

        title_id = getIntent().getStringExtra("title_id");

        sharedPreferences = getSharedPreferences("ScheduleActivity", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isChecked = sharedPreferences.getBoolean("Alarm", false);


        listView = (ListView) findViewById(R.id.listView);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        db = new DB_ScheduleAdapter(ScheduleActivity.this);
        list = db.getAll(title_id.trim());



        adapter = new ScheduleAdapter(ScheduleActivity.this, list);
        listView.setAdapter(adapter);


        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addDialog();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int code = Integer.parseInt(list.get(position).getId());
                dialog_start_stop(code);
            }
        });

        if (isChecked)
        {
            checkBox.setChecked(true);
        }
        else
        {
            checkBox.setChecked(false);
        }


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                list = db.getAll(title_id);

                if (isChecked)
                {

                    if (list != null && list.size() != 0)
                    {
                        for (int i = 0; i < list.size(); i++)
                        {

                            int year = Integer.parseInt(list.get(i).getmYear());
                            int month = Integer.parseInt(list.get(i).getmMonth());
                            int day = Integer.parseInt(list.get(i).getmDay());
                            int hour = Integer.parseInt(list.get(i).getmHour());
                            int minute = Integer.parseInt(list.get(i).getmMinute());

                            Calendar c = Calendar.getInstance();
                            c.set(year, month, day, hour, minute);

                            int code = Integer.parseInt(list.get(i).getId());
                            myAlarm.SetAlarm(ScheduleActivity.this, c.getTimeInMillis(), code);

                        }

                    }

                    editor.putBoolean("Alarm", true);
                    editor.commit();


                }
                else
                {

                    if (list != null && list.size() != 0)
                    {
                        for (int i = 0; i < list.size(); i++)
                        {
                            int code = Integer.parseInt(list.get(i).getId());
                            myAlarm.Stop(ScheduleActivity.this, code );
                        }

                    }

                    editor.putBoolean("Alarm", false);
                    editor.commit();
                }

            }
        });






    }

    private void addDialog() {

        dialog = new Dialog(ScheduleActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_schedule);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circleshape));

        Button bt_add = (Button) dialog.findViewById(R.id.bt_add);
        add_tile = (EditText) dialog.findViewById(R.id.add_tile);
        date = (TextView) dialog.findViewById(R.id.date);
        txt_time = (TextView) dialog.findViewById(R.id.txt_time);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              
                    final Calendar calendar = Calendar.getInstance();
                    mYear = calendar.get(Calendar.YEAR);
                    mMonth = calendar.get(Calendar.MONTH);
                    mDay = calendar.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                    mil_Year = year;
                                    mil_Month = month;
                                    mil_Day = dayOfMonth;

                                    date.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

                                    Calendar c = Calendar.getInstance();
                                    c.set(year, month, dayOfMonth);
                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                    String strDate = format.format(c.getTime());

                                    date_time = strDate;

                                    Log.d("simpleDateFormat" , strDate);

                                    time_click = true;

                                }
                            }, mYear, mMonth, mDay);

                    datePickerDialog.show();
                }
                
             

        });


        txt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (time_click) 
            {
                time_click = false;
                final Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(ScheduleActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                mil_Hour = hourOfDay;
                                mil_Miunte = minute;

                                Time t = new Time(hourOfDay, minute, 0);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
                                String s = simpleDateFormat.format(t);

                                date_time  = date_time +" : " + s;

                                txt_time.setText(s);

                            }
                        },

                        mHour, mMinute, false);
                timePickerDialog.show();
            }
            else 
            {
                Toast.makeText(ScheduleActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
            }
                
              


            }
        });



        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScheduleModel obj = new ScheduleModel();
                obj.setTitle_id(title_id);
                obj.setSubject(add_tile.getText().toString());
                obj.setmYear(String.valueOf(mil_Year));
                obj.setmMonth(String.valueOf(mil_Month));
                obj.setmDay(String.valueOf(mil_Day));
                obj.setmHour(String.valueOf(mil_Hour));
                obj.setmMinute(String.valueOf(mil_Miunte));
                obj.setDate(String.valueOf(date_time));

                if (db.insert(obj))
                {
                    Toast.makeText(ScheduleActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
                }
                else 
                {
                    Toast.makeText(ScheduleActivity.this, "Not Inserted", Toast.LENGTH_SHORT).show();
                }

                list = db.getAll(title_id);
                adapter.notifyDataSetChanged();
                adapter = new ScheduleAdapter(ScheduleActivity.this, list);
                listView.setAdapter(adapter);
                dialog.dismiss();

            }
        });

        if (dialog != null) {
            dialog.show();
        }


    }


    public class ScheduleAdapter extends BaseAdapter {

        Context context;
        ArrayList<ScheduleModel> list = new ArrayList<ScheduleModel>();
        ViewHolder v;


        public ScheduleAdapter(Context context, ArrayList<ScheduleModel> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            v = new ViewHolder();

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.child_schedule, null);
                convertView.setTag(v);
            } else {
                v = (ViewHolder) convertView.getTag();
            }

            v.ed_subject = (TextView) convertView.findViewById(R.id.ed_subject);
            v.ed_Date = (TextView) convertView.findViewById(R.id.ed_Date);
            v.delete = (Button) convertView.findViewById(R.id.delete);


            v.ed_subject.setText(list.get(position).getSubject());
            v.ed_Date.setText(list.get(position).getDate());

            v.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    db.delete(list.get(position).getId());
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });


            return convertView;
        }

        class ViewHolder {

            TextView ed_Date, ed_subject;
            Button delete;


        }
    }


    private void dialog_start_stop(final int req_code) {

        dialogStart = new Dialog(ScheduleActivity.this);
        dialogStart.setCancelable(true);
        dialogStart.setCanceledOnTouchOutside(true);
        dialogStart.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogStart.setContentView(R.layout.dialog_start_stop);
        dialogStart.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogStart.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circleshape));

        TextView start_alarm = (TextView) dialogStart.findViewById(R.id.start_alarm);
        TextView stop = (TextView) dialogStart.findViewById(R.id.stop);


        start_alarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                int code = req_code;

                Calendar c = Calendar.getInstance();
                c.set(mil_Year, mil_Month, mil_Day, temp_hour, temp_minute);

                myAlarm.SetAlarm(ScheduleActivity.this, c.getTimeInMillis(), code);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        if (dialog != null) {
            dialog.show();
        }


    }

}
