package com.example.dakashenqi;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by waj on 14-1-20.
 */
public class SecondActivity extends Activity {
    ListView listView;
    List<Setting> list;
    DBHelper dbHelper;
    SQLiteDatabase db;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        listView = (ListView)findViewById(R.id.listView);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        bindListView();
    }

    private void bindListView() {
        updateList();

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
//                if(view==null){
                    view = getLayoutInflater().inflate(R.layout.second_list_item,null);
//                }
                ToggleButton toggleButton = (ToggleButton)view.findViewById(R.id.toggleButton);
                TextView textView = (TextView)view.findViewById(R.id.textView);
                Button button = (Button)view.findViewById(R.id.button);
                Button button2 = (Button)view.findViewById(R.id.button2);
                Setting setting = list.get(i);
                toggleButton.setChecked(setting.enable==1?true:false);
                textView.setText(Pub.getDay(setting.day));
                button.setText(Pub.getTime(setting.shangBanHour,setting.shangBanMinute));
                button2.setText(Pub.getTime(setting.xiaBanHour,setting.xiaBanMinute));
                toggleButton.setTag(i);
                button.setTag(i);
                button2.setTag(i);

                toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        String update = "update setting set enable=? where day=?";
                        int i = (Integer)compoundButton.getTag();
                        db.execSQL(update,new Object[]{b?1:0,list.get(i).day});
                        list.get(i).enable=b?1:0;
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    int i;
                    Button button;
                    @Override
                    public void onClick(View view) {
                        i = (Integer)view.getTag();
                        button = (Button)view;
                        Setting setting = list.get(i);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(SecondActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                        String update = "update setting set shangBanHour=?,shangBanMinute=? where day=?";
                                        db.execSQL(update,new Object[]{hour,minute,list.get(i).day});
                                        list.get(i).shangBanHour = hour;
                                        list.get(i).shangBanMinute = minute;
                                        button.setText(Pub.getTime(hour,minute));
                                    }
                                },
                                setting.shangBanHour,
                                setting.shangBanMinute,
                                true );
                        timePickerDialog.setCancelable(true);
                        timePickerDialog.show();
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    int i;
                    Button button2;
                    @Override
                    public void onClick(View view) {
                        i = (Integer)view.getTag();
                        button2 = (Button)view;
                        Setting setting = list.get(i);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(SecondActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                        String update = "update setting set xiaBanHour=?,xiaBanMinute=? where day=?";
                                        db.execSQL(update,new Object[]{hour,minute,list.get(i).day});
                                        list.get(i).xiaBanHour = hour;
                                        list.get(i).xiaBanMinute = minute;
                                        button2.setText(Pub.getTime(hour,minute));
                                    }
                                },
                                setting.xiaBanHour,
                                setting.xiaBanMinute,
                                true );
                        timePickerDialog.setCancelable(true);
                        timePickerDialog.show();
                    }
                });

                return view;
            }
        });
    }

    private void updateList() {
        list = new ArrayList<Setting>();
        String select = "select * from setting";
        Cursor cursor = db.rawQuery(select,null);
        while (cursor.moveToNext()){
            Setting setting = new Setting();
            setting.day = cursor.getInt(cursor.getColumnIndex("day"));
            setting.shangBanHour = cursor.getInt(cursor.getColumnIndex("shangBanHour"));
            setting.shangBanMinute = cursor.getInt(cursor.getColumnIndex("shangBanMinute"));
            setting.xiaBanHour = cursor.getInt(cursor.getColumnIndex("xiaBanHour"));
            setting.xiaBanMinute = cursor.getInt(cursor.getColumnIndex("xiaBanMinute"));
            setting.enable = cursor.getInt(cursor.getColumnIndex("enable"));
            list.add(setting);
        }
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}