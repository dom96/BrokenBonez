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

    private void tryPlay( final boolean loop){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (loaded){
                    if (loop){
                        soundPool.play(id, 1, 1, 1, -1, 1);
                    }
                    else{
                        soundPool.play(id, 1, 1, 1, 0, 1);
                    }
                } else{
                    tryPlay(loop);
                }
            }
        }, 30);
    }
    @Override
    /**
     *
     *
     */
    public void play(boolean loop){
        tryPlay(loop);
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
    public void stop(){
        soundPool.stop(id);
    }

    @Override
    public void destroy() {
        //TODO
    }

}
