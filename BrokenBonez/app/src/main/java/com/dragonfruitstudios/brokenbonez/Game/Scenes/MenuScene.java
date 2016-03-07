package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.hardware.SensorEvent;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameActivity;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Gameplay.Bike;
import com.dragonfruitstudios.brokenbonez.Menu.MenuState;
import com.dragonfruitstudios.brokenbonez.R;
import com.plattysoft.leonids.ParticleSystem;

public class MenuScene extends Scene {
    MenuState state;

    public MenuScene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        super(assetLoader, gameSceneManager);
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

    @Override
    public void onSensorChanged(SensorEvent event) {

    }
}