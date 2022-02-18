package com.example.sample_3_4;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class MyService extends Service {

    private CommandReceiver cmdReceiver;
    private boolean flag;

    public MyService() {
    }

    @Override
    public void onCreate() {
        flag = true;
        cmdReceiver = new CommandReceiver();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.MyService");
        registerReceiver(cmdReceiver,filter);
        doJob();
        return super.onStartCommand(intent,flags,startId);
    }

    public void doJob(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag){
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.setAction("com.example.MyService");
                    intent.putExtra("data",Math.random());
                    sendBroadcast(intent);
                }
            }
        }).start();
    }

    private class CommandReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int cmd = intent.getIntExtra("cmd",-1);
            if(cmd == MainActivity.CMD_STOP_SERVICE){
                flag = false;
                stopSelf();
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(cmdReceiver);
        super.onDestroy();
    }
}