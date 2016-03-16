package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class SettingsState implements GameObject {
    private final ImageButton soundEnabled;
    private final ImageButton soundDisabled;
    private final ImageButton accelerometerEnabled;
    private final ImageButton accelerometerDisabled;
    private final ImageButton particlesEnabled;
    private final ImageButton particlesDisabled;
    private final Bitmap sound;
    private final Bitmap accelerometer;
    private final Bitmap particles;
    private final GameSceneManager gameSceneManager;
    private final VectorF soundPos;
    private final VectorF accelerometerPos;
    private final VectorF particlesPos;
    private final Settings settings;

    public SettingsState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.gameSceneManager = gameSceneManager;
        assetLoader.AddAssets(new String[]{"menu/accelerometer.png", "menu/sound.png", "menu/particles.png"});
        soundEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4), 150, 150);
        soundDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4), 150, 150);
        accelerometerEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 2), 150, 150);
        accelerometerDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 2), 150, 150);
        particlesEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4 * 3), 150, 150);
        particlesDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4 * 3), 150, 150);
        sound = assetLoader.getBitmapByName("menu/sound.png");
        accelerometer = assetLoader.getBitmapByName("menu/accelerometer.png");
        particles = assetLoader.getBitmapByName("menu/particles.png");
        this.settings = new Settings(this.gameSceneManager);
        soundPos = new VectorF(getScreenWidth() / 4 * 2, getScreenHeight() / 4);
        accelerometerPos = new VectorF(getScreenWidth() / 4 * 2, getScreenHeight() / 2);
        particlesPos = new VectorF(getScreenWidth() / 4 * 2, getScreenHeight() / 4 * 3);
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
        view.drawImage(sound, soundPos, 0, GameView.ImageOrigin.TopLeft);
        view.drawImage(accelerometer, accelerometerPos, 0, GameView.ImageOrigin.TopLeft);
        view.drawImage(particles, particlesPos, 0, GameView.ImageOrigin.TopLeft);

        if(this.settings.isBoolSoundEnabled()) {
            soundEnabled.draw(view);
        } else {
            soundDisabled.draw(view);
        }
        if(this.settings.isBoolAccelEnabled()) {
            accelerometerEnabled.draw(view);
        } else {
            accelerometerDisabled.draw(view);
        }
        if(this.settings.isBoolParticlesEnabled()){
            particlesEnabled.draw(view);
        } else {
            particlesDisabled.draw(view);
        }
    }

    public void onTouchEvent(MotionEvent event) {
        soundEnabled.onTouchEvent(event);
        accelerometerEnabled.onTouchEvent(event);
        particlesEnabled.onTouchEvent(event);

        if(soundEnabled.onTouchEvent(event)){
            if(settings.isBoolSoundEnabled()){
                settings.setBoolSoundEnabled(false);
                settings.save();
            } else {
                settings.setBoolSoundEnabled(true);

                settings.save();
            }
        }
        if(accelerometerEnabled.onTouchEvent(event)){
            if(settings.isBoolAccelEnabled()){
                settings.setBoolAccelEnabled(false);

                settings.save();
            } else {
                settings.setBoolAccelEnabled(true);
                settings.save();
            }
        }
        if(particlesEnabled.onTouchEvent(event)){
            if(settings.isBoolParticlesEnabled()){
                settings.setBoolParticlesEnabled(false);
                settings.save();

            } else {
                settings.setBoolParticlesEnabled(true);

                settings.save();
            }
        }
    }

}