package com.dragonfruitstudios.brokenbonez.AssetLoading;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

public class SoundEffect extends Sound {
    SoundPool s;
    private int id;
    private boolean loaded = false;
    private int streamID;
    private AssetManager assetM;
    private String filePath;
    private float timeRemaining = 0;
    private final Object lock = new Object();

    public SoundEffect(SoundPool soundPool, AssetManager assetM, String filePath){
        super(soundPool);
        try {
            Log.d("AssetLoader", "Loading sound from " + filePath);
            AssetFileDescriptor df = assetM.openFd(filePath);
            this.id = soundPool.load(df, 1);
            this.assetM = assetM;
            this.filePath = filePath;
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int ID, int status) {
                    loaded = true;
                }
            });

        } catch (IOException e) {
            Log.e("AssetLoader", "Error loading sound effect: " + e.getMessage());
            throw new RuntimeException("Unable to load requested sound - " + filePath);
        }

    }

    private int getDuration(){
        Music m = new Music(soundPool, assetM, this.filePath);
        int duration = m.getDuration();
        m.destroy();
        m = null;
        return duration;
    }

    private void tryPlay( final boolean loop){ //May be used later -AM
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (loaded) {
                    if (loop) {
                        streamID = soundPool.play(id, 1, 1, 1, -1, 1);
                    } else {
                        streamID = soundPool.play(id, 1, 1, 1, 0, 1);
                    }
                } else {
                    tryPlay(loop);
                }
            }
        }, 30);
    }

    private void playSound(boolean loop){
        synchronized (lock) {
            this.timeRemaining = getDuration();
        }
        if (loop) {
            streamID = soundPool.play(id, 1, 1, 1, -1, 1);
        } else {
            streamID = soundPool.play(id, 1, 1, 1, 0, 1);
        }
    }

    @Override
    public void play() {
        Log.d("Sound", "Playing sound " + id);
        playSound(false);
    }

    @Override
    public void play(boolean loop){
        Log.d("Sound", "Playing sound " + id);
        playSound(loop);
    }

    @Override
    public void play(float volume) {
        Log.d("Sound", "Playing sound " + id);
        playSound(false);
        this.setVolume(volume);
    }

    @Override
    public void play(boolean loop, float volume) {
        Log.d("Sound", "Playing sound " + id);
        playSound(loop);
        this.setVolume(volume);
    }

    @Override
    public void pause(){
        soundPool.pause(streamID);
    }

    @Override
    public void resume(){
        soundPool.resume(streamID);
    }

    @Override
    public void stop(){
        soundPool.stop(this.streamID);
    }

    @Override
    public void destroy() {
        //TODO
    }

    public void update(float lastUpdate){
        synchronized (lock) {
            if (this.timeRemaining > 0){
                this.timeRemaining =  Math.max(timeRemaining - lastUpdate,0);//= Math.max(timeRemaining - lastUpdate, 0);
            } else{
                this.timeRemaining = 0;
            }
    }}

    @Override
    public void setVolume(float volume) {
        soundPool.setVolume(streamID, volume, volume);
        this.volume = volume;
    }

    @Override
    public boolean isPlaying(){
        synchronized (lock) {
            return this.timeRemaining != 0;
        }
    }


}
