package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;

public class SettingsState implements GameObject {
    ImageButton soundEnabled;
    ImageButton soundDisabled;
    ImageButton accelerometerEnabled;
    ImageButton accelerometerDisabled;
    AssetLoader assetLoader;
    GameSceneManager gameSceneManager;
    boolean boolSoundDisabled;
    boolean boolAccelDisabled;
    boolean boolSoundEnabled;
    boolean boolAccelEnabled;
    int i = 0;
    int j = 0;

    public SettingsState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
        soundEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 2), (getScreenHeight() / 2), 60, 50);
        soundDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 2), (getScreenHeight() / 2), 60, 50);
        accelerometerEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 2), (getScreenHeight() / 4 * 3), 60, 50);
        accelerometerDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 2), (getScreenHeight() / 4 * 3), 60, 50);
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void draw(GameView view) {
        soundEnabled.draw(view);
        accelerometerEnabled.draw(view);
        drawSoundDisabled(view);
        drawAccelDisabled(view);
        drawSoundEnabled(view);
        drawAccelEnabled(view);
    }

    public void update(float lastUpdate) {

    }

    public void updateSize(int width, int height) {

    }

    public void onTouchEvent(MotionEvent event) {
        soundEnabled.onTouchEvent(event);
        accelerometerEnabled.onTouchEvent(event);
        if (soundEnabled.isTouched == true) {
            if(i == 0){
                i++;
                boolSoundEnabled = true;
                if(i > 1){
                    i = 1;
                }
            }
            if(i == 1){
                i--;
                boolSoundDisabled = true;
                if(i < 0){
                    i = 0;
                }
            }

        }
        if (accelerometerEnabled.isTouched == true) {
            if(j == 0){
                j = j + 1;
                boolAccelDisabled = true;
            }
            else if(j == 1){
                j = j - 1;
                boolAccelEnabled = true;
            }
        }
    }

    public void drawSoundDisabled(GameView view) {
        if (boolSoundDisabled == true) {
            soundDisabled.draw(view);
        }
    }

    public void drawAccelDisabled(GameView view) {
        if (boolAccelDisabled == true) {
            accelerometerDisabled.draw(view);
        }
    }

    public void drawSoundEnabled(GameView view) {
        if (i == 0) {
            soundEnabled.draw(view);
        }
        else if (i == 1){
            soundDisabled.draw(view);
        }
    }

    public void drawAccelEnabled(GameView view) {
        if (boolAccelEnabled == true) {
            accelerometerEnabled.draw(view);
        }
    }
}