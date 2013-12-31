package com.example.dakashenqi;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by waj on 13-12-26.
 */
public class MyService extends Service {
    SharedPreferences sharedPreferences;
    List<MediaPlayer> mediaPlayers;
    MyThread myThread;

    public Date getYiDaKaRiQi() {
        long yiDaKaRiQi = sharedPreferences.getLong("yiDaKaRiQi",0);
        if(yiDaKaRiQi!=0){
            return new Date(yiDaKaRiQi);
        }
        else {
            return null;
        }
    }
    public void setYiDaKaRiQi(Date yiDaKaRiQi) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("yiDaKaRiQi", yiDaKaRiQi.getTime());
        editor.commit();
    }

    boolean yiDaShangBanKaMa(){
        return sharedPreferences.getBoolean("yiDaShangBanKaMa",false);
    }
    boolean yiDaXiaBanKaMa(){
        return sharedPreferences.getBoolean("yiDaXiaBanKaMa",false);
    }
    void daShangBaKa(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("yiDaShangBanKaMa",true);
        editor.commit();
    }
    void daXiaBanKa(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("yiDaXiaBanKaMa",true);
        editor.commit();
    }
    void kaiShiXinDeYiTian(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("yiDaShangBanKaMa",false);
        editor.putBoolean("yiDaXiaBanKaMa",false);
        editor.commit();
    }
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("daKaBiaoShi",MODE_PRIVATE);
        mediaPlayers = new Vector<MediaPlayer>();
        myThread = new MyThread();
        myThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if("daKa".equals(intent.getStringExtra("caoZuo"))){
            setYiDaKaRiQi(new Date());
            for(MediaPlayer mediaPlayer : mediaPlayers){
                mediaPlayer.release();
            }
            mediaPlayers.clear();
            if(!yiDaShangBanKaMa()){
                daShangBaKa();
            }
            else if(!yiDaXiaBanKaMa()){
                daXiaBanKa();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        for(MediaPlayer mediaPlayer : mediaPlayers){
            mediaPlayer.release();
        }
        myThread.isLoop = false;
        super.onDestroy();
    }

    class MyThread extends Thread{
        boolean isLoop=true;
        @Override
        public void run() {
            while (isLoop){
                Date date = new Date();
                if(getYiDaKaRiQi()!=null){
                    if(getYiDaKaRiQi().getYear()!=date.getYear()
                            || getYiDaKaRiQi().getMonth()!=date.getMonth()
                            || getYiDaKaRiQi().getDate()!=date.getDate()
                            ){
                        kaiShiXinDeYiTian();
                    }
                }
                /*下班提醒*/
                if(date.getDay()==1 || date.getDay()==4){
                    if(date.getHours()>=20&&date.getMinutes()>=30 && !yiDaXiaBanKaMa()){
                        play();
                    }
                }
                else if(date.getDay()==2||date.getDay()==3||date.getDay()==5 ){
                    if(date.getHours()>=17 && date.getMinutes()>=30 && !yiDaXiaBanKaMa()){
                        play();
                    }
                }
                /*上班提醒*/
                if(date.getDay()==2 || date.getDay()==5){
                    if(date.getHours()>=12&&date.getMinutes()>=30 && !yiDaShangBanKaMa()){
                        play();
                    }
                }
                else if(date.getDay()==1||date.getDay()==3||date.getDay()==4){
                    if(date.getHours()>=8 && date.getMinutes()>=10  && !yiDaShangBanKaMa()){
                        play();
                    }
                }

                mySleep(1000*60);
            }
        }

        private void play() {
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.youcaihua);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            mediaPlayers.add(mediaPlayer);
            mySleep(1000*60*4);
        }

        void mySleep(long duration){
            try {
                sleep(duration);
            } catch (InterruptedException e) {
            }
        }
    }
}
