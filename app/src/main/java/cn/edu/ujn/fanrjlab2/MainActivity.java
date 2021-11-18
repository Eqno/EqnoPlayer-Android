package cn.edu.ujn.fanrjlab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.logging.Level;

public class MainActivity extends AppCompatActivity {
    private TextView tv1;
    private Button b1, b2;
    NotificationManager nm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.textView);
        b1 = findViewById(R.id.start);
        b2 = findViewById(R.id.stop);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // TODO: 2021/11/11 注册Receiver
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        BatteryReceiver batteryReceiver = new BatteryReceiver();
        registerReceiver(batteryReceiver, intentFilter);

        // TODO: 2021/11/11 添加按钮事件响应
        b1.setOnClickListener(view -> {
            Intent intent = new Intent(this, UjnMusicService.class);
            startService(intent);
            Log.v("Eqnoxx", "服务启动。");

            // TODO: 2021/11/11 添加温馨通知
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
            NotificationChannel channel = new NotificationChannel("default", "Eqnoxx", NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
            builder.setContentTitle("播放")
                    .setContentText("播放济南大学校歌。")
                    .setSmallIcon(R.mipmap.ujn_launcher_round)
                    .setTicker("ticker")
                    .setContentIntent(pIntent);
            Notification build = builder.build();
            nm.notify(0, build);
            Log.v("Eqnoxx", "温馨通知启动。");
        });
        b2.setOnClickListener(view -> {
            Intent intent = new Intent(this, UjnMusicService.class);
            stopService(intent);
            Log.v("Eqnoxx", "服务停止。");
        });
    }
    // TODO: 2021/11/11 电池电量获取内部类
    private class BatteryReceiver extends BroadcastReceiver {
        int lastLevel = 100;

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            Log.v("Eqnoxx", "当前电量："+level);
            if (lastLevel>=50 && level<50) {
                Intent intent2 = new Intent(MainActivity.this, UjnMusicService.class);
                stopService(intent2);
                Log.v("Eqnoxx", "电量过低。");
            }
            else if (lastLevel<50 && level>=50) {
                Intent intent2 = new Intent(MainActivity.this, UjnMusicService.class);
                startService(intent2);
                Log.v("Eqnoxx", "电量恢复。");
            }
            lastLevel = level;
        }
    }
}