package com.dragonfruitstudios.brokenbonez.AssetLoading;

import android.content.res.AssetManager;
import android.media.SoundPool;

public abstract class Sound{

    SoundPool soundPool;

    public Sound(SoundPool soundPool, AssetManager assetM, String filePath){
        this.soundPool = soundPool;
    }

    public abstract void play();

    public abstract void pause();

    public abstract void resume();

    public abstract void destroy();


}
