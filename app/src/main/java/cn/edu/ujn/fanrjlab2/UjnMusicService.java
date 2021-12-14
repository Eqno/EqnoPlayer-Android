package cn.edu.ujn.fanrjlab2;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class UjnMusicService extends Service {
    MediaPlayer mediaPlayer;
    Timer timer;
    TimerTask timerTask;

    public UjnMusicService() {}

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        mediaPlayer = MediaPlayer.create(this, R.raw.ujn);
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                double a = mediaPlayer.getCurrentPosition();
                double b = mediaPlayer.getDuration();
                int progress = (int) (a/b*100);
                Intent intent = new Intent();
                intent.putExtra("pro", progress);
                intent.setAction("progress");
                sendBroadcast(intent);
            }
        };
        timer.schedule(timerTask, 0, 50);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        timerTask.cancel();
        Intent intent = new Intent();
        intent.putExtra("pro", 0);
        intent.setAction("progress");
        sendBroadcast(intent);
        super.onDestroy();
    }
}