package com.example.myapplication;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends BaseAdapter {
    private List<Date> dateArray = new ArrayList();
    private Context mContext;
    private DateManager mDateManager;
    private LayoutInflater mLayoutInflater;

    protected String readDate(int position) {
        DateBaseHelper helper = new DateBaseHelper(mContext.getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        SQLiteDatabase db1 = helper.getReadableDatabase();
        SQLiteDatabase db2 = helper.getReadableDatabase();

        String str = null;
        Cursor cursor = db.query(
                "scheduledb",
                new String[]{ "title", "date", "balance"},
                null,
                null,
                null,
                null,
                null

        );
        cursor.moveToFirst();
        for(int i =0; i < cursor.getCount(); i++) {
            if (cursor.getInt(1) == position) {
                str = cursor.getString(0);

            }
            cursor.moveToNext();
        }
        cursor.close();

        Cursor cursor1 = db1.query(
                "scheduledb",
                new String[]{ "title", "date", "balance"},
                null,
                null,
                null,
                null,
                null

        );
        cursor1.moveToFirst();
        for(int i =0; i < cursor.getCount(); i++) {
            if (cursor1.getInt(1) == position) {
                str = cursor1.getString(0);
            }
        }
        cursor1.close();

        Cursor cursor2 = db2.query(
                "scheduledb",
                new String[]{ "title", "date", "balance"},
                null,
                null,
                null,
                null,
                null

        );
        cursor2.moveToFirst();
        for(int i =0; i < cursor.getCount(); i++) {
            if (cursor2.getInt(1) == position) {
                str = cursor2.getString(0);
            }
        }
        cursor2.close();
        return str;
    }

    //Wigetを定義
    private static class ViewHolder {
        public TextView dateText;
        public TextView memoText;
    }

    public CalendarAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDateManager = new DateManager();
        dateArray = mDateManager.getDays();
    }

    @Override
    public int getCount() {
        return dateArray.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.calendar_cell, null);
            holder = new ViewHolder();
            holder.dateText = convertView.findViewById(R.id.dateText);
            holder.memoText = convertView.findViewById(R.id.memoText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        //セルのサイズを指定
        float dp = mContext.getResources().getDisplayMetrics().density;
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(parent.getWidth()/7 - (int)dp, (parent.getHeight() - (int)dp * mDateManager.getWeeks() ) / mDateManager.getWeeks());
        convertView.setLayoutParams(params);

        //日付のみ表示させる
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.US);
        holder.dateText.setText(dateFormat.format(dateArray.get(position)));
        String str = readDate(position);
        if(str != null){
            holder.memoText.setText(str);
        }

        //当月以外のセルをグレーアウト
        if (mDateManager.isCurrentMonth(dateArray.get(position))){
            convertView.setBackgroundColor(Color.WHITE);
        }else {
            convertView.setBackgroundColor(Color.LTGRAY);
        }

        //当日の背景を黄色に
        if (mDateManager.isToday(dateArray.get(position))) {
            convertView.setBackgroundColor(Color.YELLOW);
        }

        //日曜日を赤、土曜日を青に
        int colorId;
        switch (mDateManager.getDayOfWeek(dateArray.get(position))){
            case 1:
                colorId = Color.RED;
                break;
            case 7:
                colorId = Color.BLUE;
                break;

            default:
                colorId = Color.BLACK;
                break;
        }
        holder.dateText.setTextColor(colorId);;

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return dateArray.get(position);
    }

    //表示月を取得
    public String getTitle(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);
        return format.format(mDateManager.mCalendar.getTime());
    }

    //翌月表示
    public void nextMonth(){
        mDateManager.nextMonth();
        dateArray = mDateManager.getDays();
        this.notifyDataSetChanged();
    }

    //前月表示
    public void prevMonth(){
        mDateManager.prevMonth();
        dateArray = mDateManager.getDays();
        this.notifyDataSetChanged();
    }

    public int getDate(int position){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM", Locale.US);
        int m = Integer.parseInt(dateFormat.format(dateArray.get(position)));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("d", Locale.US);
        int d = Integer.parseInt(dateFormat1.format(dateArray.get(position)));
        int date = m * 100 + d;
        return  date;
    }
}