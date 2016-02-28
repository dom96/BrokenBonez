package com.dragonfruitstudios.brokenbonez.AssetLoading;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

public class SoundEffect extends Sound {
    SoundPool s;
    int id;
    boolean loaded = false;
    int streamID;
    public SoundEffect(SoundPool soundPool, AssetManager assetM, String filePath){
        super(soundPool, assetM, filePath);
        try {
            Log.d("AssetLoader", "Loading sound from " + filePath);
            AssetFileDescriptor df = assetM.openFd(filePath);
            int id = soundPool.load(df, 1);
            this.id = id;

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
                } else{
                    tryPlay(loop);
                }
            }
        }, 30);
    }

    private void playSound(boolean loop){
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

    @Override
    public void setVolume(float volume) {
        soundPool.setVolume(streamID, volume, volume);
        this.volume = volume;
    }


}
