package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Gameplay.Bike;
import com.dragonfruitstudios.brokenbonez.Menu.BikeSelectionLevel;
import com.dragonfruitstudios.brokenbonez.Menu.BikeSelectionState;

public class BikeShowcaseScene extends Scene {
    BikeSelectionState state;
    BikeSelectionLevel level;
    Bike bike;
    public BikeShowcaseScene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        super(assetLoader, gameSceneManager);
        state = new BikeSelectionState(assetLoader, gameSceneManager);
        level = new BikeSelectionLevel(state);
        bike = new Bike(level, Bike.BodyType.Bike);
    }

    public void draw(GameView view) {
        bike.draw(view);
        level.getPhysicsSimulator().draw(view);
    }

    public void update(float lastUpdate) {
        level.getPhysicsSimulator().update(lastUpdate);
    }

    public void updateSize(int w, int h) {
        bike.updateSize(w, h);
    }

    public void onTouchEvent(MotionEvent event) {
        bike.setBodyType(Bike.BodyType.Bicycle);
    }

}
