package com.dragonfruitstudios.brokenbonez.Menu;

import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.Scene;

public class MenuScene implements Scene {
    MenuState state;

    public MenuScene(AssetLoader assetLoader) {
        this.state = new MenuState(assetLoader);
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