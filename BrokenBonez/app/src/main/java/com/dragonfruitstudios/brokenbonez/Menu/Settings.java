package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.Context;
import android.content.SharedPreferences;

import com.dragonfruitstudios.brokenbonez.GameSceneManager;

public class Settings {
    private boolean boolSoundEnabled;
    private boolean boolAccelEnabled;
    private boolean boolParticlesEnabled;
    private final GameSceneManager gameSceneManager;

    public Settings(GameSceneManager gameSceneManager){
        this.gameSceneManager = gameSceneManager;
        load();
    }

    public boolean isBoolSoundEnabled() {
        load();
        return boolSoundEnabled;
    }

    public void setBoolSoundEnabled(boolean boolSoundEnabled) {
        this.boolSoundEnabled = boolSoundEnabled;
    }

    public boolean isBoolAccelEnabled() {
        load();
        return boolAccelEnabled;
    }

    public void setBoolAccelEnabled(boolean boolAccelEnabled) {
        this.boolAccelEnabled = boolAccelEnabled;
    }

    public boolean isBoolParticlesEnabled() {
        load();
        return boolParticlesEnabled;
    }

    public void setBoolParticlesEnabled(boolean boolParticlesEnabled) {
        this.boolParticlesEnabled = boolParticlesEnabled;
    }

    private void load(){
        SharedPreferences gamePrefs = this.gameSceneManager.gameView.getContext().getSharedPreferences("BrokenPrefs", Context.MODE_PRIVATE);
        this.boolSoundEnabled = gamePrefs.getBoolean("boolSoundEnabled", true);
        this.boolAccelEnabled = gamePrefs.getBoolean("boolAccelEnabled", true);
        this.boolParticlesEnabled = gamePrefs.getBoolean("boolParticlesEnabled", false);
    }

    public void save(){
        SharedPreferences gamePrefs = gameSceneManager.gameView.getContext().getSharedPreferences("BrokenPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = gamePrefs.edit();
        editor.putBoolean("boolSoundEnabled", this.boolSoundEnabled);
        editor.putBoolean("boolAccelEnabled", this.boolAccelEnabled);
        editor.putBoolean("boolParticlesEnabled", this.boolParticlesEnabled);
        editor.commit();
    }
}
