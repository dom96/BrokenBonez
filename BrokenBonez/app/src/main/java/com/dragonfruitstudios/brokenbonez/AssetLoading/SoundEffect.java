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

    @SuppressWarnings("deprecation")
    public SoundEffect(SoundPool soundPool, AssetManager assetM, String filePath){
        super(soundPool, assetM, filePath);
        //SoundPool.Builder s = new SoundPool.Builder();
        try {
            Log.e("AssetLoader", "Loading sound from " + filePath);
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
        }

    }

    private void tryPlay(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (loaded){
                    soundPool.play(id, 1, 1, 1, 0, 1);
                } else{
                    tryPlay();
                }
            }
        }, 30);
    }
    @Override
    public void play(){
            tryPlay();
    }
    @Override
    public void pause(){
        soundPool.pause(id);
    }
    @Override
    public void resume(){
        soundPool.resume(id);
    }

    @Override
    public void destroy() {
        //TODO
    }

}
