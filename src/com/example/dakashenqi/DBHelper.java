package com.example.dakashenqi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by waj on 13-12-18.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "DaKaShenQi4.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table daKaJiLu (daKaShiJian INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

/*copy database*/
//    static {
//        File root = Environment.getExternalStorageDirectory();
//        File src = new File(root,"DaKaShenQi4.db");
//        File dst = new File("/data/data/com.example.dakashenqi/databases/DaKaShenQi4.db");
//        if(!src.exists()){
//            Log.e("waj","未在sdcard上找到DaKaShenQi4.db，无法完成拷贝！");
//        }
//        byte[] bytes=new byte[1024];
//        int len;
//        try {
//            FileInputStream fileInputStream = new FileInputStream(src);
//            FileOutputStream fileOutputStream = new FileOutputStream(dst);
//            while ((len=fileInputStream.read(bytes))!=0){
//                fileOutputStream.write(bytes,0,len);
//            }
//            fileOutputStream.flush();
//            fileOutputStream.close();
//            fileInputStream.close();
//            Log.i("waj","拷贝完成");
//        }
//        catch (Exception e){
//            Log.e("waj",Log.getStackTraceString(e));
//        }
//
//    }
}
