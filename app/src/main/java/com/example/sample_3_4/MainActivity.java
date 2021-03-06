package com.example.sample_3_4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final int CMD_STOP_SERVICE = 0;
    public static final int UPDATE_DATE = 0;
    public static final int UPDATE_COMPLETED = 1;
    private TextView tv;
    private Button btnStart;
    private Button btnEnd;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_DATE:
                    tv.setText("正在更新来自线程的数据：" + msg.arg1 + "%");
                    break;
                case UPDATE_COMPLETED:
                    tv.setText("已完成来自线程的更新数据！");
                    break;
            }
        }
    };
    private DataReceiver mDataReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tv = findViewById(R.id.tv);
//        btnStart = findViewById(R.id.btnStart);

//        btnStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (int i = 0; i < 100; i++) {
//                            try{
//                                Thread.sleep(150);
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//                            Message m = mHandler.obtainMessage();
//                            m.what = UPDATE_DATE;
//                            m.arg1 = i + 1;
//                            mHandler.sendMessage(m);
//                        }
//                        mHandler.sendEmptyMessage(UPDATE_COMPLETED);
//                    }
//                }).start();
//            }
//        });
//        btnStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                startActivity(intent);
//            }
//        });
        btnStart = findViewById(R.id.btnStart);
        btnEnd = findViewById(R.id.btnEnd);
        tv = findViewById(R.id.tv);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,MyService.class);
                startService(myIntent);
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setAction("com.example.MyService");
                myIntent.putExtra("cmd",CMD_STOP_SERVICE);
                sendBroadcast(myIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        mDataReceiver = new DataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.MyService");
        registerReceiver(mDataReceiver,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mDataReceiver);
        super.onStop();
    }

    private class DataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            double date = intent.getDoubleExtra("data",0);
            tv.setText("Service数据为：" + date);
        }
    }
}