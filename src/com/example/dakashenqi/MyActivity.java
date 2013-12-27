package com.example.dakashenqi;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyActivity extends Activity {
    Button button;
    ListView listView;
    List<Long> list;

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        button = (Button)findViewById(R.id.button);
        listView = (ListView)findViewById(R.id.listView);

        bindListView();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String insert = "insert into daKaJiLu (daKaShiJian) values (?)";
                sqLiteDatabase.execSQL(insert,new Object[]{System.currentTimeMillis()});
                bindListView();
                Intent intent = new Intent(MyActivity.this,MyService.class);
                intent.putExtra("caoZuo","daKa");
                startService(intent);
            }
        });

        Intent intent = new Intent(this,MyService.class);
        startService(intent);
    }

    private void bindListView() {
        list = new ArrayList();
        String select = "select * from daKaJiLu";
        Cursor cursor = sqLiteDatabase.rawQuery(select,null);
        while (cursor.moveToNext()){
            long time = cursor.getLong(cursor.getColumnIndex("daKaShiJian"));
            list.add(time);
        }
        cursor.close();
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView==null){
                    convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1,null);
                }
                TextView textView = (TextView)convertView.findViewById(android.R.id.text1);
                Date date = new Date(list.get(position));
                String str = (1900+date.getYear())+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日";
                str = str + " " + date.getHours()+"点"+date.getMinutes()+"分";
                textView.setText(str);
                return convertView;
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
