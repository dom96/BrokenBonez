package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Menu.SettingsState;

public class SettingsScene extends Scene {
    SettingsState state;

    public SettingsScene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        super(assetLoader, gameSceneManager);
        this.state = new SettingsState(assetLoader, gameSceneManager);
    }

    public void draw(GameView view) {
        state.draw(view);
    }
    public void update(float lastUpdate) {
        state.update(lastUpdate);
    }
    public void updateSize(int width, int height) {
        state.updateSize(width, height);
    }
    public void onTouchEvent(MotionEvent event) {
        state.onTouchEvent(event);
    }

}