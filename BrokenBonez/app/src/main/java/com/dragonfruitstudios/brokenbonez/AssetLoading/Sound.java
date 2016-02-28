package com.dragonfruitstudios.brokenbonez.AssetLoading;

import android.content.res.AssetManager;
import android.media.SoundPool;

public abstract class Sound{
    /**
     * Abstract class for sound objects to allow seamless use of both SoundPool and MusicPlayer
     */

    SoundPool soundPool;
    protected float volume = 1f;

    public Sound(SoundPool soundPool, AssetManager assetM, String filePath){
        this.soundPool = soundPool;
    }

    public float getVolume(){
        return this.volume;
    }

    public abstract void play(boolean loop);

    public abstract void play();

    public abstract void play(float volume);

    public abstract void play(boolean loop, float volume);

    public abstract void pause();

    public abstract void resume();

    public abstract void stop();

    public abstract void destroy();

    public abstract void setVolume(float volume);

}
