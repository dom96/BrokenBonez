package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Menu.LevelSelectionState;

public class LevelSelectionScene extends Scene {
    LevelSelectionState state;

    public LevelSelectionScene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        super(assetLoader, gameSceneManager);
        state = new LevelSelectionState(assetLoader, gameSceneManager);
    }

    public void draw(GameView view) {state.draw(view);}

    public void update(float lastUpdate) {state.update(lastUpdate);}

    public void updateSize(int w, int h) {state.updateSize(w, h);}

    public void onTouchEvent(MotionEvent event) {state.onTouchEvent(event);}
}