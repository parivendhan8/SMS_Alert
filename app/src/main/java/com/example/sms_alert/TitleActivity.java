package com.example.sms_alert;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.sms_alert.Database.DB_TitleAdapter;
import com.example.sms_alert.Model.TitleModel;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TitleActivity extends AppCompatActivity  {

    TextView header;
    ListView listView;

    FloatingActionButton fab_Add;
    Dialog dialog;
    DB_TitleAdapter db;
    TitleAdapter adapter;

    private int mHour;
    private int mMinute;

    String temp_hour = "";
    String temp_minute = "";
    String temp_title = "";
    String temp_message = "";

    ArrayList<TitleModel> list = new ArrayList<TitleModel>();
    private String title_id = "";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        setupWindowAnimations();


        ActivityCompat.requestPermissions(TitleActivity.this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                101
        );



        listView = (ListView) findViewById(R.id.listView);
        header = (TextView) findViewById(R.id.contact_list);
        fab_Add = (FloatingActionButton) findViewById(R.id.fab_add);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TitleActivity.this, ContactActivity.class));
            }
        });

        db = new DB_TitleAdapter(TitleActivity.this);
        list = db.getAll();
        adapter = new TitleAdapter(TitleActivity.this, list);
        listView.setAdapter(adapter);


        fab_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addDialog();

            }
        });



    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(5000);
        getWindow().setEnterTransition(fade);
    }


    private void addDialog() {

        dialog = new Dialog(TitleActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialod_add_title);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circleshape));

        Button bt_add = (Button) dialog.findViewById(R.id.bt_add);
        final TextView time = (TextView) dialog.findViewById(R.id.time);
        final EditText add_tile = (EditText) dialog.findViewById(R.id.add_tile);
        final EditText message = (EditText) dialog.findViewById(R.id.message);
        temp_message = message.getText().toString();






        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(TitleActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                temp_hour = String.valueOf(hourOfDay);
                                temp_minute = String.valueOf(minute);

                                Time t = new Time(hourOfDay, minute, 0);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
                                String s = simpleDateFormat.format(t);

                                time.setText(s);

                            }
                        },

                        mHour, mMinute,     false);
                timePickerDialog.show();


            }
        });



        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TitleModel obj = new TitleModel();
                obj.setTitle_name(add_tile.getText().toString());

                db.insert(obj);
                list = db.getAll();
                adapter.notifyDataSetChanged();
                adapter = new TitleAdapter(TitleActivity.this, list);
                listView.setAdapter(adapter);

                dialog.dismiss();

            }
        });

        if (dialog != null)
        {
            dialog.show();
        }




    }


    public class TitleAdapter extends BaseAdapter {

        Context context;
        ArrayList<TitleModel> list = new ArrayList<TitleModel>();
        ViewHolder v;


        public TitleAdapter(Context context, ArrayList<TitleModel> list) {
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

            v= new ViewHolder();

            if (convertView == null)
            {
                convertView= LayoutInflater.from(context).inflate(R.layout.child_title, null);
                convertView.setTag(v);
            }
            else
            {
                v= (ViewHolder) convertView.getTag();
            }

            v.ed_title = (TextView) convertView.findViewById(R.id.ed_title);
            v.delete = (Button) convertView.findViewById(R.id.delete);

            v.ed_title.setText(list.get(position).getTitle_name());

            v.ed_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    title_id = list.get(position).getId();
                    Intent intent = new Intent(TitleActivity.this, ScheduleActivity.class);
                    intent.putExtra("title_id", title_id);
                    startActivity(intent);
                }
            });

            v.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    db.delete(list.get(position).getId());
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });

            v.ed_title.setText(list.get(position).getTitle_name());


            return convertView;
        }

        class ViewHolder{

            TextView ed_title;
            Button delete;

        }
    }

}
