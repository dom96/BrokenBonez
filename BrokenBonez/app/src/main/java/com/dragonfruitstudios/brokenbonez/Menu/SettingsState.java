package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.Context;
import android.content.res.Resources;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameActivity;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.ParticleSystem.ParticleSystem;

public class SettingsState implements GameObject {
    ImageButton soundEnabled;
    ImageButton soundDisabled;
    ImageButton accelerometerEnabled;
    ImageButton accelerometerDisabled;
    ImageButton particlesEnabled;
    ImageButton particlesDisabled;
    AssetLoader assetLoader;
    GameSceneManager gameSceneManager;
    boolean boolSoundEnabled;
    boolean boolAccelEnabled;
    boolean boolParticlesEnabled;

    public SettingsState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
        soundEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4), 150, 150);
        soundDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4), 150, 150);
        accelerometerEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 2), 150, 150);
        accelerometerDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 2), 150, 150);
        particlesEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4 * 3), 150, 150);
        particlesDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4 * 3), 150, 150);
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void update(float lastUpdate) {

    }

    public void updateSize(int width, int height) {

    }

    public void draw(GameView view) {
        if(boolSoundEnabled == true) {
            soundEnabled.draw(view);
        } else {
            soundDisabled.draw(view);
        }
        if(boolAccelEnabled == true) {
            accelerometerEnabled.draw(view);
        } else {
            accelerometerDisabled.draw(view);
        }
        if(boolParticlesEnabled == true){
            particlesEnabled.draw(view);
        } else {
            particlesDisabled.draw(view);
        }
    }

    public void onTouchEvent(MotionEvent event) {
        soundEnabled.onTouchEvent(event);
        accelerometerEnabled.onTouchEvent(event);
        particlesEnabled.onTouchEvent(event);

        if(soundEnabled.onTouchEvent(event) == true){
            if(boolSoundEnabled == true){
                boolSoundEnabled = false;
                gameSceneManager.getGameSceneByName("gameScene").assetLoader.getSoundByName("bikeEngine.mp3").setVolume(0);
                gameSceneManager.getGameSceneByName("gameScene").assetLoader.getSoundByName("brokenboneztheme.ogg").setVolume(0);
            } else {
                boolSoundEnabled = true;
                gameSceneManager.getGameSceneByName("gameScene").assetLoader.getSoundByName("bikeEngine.mp3").setVolume(0.5f);
                gameSceneManager.getGameSceneByName("gameScene").assetLoader.getSoundByName("brokenboneztheme.ogg").setVolume(1f);
            }
        }
        if(accelerometerEnabled.onTouchEvent(event) == true){
            if(boolAccelEnabled == true){
                boolAccelEnabled = false;
                // Need to be able to access this/current GameActivity for the method to work
                //GameActivity.sensorManager.unregisterListener(this);
            } else {
                boolAccelEnabled = true;
            }
        }
        if(particlesEnabled.onTouchEvent(event) == true){
            if(boolParticlesEnabled == true){
                boolParticlesEnabled = false;
                ParticleSystem.particlesEnabled = false;
            } else {
                boolParticlesEnabled = true;
                ParticleSystem.particlesEnabled = true;
            }
        }
    }
}