package com.dragonfruitstudios.brokenbonez.Game.Scenes;


import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Menu.SplashState;

public class SplashScene extends Scene {
    SplashState state;

    public SplashScene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        super(assetLoader, gameSceneManager);
        this.state = new SplashState(assetLoader, gameSceneManager);
    }

    public void draw(GameView view) {
        state.draw(view);
    }
    public void onTouchEvent(MotionEvent event) {}
    public void update(float lastUpdate) {
        state.update(lastUpdate);
    }
    public void updateSize(int width, int height) {
        state.updateSize(width, height);
    }
}
