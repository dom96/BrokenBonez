package com.dragonfruitstudios.brokenbonez.AssetLoading;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

public class Music extends Sound {

    MediaPlayer m;

    public Music(SoundPool soundPool, AssetManager assetM, String filePath){
        super(soundPool);
        try {
            MediaPlayer m = new MediaPlayer();
            m.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Log.d("AssetLoader", "Loading music from " + filePath);
            AssetFileDescriptor df = assetM.openFd(filePath);
            m.setDataSource(df.getFileDescriptor(), df.getStartOffset(), df.getLength());
            m.prepare();
            this.m = m;
            //mp.prepareAsync();      //We could be loading sounds in background if need be -AM
        } catch (IOException e) {
            Log.e("AssetLoader", "Error loading sound: " + e.getMessage());
            throw new RuntimeException("Unable to load requested music - " + filePath);
        }
    }


    @Override
    public void play() {
        this.m.setLooping(false);
        this.m.start();
    }

    @Override
    public void play(boolean loop){
        this.m.setLooping(loop);
        this.m.start();
    }

    @Override
    public void play(float volume) {
        this.m.start();
        this.setVolume(volume);
    }

    @Override
    public void play(boolean loop, float volume) {
        this.m.setLooping(loop);
        this.m.start();
        this.setVolume(volume);
    }

    @Override
    public void pause(){
        this.m.pause();
    }

    @Override
    public void resume(){
        if (m.getCurrentPosition() > 1)
            this.m.start();
    }

    @Override
    public void stop(){
        this.m.stop();
    }

    @Override
    public void destroy() {
        m.release();
    }

    @Override
    public void setVolume(float volume) {
        this.m.setVolume(volume, volume);
        this.volume = volume;
    }

    @Override
    public boolean isPlaying() {
        return m.isPlaying();
    }

    public int getDuration(){
        return this.m.getDuration();
    }

    public void update(float lastUpdate){

    }
}
