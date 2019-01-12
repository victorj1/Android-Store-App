package com.example.onlinestoreapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service {
    MediaPlayer player;
    static final String AUDIO_PATH = "https://od.lk/s/NDdfMTIzOTQxNjBf/Fantasy_Game_Background.mp3";
    private int playbackPosition = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            playbackPosition = 0;
            player = new MediaPlayer();
            player.setDataSource(AUDIO_PATH);
            player.setLooping(true);
            player.setVolume(100, 100);
            Toast.makeText(getApplicationContext(), "Wait for the player to be started!", Toast.LENGTH_LONG).show();
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return START_STICKY;
        }
    }

    public void pausePlayer() {
        if (player != null && player.isPlaying()) {
            playbackPosition = player.getCurrentPosition();
            player.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        if(player != null) {
            try {
                player.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
