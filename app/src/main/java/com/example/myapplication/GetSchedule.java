package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.lang.reflect.Array;

public class GetSchedule extends AppCompatActivity{

    private Button get;
    private TextView text;
    private EditText title;
    private EditText balance;
    private RadioGroup type;
    private  CalendarAdapter ca;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_schedule);

        ca = new CalendarAdapter(this);

        Intent intent = getIntent();
        final int date = intent.getIntExtra(MainActivity.Extra_Date, 0);
        Log.d("date2", String.valueOf(date));
        get = findViewById(R.id.get);
        text = findViewById(R.id.text);
        text.setText(String.valueOf(ca.getDate(date)/100)+"月"+String.valueOf(ca.getDate(date)%100)+"日");
        title = findViewById(R.id.title);
        type = findViewById(R.id.type);
        balance = findViewById(R.id.balance);

        DateBaseHelper helper = new DateBaseHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();
        final SQLiteDatabase db1 = helper.getWritableDatabase();
        final SQLiteDatabase db2 = helper.getWritableDatabase();





        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String stitle = title.getText().toString();
                int checkedId = type.getCheckedRadioButtonId();
                int sbalance = Integer.parseInt(balance.getText().toString());
                ContentValues val = new ContentValues();
                switch (checkedId){
                    case R.id.t0:
                        insertData(db, stitle, date, sbalance);
                        break;
                    case R.id.t1:
                        insertData(db1, stitle, date, sbalance);
                        break;
                    case R.id.t2:
                        insertData(db2, stitle, date, sbalance);
                        break;
                    default:
                        break;
                }
                finish();
            }
        });
    }
    private void insertData(SQLiteDatabase db, String title, int date, int balance){

        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("date", date);
        values.put("balance", balance);

        db.insert("scheduledb", null, values);
    }
}
