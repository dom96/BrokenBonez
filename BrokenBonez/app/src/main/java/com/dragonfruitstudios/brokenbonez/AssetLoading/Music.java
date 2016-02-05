package com.dragonfruitstudios.brokenbonez.AssetLoading;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

public class Music extends Sound {

    MediaPlayer m;

    public Music(SoundPool soundPool, AssetManager assetM, String filePath){
        super(soundPool, assetM, filePath);
        try {
            MediaPlayer m = new MediaPlayer();
            Log.e("AssetLoader", "Loading music from " + filePath);
            AssetFileDescriptor df = assetM.openFd(filePath);
            m.setDataSource(df.getFileDescriptor(), df.getStartOffset(), df.getLength());
            m.prepare();
            this.m = m;
            //mp.prepareAsync();      //We could be loading sounds in background if need be -AM
        } catch (IOException e) {
            Log.e("AssetLoader", "Error loading sound: " + e.getMessage());
        }
    }


    @Override
    public void play(boolean loop){
        this.m.setLooping(loop);
        this.m.start();
    }

    @Override
    public void pause(){
        this.m.pause();
    }

    @Override
    public void resume(){
        this.m.start();
    }

    @Override
    public void stop(){
        this.m.stop();
    }

    @Override
    public void destroy() {
        //TODO
    }


}
