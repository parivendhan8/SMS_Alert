package com.example.sms_alert;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.example.sms_alert.Database.DB_ContactAdapter;
import com.example.sms_alert.Database.DB_TitleAdapter;
import com.example.sms_alert.Model.ContactModel;
import com.example.sms_alert.Model.TitleModel;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ContactActivity extends AppCompatActivity {

    Dialog dialog;
    private TextView contact_choose;
    private TextView phone;

    DB_ContactAdapter db;
    ContactAdapter adapter;
    ArrayList<ContactModel> list = new ArrayList<ContactModel>();
    ListView listView;
    FloatingActionButton fab_add;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        listView = (ListView) findViewById(R.id.listView);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        db = new DB_ContactAdapter(ContactActivity.this);
        list = db.getAll();
        adapter = new ContactAdapter(ContactActivity.this, list);
        listView.setAdapter(adapter);


        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog();
            }
        });


    }

    private void addDialog() {

        dialog = new Dialog(ContactActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_contacts);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circleshape));

        Button bt_add = (Button) dialog.findViewById(R.id.bt_add);
        contact_choose = (TextView) dialog.findViewById(R.id.contact_choose);
        phone = (TextView) dialog.findViewById(R.id.phone);


        contact_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 100);
            }
        });



        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactModel obj = new ContactModel();
                obj.setName(contact_choose.getText().toString());
                obj.setPhone(phone.getText().toString());

                if (db.insert(obj))
                {
                    Toast.makeText(ContactActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ContactActivity.this, "Not Inserted", Toast.LENGTH_SHORT).show();
                }

                list.add(obj);
                adapter.notifyDataSetChanged();



                dialog.dismiss();

            }
        });

        if (dialog != null)
        {
            dialog.show();
        }




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

                contact_choose.setText(name);
                phone.setText(number);
            }
        }



    }


    public class ContactAdapter extends BaseAdapter {

        Context context;
        ArrayList<ContactModel> list = new ArrayList<ContactModel>();
        ViewHolder v;


        public ContactAdapter(Context context, ArrayList<ContactModel> list) {
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
                convertView= LayoutInflater.from(context).inflate(R.layout.child_contact, null);
                convertView.setTag(v);
            }
            else
            {
                v= (ViewHolder) convertView.getTag();
            }

            v.contact = (TextView) convertView.findViewById(R.id.contact);
            v.delete = (Button) convertView.findViewById(R.id.delete);


            v.contact.setText(list.get(position).getName());

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

        class ViewHolder{

            TextView contact;
            Button delete;


        }
    }

}
