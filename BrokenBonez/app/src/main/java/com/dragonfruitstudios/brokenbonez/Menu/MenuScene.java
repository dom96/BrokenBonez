package com.dragonfruitstudios.brokenbonez.Menu;

import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.Scene;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;

public class MenuScene extends Scene {
    MenuState state;

    public MenuScene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.state = new MenuState(assetLoader, gameSceneManager);
    }

    public void draw(GameView view) {
        state.draw(view);
    }

    public void update(float lastUpdate) {
        state.update(lastUpdate);
    }

    public void updateSize(int w, int h) {
        state.updateSize(w, h);
    }

    public void onTouchEvent(MotionEvent event) {
       state.onTouchEvent(event);
    }
}