package com.example.dakashenqi;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by waj on 14-1-20.
 */
public class Pub {
    private static Map<Integer,String> map;
    static {
        map = new HashMap<Integer, String>();
        map.put(0,"星期日");
        map.put(1,"星期一");
        map.put(2,"星期二");
        map.put(3,"星期三");
        map.put(4,"星期四");
        map.put(5,"星期五");
        map.put(6,"星期六");
    }
    public static String getDay(int day){
        return map.get(day);
    }
    public static String getDate(Date date){
        String d = (1900+date.getYear())+"年"+formateNum(date.getMonth() + 1)+"月"+formateNum(date.getDate())+"日";
        return d;
    }
    public static String getTime(Date date){
        String t = formateNum(date.getHours())+"点"+formateNum(date.getMinutes())+"分";
        return t;
    }
    public static String getTime(int hour,int minute){
        String t = formateNum(hour)+"点"+formateNum(minute)+"分";
        return t;
    }

    private static String formateNum(int num) {
        String str;
        if(num<10){
            str = "0"+num;
        }
        else {
            str=""+num;
        }
        return str;
    }

}
