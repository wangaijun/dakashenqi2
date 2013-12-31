package com.example.dakashenqi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.*;
import java.util.*;

public class MyActivity extends Activity {
    Button button;
    ListView listView;
    List<Record> list;

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
            Date date = new Date(time);
            String d = (1900+date.getYear())+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日";
            String t = date.getHours()+"点"+date.getMinutes()+"分";

            Record record=null;
            for(Record tmp : list){
                if(tmp.date.equals(d)){
                    record = tmp;
                    break;
                }
            }
            if(record!=null){
                record.time += "\n"+t;
            }
            else {
                record = new Record(d,t);
                list.add(record);
            }
        }
        cursor.close();
//        Collections.sort(list,new Comparator<Long>() {
//            @Override
//            public int compare(Long aLong, Long aLong2) {
//                return -aLong.compareTo(aLong2);
//            }
//        });
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
                    convertView = getLayoutInflater().inflate(R.layout.list_item,null);
                }
                TextView textView = (TextView)convertView.findViewById(R.id.textView);
                TextView textView2 = (TextView)convertView.findViewById(R.id.textView2);
                textView.setText(list.get(position).date);
                textView2.setText(list.get(position).time);
                return convertView;
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.beiFen){
            try{
                File root = Environment.getExternalStorageDirectory();
                File file = new File(root,"daKaShenQi.dat");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                String select = "select * from daKaJiLu";
                Set<Long> set = new HashSet<Long>();
                Cursor cursor = sqLiteDatabase.rawQuery(select,null);
                while (cursor.moveToNext()){
                    long time = cursor.getLong(cursor.getColumnIndex("daKaShiJian"));
                    set.add(time);
                }
                cursor.close();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(set);
                objectOutputStream.flush();
                objectOutputStream.close();
                fileOutputStream.close();
                alert("成功备份到SD卡");
            }
            catch (Exception e){
                alert("备份失败，写SD卡失败");
            }
        }
        else if(item.getItemId()==R.id.huanYuan){
            try{
                File root = Environment.getExternalStorageDirectory();
                File file = new File(root,"daKaShenQi.dat");
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Set<Long> set1 = (Set<Long>)objectInputStream.readObject();

                String select = "select * from daKaJiLu";
                Set<Long> set2 = new HashSet<Long>();
                Cursor cursor = sqLiteDatabase.rawQuery(select,null);
                while (cursor.moveToNext()){
                    long time = cursor.getLong(cursor.getColumnIndex("daKaShiJian"));
                    set2.add(time);
                }
                cursor.close();

                set1.addAll(set2);

                String truncate = "delete from daKaJiLu";
                sqLiteDatabase.execSQL(truncate);
                for(Long time : set1){
                    String insert = "insert into daKaJiLu (daKaShiJian) values (?)";
                    sqLiteDatabase.execSQL(insert,new Object[]{time});
                }
                alert("还原成功");
                bindListView();
            }
            catch (Exception e){
                alert("还原失败，读SD卡失败");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void alert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("确定",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
